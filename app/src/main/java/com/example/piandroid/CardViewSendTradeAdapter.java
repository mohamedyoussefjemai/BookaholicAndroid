package com.example.piandroid;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A simple adapter that loads a CardView layout with one TextView and two Buttons, and
 * listens to clicks on the Buttons or on the CardView
 */
public class CardViewSendTradeAdapter extends RecyclerView.Adapter<CardViewSendTradeAdapter.ViewHolder> {
    ArrayList<Requests> requests;
    Context context;

    public CardViewSendTradeAdapter(Context context, ArrayList<Requests> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public CardViewSendTradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_send_trade, parent, false);
        return new CardViewSendTradeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.receiver.setText("Receiver : " + requests.get(position).getReceiver());
        holder.title.setText("Your book : " + requests.get(position).getTitle());
        holder.price.setText("Price : " + requests.get(position).getPrice() + " DT");
        holder.etat.setText(requests.get(position).getEtat());

        if(requests.get(position).getEtat().equals("accepted")){
            holder.etat.setBackgroundResource(R.drawable.greenblock);

        } else if (requests.get(position).getEtat().equals("refused")){
            holder.etat.setBackgroundResource(R.drawable.redblock);
        } else {
            holder.etat.setBackgroundResource(R.drawable.yellowblock);
        }
        holder.titlechange.setText("Book needed :"+ requests.get(position).getTitleechange());

    }


    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView receiver, title, price, etat,titlechange;
        Button updateBook, DeleteBook;
        View view, View2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            receiver = itemView.findViewById(R.id.receiver);
            title = itemView.findViewById(R.id.title);
            titlechange = itemView.findViewById(R.id.titlechange);
            price = itemView.findViewById(R.id.price);
            etat = itemView.findViewById(R.id.etat);

        }
    }

}