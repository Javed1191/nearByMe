package com.infomanav.wahed;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.SecurityAdapter;
import model_classes.Security;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

public class TAndCActivity extends AppCompatActivity
{

    private String strTermsAndConditionUrl = Application_Constants.Main_URL+"termsandcondition";
    private  Toolbar toolbar;
    private Button btn_disagree,btn_agree;
    private WebView webView;
    private JWTToken jwtToken;
    private Utility utility;
    private String strAccouontId="",userId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_and_c);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userId = Shared_Preferences_Class.readString(getApplicationContext(), Shared_Preferences_Class.USER_ID, "");
        // Initilization
       init();
        btn_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.IS_USER_AGREE,"no");
            }
        });
        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.IS_USER_AGREE,"yes");
                if(!userId.equals("")&&!userId.equals(null))
                {
                    Intent intent = new Intent(TAndCActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
                else
                {
                    Intent i = new Intent(TAndCActivity.this,LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }
        });

        getTandCText();

        WebSettings webSetting = webView.getSettings();
        // webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);




    }

    private void init()
    {
        // status bar color change
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        jwtToken = new JWTToken(getApplicationContext());
        utility = new Utility(getApplicationContext());
        strAccouontId = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");
        webView = (WebView)findViewById(R.id.webView1);
        btn_disagree = (Button) findViewById(R.id.btn_disagree);
        btn_agree = (Button) findViewById(R.id.btn_agree);
    }

    private void getTandCText()
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
           // params.put("accountID", accountID);
            ServiceHandler serviceHandler = new ServiceHandler(TAndCActivity.this);


            serviceHandler.registerUser(params, strTermsAndConditionUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token,html_data="";

                    try {
                        if (str_json != null)
                        {
                            JSONObject jobject = new JSONObject(str_json);
                            str_status = jobject.getString("status");
                            str_msg = jobject.getString("msg");
                            server_jwt_token  = jobject.getString("server_jwt_token");
                            if (str_status.equalsIgnoreCase("success"))
                            {
                                if(jwtToken.decryptJWTToken(server_jwt_token))
                                {

                                    html_data = jobject.getString("html_data");

                                    webView.setBackgroundColor(Color.WHITE);
                                    webView.getSettings().setJavaScriptEnabled(true);
                                    webView.loadDataWithBaseURL("", html_data, "text/html", "UTF-8", "");


                                    Log.i("JWT","This Is Valid");
                                }
                                else
                                {
                                    Log.i("JWT","This Is Not Valid");
                                }


                            }
                            else
                            {


                                Toast.makeText(TAndCActivity.this, str_msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(TAndCActivity.this, "This is server issue", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(TAndCActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

}
