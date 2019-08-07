package com.ordinary.android.projectcache;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class CoreModel {

    private Intent[] intents;
    private String text;
    private Drawable drawable;

    public CoreModel(Intent[] intents, Drawable drawable, String text)
    {
        this.intents = intents;
        this.drawable = drawable;
        this.text = text;

    }

    public Intent[] getIntents() {
        return intents;
    }

    public void setIntents(Intent[] intents) {
        this.intents = intents;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Drawable getDrawable()
    {
        return drawable;
    }

    public void setDrawable(Drawable drawable)
    {
        this.drawable = drawable;
    }

}
