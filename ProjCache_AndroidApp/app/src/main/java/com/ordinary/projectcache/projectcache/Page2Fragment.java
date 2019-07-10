package com.ordinary.projectcache.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Page2Fragment extends Fragment {

    private static final String TAG = "Page2Fragment";
    private final int ADD_TASK_ACTION_CODE = 1001;
    private final int ADD_END_TASK_CODE = 1002;
    private String AppPackageName = "";
    ViewPager viewPager;
    Button startAction, endAction;
    FloatingActionButton forward, previous;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_setup_page_2, container, false);
        forward = (FloatingActionButton) view.findViewById(R.id.page2Forward);
        viewPager = (ViewPager) getActivity().findViewById(R.id.setup_viewPager);
        startAction = (Button) view.findViewById(R.id.add_startAction);
        endAction = (Button) view.findViewById(R.id.add_endAction);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        previous = (FloatingActionButton) view. findViewById(R.id.page2Previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });


        startAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), start_end_tasks_activity.class);
                //startActivity(intent);
                startActivityForResult(intent, ADD_TASK_ACTION_CODE);
            }
        });

        endAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), start_end_tasks_activity.class);
                //startActivity(intent);
                startActivityForResult(intent, ADD_TASK_ACTION_CODE);
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK_ACTION_CODE && resultCode == Activity.RESULT_OK) {
            if(data.hasExtra("Application")) {
                InstalledAppInfo app = (InstalledAppInfo) data.getSerializableExtra("App");
                PackageManager pm = getActivity().getPackageManager();
                ToolFunctions.ButtonIconProcessing(getContext(), pm, app);
                startAction.setText("Open Application: " + app.getLabel());
                AppPackageName = app.getPackageName();
            }

            else if (data.hasExtra("QR"))
            {
                //Waiting for implementation
            }
        }
    }

    public String getAppPackageName()
    {
        return AppPackageName;
    }
}