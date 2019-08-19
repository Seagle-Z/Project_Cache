package com.ordinary.android.projectcache;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class WIFISelectorActivity extends AppCompatActivity {


    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<String> StoredWifi = new ArrayList<>();
    //UI Code
    private ListView selectedWIFIListView;
    Context wifi_picker_context;
    ArrayAdapter wifiLISTAdapterView;
    //Local Variables
    private WifiManager wifiManager;
    private int size = 0;
    private List<ScanResult> results;
    private ToolFunctions TF = new ToolFunctions();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_wifi_list);

        selectedWIFIListView = (ListView) findViewById(R.id.app_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

    }









}
