package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class CategoryActivity extends AppCompatActivity {

    ImageView comics_mangas, health_cooking, romance_newadult, tourism_travel, adventure, literature, personal_devlopment, history, youth, social_science, artmusic_cinema, humor, police_thrillers, religion_spirituality, school, sport_leisure, theater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        comics_mangas = findViewById(R.id.comics_mangas);
        health_cooking = findViewById(R.id.health_cooking);
        romance_newadult = findViewById(R.id.romance_newadult);
        tourism_travel = findViewById(R.id.tourism_travel);
        adventure = findViewById(R.id.adventure);
        literature = findViewById(R.id.literature);
        personal_devlopment = findViewById(R.id.personal_devlopment);
        history = findViewById(R.id.history);
        youth = findViewById(R.id.youth);
        social_science = findViewById(R.id.social_science);
        artmusic_cinema = findViewById(R.id.artmusic_cinema);
        humor = findViewById(R.id.humor);
        police_thrillers = findViewById(R.id.police_thrillers);
        religion_spirituality = findViewById(R.id.religion_spirituality);
        school = findViewById(R.id.school);
        sport_leisure = findViewById(R.id.sport_leisure);
        theater = findViewById(R.id.theater);

        comics_mangas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3002");
                startActivity(intent);
            }
        });

        health_cooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3003");
                startActivity(intent);
            }
        });

        romance_newadult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3004");
                startActivity(intent);
            }
        });

        tourism_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3005");
                startActivity(intent);
            }
        });

        adventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3006");
                startActivity(intent);
            }
        });

        literature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3007");
                startActivity(intent);
            }
        });

        personal_devlopment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3008");
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3009");
                startActivity(intent);
            }
        });

        youth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3010");
                startActivity(intent);
            }
        });

        social_science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3011");
                startActivity(intent);
            }
        });

        artmusic_cinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3012");
                startActivity(intent);
            }
        });

        humor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3013");
                startActivity(intent);
            }
        });

        police_thrillers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3014");
                startActivity(intent);
            }
        });

        religion_spirituality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3015");
                startActivity(intent);
            }
        });

        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3016");
                startActivity(intent);
            }
        });

        sport_leisure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3017");
                startActivity(intent);
            }
        });

        theater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ChatActivity.class);
                intent.putExtra("port","3018");
                startActivity(intent);
            }
        });
    }



}