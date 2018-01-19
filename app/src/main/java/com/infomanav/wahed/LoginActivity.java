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

public class LoginActivity extends AppCompatActivity
{

    private TextView tv_sign_in,tv_forgot_password;
    private JWTToken jwtToken;
    private Utility utility;
    private String strUserLoginUrl = Application_Constants.Main_URL+"login";
    private EditText edt_user_name,edt_user_password;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        // initialization
        init();

        tv_sign_in.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String strUserName="",strPassword="";

                strUserName = edt_user_name.getText().toString().trim();
                strPassword = edt_user_password.getText().toString().trim();

                if(TextUtils.isEmpty(strUserName))
                {
                    edt_user_name.setError("Please Enter User Name");
                }
                else if(TextUtils.isEmpty(strPassword))
                {
                    edt_user_password.setError("Please Enter Password");
                }
                else
                {
                   userLogin(strUserName,strPassword);
                   // userLogin1(strUserName,strPassword);
                }
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }


    private void init()
    {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        edt_user_name = (EditText) findViewById(R.id.edt_user_name);
        edt_user_password = (EditText) findViewById(R.id.edt_user_password);
        tv_sign_in = (TextView) findViewById(R.id.tv_sign_in);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        jwtToken = new JWTToken(getApplicationContext());
        utility = new Utility(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

       // ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }




    private void userLogin1(String strUserName, String strPassword)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("userEmail", strUserName);
            params.put("userPassword", strPassword);
            ServiceHandler serviceHandler = new ServiceHandler(LoginActivity.this);


            serviceHandler.callAPIObj(params, strUserLoginUrl,"login", new ServiceHandler.VolleyCallbackObject() {
                @Override
                public void onSuccess(Object result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    UserInfo userInfo = (UserInfo) result;
                    try {

                        String strUserName = userInfo.userFirstName+" " + userInfo.userLastName;

                        Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_ID,userInfo.siteUserID);
                        Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_NAME,strUserName);
                        Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_RISK_LEVEL,userInfo.ModelName);
                        Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_TYPE,userInfo.CustomerType);
                        Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_ID,userInfo.userAccountId);
                        Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.EXPECTED_RETURN,userInfo.ExpectedReturn);
                        Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.ACCOUNT_STATUS,userInfo.user_status);
                        Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_EMAIL,userInfo.userEmailID);
                        Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.MODELS_FK_ID,userInfo.modelsFkId);

                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void userLogin(String strUserName, String strPassword)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("userEmail", strUserName);
            params.put("userPassword", strPassword);
            ServiceHandler serviceHandler = new ServiceHandler(LoginActivity.this);


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
                                    JSONObject userJson = jobject.getJSONObject("user_data");
                                    siteUserID = userJson.getString("siteUserID");
                                    userAccountId = userJson.getString("userAccountId");
                                    userFirstName = userJson.getString("userFirstName");
                                    userLastName = userJson.getString("userLastName");
                                    ModelName = userJson.getString("ModelName");
                                    status = userJson.getString("status");
                                    myint = userJson.getString("myint");
                                    CustomerType = userJson.getString("CustomerType");
                                    ExpectedReturn = userJson.getString("ExpectedReturn");
                                    user_status = userJson.getString("user_status");
                                    userEmailID = userJson.getString("userEmailID");
                                    modelsFkId = userJson.getString("modelsFkId");


                                    String strUserName = userFirstName+" " + userLastName;

                                    Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_ID,siteUserID);
                                    Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_NAME,strUserName);
                                    Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_RISK_LEVEL,ModelName);
                                    Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_TYPE,CustomerType);
                                    Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_ID,userAccountId);
                                    Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.EXPECTED_RETURN,ExpectedReturn);
                                    Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.ACCOUNT_STATUS,user_status);
                                    Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.USER_EMAIL,userEmailID);
                                    Shared_Preferences_Class.writeString(getApplicationContext(),Shared_Preferences_Class.MODELS_FK_ID,modelsFkId);

                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();



                                    Log.i("JWT","This Is Valid");
                                }
                                else
                                {
                                    Log.i("JWT","This Is Not Valid");
                                }


                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, str_msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            {
                                Toast.makeText(LoginActivity.this, "This is server issue", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

}

