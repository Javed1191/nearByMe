package com.infomanav.wahed;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

import static com.infomanav.wahed.R.id.toolbar;
import static com.infomanav.wahed.R.id.tv_action_title;

public class FragmentOverview extends Fragment
{

	View view;
	private Button btn_horizontal_bar_chart,btn_pie_chart,btn_line_time_chart;
	private Context mContext;
	private JWTToken jwtToken;
	private Utility utility;
	private String strOverviewUrl = Application_Constants.Main_URL+"overview";
	private String strAccouontId="";
	private TextView tv_market_value,tv_total_gains,tv_time_weighted_return,tv_deposit_withdrawals,tv_gain_loss,tv_realized,tv_unrealized,
			tv_advisor_fees,tv_others,tv_ending_value;
    private LinearLayout lay_parent;
	public FragmentOverview()
	{
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Enable the option menu for the Fragment
		setHasOptionsMenu(true);
		
		view=inflater.inflate(R.layout.fragment_overview,container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		jwtToken = new JWTToken(getActivity());
		utility = new Utility(getActivity());
		mContext = getActivity();
		strAccouontId = Shared_Preferences_Class.readString(getActivity(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");

		btn_pie_chart = (Button) view.findViewById(R.id.btn_pie_chart);
		btn_horizontal_bar_chart = (Button) view.findViewById(R.id.btn_horizontal_bar_chart);
		btn_line_time_chart = (Button) view.findViewById(R.id.btn_line_time_chart);

		tv_market_value = (TextView) view.findViewById(R.id.tv_market_value);
		tv_total_gains = (TextView) view.findViewById(R.id.tv_total_gains);
		tv_time_weighted_return= (TextView) view.findViewById(R.id.tv_time_weighted_return);
		tv_deposit_withdrawals= (TextView) view.findViewById(R.id.tv_deposit_withdrawals);
		tv_gain_loss= (TextView) view.findViewById(R.id.tv_gain_loss);
		tv_realized= (TextView) view.findViewById(R.id.tv_realized);
		tv_unrealized= (TextView) view.findViewById(R.id.tv_unrealized);
		tv_advisor_fees= (TextView) view.findViewById(R.id.tv_advisor_fees);
		tv_others= (TextView) view.findViewById(R.id.tv_others);
		tv_ending_value= (TextView) view.findViewById(R.id.tv_ending_value);
        lay_parent = (LinearLayout) view.findViewById(R.id.lay_parent);

		btn_horizontal_bar_chart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,HorizontalBarChartActivity.class);
				startActivity(intent);

			}
		});
		btn_pie_chart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,PieChartActivity.class);
				startActivity(intent);

			}
		});
		btn_line_time_chart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,TimeChartActivity.class);
				startActivity(intent);

			}
		});

		getOverview(strAccouontId);

		return view;
	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}



	private void getOverview(String accountID)
	{

		if(utility.checkInternet())
		{

			//final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
			String strJwtToken = jwtToken.getJWTToken();

			final Map<String, String> params = new HashMap<String, String>();
			params.put("jwtToken", strJwtToken);
			params.put("accountID", accountID);
			ServiceHandler serviceHandler = new ServiceHandler(getActivity());


			serviceHandler.registerUser(params, strOverviewUrl, new ServiceHandler.VolleyCallback() {
				@Override
				public void onSuccess(String result) {
					System.out.println("str_get_category_url Json responce" + result);

					//Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
					String str_json = result;
					String str_status, str_msg,server_jwt_token;
					String  MarketValue="",TotalGains="",TWR="",NetDeposits="",GainLoss="",
							GainLossReal="",GainLossUnreal="",AdvisorsFees="";
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
									JSONArray userJsonArr = jobject.getJSONArray("user_data");

									for(int i=0; i<userJsonArr.length();i++)
									{
										JSONObject userJson = userJsonArr.getJSONObject(i);

										if(userJson.length()>0)
										{
											MarketValue = userJson.getString("MarketValue");
											TotalGains = userJson.getString("TotalGains");
											TWR = userJson.getString("TWR");
											NetDeposits = userJson.getString("NetDeposits");
											GainLoss = userJson.getString("GainLoss");
											GainLossReal = userJson.getString("GainLossReal");
											GainLossUnreal = userJson.getString("GainLossUnreal");
											AdvisorsFees = userJson.getString("AdvisorsFees");
										}


									}



									setDataToUi(tv_market_value,MarketValue);
									setDataToUi(tv_total_gains,TotalGains);
									setDataToUi(tv_time_weighted_return,TWR);
									setDataToUi(tv_deposit_withdrawals,NetDeposits);
									setDataToUi(tv_gain_loss,GainLoss);
									setDataToUi(tv_realized,GainLossReal);
									setDataToUi(tv_unrealized,GainLossUnreal);
									setDataToUi(tv_advisor_fees,AdvisorsFees);
									setDataToUi(tv_others,"");
									setDataToUi(tv_ending_value,"");

									Log.i("JWT","This Is Valid");
								}
								else
								{
									Log.i("JWT","This Is Not Valid");
								}


							}
							else
							{
								setDataToUi(tv_market_value,MarketValue);
								setDataToUi(tv_total_gains,TotalGains);
								setDataToUi(tv_time_weighted_return,TWR);
								setDataToUi(tv_deposit_withdrawals,NetDeposits);
								setDataToUi(tv_gain_loss,GainLoss);
								setDataToUi(tv_realized,GainLossReal);
								setDataToUi(tv_unrealized,GainLossUnreal);
								setDataToUi(tv_advisor_fees,AdvisorsFees);
								setDataToUi(tv_others,"");
								setDataToUi(tv_ending_value,"");

								//Toast.makeText(getActivity(), str_msg, Toast.LENGTH_SHORT).show();
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

	private void setDataToUi(TextView myTextView, String strInput)
	{
		if(!TextUtils.isEmpty(strInput)&& !strInput.equalsIgnoreCase("null"))
		{
			strInput = String.format("%.2f", Double.valueOf(strInput));
			myTextView.setText("$ "+strInput);
		}
		else
		{
			myTextView.setText("$ 0.00");
		}
	}

}
