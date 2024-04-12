package com.se309.soccernexus.util.recyclerviews;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.se309.soccernexus.MatchHistoryActivity;
import com.se309.soccernexus.PickupMatchViewActivity;
import com.se309.soccernexus.R;
import com.se309.soccernexus.ViewTeamActivity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private ArrayList<Match> matchList;

    public MatchAdapter(ArrayList<Match> matchList) {
        this.matchList = matchList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText, locationText, dateText;

        private View pastMatch;
        private long matchID;
        public long teamID;

        public ViewHolder(final View view) {
            super(view);
            nameText = view.findViewById(R.id.matches_teamName);
            locationText = view.findViewById(R.id.matches_location);
            dateText = view.findViewById(R.id.matches_dateText);
            pastMatch = view.findViewById(R.id.matches_bg);

            pastMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PickupMatchViewActivity.class);
                    intent.putExtra("matchid", matchID);
                    intent.putExtra("teamid", teamID);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.matches_items, parent, false);
        return new MatchAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = matchList.get(position).teamName;
        String location = matchList.get(position).location;
        LocalDateTime date = matchList.get(position).date;

        holder.nameText.setText(name);
        holder.locationText.setText(location);
        holder.matchID = matchList.get(position).id;
        holder.teamID = matchList.get(position).teamID;

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
