package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

public class EventSetupPage3Fragment extends Fragment {

    private static final String TAG = "EventSetupPage3Fragment";
    ViewPager viewPager;
    FloatingActionButton previous;
    EditText eventName;
    Button complete;
    SectionsPageAdapter adapter;
    Switch AutoTriggerSwitch, OneTimeEventSwitch;
    private boolean autoTrigger, oneTimeEvent = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_setup_page3, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.setup_viewPager);
        adapter = (SectionsPageAdapter) viewPager.getAdapter();
        previous = (FloatingActionButton) view.findViewById(R.id.page3Previous);
        eventName = (EditText) view.findViewById(R.id.event_name);
        complete = (Button) view.findViewById(R.id.complete);
        AutoTriggerSwitch = (Switch) view.findViewById(R.id.autotrigger_switch);
        OneTimeEventSwitch = (Switch) view.findViewById(R.id.OneTimeEvent_switch);

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

        AutoTriggerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        OneTimeEventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                EventSetupPage1Fragment p1 = (EventSetupPage1Fragment) adapter.getItem(0);
                EventSetupPage2Fragment p2 = (EventSetupPage2Fragment) adapter.getItem(1);
                EventSetupPage3Fragment p3 = (EventSetupPage3Fragment) adapter.getItem(2);
                //Log.d("",f1.getTriggerCondition());
                Log.d("", p2.getAppPackageName());

                String[] test = {"lkajsldkfj", "kjashdkjahsdkalsjdfklasjkfj"};
                String[] test2 = {"lkajsldkfj", "kjashdkjahsdkfj"};
                //TODO: Check all input if it's valid
//                Event event = new Event(99, "ABC", "EFG",
//                        "jshdfkjashd", 101,
//                        test, test2,
//                        null, null,
//                        null, null,
//                        null, null,
//                        autoTrigger, oneTimeEvent,
//                        Boolean.TRUE, Boolean.TRUE,
//                        "ASDFASDF", 10101);
//                Intent intent = new Intent();
//                intent.putExtra("Event", event);

                //getActivity().setResult(Activity.RESULT_OK, intent);
                //getActivity().finish();
            }
        });

        return view;
    }
}
