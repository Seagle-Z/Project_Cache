package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.text.Collator;

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
                WIFIInfoModel wifi = (WIFIInfoModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(); //Create a new intent object to for data returning use.
                intent.putExtra("wifi", wifi);
                setResult(Activity.RESULT_OK, intent);
                finish(); //End of Activity.
            }
        });


    } //End of onCreate()


    @Override
    protected void onResume() {
        super.onResume();
        LoadWIFIInfoTask loadAppInfoTask = new LoadWIFIInfoTask();
        loadWIFIInfoTask.execute(PackageManager.GET_META_DATA);
    }

    private void refreshIt() {
        LoadWIFIInfoTask loadAppInfoTask = new LoadWIFIInfoTask();
        loadWIFIInfoTask.execute(PackageManager.GET_META_DATA);
    }


    class LoadWIFIInfoTask extends AsyncTask<Integer, Integer, List<WIFIInfoModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        /*Start collecting application information via packageManager. Then creating small
          installed Application object, check AppInfoModel for more information. Insert app objects
          into the List then return to the listView for viewing use.
        */
        //TODO: Fix this AppInfo and such
        @Override
        protected List<WIFIInfoModel> doInBackground(Integer... params) {
            List<WIFIInfoModel> wifi_list = new ArrayList<>(); //Create InstalledAppinfo List for listview use
            PackageManager pm = getPackageManager(); //Use Android built-in packagemanage to collect app information

            List<ApplicationInfo> infos = pm.getInstalledApplications(params[0]);  //Resolve first
            Intent i;

            for (ApplicationInfo packageInfo : infos) {
                i = getPackageManager().getLaunchIntentForPackage(packageInfo.packageName);
                if (i == null) {
                    continue;
                }
                WIFIInfoModel wifi = new WIFIInfoModel(
                        packageInfo.packageName,
                        packageInfo.loadLabel(pm).toString()
                );
//                app.setPackageName(packageInfo.packageName);
//                app.setLabel(packageInfo.loadLabel(pm).toString());
//                app.setDrawable(packageInfo.loadIcon(pm));
                wifi_list.add(wifi);
            }

            Collections.sort(wifi_list, new wifiComparator()); //Sort the List before returning.
            return wifi_list;
            //return null;
        }


        //After executing the loading process, show the user how many app is loaded.
        @Override
        protected void onPostExecute(List<WIFIInfoModel> wifiInfos) {
            super.onPostExecute(wifiInfos);
            list.setAdapter(new WIFIListAdapter(WIFISelectorActivity.this, wifiInfos));
            swipeRefreshLayout.setRefreshing(false);
            Snackbar.make(list, wifiInfos.size() + "WIFI List loaded", Snackbar.LENGTH_LONG).show();
        }

        //Sort the WIFI List based on label, if label doesn't exist, sort by package name
        private class wifiComparator implements Comparator<WIFIInfoModel> {
            @Override
            public int compare(WIFIInfoModel X, WIFIInfoModel Y) {
                CharSequence x = X.getLabel();
                CharSequence y = Y.getLabel();

                if (x == null) {
                    x = X.getPackageName();
                }
                if (y == null) {
                    y = Y.getPackageName();
                }
                return Collator.getInstance().compare(x.toString(), y.toString());
            }
        }
    }

}
