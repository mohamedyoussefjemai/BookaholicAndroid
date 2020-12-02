package com.example.piandroid;


import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
    public CardViewAdapter(Context context, ArrayList<MainBook> mainbooks){
        this.context = context;
        this.mainbooks = mainbooks;
    }
    @NonNull
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_book,parent,false);
        return new CardViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //left row
        holder.imageView.setImageResource(mainbooks.get(position).getBookimage());
        holder.textView8.setText("Title : " +mainbooks.get(position).getTitle());
        holder.textView.setText("Author : "+mainbooks.get(position).getAuthor());
        holder.textView4.setText("Price : "+mainbooks.get(position).getPrice()+" DT");
        holder.textView5.setText(mainbooks.get(position).getLanguage());
        holder.textView5.setBackgroundResource(R.drawable.yellowblock);

//right row
        holder.textView3.setText(mainbooks.get(position).getUsername());
        holder.textView2.setText(mainbooks.get(position).getStatus());
        if(mainbooks.get(position).getStatus().equals("new"))
        {
            holder.textView2.setBackgroundResource(R.drawable.greenblock);
        } else if(mainbooks.get(position).getStatus().equals("old")) {
            holder.textView2.setBackgroundResource(R.drawable.redblock);
        }
else {
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
        TextView textView,textView2,textView3,textView4,textView5,textView6,textView8;
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

        }
    }
}