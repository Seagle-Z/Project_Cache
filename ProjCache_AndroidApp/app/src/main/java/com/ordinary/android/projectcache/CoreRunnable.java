package com.ordinary.android.projectcache;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CoreRunnable implements Runnable {

    private Context context;                // MainActivity's context
    private ViewPager coreViewPager;
    private Handler mainHandler;
    private Events events;
    private File eventsFile;

    CoreRunnable(Context context, File eventsFile, ViewPager coreViewPager) {
        this.context = context;
        this.coreViewPager = coreViewPager;
        this.mainHandler = new Handler(context.getMainLooper());
        this.eventsFile = eventsFile;
    }

    @Override
    public void run() {
        // TODO: 2019-08-01 User Looper instead of the for loop
        for (;;) {
            events = new Events(context, eventsFile);
            // TODO: 2019-08-01 Check if there is any event happen

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO: 2019-08-01 Change the coreViewPager to new current event list

                    // TODO: 2019-08-01 Call the execute event function/method to execute event's task

                    // TODO: 2019-08-01 In case needed, change the OnPageChangeListener
//                    coreViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                        @Override
//                        public void onPageScrolled(int i, float v, int i1) {
//
//                        }
//
//                        @Override
//                        public void onPageSelected(int i) {
//
//                        }
//
//                        @Override
//                        public void onPageScrollStateChanged(int i) {
//
//                        }
//                    });
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
//                        coreModels.add(new CoreModel(testIntent2, context.getResources().getDrawable(R.drawable.ic_menu_gallery, null),  "abc"));
//                        coreModels.add(new CoreModel(testIntent2, context.getResources().getDrawable(R.drawable.ic_menu_share, null), "def"));
//                        CoreModelAdapter coreModelAdapter = new CoreModelAdapter(context, coreModels);
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
//                        coreModels.add(new CoreModel(testIntent2, context.getResources().getDrawable(R.drawable.ic_menu_slideshow, null), "123"));
//                        coreModels.add(new CoreModel(testIntent2, context.getResources().getDrawable(R.drawable.ic_menu_manage, null), "456"));
//                        coreModels.add(new CoreModel(testIntent2, context.getResources().getDrawable(R.drawable.ic_menu_gallery, null),  "789"));
//                        CoreModelAdapter coreModelAdapter = new CoreModelAdapter(context, coreModels);
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

