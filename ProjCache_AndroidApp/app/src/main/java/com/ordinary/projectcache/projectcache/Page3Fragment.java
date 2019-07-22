package com.ordinary.projectcache.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Page3Fragment extends Fragment {

    private static final String TAG = "Page3Fragment";
    ViewPager viewPager;
    FloatingActionButton previous;
    EditText eventName;
    Button complete;
    SectionsPageAdapter adapter;
    Switch AutoT, OneTime;
    private boolean autoTrigger, oneTimeEvent = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_setup_page_3, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.setup_viewPager);
        adapter = (SectionsPageAdapter) viewPager.getAdapter();
        previous = (FloatingActionButton) view.findViewById(R.id.page3Previous);
        eventName = (EditText) view.findViewById(R.id.event_name);
        complete = (Button) view.findViewById(R.id.complete);
        AutoT = (Switch) view.findViewById(R.id.autotrigger_switch);
        OneTime = (Switch) view.findViewById(R.id.OneTimeEvent_switch);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        complete.setEnabled(true);

        eventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0)
                    complete.setEnabled(false);
                else
                    complete.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        AutoT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    autoTrigger = true;
                    Toast.makeText(getContext(), "Auto Trigger Event is on", Toast.LENGTH_SHORT).show();
                }
                else {
                    autoTrigger = false;
                    Toast.makeText(getContext(), "Auto Trigger Event is off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        OneTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    oneTimeEvent = true;
                    Toast.makeText(getContext(), "One Time Event is on", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "One Time Event is off", Toast.LENGTH_SHORT).show();
                    oneTimeEvent = false;
                }
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PagerAdapter p1 = viewPager.getAdapter().getCount();
                Page1Fragment p1 = (Page1Fragment) adapter.getItem(0);
                Page2Fragment p2 = (Page2Fragment) adapter.getItem(1);
                Page3Fragment p3 = (Page3Fragment) adapter.getItem(2);
                //Log.d("",f1.getTriggerCondition());
                Log.d("", p2.getAppPackageName());

                String[] test = {"lkajsldkfj", "kjashdkjahsdkalsjdfklasjkfj"};
                String[] test2 = {"lkajsldkfj", "kjashdkjahsdkfj"};
                //TODO: Check all input if it's valid
                Event event = new Event(99, "ABC", "EFG",
                        "jshdfkjashd", 101,
                        test, test2,
                        null, null,
                        null, null,
                        null, null,
                        autoTrigger, oneTimeEvent,
                        Boolean.TRUE, Boolean.TRUE,
                        "ASDFASDF", 10101);
                Intent intent = new Intent();
                intent.putExtra("Event", event);

                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
