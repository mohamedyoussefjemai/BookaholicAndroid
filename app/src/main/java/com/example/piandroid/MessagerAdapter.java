package com.example.piandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessagerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;
    private static final int TYPE_IMAGE_SENT = 2;
    private static final int TYPE_IMAGE_RECEIVED = 3;

    private LayoutInflater inflater;
    private List<JSONObject> messages = new ArrayList<>();

    public MessagerAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
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

        JSONObject message = messages.get(position);
        try {
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
        messages.add(jsonObject);
        notifyDataSetChanged();
    }
}