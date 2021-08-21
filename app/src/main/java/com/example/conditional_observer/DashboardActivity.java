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

    // for messages scrollable list
    public void userMessages(JSONArray response, String content) throws JSONException {
        ArrayList<String[]> messages = new ArrayList<String[]>();
        String type = "";

        for (int i = 0; i < response.length(); i++) {
            JSONObject current = response.getJSONObject(i);
            type = "message";
            String[] inboxMessage = new String[] {
                    current.getString("sender"),
                    current.getString("recipient"),
                    current.getString("header"),
                    current.getString("text"),
            };
            messages.add(inboxMessage);
            }
        createList(messages, type);
    }
    //
    public void getMessages(final String content) {
        String url ="http://192.168.1.5:8000/api/messages/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    userMessages(response, content);
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
                params.put("Authorization", "Bearer " + getIntent().getStringExtra("jwt_token"));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


    public void userReports(JSONArray response, String content) throws JSONException {
        ArrayList<String[]> reports = new ArrayList<String[]>();
        String type = "";

        for (int i = 0; i < response.length(); i++) {
            JSONObject current = response.getJSONObject(i);
            type = "report";
            String[] reportInstance = new String[] {
                    current.getString("user"),
                    current.getString("creation_date"),
                    current.getString("danger_level"),
                    current.getString("recommendation"),
            };
            reports.add(reportInstance);
        }
        createList(reports, type);
    }

    public void getReports(final String content) {
        String url ="http://192.168.1.5:8000/api/reports/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    userReports(response, content);
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
                params.put("Authorization", "Bearer " + getIntent().getStringExtra("jwt_token"));
                params.put("email", getIntent().getStringExtra("email"));
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
                getMessages("messages");
            }
        });
    }
}