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
    private Button previous;
    private EditText eventName, eventDescription;
    private Button complete;
    private EventSetupPageAdapter eventSetupPageAdapter;
    private Switch AutoTriggerSwitch, OneTimeEventSwitch;
    private boolean autoTrigger, oneTimeEvent = false;
    private EventSetupPage1Fragment p1;
    private EventSetupPage2Fragment p2;
    private ToolFunctions TF = new ToolFunctions();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_setup_page3, container, false);
        viewPager = (CustomEventSetupViewPager) getActivity().findViewById(R.id.setup_viewPager);
        eventSetupPageAdapter = (EventSetupPageAdapter) viewPager.getAdapter();
        previous = (Button) view.findViewById(R.id.page3Previous);
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

        p1 = (EventSetupPage1Fragment) eventSetupPageAdapter.getItem(0);
        p2 = (EventSetupPage2Fragment) eventSetupPageAdapter.getItem(1);

        //complete.setEnabled(true);
        event = p1.event;
        if (event != null) {
            eventName.setText(event.eventName);
            if(!event.autoTrigger)
            {
                autoTrigger = false;
                AutoTriggerSwitch.setChecked(false);
            }
            if(!event.oneTimeEvent)
            {
                oneTimeEvent = false;
                OneTimeEventSwitch.setChecked(false);
            }
            if(event.eventDescription != null)
            {
                eventDescription.setText(event.eventDescription);
            }
        }

        eventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    complete.setEnabled(false);
                    complete.setTextColor(getResources().getColor(R.color.colorUnableButton));
                }
                else {
                    complete.setEnabled(true);
                    complete.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                event.eventName = s.toString();
            }
        });

        AutoTriggerSwitch.setChecked(true);
        autoTrigger = true;
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
                TF.textEncoder(s.toString());
                event.eventDescription = s.toString();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Events events = new Events(eventSetupPageAdapter.getContext());
                if(!p1.eventModify) {
                    events.addEvent(event);
                    Toast.makeText(getContext(),"Event Created", Toast.LENGTH_SHORT).show();
                }
                else {
                    events.modifyEventByID(event.eventID, event);
                    Toast.makeText(getContext(), "Event Updated", Toast.LENGTH_SHORT).show();
                }

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
