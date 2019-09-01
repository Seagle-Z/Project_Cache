package com.ordinary.android.projectcache;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
    Event event = null;
    private CustomEventSetupViewPager viewPager;
    private FloatingActionButton previous;
    private EditText eventName, eventDescription;
    private Button complete;
    private EventSetupPageAdapter adapter;
    private Switch AutoTriggerSwitch, OneTimeEventSwitch;
    private boolean autoTrigger, oneTimeEvent = false;
    private EventSetupPage1Fragment p1;
    private EventSetupPage2Fragment p2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_setup_page3, container, false);
        viewPager = (CustomEventSetupViewPager) getActivity().findViewById(R.id.setup_viewPager);
        adapter = (EventSetupPageAdapter) viewPager.getAdapter();
        previous = (FloatingActionButton) view.findViewById(R.id.page3Previous);
        eventName = (EditText) view.findViewById(R.id.event_name);
        eventDescription = (EditText) view.findViewById(R.id.event_description);
        complete = (Button) view.findViewById(R.id.complete);
        AutoTriggerSwitch = (Switch) view.findViewById(R.id.autotrigger_switch);
        OneTimeEventSwitch = (Switch) view.findViewById(R.id.OneTimeEvent_switch);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                updateEventObj();
            }
        });

        p1 = (EventSetupPage1Fragment) adapter.getItem(0);
        p2 = (EventSetupPage2Fragment) adapter.getItem(1);

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
                event.eventName = s.toString();
            }
        });

        AutoTriggerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autoTrigger = true;
                    Toast.makeText(getContext(), "Auto Trigger Event is on", Toast.LENGTH_SHORT).show();
                } else {
                    autoTrigger = false;
                    Toast.makeText(getContext(), "Auto Trigger Event is off", Toast.LENGTH_SHORT).show();
                }
                event.autoTrigger = autoTrigger;
            }
        });

        OneTimeEventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    oneTimeEvent = true;
                    Toast.makeText(getContext(), "One Time Event is on", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "One Time Event is off", Toast.LENGTH_SHORT).show();
                    oneTimeEvent = false;
                }
                event.oneTimeEvent =  oneTimeEvent;
            }
        });


        eventDescription.addTextChangedListener(new TextWatcher() {
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
                event.eventDescription = s.toString();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.eventID = 1;
                //PagerAdapter p1 = viewPager.getAdapter().getCount();
//                EventSetupPage1Fragment p1 = (EventSetupPage1Fragment) adapter.getItem(0);
//                EventSetupPage2Fragment p2 = (EventSetupPage2Fragment) adapter.getItem(1);
//                EventSetupPage3Fragment p3 = (EventSetupPage3Fragment) adapter.getItem(2);
                //Log.d("",f1.getTriggerCondition());
//                Log.d("", p2.getAppPackageName());
//
//                String[] test = {"lkajsldkfj", "kjashdkjahsdkalsjdfklasjkfj"};
//                String[] test2 = {"lkajsldkfj", "kjashdkjahsdkfj"};


                //TODO: Check all input if it's valid
                // TODO: 2019-08-06 要检查intent时候是null后才能返回，把event的constructor更是改正成正确的样子
//                Event event = new Event("ABC", "EFG",
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
                Events events = new Events(adapter.getContext(), adapter.getFile());
                events.addEvent(event);
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        return view;
    }

    private void updateEventObj()
    {
        p1.event = event;
        p2.event = event;
    }
}
