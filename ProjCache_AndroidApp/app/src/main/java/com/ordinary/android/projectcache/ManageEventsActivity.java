package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class ManageEventsActivity extends AppCompatActivity {

    private RecyclerView eventsManagementRecyclerView;
    private RecyclerView.Adapter eventManagementAdapter;
    private RecyclerView.LayoutManager layoutManager;

    Events events;
    List<Event> eventsList;

    private static final String EVENTS_FILE_NAME = "events.csv";
    private final int REQUEST_SETUP_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_management);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        System.out.println("e1: " + eventsList);

        eventsManagementRecyclerView = findViewById(R.id.eventsManagement_RecyclerView);
        eventsManagementRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        eventManagementAdapter = new ManageEventsAdapter(this, eventsList);

        eventsManagementRecyclerView.setLayoutManager(layoutManager);
        eventsManagementRecyclerView.setAdapter(eventManagementAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == REQUEST_SETUP_CODE && resultCode == Activity.RESULT_OK) {
                eventsList = events.getEventsList();
                eventManagementAdapter.notifyDataSetChanged();

                eventsManagementRecyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(this);
                eventManagementAdapter = new ManageEventsAdapter(this, eventsList);

                eventsManagementRecyclerView.setLayoutManager(layoutManager);
                eventsManagementRecyclerView.setAdapter(eventManagementAdapter);
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
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}