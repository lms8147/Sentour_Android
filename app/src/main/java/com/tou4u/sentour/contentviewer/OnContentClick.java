package com.tou4u.sentour.contentviewer;

import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.tou4u.sentour.contentviewer.main.MainDataSubject;
import com.tou4u.sentour.util.DebugUtil;
import com.tou4u.sentour.contentviewer.main.MainActivity;
import com.tou4u.sentour.data.Content;
import com.tou4u.sentour.data.GeoPosition;

public class OnContentClick implements View.OnClickListener {

    private static final String TAG = "OnContentClick";
    private ContentHolder mContentHolder;
    private MainActivity mActivity;

    public OnContentClick(MainActivity activity, ContentHolder contentHolder) {
        mContentHolder = contentHolder;
        mActivity = activity;
    }

    @Override
    public void onClick(View v) {
        if (mContentHolder.hasBindContent())
            new AlertDialog.Builder(mActivity)
                    .setTitle("해당 컨텐츠와 관련된 정보")
                    .setPositiveButton("지도", getOnMapAction())
                    .setNegativeButton("검색", getOnSearchAction()).show();
        else
            Toast.makeText(mActivity, "로딩된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
    }

    private Intent getMapIntent(GeoPosition position) {
        String uri = String.format("geo:%s,%s", position.getLat(), position.getLon());
        Uri gmmIntentUri = Uri.parse(uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent = intent.createChooser(intent, "지도");
        return intent;
    }

    private DialogInterface.OnClickListener getOnMapAction() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Creates an Intent that will load a click location
                Content content = mContentHolder.getBindContent();
                GeoPosition position = content.getGeoPosition();
                Intent intent = getMapIntent(position);
                mActivity.startActivity(intent);

                MainDataSubject mainData = mActivity.getMainData();

                DebugUtil.logD(TAG, String.format("On Content Click %s", String.format("%s, %s, %s", content.getContentID(), mainData.getSky(), mainData.getHashTag())));

                NotificationManager manager = (NotificationManager) mActivity.getSystemService(
                        android.content.Context.NOTIFICATION_SERVICE);

                ContentCheckedNotification notification = ContentCheckedNotification.getInstance();
                notification.notificate(mActivity,
                        manager, content.getTitle(), content.getContentID(), mainData.getSky(), mainData.getHashTag());

            }
        };
    }

    private Intent getSearchIntent() {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, mContentHolder.getBindContent().getTitle());
        intent = intent.createChooser(intent, "검색");
        return intent;
    }

    private DialogInterface.OnClickListener getOnSearchAction() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getSearchIntent();
                mActivity.startActivity(intent);

                NotificationManager manager = (NotificationManager) mActivity.getSystemService(
                        android.content.Context.NOTIFICATION_SERVICE);

                MainDataSubject mainData = mActivity.getMainData();

                Content content = mContentHolder.getBindContent();
                ContentCheckedNotification notification = ContentCheckedNotification.getInstance();
                notification.notificate(mActivity,
                        manager, content.getTitle(), content.getContentID(), mainData.getSky(), mainData.getHashTag());

            }
        };
    }
}
