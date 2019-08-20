package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class EventSetupPage1Fragment extends Fragment {
    private final String TAG = "EventSetupPage1Fragment";
    private final int REQUEST_CONDITION_CODE = 1001;
    Event event;
    private CustomEventSetupViewPager viewPager;
    private Button addConditionButton;
    private FloatingActionButton forward;
    private ListView conditionListView;
    private ArrayAdapter<String> adapter;
    private View view;
    private ToolFunctions TF = new ToolFunctions();
    private Map<String, String> conditions = new Hashtable<>();
    private List<String> conditionsArrList = new ArrayList<>();
    private List<String> selectedConditionTypes = new ArrayList<>();
    private boolean editMode;
    private EventSetupPageAdapter eventSetupPageAdapter;
    private EventSetupPage2Fragment p2;
    private EventSetupPage3Fragment p3;
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
        forward = (FloatingActionButton) view.findViewById(R.id.page1Foward);
        viewPager = (CustomEventSetupViewPager) getActivity().findViewById(R.id.setup_viewPager);
        conditionListView = (ListView) view.findViewById(R.id.condition_list);
        conditionListView.setTextFilterEnabled(true);
        adapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.layout_general_list,
                R.id.general_list_textview_text,
                conditionsArrList
        );
        conditionListView.setAdapter(adapter);
        registerForContextMenu(conditionListView);
        addConditionButton = (Button) view.findViewById(R.id.add_condition);
        eventSetupPageAdapter = (EventSetupPageAdapter) viewPager.getAdapter();
        p2 = (EventSetupPage2Fragment) eventSetupPageAdapter.getItem(1);
        p3 = (EventSetupPage3Fragment) eventSetupPageAdapter.getItem(2);

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
                updateEventObj();
//                }
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.popup_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(
                        getContext(),
                        "Edit",
                        Toast.LENGTH_LONG).show();

                Intent intent = getIntent(info.position);
                startActivityForResult(intent, REQUEST_CONDITION_CODE);
                editMode = true;
                return true;
            case R.id.delete:
                Toast.makeText(getContext(), "Delete", Toast.LENGTH_LONG).show();
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete");
                adb.setNegativeButton("No no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),
                                "Cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                adb.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        conditions.remove(selectedConditionTypes.get(info.position));
                        conditionsArrList.remove(info.position);
                        selectedConditionTypes.remove(info.position);
                        adapter.notifyDataSetChanged();
                        TF.setListViewHeightBasedOnChildren(adapter, conditionListView);
                        Toast.makeText(
                                getContext(),
                                "Deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                adb.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private Intent getIntent(int position) {
        Intent intent = null;
        if (selectedConditionTypes.get(position).equals("Time")) {
            intent = new Intent(
                    getContext(),
                    SetupEventConditionDateTimeActivity.class
            );
            //Pack the value that selected from the list and send to TimeSelectorActivity
            intent.putExtra("RETRIEVE", conditions.get("TIME"));
        } else if (selectedConditionTypes.get(position).equals("App")) {
            intent = new Intent(
                    getContext(),
                    SetupEventConditionOnScreenAppActivity.class
            );
            //Pack the value that selected from the list and send to TimeSelectorActivity
            intent.putExtra("RETRIEVE", conditions.get("ON_SCREEN_APP"));
        } else if (selectedConditionTypes.get(position).equals("WIFI")) {
            intent = new Intent(
                    getContext(),
                    SetupEventConditionWifiActivity.class
            );
            intent.putExtra("RETRIEVE", conditions.get("WIFI"));
        }
        return intent;
    }

    private void updateConditionList(Intent data)
    {
        if (data.hasExtra("Time")) {
            if (!editMode) {
                conditionsArrList.add("- Added trigger method: Time");
                selectedConditionTypes.add("Time");
            }
            editMode = false;
            conditions.put("TIME", data.getStringExtra("Time"));
        }
        if (data.hasExtra("Apps")) {
            if (!editMode) {
                conditionsArrList.add("- Added trigger method: When app show on screen");
                selectedConditionTypes.add("App");
            }
            editMode = false;
            conditions.put("ON_SCREEN_APP", data.getStringExtra("Apps"));
        }
        adapter.notifyDataSetChanged();
        TF.setListViewHeightBasedOnChildren(adapter, conditionListView);
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
}