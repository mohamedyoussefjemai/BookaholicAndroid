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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SendMailActivity extends AppCompatActivity {

    private static final int MAX_LENGTH = 6;
    String ValidRandom = "";
    EditText email;
    EditText code;
    TextView code_txt;
    TextView password_txt, confirm_password_txt;
    EditText password;
    EditText confirm_password;
    Button btnSend, btnValid, btnChange;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";
    TextInputLayout password_design, confirm_password_design;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnSend = findViewById(R.id.send);
        btnValid = findViewById(R.id.valid);
        btnChange = findViewById(R.id.change);
        email = findViewById(R.id.email);
        code = findViewById(R.id.code);
        code_txt = findViewById(R.id.code_txt);
        password = findViewById(R.id.password);
        password_txt = findViewById(R.id.password_txt);
        confirm_password = findViewById(R.id.confirm_password);
        confirm_password_txt = findViewById(R.id.confirm_password_txt);
        password_design = findViewById(R.id.password_design);
        confirm_password_design = findViewById(R.id.confirm_password_design);

        mPreferences = getSharedPreferences(filename, MODE_PRIVATE);

        if (mPreferences.contains("id")) {
            email.setText(mPreferences.getString("email", ""));
        }


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String random = random();
                //  sendMail(random);
                code_txt.setVisibility(View.VISIBLE);
                code.setVisibility(View.VISIBLE);
                btnValid.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.INVISIBLE);
                ValidRandom = random;

                RequestQueue requestQueue = Volley.newRequestQueue(SendMailActivity.this);
                JSONObject jsonBody = new JSONObject();
                try {
                    try {
                        jsonBody.put("to", email.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("subject", "code vérification");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("text", "this is your verification code " + random + " write it correctly in your application. ");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final String mRequestBody = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/users/forgot";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("LOG_RESPONSE", response);

                            if (response.toString().equals("200")) {
                                Toast toast = Toast.makeText(SendMailActivity.this, "Mail sended !", Toast.LENGTH_SHORT);
                                toast.show();

                            } else {
                                Toast toast = Toast.makeText(SendMailActivity.this, "error retry ", Toast.LENGTH_SHORT);
                                toast.show();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOG_RESPONSE", error.toString());
                            Toast toast = Toast.makeText(SendMailActivity.this, "Login error else !", Toast.LENGTH_SHORT);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidRandom.equals(code.getText().toString())) {
                    password_txt.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                    password_design.setVisibility(View.VISIBLE);
                    confirm_password_design.setVisibility(View.VISIBLE);
                    confirm_password_txt.setVisibility(View.VISIBLE);
                    confirm_password.setVisibility(View.VISIBLE);
                    btnChange.setVisibility(View.VISIBLE);
                    btnValid.setVisibility(View.INVISIBLE);
                } else {
                    code_txt.setVisibility(View.INVISIBLE);
                    code.setVisibility(View.INVISIBLE);
                    btnValid.setVisibility(View.INVISIBLE);
                    confirm_password_design.setVisibility(View.INVISIBLE);
                    confirm_password_txt.setVisibility(View.INVISIBLE);
                    btnSend.setVisibility(View.VISIBLE);
                }

            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifConfirmedPassword()) {

                    RequestQueue requestQueue = Volley.newRequestQueue(SendMailActivity.this);
                    JSONObject jsonBody = new JSONObject();
                    try {
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


                        String url = "http://192.168.1.4:3000/users/update-user-email";
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG_RESPONSE", response);

                                if (response.toString().equals("200")) {
                                    Toast toast = Toast.makeText(SendMailActivity.this, "Password changed !", Toast.LENGTH_SHORT);
                                    toast.show();
                                    if (mPreferences.contains("id")) {
                                        Intent intent = new Intent(SendMailActivity.this, MenuActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(SendMailActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast toast = Toast.makeText(SendMailActivity.this, "Error Password not changed ! ", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(SendMailActivity.this, "Login error else !", Toast.LENGTH_SHORT);
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast toast = Toast.makeText(SendMailActivity.this, "the passwords must be equal !", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });


    }

    public static String random() {
        String chars = "1234567890"; // Tu supprimes les lettres dont tu ne veux pas
        String pass = "";
        for (int x = 0; x < MAX_LENGTH; x++) {
            int i = (int) Math.floor(Math.random() * 10); // Si tu supprimes des lettres tu diminues ce nb
            pass += chars.charAt(i);
        }
        System.out.println(pass);
        return pass;

    }

    public boolean verifConfirmedPassword() {

        if (confirm_password.getText().toString().equals(password.getText().toString()) == false) {
            confirm_password.setError("confirmpassword must be equal ");
            return false;
        }
        return true;
    }
}