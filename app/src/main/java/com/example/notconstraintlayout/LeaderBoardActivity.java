//package com.example.notconstraintlayout;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.widget.SearchView;
//
//import com.example.notconstraintlayout.ui.leaderboard.PlayerAdapter;
//import com.example.notconstraintlayout.ui.leaderboard.PlayerListClass;
//
//import java.util.ArrayList;
//
//
//public class LeaderBoardActivity extends AppCompatActivity{
//
//    SearchView searchView;
//    RecyclerView recyclerView;
//    ArrayList<PlayerListClass> arrayList = new ArrayList<>();
//    ArrayList<PlayerListClass> searchList;
//
//    String[] playerList = new String[]{"Ada Loveace", "Prateek", "Anna", "Ruoyun", "Adi",
//            "Vinu","Dhairya", "Temba","kartik","Shreya","Smag","Hannah","Josh","Cindy"};
//
//    String[] playerPoint = new String[]{"12400","12200","12100","11900","11500","11455","11412",
//            "10000","9900","9869","900","0","8000","10"};
//
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_leader_board);
//        recyclerView=findViewById(R.id.recycle_view);
//        searchView=findViewById(R.id.leader_board_search);
//
//        searchView.setIconified(false);
//        searchView.clearFocus();
//
//        for (int i = 0; i < playerList.length; i++){
//            PlayerListClass playerListClass = new PlayerListClass();
//            playerListClass.setPlayerName(playerList[i]);
//            playerListClass.setPlayerPoint(playerPoint[i]);
//            arrayList.add(playerListClass);
//
//        }
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LeaderBoardActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
//
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                searchList = new ArrayList<>();
//
//                if(query.length() > 0){
//
//                    for (int i= 0; i < arrayList.size(); i++) {
//                        if(arrayList.get(i).getPlayerName().toUpperCase().contains(query.toUpperCase())){
//
//                            PlayerListClass playerListClass = new PlayerListClass();
//                            playerListClass.setPlayerName(arrayList.get(i).getPlayerName());
//                            playerListClass.setPlayerPoint(arrayList.get(i).getPlayerPoint());
//                            searchList.add(playerListClass);
//
//                        }
//                    }
//
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LeaderBoardActivity.this);
//                    recyclerView.setLayoutManager(layoutManager);
//
//
//                }
//
//                else{
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LeaderBoardActivity.this);
//                    recyclerView.setLayoutManager(layoutManager);
//
//
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                searchList = new ArrayList<>();
//
//                if(newText.length() > 0){
//
//                    for (int i= 0; i < arrayList.size(); i++) {
//                        if(arrayList.get(i).getPlayerName().toUpperCase().contains(newText.toUpperCase())){
//
//                            PlayerListClass playerListClass = new PlayerListClass();
//                            playerListClass.setPlayerName(arrayList.get(i).getPlayerName());
//                            playerListClass.setPlayerPoint(arrayList.get(i).getPlayerPoint());
//                            searchList.add(playerListClass);
//
//                        }
//                    }
//
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LeaderBoardActivity.this);
//                    recyclerView.setLayoutManager(layoutManager);
//
//
//                }
//
//                else{
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LeaderBoardActivity.this);
//                    recyclerView.setLayoutManager(layoutManager);
//
//
//                }
//                return false;
//            }
//        });
//
//    }
//
//}