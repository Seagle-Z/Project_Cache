package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SetupEventActionScreenBrightness extends AppCompatActivity {

    private final int screenBrightnessMax = 100, screenBrightnessMin = 1;
    private SeekBar brightnessSeekbar;
    private TextView brightnessValue;
    private Button completeButton;
    private int curBrightnessValue, seekBarvalue;
    private Context screen_brightness_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_action_screen_brightness);
        screen_brightness_context = SetupEventActionScreenBrightness.this;

        brightnessSeekbar = (SeekBar) findViewById(R.id.brightness_seekbar);
        brightnessSeekbar.setMax(screenBrightnessMax);
        brightnessSeekbar.setMin(screenBrightnessMin);
        brightnessValue = (TextView) findViewById(R.id.seekBar_value);
        completeButton = (Button) findViewById(R.id.screen_brightness_activity_complete_button);

        // TODO: 2019-08-20 Missing edit feature.
        boolean settingsCanWrite = hasWriteSettingsPermission(screen_brightness_context);
        // If do not have then open the Can modify system settings panel.
        if (!settingsCanWrite) {
            AlertDialog.Builder RequestPermissionDialog =
                    new AlertDialog.Builder(screen_brightness_context);
            RequestPermissionDialog.setTitle("Permission needed");
            RequestPermissionDialog.setMessage(
                    "To modify brightness, please allow app to modify system setting");
            RequestPermissionDialog.setPositiveButton(
                    "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            changeWriteSettingsPermission(screen_brightness_context);
                        }
                    });
            RequestPermissionDialog.setNegativeButton(
                    "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(
                                    screen_brightness_context,
                                    "Permission Denied",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }
                    });
            RequestPermissionDialog.show();
        }

        try {
            curBrightnessValue = android.provider.Settings.System.getInt(
                    getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
            curBrightnessValue = (int) (curBrightnessValue / 255.0 * 100);
            brightnessValue.setText(
                    "Brightness Value: " + curBrightnessValue + "/" + 100);
            brightnessSeekbar.setProgress(curBrightnessValue);
        } catch (Settings.SettingNotFoundException e) {
        }

        brightnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarvalue = progress;
                brightnessValue.setText(
                        "Brightness Value: " + seekBarvalue + "/" + 100);
                Settings.System.putInt(
                        getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS,
                        (int) Math.round(seekBarvalue * 255.0 / 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                brightnessValue.setText(
                        "Brightness Value: " + seekBarvalue + "/" + 100);
                Settings.System.putInt(
                        getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS,
                        curBrightnessValue);
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (seekBarvalue == curBrightnessValue) {
                    AlertDialog.Builder Warning = new AlertDialog.Builder(screen_brightness_context);
                    Warning.setTitle("Reminder");
                    Warning.setMessage("The value did not change.");
                    Warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    Warning.show();
                } else {
                    intent.putExtra("BRIGHTNESS", Integer.toString(seekBarvalue));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private boolean hasWriteSettingsPermission(Context context) {
        boolean ret = true;
        // Get the result from below code.
        ret = Settings.System.canWrite(context);
        return ret;
    }

    private void changeWriteSettingsPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        context.startActivity(intent);
    }
}

