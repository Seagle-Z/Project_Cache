package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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
    private boolean editMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_wifi_list);

        selectedWIFIListView = findViewById(R.id.wifi_list);

        Intent intent = getIntent();
        try {
            if (intent.hasExtra("RETRIEVE")) {
                editMode = true;
            }
        } catch (NullPointerException e) {
        }

        //get the app information, pack and send back to main activity
        selectedWIFIListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Check where does the user click on
                AppInfoModel app = (AppInfoModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(); //Create a new intent object to for data returning use.
                intent.putExtra("app", app);
                setResult(Activity.RESULT_OK, intent);
                finish(); //End of Activity.
            }
        });


    } //End of onCreate()






}
