package com.ordinary.android.projectcache;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.util.AttributeSet;


public class CustomEventSetupViewPager extends ViewPager {

    public CustomEventSetupViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
