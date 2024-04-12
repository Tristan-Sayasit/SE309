package com.se309.soccernexus.util.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.se309.soccernexus.EditTeamActivity;
import com.se309.soccernexus.R;

import java.util.ArrayList;

/**
 * @author Tristan Sayasit
 * Class for handling the recycler view logic for the player list
 */
public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder> {

    private ArrayList<Player> playerList;

    /**
     * Creates a new player list adapter.
     * @param playerList
     */
    public PlayerListAdapter(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    /**
     * Class for handling each recycler view item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView playerNameText;
        private TextView playerPositionText;
        private ImageView captainLogo;

        /**
         * Creates a new ViewHolder
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameText = itemView.findViewById(R.id.playerName);
            playerPositionText = itemView.findViewById(R.id.playerPosition);
            captainLogo = itemView.findViewById(R.id.player_captainLogo);
        }
    }

    /**
     * When the ViewHolder is created.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return new ViewHolder
     */
    @NonNull
    @Override
    public PlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_items, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Binds each item to the ViewHolder. Sets the captain logo's visibility depending on whether
     * or not the player is a captain.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull PlayerListAdapter.ViewHolder holder, int position) {
        String playerName = playerList.get(position).getName();
        String playerPosition = playerList.get(position).getPosition();
        holder.playerNameText.setText(playerName);
        holder.playerPositionText.setText(playerPosition);

        if(playerList.get(position).isCaptain()) {
            holder.captainLogo.setVisibility(View.VISIBLE);
        } else {
            holder.captainLogo.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Gets the size of the player list
     * @return player list size
     */
    @Override
    public int getItemCount() {
        return playerList.size();
    }
}
