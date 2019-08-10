package com.ordinary.android.projectcache;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class CoreModel {

    private CoreTasksExecutor coreTasksExecutor;
    private String text;
    private Drawable drawable;

    public CoreModel(CoreTasksExecutor coreTasksExecutor, String text, Drawable drawable) {
        this.coreTasksExecutor = coreTasksExecutor;
        this.text = text;
        this.drawable = drawable;
    }

    public CoreTasksExecutor getCoreTasksExecutor() {
        return coreTasksExecutor;
    }

    public void setCoreTasksExecutor(CoreTasksExecutor coreTasksExecutor) {
        this.coreTasksExecutor = coreTasksExecutor;
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
