package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SetupEventActionsSelectionActivity extends AppCompatActivity {

    private final int REQUEST_SELECTION_LIST_CODE = 1001;
    private final String LAUNCH_APP = "Launch An App";
    private final String QR_CODE = "Show a QR Code";
    private final String BROWSE_URL = "Connect to A Site";
    private final String CHANGE_VOLUME = "Change Volume";
    private final String BRIGHTNESS = "Screen Brightness";

    private Context action_selection_context;
    private ListView actionListView;
    private List<String> actionArrList = new ArrayList<>();
    private ArrayAdapter<String> adapterForActionListView;
    private Map<String, String> action_options = new Hashtable<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_actions_selection);
        action_selection_context = SetupEventActionsSelectionActivity.this;

        actionListView = (ListView) findViewById(R.id.action_types);
        actionListView.setTextFilterEnabled(true);

        actionArrList.add(LAUNCH_APP);
        actionArrList.add(QR_CODE);
        actionArrList.add(BRIGHTNESS);
        actionArrList.add(BROWSE_URL);
        actionArrList.add(CHANGE_VOLUME);

         adapterForActionListView = new ArrayAdapter<String>(
                this,
                R.layout.layout_general_list,
                R.id.general_list_textview_text,
                actionArrList);

        actionListView.setAdapter(adapterForActionListView);

        actionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (actionArrList.get(position)) {
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
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if(requestCode == REQUEST_SELECTION_LIST_CODE && resultCode == Activity.RESULT_OK)
            setResult(Activity.RESULT_OK, data);
            finish();
        } catch (NullPointerException e) {
        }
    }
}
