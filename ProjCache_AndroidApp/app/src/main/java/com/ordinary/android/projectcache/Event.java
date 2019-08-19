package com.ordinary.android.projectcache;

public class Event {

    public Integer eventID;                // It is always unique, automatically generated programmatically
    public String eventName;

    public String createDate;
    public String createTime;
    public Integer priorityLevel;          // Normal is 0, bigger is higher priority level

    public String[] triggerableDay;        // the day that the event be able to trigger
    public String[] triggerableTime;       // the timeButton period that the event be able to trigger

    public String[] triggerMethods;        // The methods for start this event
    public String[] triggerValues;         // The value for the match method for start this event

    public String[] tasksTypeStart;        // The kind of tasks need to do when this event starts
    public String[] tasksValueStart;       // The value for the match task when the event starts

    public String[] tasksTypeEnd;          // The kind of tasks need to do when this event ends
    public String[] tasksValueEnd;         // The value for the match task when the event ends

    public String[] tasksTypeOngoing;
    public String[] tasksValueOngoing;
    public String[] tasksOngoingRepeatPeriod;

    public Boolean instantEvent;           // The event only n
    public Boolean oneTimeEvent;           // if true, this event will only execute once, after that, it will be deleted
    public Boolean autoTrigger;            // if true, this event will start without need to click button
    public Boolean isActivated;            // if false, the event will not happen although the trigger conditionsArrList match

    public String eventImage;              // String imageUri = "drawable://" + R.drawable.image;
    public Integer eventColor;
    public String eventCategory;           // for future usage
    public Integer executedTimes;          // How many times did this event has been used

    public Event(Integer eventID, String eventName, String createDate,
                 String createTime, Integer priorityLevel,
                 String[] triggerableDay, String[] triggerableTime,
                 String[] triggerMethods, String[] triggerValues,
                 String[] tasksTypeStart, String[] tasksValueStart,
                 String[] tasksTypeEnd, String[] tasksValueEnd,
                 String[] tasksTypeOngoing, String[] tasksValueOngoing, String[] tasksOngoingRepeatPeriod,
                 Boolean instantEvent, Boolean oneTimeEvent,
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
        this.tasksTypeOngoing = tasksTypeOngoing;
        this.tasksValueOngoing = tasksValueOngoing;
        this.tasksOngoingRepeatPeriod = tasksOngoingRepeatPeriod;

        this.instantEvent = instantEvent;
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
//        this.instantEvent = e.instantEvent;
//        this.oneTimeEvent = e.oneTimeEvent;
//        this.autoTrigger = e.autoTrigger;
//        this.isActivated = e.isActivated;
//
//        this.eventCategory = e.eventCategory;
//        this.executedTimes = e.executedTimes;
//    }
}
