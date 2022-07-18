package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.WallpaperColors;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.model.City;
import com.example.weatherapp.model.CustomAdapter;
import com.example.weatherapp.model.CustomRecyclerAdapter;
import com.example.weatherapp.model.Weather;
import com.example.weatherapp.model.iFirebaseLoadDone;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements com.example.weatherapp.model.iFirebaseLoadDone {
    TextView txtTemperature,txtSearch_City,txtCity,txtDay,txtDateDetail,txtDescription,txtTempMax,txtTempMin,txtHumidity;
    ImageView imgIconWeather;
    DatabaseReference cityRef;
    iFirebaseLoadDone iFirebaseLoadDone;
    List<City> cities;
    Dialog dialog;
    EditText editText;
    ListView listView;
    RecyclerView lv7Days;
    ArrayAdapter<String> adapter;
    int day,month,year;
    DatePickerDialog.OnDateSetListener setListener;
    ProgressBar progressBar;
    List<Weather> weatherArrayList = new ArrayList<>();
    CustomRecyclerAdapter customRecyclerAdapter;

    private final String appid = "ce5e97f388ca3857e075acfc75a98ebe";
    DecimalFormat df = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
        cityRef = FirebaseDatabase.getInstance().getReference("City");
        progressBar.setVisibility(View.VISIBLE);
        iFirebaseLoadDone = this;
        cityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<City> cities = new ArrayList<City>();
                for(DataSnapshot citySnapshot:snapshot.getChildren())
                {
                    cities.add(citySnapshot.getValue(City.class));
                };
                iFirebaseLoadDone.onFirebaseLoadSuccess(cities);
            }
            public void onCancelled(@NonNull DatabaseError error) {
                iFirebaseLoadDone.onFirebaseLoadFailed(error.getMessage());
            }
        });
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        String date = day+"/"+month+"/"+year;
        txtDay.setText(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy - HH:mm:ss");
        txtDateDetail.setText(dateFormat.format(calendar.getTime()));
        String city_name_null = txtSearch_City.getText().toString().trim();
        if(city_name_null.isEmpty()){
            GetCurrentWeatherData("Saigon");
            Get7DaysWeather("Saigon");
            setWeatherRecycler(weatherArrayList);
        }

    }

    private void addEvents() {
        txtSearch_City.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.show();
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    public void afterTextChanged(Editable editable) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        txtSearch_City.setText(adapter.getItem(i));
                        txtCity.setText(adapter.getItem(i));
                        String city_name = adapter.getItem(i).trim();
                        if(city_name.equals("Hồ Chí Minh"))
                            city_name = "Saigon";
                        GetCurrentWeatherData(city_name);
                        Get7DaysWeather(city_name);
                        dialog.dismiss();
                    }
                });
            }
        });
        txtDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month-1,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                month = m + 1;
                day = d;
                year= y;
                String date = day+"/"+month+"/"+year;
                txtDay.setText(date);
            }
        };
    }

    private void addControls() {
        txtSearch_City=findViewById(R.id.txtSearch_City);
        txtTemperature = (TextView) findViewById(R.id.txt_temperature);
        txtCity=findViewById(R.id.txt_City);
        txtDay=findViewById(R.id.txtDay);
        txtDateDetail=findViewById(R.id.txtDate_detail);
        txtDescription = findViewById(R.id.txt_descript);
        imgIconWeather = findViewById(R.id.imgIconWeather);
        txtTempMax = findViewById(R.id.txt_cv_max_temp);
        txtTempMin = findViewById(R.id.txt_cv_min_temp);
        txtHumidity=findViewById(R.id.txt_humid);
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(650,800);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editText=dialog.findViewById(R.id.edit_text);
        listView=dialog.findViewById(R.id.list_view);
        progressBar = dialog.findViewById(R.id.progressBar);
    }
    private void setWeatherRecycler(List<Weather> weatherArrayList){
        lv7Days=findViewById(R.id.lv_7days);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        lv7Days.setLayoutManager(layoutManager);
        customRecyclerAdapter = new CustomRecyclerAdapter(this,weatherArrayList);
        lv7Days.setAdapter(customRecyclerAdapter);
    }

    public void onFirebaseLoadSuccess(List<City> cityList) {
        progressBar.setVisibility(View.GONE);
        cities = cityList;
        List<String>name_list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,name_list);
        for (City city:cityList)
            name_list.add(city.getName());
            adapter.notifyDataSetChanged();

    }

    public void onFirebaseLoadFailed(String message) {

    }

    public void GetCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        final String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=ce5e97f388ca3857e075acfc75a98ebe";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String dt = jsonObject.getString("dt");
                            String ct_name = jsonObject.getString("name");
                            long l = Long.valueOf(dt);
                            Date date = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy - HH:mm:ss");
                            String DAY = simpleDateFormat.format(date);
                            txtDateDetail.setText(DAY);
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String desc = jsonObjectWeather.getString("description");
                            txtDescription.setText(desc);
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.get().load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(imgIconWeather);
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String temp = jsonObjectMain.getString("feels_like");
                            String temp_min = jsonObjectMain.getString("temp_min");
                            String temp_max = jsonObjectMain.getString("temp_max");
                            String humid = jsonObjectMain.getString("humidity");
                            txtTemperature.setText(temp + "\u2103");
                            txtTempMax.setText("Max: "+temp_max +"\u2103");
                            txtTempMin.setText("Min: "+temp_min +"\u2103");
                            txtHumidity.setText(humid+"%");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
    public void Get7DaysWeather(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        final String url = "https://api.openweathermap.org/data/2.5/forecast?q="+data+"&units=metric&cnt=7&appid=ce5e97f388ca3857e075acfc75a98ebe";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Ket qua" , response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            for (int i = 0;i<jsonArrayList.length();i++){
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String next_day = jsonObjectList.getString("dt");
                                long l = Long.valueOf(next_day);
                                Date date = new Date(l*1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM, HH:mm");
                                String DAY = simpleDateFormat.format(date);
                                JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                                String temp = jsonObjectMain.getString("feels_like");
                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");
                                weatherArrayList.add(new Weather(DAY,status,icon,temp));
                            }
                            setWeatherRecycler(weatherArrayList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
}