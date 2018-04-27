package com.zwang58.planner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class InputActivity extends AppCompatActivity {

    private EditText title, desc;
    private Button create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        title = findViewById(R.id.title_input);
        desc = findViewById(R.id.desc_input);
        create = findViewById(R.id.create_button);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = title.getText().toString();
                String d = desc.getText().toString();
                JSONObject item = new JSONObject();
                Date now = Calendar.getInstance().getTime();

                try {
                    item.put("title",t);
                    item.put("desc",d);
                    item.put("time",now.getTime());

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });




    }
}
