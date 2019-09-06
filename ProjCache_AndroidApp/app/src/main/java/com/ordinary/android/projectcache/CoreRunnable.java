package com.ordinary.android.projectcache;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// TODO: 2019-08-30 检测程序有bug，有时候即使isActivated是true也不触发，有时候是false也会触发。估计是runningEvengsID和activatedEventsID还有triggerableEventsID之间的问题

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

        this.runningEventsID = new ArrayList<>();
    }

    @Override
    public void run() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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

            }
        });

        triggerableEventsID = new ArrayList<>();
        runningEventsID = new ArrayList<>();
        events = new Events(context);
        for (; ; ) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /* TODO: 2019-08-04
                     * 让 YSL 在每次 event 新的event设置完成后在MainActivity里放一个static用来标记有events.csv有变化，
                     * 这样CoreRunnable就不需要一直读取csv文件了
                     */

                    /* After b_ysl finish add new events and modify/delete, add checking here. only update eventsList when events change */
                    if (true /* eventsListChanged */) {
                        events.updateEventsList();
                    }

                    activatedEventsID = events.getActivatedEventsIDList();
                    CoreConditionInspector cci = new CoreConditionInspector(context, events);
                    triggerableEventsID = cci.getTriggerableEventsID();

//                    List<CoreModel> curCoreModels = new ArrayList<>();
                    for (Integer i : triggerableEventsID) {
                        Event curEvent = events.getEventByID(i);
                        CoreTasksExecutor curCoreTasksExecutor =
                                new CoreTasksExecutor(context, curEvent);
                        // save the coreViewPager feature for later
//                        String curEventName = events.getEventByID(1).eventName;
//                        curCoreModels.add(new CoreModel(
//                                curCoreTasksExecutor,
//                                curEventName,//events.getEventByID(i).eventName,
//                                context.getResources().getDrawable(R.drawable.ic_menu_manage, null)
//                        ));


                        if (!runningEventsID.contains(curEvent.eventID) && curEvent.autoTrigger == true) {
                            runningEventsID.add(curEvent.eventID);
                            curCoreTasksExecutor.startThisEvent();
                        }

                    }
//                    curCoreModels.add(coreModelDefaultEvent);
//                    CoreModelAdapter coreModelAdapter = new CoreModelAdapter(context, curCoreModels);
//                    coreViewPager.setAdapter(coreModelAdapter);



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
                Thread.sleep(2000);     // every this time long, check the new status
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

