package com.se309.soccernexus.util.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.se309.soccernexus.R;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> postList;

    public PostAdapter(ArrayList<Post> postList) {
        this.postList = postList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText, contentText, postTime;

        public ViewHolder(final View view) {
            super(view);
            nameText = view.findViewById(R.id.posts_teamName);
            contentText = view.findViewById(R.id.posts_content);
            postTime = view.findViewById(R.id.posts_time);
        }
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_items, parent, false);
        return new PostAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        holder.nameText.setText(postList.get(position).teamName);
        holder.contentText.setText(postList.get(position).postContent);

        LocalDateTime date = postList.get(position).postTime;
        Duration timeSincePost = Duration.between(date, LocalDateTime.now());

        if(timeSincePost.toMinutes() < 60) {
            holder.postTime.setText(timeSincePost.toMinutes() + " min ago");
        } else if(timeSincePost.toHours() < 24) {
            holder.postTime.setText(timeSincePost.toHours() + " hours ago");
        } else if(timeSincePost.toDays() < 31) {
            holder.postTime.setText(timeSincePost.toDays() + " days ago");
        } else {
            holder.postTime.setText("Over a month ago");
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
