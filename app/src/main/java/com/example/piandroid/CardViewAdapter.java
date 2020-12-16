package com.example.piandroid;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple adapter that loads a CardView layout with one TextView and two Buttons, and
 * listens to clicks on the Buttons or on the CardView
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    ArrayList<MainBook> mainbooks;
    Context context;
    RequestOptions option;
    private ArrayList<String> TabFavoris;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";

    public CardViewAdapter(Context context, ArrayList<MainBook> mainbooks, ArrayList<String> tab) {
        this.context = context;
        this.mainbooks = mainbooks;
        this.TabFavoris = tab;
        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.mipmap.logo_mini).error(R.mipmap.logo_mini);
    }

    @NonNull
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_book, parent, false);
        return new CardViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        mPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);


// Load Image from the internet and set it into Imageview using Glide

        Glide.with(context).load("http://10.0.2.2:3000/get/image/" + mainbooks.get(position).getImage()).apply(option).into(holder.imageView);
// on click card
        Log.i("favoris dans cardview ======================>", TabFavoris.toString());


        Log.i("favoris dans cardview ======================>", String.valueOf(TabFavoris.contains(mainbooks.get(position).getId())));

        if (TabFavoris.contains(mainbooks.get(position).getId().toString())) {
            holder.heart.setBackgroundResource(R.drawable.heart);
            Log.i("book favoris ====>", mainbooks.get(position).getId());
        }
        holder.textView8.setText("Title : " + mainbooks.get(position).getTitle());
        holder.textView.setText("Author : " + mainbooks.get(position).getAuthor());
        holder.textView4.setText(mainbooks.get(position).getPrice() + " DT");
        holder.textView5.setText(mainbooks.get(position).getLanguage());
        holder.textView5.setBackgroundResource(R.drawable.yellowblock);

//right row
        holder.textView3.setText(mainbooks.get(position).getUsername());
        holder.textView2.setText(mainbooks.get(position).getStatus());
        if (mainbooks.get(position).getStatus().equals("new")) {
            holder.textView2.setBackgroundResource(R.drawable.greenblock);
        } else if (mainbooks.get(position).getStatus().equals("old")) {
            holder.textView2.setBackgroundResource(R.drawable.redblock);
        } else {
            holder.textView2.setBackgroundResource(R.drawable.blueblock);
        }
        holder.textView6.setText(mainbooks.get(position).getCategory());

//////
        holder.textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idUser = mainbooks.get(position).getUser();
                Context context = v.getContext();
                Intent intent = new Intent(context, ShowUserActivity.class);
                intent.putExtra("idUser", idUser);
                context.startActivity(intent);

            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idBook = mainbooks.get(position).getId();
                Context context = v.getContext();
                Intent intent = new Intent(context, ShowBookActivity.class);
                intent.putExtra("idBook", idBook);
                intent.putExtra("idUser",mainbooks.get(position).getUser());
                context.startActivity(intent);

            }
        });

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TabFavoris.contains(mainbooks.get(position).getId())) {
                    Toast toast = Toast.makeText(context, "this book id already in your favorite list", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    holder.heart.setBackgroundResource(R.drawable.heart);
                    TabFavoris.add(mainbooks.get(position).getId());
                    String bookfav = mainbooks.get(position).getId();
                    String titlefav = mainbooks.get(position).getTitle();
                    String authorfav = mainbooks.get(position).getAuthor();
                    String categoryfav = mainbooks.get(position).getCategory();
                    String statusfav = mainbooks.get(position).getStatus();
                    String pricefav = mainbooks.get(position).getPrice();
                    String visiblefav = "1";
                    String languagefav = mainbooks.get(position).getLanguage();
                    String userfav = mPreferences.getString("id", null);
                    String usernamefav = mPreferences.getString("username", null);
                    String imagefav = mainbooks.get(position).getImage();


                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("title", titlefav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("author", authorfav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("author", authorfav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("author", authorfav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("book", bookfav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("category", categoryfav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("status", statusfav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("price", pricefav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("visible", visiblefav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("language", languagefav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("user", userfav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("username", usernamefav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("image", imagefav);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final String mRequestBody = jsonBody.toString();
                    Log.i("fonction =======================>", mRequestBody);

                    String url = "http://10.0.2.2:3000/favoris/add-favoris";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("LOG_RESPONSE", response);
                            Toast toast = Toast.makeText(context, "Add book Done !", Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOG_RESPONSE", error.toString());
                            Toast toast = Toast.makeText(context, "Add book Failed !", Toast.LENGTH_SHORT);
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
            }
        });
    }


    @Override
    public int getItemCount() {
        return mainbooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View heart;
        ImageView imageView;
        TextView textView, textView2, textView3, textView4, textView5, textView6, textView8;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bookimage);
            textView = itemView.findViewById(R.id.name);
            textView2 = itemView.findViewById(R.id.name2);
            textView3 = itemView.findViewById(R.id.username2);
            textView4 = itemView.findViewById(R.id.price);
            textView5 = itemView.findViewById(R.id.price2);
            textView6 = itemView.findViewById(R.id.langue);
            textView8 = itemView.findViewById(R.id.username);
            heart = itemView.findViewById(R.id.heart);
        }
    }
}