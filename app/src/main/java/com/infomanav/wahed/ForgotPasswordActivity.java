package com.infomanav.wahed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import model_classes.UserInfo;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

public class ForgotPasswordActivity extends AppCompatActivity
{

    private TextView tv_submit;
    private JWTToken jwtToken;
    private Utility utility;
    private String strUserLoginUrl = Application_Constants.Main_URL+"forgot";
    private EditText edt_user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);


        // initialization
        init();

        tv_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String strUserName="",strPassword="";

                strUserName = edt_user_name.getText().toString().trim();

                if(TextUtils.isEmpty(strUserName))
                {
                    edt_user_name.setError("Please Enter User Name");
                }
                else
                {
                   forgotPassword(strUserName);
                }
            }
        });
    }


    private void init()
    {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        edt_user_name = (EditText) findViewById(R.id.edt_user_name);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        jwtToken = new JWTToken(getApplicationContext());
        utility = new Utility(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

       // ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void forgotPassword(String strUserName)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("userEmail", strUserName);
            ServiceHandler serviceHandler = new ServiceHandler(ForgotPasswordActivity.this);
            serviceHandler.registerUser(params, strUserLoginUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;
                    String  siteUserID="",userAccountId="",userFirstName="",userLastName="",ModelName="",status="",
                            CustomerType,myint="",ExpectedReturn="",user_status="",userEmailID="",modelsFkId="";
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

                                    Toast.makeText(ForgotPasswordActivity.this, str_msg, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(ForgotPasswordActivity.this, str_msg, Toast.LENGTH_SHORT).show();
                                }


                            }
                            else
                            {
                                Toast.makeText(ForgotPasswordActivity.this, str_msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "This is server issue", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(ForgotPasswordActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

}

