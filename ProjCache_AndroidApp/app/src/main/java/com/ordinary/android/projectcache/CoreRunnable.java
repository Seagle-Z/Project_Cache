package com.ordinary.android.projectcache;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CoreRunnable implements Runnable {

    private Context context;                // MainActivity's context
    private ViewPager coreViewPager;
    private Handler mainHandler;
    private Events events;
    private CoreModel coreModelDefaultEvent;
    private List<Integer> activatedEventsID;
    private List<Integer> triggerableEventsID;
    private List<Integer> runningEventsID;
    private File eventsFile;

    CoreRunnable(Context context, File eventsFile, ViewPager coreViewPager) {
        this.context = context;
        this.coreViewPager = coreViewPager;
        this.mainHandler = new Handler(context.getMainLooper());
        this.eventsFile = eventsFile;
        this.events = new Events(context, eventsFile);
        this.runningEventsID = new ArrayList<>();
    }

    @Override
    public void run() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Put the Default event into core
                CoreTasksExecutor coreTasksExecutor =
                        new CoreTasksExecutor(context, events.getDefaultEvent());

                coreModelDefaultEvent = new CoreModel(
                        coreTasksExecutor,
                        events.getDefaultEvent().eventName,
                        context.getResources().getDrawable(R.drawable.ic_menu_send, null)
                );

                List<CoreModel> coreModels = new ArrayList<>();
                coreModels.add(coreModelDefaultEvent);

                CoreModelAdapter coreModelAdapter = new CoreModelAdapter(context, coreModels);
                coreViewPager.setAdapter(coreModelAdapter);

            }
        });

        triggerableEventsID = new ArrayList<>();
        runningEventsID = new ArrayList<>();
        for (; ; ) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /* TODO: 2019-08-04
                     * 让 YSL 在每次 event 新的event设置完成后在MainActivity里放一个static用来标记有events.csv有变化，
                     * 这样CoreRunnable就不需要一直读取csv文件了
                     */
//                    if (MainActivity.eventsChanged == true) {
//                        events = new Events(context, eventsFile);
//                    }
                    events = new Events(context, eventsFile);
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
                Thread.sleep(1000);     // every this time long, check the new status
            } catch (Exception e) {
                // print out exception if needed
            }
        }
    }

    private void runOnUiThread(Runnable r) {
        mainHandler.post(r);
    }


    // Hard Code for development and testing
//    @Override
//    public void run() {
//        for (int i = 0; i < 200; i++) {
//            if (i % 2 == 0) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = "http://www.google.com";
//                        Intent testIntent2 = new Intent(Intent.ACTION_VIEW);
//                        testIntent2.setData(Uri.parse(url));
//                        List<CoreModel> coreModels = new ArrayList<>();
//                        coreModels.add(new CoreModel(
//                                testIntent2,
//                                context.getResources().getDrawable(
//                                        R.drawable.ic_menu_gallery, null),
//                                "abc"
//                        ));
//                        coreModels.add(new CoreModel(
//                                testIntent2,
//                                context.getResources().getDrawable(
//                                        R.drawable.ic_menu_share, null),
//                                "def"
//                        ));
//                        CoreModelAdapter coreModelAdapter =
//                                new CoreModelAdapter(context, coreModels);
//                        coreViewPager.setAdapter(coreModelAdapter);
//                        System.out.println("en...????");
//                    }
//                });
//
//            } else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = "http://www.bing.com";
//                        Intent testIntent2 = new Intent(Intent.ACTION_VIEW);
//                        testIntent2.setData(Uri.parse(url));
//                        List<CoreModel> coreModels = new ArrayList<>();
//                        coreModels.add(new CoreModel(
//                                testIntent2,
//                                context.getResources().getDrawable(
//                                        R.drawable.ic_menu_slideshow, null),
//                                "123"
//                        ));
//                        coreModels.add(new CoreModel(
//                                testIntent2,
//                                context.getResources().getDrawable(
//                                        R.drawable.ic_menu_manage, null),
//                                "456"
//                        ));
//                        coreModels.add(new CoreModel(
//                                testIntent2,
//                                context.getResources().getDrawable(
//                                        R.drawable.ic_menu_gallery, null),
//                                "789"
//                        ));
//                        CoreModelAdapter coreModelAdapter =
//                                new CoreModelAdapter(context, coreModels);
//                        coreViewPager.setAdapter(coreModelAdapter);
//                        System.out.println("en...!!!!");
//                    }
//                });
//            }
//            try {
//                Thread.sleep(2000);
//            } catch (Exception e) {
//            }
//            System.out.println("en...xxxx");
//        }
//    }

}

