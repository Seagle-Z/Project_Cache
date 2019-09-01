package com.ordinary.android.projectcache;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class TypeObjectModel implements Serializable {

    private String Typename;
    private transient Drawable Icon;

    public TypeObjectModel(String tName, Drawable drawable) {
        this.Typename = tName;
        this.Icon = drawable;
    }

    public String getTypename() {
        return Typename;
    }

    public void setTypename(String typename) {
        Typename = typename;
    }

    public Drawable getIcon() {
        return Icon;
    }

    public void setIcon(Drawable icon) {
        Icon = icon;
    }
}
