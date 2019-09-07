package com.ordinary.android.projectcache;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// TODO: 2019-08-30 检测程序有bug，有时候即使isActivated是true也不触发，有时候是false也会触发。
//  估计是runningEvengsID和activatedEventsID还有triggerableEventsID之间的问题

public class CoreRunnable implements Runnable {

    private Context context;                // MainActivity's context
    private File eventsFile;

    private ViewPager coreViewPager;
    private Handler mainHandler;

    private Events events;
    private List<Event> eventsList;
    private List<Integer> activatedEventsID;
    private List<Integer> triggerableEventsID;
    private List<Integer> runningEventsID;

    private CoreModel coreModelDefaultEvent;


    private static final String EVENTS_FILE_NAME = "events.csv";

    CoreRunnable(Context context, File eventsFile, ViewPager coreViewPager) {

        this.coreViewPager = coreViewPager;
        this.mainHandler = new Handler(context.getMainLooper());

        this.context = context;
        this.eventsFile = eventsFile;

        this.events = new Events(context);

        this.triggerableEventsID = new ArrayList<>();
        this.runningEventsID = new ArrayList<>();
    }

    @Override
    public void run() {

        for (; ; ) {

            /* TODO: 2019-08-04
             * 让 YSL 在每次 event 新的event设置完成后在MainActivity里放一个static用来标记有events.csv有变化，
             * 这样CoreRunnable就不需要一直读取csv文件了
             */

            if (true /* eventsListChanged */) {
                events.updateEventsList();
            }

            activatedEventsID = events.getActivatedEventsIDList();
            CoreConditionInspector cci = new CoreConditionInspector(context, events);
            triggerableEventsID = cci.getTriggerableEventsID();

            for (Integer i : triggerableEventsID) {
                Event event = events.getEventByID(i);
                CoreTasksExecutor coreTasksExecutor = new CoreTasksExecutor(context, event);

                if (!runningEventsID.contains(event.eventID) && event.autoTrigger == true) {
                    runningEventsID.add(event.eventID);
                    coreTasksExecutor.startThisEvent();
                }

            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {



                    // In case needed, change the OnPageChangeListener
                    coreViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                }
            });

            try {
                Thread.sleep(1000);     // every this time long, check the new status
            } catch (Exception e) {
                // print out exception if needed
            }
        }

    }

    private void runOnUiThread(Runnable r) {
        mainHandler.post(r);
    }

    private List<Integer> updateActivatedEventsID() {


        return null;
    }


    private String printStringArray(String[] sArr) {
        if (sArr == null) {
            return "NULL";
        }
        String rets = "";
        for (int i = 0; i < sArr.length; i++) {
            rets += sArr[i];
            if (i != sArr.length - 1) {
                rets += "|";
            }
        }
        return rets;
    }
}







// Put the Default event into core
//                CoreTasksExecutor coreTasksExecutor =
//                        new CoreTasksExecutor(context, events.getDefaultEvent());
//
//                coreModelDefaultEvent = new CoreModel(
//                        coreTasksExecutor,
//                        events.getDefaultEvent().eventName,
//                        context.getResources().getDrawable(R.drawable.ic_menu_send, null)
//                );
//
//                List<CoreModel> coreModels = new ArrayList<>();
//                coreModels.add(coreModelDefaultEvent);
//
//                CoreModelAdapter coreModelAdapter = new CoreModelAdapter(context, coreModels);
//                coreViewPager.setAdapter(coreModelAdapter);

