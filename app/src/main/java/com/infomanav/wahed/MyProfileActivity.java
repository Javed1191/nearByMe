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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class MyProfileActivity extends AppCompatActivity
{

    private TextView tv_action_title,tv_contact_edit,tv_employment_edit,tv_personal_edit;
    private String strUserInfoUrl = Application_Constants.Main_URL+"getcontactinfo";
    private String strStateUrl = Application_Constants.Main_URL+"state";
    private String strContactInfoUpdateUrl = Application_Constants.Main_URL+"contactinfoupdate";
    private String strPersonalInfoUpdateUrl = Application_Constants.Main_URL+"personalInfoupdate";
    private String strEmployeeInfoUpdateUrl = Application_Constants.Main_URL+"employeeTypeUpdate";


    private  Toolbar toolbar;
    private EditText edt_mobile,edt_address,edt_country,edt_state,edt_zip,edt_employment_type,edt_marital_stattus,
            edt_country_citizenship,edt_annual_income,edt_liquid_networth,edt_emp_name,edt_occupation;
    private JWTToken jwtToken;
    private Utility utility;
    private String strAccouontId="",strCountryId="",strStateId="",strCountryOfCituzenId="",siteUserID="",strMaritialStatusId="";
    private LinearLayout lay_contact_info_edit,lay_employment_info_edit,lay_personal_info_edit,lay_employment_type;
    private ImageView img_contact_edit,img_employment_edit,img_personal_edit;
    private AutoCompleteTextView auto_country,auto_state,auto_country_citizenship,auto_marital_status,auto_employment_type;
    private List<String>list_country_name,list_country_id,list_state_name,
            list_state_id,list_country_of_citizen_name,list_country_of_citizen_id,list_marital_status_name,list_marital_status_id,
            list_employment_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        // Initilization
        init();
        strAccouontId = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");
        siteUserID = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.USER_ID,"");
        getUserInfo(strAccouontId);

        lay_contact_info_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(img_contact_edit.getVisibility()==View.VISIBLE)
                {
                    img_contact_edit.setVisibility(View.INVISIBLE);
                    tv_contact_edit.setText("Done");

                    edt_mobile.setEnabled(true);
                    edt_mobile.requestFocus();
                    edt_address.setEnabled(true);
                    edt_zip.setEnabled(true);
                    auto_country.setEnabled(true);
                    auto_state.setEnabled(true);
                }
                else
                {
                    String userPhoneNumber="",userStreet1="",userZipCode="",strCountryName="",strStateName;

                    userPhoneNumber = edt_mobile.getText().toString();
                    userStreet1 = edt_address.getText().toString();
                    userZipCode = edt_zip.getText().toString();
                    strCountryName = auto_country.getText().toString();
                    strStateName = auto_state.getText().toString();

                    img_contact_edit.setVisibility(View.VISIBLE);
                    tv_contact_edit.setText("Edit");



                    if(TextUtils.isEmpty(userPhoneNumber))
                    {
                        edt_mobile.requestFocus();
                        edt_mobile.setError("Enter mobile number");
                    }
                    else if(TextUtils.isEmpty(userStreet1))
                    {
                        edt_address.requestFocus();
                        edt_address.setError("Enter address");
                    }
                    else if(TextUtils.isEmpty(strCountryName))
                    {
                        auto_country.requestFocus();
                        edt_address.setError("Select Country");
                    }
                    else if(TextUtils.isEmpty(strStateName))
                    {
                        auto_state.requestFocus();
                        auto_state.setError("Select State");
                    }
                    else if(TextUtils.isEmpty(userZipCode))
                    {
                        edt_zip.requestFocus();
                        edt_zip.setError("Enter zip code");
                    }
                    else {
                        updateUserContactInfo(strAccouontId,siteUserID,userPhoneNumber,userStreet1,strCountryId,strStateId,userZipCode);
                    }

                    edt_mobile.setEnabled(false);
                    edt_address.setEnabled(false);
                    edt_zip.setEnabled(false);
                    auto_country.setEnabled(false);
                    auto_state.setEnabled(false);

                }

            }
        });
        lay_employment_info_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(img_employment_edit.getVisibility()==View.VISIBLE)
                {
                    img_employment_edit.setVisibility(View.INVISIBLE);
                    tv_employment_edit.setText("Done");

                    auto_employment_type.setEnabled(true);
                    auto_employment_type.requestFocus();
                    edt_emp_name.setEnabled(true);
                    edt_occupation.setEnabled(true);
                    edt_emp_name.setEnabled(true);
                    edt_occupation.setEnabled(true);
                }
                else
                {

                    String userEmployementType="",userEmployeerName="",userOccup="";

                    userEmployementType = auto_employment_type.getText().toString();
                    userEmployeerName = edt_emp_name.getText().toString();
                    userOccup = edt_occupation.getText().toString();

                    img_employment_edit.setVisibility(View.VISIBLE);
                    tv_employment_edit.setText("Edit");
                    if(TextUtils.isEmpty(userEmployementType))
                    {
                        auto_employment_type.requestFocus();
                        auto_employment_type.setError("Select employment type");
                    }
                    else if(userEmployementType.equalsIgnoreCase("EMPLOYED"))
                    {
                        if (TextUtils.isEmpty(userEmployeerName))
                        {
                            edt_emp_name.requestFocus();
                            edt_emp_name.setError("Enter employee name");
                        }
                        else if (TextUtils.isEmpty(userOccup))
                        {
                            edt_occupation.requestFocus();
                            edt_occupation.setError("Enter occupation");
                        }
                        else
                        {
                            updateUserEmploymentInfo(strAccouontId,siteUserID,userEmployementType,userEmployeerName,userOccup);
                        }
                    }
                    else
                    {
                        updateUserEmploymentInfo(strAccouontId,siteUserID,userEmployementType,userEmployeerName,userOccup);
                    }
                    auto_employment_type.setEnabled(false);
                    edt_emp_name.setEnabled(false);
                    edt_occupation.setEnabled(false);
                    edt_emp_name.setEnabled(false);
                    edt_occupation.setEnabled(false);

                }
            }
        });
        lay_personal_info_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(img_personal_edit.getVisibility()==View.VISIBLE)
                {
                    img_personal_edit.setVisibility(View.INVISIBLE);
                    tv_personal_edit.setText("Done");

                    auto_marital_status.setEnabled(true);
                    auto_marital_status.requestFocus();
                    edt_annual_income.setEnabled(true);
                    edt_liquid_networth.setEnabled(true);
                    auto_country_citizenship.setEnabled(true);
                }
                else
                {

                    String maritalStaus="",userCitizenCountry="",userAnnualIncome="",howMuchInvestmentVal="";

                    maritalStaus = auto_marital_status.getText().toString();
                    userCitizenCountry = auto_country_citizenship.getText().toString();
                    userAnnualIncome = edt_annual_income.getText().toString();
                    howMuchInvestmentVal = edt_liquid_networth.getText().toString();

                    img_personal_edit.setVisibility(View.VISIBLE);
                    tv_personal_edit.setText("Edit");

                    if(TextUtils.isEmpty(maritalStaus))
                    {
                        auto_marital_status.requestFocus();
                        auto_marital_status.setError("Select marital status");
                    }
                    else if(TextUtils.isEmpty(userCitizenCountry))
                    {
                        auto_country_citizenship.requestFocus();
                        auto_country_citizenship.setError("Select country of citizenship");
                    }
                    else if(TextUtils.isEmpty(userAnnualIncome))
                    {
                        edt_annual_income.requestFocus();
                        edt_annual_income.setError("Enter annul income");
                    }
                    else if(TextUtils.isEmpty(howMuchInvestmentVal))
                    {
                        edt_liquid_networth.requestFocus();
                        edt_liquid_networth.setError("Enter liquid income");
                    }

                    else {
                        updateUserPersonalInfo(strAccouontId,siteUserID,strMaritialStatusId,strCountryOfCituzenId,userAnnualIncome,howMuchInvestmentVal);
                    }
                    auto_marital_status.setEnabled(false);
                    edt_annual_income.setEnabled(false);
                    edt_liquid_networth.setEnabled(false);
                    auto_country_citizenship.setEnabled(false);
                }
            }
        });

        auto_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_country.showDropDown();
            }
        });
        auto_country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus)
                {
                    auto_country.showDropDown();
                }
            }
        });
        auto_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                strCountryId = list_country_id.get(list_country_name.indexOf(auto_country.getText().toString()));
                auto_state.setText("");

                getState(strCountryId);


            }
        });


        auto_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_state.showDropDown();
            }
        });
        auto_state.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus)
                {
                    auto_state.showDropDown();
                }
            }
        });
        auto_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                strStateId = list_state_id.get(list_state_name.indexOf(auto_state.getText().toString()));

            }
        });

        auto_country_citizenship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_country_citizenship.showDropDown();
            }
        });
        auto_country_citizenship.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus)
                {
                    auto_country_citizenship.showDropDown();
                }
            }
        });
        auto_country_citizenship.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                strCountryOfCituzenId = list_country_id.get(list_country_name.indexOf(auto_country_citizenship.getText().toString()));

            }
        });


        auto_employment_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_employment_type.showDropDown();
            }
        });
        auto_employment_type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus)
                {
                    auto_employment_type.showDropDown();
                }
            }
        });

        auto_employment_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(auto_employment_type.getText().toString().equalsIgnoreCase("EMPLOYED"))
                    {
                        lay_employment_type.setVisibility(View.VISIBLE);
                    }
                    else {
                        lay_employment_type.setVisibility(View.GONE);
                    }

            }
        });


        auto_marital_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_marital_status.showDropDown();
            }
        });
        auto_marital_status.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus)
                {
                    auto_marital_status.showDropDown();
                }
            }
        });
        auto_marital_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                strMaritialStatusId = list_marital_status_id.get(list_marital_status_name.indexOf(auto_marital_status.getText().toString()));

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
        tv_action_title.setText("My Profile");

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
        lay_contact_info_edit = (LinearLayout) findViewById(R.id.lay_contact_info_edit);
        lay_employment_info_edit = (LinearLayout) findViewById(R.id.lay_employment_info_edit);
        lay_personal_info_edit = (LinearLayout) findViewById(R.id.lay_personal_info_edit);
        lay_employment_type = (LinearLayout) findViewById(R.id.lay_employment_type);

        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_country = (EditText) findViewById(R.id.edt_country);
        edt_state = (EditText) findViewById(R.id.edt_state);
        edt_zip = (EditText) findViewById(R.id.edt_zip);
        edt_employment_type  = (EditText) findViewById(R.id.edt_employment_type);
        edt_marital_stattus = (EditText) findViewById(R.id.edt_marital_stattus);
        edt_country_citizenship = (EditText) findViewById(R.id.edt_country_citizenship);
        edt_annual_income = (EditText) findViewById(R.id.edt_annual_income);
        edt_liquid_networth = (EditText) findViewById(R.id.edt_liquid_networth);
        edt_emp_name = (EditText) findViewById(R.id.edt_emp_name);
        edt_occupation = (EditText) findViewById(R.id.edt_occupation);

        tv_contact_edit = (TextView) findViewById(R.id.tv_contact_edit);
        tv_employment_edit = (TextView) findViewById(R.id.tv_employment_edit);
        tv_personal_edit = (TextView) findViewById(R.id.tv_personal_edit);

        img_contact_edit = (ImageView) findViewById(R.id.img_contact_edit);
        img_employment_edit = (ImageView) findViewById(R.id.img_employment_edit);
        img_personal_edit = (ImageView) findViewById(R.id.img_personal_edit);

        auto_country = (AutoCompleteTextView) findViewById(R.id.auto_country);
        auto_state = (AutoCompleteTextView) findViewById(R.id.auto_state);
        auto_country_citizenship = (AutoCompleteTextView) findViewById(R.id.auto_country_citizenship);
        auto_marital_status = (AutoCompleteTextView) findViewById(R.id.auto_marital_status);
        auto_employment_type = (AutoCompleteTextView) findViewById(R.id.auto_employment_type);



        list_country_name = new ArrayList<>();
        list_country_id = new ArrayList<>();
        list_country_of_citizen_name = new ArrayList<>();
        list_country_of_citizen_id = new ArrayList<>();
        list_marital_status_name = new ArrayList<>();
        list_marital_status_id = new ArrayList<>();
        list_employment_type = new ArrayList<>();

        list_marital_status_id.add("M");
        list_marital_status_id.add("D");
        list_marital_status_id.add("S");

        list_marital_status_name.add("Married");
        list_marital_status_name.add("Divorced");
        list_marital_status_name.add("Single");

        list_employment_type.add("EMPLOYED");
        list_employment_type.add("UNEMPLOYED");
        list_employment_type.add("RETIRED");
        list_employment_type.add("STUDENT");


    }


    private void getUserInfo(String accountID)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("accountID", accountID);
            ServiceHandler serviceHandler = new ServiceHandler(MyProfileActivity.this);


            serviceHandler.registerUser(params, strUserInfoUrl, new ServiceHandler.VolleyCallback() {
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
                                    JSONObject userJson = jobject.getJSONObject("user_data");
                                    JSONArray countryJsonArr = jobject.getJSONArray("country");

                                    userPhoneNumber = userJson.getString("userPhoneNumber");
                                    countryName = userJson.getString("countryName");
                                    stateName = userJson.getString("stateName");
                                    userZipCode = userJson.getString("userZipCode");
                                    maritalStaus = userJson.getString("maritalStaus");
                                    userStreet1 = userJson.getString("userStreet1");
                                    userStreet2 = userJson.getString("userStreet2");
                                    userCitizenCountryname = userJson.getString("userCitizenCountryname");
                                    userAnnualIncome = userJson.getString("userAnnualIncome");
                                    howMuchInvestmentVal = userJson.getString("howMuchInvestmentVal");
                                    employmentType = userJson.getString("employmentType");
                                    userCountry = userJson.getString("userCountry");
                                    userState = userJson.getString("userState");
                                    userCitizenCountry = userJson.getString("userCitizenCountry");
                                    userEmployeerName = userJson.getString("userEmployeerName");
                                    userOccup = userJson.getString("userOccup");

                                    strCountryId = userCountry;
                                    strStateId = userState;
                                    strCountryOfCituzenId = userCitizenCountry;

                                    setDataToUi(edt_mobile,userPhoneNumber);
                                    setDataToUi(edt_zip,userZipCode);
                                    setDataToUi(edt_address,userStreet1);
                                    setDataToUi(edt_annual_income,userAnnualIncome);
                                    setDataToUi(edt_liquid_networth,howMuchInvestmentVal);


                                    for(int i=0; i<countryJsonArr.length();i++)
                                    {
                                        JSONObject Obj = countryJsonArr.getJSONObject(i);
                                        list_country_name.add(Obj.getString("name"));
                                        list_country_id.add(Obj.getString("id"));

                                    }

                                    ArrayAdapter<String> adapter = new
                                            ArrayAdapter<String>(MyProfileActivity.this,android.R.layout.simple_list_item_1,list_country_name);

                                    auto_country.setAdapter(adapter);
                                    auto_country.setThreshold(1);

                                    auto_country_citizenship.setAdapter(adapter);
                                    auto_country_citizenship.setThreshold(1);

                                    ArrayAdapter<String> maritalStatusAdapter = new
                                            ArrayAdapter<String>(MyProfileActivity.this,android.R.layout.simple_list_item_1,list_marital_status_name);
                                    auto_marital_status.setAdapter(maritalStatusAdapter);
                                    auto_marital_status.setThreshold(1);

                                    ArrayAdapter<String> employmentTypeAdapter = new
                                            ArrayAdapter<String>(MyProfileActivity.this,android.R.layout.simple_list_item_1,list_employment_type);
                                    auto_employment_type.setAdapter(employmentTypeAdapter);
                                    auto_employment_type.setThreshold(1);


                                    if(!TextUtils.isEmpty(countryName)&& !countryName.equalsIgnoreCase("null"))
                                    {
                                        auto_country.setText(countryName);
                                    }
                                    else
                                    {
                                        auto_country.setText("-");
                                    }
                                    if(!TextUtils.isEmpty(stateName)&& !stateName.equalsIgnoreCase("null"))
                                    {
                                        auto_state.setText(stateName);
                                    }
                                    else
                                    {
                                        auto_state.setText("-");
                                    }
                                    if(!TextUtils.isEmpty(userCitizenCountryname)&& !userCitizenCountryname.equalsIgnoreCase("null"))
                                    {
                                        auto_country_citizenship.setText(userCitizenCountryname);
                                    }
                                    else
                                    {
                                        auto_country_citizenship.setText("-");
                                    }
                                    if(!TextUtils.isEmpty(maritalStaus)&& !maritalStaus.equalsIgnoreCase("null"))
                                    {
                                        auto_marital_status.setText(maritalStaus);
                                    }
                                    else
                                    {
                                        auto_marital_status.setText("-");
                                    }
                                    if(!TextUtils.isEmpty(employmentType)&& !employmentType.equalsIgnoreCase("null"))
                                    {
                                        auto_employment_type.setText(employmentType);

                                        if(employmentType.equalsIgnoreCase("EMPLOYED"))
                                        {
                                            lay_employment_type.setVisibility(View.VISIBLE);
                                            edt_emp_name.setText(userEmployeerName);
                                            edt_occupation.setText(userOccup);

                                            edt_emp_name.setEnabled(false);
                                            edt_occupation.setEnabled(false);
                                        }
                                        else
                                        {
                                            lay_employment_type.setVisibility(View.GONE);
                                        }
                                    }
                                    else
                                    {
                                        auto_employment_type.setText("");
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

    private void setDataToUi(EditText myEditText, String strInput)
    {
        if(!TextUtils.isEmpty(strInput)&& !strInput.equalsIgnoreCase("null"))
        {
            myEditText.setText(strInput);
        }
        else
        {
            myEditText.setText("-");
        }
    }


    private void getState(String country_id)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("country_id", country_id);
            ServiceHandler serviceHandler = new ServiceHandler(MyProfileActivity.this);


            serviceHandler.registerUser(params, strStateUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;
                    String  userPhoneNumber="",countryName="",stateName="",userZipCode="",maritalStaus="",
                            userStreet1="",userStreet2="",userCitizenCountry="",userCitizenCountryname="",userAnnualIncome="",howMuchInvestmentVal="",
                            employmentType="",userCountry="",userState="";
                    list_state_name = new ArrayList<>();
                    list_state_id = new ArrayList<>();
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
                                    JSONArray countryJsonArr = jobject.getJSONArray("state");
                                    for(int i=0; i<countryJsonArr.length();i++)
                                    {
                                        JSONObject Obj = countryJsonArr.getJSONObject(i);
                                        list_state_name.add(Obj.getString("name"));
                                        list_state_id.add(Obj.getString("id"));

                                    }

                                    ArrayAdapter<String> adapter = new
                                            ArrayAdapter<String>(MyProfileActivity.this,android.R.layout.simple_list_item_1,list_state_name);

                                    auto_state.setAdapter(adapter);
                                    auto_state.setThreshold(1);


                                    Log.i("JWT","This Is Valid");
                                }
                                else
                                {
                                    Log.i("JWT","This Is Not Valid");
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

    private void updateUserContactInfo(String accountID, String siteUserID, String userPhoneNumber , String userStreet1 ,
                                String userCountry, String userState, String userZipCode )
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("accountID", accountID);
            params.put("siteUserID", siteUserID);
            params.put("userPhoneNumber", userPhoneNumber);
            params.put("userStreet1", userStreet1);
            params.put("userCountry", userCountry);
            params.put("userState", userState);
            params.put("userZipCode", userZipCode);

            ServiceHandler serviceHandler = new ServiceHandler(MyProfileActivity.this);

            serviceHandler.registerUser(params, strContactInfoUpdateUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;

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

    private void updateUserPersonalInfo(String accountID, String siteUserID, String maritalStaus , String userCitizenCountry ,
                                String userAnnualIncome, String howMuchInvestmentVal)
    {
        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("accountID", accountID);
            params.put("siteUserID", siteUserID);
            params.put("maritalStaus", maritalStaus);
            params.put("userCitizenCountry", userCitizenCountry);
            params.put("userAnnualIncome", userAnnualIncome);
            params.put("howMuchInvestmentVal", howMuchInvestmentVal);

            ServiceHandler serviceHandler = new ServiceHandler(MyProfileActivity.this);

            serviceHandler.registerUser(params, strPersonalInfoUpdateUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;

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

    private void updateUserEmploymentInfo(String accountID, String siteUserID, String userEmployementType , String userEmployeerName ,
                                        String userOccup)
    {
        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("accountID", accountID);
            params.put("siteUserID", siteUserID);
            params.put("userEmployementType", userEmployementType);
            params.put("userEmployeerName", userEmployeerName);
            params.put("userOccup", userOccup);

            ServiceHandler serviceHandler = new ServiceHandler(MyProfileActivity.this);

            serviceHandler.registerUser(params, strEmployeeInfoUpdateUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;

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
