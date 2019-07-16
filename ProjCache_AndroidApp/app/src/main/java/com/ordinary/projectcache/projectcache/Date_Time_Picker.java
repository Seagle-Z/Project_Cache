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

public class Date_Time_Picker extends AppCompatActivity implements TrigConditionInit, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    Button time, date, complete;

    private int hour, minutes, Year, Month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date__time__picker_activity);

        time = (Button) findViewById(R.id.add_time);
        date = (Button) findViewById(R.id.add_date);
        complete = (Button) findViewById(R.id.date_time_complete);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(), "Time Selector");
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "Date Selector");
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.putExtra("Time", getTriggerValue());
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
    public String getTriggerMethod() {
        return null;
    }

    @Override
    public String getTriggerValue() {
        return hour + ":" + minutes;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hour = hourOfDay;
        minutes = minute;
        time.setText("Tigger time: " + getTriggerValue());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Year = year;
        Month = month;
        day = dayOfMonth;
        date.setText("Tigger Date: " + Month + "/" + day + "/" + Year);
    }
}
