package com.ordinary.android.projectcache;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;

import static android.content.Context.AUDIO_SERVICE;

public class CoreTasksExecutor {
    Context context;
    Event event;

    // For normal Event
    public CoreTasksExecutor(Context context, Event event) {
        this.context = context;
        this.event = event;
    }

    public void startThisEvent() {
        Intent[] intents = createTasksIntent(event.tasksTypeStart, event.tasksValueStart);
        if (intents == null) {
            return;
        }
        for (Intent i : intents) {
            if (i != null) {    // Due to some task do not need intent
                context.startActivity(i);
            }
        }
    }

    public void finishThisEvent() {
        Intent[] intents = createTasksIntent(event.tasksTypeEnd, event.tasksValueEnd);
        if (intents == null) {
            return;
        }
        for (Intent i : intents) {
            if (i != null) {
                context.startActivity(i);
            }
        }
    }

    public Intent[] createTasksIntent(String[] tasksType, String[] tasksValue) {
        if (tasksType == null) {
            return null;
        }
        int tasksNum = tasksType.length;
        Intent[] intents = new Intent[tasksNum];
        for (int i = 0; i < tasksNum; i++) {
            intents[i] = convertTaskToIntent(tasksType[i], tasksValue[i]);
        }
        return intents;
    }


    private Intent convertTaskToIntent(String taskType, String taskValue) {
        Intent intent = null;
        switch (taskType) {

            case "BROWSE_URL":
                intent = taskCaseBROWSE_URL(taskValue);
                break;

            case "LAUNCH_APP":
                intent = taskCaseLAUNCH_APP(taskValue);
                break;

            case "SCREEN_BRIGHTNESS":
                taskCaseSCREEN_BRIGHTNESS(taskValue);
                break;

            case "VOLUME_CHANGE":
                taskCaseVOLUME_STREAM(taskValue);
                break;

            default:
                break;

        }

        return intent;
    }

    private Intent taskCaseBROWSE_URL(String taskValue) {
        ToolFunctions toolFunctions = new ToolFunctions();
        String url = toolFunctions.textDecoder(taskValue);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }

    private Intent taskCaseLAUNCH_APP(String taskValue) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(taskValue);
        return intent;
    }

    private void taskCaseSCREEN_BRIGHTNESS(String taskValue) {
        int brightness = Integer.parseInt(taskValue);
        if (brightness < 1) {
            brightness = 1;
        }
        brightness = (int) (((double) brightness / 100.0) * 255.0);
        android.provider.Settings.System.putInt(context.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    private void taskCaseVOLUME_STREAM(String taskValue) {
        String[] volumes = taskValue.split("-");

        // Change Media Stream volume
        if (volumes[0] != "N") {
            AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            double maxVolume = (double)audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            double inputVolume = (double)Integer.parseInt(volumes[0]);
            int toVolume = (int) (inputVolume / 100.00 * maxVolume);
            System.out.println("toVolume: " + toVolume);
            audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC, toVolume, AudioManager.FLAG_SHOW_UI);
        }

        // Change Ringtone volume
        if (volumes[1] != "N") {

        }

        // Change Alarm volume
        if (volumes[2] != "N") {

        }
    }
}
