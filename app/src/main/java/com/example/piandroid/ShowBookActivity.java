package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ShowBookActivity extends AppCompatActivity {

    ImageView bookimage;
    TextView title, author, category, username, status, language, price;
    RequestOptions option;
    ImageView trade, sale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bookimage = findViewById(R.id.bookimage);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        category = findViewById(R.id.category);
        username = findViewById(R.id.username);
        status = findViewById(R.id.status);
        language = findViewById(R.id.language);
        price = findViewById(R.id.price);
        trade = findViewById(R.id.trade);
        sale = findViewById(R.id.sale);

        if (getIntent().getStringExtra("hide").equals("true")) {
            trade.setVisibility(View.INVISIBLE);
            sale.setVisibility(View.INVISIBLE);

        }


        trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, AddTradeActivity.class);

                String receiver = getIntent().getStringExtra("receiver");
//                Log.i("receiver : ", receiver);
                intent.putExtra("receiver", receiver);
                intent.putExtra("titlechange", getIntent().getStringExtra("titlechange"));
                context.startActivity(intent);
            }
        });

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, AddSaleActivity.class);

                String receiver = getIntent().getStringExtra("receiver");
                Log.i("receiver : ", receiver);
                intent.putExtra("receiver", receiver);
                String str = price.getText().toString().substring(0,price.getText().toString().length()-2);
                intent.putExtra("priceSale",str);
                intent.putExtra("titlechange", getIntent().getStringExtra("titlechange"));
                context.startActivity(intent);
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = getIntent().getExtras().getString("idUser");
                Context context = v.getContext();
                Intent intent = new Intent(context, ShowUserActivity.class);
                intent.putExtra("idUser", idUser);
                context.startActivity(intent);

            }
        });

        String idBook = getIntent().getExtras().getString("idBook");
        RequestQueue requestQueue = Volley.newRequestQueue(ShowBookActivity.this);
        JSONObject jsonBody = new JSONObject();
        final String mRequestBody = jsonBody.toString();
        String url = "http://192.168.1.4:3000/books/read-book/" + idBook;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.eye).error(R.drawable.eye);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("BOOK INFORMATION ", response);
                String responseFormatted = response.substring(1, response.length() - 1);
                Log.i("LOG_RESPONSE   formatted ", responseFormatted);

                try {
                    JSONObject jsonObj = new JSONObject(responseFormatted);
                    Glide.with(ShowBookActivity.this).load("http://192.168.1.4:3000/get/image/" + jsonObj.get("image").toString()).apply(option).into(bookimage);
                    price.setText(jsonObj.get("price").toString() + " DT");
                    username.setText(jsonObj.get("username").toString());
                    title.setText(jsonObj.get("title").toString());
                    category.setText(jsonObj.get("category").toString());
                    author.setText(jsonObj.get("author").toString());
                    category.setText(jsonObj.get("category").toString());
                    status.setText(jsonObj.get("status").toString());
                    if (status.getText().equals("new")) {
                        status.setBackgroundResource(R.drawable.greenblock);
                    } else if (status.getText().equals("old")) {
                        status.setBackgroundResource(R.drawable.redblock);
                    } else {
                        status.setBackgroundResource(R.drawable.blueblock);
                    }
                    language.setText(jsonObj.get("language").toString());
                    language.setBackgroundResource(R.drawable.yellowblock);


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

