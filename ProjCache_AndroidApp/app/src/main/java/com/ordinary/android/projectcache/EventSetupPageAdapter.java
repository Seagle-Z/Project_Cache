package com.ordinary.android.projectcache;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class EventSetupPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Context context;
    private File File;


    public EventSetupPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment Fragment, String title, Context c, File file)
    {
        mFragmentList.add(Fragment);
        mFragmentTitleList.add(title);
        context = c;
        File = file;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public Context getContext()
    {
        return context;
    }

    public File getFile()
    {
        return File;
    }
}
