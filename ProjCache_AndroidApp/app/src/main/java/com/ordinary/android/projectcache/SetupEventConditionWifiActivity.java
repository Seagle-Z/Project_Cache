package com.ordinary.android.projectcache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetupEventConditionWifiActivity extends AppCompatActivity {

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
                System.out.println("SSID: " + scanResult.SSID + "       MAC: " + scanResult.BSSID);
                wifiLISTAdapterView.notifyDataSetChanged();
                TF.setListViewHeightBasedOnChildren(wifiLISTAdapterView, selectedWIFIListView);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_condition_wifi);

        //User-Interface for Activity
        wifi_picker_context = SetupEventConditionWifiActivity.this;
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

                /*
                //CURRENT WIFI (MOVE THIS)
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                System.out.println("CURRENTLY CONNECTED: " + wifiInfo.getSSID() + "     MAC: " + getMacAddr());

                //PREVIOUSLY SAVED (MOVE THIS)
                List<WifiConfiguration> currentNetworks = wifiManager.getConfiguredNetworks();
                System.out.println("Previously Saved WIFI");
                for (WifiConfiguration test : currentNetworks) {
                    System.out.println("SSID: " + test.SSID);
                }

                System.out.println("Selecting WIFI");
                //Scan the WIFI-device currently
                scanWifi();
                */

                Intent intent = new Intent(wifi_picker_context, WIFISelectorActivity.class);
                startActivityForResult(intent, WIFI_PICKING_CODE);

            }
        });






    }

    private void scanWifi() {
        StoredWifi.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(wifi_picker_context, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }


    public String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }

}
