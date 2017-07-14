package com.hvacci.vaccination.fcmservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hvacci.vaccination.MainActivity;
import com.hvacci.vaccination.notification.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Harsh on 08-04-2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        System.out.println("data:"+remoteMessage.getData());
        if(remoteMessage.getData().size()>0){
            handledata(remoteMessage.getData());
        }
    }

    private void handledata(Map<String,String> map){

        String message = map.get("message");
        String title = "Upcoming Vaccination...";
        String timestamp = "2017-06-11 12:00:00";

        handlenotification(message,title,timestamp);

    }
    private void handlenotification(String message,String title,String timestamp){

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.putExtra("message", message);

        showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
    }
    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
}
