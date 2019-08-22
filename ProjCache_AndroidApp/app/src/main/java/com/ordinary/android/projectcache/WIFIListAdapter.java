package com.ordinary.android.projectcache;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WIFIListAdapter extends ArrayAdapter<WIFIInfoModel> {
    private LayoutInflater inflater;
    private PackageManager pm;
    private List<WIFIInfoModel> wifi_list;

    public WIFIListAdapter(Context context, List<WIFIInfoModel> wifi_list) {
        super(context, R.layout.layout_wifi_list, wifi_list);
        inflater = LayoutInflater.from(context);
        pm = context.getPackageManager();
        this.wifi_list = wifi_list;
    }

    @Override
    public View getView(int position, @Nullable View ConvertView, @NonNull ViewGroup parent) {
        WIFIInfoModel current = wifi_list.get(position);
        View view = ConvertView;

        if (view == null) {
            /* With this inflater, the list would expand dynamically based on the number of object from
             * the List<AppInfoModel> apps
             */
            view = inflater.inflate(R.layout.layout_wifi_list, parent, false);
        }

        //Get the title and set the app label to it
        TextView textviewTitle = (TextView) view.findViewById(R.id.titleTextView);
        textviewTitle.setText(current.getLabel());

        return view;
    }
}

