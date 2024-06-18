package com.example.merisuraksha.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.merisuraksha.Adapter.CustomAdapter;
import com.example.merisuraksha.Domain.ContactModel;
import com.example.merisuraksha.Domain.DbHelper;
import com.example.merisuraksha.R;
import com.example.merisuraksha.ShakeServices.ReactivateService;
import com.example.merisuraksha.ShakeServices.SensorService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class SmsActivity extends AppCompatActivity {

    private static final int IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002;
    private static final int PICK_CONTACT = 1;

    // Variables for UI elements and data handling
    private Button button1;
    private ListView listView;
    private DbHelper db;
    private List<ContactModel> list;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        // Start the sensor service
        startSensorService();

        // Initialize UI elements and data
        initUI();

        // Handle button click event
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call getContacts() method
                if (db.count() != 5) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                } else {
                    Toast.makeText(SmsActivity.this, "Can't Add more than 5 Contacts", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to check if the sensor service is running
    private void startSensorService() {
        SensorService sensorService = new SensorService();
        Intent intent = new Intent(this, sensorService.getClass());
        if (!isMyServiceRunning(sensorService.getClass())) {
            startService(intent);
        }
    }

    // Method to initialize UI elements and data
    private void initUI() {
        button1 = findViewById(R.id.Button1);
        listView = findViewById(R.id.ListView);
        db = new DbHelper(this);
        list = db.getAllContacts();
        customAdapter = new CustomAdapter(this, list);
        listView.setAdapter(customAdapter);
    }

    // Method to check if the service is running
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }

    @Override
    protected void onDestroy() {
        // Broadcast intent to restart service
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, ReactivateService.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Handle permission request results
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permissions Denied!\n Can't use the App!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle result of contact picking
        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK && data != null) {
            processContactData(data.getData());
        }
    }

    // Method to process selected contact data
    @SuppressLint("Range")
    private void processContactData(Uri contactData) {
        Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String phone = null;
            try {
                if (hasPhone != null && hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    if (phones != null && phones.moveToFirst()) {
                        phone = phones.getString(phones.getColumnIndex("data1"));
                        phones.close();
                    }
                }
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                db.addcontact(new ContactModel(0, name, phone));
                list = db.getAllContacts();
                customAdapter.refresh(list);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    // Method to prompt user to remove battery optimization
    private void askIgnoreOptimization() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Battery Optimization")
                .setMessage("Please disable battery optimization for this app to ensure proper functionality.")
                .setPositiveButton("Go to Settings", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sos_menu, menu);
        return true;
    }

  //  @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        startActivity(new Intent(getApplicationContext(), SmsBtnInst.class));
//        return true;
//    }
}
