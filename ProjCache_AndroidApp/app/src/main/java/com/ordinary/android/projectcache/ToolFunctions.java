package com.ordinary.android.projectcache;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.ImageWriter;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public Bitmap imageCompression(Context context, InputStream is)
    {
        Bitmap original = BitmapFactory.decodeStream(is);
        createDirectoryAndSaveFile(context,original, "Original");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 60, output);
        Bitmap out = BitmapFactory.decodeStream(new ByteArrayInputStream(output.toByteArray()));
        createDirectoryAndSaveFile(context, out, "After");
        return out;
    }

    public static void createDirectoryAndSaveFile(Context context, Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/sdcard/Android/data/com.ordinary.android.projectcache/barcode");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/Android/data/com.ordinary.android.projectcache/barcode");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/Android/data/com.ordinary.android.projectcache/barcode"), fileName);
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

    public int[] textEncoder(String inputString) {
        int[] asciiIntArray = new int[inputString.length()];
        for (int i = 0; i < inputString.length(); i++) {
            asciiIntArray[i] = (int) inputString.charAt(i);
        }

        return asciiIntArray;
    }

    public String textDecoder(int[] inputIntArray) {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < inputIntArray.length; i++) {
            stringBuilder.append((char) inputIntArray[i]);
        }
        return stringBuilder.toString();
    }

    // This can only decode string with "-" to separate each string integer
    // This function assume clean input
    public String textDecoder(String inputIntArrayString) {

        String[] stringList = inputIntArrayString.split("-");
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : stringList) {
            stringBuilder.append((char) Integer.parseInt(s));
        }

        return stringBuilder.toString();
    }


}


