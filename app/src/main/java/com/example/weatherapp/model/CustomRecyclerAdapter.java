package com.example.weatherapp.model;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomRecyclerViewHolder> {

    Context context;
    List<Weather> weatherList;

    public CustomRecyclerAdapter(Context context, List<Weather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_day,parent,false);
        return new CustomRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerViewHolder holder, int position) {
        Picasso.get().load("https://openweathermap.org/img/wn/"+weatherList.get(position).getImageStatus()+"@2x.png").into(holder.imgStatus);
        holder.txtStatus.setText("Status: "+weatherList.get(position).getStatus());
        holder.txtDate.setText(weatherList.get(position).getDay());
        holder.txtTemp.setText("Temp: "+weatherList.get(position).getTemp()+"\u2103");

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static final class CustomRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView imgStatus;
        TextView txtDate,txtStatus,txtTemp;
        public CustomRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStatus = itemView.findViewById(R.id.img_cv_status_adapter);
            txtDate = itemView.findViewById(R.id.txt_next_day);
            txtStatus = itemView.findViewById(R.id.txt_cv_status_adapter);
            txtTemp = itemView.findViewById(R.id.txt_cv_temp_adapter);
        }
    }


}
