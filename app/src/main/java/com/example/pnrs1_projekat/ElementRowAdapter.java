package com.example.pnrs1_projekat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ElementRowAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ElementRow> mElements;
    private RadioButton selected = null;

    public ElementRowAdapter(Context context){
        mContext = context;
        mElements = new ArrayList<ElementRow>();
    }
    public void addElement(ElementRow element){
        mElements.add(element);
        notifyDataSetChanged();
    }
    public void removeElement(int position){
        mElements.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mElements.size();
    }

    @Override
    public Object getItem(int position) {
        Object returnValue = null;
        try{
            returnValue = mElements.get(position);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return returnValue;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.element_row, null);
            ViewHolder holder = new ViewHolder();
            holder.button = (Button) view.findViewById(R.id.buttonChangeActivity);
            holder.city = (TextView) view.findViewById(R.id.textViewLocation);
            view.setTag(holder);
        }

        ElementRow element = (ElementRow) getItem(position);
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.city.setText(element.location);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, DetailsActivity.class);
                Bundle b = new Bundle();
                b.putString("location", holder.city.getText().toString());
                myIntent.putExtras(b);
                mContext.startActivity(myIntent);

                selected = (RadioButton)v;
                selected.setChecked(false);
            }
        });

        return view;
    }

    private class ViewHolder{
        public Button button;
        public TextView city = null;
    }
}
