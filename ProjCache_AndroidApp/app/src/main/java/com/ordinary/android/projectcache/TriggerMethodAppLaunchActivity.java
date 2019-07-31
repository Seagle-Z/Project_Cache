package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TriggerMethodAppLaunchActivity extends AppCompatActivity {

    Button addAppButton, completeButton;
    ListView selectedAppListView;
    ArrayList<InstalledAppInfo> selectedAppArrList = new ArrayList<>();
    AppListAdapter appListAdapterView;
    Context app_picker_context;
    PackageManager pm;
    private final int APP_PICKING_CODE = 1010;
    private int selectedEditPosition;
    private boolean editMode;
    private InstalledAppInfo returnedApp;
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

        selectedAppListView.setTextFilterEnabled(true);
//        adapterFortimeListView = new ArrayAdapter<String>(app_picker_context, R.layout.layout_app_list, R.id.condition_name, selectedAppArrList);
        appListAdapterView = new AppListAdapter(app_picker_context, selectedAppArrList);
        selectedAppListView.setAdapter(appListAdapterView);
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
                //TODO: Add packagename to data structure
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == APP_PICKING_CODE && resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("App")) {
                    if (!editMode) {
                        returnedApp = (InstalledAppInfo) data.getSerializableExtra("App");
                        if (!checkDuplicate(returnedApp, selectedAppArrList)) {
                            if (returnedApp != null) {
                                try {
                                    returnedApp.setDrawable(pm.getApplicationIcon(returnedApp.getPackageName()));
                                    selectedAppArrList.add(returnedApp);
                                    appListAdapterView.notifyDataSetChanged();
                                    TF.setListViewHeightBasedOnChildren(appListAdapterView, selectedAppListView);
                                } catch (PackageManager.NameNotFoundException e) {
                                }
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
                AlertDialog.Builder adb = new AlertDialog.Builder(app_picker_context);
                adb.setTitle("Delete");
                adb.setNegativeButton("No no", null);
                adb.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(app_picker_context, "Deleting", Toast.LENGTH_SHORT).show();
                        selectedAppArrList.remove(info.position);
                        appListAdapterView.notifyDataSetChanged();
                        TF.setListViewHeightBasedOnChildren(appListAdapterView, selectedAppListView);
                        Toast.makeText(app_picker_context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                adb.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public boolean checkDuplicate(InstalledAppInfo app, ArrayList<InstalledAppInfo> applist) {
        if (!applist.isEmpty()) {
            for (InstalledAppInfo installedAppInfo : applist) {
                if (installedAppInfo.getPackageName().equals(app.getPackageName()))
                    return true;
            }
        }
        return false;
    }
//
//    public String[] parseResult(ArrayList<String> timeList) {
//        String[] result = new String[timeList.size()];
//        String s = "";
//        for (int i = 0; i < result.length; i++) {
//            s = timeList.get(i);
//            if (timeList.get(i).contains("- Event activates between: ")) {
//                s = s.replace("- Event activates between: ", "");
//            } else if (s.contains("- Event activates at: ")) {
//                s = s.replace("- Event activates at: ", "");
//            }
//            result[i] = s;
//        }
//        return result;
//    }
}
