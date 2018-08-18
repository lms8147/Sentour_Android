package com.tou4u.sentour.httpapi;

import com.tou4u.sentour.BuildConfig;
import com.tou4u.sentour.util.DebugUtil;
import com.tou4u.sentour.data.GeoPosition;
import com.tou4u.sentour.data.Content;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContentAPI {

    private static final String TAG = "ContentAPI";

    private HashMap<String, String> header;

    private HttpRequester requester;

    public ContentAPI() {
        requester = new HttpRequester();
        header = new HashMap<>();
//        header.put("Content-Type", "application/json");
    }

    public ArrayList<Content> getContents(String latitude, String longitude, int radius, int contentType, String sky, String hashtag) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("radius", radius);
        params.put("contentType", contentType);
        params.put("sky", sky);
        params.put("HashTag", hashtag);
        params.put("lat", latitude);
        params.put("lng", longitude);

        DebugUtil.logD(TAG, String.format("Hash Tag Encode Check : %s", hashtag));



        String url = BuildConfig.BACK_END_SERVER_BASE_URL +  "Contents.jsp";
        String result = requester.requestGetData(url, null, params);
        if (result == null) {
            DebugUtil.logD(TAG, "Result None");
            return null;
        } else
            DebugUtil.logD(TAG, String.format("Get Result : %s", result));

        ArrayList<Content> contents = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(result);
            JSONObject response = root.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONArray items = body.getJSONArray("items");
            final int size = items.length();
            for (int i = 0; i < size; i++) {
                JSONObject content = items.getJSONObject(i);
                String contentID = content.getString("contentId");
                int dist = Integer.parseInt(content.getString("dist"));
                String title = content.getString("title");
                String imageUrl = null;
                if (content.has("imageURL")) {
                    imageUrl = content.getString("imageURL");
                }
                float unlike = Float.parseFloat(content.getString("unlike"));
                float unlikeSky = Float.parseFloat(content.getString("unlikeSky"));
                String lat = content.getString("lat");
                String lng = content.getString("lng");

                Content.Builder builder = new Content.Builder();
                builder.setContentID(contentID.toString());
                builder.setDist(dist);
                builder.setTitle(title);
                builder.setFirstImageUrl(imageUrl);
                builder.setUnlike(unlike);
                builder.setUnlikeSky(unlikeSky);
                builder.setGeoPosition(new GeoPosition(lat, lng));
                contents.add(builder.build());
            }

            return contents;
        } catch (JSONException e) {
            DebugUtil.logE(TAG, "Error On Make Json", e);
        }

        return null;
    }

}
