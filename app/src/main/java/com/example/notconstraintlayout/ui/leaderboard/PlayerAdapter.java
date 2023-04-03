package com.example.notconstraintlayout.ui.leaderboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.notconstraintlayout.R;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyHolder> {

    LeaderBoardFragment context;
    ArrayList<PlayerListClass> arrayList;
    LayoutInflater layoutInflater;

    public PlayerAdapter(LeaderBoardFragment context, ArrayList<PlayerListClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context.getActivity());
    }

    @NonNull
    @Override
    public PlayerAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.player_file,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter.MyHolder holder, int position) {

        holder.playerName.setText(arrayList.get(position).getPlayerName());
        holder.playerPoint.setText(arrayList.get(position).getPlayerPoint());
        holder.playerRank.setText(Integer.toString(arrayList.get(position).getPlayerRank()));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView playerName,playerPoint,playerRank;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.player_name);
            playerPoint = itemView.findViewById(R.id.player_points);
            playerRank = itemView.findViewById(R.id.rank);

        }
    }
}