package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
  This class is designed to open the Time selection activity. User would choose time values
  to trigger certain tasks
*/
public class TimeSelectorActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener {


    /* addBeginTimeButton: The button would open the TimePickerDialog to select time, selected time
                           will be add to related variables

       addEndTimeButton: The button would open the TimePickerDialog to select time, selected time
                         will be add to related variables

       selectorActivityCompleteButton: The button would check whether user selected valid time, if
                                       time is valid, then activity would be closed and result would
                                       be return to SetupTriggerMethodDateTimeActivity.
     */
    Button addBeginTimeButton, addEndTimeButton, selectorActivityCompleteButton;

    //timeRangeOnOffModeSwitch: A switch to turn on time range feature.
    Switch timeRangeOnOffModeSwitch;
    //Use to display in time range mode
    TextView EndTimeTextView;
    /*TimeRangeOn: To check whether user decided to use the time range feature, if on,
                   then, EndTimeTextView and addEndTimeButton would be activated.

      editMode: To check if the current activity is called based on the editing feature.

      buttonPress: To check if the user clicked the addBeginTimeButton or addEndtimeButton.
    */
    private boolean timeRangeOn, editMode, buttonPress;
    //TimeRangeDivider is used to parse retrieval time for editing feature.
    private String[] timeRangeDivider;

    /*
      hour: A string that is used to store hour value as a string, e.g. 08:00, 8 is the value
      minutes: A string that is used to store minute as a string, e.g. 08:15, 15 is the value
      retrieveTime: A string that is used to store retrieval time information from the editing
                    feature, e.g, "8:00" or "8:00-9:00" are two types of retrieval time value
      beginHour: A string that is used in both edit and non-edit mode to store the time range
                 hour value. E.g "8:15-9:30", 8 is the beginHour
      beginMinute: A string that is used in both edit and non-edit mode to store the time range
                  minute value. E.g "8:15-9:30", 15 is the beginMinutes
      endHour: Similar idea to beginHour, to store 9 from the example above.
      endMinutes: Similar idea to beginMinutes, to store 30 from the example above.
    */
    private String hour = null, minutes = null, retrieveTime = null,
            beginHour = null, beginMinute = null, endHour = null, endMinute = null;

