package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.view.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ManageEventsActivity extends AppCompatActivity {

    private RecyclerView eventsManagementRecyclerView;
    private RecyclerView.Adapter eventsManagementAdapter;
    private RecyclerView.LayoutManager layoutManager;

    Events events;
    List<Event> eventsList;
    List<Integer> selectedList;

    private static final String EVENTS_FILE_NAME = "events.csv";
    private final int REQUEST_SETUP_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_management);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Make sure the status bar is true color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startEventSetup =
                        new Intent(ManageEventsActivity.this, EventSetupActivity.class);
                File eventsFile = new File(getFilesDir(), EVENTS_FILE_NAME);
                startEventSetup.putExtra("EVENT_FILE", eventsFile);
                startActivityForResult(startEventSetup, REQUEST_SETUP_CODE);
            }
        });

        events = new Events(this.getApplicationContext());
        eventsList = events.getEventsList();
        selectedList = new ArrayList<>();

        eventsManagementRecyclerView = findViewById(R.id.eventsManagement_RecyclerView);
        eventsManagementRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        eventsManagementAdapter = new ManageEventsAdapter(this, eventsList, selectedList);

        eventsManagementRecyclerView.setLayoutManager(layoutManager);
        eventsManagementRecyclerView.setAdapter(eventsManagementAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == REQUEST_SETUP_CODE && resultCode == Activity.RESULT_OK) {
                events.updateByEventsCSV();
                eventsList = events.getEventsList();

                eventsManagementRecyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(this);
                eventsManagementAdapter = new ManageEventsAdapter(this, eventsList, selectedList);

                eventsManagementRecyclerView.setLayoutManager(layoutManager);
                eventsManagementRecyclerView.setAdapter(eventsManagementAdapter);
                eventsManagementAdapter.notifyDataSetChanged();
            }


        } catch (NullPointerException e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.events_management_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_itemButton:
                if (selectedList == null || selectedList.size() == 0) {
                    Toast.makeText(
                            this,
                            "Click event image to choose the event you want to delete",
                            Toast.LENGTH_SHORT).show();
                } else {
                    deleteEvents();
                    events.updateByEventsCSV();
                    eventsManagementAdapter = new ManageEventsAdapter(this, eventsList, selectedList);
                    eventsManagementRecyclerView.setAdapter(eventsManagementAdapter);
                    eventsManagementAdapter.notifyDataSetChanged();
                    for (Integer i : selectedList) {
                        System.out.println("selectList 还有: " + i);
                    }
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteEvents() {
        for (Integer i : selectedList) {
            events.deleteEventByID(i);
        }
        selectedList = new ArrayList<>();
    }
}
