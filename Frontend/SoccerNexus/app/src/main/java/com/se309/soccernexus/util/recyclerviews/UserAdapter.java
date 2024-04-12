package com.se309.soccernexus.util.recyclerviews;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.ConversationActions;
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
import com.se309.soccernexus.PrivateMessageLobbyActivity;
import com.se309.soccernexus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Nathan Turnis
 * Class for handling the recycler view logic for User
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private ArrayList<User> usersList;
    public Context context;
    long teamID;

    /**
     * Creates a new user adapter
     * @param usersList
     * @param context the context of where this adapter lives
     */
    public UserAdapter(ArrayList<User> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    /**
     * Creates a new user adapter. Stores the id of the team associated with this user.
     * @param usersList
     * @param context
     * @param teamID
     */
    public UserAdapter(ArrayList<User> usersList, Context context, long teamID) {
        this.usersList = usersList;
        this.context = context;
        this.teamID = teamID;
    }

    /**
     * Class for handling each recycler view item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTxt;
        private long otherID;
        private Button button, denyBtn;

        /**
         * Creates a new ViewHolder. When adding a friend, a request is made to add the friend. When
         * inviting a player, a request is made to invite the player.
         * @param view
         */
        public ViewHolder(final View view) {
            super(view);
            nameTxt = view.findViewById(R.id.friends_nameTxt);
            button = view.findViewById(R.id.friends_addFriendBtn);
            denyBtn = view.findViewById(R.id.friends_denyFriendBtn);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (button.getText().equals("Add Friend")) {

                        //call endpoint to add friend for both users

                        JSONObject body = new JSONObject();
                        try {
                            body.put("signed_in_player_id", playerID);
                            body.put("friend_id", otherID);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                        RequestQueue queue = Volley.newRequestQueue(context);
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_SERVER_URL + "/friends/sendRequest", body,

                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("Added friend response", response.toString());
                                        button.setText("Request Sent");
                                    }
                                },

                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error.networkResponse.statusCode == 400) {
                                            button.setText("Already sent");
                                        }
                                    }
                                }

                        );
                        queue.add(request);

                        button.setEnabled(false);
                    } else if(button.getText().equals("Accept")) {
                        //accept friend request

                        JSONObject body = new JSONObject();
                        try {
                            body.put("request_id", otherID);
                            body.put("signed_in_player_id", playerID);
                        } catch (JSONException e) {
                            throw  new RuntimeException(e);
                        }

                        RequestQueue queue = Volley.newRequestQueue(context);
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_SERVER_URL + "/friends/acceptRequest", body,

                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("accepted friend", response.toString());
                                        usersList.remove(getAdapterPosition());
                                        notifyDataSetChanged();
                                    }
                                },

                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }

                        );
                        queue.add(request);
                    } else {
                        //invite
                        JSONObject body = new JSONObject();
                        try {
                            body.put("player_id", otherID);
                            body.put("team_id", teamID);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                        RequestQueue queue = Volley.newRequestQueue(context);
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_SERVER_URL + "/teams/invitePlayer", body,

                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("invited response", response.toString());
                                        button.setEnabled(false);
                                        button.setText("Invited");
                                    }
                                },

                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }

                        );
                        queue.add(request);
                    }
                }
            });

            denyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue queue = Volley.newRequestQueue(context);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASE_SERVER_URL + "/friends/denyRequest/" + otherID, null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("deny request", response.toString());
                                    usersList.remove(getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

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
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_items, parent, false);
        return new UserAdapter.ViewHolder(itemView);
    }

    /**
     * Binds each item to the recycler view. Sets button text to add friend or invite depending
     * on the User's addFriendOrInvite.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String firstName = usersList.get(position).getFirstName();
        String lastName = usersList.get(position).getLastName();
        holder.nameTxt.setText(firstName + " " + lastName);
        holder.otherID = usersList.get(position).getID();

        if(usersList.get(position).getAddFriendOrInvite() == 0) {
            holder.button.setText("Add Friend");
        } else {
            holder.button.setText("Invite");
        }

        if(usersList.get(position).canDeny == 1) {
            holder.denyBtn.setVisibility(View.VISIBLE);
            holder.button.setText("Accept");
        } else {
            holder.denyBtn.setVisibility(View.GONE);
        }
    }

    /**
     * Gets the userList size
     * @return userList size
     */
    @Override
    public int getItemCount() {
        return usersList.size();
    }

}
