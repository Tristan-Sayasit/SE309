package com.se309.soccernexus.util.recyclerviews;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;
import static com.se309.soccernexus.util.Const.playerID;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.IndividualMessageActivity;
import com.se309.soccernexus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Nathan Turnis
 * Class for handling the recycler view logic for the UserChatLobby
 */
public class UserChatLobbyAdapter extends RecyclerView.Adapter<UserChatLobbyAdapter.ViewHolder> {

    private ArrayList<UserChatLobby> usersChatList;
    public Context context;

    /**
     * Creates a new UserChatLobby adapter
     * @param usersChatList
     * @param context the context of where this adapter lives
     */
    public UserChatLobbyAdapter(ArrayList<UserChatLobby> usersChatList, Context context) {
        this.usersChatList = usersChatList;
        this.context = context;
    }

    /**
     * Class for handling each recycler view item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText, latestMessageText;
        private ImageButton enterButton, removeButton;
        private View userView;
        private String username;
        private long otherID;

        /**
         * Creates a new ViewHolder. If the user clicks the remove friend button, a request is
         * made to remove that friend.
         * @param view
         */
        public ViewHolder(final View view) {
            super(view);

            nameText = view.findViewById(R.id.chat_lobby_nameTxt);
            latestMessageText = view.findViewById(R.id.chat_lobby_latestMsg);
            enterButton = view.findViewById(R.id.chat_lobby_enterBtn);
            removeButton = view.findViewById(R.id.chat_lobby_removeBtn);
            userView = view.findViewById(R.id.chat_lobby_bgView);

            enterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to chat screen
                    Intent intent = new Intent(view.getContext(), IndividualMessageActivity.class);
                    intent.putExtra("name", nameText.getText().toString());
                    intent.putExtra("username", username);
                    view.getContext().startActivity(intent);
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RequestQueue queue = Volley.newRequestQueue(context);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASE_SERVER_URL + "/friends/removeFriend/" + playerID + "/" + otherID, null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("Removed friend response", response.toString());
                                    usersChatList.removeIf(x -> x.getId() == otherID);
                                    notifyDataSetChanged();
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("remove friend error code", String.valueOf(error.networkResponse.statusCode));
                                    Log.i("error removed friend", error.toString());
                                }
                            }

                    );

                    queue.add(request);

                }
            });

            userView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to chat screen
                    Intent intent = new Intent(view.getContext(), IndividualMessageActivity.class);
                    intent.putExtra("name", nameText.getText().toString());
                    intent.putExtra("username", username);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    /**
     * When the ViewHolder is created
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return new ViewHolder
     */
    @NonNull
    @Override
    public UserChatLobbyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_lobby_items, parent, false);
        return new UserChatLobbyAdapter.ViewHolder(itemView);
    }

    /**
     * Binds each item to the ViewHolder.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull UserChatLobbyAdapter.ViewHolder holder, int position) {
        String name = usersChatList.get(position).getName();
        String msg = usersChatList.get(position).getLatestMessage();
        holder.nameText.setText(name);
        holder.latestMessageText.setText(msg);
        holder.otherID = usersChatList.get(position).getId();
        holder.username = usersChatList.get(position).getUsername();

        //depending on whether or not the remove button has been pressed, update accordingly
        if(!usersChatList.get(position).isCanRemove()) {
            holder.enterButton.setVisibility(View.VISIBLE);
            holder.removeButton.setVisibility(View.GONE);
            holder.userView.setClickable(true);
        } else {
            holder.enterButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.VISIBLE);
            holder.userView.setClickable(false);
        }

    }


    /**
     * Gets the users chat list size
     * @return usersChatList size
     */
    @Override
    public int getItemCount() {
        return usersChatList.size();
    }
}
