package com.infomanav.wahed;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import services.Application_Constants;
import services.Utility;

public class AccountHistoryActivity extends AppCompatActivity
{

    private TextView tv_action_title;
    private String strGetProducts = Application_Constants.Main_URL+"xAction=productList";
    private Utility utility;
    private  Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_history);
        // Initilization
        init();
        initTable();


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
        tv_action_title.setText("Account History");

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

    }

    public void initTable()
    {
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tbrow0.setGravity(Gravity.CENTER_VERTICAL);
        TextView tv0 = new TextView(this);
        tv0.setText("Date");
        tv0.setTextColor(Color.BLACK);
        tv0.setGravity(Gravity.LEFT);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("    Transaction Type");
        tv1.setTextColor(Color.BLACK);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("    Amount");
        tv2.setTextColor(Color.BLACK);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText("                     ");
        tv3.setTextColor(Color.BLACK);
        tbrow0.addView(tv3);

        stk.addView(tbrow0);

        View blank_view = new View(this);
        blank_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));
        blank_view.setBackgroundColor(Color.parseColor("#00000000"));
        stk.addView(blank_view);

        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
        view.setBackgroundColor(Color.parseColor("#eaeaea"));
        stk.addView(view);

        for (int i = 0; i < 25; i++)
        {
            TableRow tbrow = new TableRow(this);
            tbrow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tbrow.setGravity(Gravity.CENTER_VERTICAL);
            TextView t1v = new TextView(this);
            t1v.setText("15-02-2017");
            t1v.setTextColor(Color.GRAY);
            //t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText("    Deposit");
            t2v.setTextColor(Color.GRAY);
            //t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText("    $1.5000000\n");
            t3v.setTextColor(Color.GRAY);
           //t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);

            TextView t4v = new TextView(this);
            t4v.setText(Html.fromHtml("                   "));
            t4v.setTextColor(Color.GRAY);
           // t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);

            stk.addView(tbrow);

            View view_horizontal = new View(this);
            view_horizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            view_horizontal.setBackgroundColor(Color.parseColor("#eaeaea"));
            stk.addView(view_horizontal);
        }

    }

}
