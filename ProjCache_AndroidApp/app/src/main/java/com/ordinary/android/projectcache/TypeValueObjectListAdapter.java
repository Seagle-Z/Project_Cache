package com.ordinary.android.projectcache;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TypeValueObjectListAdapter extends ArrayAdapter<TypeValueObjectModel> {

    private LayoutInflater inflater;
    private List<TypeValueObjectModel> List;

    public TypeValueObjectListAdapter(Context context, List<TypeValueObjectModel> list) {
        super(context, R.layout.layout_type_value, list);
        inflater = LayoutInflater.from(context);
        this.List = list;

    }

    @Override
    public View getView(int position, @Nullable View ConvertView, @NonNull ViewGroup parent) {
        View view = ConvertView;
        TypeValueObjectModel current = List.get(position);
        if(view == null)
        {
            /* With this inflater, the list would expand dynamically based on the number of object
               from the List<AppInfoModel> apps
             */
            view = inflater.inflate(R.layout.layout_type_value, parent, false);
        }

        //Get the title and set the app label to it
        TextView tNameTextView = (TextView) view.findViewById(R.id.selected_type);
        tNameTextView.setText(current.getTypename());

        TextView valTextView = (TextView) view.findViewById(R.id.selected_value);

        ImageView imageView = (ImageView) view.findViewById(R.id.iconImage);
        Drawable background = current.getIcon();
        imageView.setBackground(background);

        return view;
    }
}
