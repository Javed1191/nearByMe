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
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class NotificationActivity extends AppCompatActivity
{

    private TextView tv_action_title;
    private String strNotificationInfoUrl = Application_Constants.Main_URL+"notification";
    private String strSetNotificationInfoUrl = Application_Constants.Main_URL+"updatenotification";
    private JWTToken jwtToken;
    private Utility utility;
    private  Toolbar toolbar;
    private Switch switch_announcement,switch_invitation,switch_information;
    private String strAccouontId="",strUserId="";
    private String strAnnouncement="",strInvitation="",strInformation="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        // Initilization
       init();
        getNotificationInfo(strAccouontId,strUserId);


        switch_announcement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                if(switch_announcement.isChecked())
                {
                    strAnnouncement = "yes";
                }
                else
                {
                    strAnnouncement = "no";
                }
                if(switch_invitation.isChecked())
                {
                    strInvitation = "yes";
                }
                else
                {
                    strInvitation = "no";
                }
                if(switch_information.isChecked())
                {
                    strInformation = "yes";
                }
                else
                {
                    strInformation = "no";
                }

                setNotificationInfo(strAccouontId,strUserId,strAnnouncement,strInvitation,strInformation);
            }
        });


        switch_invitation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                if(switch_announcement.isChecked())
                {
                    strAnnouncement = "yes";
                }
                else
                {
                    strAnnouncement = "no";
                }
                if(switch_invitation.isChecked())
                {
                    strInvitation = "yes";
                }
                else
                {
                    strInvitation = "no";
                }
                if(switch_information.isChecked())
                {
                    strInformation = "yes";
                }
                else
                {
                    strInformation = "no";
                }

                setNotificationInfo(strAccouontId,strUserId,strAnnouncement,strInvitation,strInformation);
            }
        });

        switch_information.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                if(switch_announcement.isChecked())
                {
                    strAnnouncement = "yes";
                }
                else
                {
                    strAnnouncement = "no";
                }
                if(switch_invitation.isChecked())
                {
                    strInvitation = "yes";
                }
                else
                {
                    strInvitation = "no";
                }
                if(switch_information.isChecked())
                {
                    strInformation = "yes";
                }
                else
                {
                    strInformation = "no";
                }

                setNotificationInfo(strAccouontId,strUserId,strAnnouncement,strInvitation,strInformation);
            }
        });


    }

    private void init()
    {
        strAccouontId = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");
        strUserId = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_ID,"");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_action_title = (TextView) toolbar.findViewById(R.id.tv_action_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tv_action_title.setText("Notifications");

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
        jwtToken = new JWTToken(getApplicationContext());

        switch_announcement = (Switch) findViewById(R.id.switch_announcement);
        switch_invitation = (Switch) findViewById(R.id.switch_invitation);
        switch_information = (Switch) findViewById(R.id.switch_information);

    }

    private void getNotificationInfo(String accountID, String siteUserID)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("accountID", accountID);
            params.put("siteUserID", siteUserID);
            ServiceHandler serviceHandler = new ServiceHandler(NotificationActivity.this);


            serviceHandler.registerUser(params, strNotificationInfoUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;
                    String  Notifications_Announcement="",Notifications_Invitation="",Notifications_Info="";
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
                                    JSONArray jArray = new JSONArray();
                                    jArray = jobject.getJSONArray("user_data");

                                    for (int i = 0; i < jArray.length(); i++)
                                    {
                                        JSONObject Obj = jArray.getJSONObject(i);
                                        // A category variables

                                        Notifications_Announcement = Obj.getString("Notifications_Announcement");
                                        Notifications_Invitation = Obj.getString("Notifications_Invitation");
                                        Notifications_Info = Obj.getString("Notifications_Info");
                                    }

                                    if(!TextUtils.isEmpty(Notifications_Announcement))
                                    {
                                        if(Notifications_Announcement.equalsIgnoreCase("yes"))
                                        {
                                            switch_announcement.setChecked(true);
                                        }
                                        else
                                        {
                                            switch_announcement.setChecked(false);
                                        }
                                    }
                                    else
                                    {
                                        switch_announcement.setChecked(false);
                                    }


                                    if(!TextUtils.isEmpty(Notifications_Invitation))
                                    {
                                        if(Notifications_Invitation.equalsIgnoreCase("yes"))
                                        {
                                            switch_invitation.setChecked(true);
                                        }
                                        else
                                        {
                                            switch_invitation.setChecked(false);
                                        }
                                    }
                                    else
                                    {
                                        switch_invitation.setChecked(false);
                                    }

                                    if(!TextUtils.isEmpty(Notifications_Info))
                                    {
                                        if(Notifications_Info.equalsIgnoreCase("yes"))
                                        {
                                            switch_information.setChecked(true);
                                        }
                                        else
                                        {
                                            switch_information.setChecked(false);
                                        }
                                    }
                                    else
                                    {
                                        switch_information.setChecked(false);
                                    }


                                    Log.i("JWT","This Is Valid");
                                }
                                else
                                {
                                    Log.i("JWT","This Is Not Valid");
                                }


                            }
                            else
                            {
                                Toast.makeText(NotificationActivity.this, str_msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(NotificationActivity.this, "This is server issue", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(NotificationActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void setNotificationInfo(String accountID, String siteUserID, String Notifications_Announcement,String Notifications_Invitation,
                                     String Notifications_Info)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("accountID", accountID);
            params.put("siteUserID", siteUserID);
            params.put("Notifications_Announcement", Notifications_Announcement);
            params.put("Notifications_Invitation", Notifications_Invitation);
            params.put("Notifications_Info", Notifications_Info);
            ServiceHandler serviceHandler = new ServiceHandler(NotificationActivity.this);


            serviceHandler.registerUser(params, strSetNotificationInfoUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;
                    String  Notifications_Announcement="",Notifications_Invitation="",Notifications_Info="";
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
                                    JSONArray jArray = new JSONArray();
                                    jArray = jobject.getJSONArray("user_data");

                                    for (int i = 0; i < jArray.length(); i++)
                                    {
                                        JSONObject Obj = jArray.getJSONObject(i);
                                        // A category variables

                                        Notifications_Announcement = Obj.getString("Notifications_Announcement");
                                        Notifications_Invitation = Obj.getString("Notifications_Invitation");
                                        Notifications_Info = Obj.getString("Notifications_Info");
                                    }

                                    if(!TextUtils.isEmpty(Notifications_Announcement))
                                    {
                                        if(Notifications_Announcement.equalsIgnoreCase("yes"))
                                        {
                                            switch_announcement.setChecked(true);
                                        }
                                        else
                                        {
                                            switch_announcement.setChecked(false);
                                        }
                                    }
                                    else
                                    {
                                        switch_announcement.setChecked(false);
                                    }


                                    if(!TextUtils.isEmpty(Notifications_Invitation))
                                    {
                                        if(Notifications_Invitation.equalsIgnoreCase("yes"))
                                        {
                                            switch_invitation.setChecked(true);
                                        }
                                        else
                                        {
                                            switch_invitation.setChecked(false);
                                        }
                                    }
                                    else
                                    {
                                        switch_invitation.setChecked(false);
                                    }

                                    if(!TextUtils.isEmpty(Notifications_Info))
                                    {
                                        if(Notifications_Info.equalsIgnoreCase("yes"))
                                        {
                                            switch_information.setChecked(true);
                                        }
                                        else
                                        {
                                            switch_information.setChecked(false);
                                        }
                                    }
                                    else
                                    {
                                        switch_information.setChecked(false);
                                    }


                                    Log.i("JWT","This Is Valid");
                                }
                                else
                                {
                                    Log.i("JWT","This Is Not Valid");
                                }


                            }
                            else
                            {
                                Toast.makeText(NotificationActivity.this, str_msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(NotificationActivity.this, "This is server issue", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(NotificationActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


}
