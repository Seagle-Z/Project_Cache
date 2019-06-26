package com.ordinary.projectcache.projectcache;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/* This class is use for Application List View. ApplicationInfo is a built-in class that
 * includes all the information for any application on the system. Including label, package name,
 * Package icon etc. */

public class InstalledAppInfo implements Serializable {
    private String packageName;
    private String label;
    private transient Drawable packageIcon;


    public void setLabel(String s)
    {
        label = s;
    }

    public String getLabel() {
        return label;
    }

    public void setPackageName(String s)
    {
        packageName = s;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setDrawable(Drawable d)
    {
        packageIcon = d;
    }

    public Drawable getPackageIcon()
    {
        return packageIcon;
    }
}
