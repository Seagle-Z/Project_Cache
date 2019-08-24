package com.ordinary.android.projectcache;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.style.BulletSpan;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_SETUP_CODE = 1002;
    private final int STORAGE_PERMISSION_CODE = 1010;

    private ViewPager coreViewPager;
    private Thread coreThread;

    private static final String EVENTS_FILE_NAME = "events.csv";
    public Events events;
    public File eventsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //- ToolBar -------------------------------------------------------------------------------*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkPermissionStatus();


        //- Floating Button -----------------------------------------------------------------------*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startEventSetup =
                        new Intent(MainActivity.this, EventSetupActivity.class);
                startEventSetup.putExtra("EVENT_FILE",eventsFile);
                startActivityForResult(startEventSetup, REQUEST_SETUP_CODE);
            }
        });


        //- Drawer Menu ---------------------------------------------------------------------------*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //- Events Object -------------------------------------------------------------------------*
        eventsFile = new File(getFilesDir(), EVENTS_FILE_NAME);
        events = new Events(this, eventsFile);


        //- Core Construction ---------------------------------------------------------------------*
        coreViewPager = findViewById(R.id.core_viewPager);
        coreViewPager.setPadding(20, 0, 20, 0);

        CoreRunnable coreRunnable = new CoreRunnable(this, eventsFile, coreViewPager);
        coreThread = new Thread(coreRunnable);
        coreThread.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_SETUP_CODE == requestCode && resultCode == Activity.RESULT_OK) {
            Event event = (Event) data.getSerializableExtra("Event");
            events.addEvent(event);
        }
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

        if (id == R.id.nav_manageEvents) {
            //** add, delete, modify events here
        } else if (id == R.id.nav_createEvent) {
            Intent startEventSetup =
                    new Intent(MainActivity.this, EventSetupActivity.class);
            startEventSetup.putExtra("EVENT_FILE",eventsFile);
            startActivityForResult(startEventSetup, REQUEST_SETUP_CODE);
        } else if (id == R.id.nav_about) {
            //** put our link here "https://www.ordinary.com
        } else if (id == R.id.nav_account) {
            //** for login account
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkPermissionStatus() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(
                    MainActivity.this,
                    "You have already granted this permission!",
                    Toast.LENGTH_LONG
            ).show();
        else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This Permission is needed")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_CODE
                            );
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
