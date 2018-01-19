package com.infomanav.wahed;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.R.attr.x;
import static android.R.attr.y;

public class TimeChartActivity extends AppCompatActivity {
    private LineChart mChart;
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
    ArrayList<String> list_lables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_chart);


        list_lables = new ArrayList<String>();
        list_lables.add("jan");
        list_lables.add("feb");
        list_lables.add("mar");
        list_lables.add("apr");
        list_lables.add("may");
        list_lables.add("june");
        list_lables.add("jully");
        list_lables.add("aug");
        list_lables.add("sep");
        list_lables.add("act");
        list_lables.add("nov");
        list_lables.add("dec");
        list_lables.add("dec");
        list_lables.add("dec");
        list_lables.add("dec");
        list_lables.add("dec");
        list_lables.add("dec");

        mChart = (LineChart) findViewById(R.id.chart1);

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
        mChart.canScrollHorizontally(1);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        // add data
        setData(70, 50);
        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
       /* xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));


                //return String.valueOf(value*20);

            }
        });*/

        xAxis.setValueFormatter(new IndexAxisValueFormatter(list_lables));

       // mChart.getXAxis().setValueFormatter(new LabelFormatter(mMonths));


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(170f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

       /* leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(value*100);
            }
        });*/
    }

    private void setData(int count, float range) {

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
        ArrayList<Entry> values = new ArrayList<Entry>();

        values.add(new Entry(0, 95));
        values.add(new Entry(1, 99));
        values.add(new Entry(2,150));
        values.add(new Entry(3, 130));
        values.add(new Entry(4, 155));
        values.add(new Entry(5, 144));
        values.add(new Entry(6, 100));
        values.add(new Entry(7, 120));
        values.add(new Entry(8, 20));
        values.add(new Entry(9, 70));
        values.add(new Entry(10, 60));
        values.add(new Entry(12, 120));
        values.add(new Entry(13, 10));
        values.add(new Entry(14, 80));
        values.add(new Entry(15, 50));
        values.add(new Entry(16, 60));
        values.add(new Entry(17, 70));
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
    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }
}
