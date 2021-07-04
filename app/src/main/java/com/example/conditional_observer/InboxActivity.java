package com.example.conditional_observer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InboxActivity extends AppCompatActivity {

    public void back(View view) {
        this.finish();
    }

    // Sad about this method
    public ArrayList<String> filtration(JSONArray response) throws JSONException {
        String organization = "";
        ArrayList<String> colleagues = new ArrayList<String>();

        // finding current user's organization
        for (int i = 0; i < response.length(); i++) {
            JSONObject current = response.getJSONObject(i);

            if (current.getString("email").equals(getIntent().getStringExtra("email"))) {
                organization = current.getString("organization");
                break;
            }
        }
        // finding current user's colleague
        for (int i = 0; i < response.length(); i++) {
            JSONObject current = response.getJSONObject(i);

            if (current.getString("organization").equals(organization) && !current.getString("email").equals(getIntent().getStringExtra("email"))) {
                String colleague = current.getString("email");
                colleagues.add(colleague);
            }
        }

        return colleagues;
    }

    public void aaa(String[] str) {
        Spinner userListDropdown = findViewById(R.id.response_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, str);
        userListDropdown.setAdapter(adapter);
    }

    public void getOrganizationUsers() {
        String url ="http://192.168.1.4:8000/api/users/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<String> resp = filtration(response);
                    String[] preColleagues = new String[filtration(response).size()];
                    for (int i = 0; i < filtration(response).size(); i++) {
                        preColleagues[i] = resp.get(i);
                    }

                    aaa(preColleagues);
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

    public void sendMessage() {
        TextView textView = findViewById(R.id.response_text);
        String messageText = textView.getText().toString();

        String url ="http://192.168.1.4:8000/api/inbox/";

        Spinner userListDropdown = findViewById(R.id.response_list);
        String user = userListDropdown.getSelectedItem().toString();
        JSONObject body = new JSONObject();

        try {
            body.put("user", 2);
            body.put("sender", getIntent().getStringExtra("email"));
            body.put("topic", "Personal message");
            body.put("demand", "false");
            body.put("text", messageText);
            body.put("recipient", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(InboxActivity.this, "Message was sent", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_inbox);

        getOrganizationUsers();

        TextView from, topic, letter;
        String[] message = (String[]) getIntent().getSerializableExtra("message");

        letter = findViewById(R.id.letter_text);
        from = findViewById(R.id.from_label);
        topic = findViewById(R.id.topic_label);

        letter.setMovementMethod(new ScrollingMovementMethod());

        letter.setText(message[3]);
        from.setText(from.getText() + message[0]);
        topic.setText(topic.getText() + message[2]);


        ImageButton backBtn = findViewById(R.id.back_to_dash2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back(view);
            }
        });

        Button send = findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }
}