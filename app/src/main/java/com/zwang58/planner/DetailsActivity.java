package com.zwang58.planner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private TextView title_content, time_content, date_content, gps_content, desc_content;
    private Button delete;
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


    }
}
