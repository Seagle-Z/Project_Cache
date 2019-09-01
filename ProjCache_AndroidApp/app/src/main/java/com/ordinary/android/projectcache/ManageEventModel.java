package com.ordinary.android.projectcache;

public class ManageEventModel {

    private boolean isSelected;
    private Event event;

    public ManageEventModel(Event event) {
        isSelected = false;
        this.event = event;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
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
