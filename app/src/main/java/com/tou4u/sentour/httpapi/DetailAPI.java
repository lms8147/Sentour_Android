package com.tou4u.sentour.httpapi;

import com.tou4u.sentour.BuildConfig;
import com.tou4u.sentour.util.DebugUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailAPI {

    private static final String TAG = "DetailAPI";

    private static final String serviceKey = BuildConfig.VISITKOREA_API_KEY;

    private HashMap<String, String> header;

    private HttpRequester requester;

    public DetailAPI() {
        requester = new HttpRequester();
        header = new HashMap<>();
        header.put("Content-Type", "application/json");
    }

    public String getDetail(String contentID) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("MobileOS", "AND");
        params.put("MobileApp", "SimT");
        params.put("ServiceKey", serviceKey);
        params.put("numOfRows", "1");
        params.put("pageNo", "1");

        params.put("contentId", contentID);

        params.put("firstImageYN", "Y");
        params.put("overviewYN", "Y");
        params.put("mapinfoYN", "Y");
        params.put("defaultYN", "Y");

        params.put("areacodeYN", "N");
        params.put("catcodeYN", "N");
        params.put("addrinfoYN", "N");

        params.put("_type", "json");

        String url = BuildConfig.VISITKOREA_API_URL;
        String result = requester.requestGetData(url, header, params);

        if (result == null) {
            DebugUtil.logD(TAG, "Result None");
            return null;
        } else
            DebugUtil.logD(TAG, String.format("Get Result : %s", result));

        try {
            JSONObject root = new JSONObject(result);
            JSONObject response = root.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject item = body.getJSONObject("items").getJSONObject("item");

            String detail = item.getString("overview");

            detail = detail.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
            detail = detail.replaceAll("\n", "");

            if (detail.length() > 200) {
                detail = detail.substring(0, 200) + "...";
            }

            return detail;
        } catch (JSONException e) {
            DebugUtil.logE(TAG, "Error On Make Json", e);
        } catch (NullPointerException e) {
            DebugUtil.logE(TAG, "Error On Control Data", e);
        }
        return null;
    }

}
