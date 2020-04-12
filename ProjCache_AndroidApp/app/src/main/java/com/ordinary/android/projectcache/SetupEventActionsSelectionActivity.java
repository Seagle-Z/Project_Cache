package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SetupEventActionsSelectionActivity
        extends AppCompatActivity implements EventSetupSelectionListAdapter.mOnItemClickListener {

    private final int REQUEST_SELECTION_LIST_CODE = 1001;
    private final String LAUNCH_APP = "Launch An App";
    private final String QR_CODE = "Show a QR Code";
    private final String BROWSE_URL = "Open a Web Link";
    private final String CHANGE_VOLUME = "Volume Change";
    private final String BRIGHTNESS = "Screen Brightness";
    public ToolFunctions TF = new ToolFunctions();
    private Context action_selection_context;
    private RecyclerView actionRecyclerView;
    private Map<String, String> actions = new Hashtable<>();
    private List<TypeObjectModel> actionArrList = new ArrayList<>();
    private RecyclerView.Adapter adapterForActionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_actions_selection);
        action_selection_context = SetupEventActionsSelectionActivity.this;

        actions.put("LAUNCH_APP", LAUNCH_APP);
        actions.put("QR_CODE", QR_CODE);
        actions.put("BROWSE_URL", BROWSE_URL);
        actions.put("VOLUME_CHANGE", CHANGE_VOLUME);
        actions.put("SCREEN_BRIGHTNESS", BRIGHTNESS);

        actionRecyclerView = (RecyclerView) findViewById(R.id.action_types);
        actionRecyclerView.setLayoutManager(new LinearLayoutManager(action_selection_context));
        actionArrList.add(
                new TypeObjectModel(
                        actions.get("LAUNCH_APP"),
                        getDrawable(R.drawable.icon_action_app)));

        actionArrList.add(
                new TypeObjectModel(
                        actions.get("SCREEN_BRIGHTNESS"),
                        getDrawable(R.drawable.icon_action_brightness)));

        actionArrList.add(
                new TypeObjectModel(
                        actions.get("QR_CODE"),
                        getDrawable(R.drawable.icon_action_qrcode)));

        actionArrList.add(
                new TypeObjectModel(
                        actions.get("BROWSE_URL"),
                        getDrawable(R.drawable.icon_action_broswer)));

        actionArrList.add(
                new TypeObjectModel(
                        actions.get("VOLUME_CHANGE"),
                        getDrawable(R.drawable.icon_action_volume)));

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            for (String key : extras.keySet()) {
                for (int i = 0; i < actionArrList.size(); i++) {
                    if (actions.get(key).equalsIgnoreCase(actionArrList.get(i).getTypename())) {
                        actionArrList.remove(i);
                        break;
                    }
                }
            }
        }

        //Collections.sort(actionArrList, TF.getComparator());


        adapterForActionListView = new EventSetupSelectionListAdapter(
                action_selection_context,
                actionArrList,
                this);

        actionRecyclerView.setAdapter(adapterForActionListView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_SELECTION_LIST_CODE && resultCode == Activity.RESULT_OK)
                setResult(Activity.RESULT_OK, data);
            finish();
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = null;
        switch (actionArrList.get(position).getTypename()) {
            case LAUNCH_APP:
                intent = new Intent(
                        action_selection_context,
                        AppListActivity.class);
                break;

            case QR_CODE:
                intent = new Intent(
                        action_selection_context,
                        SetupEventActionBarCodeActivity.class);
                //showAlertMessage();
                break;

            case BRIGHTNESS:
                intent = new Intent(
                        action_selection_context,
                        SetupEventActionScreenBrightness.class);
                break;

            case BROWSE_URL:
                intent = new Intent(
                        action_selection_context,
                        SetupEventActionBrowseUrlActivity.class);
                break;

            case CHANGE_VOLUME:
                intent = new Intent(
                        action_selection_context,
                        SetupEventActionVolumeActivity.class);
                break;

            default:
                Log.d("", "No Item Selected");
        }
        if (intent != null)
            startActivityForResult(intent, REQUEST_SELECTION_LIST_CODE);
    }

    private void showAlertMessage() {
        AlertDialog.Builder alert =
                new AlertDialog.Builder(action_selection_context);
        alert.setTitle("Sorry");
        alert.setMessage("Current feature is under construction. Stay close for new updates.");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
