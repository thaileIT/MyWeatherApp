package com.example.weatherapp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Weather> arrayList;

    public CustomAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.row_day,null);
        Weather weather = arrayList.get(i);

        TextView txtDay = view.findViewById(R.id.txt_next_day);
        TextView txtStatus = view.findViewById(R.id.txt_cv_status_adapter);
        TextView txtTemp = view.findViewById(R.id.txt_cv_temp_adapter);
        ImageView imgStatus = view.findViewById(R.id.img_cv_status_adapter);

        txtDay.setText(weather.Day);
        txtStatus.setText(weather.Status);
        txtTemp.setText(weather.Temp+"\u2013");

        Picasso.get().load("https://openweathermap.org/img/wn/"+weather.ImageStatus+"@2x.png").into(imgStatus);

        return view;
    }
}
