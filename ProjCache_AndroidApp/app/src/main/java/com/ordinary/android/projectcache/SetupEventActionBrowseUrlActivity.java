package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetupEventActionBrowseUrlActivity extends AppCompatActivity {

    EditText inputUrlEditText;
    Button completeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_action_browse_url);

        final ToolFunctions toolFunctions = new ToolFunctions();

        inputUrlEditText = findViewById(R.id.inputUrl_editText);

        completeButton = findViewById(R.id.complete_button);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputUrlEditText.getText().toString().equals("")) {
                    Toast.makeText(
                            SetupEventActionBrowseUrlActivity.this,
                            "Empty is not available  >_<",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    String url = inputUrlEditText.getText().toString();
                    int[] urlEncoded = toolFunctions.asciiEncoder(url);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < urlEncoded.length; i++) {
                        //System.out.println("------------------------" + i);
                        sb.append(urlEncoded[i]);
                        if (i != urlEncoded.length - 1) {
                            sb.append("-");
                        }
                    }
                    String retUrl = sb.toString();
                    System.out.println(retUrl);

                    // return value
                    Intent intent = new Intent();
                    intent.putExtra("BROWSE_URL", retUrl);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });


    }
}
