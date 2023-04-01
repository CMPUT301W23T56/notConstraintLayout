package com.example.notconstraintlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class QrCodeAdapter extends ArrayAdapter<QrClass> {

    private Context mContext;
    private ArrayList<QrClass> mQrCodes;
    private int mResource;

    public QrCodeAdapter(Context context, ArrayList<QrClass> qrCodes) {
        super(context, 0, qrCodes);
        mContext = context;
        mQrCodes = qrCodes;
        mResource = R.layout.dashboard_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        int points = getItem(position).getPoints();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView qrName = (TextView) convertView.findViewById(R.id.dbqr_name);
        TextView qrPoints = (TextView) convertView.findViewById(R.id.dbqr_points);

        qrName.setText(name);
        qrPoints.setText(String.valueOf(points));

        return convertView;
    }
}
