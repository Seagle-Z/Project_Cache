package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class EventSetupPage1Fragment
        extends Fragment implements TypeValueObjectAdapter.intentResultCollectingInterface {
    private final String TAG = "EventSetupPage1Fragment";
    private final int REQUEST_CONDITION_CODE = 1001;
    Event event;
    private CustomEventSetupViewPager viewPager;
    private Button addConditionButton;
    private Button forward;
    private RecyclerView conditionRecyclerView;
    private RecyclerView.Adapter adapterForRecyclerView;
    private View view;
    private ToolFunctions TF = new ToolFunctions();
    private Map<String, String> conditions = new Hashtable<>();
    private List<TypeValueObjectModel> conditionsArrList = new ArrayList<>();
    private boolean editMode;
    private EventSetupPageAdapter eventSetupPageAdapter;
    private EventSetupPage2Fragment p2;
    private EventSetupPage3Fragment p3;
    private int selectedPosition;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(
                R.layout.fragment_event_setup_page1,
                container,
                false
        );

        forward = (Button) view.findViewById(R.id.page1Foward);
        viewPager = (CustomEventSetupViewPager) getActivity().findViewById(R.id.setup_viewPager);
        conditionRecyclerView = (RecyclerView) view.findViewById(R.id.typeValueObj_RecyclerView);
        conditionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //conditionRecyclerView.setTextFilterEnabled(true);
        adapterForRecyclerView = new TypeValueObjectAdapter(getContext(), conditionsArrList, conditions, this);

        conditionRecyclerView.setAdapter(adapterForRecyclerView);

        registerForContextMenu(conditionRecyclerView);
        addConditionButton = (Button) view.findViewById(R.id.add_condition);
        eventSetupPageAdapter = (EventSetupPageAdapter) viewPager.getAdapter();
        p2 = (EventSetupPage2Fragment) eventSetupPageAdapter.getItem(1);
        p3 = (EventSetupPage3Fragment) eventSetupPageAdapter.getItem(2);

        event = eventSetupPageAdapter.getEvent();
        if (event != null)
            System.out.println(event.eventName);

        if (event == null) {
            event = new Event(
                    1000, null, null,
                    null, 0,
                    null, null,
                    null, null,
                    null, null,
                    null, null,
                    null, null, null,
                    false, false,
                    true, true,
                    null,
                    null, 0xffffff,
                    "NULL", 0);
        }

        //The "Select A Condition" button that trigger the
        addConditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getContext(),
                        SetupEventConditionsSelectionActivity.class);

                if (!conditions.isEmpty()) {
                    Bundle bundle = new Bundle();
                    for (Map.Entry m : conditions.entrySet()) {
                        bundle.putString(m.getKey().toString(), "");
                    }
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent, REQUEST_CONDITION_CODE);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!conditionsArrList.isEmpty())
//                {
                viewPager.setCurrentItem(1);
