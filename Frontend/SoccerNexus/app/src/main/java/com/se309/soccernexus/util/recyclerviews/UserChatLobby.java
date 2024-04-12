package com.se309.soccernexus.util.recyclerviews;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * @author Nathan Turnis
 * Class for storing the UserChatLobby
 */
public class UserChatLobby {

    private long id;

    private String name, username;

    private String latestMessage;

    private ImageView profilePic;

    private ImageButton enterChatBtn, removeBtn;

    private View view;

    //used to determine if this user can be removed from friends list
    private boolean canRemove = false;

    /**
     * Create a new user.
     * @param name name of the user
     * @param latestMessage user's latest message
     * @param id id of the user
     * @param username the user's username
     */
    public UserChatLobby(String name, String latestMessage, long id, String username) {
        this.name = name;
        this.latestMessage = latestMessage;
        this.id = id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public ImageView getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(ImageView profilePic) {
        this.profilePic = profilePic;
    }

    public void setProfilePicBitmap(Bitmap bitmap) {
        this.profilePic.setImageBitmap(bitmap);
    }

    public ImageButton getEnterChatBtn() {
        return enterChatBtn;
    }

    public ImageButton getRemoveBtn() {
        return removeBtn;
    }

    public void setRemoveBtn(ImageButton removeBtn) {
        this.removeBtn = removeBtn;
    }

    public void setEnterChatBtn(ImageButton enterChatBtn) {
        this.enterChatBtn = enterChatBtn;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCanRemove() {
        return canRemove;
    }

    public void setCanRemove(boolean test) {
        this.canRemove = test;
    }
}
