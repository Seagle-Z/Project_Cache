package com.ordinary.projectcache.projectcache;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Events {

    private List<Event> events;
    Context context;
    File eventsFile;

    public Events(Context inputContext, File inputEventsFile) {

        events = new ArrayList<>();
        context = inputContext;
        eventsFile = inputEventsFile;

        // if the events.csv file does not exists, create one
        if (!eventsFile.exists() || eventsFile.isDirectory()) {
            FileOutputStream fos = null;

            try {
                fos = context.openFileOutput(eventsFile.getName(), context.MODE_PRIVATE);
                String csvTitle = "eventID, eventName, createDate, createTime, priorityLevel, " +
                        "triggerableDay, triggerableTime, " +
                        "triggerMethods, triggerValues, tasksTypeStart, tasksValueStart, " +
                        "tasksTypeEnd, tasksValueEnd, " +
                        "selfResetEvent, oneTimeEvent, " +
                        "autoTrigger, isActivated, " +
                        "eventCategory, executedTimes\n";
                fos.write(csvTitle.getBytes());

                //** Hard code some event for development ************************************ START
                String testEvent1 = "10, Dunkin' Donuts, 2019-6-1, 10:16, -1, " +
                        "NULL, NULL, " +
                        "CLOSE_ON_GEO_STORE, Dunkin' Dounts, START_APP, Dunkin' Donuts, " +
                        "NULL, NULL, " +
                        "false, false, " +
                        "false, true, " +
                        "NULL, 0\n";
                String testEvent2 = "14, Parking QRCode, 2019-5-12, 16:02, 0, " +
                        "NULL, NULL, " +
                        "CLOSE_ON_GEO_LL, 41.938093&-87.644257, SHOW_QR_CODE, testing_qrcode.png, " +
                        "NULL, NULL, " +
                        "false, false, " +
                        "false, true, " +
                        "NULL, 0\n";
                fos.write(testEvent1.getBytes());
                fos.write(testEvent2.getBytes());
                //Toast.makeText(this, "Hard code events created", Toast.LENGTH_LONG).show();
                //** Hard code some event for development FINISH **************************** FINISH

                fos.close();

                //** for debugging *********************************************************** START
                Toast.makeText(context, "csv created, and Title saved to " +
                        context.getFilesDir() + "/" + eventsFile, Toast.LENGTH_LONG).show();
                //** for debugging ********************************************************** FINISH
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileInputStream fis = null;

        try {
            fis = context.openFileInput(eventsFile.getName());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            line = br.readLine();
            Log.d("", line);
            while ((line = br.readLine()) != null) {
                Log.d("", line);
                events.add(parseEventData(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Event parseEventData(String line) {
        // take the whole line
        String[] data = line.split(", ");

        // put the info in different string
        Integer tEventID = Integer.parseInt(data[0]);
        String tEventName = data[1];
        String tCreateData = data[2];
        String tCreateTime = data[3];
        Integer tPriorityLevel = Integer.parseInt(data[4]);

        String[] tTriggerableDay = data[5].split("\\|");
        String[] tTriggerableTime = data[6].split("\\|");

        String[] tTriggerMethodsStart = data[7].split("\\|");
        String[] tTriggerValuesStart = data[8].split("\\|");
        String[] tTasksTypeStart = data[9].split("\\|");
        String[] tTasksValueStart = data[10].split("\\|");

        String[] tTasksTypeEnd = data[11].split("\\|");
        String[] tTasksValueEnd = data[12].split("\\|");

        Boolean tSelfResetEvent, tOneTimeEvent, tAutoTrigger, tIsActivated;
        if (data[13].equalsIgnoreCase("true"))
            tSelfResetEvent = true;
        else
            tSelfResetEvent = false;

        if (data[14].equalsIgnoreCase("true"))
            tOneTimeEvent = true;
        else
            tOneTimeEvent = false;

        if (data[15].equalsIgnoreCase("true"))
            tAutoTrigger = true;
        else
            tAutoTrigger = false;

        if (data[16].equalsIgnoreCase("true"))
            tIsActivated = true;
        else
            tIsActivated = false;

        String tEventCategory = data[17];
        Integer tExecutedTimes = Integer.parseInt(data[18]);

        return new Event(tEventID, tEventName, tCreateData,
                tCreateTime, tPriorityLevel,
                tTriggerableDay, tTriggerableTime,
                tTriggerMethodsStart, tTriggerValuesStart,
                tTasksTypeStart, tTasksValueStart,
                tTasksTypeEnd, tTasksValueEnd,
                tSelfResetEvent, tOneTimeEvent,
                tAutoTrigger, tIsActivated,
                tEventCategory, tExecutedTimes);

    }

    public boolean addEvent(Event newEvent) {

        for (Event e : events) {
            if (e.eventName.equals(newEvent.eventName)) {
                return false;   // return false if the eventName existed
            }
        }

        addEventToCSV(newEvent);    // add the event to events.csv
        events.add(newEvent);       // add the event to events

        return true;
    }

    private void addEventToCSV(Event newEvent) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(eventsFile.getName(), context.MODE_APPEND);

            String eventText = newEvent.eventID.toString() + ", " + newEvent.eventName + ", " +
                    newEvent.createDate + ", " + newEvent.createTime + ", " + newEvent.priorityLevel.toString() + ", " +
                    stringArrayToString(newEvent.triggerableDay) + ", " + stringArrayToString(newEvent.triggerableTime) + ", " +
                    stringArrayToString(newEvent.triggerMethods) + ", " + stringArrayToString(newEvent.triggerValues) + ", " +
                    stringArrayToString(newEvent.tasksTypeStart) + ", " + stringArrayToString(newEvent.tasksValueStart) + ", " +
                    stringArrayToString(newEvent.tasksTypeEnd) + ", " + stringArrayToString(newEvent.tasksValueEnd) + ", " +
                    newEvent.selfResetEvent.toString() + ", " + newEvent.oneTimeEvent.toString() + ", " +
                    newEvent.autoTrigger.toString() + ", " + newEvent.isActivated.toString() + ", " +
                    newEvent.eventCategory + ", " + newEvent.executedTimes.toString() + "\n";

            fos.write(eventText.getBytes());

            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String stringArrayToString(String[] arr) {
        if (arr == null) {
            return "NULL";
        }
        if (arr.length == 0) {
            return "NULL";
        }

        String s = "";
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                s += "|";
            }
            s += arr[i];
        }

        return s;
    }

    public boolean deleteEventById(Integer deleteEventID) {


        return false;
    }

    public boolean modifyEvent(Event e) {


        return false;
    }

    public Event getEventByID(Integer seekingEventID) {

        for (Event e : events) {
            if (e.eventID == seekingEventID) {
                return e;
            }
        }

        return null;    // if did not find a event match the ID, return null
    }

    public Event getEventByName(String seekingEventName) {

        for (Event e : events) {
            if (e.eventName.equals(seekingEventName)) {
                return e;
            }
        }

        return null;    // if did not find a event match the name, return null
    }


}

class Event implements Serializable {

    Integer eventID;                // It is always unique, automatically generated programmatically
    String eventName;

    String createDate;
    String createTime;
    Integer priorityLevel;          // Normal is 0, bigger is higher priority level

    String[] triggerableDay;          // the day that the event be able to trigger
    String[] triggerableTime;         // the timeButton period that the event be able to trigger

    String[] triggerMethods;        // The methods for start this event
    String[] triggerValues;         // The value for the match method for start this event

    String[] tasksTypeStart;        // The kind of tasks need to do when this event starts
    String[] tasksValueStart;       // The value for the match task when the event starts

    String[] tasksTypeEnd;          // The kind of tasks need to do when this event ends
    String[] tasksValueEnd;         // The value for the match task when the event ends

    Boolean selfResetEvent;         // if true, when event ends, all settings will reset
    Boolean oneTimeEvent;           // if true, this event will only execute once, after that, it will be deleted

    Boolean autoTrigger;            // if true, this event will start without need to click button
    Boolean isActivated;            // if false, the event will not happen although the trigger conditionsArrList match

    String eventCategory;           // for future usage
    Integer executedTimes;          // How many times did this event has been used


    public Event(Integer eventID, String eventName, String createDate,
                 String createTime, Integer priorityLevel,
                 String[] triggerableDay, String[] triggerableTime,
                 String[] triggerMethods, String[] triggerValues,
                 String[] tasksTypeStart, String[] tasksValueStart,
                 String[] tasksTypeEnd, String[] tasksValueEnd,
                 Boolean selfResetEvent, Boolean oneTimeEvent,
                 Boolean autoTrigger, Boolean isActivated,
                 String eventCategory, Integer executedTimes) {

        this.eventID = eventID;
        this.eventName = eventName;
        this.createDate = createDate;
        this.createTime = createTime;
        this.priorityLevel = priorityLevel;

        this.triggerableDay = triggerableDay;
        this.triggerableTime = triggerableTime;
        this.triggerMethods = triggerMethods;
        this.triggerValues = triggerValues;

        this.tasksTypeStart = tasksTypeStart;
        this.tasksValueStart = tasksValueStart;
        this.tasksTypeEnd = tasksTypeEnd;
        this.tasksValueEnd = tasksValueEnd;

        this.selfResetEvent = selfResetEvent;
        this.oneTimeEvent = oneTimeEvent;
        this.autoTrigger = autoTrigger;
        this.isActivated = isActivated;

        this.eventCategory = eventCategory;
        this.executedTimes = executedTimes;

    }

    public Event(Event e) {
        this.eventID = e.eventID;
        this.eventName = e.eventName;
        this.createDate = e.createDate;
        this.createTime = e.createTime;
        this.priorityLevel = e.priorityLevel;

        this.triggerableDay = e.triggerableDay;
        this.triggerableTime = e.triggerableTime;

        this.triggerMethods = e.triggerMethods;
        this.triggerValues = e.triggerValues;
        this.tasksTypeStart = e.tasksTypeStart;
        this.tasksValueStart = e.tasksValueStart;

        this.tasksTypeEnd = e.tasksTypeEnd;
        this.tasksValueEnd = e.tasksValueEnd;

        this.selfResetEvent = e.selfResetEvent;
        this.oneTimeEvent = e.oneTimeEvent;
        this.autoTrigger = e.autoTrigger;
        this.isActivated = e.isActivated;

        this.eventCategory = e.eventCategory;
        this.executedTimes = e.executedTimes;
    }
}
