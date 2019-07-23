package com.ordinary.projectcache.projectcache;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class Date_Time_Picker extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    Button timeButton, dateButton, completeButton;

    private int hour, minutes, Year, Month, day;
    private boolean selectedHour, selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date__time__picker_activity);

        timeButton = (Button) findViewById(R.id.add_time);
        dateButton = (Button) findViewById(R.id.add_date);
        completeButton = (Button) findViewById(R.id.date_time_complete);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(), "Time Selector");
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
                    Intent intent = new Intent();
                    intent.putExtra("Time", hour + ":" + minutes);
                    intent.putExtra("Date", Month + "/" + day + "/" + Year);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                catch (NullPointerException e)
                {}
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hour = hourOfDay;
        minutes = minute;
        timeButton.setText("Tigger timeButton: " + hour + ":" + minutes);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Year = year;
        Month = month;
        day = dayOfMonth;
        dateButton.setText("Tigger Date: " + Month + "/" + day + "/" + Year);
    }
}
