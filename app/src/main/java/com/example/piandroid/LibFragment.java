package com.example.piandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

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
import java.util.Set;

import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

public class LibFragment extends Fragment implements OnTouchListener {

    CardViewLibAdapter cardViewLibAdapter;
    private ArrayList<MainBook> mainbooks = new ArrayList<>();
    RecyclerView recyclerView2;
    ArrayList<MainCategory> mainCategories;
    ArrayList<String> stringArrayList;
    private SharedPreferences mPreferences;
    private ImageView sw;
    private TextView clean;

    final String filename = "BookaholicLogin";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_lib, container, false);
        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View sw = getView().findViewById(R.id.switch1);
        clean = getView().findViewById(R.id.clean);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sw.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
            }

            public void onSwipeLeft() {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, new PostFragment())
                        .commit();
            }

            public void onSwipeBottom() {
            }

        });

        //clean cache storage
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences.Editor prefEditor = mPreferences.edit();
                prefEditor.remove("lib");
                prefEditor.commit();
                mainbooks.clear();
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragment_container, new LibFragment())
                        .commit();
                Toast.makeText(getContext(), "storage cleaned ", Toast.LENGTH_SHORT).show();

            }
        });


        ///////////
        mPreferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);

        if (isNetworkAvailable()) {
            Log.i("test connexion oui  ====>", String.valueOf(isNetworkAvailable()));
            if (!mPreferences.contains("post")) {
                final SharedPreferences.Editor prefEditor = mPreferences.edit();
                prefEditor.putString("post", null);
                prefEditor.commit();
            }
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                            book.setVisible(jsonArray.getJSONObject(i).get("visible").toString());
                            book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                            Log.i("book =======================>", book.getCategory());

                            mainbooks.add(book);
                        }
                        Log.i("size array =======================>", String.valueOf(mainbooks.size()));
                        /////////////////////lib preferences
                        final SharedPreferences.Editor prefEditor = mPreferences.edit();
                        prefEditor.putString("lib", String.valueOf(jsonArray));
                        prefEditor.commit();
                        Log.i("test lib ====>", String.valueOf(mPreferences.getString("lib", null)));


                        recyclerView2 = view.findViewById(R.id.recycler_view_books);
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

                        LinearLayoutManager layoutManager2 = new LinearLayoutManager(
                                getContext(), LinearLayoutManager.VERTICAL, false
                        );
                        cardViewLibAdapter = new CardViewLibAdapter(getContext(), mainbooks);

                        recyclerView2.setAdapter(cardViewLibAdapter);

                        recyclerView2.setItemAnimator(new DefaultItemAnimator());

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
        } else {
            if (!mPreferences.contains("post")) {
                final SharedPreferences.Editor prefEditor = mPreferences.edit();
                prefEditor.putString("post", null);
                prefEditor.commit();
            }
            Log.i("test connexion non  ====>", String.valueOf(isNetworkAvailable()));
            String JsonArray = mPreferences.getString("lib", null);


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
                        book.setVisible(mJSONArray.getJSONObject(i).get("visible").toString());
                        book.setBookimage(mJSONArray.getJSONObject(i).get("image").toString());
                        Log.i("book sans co =======================>", book.getCategory());

                        mainbooks.add(book);
                    }
                    Log.i("size array =======================>", String.valueOf(mainbooks.size()));
                    /////////////////////lib preferences

                    recyclerView2 = view.findViewById(R.id.recycler_view_books);
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

                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(
                            getContext(), LinearLayoutManager.VERTICAL, false
                    );
                    cardViewLibAdapter = new CardViewLibAdapter(getContext(), mainbooks);

                    recyclerView2.setAdapter(cardViewLibAdapter);

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}