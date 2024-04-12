package com.se309.soccernexus.util.recyclerviews;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.HomeActivity;
import com.se309.soccernexus.PickupMatchChallengeActivity;
import com.se309.soccernexus.R;
import com.se309.soccernexus.ViewTeamActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * @author Tristan Sayasit
 * Class for handling the recycler view logic for the team
 */
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
    private ArrayList<Team> teamList;
    private Context context;
    PickupMatchChallengeActivity pickupMatchChallengeActivity;

    /**
     * Creates a new team adapter.
     * @param teamList
     * @param context
     */
    public TeamAdapter(ArrayList<Team> teamList, Context context) {
        this.teamList = teamList;
        this.context = context;
    }

    /**
     * Creates a new team adapter for the challenge system.
     * @param teamList
     * @param context
     * @param pickupMatchChallengeActivity
     */
    public TeamAdapter(ArrayList<Team> teamList, Context context, PickupMatchChallengeActivity pickupMatchChallengeActivity) {
        this.teamList = teamList;
        this.context = context;
        this.pickupMatchChallengeActivity = pickupMatchChallengeActivity;
    }

    /**
     * Class for handling each recycler view item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView teamLocation;
        private TextView playerCount;
        private Button button;
        private long id;

        /**
         * Creates a new ViewHolder. When the corresponding button is pressed, the action done
         * will be based on what the value of btnToShow is. Makes a request to join a team if the
         * value is 1.
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.teams_items_teamName);
            button = itemView.findViewById(R.id.teams_items_enterBtn);
            teamLocation = itemView.findViewById(R.id.teams_items_teamLocation);
            playerCount = itemView.findViewById(R.id.teams_items_playerCount);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (button.getText().equals("Enter")) {
                        Intent intent = new Intent(context, ViewTeamActivity.class);
                        intent.putExtra("teamid", id);
                        context.startActivity(intent);
                    } else if (button.getText().equals("Join")) {
                        JSONObject body = new JSONObject();
                        try {
                            body.put("team_id", id);
                            body.put("player_id", playerID);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        RequestQueue queue = Volley.newRequestQueue(context);
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_SERVER_URL + "/teams/joinTeam", body,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("join response", response.toString());
                                        Intent intent = new Intent(context, ViewTeamActivity.class);
                                        intent.putExtra("teamid", id);
                                        view.getContext().startActivity(intent);
                                    }
                                },

                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i("error response", error.toString());
                                    }
                                }
                        );
                        queue.add(request);
                    } else if(button.getText().equals("Challenge")) {
                        pickupMatchChallengeActivity.openModal(nameText.getText().toString(), id);
                    }
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
     * @return
     */
    @NonNull
    @Override
    public TeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.teams_items, parent, false);
        return new TeamAdapter.ViewHolder(itemView);
    }

    /**
     * Binds each item to the ViewHolder. Gets the btnToShow value and sets the button text based
     * on that value. (0 for enter, 1 for join, 2 for challenge)
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.ViewHolder holder, int position) {
        String name = teamList.get(position).getName();
        String location = teamList.get(position).getLocation();
        int playerCount = teamList.get(position).getPlayerCount();
        holder.teamLocation.setText(location);
        holder.playerCount.setText(playerCount + "/12");
        holder.nameText.setText(name);
        holder.id = teamList.get(position).getId();

        if(teamList.get(position).getBtnToShow() == 0) {
            holder.button.setText("Enter");
        } else if(teamList.get(position).getBtnToShow() == 1) {
            holder.button.setText("Join");
        } else if(teamList.get(position).getBtnToShow() == 2) {
            holder.button.setText("Challenge");
        }

    }

    /**
     * Gets the size of the teams list
     * @return teams list size
     */
    @Override
    public int getItemCount() {
        return teamList.size();
    }
}
