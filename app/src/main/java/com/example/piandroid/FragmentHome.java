package com.example.piandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class FragmentHome extends Fragment {

    CardViewAdapter cardViewAdapter;
    private ArrayList<MainBook> mainbooks = new ArrayList<>();
    RecyclerView recyclerView2;

    RecyclerView recyclerView;
    ArrayList<MainCategory> mainCategories;
    MainAdapter mainAdapter;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";
    ImageView comics_mangas, health_cooking, romance_newadult, tourism_travel, adventure, literature, personal_devlopment, history, youth, social_science, artmusic_cinema, humor, police_thrillers, religion_spirituality, school, sport_leisure, theater, all;
    TextView comics_mangas_text, health_cooking_text, romance_newadult_text, tourism_travel_text, adventure_text, literature_text, personal_devlopment_text, history_text, youth_text, social_science_text,
            artmusic_cinema_text, humor_text, police_thrillers_text, religion_spirituality_text, school_text, sport_leisure_text, theater_text, all_text;
    String categoryChoosed = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        //variable

        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        comics_mangas = rootView.findViewById(R.id.comics_mangas);
        comics_mangas_text = rootView.findViewById(R.id.comics_mangas_text);
        health_cooking = rootView.findViewById(R.id.health_cooking);
        health_cooking_text = rootView.findViewById(R.id.health_cooking_text);
        romance_newadult = rootView.findViewById(R.id.romance_newadult);
        romance_newadult_text = rootView.findViewById(R.id.romance_newadult_text);
        tourism_travel = rootView.findViewById(R.id.tourism_travel);
        tourism_travel_text = rootView.findViewById(R.id.tourism_travel_text);
        adventure = rootView.findViewById(R.id.adventure);
        adventure_text = rootView.findViewById(R.id.adventure_text);
        literature = rootView.findViewById(R.id.literature);
        literature_text = rootView.findViewById(R.id.literature_text);
        personal_devlopment = rootView.findViewById(R.id.personal_devlopment);
        personal_devlopment_text = rootView.findViewById(R.id.personal_devlopment_text);
        history = rootView.findViewById(R.id.history);
        history_text = rootView.findViewById(R.id.history_text);
        youth = rootView.findViewById(R.id.youth);
        youth_text = rootView.findViewById(R.id.youth_text);
        social_science = rootView.findViewById(R.id.social_science);
        social_science_text = rootView.findViewById(R.id.social_science_text);
        artmusic_cinema = rootView.findViewById(R.id.artmusic_cinema);
        artmusic_cinema_text = rootView.findViewById(R.id.artmusic_cinema_text);
        humor = rootView.findViewById(R.id.humor);
        humor_text = rootView.findViewById(R.id.humor_text);
        police_thrillers = rootView.findViewById(R.id.police_thrillers);
        police_thrillers_text = rootView.findViewById(R.id.police_thrillers_text);
        religion_spirituality = rootView.findViewById(R.id.religion_spirituality);
        religion_spirituality_text = rootView.findViewById(R.id.religion_spirituality_text);
        school = rootView.findViewById(R.id.school);
        school_text = rootView.findViewById(R.id.school_text);
        sport_leisure = rootView.findViewById(R.id.sport_leisure);
        sport_leisure_text = rootView.findViewById(R.id.sport_leisure_text);
        theater = rootView.findViewById(R.id.theater);
        theater_text = rootView.findViewById(R.id.theater_text);
        all = rootView.findViewById(R.id.all);
        all_text = rootView.findViewById(R.id.all_text);

        return rootView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final ArrayList<String> TabFavoris = new ArrayList<>();
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //all
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryChoosed = all_text.getText().toString();

                RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                JSONObject jsonBody = new JSONObject();
                final String mRequestBody2 = jsonBody.toString();


                String url = "http://192.168.1.4:3000/books/";


                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                            mainbooks.clear();

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
                                book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                Log.i("book =======================>", book.getCategory());

                                mainbooks.add(book);
                            }
                            Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                            Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                    R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                    R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                    R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                            String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                    "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                    "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                    "theater"};
                            Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                    R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                    R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                    R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                            mainCategories = new ArrayList<>();
                            for (int i = 0; i < categoryLogo.length; i++) {
                                MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                mainCategories.add(category);
                            }


                            recyclerView2.getAdapter().notifyDataSetChanged();

                            Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                            //  notifyItemRemoved(position);
                            //  notifyItemRangeChanged(position, mainbooks.size());
                            //  notifyDataSetChanged();
                            Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                            return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                            return null;
                        }
                    }

                };
                requestQueue2.add(stringRequest2);

            }
        });


        //comics and mangas
        comics_mangas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {

                    categoryChoosed = comics_mangas_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/" + categoryChoosed;


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);

                }
            }
        });

        //health cooking
        health_cooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = health_cooking_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/" + categoryChoosed;


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //romance new adult
        romance_newadult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = romance_newadult_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/romance";


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);

                }
            }
        });
        //tourism and travel
        tourism_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = tourism_travel_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/travel";


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //adventure
        adventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = adventure_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/" + categoryChoosed;


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //literature
        literature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = literature_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/" + categoryChoosed;


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //personal devlopment
        personal_devlopment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = personal_devlopment_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/devper";


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //history
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = history_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/" + categoryChoosed;


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //youth
        youth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = youth_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/" + categoryChoosed;


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //social science
        social_science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = social_science_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/social";


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
//art music and cinema
        artmusic_cinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = artmusic_cinema_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/art";


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //humor
        humor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = humor_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/" + categoryChoosed;


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //police and thrillers
        police_thrillers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = police_thrillers_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/police";


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //religion and spirituality
        religion_spirituality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = religion_spirituality_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/religion";


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //school
        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = school_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/" + categoryChoosed;


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //sport and leisure
        sport_leisure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = sport_leisure_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/sport";


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //theater
        theater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                } else {
                    categoryChoosed = theater_text.getText().toString();

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();
                    final String mRequestBody2 = jsonBody.toString();


                    String url = "http://192.168.1.4:3000/books/read-book-category/" + categoryChoosed;


                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                mainbooks.clear();

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
                                    book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                    Log.i("book =======================>", book.getCategory());

                                    mainbooks.add(book);
                                }
                                Log.i("size array =======================>", String.valueOf(mainbooks.size()));

                                Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                String[] categoryName = {"comic & mangas", "Health & cooking", "romance & new adult", "tourism & travel",
                                        "adventure", "literature", "Personal development", "History", "youth", "social Sciences", "art music & cinema",
                                        "humor", "police & thrillers", "Religion and spirituality", "school", "sport & leisure",
                                        "theater"};
                                Integer[] bookimage = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                        R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                        R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                        R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};
                                mainCategories = new ArrayList<>();
                                for (int i = 0; i < categoryLogo.length; i++) {
                                    MainCategory category = new MainCategory(categoryLogo[i], categoryName[i]);
                                    mainCategories.add(category);
                                }


                                recyclerView2.getAdapter().notifyDataSetChanged();

                                Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                //  notifyItemRemoved(position);
                                //  notifyItemRangeChanged(position, mainbooks.size());
                                //  notifyDataSetChanged();
                                Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));


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
                                return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                                return null;
                            }
                        }

                    };
                    requestQueue2.add(stringRequest2);
                }
            }
        });
        //favoris
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject jsonBody2 = new JSONObject();
        final String mRequestBody = jsonBody2.toString();

        mPreferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);

        String url2 = "http://192.168.1.4:3000/favoris/read-favoris/" + mPreferences.getString("id", null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_RESPONSE   favoris ", response);
                String responseFormatted = response.substring(1, response.length() - 1);
                Log.i("LOG_RESPONSE   formatted ", responseFormatted);


                try {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {

                        TabFavoris.add(jsonArray.getJSONObject(i).get("book").toString());
                        Log.i("Tabb favoris add id ========> ", TabFavoris.get(i));
                    }


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
        Log.i("aaaaaaaaaaaaaaaaaaaaaa", TabFavoris.toString());
        requestQueue.add(stringRequest);


        ////////////////////////

        if (categoryChoosed.equals("")) {

            mPreferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);

            if (isNetworkAvailable()) {
                Log.i("test connexion oui  ====>", String.valueOf(isNetworkAvailable()));
                if (!mPreferences.contains("home")) {
                    final SharedPreferences.Editor prefEditor = mPreferences.edit();
                    prefEditor.putString("home", null);
                    prefEditor.commit();
                }

                RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                JSONObject jsonBody = new JSONObject();
                final String mRequestBody2 = jsonBody.toString();


                String url = "http://192.168.1.4:3000/books/";
                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                Log.i("book =======================>", book.getCategory());

                                mainbooks.add(book);
                            }
                            Log.i("size array =======================>", String.valueOf(mainbooks.size()));
                            final SharedPreferences.Editor prefEditor = mPreferences.edit();
                            prefEditor.putString("home", String.valueOf(jsonArray));
                            prefEditor.commit();


                            Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                    R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                    R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                    R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};

                            // recycle view cellule
                            recyclerView2 = view.findViewById(R.id.recycler_view_books);
                            LinearLayoutManager layoutManager2 = new LinearLayoutManager(
                                    getContext(), LinearLayoutManager.VERTICAL, false
                            );
                            cardViewAdapter = new CardViewAdapter(getContext(), mainbooks, TabFavoris);

                            recyclerView2.setAdapter(cardViewAdapter);

                            recyclerView2.setItemAnimator(new DefaultItemAnimator());

                            recyclerView2.setLayoutManager(layoutManager2);
                            // fin cellule

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
                            return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                            return null;
                        }
                    }

                };
                requestQueue2.add(stringRequest2);
            } else {
                if (!mPreferences.contains("home")) {
                    final SharedPreferences.Editor prefEditor = mPreferences.edit();
                    prefEditor.putString("home", null);
                    prefEditor.commit();
                }
                Log.i("test connexion non  ====>", String.valueOf(isNetworkAvailable()));

                String JsonArray = mPreferences.getString("home", null);


                JSONArray mJSONArray = null;
                try {
                    if (JsonArray != null) {
                        mJSONArray = new JSONArray(JsonArray);
                        for (int i = 0; i < mJSONArray.length(); i++) {
                            MainBook book = new MainBook();
                            book.setId(mJSONArray.getJSONObject(i).get("id").toString());
                            book.setTitle(mJSONArray.getJSONObject(i).get("title").toString());
                            book.setAuthor(mJSONArray.getJSONObject(i).get("author").toString());
                            book.setPrice(mJSONArray.getJSONObject(i).get("price").toString());
                            book.setCategory(mJSONArray.getJSONObject(i).get("category").toString());
                            book.setStatus(mJSONArray.getJSONObject(i).get("status").toString());
                            book.setImage(mJSONArray.getJSONObject(i).get("image").toString());
                            book.setLanguage(mJSONArray.getJSONObject(i).get("language").toString());
                            book.setUser(mJSONArray.getJSONObject(i).get("user").toString());
                            book.setUsername(mJSONArray.getJSONObject(i).get("username").toString());
                            book.setBookimage(mJSONArray.getJSONObject(i).get("image").toString());
                            Log.i("book =======================>", book.getCategory());

                            mainbooks.add(book);
                        }
                        recyclerView2 = view.findViewById(R.id.recycler_view_books);
                        Integer[] categoryLogo = {R.drawable.superheroo, R.drawable.chef, R.drawable.hearts, R.drawable.travel,
                                R.drawable.traveler, R.drawable.papyrus, R.drawable.personaldevelopment, R.drawable.parchment,
                                R.drawable.youth, R.drawable.psychology, R.drawable.violin, R.drawable.humor, R.drawable.villian,
                                R.drawable.yinyang, R.drawable.whiteboard, R.drawable.barbell, R.drawable.theater};


                        LinearLayoutManager layoutManager2 = new LinearLayoutManager(
                                getContext(), LinearLayoutManager.VERTICAL, false
                        );
                        cardViewAdapter = new CardViewAdapter(getContext(), mainbooks, TabFavoris);

                        recyclerView2.setAdapter(cardViewAdapter);

                        recyclerView2.setItemAnimator(new DefaultItemAnimator());

                        recyclerView2.setLayoutManager(layoutManager2);


                    } else {
                        Toast.makeText(getContext(), "Need connexion ", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}