package com.nearbyme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.NearByListAdapter;
import classes.LocationPoints;
import classes.Place;
import services.Application_Constants;
import services.JWTToken;
import services.LoadingDialog;
import services.MCrypt;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;
import utils.GetCurrentLocation;
import views.DividerItemDecoration;

import static android.content.Context.LOCATION_SERVICE;

public class FragmentNearByMe extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
	View view;
	private ImageView img_list_view, img_map_view, img_ar_view;
	private RecyclerView mRecyclerView;
	private NearByListAdapter nearByListAdapter;
	private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
	private GoogleApiClient mGoogleApiClient;
	private SupportMapFragment mapFragment = null;
	private LocationManager locationManager;
	private Location mLastLocation;
	private double dbl_latitude = 0, dbl_longitude = 0;
	private MarkerOptions options = new MarkerOptions();
	private ArrayList<LocationPoints> locationFeedsArrayList;
	private RelativeLayout lay_map_view;
	private LoadingDialog loadingDialog;
	private JWTToken jwtToken;
	private Utility utility;
	List<NameValuePair> list_param;
	private String strGetArATMUrl = Application_Constants.Main_URL+"xAction=arvatm";
    private String strGetArBranchUrl = Application_Constants.Main_URL+"xAction=arvbranch";

    private String location_type="";
	private MCrypt mcrypt;
	ArrayList<Place> placeArrayList;
    private TextView tv_type;
	private GetCurrentLocation getCurrentLocation;

	private static final int START_ARVIEW = 12432;

	private LinearLayout lay_menu_list_view,lay_menu_map_view,lay_menu_ar_view;

	public FragmentNearByMe() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Enable the option menu for the Fragment
		setHasOptionsMenu(true);
		view = inflater.inflate(R.layout.fragment_near_by_me, container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		img_list_view = (ImageView) view.findViewById(R.id.img_list_view);
		img_map_view = (ImageView) view.findViewById(R.id.img_map_view);
		img_ar_view = (ImageView) view.findViewById(R.id.img_ar_view);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		lay_map_view = (RelativeLayout) view.findViewById(R.id.lay_map_view);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
		lay_menu_list_view = (LinearLayout) view.findViewById(R.id.lay_menu_list_view);
		lay_menu_map_view= (LinearLayout) view.findViewById(R.id.lay_menu_map_view);
		lay_menu_ar_view= (LinearLayout) view.findViewById(R.id.lay_menu_ar_view);

		mcrypt = new MCrypt();
		getCurrentLocation = new GetCurrentLocation(getActivity());

        if(getArguments()!=null)
        {
            location_type = getArguments().getString("location_type");

            if(location_type.equalsIgnoreCase("atm"))
            {
                tv_type.setText(R.string.atm);
            }
            else
            {
                tv_type.setText(R.string.branch);
            }
        }

		if (checkGooglePlayServices()) {
			buildGoogleApiClient();
		}

		locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

		utility = new Utility(getActivity());
		jwtToken = new JWTToken(getActivity());
		list_param = new ArrayList<>();
		//add ItemDecoration
		//mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
		//or
		//mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
		//or
		/*mRecyclerView.addItemDecoration(
				new DividerItemDecoration(getActivity(), R.drawable.divider));*/
		lay_menu_list_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				img_list_view.setImageResource(R.drawable.icon_list_view_active);
				img_map_view.setImageResource(R.drawable.icon_map_view);
				img_ar_view.setImageResource(R.drawable.icon_ar_view);

				mRecyclerView.setVisibility(View.VISIBLE);
				lay_map_view.setVisibility(View.GONE);
			}
		});
		lay_menu_map_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				img_list_view.setImageResource(R.drawable.icon_list_view);
				img_map_view.setImageResource(R.drawable.icon_map_view_active);
				img_ar_view.setImageResource(R.drawable.icon_ar_view);

				mRecyclerView.setVisibility(View.GONE);
				lay_map_view.setVisibility(View.VISIBLE);
				//setMap();
			}
		});
		lay_menu_ar_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*img_list_view.setImageResource(R.drawable.icon_list_view);
				img_map_view.setImageResource(R.drawable.icon_map_view);
				img_ar_view.setImageResource(R.drawable.icon_ar_view_active);*/

				img_ar_view.setImageResource(R.drawable.icon_ar_view_active);

				SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
				Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
				if (accelerometerSensor != null)
				{

					if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
					{
						placeArrayList = new ArrayList<Place>();
					}

					Intent intent =  new Intent(getActivity(),ARView.class);
					intent.putExtra("placeArrayList",placeArrayList);
					startActivityForResult(intent, START_ARVIEW);
				}
				else {
					new AlertDialog.Builder(getActivity())
							.setTitle("Near By Me")
							.setMessage("This device is not compatible for this functionality")
							.setCancelable(false)
							.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// Whatever...
								}
							}).show();
				}

				img_ar_view.setImageResource(R.drawable.icon_ar_view);
			}
		});

		if(location_type.equalsIgnoreCase("atm"))
		{
			new GetNearByLocaation().execute();
		}
		else
		{
			new GetNearByBanks().execute();
		}

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {
			if (resultCode == getActivity().RESULT_OK) {
				// Make sure the app is not already connected or attempting to connect
				if (!mGoogleApiClient.isConnecting() &&
						!mGoogleApiClient.isConnected()) {
					mGoogleApiClient.connect();
				}
			} else if (resultCode == getActivity().RESULT_CANCELED) {
				Toast.makeText(getActivity(), "Google Play Services must be installed.",
						Toast.LENGTH_SHORT).show();
			}
		}
		else if(requestCode == START_ARVIEW)
		{
			img_ar_view.setImageResource(R.drawable.icon_ar_view);
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
		int checkGooglePlayServices = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
		/*
		* Google Play Services is missing or update is required
		*  return code could be
		* SUCCESS,
		* SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
		* SERVICE_DISABLED, SERVICE_INVALID.
		*/
			GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
					getActivity(), REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
			return false;
		}
		return true;
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	public void setMap()
	{
		mapFragment.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap mMap) {


				// Styling map
				boolean success = mMap.setMapStyle(
						MapStyleOptions.loadRawResourceStyle(
								getActivity(), R.raw.style_json));

					try {
                        for (int i = 0; i < placeArrayList.size(); i++)
                        {
                            Place place = placeArrayList.get(i);
                            LatLng point = new LatLng(Double.parseDouble(place.getLat()), Double.parseDouble(place.getLog()));

                            Marker m = mMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .title(place.getShort_atm_name())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_marker)));

                            //setMarkerBounce(m);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


				LatLng latLng = new LatLng(dbl_latitude, dbl_longitude);
				Marker m = mMap.addMarker(new MarkerOptions()
						.position(latLng)
						.title("You")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.you_marker)));
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dbl_latitude, dbl_longitude), 15));


				mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
					@Override
					public boolean onMarkerClick(Marker marker) {

						//setMarkerBounce(marker);
						for(int i =0; i<placeArrayList.size();i++)
						{
							Place place = placeArrayList.get(i);


							if(place.getShort_atm_name().equals(marker.getTitle()))
							{
								showDialog(place.getShort_atm_name(),place.getDistance(),place.getAddress_1()+" "+place.getAddress_2()+" "+ place.getAddress_3(),place.getLat(),place.getLog());

								break;
							}
						}
						return false;
					}
				});

			}
		});
	}

	private void setMarkerBounce(final Marker marker) {
		final Handler handler = new Handler();
		final long startTime = SystemClock.uptimeMillis();
		final long duration = 2000;
		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - startTime;
				float t = Math.max(1 - interpolator.getInterpolation((float) elapsed/duration), 0);
				marker.setAnchor(1.0f, 1.0f +  t);

				if (t > 0.0) {
					handler.postDelayed(this, 16);
				} else {
					//setMarkerBounce(marker);
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
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
		if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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



			Toast.makeText(getActivity(), "Latitude:" + mLastLocation.getLatitude()+", Longitude:"+mLastLocation.getLongitude(),Toast.LENGTH_LONG).show();

		}
		else if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("GPS Not Enabled")
					.setMessage("Enable GPS")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
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

		private String atm_id,short_atm_name,base_branch_name,address_1,address_2,address_3,area,pincode,district,city,
				state,lat,log,distance;
		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();

			loadingDialog =  new LoadingDialog(getActivity());
			loadingDialog.show();

			img_list_view.setImageResource(R.drawable.icon_list_view_active);
			img_map_view.setImageResource(R.drawable.icon_map_view);
			img_ar_view.setImageResource(R.drawable.icon_ar_view);

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub


			ServiceHandler sh = new ServiceHandler(getActivity());
			String str_device_token = Shared_Preferences_Class.readString(getActivity(), Shared_Preferences_Class.DEVICE_TOKEN, "");

			String strJwtToken = jwtToken.getJWTToken();
			placeArrayList = new ArrayList<Place>();

			dbl_latitude = getCurrentLocation.getLatitude();
			dbl_longitude = getCurrentLocation.getLongitude();

			list_param.add(new BasicNameValuePair("device_token", str_device_token));
			list_param.add(new BasicNameValuePair("latitude", String.valueOf(dbl_latitude)));
			list_param.add(new BasicNameValuePair("longitude", String.valueOf(dbl_longitude)));
			list_param.add(new BasicNameValuePair("distance", "5"));
			list_param.add(new BasicNameValuePair("jwtToken", strJwtToken));

			Log.d("Location","Latitude: "+  String.valueOf(dbl_latitude)+" Longitude: "+String.valueOf(dbl_longitude));

			try {
				str_json = sh.makeServiceCall(strGetArATMUrl, ServiceHandler.POST, list_param);
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

						/*JSONObject jsonObject = new JSONObject();
						jsonObject = jobject.getJSONObject("VENUEDATA");*/


							for (int i = 0; i < jArray.length(); i++) {
								JSONObject Obj = jArray.getJSONObject(i);

								Place place = new Place();

								place.atm_id = Obj.getString("atm_id");
								place.short_atm_name = Obj.getString("short_atm_name");
								place.base_branch_name = Obj.getString("base_branch_name");
								place.address_1 = Obj.getString("address_1");
								place.address_2 = Obj.getString("address_2");
								place.address_3 = Obj.getString("address_3");
								place.area = Obj.getString("area");
								place.pincode = Obj.getString("pincode");
								place.district = Obj.getString("district");
								place.city = Obj.getString("city");
								place.state = Obj.getString("state");
								place.lat = Obj.getString("lat");
								place.log = Obj.getString("log");
								place.distance = Obj.getString("distance");


								placeArrayList.add(place);

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
					nearByListAdapter = new NearByListAdapter(getActivity(), placeArrayList);
					mRecyclerView.setAdapter(nearByListAdapter);

					setMap();
				} catch (Exception e) {
					e.printStackTrace();
				}



			}
			else if (str_status.equalsIgnoreCase("fail"))
			{

			}
			else
			{
				Toast.makeText(getActivity(), str_status,Toast.LENGTH_SHORT).show();
			}


		}

	}


    private class GetNearByBanks extends AsyncTask<String, String, String>
    {
        String str_json, str_status="",server_jwt_token;

        private String branch_id,branch_name,branch_city,branch_type,rbi_center_name,geographical_state,branch_address,pincode,lat,log,distance;
        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();

            loadingDialog =  new LoadingDialog(getActivity());
            loadingDialog.show();

            img_list_view.setImageResource(R.drawable.icon_list_view_active);
            img_map_view.setImageResource(R.drawable.icon_map_view);
            img_ar_view.setImageResource(R.drawable.icon_ar_view);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub


            ServiceHandler sh = new ServiceHandler(getActivity());
            String str_device_token = Shared_Preferences_Class.readString(getActivity(), Shared_Preferences_Class.DEVICE_TOKEN, "");

            String strJwtToken = jwtToken.getJWTToken();
            placeArrayList = new ArrayList<Place>();

            dbl_latitude = getCurrentLocation.getLatitude();
            dbl_longitude = getCurrentLocation.getLongitude();

            list_param.add(new BasicNameValuePair("device_token", str_device_token));
            list_param.add(new BasicNameValuePair("latitude", String.valueOf(dbl_latitude)));
            list_param.add(new BasicNameValuePair("longitude", String.valueOf(dbl_longitude)));
            list_param.add(new BasicNameValuePair("distance", "5"));
            list_param.add(new BasicNameValuePair("jwtToken", strJwtToken));

            Log.d("Location","Latitude: "+  String.valueOf(dbl_latitude)+" Longitude: "+String.valueOf(dbl_longitude));

            try {
                str_json = sh.makeServiceCall(strGetArBranchUrl, ServiceHandler.POST, list_param);
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

						/*JSONObject jsonObject = new JSONObject();
						jsonObject = jobject.getJSONObject("VENUEDATA");*/


                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject Obj = jArray.getJSONObject(i);

								Place place = new Place();

                                // A category variables
								place.atm_id = Obj.getString("branch_id");
								place.short_atm_name = Obj.getString("branch_name");
								place.base_branch_name = Obj.getString("branch_name");
								place.address_1 = Obj.getString("branch_address");
								place.address_2 = "";
								place.address_3 = "";
								place.area = "";
								place.pincode = Obj.getString("pincode");
								place.district = "";
								place.city = Obj.getString("branch_city");
								place.state = Obj.getString("geographical_state");

								//branch_type = Obj.getString("branch_type");
								//rbi_center_name = Obj.getString("rbi_center_name");

								place.lat = Obj.getString("lat");
								place.log = Obj.getString("log");
								place.distance = Obj.getString("distance");

                                placeArrayList.add(place);
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
				Log.e("", "Issue is here" + e.getMessage());
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
                    nearByListAdapter = new NearByListAdapter(getActivity(), placeArrayList);
                    mRecyclerView.setAdapter(nearByListAdapter);

                    setMap();
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
            else if (str_status.equalsIgnoreCase("fail"))
            {

            }
            else
            {
                Toast.makeText(getActivity(), str_status,Toast.LENGTH_SHORT).show();
            }
        }
    }

	public void showDialog(String strName, String strDistance, String strAddress, final String strLat, final String strLong)
	{
		final android.app.AlertDialog builder = new android.app.AlertDialog.Builder(getActivity()).create();

		builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View builder_view = inflater.inflate(R.layout.activity_near_by_me_location_detail_dialog, null);
		builder.setView(builder_view);
		//bid_now.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		// builder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


		TextView tv_confirm = (TextView) builder_view.findViewById(R.id.tv_confirm);
		TextView tv_cancel = (TextView) builder_view.findViewById(R.id.tv_cancel);
		final TextView tv_name = (TextView) builder_view.findViewById(R.id.tv_name);
		final TextView tv_address = (TextView) builder_view.findViewById(R.id.tv_address);
		final TextView tv_distance = (TextView) builder_view.findViewById(R.id.tv_distance);

		final LinearLayout lay_direction = (LinearLayout) builder_view.findViewById(R.id.lay_direction);


		tv_name.setText(strName);
		tv_address.setText(strAddress);

		strDistance = String.format("%.2f",Double.parseDouble(strDistance));
		tv_distance.setText("Distance: "+strDistance+" km");

		lay_direction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showRout(strLat,strLong);
			}
		});

		tv_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				try {
					builder.cancel();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		tv_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					//dialog.dismiss();
					builder.cancel();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		builder.show();
		Window window = builder.getWindow();
		window.setLayout(((NearByMeMainActivity)getActivity()).screenWidth/2, LinearLayout.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);

	}

	private void showRout(String strDesLat, String strDesLong)
	{
		String strStartLat="",strStartLong="";
		strStartLat = String.valueOf(dbl_latitude);
		strStartLong = String.valueOf(dbl_longitude);

		String strUrl = "http://maps.google.com/maps?saddr="+strStartLat+","+strStartLong+"&daddr="+strDesLat+","+strDesLong;

		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(strUrl));
		getActivity().startActivity(intent);
	}
}
