package com.infomanav.wahed;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import adapter.CategoryAdapter;
import model_classes.Category;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Shared_Preferences_Class;
import services.Utility;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class FragmentPerformance extends Fragment
{

	View view;
	private JWTToken jwtToken;
	private Utility utility;
	private String strPerformanceUrl = Application_Constants.Main_URL+"perfomance";
	private LineChart mChart;
	private String strAccouontId="",Symbol="";
	protected Typeface mTfRegular;
	protected Typeface mTfLight;
    ArrayList<String> list_lables,list_lables_date;
	XAxis xAxis;
	public FragmentPerformance()
	{
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Enable the option menu for the Fragment
		setHasOptionsMenu(true);
		
		view=inflater.inflate(R.layout.fragment_performance,container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		jwtToken = new JWTToken(getActivity());
		utility = new Utility(getActivity());
		strAccouontId = Shared_Preferences_Class.readString(getActivity(),Shared_Preferences_Class.USER_ACCOUNT_ID,"");

		mChart = (LineChart) view.findViewById(R.id.chart1);

		// no description text
		mChart.getDescription().setEnabled(false);

		// enable touch gestures
		mChart.setTouchEnabled(true);

		mChart.setDragDecelerationFrictionCoef(0.9f);

		// enable scaling and dragging
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		mChart.setDrawGridBackground(false);
		mChart.setHighlightPerDragEnabled(true);
		mChart.canScrollHorizontally(0);

		// set an alternative background color
		mChart.setBackgroundColor(Color.WHITE);
		mChart.setViewPortOffsets(50f, 50f, 0f, 0f);


		if(getArguments()!=null)
		{
			Symbol=getArguments().getString("Symbol");

			getPerformance(strAccouontId,Symbol);
		}

		// add data
		//setData(70, 50);
		mChart.invalidate();

		// get the legend (only possible after setting data)
		Legend l = mChart.getLegend();
		l.setEnabled(false);

		xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.TOP);
		xAxis.setTypeface(mTfLight);
		xAxis.setTextSize(10f);
		xAxis.setTextColor(Color.WHITE);
		xAxis.setDrawAxisLine(true);
		xAxis.setDrawGridLines(true);
		xAxis.setTextColor(Color.rgb(0, 0, 0));
		xAxis.setCenterAxisLabels(true);
		xAxis.setGranularity(1f); // one hour
		/*xAxis.setValueFormatter(new IAxisValueFormatter() {

			private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM");

			@Override
			public String getFormattedValue(float value, AxisBase axis)
			{

				System.out.print("Data"+value);
                return  "March";

               *//* long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));*//*


				//return String.valueOf(value*20);

			}
		});*/




		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		leftAxis.setTypeface(mTfLight);
		leftAxis.setTextColor(ColorTemplate.getHoloBlue());
		leftAxis.setDrawGridLines(true);
		leftAxis.setGranularityEnabled(true);
		leftAxis.setAxisMinimum(0f);
		//leftAxis.setAxisMaximum(170f);
		leftAxis.setYOffset(-9f);
		leftAxis.setTextColor(Color.rgb(0, 0, 0));

		YAxis rightAxis = mChart.getAxisRight();
		rightAxis.setEnabled(false);


        list_lables = new ArrayList<String>();
        list_lables.add("jan");
        list_lables.add("feb");
        list_lables.add("mar");
        list_lables.add("apr");
        list_lables.add("may");
        list_lables.add("june");
        list_lables.add("jully");
        list_lables.add("aug");

		/*leftAxis.setValueFormatter(new IAxisValueFormatter() {
			@Override
			public String getFormattedValue(float value, AxisBase axis) {
				return String.valueOf(value*100);
			}
		});*/


		//getOverview(strAccouontId);

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



	private void setData(int count, float range) {

        ArrayList<String> xVals = setXAxisValues();


		// now in hours
       /* long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());

        ArrayList<Entry> values = new ArrayList<Entry>();

        float from = now;

        // count = hours
        float to = now + count;

        // increment by 1 hour
        for (float x = from; x < to; x++) {

            float y = getRandom(range, 100);
            values.add(new Entry(x, y)); // add one entry per hour
        }*/

        ArrayList<String> list_lables = new ArrayList<String>();
        list_lables.add("jan");
        list_lables.add("feb");
        list_lables.add("mar");
        list_lables.add("apr");
        list_lables.add("may");
        list_lables.add("june");
        list_lables.add("jully");
        list_lables.add("aug");

		ArrayList<Entry> values = new ArrayList<Entry>();

		values.add(new Entry(20, 95));
		values.add(new Entry(40, 99));
		values.add(new Entry(60,150));
		values.add(new Entry(80, 130));
		values.add(new Entry(100, 155));
		values.add(new Entry(120, 144));
		values.add(new Entry(140, 100));
		values.add(new Entry(160, 120));
       /* values.add(new Entry(150, 110));
        values.add(new Entry(160, 90));
        values.add(new Entry(170, 120));
        values.add(new Entry(180, 150));*/
       /* values.add(new Entry(20, 30));
        values.add(new Entry(3, 40));
        values.add(new Entry(45, 50));
        values.add(new Entry(57, 60));
        values.add(new Entry(66, 70));
        values.add(new Entry(30, 80));
        values.add(new Entry(77, 90));
        values.add(new Entry(80, 91));
        values.add(new Entry(60, 110));
        values.add(new Entry(85, 120));
        values.add(new Entry(96, 130));*/

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(values, "DataSet 1");
		set1.setAxisDependency(YAxis.AxisDependency.LEFT);
		set1.setColor(ColorTemplate.getHoloBlue());
		set1.setValueTextColor(ColorTemplate.getHoloBlue());
		set1.setLineWidth(1.5f);
		set1.setDrawCircles(false);
		set1.setDrawValues(false);
		set1.setFillAlpha(65);
		set1.setFillColor(ColorTemplate.getHoloBlue());
		set1.setHighLightColor(Color.rgb(244, 117, 117));
		set1.setDrawCircleHole(false);

		// create a data object with the datasets
		LineData data = new LineData(set1);
		data.setValueTextColor(Color.WHITE);
		data.setValueTextSize(9f);

		// set data
		mChart.setData(data);

		mChart.animateX(3000);



	}

    private ArrayList<String> setXAxisValues(){
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");

        return xVals;
    }

    // This is used to store Y-axis values
    private ArrayList<Entry> setYAxisValues(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(60, 0));
        yVals.add(new Entry(48, 1));
        yVals.add(new Entry(70.5f, 2));
        yVals.add(new Entry(100, 3));
        yVals.add(new Entry(180.9f, 4));

        return yVals;
    }

	protected float getRandom(float range, float startsfrom) {
		return (float) (Math.random() * range) + startsfrom;
	}



	private void getPerformance(final String accountID, final String symbol)
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


			serviceHandler.registerUser(params, strPerformanceUrl, new ServiceHandler.VolleyCallback() {
				@Override
				public void onSuccess(String result) {
					System.out.println(strPerformanceUrl+" :Json responce" + result);

					//Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
					String str_json = result;
					String str_status, str_msg,server_jwt_token;
					String  date="",open="",high="",low="",close="",
							volume="",adj_close="";
					ArrayList<Entry> values = new ArrayList<Entry>();
					list_lables_date = new ArrayList<String>();

					SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM");
					SimpleDateFormat input = new SimpleDateFormat("yy-MM-dd");
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
									JSONObject jsonObject = new JSONObject();
									jsonObject = jobject.getJSONObject("user_data");

									if(jsonObject.length()>0)
									{
										JSONArray jArray = jsonObject.getJSONArray(Symbol);
										for (int i = 0; i < jArray.length(); i++)
										{
											JSONObject Obj = jArray.getJSONObject(i);
											// A category variables

											if(i>0)
											{
												date = Obj.getString("date");
												open = Obj.getString("open");
												high = Obj.getString("high");
												low = Obj.getString("low");
												close = Obj.getString("close");
												volume = Obj.getString("volume");
												adj_close = Obj.getString("adj_close");

											/*	Date date1 = new SimpleDateFormat("yyyy-mm-dd", Locale.US).parse(date);
												long milliseconds = date1.getTime();
												long millisecondsFromNow = milliseconds - (new Date()).getTime();
												Time time = new Time(milliseconds);

												Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
												calendar.setTime(date1);   // assigns calendar to given date
												calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
												calendar.get(Calendar.HOUR);        // gets hour in 12h format
												calendar.get(Calendar.MONTH);


												int hours = calendar.get(Calendar.HOUR);    *///Calendar.getInstance().get(date1.getHours());

												Date date1 = new SimpleDateFormat("yy-MM-dd").parse(date);

												date = mFormat.format(date1);



												list_lables_date.add(date);
												values.add(new Entry(i, Float.parseFloat(close)));

											}

										}
										/*values.add(new Entry(20, 95));
										values.add(new Entry(40, 99));
										values.add(new Entry(60,150));
										values.add(new Entry(80, 130));
										values.add(new Entry(100, 155));
										values.add(new Entry(120, 144));
										values.add(new Entry(140, 100));
										values.add(new Entry(160, 120));


*/
										xAxis.setValueFormatter(new IndexAxisValueFormatter(list_lables_date));

										// create a dataset and give it a type
										LineDataSet set1 = new LineDataSet(values, "DataSet 1");
										set1.setAxisDependency(YAxis.AxisDependency.LEFT);
										set1.setColor(ColorTemplate.getHoloBlue());
										set1.setValueTextColor(ColorTemplate.getHoloBlue());
										set1.setLineWidth(1.5f);
										set1.setDrawCircles(false);
										set1.setDrawValues(false);
										set1.setFillAlpha(65);
										set1.setFillColor(ColorTemplate.getHoloBlue());
										set1.setHighLightColor(Color.rgb(244, 117, 117));
										set1.setDrawCircleHole(true);

										// create a data object with the datasets
										LineData data = new LineData(set1);
										data.setValueTextColor(Color.WHITE);
										data.setValueTextSize(9f);

										// set data
										mChart.setData(data);

										mChart.animateX(3000);




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
					} catch (Exception e) {
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
