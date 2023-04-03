package com.example.notconstraintlayout.ui.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.notconstraintlayout.QrClass;
import com.example.notconstraintlayout.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListPopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListPopFragment extends DialogFragment {

    QrClass qrClass;
    DeleteListPopDialogListener listener;

    public ListPopFragment() {
        // Required empty public constructor
    }

    interface DeleteListPopDialogListener{
        void deleteQr (QrClass qrClass);
        void commentQr (QrClass qrClass);
    }

    // this method will actually perform the delete qr code function
    //private DeleteListPopDialogListener listener;
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof DeleteListPopDialogListener){
            listener = (DeleteListPopDialogListener) context;}
        else{
            throw new RuntimeException(context + "must implement DeleteListPopDialogListener");
        }
    }


    @NonNull
    @Override
    // this method is for the pop up window that allow the user to delete the QR code
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        ImageButton deleteButton;
//        ImageButton commentButton;

        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_pop, null);
        TextView Qr_name = view.findViewById(R.id.qr_name);
        TextView Qr_points = view.findViewById(R.id.qr_points);
        //  AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ImageButton deleteButton = view.findViewById(R.id.delete_button);
        ImageButton commentButton = view.findViewById(R.id.comment_button);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteQr(qrClass);
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle("Delete QR Code");

        return builder.create();
    }

    public static ListPopFragment newInstance(String param1, String param2) {
        ListPopFragment fragment = new ListPopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_pop, container, false);
    }

    public void setQrClass(QrClass qrClass) {
        this.qrClass = qrClass;
    }
}