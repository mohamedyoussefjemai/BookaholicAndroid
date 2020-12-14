package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private View btnHome;
    private View btnLib;
    private View btnFavoris;
    private View btnprofile;
    private View addBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        showFragment(new FragmentHome());


        btnHome = (View) findViewById(R.id.btnHome);
        btnLib = (View) findViewById(R.id.btnLib);
        btnFavoris = (View) findViewById(R.id.btnFavoris);
        btnprofile = (View) findViewById(R.id.btnprofile);
        addBook = (View) findViewById(R.id.btnAdd);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new FragmentHome());
            }
        });
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new ProfileFragment());
            }
        });

        btnLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new LibFragment());
            }
        });
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });

        btnFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new FavoriteFragment());
            }
        });
    }


    void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .addToBackStack(null)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}