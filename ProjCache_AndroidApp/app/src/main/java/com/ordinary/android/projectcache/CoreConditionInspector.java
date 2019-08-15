package com.ordinary.android.projectcache;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
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

            case "APP_ON_SCREEN":
                return inspectAPP_ON_SCREEN(value);

            case "TIME":
                return inspectTIME(value);


        }

        return false;
    }

    private boolean inspectTIME(String value) {
        boolean[] minutesInDay = new boolean[1440];
        String[] times = value.split("#");
        for (String t : times) {
            if (t.contains("-")) {
                String[] tr = t.split("-");         // the string array time range
                String[] start = tr[0].split(":");  // the string array time start
                int hs = Integer.parseInt(start[0]);      // the int hour(start)
                int ms = Integer.parseInt(start[1]);      // the int minute(start)
                int s = hs * 60 + ms;                     // start at sth minute in the day
                String[] end = tr[1].split(":");    // the string array time end
                int he = Integer.parseInt(end[0]);        // the int hour(end)
                int me = Integer.parseInt(end[1]);        // the int minute(end)
                int e = he * 60 + me;                     // end at eth minute in the day
                for (int i = s; i < e; i++) {
                    minutesInDay[i] = true;
                }
            } else {
                String[] tt = t.split(":");
                int h = Integer.parseInt(tt[0]);
                int m = Integer.parseInt(tt[1]);
                minutesInDay[h * 60 + m] = true;
            }
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        int nthMinuteOfTheDay = hour * 60 + minute;
        if (minutesInDay[nthMinuteOfTheDay]) {
            return true;
        }

        return false;
    }

    private boolean inspectAPP_ON_SCREEN(String value) {
        return false;
    }
}























