package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ShowUserActivity extends AppCompatActivity {

    TextView username, email, phone, address, trade, sale, contact;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";
    ImageView image;
    RequestOptions option = new RequestOptions().centerCrop().placeholder(R.drawable.bookmale2).error(R.drawable.bookmale2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        trade = findViewById(R.id.trade);
        sale = findViewById(R.id.sale);
        image = findViewById(R.id.bookimage);
        contact = findViewById(R.id.contact);


        String idUser = getIntent().getExtras().getString("idUser");


        RequestQueue requestQueue = Volley.newRequestQueue(ShowUserActivity.this);
        JSONObject jsonBody = new JSONObject();
        final String mRequestBody = jsonBody.toString();

        String url = "http://10.0.2.2:3000/users/read-user/" + idUser;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("BOOK INFORMATION ", response);
                String responseFormatted = response.substring(1, response.length() - 1);
                Log.i("LOG_RESPONSE   formatted ", responseFormatted);

                try {
                    JSONObject jsonObj = new JSONObject(responseFormatted);


                    username.setText(jsonObj.get("username").toString());

                    email.setText("Email : " + jsonObj.get("email").toString());

                    phone.setText("Phone : " + jsonObj.get("phone").toString());

                    address.setText("Address : " + jsonObj.get("address").toString());

                    trade.setText("Trade : " + jsonObj.get("trade").toString());

                    sale.setText("Sale : " + jsonObj.get("sale").toString());


                    if (mPreferences.getString("messenger", null).equals("null")) {
                        contact.setVisibility(View.INVISIBLE);
                    }


                    contact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                String url;
                                url = "https://www.messenger.com/t/" + jsonObj.get("messenger").toString();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    if (mPreferences.getString("image", null) == null) {
                        image.setBackgroundResource(R.drawable.bookmale2);
                    } else {
                        String nameImage = mPreferences.getString("image", null);
                        Glide.with(ShowUserActivity.this).load("http://10.0.2.2:3000/get/image/" + jsonObj.get("image").toString()).apply(option).into(image);

                    }


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
    }
}
