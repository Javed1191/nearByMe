package com.infomanav.wahed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import services.Application_Constants;
import services.Shared_Preferences_Class;
import services.Utility;

public class TransactionDetailsActivity extends AppCompatActivity
{

    private TextView tv_action_title,tv_transaction_title,tv_account_debited,tv_date;
    private String strGetProducts = Application_Constants.Main_URL+"xAction=productList";
    private Utility utility;
    private  Toolbar toolbar;
    private String title="",amount="",date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        // Initilization
       init();

        Intent intent = getIntent();

        if(intent.getExtras()!=null)
        {

            title = intent.getStringExtra("title");
            amount = intent.getStringExtra("amount");
            date = intent.getStringExtra("date");


            if (!TextUtils.isEmpty(title)) {
                tv_transaction_title.setText(title);
            }
            if (!TextUtils.isEmpty(amount))
            {
                amount = String.format("%.2f", Double.valueOf(amount));

                tv_account_debited.setText(amount);
            }
            if (!TextUtils.isEmpty(date)) {
                tv_date.setText(date);
            }
        }
    }

    private void init()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_action_title = (TextView) toolbar.findViewById(R.id.tv_action_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tv_action_title.setText("Transaction Details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("menu","home");
                startActivity(intent);
            }
        });
        // status bar color change
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        utility = new Utility(getApplicationContext());

        tv_transaction_title = (TextView) findViewById(R.id.tv_transaction_title);
        tv_account_debited = (TextView) findViewById(R.id.tv_account_debited);
        tv_date = (TextView) findViewById(R.id.tv_date);

    }


}
