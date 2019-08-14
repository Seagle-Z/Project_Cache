package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TriggerMethodAppLaunchActivity extends AppCompatActivity {

    private final int APP_PICKING_CODE = 1010;
    Button addAppButton, completeButton;
    ListView selectedAppListView;
    List<InstalledAppInfo> selectedAppArrList = new ArrayList<InstalledAppInfo>() ;
    AppListAdapter appListAdapterView;
    Context app_picker_context;
    PackageManager pm;
    AlertDialog.Builder warning;
    private int selectedEditPosition;
    private boolean editMode;
    private InstalledAppInfo returnedApp;
    private String retrieveAppList;
    //private ArrayList<String> selectedAppDomainName = new ArrayList<>();
    private ToolFunctions TF = new ToolFunctions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_method_app_launch);
        app_picker_context = TriggerMethodAppLaunchActivity.this;
        addAppButton = (Button) findViewById(R.id.add_app);
        completeButton = (Button) findViewById(R.id.application_picker_activity_complete_button);
        selectedAppListView = (ListView) findViewById(R.id.selected_app_list);
        pm = TriggerMethodAppLaunchActivity.this.getPackageManager();
        warning = new AlertDialog.Builder(app_picker_context);
        selectedAppListView.setTextFilterEnabled(true);
//        adapterForTimeListView = new ArrayAdapter<String>(app_picker_context, R.layout.layout_app_list, R.id.condition_name, selectedAppArrList);
        
        appListAdapterView = new AppListAdapter(app_picker_context, selectedAppArrList);
        selectedAppListView.setAdapter(appListAdapterView);
        selectedAppListView.setTextFilterEnabled(true);
        registerForContextMenu(selectedAppListView);

        addAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(app_picker_context, AppListActivity.class);
                startActivityForResult(intent, APP_PICKING_CODE);
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!selectedAppArrList.isEmpty()) {
                        String intentResult = "";
                        for (int i = 0; i < selectedAppArrList.size(); i++) {
                            if (i != selectedAppArrList.size() - 1)
                                intentResult = intentResult + selectedAppArrList.get(i).getPackageName() + "#";
                            else
                                intentResult = intentResult + selectedAppArrList.get(i).getPackageName();
                        }
                        Intent intent = new Intent();
                        intent.putExtra("Apps", intentResult);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        AlertDialog.Builder adb = new AlertDialog.Builder(app_picker_context);
                        adb.setTitle("Warning");
                        adb.setMessage(
                                "Please add at least one application for the event"
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
                retrieveAppList = intent.getStringExtra("RETRIEVE");
                String[] timeRangeDivider = retrieveAppList.split("#");
                System.out.println(timeRangeDivider.length);
                for (String s : timeRangeDivider) {
                    InstalledAppInfo info = getReturnedApp(s);
                    selectedAppArrList.add(info);
                }
                appListAdapterView.notifyDataSetChanged();
                TF.setListViewHeightBasedOnChildren(appListAdapterView, selectedAppListView);
            }
        } catch (NullPointerException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == APP_PICKING_CODE && resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("app")) {
                    returnedApp = (InstalledAppInfo) data.getSerializableExtra("app");
                    if (returnedApp != null) {
                        if (!checkDuplicate(returnedApp, selectedAppArrList)) {
                            try {
                                returnedApp.setDrawable(pm.getApplicationIcon(returnedApp.getPackageName()));
                                if (!editMode) {
                                    selectedAppArrList.add(returnedApp);
                                } else {
                                    selectedAppArrList.set(selectedEditPosition, returnedApp);
                                }
                                appListAdapterView.notifyDataSetChanged();
                                TF.setListViewHeightBasedOnChildren(appListAdapterView, selectedAppListView);
                            } catch (PackageManager.NameNotFoundException e) {
                            }
                        } else
                            Toast.makeText(app_picker_context, "App is already added", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.popup_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(app_picker_context, "Edit", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(app_picker_context, AppListActivity.class);
                intent.putExtra("RETRIEVE", returnedApp);
                startActivityForResult(intent, APP_PICKING_CODE);
                editMode = true;
                selectedEditPosition = info.position;
                return true;

            case R.id.delete:
                warning.setTitle("Delete");
                warning.setNegativeButton("No no", null);
                warning.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(app_picker_context, "Deleting", Toast.LENGTH_SHORT).show();
                        selectedAppArrList.remove(info.position);
                        appListAdapterView.notifyDataSetChanged();
                        TF.setListViewHeightBasedOnChildren(appListAdapterView, selectedAppListView);
                        Toast.makeText(app_picker_context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                warning.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public boolean checkDuplicate(InstalledAppInfo app, List<InstalledAppInfo> applist) {
        if (!applist.isEmpty()) {
            for (InstalledAppInfo installedAppInfo : applist) {
                if (installedAppInfo.getPackageName().equals(app.getPackageName())) {
                    warning.setTitle("Warning");
                    warning.setMessage("Application is already on the list.");
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

    public InstalledAppInfo getReturnedApp(String packageName) {
        InstalledAppInfo app = null;
        try {
            app = new InstalledAppInfo(packageName,
                    pm.getApplicationInfo(
                            packageName, 0).loadLabel(pm).toString(),
                    pm.getApplicationIcon(packageName));
        } catch (PackageManager.NameNotFoundException e) {
        }
        return app;
    }
}
