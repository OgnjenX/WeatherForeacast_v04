package com.example.projekat1;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class CharacterAdapter extends BaseAdapter {

    private RadioButton rb = null;
    private Context mContext;
    private ArrayList<Character> mCharacters;

    public CharacterAdapter(Context context){
        mContext = context;
        mCharacters = new ArrayList<Character>();
    }
    public void addCharacter(Character c){
        mCharacters.add(c);
        notifyDataSetChanged();
    }

    public void deleteCharacter(int position){
        mCharacters.remove(position);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCharacters.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try {
            rv = mCharacters.get(position);
        } catch (IndexOutOfBoundsException e) {
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
            view = inflater.inflate(R.layout.character_row, null);
            ViewHolder holder = new ViewHolder();
            holder.mradioButton = (RadioButton) view.findViewById(R.id.radio_button_id);
            holder.mName = (TextView) view.findViewById(R.id.name_in_caracter_row);
            view.setTag(holder);
        }
        Character character = (Character) getItem(position);
        final ViewHolder holder  = (ViewHolder)view.getTag();
        holder.mName .setText(character.mName);

        holder.mradioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailsActivity.class);

                Bundle b = new Bundle();
                b.putString("grad",holder.mName.getText().toString() );
                intent.putExtras(b);

                mContext.startActivity(intent);


                rb = (RadioButton)v;
                rb.setChecked(false);
            }
        });

        return view;

    }

    private class ViewHolder {
        public RadioButton mradioButton = null;
        public TextView mName = null;
    }
}
