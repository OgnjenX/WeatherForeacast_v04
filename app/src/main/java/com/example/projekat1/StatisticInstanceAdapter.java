package com.example.projekat1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class StatisticInstanceAdapter extends BaseAdapter {


    private ArrayList<StatisticInstance> mStatisticIntanceList;
    private Context mContext;

    public StatisticInstanceAdapter(Context context) {
        mContext = context;
        mStatisticIntanceList = new ArrayList<StatisticInstance>();
    }

    @Override
    public int getCount() {
        return mStatisticIntanceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStatisticIntanceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void update(StatisticInstance[] statisticInstances) {
        mStatisticIntanceList.clear();
        if (statisticInstances != null) {
            for (StatisticInstance i : statisticInstances) {
                mStatisticIntanceList.add(i);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.statistic_row, null);

            ViewHolder holder = new ViewHolder();
            holder.mDate = (TextView) convertView.findViewById(
                    R.id.datum_id);
            holder.mTemperature = (TextView) convertView.findViewById(
                    R.id.temperature_id);
            holder.mPressure = (TextView) convertView.findViewById(
                    R.id.pressure_id);
            holder.mHumidity = (TextView) convertView.findViewById(
                    R.id.hummidity_id);
            convertView.setTag(holder);
        }

        StatisticInstance statisticInstance = (StatisticInstance) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.mDate.setText(statisticInstance.getmDate());
        holder.mTemperature.setText(statisticInstance.getmTemperature().toString());
        holder.mHumidity.setText(statisticInstance.getmHummidity());
        holder.mPressure.setText(statisticInstance.getmPressure());

        return convertView;
    }

    private class ViewHolder {
        public TextView mDate = null;
        public TextView mTemperature = null;
        public TextView mPressure = null;
        public TextView mHumidity = null;
    }


}
