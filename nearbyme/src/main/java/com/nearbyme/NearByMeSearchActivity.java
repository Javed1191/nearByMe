package com.nearbyme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import java.util.List;

import services.Application_Constants;
import services.JWTToken;
import services.MCrypt;
import services.Utility;

public class NearByMeSearchActivity extends AppCompatActivity
{

    private EditText edt_application_no,edt_custome_name,edt_product_code,edt_company_code,edt_villege_code,
            edt_lc_code,edt_lg_code,edt_mobile;
    private RelativeLayout lay_continue;
    private ProgressDialog regDialog;
    private List<NameValuePair> list_param;
    private String strRegistrUrl = Application_Constants.Main_URL+"xAction=userSignUp";
    private String strEmpCode="",str_user_gcm_reg_no="123",forgot_pass="";
    private Button btn_login;
    private Utility utility;
    private TextView tv_register,tv_bottom_text,tv_action_title,tv_submit,tv_reset;
    private String master_key="1081882EF91SC6045F3B";
    private JWTToken jwtToken;
   private MCrypt mcrypt;
    private Spinner spn_request_type,spn_product_code;
    private List<String> list_request_type,list_product_code;
    private CheckBox chk_yes,chk_no;

    private String strRequestType,strApplicationNo,strCustomerName,strProductCode,strCompanyCode,strVillageCode,
            strSingleName="Y",strLcCode,strLgcode,strMobile;
    private boolean is_exist=false;
    private ProgressDialog leadSoDialog;
    private ImageView img_back,img_mic;
    private EditText edt_search;

    private LinearLayout lnrLytBranch, lnrLytATM;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_me_search);

        img_back = (ImageView) findViewById(R.id.img_back);
        edt_search = (EditText) findViewById(R.id.edt_search);
        img_mic = (ImageView) findViewById(R.id.img_mic);

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    String strSearchKey = edt_search.getText().toString();
                    if(!strSearchKey.isEmpty())
                    {

                        Intent intent = new Intent(NearByMeSearchActivity.this,NearByMeSearchResultActivity.class);
                        intent.putExtra("searchKey",strSearchKey);
                        startActivity(intent);

                    }
                    else {
                        edt_search.setError("Please enter any keyword");
                    }


                    return true;
                }
                return false;
            }
        });


       /* if(!strEmpCode.equals("")&&!strEmpCode.equals(null))
        {
            Intent intent = new Intent(RegisterActivity.this,MainActivity2.class);
            startActivity(intent);
            finish();
        }
        else
        {
            GCMRegistrar.checkDevice(this);
            GCMRegistrar.checkManifest(this);

            GCMRegistrar.register(RegisterActivity.this,
                    GCMIntentService.SENDER_ID);


            str_user_gcm_reg_no = GCMIntentService.REGISTRAION_ID;
            String gcmreg = str_user_gcm_reg_no;

            String imeistring= utility.getDeviceId();
        }*/
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // status bar color change
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        lnrLytATM = (LinearLayout) findViewById(R.id.lnrLytATM);
        lnrLytATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("to", "atm");
                setResult(RESULT_OK, data);
                finish();
            }
        });

        lnrLytBranch = (LinearLayout) findViewById(R.id.lnrLytBranch);
        lnrLytBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("to", "branch");
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
