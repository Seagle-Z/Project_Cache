package com.ordinary.android.projectcache;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SetupEventConditionWifiActivity extends AppCompatActivity implements TypeObjectAdapter.intentResultCollectingInterface {

    //Code for WIFISelectorActivity result request only
    private final int WIFI_PICKING_CODE = 1014;
    private final int WIFI_PERMISSION_CODE = 1030;

    //User-Interface Code
    Button addWifiButton, completeButton;
    Context wifi_picker_context;

    RecyclerView selectedWIFIRecycleView;
    RecyclerView.Adapter selectedWIFIAdap;
    private List<TypeObjectModel> selectedWIFITypeObjectModelList = new ArrayList<TypeObjectModel>();

    //Local Variables
    private ToolFunctions TF = new ToolFunctions();
    private String returnedWifi;
    private List<String> encryptedSSIDArrList = new ArrayList<String>();    //Ex) {91-24-123#43-35-62}
    private boolean editMode;
    private int selectedEditPosition;
    private AlertDialog.Builder warning;
    private String retrieveWIFIList;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_condition_wifi);

        //User-Interface for Activity
        wifi_picker_context = SetupEventConditionWifiActivity.this;
        addWifiButton = findViewById(R.id.add_wifi);
        completeButton = findViewById(R.id.wifi_picker_activity_complete_button);

        //Check Permissions
        checkPermissionStatus();

        //View Format
        selectedWIFIRecycleView = (RecyclerView) findViewById(R.id.selectedWIFIListRecycleView);
        selectedWIFIRecycleView.setLayoutManager(new LinearLayoutManager(wifi_picker_context));

        warning = new AlertDialog.Builder(wifi_picker_context);

        selectedWIFIAdap = new TypeObjectAdapter(wifi_picker_context, selectedWIFITypeObjectModelList, this);
        selectedWIFIRecycleView.setAdapter(selectedWIFIAdap);

        wifiManager = (WifiManager) wifi_picker_context.getSystemService(Context.WIFI_SERVICE);

        selectedWIFIAdap.notifyDataSetChanged();

        addWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMode = false;

                Intent intent = new Intent(wifi_picker_context, WIFISelectorActivity.class);
                startActivityForResult(intent, WIFI_PICKING_CODE);

            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!selectedWIFITypeObjectModelList.isEmpty()) {
                        String intentResult = "";

                        for (int i = 0; i < selectedWIFITypeObjectModelList.size(); i++) {                               //Unencoded List   Ex) selectedAppArrList = {UIC, Guest, DeVilla, ..}

                            // Encoding the String
                            int tempEncoded[] = TF.textEncoder(selectedWIFITypeObjectModelList.get(i).getTypename());
                            String tempString = "";

                            //Padding the string with '-' between each number
                            for (int j = 0; j < tempEncoded.length; j++) {
                                if (j != tempEncoded.length - 1)
                                    tempString = tempString + tempEncoded[j] + "-";
                                else
                                    tempString = tempString + tempEncoded[j];
                            }
                            encryptedSSIDArrList.add(tempString);
                        }

                        //Append everything in the encryptedSSIDArrList into intentResult string;
                        for (int k = 0; k < encryptedSSIDArrList.size(); k++) {
                            if (k != encryptedSSIDArrList.size() - 1)
                                intentResult = intentResult + encryptedSSIDArrList.get(k) + "#";
                            else
                                intentResult = intentResult + encryptedSSIDArrList.get(k);
                        }

                        Intent intent = new Intent();
                        intent.putExtra("WIFI", intentResult);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        AlertDialog.Builder adb = new AlertDialog.Builder(wifi_picker_context);
                        adb.setTitle("Warning");
                        adb.setMessage(
                                "Please add at least one WIFI for the event"
                        );
                        adb.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        adb.show();
                    }
                } catch (NullPointerException e) {
                }

            }
        });

        Intent intent = getIntent();
        try {
            if (intent.hasExtra("RETRIEVE")) {
                retrieveWIFIList = intent.getStringExtra("RETRIEVE");                   //Encoded String must be decoded.
                String[] wifiStringDivider = retrieveWIFIList.split("#");               //Splits each string up from '#' delim.

                for (int i = 0; i < wifiStringDivider.length; i++) {
                    String selectedEncodedString = wifiStringDivider[i];
                    String[] tempNumArray = selectedEncodedString.split("-");           // splitting string by '-'

                    int tempNumSize = tempNumArray.length;
                    int tempNum[] = new int[tempNumSize];

                    //Converting String Integer Array into Integer Integer Array
                    for (int z = 0; z < tempNumArray.length; z++) {
                        tempNum[z] = Integer.parseInt(tempNumArray[z]);
                    }

                    selectedWIFITypeObjectModelList.add(new TypeObjectModel(TF.textDecoder(tempNum), getDrawable(R.drawable.icon_condition_wifi))); //Add Decoded String onto the wifiList
                }

                selectedWIFIAdap.notifyDataSetChanged();
            }
        } catch (NullPointerException e) {
        }

    } //End of onCreate()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == WIFI_PICKING_CODE && resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("WIFI")) {
                    returnedWifi = data.getStringExtra("WIFI");
                    if (returnedWifi != null) {
                        if (!checkDuplicate(returnedWifi, selectedWIFITypeObjectModelList)) {
                            if (!editMode) {
                                //selectedWifiArrList.add(returnedWifi);                //Places the data from the WifiSelectorActivity.java into the arraylist
                                selectedWIFITypeObjectModelList.add(new TypeObjectModel(returnedWifi, getDrawable(R.drawable.icon_condition_wifi)));
                            } else {
                                selectedWIFITypeObjectModelList.set(selectedEditPosition, new TypeObjectModel(returnedWifi, getDrawable(R.drawable.icon_condition_wifi)));
                            }
                            selectedWIFIAdap.notifyDataSetChanged();
                        } else
                            Toast.makeText(
                                    wifi_picker_context,
                                    "WIFI is already added",
                                    Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (NullPointerException e) {
        }

    }

    public boolean checkDuplicate(String wifi, List<TypeObjectModel> wifilist) {
        if (!wifilist.isEmpty()) {
            for (TypeObjectModel wifiInfoModel : wifilist) {
                if (wifiInfoModel.getTypename().equals(wifi)) {
                    warning.setTitle("Warning");
                    warning.setMessage("WIFI is already on the list.");
                    warning.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    warning.show();
                    return true;
                }
            }
        }
        return false;
    }

    private void checkPermissionStatus() {
        if (ContextCompat.checkSelfPermission(wifi_picker_context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED  && ContextCompat.checkSelfPermission(wifi_picker_context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(
                    wifi_picker_context,
                    "You have already granted this permission!",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)  && ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This Permission is needed")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(
                                    SetupEventConditionWifiActivity.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                    WIFI_PERMISSION_CODE
                            );
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    WIFI_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WIFI_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    @Override
    public void getIntent(int position) {
        selectedEditPosition = position;
        editMode = true;
        Intent intent = new Intent(wifi_picker_context, WIFISelectorActivity.class);
        startActivityForResult(intent, WIFI_PICKING_CODE);
    }
}