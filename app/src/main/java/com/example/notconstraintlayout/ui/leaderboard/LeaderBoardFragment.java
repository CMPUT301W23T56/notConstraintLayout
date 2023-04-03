package com.example.notconstraintlayout.ui.leaderboard;

import static android.content.ContentValues.TAG;
import static com.example.notconstraintlayout.R.layout.fragment_leaderboard;

import android.os.Bundle;
import android.util.Log;
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

/**
 * The main class for the LeaderBoardFragment, which is responsible for rendering the leaderboard
 * and managing search functionality.
 */
public class LeaderBoardFragment extends Fragment {

    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<PlayerListClass> arrayList = new ArrayList<>();
    ArrayList<PlayerListClass> searchList;

    private FragmentLeaderboardBinding binding;
    TextView userRankTextView;
    TextView userNameTextView;
    TextView userPointsTextView;
    String currentUsername;
    int current_points;
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

        userRankTextView = root.findViewById(R.id.user_rank);
        userNameTextView = root.findViewById(R.id.user_name);
        userPointsTextView = root.findViewById(R.id.user_points);

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
    /**
     * Filters the leaderboard data based on the provided search query.
     * This method searches the player list for player names that contain the provided query string
     *If the query is empty, the method displays the full player list. If the query
     * is non-empty, the method displays only the players whose names match the query.
     * @param query The search query string used to filter player names. If empty, the entire player list is displayed.
     */
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
    /**
     * Loads and displays the leaderboard data.
     * This method retrieves the user profiles from the user database manager and sorts them
     * in descending order based on their total scores. Then, it creates a
     * object for each user profile, sets the player name, points, and rank, and adds them to the
     * leaderboard array list.
     */
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

                userDBManager userDbManager = new userDBManager(requireContext());
                userDbManager.getUserProfile(new userDBManager.OnUserProfileLoadedListener() {
                    @Override
                    public void onUserProfileLoaded(UserProfile userProfile) {
                        if (userProfile != null) {
                            String currentUsername = userProfile.getUsername();
                            current_points = userProfile.getTotalScore();
                            // Use the currentUsername to display the user's name in the UI
                        } else {
                            Log.d(TAG, "Failed to load user profile.");
                        }
                    }
                });

                int rank = 1;
                for (UserProfile userProfile : userProfiles) {
                    PlayerListClass playerListClass = new PlayerListClass();
                    playerListClass.setPlayerName(userProfile.getUsername());
                    playerListClass.setPlayerPoint(String.valueOf(userProfile.getTotalScore()));
                    playerListClass.setPlayerRank(rank++);
                    arrayList.add(playerListClass);
                    if (userProfile.getUsername().equals(currentUsername)) {
                        Log.d(TAG, "User found");
                        userRankTextView.setText(String.valueOf(rank));
                        userNameTextView.setText(currentUsername);
                        userPointsTextView.setText(current_points);
                    }
                }
                PlayerAdapter playerAdapter = new PlayerAdapter(LeaderBoardFragment.this, arrayList);
                recyclerView.setAdapter(playerAdapter);
            }
        });
    }
}

