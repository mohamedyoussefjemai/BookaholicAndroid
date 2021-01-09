package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AddSaleActivity extends AppCompatActivity {

    private TextView price_text;
    private EditText price;
    private Button AddSale;

    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);


        price_text = (TextView) findViewById(R.id.price_text);
        price = findViewById(R.id.price);
        price.setInputType(InputType.TYPE_CLASS_NUMBER);
        AddSale = (Button) findViewById(R.id.AddSale);

        AddSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(AddSaleActivity.this);
                    final JSONObject jsonBody = new JSONObject();
                    jsonBody.put("type", "sale");
                    jsonBody.put("etat", "waiting");
                    jsonBody.put("price", price.getText().toString());

                    jsonBody.put("sender", mPreferences.getString("username", null));
                    Log.i("sender  :",mPreferences.getString("username", null));

                    jsonBody.put("receiver", getIntent().getStringExtra("receiver"));
                    jsonBody.put("titlechange", getIntent().getStringExtra("titlechange"));

                    Log.i("receiver  :",getIntent().getStringExtra("receiver"));

                    final String mRequestBody = jsonBody.toString();
                    Log.i("fonction =======================>", mRequestBody);

                    String url = "http://192.168.1.4:3000/requests/add-request";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("LOG_RESPONSE", response);
                            Toast toast = Toast.makeText(AddSaleActivity.this, "Add sale request Done !", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(AddSaleActivity.this, MenuActivity.class);
                            startActivity(intent);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOG_RESPONSE", error.toString());
                            Toast toast = Toast.makeText(AddSaleActivity.this, "Add sale request Failed !", Toast.LENGTH_SHORT);
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


        });


    }
}