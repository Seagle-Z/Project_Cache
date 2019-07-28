package com.ordinary.projectcache.projectcache;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActionTaskSelectionActivity extends AppCompatActivity {

    private final int REQUEST_APP_LIST_CODE = 1001;
    Boolean app = false;
    Boolean QR = false;
    Button openApp;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_end_tasks_activity);
        viewPager = (ViewPager) findViewById(R.id.setup_viewPager);
        openApp = (Button) findViewById(R.id.open_app);
        openApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app = true;
                Intent intent = new Intent(ActionTaskSelectionActivity.this, AppListActivity.class);
                startActivityForResult(intent, REQUEST_APP_LIST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (app) {
            try {
                data.putExtra("Application", "");
            } catch (NullPointerException e) {
            }
        } else if (QR) {
            data.putExtra("QR", "");
        }

        setResult(RESULT_OK, data);
        finish();
    }
}
