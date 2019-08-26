package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//import android.content.BroadcastReceiver;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.net.wifi.WifiConfiguration;
//import android.net.wifi.WifiInfo;
//import java.util.Collections;
//import java.net.NetworkInterface;

public class SetupEventConditionWifiActivity extends AppCompatActivity {

    //Code for WIFISelectorActivity result request only
    private final int WIFI_PICKING_CODE = 1014;
    //ArrayList<String> StoredWifi = new ArrayList<>();

    //User-Interface Code
    Button addWifiButton, completeButton;
    ListView selectedWIFIListView;
    Context wifi_picker_context;
    ArrayAdapter wifiLISTAdapterView;
    //Local Variables
    private ToolFunctions TF = new ToolFunctions();
    private WIFIInfoModel returnedWifi;
    private List<String> selectedWifiArrList = new ArrayList<String>();     //Ex) {UIC-WIFI, Guest, MyHome}
    private List<String> encryptedSSIDArrList = new ArrayList<String>();    //Ex) {91-24-123#43-35-62}
    private boolean editMode;
    private int selectedEditPosition;
    private AlertDialog.Builder warning;
    private String retrieveWIFIList;
    private WifiManager wifiManager;
    //private int size = 0;
    //private List<ScanResult> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_condition_wifi);

        //User-Interface for Activity
        wifi_picker_context = SetupEventConditionWifiActivity.this;
        addWifiButton = findViewById(R.id.add_wifi);
        completeButton = findViewById(R.id.wifi_picker_activity_complete_button);
        selectedWIFIListView = findViewById(R.id.selected_wifi_list);
        warning = new AlertDialog.Builder(wifi_picker_context);

        selectedWIFIListView.setTextFilterEnabled(true);

        wifiLISTAdapterView = new ArrayAdapter<String>(
                wifi_picker_context,
                R.layout.layout_general_list,
                R.id.general_list_textview_text,
                selectedWifiArrList);

        selectedWIFIListView.setAdapter(wifiLISTAdapterView);
        registerForContextMenu(selectedWIFIListView);

        wifiManager = (WifiManager) wifi_picker_context.getSystemService(Context.WIFI_SERVICE);

        addWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMode = false;

