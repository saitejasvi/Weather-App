package com.example.vallsa7.jpmcweather.Network;

import com.example.vallsa7.jpmcweather.Model.ForecastData;
import com.example.vallsa7.jpmcweather.Model.WeatherData;
import com.example.vallsa7.jpmcweather.Utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Retrofit with GSON is used to make the remote API call to fetch weather information. An app-id from API is used which can only get
 * forecast for 5 consequent days without paid subscription.
 */

public class APICallService {

    private final ServicesDownloaderFromLocation servicesDownloaderFromLocation;
    private final ServicesDownloaderFromCoord servicesDownloaderFromCoord;
    private final ForecastFromLocation forecastFromLocation;
    private final ForecastFromCoord forecastFromCoord;

    public APICallService() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {

            @Override
            public void intercept(RequestFacade request) {

            }
        };
        // Get GSON
        Gson gson = new GsonBuilder().create();
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(2, TimeUnit.MINUTES);

        RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.FULL;
        // create the rest adapter
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(logLevel)
                .setEndpoint(Constants.OPEN_WEATHER_MAP_ENDPOINT)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(client))
                .setConverter(new GsonConverter(gson))
                .build();

        servicesDownloaderFromLocation = restAdapter.create(ServicesDownloaderFromLocation.class);
        servicesDownloaderFromCoord = restAdapter.create(ServicesDownloaderFromCoord.class);
        forecastFromLocation = restAdapter.create(ForecastFromLocation.class);
        forecastFromCoord = restAdapter.create(ForecastFromCoord.class);
    }

    public void getWeatherDataFromLocation(Callback<WeatherData> callback, String location) {
        String q = location;
        String appid = Constants.OWM_APP_ID;
        servicesDownloaderFromLocation.getWeatherData(q, appid, callback);
    }

    public void getWeatherDataFromCoord(Callback<WeatherData> callback, final String lat, final String lon) {
        String appid = Constants.OWM_APP_ID;
        servicesDownloaderFromCoord.getWeatherData(lat, lon, appid, callback);
    }

    public void getForecastFromLocation(Callback<ForecastData> callback, final String city, final String cnt) {
        String appid = Constants.OWM_APP_ID;
        String mode = Constants.JSON;
        String units = Constants.METRIC;
        forecastFromLocation.getWeatherData(city, mode, appid, callback);
    }

    public void getForecastFromCoord(Callback<ForecastData> callback, final String lat, final String lon, final String cnt) {
        String appid = Constants.OWM_APP_ID;
        forecastFromCoord.getWeatherData(lat, lon, appid, callback);
    }

    public interface ServicesDownloaderFromLocation {
            @GET(Constants.OWM_WEATHER)
            void getWeatherData(@Query(Constants.Q)String q,
                                @Query(Constants.APPID)String appid,
                                Callback<WeatherData> callback);
    }

    public interface ServicesDownloaderFromCoord
    {
        @GET(Constants.OWM_WEATHER)
        void getWeatherData(@Query(Constants.LATITUDE)String lat,
                            @Query(Constants.LONGITUDE)String lon,
                            @Query(Constants.APPID)String appid,
                            Callback<WeatherData> callback);
    }

    public interface ForecastFromLocation
    {
        @GET(Constants.OWM_FORECAST)
        void getWeatherData(@Query(Constants.Q)String city,
                            @Query(Constants.MODE)String mode,
                            @Query(Constants.APPID)String appid,
                            Callback<ForecastData> callback);
    }

    public interface ForecastFromCoord
    {
        @GET(Constants.OWM_FORECAST)
        void getWeatherData(@Query(Constants.LATITUDE)String lat,
                            @Query(Constants.LONGITUDE)String lon,
                            @Query(Constants.APPID)String appid,
                            Callback<ForecastData> callback);
    }
}


