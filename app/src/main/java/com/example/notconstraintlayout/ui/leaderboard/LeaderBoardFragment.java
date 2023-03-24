package com.example.notconstraintlayout.ui.leaderboard;

import static com.example.notconstraintlayout.R.layout.fragment_leaderboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notconstraintlayout.R;
import com.example.notconstraintlayout.UserProfile;
import com.example.notconstraintlayout.databinding.FragmentLeaderboardBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LeaderBoardFragment extends Fragment {

    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<PlayerListClass> arrayList = new ArrayList<>();
    ArrayList<PlayerListClass> searchList;

    ArrayList<UserProfile> playerList;
    ArrayAdapter<UserProfile> playerAdapter;

    final String TAG = "Sample";



//    String[] playerList = new String[]{"Ada Loveace", "Prateek", "Anna", "Ruoyun", "Adi",
//            "Vinu","Dhairya", "Temba","kartik","Shreya","Smag","Hannah","Josh","Cindy"};
//
//    String[] playerPoint = new String[]{"12400","12200","12100","11900","11500","11455","11412",
//            "10000","9900","9869","900","0","8000","10"};

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Profiles");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                playerList.clear();
                for(QueryDocumentSnapshot doc: value)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("username")));
                    String name = doc.getId();
                    int score = (int) doc.getData().get("Total Score");
                    playerList.add(new UserProfile(name, score));
                }
                playerAdapter.notifyDataSetChanged();
            }
        });

        View view = inflater.inflate(fragment_leaderboard,container,false);
        searchView = view.findViewById(R.id.leader_board_search);
        recyclerView = view.findViewById(R.id.recycle_view);
        searchView.setIconified(false);
        searchView.clearFocus();

        for (int i = 0; i < playerList.size(); i++){
            PlayerListClass playerListClass = new PlayerListClass();
            playerListClass.setPlayerName(playerList.get(i).getUsername());
            playerListClass.setPlayerPoint(Integer.toString(playerList.get(i).getTotalScore()));
            arrayList.add(playerListClass);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

