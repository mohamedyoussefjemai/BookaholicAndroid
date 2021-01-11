package com.example.piandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

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


public class ReceiveTradeFragment extends Fragment {

    CardViewReceiveTradeAdapter cardViewReceiveTradeAdapter;
    private ArrayList<Requests> requests = new ArrayList<>();
    RecyclerView recyclerView2;

    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_receive_trade, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final ArrayList<String> TabFavoris = new ArrayList<>();
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPreferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject jsonBody = new JSONObject();
        final String mRequestBody = jsonBody.toString();

        String url = "http://192.168.1.4:3000/requests/read-trade-received/" + mPreferences.getString("username", null);

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
                    if(!requests.isEmpty()) {
                        requests = new ArrayList<>();
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Requests alltradesend = new Requests();
                        alltradesend.setId(jsonArray.getJSONObject(i).get("id").toString());
                        alltradesend.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                        alltradesend.setType(jsonArray.getJSONObject(i).get("type").toString());
                        alltradesend.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                        alltradesend.setSender(jsonArray.getJSONObject(i).get("sender").toString());
                        alltradesend.setReceiver(jsonArray.getJSONObject(i).get("receiver").toString());
                        alltradesend.setEtat(jsonArray.getJSONObject(i).get("etat").toString());
                        alltradesend.setTitleechange(jsonArray.getJSONObject(i).get("titlechange").toString());

                        requests.add(alltradesend);

                    }
                    Log.i("size array =======================>", String.valueOf(requests.size()));

                    recyclerView2 = view.findViewById(R.id.recycler_view_receive_trade);


                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(
                            getContext(), LinearLayoutManager.VERTICAL, false
                    );
                    cardViewReceiveTradeAdapter = new CardViewReceiveTradeAdapter(getContext(), requests);

                    recyclerView2.setAdapter(cardViewReceiveTradeAdapter);

                    recyclerView2.setItemAnimator(new DefaultItemAnimator());

                    recyclerView2.setLayoutManager(layoutManager2);


                } catch (JSONException e) {
                    e.printStackTrace();
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