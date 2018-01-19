package com.infomanav.wahed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import services.Application_Constants;
import services.Utility;

public class TAndCActivity1 extends AppCompatActivity
{

    private TextView tv_title1,tv_about_desc1,tv_title2;
    private String strGetProducts = Application_Constants.Main_URL+"xAction=productList";
    private Utility utility;
    private  Toolbar toolbar;
    private Button btn_disagree,btn_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_and_c);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Initilization
       init();
        btn_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TAndCActivity1.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void init()
    {
        // status bar color change
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        utility = new Utility(getApplicationContext());
        tv_title1 = (TextView) findViewById(R.id.tv_title1);
        tv_about_desc1 = (TextView) findViewById(R.id.tv_about_desc1);
        tv_title2 = (TextView) findViewById(R.id.tv_title2);
        btn_disagree = (Button) findViewById(R.id.btn_disagree);
        btn_agree = (Button) findViewById(R.id.btn_agree);

        tv_title1.setText(Html.fromHtml("<b>NVESTMENT ADVISORY AGREEMENT</b>"));
        tv_about_desc1.setText(Html.fromHtml("\n" +
                "\n" +
                "You (“Client”) and Wahed Invest Inc., a Delaware \n" +
                "corporation and an SEC registered investment \n" +
                "adviser (“Wahed”), agree to enter into an \n" +
                "investment advisory relationship which Wahed will manage your brokerage account at such\n" +
                "custodian as Wahed may designate (“Custodian”). This Agreement is effective as of the first day an \n" +
                "account is opened with such Custodian and is ready to receive trading instructions from Wahed (the “Effective Date”) based upon the investment plan recommended by Wahed to Client (the “Plan”). In consideration of the mutual covenants herein, Client and Wahed agree as follows:\n" +
                "\n"));
        tv_title2.setText(Html.fromHtml("<b><font color='black'>Scope of Services:</font></b> <font color='#696969'>Client hereby appoints Wahed as investment manager for all assets that shall be \n" +
                "designated by deposit or transfer into one or more \n" +
                "account or accounts to be established in the name of Client at Custodian (hereinafter “Accounts”). Client authorizes Wahed to perform the services \n" +
                "indicated below in accordance with the financial \n" +
                "circumstances, investment objectives, and risk \n" +
                "tolerance of Client. Wahed accepts the</font>."));

    }


}
