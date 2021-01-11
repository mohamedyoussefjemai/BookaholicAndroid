package com.example.piandroid;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

        //delete
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
                                String idRequest = requests.get(position).getId();

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    String idBook = requests.get(position).getId();
                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    JSONObject jsonBody = new JSONObject();

                                    final String mRequestBody = jsonBody.toString();


                                    String url = "http://192.168.1.4:3000/requests/delete-trade-request/" + idRequest;
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
                                    Log.i("taille avant delete ===================================================>", String.valueOf(requests.size()));
                                    requests.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, requests.size());
                                    notifyDataSetChanged();
                                    Log.i("taille aprés delete ===================================================>", String.valueOf(requests.size()));

////
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

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