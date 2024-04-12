package com.se309.soccernexus.util.recyclerviews;

/**
 * @author Nathan Turnis
 * Class for storing a chat object
 */
public class Chat {

    private String message;
    private int senderOrReceiver; //1 for send, 0 for receive
    private boolean hasRead; //will always be false if receiver

    /**
     * Creates a new chat object.
     * @param message The message associated with this chat.
     * @param senderOrReceiver Whether or not this chat is sent/received. 0 is received, 1 if sent.
     * @param hasRead Whether or not the message has been read. Will always be true if a sender.
     */
    public Chat(String message, int senderOrReceiver, boolean hasRead) {
        this.message = message;
        this.senderOrReceiver = senderOrReceiver;
        this.hasRead = hasRead;
    }

    /**
     * Gets the message.
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets sender or receiver.
     * @return sender or receiver
     */
    public int getSenderOrReceiver() {
        return senderOrReceiver;
    }

    /**
     * Sets sender or receiver
     * @param senderOrReceiver
     */
    public void setSenderOrReceiver(int senderOrReceiver) {
        this.senderOrReceiver = senderOrReceiver;
    }

    /**
     * Gets hasRead
     * @return hasRead
     */
    public boolean isHasRead() {
        return hasRead;
    }

    /**
     * Sets hasRead
     * @param hasRead
     */
    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }
}
