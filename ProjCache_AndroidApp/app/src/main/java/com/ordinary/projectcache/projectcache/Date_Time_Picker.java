package com.ordinary.projectcache.projectcache;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.xml.transform.Templates;

public class Date_Time_Picker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button timeButton, dateButton, completeButton;
    private final int TIME_PICKING_CODE = 1013;
    Context date_time_picker_Context;
    private int Year, Month, day, selectedEditPosition;
    private String returnedTime = "";
    private boolean selectedHour, selectedDate, editMode;
    ListView timeListView;
    ArrayList<String> selectedTimeArrList = new ArrayList<>();
    ArrayAdapter<String> adapterFortimeListView;
    private ToolFunctions TF = new ToolFunctions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date__time__picker_activity);
        date_time_picker_Context = Date_Time_Picker.this;
        timeButton = (Button) findViewById(R.id.add_time);
        dateButton = (Button) findViewById(R.id.add_date);
        completeButton = (Button) findViewById(R.id.date_time_picker_activity_complete_button);


        timeListView = (ListView) findViewById(R.id.date_time_list);
        timeListView.setTextFilterEnabled(true);
        adapterFortimeListView = new ArrayAdapter<String>(Date_Time_Picker.this, R.layout.general_list_layout, R.id.condition_name, selectedTimeArrList);
        timeListView.setAdapter(adapterFortimeListView);
        registerForContextMenu(timeListView);


        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Date_Time_Picker.this, Time_Selector_Activity.class);
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
//                    Intent intent = new Intent();
//                    intent.putExtra("Time", hour + ":" + minutes);
//                    intent.putExtra("Date", Month + "/" + day + "/" + Year);
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();
                }
                catch (NullPointerException e)
                {}
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == TIME_PICKING_CODE && resultCode == Activity.RESULT_OK) {
                if(data.hasExtra("Time")) {
                    if(!editMode) {
                        returnedTime = data.getStringExtra("Time");
                        if(returnedTime.contains("-"))
                        {
                            selectedTimeArrList.add("- Event activate between: " + returnedTime);
                            adapterFortimeListView.notifyDataSetChanged();
                            TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
                        }
                        else {
                            selectedTimeArrList.add("- Event activate at: " + returnedTime);
                            adapterFortimeListView.notifyDataSetChanged();
                            TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
                        }
                    }
                    else
                    {
                        selectedTimeArrList.set(selectedEditPosition, "- Event activate at: " + data.getStringExtra("Time"));
                        editMode = false;
                        adapterFortimeListView.notifyDataSetChanged();
                    }
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
                Intent intent = new Intent(Date_Time_Picker.this, Time_Selector_Activity.class);
                intent.putExtra("RETRIEVE", returnedTime);
                startActivityForResult(intent, TIME_PICKING_CODE);
                editMode = true;
                selectedEditPosition = info.position;
                return true;

            case R.id.delete:
                Toast.makeText(date_time_picker_Context, "Delete", Toast.LENGTH_LONG).show();
                AlertDialog.Builder adb = new AlertDialog.Builder(date_time_picker_Context);
                adb.setTitle("Delete");
                adb.setNegativeButton("No no", null);
                adb.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectedTimeArrList.remove(info.position);
                        adapterFortimeListView.notifyDataSetChanged();
                        TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
                    }
                });
                adb.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}