package org.ekokonnect.reprohealth;

import models.Message;
import models.MessageDataSource;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
	private static final String TAG = "REPRO";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            	Log.e(TAG, "Unhandled Method");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendNotification("Deleted messages on server: " +
//                        extras.toString());
            	Log.e(TAG, "Unhandled Method");
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

            	//Push To SQLite
            	String news = (String) extras.get("data");
            	Message n = null;
            	try {
            		JSONObject msg = new JSONObject(news);
					n =  new Message(msg.getJSONObject("message").getLong("mid"),
							msg.getJSONObject("message").getJSONObject("msg").getString("text"),
							msg.getJSONObject("message").getJSONObject("msg").getBoolean("isMine"),
							msg.getJSONObject("message").getJSONObject("msg").getLong("date"));
					n.statusOK = true;
					MessageDataSource db = new MessageDataSource(getApplicationContext());
					db.open();
					db.insertIntoTable(n);
					db.close();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	Log.d(TAG, "News: " + news);
            	

                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification(n);
//                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Message n) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        
        long[] vib = {250,250,250,250}; 

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ChatActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_stat_app)
        .setContentTitle("ReproHealth Chat")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(n.getMessage()))
        .setContentText(n.getMessage())
        .setAutoCancel(true)
        .setVibrate(vib)
        .setLights(0xFFFF0000, 1000, 750);
        
        mBuilder.setLights(0xff00ff00, 300, 1000);
//        mBuilder.setSound(Notification.DEFAULT_SOUND);
        
        //mBuilder.

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}