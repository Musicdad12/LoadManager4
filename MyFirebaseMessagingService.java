package com.jrschugel.loadmanager;

/*
  Created by seanm on 8/5/2017.
  Copyright 2017. All rights reserved.
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // Session Manager Class
    SessionManager session;
    final static Integer mNotificationIdNewLoad = 123;
    public static final String LOAD_INFO_CHANNEL_ID = "2465";
    public static final String MESSAGE_CHANNEL_ID = "2466";
    public static final String LOAD_INFO_CHANNEL_NAME = "New Load Information";
    public static final String MESSAGE_CHANNEL_NAME = "Messages";
    private static final String TAG = "MyFirebaseMessagingServ";
    DatabaseHelper myDb;
    DatabaseHelperStops myDb2;
    String LoadNumber;
    NotificationManager mNotificationManager;
    Uri uriSound = null;
    NotificationUtils myNu;


    @Override
    public void onMessageReceived(RemoteMessage message) {

        Log.d(TAG, "onMessageReceived: " + message.toString());

        String title = Objects.requireNonNull(message.getNotification()).getTitle();
        String sound = message.getNotification().getSound();
        String text = message.getNotification().getBody();
        session = new SessionManager(this);
        myDb = new DatabaseHelper(this);
        myDb2 = new DatabaseHelperStops(this);
        myNu = new NotificationUtils(this);

        if (Objects.equals(title, "Load Information")) {

            String strLoadData = message.getData().toString();
            if (strLoadData != null) {
                try {
                    JSONObject jsonObj = new JSONObject(strLoadData);
                    // Getting JSON Array node
                    JSONArray details = jsonObj.getJSONArray("LoadInfo");
                    // looping through All Contacts
                    for (int i = 0; i < details.length(); i++) {
                        JSONObject c = details.getJSONObject(i);

                        LoadNumber = c.getString("LoadNumber");
                        String CustPO = c.getString("CustomerPO");
                        String Weight = c.getString("Weight");
                        String Pieces = c.getString("Pieces");
                        String BOLNo = c.getString("BLNumber");
                        String TrlrNo = c.getString("TrailerNumber");
                        String DrvLoad = c.getString("DriverLoad");
                        String DrvUnload = c.getString("DriverUnload");
                        String Loaded = c.getString("LoadedMiles");
                        String Empty = c.getString("EmptyMiles");
                        String TempLow = c.getString("TempLow");
                        String TempHigh = c.getString("TempHigh");
                        String Preload = c.getString("Preloaded");
                        String DropHook = c.getString("DropHook");
                        String Comments = c.getString("Comments");
                        myDb = new DatabaseHelper(this);
                        if (!myDb.searchLoad(Integer.parseInt(LoadNumber))) {
                            myDb.insertData(Integer.parseInt(LoadNumber),
                                    CustPO, Weight, Pieces, BOLNo, TrlrNo,
                                    DrvLoad, DrvUnload, Loaded, Empty, TempLow, TempHigh,
                                    Preload, DropHook, Comments, 2);

                        }
                    }
                    details = jsonObj.getJSONArray("StopInfo");
                    // looping through All Contacts
                    for (int i = 0; i < details.length(); i++) {
                        JSONObject c = details.getJSONObject(i);

                        LoadNumber = c.getString("LoadNumber");
                        String StopType = c.getString("StopType");
                        String CustID = c.getString("CustomerID");
                        String CustName = c.getString("CustomerName");
                        String CustAddr = c.getString("CustomerAddr1");
                        String CustAddr2 = c.getString("CustomerAddr2");
                        String CustCity = c.getString("CustomerCity");
                        String CustState = c.getString("CustomerState");
                        String CustPhone = c.getString("CustomerPhone");
                        String CustContact = c.getString("CustomerContact");
                        //String CustComments = c.getString("CustomerComments");
                        String EarlyDate = c.getString("StopEarlyDate");
                        String EarlyTime = c.getString("StopEarlyTime");
                        String LateDate = c.getString("StopLateDate");
                        String LateTime = c.getString("StopLateTime");

                        myDb2.AddStop(Integer.parseInt(LoadNumber), Integer.parseInt(StopType), Integer.parseInt(CustID), CustName, CustAddr, CustAddr2,
                                CustCity, CustState, CustPhone, CustContact, EarlyDate, EarlyTime, LateDate, LateTime);
                    }

                    this.sendNotification(new NotificationData(LOAD_INFO_CHANNEL_ID, title, text, sound));
                    ShortcutBadger.applyCount(this, myDb.NotificationLoadCount());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("GCM", "onMessageReceived: " + e.toString());
                }
            }
        } else if (Objects.equals(title, "Message")) {
            Log.d("GCM", "onMessageReceived: Text Message received");
            Calendar c = Calendar.getInstance();
            String strLoadData = message.getData().toString();
            if (strLoadData != null) {
                try {
                    JSONObject jsonObj = new JSONObject(strLoadData);
                    myDb.saveMessage(c.getTime().toString(), jsonObj.toString(), message.getNotification().getBody());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("GCM", "onMessageReceived: " + e.toString());
                }
            }
            this.sendNotification(new NotificationData(MESSAGE_CHANNEL_ID, title, text, sound));
        }
    }

     // Create and show a simple notification containing the received GCM message.
    public void sendNotification(NotificationData notificationData) {
        switch (notificationData.getId()) {
            case LOAD_INFO_CHANNEL_ID:

                Intent intent = new Intent(this, NavDrawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

                RemoteViews remoteViewsBig = new RemoteViews(this.getPackageName(), R.layout.custom_notification);
                remoteViewsBig.setTextViewText(R.id.tvTitle, notificationData.getTitle());
                remoteViewsBig.setTextViewText(R.id.tvShipper, myDb2.getShipper(LoadNumber));
                remoteViewsBig.setTextViewText(R.id.tvConsignee, myDb2.getConsignee(LoadNumber));

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, LOAD_INFO_CHANNEL_ID);
                builder.setSmallIcon(R.drawable.truck_icon_16);
                builder.setAutoCancel(true);
                builder.setContentIntent(contentIntent);
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                //builder.setCustomContentView(remoteViews);
                builder.setContent(remoteViewsBig);

                mNotificationManager = (NotificationManager) this
                        .getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationCompat.Builder nb = myNu.getCustomChannelNotification(remoteViewsBig, LOAD_INFO_CHANNEL_ID);
                    myNu.getManager().notify(Integer.parseInt(LOAD_INFO_CHANNEL_ID), nb.build());
                } else {
                    mNotificationManager.notify(mNotificationIdNewLoad, builder.build());
                    SharedPreferences getAlarms = PreferenceManager.
                            getDefaultSharedPreferences(getBaseContext());
                    if (getAlarms.getString("ringtonePref", null) != null) {
                        String alarms = getAlarms.getString("ringtonePref", null);
                        uriSound = Uri.parse(alarms);
                    } else {
                        uriSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.truck_horn);
                    }
                    Ringtone ring = RingtoneManager.getRingtone(getApplicationContext(), uriSound);
                    if (getAlarms.getBoolean("NewLoadSound", false)) {
                        ring.play();
                    }
                    // for vibrating phone
                    ((Vibrator) Objects.requireNonNull(getApplicationContext().getSystemService(
                            Context.VIBRATOR_SERVICE))).vibrate(800);
                    Integer NewLoads = myDb.NotificationLoadCount();
                    ShortcutBadger.applyCount(this, NewLoads); //for 1.1.4+
                }
            case MESSAGE_CHANNEL_ID:
                NotificationCompat.Builder nb = myNu.getChannelNotification(notificationData.getTitle(), notificationData.getTextMessage(), NotificationUtils.MESSAGE_CHANNEL_ID);
                myNu.getManager().notify(Integer.parseInt(MESSAGE_CHANNEL_ID), nb.build());
        }
    }
}

