package com.infomanav.wahed;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.CategoryAdapter;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

public class FragmentShariahboard extends Fragment
{

	View view;
	private JWTToken jwtToken;
	private Utility utility;
	private String strShariahBoardUrl = Application_Constants.Main_URL+"shariaboard";
	private String strAccouontId="";
	private TouchyWebView webView;
	private String symbol="";
	public FragmentShariahboard()
	{
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Enable the option menu for the Fragment
		setHasOptionsMenu(true);
		
		view=inflater.inflate(R.layout.fragment_shariahboard,container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		jwtToken = new JWTToken(getActivity());
		utility = new Utility(getActivity());
		strAccouontId = Shared_Preferences_Class.readString(getActivity(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");
		webView = (TouchyWebView)view.findViewById(R.id.webView1);

		if(getArguments()!=null)
		{
			symbol=getArguments().getString("symbol");

			getShariahBoard(strAccouontId,symbol);
		}

		WebSettings webSetting = webView.getSettings();
		// webSetting.setBuiltInZoomControls(true);
		webSetting.setJavaScriptEnabled(true);




		return view;
	}

	/*boolean _areLecturesLoaded = false;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && !_areLecturesLoaded ) {
			loadLectures();
			_areLecturesLoaded = true;
		}
	}*/


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	private void getShariahBoard(String accountID, String symbol)
	{

		if(utility.checkInternet())
		{

			//final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
			String strJwtToken = jwtToken.getJWTToken();

			final Map<String, String> params = new HashMap<String, String>();
			params.put("jwtToken", strJwtToken);
			params.put("accountID", accountID);
			params.put("symbol", symbol);
			ServiceHandler serviceHandler = new ServiceHandler(getActivity());


			serviceHandler.registerUser(params, strShariahBoardUrl, new ServiceHandler.VolleyCallback() {
				@Override
				public void onSuccess(String result) {
					System.out.println("str_get_category_url Json responce" + result);

					//Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
					String str_json = result;
					String str_status, str_msg,server_jwt_token;
					String  sharia_board="";
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
									sharia_board = jobject.getString("sharia_board");

									webView.setBackgroundColor(Color.TRANSPARENT);
									webView.getSettings().setJavaScriptEnabled(true);
									webView.loadDataWithBaseURL("", sharia_board, "text/html", "UTF-8", "");

									Log.i("JWT","This Is Valid");
								}
								else
								{
									Log.i("JWT","This Is Not Valid");
								}


							}
							else
							{
								Toast.makeText(getActivity(), str_msg, Toast.LENGTH_SHORT).show();
							}

						}
						else
						{
							Toast.makeText(getActivity(), "This is server issue", Toast.LENGTH_SHORT).show();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		else {
			//Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
			Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "No Internet Connection", Snackbar.LENGTH_LONG);
			TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
			tv.setTextColor(getResources().getColor(R.color.colorAccent));
			snackbar.show();
		}

	}
}
