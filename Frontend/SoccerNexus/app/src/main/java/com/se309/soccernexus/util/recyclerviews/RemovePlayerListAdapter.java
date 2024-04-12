package com.se309.soccernexus.util.recyclerviews;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Tristan Sayasit
 * Class for handling the recycler view logic for the player list
 */
public class RemovePlayerListAdapter extends RecyclerView.Adapter<RemovePlayerListAdapter.ViewHolder> {

    private ArrayList<removePlayer> removePlayerList;

    /**
     * Creates a new player list adapter.
     * @param removePlayerListAdapter
     */
    public RemovePlayerListAdapter(ArrayList<removePlayer> playerList) {
        this.removePlayerList = playerList;
    }

    /**
     * Class for handling each recycler view item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView playerNameText;
        private TextView playerPositionText;
        private ImageView delete;
        public long player_id;
        public long team_id;

        /**
         * Creates a new ViewHolder
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameText = itemView.findViewById(R.id.playerName);
            playerPositionText = itemView.findViewById(R.id.playerPosition);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue queue = Volley.newRequestQueue(delete.getContext());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASE_SERVER_URL + "/teams/removePlayerFromTeam/" + team_id + "/" + player_id, null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("remove click","remove was clicked success");
                                    removePlayerList.remove(getAdapterPosition());
                                    notifyDataSetChanged();

                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("remove click","remove was clicked error");
                                }
                            }

                    );
                    queue.add(request);
                }
            });
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
    public RemovePlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.remove_player_list_items, parent, false);
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
    public void onBindViewHolder(@NonNull RemovePlayerListAdapter.ViewHolder holder, int position) {
        String playerName = removePlayerList.get(position).getName();
        String playerPosition = removePlayerList.get(position).getPosition();
        holder.playerNameText.setText(playerName);
        holder.playerPositionText.setText(playerPosition);
        holder.player_id = removePlayerList.get(position).player_id;
        holder.team_id = removePlayerList.get(position).team_id;
    }

    /**
     * Gets the size of the player list
     * @return player list size
     */
    @Override
    public int getItemCount() {
        return removePlayerList.size();
    }
}
