package com.fredroid.parceltracking.service;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fredroid.parceltracking.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by jackttc on 26/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getName();
    private static final int TYPE_NOTIFICATION_NOTIFICATION = 1;
    private static final int TYPE_NOTIFICATION_CHAT = 2;

    private NotificationHelper noti;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        noti = new NotificationHelper(this);


        Log.d(MyFirebaseMessagingService.class.getName(), "From: " + remoteMessage.getFrom());

        Log.d(MyFirebaseMessagingService.class.getName(), "From: " + remoteMessage.getNotification().getBody());
        //  noti.getNotification1(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());


        if (remoteMessage.getNotification().getBody().equals("notification"))
        {

            sendNotification(TYPE_NOTIFICATION_NOTIFICATION, getApplication().getString(R.string.app_name),remoteMessage.getNotification().getBody());
            //noti.getNotification1("notification",remoteMessage.getNotification().getBody());
        }
        else
        {
            sendNotification(TYPE_NOTIFICATION_CHAT,getApplication().getString(R.string.app_name),remoteMessage.getNotification().getBody());


            // noti.getNotification1("chat",remoteMessage.getNotification().getBody());
        }

    }

    /* TODO: comment super.handleIntent and handle if action from notification is to be cleared when entering the app or when multiple user account switching is required and supported on the server
    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
    }
    */


    public void sendNotification(int id, String title, String body) {
        NotificationCompat.Builder nb = null;
        nb = noti.getNotification1(title,body);
        if (nb != null) {
            noti.notify(id, nb);
        }
    }

}
