package com.ordinary.projectcache.projectcache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

public class AdapterCoreModel extends PagerAdapter {

    private List<CoreModel> coreModels;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterCoreModel(List<CoreModel> coreModels, Context context) {
        this.coreModels = coreModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return coreModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.core_item, container, false);

        ImageButton imageButtonCore;
        imageButtonCore = view.findViewById(R.id.core_imageButton);
        imageButtonCore.setImageResource(coreModels.get(position).getImage());

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
