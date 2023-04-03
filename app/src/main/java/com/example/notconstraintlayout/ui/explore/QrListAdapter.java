package com.example.notconstraintlayout.ui.explore;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notconstraintlayout.QrClass;
import com.example.notconstraintlayout.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QrListAdapter extends RecyclerView.Adapter<QrListAdapter.ViewHolder> {

    private List<QrClass> qrList;
    private static Location userLocation;

    public QrListAdapter(List<QrClass> qrList, Location userLocation) {
        this.qrList = qrList;
        this.userLocation = userLocation;
        // Sort the list by distance if user location is available
        if (userLocation != null) {
            sortListByDistance();
        }
    }

    // Comparator to sort qrList by distance
    private static final Comparator<QrClass> DISTANCE_COMPARATOR = new Comparator<QrClass>() {
        @Override
        public int compare(QrClass qr1, QrClass qr2) {
            if (qr1.getLocation() == null && qr2.getLocation() == null) {
                // Sort by name if both QR codes don't have location
                return qr1.getName().compareTo(qr2.getName());
            } else if (qr1.getLocation() == null) {
                // Sort QR code without location to the end
                return 1;
            } else if (qr2.getLocation() == null) {
                // Sort QR code without location to the end
                return -1;
            } else {
                // Sort by distance
                float[] results1 = new float[1];
                Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), qr1.getLocation().getLatitude(), qr1.getLocation().getLongitude(), results1);
                float distance1 = results1[0];
                float[] results2 = new float[1];
                Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), qr2.getLocation().getLatitude(), qr2.getLocation().getLongitude(), results2);
                float distance2 = results2[0];
                return Float.compare(distance1, distance2);
            }
        }
    };

    private void sortListByDistance() {
        Collections.sort(qrList, DISTANCE_COMPARATOR);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_qr, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QrClass qr = qrList.get(position);

        // Set the name and points
        holder.nameTextView.setText(qr.getName());
        holder.pointsTextView.setText(String.valueOf(qr.getPoints()));

        if (qr.getLocation() == null) {
            holder.distanceTextView.setVisibility(View.GONE);
            holder.awayTextView.setVisibility(View.GONE);
        } else {
            // Compute the distance between the user's location and the QR location
            float[] results = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), qr.getLocation().getLatitude(), qr.getLocation().getLongitude(), results);
            float distance = results[0];

            // Set the distance
            holder.distanceTextView.setText(String.format("%.2f km", distance / 1000));
        }
    }

    @Override
    public int getItemCount() {
        return qrList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView pointsTextView;
        public TextView distanceTextView;

        public TextView awayTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.qr_name);
            pointsTextView = itemView.findViewById(R.id.qr_points);
            distanceTextView = itemView.findViewById(R.id.qr_distance);
            awayTextView = itemView.findViewById(R.id.away);
        }
    }
}
