package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//import android.content.BroadcastReceiver;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.net.wifi.WifiConfiguration;
//import android.net.wifi.WifiInfo;
//import java.util.Collections;
//import java.net.NetworkInterface;

public class SetupEventConditionWifiActivity extends AppCompatActivity implements TypeObjectAdapter.intentResultCollectingInterface{

    //Code for WIFISelectorActivity result request only
    private final int WIFI_PICKING_CODE = 1014;
    //ArrayList<String> StoredWifi = new ArrayList<>();

    //User-Interface Code
    Button addWifiButton, completeButton;
    //ListView selectedWIFIListView;
    Context wifi_picker_context;
    //ArrayAdapter wifiLISTAdapterView;

    RecyclerView selectedWIFIRV;
    RecyclerView.Adapter selectedWIFIAdap;
    private List<TypeObjectModel> selectedWIFITList = new ArrayList<TypeObjectModel>();


    //Local Variables
    private ToolFunctions TF = new ToolFunctions();
    private String returnedWifi;
    //private List<String> selectedWifiArrList = new ArrayList<String>();     //Ex) {UIC-WIFI, Guest, MyHome}
    private List<String> encryptedSSIDArrList = new ArrayList<String>();    //Ex) {91-24-123#43-35-62}
    private boolean editMode;
    private int selectedEditPosition;
    private AlertDialog.Builder warning;
    private String retrieveWIFIList;
    private WifiManager wifiManager;
    //private int size = 0;
    //private List<ScanResult> results;

    //Interface for Interacting with CardView
    public static interface ClickListener{
        public void onLongClick(View view,int position);
        public void onClick(View view,int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_condition_wifi);

        //User-Interface for Activity
        wifi_picker_context = SetupEventConditionWifiActivity.this;
        addWifiButton = findViewById(R.id.add_wifi);
        completeButton = findViewById(R.id.wifi_picker_activity_complete_button);
        //selectedWIFIListView = findViewById(R.id.selected_wifi_list);

        selectedWIFIRV = (RecyclerView) findViewById(R.id.selectedWIFIListRV);
        selectedWIFIRV.setLayoutManager(new LinearLayoutManager(wifi_picker_context));

        warning = new AlertDialog.Builder(wifi_picker_context);

        //selectedWIFIListView.setTextFilterEnabled(true);    //Not needed for RecyclerView

//        wifiLISTAdapterView = new ArrayAdapter<String>(
//                wifi_picker_context,
//                R.layout.layout_general_list,
//                R.id.general_list_textview_text,
//                selectedWifiArrList);

        //selectedWIFIListView.setAdapter(wifiLISTAdapterView);

        selectedWIFIAdap = new TypeObjectAdapter(wifi_picker_context, selectedWIFITList,this);

        //Touch Gesture for either Long-Press / One Click (Functionality works only for LongPress only)
        selectedWIFIRV.addOnItemTouchListener(new RecyclerTouchListener(this,
                selectedWIFIRV, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                // Toast.makeText(MainActivity.this, "Single Click on position        :"+position,
                //        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                final int localPos = position;

                warning.setTitle("Delete");
                warning.setNegativeButton("No no", null);
                warning.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(wifi_picker_context, "Deleting", Toast.LENGTH_SHORT).show();
                        //selectedAppArrList.remove(info.position);
                        selectedWIFITList.remove(localPos);
                        //appListAdapterView.notifyDataSetChanged();
                        selectedWIFIAdap.notifyDataSetChanged();
                        //TF.setListViewHeightBasedOnChildren(appListAdapterView, selectedAppListView);
                        Toast.makeText(wifi_picker_context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                warning.show();

            }
        }));


        selectedWIFIRV.setAdapter(selectedWIFIAdap);

       //registerForContextMenu(selectedWIFIListView);

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
                    if (!selectedWIFITList.isEmpty()) {
                        String intentResult = "";

                        for (int i = 0; i < selectedWIFITList.size(); i++) {                               //Unencoded List   Ex) selectedAppArrList = {UIC, Guest, DeVilla, ..}

                            // Encoding the String
                            int tempEncoded[] = TF.textEncoder(selectedWIFITList.get(i).getTypename());
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
                        intent.putExtra("Wifi", intentResult);
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
                    String[] tempNumArray = selectedEncodedString.split("-"); // splitting string by '-'

                    int tempNumSize = tempNumArray.length;
                    int tempNum[] = new int[tempNumSize];

                    //Converting String Integer Array into Integer Integer Array
                    for (int z = 0; z < tempNumArray.length; z++) {
                        tempNum[z] = Integer.parseInt(tempNumArray[z]);
                    }

                    //selectedWifiArrList.add(TF.textDecoder(tempNum)); //Add Decoded String onto the wifiList
                    selectedWIFITList.add(new TypeObjectModel(TF.textDecoder(tempNum), getDrawable(R.drawable.icon_condition_wifi)));
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
                if (data.hasExtra("wifi")) {
                    returnedWifi = data.getStringExtra("wifi");
                    if (returnedWifi != null) {
                        if (!checkDuplicate(returnedWifi, selectedWIFITList)) {
                            if (!editMode) {
                                //selectedWifiArrList.add(returnedWifi);                //Places the data from the WifiSelectorActivity.java into the arraylist
                                selectedWIFITList.add(new TypeObjectModel(returnedWifi, getDrawable(R.drawable.icon_condition_wifi)));
                            } else {
                                selectedWIFITList.set(selectedEditPosition, new TypeObjectModel(returnedWifi, getDrawable(R.drawable.icon_condition_wifi)) );
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

    @Override
    public void getIntent(int position) {
        selectedEditPosition = position;
        editMode = true;
        Intent intent = new Intent(wifi_picker_context, WIFISelectorActivity.class);
        startActivityForResult(intent, WIFI_PICKING_CODE);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}