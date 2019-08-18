package com.ordinary.android.projectcache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SetupTriggerMethodWifiActivity extends AppCompatActivity {

    //Code for WIFISelectorActivity result request only
    private final int WIFI_PICKING_CODE = 1014;
    ArrayList<String> StoredWifi = new ArrayList<>();
    //User-Interface Code
    Button addWifiButton, completeButton;
    ListView selectedWIFIListView;
    Context wifi_picker_context;
    ArrayAdapter wifiLISTAdapterView;
    //Local Variables
    private WifiManager wifiManager;
    private int size = 0;
    private List<ScanResult> results;
    private ToolFunctions TF = new ToolFunctions();
    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            unregisterReceiver(wifiReceiver);

            for (ScanResult scanResult : results) {
                StoredWifi.add(scanResult.SSID);             //Saves into Scan Results
                System.out.println(scanResult.SSID);
                wifiLISTAdapterView.notifyDataSetChanged();
                TF.setListViewHeightBasedOnChildren(wifiLISTAdapterView, selectedWIFIListView);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_method_wifi);

        //User-Interface for Activity
        wifi_picker_context = SetupTriggerMethodWifiActivity.this;
        addWifiButton = findViewById(R.id.add_wifi);
        completeButton = findViewById(R.id.wifi_picker_activity_complete_button);
        selectedWIFIListView = findViewById(R.id.selected_wifi_list);


        selectedWIFIListView.setTextFilterEnabled(true);
//        adapterForTimeListView = new ArrayAdapter<String>(app_picker_context, R.layout.layout_app_list, R.id.condition_name, selectedAppArrList);

        wifiLISTAdapterView = new ArrayAdapter<String>(
                wifi_picker_context,
                R.layout.layout_general_list,
                R.id.general_list_textview_text,
                StoredWifi);

        selectedWIFIListView.setAdapter(wifiLISTAdapterView);
        registerForContextMenu(selectedWIFIListView);

        wifiManager = (WifiManager) wifi_picker_context.getSystemService(Context.WIFI_SERVICE);

        //Scan the WIFI-device currently
        //scanWifi();

        addWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Selecting WIFI");
                //Scan the WIFI-device currently
                scanWifi();
            }
        });

    }

    private void scanWifi() {
        StoredWifi.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(wifi_picker_context, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }


}
