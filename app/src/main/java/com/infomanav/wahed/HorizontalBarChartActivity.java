package com.infomanav.wahed;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class HorizontalBarChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    protected com.github.mikephil.charting.charts.HorizontalBarChart mChart;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_bar_chart);

        mChart = (com.github.mikephil.charting.charts.HorizontalBarChart) findViewById(R.id.chart1);
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

        XAxis xl = mChart.getXAxis();
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



       /* xl.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {

                String strCountry="";

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


                *//*switch (value)
                {
                    case 10:
                        return "USA";
                    break;
                    case 20:
                        return "USA";
                    break;
                    case 30:
                        return "USA";
                    break;
                    case 40:
                        return "USA";
                    break;
                }
                return "Test";*//*
                return strCountry;
            }
        });*/

        YAxis yr = mChart.getAxisRight();
        yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        setData(8, 50);
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


    }

    @Override
    public void onValueSelected(Entry e, Highlight h)
    {
        mChart.setFitBars(true);
        mChart.invalidate();
    }

    @Override
    public void onNothingSelected() {
    }

    private void setData(int count, float range)
    {

        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        /*for (int i = 0; i < count; i++)
        {
            float val = (float) (Math.random() * range);
            yVals1.add(new BarEntry(i * spaceForBar, val));
        }*/


        yVals1.add(new BarEntry(10,30));
        yVals1.add(new BarEntry(20,50));
        yVals1.add(new BarEntry(30,40));
        yVals1.add(new BarEntry(40,50));
        yVals1.add(new BarEntry(50,30));
        yVals1.add(new BarEntry(60,70));
        yVals1.add(new BarEntry(70,80));
        yVals1.add(new BarEntry(80,60));
        yVals1.add(new BarEntry(90,300));
        yVals1.add(new BarEntry(100,50));
        yVals1.add(new BarEntry(120,300));
        yVals1.add(new BarEntry(150,80));


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0)
        {
            set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        }
        else
            {
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


}
