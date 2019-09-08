package com.ordinary.android.projectcache;

public class ManageEventModel {

    private Event event;

    public ManageEventModel(Event event) {
        this.event = event;
    }

    public int getEventID() {
        return event.eventID;
    }

    public String getEventName() {
        return event.eventName;
    }

    public String getEventDescription() {
        return event.eventDescription;
    }

    public boolean eventIsActivated() {
        return event.isActivated;
    }

    public String getEventImage() {
        return event.eventImage;
    }
}
