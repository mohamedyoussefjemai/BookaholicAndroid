package com.example.piandroid;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private TextView name_text;
    //private TextView email_txt;
    //  private TextView adresse_txt;
    //private TextView phone_txt;
    private TextView change_password;
    private TextView logout;
    private EditText phone, email, address, input;
    private ImageButton btn_email, btn_phone, btn_address, btn_edit;
    private TextView icon_trade, icon_sale;
    private TextView icon_trade_text,icon_sale_text;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";
    private Button btn_update_all;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_edit = (ImageButton) view.findViewById(R.id.btn_edit);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Username");
        builder.setIcon(R.drawable.psychology);
        builder.setMessage("Please write your new username to complete the update");
        input = new EditText(getContext());
        builder.setView(input);

//SET POSITIVE BUTTON
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = input.getText().toString();

//request update username
                if (txt.length() == 0) {
                    input.setError("username must be not null");
                } else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("username", input.getText().toString());
                        final String mRequestBody = jsonBody.toString();

                        String url = "http://10.0.2.2:3000/users/update-user-username/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(getContext(), "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("username", input.getText().toString());
                                prefEditor.commit();
                                Log.i("shared ==========>", mPreferences.getString("username", null));
                                Log.i("text envoyé ==========>", input.getText().toString());


                                getActivity().getSupportFragmentManager()
                                        .beginTransaction().replace(R.id.fragment_container, new ProfileFragment())
                                        .commit();


                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(getContext(), "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        });
        //negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
////////

        //create the dialog
        final AlertDialog ad = builder.create();

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name_text = (TextView) view.findViewById(R.id.name_text);
        //  email_txt = (TextView) view.findViewById(R.id.email_txt);
        //  adresse_txt = (TextView) view.findViewById(R.id.adresse_txt);
        // phone_txt = (TextView) view.findViewById(R.id.phone_txt);
        change_password = (TextView) view.findViewById(R.id.change_password);
        logout = (TextView) view.findViewById(R.id.logout);
        mPreferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        btn_email = (ImageButton) view.findViewById(R.id.btn_email);
        btn_phone = (ImageButton) view.findViewById(R.id.btn_phone);
        btn_address = (ImageButton) view.findViewById(R.id.btn_addr);
        phone = (EditText) view.findViewById(R.id.tel);
        email = (EditText) view.findViewById(R.id.email);
        address = (EditText) view.findViewById(R.id.adresse);

        name_text.setText(mPreferences.getString("username", null));
        //    email_txt.setText(mPreferences.getString("email", null));
        email.setHint(mPreferences.getString("email", null));
        //   adresse_txt.setText(mPreferences.getString("address", null));
        address.setHint(mPreferences.getString("address", null));
        //  phone_txt.setText(mPreferences.getString("phone", null));
        phone.setHint(mPreferences.getString("phone", null));

        //image action
        icon_trade =  view.findViewById(R.id.icon_trade);
        icon_sale =  view.findViewById(R.id.icon_sale);
        btn_update_all = (Button) view.findViewById(R.id.updateall);
        icon_trade_text =view.findViewById(R.id.icon_trade_text);
         icon_sale_text =view.findViewById(R.id.icon_sale_text);
        icon_trade.setText(mPreferences.getString("trade","").toString());
        icon_sale.setText(mPreferences.getString("sale","").toString());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                prefEditor.remove("id");
                prefEditor.remove("username");
                prefEditor.remove("birthdate");
                prefEditor.remove("email");
                prefEditor.remove("phone");
                prefEditor.remove("sale");
                prefEditor.remove("trade");
                prefEditor.remove("image");
                prefEditor.clear();
                prefEditor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SendMailActivity.class);
                startActivity(intent);
            }
        });

        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().toString().length() != 8) {
                    phone.setError("phone length must be 8");
                } else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("phone", phone.getText().toString());
                        final String mRequestBody = jsonBody.toString();

                        String url = "http://10.0.2.2:3000/users/update-user-phone/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(getContext(), "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("phone", phone.getText().toString());
                                prefEditor.commit();
                                Log.i("shared ==========>", mPreferences.getString("phone", null));
                                Log.i("text envoyé ==========>", phone.getText().toString());

                                getActivity().getSupportFragmentManager()
                                        .beginTransaction().replace(R.id.fragment_container, new ProfileFragment())
                                        .commit();

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(getContext(), "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().length() == 0) {
                    email.setError("email is required!");
                } else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("email", email.getText().toString());
                        final String mRequestBody = jsonBody.toString();

                        String url = "http://10.0.2.2:3000/users/update-user-email/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(getContext(), "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("email", email.getText().toString());
                                prefEditor.commit();
                                Log.i("shared ==========>", mPreferences.getString("email", null));
                                Log.i("text envoyé ==========>", email.getText().toString());

                                getActivity().getSupportFragmentManager()
                                        .beginTransaction().replace(R.id.fragment_container, new ProfileFragment())
                                        .commit();

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(getContext(), "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });
        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address.getText().toString().length() == 0) {
                    address.setError("address is required!");
                } else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("address", address.getText().toString());
                        final String mRequestBody = jsonBody.toString();

                        String url = "http://10.0.2.2:3000/users/update-user-address/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(getContext(), "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("address", address.getText().toString());
                                prefEditor.commit();
                                Log.i("shared ==========>", mPreferences.getString("address", null));
                                Log.i("text envoyé ==========>", address.getText().toString());

                                getActivity().getSupportFragmentManager()
                                        .beginTransaction().replace(R.id.fragment_container, new ProfileFragment())
                                        .commit();

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(getContext(), "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });

        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().toString().length() != 8) {
                    phone.setError("phone length must be 8");
                } else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("phone", phone.getText().toString());
                        final String mRequestBody = jsonBody.toString();

                        String url = "http://10.0.2.2:3000/users/update-user-phone/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(getContext(), "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("phone", phone.getText().toString());
                                prefEditor.commit();
                                Log.i("shared ==========>", mPreferences.getString("phone", null));
                                Log.i("text envoyé ==========>", phone.getText().toString());

                                getActivity().getSupportFragmentManager()
                                        .beginTransaction().replace(R.id.fragment_container, new ProfileFragment())
                                        .commit();

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(getContext(), "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });
        btn_update_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().length() == 0) {
                    email.setError("email is required!");
                } else
                    if (phone.getText().toString().length() != 8) {
                        phone.setError("phone is required!");
                    }
else
                if (address.getText().toString().length() == 0) {
                    address.setError("address is required!");
                }
                 else {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("email", email.getText().toString());
                        jsonBody.put("phone", phone.getText().toString());
                        jsonBody.put("address", address.getText().toString());

                        final String mRequestBody = jsonBody.toString();

                        String url = "http://10.0.2.2:3000/users/update-user-all/" + mPreferences.getString("id", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final SharedPreferences.Editor prefEditor = mPreferences.edit();

                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(getContext(), "Update Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                prefEditor.putString("email", email.getText().toString());
                                prefEditor.putString("phone", phone.getText().toString());
                                prefEditor.putString("address", address.getText().toString());
                                prefEditor.commit();

                                getActivity().getSupportFragmentManager()
                                        .beginTransaction().replace(R.id.fragment_container, new ProfileFragment())
                                        .commit();

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(getContext(), "Update Failed !", Toast.LENGTH_SHORT);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });
    }


}