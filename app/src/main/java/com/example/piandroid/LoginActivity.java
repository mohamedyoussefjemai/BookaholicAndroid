package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;

import com.android.volley.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView newAccount;
    private TextView forgotPassword;
    private Button btnLogin;
    private EditText email;
    private EditText password;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        forgotPassword = (TextView) findViewById(R.id.forgot_pass);
        newAccount = (TextView) findViewById(R.id.create_account);
        btnLogin = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        mPreferences = getSharedPreferences(filename, MODE_PRIVATE);

    }

    public void showRegister(View view) {

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showForgotPassword(View view) {

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SendMailActivity.class);
                startActivity(intent);
            }
        });

    }

    public void Login(View view) {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("email", email.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonBody.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String mRequestBody = jsonBody.toString();


                String url = "http://192.168.1.4:3000/users/login";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);

                        if (response.toString().equals("200")) {
                            //sharedpreferences update
                            final SharedPreferences.Editor prefEditor = mPreferences.edit();


                            //get user information

                            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                            JSONObject jsonBody = new JSONObject();
                            final String mRequestBody = jsonBody.toString();
                            String url = "http://192.168.1.4:3000/users/read-user-email/" + email.getText();

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("USER CONNECTED INFORMATION ", response);
                                    String responseFormatted = response.substring(1, response.length() - 1);
                                    Log.i("LOG_RESPONSE   formatted ", responseFormatted);

                                    try {
                                        JSONObject jsonObj = new JSONObject(responseFormatted);
                                        //pref editor details user login in
                                        prefEditor.putString("id", jsonObj.get("id").toString());
                                        prefEditor.putString("username", jsonObj.get("username").toString());
                                        prefEditor.putString("birthdate", jsonObj.get("birthdate").toString());
                                        prefEditor.putString("email", jsonObj.get("email").toString());
                                        prefEditor.putString("address", jsonObj.get("address").toString());
                                        prefEditor.putString("phone", jsonObj.get("phone").toString());
                                        prefEditor.putString("messenger", jsonObj.get("messenger").toString());
                                        prefEditor.putString("sale", jsonObj.get("sale").toString());
                                        prefEditor.putString("trade", jsonObj.get("trade").toString());
                                        prefEditor.putString("image", jsonObj.get("image").toString());
                                        prefEditor.commit();
                                        Log.i("connexion data ",jsonObj.toString());


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("LOG_RESPONSE", error.toString());

                                }
                            }) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8";
                                }

                                @Override
                                public byte[] getBody() throws AuthFailureError {
                                    try {
                                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                                    } catch (UnsupportedEncodingException uee) {
                                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                                        return null;
                                    }
                                }

                            };

                            requestQueue.add(stringRequest);


                            //toast login in
                            Toast toast = Toast.makeText(LoginActivity.this, "Welcome !", Toast.LENGTH_SHORT);
                            toast.show();

                            //intent
                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                            startActivity(intent);


                        } else {
                            Toast toast = Toast.makeText(LoginActivity.this, "email or password incorrect !", Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                        Toast toast = Toast.makeText(LoginActivity.this, "email or password incorrect !", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                requestQueue.add(stringRequest);
            }

        });
    }
}
