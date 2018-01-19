package com.infomanav.wahed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import services.Shared_Preferences_Class;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private TextView tv_action_title,tv_user_name,tv_account_id,tv_account_type,tv_risk_level;
    private RelativeLayout lay_security,lay_notification,lay_my_profile,lay_investment_profile,lay_account_history,
            lay_invite_friend,lay_rate_us,lay_send_feedback;
    private Button btn_signout;
    private  String strUserName ="",strAccouontId="",strAccoountType="",strRiskLevel="";
    private DrawerLayout drawer_layout;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

       /* Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -1); // from 1 years ago

        Stock stock = null;
        try {
            //stock = YahooFinance.get("INTC");
            stock = YahooFinance.get("GOOG", from, to, Interval.DAILY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BigDecimal price = stock.getQuote().getPrice();
        BigDecimal change = stock.getQuote().getChangeInPercent();
        BigDecimal peg = stock.getStats().getPeg();
        BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();

        stock.print();*/

        // initialization
        init();
        strUserName = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_NAME,"");
        strAccouontId = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");
        strAccoountType = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_TYPE,"");
        strRiskLevel = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_RISK_LEVEL,"");

        if(!TextUtils.isEmpty(strUserName))
        {
                tv_user_name.setText(strUserName);
        }
        if(!TextUtils.isEmpty(strAccouontId))
        {
            tv_account_id.setText(strAccouontId);
        }
        if(!TextUtils.isEmpty(strAccoountType))
        {
            tv_account_type.setText(strAccoountType);
        }
        if(!TextUtils.isEmpty(strRiskLevel))
        {
            tv_risk_level.setText(strRiskLevel);
        }

        FragmentHomeTab fragment = new FragmentHomeTab();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);//.addToBackStack("Home");
        fragmentTransaction.commit();

        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog builder = new AlertDialog.Builder(MainActivity.this).create();

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View builder_view = inflater.inflate(R.layout.activity_logout_dialog, null);
                builder.setView(builder_view);
                // builder.setCancelable(false);

                // builder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


                TextView tv_confirm = (TextView) builder_view.findViewById(R.id.tv_confirm);
                TextView tv_cancel = (TextView) builder_view.findViewById(R.id.tv_cancel);


                tv_confirm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        try {
                            Shared_Preferences_Class.getEditor(getApplicationContext()).remove(Shared_Preferences_Class.USER_ID).commit();
                            Shared_Preferences_Class.getEditor(getApplicationContext()).remove(Shared_Preferences_Class.USER_NAME).commit();
                            Shared_Preferences_Class.getEditor(getApplicationContext()).remove(Shared_Preferences_Class.USER_RISK_LEVEL).commit();
                            Shared_Preferences_Class.getEditor(getApplicationContext()).remove(Shared_Preferences_Class.USER_ACCOUNT_TYPE).commit();
                            Shared_Preferences_Class.getEditor(getApplicationContext()).remove(Shared_Preferences_Class.USER_ACCOUNT_ID).commit();
                            Shared_Preferences_Class.getEditor(getApplicationContext()).remove(Shared_Preferences_Class.EXPECTED_RETURN).commit();
                            Shared_Preferences_Class.getEditor(getApplicationContext()).remove(Shared_Preferences_Class.ACCOUNT_STATUS).commit();
                            Shared_Preferences_Class.getEditor(getApplicationContext()).remove(Shared_Preferences_Class.USER_EMAIL).commit();
                            Shared_Preferences_Class.getEditor(getApplicationContext()).remove(Shared_Preferences_Class.MODELS_FK_ID).commit();


                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                            builder.cancel();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                tv_cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        try {

                            //dialog.dismiss();
                            builder.cancel();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                //dialog.show();
                builder.show();
            }
        });




    }

    private void init()
    {
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tv_action_title = (TextView) findViewById(R.id.tv_action_title);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_account_id  = (TextView) findViewById(R.id.tv_account_id);
        tv_account_type = (TextView) findViewById(R.id.tv_account_type);
        tv_risk_level = (TextView) findViewById(R.id.tv_risk_level);
        lay_security = (RelativeLayout) findViewById(R.id.lay_security);
        lay_notification = (RelativeLayout) findViewById(R.id.lay_notification);
        lay_my_profile = (RelativeLayout) findViewById(R.id.lay_my_profile);
        lay_investment_profile = (RelativeLayout) findViewById(R.id.lay_investment_profile);
        lay_account_history = (RelativeLayout) findViewById(R.id.lay_account_history);
        lay_invite_friend = (RelativeLayout) findViewById(R.id.lay_invite_friend);
        lay_rate_us = (RelativeLayout) findViewById(R.id.lay_rate_us);
        lay_send_feedback = (RelativeLayout) findViewById(R.id.lay_send_feedback);
        btn_signout = (Button) findViewById(R.id.btn_signout);

        lay_security.setOnClickListener(this);
        lay_notification.setOnClickListener(this);
        lay_my_profile.setOnClickListener(this);
        lay_investment_profile.setOnClickListener(this);
        lay_account_history.setOnClickListener(this);
        lay_invite_friend.setOnClickListener(this);
        lay_rate_us.setOnClickListener(this);
        lay_send_feedback.setOnClickListener(this);
        btn_signout.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
       /* if (id == R.id.nav_security)
        {
           *//* Intent intent = new Intent(MainActivity.this,SecurityActivity.class);
            startActivity(intent);*//*
            // Handle the camera action
        }
        else if (id == R.id.nav_notification)
        { Intent intent = new Intent(MainActivity.this,NotificationActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_investment)
        { Intent intent = new Intent(MainActivity.this,InvestmentProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_account_history)
        { *//*Intent intent = new Intent(MainActivity.this,AccountHistoryActivity.class);
            startActivity(intent);*//*
        }
        else if (id == R.id.nav_my_profile)
        { Intent intent = new Intent(MainActivity.this,TAndCActivity.class);
            startActivity(intent);
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v)
    {

        Intent intent;
        switch(v.getId())
        {

            case R.id.lay_security:

                intent = new Intent(MainActivity.this,SecurityActivity.class);
                startActivity(intent);
                break;

            case R.id.lay_notification:
                intent = new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(intent);
                break;
            case R.id.lay_my_profile:
                intent = new Intent(MainActivity.this,MyProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.lay_investment_profile:
                intent = new Intent(MainActivity.this,InvestmentProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.lay_account_history:
                intent = new Intent(MainActivity.this,AccountHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.lay_invite_friend:

                String str_share = "https://beta2.wahedinvest.com/";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, str_share);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;
            case R.id.lay_rate_us:

                break;
            case R.id.lay_send_feedback:

                break;
            case R.id.btn_signout:
                Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
