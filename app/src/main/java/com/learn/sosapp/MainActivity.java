package com.learn.sosapp;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = "SOSApp";
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);


        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw = findViewById(R.id.switch1);
        sw.getThumbDrawable().setTint(Color.GREEN);
        sw.getTrackDrawable().setTint(Color.GRAY);
        sw.setTextColor(Color.BLACK);
        sw.setTextSize(16);
        sw.setTypeface(null, Typeface.BOLD);
        if (isMyServiceRunning(NewLockService.class)) {
            sw.setChecked(true);
            sw.setText("Disable run in Background:");
        }

        while (isMyServiceRunning(ShakeGestureService.class)) {
            stopService(new Intent(this, ShakeGestureService.class));
        }
        while (isMyServiceRunning(LockService.class)) {
            stopService(new Intent(this, LockService.class));
        }


        if (!isMyServiceRunning(ShakeGestureService.class)) {
            //Log.d(TAG, "Service Not running :: Shake Gesture Service");
            startService(new Intent(this, ShakeGestureService.class));
        }
        if (!isMyServiceRunning(LockService.class)) {
            //Log.d(TAG, "Service Not running :: LockService");
            startService(new Intent(this, LockService.class));
        }

        Button PoliceButton = findViewById(R.id.police);
        PoliceButton.setBackgroundColor(Color.parseColor("#00bfff"));
        PoliceButton.setTextColor(Color.WHITE);
        PoliceButton.setTextSize(18);
        PoliceButton.setTypeface(null, Typeface.BOLD);
        PoliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Google Maps
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=police+stations");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        Button HospitalButton = findViewById(R.id.hospital);
        HospitalButton.setBackgroundColor(Color.parseColor("#ff8080"));
        HospitalButton.setTextColor(Color.WHITE);
        HospitalButton.setTextSize(18);
        HospitalButton.setTypeface(null, Typeface.BOLD);
        HospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Google Maps
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=hospitals");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

//        Button trackUser = findViewById(R.id.TRACK);
//        trackUser.setBackgroundColor(Color.GREEN);
//        trackUser.setTextColor(Color.WHITE);
//        trackUser.setTextSize(18);
//        trackUser.setTypeface(null, Typeface.BOLD);
//        trackUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent map = new Intent(getApplicationContext(), TrackUser.class);
//                startActivity(map);
//            }
//        });

        Button updateContacts = findViewById(R.id.UpdateContacts);
        updateContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contacts = new Intent(getApplicationContext(), UpdateEmergencyContacts.class);
                startActivity(contacts);
            }
        });
        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("contact1", "+919935679814");
        editor.putString("contact2", "+916387784635");
        editor.putString("contact3", "+919336314008");
        editor.apply();
        long result = databaseHelper.insertData("+919935679814", "Contact1");
        if (result == -1) {
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
        }
        long result2 = databaseHelper.insertData("+916387784635", "Contact2");
        if (result2 == -1) {
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
        }
        long result3 = databaseHelper.insertData("+919336314008", "Contact3");
        if (result3 == -1) {
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
        }


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    sw.setText("Don't Run in Background");
                    Log.d(TAG, "Starting NewLockService");
                    if (!isMyServiceRunning(NewLockService.class)) {
                        startService(new Intent(getApplicationContext(), NewLockService.class));
                    }
                    Toast.makeText(getApplicationContext(), "Button On", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    sw.setText("Run App in Background");
                    stopService(new Intent(getApplicationContext(), NewLockService.class));
                    Toast.makeText(getApplicationContext(), "Button Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.d(TAG, "ON Create Ended");
    }


    @SuppressWarnings("deprecation")
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}