package com.ordinary.projectcache.projectcache;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class Events {

    private List<Event> events;

    public Events(Context context, File eventsFile) {

        events = new ArrayList<>();

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
                parseEventData(line); //** 这个function有问题，要检查那里出问题了
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseEventData(String line) {
        String[] data = line.split(", ");


        Integer tEventID = Integer.parseInt(data[0]);
        String tEventName = data[1];
        String tCreateData = data[2];
        String tCreateTime = data[3];
        Integer tPriorityLevel = Integer.parseInt(data[4]);

        String[] tTriggerMethodsStart = data[5].split("|");
        String[] tTriggerValuesStart = data[6].split("|");
        String[] tTasksTypeStart = data[7].split("|");
        String[] tTasksValueStart = data[8].split("|");


        String[] tTasksTypeEnd = data[9].split("|");
        String[] tTasksValueEnd = data[10].split("|");

        Boolean tSelfResetEvent, tOneTimeEvent, tIsActivated;
        if (data[11].equalsIgnoreCase("true"))
            tSelfResetEvent = true;
        else
            tSelfResetEvent = false;

        if (data[12].equalsIgnoreCase("true"))
            tOneTimeEvent = true;
        else
            tOneTimeEvent = false;

        if (data[13].equalsIgnoreCase("true"))
            tIsActivated = true;
        else
            tIsActivated = false;

        String tEventCategory = data[14];
        Integer tExecutedTimes = Integer.parseInt(data[15]);

        events.add(new Event(tEventID, tEventName, tCreateData,
                tCreateTime, tPriorityLevel,
                tTriggerMethodsStart, tTriggerValuesStart,
                tTasksTypeStart, tTasksValueStart,
                tTasksTypeEnd, tTasksValueEnd,
                tSelfResetEvent, tOneTimeEvent, tIsActivated,
                tEventCategory, tExecutedTimes));

        System.out.println(events.get(0).eventID + " " + events.get(0).eventName + " " + events.get(0).oneTimeEvent);
    }

    public boolean addEvent() {


        return false;
    }

    public boolean deleteEvent(Integer deleteEventID) {



        return false;
    }

    public boolean modifyEvent() {


        return false;
    }

    public Event getEventByID(Integer seekingEventID) {


        return null;
    }

    public Event getEventByName(String seekingEventName) {


        return null;
    }


}

class Event {

    Integer eventID;                // It is always unique, automatically generated programmatically
    String eventName;

    String createDate;
    String createTime;
    Integer priorityLevel;          // Normal is 0, bigger is higher priority level

    String[] triggerMethods;        // The methods for start this event
    String[] triggerValues;         // The value for the match method for start this event

    String[] tasksTypeStart;        // The kind of tasks need to do when this event starts
    String[] tasksValueStart;       // The value for the match task when the event starts

    String[] tasksTypeEnd;          // The kind of tasks need to do when this event ends
    String[] tasksValueEnd;         // The value for the match task when the event ends

    Boolean selfResetEvent;         // if true, when event ends, all settings will reset
    Boolean oneTimeEvent;           // if true, this event will only execute once, after that, it will be deleted
    Boolean isActivated;            // if false, the event will not happen although the trigger conditions match

    String eventCategory;           // for future usage

    Integer executedTimes;          // How many times did this event has been used





    public Event(Integer eventID, String eventName, String createDate,
                 String createTime, Integer priorityLevel,
                 String[] triggerMethodsStart, String[] triggerValuesStart,
                 String[] tasksTypeStart, String[] tasksValueStart,
                 String[] tasksTypeEnd, String[] tasksValueEnd,
                 Boolean selfResetEvent, Boolean oneTimeEvent, Boolean isActivated,
                 String eventCategory, Integer executedTimes) {

        this.eventID = eventID;
        this.eventName = eventName;
        this.createDate = createDate;
        this.createTime = createTime;
        this.priorityLevel = priorityLevel;

        this.triggerMethods = triggerMethodsStart;
        this.triggerValues = triggerValuesStart;
        this.tasksTypeStart = tasksTypeStart;
        this.tasksValueStart = tasksValueStart;

        this.tasksTypeEnd = tasksTypeEnd;
        this.tasksValueEnd = tasksValueEnd;

        this.selfResetEvent = selfResetEvent;
        this.oneTimeEvent = oneTimeEvent;
        this.isActivated = isActivated;

        this.eventCategory = eventCategory;
        this.executedTimes = executedTimes;

    }

}
