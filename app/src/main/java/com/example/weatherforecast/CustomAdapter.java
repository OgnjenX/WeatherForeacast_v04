package com.example.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Grad> mGradovi;
    private RadioButton selected;


    public CustomAdapter(Context c){
        mContext = c;
        mGradovi = new ArrayList<Grad>();
    }

    public void AddGrad(Grad g){
        mGradovi.add(g);
        notifyDataSetChanged();
    }

    public void RemoveGrad(int position){
        mGradovi.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mGradovi.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try{
            rv = mGradovi.get(position);
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return rv;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.element_row, null);
            ViewHolder holder = new ViewHolder();
            holder.imeGrada = view.findViewById(R.id.PrikazImenaGrada);
            holder.button = view.findViewById(R.id.SelekcijaGrada);
            view.setTag(holder);
        }

        Grad grad = (Grad) getItem(position);
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.imeGrada.setText(grad.mImeGrada);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailsActivity.class);
                Bundle b = new Bundle();
                b.putString("text_grad_u_listi", holder.imeGrada.getText().toString());
                intent.putExtras(b);
                mContext.startActivity(intent);

                selected = (RadioButton)v;
                selected.setChecked(false);
            }
        });
        return view;
    }

    private class ViewHolder{
        TextView imeGrada = null;
        RadioButton button;
    }
}
