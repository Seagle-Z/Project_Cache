General separator rules:
L1 separator logically is "and"
L2 separator logically is "or"


triggerMethod keywords:

TIME
A time that correct to minute, only in this minute that the event will be
triggered. It also could be a time range.

DAY
A single day that the event will be triggered all day. The better way to use
this is to use is with TIME or TIME_RANGE

APP_LAUNCHING
When a App opened by user, trigger this event

BLUETOOTH
If the device connected to a Bluetooth device, then triggered this event. Finish
this event when the bluetooth connection disconnect.

WIFI
If the device connected to a Wi-Fi host, then triggered this event. Finish this
event when the bluetooth connection disconnect.

LOCATION
If the device in a specific location, then trigger the event. Finish this event
when the device leave this location.



TriggerValues rules that match triggerMethod keywords

TIME
Use HH:MM format.
Use 24 hours per day format.
Use "#" without space as L2 separator to separate each time
E.g.    "08:02"
        “14:28-17:50"
        “08:30-10:48#17:50-19:30#22:00-23:36"
        "08:02#08:30-09:30#12:00#22:00-23:36"

DAY
Use the first 3 characters of day name indicate the day.
Use "#" without space as L2 separator to separate each day.
All days should follow the order of Monday to Sunday.
E.g. "MON#TUE#FRI#SAT"
Use "WKD" for weekdays that is Monday to Friday.
Use "WKN" for weekend that is Saturday and Sunday.

APP_LAUNCHING
Use "#" without space as L2 separator to separate each App
E.g.    "tv.danmuku.bili"
        "tv.danmuku.bili#youtube.google.com"

BLUETOOTH


WIFI


LOCATION





About Task ---------------------------------------------------------------------

tasksType's keywords:

OPEN_APP
When the event triggered, open one or more App.

QR_CODE
Show a QR code.

BROSE_URL
Open url in web browser.

VOLUME
Change device volume.

BRIGHT
Change screen brightness.



tasksValue's rules that match triggerMethod keywords

OPEN_APP
Use "#" without space as L2 separator to separate each App
E.g.    "tv.danmuku.bili"
        "tv.danmuku.bili#maps.google.com"

QR_CODE
Only one QR code could be Show
Store the QR code picture's directory

BROSE_URL
For BROSE_URL, we do not store url in events.csv. Instead, we stroe urlID in event.csv
If a event contains task about url, the event generator will automatically
create urlID for the url, and store the url in urlStore.csv file.
Only one url could be open
E.g.
In events.csv:
tasksTypeStart          tasksValueStart
BRIGHT|BROSE_URL        80|24
In urlStore.csv, the format is urlID, url
E.g.
In urlStore.csv
1, https://www.google.com
2, https://www.baidu.com
24, https://www.bilibili.com
51, https://maps.google.com

VOLUME
From 0% to 100%, only 10, 20, 30... available
Use only integer number without "%"
E.g.    "20"    means set volume to 20%
        "100"   means set volume to 100%
For some device that do not match 10 level change, we will map each to a value
that match the device.

BRIGHT
From 0% to 100%, only 10, 20, 30... available
Use only integer number without "%"
E.g.    "20"    means set brightness to 20%
        "100"   means set brightness to 100%
For some device that do not match 10 level change, we will map each to a value
that match the device.