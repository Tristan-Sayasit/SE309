package com.se309.soccernexus.util.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.se309.soccernexus.R;

import java.util.ArrayList;

/**
 * Class for handling the recycler for logic for the Team messages
 */
public class TeamMsgAdapter  extends RecyclerView.Adapter<TeamMsgAdapter.ViewHolder>{
    private ArrayList<TeamMsg> teamMsgList;

    /**
     * Creates a new TeamMsgAdapter
     * @param msgList
     */
    public TeamMsgAdapter(ArrayList<TeamMsg> msgList) {
        this.teamMsgList = msgList;
    }

    /**
     * Class for handling each recycler view item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout leftChatLayout, rightChatLayout;
        TextView leftChatText, rightChatText;
        TextView usernameTxt;

        /**
         * Creates a new ViewHolder.
         * @param view
         */
        public ViewHolder(final View view) {
            super(view);

            leftChatLayout = view.findViewById(R.id.team_left_message_layout);
            rightChatLayout = view.findViewById(R.id.team_right_message_layout);
            leftChatText = view.findViewById(R.id.team_left_chat_text);
            rightChatText = view.findViewById(R.id.team_right_chat_text);
            usernameTxt = view.findViewById(R.id.team_message_readTxt);
        }

        /**
         * Displays the right chat (blue - sender)
         * @param msg
         */
        public void displayRight(String msg) {
            leftChatLayout.setVisibility(View.GONE);
            rightChatLayout.setVisibility(View.VISIBLE);
            rightChatText.setText(msg);
        }

        /**
         * Displays the left chat (gray - receiver)
         * @param msg
         */
        public void displayLeft(String msg) {
            rightChatLayout.setVisibility(View.GONE);
            leftChatLayout.setVisibility(View.VISIBLE);
            leftChatText.setText(msg);
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
    public TeamMsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_message_items, parent, false);
        return new TeamMsgAdapter.ViewHolder(itemView);
    }

    /**
     * Binds each item to the ViewHolder. Sets the sender/receiver value and username.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TeamMsgAdapter.ViewHolder holder, int position) {
        String msg = teamMsgList.get(position).message;
        if(teamMsgList.get(position).senderOrReceiver == 0) {
            holder.displayLeft(msg);
            holder.usernameTxt.setText(teamMsgList.get(position).userName);
            holder.usernameTxt.setVisibility(View.VISIBLE);
        } else {
            holder.displayRight(msg);
        }

    }

    /**
     * Gets the size of the message list
     * @return teamMsgList size
     */
    @Override
    public int getItemCount() {
        return teamMsgList.size();
    }
}
