package com.ordinary.android.projectcache;

import android.content.Context;
import android.util.Log;

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

public class Events {

    Context context;
    File eventsFile;

    private List<Event> eventsList;     // store all the events information
    private Event defaultEvent;
    List<Integer> activatedEventsList;   // store all eventID of activated events (switch is on)


    public Events(Context inputContext, File inputEventsFile) {

        context = inputContext;
        eventsFile = inputEventsFile;

        eventsList = new ArrayList<>();
        activatedEventsList = new ArrayList<>();
        for (Event e : eventsList) {
            if (e.isActivated) {
                activatedEventsList.add(e.eventID);
            }
        }

        // if the eventsList.csv file does not exists, create one
        if (!eventsFile.exists() || eventsFile.isDirectory()) {
            eventsCSVConstruct();
            // initialize the demo default event, put it into csv
            defaultEvent = firstTimeInitializeDemoDefaultEvent();
            //defaultEvent.eventID = 0;
            addEventToCSV(defaultEvent);

            //** Hard code some event for development **************************************** START
            hardCodeSomeTestingEventsForDevelopment();
            //** Hard code some event for development *************************************** FINISH
        }

        FileInputStream fis = null;

        // input events to the events object form events.csv file
        try {
            fis = context.openFileInput(eventsFile.getName());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            // ignore the first line which is title
            line = br.readLine();
            Log.d(" ", line);

            // parse defaultEvent
            line = br.readLine();
            Log.d(" ", line);
            defaultEvent = parseEventData(line);

            // parse events to eventsList
            while ((line = br.readLine()) != null) {
                Log.d(" ", line);
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

        newEvent.eventID = eventsList.size() + 1;
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

    public void resetDefaultEvent(Event e){
        this.defaultEvent = e;
    }

    public Event getDefaultEvent() {
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

    public List<Event> getEventsList() {
        return eventsList;
    }

    public void updateEventActivationStatus(String eventName, boolean status) {
        int eventID = (getEventByName(eventName).eventID);
        eventsList.get(eventID).isActivated = status;
    }

    public void updateEventActivationStatus(int eventID, boolean status) {
        eventsList.get(eventID).isActivated = status;
    }

    public List<Integer> getActivatedEventsIDList() {
        activatedEventsList = new ArrayList<>();
        for (Event e : eventsList) {
            if (e.isActivated) {
                activatedEventsList.add(e.eventID);
            }
        }
        return activatedEventsList;
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
            eventsList.get(i).eventID = i + 1;
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
        //defaultEvent.eventID = 0;
        addEventToCSV(defaultEvent);
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
                    "momentEvent, oneTimeEvent, " +
                    "autoTrigger, isActivated, " +
                    "eventImage, eventColor, eventCategory, executedTimes\n";
            fos.write(csvTitle.getBytes());
            fos.close();

            //** for debugging *************************************************************** START
//            Toast.makeText(context, "csv created, and Title saved to " +
//                    context.getFilesDir() + "/" + eventsFile, Toast.LENGTH_LONG).show();
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

            String eventText = newEvent.eventID.toString() + ", " + nullOrString(newEvent.eventName) + ", " +
                    nullOrString(newEvent.createDate) + ", " + nullOrString(newEvent.createTime) + ", " +
                    nullOrString(newEvent.priorityLevel) + ", " +
                    strArrJoiner(newEvent.triggerableDay) + ", " + strArrJoiner(newEvent.triggerableTime) + ", " +
                    strArrJoiner(newEvent.triggerMethods) + ", " + strArrJoiner(newEvent.triggerValues) + ", " +
                    strArrJoiner(newEvent.tasksTypeStart) + ", " + strArrJoiner(newEvent.tasksValueStart) + ", " +
                    strArrJoiner(newEvent.tasksTypeEnd) + ", " + strArrJoiner(newEvent.tasksValueEnd) + ", " +
                    nullOrString(newEvent.momentEvent) + ", " + nullOrString(newEvent.oneTimeEvent) + ", " +
                    nullOrString(newEvent.autoTrigger) + ", " + nullOrString(newEvent.isActivated) + ", " +
                    nullOrString(newEvent.eventImage) + ", " + nullOrString(newEvent.eventColor) + ", " +
                    nullOrString(newEvent.eventCategory) + ", " + nullOrString(newEvent.executedTimes) + "\n";

            fos.write(eventText.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String nullOrString(String str) {
        if (str == null) {
            return "NULL";
        } else {
            return str;
        }
    }

    private String nullOrString(Integer integer) {
        if (integer == null) {
            return "NULL";
        } else {
            return integer.toString();
        }
    }

    private String nullOrString(Boolean bool) {
        if (bool == null) {
            return "NULL";
        } else {
            if (bool == true) {
                return "true";
            } else {
                return "false";
            }
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

        String[] tTriggerableDay = parseStringListPossibleNULL(data[5]);
        String[] tTriggerableTime = parseStringListPossibleNULL(data[6]);
        String[] tTriggerMethodsStart = parseStringListPossibleNULL(data[7]);
        String[] tTriggerValuesStart = parseStringListPossibleNULL(data[8]);

        String[] tTasksTypeStart = parseStringListPossibleNULL(data[9]);
        String[] tTasksValueStart = parseStringListPossibleNULL(data[10]);
        String[] tTasksTypeEnd = parseStringListPossibleNULL(data[11]);
        String[] tTasksValueEnd = parseStringListPossibleNULL(data[12]);

        Boolean tMomentEvent, tOneTimeEvent, tAutoTrigger, tIsActivated;
        if (data[13].equalsIgnoreCase("true"))
            tMomentEvent = true;
        else
            tMomentEvent = false;

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

        String tEventImage = null;
        if (!data[17].equals("NULL")) {
            tEventImage = data[17];
        }

        Integer tEventColor = null;
        if (!data[18].equals("NULL")) {
            tEventColor = Integer.parseInt(data[18]);
        }

        String tEventCategory = data[19];
        Integer tExecutedTimes = Integer.parseInt(data[20]);

        return new Event(tEventID, tEventName, tCreateData,
                tCreateTime, tPriorityLevel,
                tTriggerableDay, tTriggerableTime,
                tTriggerMethodsStart, tTriggerValuesStart,
                tTasksTypeStart, tTasksValueStart,
                tTasksTypeEnd, tTasksValueEnd,
                tMomentEvent, tOneTimeEvent,
                tAutoTrigger, tIsActivated,
                tEventImage, tEventColor,
                tEventCategory, tExecutedTimes);

    }

    private String[] parseStringListPossibleNULL(String data) {
        if (data.equals("NULL")) {
            return null;
        } else {
            return data.split("\\|");
        }
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

    private Event firstTimeInitializeDemoDefaultEvent() {
        String[] tasksType = {"LAUNCH_APP"};
        String[] tasksValue = {"com.google.android.youtube"};

        return new Event(0, "Default", "2019-08-09",
                "21:46", -1,
                null, null,
                null, null,
                tasksType, tasksValue,
                null, null,
                true, false,
                false, true,
                "" + R.drawable.ic_menu_send, null,
                "DEFAULT", 0);
    }

    //** Hard code some event for development ************************************************ START
    private void hardCodeSomeTestingEventsForDevelopment() {

        String[] triggerMethod1 = {"TIME"}, triggerValues1 = {"21:40-22:40|23:17"};
        String[] tasksTypeStart1 = {"LAUNCH_APP"}, tasksValueStart1 = {"com.google.android.music"};
        Event testEvent1 = new Event(
                2345, "test event 4", "2019-08-03",
                "19:15", 0,
                null, null,
                triggerMethod1, triggerValues1,
                tasksTypeStart1, tasksValueStart1,
                null, null,
                false, false,
                false, true,
                "" + R.drawable.ic_menu_share, 0x000000,
                "NULL", 0);


        String[] triggerMethod2 = {"TIME"}, triggerValues2 = {"19:15-19:29"};
        String[] tasksTypeStart2 = {"LAUNCH_APP"}, tasksValueStart2 = {"com.android.chrome"};
        Event testEvent2 = new Event(
                1234,"test event 2", "2019-08-02",
                "19:31", 0,
                null, null,
                triggerMethod2, triggerValues2,
                tasksTypeStart2, tasksValueStart2,
                null, null,
                false, false,
                false, true,
                "" + R.drawable.ic_menu_gallery, 0xffffff,
                "NULL", 0);



        addEvent(testEvent2);
        addEvent(testEvent1);
        addEvent(testEvent2);

    }
    //** Hard code some event for development FINISH **************************************** FINISH

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

    Boolean momentEvent;            // if true, when event ends, all settings will reset
    Boolean oneTimeEvent;           // if true, this event will only execute once, after that, it will be deleted
    Boolean autoTrigger;            // if true, this event will start without need to click button
    Boolean isActivated;            // if false, the event will not happen although the trigger conditionsArrList match

    String eventImage;              // String imageUri = "drawable://" + R.drawable.image;
    Integer eventColor;
    String eventCategory;           // for future usage
    Integer executedTimes;          // How many times did this event has been used

    public Event(Integer eventID, String eventName, String createDate,
                 String createTime, Integer priorityLevel,
                 String[] triggerableDay, String[] triggerableTime,
                 String[] triggerMethods, String[] triggerValues,
                 String[] tasksTypeStart, String[] tasksValueStart,
                 String[] tasksTypeEnd, String[] tasksValueEnd,
                 Boolean momentEvent, Boolean oneTimeEvent,
                 Boolean autoTrigger, Boolean isActivated,
                 String eventImage, Integer eventColor,
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

        this.momentEvent = momentEvent;
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
//        this.momentEvent = e.momentEvent;
//        this.oneTimeEvent = e.oneTimeEvent;
//        this.autoTrigger = e.autoTrigger;
//        this.isActivated = e.isActivated;
//
//        this.eventCategory = e.eventCategory;
//        this.executedTimes = e.executedTimes;
//    }
}
