package com.ordinary.android.projectcache;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventsManagementActivity extends AppCompatActivity {

    private RecyclerView eventsManagementRecyclerView;
    private RecyclerView.Adapter EventManagementAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Events events;
    private static final String EVENTS_FILE_NAME = "events.csv";

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        File eventsFile = new File(getFilesDir(), EVENTS_FILE_NAME);
        events = new Events(this.getApplicationContext(), eventsFile);
        List<Event> eventsList = events.getEventsList();

        List<EventManagementModel> EventsManagementList;
        EventsManagementList = new ArrayList<>();
        for (Event e : eventsList) {
            EventsManagementList.add(new EventManagementModel(
                    e.eventName, e.eventDescription, e.isActivated, R.drawable.ic_menu_send/*e.eventImage*/
            ));
        }

        eventsManagementRecyclerView = findViewById(R.id.eventsManagement_RecyclerView);
        eventsManagementRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        EventManagementAdapter = new EventsManagementAdapter(EventsManagementList);

        eventsManagementRecyclerView.setLayoutManager(layoutManager);
        eventsManagementRecyclerView.setAdapter(EventManagementAdapter);

    }

}
