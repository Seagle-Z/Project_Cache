package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.text.Collator;

public class WIFISelectorActivity extends AppCompatActivity {

    //UI Code
    //private ListView selectedWIFIListView;
    private Context wifi_picker_context;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView selectedWIFIRV;
    private RecyclerView.Adapter selectedWIFIRVAdap;

    //Local Variables
    private WifiManager wifiManager;
    private boolean editMode;
    private List<TypeObjectModel> wifiInfoList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_wifi_list);

        wifi_picker_context = WIFISelectorActivity.this;
        selectedWIFIRV = (RecyclerView) findViewById(R.id.wifi_rv);
        selectedWIFIRV.setLayoutManager(new LinearLayoutManager(wifi_picker_context));

        selectedWIFIRVAdap = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };

        selectedWIFIRV.setAdapter(selectedWIFIRVAdap);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshWifi);

        Intent intent = getIntent();
        try {
            if (intent.hasExtra("RETRIEVE")) {
                editMode = true;
            }
        } catch (NullPointerException e) {
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshIt(); //fresh the UI when user requests
            }
        });

        wifiManager = (WifiManager) wifi_picker_context.getSystemService(Context.WIFI_SERVICE);

        selectedWIFIRVAdap.notifyDataSetChanged();

        //selectedWIFIRV.setAdapter(selectedWIFIRVAdap);
        //selectedWIFIRVAdap.notifyDataSetChanged();
        //get the app information, pack and send back to main activity
//        selectedWIFIListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                //Check where does the user click on
//                WIFIInfoModel wifi = (WIFIInfoModel) parent.getItemAtPosition(position);
//                Intent intent = new Intent(); //Create a new intent object to for data returning use.
//                intent.putExtra("wifi", wifi);
//                setResult(Activity.RESULT_OK, intent);
//                finish(); //End of Activity.
//            }
//        });




    } //End of onCreate()


    // Decoded Code
    //  int [] tmep = {86,74,68};
    //  System.out.println(TF.textDecoder(tmep));


    @Override
    protected void onResume() {
        super.onResume();
        LoadWIFIInfoTask loadWIFIInfoTask = new LoadWIFIInfoTask();
        loadWIFIInfoTask.execute();
    }

    private void refreshIt() {
        LoadWIFIInfoTask loadWIFIInfoTask = new LoadWIFIInfoTask();
        loadWIFIInfoTask.execute();
    }


    class LoadWIFIInfoTask extends AsyncTask<Integer, Integer, List<TypeObjectModel>> implements TypeObjectAdapter.mOnItemClickListener  {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        /*Start collecting wifi information. Then creating small
          wifi object, check WIFIInfoModel for more information. Insert wifi objects
          into the List then return to the listView for viewing use.
        */
        //TODO: Fix this similar to AppInfo
        @Override
        protected List<TypeObjectModel> doInBackground(Integer... params) {
            //List<TypeObjectModel> wifi_list = new ArrayList<>(); //Create InstalledAppinfo List for listview use

            //PREVIOUSLY SAVED WIFI Connections
            List<WifiConfiguration> currentNetworks = wifiManager.getConfiguredNetworks();
            //System.out.println("Previously Saved WIFI");
            for (WifiConfiguration prevConnections : currentNetworks) {

                String SSID = prevConnections.SSID.replaceAll("\"","");

                TypeObjectModel wifi = new TypeObjectModel(SSID, getDrawable(R.drawable.icon_condition_wifi));

                wifiInfoList.add(wifi);
            }

            Collections.sort(wifiInfoList, new wifiComparator()); //Sort the List before returning.
            return wifiInfoList;
        }

        //After executing the loading process, show the user how many app is loaded.
        @Override
        protected void onPostExecute(List<TypeObjectModel> wifiInfos) {
            super.onPostExecute(wifiInfos);

            selectedWIFIRVAdap = new TypeObjectAdapter(wifi_picker_context, wifiInfos,this);
            selectedWIFIRV.setAdapter(selectedWIFIRVAdap);
            swipeRefreshLayout.setRefreshing(false);
            Snackbar.make(selectedWIFIRV, wifiInfos.size() + " WIFI List loaded", Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onItemClick(int position) {
            //        //Check where does the user click on
////                WIFIInfoModel wifi = (WIFIInfoModel) parent.getItemAtPosition(position);
////                Intent intent = new Intent(); //Create a new intent object to for data returning use.
////                intent.putExtra("wifi", wifi);
////                setResult(Activity.RESULT_OK, intent);
////                finish(); //End of Activity.

            Intent intent = new Intent();
            intent.putExtra("wifi", wifiInfoList.get(position).getTypename());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        //Sort the WIFI List based on label, if label doesn't exist, sort by package name
        private class wifiComparator implements Comparator<TypeObjectModel> {
            @Override
            public int compare(TypeObjectModel X, TypeObjectModel Y) {
                CharSequence x = X.getTypename();
                CharSequence y = Y.getTypename();

                if (x == null) {
                    x = X.getTypename();
                }
                if (y == null) {
                    y = Y.getTypename();
                }
                return Collator.getInstance().compare(x.toString(), y.toString());
            }
        }
    } //End of LoadWIFIInfoTask


}
