package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class EventSetupPage2Fragment extends Fragment implements
        TypeValueObjectAdapter.mOnItemClickListener {

    private static final String TAG = "EventSetupPage2Fragment";
    private final int ADD_TASK_ACTION_CODE = 1001;
    Event event = null;
    private String AppPackageName = "";
    private CustomEventSetupViewPager viewPager;
    private Button startActionButton, ongoingActionButton, endActionButton;
    private Button forward, previous;
    private RecyclerView startActionRecyclerView, ongoingActionRecyclerView, endActionRecyclerView;
    private RecyclerView.Adapter adapterForStartActionRecyclerView, adapterForOngoingActionRecyclerView,
            adapterForEndActionRecyclerView;
    private List<TypeValueObjectModel> startActionList = new ArrayList<>();
    private Map<String, String> startActionKeyValue = new Hashtable<>();
    private List<TypeValueObjectModel> ongoingActionList = new ArrayList<>();
    private Map<String, String> ongoingActionKeyValue = new Hashtable<>();
    private List<TypeValueObjectModel> endActionList = new ArrayList<>();
    private Map<String, String> endActionKeyValue = new Hashtable<>();
    private ToolFunctions TF = new ToolFunctions();
    private PackageManager pm;
    private int buttonPressCode = -999, selectedEditedPosition;
    private boolean editMode, parsing;
    private EventSetupPage1Fragment p1;
    private EventSetupPage3Fragment p3;
    private EventSetupPageAdapter eventSetupPageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_setup_page2, container, false);
        viewPager = (CustomEventSetupViewPager) getActivity().findViewById(R.id.setup_viewPager);
        pm = getActivity().getPackageManager();

        forward = (Button) view.findViewById(R.id.page2Forward);
        previous = (Button) view.findViewById(R.id.page2Previous);
        startActionButton = (Button) view.findViewById(R.id.add_startAction);
        ongoingActionButton = (Button) view.findViewById(R.id.add_ongoingAction);
        endActionButton = (Button) view.findViewById(R.id.add_endAction);

        startActionRecyclerView = (RecyclerView) view.findViewById(R.id.start_action_rv);
        ongoingActionRecyclerView = (RecyclerView) view.findViewById(R.id.ongoing_action_rv);
        endActionRecyclerView = (RecyclerView) view.findViewById(R.id.end_action_rv);

        startActionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ongoingActionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        endActionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventSetupPageAdapter = (EventSetupPageAdapter) viewPager.getAdapter();
        p1 = (EventSetupPage1Fragment) eventSetupPageAdapter.getItem(0);
        p3 = (EventSetupPage3Fragment) eventSetupPageAdapter.getItem(2);

        event = eventSetupPageAdapter.getEvent();
        if (event != null) {
            parsing = true;
            if(event.tasksTypeStart.length != 0) {
                parseEventObj(event.tasksTypeStart, event.tasksValueStart, 1);
            }
            if(event.tasksTypeOngoing.length != 0)
            {
                parseEventObj(event.tasksTypeOngoing, event.tasksValueOngoing, 2);
            }
            if(event.tasksTypeEnd.length != 0)
            {
                parseEventObj(event.tasksTypeEnd, event.tasksValueEnd, 3);
            }
            parsing = false;
        }

        adapterForStartActionRecyclerView = new TypeValueObjectAdapter(
                getContext(),
                startActionList,
                startActionKeyValue,
                this, 1);

        adapterForOngoingActionRecyclerView = new TypeValueObjectAdapter(
                getContext(),
                ongoingActionList,
                ongoingActionKeyValue,
                this, 2);

        adapterForEndActionRecyclerView = new TypeValueObjectAdapter(
                getContext(),
                endActionList,
                endActionKeyValue,
                this, 3);

        startActionRecyclerView.setAdapter(adapterForStartActionRecyclerView);
        ongoingActionRecyclerView.setAdapter(adapterForOngoingActionRecyclerView);
        endActionRecyclerView.setAdapter(adapterForEndActionRecyclerView);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startActionList.size() != 0 || ongoingActionList.size() != 0 || endActionList.size() != 0)
                    viewPager.setCurrentItem(2);
                else
                {
                    AlertDialog.Builder warning = new AlertDialog.Builder(getContext());
                    warning.setTitle("Warning");
                    warning.setMessage("Please selected at least one action first in any section.");
                    warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    warning.show();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        startActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressCode = 1;
                Intent intent = new Intent(getContext(), SetupEventActionsSelectionActivity.class);
                if (!startActionKeyValue.isEmpty()) {
                    Bundle bundle = new Bundle();
                    for (Map.Entry m : startActionKeyValue.entrySet()) {
                        String[] key = m.getKey().toString().split("#");
                        bundle.putString(key[0], "");
                    }
                    intent.putExtras(bundle);
                }

                startActivityForResult(intent, ADD_TASK_ACTION_CODE);
            }
        });

        ongoingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                buttonPressCode = 2;
