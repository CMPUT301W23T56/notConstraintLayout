package com.example.notconstraintlayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class ScanResultFragment extends DialogFragment {

    private static final String ARG_SCORE = "score";
    private static final String ARG_NAME = "name";

    public static ScanResultFragment newInstance(int score, String name) {
        ScanResultFragment fragment = new ScanResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE, score);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView scoreText;
    private TextView nameText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_result, container, false);
        scoreText = view.findViewById(R.id.score_text);
        nameText = view.findViewById(R.id.name_text);
        return view;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int score = getArguments().getInt(ARG_SCORE);
        String name = getArguments().getString(ARG_NAME);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_scan_result, null);

        TextView scoreTextView = view.findViewById(R.id.score_text);
        scoreTextView.setText(getString(R.string.score_format, score));

        TextView nameTextView = view.findViewById(R.id.name_text);
        nameTextView.setText(getString(R.string.name_format, name));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.scan_result_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}

