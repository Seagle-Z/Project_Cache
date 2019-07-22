package com.ordinary.projectcache.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Page1Fragment extends Fragment {
    private final String TAG = "Page1Fragment";
    ViewPager viewPager;
    Button addCondition;
    FloatingActionButton forward;
    private final int REQUEST_CONDITION_CODE = 1001;
    private String triggerCondition = "";
    ListView conditionList;
    ArrayList<String> conditions = new ArrayList<>();
    ArrayAdapter<String> adapter;
    View view;
    ViewGroup vg;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_setup_page_1, container, false);
        forward = (FloatingActionButton) view.findViewById(R.id.page1Foward);
        viewPager = (ViewPager) getActivity().findViewById(R.id.setup_viewPager);
        conditionList = (ListView) view.findViewById(R.id.condition_list);
        conditionList.setTextFilterEnabled(true);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.condition_list_layout, R.id.condition_name, conditions);
        conditionList.setAdapter(adapter);
        registerForContextMenu(conditionList);
        addCondition = (Button) view.findViewById(R.id.add_condition);


        addCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), condition_list_activity.class);
                startActivityForResult(intent, REQUEST_CONDITION_CODE);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!addCondition.getText().toString().contains("Select"))
//                    viewPager.setCurrentItem(1);
//                else {
//                    Toast.makeText(getContext(), "Please add a condition first", Toast.LENGTH_SHORT).show();
//                }
                viewPager.setCurrentItem(1);
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
                    conditions.add("Time: " + data.getStringExtra("Time") + " | Date: " + data.getStringExtra("Date"));
                    int i = conditions.size();
                    adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(conditionList);
                }
            }
        } catch (NullPointerException e) {
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        if (this.adapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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
                Toast.makeText(getContext(), "Edit", Toast.LENGTH_LONG).show();
                return true;
            case R.id.delete:
                Toast.makeText(getContext(), "Delete", Toast.LENGTH_LONG).show();
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete");
                adb.setNegativeButton("No no", null);
                adb.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        conditions.remove(info.position);
                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(conditionList);
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
