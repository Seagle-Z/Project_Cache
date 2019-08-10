package com.ordinary.android.projectcache;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class CoreConditionInspector {

    Context context;
    Events events;

    public CoreConditionInspector(Context context, Events events) {
        this.context = context;
        this.events = events;
    }

    public List<Integer> getTriggerableEventsID() {
        List<Integer> triggerableEventID = new ArrayList<>();

        for (Event e : events.getEventsList()) {
            if (conditionsMatch(e)) {
                triggerableEventID.add(e.eventID);
            }
        }

        return triggerableEventID;
    }

    public boolean conditionsMatch(Event event) {
        int matchs = 0;
        for (int i = 0; i < event.triggerMethods.length; i++) {
            if (inspectCondition(event.triggerMethods[i], event.triggerValues[i])) {
                matchs++;
            }
        }

        if (matchs == event.triggerMethods.length) {
            return true;
        }
        return false;
    }

    private boolean inspectCondition(String method, String value) {
        switch (method) {
            case "TIME":
                return inspectTIME(value);

            case "APP_ON_SCREEN":
                return inspectAPP_ON_SCREEN(value);
        }

        return false;
    }

    private boolean inspectTIME(String value) {
        return false;
    }

    private boolean inspectAPP_ON_SCREEN(String value) {
        return false;
    }
}























