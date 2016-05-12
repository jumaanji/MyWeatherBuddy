package com.example.jumana.myweatherbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jumana.myweatherbuddy.data.model.City;
import com.example.jumana.myweatherbuddy.data.remote.WeatherAPI;
import com.example.jumana.myweatherbuddy.themes.MainTheme;
import com.example.jumana.myweatherbuddy.themes.Utils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Params extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    Button btnShowLocation;
    GPSTracker gps;
    Button changetheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.mcontext);

                // check if GPS enabled
                if (gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    String msg = "";
                    Geocoder gcd = new Geocoder(MainActivity.mcontext, Locale.getDefault());
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

                } else {
                    Log.d("tag", "je passe par ici");
                    gps.showSettingsAlert();
                    Log.d("tag", "je passe par ici aussi");
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

        changetheme = (Button) findViewById(R.id.changeTheme);
        changetheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.mcontext, MainTheme.class);
                MainActivity.mcontext.startActivity(myIntent);
            }
        });

    }

    //Fonction d'affichage du code
    public void Launch(String search) {
        SharedPreferences sharedpreferences = getSharedPreferences(Params.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("search", search);
        editor.apply();
        this.finish();
        this.startActivity(new Intent(this, MainActivity.class));
    }
}
