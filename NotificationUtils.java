package com.jrschugel.loadmanager;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import static com.jrschugel.loadmanager.MainActivityFragment.messageListAdapter;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String LOAD_INFO_CHANNEL_ID = "2465";
    public static final String MESSAGE_CHANNEL_ID = "2466";
    public static final String LOAD_INFO_CHANNEL_NAME = "New Load Information";
    public static final String MESSAGE_CHANNEL_NAME = "Messages";
    private static final String TAG = "NotificationUtils";

    public NotificationUtils(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {
            // create Load Information channel
            NotificationChannel loadDataChannel = new NotificationChannel(LOAD_INFO_CHANNEL_ID,
                    LOAD_INFO_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            loadDataChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            loadDataChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            loadDataChannel.setLightColor(Color.YELLOW);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            loadDataChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            loadDataChannel.setShowBadge(true);
            getManager().createNotificationChannel(loadDataChannel);

            // create Messaging channel
            NotificationChannel messageChannel = new NotificationChannel(MESSAGE_CHANNEL_ID,
                    MESSAGE_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            messageChannel.enableLights(true);
            messageChannel.enableVibration(true);
            messageChannel.setLightColor(Color.GREEN);
            messageChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(messageChannel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification (String title, String message, String channelID) {
        switch (channelID) {
            case LOAD_INFO_CHANNEL_ID:
                return new NotificationCompat.Builder(getApplicationContext(), channelID)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSound(Uri.parse("android.resource://com.jrschugel.loadmanager/" + R.raw.truck_horn))
                        .setSmallIcon(R.drawable.truck_icon_16);
            case MESSAGE_CHANNEL_ID:
                return new NotificationCompat.Builder(getApplicationContext(), channelID)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSound(Uri.parse("android.resource://com.jrschugel.loadmanager/" + R.raw.new_message))
                        .setSmallIcon(R.drawable.ic_message);
        }
        return null;
    }

    public NotificationCompat.Builder getCustomChannelNotification (RemoteViews remoteViews, String channelID) {
                return new NotificationCompat.Builder(getApplicationContext(), channelID)
                        .setContent(remoteViews);

    }

    public static boolean isAppIsInBackground(Context context) {
        Log.e(TAG, "isAppIsInBackground: "+"show" );
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                        processInfo.processName.equals(context.getPackageName())) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static void updateTextMessages(Context context) {
        DatabaseHelper myDb = new DatabaseHelper(context);
        Cursor curMessages = myDb.getMessages();
        messageListAdapter = new MessageListAdapter(context, curMessages);
        MainActivityFragment.lstMessages.setAdapter(messageListAdapter);
    }
}