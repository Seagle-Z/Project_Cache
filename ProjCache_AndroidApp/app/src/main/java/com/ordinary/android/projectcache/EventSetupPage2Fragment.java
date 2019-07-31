package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EventSetupPage2Fragment extends Fragment {

    private static final String TAG = "EventSetupPage2Fragment";
    private final int ADD_TASK_ACTION_CODE = 1001;
    private final int ADD_END_TASK_CODE = 1002;
    private String AppPackageName = "";
    ViewPager viewPager;
    Button startActionButton, endActionButton;
    FloatingActionButton forward, previous;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_setup_page2, container, false);
        forward = (FloatingActionButton) view.findViewById(R.id.page2Forward);
        viewPager = (ViewPager) getActivity().findViewById(R.id.setup_viewPager);
        startActionButton = (Button) view.findViewById(R.id.add_startAction);
        endActionButton = (Button) view.findViewById(R.id.add_endAction);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        previous = (FloatingActionButton) view.findViewById(R.id.page2Previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });


        startActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActionTaskSelectionActivity.class);
                startActivityForResult(intent, ADD_TASK_ACTION_CODE);
            }
        });

        endActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActionTaskSelectionActivity.class);
                startActivityForResult(intent, ADD_TASK_ACTION_CODE);
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK_ACTION_CODE && resultCode == Activity.RESULT_OK) {
            try {
                if (data.hasExtra("Application")) {
                    InstalledAppInfo app = (InstalledAppInfo) data.getSerializableExtra("App");
                    PackageManager pm = getActivity().getPackageManager();
                    ToolFunctions.ButtonIconProcessing(getContext(), pm, app);
                    startActionButton.setText("Open Application: " + app.getLabel());
                    AppPackageName = app.getPackageName();
                } else if (data.hasExtra("QR")) {
                    //Waiting for implementation
                }
            } catch (NullPointerException e) {
            }
        }
    }

    public String getAppPackageName() {
        return AppPackageName;
    }
}
