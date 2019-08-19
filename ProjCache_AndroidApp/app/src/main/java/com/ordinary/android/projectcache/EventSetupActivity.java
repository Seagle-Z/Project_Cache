package com.ordinary.android.projectcache;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

public class EventSetupActivity extends AppCompatActivity {

    private static final String TAG = "EventSetupActivity";
    private EventSetupPageAdapter mEventSetupPageAdapter;
    private CustomEventSetupViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_setup);
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


    private void setupViewPager(CustomEventSetupViewPager viewPager)
    {
        EventSetupPageAdapter adapter = new EventSetupPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new EventSetupPage1Fragment(), "Condition");
        adapter.addFragment(new EventSetupPage2Fragment(), "Actions");
        adapter.addFragment(new EventSetupPage3Fragment(), "Event Summary");
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(limit);
    }
}