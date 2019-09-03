package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SetupEventActionsSelectionActivity
        extends AppCompatActivity implements TypeObjectAdapter.mOnItemClickListener{

    private final int REQUEST_SELECTION_LIST_CODE = 1001;
    private final String LAUNCH_APP = "Launch An App";
    private final String QR_CODE = "Show a QR Code";
    private final String BROWSE_URL = "Open a Web Link";
    private final String CHANGE_VOLUME = "Change Volume";
    private final String BRIGHTNESS = "Screen Brightness";

    private Context action_selection_context;
    private RecyclerView actionRV;
    private List<TypeObjectModel> actionArrList = new ArrayList<>();
    private RecyclerView.Adapter adapterForActionListView;
    private Map<String, String> action_options = new Hashtable<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_actions_selection);
        action_selection_context = SetupEventActionsSelectionActivity.this;

        actionRV = (RecyclerView) findViewById(R.id.action_types);
        actionRV.setLayoutManager(new LinearLayoutManager(action_selection_context));
        actionArrList.add(
                new TypeObjectModel(
                        LAUNCH_APP,
                        getDrawable(R.drawable.icon_action_app)));

        actionArrList.add(
                new TypeObjectModel(
                        QR_CODE,
                        getDrawable(R.drawable.testing_qrcode)));
        actionArrList.add(
                new TypeObjectModel(
                        BRIGHTNESS,
                        getDrawable(R.drawable.icon_action_brightness)));

        actionArrList.add(
                new TypeObjectModel(
                        BROWSE_URL,
                        getDrawable(R.drawable.icon_action_brower)));

        actionArrList.add(
                new TypeObjectModel(
                        CHANGE_VOLUME,
                        getDrawable(R.drawable.icon_action_volume)));



        adapterForActionListView = new TypeObjectAdapter(
                action_selection_context,
                actionArrList,
                this);

        actionRV.setAdapter(adapterForActionListView);

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
                        break;

                    case BRIGHTNESS:
                        intent = new Intent(
                                action_selection_context,
                                SetupEventActionScreenBrightness.class);
                        //startActivityForResult(intent, REQUEST_SELECTION_LIST_CODE);
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
                startActivityForResult(intent, REQUEST_SELECTION_LIST_CODE);
    }
}
