package com.example.notconstraintlayout.ui.explore;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notconstraintlayout.QrClass;
import com.example.notconstraintlayout.R;
import com.example.notconstraintlayout.databinding.FragmentExploreBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public abstract class ExploreFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private FragmentExploreBinding binding;
    private GoogleMap mMap;
    private View mapView;
    private View listView;
    private RecyclerView recyclerView;

    private List<QrClass> qrArray = new ArrayList<>();

    private LocationManager locationManager;
    private String locationProvider;
    private Location userLocation;

    public abstract void OnAttach(@NonNull Context context);

    @NonNull
    public abstract Dialog onCreateDialog(@Nullable Bundle savedInstanceState);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExploreViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ExploreViewModel.class);

        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        StickySwitch stickySwitch = root.findViewById(R.id.sticky_switch);
        mapView = inflater.inflate(R.layout.explore_map, container, false);
        listView = root.findViewById(R.id.explore_list);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        qrArray.add(new QrClass("cool FroMoMegaSpectralCrab", new LatLng(53.517793, -113.513926), 100));
        qrArray.add(new QrClass("cool FroMoMegaSpectralCrab", new LatLng(53.520796, -113.505105), 200));
        qrArray.add(new QrClass("cool FroLoUltraSpectralCrab", new LatLng(53.525067, -113.526767), 300));
        qrArray.add(new QrClass("hot GloLoUltraSpectralShark", new LatLng(53.521092, -113.530964), 400));



        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                Log.d(TAG, "Now Selected : " + direction.name() + ", Current Text : " + text);
                if (direction == StickySwitch.Direction.LEFT) {
                    // Show list view and hide map view
                    listView.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.GONE);

                    // Remove map view from view hierarchy
                    ((ViewGroup) mapView.getParent()).removeView(mapView);
                } else {
                    // Show map view and hide list view
                    mapView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);

                    // Add map view to view hierarchy
                    ((ViewGroup) getView()).addView(mapView);
                }
            }
        });

        // Get the location manager and set the location provider
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.NETWORK_PROVIDER;

        // Request for location updates
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            // Initialize location manager
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            // Set location provider
            Criteria criteria = new Criteria();
            locationProvider = locationManager.getBestProvider(criteria, false);

            // Request location updates
            if (locationProvider != null) {
                locationManager.requestLocationUpdates(locationProvider, 0L, 0f, this);
                userLocation = locationManager.getLastKnownLocation(locationProvider);
            }
        }
        recyclerView = root.findViewById(R.id.explore_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        QrListAdapter adapter = new QrListAdapter(qrArray, userLocation);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Enable location button
            mMap.setMyLocationEnabled(true);

            // Get last known location and move camera there
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                        }
                    });

            // Loop through qrList and add markers
            for (QrClass qr : qrArray) {
                LatLng qrLatLng = qr.getLocation();
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(qrLatLng)
                        .title(qr.getName())
                        .snippet("Points: " + qr.getPoints());
                mMap.addMarker(markerOptions);
            }

        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle status changes here
    }

    public void onProviderEnabled(String provider) {
        // Handle provider enabled here
    }

    public void onProviderDisabled(String provider) {
        // Handle provider disabled here
    }
}