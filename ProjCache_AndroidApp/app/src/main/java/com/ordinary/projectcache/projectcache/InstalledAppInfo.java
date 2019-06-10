package com.ordinary.projectcache.projectcache;

import android.content.pm.ApplicationInfo;

/* This class is use for Application List View. ApplicationInfo is a built-in class that
 * includes all the information for any application on the system. Including label, package name,
 * Package icon etc. */

public class InstalledAppInfo {
    private ApplicationInfo info;
    private String label;

    public void setInfo(ApplicationInfo Info)
    {
        info = Info;
    }

    public void setLabel(String s)
    {
        label = s;
    }
    public ApplicationInfo getInfo()
    {
        return info;
    }
    public String getLabel() {
        return label;
    }
}
