package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Switch;

public class SetupEventActionVolumeActivity extends AppCompatActivity {

    private final int VOLUME_MAX_VALUE = 100;
    private final int VOLUME_MIN_VALUE = 0;
    private int originalVolumeValue;

    private Switch playTestSwitch;
    private boolean playTest;

    private CheckBox streamCheckBox;
    private SeekBar streamSeekBar;

    private CheckBox ringtoneCheckBox;
    private SeekBar ringtoneSeekBar;

    private CheckBox alarmsCheckBox;
    private SeekBar alarmsSeekBar;

    private Button completeButton;

    private int streamSeekBarValue;
    private int ringtoneSeekBarValue;
    private int alarmsSeekBarValue;

    private boolean changeStream;
    private boolean changeRingtone;
    private boolean changeAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_action_volume);

        final AudioManager audioManager = (AudioManager)getSystemService(this.AUDIO_SERVICE);
        originalVolumeValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        playTest = false;

        streamSeekBarValue = 0;
        ringtoneSeekBarValue = 0;
        alarmsSeekBarValue = 0;

        changeStream = false;
        changeRingtone = false;
        changeAlarms = false;

        //- Declare Switch ------------------------------------------------------------------------*
        playTestSwitch = findViewById(R.id.playTestVolume_switch);
        playTestSwitch.setChecked(playTest);

        //- Declare checkBoxes --------------------------------------------------------------------*
        streamCheckBox = findViewById(R.id.streamVolume_checkBox);
        streamCheckBox.setText("Change Media Volume?");

        ringtoneCheckBox = findViewById(R.id.ringtoneVolume_checkBox);
        ringtoneCheckBox.setText("Change Ringtone Volume?");

        alarmsCheckBox = findViewById(R.id.alarmsVolume_checkBox);
        alarmsCheckBox.setText("Change Alarms Volume?");


        //- Declare seekBars ----------------------------------------------------------------------*
        streamSeekBar = findViewById(R.id.streamVolume_seekBar);
        streamSeekBar.setMax(VOLUME_MAX_VALUE);
        streamSeekBar.setMin(VOLUME_MIN_VALUE);
        streamSeekBar.setEnabled(changeStream);

        ringtoneSeekBar = findViewById(R.id.ringtoneVolume_seekBar);
        ringtoneSeekBar.setMax(VOLUME_MAX_VALUE);
        ringtoneSeekBar.setMin(VOLUME_MIN_VALUE);
        ringtoneSeekBar.setEnabled(changeRingtone);

        alarmsSeekBar = findViewById(R.id.alarmsVolume_seekBar);
        alarmsSeekBar.setMax(VOLUME_MAX_VALUE);
        alarmsSeekBar.setMin(VOLUME_MIN_VALUE);
        alarmsSeekBar.setEnabled(changeAlarms);


        //- Declare Complete button ---------------------------------------------------------------*
        completeButton = findViewById(R.id.complete_button);


        //- Set Play Test switch listener ---------------------------------------------------------*
        playTestSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playTestSwitch.isChecked()) {
                    playTest = true;
                } else {
                    playTest = false;
                }
            }
        });


        //- Set checkBoxes listener ---------------------------------------------------------------*
        streamCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (streamCheckBox.isChecked()) {
                    changeStream = true;
                    streamCheckBox.setText("Stream Volume: " + streamSeekBarValue + "%");
                } else {
                    changeStream = false;
                    streamCheckBox.setText("Change Media Volume?");
                }
                streamSeekBar.setEnabled(changeStream);
            }
        });

        ringtoneCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringtoneCheckBox.isChecked()) {
                    changeRingtone = true;
                    ringtoneCheckBox.setText("Ringtone Volume: " + ringtoneSeekBarValue + '%');
                } else {
                    changeRingtone = false;
                    ringtoneCheckBox.setText("Change Ringtone Volume?");
                }
                ringtoneSeekBar.setEnabled(changeRingtone);
            }
        });

        alarmsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmsCheckBox.isChecked()) {
                    changeAlarms = true;
                } else {
                    changeAlarms = false;
                    alarmsCheckBox.setText("Change Alarms Volume?");
                }
                alarmsSeekBar.setEnabled(changeAlarms);
            }
        });


        //- Set seekBars listener -----------------------------------------------------------------*
        streamSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                streamSeekBarValue = progress;
                streamCheckBox.setText("Stream Volume: " + streamSeekBarValue + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (playTest) {
                    PlayShortSoundRunnable pssr = new PlayShortSoundRunnable(streamSeekBarValue);
                    Thread playSoundThread = new Thread(pssr);
                    playSoundThread.start();
                }
            }
        });

        ringtoneSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ringtoneSeekBarValue = progress;
                ringtoneCheckBox.setText("Ringtone Volume: " + ringtoneSeekBarValue + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (playTest) {
                    PlayShortSoundRunnable pssr = new PlayShortSoundRunnable(ringtoneSeekBarValue);
                    Thread playSoundThread = new Thread(pssr);
                    playSoundThread.start();
                }
            }
        });

        alarmsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alarmsSeekBarValue = progress;
                alarmsCheckBox.setText("Alarms Volume: " + alarmsSeekBarValue + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (playTest) {
                    PlayShortSoundRunnable pssr = new PlayShortSoundRunnable(alarmsSeekBarValue);
                    Thread playSoundThread = new Thread(pssr);
                    playSoundThread.start();
                }
            }
        });


        //- Set Complete Button listener ----------------------------------------------------------*
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // make sure the volume reset to original
                resetVolume(audioManager);

                // build up the return string
                String retValue = "";

                if (changeStream) {
                    retValue += Integer.toString(streamSeekBarValue);
                } else {
                    retValue += "N";
                }

                retValue += "-";

                if (changeRingtone) {
                    retValue += Integer.toString(ringtoneSeekBarValue);
                } else {
                    retValue += "N";
                }

                retValue += "-";

                if (changeAlarms) {
                    retValue += Integer.toString(alarmsSeekBarValue);
                } else {
                    retValue += "N";
                }

                // return value
                Intent intent = new Intent();
                intent.putExtra("VOLUME", retValue);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    private void resetVolume(AudioManager audioManager) {
        // reset the volume back to original
        audioManager = (AudioManager)getSystemService(this.AUDIO_SERVICE);
        audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC, originalVolumeValue, AudioManager.FLAG_SHOW_UI);
    }

    private void playTestingSound(int testVolume) {

        AudioManager audioManager = (AudioManager)getSystemService(this.AUDIO_SERVICE);

        double maxVolume = (double)audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int playTestVolume = (int)((double)testVolume * (maxVolume / 100.0));

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, playTestVolume, 0);
        Uri notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer player = MediaPlayer.create(getApplicationContext(), notification);

        // play the testing sound
        player.start();

        // pause to wait the sound finish
        try {
            Thread.sleep(800);     // every this time long, check the new status
        } catch (Exception e) {
            // print out exception if needed
        }

        // reset the volume back to original
        audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC, originalVolumeValue, AudioManager.FLAG_SHOW_UI);
    }

    class PlayShortSoundRunnable implements Runnable {

        int testVolume;

        PlayShortSoundRunnable(int testVolume) {
            this.testVolume = testVolume;
        }

        @Override
        public void run() {
            playTestingSound(testVolume);
        }
    }
}


