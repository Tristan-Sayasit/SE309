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
 * @author Nathan Turnis
 * Class for handling the recycler view logic for the chat.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    private ArrayList<Chat> chatList;

    /**
     * Creates a new chat adapter.
     * @param chatList the list to create.
     */
    public ChatAdapter(ArrayList<Chat> chatList) {
        this.chatList = chatList;
    }

    /**
     * Class for handling each recycler view item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout leftChatLayout, rightChatLayout;
        TextView leftChatText, rightChatText, readTxt;

        /**
         * Creates a new ViewHolder
         * @param view
         */
        public ViewHolder(final View view) {
            super(view);

            leftChatLayout = view.findViewById(R.id.left_message_layout);
            rightChatLayout = view.findViewById(R.id.right_message_layout);
            leftChatText = view.findViewById(R.id.left_chat_text);
            rightChatText = view.findViewById(R.id.right_chat_text);
            readTxt = view.findViewById(R.id.message_readTxt);
        }

        /**
         * Displays the right message (blue - sender)
         * @param msg
         */
        public void displayRight(String msg) {
            leftChatLayout.setVisibility(View.GONE);
            rightChatLayout.setVisibility(View.VISIBLE);
            rightChatText.setText(msg);
        }

        /**
         * Displays the left message (gray - receiver)
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
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_items, parent, false);
        return new ChatAdapter.ViewHolder(itemView);
    }

    /**
     * Binds each item to the ViewHolder. Decides whether or not to make chat receiver or sender.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        String msg = chatList.get(position).getMessage();
        if(chatList.get(position).getSenderOrReceiver() == 0) {
            holder.displayLeft(msg);
        } else {
            holder.displayRight(msg);
        }

        if(chatList.get(position).isHasRead()) {
            holder.readTxt.setVisibility(View.VISIBLE);
        } else {
            holder.readTxt.setVisibility(View.GONE);
        }

    }

    /**
     * Chat list size.
     * @return Chat list size
     */
    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
