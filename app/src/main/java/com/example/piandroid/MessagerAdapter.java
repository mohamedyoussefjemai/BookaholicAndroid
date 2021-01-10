package com.example.piandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piandroid.database.AppDataBase;
import com.example.piandroid.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessagerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;
    private static final int TYPE_IMAGE_SENT = 2;
    private static final int TYPE_IMAGE_RECEIVED = 3;
    private final Context context;


    private SharedPreferences mPreferences;
    final String filename = "BookaholicLogin";

    private LayoutInflater inflater;
    private List<JSONObject> messages = new ArrayList<>();
    String port;

    public MessagerAdapter(Context context, LayoutInflater inflater, List<JSONObject> messages, String port) {
        this.context = context;
        this.inflater = inflater;
        if (messages.size() == 0) {
            this.messages = new ArrayList<>();
        } else {
            this.messages = messages;
        }
        this.port = port;
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageTxt;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            messageTxt = itemView.findViewById(R.id.sendText);


        }
    }

    private class SentImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SentImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    private class ReceiveMessageHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, messageTxt;

        public ReceiveMessageHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameText);
            messageTxt = itemView.findViewById(R.id.receivedText);

        }
    }


    private class ReceiveImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTxt;

        public ReceiveImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            nameTxt = itemView.findViewById(R.id.nameText);

        }
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject message = messages.get(position);
        try {
            if (message.getBoolean("isSent")) {

                if (message.has("message")) {
                    return TYPE_MESSAGE_SENT;
                } else {
                    return TYPE_IMAGE_SENT;
                }

            } else {

                if (message.has("message")) {
                    return TYPE_MESSAGE_RECEIVED;
                } else {
                    return TYPE_IMAGE_RECEIVED;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case TYPE_MESSAGE_SENT:
                view = inflater.inflate(R.layout.item_send_message, parent, false);
                return new SentMessageHolder(view);
            case TYPE_MESSAGE_RECEIVED:
                view = inflater.inflate(R.layout.item_received_message, parent, false);
                return new ReceiveMessageHolder(view);
            case TYPE_IMAGE_SENT:
                view = inflater.inflate(R.layout.item_send_image, parent, false);
                return new SentImageHolder(view);
            case TYPE_IMAGE_RECEIVED:
                view = inflater.inflate(R.layout.item_received_photo, parent, false);
                return new ReceiveImageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        mPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);


        JSONObject message = messages.get(position);
        try {
            String username = mPreferences.getString("username", null);


            if (message.getBoolean("isSent")) {
                if (message.has("message")) {

                    SentMessageHolder messageHolder = (SentMessageHolder) holder;
                    messageHolder.messageTxt.setText(message.getString("message"));

                } else {

                    SentImageHolder imageHolder = (SentImageHolder) holder;
                    Bitmap bitmap = getBitmapFromString(message.getString("image"));
                    imageHolder.imageView.setImageBitmap(bitmap);

                }
            } else {
                if (message.has("message")) {

                    ReceiveMessageHolder messageHolder = (ReceiveMessageHolder) holder;
                    messageHolder.nameTxt.setText(message.getString("name"));
                    messageHolder.messageTxt.setText(message.getString("message"));

                } else {

                    ReceiveImageHolder imageHolder = (ReceiveImageHolder) holder;
                    imageHolder.nameTxt.setText(message.getString("name"));
                    Bitmap bitmap = getBitmapFromString(message.getString("image"));
                    imageHolder.imageView.setImageBitmap(bitmap);

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromString(String image) {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addItem(JSONObject jsonObject) {
        mPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String name = mPreferences.getString("username", null);
        //insert

        AppDataBase database = AppDataBase.getAppDatabase(context);
        User user = new User();
        try {
            user.setName(jsonObject.getString("name"));
            user.setPort(port);
            if (jsonObject.has("message")) {
                user.setMessage(jsonObject.getString("message"));
            } else {
                user.setImage(jsonObject.getString("image"));
            }
            if (jsonObject.getString("name").equals(name)) {
                jsonObject.put("isSent", true);
            } else {
                jsonObject.put("isSent", false);

            }
            user.setSent(false);
            database.userDao().insertOne(user);
            Log.i("insert done", "insert done");
            messages.add(jsonObject);
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //end


    }
}
