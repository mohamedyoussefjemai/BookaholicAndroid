package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class ProfileActivity extends AppCompatActivity {

    private TextView name_text;
    private TextView email_txt;
    private TextView adresse_txt;
    private TextView phone_txt;
    private TextView change_password;
    private TextView logout;
    private EditText phone, email, address,input;
    private ImageButton btn_email, btn_phone,btn_address, btn_edit;
    private View icon_lib;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btn_edit = (ImageButton) findViewById(R.id.btn_edit);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
builder.setTitle("Update Username");
builder.setIcon(R.drawable.psychology);
builder.setMessage("Please write your new username to complete the update");
input = new EditText(this);
builder.setView(input);

//SET POSITIVE BUTTON
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = input.getText().toString();

//request update username
                if (txt.length()==0) {
                    input.setError("username must be not null");
                } else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("username", input.getText().toString());
                        final String mRequestBody = jsonBody.toString();

                        String url = "http://192.168.1.4:3000/users/update-user-username/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(ProfileActivity.this, "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("username", input.getText().toString());
                                prefEditor.commit();
                                Log.i("shared ==========>", mPreferences.getString("username", null));
                                Log.i("text envoyé ==========>", input.getText().toString());

                                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                startActivity(intent);

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(ProfileActivity.this, "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }






            }
        });
        //negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //create the dialog
        final AlertDialog ad = builder.create();

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name_text = (TextView) findViewById(R.id.name_text);
        email_txt = (TextView) findViewById(R.id.email_txt);
        adresse_txt = (TextView) findViewById(R.id.adresse_txt);
        phone_txt = (TextView) findViewById(R.id.phone_txt);
        change_password = (TextView) findViewById(R.id.change_password);
        logout = (TextView) findViewById(R.id.logout);
        mPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        btn_email = (ImageButton) findViewById(R.id.btn_email);
        btn_phone = (ImageButton) findViewById(R.id.btn_phone);
        btn_address = (ImageButton) findViewById(R.id.btn_addr);
        phone = (EditText) findViewById(R.id.tel);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.adresse);

        name_text.setText(mPreferences.getString("username", null));
        email_txt.setText(mPreferences.getString("email", null));
        adresse_txt.setText(mPreferences.getString("address", null));
        phone_txt.setText(mPreferences.getString("phone", null));
        //image action
        icon_lib= (View) findViewById(R.id.icon_lib);

    }

    public void showForgetPassword(View view) {

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SendMailActivity.class);
                startActivity(intent);
            }
        });

    }
    public void ShowLib(View view) {

        icon_lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
    public void Logout(View view) {

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                prefEditor.remove("id");
                prefEditor.remove("username");
                prefEditor.remove("birthdate");
                prefEditor.remove("email");
                prefEditor.remove("phone");
                prefEditor.remove("sale");
                prefEditor.remove("trade");
                prefEditor.remove("image");
                prefEditor.clear();
                prefEditor.commit();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    int count = 0;

    @Override
    public void onBackPressed() {
        count++;
        if (count == 9999) {
            super.onBackPressed();
        }
    }

    //update phone
    public void updatePhone(View view) {
        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().toString().length() != 8) {
                    phone.setError("phone length must be 8");
                } else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("phone", phone.getText().toString());
                        final String mRequestBody = jsonBody.toString();

                        String url = "http://192.168.1.4:3000/users/update-user-phone/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(ProfileActivity.this, "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("phone", phone.getText().toString());
                                prefEditor.commit();
                                Log.i("shared ==========>", mPreferences.getString("phone", null));
                                Log.i("text envoyé ==========>", phone.getText().toString());

                                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                startActivity(intent);

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(ProfileActivity.this, "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });

    }

    //update email
    public void updateEmail(View view) {
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().length() == 0) {
                    email.setError("email is required!");
                } else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("email", email.getText().toString());
                        final String mRequestBody = jsonBody.toString();

                        String url = "http://192.168.1.4:3000/users/update-user-email/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(ProfileActivity.this, "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("email", email.getText().toString());
                                prefEditor.commit();
                                Log.i("shared ==========>", mPreferences.getString("email", null));
                                Log.i("text envoyé ==========>", email.getText().toString());

                                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                startActivity(intent);

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(ProfileActivity.this, "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });

    }

    //update email
    public void updateAddress(View view) {
        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address.getText().toString().length() == 0) {
                    address.setError("address is required!");
                } else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("address", address.getText().toString());
                        final String mRequestBody = jsonBody.toString();

                        String url = "http://192.168.1.4:3000/users/update-user-address/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(ProfileActivity.this, "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("address", address.getText().toString());
                                prefEditor.commit();
                                Log.i("shared ==========>", mPreferences.getString("address", null));
                                Log.i("text envoyé ==========>", address.getText().toString());

                                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                startActivity(intent);

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(ProfileActivity.this, "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });

    }

    //update username
    public void updateUsername(View view) {
        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().toString().length() != 8) {
                    phone.setError("phone length must be 8");
                } else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("phone", phone.getText().toString());
                        final String mRequestBody = jsonBody.toString();

                        String url = "http://192.168.1.4:3000/users/update-user-phone/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(ProfileActivity.this, "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("phone", phone.getText().toString());
                                prefEditor.commit();
                                Log.i("shared ==========>", mPreferences.getString("phone", null));
                                Log.i("text envoyé ==========>", phone.getText().toString());

                                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                startActivity(intent);

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(ProfileActivity.this, "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });

    }
}