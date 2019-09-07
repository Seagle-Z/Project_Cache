package com.ordinary.android.projectcache;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.content.Context.ACTIVITY_SERVICE;

public class CoreConditionInspector {

    Context context;
    Events events;

    public CoreConditionInspector(Context context, Events events) {
        this.context = context;
        this.events = events;
    }

    public List<Integer> getTriggerableEventsID() {
        List<Integer> triggerableEventID = new ArrayList<>();
        List<Integer> activatedEventsIDList = events.getActivatedEventsIDList();

        for (Integer id : activatedEventsIDList) {
            if (conditionsMatch(events.getEventByID(id))) {
                triggerableEventID.add(id);
            }
        }

//        System.out.print("Triggerable List: ");
//        for (Integer i : triggerableEventID) {
//            System.out.print(i);
//        }

        return triggerableEventID;
    }

    public boolean conditionsMatch(Event event) {
        if (event.triggerMethods == null) {
            return false;
        }

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

            case "WIFI":
                return inspectWIFI(value);

            default:
                break;

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

    private boolean inspectWIFI(String value) {
        String[] ssidList = value.split("#");

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;

        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            String curSSID = wifiInfo.getSSID();
            int l = curSSID.length();
            curSSID = curSSID.substring(1, l - 1);
            ToolFunctions toolFunctions = new ToolFunctions();
            for (String ssid : ssidList) {
                if (ssid.length() < 3) {
                    continue;
                }
                if (curSSID.equals(toolFunctions.textDecoder(ssid))) {
                    return true;
                }
            }
        }

        return false;
    }

}























