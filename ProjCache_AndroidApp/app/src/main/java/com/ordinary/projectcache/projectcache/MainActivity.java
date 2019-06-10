package com.ordinary.projectcache.projectcache;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int REQUEST_APP_LIST_CODE = 1001;
    ViewPager viewPagerCore;
    AdapterCoreModel adapterCoreModel;
    List<CoreModel> coreModels;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent applistActivity = new Intent(MainActivity.this, AppList.class);
                startActivityForResult(applistActivity, REQUEST_APP_LIST_CODE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //-- Core implementation ----------------------------------------------------------- START *
        coreModels = new ArrayList<>();

        //** Hard code some card for development ***************************************************
        coreModels.add(new CoreModel(R.drawable.ic_menu_camera, "QR Code"));
        coreModels.add(new CoreModel(R.drawable.ic_menu_send, "UIC"));
        coreModels.add(new CoreModel(R.drawable.ic_menu_share, "Zero flay"));
        //** Hard code some card for developemnt FINISH ********************************************

        adapterCoreModel = new AdapterCoreModel(coreModels, this);

        viewPagerCore = findViewById(R.id.core_viewPager);
        viewPagerCore.setAdapter(adapterCoreModel);
        viewPagerCore.setPadding(20, 0, 20, 0);

        viewPagerCore.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //-- Core implementation ---------------------------------------------------------- FINISH *

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_events) {
            //** add, delete, modify events here
        } else if (id == R.id.nav_about) {
            //** put our link here "https://www.ordinary.com
        } else if (id == R.id.nav_account) {
            //** for login account
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_APP_LIST_CODE &&resultCode == Activity.RESULT_OK)
        {
            Log.d("Original", coreModels.get(0).getText()); //debug for original Text label
            String s = data.getStringExtra("SelectedApp");
            coreModels.get(0).setText(s);
            Log.d("After", coreModels.get(0).getText()); //debug for retrieved app label
            //coreModels.add(new CoreModel(R.drawable.ic_menu_gallery, s));
        }
    }
}
