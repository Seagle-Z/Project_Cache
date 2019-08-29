package com.ordinary.android.projectcache;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class TypeValueObjectModel implements Serializable {
    private String Typename;
    private String Values;
    private transient Drawable Icon;

    public TypeValueObjectModel(String tName, String val, Drawable drawable)
    {
        this.Typename = tName;
        this.Values = val;
        this.Icon = drawable;
    }

    public void setTypename(String typename) {
        Typename = typename;
    }

    public String getTypename() {
        return Typename;
    }

    public void setValues(String values) {
        Values = values;
    }

    public String getValues() {
        return Values;
    }


    public void setIcon(Drawable icon) {
        Icon = icon;
    }

    public Drawable getIcon() {
        return Icon;
    }
}
