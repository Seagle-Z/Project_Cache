package com.ordinary.projectcache.projectcache;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;

public class CoreModel {

    private int image;
    private String text;


    public CoreModel(int image, String text) {

        this.image = image;
        this.text = text;

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
