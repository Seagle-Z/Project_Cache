package com.ordinary.android.projectcache;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CoreThread extends Thread {

    AppCompatActivity aca;

    Context context;

    ViewPager coreViewPager;

    CoreThread(Context context, TextView testTextView, ViewPager coreViewPager) {
        this.context = context;


        this.coreViewPager = coreViewPager;

        aca = new AppCompatActivity();
    }



//    @Override
//    public void run(new Runnable) {
//
//        for (int i = 0; i < 100; i++) {
//
//
//            String url = "http://www.example.com";
//            Intent testIntent2 = new Intent(Intent.ACTION_VIEW);
//            testIntent2.setData(Uri.parse(url));
//            List<CoreModel>coreModels = new ArrayList<>();
//            coreModels.add(new CoreModel(testIntent2, context.getResources().getDrawable(R.drawable.ic_menu_camera, null),  "abc"));
//            coreModels.add(new CoreModel(testIntent2, context.getResources().getDrawable(R.drawable.ic_menu_send, null), "def"));
//            CoreModelAdapter coreModelAdapter = new CoreModelAdapter(context, coreModels);
//            coreViewPager.setAdapter(coreModelAdapter);
//
//            SystemClock.sleep(200);
//        }
//
//    }
}
