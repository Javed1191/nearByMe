package com.infomanav.wahed;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import adapter.SecurityAdapter;
import adapter.TransactionAdapter;
import model_classes.Security;
import model_classes.Transaction;
import model_classes.TransactionObj;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.infomanav.wahed.R.id.toolbar;
import static com.infomanav.wahed.R.id.tv_action_title;

public class FragmentTabTransaction extends Fragment
{

	View view;
	private RecyclerView transaction_recycler_view;
	private TextView tv_data_not_found;
	private TransactionAdapter transactionAdapter;
	private String strTransactionUrl = Application_Constants.Main_URL+"transaction";
	private JWTToken jwtToken;
	private Utility utility;
	private  String strAccouontId="";

	public FragmentTabTransaction()
	{
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Enable the option menu for the Fragment
		setHasOptionsMenu(true);
		
		view=inflater.inflate(R.layout.fragment_transaction,container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		init();
		getTransaction(strAccouontId);
		//getTransaction1(strAccouontId);

		return view;
	}


	private void init()
	{
		jwtToken = new JWTToken(getActivity());
		utility = new Utility(getActivity());
		strAccouontId = Shared_Preferences_Class.readString(getActivity(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");
		transaction_recycler_view = (RecyclerView)view.findViewById(R.id.transaction_recycler_view);
		transaction_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
		tv_data_not_found = (TextView) view.findViewById(R.id.tv_data_not_found);

		/*final ArrayList<Transaction> transactionArrayList = new ArrayList<Transaction>();

		for (int i = 0; i < 20; i++)
		{
			if((i % 2 == 1))
			{
				Transaction feeds = new Transaction("FX Transaction & P L","Today","$50.123",false);
				transactionArrayList.add(feeds);
			}
			else
			{
				Transaction feeds = new Transaction("FX Transaction & P L","Today","$50.123",true);
				transactionArrayList.add(feeds);
			}



		}
		transactionAdapter = new TransactionAdapter(getActivity(), transactionArrayList);
		transaction_recycler_view.setAdapter(transactionAdapter);*/
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	private void getTransaction1(String accountID)
	{

		if(utility.checkInternet())
		{
			//final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
			String strJwtToken = jwtToken.getJWTToken();

			final Map<String, String> params = new HashMap<String, String>();
			params.put("jwtToken", strJwtToken);
			params.put("accountID", accountID);
			ServiceHandler serviceHandler = new ServiceHandler(getActivity());

			serviceHandler.callAPIObj(params, strTransactionUrl,"transaction", new ServiceHandler.VolleyCallbackObject() {
				@Override
				public void onSuccess(Object result) {
					System.out.println("str_get_category_url Json responce" + result);

					//Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
					//String str_json = result;
					String str_status, str_msg,server_jwt_token;
					String  title="",id="",userAccountId="",externalTransferId="",state="",
							amount="",estimatedFundsAvailableDate="",achRelationshipId="",fees="",
							FundingReqDate="",disbursementType="",requestedAmount="",rejectReason=""
							,GeneratedBy="",trade_line_status="",fundsAvailable="",withdrawVerified="";
					 ArrayList<Transaction> transactionArrayList = new ArrayList<Transaction>();
					TransactionObj transactionObj=null;
					try {

						transactionObj = (TransactionObj) result;
						transactionArrayList = transactionObj.getTransactionArrayList();
						tv_data_not_found.setVisibility(View.GONE);
						transaction_recycler_view.setVisibility(View.VISIBLE);
						transactionAdapter = new TransactionAdapter(getActivity(), transactionArrayList);
						transaction_recycler_view.setAdapter(transactionAdapter);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		else {
			Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
		}

	}


	private void getTransaction(String accountID)
	{

		if(utility.checkInternet())
		{
			//final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
			String strJwtToken = jwtToken.getJWTToken();

			final Map<String, String> params = new HashMap<String, String>();
			params.put("jwtToken", strJwtToken);
			params.put("accountID", accountID);
			ServiceHandler serviceHandler = new ServiceHandler(getActivity());


			serviceHandler.registerUser(params, strTransactionUrl, new ServiceHandler.VolleyCallback() {
				@Override
				public void onSuccess(String result) {
					System.out.println("str_get_category_url Json responce" + result);

					//Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
					String str_json = result;
					String str_status, str_msg,server_jwt_token;
					String  title="",id="",userAccountId="",externalTransferId="",state="",
                            amount="",estimatedFundsAvailableDate="",achRelationshipId="",fees="",
                            FundingReqDate="",disbursementType="",requestedAmount="",rejectReason=""
                    ,GeneratedBy="",trade_line_status="",fundsAvailable="",withdrawVerified="",direction="";
					final ArrayList<Transaction> transactionArrayList = new ArrayList<Transaction>();
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


                                        title = Obj.getString("title");
                                        id = Obj.getString("id");
                                        userAccountId = Obj.getString("userAccountId");
                                        externalTransferId = Obj.getString("externalTransferId");
                                        state = Obj.getString("state");
                                        amount = Obj.getString("amount");
                                        estimatedFundsAvailableDate = Obj.getString("estimatedFundsAvailableDate");
                                        achRelationshipId = Obj.getString("achRelationshipId");
                                        fees = Obj.getString("fees");
                                        FundingReqDate = Obj.getString("FundingReqDate");
                                        disbursementType = Obj.getString("disbursementType");
                                        requestedAmount = Obj.getString("requestedAmount");
                                        rejectReason = Obj.getString("rejectReason");
                                        GeneratedBy = Obj.getString("GeneratedBy");
                                        trade_line_status = Obj.getString("trade_line_status");
                                        fundsAvailable = Obj.getString("fundsAvailable");
                                        withdrawVerified = Obj.getString("withdrawVerified");
										direction = Obj.getString("direction");
										FundingReqDate = dateFormatChange(FundingReqDate);

										if(direction.equalsIgnoreCase("OUTGOING"))
										{
											Transaction feeds = new Transaction(title,FundingReqDate,amount,false);
											transactionArrayList.add(feeds);
										}
										else
										{
											Transaction feeds = new Transaction(title,FundingReqDate,amount,true);
											transactionArrayList.add(feeds);
										}


									}


									tv_data_not_found.setVisibility(View.GONE);
									transaction_recycler_view.setVisibility(View.VISIBLE);
									transactionAdapter = new TransactionAdapter(getActivity(), transactionArrayList);
									transaction_recycler_view.setAdapter(transactionAdapter);


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
								transaction_recycler_view.setVisibility(View.GONE);

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
			Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
		}

	}


	private String dateFormatChange(String strActualDate)
	{
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		//SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		Date date = null;
		try {
			 date = format.parse(strActualDate);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String strDate = "";
		// Display a date in day, month, year format
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		// Display date with a short day and month name
		formatter = new SimpleDateFormat("EEE, dd MMMM yyyy");
		strDate = formatter.format(date);

		return  strDate;
	}


}
