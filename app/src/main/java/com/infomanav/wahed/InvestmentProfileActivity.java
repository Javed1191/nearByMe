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

public class InvestmentProfileActivity extends AppCompatActivity
{

    private TextView tv_action_title,tv_account_status,tv_current_risk,tv_exp_return;
    private String strGetProducts = Application_Constants.Main_URL+"xAction=productList";
    private Utility utility;
    private  Toolbar toolbar;
    private String strAccountStatus="",strRiskProfile="",strExpectedReturn="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_profile);
        // Initilization
       init();
        strAccountStatus = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.ACCOUNT_STATUS,"");
        strExpectedReturn = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.EXPECTED_RETURN,"");
        strRiskProfile = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_RISK_LEVEL,"");

        if(!TextUtils.isEmpty(strAccountStatus))
        {
            tv_account_status.setText(strAccountStatus);
        }
        if(!TextUtils.isEmpty(strExpectedReturn))
        {
            tv_exp_return.setText(strExpectedReturn);
        }
        if(!TextUtils.isEmpty(strRiskProfile))
        {
            tv_current_risk.setText(strRiskProfile);
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
        tv_action_title.setText("Investment Profile");

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

        tv_account_status = (TextView) findViewById(R.id.tv_account_status);
        tv_current_risk = (TextView) findViewById(R.id.tv_current_risk);
        tv_exp_return = (TextView) findViewById(R.id.tv_exp_return);

    }


}
