package com.ordinary.projectcache.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimeSelectorActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Button addBeginTimeButton, addEndTimeButton, selectorActivityCompleteButton;
    Switch timeRangeOnOffModeSwitch;
    private boolean timeRangeOn, editMode, buttonPress;
    TextView BeginTimeTextView, EndTimeTextView;
    private String[] timeRangeDivider;
    private String hour = null, minutes = null, retrieveTime = null,
            beginHour = null, beginMinute = null, endHour = null, endMinute = null;
    private AlertDialog.Builder WarningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_selector);

        addBeginTimeButton = (Button) findViewById(R.id.pick_start_time);
        addEndTimeButton = (Button) findViewById(R.id.pick_end_time);
        selectorActivityCompleteButton = (Button) findViewById(R.id.time_selector_activity_layout_complete_button);
        timeRangeOnOffModeSwitch = (Switch) findViewById(R.id.time_range_switch);
        BeginTimeTextView = (TextView) findViewById(R.id.start_time_title);
        EndTimeTextView = (TextView) findViewById(R.id.end_time_title);
        addEndTimeButton.setVisibility(View.INVISIBLE);
        EndTimeTextView.setVisibility(View.INVISIBLE);
        WarningDialog = new AlertDialog.Builder(TimeSelectorActivity.this);
        WarningDialog.setTitle("Warning");

        Intent intent = getIntent();
        try {
            if (intent.hasExtra("RETRIEVE")) {
                retrieveTime = intent.getStringExtra("RETRIEVE");
                editMode = true;
                if (retrieveTime.contains("-")) {
                    timeRangeDivider = retrieveTime.split("-");
                    splitTimeRange(timeRangeDivider);
                    timeRangeOn = true;
                    timeRangeOnOffModeSwitch.setChecked(true);
                    addEndTimeButton.setEnabled(true);
                    addEndTimeButton.setVisibility(View.VISIBLE);
                    EndTimeTextView.setVisibility(View.VISIBLE);
                    addBeginTimeButton.setText("Event activate at: " + beginHour + ":" + beginMinute);
                    addEndTimeButton.setText("Event stops at: " + endHour + ":" + endMinute);
                } else {
                    addBeginTimeButton.setText("Event activate at: " + retrieveTime);
                    String[] tempTime = retrieveTime.split(":");
                    hour = tempTime[0].trim();
                    minutes = tempTime[1].trim();
                    startTimePicker();
                    buttonPress = true;
                }
            }
        } catch (NullPointerException e) {
        }

        timeRangeOnOffModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timeRangeOn = true;
                    Toast.makeText(TimeSelectorActivity.this, "Time Range Mode ON", Toast.LENGTH_SHORT).show();
                    addEndTimeButton.setEnabled(true);
                    addEndTimeButton.setVisibility(View.VISIBLE);
                    EndTimeTextView.setVisibility(View.VISIBLE);
                } else {
                    timeRangeOn = false;
                    Toast.makeText(TimeSelectorActivity.this, "Time Range Mode OFF", Toast.LENGTH_SHORT).show();
                    addEndTimeButton.setEnabled(false);
                    addEndTimeButton.setVisibility(View.INVISIBLE);
                    EndTimeTextView.setVisibility(View.INVISIBLE);
                    addEndTimeButton.setText("");
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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (hourOfDay < 10) {
            hour = "0" + Integer.toString(hourOfDay);
        } else
            hour = Integer.toString(hourOfDay);

        if (minute < 10)
            minutes = "0" + Integer.toString(minute);
        else
            minutes = Integer.toString(minute);

        if (buttonPress) {
            if (hour == endHour && minutes == endMinute)
                unchangedWarningWindow(WarningDialog, 0);
            else {
                addBeginTimeButton.setText("Event activates at: " + hour + ":" + minutes);
                beginHour = hour;
                beginMinute = minutes;
            }

        } else {
            if (hour == beginHour && minutes == beginMinute)
                unchangedWarningWindow(WarningDialog, 0);
            else {
                addEndTimeButton.setText("Event ends at: " + hour + ":" + minutes);
                endHour = hour;
                endMinute = minutes;
            }
        }
    }


    public void startTimePicker() {
        DialogFragment timepicker = new TimePickerFragment();
        timepicker.show(getSupportFragmentManager(), "Time Selector");
    }


    private void checkResult() {

        if (editMode) {
            if (!timeRangeOn) {

                if (retrieveTime.equals(hour + ":" + minutes))
                    unchangedWarningWindow(WarningDialog, 2);
                if (!retrieveTime.equals(hour + ":" + minutes) && hour != null && minutes != null) {
                    getIntent(hour + ":" + minutes);
                    finish();
                }
                if(hour == null && minutes == null)
                {
                    getIntent(timeRangeDivider[0]);
                    finish();
                }

            } else {
                if (timeRangeDivider[0].trim().equals(beginHour + ":" + beginMinute) && timeRangeDivider[1].trim().equals(endHour + ":" + endMinute))
                    unchangedWarningWindow(WarningDialog, 1);
                if (!timeRangeDivider[0].trim().equals(beginHour + ":" + beginMinute) || !timeRangeDivider[1].trim().equals(endHour + ":" + endMinute)) {
                    getIntent(beginHour + ":" + beginMinute + "-" + endHour + ":" + endMinute);
                    finish();
                }
            }
        }

        if (!editMode) {
            if (!timeRangeOn) {
                if (addBeginTimeButton.getText().toString().contains("Add")) {
                    WarningDialog.setTitle("Warning");
                    WarningDialog.setMessage("No time selected, please try again.");
                    WarningDialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    WarningDialog.show();
                } else {
                    getIntent(hour + ":" + minutes);
                    finish();
                }
            }

            if (timeRangeOn) {
                if (addBeginTimeButton.getText().toString().contains("Add") || addEndTimeButton.getText().toString().contains("Add")) {
                    WarningDialog.setTitle("Warning");
                    WarningDialog.setMessage("Please add both start and end time to complete the process");
                    WarningDialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    WarningDialog.show();
                } else if (!addBeginTimeButton.getText().toString().contains("Add") && !addEndTimeButton.getText().toString().contains("Add")) {
                    getIntent(beginHour + ":" + beginMinute + "-" + endHour + ":" + endMinute);
                    finish();
                }
            }
        }
    }

    public Intent getIntent(String result) {
        Intent intent = new Intent();
        intent.putExtra("Time", result);
        setResult(Activity.RESULT_OK, intent);
        return intent;
    }

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

    public void unchangedWarningWindow(AlertDialog.Builder WarningDialog, int caseID) {
        switch (caseID) {
            case 0:
                WarningDialog.setMessage("The selected hour cannot be the same.");
                WarningDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                WarningDialog.show();
                break;
            case 1:
                WarningDialog.setMessage("The selected hour is unchanged, are you sure to continue the process?");
                WarningDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
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
            default:
        }
    }
}
