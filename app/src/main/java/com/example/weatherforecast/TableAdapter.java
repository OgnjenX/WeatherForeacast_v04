package com.example.weatherforecast;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TableAdapter extends BaseAdapter {
    @SuppressLint("ConstantLocale")
    private static final String day = new SimpleDateFormat("EEE", Locale.getDefault()).format(new Date());
    private static boolean isChecked = false;
    private ArrayList<Item> mList = new ArrayList<>();

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String[] getItem(int position) {
        String[] rv = null;
        try {
            rv = mList.get(position).getStrings();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return rv;
    }

    public void addItem(String string1, String string2, String string3, String string4) {
        mList.add(new Item(string1, string2, string3, string4));
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.table_row, null);
            rowView.setOnClickListener(null);

            ViewHolder viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            rowView = convertView;
        }

        String[] strings = getItem(position);
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        if (!isChecked && day.equals(strings[0])) {
            viewHolder.tv1.setTextColor(Color.RED);
            isChecked = true;
        }
        viewHolder.tv1.setText(strings[0]);
        viewHolder.tv2.setText(strings[1]);
        viewHolder.tv3.setText(strings[2]);
        viewHolder.tv4.setText(strings[3]);

        return rowView;
    }

    private static class Item {
        private String string1, string2, string3, string4;

        private Item(String string1, String string2, String string3, String string4) {
            this.string1 = string1;
            this.string2 = string2;
            this.string3 = string3;
            this.string4 = string4;
        }

        private String[] getStrings() {
            return new String[]{string1, string2, string3, string4};
        }
    }

    private static class ViewHolder {
        private TextView tv1, tv2, tv3, tv4;

        private ViewHolder(View view) {
            this.tv1 = view.findViewById(R.id.tableRowCel1);
            this.tv2 = view.findViewById(R.id.tableRowCel2);
            this.tv3 = view.findViewById(R.id.tableRowCel3);
            this.tv4 = view.findViewById(R.id.tableRowCel4);
        }
    }
}
