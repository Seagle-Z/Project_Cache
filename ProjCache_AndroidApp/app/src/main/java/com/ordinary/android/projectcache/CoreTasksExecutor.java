package com.ordinary.android.projectcache;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

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
        context.startActivities(intents);
    }

    public void endThisEvent() {
        Intent[] intents = createTasksIntent(event.tasksTypeEnd, event.tasksValueEnd);
        context.startActivities(intents);
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
            /* TODO: 2019-08-04
             * 这里需要改成在events里用用urlID找url的method从存储url的csv里返回url
             */
            case "BROWSE_URL":
                intent = taskCaseBROWSE_URL(taskValue);
                break;

            case "LAUNCH_APP":
                intent = taskCaseLAUNCH_APP(taskValue);
                break;

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
}
