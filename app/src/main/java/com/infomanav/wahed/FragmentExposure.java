package com.infomanav.wahed;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.CategoryAdapter;
import adapter.ExposureAdapter;
import model_classes.Category;
import model_classes.Exposure;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class FragmentExposure extends Fragment implements OnChartValueSelectedListener
{

	View view;
	private JWTToken jwtToken;
	private Utility utility;
	private String strExposureUrl = Application_Constants.Main_URL+"exposure";
	protected com.github.mikephil.charting.charts.HorizontalBarChart mChart;
	private String strAccouontId="",Symbol="";
	protected Typeface mTfRegular;
	protected Typeface mTfLight;
	private RecyclerView exposure_recycler_view;
	private ExposureAdapter exposureAdapter;
	ArrayList<String> list_lables;
	private TextView tv_sector_text;
	XAxis xl;
	public FragmentExposure()
	{
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Enable the option menu for the Fragment
		setHasOptionsMenu(true);
		
		view=inflater.inflate(R.layout.fragment_exposure,container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		jwtToken = new JWTToken(getActivity());
		utility = new Utility(getActivity());
		strAccouontId = Shared_Preferences_Class.readString(getActivity(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");

		exposure_recycler_view = (RecyclerView)view.findViewById(R.id.exposure_recycler_view);
		exposure_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
		exposure_recycler_view.setNestedScrollingEnabled(false);
		exposure_recycler_view.addItemDecoration(new SimpleDividerItemDecoration(25));

		tv_sector_text = (TextView) view.findViewById(R.id.tv_sector_text);


        if(getArguments()!=null)
        {
            Symbol=getArguments().getString("Symbol");

          getExposure(strAccouontId,Symbol);
        }



		mChart = (com.github.mikephil.charting.charts.HorizontalBarChart) view.findViewById(R.id.chart1);
		mChart.setOnChartValueSelectedListener(this);
		// mChart.setHighlightEnabled(false);

		mChart.setDrawBarShadow(false);

		mChart.setDrawValueAboveBar(true);

		mChart.getDescription().setEnabled(false);

		// if more than 60 entries are displayed in the chart, no values will be
		// drawn
		mChart.setMaxVisibleValueCount(60);

		// scaling can now only be done on x- and y-axis separately
		mChart.setPinchZoom(false);

		// draw shadows for each bar that show the maximum value
		// mChart.setDrawBarShadow(true);

		mChart.setDrawGridBackground(false);

		xl = mChart.getXAxis();
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		xl.setTypeface(mTfLight);
		xl.setDrawAxisLine(true);
		xl.setDrawGridLines(false);
		xl.setGranularity(10f);

		YAxis yl = mChart.getAxisLeft();
		yl.setTypeface(mTfLight);
		yl.setDrawAxisLine(true);
		yl.setDrawGridLines(true);
		yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);


		//xl.setValueFormatter(new IndexAxisValueFormatter(schoolbag2));


		xl.setValueFormatter(new IAxisValueFormatter()
		{
			@Override
			public String getFormattedValue(float value, AxisBase axis)
			{
				String strCountry="";

			/*	String strCountry="";

				if(value==10)
				{
					strCountry = "USA";
				}
				else if(value==20)
				{
					strCountry = "JAPAN";
				}
				else if(value==30)
				{
					strCountry = "UK";
				}
				else if(value==40)
				{
					strCountry = "Swizerland";
				}
				else if(value==50)
				{
					strCountry = "France";
				}
				else if(value==60)
				{
					strCountry = "Germany";
				}
				else if(value==70)
				{
					strCountry = "Canada";
				}
				else if(value==80)
				{
					strCountry = "Other";
				}



				return strCountry;*/

				float val = value/10;

				strCountry = list_lables.get((int)val);

				return strCountry;
			}
		});

		YAxis yr = mChart.getAxisRight();
		yr.setTypeface(mTfLight);
		yr.setDrawAxisLine(true);
		yr.setDrawGridLines(false);
		yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

		//setData(8, 50);
		mChart.setFitBars(true);
		mChart.animateY(2500);


		Legend l = mChart.getLegend();
		l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
		l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
		l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
		l.setDrawInside(false);
		l.setFormSize(8f);
		l.setXEntrySpace(4f);
		l.setEnabled(false);

		// mChart.setDescription("");    // Hide the description
		//mChart.getAxisLeft().setDrawLabels(false);
		mChart.getAxisRight().setDrawLabels(true);
		mChart.getAxisLeft().setDrawLabels(false);
		//mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		//mChart.getXAxis().setDrawLabels(false);

		//   mChart.setViewPortOffsets(0f, 0f, 0f, 0f);



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


	private void setData(int count, float range)
	{


		ArrayList<String> list_lables = new ArrayList<String>();
		list_lables.add("jan");
		list_lables.add("feb");
		list_lables.add("mar");
		list_lables.add("apr");
		list_lables.add("may");
		list_lables.add("june");
		list_lables.add("jully");
		list_lables.add("aug");


		float barWidth = 9f;
		float spaceForBar = 10f;
		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        /*for (int i = 0; i < count; i++)
        {
            float val = (float) (Math.random() * range);
            yVals1.add(new BarEntry(i * spaceForBar, val));
        }*/


		yVals1.add(new BarEntry(10,5));
		yVals1.add(new BarEntry(20,40));
		yVals1.add(new BarEntry(30,40));
		yVals1.add(new BarEntry(40,20));
		yVals1.add(new BarEntry(50,30));
		yVals1.add(new BarEntry(60,10));


		BarDataSet set1;

		if (mChart.getData() != null &&
				mChart.getData().getDataSetCount() > 0)
		{
			set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
			set1.setValues(yVals1);
			mChart.getData().notifyDataChanged();
			mChart.notifyDataSetChanged();
		} else {
			set1 = new BarDataSet(yVals1, "DataSet 1");

			ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
			dataSets.add(set1);

			BarData data = new BarData(dataSets);
			data.setValueTextSize(10f);
			data.setValueTypeface(mTfLight);
			data.setBarWidth(barWidth);
			mChart.setData(data);
		}
	}

	@Override
	public void onValueSelected(Entry e, Highlight h) {
		mChart.setFitBars(true);
		mChart.invalidate();
	}

	@Override
	public void onNothingSelected() {
	}

    private void getExposure(final String accountID, final String symbol)
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


            serviceHandler.registerUser(params, strExposureUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("str_get_category_url Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;
                    String  exposure_id="",exposure_symbol="",exposure_description="",exposure_country="",
							exposure_percentage="",sector_id="",sector_symbol="",sector_description="",
							sector_name="",sector_percentage="";
                    final ArrayList<Exposure> exposureArrayList = new ArrayList<Exposure>();
					float barWidth = 9f;
					float spaceForBar = 10f;
					ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
					list_lables = new ArrayList<String>();
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
                                    jArray = jobject.getJSONArray("exposure_country");

									JSONArray jArraySector = new JSONArray();
									jArraySector = jobject.getJSONArray("exposure_sector");

                                    for (int i = 0; i < jArray.length(); i++)
                                    {
                                        JSONObject Obj = jArray.getJSONObject(i);
                                        // A category variables
										exposure_id = Obj.getString("exposure_id");
										exposure_symbol = Obj.getString("exposure_symbol");
										exposure_description = Obj.getString("exposure_description");
										exposure_country = Obj.getString("exposure_country");
										exposure_percentage = Obj.getString("exposure_percentage");

										list_lables.add(exposure_country);

										int temp = i *10;

										yVals1.add(new BarEntry(temp,Integer.parseInt(exposure_percentage)));
                                    }

									//xl.setValueFormatter(new IndexAxisValueFormatter(list_lables));


									BarDataSet set1;

									if (mChart.getData() != null &&
											mChart.getData().getDataSetCount() > 0)
									{
										set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
										set1.setValues(yVals1);
										mChart.getData().notifyDataChanged();
										mChart.notifyDataSetChanged();
									} else {
										set1 = new BarDataSet(yVals1, "DataSet 1");

										ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
										dataSets.add(set1);

										BarData data = new BarData(dataSets);
										data.setValueTextSize(10f);
										data.setValueTypeface(mTfLight);
										data.setBarWidth(barWidth);
										mChart.setData(data);
									}

                                    if(jArraySector.length()>0)
									{
										for (int i = 0; i < jArraySector.length(); i++)
										{
											JSONObject Obj = jArraySector.getJSONObject(i);
											// A category variables
											sector_id = Obj.getString("sector_id");
											sector_symbol = Obj.getString("sector_symbol");
											sector_description = Obj.getString("sector_description");
											sector_name = Obj.getString("sector_name");
											sector_percentage = Obj.getString("sector_percentage");

											Exposure category = new Exposure(sector_id,sector_symbol,sector_description,sector_name,sector_percentage);
											exposureArrayList.add(category);
										}

										tv_sector_text.setVisibility(View.VISIBLE);
										exposure_recycler_view.setVisibility(View.VISIBLE);
										exposureAdapter = new ExposureAdapter(getActivity(), exposureArrayList);
										exposure_recycler_view.setAdapter(exposureAdapter);
									}


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

}
