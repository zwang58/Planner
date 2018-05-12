package com.zwang58.planner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity {
    TextView title_content, time_content, date_content, gps_content, desc_content;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title_content = findViewById(R.id.title_content);
        time_content = findViewById(R.id.time_content);
        date_content = findViewById(R.id.date_content);
        gps_content = findViewById(R.id.gps_content);
        desc_content = findViewById(R.id.desc_content);
        delete = findViewById(R.id.delete_button);
        final int index;

        Intent i = getIntent();
        index = i.getIntExtra("index", -1);
        title_content.setText(i.getStringExtra("title"));
        desc_content.setText(i.getStringExtra("desc"));
        gps_content.setText(i.getStringExtra("gps"));
        time_content.setText(i.getStringExtra("time"));
        date_content.setText(i.getStringExtra("date"));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index != -1){
                    deleteEvent(index);
                }
                Intent i = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    void deleteEvent(int i){
        JSONObject jo;
        JSONArray ja;
        try{
            File f = new File(getFilesDir(), "file.ser");
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream o = new ObjectInputStream(fi);
            String j = null;
            try{
                j = (String) o.readObject();
            }
            catch(ClassNotFoundException c){
                c.printStackTrace();
            }
            try {
                jo = new JSONObject(j);
                ja = jo.getJSONArray("data");

                //remove the event from jsonArray and overwrite old data
                ja.remove(i);
                jo.put("data",ja);
                FileOutputStream fo = new FileOutputStream(f);
                ObjectOutputStream oo = new ObjectOutputStream(fo);
                String js = jo.toString();
                oo.writeObject(js);
                oo.close();
                fo.close();
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            o.close();
            fi.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        String toast = "event#" +i+ " has been deleted";
        Toast.makeText(DetailsActivity.this, toast, Toast.LENGTH_SHORT).show();
    }
}
