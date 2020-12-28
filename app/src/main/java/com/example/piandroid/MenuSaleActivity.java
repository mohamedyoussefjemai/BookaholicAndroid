package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

public class MenuSaleActivity extends AppCompatActivity {

    private View btnSended;
    private View btnReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_trade);
        showFragment(new SendSaleFragment());

        btnSended = (View) findViewById(R.id.sended);
        btnReceived = (View) findViewById(R.id.received);

        btnSended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new SendSaleFragment());
            }
        });

        btnReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new ReceiveSaleFragment());
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