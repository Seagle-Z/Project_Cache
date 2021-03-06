package com.ordinary.android.projectcache;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

/* TODO: 2019-08-26 Add functionality that can input an Event, then put all info about this Event
         into UI elements. This functionality used for user modify event.
 */

public class EventSetupActivity extends AppCompatActivity {

    private static final String TAG = "EventSetupActivity";
    private EventSetupPageAdapter mEventSetupPageAdapter;
    private CustomEventSetupViewPager mViewPager;
    private Context context;
    private Event event = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_setup);
        context = getApplicationContext();

        Intent intent = getIntent();
        if (intent.hasExtra("MODIFY_EVENT")) {
            Events events = new Events(context);
            int result = intent.getIntExtra("MODIFY_EVENT", 0);
            event = events.getEventByID(result);
        }

        mEventSetupPageAdapter = new EventSetupPageAdapter(getSupportFragmentManager());
        mViewPager = (CustomEventSetupViewPager) findViewById(R.id.setup_viewPager);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.setup_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }
    }

    private void setupViewPager(CustomEventSetupViewPager viewPager) {
        EventSetupPageAdapter adapter = new EventSetupPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new EventSetupPage1Fragment(), "Conditions", context, event);
        adapter.addFragment(new EventSetupPage2Fragment(), "Actions", context, event);
        adapter.addFragment(new EventSetupPage3Fragment(), "Summary", context, event);
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(limit);
    }
}