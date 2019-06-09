package com.ordinary.projectcache.projectcache;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppList extends AppCompatActivity {
    private List<InstalledAppInfo> apps;
    private ListView list;
    private int appCode = 1;
    private static String appDomain = "";
    //private static Bitmap appIcon;
    private static Drawable appIcon;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list_view);
        list = (ListView) findViewById(R.id.app_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        list.setTextFilterEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshIt();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InstalledAppInfo app = (InstalledAppInfo) parent.getItemAtPosition(position);
                appDomain = app.info.packageName;

                appIcon = app.info.loadIcon(getPackageManager());
                //appIcon = drawableToBitmap(app.info.loadIcon(getPackageManager()));
                Log.d("", appDomain);
                finish();
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

    public static String getDomain()
    {
        return appDomain;
    }

//    public static Bitmap getAppIcon()
//    {
//        return appIcon;
//    }
    public static Drawable getAppIcon()
    {
        return appIcon;
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    class LoadAppInfoTask extends AsyncTask<Integer, Integer, List<InstalledAppInfo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<InstalledAppInfo> doInBackground(Integer... params) {
            List<InstalledAppInfo> apps = new ArrayList<>();
            PackageManager pm = getPackageManager();

            List<ApplicationInfo> infos = pm.getInstalledApplications(params[0]);
            Intent i;
            for (ApplicationInfo packageInfo : infos) {
                i = getPackageManager().getLaunchIntentForPackage(packageInfo.packageName);
                if (i == null) {
                    continue;
                }
                InstalledAppInfo app = new InstalledAppInfo();
                app.info = packageInfo;
                app.label = packageInfo.loadLabel(pm).toString();
                apps.add(app);
            }

            Collections.sort(apps, new appComparator());
            return apps;
            //return null;
        }

        @Override
        protected void onPostExecute(List<InstalledAppInfo> appInfos) {
            super.onPostExecute(appInfos);
            list.setAdapter(new AdapterForAppList(AppList.this, appInfos));
            swipeRefreshLayout.setRefreshing(false);
            Snackbar.make(list, appInfos.size() + "applications loaded", Snackbar.LENGTH_LONG).show();
        }

        private class appComparator implements Comparator<InstalledAppInfo> {
            @Override
            public int compare(InstalledAppInfo X, InstalledAppInfo Y) {
                CharSequence x = X.label;
                CharSequence y = Y.label;

                if (x == null) {
                    x = X.info.packageName;
                }
                if (y == null) {
                    y = Y.info.packageName;
                }
                return Collator.getInstance().compare(x.toString(), y.toString());
            }
        }
    }
}
