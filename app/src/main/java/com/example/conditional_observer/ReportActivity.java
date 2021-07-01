package com.example.conditional_observer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity {

    public void backToDashboard(View view) {
        Intent i = new Intent(this, DashboardActivity.class);
        this.finish();
        startActivity(i);
    }

    public void insertValues() {
        TextView email, pulse, temp, pressure, recommendation, date;

        email = findViewById(R.id.email_text);
        pulse = findViewById(R.id.pulse_text);
        temp = findViewById(R.id.temperature_text);
        pressure = findViewById(R.id.pressure_text);
        recommendation = findViewById(R.id.recommendation_text);
        date = findViewById(R.id.date_text);

        String[] report = (String[]) getIntent().getSerializableExtra("report");
        email.setText(email.getText() + " " + report[1]);
        pulse.setText(pulse.getText() + " " + report[6]);
        temp.setText(temp.getText() + " " + report[3]);
        pressure.setText(pressure.getText() + " " + report[4] + " " + report[5]);
        recommendation.setText(recommendation.getText() + " " + report[7]);
        date.setText(date.getText() + " " + report[0]);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        recommendation.setWidth(width - 160);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        insertValues();

        ImageButton backToDash = findViewById(R.id.back_to_dash2);
        backToDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToDashboard(view);
            }
        });
    }
}