package com.example.bingchen.meet4sports;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bingchen.meet4sports.Util.WeatherController;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by User on 4/15/2017.
 */

public class ActivityWeather extends AppCompatActivity {

    private HashMap<String, String> weatherRecord = new HashMap<>();
    ImageView weatherIcon;
    TextView forecast_reading, temperature_reading, humidity_reading, wind_speed_reading, wind_direction_reading;
    TextView period, west_forecast, east_forecast, central_forecast, south_forecast, north_forecast;
    Button refreshBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // set content view
        setContentView(R.layout.activity_weather);

        // get all views
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        forecast_reading = (TextView) findViewById(R.id.forecast_reading);
        temperature_reading = (TextView) findViewById(R.id.temperature_reading);
        humidity_reading = (TextView) findViewById(R.id.humidity_reading);
        wind_speed_reading = (TextView) findViewById(R.id.wind_speed_reading);
        wind_direction_reading = (TextView) findViewById(R.id.wind_direction_reading);
        period = (TextView) findViewById(R.id.period);
        west_forecast = (TextView) findViewById(R.id.west_forecast);
        east_forecast = (TextView) findViewById(R.id.east_forecast);
        central_forecast = (TextView) findViewById(R.id.central_forecast);
        south_forecast = (TextView) findViewById(R.id.south_forecast);
        north_forecast = (TextView) findViewById(R.id.north_forecast);
        refreshBtn = (Button) findViewById(R.id.weather_btn);

        BottomNavigationViewHelper.removeNavigationShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        // set weather data
        try{
            getWeatherInfo();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // refresh button
        refreshBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try{
                    getWeatherInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // bottom Navigation Setting
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent0 = new Intent(ActivityWeather.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_message:
                        Intent intent1 = new Intent(ActivityWeather.this, ActivityMessage.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_search:
                        Intent intent2 = new Intent(ActivityWeather.this, ActivitySearch.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_weather:

                        break;

                    case R.id.ic_profile:
                        Intent intent4 = new Intent(ActivityWeather.this, ActivityProfile.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });
    }

    public void getWeatherInfo() throws JSONException, IOException{
        weatherRecord = WeatherController.getWeatherForecast();

        forecast_reading.setText(weatherRecord.get("forecast"));
        temperature_reading.setText(weatherRecord.get("temperature_low") + " ~ " + weatherRecord.get("temperature_high") + "\u2103");
        humidity_reading.setText(weatherRecord.get("humidity_low") + " ~ " + weatherRecord.get("humidity_high") + "%");
        wind_speed_reading.setText(weatherRecord.get("wind_speed_low") + " ~ " + weatherRecord.get("wind_speed_high") + " km/h");
        wind_direction_reading.setText(weatherRecord.get("wind_direction"));

        period.setText("From " + weatherRecord.get("start_time") + " to " + weatherRecord.get("end_time"));
        west_forecast.setText(weatherRecord.get("west"));
        east_forecast.setText(weatherRecord.get("east"));
        central_forecast.setText(weatherRecord.get("central"));
        south_forecast.setText(weatherRecord.get("south"));
        north_forecast.setText(weatherRecord.get("north"));

        String file_name = "weather_" + WeatherController.getCurrentForecast();
        int img = getResources().getIdentifier(file_name, "drawable", getPackageName());
        weatherIcon.setImageResource(img);
    }
}
