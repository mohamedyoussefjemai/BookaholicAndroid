package com.example.piandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class AddBookActivity extends AppCompatActivity {

    private TextView price_text, category_text, status_text, language_text;
    private EditText title, author, price;
    private Switch visible;
    private Spinner category, status, language;
    private Button AddBook, btnchoose;
    private ImageView bookimage;
    int visible_int = 0;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";
    int SELECT_PHOTO = 1;
    Uri uri;
    Bitmap bitmap;
    private static final int STORAGE_PERMISSION_CODE = 2342;
    private static final int PICK_IMAGE_REQUEST = 22;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "permission not granted", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void showfileChooser() {
        Intent intent = new Intent();
        intent.setType("images/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //request permission
        requestStoragePermission();
        //end request permission

        price_text = findViewById(R.id.price_text);
        category_text = findViewById(R.id.category_text);
        status_text = findViewById(R.id.status_text);
        language_text = findViewById(R.id.language_text);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        visible = (Switch) findViewById(R.id.switch1);
        price = findViewById(R.id.price);
        price.setInputType(InputType.TYPE_CLASS_NUMBER);
        category = findViewById(R.id.category);
        status = findViewById(R.id.status);
        language = findViewById(R.id.language);
        AddBook = (Button) findViewById(R.id.AddBook);
        btnchoose = findViewById(R.id.choose);
        bookimage = findViewById(R.id.bookimage);
        //shared preferences
        mPreferences = getSharedPreferences(filename, MODE_PRIVATE);

        //switch visisble
        visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    price_text.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    visible_int = 1;
                } else {
                    price_text.setVisibility(View.INVISIBLE);
                    price.setVisibility(View.INVISIBLE);
                    visible_int = 0;

                }
            }
        });


        AddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //test upload
            //    uploadImage();


                Log.i("cat=========>", category.getSelectedItem().toString());
                Log.i("stat=========>", category.getSelectedItem().toString());
                Log.i("language =========>", category.getSelectedItem().toString());
                if (verifFields()) {

                    try {

                        RequestQueue requestQueue = Volley.newRequestQueue(AddBookActivity.this);
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("title", title.getText().toString());
                        jsonBody.put("author", author.getText().toString());
                        if (visible_int == 0) {
                            jsonBody.put("visible", 0);
                            jsonBody.put("price", 0);
                        } else {
                            jsonBody.put("visible", 1);
                            jsonBody.put("price", price.getText().toString());
                        }


                        jsonBody.put("category", category.getSelectedItem().toString());
                        jsonBody.put("status", status.getSelectedItem().toString());
                        jsonBody.put("language", language.getSelectedItem().toString());
                        jsonBody.put("user", mPreferences.getString("id", null));
                        jsonBody.put("username", mPreferences.getString("username", null));
                        jsonBody.put("image", "image_1607374127052.jpg");

                        final String mRequestBody = jsonBody.toString();
                        Log.i("fonction =======================>", mRequestBody);

                        String url = "http://10.0.2.2:3000/books/add-book";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG_RESPONSE", response);
                                Toast toast = Toast.makeText(AddBookActivity.this, "Add book Done !", Toast.LENGTH_SHORT);
                                toast.show();
                                Intent intent = new Intent(AddBookActivity.this, MenuActivity.class);
                                startActivity(intent);

                            }
                        }, new ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast toast = Toast.makeText(AddBookActivity.this, "Add book Failed !", Toast.LENGTH_SHORT);
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


    public void Choose(View view) {
        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Log.i("uri ==========>", uri.toString());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bookimage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private void uploadImage() {
        String name = title.getText().toString().trim();
        String path = getPath(uri);

        try {
            String uploadid = UUID.randomUUID().toString();
            new MultipartUploadRequest(this, uploadid, "http://10.0.2.2:3000/upload")
                    .addFileToUpload(path, "image")
                    .addParameter("name", name)
                    .setNotificationConfig(new UploadNotificationConfig())
            .setMaxRetries(2)
            .startUpload();

        } catch (Exception e) {

        }
    }


    public boolean verifFields() {
        if (title.getText().toString().length() == 0) {
            title.setError("title is required!");
            return false;
        }
        if (author.getText().toString().length() == 0) {
            author.setError("author is required!");
            return false;
        }

        if (category.getSelectedItem().toString().equals("Please select a category")) {
            category_text.setError("category is required!");
            return false;
        }
        if (status.getSelectedItem().toString().equals("Please select a status")) {
            status_text.setError("status is required!");
            return false;
        }
        if (language.getSelectedItem().toString().equals("Please select a language")) {
            language_text.setError("language is required!");
            return false;
        }
        if (visible_int == 1 && price.getText().toString().length() == 0) {
            price_text.setError("price is required!");
            return false;
        }
        return true;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}