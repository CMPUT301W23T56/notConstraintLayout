package com.example.notconstraintlayout.ui.explore;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notconstraintlayout.QrClass;
import com.example.notconstraintlayout.R;

import java.util.List;

public class QrListAdapter extends RecyclerView.Adapter<QrListAdapter.ViewHolder> {

    private List<QrClass> qrList;
    private Location userLocation;

    public QrListAdapter(List<QrClass> qrList, Location userLocation) {
        this.qrList = qrList;
        this.userLocation = userLocation;
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

        // Compute the distance between the user's location and the QR location
        float[] results = new float[1];
        if (userLocation != null && qr.getLocation() != null) {
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), qr.getLocation().latitude, qr.getLocation().longitude, results);
            float distance = results[0];

            // Set the distance
            holder.distanceTextView.setText(String.format("%.2f km", distance / 1000));
        } else {
            holder.distanceTextView.setText("");
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

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.qr_name);
            pointsTextView = itemView.findViewById(R.id.qr_points);
            distanceTextView = itemView.findViewById(R.id.qr_distance);
        }
    }
}
