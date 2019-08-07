package com.ordinary.android.projectcache;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

// TODO: 2019-08-04 增加 event 的 picture，例如开 App 就是 App 的logo
// TODO: 2019-08-06 加入eventColor 特性

public class Events {

    Context context;
    File eventsFile;

    private List<Event> eventsList;     // store all the events information
    private DefaultEvent defaultEvent;
    List<Integer> activateEventsList;   // store all eventID of activated events (switch is on)
    List<Integer> runningEventsList;    // store the eventIDs that event is currently running


    public Events(Context inputContext, File inputEventsFile) {

        context = inputContext;
        eventsFile = inputEventsFile;

        eventsList = new ArrayList<>();
        activateEventsList = new ArrayList<>();
        for (Event e : eventsList) {
            if (e.isActivated) {
                activateEventsList.add(e.eventID);
            }
        }

        // if the eventsList.csv file does not exists, create one
        if (!eventsFile.exists() || eventsFile.isDirectory()) {
            eventsCSVConstruct();
            // initialize the demo default event, put it into csv
            defaultEvent = firstTimeInitializeDemoDefaultEvent();
            setDefaultEventToCSV(defaultEvent);

            //** Hard code some event for development **************************************** START
            hardCodeSomeTestingEventsForDevelopment();
            //** Hard code some event for development FINISH ******************************** FINISH
        }

        FileInputStream fis = null;

        // input events
        try {
            fis = context.openFileInput(eventsFile.getName());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            // ignore the first line which is title
            line = br.readLine();
            Log.d("", line);

            // parse defaultEvent
            line = br.readLine();
            Log.d(" ", line);
            defaultEvent = parseDefaultEventData(line);

            // parse events to eventsList
            while ((line = br.readLine()) != null) {
                Log.d("", line);
                eventsList.add(parseEventData(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean addEvent(Event newEvent) {
        for (Event e : eventsList) {
            if (e.eventName.equals(newEvent.eventName)) {
                return false;
            }
        }

        newEvent.eventID = eventsList.size();
        eventsList.add(newEvent);

        return refreshEvents();
    }

    public boolean deleteEventById(Integer eventID) {
        if (eventID >= eventsList.size()) {
            return false;
        }

        eventsList.remove(eventID);
        for (int i = eventID; i < eventsList.size(); i++) {
            eventsList.get(i).eventID--;
        }

        return refreshEvents();
    }

    public boolean modifyEvent(Event e) {
        if (e.eventID >= eventsList.size()) {
            return false;
        }

        eventsList.set(e.eventID, e);

        return refreshEvents();
    }

    public void resetDefaultEvent(DefaultEvent e){
        this.defaultEvent = e;
    }

    public DefaultEvent getDefaultEvent() {
        return defaultEvent;
    }

    public Event getEventByID(Integer seekingEventID) {
        if (seekingEventID >= eventsList.size()) {
            return null;
        }

        return eventsList.get(seekingEventID);
    }

    public Event getEventByName(String seekingEventName) {
        for (Event e : eventsList) {
            if (e.eventName.equals(seekingEventName)) {
                return e;
            }
        }

        return null;
    }

    public boolean updateActivedEventsList(int eventID, boolean activated) {
        eventsList.get(eventID).isActivated = activated;
        activateEventsList = new ArrayList<>();
        for (Event e : eventsList) {
            if (e.isActivated) {
                activateEventsList.add(e.eventID);
            }
        }
        return refreshEvents();
    }

    public void updateRunningEventsList() {

    }

    private boolean refreshEvents() {
        // sort eventsList by eventName in ascending order
        Collections.sort(eventsList, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.eventName.compareTo(e2.eventName);
            }
        });
        for (int i = 0; i < eventsList.size(); i++) {
            eventsList.get(i).eventID = i;
        }
        return refreshEventsCSV();
    }

    private boolean refreshEventsCSV() {
        boolean deleted = eventsFile.delete();
        if (!deleted) {
            return false;
        }

        // construct csv and put the default event
        eventsCSVConstruct();
        // put default into csv
        setDefaultEventToCSV(defaultEvent);
        // put others event
        for (Event e : eventsList) {
            addEventToCSV(e);
        }
        return true;
    }

    private void eventsCSVConstruct() {
        try {
            FileOutputStream fos = null;
            fos = context.openFileOutput(eventsFile.getName(), context.MODE_PRIVATE);
            String csvTitle = "eventID, eventName, createDate, createTime, priorityLevel, " +
                    "triggerableDay, triggerableTime, " +
                    "triggerMethods, triggerValues, tasksTypeStart, tasksValueStart, " +
                    "tasksTypeEnd, tasksValueEnd, " +
                    "selfResetEvent, oneTimeEvent, " +
                    "autoTrigger, isActivated, " +
                    "eventImage, eventColor, eventCategory, executedTimes\n";
            fos.write(csvTitle.getBytes());
            fos.close();

            //** for debugging *************************************************************** START
            Toast.makeText(context, "csv created, and Title saved to " +
                    context.getFilesDir() + "/" + eventsFile, Toast.LENGTH_LONG).show();
            //** for debugging ************************************************************** FINISH
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addEventToCSV(Event newEvent) {
        try {
            FileOutputStream fos = null;
            fos = context.openFileOutput(eventsFile.getName(), context.MODE_APPEND);

            String eventText = newEvent.eventID.toString() + ", " + newEvent.eventName + ", " +
                    newEvent.createDate + ", " + newEvent.createTime + ", " + newEvent.priorityLevel.toString() + ", " +
                    strArrJoiner(newEvent.triggerableDay) + ", " + strArrJoiner(newEvent.triggerableTime) + ", " +
                    strArrJoiner(newEvent.triggerMethods) + ", " + strArrJoiner(newEvent.triggerValues) + ", " +
                    strArrJoiner(newEvent.tasksTypeStart) + ", " + strArrJoiner(newEvent.tasksValueStart) + ", " +
                    strArrJoiner(newEvent.tasksTypeEnd) + ", " + strArrJoiner(newEvent.tasksValueEnd) + ", " +
                    newEvent.selfResetEvent.toString() + ", " + newEvent.oneTimeEvent.toString() + ", " +
                    newEvent.autoTrigger.toString() + ", " + newEvent.isActivated.toString() + ", " +
                    newEvent.eventImage + ", " + newEvent.eventColor.toString() + ", " +
                    newEvent.eventCategory + ", " + newEvent.executedTimes.toString() + "\n";

            fos.write(eventText.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultEventToCSV(DefaultEvent defaultEvent) {
        try {
            FileOutputStream fos = null;
            fos = context.openFileOutput(eventsFile.getName(), context.MODE_APPEND);
            String eventText = strArrJoiner(defaultEvent.tasksType) + ", " +
                    strArrJoiner(defaultEvent.tasksValue) + "\n";
            fos.write(eventText.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DefaultEvent parseDefaultEventData(String line) {
        String[] data = line.split(", ");
        String[] tTasksType = data[0].split("\\|");
        String[] tTasksValue = data[1].split("\\|");
        return new DefaultEvent(tTasksType, tTasksValue);
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

        String tEventImage = data[17];
        Integer tEventColor = Integer.parseInt(data[18]);
        String tEventCategory = data[19];
        Integer tExecutedTimes = Integer.parseInt(data[20]);

        return new Event(tEventName, tCreateData,
                tCreateTime, tPriorityLevel,
                tTriggerableDay, tTriggerableTime,
                tTriggerMethodsStart, tTriggerValuesStart,
                tTasksTypeStart, tTasksValueStart,
                tTasksTypeEnd, tTasksValueEnd,
                tSelfResetEvent, tOneTimeEvent,
                tAutoTrigger, tIsActivated,
                tEventImage, tEventColor,
                tEventCategory, tExecutedTimes);

    }

    private String strArrJoiner(String[] arr) {
        if (arr == null) {
            return "NULL";
        }
        if (arr.length == 0) {
            return "NULL";
        }

        StringJoiner joiner = new StringJoiner("|");
        for (int i = 0; i < arr.length; i++) {
            joiner.add(arr[i]);
        }

        return joiner.toString();
    }

    private DefaultEvent firstTimeInitializeDemoDefaultEvent() {
        String[] tasksType = {"BROWSE_URL|LAUNCH_APP"};
        String[] tasksValue = {"https://www.google.com|com.google.android.youtube"};

        return new DefaultEvent(tasksType, tasksValue);
    }

    //** Hard code some event for development ************************************************ START
    private void hardCodeSomeTestingEventsForDevelopment() {

        String[] triggerMethod1 = {"TIME"}, triggerValues1 = {"19:15|19:17"};
        String[] tasksTypeStart1 = {"LAUNCH_APP"}, tasksValueStart1 = {"com.google.android.youtube"};
        Event testEvent1 = new Event(
                "test event 1", "2019-08-03",
                "19:15", 0,
                null, null,
                triggerMethod1, triggerValues1,
                tasksTypeStart1, tasksValueStart1,
                null, null,
                false, false,
                false, true,
                null, 0x000000,
                "NULL", 0);


        String[] triggerMethod2 = {"TIME"}, triggerValues2 = {"19:15-19:29"};
        String[] tasksTypeStart2 = {"LAUNCH_APP"}, tasksValueStart2 = {"com.android.chrome"};
        Event testEvent2 = new Event(
                "test event 2", "2019-08-02",
                "19:31", 0,
                null, null,
                triggerMethod2, triggerValues2,
                tasksTypeStart2, tasksValueStart2,
                null, null,
                false, false,
                false, true,
                null, 0xffffff,
                "NULL", 0);



        addEvent(testEvent2);
        addEvent(testEvent1);
        addEvent(testEvent2);

    }
    //** Hard code some event for development FINISH **************************************** FINISH

}

class DefaultEvent {
    String[] tasksType;
    String[] tasksValue;

    DefaultEvent(String[] tasksType, String[] tasksValue) {
        this.tasksType = tasksType;
        this.tasksValue = tasksValue;
    }

}

class Event {

    Integer eventID;                // It is always unique, automatically generated programmatically
    String eventName;

    String createDate;
    String createTime;
    Integer priorityLevel;          // Normal is 0, bigger is higher priority level

    String[] triggerableDay;        // the day that the event be able to trigger
    String[] triggerableTime;       // the timeButton period that the event be able to trigger

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

    String eventImage;
    Integer eventColor;
    String eventCategory;           // for future usage
    Integer executedTimes;          // How many times did this event has been used


    public Event(String eventName, String createDate,
                 String createTime, Integer priorityLevel,
                 String[] triggerableDay, String[] triggerableTime,
                 String[] triggerMethods, String[] triggerValues,
                 String[] tasksTypeStart, String[] tasksValueStart,
                 String[] tasksTypeEnd, String[] tasksValueEnd,
                 Boolean selfResetEvent, Boolean oneTimeEvent,
                 Boolean autoTrigger, Boolean isActivated,
                 String eventImage, Integer eventColor,
                 String eventCategory, Integer executedTimes) {

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

        this.eventImage = eventImage;
        this.eventColor = eventColor;
        this.eventCategory = eventCategory;
        this.executedTimes = executedTimes;

    }

//    public Event(Event e) {
//        this.eventID = e.eventID;
//        this.eventName = e.eventName;
//        this.createDate = e.createDate;
//        this.createTime = e.createTime;
//        this.priorityLevel = e.priorityLevel;
//
//        this.triggerableDay = e.triggerableDay;
//        this.triggerableTime = e.triggerableTime;
//
//        this.triggerMethods = e.triggerMethods;
//        this.triggerValues = e.triggerValues;
//        this.tasksTypeStart = e.tasksTypeStart;
//        this.tasksValueStart = e.tasksValueStart;
//
//        this.tasksTypeEnd = e.tasksTypeEnd;
//        this.tasksValueEnd = e.tasksValueEnd;
//
//        this.selfResetEvent = e.selfResetEvent;
//        this.oneTimeEvent = e.oneTimeEvent;
//        this.autoTrigger = e.autoTrigger;
//        this.isActivated = e.isActivated;
//
//        this.eventCategory = e.eventCategory;
//        this.executedTimes = e.executedTimes;
//    }
}
