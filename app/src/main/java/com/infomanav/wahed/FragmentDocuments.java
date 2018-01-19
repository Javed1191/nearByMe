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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.DocumentAdapter;
import adapter.SecurityAdapter;
import adapter.TransactionAdapter;
import model_classes.Document;
import model_classes.Security;
import model_classes.Transaction;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

public class FragmentDocuments extends Fragment
{

	View view;
	private RecyclerView document_recycler_view;
	private TextView tv_data_not_found;
	private DocumentAdapter documentAdapter;
	private String strDocumentsUrl = Application_Constants.Main_URL+"document";
	private JWTToken jwtToken;
	private Utility utility;
	private String strAccouontId="";
	public FragmentDocuments()
	{
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Enable the option menu for the Fragment
		setHasOptionsMenu(true);
		
		view=inflater.inflate(R.layout.fragment_documents,container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		init();
		getDocuments(strAccouontId);


		return view;
	}



	private void init()
	{
		document_recycler_view = (RecyclerView)view.findViewById(R.id.document_recycler_view);
		document_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
		tv_data_not_found = (TextView) view.findViewById(R.id.tv_data_not_found);
		jwtToken = new JWTToken(getActivity());
		utility = new Utility(getActivity());
		strAccouontId = Shared_Preferences_Class.readString(getActivity(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");

		/*final ArrayList<Document> transactionArrayList = new ArrayList<Document>();

		for (int i = 0; i < 20; i++)
		{

				Document feeds = new Document("FX Transaction & P L","Today","$50.123",false);
				transactionArrayList.add(feeds);




		}
		documentAdapter = new DocumentAdapter(getActivity(), transactionArrayList);
		document_recycler_view.setAdapter(documentAdapter);*/
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	private void getDocuments(String accountID)
	{

		if(utility.checkInternet())
		{
			//final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
			String strJwtToken = jwtToken.getJWTToken();

			final Map<String, String> params = new HashMap<String, String>();
			params.put("jwtToken", strJwtToken);
			params.put("accountID", accountID);
			ServiceHandler serviceHandler = new ServiceHandler(getActivity());


			serviceHandler.registerUser(params, strDocumentsUrl, new ServiceHandler.VolleyCallback() {
				@Override
				public void onSuccess(String result) {
					System.out.println("str_get_category_url Json responce" + result);

					//Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
					String str_json = result;
					String str_status, str_msg,server_jwt_token;
					String  name_document="",document_link="",document_date="",document_size="",document_desc="";
					final ArrayList<Document> transactionArrayList = new ArrayList<Document>();
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
									jArray = jobject.getJSONArray("document_data");

									for (int i = 0; i < jArray.length(); i++)
									{
										JSONObject Obj = jArray.getJSONObject(i);
										// A category variables

										name_document = Obj.getString("name_document");
										document_link = Obj.getString("document_link");
										document_date = Obj.getString("document_date");
										document_size = Obj.getString("document_size");

										document_desc = document_size+"  "+document_date;

										Document feeds = new Document(name_document,document_desc,document_link);
										transactionArrayList.add(feeds);

									}


									tv_data_not_found.setVisibility(View.GONE);
									document_recycler_view.setVisibility(View.VISIBLE);
									documentAdapter = new DocumentAdapter(getActivity(), transactionArrayList);
									document_recycler_view.setAdapter(documentAdapter);



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
								document_recycler_view.setVisibility(View.GONE);

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

}
