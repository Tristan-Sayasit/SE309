package com.se309.soccernexus.util.recyclerviews;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.se309.soccernexus.FriendRequestsActivity;
import com.se309.soccernexus.HomeActivity;
import com.se309.soccernexus.JoinTeamActivity;
import com.se309.soccernexus.PickupMatchRequestsActivity;
import com.se309.soccernexus.PrivateMessageLobbyActivity;
import com.se309.soccernexus.R;
import com.se309.soccernexus.util.NotificationType;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Nathan Turnis
 * Class for handling the recycler view for the Notifications
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private ArrayList<Notification> notificationsList;
    Context context;

    /**
     * Creates a new notifications adapter.
     * @param notificationsList
     * @param context The context of where the notification lives
     */
    public NotificationsAdapter(ArrayList<Notification> notificationsList, Context context) {
        this.notificationsList = notificationsList;
        this.context = context;
    }

    /**
     * Class for handling each recycler view item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText, descriptionText;
        private ImageButton clearBtn, enterBtn;
        private long id, specialID;
        private View bg;
        private NotificationType notificationType;

        /**
         * Creates a new ViewHolder. If the user clicks the clear button, a request is made
         * to delete the notification from the user's notification list.
         * @param view
         */
        public ViewHolder(final View view) {
            super(view);
            nameText = view.findViewById(R.id.notification_title);
            descriptionText = view.findViewById(R.id.notification_description);
            clearBtn = view.findViewById(R.id.notificationClearBtn);
            enterBtn = view.findViewById(R.id.notificationChevron);
            bg = view.findViewById(R.id.notification_bg);

            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("button clicked", nameText.getText().toString());

                    RequestQueue queue = Volley.newRequestQueue(context);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASE_SERVER_URL + "/notifications/readNotification/" + id, null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    notificationsList.remove(getAdapterPosition());
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

            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RequestQueue queue = Volley.newRequestQueue(context);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASE_SERVER_URL + "/notifications/readNotification/" + id, null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    notificationsList.remove(getAdapterPosition());
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

                    if(notificationType == NotificationType.FRIEND_REQUEST) {
                        Intent intent = new Intent(context, FriendRequestsActivity.class);
                        view.getContext().startActivity(intent);
                    } else if(notificationType == NotificationType.MESSAGE) {
                        Intent intent = new Intent(context, PrivateMessageLobbyActivity.class);
                        view.getContext().startActivity(intent);
                    } else if(notificationType == NotificationType.INVITE) {
                        //WILL NEED TO CHANGE ONCE A PROPER ACCEPT INVITE PAGE IS CREATED
                        Intent intent = new Intent(context, JoinTeamActivity.class);
                        view.getContext().startActivity(intent);
                    }


                }
            });

            enterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RequestQueue queue = Volley.newRequestQueue(context);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASE_SERVER_URL + "/notifications/readNotification/" + id, null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    notificationsList.remove(getAdapterPosition());
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

                    if(notificationType == NotificationType.FRIEND_REQUEST) {
                        Intent intent = new Intent(context, FriendRequestsActivity.class);
                        view.getContext().startActivity(intent);
                    } else if(notificationType == NotificationType.MESSAGE) {
                        Intent intent = new Intent(context, PrivateMessageLobbyActivity.class);
                        view.getContext().startActivity(intent);
                    } else if(notificationType == NotificationType.INVITE) {
                        //WILL NEED TO CHANGE ONCE A PROPER ACCEPT INVITE PAGE IS CREATED
                        Intent intent = new Intent(context, JoinTeamActivity.class);
                        view.getContext().startActivity(intent);
                    } else if(notificationType == NotificationType.CHALLENGE) {
                        Intent intent = new Intent(context, PickupMatchRequestsActivity.class);

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
     * @return new ViewHolder
     */
    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_items, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Binds each item to the ViewHolder.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {
        String name = notificationsList.get(position).getName();
        String description = notificationsList.get(position).getDescription();
        holder.nameText.setText(name);
        holder.descriptionText.setText(description);
        holder.id = notificationsList.get(position).id;
        NotificationType notificationType = notificationsList.get(position).notificationType;
        holder.notificationType = notificationType;
        holder.specialID = notificationsList.get(position).specialID;

        if(notificationType == NotificationType.FRIEND_REQUEST || notificationType == NotificationType.INVITE) {
            holder.clearBtn.setVisibility(View.GONE);
            holder.enterBtn.setVisibility(View.VISIBLE);
        } else if(notificationType == NotificationType.MESSAGE ) {
            holder.clearBtn.setVisibility(View.VISIBLE);
            holder.enterBtn.setVisibility(View.GONE);
        }

    }

    /**
     * Gets the size of the notifications list.
     * @return notifications list size
     */
    @Override
    public int getItemCount() {
        return notificationsList.size();
    }



}
