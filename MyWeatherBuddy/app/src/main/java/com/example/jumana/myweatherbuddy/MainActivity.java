package com.example.jumana.myweatherbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.jumana.myweatherbuddy.data.model.City;
import com.example.jumana.myweatherbuddy.data.model.Main;
import com.example.jumana.myweatherbuddy.data.remote.WeatherAPI;
import com.example.jumana.myweatherbuddy.themes.MainTheme;
import com.example.jumana.myweatherbuddy.themes.Utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String APPID = "a74b07463901e2cc9c3ea58f5b55ba3e";
    public static Context mcontext;
    public static Activity mActivity;
    Button params;
    CardView cardView;
    CardView secondCardView;
    CardView thirdCardView;
    TextView cityname;
    TextView weather;
    TextView Desc;
    TextView Sunrise;
    TextView Sunset;
    ImageView imageview;
    ListView maListViewPerso;
    DecimalFormat oneDigit = new DecimalFormat("#,##0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mActivity = this;
        this.mcontext = MainActivity.this;
        Utils.onActivityCreateSetTheme(this, Params.MyPREFERENCES, MainActivity.mcontext);

        params = (Button) findViewById(R.id.params);
        params.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Params.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        SharedPreferences sharedpreferences;
        sharedpreferences = MainActivity.this.getSharedPreferences(Params.MyPREFERENCES, MainActivity.MODE_PRIVATE);
        String search = sharedpreferences.getString("search", "error : no city");

        WeatherAPI.Factory.getInstance().getCity(search, APPID, "fr").enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                String name = response.body().getName() + ", " + response.body().getSys().getCountry() + "\n";
                String desc = response.body().getWeather().get(0).getMain() + " (" + response.body().getWeather().get(0).getDescription() + ")\n";
                String temp = oneDigit.format(response.body().getMain().getTemp() - 273.15) + "°\n";
                String temp_min_max =  oneDigit.format(response.body().getMain().getTempMin() - 273.15) + "° - " + oneDigit.format(response.body().getMain().getTempMax() - 273.15) + "°\n";
                String humidity = response.body().getMain().getHumidity().toString() + "\n";
                String clouds = response.body().getClouds().getAll().toString() + "\n";
                String wind_speed = response.body().getWind().getSpeed().toString() + " Km/h\n";
                String pression = response.body().getMain().getPressure().toString() + "\n";

                String sunrise = getDateCurrentTimeZone(response.body().getSys().getSunrise());
                String sunset = getDateCurrentTimeZone(response.body().getSys().getSunset());

                cityname = (TextView) findViewById(R.id.CityName);
                weather = (TextView) findViewById(R.id.ActualTemp);
                Desc = (TextView) findViewById(R.id.Desc);
                Sunrise = (TextView) findViewById(R.id.Sunrise);
                Sunset = (TextView) findViewById(R.id.Sunset);

                cardView = (CardView) findViewById(R.id.FirstCardView);
                secondCardView = (CardView) findViewById(R.id.SecondCardView);
                thirdCardView = (CardView) findViewById(R.id.ThirdCardView);


                cityname.setText(name);
                weather.setText(temp);
                Desc.setText(desc);
                Sunrise.setText(sunrise);
                Sunset.setText(sunset);
                String uri = "@drawable/a"+response.body().getWeather().get(0).getIcon();

                int imageResource = getResources().getIdentifier(uri, null, getPackageName());

                imageview = (ImageView)findViewById(R.id.imageView);
                Drawable res = getResources().getDrawable(imageResource);
                imageview.setImageDrawable(res);

                maListViewPerso = (ListView) findViewById(R.id.listviewperso);
                ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map;

                map = new HashMap<String, String>();
                map.put("titre", "Temperature Min - Max");
                map.put("description", temp_min_max);
                map.put("img", String.valueOf(R.drawable.thermometer));
                listItem.add(map);

                map = new HashMap<String, String>();
                map.put("titre", "Humidity");
                map.put("description", humidity);
                map.put("img", String.valueOf(R.drawable.humidity));
                listItem.add(map);

                map = new HashMap<String, String>();
                map.put("titre", "Clouds");
                map.put("description", clouds);
                map.put("img", String.valueOf(R.drawable.clouds));
                listItem.add(map);

                map = new HashMap<String, String>();
                map.put("titre", "Wind Speed");
                map.put("description", wind_speed);
                map.put("img", String.valueOf(R.drawable.wind));
                listItem.add(map);

                map = new HashMap<String, String>();
                map.put("titre", "Pressure");
                map.put("description", pression);
                map.put("img", String.valueOf(R.drawable.pressure));
                listItem.add(map);

                SimpleAdapter mSchedule = new SimpleAdapter(MainActivity.this, listItem, R.layout.affichageitem,
                        new String[]{"img", "titre", "description"}, new int[]{R.id.img, R.id.titre, R.id.description});

                maListViewPerso.setAdapter(mSchedule);
                cardView.setVisibility(View.VISIBLE);
                secondCardView.setVisibility(View.VISIBLE);
                thirdCardView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }
}

