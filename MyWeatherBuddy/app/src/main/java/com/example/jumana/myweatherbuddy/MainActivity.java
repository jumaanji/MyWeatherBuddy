package com.example.jumana.myweatherbuddy;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jumana.myweatherbuddy.data.model.City;
import com.example.jumana.myweatherbuddy.data.remote.WeatherAPI;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String APPID = "a74b07463901e2cc9c3ea58f5b55ba3e";
    TextView cityname;
    DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
    Button btnShowLocation;
    // GPSTracker class
    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                cityname = (TextView) findViewById(R.id.CityName);
                cityname.setText("");
                // create class object
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    String msg = "Your Location is - \nLat: " + latitude + "\nLong: " + longitude;
                    Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        msg = addresses.get(0).getLocality();
                        //ici la requette avec l'addresse de géolocalisation
                        Launch(msg);
                    }

                }else{
                    gps.showSettingsAlert();
                }

            }
        });

        final EditText editText = (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = editText.getText().toString();

                if (search != "" && search != null && !search.isEmpty()) {
                    Launch(search);
                }

            }
        });

    }

    //Fonction d'affichage du code
    public void Launch(String search) {
        WeatherAPI.Factory.getInstance().getCity(search, APPID).enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                String name = "City : " + response.body().getName() + "\n";
                String weather = "Weather : " + response.body().getWeather().get(0).getMain() + " (" + response.body().getWeather().get(0).getDescription() + ")\n";
                String temp = "Temperature : " + oneDigit.format(response.body().getMain().getTemp() - 273.15) + "°\n";
                String temp_min = "Temparature minimum : " + oneDigit.format(response.body().getMain().getTempMin() - 273.15) + "°\n";
                String temp_max = "Temparature maximum : " + oneDigit.format(response.body().getMain().getTempMax() - 273.15) + "°\n";
                String humidity = "Humidity : " + response.body().getMain().getHumidity().toString() + "\n";
                String clouds = "Clouds : " + response.body().getClouds().getAll().toString() + "\n";
                String wind_speed = "Wind speed : " + response.body().getWind().getSpeed().toString() + "\n";
                String wind_deg = "Wind Degree : " + response.body().getWind().getDeg().toString() + "\n";
                cityname = (TextView) findViewById(R.id.CityName);
                cityname.setText(name + weather + temp + temp_min + temp_max + humidity + clouds + wind_speed + wind_deg);
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
}

