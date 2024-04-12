package com.se309.soccernexus.util.recyclerviews;

import static com.se309.soccernexus.util.Const.BASE_SERVER_URL;

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
import com.se309.soccernexus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MatchRequestsAdapter extends RecyclerView.Adapter<MatchRequestsAdapter.ViewHolder>{

    private ArrayList<Match> matchList;

    public MatchRequestsAdapter(ArrayList<Match> matchList) {
        this.matchList = matchList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText, locationText, dateText;
        private Button acceptBtn, denyBtn;
        long matchID;

        public ViewHolder(final View view) {
            super(view);

            nameText = view.findViewById(R.id.pickupItem_teamNameTxt);
            locationText = view.findViewById(R.id.pickupItem_location);
            dateText = view.findViewById(R.id.pickupItem_dateText);
            acceptBtn = view.findViewById(R.id.pickupItem_acceptBtn);
            denyBtn = view.findViewById(R.id.pickupItem_denyBtn);

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONObject body = new JSONObject();
                    try {
                        body.put("pickup_match_id", matchID);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    RequestQueue queue = Volley.newRequestQueue(view.getContext());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_SERVER_URL + "/matches/acceptChallenge", body,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    matchList.remove(getAdapterPosition());
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

            denyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue queue = Volley.newRequestQueue(view.getContext());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASE_SERVER_URL + "/matches/denyChallenge/" + matchID, null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    matchList.remove(getAdapterPosition());
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

    @NonNull
    @Override
    public MatchRequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_request_items, parent, false);
        return new MatchRequestsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchRequestsAdapter.ViewHolder holder, int position) {

        String name = matchList.get(position).teamName;
        String location = matchList.get(position).location;
        LocalDateTime date = matchList.get(position).date;

        holder.nameText.setText(name);
        holder.locationText.setText(location);
        holder.matchID = matchList.get(position).id;

        String shortenedDay = "Sun";

        switch (date.getDayOfWeek().toString().toLowerCase()) {
            case "sunday":
                shortenedDay = "Sun";
                break;
            case "monday":
                shortenedDay = "Mon";
                break;
            case "tuesday":
                shortenedDay = "Tue";
                break;
            case "wednesday":
                shortenedDay = "Wed";
                break;
            case "thursday":
                shortenedDay = "Thu";
                break;
            case "friday":
                shortenedDay = "Fri";
                break;
            case "saturday":
                shortenedDay = "Sat";
                break;
        }

        int regularHour;
        String period;
        int militaryHour = date.getHour();

        if (militaryHour == 0) {
            regularHour = 12;
            period = "AM";
        } else if (militaryHour == 12) {
            regularHour = 12;
            period = "PM";
        } else if (militaryHour < 12) {
            regularHour = militaryHour;
            period = "AM";
        } else {
            regularHour = militaryHour - 12;
            period = "PM";
        }

        String min = "00";
        if(date.getMinute() < 10) {
            min = "0" + date.getMinute();
        }  else {
            min = String.valueOf(date.getMinute());
        }


        String dateText = shortenedDay + ", " + date.getMonthValue() + "/" + date.getDayOfMonth() + " " +
                regularHour + ":" + min + " " + period;

        holder.dateText.setText(dateText);

    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }
}
