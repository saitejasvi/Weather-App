package com.example.vallsa7.jpmcweather.View;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vallsa7.jpmcweather.Model.ForecastData;
import com.example.vallsa7.jpmcweather.Model.WeatherData;
import com.example.vallsa7.jpmcweather.Network.APICallService;
import com.example.vallsa7.jpmcweather.Network.CheckConnection;
import com.example.vallsa7.jpmcweather.Preferences.CityPrefs;
import com.example.vallsa7.jpmcweather.Preferences.Prefs;
import com.example.vallsa7.jpmcweather.R;
import com.example.vallsa7.jpmcweather.Utils.Constants;
import com.example.vallsa7.jpmcweather.Utils.WeatherIcons;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class WeatherFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;

    // GPS location request variables
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    CheckConnection cc;
    CityPrefs cityPrefs;
    Prefs prefs;
    Handler handler;

    // Views for the fragment
    RecyclerView horizontalRecyclerView;
    SwipeRefreshLayout swipeView;
    View rootView;
    TextView windView;
    TextView humidityView;
    TextView dailyView;
    TextView updatedField;
    TextView cityField;
    TextView sunriseView;
    TextView sunsetView;
    ImageView windIcon;
    ImageView huminityIcon;
    ImageView sunriseIcon;
    ImageView sunsetIcon;
    ImageView weatherIcon;
    TextView temp;
    TextView farenheit;
    WeatherIcons icons;

    // Constructor for the fragment class
    public WeatherFragment() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Creating and returning the view
        rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        setHasOptionsMenu(true);
        icons = new WeatherIcons();
        windView = rootView.findViewById(R.id.wind_view);
        humidityView = rootView.findViewById(R.id.humidity_view);
        swipeView = rootView.findViewById(R.id.swipe);
        dailyView = rootView.findViewById(R.id.daily_view);
        updatedField = rootView.findViewById(R.id.updated_field);
        cityField = rootView.findViewById(R.id.city_field);
        sunriseView = rootView.findViewById(R.id.sunrise_view);
        sunsetView = rootView.findViewById(R.id.sunset_view);
        windIcon = rootView.findViewById(R.id.wind_icon);
        huminityIcon = rootView.findViewById(R.id.humidity_icon);
        sunriseIcon = rootView.findViewById(R.id.sunrise_icon);
        sunsetIcon = rootView.findViewById(R.id.sunset_icon);
        weatherIcon = rootView.findViewById(R.id.weather_icon11);
        farenheit = rootView.findViewById(R.id.direction_view);
        horizontalRecyclerView = rootView.findViewById(R.id.horizontal_recycler_view);
        temp = rootView.findViewById(R.id.button1);
        cityPrefs = new CityPrefs(getContext());
        prefs = new Prefs(getContext());
        cc = new CheckConnection(getContext());
        int mode;
        if (prefs.isFirstTimeLaunch()) {
            mode = 1;
        } else {
            mode = 0;
        }
        if (mode == 0) {
            updateWeatherData(cityPrefs.getCity(), null, null);
        } else {
            updateWeatherData(null, Float.toString(prefs.getLatitude()), Float.toString(prefs.getLongitude()));
        }
        swipeView.setColorSchemeResources(R.color.red, R.color.green, R.color.blue, R.color.yellow, R.color.orange);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changeCity(cityPrefs.getCity());
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                    }
                }, 4000);
            }
        });
        cityField.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        updatedField.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        humidityView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        windView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        swipeView.setColorSchemeResources(R.color.red, R.color.green , R.color.blue , R.color.yellow , R.color.orange);
        dailyView.setText(getString(R.string.daily));
        dailyView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        sunriseView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        sunsetView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        horizontalRecyclerView.setBackgroundColor(getResources().getColor(R.color.yellow));
        return rootView;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (!cc.isNetworkAvailable())
                showNoInternet();
            else {
                updateWeatherData(cityPrefs.getCity(), null, null);
            }
        } else if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {

            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), Constants.PLAY_SERVICES_INSTALL,
                        Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            APICallService serviceDownloader = new APICallService();
            serviceDownloader.getWeatherDataFromCoord(weatherCallback, mLastLocation.getLatitude()+"", mLastLocation.getLongitude()+"");
            serviceDownloader.getForecastFromCoord(forecastCallback, mLastLocation.getLatitude()+"", mLastLocation.getLongitude()+"", 7 + "");

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    createLocationRequest();

                } else {

                    getActivity().finish();
                }
                return;
            }
        }
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(200000);
        mLocationRequest.setFastestInterval(50000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Connection request to open weather map API using retrofit.
    private void updateWeatherData(final String city, final String lat, final String lon) {
        //prepare connection request
        APICallService serviceDownloader = new APICallService();
        if (city != null) {
            serviceDownloader.getWeatherDataFromLocation(weatherCallback, city);
            serviceDownloader.getForecastFromLocation(forecastCallback, city, 7 + "");
        } else if ((lat != null) && (lon != null)) {
            serviceDownloader.getWeatherDataFromCoord(weatherCallback, lat, lon);
            serviceDownloader.getForecastFromCoord(forecastCallback, lat, lon, 7 + "");
        }

    }

    // Callbacks for weather Data API call.
    public Callback<WeatherData> weatherCallback = new Callback<WeatherData>() {
        @Override
        public void success(WeatherData weatherQuery, Response response) {
            renderWeather(weatherQuery);
            cityPrefs.setCity(weatherQuery.name);
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e("MY_APP", error.getLocalizedMessage());
            String message = error.getLocalizedMessage().toString().toLowerCase();
            if (message.contains("404")){
                Toast.makeText(getContext(), Constants.CITY_NOT_FOUND, Toast.LENGTH_LONG).show();
            } else if (message.contains(Constants.No_INTERNET)) {
                Toast.makeText(getContext(), Constants.CHECK_INTERNET, Toast.LENGTH_LONG).show();
            }
        }
    };

    // Menu items: Search and Current Location
    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_action_items, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.location :
                if (!cc.isNetworkAvailable()) {
                    showNoInternet();
                } else {
                    checkPermissions();
                }
                break;
            case R.id.search :
                if (!cc.isNetworkAvailable()) {
                    showNoInternet();
                } else {
                    showMenuInputDialog();
                }
                break;
        }
        return true;
    }

    // checks for google api services before accessing them.
    private boolean checkGooglePlayServices() {
        int checkGooglePlayServices = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());

        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices, getActivity(), REQUEST_CODE_RECOVER_PLAY_SERVICES).show();

            return false;
        }

        return true;

    }

    // Using Google API client to get the current user location
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    // Checks for dynamic permissions
    public void checkPermissions() {
        if (checkGooglePlayServices()) {
            buildGoogleApiClient();
            int permissionCheck = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // do nothing
                } else {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }

        }
    }

    // Callbacks for Forecast Data API call.
    public Callback<ForecastData> forecastCallback = new Callback<ForecastData>() {

        @Override
        public void success(ForecastData weatherQuery, Response response) {

            renderForecast(weatherQuery);
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e("MY_APP", error.getLocalizedMessage());
        }
    };

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void changeCity(String city) {
        if (!swipeView.isRefreshing())
        updateWeatherData(city, null, null);
        cityPrefs.setCity(city);
    }

    // When there is no internet enabled and user tries to access the weather info, this dialog box appears.
    public void showNoInternet() {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle(getString(R.string.no_internet_title))
                .setMessage(getString(R.string.no_internet_content))
                .setPositiveButton(getString(R.string.no_internet_mobile_data), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(Constants.SETTINGS_ACTIVITY, Constants.DATA_USAGE_ACTIVITY));
                        startActivityForResult(intent, 0);
                    }
                })
                .setNegativeButton(getString(R.string.no_internet_wifi), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                    }
                })
                .show();
    }

    // Setting the UI after the network call. Also snack bar is used to display information if the user is confused about the icons.
    private void renderWeather(final WeatherData weatherData) {
        cityField.setText(weatherData.name);
        sunriseView.setText(KToF(weatherData.main.temp_max)+ "°"  + " F");
        sunsetView.setText(KToF(weatherData.main.temp_min)+ "°"  + " F");
        humidityView.setText(weatherData.main.humidity + " %");
        windView.setText(weatherData.wind.speed + getString(R.string.metric));
        windIcon.setImageResource(R.drawable.wind);
        huminityIcon.setImageResource(R.drawable.humidity);
        sunsetIcon.setImageResource(R.drawable.min_temp);
        sunriseIcon.setImageResource(R.drawable.max_temp);
        weatherIcon.setImageResource(icons.getWeatherIcon(weatherData.weather.get(0).icon));
        android.view.ViewGroup.LayoutParams layoutParams = weatherIcon.getLayoutParams();
        layoutParams.width = 800;
        layoutParams.height = 650;
        weatherIcon.setLayoutParams(layoutParams);
        temp.setText(KToF(weatherData.main.temp )+"°" + "\n"+ "\n" + weatherData.weather.get(0).main );
        farenheit.setText( "F" );
            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = "Last update: " + df.format(new Date((weatherData.dt) * 1000));
            updatedField.setText(updatedOn);

        huminityIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.humidity) , weatherData.main.humidity) , Snackbar.LENGTH_SHORT).show();
                }
            });
        humidityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(rootView, String.format(Locale.ENGLISH, getString(R.string.humidity), weatherData.main.humidity) , Snackbar.LENGTH_SHORT).show();
            }
        });

        windIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.wind_speed) , weatherData.wind.speed, getString(R.string.metric))  , Snackbar.LENGTH_SHORT).show();
                }
        });
        windView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.wind_speed) , weatherData.wind.speed, getString(R.string.metric))  , Snackbar.LENGTH_SHORT).show();
                }
            });
        sunriseIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.max_temp) ,KToF(weatherData.main.temp_max)+ "°"  + " F" ) , Snackbar.LENGTH_SHORT).show();
                }
            });
        sunriseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(rootView, String.format(Locale.ENGLISH, getString(R.string.max_temp), KToF(weatherData.main.temp_max)+ "°"  + " F"), Snackbar.LENGTH_SHORT).show();
            }
        });
        sunsetIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.min_temp) ,KToF(weatherData.main.temp_min)+ "°"  + " F" ) , Snackbar.LENGTH_SHORT).show();
                }
        });
        sunsetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(rootView, String.format(Locale.ENGLISH, getString(R.string.min_temp), KToF(weatherData.main.temp_min)+ "°"  + " F"), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    // Rendering the forecast view after the network call to API.
    public void renderForecast(final ForecastData forecastData) {
        ArrayList<ForecastData.FList> list1 = new ArrayList<>();
        int i = 0;
        for (ForecastData.FList list : forecastData.list) {
            if (i % 8 == 0) {
                list1.add(list);
            }
            i++;
        }
        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(list1);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManager);
        horizontalRecyclerView.setAdapter(horizontalAdapter);

    }

    // Dialog Box that enables the input entry to enter the city for weather information. Could've used material design given more time.
    private void showMenuInputDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setTitle(getString(R.string.change_city))
                .setMessage(getString(R.string.enter_zip_code))
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        changeCity(input.getText().toString());
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();                    }
                })
                .setView(input)
                .show();
    }

    // Horizontal adapter for scrollable view of the forecast data
    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private List<ForecastData.FList> horizontalList;

        class MyViewHolder extends RecyclerView.ViewHolder {
            // Each view holds the icon and Max temperature of that particular day.
            ImageView weather_icon;
            TextView details_view;
            MyViewHolder(View view) {
                super(view);
                weather_icon = view.findViewById(R.id.weather_icon);
                details_view = view.findViewById(R.id.details_view);
            }
        }

        HorizontalAdapter(List<ForecastData.FList> horizontalList) {
            this.horizontalList = horizontalList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.forecast_list_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.weather_icon.setImageResource(icons.getWeatherIcon(horizontalList.get(position).weather.get(0).icon));
              long date1 = horizontalList.get(position).dt;
            Date expiry = new Date(date1 * 1000);
            String date = new SimpleDateFormat("EE, dd", Locale.US).format(expiry);
            SpannableString ss1 = new SpannableString(date + "\n"
                    + KToF((horizontalList.get(position).main.temp_max)) + "°" + "    " + "F" + "\n");
            ss1.setSpan(new RelativeSizeSpan(1.1f), 0, 7, 0); // set size
            ss1.setSpan(new RelativeSizeSpan(1.4f), 8, 12, 0);
            holder.details_view.setText(ss1);
            holder.details_view.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        }

        @Override
        public int getItemCount() {

            return horizontalList.size();
        }

    }

    // Method that converts temperature from Kelvin to Fahrenheit.
    public String KToF(double kelvinTemp) {
       double fTemp = (1.8 * ((kelvinTemp) - 273) + 32);
        return String.format("%.0f",fTemp);
    }
}



