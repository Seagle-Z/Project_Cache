package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TriggerMethodDateTimeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button timeButton, dateButton, completeButton;
    private final int TIME_PICKING_CODE = 1013;
    Context date_time_picker_Context;
    private int Year, Month, day, selectedEditPosition;
    private String returnedTime = "";
    private boolean editMode;
    ListView timeListView;
    ArrayList<String> selectedTimeArrList = new ArrayList<>();
    List<Boolean> activatedHours = new ArrayList<Boolean>(Arrays.asList(new Boolean[1440]));

    ArrayAdapter<String> adapterFortimeListView;
    private ToolFunctions TF = new ToolFunctions();
    private AlertDialog.Builder warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_method_date_time);
        date_time_picker_Context = TriggerMethodDateTimeActivity.this;
        timeButton = (Button) findViewById(R.id.add_time);
        dateButton = (Button) findViewById(R.id.add_date);
        completeButton = (Button) findViewById(R.id.date_time_picker_activity_complete_button);


        timeListView = (ListView) findViewById(R.id.date_time_list);
        timeListView.setTextFilterEnabled(true);
        adapterFortimeListView = new ArrayAdapter<String>(date_time_picker_Context, R.layout.layout_general_list, R.id.condition_name, selectedTimeArrList);
        timeListView.setAdapter(adapterFortimeListView);
        registerForContextMenu(timeListView);
        Collections.fill(activatedHours, Boolean.FALSE);

        warning = new AlertDialog.Builder(date_time_picker_Context);


        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(date_time_picker_Context, TimeSelectorActivity.class);
                startActivityForResult(intent, TIME_PICKING_CODE);
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "Date Selector");
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!selectedTimeArrList.isEmpty()) {
                        String[] result = parseResult(selectedTimeArrList);
                        String intentResult = "";
                        for (int i = 0; i < result.length; i++) {
                            if (i != result.length - 1)
                                intentResult = intentResult + result[i] + "#";
                            else
                                intentResult = intentResult + result[i];
                        }
                        Intent intent = new Intent();
                        intent.putExtra("Time", intentResult);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        Context context = TriggerMethodDateTimeActivity.this;
                        final AlertDialog.Builder WarningDialog = new AlertDialog.Builder(context);
                        WarningDialog.setTitle("Warning");
                        WarningDialog.setMessage("Please add at least one occurring time for the event");
                        WarningDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        WarningDialog.show();
                    }
                } catch (NullPointerException e) {
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == TIME_PICKING_CODE && resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("Time")) {
                    returnedTime = data.getStringExtra("Time");
                    // TODO: 2019-07-29 Merge time that is within the time range
                    isTimeInRange(returnedTime, activatedHours);
                    rebuildSelectedTimeArray();
                    adapterFortimeListView.notifyDataSetChanged();
                    TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
//                    if (!isTimeInRange(returnedTime, activatedHours)) {
//                        rebuildSelectedTimeArray();
//                        adapterFortimeListView.notifyDataSetChanged();
//                        TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
////                        if (!editMode) {
////                            if (returnedTime.contains("-")) {
////                                selectedTimeArrList.add("- Event activates between: " + returnedTime);
////                                //selectedTimeValue.add(returnedTime);
////                                adapterFortimeListView.notifyDataSetChanged();
////                                TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
////                            } else {
////                                selectedTimeArrList.add("- Event activates at: " + returnedTime);
////                                //selectedTimeValue.add(returnedTime);
////                                adapterFortimeListView.notifyDataSetChanged();
////                                TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
////                            }
////                        } else {
////                            if (returnedTime.contains("-")) {
////                                selectedTimeArrList.set(selectedEditPosition, "- Event activates between " + returnedTime);
////                                //selectedTimeValue.set(selectedEditPosition, returnedTime);
////                                editMode = false;
////                                adapterFortimeListView.notifyDataSetChanged();
////                            } else {
////                                selectedTimeArrList.set(selectedEditPosition, "- Event activates at: " + returnedTime);
////                                //selectedTimeValue.set(selectedEditPosition, returnedTime);
////                                editMode = false;
////                                adapterFortimeListView.notifyDataSetChanged();
////                            }
////                        }
//
//                    } else {
//                        rebuildSelectedTimeArray();
//                        adapterFortimeListView.notifyDataSetChanged();
//                        TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
//                    }
                }
            }
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Year = year;
        Month = month;
        day = dayOfMonth;
        dateButton.setText("Tigger Date: " + Month + "/" + day + "/" + Year);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.popup_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(date_time_picker_Context, "Edit", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(TriggerMethodDateTimeActivity.this, TimeSelectorActivity.class);
                intent.putExtra("RETRIEVE", returnedTime);
                startActivityForResult(intent, TIME_PICKING_CODE);
                editMode = true;
                selectedEditPosition = info.position;
                return true;

            case R.id.delete:
                warning.setTitle("Delete");
                warning.setNegativeButton("No no", null);
                warning.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(date_time_picker_Context, "Deleting", Toast.LENGTH_SHORT).show();
                        selectedTimeArrList.remove(info.position);
                        //selectedTimeValue.remove(info.position);
                        adapterFortimeListView.notifyDataSetChanged();
                        TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
                        Toast.makeText(date_time_picker_Context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                warning.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public String[] parseResult(ArrayList<String> timeList) {
        String[] result = new String[timeList.size()];
        String s = "";
        for (int i = 0; i < result.length; i++) {
            s = timeList.get(i);
            if (timeList.get(i).contains("- Event activates between: ")) {
                s = s.replace("- Event activates between: ", "");
            } else if (s.contains("- Event activates at: ")) {
                s = s.replace("- Event activates at: ", "");
            }
            result[i] = s;
        }
        return result;
    }

    public boolean isTimeInRange(String s, List<Boolean> activatedHours) {
        if (!s.contains("-")) {
            String[] timeSpliter = s.split(":");
            int hour = Integer.parseInt(timeSpliter[0]);
            int min = Integer.parseInt(timeSpliter[1]);
            int timeSlot = hour * 60 + min;
            if (activatedHours.get(timeSlot) == true)
                return true;
            else {
                activatedHours.set(timeSlot,true);
                return false;
            }
        } else {
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

            //Scenario 1: if startTime is already in a range, but end time is out of range.
            // E.g. Known 10AM-12PM, new time 11:30AM-01:00PM
            if (activatedHours.get(timeSlot1)) {
                if (activatedHours.get(timeSlot2)) {
                    return true;
                } else {
                    setTimeRangeTrue(timeSlot1, timeSlot2);
                    return true;
                }
            }
            //Scenario 2: if startTime is not in a range, but end time is.
            //E.g. Known 10AM-12PM, new time 9AM-10:30
            else if (!activatedHours.get(timeSlot1) && activatedHours.get(timeSlot2)) {
                setTimeRangeTrue(timeSlot1, timeSlot2);
                return true;
            }

            //Sencario 3: if both start time and end time is not any time range
            //E.g. 10AM-12PM, new time 1PM-3PM
            else if (!activatedHours.get(timeSlot1) && !activatedHours.get(timeSlot2)) {
                setTimeRangeTrue(timeSlot1, timeSlot2);
                return false;
            }
        }
        return false;
    }

    public void setTimeRangeTrue(int timeSlot1, int timeSlot2) {
        for (int i = timeSlot1; i <= timeSlot2; i++) {
            activatedHours.set(i, true);
        }
    }

    public void rebuildSelectedTimeArray() {
        selectedTimeArrList.clear();
        int startTime = 0;
        int endTime = 0;
        int j = 0;
        for (int i = 0; i < activatedHours.size(); i++) {
            for (j = i + 1; j < activatedHours.size(); j++) {
                if (activatedHours.get(i) && !activatedHours.get(j)) {
                    selectedTimeArrList.add("- Event activates at: " + i / 60 + ":" + i % 60);
                    i = j;
                    break;
                }
                if (activatedHours.get(i) && activatedHours.get(j)) {
                    startTime = i;
                    endTime = j;
                    if (j == activatedHours.size() - 1 || !activatedHours.get(j + 1)) {
                        selectedTimeArrList.add("- Event activates between: " + startTime / 60 + ":" + startTime % 60 + "-" + endTime / 60 + ":" + endTime % 60);
                        i = j;
                        startTime = 0;
                        endTime = 0;
                        break;
                    }
                }
            }
        }
    }
}

//    public boolean checkduplicate(String s, ArrayList<String> selectedTimeValue) {
//        if (selectedTimeValue.isEmpty())
//            return false;
//        else {
//            for (String string : selectedTimeValue) {
//                if (s.equals(string))
//                    return true;
//            }
//        }
//        return false;
//    }

//                        }else {
//                        warning.setTitle("Reminder");
//                        //warning.setMessage("The selected hour is in between time range " + selectedTimeValue.get(selectedTimeValueArrayPosition) + ", program automatically merged it for you");
//                        warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                        warning.show();
//                    } else {
//                        warning.setTitle("");
//                        warning.setMessage("The selected hour is already existed on list.");
//                        warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                        warning.show();

//10:23 - 10:40
//0         1        ...... 10*60+23
//1376/60 == 22  -> 1376-
//"1376/60(小时)" + ":" + "1376%60"(分钟)