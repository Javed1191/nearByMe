package com.infomanav.wahed;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import adapter.CategoryAdapter;
import adapter.DocumentAdapter;
import adapter.SecurityAdapter;
import adapter.TransactionAdapter;
import model_classes.Category;
import model_classes.Document;
import model_classes.Security;
import model_classes.SecurityObj;
import model_classes.Transaction;
import model_classes.UserInfo;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

public class SecurityActivity extends AppCompatActivity
{

    private TextView tv_action_title,tv_data_not_found,tv_email,tv_change_password;
    private String strPositionUrl = Application_Constants.Main_URL+"security";
    private JWTToken jwtToken;
    private Utility utility;
    private  Toolbar toolbar;
    private  RecyclerView security_recycler_view;
    private SecurityAdapter securityAdapter;
    private NestedScrollView nest_scroll_view;
    private  String strAccouontId="",strEmailId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        // Initilization
        init();
       // initTable();

        //getSecurity(strAccouontId);
        getSecurity1(strAccouontId);


    }

    private void init()
    {
        jwtToken = new JWTToken(getApplicationContext());
        utility = new Utility(getApplicationContext());
        strAccouontId = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");
        strEmailId = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_EMAIL,"");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_action_title = (TextView) toolbar.findViewById(R.id.tv_action_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tv_action_title.setText("Security");

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

        security_recycler_view = (RecyclerView)findViewById(R.id.security_recycler_view);
        security_recycler_view.setLayoutManager(new LinearLayoutManager(SecurityActivity.this));
        security_recycler_view.setNestedScrollingEnabled(false);

        tv_email  = (TextView) findViewById(R.id.tv_email);
        tv_change_password = (TextView) findViewById(R.id.tv_change_password);
        tv_data_not_found = (TextView) findViewById(R.id.tv_data_not_found);

        tv_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecurityActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });



      /* // tv_security_found = (TextView) findViewById(R.id.tv_security_found);
        final ArrayList<Security> transactionArrayList = new ArrayList<Security>();

        for (int i = 0; i < 10; i++)
        {

            Security feeds = new Security("192.168.12.2","Mah","IN","2016-09-21","10:24");
            transactionArrayList.add(feeds);




        }
        securityAdapter = new SecurityAdapter(SecurityActivity.this, transactionArrayList);
        security_recycler_view.setAdapter(securityAdapter);*/

        nest_scroll_view = (NestedScrollView) findViewById(R.id.nest_scroll_view);

        nest_scroll_view.post(new Runnable() {

            public void run() {

                nest_scroll_view.scrollTo(0,0);
            }
        });

    }

    public void initTable()
    {
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_VERTICAL);
        TextView tv0 = new TextView(this);
        tv0.setText("Ip Address");
        tv0.setTextColor(Color.BLACK);
        tv0.setGravity(Gravity.LEFT);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("    State/Region");
        tv1.setTextColor(Color.BLACK);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("    Country");
        tv2.setTextColor(Color.BLACK);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("    Time");
        tv3.setTextColor(Color.BLACK);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);

        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
        view.setBackgroundColor(Color.parseColor("#eaeaea"));
        stk.addView(view);

        for (int i = 0; i < 25; i++)
        {
            TableRow tbrow = new TableRow(this);
            tbrow.setGravity(Gravity.CENTER_VERTICAL);
            TextView t1v = new TextView(this);
            t1v.setText("192.168.22");
            t1v.setTextColor(Color.GRAY);
            //t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText("    Mah");
            t2v.setTextColor(Color.GRAY);
            //t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText("    IN");
            t3v.setTextColor(Color.GRAY);
           //t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);

            TextView t4v = new TextView(this);
            t4v.setText(Html.fromHtml("&nbsp;&nbsp;&nbsp 15-02-2017 \n    <small><br>&nbsp;&nbsp;&nbsp;&nbsp 15:12</small>"));
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


    private void getSecurity1(String accountID)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("accountID", accountID);
            ServiceHandler serviceHandler = new ServiceHandler(SecurityActivity.this);
            serviceHandler.callAPIObj(params, strPositionUrl,"security", new ServiceHandler.VolleyCallbackObject() {
                @Override
                public void onSuccess(Object result) {
                    System.out.println("str_get_category_url Json responce" + result);
                     ArrayList<Security> transactionArrayList = new ArrayList<Security>();
                    SecurityObj securityObj = (SecurityObj) result;
                    try {

                        if(!TextUtils.isEmpty(strEmailId))
                        {
                            tv_email.setText(strEmailId);
                        }
                        tv_data_not_found.setVisibility(View.GONE);
                        security_recycler_view.setVisibility(View.VISIBLE);
                        securityAdapter = new SecurityAdapter(SecurityActivity.this, securityObj.getSecurityArrayList());
                        security_recycler_view.setAdapter(securityAdapter);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(SecurityActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
    private void getSecurity(String accountID)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("accountID", accountID);
            ServiceHandler serviceHandler = new ServiceHandler(SecurityActivity.this);


            serviceHandler.registerUser(params, strPositionUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;
                    String  id="",Acivity="",CreatedON="",SiteUserID="",ClientAccountID="",
                            State="",IPAddress="",Country="",LoginTime="",
                            strDate="",strTime="";
                    final ArrayList<Security> transactionArrayList = new ArrayList<Security>();
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

                                        Acivity = Obj.getString("Acivity");
                                        CreatedON = Obj.getString("CreatedON");
                                        id = Obj.getString("id");
                                        SiteUserID = Obj.getString("SiteUserID");
                                        ClientAccountID = Obj.getString("ClientAccountID");
                                        IPAddress = Obj.getString("IPAddress");
                                        State = Obj.getString("State");
                                        Country = Obj.getString("Country");
                                        LoginTime = Obj.getString("LoginTime");

                                        if(!TextUtils.isEmpty(LoginTime))
                                        {
                                            String[] date_time = LoginTime.split("\\s+");
                                            strDate = date_time[0];
                                            strTime = date_time[1];
                                        }


                                        Security feeds = new Security(IPAddress,State,Country,strDate,strTime);
                                        transactionArrayList.add(feeds);

                                    }

                                    if(!TextUtils.isEmpty(strEmailId))
                                    {
                                        tv_email.setText(strEmailId);
                                    }
                                    tv_data_not_found.setVisibility(View.GONE);
                                    security_recycler_view.setVisibility(View.VISIBLE);
                                    securityAdapter = new SecurityAdapter(SecurityActivity.this, transactionArrayList);
                                    security_recycler_view.setAdapter(securityAdapter);



                                    Log.i("JWT","This Is Valid");
                                }
                                else
                                {
                                    Log.i("JWT","This Is Not Valid");
                                }


                            }
                            else
                            {
                                tv_data_not_found.setVisibility(View.VISIBLE);
                                security_recycler_view.setVisibility(View.GONE);

                                Toast.makeText(SecurityActivity.this, str_msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(SecurityActivity.this, "This is server issue", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(SecurityActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


}
