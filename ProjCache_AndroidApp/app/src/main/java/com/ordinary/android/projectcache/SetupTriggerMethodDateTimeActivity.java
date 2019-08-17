package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetupTriggerMethodDateTimeActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    //Code for TimeSelectorActivity result request only
    private final int TIME_PICKING_CODE = 1013;

    /*timeButton: activates the timeSelectorActivity once it's clicked
      dateButton: open the DateOcikerDialog
      completeButton: return all selected time value
      */
    Button timeButton, dateButton, completeButton;

    Context date_time_picker_Context; //Current activity's context
    ListView timeListView; //ListView for viewing selected time

    //Store selected time for ListView use
    ArrayList<String> selectedTimeArrList = new ArrayList<>();
    //To store the actual return value that later will pass back to EventSetupPage1Fragment.
    ArrayList<String> selectedTimeValue = new ArrayList<>();
    //Used to activated time based on the time calculation
    List<Boolean> activatedHours = new ArrayList<Boolean>(Arrays.asList(new Boolean[1440]));
    ArrayAdapter<String> adapterForTimeListView; //Adapter for ListView use only

    private int Year, Month, day, selectedEditPosition; //Variable for date information
    private String returnedTime = null; //Variable to store return result from TimeSelectorActivity
    private boolean editMode; //Check if it's in editing mode.
    private String retrieveTime = null; //To store original editing value

    //Object use for Access ToolFunction class' methods.
    private ToolFunctions TF = new ToolFunctions();
    private AlertDialog.Builder warning; //For displaying warning messages

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_method_date_time);
        date_time_picker_Context = SetupTriggerMethodDateTimeActivity.this;
        timeButton = findViewById(R.id.add_time);
        dateButton = findViewById(R.id.add_date);
        completeButton = findViewById(R.id.date_time_picker_activity_complete_button);


        timeListView = findViewById(R.id.date_time_list);
        timeListView.setTextFilterEnabled(true);

        //Initialize an adapter to inflate the ListView later
        adapterForTimeListView =
                new ArrayAdapter<String>(
                        date_time_picker_Context,
                        R.layout.layout_general_list,
                        R.id.condition_name,
                        selectedTimeArrList
                );

        timeListView.setAdapter(adapterForTimeListView);
        registerForContextMenu(timeListView);
        Collections.fill(activatedHours, Boolean.FALSE);

        warning = new AlertDialog.Builder(date_time_picker_Context);


        //Check if the class is called from the edit feature.
        Intent intent = getIntent();
        try {
            if (intent.hasExtra("RETRIEVE")) {
                parseRetrievalData(intent);
            }
        } catch (NullPointerException e) {
        }


        //Calls the TimeSelectorActivity class to pick time
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(date_time_picker_Context, TimeSelectorActivity.class);
                startActivityForResult(intent, TIME_PICKING_CODE);
                editMode = false;
            }
        });

        //Display date picker dialog for picking date
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "Date Selector");
                editMode = false;
            }
        });

        //Return result back to the EventSetupPage1Fragment
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //check if there any time value
                    if (!selectedTimeValue.isEmpty()) {
                        String intentResult = "";
                        for (int i = 0; i < selectedTimeValue.size(); i++) {
                            if (i != selectedTimeValue.size() - 1)
                                intentResult = intentResult + selectedTimeValue.get(i) + "#";
                            else
                                intentResult = intentResult + selectedTimeValue.get(i);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("Time", intentResult);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    //If there is no time selected, then give warning.
                    else {
                        AlertDialog.Builder adb = new AlertDialog.Builder(date_time_picker_Context);
                        adb.setTitle("Warning");
                        adb.setMessage(
                                "Please add at least one occurring time for the event"
                        );
                        adb.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        adb.show();
                    }
                } catch (NullPointerException e) {
                }
            }
        });
    }

    //Received result from TimeSelectorActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == TIME_PICKING_CODE && resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("Time")) {
                    //Store ReturnTime in a local string variable
                    returnedTime = data.getStringExtra("Time");
                    /*check if there is any existing time range that the current selected time can
                     can be merged in. For instance, 9:45am can be merge in 9:30am-10:00am.
                    */
                    isTimeInRange(returnedTime, activatedHours);

                    //refresh the time list
                    rebuildSelectedTimeArray();
                    adapterForTimeListView.notifyDataSetChanged();
                    TF.setListViewHeightBasedOnChildren(adapterForTimeListView, timeListView);
                }
            }
        } catch (NullPointerException e) {
        }
    }


    //For date information.
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Year = year;
        Month = month;
        day = dayOfMonth;
        dateButton.setText("Tigger Date: " + Month + "/" + day + "/" + Year);
    }

    //For small long press popup window.
    @Override
    public void onCreateContextMenu(
            ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.popup_menu, menu);
    }

    //Popup menu clicking listener.
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case R.id.edit:
                Toast.makeText(
                        date_time_picker_Context,
                        "Edit",
                        Toast.LENGTH_LONG).show();
                Intent intent =
                        new Intent(
                                SetupTriggerMethodDateTimeActivity.this,
                                TimeSelectorActivity.class
                        );

                //Pack the value that selected from the list and send to TimeSelectorActivity
                intent.putExtra("RETRIEVE", selectedTimeValue.get(info.position));
                selectedEditPosition = info.position;
                startActivityForResult(intent, TIME_PICKING_CODE);
                editMode = true;
                return true;

            case R.id.delete:
                AlertDialog.Builder deleter = new AlertDialog.Builder(date_time_picker_Context);
                deleter.setTitle("Delete");
                deleter.setNegativeButton("No no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(
                                date_time_picker_Context,
                                "Cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                //Delete what the user selected
                deleter.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(
                                date_time_picker_Context,
                                "Deleting",
                                Toast.LENGTH_SHORT).show();

                        updateActivatedHourList(selectedTimeValue.get(info.position), false);
                        rebuildSelectedTimeArray();
                        adapterForTimeListView.notifyDataSetChanged();
                        TF.setListViewHeightBasedOnChildren(adapterForTimeListView, timeListView);
                        Toast.makeText(
                                date_time_picker_Context,
                                "Deleted",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                warning.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void parseRetrievalData(Intent intent)
    {
        retrieveTime = intent.getStringExtra("RETRIEVE");
        editMode = true;
        String[] timeRangeDivider = retrieveTime.split("#");
        for(String s : timeRangeDivider)
        {
            updateActivatedHourList(s, true);
            selectedTimeValue.add(s);
        }
        rebuildSelectedTimeArray();
        adapterForTimeListView.notifyDataSetChanged();
        TF.setListViewHeightBasedOnChildren(adapterForTimeListView, timeListView);
    }
    //Auto-detect if the new selected hour can be merge in any time slot
    public boolean isTimeInRange(String s, List<Boolean> activatedHours) {
        if(editMode)
        {
            selectedTimeArrList.remove(selectedEditPosition);
            updateActivatedHourList(selectedTimeValue.get(selectedEditPosition), false);
            selectedTimeValue.remove(selectedEditPosition);
        }
        //first check if the time is just single time
        if (!s.contains("-")) {
            String[] timeSpliter = s.split(":");
            int hour = Integer.parseInt(timeSpliter[0]);
            int min = Integer.parseInt(timeSpliter[1]);

            //convert the time into an integer
            int timeSlot = hour * 60 + min;
            //Check the boolean list at the index see if it's already on
            if (activatedHours.get(timeSlot) == true) {
                warning.setTitle("Warning");
                warning.setMessage("Selected hour is already exists on the list");
                warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                warning.show();
                return true;
            }
            //If the hour does not exist, then add to the list.
            else {
                activatedHours.set(timeSlot, true);
                if (activatedHours.get(timeSlot - 1) || activatedHours.get(timeSlot + 1)) {
                    warning.setTitle("Warning");
                    warning.setMessage("The selected hour is able to merge with other time range, " +
                            "program automatically merged for you.");
                    warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    warning.show();
                }
                return false;
            }
        }// End of single hour checking

        // If the time has time range
        else {
            String[] timeRangeDivider = s.split("-");
            String tempBeginTime = timeRangeDivider[0].trim();
            String tempEndTime = timeRangeDivider[1].trim();
            String[] _tempBeginHour = tempBeginTime.split(":");
            String[] _tempEndhour = tempEndTime.split(":");

            /*Similar to the single hour time slot calculation, but here do twice for both begin and
              end time
             */
            int beginHour = Integer.parseInt(_tempBeginHour[0].trim());
            int beginMinute = Integer.parseInt(_tempBeginHour[1].trim());


            int endHour = Integer.parseInt(_tempEndhour[0].trim());
            int endMinute = Integer.parseInt(_tempEndhour[1].trim());

            int timeSlot1 = beginHour * 60 + beginMinute;
            int timeSlot2 = endHour * 60 + endMinute;

            //Scenario 1: if startTime is already in a range, but end time is out of range.
            // E.g. Known 10AM-12PM, new time 11:30AM-01:00PM
            if (activatedHours.get(timeSlot1)) {
                if (activatedHours.get(timeSlot2)) {
                    warning.setTitle("Reminder");
                    warning.setMessage(
                            "The selected hour is in one of the time range," +
                                    " program automatically merged for you.");
                    warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    warning.show();
                    return true;
                } else {
                    warning.setTitle("Reminder");
                    warning.setMessage(
                            "The selected end hour is overlapping with other time range," +
                                    " program automatically merged for you.");
                    warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    warning.show();
                    setTimeRangeBoolean(timeSlot1, timeSlot2, true);
                    return true;
                }
            }
            //Scenario 2: if startTime is not in a range, but end time is.
            //E.g. Known 10AM-12PM, new time 9AM-10:30
            else if (!activatedHours.get(timeSlot1) && activatedHours.get(timeSlot2)) {
                warning.setTitle("Reminder");
                warning.setMessage(
                        "The selected start hour is overlapping with other time range," +
                                " program automatically merged for you.");
                warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                setTimeRangeBoolean(timeSlot1, timeSlot2, true);
                return true;
            }

            //Sencario 3: if both start time and end time is not any time range
            //E.g. 10AM-12PM, new time 1PM-3PM
            else if (!activatedHours.get(timeSlot1) && !activatedHours.get(timeSlot2)) {
                setTimeRangeBoolean(timeSlot1, timeSlot2, true);
                Toast.makeText(date_time_picker_Context,
                        "Added Successfully",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

    //update the activatedHour boolean list for new time
    public void setTimeRangeBoolean(int timeSlot1, int timeSlot2, boolean flag) {
        for (int i = timeSlot1; i <= timeSlot2; i++) {
            activatedHours.set(i, flag);
        }
    }

    //Refresh the displaying list to match with the latest changes.
    public void rebuildSelectedTimeArray() {
        selectedTimeArrList.clear();
        selectedTimeValue.clear();
        //Only use for time range
        int startTime = 0;
        int endTime = 0;
        int j = 0;

        //Use two "pointers" to check the array
        for (int i = 0; i < activatedHours.size(); i++) {
            for (j = i + 1; j < activatedHours.size(); j++) {

                //If the i is true and j is false, then we know it's only a single time
                if (activatedHours.get(i) && !activatedHours.get(j)) {
                    String hour, min;

                    /*Check the syntax to determine if need to add 0 before values, check function
                      for more details
                    */
                    hour = checkHour(i);
                    min = checkMin(i);

                    selectedTimeArrList.add("- Event activates at: " + hour + ":" + min);
                    selectedTimeValue.add(i / 60 + ":" + i % 60);
                    i = j; //Update the "pointer" to the last check position
                    break;
                }

                /*If the current position i is true, and then next one is true, then continue
                  looping until finding the next false position. Then conclude the start and end
                  time.
                */
                if (activatedHours.get(i) && activatedHours.get(j)) {
                    startTime = i;
                    endTime = j;
                    if (j == activatedHours.size() - 1 || !activatedHours.get(j + 1)) {
                        String beginHr, beginMin, endHr, endMin;

                        /*Check the syntax to determine if need to add 0 before values, check
                        function for more details*/
                        beginHr = checkHour(startTime);
                        beginMin = checkMin(startTime);
                        endHr = checkHour(endTime);
                        endMin = checkMin(endTime);

                        selectedTimeArrList.add(
                                "- Event activates between: " +
                                        beginHr + ":" +
                                        beginMin + "-" +
                                        endHr + ":" +
                                        endMin
                        );
                        selectedTimeValue.add(startTime / 60 + ":" +
                                startTime % 60 + "-" +
                                endTime / 60 + ":" +
                                endTime % 60);
                        i = j; //Update the "pointer" to the last check position
                        startTime = 0;
                        endTime = 0;
                        break;
                    }
                }
            }
        }
    }

    public void updateActivatedHourList(String s, boolean flag) {

        if (s.contains("-")) {
            String[] timeRangeDivider = s.split("-");
            String tempBeginTime = timeRangeDivider[0].trim();
            String tempEndTime = timeRangeDivider[1].trim();
            String[] _tempBeginHour = tempBeginTime.split(":");
            String[] _tempEndhour = tempEndTime.split(":");
            int beginHour = Integer.parseInt(_tempBeginHour[0].trim());
            int beginMinute = Integer.parseInt(_tempBeginHour[1].trim());
            int timeSlot1 = beginHour * 60 + beginMinute;
            int endHour = Integer.parseInt(_tempEndhour[0].trim());
            int endMinute = Integer.parseInt(_tempEndhour[1].trim());
            int timeSlot2 = endHour * 60 + endMinute;

            setTimeRangeBoolean(timeSlot1, timeSlot2, flag);
        } else {
            String[] time = s.split(":");
            int timeslot = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]) % 60;
            activatedHours.set(timeslot, flag);
        }
    }

    /*This function check if the selected hour value is less than 10, if so, then display a 0 before
      the hour. E.g 9:15 -> 09:15
     */
    public String checkHour(int timeValue) {
        String hour = null;
        if (timeValue / 60 < 10)
            hour = "0" + timeValue / 60;
        else
            hour = Integer.toString(timeValue / 60);

        return hour;
    }

    /*Check if the selected minute value is less than 10, if so, then display a 0 before
      the min. E.g 10:1 -> 10:01
     */
    public String checkMin(int timeValue) {
        String min = null;
        if (timeValue % 60 < 10)
            min = "0" + timeValue % 60;
        else
            min = Integer.toString(timeValue % 60);

        return min;
    }

}