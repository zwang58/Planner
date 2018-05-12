package com.zwang58.planner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class InputActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private EditText title, desc;
    private Button create;
    private String gps = "Location unknown";
    public JSONObject jo = null;
    public JSONArray ja = null;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        title = findViewById(R.id.title_input);
        desc = findViewById(R.id.desc_input);
        create = findViewById(R.id.create_button);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (location != null)
                    gps = location.getLatitude() + ", " + location.getLongitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        try {
            File f = new File(getFilesDir(), "file.ser");
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream o = new ObjectInputStream(fi);
            // Notice here that we are de-serializing a String object (instead of
            // a JSONObject object) and passing the String to the JSONObject’s
            // constructor. That’s because String is serializable and
            // JSONObject is not. To convert a JSONObject back to a String, simply
            // call the JSONObject’s toString method.
            String j = null;
            try {
                j = (String) o.readObject();
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
            }
            try {
                jo = new JSONObject(j);
                ja = jo.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            // Here, initialize a new JSONObject
            jo = new JSONObject();
            ja = new JSONArray();
            try {
                jo.put("data", ja);
            } catch (JSONException j) {
                j.printStackTrace();
            }
        }

        ActivityCompat.requestPermissions
                (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);

        create.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                String t = title.getText().toString();
                String d = desc.getText().toString();
                Date now = Calendar.getInstance().getTime();
                DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
                DateFormat time = new SimpleDateFormat("hh:mm:ss a");
                date.setTimeZone(TimeZone.getTimeZone("PDT"));
                time.setTimeZone(TimeZone.getTimeZone("PDT"));

                JSONObject newEvent = new JSONObject();
                try {
                    newEvent.put("title", t);
                    newEvent.put("desc", d);
                    newEvent.put("time",time.format(now));
                    newEvent.put("date",date.format(now));
                    newEvent.put("gps", gps);
                    ja.put(newEvent);
                    jo.put("data",ja);
                }
                catch(JSONException j){
                    j.printStackTrace();
                }

                // write the file
                try{
                    File f = new File(getFilesDir(), "file.ser");
                    FileOutputStream fo = new FileOutputStream(f);
                    ObjectOutputStream o = new ObjectOutputStream(fo);
                    String j = jo.toString();
                    o.writeObject(j);
                    o.close();
                    fo.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }

                String toast = "title: " + t + "\ndesc: " + d + "\ntime: " + now.toString() + "\ngps: " + gps;
                Toast.makeText(InputActivity.this, toast, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(InputActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            locationManager.requestLocationUpdates
                    (LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
            locationManager.requestLocationUpdates
                    (LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }catch(SecurityException se){

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //Log.d(TAG, "callback");
        switch (requestCode) {
            case 99:
                // If the permissions aren't set, then return. Otherwise, proceed.
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //Log.d(TAG, "returning program");
                    gps = "Location Permission Missing";
                    return;
                }
                else{
                }
                break;
            default:
                break;
        }
    }


}
