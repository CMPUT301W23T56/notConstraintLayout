package com.example.notconstraintlayout.ui.leaderboard;

import static com.example.notconstraintlayout.R.layout.fragment_leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notconstraintlayout.R;
import com.example.notconstraintlayout.UserProfile;
import com.example.notconstraintlayout.databinding.FragmentLeaderboardBinding;
import com.example.notconstraintlayout.userDBManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderBoardFragment extends Fragment {

    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<PlayerListClass> arrayList = new ArrayList<>();
    ArrayList<PlayerListClass> searchList;

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
        searchList = new ArrayList<>();
        searchView.setIconified(false);
        searchView.clearFocus();

        loadLeaderboardData();
        //RecyclerView recyclerView = (RecyclerView)

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        PlayerAdapter playerAdapter = new PlayerAdapter(LeaderBoardFragment.this, arrayList);
        recyclerView.setAdapter(playerAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterLeaderboardData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterLeaderboardData(newText);
                return false;
            }
        });


        return view;
    }

    private void filterLeaderboardData(String query) {
        if (query.isEmpty()) {
            PlayerAdapter playerAdapter = new PlayerAdapter(LeaderBoardFragment.this, arrayList);
            recyclerView.setAdapter(playerAdapter);
        } else {
            searchList.clear();
            for (PlayerListClass player : arrayList) {
                if (player.getPlayerName().toUpperCase().contains(query.toUpperCase())) {
                    searchList.add(player);
                }
            }
            PlayerAdapter playerAdapter = new PlayerAdapter(LeaderBoardFragment.this, searchList);
            recyclerView.setAdapter(playerAdapter);
        }
    }

    private void loadLeaderboardData() {
        userDBManager userDbManager = new userDBManager(requireContext());
        userDbManager.getUsers(new userDBManager.OnUsersLoadedListener() {
            @Override
            public void onUsersLoaded(List<UserProfile> userProfiles) {
                arrayList.clear();
                Collections.sort(userProfiles, new Comparator<UserProfile>() {
                    @Override
                    public int compare(UserProfile u1, UserProfile u2) {
                        return u2.getTotalScore() - u1.getTotalScore();
                    }
                });
                int rank = 1;
                for (UserProfile userProfile : userProfiles) {
                    PlayerListClass playerListClass = new PlayerListClass();
                    playerListClass.setPlayerName(userProfile.getUsername());
                    playerListClass.setPlayerPoint(String.valueOf(userProfile.getTotalScore()));
                    playerListClass.setPlayerRank(rank++);
                    arrayList.add(playerListClass);
                }
                PlayerAdapter playerAdapter = new PlayerAdapter(LeaderBoardFragment.this, arrayList);
                recyclerView.setAdapter(playerAdapter);
            }
        });
    }
}

