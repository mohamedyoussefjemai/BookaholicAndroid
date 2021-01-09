package com.example.piandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    CardViewAdapter cardViewAdapter;
    private ArrayList<MainBook> mainbooks = new ArrayList<>();
    RecyclerView recyclerView2;
    ArrayList<String> TabFavoris = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<MainCategory> mainCategories;
    MainAdapter mainAdapter;

    public HomeActivity() {
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
/////////////////

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        JSONObject jsonBody = new JSONObject();
        final String mRequestBody = jsonBody.toString();


        String url = "http://192.168.1.4:3000/books/";


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
                        MainBook book = new MainBook();
                        book.setId(jsonArray.getJSONObject(i).get("id").toString());
                        book.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                        book.setAuthor(jsonArray.getJSONObject(i).get("author").toString());
                        book.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                        book.setCategory(jsonArray.getJSONObject(i).get("category").toString());
                        book.setStatus(jsonArray.getJSONObject(i).get("status").toString());
                        book.setImage(jsonArray.getJSONObject(i).get("image").toString());
                        book.setLanguage(jsonArray.getJSONObject(i).get("language").toString());
                        book.setUser(jsonArray.getJSONObject(i).get("user").toString());
                        book.setUsername(jsonArray.getJSONObject(i).get("username").toString());
//                        book.setBookimage(R.drawable.book3);
                        Log.i("book =======================>", book.getCategory());

                        mainbooks.add(book);
                    }
                    Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                    recyclerView = findViewById(R.id.recycler_view);
                    recyclerView2 = findViewById(R.id.recycler_view_books);
                    Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                            R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                            R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                            R.drawable.yinyang,R.drawable.whiteboard,R.drawable.barbell,R.drawable.theater};
                    String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                            "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                            "humor", "police & thrillers", "Religion and spirituality","school","sport & leisure",
                            "theater"};
                    Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                            R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                            R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                            R.drawable.yinyang,R.drawable.whiteboard,R.drawable.barbell,R.drawable.theater};
                    mainCategories = new ArrayList<>();
                    for (int i = 0; i < categoryLogo.length; i++) {
                        MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                        mainCategories.add(category);
                    }

                    mainAdapter = new MainAdapter(HomeActivity.this, mainCategories);
                    recyclerView.setAdapter(mainAdapter);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(
                            HomeActivity.this, LinearLayoutManager.HORIZONTAL, false
                    );
                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(
                            HomeActivity.this, LinearLayoutManager.VERTICAL, false
                    );
                    mainAdapter = new MainAdapter(HomeActivity.this, mainCategories);
                    cardViewAdapter = new CardViewAdapter(HomeActivity.this, mainbooks,TabFavoris);

                    recyclerView.setAdapter(mainAdapter);
                    recyclerView2.setAdapter(cardViewAdapter);

                    recyclerView2.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView2.setLayoutManager(layoutManager2);


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

    }


}

