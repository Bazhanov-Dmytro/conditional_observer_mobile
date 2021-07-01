package com.example.conditional_observer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public void login(String token, String email) {
        Intent i = new Intent(this, DashboardActivity.class);
        i.putExtra("jwt_token", token);
        i.putExtra("email", email);
        startActivity(i);
    }

    public String[] getLoginData() {
        TextInputEditText email_field = findViewById(R.id.email_field);
        String email = email_field.getText().toString();

        EditText password_field = findViewById(R.id.password_field);
        String password = password_field.getText().toString();

        return new String[]{email, password};
    }

    public void loginRequest() {
        //
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url ="http://192.168.1.4:8000/get_jwt_token/";

        final String[] credentials = getLoginData();

        JSONObject body = new JSONObject();
        try {
            body.put("email", credentials[0]);
            body.put("password", credentials[1]);

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
                            login(response.getString("token"), credentials[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRequest();
            }
        });
    }
}