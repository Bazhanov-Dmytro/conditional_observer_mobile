package com.example.conditional_observer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class InboxActivity extends AppCompatActivity {

    public void backToDashboard(View view) {
        Intent i = new Intent(this, DashboardActivity.class);
        this.finish();
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        Spinner userListDropdown = findViewById(R.id.response_list);
        String[] items = new String[] {"John", "Bob", "Tom"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        userListDropdown.setAdapter(adapter);

        TextView letter = findViewById(R.id.letter_text);
        letter.setMovementMethod(new ScrollingMovementMethod());

        ImageButton backBtn = findViewById(R.id.back_to_dash2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToDashboard(view);
            }
        });
    }
}