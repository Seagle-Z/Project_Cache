General separator rules:
L1 separator logically is "and"
L2 separator logically is "or"

1.
eventID
Integer
Must unique
The eventID of the Default Event is 0.
The eventID 0 is reserve for Default Event.

2.
eventName
String
eventName should be unique. eventName is decided by user.

3.
createDate
The date that the event create
Use YYYY-MM-DD format.
E.g. “2019-6-1”

4.
createTime
The time that the event create
Use HH:MM format.
E.g. “16:48”

5.
priorityLevel
Integer
Bigger is higher priority level
Normally event priorityLevel is 0
The priorityLevel of the default event is -1

6. 暂时弃用，全都写 "NULL"
triggerableDay
String
Use the first 3 characters of day name indicate the day.
Use "|" without space to separate days.
Be careful!! Use "\\|" when parse due to Java's syntax.
All days should follow the order of Monday to Sunday.
E.g. "MON|TUE|FRI|SAT"
Use "WKD" for weekdays that is Monday to Friday.
Use "WKN" for weekend that is Saturday and Sunday.
If triggerableDay is "NULL", it means it will available all the day.

7. 暂时弃用，全都写 "NULL"
triggerableTime
String
HH:MM-HH:MM###HH:MM-HH:MM
Use "|" without space to separate days.
Be careful!! Use "\\|" when parse due to Java's syntax.
E.g. “16:59-17:50|18:20-22:25”
Time should not have superposition.
The constructor will automatically convert it to no superposition.
E.g. If input is "14:00-16:00|15:00-17:00|20:00-21:00",
     then the constructor will automatically convert it to
     "14:00-17:00|0:00-21:00"

8 & 9
triggerMethod & triggerValues
String
Use “|” without space as L1 separator to separate each triggerMethod
The first position triggerValues match the first position triggerMethod
E.g. triggerMethod            triggerValues
     “TIME|BLUETOOTH”         "20:00-22:30#23:00-23:30|WH-1000Xm3"
Explanation: Trigger this event if:
                1. time is in 20:00-22:30 or in 23:00-23:30
                2. Bluetooth connected to WH-1000Xm3
The explanation of keywords will be in MethodValue&TaskValue Documentation.

10 & 11
tasksTypeStart & taskValueStart
String
Use “|” without space as L1 separator to separate each taskTypeStart
The first position tasksTypeStart match the first position taskValueStart
E.g. tasksTypeStart             taskValueStart
     OPEN_APP|VOLUME|MODE       tv.danmuku.bili|50|DO_NOT_DISTRIBUTE
Explanation: When the event triggered, then do:
                1. Open Bilibili app
                2. Change volume to 50%
                3. Change mode to "Do not distribute"
The explanation of keywords will be in MethodValue&TaskValue Documentation.

12 & 13
tasksTypeEnd & tasksValueEnd
String
All same with 10 & 11
The only different is when the event ends, do tasksTypeEnd & tasksValueEnd

14.
selfResetEvent
Boolean
If true, when the event finish, it reset all the settings back to the
value when the event start.

15.
oneTimeEvent
Boolean
If true, this event will automatically set the isActivated value to false

16.
autoTrigger
Boolean
If true, this event can be triggered automatically without click the event core card.
If false, this event can be triggered only when the user click the event core card.

17.
isActivated
Boolean
If true, this event is activated, and it can be triggered.
If false, this event is not activated, it cannot be triggered.

18.
eventCategory
String
The category of the event, used for classify events.

19.
executedTimes
Integer
How many times did this event executed.
