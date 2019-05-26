package com.example.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ElementAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Element> mElements;
    private Intent intent;

    /* metoda za pravljenje novog adaptera */

    public ElementAdapter(Context context) {
        mContext = context;
        mElements = new ArrayList<Element>();
    }

    /* metoda za dodavanje elemenata u ListView */

    public void addElement(Element element) {
        boolean present = false;

        for(Element e: mElements) {
            if(e.mCityName.equals(element.mCityName)) {
                present = true;
                break;
            }
        }
        if(!present) {
            if(element.mCityName.length() > 0) {
                mElements.add(element);
                notifyDataSetChanged();
            }
        }
    }

    /* metoda za brisanje elemenata iz ListView */

    public void deleteElement(int position) {
        mElements.remove(position);
        notifyDataSetChanged();
    }

    /* ukupan broj elemenata koji ce biti prikazani u listi */

    @Override
    public int getCount() {
        return mElements.size();
    }

    @Override
    public Object getItem(int position) {
        Object retVal = null;
        try {
             retVal = mElements.get(position);
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.element_row, null);
            ViewHolder holder = new ViewHolder();
            holder.radioButton = view.findViewById(R.id.radio_button_main_list);
            holder.textView = view.findViewById(R.id.text_view_main_list);
            holder.mButton = view.findViewById(R.id.buton_list_add);
            view.setTag(holder);
        }

        Element element = (Element) getItem(position);
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.textView.setText(element.mCityName);

        intent = new Intent(mContext, DetailsActivity.class);

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("poslataLokacija", holder.textView.getText().toString());
                mContext.startActivity(intent);

                RadioButton rb = (RadioButton) v;
                rb.setChecked(false);
            }
        });

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("poslataLokacija", holder.textView.getText().toString());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    private class ViewHolder {
        public RadioButton radioButton = null;
        public TextView textView = null;
        public Button mButton = null;
    }
}
