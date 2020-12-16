package com.example.piandroid;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ProfileFragment extends Fragment {

    ApiService apiService;

    private TextView name_text;
    private ImageButton btn_image;
    private TextView change_password;
    private TextView logout;
    private EditText phone, email, address, input;
    private ImageButton btn_email, btn_phone, btn_address, btn_edit;
    private TextView icon_trade, icon_sale;
    private TextView icon_trade_text, icon_sale_text;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";
    private Button btn_update_all;

    int SELECT_PHOTO = 1;
    Uri uri;
    Bitmap bitmap;
    private String name = "image";
    private static final int STORAGE_PERMISSION_CODE = 2342;
    private static final int PICK_IMAGE_REQUEST = 22;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    RequestOptions option= new RequestOptions().centerCrop().placeholder(R.drawable.bookmale2).error(R.drawable.bookmale2);

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


        mPreferences = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);


        //request permission
        requestStoragePermission();
        //end request permission

        this.askPermissions();
        this.initRetrofitClient();

        btn_edit = (ImageButton) view.findViewById(R.id.btn_edit);
        btn_image = (ImageButton) view.findViewById(R.id.btn_image);

        //choose image

        if(mPreferences.getString("image",null) == null)
        {
            btn_image.setBackgroundResource(R.drawable.bookmale2);
        }
        else {
            String nameImage = mPreferences.getString("image", null);
            Glide.with(getActivity()).load("http://10.0.2.2:3000/get/image/"+nameImage).apply(option).into(btn_image);

        }


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
        icon_trade = view.findViewById(R.id.icon_trade);
        icon_sale = view.findViewById(R.id.icon_sale);
        btn_update_all = (Button) view.findViewById(R.id.updateall);
        icon_trade_text = view.findViewById(R.id.icon_trade_text);
        icon_sale_text = view.findViewById(R.id.icon_sale_text);
        icon_trade.setText(mPreferences.getString("trade", "").toString());
        icon_sale.setText(mPreferences.getString("sale", "").toString());
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
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserImageActivity.class);
                startActivity(intent);
            }
        });

        btn_update_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().length() == 0) {
                    email.setError("email is required!");
                } else if (phone.getText().toString().length() != 8) {
                    phone.setError("phone is required!");
                } else if (address.getText().toString().length() == 0) {
                    address.setError("address is required!");
                } else {
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

    private boolean hasPermission(String permission) {
     /*   if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }*/
        return true;
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }


    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000/upload/").client(client).build().create(ApiService.class);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Log.i("uri ==========>", uri.toString());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                btn_image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "permission granted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getActivity(), "permission not granted", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
}