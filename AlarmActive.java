package com.jrschugel.loadmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import java.util.Calendar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;

import com.jrschugel.loadmanager.model.Alarm;
import com.jrschugel.loadmanager.model.AlarmMsg;
import com.jrschugel.loadmanager.model.AlarmTime;
import com.jrschugel.loadmanager.model.DbHelper;

import static com.jrschugel.loadmanager.RemindMe.db;

/**
 * Created by seanm on 7/20/2017.
 */

public class AlarmActive extends Activity  {
    protected static final String LOG_TAG = "MediaPlayerDemo";

    MediaPlayer mp = new MediaPlayer();
    MediaPlayer player;
    int volume_level = 10, volume_incr = 10;
    boolean done;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        playAlarm();
    }

    void playAlarm() {
        AlertDialog dlg = new AlertDialog.Builder(this).create();
        this.getIntent();
        //long alarmMsgId = getIntent().getLongExtra("alarmMsgId", -1);
        final long alarmId = getIntent().getLongExtra("alarmId", -1);
        dlg.setTitle("Wake up time!");
        dlg.setButton(AlertDialog.BUTTON_POSITIVE, "OFF", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                player.setOnCompletionListener(null);
                player.stop();
                player.release();
                done = true;
                finish();

            }
        });

        dlg.setButton(AlertDialog.BUTTON_NEGATIVE, "SNOOZE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent alarm = new Intent(AlarmActive.this, AlarmReceiver.class);

                PendingIntent alarmIntent = PendingIntent.getBroadcast(AlarmActive.this, 0, alarm, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + 60000,
                        600000, alarmIntent);

                player.setOnCompletionListener(null);
                player.stop();
                player.release();
                done = true;
                finish();

            }
        });

        dlg.show();
        player = MediaPlayer.create(this, Uri.parse(RemindMe.getRingtone()));
        player.setVolume(volume_level, volume_level);
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                volume_level += volume_incr;
                player.setVolume(volume_level, volume_level);
    /*try {
     player.prepare();
    } catch (IOException e) {
     // Should be a CANTHAPPEN since was previously prepared!
     Log.i(LOG_TAG, "Unexpected IOException " + e);
    }*/
                player.start();
            }
        });
    }
}
