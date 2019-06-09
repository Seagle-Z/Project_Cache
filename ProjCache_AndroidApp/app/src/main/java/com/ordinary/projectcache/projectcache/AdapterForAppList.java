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
    LayoutInflater inflater;
    PackageManager pm;
    List<InstalledAppInfo> apps;

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
            view = inflater.inflate(R.layout.app_list_layout, parent, false);

        }

        TextView textviewTitle = (TextView)view.findViewById(R.id.titleTextView);
        textviewTitle.setText(current.label);

        try{
            PackageInfo packageInfo = pm.getPackageInfo(current.info.packageName, 0);
//            if(!TextUtils.isEmpty(packageInfo.versionName))
//            {
//                String versionInfo = String.format("%s", packageInfo.versionName);
//                TextView textVersion = (TextView) view.findViewById(R.id.versionID);
//                textVersion.setText(versionInfo);
//            }

//            if(!TextUtils.isEmpty(current.info.packageName))
//            {
//                TextView textSub = (TextView)view.findViewById(R.id.subTitle);
//                textSub.setText(current.info.packageName);
//            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.iconImage);
        Drawable background = current.info.loadIcon(pm);
        imageView.setBackground(background);
        return view;
    }
}
