package com.tou4u.sentour.httpapi;

import android.os.AsyncTask;

import com.tou4u.sentour.BuildConfig;
import com.tou4u.sentour.util.DebugUtil;

import java.util.HashMap;

public class UnlikePostAPI {

    private static final String TAG = "UnlikePostAPI";

    private HashMap<String, String> header;

    private HttpRequester requester;

    public UnlikePostAPI() {
        requester = new HttpRequester();
        header = new HashMap<>();
//        header.put("Content-Type", "application/json");
    }

    public void postUnlike(String contentID, String sky, String hashtag, String unlike) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("contentID", contentID);
        params.put("sky", sky);
        params.put("HashTag", hashtag);
        params.put("Unlike", unlike);

        DebugUtil.logD(TAG, String.format("Hash Tag Encode Check : %s", hashtag));


        new AsyncTask<HashMap, Void, Void>() {

            @Override
            protected Void doInBackground(HashMap... params) {
                String url = BuildConfig.BACK_END_SERVER_BASE_URL + "Unlike.jsp";
                String result = requester.requestGetData(url, null, params[0]);
                if (result == null) {
                    DebugUtil.logD(TAG, "Result None");
                } else
                    DebugUtil.logD(TAG, String.format("Get Result : %s", result));
                return null;
            }
        }.execute(params);


    }

}
