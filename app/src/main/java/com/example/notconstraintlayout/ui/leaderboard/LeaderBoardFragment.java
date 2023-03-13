package com.example.notconstraintlayout.ui.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import static com.example.notconstraintlayout.R.layout.fragment_leaderboard;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.SearchView;

import com.example.notconstraintlayout.ui.leaderboard.PlayerAdapter;
import com.example.notconstraintlayout.ui.leaderboard.PlayerListClass;
import com.example.notconstraintlayout.R;

import java.util.ArrayList;

import com.example.notconstraintlayout.databinding.FragmentLeaderboardBinding;

public class LeaderBoardFragment extends Fragment {

    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<PlayerListClass> arrayList = new ArrayList<>();
    ArrayList<PlayerListClass> searchList;

    String[] playerList = new String[]{"Ada Loveace", "Prateek", "Anna", "Ruoyun", "Adi",
            "Vinu","Dhairya", "Temba","kartik","Shreya","Smag","Hannah","Josh","Cindy"};

    String[] playerPoint = new String[]{"12400","12200","12100","11900","11500","11455","11412",
            "10000","9900","9869","900","0","8000","10"};

    private FragmentLeaderboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        com.example.notconstraintlayout.ui.leaderboard.LeaderboardViewModel leaderboardViewModel =
                new ViewModelProvider(this).get(com.example.notconstraintlayout.ui.leaderboard.LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLeaderboard;
        leaderboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        super.onCreate(savedInstanceState);


        View view = inflater.inflate(fragment_leaderboard,container,false);
        searchView = view.findViewById(R.id.leader_board_search);
        recyclerView = view.findViewById(R.id.recycle_view);
        searchView.setIconified(false);
        searchView.clearFocus();

        for (int i = 0; i < playerList.length; i++){
            PlayerListClass playerListClass = new PlayerListClass();
            playerListClass.setPlayerName(playerList[i]);
            playerListClass.setPlayerPoint(playerPoint[i]);
            arrayList.add(playerListClass);

        }
        //RecyclerView recyclerView = (RecyclerView)

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LeaderboardFragment.this);


//        recyclerView.setLayoutManager(layoutManager);

        PlayerAdapter playerAdapter = new PlayerAdapter(LeaderBoardFragment.this, arrayList);
        recyclerView.setAdapter(playerAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchList = new ArrayList<>();

                if(query.length() > 0){

                    for (int i= 0; i < arrayList.size(); i++) {
                        if(arrayList.get(i).getPlayerName().toUpperCase().contains(query.toUpperCase())){

                            PlayerListClass playerListClass = new PlayerListClass();
                            playerListClass.setPlayerName(arrayList.get(i).getPlayerName());
                            playerListClass.setPlayerPoint(arrayList.get(i).getPlayerPoint());
                            searchList.add(playerListClass);

                        }
                    }

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);

                    PlayerAdapter playerAdapter = new PlayerAdapter(LeaderBoardFragment.this, searchList);
                    recyclerView.setAdapter(playerAdapter);
                }

                else{
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);

                    PlayerAdapter playerAdapter = new PlayerAdapter(LeaderBoardFragment.this, arrayList);
                    recyclerView.setAdapter(playerAdapter);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchList = new ArrayList<>();

                if(newText.length() > 0){

                    for (int i= 0; i < arrayList.size(); i++) {
                        if(arrayList.get(i).getPlayerName().toUpperCase().contains(newText.toUpperCase())){

                            PlayerListClass playerListClass = new PlayerListClass();
                            playerListClass.setPlayerName(arrayList.get(i).getPlayerName());
                            playerListClass.setPlayerPoint(arrayList.get(i).getPlayerPoint());
                            searchList.add(playerListClass);

                        }
                    }

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);

                    PlayerAdapter playerAdapter = new PlayerAdapter(LeaderBoardFragment.this, searchList);
                    recyclerView.setAdapter(playerAdapter);
                }

                else{
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);

                    PlayerAdapter playerAdapter = new PlayerAdapter(LeaderBoardFragment.this, arrayList);
                    recyclerView.setAdapter(playerAdapter);
                }
                return false;
            }
        });

        return view;
    }











}

