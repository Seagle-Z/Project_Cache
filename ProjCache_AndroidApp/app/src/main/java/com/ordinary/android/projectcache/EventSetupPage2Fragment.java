package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class EventSetupPage2Fragment extends Fragment {

    private static final String TAG = "EventSetupPage2Fragment";
    private final int ADD_TASK_ACTION_CODE = 1001;
    Event event = null;
    private String AppPackageName = "";
    private CustomEventSetupViewPager viewPager;
    private Button startActionButton, ongoingActionButton, endActionButton;
    private FloatingActionButton forward, previous;
    private ListView startActionListView, ongoingActionListView, endActionListView;
    private ArrayAdapter<String> adapterForStartActionListView,
            adapterForOngoingActionListview, adapterForEndActionListView;
    private List<String> startActionList = new ArrayList<>();
    private Map<String, String> startActionKeyValue = new Hashtable<>();
    private List<String> ongoingActionList = new ArrayList<>();
    private Map<String, String> ongoingActionKeyValue = new Hashtable<>();
    private List<String> endActionList = new ArrayList<>();
    private Map<String, String> endActionKeyValue = new Hashtable<>();
    private ToolFunctions TF = new ToolFunctions();
    private PackageManager pm;
    private int buttonPressCode = -999, selectedEditedPosition;
    private boolean editMode;
    private EventSetupPage1Fragment p1;
    private EventSetupPage3Fragment p3;
    private EventSetupPageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_setup_page2, container, false);
        viewPager = (CustomEventSetupViewPager) getActivity().findViewById(R.id.setup_viewPager);
        pm = getActivity().getPackageManager();

        forward = (FloatingActionButton) view.findViewById(R.id.page2Forward);
        previous = (FloatingActionButton) view.findViewById(R.id.page2Previous);
        startActionButton = (Button) view.findViewById(R.id.add_startAction);
        ongoingActionButton = (Button) view.findViewById(R.id.add_ongoingAction);
        endActionButton = (Button) view.findViewById(R.id.add_endAction);

        startActionListView = (ListView) view.findViewById(R.id.start_action_listview);
        ongoingActionListView = (ListView) view.findViewById(R.id.ongoing_action_listview);
        endActionListView = (ListView) view.findViewById(R.id.end_action_listview);

        adapter = (EventSetupPageAdapter) viewPager.getAdapter();
        p1 = (EventSetupPage1Fragment) adapter.getItem(0);
        p3 = (EventSetupPage3Fragment) adapter.getItem(2);

        adapterForStartActionListView = new ArrayAdapter<String>(
                getContext(),
                R.layout.layout_general_list,
                R.id.general_list_textview_text,
                startActionList);

        adapterForOngoingActionListview = new ArrayAdapter<String>(
                getContext(),
                R.layout.layout_general_list,
                R.id.general_list_textview_text,
                ongoingActionList);

        adapterForEndActionListView = new ArrayAdapter<String>(
                getContext(),
                R.layout.layout_general_list,
                R.id.general_list_textview_text,
                endActionList);

        startActionListView.setAdapter(adapterForStartActionListView);
        startActionListView.setTextFilterEnabled(true);
        registerForContextMenu(startActionListView);

        ongoingActionListView.setAdapter(adapterForOngoingActionListview);
        ongoingActionListView.setTextFilterEnabled(true);
        registerForContextMenu(ongoingActionListView);

        endActionListView.setAdapter(adapterForEndActionListView);
        endActionListView.setTextFilterEnabled(true);
        registerForContextMenu(endActionListView);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                System.out.println(p1.event.eventID);
            }
        });

        startActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressCode = 1;
                Intent intent = new Intent(getContext(), SetupEventActionsSelectionActivity.class);
                startActivityForResult(intent, ADD_TASK_ACTION_CODE);
            }
        });

        ongoingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressCode = 2;
                Intent intent = new Intent(getContext(), SetupEventActionsSelectionActivity.class);
                startActivityForResult(intent, ADD_TASK_ACTION_CODE);
            }
        });

        endActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressCode = 3;
                Intent intent = new Intent(getContext(), SetupEventActionsSelectionActivity.class);
                startActivityForResult(intent, ADD_TASK_ACTION_CODE);
            }
        });
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK_ACTION_CODE && resultCode == Activity.RESULT_OK) {
            try {
//                if (data.hasExtra("Application")) {
//                    AppInfoModel app = (AppInfoModel) data.getSerializableExtra("App");
//                    PackageManager pm = getActivity().getPackageManager();
//                    updateArrayForListView(app,buttonPressCode);
//                    //ToolFunctions.ButtonIconProcessing(getContext(), pm, app);
//                    //startActionButton.setText("Open Application: " + app.getLabel());
//                    AppPackageName = app.getPackageName();
//                } else if (data.hasExtra("QR")) {
//                    //Waiting for implementation
//                }
                updateArrayForListView(data, buttonPressCode);
            } catch (NullPointerException e) {
            }
        }
    }

    public String getAppPackageName() {
        return AppPackageName;
    }

    public void updateEventObject(Event e) {
        this.event = e;
    }

    //Update the related list based on the case input
    private void updateArrayForListView(Intent intent, int buttonCode) {
        if (buttonCode == 1) {
            addToRelatedList(
                    startActionList, startActionKeyValue,
                    adapterForStartActionListView, intent,
                    startActionListView);
        } else if (buttonCode == 2) {
            if(intent.hasExtra("app"))
            {
                addToRelatedList(
                        ongoingActionList, ongoingActionKeyValue,
                        adapterForOngoingActionListview, intent,
                        ongoingActionListView);
            }

        } else if (buttonCode == 3){
            if(intent.hasExtra("app"))
            {
                addToRelatedList(
                        endActionList, endActionKeyValue,
                        adapterForEndActionListView, intent,
                        endActionListView);
            }
        } else{
            //Should never happened
            Log.d("","No Button Pressed");
        }
    }

    private void addToRelatedList(List<String> editingList, Map<String, String> editingHashtable,
                                  ArrayAdapter<?> adapter, Intent intent,
                                  ListView editingListView)
    {
        if(intent.hasExtra("app"))
        {
            AppInfoModel app = (AppInfoModel) intent.getSerializableExtra("app");
            if(!editMode)
                editingList.add("Open Application: " + app.getLabel());
            else
                editingList.set(selectedEditedPosition, "Open Application: " + app.getLabel());
            editingHashtable.put("App", app.getPackageName());
            adapter.notifyDataSetChanged();
            TF.setListViewHeightBasedOnChildren(adapter, editingListView);
        }
    }
}
