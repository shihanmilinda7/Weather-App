package com.example.myweather;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemname;
    private final String[] monthno;
    private final String[] descrption;
    private final Integer[] imgid;

    private final String[] humidity;
    //private final String[] feels;
    private final String[] minMax;


    public CustomListAdapter(Activity context, String[] itemname, String[] monthno, Integer[] imgid, String[] descrption, String[] humidity, String[] minMax) {
        super(context, R.layout.my_list, itemname);
        this.context = context;
        this.itemname = itemname;
        this.monthno = monthno;
        this.imgid = imgid;
        this.descrption = descrption;
        this.humidity = humidity;
        //this.feels = feels;
        this.minMax = minMax;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        if(position == 0) {
            View rowView = inflater.inflate(R.layout.my_list_fav, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.dayName);
            TextView txtTitle_1 = (TextView) rowView.findViewById(R.id.tempValue);
            TextView txtTitle_2 = (TextView) rowView.findViewById(R.id.desc);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            TextView txtTitle_3 = (TextView) rowView.findViewById(R.id.liveHumidity);
            TextView txtTitle_4 = (TextView) rowView.findViewById(R.id.minMaxTemp);
            txtTitle.setText(itemname[position]);
            txtTitle_1.setText(monthno[position]);
            txtTitle_2.setText(descrption[position]);
            txtTitle_3.setText(humidity[position]);
            txtTitle_4.setText(minMax[position]);
            imageView.setImageResource(imgid[position]);
            return rowView;
        }else{
            View rowView = inflater.inflate(R.layout.my_list, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.dayName);
            TextView txtTitle_1 = (TextView) rowView.findViewById(R.id.tempValue);
            TextView txtTitle_2 = (TextView) rowView.findViewById(R.id.desc);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            txtTitle.setText(itemname[position]);
            txtTitle_1.setText(monthno[position]);
            txtTitle_2.setText(descrption[position]);
            imageView.setImageResource(imgid[position]);
            return rowView;
        }
    }
}


