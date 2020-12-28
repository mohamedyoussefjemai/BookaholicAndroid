package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.R.layout.simple_dropdown_item_1line;

public class AddTradeActivity extends AppCompatActivity {

    private TextView price_text;
    private EditText price;
    private Switch visible;
    private Spinner books;
    private Button AddTrade;
    int visible_int;

    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trade);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);


        price_text = (TextView) findViewById(R.id.price_text);
        visible = (Switch) findViewById(R.id.switch1);
        price = findViewById(R.id.price);
        price.setInputType(InputType.TYPE_CLASS_NUMBER);
        books = (Spinner) findViewById(R.id.books);
        AddTrade = (Button) findViewById(R.id.AddTrade);

        //switch visisble
        visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    price_text.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    visible_int = 1;
                } else {
                    price_text.setVisibility(View.INVISIBLE);
                    price.setVisibility(View.INVISIBLE);
                    visible_int = 0;

                }
            }
        });





        List<String> spinnerArray = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(AddTradeActivity.this);
        JSONObject jsonBody = new JSONObject();
        final String mRequestBody = jsonBody.toString();

        String url = "http://10.0.2.2:3000/books/lib-book/" + mPreferences.getString("id", null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_RESPONSE   user ", response);
                String responseFormatted = response.substring(1, response.length() - 1);
                Log.i("LOG_RESPONSE   formatted ", responseFormatted);


                try {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("JSON ARRAY  ", jsonArray.toString());


                    for (int i = 0; i < jsonArray.length(); i++) {
                        spinnerArray.add(jsonArray.getJSONObject(i).get("title").toString());
                    }
                    Log.i("size array =======================>", String.valueOf(spinnerArray.size()));
                    ///////////////////// spinner set

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    books.setAdapter(adapter);

//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// (4) set the adapter on the spinner
                    books.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }

        },
                new Response.ErrorListener() {
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

        AddTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(AddTradeActivity.this);
                    final JSONObject jsonBody = new JSONObject();
                    jsonBody.put("title", books.getSelectedItem().toString());
                    Log.i("title :",books.getSelectedItem().toString());
                    jsonBody.put("type", "trade");
                    jsonBody.put("etat", "waiting");

                    if (visible_int == 0) {
                        jsonBody.put("price", 0);

                    } else {
                        jsonBody.put("price", price.getText().toString());
                        Log.i("price  :",price.getText().toString());

                    }


                    jsonBody.put("sender", mPreferences.getString("username", null));
                    Log.i("sender  :",mPreferences.getString("username", null));

                    jsonBody.put("receiver", getIntent().getStringExtra("receiver"));
                    jsonBody.put("titlechange", getIntent().getStringExtra("titlechange"));

                    Log.i("receiver  :",getIntent().getStringExtra("receiver"));

                    final String mRequestBody = jsonBody.toString();
                    Log.i("fonction =======================>", mRequestBody);

                    String url = "http://10.0.2.2:3000/requests/add-request";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("LOG_RESPONSE", response);
                            Toast toast = Toast.makeText(AddTradeActivity.this, "Add trade request Done !", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(AddTradeActivity.this, MenuActivity.class);
                            startActivity(intent);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOG_RESPONSE", error.toString());
                            Toast toast = Toast.makeText(AddTradeActivity.this, "Add trade request Failed !", Toast.LENGTH_SHORT);
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