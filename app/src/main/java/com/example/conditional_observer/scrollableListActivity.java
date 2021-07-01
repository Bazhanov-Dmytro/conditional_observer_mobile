package com.example.conditional_observer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class scrollableListActivity extends AppCompatActivity {

    // Modify method to create buttons with report generation date instead of static text
    // Add for each button an onClick event which triggers data displaying activity
    public void createReport(String[] report) {
        Intent i = new Intent(this, ReportActivity.class);
        i.putExtra("report", report);
        startActivity(i);
    }

    public void createButtons() {
        LinearLayout ll = (LinearLayout)findViewById(R.id.button_layout);

        final ArrayList<String[]> reports = (ArrayList<String[]>) getIntent().getSerializableExtra("reports");
        for (int i = 0; i < reports.size(); i++) {
            Button myButton = new Button(this);
            myButton.setText(Arrays.toString(new String[]{reports.get(i)[0]}));

            final int finalI = i;
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createReport(reports.get(finalI));
                }
            });

            myButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.addView(myButton);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_list);

        createButtons();
    }
}