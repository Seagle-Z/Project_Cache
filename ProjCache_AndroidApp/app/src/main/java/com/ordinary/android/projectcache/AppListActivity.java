package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppListActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    private List<InstalledAppInfo> apps;
    private ListView list;
    private boolean editMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_app_list);
        list = (ListView) findViewById(R.id.app_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        list.setTextFilterEnabled(true);
        //pull down can refresh the app list

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

        //get the app information, pack and send back to main activity
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Check where does the user click on
                InstalledAppInfo app = (InstalledAppInfo) parent.getItemAtPosition(position);
                Intent intent = new Intent(); //Create a new intent object to for data returning use.
                intent.putExtra("app", app);
                setResult(Activity.RESULT_OK, intent);
                finish(); //End of Activity.
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadAppInfoTask loadAppInfoTask = new LoadAppInfoTask();
        loadAppInfoTask.execute(PackageManager.GET_META_DATA);
    }

    private void refreshIt() {
        LoadAppInfoTask loadAppInfoTask = new LoadAppInfoTask();
        loadAppInfoTask.execute(PackageManager.GET_META_DATA);
    }


    class LoadAppInfoTask extends AsyncTask<Integer, Integer, List<InstalledAppInfo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        /*Start collecting application information via packageManager. Then creating small
          installed Application object, check InstalledAppInfo for more information. Insert app objects
          into the List then return to the listView for viewing use.
        */
        @Override
        protected List<InstalledAppInfo> doInBackground(Integer... params) {
            List<InstalledAppInfo> apps = new ArrayList<>(); //Create InstalledAppinfo List for listview use
            PackageManager pm = getPackageManager(); //Use Android built-in packagemanage to collect app information

            List<ApplicationInfo> infos = pm.getInstalledApplications(params[0]);
            Intent i;
            for (ApplicationInfo packageInfo : infos) {
                i = getPackageManager().getLaunchIntentForPackage(packageInfo.packageName);
                if (i == null) {
                    continue;
                }
                InstalledAppInfo app = new InstalledAppInfo(
                        packageInfo.packageName,
                        packageInfo.loadLabel(pm).toString(),
                        packageInfo.loadIcon(pm)
                );
//                app.setPackageName(packageInfo.packageName);
//                app.setLabel(packageInfo.loadLabel(pm).toString());
//                app.setDrawable(packageInfo.loadIcon(pm));
                apps.add(app);
            }

            Collections.sort(apps, new appComparator()); //Sort the List before returning.
            return apps;
            //return null;
        }


        //After executing the loading process, show the user how many app is loaded.
        @Override
        protected void onPostExecute(List<InstalledAppInfo> appInfos) {
            super.onPostExecute(appInfos);
            list.setAdapter(new AppListAdapter(AppListActivity.this, appInfos));
            swipeRefreshLayout.setRefreshing(false);
            Snackbar.make(list, appInfos.size() + "applications loaded", Snackbar.LENGTH_LONG).show();
        }

        //Sort the application based on label, if label doesn't exist, sort by package name
        private class appComparator implements Comparator<InstalledAppInfo> {
            @Override
            public int compare(InstalledAppInfo X, InstalledAppInfo Y) {
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
