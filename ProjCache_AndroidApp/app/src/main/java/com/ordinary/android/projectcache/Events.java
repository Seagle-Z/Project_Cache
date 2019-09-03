package com.ordinary.android.projectcache;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
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

public class Events extends AppCompatActivity {

    // TODO: 2019-08-16 重建CSV的时候要弄一个备份，以防在重建过程中app被关掉，CSV里的信息就没了

    Context context;
    File eventsFile;
    private static final String EVENTS_FILE_NAME = "events.csv";

    private List<Event> eventsList;     // store all the events information
    private Event defaultEvent;
    public List<Integer> activatedEventsList;   // store all eventID of activated events (switch is on)


    public Events(Context context) {
        File eventsFile = new File(context.getFilesDir(), EVENTS_FILE_NAME);
        this.context = context;
        this.eventsFile = eventsFile;
        createEvents(context, eventsFile);
    }

    public Events(Context inputContext, File inputEventsFile) {
        this.context = inputContext;
        this.eventsFile = inputEventsFile;
        createEvents(inputContext, inputEventsFile);
    }


    public void createEvents(Context inputContext, File inputEventsFile) {

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
            createTestingEvents();
            //** Hard code some event for development *************************************** FINISH
        }

        // input events to the events object form events.csv file
        updateEventsList();

