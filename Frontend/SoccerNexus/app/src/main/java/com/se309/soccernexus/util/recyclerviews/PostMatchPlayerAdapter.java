package com.se309.soccernexus.util.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.se309.soccernexus.R;

import java.util.ArrayList;

public class PostMatchPlayerAdapter extends RecyclerView.Adapter<PostMatchPlayerAdapter.PostMatchViewHolder> {

    private ArrayList<PostMatchPlayer> postMatchPlayerList;

    public PostMatchPlayerAdapter(ArrayList<PostMatchPlayer> postList) {
        this.postMatchPlayerList = postList;
    }

    public class PostMatchViewHolder extends RecyclerView.ViewHolder {
        private TextView playerName2;
        public TextInputLayout goals_scored, assists_made;

        public PostMatchViewHolder(final View view) {
            super(view);
            playerName2 = itemView.findViewById(R.id.playerName2);
            goals_scored = itemView.findViewById(R.id.goals_scored);
            assists_made = itemView.findViewById(R.id.assists_made);
        }
    }

    @NonNull
    @Override
    public PostMatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_match_stats_form_items, parent, false);
        return new PostMatchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostMatchViewHolder holder, int position) {
        holder.playerName2.setText(postMatchPlayerList.get(position).playerName);
    }

    @Override
    public int getItemCount() {
        return postMatchPlayerList.size();
    }
}