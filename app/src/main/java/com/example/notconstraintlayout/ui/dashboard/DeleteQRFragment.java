package com.example.notconstraintlayout.ui.dashboard;

import static com.example.notconstraintlayout.R.layout.fragment_delet_q_r;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.notconstraintlayout.QrClass;
import com.example.notconstraintlayout.R;

public class DeleteQRFragment extends DialogFragment {

    QrClass qrClass;
    QrClass delete_qr;


    public DeleteQRFragment(QrClass qrClass) {
        delete_qr = qrClass;
    }

    

    interface DeleteQRDialogListener {
        void deleteQr(QrClass qrClass);



    }

    // this method wil actually perform the function of delete QR code from the list
    private DeleteQRDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DeleteQRDialogListener) {
            listener = (DeleteQRDialogListener) context;
        }
    }

    @NonNull
    @Override
    // This method is for pop up window that allow the user to confirm whether to delete the QR from the list
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view =
                LayoutInflater.from(getContext()).inflate(fragment_delet_q_r, null);

        TextView deleteQrName = view.findViewById(R.id.qr_name);
        TextView deleteQrPoints = view.findViewById(R.id.qr_points);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (this.qrClass  != null) {
            deleteQrName.setText(qrClass.getName());
            deleteQrPoints.setText((qrClass.getPoints()));
        }


        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete",(dialog, which) -> {
                    String name = deleteQrName.getText().toString();
                    String points = deleteQrPoints.getText().toString();
                    listener.deleteQr(delete_qr);


                })
                .create();
    }
}


