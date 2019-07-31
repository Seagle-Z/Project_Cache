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

public class EventSetupPage1Fragment extends Fragment {
    private final String TAG = "EventSetupPage1Fragment";
    ViewPager viewPager;
    Button addConditionButton;
    FloatingActionButton forward;
    private final int REQUEST_CONDITION_CODE = 1001;
    private String triggerCondition = "";
    ListView conditionListView;
    ArrayList<String> conditionsArrList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    View view;
    ToolFunctions TF = new ToolFunctions();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_setup_page1, container, false);
        forward = (FloatingActionButton) view.findViewById(R.id.page1Foward);
        viewPager = (ViewPager) getActivity().findViewById(R.id.setup_viewPager);
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


//        TextView widget = (TextView) this;
//        Object text = widget.getText();
//        if (text instanceof Spanned) {
//            Spannable buffer = (Spannable) text;
//
//            int action = event.getAction();
//
//            if (action == MotionEvent.ACTION_UP
//                    || action == MotionEvent.ACTION_DOWN) {
//                int x = (int) event.getX();
//                int y = (int) event.getY();
//
//                x -= widget.getTotalPaddingLeft();
//                y -= widget.getTotalPaddingTop();
//
//                x += widget.getScrollX();
//                y += widget.getScrollY();
//
//                Layout layout = widget.getLayout();
//                int line = layout.getLineForVertical(y);
//                int off = layout.getOffsetForHorizontal(line, x);
//
//                ClickableSpan[] link = buffer.getSpans(off, off,
//                        ClickableSpan.class);
//
//                if (link.length != 0) {
//                    if (action == MotionEvent.ACTION_UP) {
//                        link[0].onClick(widget);
//                    } else if (action == MotionEvent.ACTION_DOWN) {
//                        Selection.setSelection(buffer,
//                                buffer.getSpanStart(link[0]),
//                                buffer.getSpanEnd(link[0]));
//                    }
//                    return true;
//                }
//            }
//
//        }
//
//        return false;
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_CONDITION_CODE && resultCode == Activity.RESULT_OK) {
                if(data.hasExtra("GivenTime")) {
                    conditionsArrList.add("Added trigger method: Time");

                    adapter.notifyDataSetChanged();
                    TF.setListViewHeightBasedOnChildren(adapter,conditionListView);
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
                Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                Toast.makeText(getContext(), "Delete", Toast.LENGTH_LONG).show();
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete");
                adb.setNegativeButton("No no", null);
                adb.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        conditionsArrList.remove(info.position);
                        adapter.notifyDataSetChanged();
                        TF.setListViewHeightBasedOnChildren(adapter, conditionListView);
                    }
                });
                adb.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public String getTriggerCondition() {
        return triggerCondition;
    }
}
