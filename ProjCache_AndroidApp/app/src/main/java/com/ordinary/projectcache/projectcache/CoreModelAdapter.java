package com.ordinary.projectcache.projectcache;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CoreModelAdapter extends PagerAdapter {

    private List<CoreModel> coreModels;
    private LayoutInflater layoutInflater;
    private Context context;

    public CoreModelAdapter(Context context, List<CoreModel> coreModels) {
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

        // set image for the core image button
        final ImageView coreImageView;
        coreImageView = view.findViewById(R.id.core_imageView);
        coreImageView.setImageDrawable(coreModels.get(position).getDrawable());

        // set text info for the current core card
        final TextView coreTextView;
        coreTextView = view.findViewById(R.id.core_textView);
        coreTextView.setText(coreModels.get(position).getText());

        // set cardView for the core card
        CardView coreCardView;
        coreCardView = view.findViewById(R.id.core_CardView);
        final int pos = position;
        coreCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(coreModels.get(pos).getIntent());
            }
        });

        // add the view to container
        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
