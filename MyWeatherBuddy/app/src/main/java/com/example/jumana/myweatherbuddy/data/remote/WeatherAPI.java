package com.example.jumana.myweatherbuddy.data.remote;

import com.example.jumana.myweatherbuddy.data.model.City;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherAPI {

    String BASE_URL = "http://api.openweathermap.org/";

    @GET("/data/2.5/weather")
    Call<City> getCity(@Query("q") String query, @Query("appid") String id);

    class Factory {

        private static WeatherAPI service;

        public static WeatherAPI getInstance() {

            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                service = retrofit.create(WeatherAPI.class);

                return service;
            } else {
                return service;
            }
        }
    }
}