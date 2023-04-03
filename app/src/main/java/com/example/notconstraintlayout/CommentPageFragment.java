package com.example.notconstraintlayout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.notconstraintlayout.ui.leaderboard.LeaderBoardFragment;
import com.example.notconstraintlayout.ui.leaderboard.PlayerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CommentPageFragment extends Fragment {

    ListView commentList;
    EditText newComment;
    LinearLayout commentField;
    ArrayAdapter<String> commentAdapter;
    ArrayList<String> dataList;


    public CommentPageFragment() {
        // Required empty public constructor
    }


    public static CommentPageFragment newInstance(String param1, String param2) {
        CommentPageFragment fragment = new CommentPageFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comment_page, container, false);

        commentField = view.findViewById(R.id.field_commentEntry);
        newComment = view.findViewById(R.id.Add_comment);
        commentList = view.findViewById(R.id.Comment_list);

        dataList = new ArrayList<>();

        // Set up the ArrayAdapter
        commentAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        commentList.setAdapter(commentAdapter);

        final Button confirmButton = view.findViewById(R.id.button_send);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String commentText = newComment.getText().toString();
                if (!commentText.isEmpty()) {
                    commentAdapter.add(commentText);
                    newComment.getText().clear();
                    commentField.setVisibility(View.INVISIBLE);
                }
            }
        });

        return view;
    }
}