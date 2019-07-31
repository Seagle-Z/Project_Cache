package com.ordinary.projectcache.projectcache;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;

public class CoreModel {

    private Intent intent;
    private String text;
    private Drawable drawable;

    public CoreModel(Intent intent, Drawable drawable, String text)
    {
        this.intent = intent;
        this.drawable = drawable;
        this.text = text;

    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
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
