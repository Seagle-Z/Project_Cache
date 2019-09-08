package com.ordinary.android.projectcache;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/* This class is use for Application List View. ApplicationInfo is a built-in class that
 * includes all the information for any application on the system. Including label, package name,
 * Package icon etc. */

public class AppInfoModel implements Serializable {
    private String packageName;
    private String label;
    private transient Drawable packageIcon;

    public AppInfoModel(String pName, String pLabel, Drawable pIcon)
    {
        packageName = pName;
        label = pLabel;
        packageIcon = pIcon;
    }

    public AppInfoModel(String pName, Context context)
    {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try{
            ai = pm.getApplicationInfo(pName,0);
            this.packageName = pName;
            this.label = pm.getApplicationLabel(ai).toString();
            this.packageIcon = pm.getApplicationIcon(pName);
        }catch (final PackageManager.NameNotFoundException e) {}
    }

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
