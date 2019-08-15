package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TriggerMethodSelectionActivity extends AppCompatActivity {

    private final int REQUEST_TIME_INFORMATION_CODE = 1001;
    private final int REQUEST_LAUNCHING_APP_INFORMATION_CODE = 1002;
    private final int REQUEST_WIFI_INFORMATION_CODE = 1003;
    // TODO: 2019-08-14 add string constants
    private final String GPS = "GPS Location";
    private final String BT = "Bluetooth";
    private final String WIFI = "WIFI";
    private final String Time = "Time";
    private final String OS_APP = "On-Screen App";
    ListView conditionListView;
    ArrayList<String> conditionsArrList = new ArrayList<>();
    ArrayAdapter<String> adapterForConditionListView;
    private int conditionTypeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_method_selection);

        conditionListView = (ListView) findViewById(R.id.condition_types);
        conditionListView.setTextFilterEnabled(true);

        conditionsArrList.add(GPS);
        conditionsArrList.add(BT);
        conditionsArrList.add(WIFI);
        conditionsArrList.add(Time);
        conditionsArrList.add(OS_APP);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            for (String key : extras.keySet()) {
                for (int i = 0; i < conditionsArrList.size(); i++) {
                    if (key.equals(conditionsArrList.get(i))) {
                        conditionsArrList.remove(i);
                        break;
                    }
                }
            }
        }


        adapterForConditionListView = new ArrayAdapter<String>(
                this,
                R.layout.layout_general_list,
                R.id.condition_name,
                conditionsArrList);
        conditionListView.setAdapter(adapterForConditionListView);


        conditionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (conditionsArrList.get(position)) {
                    case GPS:
                        conditionTypeID = 1;
                        break;
                    case BT:
                        break;
                    case WIFI:
                        conditionTypeID = 2;
                        Intent WIFIMode = new Intent(TriggerMethodSelectionActivity.this, TriggerMethodWifiActivity.class);
                        startActivityForResult(WIFIMode, REQUEST_WIFI_INFORMATION_CODE);
                        break;
                    case Time:
                        conditionTypeID = 3;
                        Intent timeMode = new Intent(TriggerMethodSelectionActivity.this, TriggerMethodDateTimeActivity.class);
                        startActivityForResult(timeMode, REQUEST_TIME_INFORMATION_CODE);
                        break;
                    case OS_APP:
                        conditionTypeID = 4;
                        Intent AppLaunching = new Intent(TriggerMethodSelectionActivity.this, TriggerMethodAppLaunchActivity.class);
                        startActivityForResult(AppLaunching, REQUEST_LAUNCHING_APP_INFORMATION_CODE);
                        break;
                    default:
                        Log.d("", "No Item selected");
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
