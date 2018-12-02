package com.example.julio.activities.sysadmin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;

public class SeeDetailsProcess extends AppCompatActivity {

    private String processDetails;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_details_process_sysadmin);

        Bundle extras = getIntent().getExtras();

        processDetails = extras.getString("processDetails");

        textView = findViewById(R.id.textView_processDetails);

        textView.setText(processDetails);


    }
}
