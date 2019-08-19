package com.ordinary.android.projectcache;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class ToolFunctions {

    public static Drawable ButtonIconProcessing(Context context, PackageManager pm, AppInfoModel app) {
        Drawable dResult = null;
        try {
            Drawable d = pm.getApplicationIcon(app.getPackageName());
            int height = d.getIntrinsicHeight();
            int width = d.getMinimumWidth();
            Bitmap bitmap = null;
            if (d instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) d).getBitmap();
                dResult = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
                createDirectoryAndSaveFile(context, bitmap, app.getLabel() + ".png");
            } else if (d instanceof AdaptiveIconDrawable) {
                Drawable backgroundDr = ((AdaptiveIconDrawable) d).getBackground();
                Drawable foregroundDr = ((AdaptiveIconDrawable) d).getForeground();

                Drawable[] drr = new Drawable[2];
                drr[0] = backgroundDr;
                drr[1] = foregroundDr;

                LayerDrawable layerDrawable = new LayerDrawable(drr);

                width = layerDrawable.getIntrinsicWidth();
                height = layerDrawable.getIntrinsicHeight();

                Bitmap convertedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(convertedBitmap);

                layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                layerDrawable.draw(canvas);
                dResult = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(convertedBitmap, width, height, true));
                createDirectoryAndSaveFile(context, convertedBitmap, app.getLabel() + ".png");
            }
            // coreModels.get(0).
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return dResult;
    }

    public static void createDirectoryAndSaveFile(Context context, Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/sdcard/Android/data/com.ordinary.projectcache.projectcache");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/Android/data/com.ordinary.projectcache.projectcache");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/Android/data/com.ordinary.projectcache.projectcache"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(context, "File created", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListViewHeightBasedOnChildren(ArrayAdapter<?> arrayAdapter, ListView listView) {
        if (arrayAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            View listItem = arrayAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (arrayAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}


