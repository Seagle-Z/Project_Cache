package com.ordinary.android.projectcache;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.widget.Toast;

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
        for (Intent i : intents) {
            if (i != null) {
                context.startActivity(i);
            }
        }
    }

    public void endThisEvent() {
        Intent[] intents = createTasksIntent(event.tasksTypeEnd, event.tasksValueEnd);
        for (Intent i : intents) {
            if (i != null) {
                context.startActivity(i);
            }
        }
    }

    public Intent[] createTasksIntent(String[] tasksType, String[] tasksValue) {
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

            case "VOLUME_STREAM":
                taskCaseVOLUME_STREAM(taskValue);


        }

        return intent;
    }

    private Intent taskCaseBROWSE_URL(String taskValue) {
        String url = taskValue;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }

    private Intent taskCaseLAUNCH_APP(String taskValue) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(taskValue);
        return intent;
    }

    private void taskCaseVOLUME_STREAM(String taskValue) {
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        double maxVolume = (double)audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        double inputVolume = (double)Integer.parseInt(taskValue);
        int toVolume = (int) (inputVolume / 100.00 * maxVolume);
        audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC, toVolume, AudioManager.FLAG_SHOW_UI);
    }
}
