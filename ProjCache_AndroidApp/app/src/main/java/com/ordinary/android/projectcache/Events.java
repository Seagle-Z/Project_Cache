package com.ordinary.android.projectcache;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Events extends AppCompatActivity {

    // TODO: 2019-08-16 重建CSV的时候要弄一个备份，以防在重建过程中app被关掉，CSV里的信息就没了

    Context context;
    File eventsFile;
    private static final String EVENTS_FILE_NAME = "events.csv";

    private Event defaultEvent;
    private List<Event> eventsList;                  // store all the events information
    private List<Integer> activatedEventsIDList;     // store all eventID of activated events


    //- Constructor --------------------------------------------------------------------------------*
    public Events(Context context) {
        File eventsFile = new File(context.getFilesDir(), EVENTS_FILE_NAME);
        this.context = context;
        this.eventsFile = eventsFile;
        constructorHelper(context, eventsFile);
    }


    public Events(Context inputContext, File inputEventsFile) {
        this.context = inputContext;
        this.eventsFile = inputEventsFile;
        constructorHelper(inputContext, inputEventsFile);
    }


    //- constructor helper -------------------------------------------------------------------------*
    public void constructorHelper(Context inputContext, File inputEventsFile) {

        eventsList = new ArrayList<>();
        activatedEventsIDList = new ArrayList<>();

        // if the eventsList.csv file does not exists, create one
        if (!eventsFile.exists() || eventsFile.isDirectory()) {
            constructEventsCSV();
            // initialize the demo default event, put it into csv
            defaultEvent = firstTimeInitializeDemoDefaultEvent();
            //defaultEvent.eventID = 0;
            addEventToCSV(defaultEvent);
        }

        // input events to the events object form events.csv file
        updateByEventsCSV();

        for (Event e : eventsList) {
            if (e.isActivated) {
                activatedEventsIDList.add(e.eventID);
            }
        }

    }


    //- events.csv constructor ---------------------------------------------------------------------*
    private void constructEventsCSV() {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //- Initialize defaultEvent and put it into events.csv -----------------------------------------*
    private Event firstTimeInitializeDemoDefaultEvent() {
        String[] tasksType = {"LAUNCH_APP"};
        String[] tasksValue = {"com.google.android.youtube"};

        return new Event(-1, "68-101-102-97-117-108-116", "2019-08-09",
                "21:46", -1,
                null, null,
                null, null,
                tasksType, tasksValue,
                null, null,
                null, null, null,
                true, false,
                false, true,
                "84-104-105-115-32-105-115-32-68-101-102-97-" +
                        "117-108-116-32-101-118-101-110-116",
                "" + R.drawable.ic_menu_send, null,
                "DEFAULT", 0);
    }


    //- Add an Event to events.csv -----------------------------------------------------------------*
    private void addEventToCSV(Event newEvent) {

        try {
            FileOutputStream fos = null;
            fos = context.openFileOutput(eventsFile.getName(), context.MODE_APPEND);

            String eventText = newEvent.eventID.toString() + ", " +
                    nullOrString(newEvent.eventName) + ", " +
                    nullOrString(newEvent.createDate) + ", " +
                    nullOrString(newEvent.createTime) + ", " +
                    nullOrString(newEvent.priorityLevel) + ", " +
                    strArrJoiner(newEvent.triggerableDay) + ", " +
                    strArrJoiner(newEvent.triggerableTime) + ", " +
                    strArrJoiner(newEvent.triggerMethods) + ", " +
                    strArrJoiner(newEvent.triggerValues) + ", " +
                    strArrJoiner(newEvent.tasksTypeStart) + ", " +
                    strArrJoiner(newEvent.tasksValueStart) + ", " +
                    strArrJoiner(newEvent.tasksTypeEnd) + ", " +
                    strArrJoiner(newEvent.tasksValueEnd) + ", " +
                    strArrJoiner(newEvent.tasksTypeOngoing) + ", " +
                    strArrJoiner(newEvent.tasksValueOngoing) + ", " +
                    strArrJoiner(newEvent.tasksOngoingRepeatPeriod) + ", " +
                    nullOrString(newEvent.instantEvent) + ", " +
                    nullOrString(newEvent.oneTimeEvent) + ", " +
                    nullOrString(newEvent.autoTrigger) + ", " +
                    nullOrString(newEvent.isActivated) + ", " +
                    nullOrString(newEvent.eventDescription) + ", " +
                    nullOrString(newEvent.eventImage) + ", " +
                    nullOrString(newEvent.eventColor) + ", " +
                    nullOrString(newEvent.eventCategory) + ", " +
                    nullOrString(newEvent.executedTimes) + "\n";

            fos.write(eventText.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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


    //- Update eventsList and activatedEventsIDList by rescan events.csv ---------------------------*
    public void updateByEventsCSV() {
        eventsList = new ArrayList<>();
        activatedEventsIDList = new ArrayList<>();

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

            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Event e : eventsList) {
            if (e.isActivated) {
                activatedEventsIDList.add(e.eventID);
            }
        }
    }


    //- Refresh events.csv file by eventsList ------------------------------------------------------*
    private boolean refreshEventsCSV() {
        boolean deleted = eventsFile.delete();
        if (!deleted) {
            return false;
        }

        constructEventsCSV();
        addEventToCSV(defaultEvent);
        for (Event e : eventsList) {
            addEventToCSV(e);
        }

        return true;
    }


    //- Get Event methods --------------------------------------------------------------------------*
    public Event getEventByID(Integer eventID) {
        for (Event e : eventsList) {
            if (e.eventID == eventID) {
                return e;
            }
        }

        return null;
    }

    public Event getEventByName(String eventName) {
        for (Event e : eventsList) {
            if (e.eventName.equals(eventName)) {
                return e;
            }
        }

        return null;
    }

    public Event getDefaultEvent() {
        return defaultEvent;
    }

    public List<Event> getEventsList() {
        return eventsList;
    }

    public List<Integer> getActivatedEventsIDList() {
        return activatedEventsIDList;
    }


    //- Add Event methods --------------------------------------------------------------------------*
    // Attention！ Add method will force to update events info by events.csv
    public boolean addEvent(Event newEvent) {
        updateByEventsCSV();

        for (Event e : eventsList) {
            if (e.eventName.equals(newEvent.eventName)) {
                return false;
            }
        }

        boolean inserted = false;
        for (int i = 0; i < eventsList.size(); i++) {
            if (eventsList.get(i).eventID != i) {
                newEvent.eventID = i;
                eventsList.add(i, newEvent);
                inserted = true;
                break;
            }
        }
        if (!inserted) {
            newEvent.eventID = eventsList.size();
            eventsList.add(newEvent);
        }

        if (newEvent.isActivated) {
            activatedEventsIDList.add(newEvent.eventID);
        }

        return refreshEventsCSV();
    }


    //- Delete Event methods -----------------------------------------------------------------------*
    // Attention！ Delete method will force to update events info by events.csv
    public boolean deleteEventByID(Integer eventID) {
        updateByEventsCSV();

        boolean found = false;
        int deleteIndex = -1;
        for (int i = 0; i < eventsList.size(); i++) {
            if (eventsList.get(i).eventID == eventID) {
                deleteIndex = i;
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }
        eventsList.remove(deleteIndex);

        activatedEventsIDList = new ArrayList<>();
        for (Event e : eventsList) {
            if (e.isActivated) {
                activatedEventsIDList.add(e.eventID);
            }
        }

        return refreshEventsCSV();
    }

    public boolean deleteEventByName(String eventName) {
        updateByEventsCSV();

        boolean found = false;
        int deleteIndex = -1;
        for (int i = 0; i < eventsList.size(); i++) {
            if (eventsList.get(i).eventName.equals(eventName)) {
                deleteIndex = i;
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }
        eventsList.remove(deleteIndex);

        activatedEventsIDList = new ArrayList<>();
        for (Event e : eventsList) {
            if (e.isActivated) {
                activatedEventsIDList.add(e.eventID);
            }
        }

        return refreshEventsCSV();
    }


    //- Modify Event methods -----------------------------------------------------------------------*
    // Attention！ Modify method will force to update events info by events.csv
    public boolean modifyEventByID(Integer eventID, Event event) {
        updateByEventsCSV();
        for (int i = 0; i < eventsList.size(); i++) {
            if (eventsList.get(i).eventID == eventID) {
                eventsList.set(i, event);
                break;
            }
        }

        activatedEventsIDList = new ArrayList<>();
        for (Event e : eventsList) {
            if (e.isActivated) {
                activatedEventsIDList.add(e.eventID);
            }
        }

        return refreshEventsCSV();
    }

    public boolean modifyEventByName(String eventName, Event event) {
        updateByEventsCSV();
        for (int i = 0; i < eventsList.size(); i++) {
            if (eventsList.get(i).eventName.equals(eventName)) {
                eventsList.set(i, event);
                break;
            }
        }

        activatedEventsIDList = new ArrayList<>();
        for (Event e : eventsList) {
            if (e.isActivated) {
                activatedEventsIDList.add(e.eventID);
            }
        }

        return refreshEventsCSV();
    }


    //- Update Event Activation Status methods -----------------------------------------------------*
    // Attention！ Update Event Activation Status will force to update events info by events.csv
    public boolean updateEventActivationStatus(Integer eventID, boolean status) {
        for (Event e : eventsList) {
            if (e.eventID == eventID) {
                e.isActivated = status;
                break;
            }
        }

        activatedEventsIDList = new ArrayList<>();
        for (Event e : eventsList) {
            if (e.isActivated) {
                activatedEventsIDList.add(e.eventID);
            }
        }

        return refreshEventsCSV();
    }

    public boolean updateEventActivationStatus(String eventName, boolean status) {
        for (Event e : eventsList) {
            if (e.eventName.equals(eventName)) {
                e.isActivated = status;
                break;
            }
        }

        activatedEventsIDList = new ArrayList<>();
        for (Event e : eventsList) {
            if (e.isActivated) {
                activatedEventsIDList.add(e.eventID);
            }
        }

        return refreshEventsCSV();
    }


    //- Parsing method -----------------------------------------------------------------------------*
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

}
