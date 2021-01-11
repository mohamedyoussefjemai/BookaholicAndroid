package com.example.piandroid;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A simple adapter that loads a CardView layout with one TextView and two Buttons, and
 * listens to clicks on the Buttons or on the CardView
 */
public class CardViewFavoriteAdapter extends RecyclerView.Adapter<CardViewFavoriteAdapter.ViewHolder> {
    ArrayList<MainBook> mainbooks;
    Context context;
    RequestOptions option;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";
    RecyclerView mRecyclerView;

    public CardViewFavoriteAdapter(Context context, ArrayList<MainBook> mainbooks) {
        this.context = context;
        this.mainbooks = mainbooks;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.eye).error(R.drawable.eye);

    }


    public CardViewFavoriteAdapter(@NonNull RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }


    @NonNull
    @Override
    public CardViewFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_favoris, parent, false);
        return new CardViewFavoriteAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        mPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        //left row
        // holder.imageView.setImageResource(mainbooks.get(position).getBookimage());
        Glide.with(context).load("http://192.168.1.4:3000/get/image/" + mainbooks.get(position).getImage()).apply(option).into(holder.imageView);

        holder.textView8.setText("Title : " + mainbooks.get(position).getTitle());
        holder.textView.setText("Author : " + mainbooks.get(position).getAuthor());
        holder.textView4.setText(mainbooks.get(position).getPrice() + " DT");
        holder.textView5.setText(mainbooks.get(position).getLanguage());
        holder.textView5.setBackgroundResource(R.drawable.yellowblock);
        holder.textView2.setText(mainbooks.get(position).getStatus());
        holder.textView7.setText(mainbooks.get(position).getUsername());


//////
        holder.textView7.setOnClickListener(new View.OnClickListener() {
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
                if (isNetworkAvailable() == false) {
                    Toast toast = Toast.makeText(context, "Need connexion !", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    String idBook = mainbooks.get(position).getId();
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ShowBookActivity.class);
                    intent.putExtra("idBook", idBook);
                    intent.putExtra("idUser", mainbooks.get(position).getUser());
                    intent.putExtra("receiver", mainbooks.get(position).getUsername());
                    intent.putExtra("titlechange", mainbooks.get(position).getTitle());
                    intent.putExtra("hide", "false");
                    context.startActivity(intent);
                }
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable() == false) {
                    Toast toast = Toast.makeText(context, "Need connexion !", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    new AlertDialog.Builder(context)
                            .setTitle("Delete Book")
                            .setMessage("Do you really want to delete this book?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    String idBook = mainbooks.get(position).getId();
                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    JSONObject jsonBody = new JSONObject();

                                    final String mRequestBody = jsonBody.toString();


                                    String url = "http://192.168.1.4:3000/favoris/delete-favoris/" + mPreferences.getString("id", null) + "/" + idBook;
                                    StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.i("LOG_RESPONSE", response);

                                            //toast login in
                                            Toast toast = Toast.makeText(context, "Book Deleted !", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }


                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("LOG_RESPONSE", error.toString());
                                            Toast toast = Toast.makeText(context, "Error Delete book !", Toast.LENGTH_SHORT);
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
                                    ////
                                    Log.i("taille avant delete ===================================================>", String.valueOf(mainbooks.size()));
                                    mainbooks.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mainbooks.size());
                                    notifyDataSetChanged();
                                    Log.i("taille aprés delete ===================================================>", String.valueOf(mainbooks.size()));

////
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

        if (mainbooks.get(position).getStatus().equals("new")) {
            holder.textView2.setBackgroundResource(R.drawable.greenblock);
        } else if (mainbooks.get(position).getStatus().equals("old")) {
            holder.textView2.setBackgroundResource(R.drawable.redblock);
        } else {
            holder.textView2.setBackgroundResource(R.drawable.blueblock);
        }
        holder.textView6.setText(mainbooks.get(position).getCategory());


    }


    @Override
    public int getItemCount() {
        return mainbooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, textView2, textView4, textView5, textView6, textView8, textView7;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bookimage);
            textView = itemView.findViewById(R.id.name);
            textView2 = itemView.findViewById(R.id.name2);
            textView4 = itemView.findViewById(R.id.price);
            textView5 = itemView.findViewById(R.id.price2);
            textView6 = itemView.findViewById(R.id.langue);
            textView8 = itemView.findViewById(R.id.username);
            textView7 = itemView.findViewById(R.id.price3);
            view = itemView.findViewById(R.id.trash);

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}