package com.ordinary.projectcache.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class condition_list_activity extends AppCompatActivity {


    ListView conditionListView;
    ArrayList<String> conditionsArrList = new ArrayList<>();
    ArrayAdapter<String> adapterForConditionListView;
    private String condition_Type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.condition_list_activity);

        conditionListView = (ListView) findViewById(R.id.condition_types);
        conditionListView.setTextFilterEnabled(true);

        conditionsArrList.add("GPS Location");
        conditionsArrList.add("Bluetooth");
        conditionsArrList.add("WIFI");
        conditionsArrList.add("Time");
        conditionsArrList.add("When App Launching");

        adapterForConditionListView = new ArrayAdapter<String>(this, R.layout.condition_options_layout, R.id.condition_type, conditionsArrList);
        conditionListView.setAdapter(adapterForConditionListView);

        conditionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        condition_Type = "GPS";
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        condition_Type = "Time";
                        Intent intent = new Intent(condition_list_activity.this, Date_Time_Picker.class);
                        startActivityForResult(intent, 10);
                        break;
                    case 4:
                        condition_Type = "WAL";
                        break;
                    default:
                        Log.d("","No Item selected");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (condition_Type.equals("Time")) {
            try {
                //data.putExtra("Time", "");
                data.putExtra("GivenTime", "");
                //System.out.println(data.getStringExtra("Time"));
            } catch (NullPointerException e) {
            }
        }
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
