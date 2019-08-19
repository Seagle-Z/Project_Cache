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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SetupEventConditionSelectionActivity extends AppCompatActivity {

    private final int REQUEST_TIME_INFORMATION_CODE = 1001;
    private final int REQUEST_LAUNCHING_APP_INFORMATION_CODE = 1002;
    private final int REQUEST_WIFI_INFORMATION_CODE = 1003;
    private Map<String, String> conditions = new Hashtable<>();

    private final String GPS = "GPS Location";
    private final String BT = "Bluetooth";
    private final String WIFI = "Wi-Fi Connection";
    private final String Time = "Time";
    private final String OS_APP = "On-Screen App";

    private ListView conditionListView;
    private List<String> conditionsArrList = new ArrayList<>();
    private ArrayAdapter<String> adapterForConditionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_method_selection);
        conditions.put("LOCATION", GPS);
        conditions.put("BLUETOOTH", BT);
        conditions.put("WIFI", WIFI);
        conditions.put("TIME", Time);
        conditions.put("ON_SCREEN_APP", OS_APP);

        conditionListView = (ListView) findViewById(R.id.condition_types);
        conditionListView.setTextFilterEnabled(true);

        conditionsArrList.add(conditions.get("LOCATION"));
        conditionsArrList.add(conditions.get("BLUETOOTH"));
        conditionsArrList.add(conditions.get("WIFI"));
        conditionsArrList.add(conditions.get("TIME"));
        conditionsArrList.add(conditions.get("ON_SCREEN_APP"));


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            for (String key : extras.keySet()) {
                for(int i = 0; i < conditionsArrList.size(); i++)
                {
                    if(conditions.get(key).equals(conditionsArrList.get(i))) {
                        conditionsArrList.remove(i);
                        break;
                    }
                }
            }
        }

        adapterForConditionListView = new ArrayAdapter<String>(
                this,
                R.layout.layout_general_list,
                R.id.general_list_textview_text,
                conditionsArrList);
        conditionListView.setAdapter(adapterForConditionListView);


        conditionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (conditionsArrList.get(position)) {
                    case GPS:
                        break;
                    case BT:
                        break;
                    case WIFI:
                        Intent WIFIMode = new Intent(SetupEventConditionSelectionActivity.this, SetupEventConditionWifiActivity.class);
                        startActivityForResult(WIFIMode, REQUEST_WIFI_INFORMATION_CODE);
                        break;
                    case Time:
                        Intent timeMode = new Intent(SetupEventConditionSelectionActivity.this, SetupEventConditionDateTimeActivity.class);
                        startActivityForResult(timeMode, REQUEST_TIME_INFORMATION_CODE);
                        break;
                    case OS_APP:
                        Intent AppLaunching = new Intent(SetupEventConditionSelectionActivity.this, SetupEventConditionOnScreenAppActivity.class);
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