//                else {
//                    Toast.makeText(getContext(), "Please add a condition first", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_CONDITION_CODE && resultCode == Activity.RESULT_OK) {
                updateConditionList(data);
            }
        } catch (NullPointerException e) {
            editMode = false;
        }
    }

    @Override
    public void getIntent(int position) {
        Intent intent = null;
        if (conditionsArrList.get(position).getTypename().equalsIgnoreCase(("Time"))) {
            intent = new Intent(
                    getContext(),
                    SetupEventConditionDateTimeActivity.class
            );
            //Pack the value that selected from the list and send to TimeSelectorActivity
            intent.putExtra("RETRIEVE", conditions.get("TIME"));
        } else if (conditionsArrList.get(position).getTypename().equalsIgnoreCase(("App"))) {
            intent = new Intent(
                    getContext(),
                    SetupEventConditionOnScreenAppActivity.class
            );
            //Pack the value that selected from the list and send to TimeSelectorActivity
            intent.putExtra("RETRIEVE", conditions.get("ON_SCREEN_APP"));
        } else if (conditionsArrList.get(position).getTypename().equalsIgnoreCase(("WIFI"))) {
            intent = new Intent(
                    getContext(),
                    SetupEventConditionWifiActivity.class
            );
            intent.putExtra("RETRIEVE", conditions.get("WIFI"));
        }
        startActivityForResult(intent, REQUEST_CONDITION_CODE);
        editMode = true;
        selectedPosition = position;
    }

    private void updateConditionList(Intent data) {
        if (data.hasExtra("Time")) {

            String s = data.getStringExtra("Time");
            String time[] = s.split("#");
            StringJoiner newTimeString = parseTimeData(time);
            String result = "";
            if (newTimeString.toString().length() > 33) {
                result = newTimeString.toString().substring(0, 30);
                result = result + "...";
            } else
                result = newTimeString.toString();


            if (!editMode) {
                conditionsArrList.add(new TypeValueObjectModel(
                        "Time",
                        result,
                        getResources().getDrawable(R.drawable.icon_clock))
                );
            } else {
                conditionsArrList.get(selectedPosition).setValues(newTimeString.toString());
            }
            editMode = false;
            conditions.put("TIME", data.getStringExtra("Time"));
        }
//        if (data.hasExtra("Apps")) {
//            if (!editMode) {
//                conditionsArrList.add("- Added trigger method: When app show on screen");
//                selectedConditionTypes.add("App");
//            }
//            editMode = false;
//            conditions.put("ON_SCREEN_APP", data.getStringExtra("Apps"));
//        }
//
        //Setup for WIFIPage
        if (data.hasExtra("Wifi")) {
            String s = data.getStringExtra("Wifi");
            String wifinames[] = s.split("#");
            StringJoiner newWifiString = new StringJoiner(", ");
            for (int i = 0; i < wifinames.length; i++) {
                wifinames[i] = TF.textDecoder(wifinames[i]);
                newWifiString.add(wifinames[i]);
            }
            String result = "";
            if (newWifiString.toString().length() > 33) {
                System.out.println(newWifiString.toString().length());
                result = newWifiString.toString().substring(0, 30);
                result = result + "...";
            } else
                result = newWifiString.toString();

            if (!editMode) {
                conditionsArrList.add(new TypeValueObjectModel(
                        "WIFI",
                        result,
                        getResources().getDrawable(R.drawable.icon_wifi))
                );
            } else {
                conditionsArrList.get(selectedPosition).setValues(newWifiString.toString());
            }
            editMode = false;
            conditions.put("WIFI", data.getStringExtra("Wifi"));
        }

        updateEventObj();
        adapterForRecyclerView.notifyDataSetChanged();
    }

    private void updateEventObj() {
        String[] methods = new String[conditions.size()];
        String[] values = new String[conditions.size()];
        int i = 0;
        for (Map.Entry m : conditions.entrySet()) {
            methods[i] = m.getKey().toString();
            values[i] = m.getValue().toString();
            i++;
        }
        event.triggerMethods = methods;
        event.triggerValues = values;
        p2.event = event;
        p3.event = event;
    }

    private StringJoiner parseTimeData(String[] time) {
        StringJoiner newTimeString = new StringJoiner("\n ");
        DateFormat sdf = new SimpleDateFormat("HH:mm");

        for (int i = 0; i < time.length; i++) {
            if (time[i].contains("-")) {
                String timeRange[] = time[i].split("-");
                StringJoiner tempTimeRange = new StringJoiner("-");
                for (String t : timeRange) {
                    try {
                        Date hour = sdf.parse(t);
                        String Hr, Min;

                        /*Check the syntax to determine if need to add 0 before values, check
                        function for more details*/
                        Hr = checkHour(hour);
                        Min = checkMin(hour);
                        tempTimeRange.add(Hr + ":" + Min);
                    } catch (ParseException e) {
                    }
                }
                newTimeString.add(tempTimeRange.toString());
            } else {
                try {
                    Date hour = sdf.parse(time[i]);
                    String Hr, Min;

                        /*Check the syntax to determine if need to add 0 before values, check
                        function for more details*/
                    Hr = checkHour(hour);
                    Min = checkMin(hour);
                    newTimeString.add(Hr + ":" + Min);
                } catch (ParseException e) {
                }
            }
        }
        return newTimeString;
    }

    private String checkHour(Date hour) {
        String Hr = "";
        if (hour.getHours() < 10) {
            Hr = "0" + hour.getHours();
        } else
            Hr = Integer.toString(hour.getHours());
        return Hr;
    }

    private String checkMin(Date hour) {
        String Min = "";
        if (hour.getMinutes() < 10)
            Min = "0" + hour.getMinutes();
        else
            Min = Integer.toString(hour.getMinutes());

        return Min;
    }
}