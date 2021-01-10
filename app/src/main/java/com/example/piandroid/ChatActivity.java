package com.example.piandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.piandroid.database.AppDataBase;
import com.example.piandroid.entity.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatActivity extends AppCompatActivity implements TextWatcher {
    private String name;
    private WebSocket webSocket;
    private String SERVER_PATH = "ws://192.168.1.4:3001/";
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";
    private int IMAGE_REQUEST_ID = 16;
    ArrayList<JSONObject> messages=new ArrayList<>();
    List<User> users;

    private MessagerAdapter messagerAdapter;
    AppDataBase database ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);



        //get Username
        name = mPreferences.getString("username", null);

        recyclerView = findViewById(R.id.chatrecyclerView);
        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);

        recyclerView = findViewById(R.id.chatrecyclerView);

        /* select */
        /*  select avant de charger les nouveaux test*/

        database = AppDataBase.getAppDatabase(getApplicationContext());
        users = database.userDao().getAll();
        for (int i = 0; i < users.size(); i++) {
          //  Log.i("rowList ==>"+i+" ", users.get(i).getName());
          //  Log.i("rowList ==>"+i+" ", users.get(i).getMessage());
         //   Log.i("rowList ==>"+i+" ", users.get(i).getSent().toString());
       //     Log.i("rowList ==>"+i+" ", users.get(i).getImage());

            name = mPreferences.getString("username", null);

            String port = getIntent().getStringExtra("port");

            if (users.get(i).getPort().equals(port)) {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("name", users.get(i).getName());
                    if(users.get(i).getMessage() != null)
                    {
                        jsonObject.put("message", users.get(i).getMessage());

                    }else {
                        jsonObject.put("image", users.get(i).getImage());

                    }
               //     jsonObject.put("image", users.get(i).getImage());
               //     jsonObject.put("message", users.get(i).getMessage());
                    jsonObject.put("isSent", users.get(i).getSent());

                    if(!users.get(i).getName().equals(name))
                    {
                        jsonObject.put("isSent", false);
                    }
                    else  {
                        jsonObject.put("isSent",true);

                    }

                    messages.add(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }

        /*                     test                 */





        messagerAdapter = new MessagerAdapter(this, getLayoutInflater(),messages,getIntent().getStringExtra("port"));

        recyclerView.setAdapter(messagerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initiateSocketConnection();
    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        String port = getIntent().getStringExtra("port");
        SERVER_PATH ="ws://192.168.1.4:"+port+"/";
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString().trim();
        if (string.isEmpty()) {
            resetMessageEdit();
        } else {
            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void resetMessageEdit() {
        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);

    }


    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            runOnUiThread(() -> {
                Toast.makeText(ChatActivity.this, "Socket Connection Successful !", Toast.LENGTH_SHORT).show();
                initializeView();
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
                JSONObject jsonObject = new JSONObject(text);
                String des_user = getIntent().getStringExtra("des_username");
                if (jsonObject.getString("name").equals(name) || jsonObject.getString("des_user").equals(name)) {
                    super.onMessage(webSocket, text);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            runOnUiThread(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    jsonObject.put("isSent", false);
                    String port = getIntent().getStringExtra("port");

                    messagerAdapter.addItem(jsonObject);

                    recyclerView.smoothScrollToPosition(messagerAdapter.getItemCount() - 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        }
    }

    private void initializeView() {

        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);

        recyclerView = findViewById(R.id.chatrecyclerView);

        messagerAdapter = new MessagerAdapter(this, getLayoutInflater(),messages,getIntent().getStringExtra("port"));

        recyclerView.setAdapter(messagerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageEdit.addTextChangedListener(this);
        sendBtn.setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
                String des_user = getIntent().getStringExtra("des_username");

                jsonObject.put("name", name);
                jsonObject.put("message", messageEdit.getText().toString());


                webSocket.send(jsonObject.toString());

                jsonObject.put("isSent", true);

                messagerAdapter.addItem(jsonObject);

                recyclerView.smoothScrollToPosition(messagerAdapter.getItemCount() - 1);
                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        pickImgBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Pick Image"), IMAGE_REQUEST_ID);

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);
                sendImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendImage(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String Base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("name", name);
            jsonObject.put("image", Base64String);

            webSocket.send(jsonObject.toString());

            jsonObject.put("isSent", true);
            messagerAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messagerAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}