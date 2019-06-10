package com.ordinary.projectcache.projectcache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

public class AdapterForAppList extends ArrayAdapter<InstalledAppInfo> {
    private LayoutInflater inflater;
    private PackageManager pm;
    private List<InstalledAppInfo> apps;

    public AdapterForAppList(Context context, List<InstalledAppInfo> apps)
    {
        super(context, R.layout.app_list_layout, apps);
        //super(context, R.layout.app_list_layout, apps);
        inflater = LayoutInflater.from(context);
        pm = context.getPackageManager();
        this.apps = apps;
    }

    @Override
    public View getView(int position, @Nullable View ConvertView, @NonNull ViewGroup parent)
    {
        InstalledAppInfo current = apps.get(position);
        View view = ConvertView;
        
        if(view == null)
        {
            /* With this inflater, the list would expand dynamically based on the number of object from
             * the List<InstalledAppInfo> apps
             */
            view = inflater.inflate(R.layout.app_list_layout, parent, false);
        }

        //Get the title and set the app label to it
        TextView textviewTitle = (TextView)view.findViewById(R.id.titleTextView);
        textviewTitle.setText(current.getLabel());

//        try{
//            PackageInfo packageInfo = pm.getPackageInfo(current.getInfo().packageName, 0);
//        }
//        catch (PackageManager.NameNotFoundException e)
//        {
//            e.printStackTrace();
//        }

        //Get the App Icon and set to the image
        ImageView imageView = (ImageView) view.findViewById(R.id.iconImage);
        Drawable background = current.getInfo().loadIcon(pm);
        imageView.setBackground(background);
        return view;
    }
}
