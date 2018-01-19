package com.infomanav.wahed;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.CategoryAdapter;
import adapter.ExpandableListAdapter;
import model_classes.Category;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class FragmentPosition extends Fragment implements OnChartValueSelectedListener
{

	View view;
	private PieChart mChart;
	protected String[] mMonths = new String[] {
			"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
	};

	protected String[] mParties = new String[] {
			"Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
			"Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
			"Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
			"Party Y", "Party Z"
	};

	protected Typeface mTfRegular;
	protected Typeface mTfLight;
	public DisplayMetrics displayMetrics;

	private RecyclerView feed_recycler_view;
	private CategoryAdapter categoryAdapter;
	private JWTToken jwtToken;
	private Utility utility;
	private String strPositionUrl = Application_Constants.Main_URL+"position";
	private  String strAccouontId="",strModelsFkId="";
    private List<String>list_color;
	public FragmentPosition()
	{
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Enable the option menu for the Fragment
		setHasOptionsMenu(true);
		
		view=inflater.inflate(R.layout.fragment_position,container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		displayMetrics = getResources().getDisplayMetrics();
		jwtToken = new JWTToken(getActivity());
		utility = new Utility(getActivity());
		strAccouontId = Shared_Preferences_Class.readString(getActivity(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");
        strModelsFkId = Shared_Preferences_Class.readString(getActivity(),Shared_Preferences_Class.MODELS_FK_ID,"");


		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayMetrics.widthPixels, (displayMetrics.widthPixels / 2)+300);


		mChart = (PieChart) view.findViewById(R.id.chart1);
		mChart.setUsePercentValues(false);
		mChart.getDescription().setEnabled(false);
		mChart.setExtraOffsets(5, 10, 5, 5);
		mChart.setLayoutParams(layoutParams); // set dianamic hight width

		mChart.setDragDecelerationFrictionCoef(0.95f);

		mChart.setCenterTextTypeface(mTfLight);
		mChart.setCenterText(generateCenterSpannableText());

		mChart.setDrawHoleEnabled(true);
		mChart.setHoleColor(Color.WHITE);

		mChart.setTransparentCircleColor(Color.WHITE);
		mChart.setTransparentCircleAlpha(110);

		mChart.setHoleRadius(75f); // // inner space
		mChart.setTransparentCircleRadius(0f);

		mChart.setDrawCenterText(false);

		mChart.setRotationAngle(0);
		// enable rotation of the chart by touch
		mChart.setRotationEnabled(false);
		mChart.setHighlightPerTapEnabled(true);

		// mChart.setUnit(" €");
		// mChart.setDrawUnitsInChart(true);

		// add a selection listener
		mChart.setOnChartValueSelectedListener(this);
		//setData(6, 100);

		mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
		// mChart.spin(2000, 0, 360);
		mChart.setDrawEntryLabels(false);


		Legend l = mChart.getLegend();
		l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
		l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
		l.setOrientation(Legend.LegendOrientation.VERTICAL);
		l.setDrawInside(true);
		l.setXEntrySpace(7f);
		l.setYEntrySpace(0f);
		l.setYOffset(0f);
		l.setEnabled(false);

		// entry label styling
		mChart.setEntryLabelColor(Color.WHITE);
		mChart.setEntryLabelTypeface(mTfRegular);
		mChart.setEntryLabelTextSize(12f);

		feed_recycler_view = (RecyclerView)view.findViewById(R.id.feed_recycler_view);
		feed_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
		feed_recycler_view.setNestedScrollingEnabled(false);
		feed_recycler_view.addItemDecoration(new SimpleDividerItemDecoration(25));

        list_color = new ArrayList<>();
        list_color.add("#5686B6");
        list_color.add("#000000");
        list_color.add("#001B4A");
        list_color.add("#0E3E6E");
        list_color.add("#326292");
        list_color.add("#5686B6");
        list_color.add("#000000");
        list_color.add("#5686B6");


		/*String Quantity ="116.0000000000";
		Quantity = String.format("%.2f", Double.valueOf(Quantity));*/

		setData();
		getPosition(strAccouontId,strModelsFkId);

		//mChart.setDrawMarkers(false);


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

	public void setData()
	{
		final ArrayList<Category> feedsArrayList = new ArrayList<Category>();


		/*for (int i = 0; i < 10; i++) {
			*//*JSONObject Obj = jArray.getJSONObject(i);
			// A category variables
			tokImage = Obj.getString("tokImage");
			tokDate = Obj.getString("tokDate");
			tokText = Obj.getString("tokText");
			userFirstName = Obj.getString("userFirstName");
			userLastName = Obj.getString("userLastName");
			userEmail = Obj.getString("userEmail");
			userProfileImage = Obj.getString("userProfileImage");
			productTitle  = Obj.getString("productTitle");
			tokView = Obj.getString("tokTotalView");
			tokLike= Obj.getString("tokLike");
			isTokLike= Obj.getString("isTokLike");
			tokID= Obj.getString("tokID");
			siteUserID= Obj.getString("siteUserID");
			productID= Obj.getString("productID");
			reTockcount = Obj.getString("reTockcount");
			tokShare = Obj.getString("tokShare");
			tagName	  = Obj.getString("tagName");



			tokDate = getFormatedTokDate(tokDate);

			String strUserName = userFirstName+" "+ userLastName;

			tokText = StringEscapeUtils.unescapeJava(tokText);


			if(!tagName.isEmpty())
			{
				if(!tagName.equalsIgnoreCase("0"))
				{
					tagName = tagName.replace('~',',');

					strUserName = "<b>"+strUserName +"</b>" + " <i>Tok with </i> "+ "<b>"+tagName+"</b>";

				}
			}*//*

		}*/

		/*Category feeds = new Category("Test","Test","Test",false,"#5686B6");
		feedsArrayList.add(feeds);
		feeds = new Category("Test","Test","Test",false,"#000000");
		feedsArrayList.add(feeds);
		feeds = new Category("Test","Test","Test",false,"#5686B6");
		feedsArrayList.add(feeds);
		feeds = new Category("Test","Test","Test",false,"#001B4A");
		feedsArrayList.add(feeds);
		feeds = new Category("Test","Test","Test",false,"#0E3E6E");
		feedsArrayList.add(feeds);
		feeds = new Category("Test","Test","Test",false,"#326292");
		feedsArrayList.add(feeds);


		feed_recycler_view.setVisibility(View.VISIBLE);
		categoryAdapter = new CategoryAdapter(getActivity(), feedsArrayList);

		feed_recycler_view.setAdapter(categoryAdapter);
*/


	}

	private void setData(int count, float range) {

		float mult = range;

		ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

		// NOTE: The order of the entries when being added to the entries array determines their position around the center of
		// the chart.
		/*for (int i = 0; i < count ; i++)
		{
			//entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mParties[i % mParties.length]));
			entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), ""));
		}*/

		entries.add(new PieEntry((float)54, "rest"));
		entries.add(new PieEntry((float) 5, ""));
		entries.add(new PieEntry((float) 3, ""));
		entries.add(new PieEntry((float) 13, ""));
		entries.add(new PieEntry((float) 3, ""));
		entries.add(new PieEntry((float) 34, ""));
		/*entries.add(new PieEntry((float) 6, ""));
		entries.add(new PieEntry((float) 11, ""));*/

		PieDataSet dataSet = new PieDataSet(entries, "Election Results");
		dataSet.setSliceSpace(2f); // slice space
		dataSet.setSelectionShift(5f);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();
		/*for (int c : ColorTemplate.VORDIPLOM_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.JOYFUL_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.COLORFUL_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.LIBERTY_COLORS)
			colors.add(c);*/

		/*for (int c : ColorTemplate.PASTEL_COLORS)
			colors.add(c);*/

		colors.add(rgb("#5686B6"));
		colors.add(rgb("#000000"));
		colors.add(rgb("#5686B6"));
		colors.add(rgb("#001B4A"));
		colors.add(rgb("#0E3E6E"));
		colors.add(rgb("#326292"));

		/*colors.add(rgb("#000000"));
		colors.add(rgb("#001B4A"));*/

		//colors.add(ColorTemplate.getHoloBlue());

		dataSet.setColors(colors);
		//dataSet.setSelectionShift(0f);

		PieData data = new PieData(dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.BLACK);
		data.setValueTypeface(mTfLight);
		mChart.setData(data);

		// remove text which is insite the slices
		mChart.highlightValues(null);
		for (IDataSet<?> set : mChart.getData().getDataSets())
			set.setDrawValues(!set.isDrawValuesEnabled());

		mChart.invalidate();
	}

	private SpannableString generateCenterSpannableText() {

		SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
		s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
		s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
		s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
		s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
		s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
		s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
		return s;
	}




	private void getPosition(final String accountID, final String modelsFkId)
	{

		if(utility.checkInternet())
		{
			//final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
			String strJwtToken = jwtToken.getJWTToken();

			final Map<String, String> params = new HashMap<String, String>();
			params.put("jwtToken", strJwtToken);
			params.put("accountID", accountID);
            params.put("modelsFkId", modelsFkId);
			ServiceHandler serviceHandler = new ServiceHandler(getActivity());


			serviceHandler.registerUser(params, strPositionUrl, new ServiceHandler.VolleyCallback() {
				@Override
				public void onSuccess(String result) {
					System.out.println("str_get_category_url Json responce" + result);

					//Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
					String str_json = result;
					String str_status, str_msg,server_jwt_token;
					String  id="",AccountNumber="",SecuritiesName="",Symbol="",Allocation="",
							Quantity="",CostBasisPrice="",MarketPrice="",InvestementValue="",
							PosValuePerSec="",UpdatedDate="",UpdatedDateUnix="",SecurityDescription="",
							UnrealizedG_LPerSecurity="",positionID="",apiPrice,apichange,apistatus;
					final ArrayList<Category> categoryArrayList = new ArrayList<Category>();
                    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                    ArrayList<Integer> colors = new ArrayList<Integer>();
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
										positionID = Obj.getString("positionID");
										AccountNumber = Obj.getString("AccountNumber");
										SecuritiesName = Obj.getString("SecuritiesName");
										Symbol = Obj.getString("Symbol");
										Allocation = Obj.getString("Allocation");
										Quantity = Obj.getString("Quantity");
										CostBasisPrice = Obj.getString("CostBasisPrice");
										MarketPrice = Obj.getString("MarketPrice");
										InvestementValue = Obj.getString("InvestementValue");
										UnrealizedG_LPerSecurity = Obj.getString("UnrealizedG");
										PosValuePerSec = Obj.getString("PosValuePerSec");
										UpdatedDate = Obj.getString("UpdatedDate");
										UpdatedDateUnix = Obj.getString("UpdatedDateUnix");
										SecurityDescription = Obj.getString("SecurityDescription");
										apiPrice = Obj.getString("apiPrice");
										apichange  = Obj.getString("apichange");
										apistatus = Obj.getString("apistatus");

										if(!TextUtils.isEmpty(Quantity)&&!Quantity.equalsIgnoreCase("null"))
										{
											Quantity = String.format("%.2f", Double.valueOf(Quantity));
										}

										if(TextUtils.isEmpty(SecurityDescription)||SecurityDescription.equalsIgnoreCase("null"))
										{
											SecurityDescription="N/A";
										}
										if(apichange.equalsIgnoreCase("N/A"))
										{
											apichange="0.00";
										}

                                        entries.add(new PieEntry((float)Float.parseFloat(Allocation), SecuritiesName));

                                        colors.add(rgb(list_color.get(i)));

										Category category = new Category(accountID,modelsFkId,positionID,AccountNumber,SecuritiesName,Symbol,Allocation,Quantity,CostBasisPrice,MarketPrice,
												InvestementValue, PosValuePerSec,
												UpdatedDate,UpdatedDateUnix,SecurityDescription,
												UnrealizedG_LPerSecurity,false,list_color.get(i),apiPrice,apichange,apistatus);
										categoryArrayList.add(category);


									}

                                    PieDataSet dataSet = new PieDataSet(entries, "Wahed Invests");
                                    dataSet.setSliceSpace(2f); // slice space
                                    dataSet.setSelectionShift(5f);

                                    dataSet.setColors(colors);
                                    //dataSet.setSelectionShift(0f);

                                    PieData data = new PieData(dataSet);
                                    data.setValueFormatter(new PercentFormatter());
                                    data.setValueTextSize(11f);
                                    data.setValueTextColor(Color.BLACK);
                                    data.setValueTypeface(mTfLight);
                                    mChart.setData(data);

                                    // remove text which is insite the slices
                                    mChart.highlightValues(null);
                                    for (IDataSet<?> set : mChart.getData().getDataSets())
                                        set.setDrawValues(!set.isDrawValuesEnabled());

                                    mChart.invalidate();

                                    feed_recycler_view.setVisibility(View.VISIBLE);
                                    categoryAdapter = new CategoryAdapter(getActivity(), categoryArrayList);
                                    feed_recycler_view.setAdapter(categoryAdapter);
									Log.i("JWT","This Is Valid");
								}
								else
								{
									Log.i("JWT","This Is Not Valid");
								}


							}
							else
							{
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



/*

	private void getFeeds()
	{

		if(utility.checkInternet())
		{

			final Map<String, String> params = new HashMap<String, String>();
			params.put("productID", productId);
			params.put("siteUserID", siteUserID);


			ServiceHandler1 serviceHandler = new ServiceHandler1(getActivity());
			final ArrayList<Feeds> feedsArrayList = new ArrayList<Feeds>();

			serviceHandler.registerUser(params, strGetfeeds, new ServiceHandler1.VolleyCallback() {
				@Override
				public void onSuccess(String result) {
					System.out.println("Json responce" + result);

					//Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
					String str_json = result;
					String str_status,str_msg;
					String userFirstName,userLastName,tokImage,tokDate,tokText,userEmail,userProfileImage,tokView,tokLike,isTokLike,
							tokID,siteUserID,productID,reTockcount,tokShare,productTitle,tagName;
					try {
						if(str_json!=null)
						{
							JSONObject jobject = new JSONObject(str_json);
							str_status = jobject.getString("status");
							str_msg = jobject.getString("msg");
							if(str_status.equalsIgnoreCase("success"))
							{
								JSONArray jArray = new JSONArray();
								jArray = jobject.getJSONArray("moreFeed");

								for (int i = 0; i < jArray.length(); i++)
								{
									JSONObject Obj = jArray.getJSONObject(i);
									// A category variables
									tokImage = Obj.getString("tokImage");
									tokDate = Obj.getString("tokDate");
									tokText = Obj.getString("tokText");
									userFirstName = Obj.getString("userFirstName");
									userLastName = Obj.getString("userLastName");
									userEmail = Obj.getString("userEmail");
									userProfileImage = Obj.getString("userProfileImage");
									productTitle  = Obj.getString("productTitle");
									tokView = Obj.getString("tokTotalView");
									tokLike= Obj.getString("tokLike");
									isTokLike= Obj.getString("isTokLike");
									tokID= Obj.getString("tokID");
									siteUserID= Obj.getString("siteUserID");
									productID= Obj.getString("productID");
									reTockcount = Obj.getString("reTockcount");
									tokShare = Obj.getString("tokShare");
									tagName	  = Obj.getString("tagName");



									tokDate = getFormatedTokDate(tokDate);

									String strUserName = userFirstName+" "+ userLastName;

									tokText = StringEscapeUtils.unescapeJava(tokText);


									if(!tagName.isEmpty())
									{
										if(!tagName.equalsIgnoreCase("0"))
										{
											tagName = tagName.replace('~',',');

											strUserName = "<b>"+strUserName +"</b>" + " <i>Tok with </i> "+ "<b>"+tagName+"</b>";

										}
									}


									Feeds feeds = new Feeds(strUserName,tokImage,tokDate,tokText,userEmail,userProfileImage,tokView,
											tokLike,isTokLike,tokID,siteUserID,productID,reTockcount,tokShare,productTitle);
									feedsArrayList.add(feeds);

								}

								if(str_status.equals("success"))
								{

									swipeContainer.setRefreshing(false);


									tv_feeds_not_found.setVisibility(View.GONE);
									feed_recycler_view.setVisibility(View.VISIBLE);
									feedsAdapter = new FeedsAdapter(getActivity(), feedsArrayList);

									feed_recycler_view.setAdapter(feedsAdapter);
								}
								else
								{
									tv_feeds_not_found.setVisibility(View.VISIBLE);
									feed_recycler_view.setVisibility(View.GONE);

								}


							}
							else
							{
								Toast.makeText(getActivity(), str_msg,Toast.LENGTH_SHORT).show();
							}
						}
						else
						{
							Toast.makeText(getActivity(), "This may be server issue",Toast.LENGTH_SHORT).show();
						}

					} catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}

	}
*/


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onValueSelected(Entry e, Highlight h) {

		if (e == null)
			return;
		Log.i("VAL SELECTED",
				"Value: " + e.getY() + ", index: " + h.getX()
						+ ", DataSet index: " + h.getDataSetIndex());
	}

	@Override
	public void onNothingSelected() {
	}
}
