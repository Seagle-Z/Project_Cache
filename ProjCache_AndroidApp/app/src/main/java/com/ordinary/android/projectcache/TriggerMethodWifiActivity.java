package com.ordinary.android.projectcache;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class TriggerMethodWifiActivity extends AppCompatActivity {

    //Code for WIFISelectorActivity result request only
    private final int WIFI_PICKING_CODE = 1014;

    Button addWifiButton, completeButton;
    ListView selectedWIFIListView;
    Context wifi_picker_context;
    ArrayAdapter wifiLISTAdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_method_wifi);

        //User-Interface for Activity
        addWifiButton  = findViewById(R.id.add_wifi);
        completeButton = findViewById(R.id.wifi_picker_activity_complete_button);
        selectedWIFIListView = findViewById(R.id.selected_wifi_list);


        selectedWIFIListView.setTextFilterEnabled(true);
//        adapterForTimeListView = new ArrayAdapter<String>(app_picker_context, R.layout.layout_app_list, R.id.condition_name, selectedAppArrList);

        wifiLISTAdapterView = new ArrayAdapter<String>(wifi_picker_context, selectedWIFIListView);
        selectedWIFIListView.setAdapter(wifiLISTAdapterView);
        registerForContextMenu(selectedWIFIListView);


        addWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Selecting WIFI");

            }
        });
    }
}
