package com.ordinary.android.projectcache;

import android.content.Context;
import android.content.Intent;
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
    private List<Integer> triggerableEventsID;
    //public static List<Integer> runningEventsID;
    private File eventsFile;

    CoreRunnable(Context context, File eventsFile, ViewPager coreViewPager) {
        this.context = context;
        this.coreViewPager = coreViewPager;
        this.mainHandler = new Handler(context.getMainLooper());
        this.eventsFile = eventsFile;
        this.events = new Events(context, eventsFile);
    }

    @Override
    public void run() {

        /* TODO: 2019-08-04
         * 让 YSL 在每次 event 新的event设置完成后在internal storage加一个文件用于告知events class当前存在新的
         * events变动需要更新。这样CoreRunnable就不需要一直读取csv文件了
         */

        // TODO: 2019-08-09 要想办法解决好runningEvent的存储问题，想想还有没有不需要用global的方法
        // TODO: 2019-08-09 整顿events，解决default event的形式问题。目前想的是放一整个event，event ID写-1

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CoreTasksExecutor cte =
                        new CoreTasksExecutor(context, events.getDefaultEvent());

                final List<CoreModel> coreModels = new ArrayList<>();
                coreModels.add(new CoreModel(
                        cte,
                        events.getDefaultEvent().eventName,
                        context.getResources().getDrawable(R.drawable.ic_menu_send, null)
                ));

                CoreModelAdapter coreModelAdapter = new CoreModelAdapter(context, coreModels);
                coreViewPager.setAdapter(coreModelAdapter);

//                CoreTasksExecutor coreTaskExecutor =
//                        new CoreTasksExecutor(
//                                context,
//                                events.getDefaultEvent().tasksType,
//                                events.getDefaultEvent().tasksValue
//                        );
//
//                Intent[] intents = coreTaskExecutor.tasksToDo();
//
//                final List<CoreModel> defaultEventModel = new ArrayList<>();
//                defaultEventModel.add(new CoreModel(
//                        intents,
//                        context.getResources().getDrawable(R.drawable.ic_menu_send, null),
//                        "Default Event"
//                ));
//
//                CoreModelAdapter coreModelAdapter =
//                        new CoreModelAdapter(context, defaultEventModel);
//                coreViewPager.setAdapter(coreModelAdapter);

            }
        });

        triggerableEventsID = new ArrayList<>();
        MainActivity.runningEventsID = new ArrayList<>();
        for (;;) {
            // Get all actived Event
            events.updateActivatedEventsList();

            // Check if there is any events triggerable
            CoreConditionInspector cci = new CoreConditionInspector(context, events);
            triggerableEventsID = cci.getTriggerableEventsID();

            List<CoreModel> coreModels = new ArrayList<>();
            for (Integer i : triggerableEventsID) {

            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Change the coreViewPager to new current triggerable event list


                    // TODO: 2019-08-01 Call the execute event function/method to execute event's task

                    // TODO: 2019-08-01 In case needed, change the OnPageChangeListener
                    coreViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {

                        }

                        @Override
                        public void onPageSelected(int i) {
                            coreViewPager.setBackgroundColor(0x444444);
                        }

                        @Override
                        public void onPageScrollStateChanged(int i) {

                        }
                    });
                }
            });

            try {
                Thread.sleep(5000);     // every this time long, check the new status
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

