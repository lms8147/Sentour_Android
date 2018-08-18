package com.tou4u.sentour.contentviewer;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.tou4u.sentour.R;
import com.tou4u.sentour.unlikeviewer.main.UnlikeActivity;

import java.util.HashSet;

public class ContentCheckedNotification {

    private HashSet<Integer> notifyIDs;

    private static class Singleton {
        private static final ContentCheckedNotification instance = new ContentCheckedNotification();
    }

    public static ContentCheckedNotification getInstance() {
        return Singleton.instance;
    }

    private ContentCheckedNotification() {
        notifyIDs = new HashSet<>();
    }

    private int makeNotifyID() {
        int id;
        while (true) {
            id = (int) (Math.random() * 10000);
            if (!notifyIDs.contains(id)) {
                notifyIDs.add(id);
                break;
            }
        }
        return id;
    }

    private Notification getNotification(Context context, int notifyID, String contentTitle, String contentID, String sky, String hashTag) {

        Intent notiIntent = new Intent(context, UnlikeActivity.class);
        notiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notiIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notiIntent.putExtra(UnlikeActivity.EXTRA_CONTENT_ID, contentID);
        notiIntent.putExtra(UnlikeActivity.EXTRA_SKY, sky);
        notiIntent.putExtra(UnlikeActivity.EXTRA_HASH_TAG, hashTag);
        notiIntent.putExtra(UnlikeActivity.EXTRA_ID, notifyID);

        PendingIntent content = PendingIntent.getActivity(
                context, notifyID, notiIntent, PendingIntent.FLAG_ONE_SHOT);

        return new Notification.Builder(context)
                .setTicker("여행지를 확인하셨습니다.")
                .setContentTitle(contentTitle)
                .setContentText("해당 여행지가 별로였다면 눌러주세요.")
                .setSmallIcon(R.drawable.ic_unlike)
                .setContentIntent(content)
                .build();
    }

    public void notificate(Context context, NotificationManager manager, String contentTitle, String contentID, String sky, String hashTag) {
        int notifyID = makeNotifyID();
        Notification notification = getNotification(context, notifyID, contentTitle, contentID, sky, hashTag);
        manager.notify(notifyID, notification);
    }

}