                Intent intent = new Intent(wifi_picker_context, WIFISelectorActivity.class);
                startActivityForResult(intent, WIFI_PICKING_CODE);

            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!selectedWifiArrList.isEmpty()) {
                        String intentResult = "";

                        for (int i = 0; i < selectedWifiArrList.size(); i++) {                               //Unencoded List   Ex) selectedAppArrList = {UIC, Guest, DeVilla, ..}

                            // Encoding the String
                            int tempEncoded[] = TF.textEncoder(selectedWifiArrList.get(i));
                            String tempString = "";

                            //Padding the string with '-' between each number
                            for (int j = 0; j < tempEncoded.length; j++) {
                                if (j != tempEncoded.length - 1)
                                    tempString = tempString + tempEncoded[j] + "-";
                                else
                                    tempString = tempString + tempEncoded[j];
                            }
                            encryptedSSIDArrList.add(tempString);
                        }

                        //Append everything in the encryptedSSIDArrList into intentResult string;
                        for (int k = 0; k < encryptedSSIDArrList.size(); k++) {
                            if (k != encryptedSSIDArrList.size() - 1)
                                intentResult = intentResult + encryptedSSIDArrList.get(k) + "#";
                            else
                                intentResult = intentResult + encryptedSSIDArrList.get(k);
                        }

                        Intent intent = new Intent();
                        intent.putExtra("Wifi", intentResult);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        AlertDialog.Builder adb = new AlertDialog.Builder(wifi_picker_context);
                        adb.setTitle("Warning");
                        adb.setMessage(
                                "Please add at least one WIFI for the event"
                        );
                        adb.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        adb.show();
                    }
                } catch (NullPointerException e) {
                }

            }
        });

        Intent intent = getIntent();
        try {
            if (intent.hasExtra("RETRIEVE")) {
                retrieveWIFIList = intent.getStringExtra("RETRIEVE");                   //Encoded String must be decoded.
                String[] wifiStringDivider = retrieveWIFIList.split("#");               //Splits each string up from '#' delim.

                for (int i = 0; i < wifiStringDivider.length; i++) {
                    String selectedEncodedString = wifiStringDivider[i];
                    String[] tempNumArray = selectedEncodedString.split("-"); // splitting string by '-'

                    int tempNumSize = tempNumArray.length;
                    int tempNum[] = new int[tempNumSize];

                    //Converting String Integer Array into Integer Integer Array
                    for (int z = 0; z < tempNumArray.length; z++) {
                        tempNum[z] = Integer.parseInt(tempNumArray[z]);
                    }

                    selectedWifiArrList.add(TF.textDecoder(tempNum)); //Add Decoded String onto the wifiList
                }

                wifiLISTAdapterView.notifyDataSetChanged();
                TF.setListViewHeightBasedOnChildren(wifiLISTAdapterView, selectedWIFIListView);
            }
        } catch (NullPointerException e) {
        }

    } //End of onCreate()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == WIFI_PICKING_CODE && resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("wifi")) {
                    returnedWifi = (WIFIInfoModel) data.getSerializableExtra("wifi");

                    if (returnedWifi != null) {
                        if (!checkDuplicate(returnedWifi.getWIFIName(), selectedWifiArrList)) {
                            if (!editMode) {
                                selectedWifiArrList.add(returnedWifi.getWIFIName());                //Places the data from the WifiSelectorActivity.java into the arraylist
                            } else {
                                selectedWifiArrList.set(
                                        selectedEditPosition,
                                        returnedWifi.getWIFIName());
                            }
                            wifiLISTAdapterView.notifyDataSetChanged();
                            TF.setListViewHeightBasedOnChildren(
                                    wifiLISTAdapterView,
                                    selectedWIFIListView);

                        } else
                            Toast.makeText(
                                    wifi_picker_context,
                                    "WIFI is already added",
                                    Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (NullPointerException e) {
        }
    }

    public boolean checkDuplicate(String wifi, List<String> wifilist) {
        if (!wifilist.isEmpty()) {
            for (String wifiInfoModel : wifilist) {
                if (wifiInfoModel.equals(wifi)) {
                    warning.setTitle("Warning");
                    warning.setMessage("WIFI is already on the list.");
                    warning.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    warning.show();
                    return true;
                }
            }
        }
        return false;
    }

//    private void scanWifi() {
//        StoredWifi.clear();
//        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//        wifiManager.startScan();
//        Toast.makeText(wifi_picker_context, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
//    }

//    public String getMacAddr() {
//        try {
//            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface nif : all) {
//                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
//
//                byte[] macBytes = nif.getHardwareAddress();
//                if (macBytes == null) {
//                    return "";
//                }
//
//                StringBuilder res1 = new StringBuilder();
//                for (byte b : macBytes) {
//                    //res1.append(Integer.toHexString(b & 0xFF) + ":");
//                    res1.append(String.format("%02X:", b));
//                }
//
//                if (res1.length() > 0) {
//                    res1.deleteCharAt(res1.length() - 1);
//                }
//                return res1.toString();
//            }
//        } catch (Exception ex) {
//        }
//        return "";
//    }

//    Used for Scanning WIFIs on device
//    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            results = wifiManager.getScanResults();
//            unregisterReceiver(wifiReceiver);
//
//            for (ScanResult scanResult : results) {
//                StoredWifi.add(scanResult.SSID);             //Saves into Scan Results
//                System.out.println("SSID: " + scanResult.SSID + "       MAC: " + scanResult.BSSID);
//                wifiLISTAdapterView.notifyDataSetChanged();
//                TF.setListViewHeightBasedOnChildren(wifiLISTAdapterView, selectedWIFIListView);
//            }
//        }
//    };
}
