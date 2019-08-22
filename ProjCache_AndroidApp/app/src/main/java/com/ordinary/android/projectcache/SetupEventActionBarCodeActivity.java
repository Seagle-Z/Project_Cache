package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class SetupEventActionBarCodeActivity extends AppCompatActivity {

    private Button albumButton, cameraScannerButton, completeButton;
    private Context display_barcode_context;
    private ImageView barcodeImage;
    private final int BARCODE_IMAGE_REQUEST_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_action_show_bar_code);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        display_barcode_context = SetupEventActionBarCodeActivity.this;

        cameraScannerButton = (Button) findViewById(R.id.scanner_barcode_with_camera);
        albumButton = (Button) findViewById(R.id.select_barcode_with_galley);
        completeButton = (Button) findViewById(R.id.display_barcode_activity_button);
        completeButton.setVisibility(View.INVISIBLE);
        barcodeImage = (ImageView) findViewById(R.id.barcode_image);

        cameraScannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), BARCODE_IMAGE_REQUEST_CODE);
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BARCODE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                InputStream inputStream = display_barcode_context.getContentResolver().openInputStream(data.getData());
                barcodeChecking(inputStream);
            } catch (FileNotFoundException | NullPointerException e) {
            }
        }
    }

    private void barcodeChecking(InputStream is)
    {
        final Bitmap barcode = BitmapFactory.decodeStream(is);
        final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(barcode);
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
            @Override
            public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                if(firebaseVisionBarcodes.size() < 1)
                {
                    final AlertDialog.Builder failure = new AlertDialog.Builder(display_barcode_context);
                    failure.setTitle("Scan Failure");
                    failure.setMessage(
                            "No barcode detected in this image. " +
                                    "Barcode might not be presented or clear");
                    failure.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    failure.show();
                }
                else
                    barcodeImage.setImageBitmap(barcode);
            }
        });
    }
}
