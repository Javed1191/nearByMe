package com.infomanav.wahed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

public class ChangePasswordActivity extends AppCompatActivity
{

    private TextView tv_action_title,tv_submit;
    private String strChnagePassUrl = Application_Constants.Main_URL+"changepassword";
    private TextInputLayout til_old_pass,til_new_pass,til_re_pass;
    private  Toolbar toolbar;
    private EditText edt_old_pass,edt_new_password,edt_re_password;
    private JWTToken jwtToken;
    private Utility utility;
    private String strAccouontId="",siteUserID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        // Initilization
        init();
        strAccouontId = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");
        siteUserID = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_ID,"");

        tv_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String strOldPass = "", strNewPass = "", strRePass = "";
                strOldPass = edt_old_pass.getText().toString();
                strNewPass = edt_new_password.getText().toString();
                strRePass = edt_re_password.getText().toString();
                if (TextUtils.isEmpty(strOldPass)) {
                    edt_old_pass.requestFocus();
                    til_old_pass.setError("Enter old password");
                } else if (TextUtils.isEmpty(strNewPass)) {
                    edt_new_password.requestFocus();
                    til_old_pass.setError(null);
                    til_new_pass.setError("Enter new password");
                } else if (TextUtils.isEmpty(strRePass)) {
                    edt_re_password.requestFocus();
                    til_new_pass.setError(null);
                    til_re_pass.setError("Retype new password");
                } else if (!strNewPass.equals(strRePass)) {
                    edt_re_password.requestFocus();
                    til_re_pass.setError("New password does not match");
                } else {
                    til_re_pass.setError(null);
                    changePassword(strAccouontId,siteUserID,strOldPass,strNewPass);
                }
                }

        });
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
        tv_action_title.setText("Change Password");

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
        jwtToken = new JWTToken(getApplicationContext());
        utility = new Utility(getApplicationContext());

        tv_submit = (TextView) findViewById(R.id.tv_submit);

        edt_old_pass = (EditText) findViewById(R.id.edt_old_pass);
        edt_new_password = (EditText) findViewById(R.id.edt_new_password);
        edt_re_password = (EditText) findViewById(R.id.edt_re_password);

        til_old_pass = (TextInputLayout) findViewById(R.id.til_old_pass);
        til_new_pass = (TextInputLayout) findViewById(R.id.til_new_pass);
        til_re_pass = (TextInputLayout) findViewById(R.id.til_re_pass);
    }


    private void changePassword(String accountID, String siteUserID, String oldpassword, String newpassword)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("accountID", accountID);
            params.put("siteUserID", siteUserID);
            params.put("oldpassword", oldpassword);
            params.put("newpassword", newpassword);
            ServiceHandler serviceHandler = new ServiceHandler(ChangePasswordActivity.this);


            serviceHandler.registerUser(params, strChnagePassUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;
                    String  userPhoneNumber="",countryName="",stateName="",userZipCode="",maritalStaus="",
                            userStreet1="",userStreet2="",userCitizenCountry="",userCitizenCountryname="",userAnnualIncome="",howMuchInvestmentVal="",
                            employmentType="",userCountry="",userState="",userEmployeerName="",userOccup="";
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
                                    Toast.makeText(getApplicationContext(), str_msg, Toast.LENGTH_SHORT).show();

                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), str_msg, Toast.LENGTH_SHORT).show();
                                }


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), str_msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "This is server issue", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

}
