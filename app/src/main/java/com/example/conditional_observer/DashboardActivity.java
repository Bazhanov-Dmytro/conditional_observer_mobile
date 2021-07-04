package com.example.conditional_observer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    public void logot(View view) {
        Intent i = new Intent(this, MainActivity.class);
        this.finish();
        startActivity(i);
    }

    // Transmit JSON into next activity
    public void createList(ArrayList<String[]> reports, String type) {
        Intent i = new Intent(this, scrollableListActivity.class);
        i.putExtra("type", type);
        i.putExtra("email", getIntent().getStringExtra("email"));
        i.putExtra("jwt_token", getIntent().getStringExtra("jwt_token"));
        i.putExtra(type, reports);
        startActivity(i);
    }

    // Filter reports
    // Separate JSON into arrays
    public void userFiltration(JSONArray response, String content) throws JSONException {
        ArrayList<String[]> reports = new ArrayList<String[]>();
        String type = "";

        for (int i = 0; i < response.length(); i++) {
            JSONObject current = response.getJSONObject(i);

            if (current.getString("user").equals(getIntent().getStringExtra("email")) && content.equals("reports")) {
                type = "report";
                String[] report = new String[] {
                        current.getString("date"),
                        current.getString("user"),
                        current.getString("organization"),
                        current.getString("temperature"),
                        current.getString("upper_pressure"),
                        current.getString("lower_pressure"),
                        current.getString("pulse"),
                        current.getString("recommendations"),
                };
                reports.add(report);
            } else if (content.equals("inbox")) {
                type = "message";
                String[] inboxMessage = new String[] {
                        current.getString("sender"),
                        current.getString("recipient"),
                        current.getString("topic"),
                        current.getString("text"),
                };
                reports.add(inboxMessage);
            }
        }
        createList(reports, type);
    }

    public void getReports(final String content) {
        String url ="http://192.168.1.4:8000/api/" + content + "/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    userFiltration(response, content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "JWT " + getIntent().getStringExtra("jwt_token"));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button logoutBtn = findViewById(R.id.logout_button);
        Button messagesBtn = findViewById(R.id.messages_button);
        Button reportBtn = findViewById(R.id.reports_button);
        Button infoBtn = findViewById(R.id.info_button);

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, getIntent().getStringExtra("jwt_token"), Toast.LENGTH_SHORT).show();
            }
        });
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReports("reports");
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logot(view);
            }
        });
        messagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReports("inbox");
            }
        });
    }
}