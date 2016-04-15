package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.chart.BuildChart;
import com.sam_chordas.android.stockhawk.util.AppConstants;
import com.sam_chordas.android.stockhawk.util.NetworkUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class StockDetailActivity extends Activity implements SeekBar.OnSeekBarChangeListener,
        OnChartGestureListener, OnChartValueSelectedListener {

    private static String LOG_TAG = StockDetailActivity.class.getSimpleName();
    private OkHttpClient client = new OkHttpClient();
    private String symbol;
    private LineChart mChart;

    String fetchData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public void networkToast(){
        Toast.makeText(this, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        if (!NetworkUtil.checkInternetConnection(StockDetailActivity.this)) {
            NetworkUtil.showNetWorkSnackBar(StockDetailActivity.this);
            return;
        }
        Intent intent = getIntent();
        if ( intent.hasExtra(AppConstants.INTENT_EXTRA) ) {
            symbol = intent.getStringExtra(AppConstants.INTENT_EXTRA);
            DownloadQuoteDetailsTask quoteDetailsTask = new DownloadQuoteDetailsTask();
            quoteDetailsTask.execute(symbol);
        }
        initChart();
    }

    private void initChart(){
        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setNoDataText("Fetching datasets...");
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.setDescription("");
//        mChart.setNoDataTextDescription(getString(R.string.provide_data_for_chart));

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
//        mChart.setMarkerView(mv);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        LimitLine ll1 = new LimitLine(130f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTypeface(tf);

        LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setTypeface(tf);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaxValue(220f);
        leftAxis.setAxisMinValue(-50f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

//        // add Dummy data for initialisation
//        List<String> xValues = new ArrayList<>();
//        xValues.add("a");
//        xValues.add("b");
//        xValues.add("c");
//
//        List<Entry> yValues = new ArrayList<>();
//        yValues.add(new Entry(10.0F, 0));
//        yValues.add(new Entry(20.0F, 1));
//        yValues.add(new Entry(40.0F, 2));
//
//        setData(xValues, yValues);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

        // // dont forget to refresh the drawing
         mChart.invalidate();

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /*****************************************DownloadQuoteDetailsTask*****************************************************/
    private class DownloadQuoteDetailsTask extends AsyncTask<String, Void, BuildChart> {
        @Override
        protected BuildChart doInBackground(String... params) {

            String stockInput = params[0];

            StringBuilder urlStringBuilder = new StringBuilder();

            try{
                urlStringBuilder.append("http://chartapi.finance.yahoo.com/instrument/1.0/");
                urlStringBuilder.append(URLEncoder.encode(stockInput, "UTF-8"));
                urlStringBuilder.append("/chartdata;type=close;range=1y/json");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String urlString;
            urlString = urlStringBuilder.toString();

            String getResponse = "";

            try {
                getResponse = fetchData(urlString);
            } catch (IOException e){
                e.printStackTrace();
            }

            int startIndex = "finance_charts_json_callback( ".length();
            int endIndex = getResponse.length() - 1;

            getResponse = getResponse.substring(startIndex, endIndex);

            Log.d(LOG_TAG, getResponse);

            JSONObject jsonObject = null;
            JSONArray resultsArray = null;

            BuildChart buildChart = new BuildChart();
            float[] results;

            try {
                jsonObject = new JSONObject(getResponse);

                jsonObject = jsonObject.getJSONObject("ranges").getJSONObject("close");
                String  minRange = jsonObject.getString("min");
                String  maxRange = jsonObject.getString("max");
                buildChart.setMinimum(minRange);
                buildChart.setMaximum(maxRange);

                jsonObject = new JSONObject(getResponse);
                resultsArray = jsonObject.getJSONArray("series");

                buildChart.addDataPoint(resultsArray);
            } catch (JSONException e){
                Log.e(LOG_TAG, "String to JSON failed: " + e);
            }

            return buildChart;
        }

        @Override
        protected void onPostExecute(BuildChart buildChart) {
            super.onPostExecute(buildChart);

            mChart.getAxisLeft().setAxisMaxValue(buildChart.getMaximum());
            mChart.getAxisLeft().setAxisMinValue(buildChart.getMinimum());
            mChart.getAxisLeft().setTextColor(Color.WHITE);
            setData(buildChart.getXAxisList(), buildChart.getYAxisList());
            mChart.getXAxis().setValues(buildChart.getXAxisList());
            mChart.getXAxis().setTextColor(Color.WHITE);
            Legend l = mChart.getLegend();

            // modify the legend ... by default it is on the left
            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            l.setForm(Legend.LegendForm.SQUARE);
            mChart.invalidate();
        }
    }



    private void setData(List<String> xValues, List<Entry> yValues) {
        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setYVals(yValues);
            mChart.getData().setXVals(xValues);
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yValues, "Months");

            // set1.setFillAlpha(110);
             set1.setFillColor(Color.WHITE);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setValueTextColor(Color.WHITE);
            set1.setDrawFilled(true);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(xValues, dataSets);

            // set data
            mChart.setData(data);
        }
    }
}