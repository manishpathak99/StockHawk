package com.sam_chordas.android.stockhawk.chart;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BuildChart {

    private ArrayList<String> mLabel;
    private ArrayList<Float> mValue;
    int mMinimum;
    int mMaximum;

    public BuildChart() {
        mLabel = new ArrayList<String>();
        mValue = new ArrayList<Float>();
    }

    public void addDataPoint(JSONObject jsonObject) {
        try {
            String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
            String close = jsonObject.getString("close");
            String closeDate = jsonObject.getString("Date");
            int currentMonth = Integer.valueOf(closeDate.substring(4,6))-1;
            mLabel.add(months[currentMonth]);
            mValue.add(Float.valueOf(close));
        } catch (JSONException e){
            Log.e("LOG_TAG", "String to JSON failed: " + e);
        }
    }

    public void addDataPoint(JSONArray jsonArray) {
        int curMonth = 0;
        ArrayList<Float> pointAvgByMonthly = new ArrayList<>();
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
        try {
            if (jsonArray != null && jsonArray.length() != 0){
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String close = jsonObject.getString("close");
                    String closeDate = jsonObject.getString("Date");
                    int currentMonth = Integer.valueOf(closeDate.substring(4,6))-1;
                    if(i == 0){
                        curMonth = currentMonth;
                    }
                    if(currentMonth != curMonth) {
                        curMonth = currentMonth;
                        float sum = 0;
                        for(Float aFloat : pointAvgByMonthly) {
                            sum += aFloat;
                        }
                        mLabel.add(months[currentMonth]);
                        mValue.add(sum / pointAvgByMonthly.size());
                        pointAvgByMonthly.clear();
                        //and take a sum  when month changes and add to mLabel and mValue List and reset array and store one point again
                    }
                        pointAvgByMonthly.add(Float.valueOf(close));
                        // store points in array

//                    buildChart.addDataPoint(jsonArray);
                }
            }




//            mLabel.add(months[currentMonth]);
//            mValue.add(Float.valueOf(close));
        } catch (JSONException e){
            Log.e("LOG_TAG", "String to JSON failed: " + e);
        }
    }

    public String[] getLabels() {
        String[] mResults = new String[mLabel.size()];
        mResults = mLabel.toArray(mResults);

        String previousLabel = mResults[0];

        for(int i = 1; i < mResults.length - 1; i++) {
            if (previousLabel.equalsIgnoreCase(mResults[i])) {
                mResults[i] = "";
            } else {
                previousLabel =  mResults[i];
            }
        }

        return mResults;
    }

    public float[] getPoints() {
        float[] mResults = new float[mValue.size()];
        int i = 0;

        for(Float f : mValue) {
            mResults[i++] = (f != null ? f : f.NaN);
        }

        return mResults;
    }

    public void setMinimum(String minimum) {

        Integer result = 0;

        try {
            String value = minimum.substring(0, minimum.indexOf("."));
            result = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            Log.e("LOG_TAG", e.toString());
        }

        if ( result < 100 ) {
            mMinimum = 0;
        } else {
            int length = minimum.indexOf(".") - 1;
            Integer firstDigit = Integer.valueOf(minimum.substring(0,1));
            mMinimum = (int)(firstDigit * (Math.pow((double)10.0, (double)length))) ;
        }
    }

    public void setMaximum(String maximum) {

        Integer result = 1000;

        try {
            String value = maximum.substring(0, maximum.indexOf("."));
            result = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            Log.e("LOG_TAG", e.toString());
        }

        int length = maximum.indexOf(".") - 1;
        Integer firstDigit = Integer.valueOf(maximum.substring(0, 1));
        mMaximum = (int)((firstDigit+1) * (Math.pow((double)10.0, (double)length)));
    }

    public int getMinimum() {
        return mMinimum;
    }

    public int getMaximum() {
        return mMaximum;
    }

    public List<String> getXAxisList(){
        return mLabel;
    }

    public List<Entry> getYAxisList(){
        List<Entry> yValues = new ArrayList<>();
        for (int index = 0; index < mValue.size(); index++) {
            yValues.add(new Entry(mValue.get(index), index));
        }
        return yValues;
    }
}