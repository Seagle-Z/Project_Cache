package com.ordinary.projectcache.projectcache;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;

public class CoreModel {

    private int image;
    private String text;
    private Drawable drawable;

//    public CoreModel(int image, String text) {
//        this.image = image;
//        this.text = text;
//    }
//
//    public int getImage() {
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }

    public CoreModel(Drawable drawable, String text)
    {
        this.drawable = drawable;
        this.text = text;
    }

    public Drawable getDrawable()
    {
        return drawable;
    }

    public void setDrawable(Drawable D)
    {
        drawable = D;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
