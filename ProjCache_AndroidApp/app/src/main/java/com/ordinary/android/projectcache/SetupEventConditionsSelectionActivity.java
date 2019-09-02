package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SetupEventConditionsSelectionActivity
        extends AppCompatActivity implements TypeObjectAdapter.mOnItemClickListener {

    private final int REQUEST_INFORMATION_CODE = 1001;
    private Map<String, String> conditions = new Hashtable<>();

    private final String GPS = "GPS Location";
    private final String BT = "Bluetooth";
    private final String WIFI = "Wi-Fi Connection";
    private final String Time = "Time";
    private final String OS_APP = "On-Screen App";

    private Context context;
    private RecyclerView conditionOptionRV;
    private RecyclerView.Adapter adapterForconditionOptionRV;
    private List<TypeObjectModel> conditionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event_conditions_selection);
        context = SetupEventConditionsSelectionActivity.this;
        conditions.put("LOCATION", GPS);
        conditions.put("BLUETOOTH", BT);
        conditions.put("WIFI", WIFI);
        conditions.put("TIME", Time);
        conditions.put("ON_SCREEN_APP", OS_APP);

        conditionOptionRV = (RecyclerView) findViewById(R.id.condition_types);
        conditionOptionRV.setLayoutManager(new LinearLayoutManager(context));

        conditionsList.add(new TypeObjectModel(
                conditions.get("LOCATION"),
                getDrawable(R.drawable.icon_location)));
        conditionsList.add(new TypeObjectModel(
                conditions.get("BLUETOOTH"),
                getDrawable(R.drawable.icon_bluetooth)));
        conditionsList.add(new TypeObjectModel(
                conditions.get("WIFI"),
                getDrawable(R.drawable.icon_wifi)));
        conditionsList.add(new TypeObjectModel(
                conditions.get("TIME"),
                getDrawable(R.drawable.icon_clock)));
//        conditionsList.add(new TypeObjectModel(
//                conditions.get("ON_SCREEN_APP"),
//                getDrawable(R.drawable.icon_application)));


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            for (String key : extras.keySet()) {
                for(int i = 0; i < conditionsList.size(); i++)
                {
                    if(conditions.get(key).equalsIgnoreCase(conditionsList.get(i).getTypename())) {
                        conditionsList.remove(i);
                        break;
                    }
                }
            }
        }

        adapterForconditionOptionRV = new TypeObjectAdapter(
                context,
                conditionsList,
                 this);

        conditionOptionRV.setAdapter(adapterForconditionOptionRV);
    }
    @Override
    public void onItemClick(int position) {
        Intent intent = null;
        switch (conditionsList.get(position).getTypename()) {
            case GPS:
                showAlertMessage();
                break;
            case BT:
                showAlertMessage();
                break;
            case WIFI:
                intent = new Intent(
                        context,
                        SetupEventConditionWifiActivity.class);
                break;
            case Time:
                intent= new Intent(
                        context,
                        SetupEventConditionDateTimeActivity.class);
                break;
            case OS_APP:
                showAlertMessage();
                break;
            default:
                Log.d("", "No Item selected");
        }
        try {
            startActivityForResult(intent, REQUEST_INFORMATION_CODE);
        }catch (NullPointerException e){}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    private void showAlertMessage()
    {
        AlertDialog.Builder alert =
                new AlertDialog.Builder(context);
        alert.setTitle("Sorry");
        alert.setMessage("Current feature is under construction. Stay close for new updates.");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}