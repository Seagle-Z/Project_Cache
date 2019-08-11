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
import android.support.v4.view.ViewPager;
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
import java.util.HashMap;
import java.util.Map;

public class EventSetupPage1Fragment extends Fragment {
    private final String TAG = "EventSetupPage1Fragment";
    CustomEventSetupViewPager viewPager;
    Button addConditionButton;
    FloatingActionButton forward;
    ListView conditionListView;
    ArrayList<String> conditionsArrList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    View view;
    private ToolFunctions TF = new ToolFunctions();
    private HashMap<String, String> conditions = new HashMap<>();
    private ArrayList<String> selectedConditionTypes = new ArrayList<>();
    private final int REQUEST_CONDITION_CODE = 1001;
    //Code for TimeSelectorActivity result request only
    private boolean editMode;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_setup_page1, container, false);
        forward = (FloatingActionButton) view.findViewById(R.id.page1Foward);
        viewPager = (CustomEventSetupViewPager) getActivity().findViewById(R.id.setup_viewPager);
        conditionListView = (ListView) view.findViewById(R.id.condition_list);
        conditionListView.setTextFilterEnabled(true);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.layout_general_list, R.id.condition_name, conditionsArrList);
        conditionListView.setAdapter(adapter);
        registerForContextMenu(conditionListView);
        addConditionButton = (Button) view.findViewById(R.id.add_condition);


        addConditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TriggerMethodSelectionActivity.class);
                for (Map.Entry m : conditions.entrySet()) {
                    intent.putExtra(m.getKey().toString(), "");
                }

                startActivityForResult(intent, REQUEST_CONDITION_CODE);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!conditionsArrList.isEmpty())
                    viewPager.setCurrentItem(1);
                else {
                    Toast.makeText(getContext(), "Please add a condition first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_CONDITION_CODE && resultCode == Activity.RESULT_OK) {
                if(data.hasExtra("Time")) {
                    if(!editMode) {
                        conditionsArrList.add("- Added trigger method: Time");
                        selectedConditionTypes.add("Time");
                        adapter.notifyDataSetChanged();
                        TF.setListViewHeightBasedOnChildren(adapter,conditionListView);
                    }
                    conditions.put("Time", data.getStringExtra("Time"));
                    System.out.println(conditions.get("Time"));
                }
            }
        } catch (NullPointerException e) {
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
                Intent intent =
                        new Intent(
                                getContext(),
                                TriggerMethodDateTimeActivity.class
                        );

                //Pack the value that selected from the list and send to TimeSelectorActivity
                intent.putExtra("RETRIEVE", conditions.get("Time"));
                startActivityForResult(intent, REQUEST_CONDITION_CODE);
                editMode = true;
                return true;
            case R.id.delete:
                Toast.makeText(getContext(), "Delete", Toast.LENGTH_LONG).show();
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete");
                adb.setNegativeButton("No no", null);
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
}
