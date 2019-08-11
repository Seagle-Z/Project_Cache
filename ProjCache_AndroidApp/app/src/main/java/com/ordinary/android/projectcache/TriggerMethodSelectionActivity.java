package com.ordinary.android.projectcache;

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

public class TriggerMethodSelectionActivity extends AppCompatActivity {

    ListView conditionListView;
    ArrayList<String> conditionsArrList = new ArrayList<>();
    ArrayAdapter<String> adapterForConditionListView;
    private int conditionTypeID;
    private final int REQUEST_TIME_INFORMATION_CODE = 1001;
    private final int REQUEST_LAUNCHING_APP_INFORMATION_CODE = 1002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_method_selection);

        conditionListView = (ListView) findViewById(R.id.condition_types);
        conditionListView.setTextFilterEnabled(true);

        conditionsArrList.add("GPS Location");
        conditionsArrList.add("Bluetooth");
        conditionsArrList.add("WIFI");
        conditionsArrList.add("Time");
        conditionsArrList.add("When App Launching");

        adapterForConditionListView = new ArrayAdapter<String>(this, R.layout.layout_general_list, R.id.condition_name, conditionsArrList);
        conditionListView.setAdapter(adapterForConditionListView);

        conditionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        conditionTypeID = 1;
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        conditionTypeID = 3;
                        Intent timeMode = new Intent(TriggerMethodSelectionActivity.this, TriggerMethodDateTimeActivity.class);
                        startActivityForResult(timeMode, REQUEST_TIME_INFORMATION_CODE);
                        break;
                    case 4:
                        conditionTypeID = 4;
                        Intent AppLaunching  = new Intent(TriggerMethodSelectionActivity.this, TriggerMethodAppLaunchActivity.class);
                        startActivityForResult(AppLaunching, REQUEST_LAUNCHING_APP_INFORMATION_CODE);
                        break;
                    default:
                        Log.d("","No Item selected");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
