package com.example.bingchen.meet4sports.Util;

import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class WeatherController {
    private static String currentForecast = null;
    private static HashMap<String, String> weatherRecord = new HashMap<>();

    public static HashMap<String, String> getWeatherForecast() throws JSONException, IOException{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        URL sgGov = new URL("https://api.data.gov.sg/v1/environment/24-hour-weather-forecast?" + formatter.format(now));
        URLConnection gov = sgGov.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        gov.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        JSONObject myResponse = new JSONObject(response.toString());
        JSONArray myArray;
        myArray = myResponse.getJSONArray("items");

        JSONObject items = new JSONObject(myArray.get(0).toString());
        JSONObject general = new JSONObject(items.get("general").toString());
        JSONObject closest_period = new JSONObject(items.getJSONArray("periods").get(0).toString());

        // current forecast
        JSONObject humidity = general.getJSONObject("relative_humidity");
        JSONObject temperature = general.getJSONObject("temperature");
        JSONObject wind = general.getJSONObject("wind");
        JSONObject wind_speed = wind.getJSONObject("speed");

        // recent 6 hour forecast
        JSONObject time = new JSONObject(closest_period.get("time").toString());
        JSONObject region = new JSONObject(closest_period.get("regions").toString());

        // feed data to hashmap
        weatherRecord.put("forecast", general.getString("forecast"));
        currentForecast = general.getString("forecast").replaceAll("[^A-Za-z]+", "").toLowerCase();

        weatherRecord.put("humidity_low", humidity.getString("low"));
        weatherRecord.put("humidity_high", humidity.getString("high"));

        weatherRecord.put("temperature_low", temperature.getString("low"));
        weatherRecord.put("temperature_high", temperature.getString("high"));

        weatherRecord.put("wind_direction", wind.getString("direction").toLowerCase());
        weatherRecord.put("wind_speed_low", wind_speed.getString("low"));
        weatherRecord.put("wind_speed_high", wind_speed.getString("high"));

        weatherRecord.put("start_time", time.getString("start").substring(11, 13) + ":00");
        weatherRecord.put("end_time", time.getString("end").substring(11, 13) + ":00");

        weatherRecord.put("west", region.getString("west"));
        weatherRecord.put("east", region.getString("east"));
        weatherRecord.put("central", region.getString("central"));
        weatherRecord.put("south", region.getString("south"));
        weatherRecord.put("north", region.getString("north"));

        return weatherRecord;
    }

    public static String getCurrentForecast(){
        return currentForecast;
    }
}
