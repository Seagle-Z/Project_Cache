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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TriggerMethodDataTimeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button timeButton, dateButton, completeButton;
    private final int TIME_PICKING_CODE = 1013;
    Context date_time_picker_Context;
    private int Year, Month, day, selectedEditPosition;
    private String returnedTime = "";
    private boolean editMode;
    ListView timeListView;
    ArrayList<String> selectedTimeArrList = new ArrayList<>();
    ArrayAdapter<String> adapterFortimeListView;
    private ToolFunctions TF = new ToolFunctions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date__time__picker_activity);
        date_time_picker_Context = TriggerMethodDataTimeActivity.this;
        timeButton = (Button) findViewById(R.id.add_time);
        dateButton = (Button) findViewById(R.id.add_date);
        completeButton = (Button) findViewById(R.id.date_time_picker_activity_complete_button);


        timeListView = (ListView) findViewById(R.id.date_time_list);
        timeListView.setTextFilterEnabled(true);
        adapterFortimeListView = new ArrayAdapter<String>(date_time_picker_Context, R.layout.general_list_layout, R.id.condition_name, selectedTimeArrList);
        timeListView.setAdapter(adapterFortimeListView);
        registerForContextMenu(timeListView);


        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(date_time_picker_Context, TimeSelectorActivity.class);
                startActivityForResult(intent, TIME_PICKING_CODE);
                timeButton.setEnabled(false);
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
                        Context context = TriggerMethodDataTimeActivity.this;
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
                    if (!editMode) {
                        returnedTime = data.getStringExtra("Time");
                        if (returnedTime.contains("-")) {
                            selectedTimeArrList.add("- Event activates between: " + returnedTime);
                            adapterFortimeListView.notifyDataSetChanged();
                            TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
                        } else {
                            selectedTimeArrList.add("- Event activates at: " + returnedTime);
                            adapterFortimeListView.notifyDataSetChanged();
                            TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
                        }
                    } else {
                        selectedTimeArrList.set(selectedEditPosition, "- Event activates at: " + data.getStringExtra("Time"));
                        editMode = false;
                        adapterFortimeListView.notifyDataSetChanged();
                    }
                }
            }
            timeButton.setEnabled(true);
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
                Intent intent = new Intent(TriggerMethodDataTimeActivity.this, TimeSelectorActivity.class);
                intent.putExtra("RETRIEVE", returnedTime);
                startActivityForResult(intent, TIME_PICKING_CODE);
                editMode = true;
                selectedEditPosition = info.position;
                return true;

            case R.id.delete:
                AlertDialog.Builder adb = new AlertDialog.Builder(date_time_picker_Context);
                adb.setTitle("Delete");
                adb.setNegativeButton("No no", null);
                adb.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(date_time_picker_Context, "Deleting", Toast.LENGTH_SHORT).show();
                        selectedTimeArrList.remove(info.position);
                        adapterFortimeListView.notifyDataSetChanged();
                        TF.setListViewHeightBasedOnChildren(adapterFortimeListView, timeListView);
                        Toast.makeText(date_time_picker_Context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                adb.show();
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
}