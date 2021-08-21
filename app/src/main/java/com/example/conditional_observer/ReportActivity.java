package com.example.conditional_observer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity {

    public void back(View view) {
        this.finish();
    }

    public void insertValues() {
        TextView email, dangerLevel, recommendation, date;

        email = findViewById(R.id.email_text);
        dangerLevel = findViewById(R.id.danger_level_text);
        recommendation = findViewById(R.id.recommendation_text);
        date = findViewById(R.id.date_text);

        String[] report = (String[]) getIntent().getSerializableExtra("report");
        email.setText(email.getText() + " " + report[0]);
        dangerLevel.setText(dangerLevel.getText() + " " + report[2]);
        recommendation.setText(recommendation.getText() + "\n " + report[3]);
        date.setText(date.getText() + " " + report[1]);

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
                back(view);
            }
        });
    }
}