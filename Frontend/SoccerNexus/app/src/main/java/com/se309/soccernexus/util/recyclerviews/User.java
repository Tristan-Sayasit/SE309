package com.se309.soccernexus.util.recyclerviews;

import android.widget.ImageView;

/**
 * @author Nathan Turnis
 * Class for storing a User object
 */
public class User {

    private String firstName, lastName, username;
    private long ID;
    private ImageView profilePicture;

    private int addFriendOrInvite; //0 for addfriend, 1 for invite
    public int canDeny;

    /**
     * Creates a new User
     * @param firstName first name of the user
     * @param lastName last name of the user
     * @param username user's usernmae
     * @param id user's id
     * @param addFriendOrInvite whether or not to show add friend or invite (0 for add friend, 1 for
     *                          invite)
     */
    public User(String firstName, String lastName, String username, long id, int addFriendOrInvite, int canDeny) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.ID = id;
        this.addFriendOrInvite = addFriendOrInvite;
        this.canDeny = canDeny;
    }

    /**
     * Gets the first name of the user
     * @return user first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user
     * @return user last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's username
     * @return user username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the profile picture associated with the user
     * @return profile picture ImageView
     */
    public ImageView getProfilePicture() {
        return profilePicture;
    }

    /**
     * Sets the user's profile picture
     * @param profilePicture
     */
    public void setProfilePicture(ImageView profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Gets the user's id
     * @return user id
     */
    public long getID() {
        return ID;
    }

    /**
     * Sets the user's id
     * @param ID
     */
    public void setID(long ID) {
        this.ID = ID;
    }

    /**
     * Gets whether or not to show add friend or invite
     * @return addfriend or invite
     */
    public int getAddFriendOrInvite() {
        return addFriendOrInvite;
    }

    /**
     * Sets whether or not to show add friend or invite
     * @param addFriendOrInvite
     */
    public void setAddFriendOrInvite(int addFriendOrInvite) {
        this.addFriendOrInvite = addFriendOrInvite;
    }
}
