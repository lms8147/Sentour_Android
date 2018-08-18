package com.tou4u.sentour.httpapi;

import com.tou4u.sentour.BuildConfig;
import com.tou4u.sentour.util.DebugUtil;
import com.tou4u.sentour.data.GeoPosition;
import com.tou4u.sentour.data.WeatherData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WeatherAPI {

    private static final String TAG = "WeatherAPI";

    private static final String appKey = BuildConfig.SK_DEV_API_KEY;

    private HashMap<String, String> header;

    private HttpRequester requester;

    public WeatherAPI() {
        requester = new HttpRequester();
        header = new HashMap<>();
        header.put("appKey", appKey);
        header.put("Content-Type", "application/json");
    }

    public WeatherData getSkyData(GeoPosition geoPosition) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("version", 1);
        params.put("lat", geoPosition.getLat());
        params.put("lon", geoPosition.getLon());

        String url = BuildConfig.SK_DEV_API_URL;
        String result = requester.requestGetData(url, header, params);

        if (result == null) {
            DebugUtil.logD(TAG, "Result None");
            return null;
        } else
            DebugUtil.logD(TAG, String.format("Get Result : %s", result));

        try {
            JSONObject root = new JSONObject(result);
            JSONObject weather = root.getJSONObject("weather");
            JSONObject hourly = weather.getJSONArray("hourly").getJSONObject(0);
            JSONObject sky = hourly.getJSONObject("sky");
            String name = sky.getString("name");
            String code = sky.getString("code");
            JSONObject temperature = hourly.getJSONObject("temperature");
            String tc = temperature.getString("tc");

            WeatherData weatherData = new WeatherData(name, code, tc);
            return weatherData;
        } catch (JSONException e) {
            DebugUtil.logE(TAG, "Error On Get SkyData JSONException", e);
        } catch (NullPointerException e) {
            DebugUtil.logE(TAG, "Error On Get SkyData NullPointerException", e);
        }

        return null;
    }


}
