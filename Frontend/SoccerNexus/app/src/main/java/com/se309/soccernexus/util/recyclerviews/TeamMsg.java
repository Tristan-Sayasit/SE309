package com.se309.soccernexus.util.recyclerviews;

/**
 * Class for storing each team message
 */
public class TeamMsg {
    public String message;
    public int senderOrReceiver;
    public String userName;

    /**
     * Creates a new message
     * @param message message text
     * @param senderOrReceiver whether or not the message is a sender or receiver
     * @param userName the username who sent this message
     */
    public TeamMsg(String message, int senderOrReceiver, String userName) {
        this.message = message;
        this.senderOrReceiver = senderOrReceiver;
        this.userName = userName;
    }
}