//                Intent intent = new Intent(getContext(), SetupEventActionsSelectionActivity.class);
                Intent intent = new Intent(getContext(), SetupEventActionsSelectionActivity.class);
                if (!ongoingActionKeyValue.isEmpty()) {
                    Bundle bundle = new Bundle();
                    for (Map.Entry m : ongoingActionKeyValue.entrySet()) {
                        String[] key = m.getKey().toString().split("#");
                        bundle.putString(key[0], "");
                    }
                    intent.putExtras(bundle);
                }
//                startActivityForResult(intent, ADD_TASK_ACTION_CODE);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
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
        });

        endActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressCode = 3;
                Intent intent = new Intent(getContext(), SetupEventActionsSelectionActivity.class);
                if (!endActionKeyValue.isEmpty()) {
                    Bundle bundle = new Bundle();
                    for (Map.Entry m : endActionKeyValue.entrySet()) {
                        String[] key = m.getKey().toString().split("#");
                        bundle.putString(key[0], "");
                    }
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent, ADD_TASK_ACTION_CODE);
            }
        });
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK_ACTION_CODE && resultCode == Activity.RESULT_OK) {
            try {
                updateArrayForListView(data, buttonPressCode);
            } catch (NullPointerException e) {
            }
        }
    }

    private void parseEventObj(String[] eventTasksType, String[] eventTaskValue, int code)
    {
        for (int i = 0; i < eventTasksType.length; i++) {
            Intent data = new Intent();
            data.putExtra(eventTasksType[i], eventTaskValue[i]);
            updateArrayForListView(data, code);
        }
    }
    //Update the related list based on the case input
    private void updateArrayForListView(Intent intent, int buttonCode) {
        if (buttonCode == 1) {
            addToRelatedList(
                    startActionList, startActionKeyValue,
                    adapterForStartActionRecyclerView, intent);
            //adapterForStartActionRecyclerView.notifyDataSetChanged();
        } else if (buttonCode == 2) {
            addToRelatedList(
                    ongoingActionList, ongoingActionKeyValue,
                    adapterForOngoingActionRecyclerView, intent);
            //adapterForOngoingActionRecyclerView.notifyDataSetChanged();
        } else if (buttonCode == 3) {
            addToRelatedList(
                    endActionList, endActionKeyValue,
                    adapterForEndActionRecyclerView, intent);
            //adapterForEndActionRecyclerView.notifyDataSetChanged();
        } else {
            //Should never happened
            Log.d("", "No Button Pressed");
        }
        updateEventObj();
    }

    private void addToRelatedList(List<TypeValueObjectModel> editingList, Map<String, String> editingHashtable,
                                  RecyclerView.Adapter adapter, Intent intent) {

        if (intent.hasExtra("LAUNCH_APP")) {
            AppInfoModel app;
            if(parsing)
            {
                app = new AppInfoModel(intent.getStringExtra("LAUNCH_APP"), getContext());
            }
            else
                app = (AppInfoModel) intent.getSerializableExtra("LAUNCH_APP");
            if (!editMode)
                editingList.add(
                        new TypeValueObjectModel(
                                "Launch An App",
                                app.getLabel(),
                                getResources().getDrawable(R.drawable.icon_action_app)
                        )
                );
            else
                editingList.set(
                        selectedEditedPosition,
                        new TypeValueObjectModel(
                                "Launch An App",
                                app.getLabel(),
                                getResources().getDrawable(R.drawable.icon_action_app)
                        )
                );

            editingHashtable.put("LAUNCH_APP#Launch An App", app.getPackageName());
        }
        if (intent.hasExtra("SCREEN_BRIGHTNESS")) {
            if (!editMode)
                editingList.add(
                        new TypeValueObjectModel(
                                "Screen Brightness",
                                intent.getStringExtra("SCREEN_BRIGHTNESS"),
                                getResources().getDrawable(R.drawable.icon_action_brightness)
                        )
                );
            else
                editingList.set(
                        selectedEditedPosition,
                        new TypeValueObjectModel(
                                "Screen Brightness", //typename
                                intent.getStringExtra("SCREEN_BRIGHTNESS"),
                                getResources().getDrawable(R.drawable.icon_action_brightness)
                        )
                );

            editingHashtable.put("SCREEN_BRIGHTNESS#Screen Brightness", intent.getStringExtra("SCREEN_BRIGHTNESS"));
        }

        if (intent.hasExtra("CHANGE_VOLUME")) {
            String result = parseVolume(intent.getStringExtra("CHANGE_VOLUME"));
            if (!editMode)
                editingList.add(
                        new TypeValueObjectModel(
                                "Volume Change",
                                result,
                                getResources().getDrawable(R.drawable.icon_action_volume)
                        )
                );
            else
                editingList.set(
                        selectedEditedPosition,
                        new TypeValueObjectModel(
                                "Volume Change",
                                result,
                                getResources().getDrawable(R.drawable.icon_action_volume)
                        )
                );
            editingHashtable.put("CHANGE_VOLUME#Volume Change", intent.getStringExtra("VOLUME"));
        }

        if (intent.hasExtra("BROWSE_URL")) {
            String result = parseURL(intent.getStringExtra("BROWSE_URL"));
            if (!editMode)
                editingList.add(
                        new TypeValueObjectModel(
                                "Open a Web Link",
                                result,
                                getResources().getDrawable(R.drawable.icon_action_broswer)
                        )
                );
            else
                editingList.set(
                        selectedEditedPosition,
                        new TypeValueObjectModel(
                                "Open a Web Link",
                                result,
                                getResources().getDrawable(R.drawable.icon_action_broswer)
                        )
                );
            editingHashtable.put("BROWSE_URL#Open a Web Link", intent.getStringExtra("BROWSE_URL"));
        }

        editMode = false;
    }

    @Override
    public void onItemClick(int position, int key) {
        Intent intent = null;
        if (key == 1) {
            intent = editRecycleViewItem(startActionList, startActionKeyValue, position);
        } else if (key == 2) {
            intent = editRecycleViewItem(ongoingActionList, ongoingActionKeyValue, position);
        } else {
            intent = editRecycleViewItem(endActionList, endActionKeyValue, position);
        }
        startActivityForResult(intent, ADD_TASK_ACTION_CODE);
        editMode = true;
        selectedEditedPosition = position;
    }

    private Intent editRecycleViewItem(List<TypeValueObjectModel> list, Map<String, String> map, int position) {
        Intent intent = null;
        if (list.get(position).getTypename().equalsIgnoreCase(("Launch An App"))) {
            intent = new Intent(
                    getContext(),
                    AppListActivity.class
            );
            //Pack the value that selected from the list and send to TimeSelectorActivity
            intent.putExtra("RETRIEVE", map.get("LAUNCH_APP#Launch An App"));
        } else if (list.get(position).getTypename().equalsIgnoreCase(("Screen Brightness"))) {
            intent = new Intent(
                    getContext(),
                    SetupEventActionScreenBrightness.class
            );
            //Pack the value that selected from the list and send to TimeSelectorActivity
            intent.putExtra("RETRIEVE", map.get("SCREEN_BRIGHTNESS#Screen Brightness"));
        } else if (list.get(position).getTypename().equalsIgnoreCase(("Volume Change"))) {
            intent = new Intent(
                    getContext(),
                    SetupEventActionVolumeActivity.class
            );
            intent.putExtra("RETRIEVE", map.get("CHANGE_VOLUME#Volume Change"));
        } else if (list.get(position).getTypename().equalsIgnoreCase("Open a Web Link")) {
            intent = new Intent(
                    getContext(),
                    SetupEventActionBrowseUrlActivity.class
            );
            intent.putExtra("RETRIEVE", map.get("BROWSE_URL#Open a Web Link"));
        }
        return intent;
    }

    private String parseURL(String s) {
        String result = TF.textDecoder(s);

        if (result.length() > 33) {
            result = result.substring(0, 30);
            result = result + "...";
        }
        return result;
    }

    private String parseVolume(String s) {
        String volumes[] = s.split("-");
        StringBuilder result = new StringBuilder();
        String SEPERATOR = "";
        for (int i = 0; i < volumes.length; i++) {
            if (i == 0) {
                if (volumes[i].equals("N")) {
                    result.append(SEPERATOR);
                    result.append("Media: -");
                    SEPERATOR = ", ";
                } else {
                    result.append(SEPERATOR);
                    result.append("Media: " + volumes[i]);
                    SEPERATOR = ", ";
                }
            } else if (i == 1) {
                if (volumes[i].equals("N")) {
                    result.append(SEPERATOR);
                    result.append("RingTone: -");
                } else {
                    result.append(SEPERATOR);
                    result.append("Ringtone: " + volumes[i]);
                }
            } else {
                if (volumes[i].equals("N")) {
                    result.append(SEPERATOR);
                    result.append("Alarm: -");
                } else {
                    result.append(SEPERATOR);
                    result.append("Alarm: " + volumes[i]);
                }
            }
        }
        return result.toString();
    }


    private void updateEventObj() {
        String[] taskTypeStart = getActionKeyValueStrArr(startActionKeyValue, 1);
        String[] taskStartValue = getActionKeyValueStrArr(startActionKeyValue, 2);
        String[] taskTypeOngoing = getActionKeyValueStrArr(ongoingActionKeyValue, 1);
        String[] taskValueOngoing = getActionKeyValueStrArr(ongoingActionKeyValue, 2);
        String[] taskTypeEnd = getActionKeyValueStrArr(endActionKeyValue, 1);
        String[] taskValueEnd = getActionKeyValueStrArr(endActionKeyValue, 2);

        event.tasksTypeStart = taskTypeStart;
        event.tasksValueStart = taskStartValue;
        event.tasksTypeOngoing = taskTypeOngoing;
        event.tasksValueOngoing = taskValueOngoing;
        event.tasksTypeEnd = taskTypeEnd;
        event.tasksValueEnd = taskValueEnd;

        p1.event = event;
        p3.event = event;
    }

    private String[] getActionKeyValueStrArr(Map<String, String> ActionKeyValue, int keyOrValue) {
        String[] arr = new String[ActionKeyValue.size()];
        int i = 0;
        if (keyOrValue == 1) {
            for (Map.Entry m : ActionKeyValue.entrySet()) {
                String[] key = m.getKey().toString().split("#");
                arr[i] = key[0];
                i++;
            }
        } else {
            for (Map.Entry m : ActionKeyValue.entrySet()) {
                arr[i] = m.getValue().toString();
                i++;
            }
        }
        return arr;
    }
}
