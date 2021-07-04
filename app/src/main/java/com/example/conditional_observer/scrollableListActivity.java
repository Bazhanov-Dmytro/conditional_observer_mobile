package com.example.conditional_observer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class scrollableListActivity extends AppCompatActivity {

    // Modify method to create buttons with report generation date instead of static text
    // Add for each button an onClick event which triggers data displaying activity
    public void createReport(String[] report, String type) {
        if (type.equals("report")) {
            Intent i = new Intent(this, ReportActivity.class);
            i.putExtra("report", report);
            i.putExtra("jwt_token", getIntent().getStringExtra("jwt_token"));
            startActivity(i);
        } else {
            Intent i = new Intent(this, InboxActivity.class);
            i.putExtra("message", report);
            i.putExtra("jwt_token", getIntent().getStringExtra("jwt_token"));
            i.putExtra("email", getIntent().getStringExtra("email"));
            startActivity(i);
        }
    }

    public void createButtons(final String type) {
        LinearLayout ll = (LinearLayout)findViewById(R.id.button_layout);

        final ArrayList<String[]> reports = (ArrayList<String[]>) getIntent().getSerializableExtra(type);
        for (int i = 0; i < reports.size(); i++) {
            if (getIntent().getStringExtra("type").equals("message") && reports.get(i)[1].equals(getIntent().getStringExtra("email"))) {

                // Size of code must be reduced while refactoring
                Button myButton = new Button(this);
                myButton.setText(Arrays.toString(new String[]{reports.get(i)[0]}));

                final int finalI = i;
                myButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createReport(reports.get(finalI), type);
                    }
                });

                myButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.addView(myButton);
                continue;
            }

            // Size of code must be reduced while refactoring
            if (getIntent().getStringExtra("type").equals("report")) {
                Button myButton = new Button(this);
                myButton.setText(Arrays.toString(new String[]{reports.get(i)[0]}));

                final int finalI = i;
                myButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createReport(reports.get(finalI), type);
                    }
                });

                myButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.addView(myButton);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_list);

        createButtons(getIntent().getStringExtra("type"));
    }
}