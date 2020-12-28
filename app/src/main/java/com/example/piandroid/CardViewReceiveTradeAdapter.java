package com.example.piandroid;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A simple adapter that loads a CardView layout with one TextView and two Buttons, and
 * listens to clicks on the Buttons or on the CardView
 */
public class CardViewReceiveTradeAdapter extends RecyclerView.Adapter<CardViewReceiveTradeAdapter.ViewHolder> {
    ArrayList<Requests> requests;
    Context context;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";


    public CardViewReceiveTradeAdapter(Context context, ArrayList<Requests> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public CardViewReceiveTradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_receive_trade, parent, false);
        return new CardViewReceiveTradeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.sender.setText(requests.get(position).getSender());

        Log.i("holder", requests.get(position).getSender());
        Log.i("holder2", requests.get(position).getReceiver());
        Log.i("title", requests.get(position).getTitle());
        Log.i("titleechange", requests.get(position).getTitleechange());

        holder.title.setText(requests.get(position).getTitle());
        holder.titlechange.setText(requests.get(position).getTitleechange());
        holder.price.setText(requests.get(position).getPrice() + " DT");
        holder.etat.setText(requests.get(position).getEtat());
        if (requests.get(position).getEtat().equals("accepted")) {
            holder.etat.setBackgroundResource(R.drawable.greenblock);

        } else if (requests.get(position).getEtat().equals("refused")) {
            holder.etat.setBackgroundResource(R.drawable.redblock);
        } else {
            holder.etat.setBackgroundResource(R.drawable.yellowblock);
        }


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Accept Trade")
                        .setMessage("Do you really want to accept this trade ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {


                                try {
                                    String idRequest = requests.get(position).getId();

                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    JSONObject jsonBody = new JSONObject();
                                    jsonBody.put("usernameReceiver", requests.get(position).getReceiver());
                                    jsonBody.put("usernameSender", requests.get(position).getSender());
                                    jsonBody.put("title", requests.get(position).getTitle());
                                    jsonBody.put("titlechange", requests.get(position).getTitleechange());

                                    final String mRequestBody = jsonBody.toString();

                                    Log.i("bodyyyyyyyyyyyyyyyyyy",jsonBody.toString());
                                    String url = "http://10.0.2.2:3000/requests/accept-trade-request/" + idRequest;

                                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.i("LOG_RESPONSE", response);

                                            //toast login in
                                            Toast toast = Toast.makeText(context, "Trade Updated !", Toast.LENGTH_SHORT);
                                            toast.show();
                                            mPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);

                                            int trade = Integer.parseInt(mPreferences.getString("trade", null));
                                            final SharedPreferences.Editor prefEditor = mPreferences.edit();
                                            trade++;
                                            prefEditor.putString("trade", String.valueOf(trade));
                                            prefEditor.commit();
                                        }


                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            if (isNetworkAvailable() == false) {

                                                Toast toast = Toast.makeText(context, "Need connexion !", Toast.LENGTH_SHORT);
                                                toast.show();
                                            } else {
                                                Log.e("LOG_RESPONSE", error.toString());
                                                Toast toast = Toast.makeText(context, "Error Delete book !", Toast.LENGTH_SHORT);
                                                toast.show();
                                            }
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

                                    requests.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, requests.size());
                                    notifyDataSetChanged();
                                    requestQueue.add(stringRequest);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).
                        show();
            }

        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Refuse Trade")
                        .setMessage("Do you really want to refuse this trade ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                String idRequest = requests.get(position).getId();


                                RequestQueue requestQueue = Volley.newRequestQueue(context);
                                JSONObject jsonBody = new JSONObject();

                                final String mRequestBody = jsonBody.toString();

                                String url = "http://10.0.2.2:3000/requests/refuse-trade-request/" + idRequest;
                                StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.i("LOG_RESPONSE", response);

                                        //toast login in
                                        Toast toast = Toast.makeText(context, "Trade Updated !", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }


                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        if (isNetworkAvailable() == false) {

                                            Toast toast = Toast.makeText(context, "Need connexion !", Toast.LENGTH_SHORT);
                                            toast.show();
                                        } else {
                                            Log.e("LOG_RESPONSE", error.toString());
                                            Toast toast = Toast.makeText(context, "Error Delete book !", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
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
                                notifyItemRangeChanged(position, requests.size());
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();


            }

        });

    }


    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sender, title, price, etat, titlechange;
        Button accept, reject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.sender);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            etat = itemView.findViewById(R.id.etat);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
            titlechange = itemView.findViewById(R.id.titlechange);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}