        //** Hard code modify event for development ****************************************** START
        modifyTestingEvents();
        //** Hard code modify event for development ***************************************** FINISH

    }

    public void updateEventsList() {
        eventsList = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(eventsFile.getName());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            // ignore the first line which is title
            line = br.readLine();
            //Log.d(" ", line);

            // parse defaultEvent
            line = br.readLine();
            //Log.d(" ", line);
            defaultEvent = parseEventData(line);

            // parse events to eventsList
            while ((line = br.readLine()) != null) {
                //Log.d(" ", line);
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

    public boolean deleteEventByID(int inputEventID) {
        if (inputEventID > eventsList.size()) {
            return false;
        }

        eventsList.remove(inputEventID);

        for (int i = 0; i < eventsList.size(); i++) {
            eventsList.get(i).eventID = i;
        }

        return refreshEvents();
    }

    public boolean deleteEventByName(String inputEventName) {
        for (Event event : eventsList) {
            if (event.eventName.equals(inputEventName)) {
                return deleteEventByID(event.eventID);
            }
        }
        return false;
    }

    public boolean modifyEventByID(Integer eventID, Event newEvent) {
        deleteEventByID(eventID);
        addEvent(newEvent);
        return true;
    }

    public boolean modifyEventByName(String eventName, Event newEvent) {
        deleteEventByName(eventName);
        addEvent(newEvent);
        return true;
    }

    public boolean resetDefaultEvent(Event e) {
        if (e.eventID != -1) {
            return false;
        }

        this.defaultEvent = e;
        return true;
    }

    public Event getDefaultEvent() {
        return defaultEvent;
    }

    public Event getEventByID(Integer seekingEventID) {
        updateEventsList();
        if (seekingEventID > eventsList.size()) {
            return null;
        }

        return eventsList.get(seekingEventID);
    }

    public Event getEventByName(String seekingEventName) {
        updateEventsList();
        for (Event e : eventsList) {
            if (e.eventName.equals(seekingEventName)) {
                return e;
            }
        }

        return null;
    }

    public List<Event> getEventsList() {
        updateEventsList();
        return eventsList;
    }

    public boolean updateEventActivationStatus(String eventName, boolean status) {
        updateEventsList();
        int eventID = getEventByName(eventName).eventID;
        eventsList.get(eventID).isActivated = status;
        return refreshEvents();
    }

    public boolean updateEventActivationStatus(int eventID, boolean status) {
        updateEventsList();
        eventsList.get(eventID).isActivated = status;
        return refreshEvents();
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
                    "tasksTypeOngoing, tasksValueOngoing, tasksOngoingRepeatPeriod, " +
                    "instantEvent, oneTimeEvent, " +
                    "autoTrigger, isActivated, " +
                    "eventDescription, eventImage, eventColor, eventCategory, executedTimes\n";
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
                    strArrJoiner(newEvent.tasksTypeOngoing) + ", " + strArrJoiner(newEvent.tasksValueOngoing) + ", " +
                    strArrJoiner(newEvent.tasksOngoingRepeatPeriod) + ", " +
                    nullOrString(newEvent.instantEvent) + ", " + nullOrString(newEvent.oneTimeEvent) + ", " +
                    nullOrString(newEvent.autoTrigger) + ", " + nullOrString(newEvent.isActivated) + ", " +
                    nullOrString(newEvent.eventDescription) + ", " +
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
        String[] tTasksTypeOngoing = parseStringListPossibleNULL(data[13]);
        String[] tTasksValueOngoing = parseStringListPossibleNULL(data[14]);
        String[] tTasksOngoingRepeatPeriod = parseStringListPossibleNULL(data[15]);

        Boolean tInstantEvent, tOneTimeEvent, tAutoTrigger, tIsActivated;
        if (data[16].equalsIgnoreCase("true"))
            tInstantEvent = true;
        else
            tInstantEvent = false;

        if (data[17].equalsIgnoreCase("true"))
            tOneTimeEvent = true;
        else
            tOneTimeEvent = false;

        if (data[18].equalsIgnoreCase("true"))
            tAutoTrigger = true;
        else
            tAutoTrigger = false;

        if (data[19].equalsIgnoreCase("true"))
            tIsActivated = true;
        else
            tIsActivated = false;

        String tEventDescription = null;
        if (!data[20].equals("NULL")) {
            tEventDescription = data[20];
        }

        String tEventImage = null;
        if (!data[21].equals("NULL")) {
            tEventImage = data[21];
        }

        Integer tEventColor = null;
        if (!data[22].equals("NULL")) {
            tEventColor = Integer.parseInt(data[22]);
        }

        String tEventCategory = data[23];

        Integer tExecutedTimes = Integer.parseInt(data[24]);

        return new Event(tEventID, tEventName, tCreateData,
                tCreateTime, tPriorityLevel,
                tTriggerableDay, tTriggerableTime,
                tTriggerMethodsStart, tTriggerValuesStart,
                tTasksTypeStart, tTasksValueStart,
                tTasksTypeEnd, tTasksValueEnd,
                tTasksTypeOngoing, tTasksValueOngoing, tTasksOngoingRepeatPeriod,
                tInstantEvent, tOneTimeEvent,
                tAutoTrigger, tIsActivated,
                tEventDescription,
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

        return new Event(-1, "Default", "2019-08-09",
                "21:46", -1,
                null, null,
                null, null,
                tasksType, tasksValue,
                null, null,
                null, null, null,
                true, false,
                false, true,
                "This is Default event",
                "" + R.drawable.ic_menu_send, null,
                "DEFAULT", 0);
    }



    //** Hard code some event for development ************************************************ START
    public void createTestingEvents() {

        String[] triggerMethod1 = null, triggerValues1 = null;
        String[] tasksTypeStart1 = null, tasksValueStart1 = null;
        Event testEvent1 = new Event(
                2345, "test event 1", "2019-08-03",
                "19:15", 0,
                null, null,
                triggerMethod1, triggerValues1,
                tasksTypeStart1, tasksValueStart1,
                null, null,
                null, null, null,
                false, false,
                true, true,
                "This is the test event 1",
                "" + R.drawable.ic_menu_share, 0x000000,
                "NULL", 0);


        String[] triggerMethod2 = {"TIME"}, triggerValues2 = {"14:39#14:41"};
        String[] tasksTypeStart2 = {"VOLUME_STREAM"}, tasksValueStart2 = {"80"};
        Event testEvent2 = new Event(
                1234, "test event 2", "2019-08-02",
                "19:31", 0,
                null, null,
                triggerMethod2, triggerValues2,
                tasksTypeStart2, tasksValueStart2,
                null, null,
                null, null, null,
                false, false,
                true, true,
                "This is the test event 2",
                "" + R.drawable.ic_menu_gallery, 0xffffff,
                "NULL", 0);

        String[] triggerMethod3 = {"TIME"}, triggerValues3 = {"14:39#14:41"};
        String[] tasksTypeStart3 = {"VOLUME_STREAM"}, tasksValueStart3 = {"80"};
        Event testEvent3 = new Event(
                1234, "test event 3", "2019-08-02",
                "19:31", 0,
                null, null,
                triggerMethod3, triggerValues3,
                tasksTypeStart3, tasksValueStart3,
                null, null,
                null, null, null,
                false, false,
                true, true,
                "This is the test event 3",
                "" + R.drawable.ic_menu_gallery, 0xffffff,
                "NULL", 0);

        addEvent(testEvent1);
//        addEvent(testEvent2);
//        addEvent(testEvent3);

    }

    // modify testing events here
    public void modifyTestingEvents() {

        String[] triggerMethodM1 = null;//{"TIME"};
        String[] triggerValuesM1 = null;//{"8:08-23:10"};

        String[] tasksTypeStartM1 = null;//{"BROWSE_URL"};
        String[] tasksValueStartM1 = null;//{"104-116-116-112-115-58-47-47-119-119-119-46-98-105-108-105-98-105-108-105-46-99-111-109"};

        String[] tasksTypeEndM1 = null;
        String[] tasksValueEndM1 = null;

        String[] tasksTypeOngoingM1 = null;
        String[] tasksValueOngoingM1 = null;
        String[] tasksOngoingRepeatPeriodM1 = null;

        Event testEventM1 = new Event(
                2345, "test event 1", "2019-08-03",
                "19:15", 0,
                null, null,
                triggerMethodM1, triggerValuesM1,
                tasksTypeStartM1, tasksValueStartM1,
                tasksTypeEndM1, tasksValueEndM1,
                tasksTypeOngoingM1, tasksValueOngoingM1, tasksOngoingRepeatPeriodM1,
                false, false,
                true, true,
                "This is the test event 1",
                null, 0x000000,
                "NULL", 0);


        // test event 2
        String[] triggerMethodM2 = null; //{"TIME"};
        String[] triggerValuesM2 = null; //{"19:25"};

        String[] tasksTypeStartM2 = null; //{"VOLUME_STREAM"};
        String[] tasksValueStartM2 = null; //{"20"};

        String[] tasksTypeEndM2 = null;
        String[] tasksValueEndM2 = null;

        String[] tasksTypeOngoingM2 = null;
        String[] tasksValueOngoingM2 = null;
        String[] tasksOngoingRepeatPeriodM2 = null;

        Event testEventM2 = new Event(
                1234,"test event 2", "2019-08-02",
                "19:31", 0,
                null, null,
                triggerMethodM2, triggerValuesM2,
                tasksTypeStartM2, tasksValueStartM2,
                tasksTypeEndM2, tasksValueEndM2,
                tasksTypeOngoingM2, tasksValueOngoingM2, tasksOngoingRepeatPeriodM2,
                false, false,
                true, true,
                "This is the test event 2, this is long description. Can it display?",
                "" + R.drawable.ic_menu_gallery, 0xffffff,
                "NULL", 0);


        // test event 3
        String[] triggerMethodM3 = null; //{"TIME"};
        String[] triggerValuesM3 = null; //{"14:39#14:41"};

        String[] tasksTypeStartM3 = null; //{"VOLUME_STREAM"};
        String[] tasksValueStartM3 = null; //{"50"};

        String[] tasksTypeEndM3 = null;
        String[] tasksValueEndM3 = null;

        String[] tasksTypeOngoingM3 = null;
        String[] tasksValueOngoingM3 = null;
        String[] tasksOngoingRepeatPeriodM3 = null;

        Event testEventM3 = new Event(
                1234, "test event 3", "2019-08-02",
                "19:31", 0,
                null, null,
                triggerMethodM3, triggerValuesM3,
                tasksTypeStartM3, tasksValueStartM3,
                tasksTypeEndM3, tasksValueEndM3,
                tasksTypeOngoingM3, tasksValueOngoingM3, tasksOngoingRepeatPeriodM3,
                false, false,
                true, true,
                "This is the test event 3",
                "" + R.drawable.ic_menu_gallery, 0xffffff,
                "NULL", 0);

//        modifyEventByName("test event 1", testEventM1);
//        modifyEventByName("test event 2", testEventM2);
//        modifyEventByName("test event 3", testEventM3);

    }
    //** Hard code some event for development FINISH **************************************** FINISH
}
