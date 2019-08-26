package com.ordinary.android.projectcache;

public class EventManagementModel {

    private String eventName;
    private String eventDescription;
    private boolean eventActivated;
    private Integer eventImage;

    public EventManagementModel(
            String eventName, String eventDescription, boolean eventActivated, Integer eventImage) {

        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventActivated = eventActivated;

        if (eventImage == null) {

        } else {
            this.eventImage = eventImage;
        }

    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public boolean getEventActivated() {
        return eventActivated;
    }

    public int getEventImage() {
        return eventImage;
    }
}
