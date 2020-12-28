package com.example.piandroid;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A simple adapter that loads a CardView layout with one TextView and two Buttons, and
 * listens to clicks on the Buttons or on the CardView
 */
public class CardViewSendSaleAdapter extends RecyclerView.Adapter<CardViewSendSaleAdapter.ViewHolder> {
    ArrayList<Requests> requests;
    Context context;

    public CardViewSendSaleAdapter(Context context, ArrayList<Requests> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public CardViewSendSaleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_send_sale, parent, false);
        return new CardViewSendSaleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.receiver.setText("Receiver : " + requests.get(position).getReceiver());
      //  holder.title.setText("Your book : " + requests.get(position).getTitle());
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