package com.jrschugel.loadmanager;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Belal on 8/29/2017.
 */

//class extending the Broadcast Receiver
public class AlarmReceiver extends BroadcastReceiver {

    public static Ringtone mRingtone;

    //the method will be fired when the alarm is triggerred
    @Override
    public void onReceive(Context context, Intent intent) {

        //you can check the log that it is fired
        //Here we are actually not doing anything
        //but you can do any task here that you want to be done at a specific time everyday
        Log.d("MyAlarmBelal", "Alarm just fired");
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mRingtone = RingtoneManager.getRingtone(context, notification);
        mRingtone.play();
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent mIntent = new Intent(context, AlarmReceiver.class);

        //creating a pending intent using the intent
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, 5696, mIntent, 0);

        //setting the repeating alarm that will be fired every day
        mAlarmManager.cancel(mPendingIntent);
    }

    public static void stopAlarm() {
        if (mRingtone != null) {
            mRingtone.stop();
        }
    }

}