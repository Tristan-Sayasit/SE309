package com.se309.soccernexus.util.recyclerviews;

import android.widget.ImageButton;

import com.se309.soccernexus.util.NotificationType;

/**
 * @author Nathan Turnis
 * Class for storing a notification object
 */
public class Notification {

    private String name, description;
    private ImageButton clearBtn;
    public long id, specialID;
    public NotificationType notificationType;

    /**
     * Creates a new notification.
     * @param name Name of the notification
     * @param description Notification description
     * @param id Notification ID
     */
    public Notification(String name, String description, long id, NotificationType notificationType, long specialID) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.notificationType = notificationType;
        this.specialID = specialID;
    }

    /**
     * Gets the notification name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the notification name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the notification description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the notification description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the clearButton
     * @return image button
     */
    public ImageButton getClearBtn() {
        return clearBtn;
    }

    /**
     * Sets the clear image button
     * @param clearBtn
     */
    public void setClearBtn(ImageButton clearBtn) {
        this.clearBtn = clearBtn;
    }
}


