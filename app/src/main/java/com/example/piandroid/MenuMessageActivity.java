package com.example.piandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MenuMessageActivity extends AppCompatActivity {

    private Button btn_groups, btn_trade, btn_sales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_message);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_groups = (Button) findViewById(R.id.Groups);
        btn_trade = (Button) findViewById(R.id.Trade);
        btn_sales = (Button) findViewById(R.id.Sale);


        btn_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuMessageActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        btn_trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuMessageActivity.this, MenuTradeActivity.class);
                startActivity(intent);
            }
        });

        btn_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuMessageActivity.this, MenuSaleActivity.class);
                startActivity(intent);
            }
        });
    }
}