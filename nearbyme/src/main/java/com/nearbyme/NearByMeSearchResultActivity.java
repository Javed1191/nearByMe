package com.nearbyme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.SearchListAdapter;
import classes.Search;
import services.Application_Constants;
import services.JWTToken;
import services.LoadingDialog;
import services.MCrypt;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

public class NearByMeSearchResultActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{


    private EditText edt_application_no,edt_custome_name,edt_product_code,edt_company_code,edt_villege_code,
            edt_lc_code,edt_lg_code,edt_mobile;
    private RelativeLayout lay_continue;
    private ProgressDialog regDialog;
    private List<NameValuePair> list_param;
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
    private String searchKey;
    private LoadingDialog loadingDialog;
    private ArrayList<Search> placeArrayList;
    private String strSearchUrl = Application_Constants.Main_URL+"xAction=arvsearch";
    private SearchListAdapter searchListAdapter;
    private RecyclerView mRecyclerView;
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double dbl_latitude = 0, dbl_longitude = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_me_search_result);

       /* getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_text);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Result");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(NearByMeSearchResultActivity.this));

       /* mRecyclerView.addItemDecoration(
                new DividerItemDecoration(NearByMeSearchResultActivity.this, R.drawable.divider));*/
        jwtToken = new JWTToken(getApplicationContext());
        mcrypt = new MCrypt();

        if (checkGooglePlayServices()) {
            buildGoogleApiClient();
        }

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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        if(intent.getExtras()!=null)
        {
            searchKey = intent.getStringExtra("searchKey");
        }

        // status bar color change
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        String search_type = Shared_Preferences_Class.readString(getApplicationContext(),Shared_Preferences_Class.SEARCH_TYPE,"");
        new GetNearByLocaation(searchKey,search_type).execute();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(NearByMeSearchResultActivity.this, "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
		/*if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);
		if (mLastLocation != null) {
			dbl_latitude = mLastLocation.getLatitude();
			dbl_longitude = mLastLocation.getLongitude();
			//getNearBy(String.valueOf(dbl_latitude),String.valueOf(dbl_longitude),siteUserID);
			// Toast.makeText(this, "Latitude:" + mLastLocation.getLatitude()+", Longitude:"+mLastLocation.getLongitude(),Toast.LENGTH_LONG).show();
			setMap();
		}*/
        //checkPlayServices();
    }

    private boolean checkGooglePlayServices() {
        int checkGooglePlayServices = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
		/*
		* Google Play Services is missing or update is required
		*  return code could be
		* SUCCESS,
		* SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
		* SERVICE_DISABLED, SERVICE_INVALID.
		*/
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
                    this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null)
        {

            dbl_latitude =  mLastLocation.getLatitude();
            dbl_longitude =  mLastLocation.getLongitude();

            //getNearBy(String.valueOf(dbl_latitude),String.valueOf(dbl_longitude),siteUserID);

            Toast.makeText(getApplicationContext(), "Latitude:" + mLastLocation.getLatitude()+", Longitude:"+mLastLocation.getLongitude(),Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("LOCATION", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }



    private class GetNearByLocaation extends AsyncTask<String, String, String>
    {
        String str_json, str_status="",server_jwt_token;

        private String address,title,base_branch_name,address_1,address_2,address_3,area,district,city,
                state,search_type,txt_search,branch_id,branch_name,branch_city,branch_type,rbi_center_name,geographical_state,branch_address,pincode,lat,log,distance;

        private GetNearByLocaation(String txt_search, String search_type)
        {
            this.txt_search=txt_search;
            this.search_type=search_type;
        }

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();

            loadingDialog =  new LoadingDialog(NearByMeSearchResultActivity.this);
            loadingDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            ServiceHandler sh = new ServiceHandler(NearByMeSearchResultActivity.this);
            String str_device_token = Shared_Preferences_Class.readString(NearByMeSearchResultActivity.this, Shared_Preferences_Class.DEVICE_TOKEN, "");

            String strJwtToken = jwtToken.getJWTToken();
            placeArrayList = new ArrayList<Search>();
            list_param = new ArrayList<>();

            list_param.add(new BasicNameValuePair("device_token", str_device_token));
           /* list_param.add(new BasicNameValuePair("latitude", String.valueOf(dbl_latitude)));
            list_param.add(new BasicNameValuePair("longitude", String.valueOf(dbl_longitude)));*/
            list_param.add(new BasicNameValuePair("txt_search", txt_search));
            list_param.add(new BasicNameValuePair("search_type", search_type));
            list_param.add(new BasicNameValuePair("jwtToken", strJwtToken));


            try {
                str_json = sh.makeServiceCall(strSearchUrl, ServiceHandler.POST, list_param);
                if (str_json != null)
                {
                    JSONObject jobject = new JSONObject(str_json);
                    //JSONObject str_status_obj =jobject.getJSONObject("STATUS");

                    server_jwt_token= jobject.getString("server_jwt_token");
                    str_status = jobject.getString("status");

                    if(jwtToken.decryptJWTToken(server_jwt_token))
                    {
                        try {

                            str_status = new String( mcrypt.decrypt( str_status ), "UTF-8" );
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        if (str_status.equalsIgnoreCase("success"))
                        {
                            JSONArray jArray = new JSONArray();
                            jArray = jobject.getJSONArray("result");

                            if(jArray.length()>0)
                            {
                                for (int i = 0; i < jArray.length(); i++)
                                {
                                    JSONObject Obj = jArray.getJSONObject(i);

                                    // A category variables
                                    address = Obj.getString("address");
                                    area = Obj.getString("area");
                                    city = Obj.getString("city");
                                    lat = Obj.getString("lat");
                                    log = Obj.getString("log");
                                    title = Obj.getString("title");


                                    Search place = new Search(address,city,lat,log,title);
                                    placeArrayList.add(place);

                                    /*Apps country = new Apps(app_id, app_title, app_logo, app_link, package_name);
								appsList.add(country);*/
                                }

                            }
                        } else if (str_status.equalsIgnoreCase("fail")) {
                            //str_msg = jobject.getString("msg");

                        }
                    }
                    else
                    {
                        str_status="fail";
                    }
                }
                else
                {
                    str_status ="This may be server issue";
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //dialogApps.dismiss();
            if(loadingDialog!=null && loadingDialog.isShowing())
                loadingDialog.dismiss();
            super.onPostExecute(result);

            if (str_status.equalsIgnoreCase("success"))
            {
                try {
                    searchListAdapter = new SearchListAdapter(NearByMeSearchResultActivity.this, placeArrayList);
                    mRecyclerView.setAdapter(searchListAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (str_status.equalsIgnoreCase("fail"))
            {

            }
            else
            {
                Toast.makeText(NearByMeSearchResultActivity.this, str_status,Toast.LENGTH_SHORT).show();
            }


        }

    }


}
