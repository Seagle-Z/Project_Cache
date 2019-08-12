package com.ordinary.android.projectcache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TriggerMethodWifiActivity extends AppCompatActivity {

    Button addWifiButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_method_wifi);

        addWifiButton = findViewById(R.id.add_wifi);
        addWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("You got a B? you suck");
            }
        });
    }
}
