package com.ordinary.android.projectcache;


import java.io.Serializable;

/* This class is use for WIFI List View.
 */

public class WIFIInfoModel implements Serializable {
    private String wifiName;
    private String macAddr;

    public WIFIInfoModel(String wName, String mAddr) {
        wifiName = wName;
        macAddr = mAddr;
    }

    public void setMacAddr(String s) {
        macAddr = s;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setWIFIName(String s) {
        wifiName = s;
    }

    public String getWIFIName() {
        return wifiName;
    }

}
