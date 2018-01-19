package com.infomanav.wahed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import services.AESHelper;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class SplashScreenActivity extends AppCompatActivity {
	private static int SPLASH_TIME_OUT = 500;
	private boolean flag;
	private String userId,is_user_agree="";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);

		userId = Shared_Preferences_Class.readString(getApplicationContext(), Shared_Preferences_Class.USER_ID, "");
		is_user_agree = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.IS_USER_AGREE,"");

		// Hide status bar
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
		}




		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run() 
			{

				if(is_user_agree.equalsIgnoreCase("yes"))
				{
					if(!userId.equals("")&&!userId.equals(null))
					{
						Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
						finish();
					}
					else
					{
						Intent i = new Intent(SplashScreenActivity.this,LoginActivity.class);
						startActivity(i);
						overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
						finish();
					}
				}
				else
					{
					Intent intent = new Intent(SplashScreenActivity.this,TAndCActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					finish();
				}



			}
		}, SPLASH_TIME_OUT);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
			}
		}, SPLASH_TIME_OUT);

	}


}
