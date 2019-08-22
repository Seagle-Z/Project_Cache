package com.ordinary.android.projectcache;


import java.io.Serializable;

/* This class is use for WIFI List View.
 */

public class WIFIInfoModel implements Serializable {
    private String packageName;
    private String label;

    public WIFIInfoModel(String pName, String pLabel) {
            packageName = pName;
            label = pLabel;
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

}