    //This AlterDialog is used to display any popup messages.
    private AlertDialog.Builder WarningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_selector);

        addBeginTimeButton = (Button) findViewById(R.id.pick_start_time);
        addEndTimeButton = (Button) findViewById(R.id.pick_end_time);
        selectorActivityCompleteButton =
                (Button) findViewById(R.id.time_selector_activity_layout_complete_button);
        timeRangeOnOffModeSwitch = (Switch) findViewById(R.id.time_range_switch);
        EndTimeTextView = (TextView) findViewById(R.id.end_time_title);

        //Due to default timeRangeOn == False, set endTime related feature invisible.
        addEndTimeButton.setVisibility(View.INVISIBLE);
        EndTimeTextView.setVisibility(View.INVISIBLE);

        //Initialize the warning window for any kind of popup warning purposes
        WarningDialog = new AlertDialog.Builder(TimeSelectorActivity.this);
        WarningDialog.setTitle("Warning");


        /*First check the whether the intent is called from the SetupTriggerMethodDateTimeActivity's
          editMode, if it does, it will activate any editing related feature.
        */
        Intent intent = getIntent();
        try {
            if (intent.hasExtra("RETRIEVE")) {
                retrieveTime = intent.getStringExtra("RETRIEVE");
                editMode = true;

                //Check if it's time range or single time to decide a different parse method.
                if (retrieveTime.contains("-")) {
                    timeRangeDivider = retrieveTime.split("-");
                    splitTimeRange(timeRangeDivider);
                    timeRangeOn = true;
                    timeRangeOnOffModeSwitch.setChecked(true);
                    addEndTimeButton.setEnabled(true);
                    addEndTimeButton.setVisibility(View.VISIBLE);
                    EndTimeTextView.setVisibility(View.VISIBLE);
                    addBeginTimeButton.setText(
                            "Event activate at: " +
                                    beginHour + ":" + beginMinute
                    );
                    addEndTimeButton.setText("Event stops at: " + endHour + ":" + endMinute);
                } else {
                    addBeginTimeButton.setText("Event activate at: " + retrieveTime);
                    String[] tempTime = retrieveTime.split(":");
                    beginHour = tempTime[0].trim();
                    beginMinute = tempTime[1].trim();
                }
            }
        } catch (NullPointerException e) {
        }

        /*Check if the timeRange requirement is needed, if it's on, then set the add ending time
          related functionality turn on.
        */
        timeRangeOnOffModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timeRangeOn = true;

                    Toast.makeText(TimeSelectorActivity.this,
                            "Time Range Mode ON",
                            Toast.LENGTH_SHORT).show();

                    if(beginHour == null) {
                        addEndTimeButton.setEnabled(false);
                        addEndTimeButton.getBackground().setColorFilter(Color.GRAY,
                                PorterDuff.Mode.MULTIPLY);
                    }
                    else
                    {
                        addEndTimeButton.setEnabled(true);
                        addEndTimeButton.getBackground().setColorFilter(Color.BLACK,
                                PorterDuff.Mode.MULTIPLY);
                    }
                    addEndTimeButton.setVisibility(View.VISIBLE);
                    EndTimeTextView.setVisibility(View.VISIBLE);
                } else {
                    timeRangeOn = false;

                    Toast.makeText(TimeSelectorActivity.this,
                            "Time Range Mode OFF",
                            Toast.LENGTH_SHORT).show();

                    addEndTimeButton.setEnabled(false);
                    addEndTimeButton.setVisibility(View.INVISIBLE);
                    EndTimeTextView.setVisibility(View.INVISIBLE);
                    endHour = null;
                    endMinute = null;
                }

            }
        });


        addBeginTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePicker();
                buttonPress = true;
            }
        });

        addEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePicker();
                buttonPress = false;
            }
        });

        /*The OnClickListener for the complete button, if the button is click, then it would check
          whether the selected hours are valid.
        */
        selectorActivityCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkResult();
                } catch (NullPointerException e) {
                }
            }
        });
    }

    /*An required method that the TimePickerDialog class need to receive data back from the
      TimeDialog window.
    */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        /* check if the hour and min is less than 10, if so add a 0 before the value for better
           printing use. Eg. 9:1 -> 09:01
        */
        if (hourOfDay < 10) {
            hour = "0" + Integer.toString(hourOfDay);
        } else
            hour = Integer.toString(hourOfDay);

        if (minute < 10)
            minutes = "0" + Integer.toString(minute);
        else
            minutes = Integer.toString(minute);

        //Parse the time with the SimpleDateFormat method.
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        Date input = null;
        Date knownTime = null;

        if (buttonPress) {
            //If the addBeginTimeButton is click, then check if the selected value is valid

            if (endHour != null && endMinute != null) {
                try {
                    input = parser.parse(hour + ":" + minutes);
                    knownTime = parser.parse(endHour + ":" + endMinute);
                } catch (ParseException e) {
                }
                /*If the value is the same, then it would popup a warning message that begin time
                  can't not be same as end time.
                */
                if (knownTime.equals(input))
                    unchangedWarningWindow(WarningDialog, 2);

                /* If the end time value already exist, e.g. 10:00pm., if the user selected 11:00pm
                   as the starting time, then it would show an warning message that start time
                   cannot begins after the ending time.
                */
                if (knownTime.before(input))
                    unchangedWarningWindow(WarningDialog, 5);
            }
            //If time value is valid, then it adds successfully.
            else {
                addBeginTimeButton.setText("Event activates at: " + hour + ":" + minutes);
                beginHour = hour;
                beginMinute = minutes;
                addEndTimeButton.setEnabled(true);
                addEndTimeButton.getBackground().setColorFilter(Color.BLACK,
                        PorterDuff.Mode.MULTIPLY);
            }
        }
        //If the addBeginTimeButton is click, then check if the selected value is valid
        else {
            //Use to check if the input is valid, if is valid, value stays true
            boolean flag = true;

            try {
                input = parser.parse(hour + ":" + minutes);
                knownTime = parser.parse(beginHour + ":" + beginMinute);
            } catch (ParseException e) {
            }

            /*If the value is the same, then it would popup a warning message that begin time
                  can't not be same as end time.
                */
            if (knownTime.equals(input)) {
                unchangedWarningWindow(WarningDialog, 2);
                flag = false;
            }

            /* If the start time value already exist, e.g. 10:00pm., if the user selected 9:00pm
                   as the starting time, then it would show an warning message that end time
                   cannot end before the start time.
                */
            if (knownTime.after(input)) {
                unchangedWarningWindow(WarningDialog, 6);
                flag = false;
            }
            if (flag) {
                addEndTimeButton.setText("Event ends at: " + hour + ":" + minutes);
                endHour = hour;
                endMinute = minutes;
            }
        }
    }

    //Display the TimePicker Dialog Window
    public void startTimePicker() {
        DialogFragment timepicker = new TimePickerFragment();
        timepicker.show(getSupportFragmentManager(), "Time Selector");
    }

    /*This function is called when the complete button is clicked. It checks if the final result
      is valid to return, this includes the checking for editing mode.
    */
    private void checkResult() {

        //if it's in editing mode
        if (editMode) {
            //While there is no time range required
            if (!timeRangeOn) {
                /*if the original edit value is a time range based, which means user manually turn
                  the time range feature off during editing, then it only returns either the
                  original begin time or new begin time if user changed
                */
                if (retrieveTime.contains("-")) {
                    if (hour == null && minutes == null) {
                        getIntent(beginHour + ":" + beginMinute);
                        finish();
                    }
                }
                /*If the original value is not time range based, then check if the new selected
                  value if the same */
                else {
                    //if the new selected time value is the same, then show an unchanged warning
                    if (retrieveTime.equals(hour + ":" + minutes))
                        unchangedWarningWindow(WarningDialog, 1);
                    //if the new selected time value is not the same, return the value normally.
                    if (!retrieveTime.equals(hour + ":" + minutes) &&
                            hour != null &&
                            minutes != null) {
                        getIntent(hour + ":" + minutes);
                        finish();
                    }
                    //if there is no change, then just return the original value
                    if (hour == null && minutes == null) {
                        getIntent(beginHour + ":" + beginMinute);
                        finish();
                    }
                }
            } //end edit mode + time range on

            //if time range is on
            else {
                /* if the original value is not a time range based value*/
                if (!retrieveTime.contains("-")) {
                    /*while the endhour value is not selected, then a warning message would show
                      up about empty end time value
                     */
                    if (endHour == null && endMinute == null) {
                        unchangedWarningWindow(WarningDialog, 3);
                    }
                    //if the input is valid, then return
                    else {
                        getIntent(beginHour + ":" +
                                beginMinute + "-" + endHour + ":" + endMinute);
                        finish();
                    }
                }
                /*If the original value is already a time based*/
                else {
                    /*Show an unchanged warning message if the time is same as the original value*/
                    if (timeRangeDivider[0].trim().equals(
                            beginHour + ":" + beginMinute) &&
                            timeRangeDivider[1].trim().equals(endHour + ":" + endMinute)) {
                        unchangedWarningWindow(WarningDialog, 1);
                    }
                    //if either result has chagnes, then should return normally
                    if (!timeRangeDivider[0].trim().equals(beginHour + ":" + beginMinute) ||
                            !timeRangeDivider[1].trim().equals(endHour + ":" + endMinute)) {
                        getIntent(beginHour + ":" +
                                beginMinute + "-" + endHour + ":" + endMinute);
                        finish();
                    }
                }
            }//End time range mode checking
        }//End editing mode checking

        //if Editing mode is not on
        else {
            //If there is no time range required
            if (!timeRangeOn) {
                //If there is no time value when click complete, then a warning message popup
                if (addBeginTimeButton.getText().toString().contains("Add")) {
                    unchangedWarningWindow(WarningDialog, 4);
                } else {
                    getIntent(hour + ":" + minutes);
                    finish();
                }
            }

            //if time range is on, check if input is empty, if not return, if empty show warning
            if (timeRangeOn) {
                if (addBeginTimeButton.getText().toString().contains("Add") ||
                        addEndTimeButton.getText().toString().contains("Add")) {
                    unchangedWarningWindow(WarningDialog, 3);
                } else if (!addBeginTimeButton.getText().toString().contains("Add") &&
                        !addEndTimeButton.getText().toString().contains("Add")) {
                    getIntent(beginHour + ":" +
                            beginMinute + "-" + endHour + ":" + endMinute);
                    finish();
                }
            }
        }
    }

    //Create an return intent with required value
    public Intent getIntent(String result) {
        Intent intent = new Intent();
        intent.putExtra("Time", result);
        setResult(Activity.RESULT_OK, intent);
        return intent;
    }

    //Only use for time range based value parsing only
    public void splitTimeRange(String[] timeRangeDivider) {
        String tempBeginTime = timeRangeDivider[0].trim();
        String tempEndTime = timeRangeDivider[1].trim();
        String[] _tempBeginHour = tempBeginTime.split(":");
        String[] _tempEndhour = tempEndTime.split(":");
        beginHour = _tempBeginHour[0].trim();
        beginMinute = _tempBeginHour[1].trim();
        endHour = _tempEndhour[0].trim();
        endMinute = _tempEndhour[1].trim();
    }

    //A collection of warning messages
    public void unchangedWarningWindow(AlertDialog.Builder WarningDialog, int caseID) {
        switch (caseID) {

            //Unchanged valid warning
            case 1:
                WarningDialog.setMessage(
                        "The selected hour is unchanged, are you sure to continue the process?"
                );
                WarningDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                WarningDialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getIntent(retrieveTime);
                        finish();
                    }
                });
                WarningDialog.show();
                break;
            //Same input warning
            case 2:
                WarningDialog.setMessage("The selected hour cannot be the same.");
                WarningDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                WarningDialog.show();
                break;

            //Missing input for time range
            case 3:
                WarningDialog.setMessage(
                        "Please add both start and end time to complete the process"
                );
                WarningDialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                WarningDialog.show();
                break;

            //Missing input for non-time-range
            case 4:
                WarningDialog.setMessage("No time selected, please try again.");
                WarningDialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                WarningDialog.show();
                break;

            //Invalid time selection
            case 5:
                WarningDialog.setMessage("Starting time must occur before the ending time.");
                WarningDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                WarningDialog.show();
                break;

            //Invalid time selection
            case 6:
                WarningDialog.setMessage("Ending time must occur after the starting time.");
                WarningDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                WarningDialog.show();
                break;
            default:
        }
    }
}
