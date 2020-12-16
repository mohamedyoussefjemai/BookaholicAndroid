package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    private EditText username;
    private EditText birthday;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText adresse;
    private EditText phone;
    private Button btnRegister;
    private ArrayList<String> useremail = new ArrayList<>();

    private boolean test;

    public void setTest(boolean test) {
        this.test = test;
    }

    public boolean isTest() {
        return test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        birthday = findViewById(R.id.birthday);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        adresse = findViewById(R.id.adresse);
        phone = findViewById(R.id.phone);
        btnRegister = findViewById(R.id.Register);
        ArrayList<String> stringArrayList;

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthday.setText(year + "/" + (month + 1) + "/" + day);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        adresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void Register(View view) {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                RequestQueue requestQueue2 = Volley.newRequestQueue(RegisterActivity.this);
                JSONObject jsonBody2 = new JSONObject();
                try {
                    jsonBody2.put("email", email.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String mRequestBody2 = jsonBody2.toString();

                String url2 = "http://10.0.2.2:3000/users/read-user-email/" + email.getText().toString();
                StringRequest stringRequest2 = new StringRequest(Request.Method.GET,
                        url2,
                        new Response.Listener<String>() {

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
                                    if (jsonArray == null) {
                                        setTest(false);
                                    } else {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            MainBook book = new MainBook();
                                            book.setId(jsonArray.getJSONObject(i).get("id").toString());
                                            book.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                                            book.setAuthor(jsonArray.getJSONObject(i).get("author").toString());
                                            book.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                                            book.setCategory(jsonArray.getJSONObject(i).get("category").toString());
                                            book.setStatus(jsonArray.getJSONObject(i).get("status").toString());
                                            book.setImage(jsonArray.getJSONObject(i).get("image").toString());
                                            book.setLanguage(jsonArray.getJSONObject(i).get("language").toString());
                                            book.setUser(jsonArray.getJSONObject(i).get("user").toString());
                                            book.setUsername(jsonArray.getJSONObject(i).get("username").toString());
                                            book.setVisible(jsonArray.getJSONObject(i).get("visible").toString());
                                            book.setBookimage(jsonArray.getJSONObject(i).get("image").toString());
                                            Log.i("book =======================>", book.getCategory());
                                            useremail.add(jsonArray.getJSONObject(i).get("email").toString());
                                        }
                                    }

                                Log.i("size array =======================>", String.valueOf(useremail.size()));

                                if (useremail.contains(email.getText().toString())) {
                                    setTest(true);
                                } else {
                                    setTest(false);
                                }

                            } catch(
                            JSONException e)

                            {
                                e.printStackTrace();
                            }
                        }
            },new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse (VolleyError error){
                Log.e("LOG_RESPONSE", error.toString());
                Toast toast = Toast.makeText(RegisterActivity.this, "Register Failed !", Toast.LENGTH_SHORT);
                toast.show();


            }
            })

            {
                @Override
                public String getBodyContentType () {
                return "application/json; charset=utf-8";
            }

                @Override
                public byte[] getBody () throws AuthFailureError {
                try {
                    return mRequestBody2 == null ? null : mRequestBody2.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody2, "utf-8");
                    return null;
                }
            }

                @Override
                protected Response<String> parseNetworkResponse (NetworkResponse
                response){
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
            }

            ;

                requestQueue2.add(stringRequest2);
                Log.i("dernier test",String.valueOf(

            isTest()));


                Log.i("test avant register",String.valueOf(test));
                if(

            isTest())

            {
                Toast toast = Toast.makeText(RegisterActivity.this, "Email used !", Toast.LENGTH_SHORT);
                toast.show();
            } else if(
*/
          if  (verifFields())

            {

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("username", username.getText().toString());
                    jsonBody.put("email", email.getText().toString());
                    jsonBody.put("address", adresse.getText().toString());
                    jsonBody.put("phone", phone.getText().toString());
                    jsonBody.put("sale", 0);
                    jsonBody.put("trade", 0);
                    jsonBody.put("password", password.getText().toString());
                    jsonBody.put("birthdate", birthday.getText().toString());
                    jsonBody.put("image", "null");


                    final String mRequestBody = jsonBody.toString();

                    String url = "http://10.0.2.2:3000/users/add-user";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("LOG_RESPONSE", response);
                            Toast toast = Toast.makeText(RegisterActivity.this, "Registration Done !", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOG_RESPONSE", error.toString());
                            Toast toast = Toast.makeText(RegisterActivity.this, "Register Failed !", Toast.LENGTH_SHORT);
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
            } else

            {
                Toast toast = Toast.makeText(RegisterActivity.this, "Register Failed !", Toast.LENGTH_SHORT);
                toast.show();

            }
        }


    });

}

    public boolean verifFields() {
        if (username.getText().toString().length() == 0) {
            username.setError("username is required!");
            return false;
        }
        if (email.getText().toString().length() == 0) {
            email.setError("email is required!");
            return false;
        }

        if (birthday.getText().length() == 0) {
            birthday.setError("birthday is required!");
            return false;
        }
        if (password.getText().toString().length() == 0) {
            password.setError("password is required!");
            return false;
        }
        if (confirmPassword.getText().toString().length() == 0) {
            confirmPassword.setError("confirmpassword is required!");
            return false;
        }
        if (confirmPassword.getText().toString().equals(password.getText().toString()) == false) {
            confirmPassword.setError("confirmpassword must be equal ");
            return false;
        }
        if (adresse.getText().toString().length() == 0) {
            adresse.setError("adresse is required!");
            return false;
        }
        if (phone.getText().toString().length() != 8) {
            phone.setError("phone is required!");
            return false;
        }

        return true;
    }


}