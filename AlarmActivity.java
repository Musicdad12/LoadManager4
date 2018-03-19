package com.jrschugel.loadmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmActivity extends Fragment {

    //the timepicker object
    TimePicker timePicker;
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;
    Intent mIntent;
    String setTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_alarm, container, false);

        //getting the timepicker object
        timePicker = rootView.findViewById(R.id.timePicker);

        //attaching clicklistener on button
        rootView.findViewById(R.id.buttonAlarmSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We need a calendar object to get the specified time in millis
                //as the alarm manager method takes time in millis to setup the alarm
                Calendar now = Calendar.getInstance();
                Calendar alarm = Calendar.getInstance();

                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    alarm.set(alarm.get(Calendar.YEAR), alarm.get(Calendar.MONTH), alarm.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(), timePicker.getMinute(), 0);
                    if (timePicker.getMinute() < 10) {
                        setTime = timePicker.getHour() + ":0" + timePicker.getMinute();
                    } else {
                        setTime = timePicker.getHour() + ":" + timePicker.getMinute();
                    }
                } else {
                    alarm.set(alarm.get(Calendar.YEAR), alarm.get(Calendar.MONTH), alarm.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                    if (timePicker.getCurrentMinute() < 10) {
                        setTime = timePicker.getCurrentHour() + ":0" + timePicker.getCurrentMinute();
                    } else {
                        setTime = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                    }
                }

                if (alarm.before(now)) alarm.add(Calendar.DAY_OF_MONTH, 1);
                setAlarm(alarm.getTimeInMillis());
            }
        });

        rootView.findViewById(R.id.buttonAlarmStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarm();
            }
        });

        return rootView;
    }

    private void setAlarm(long time) {
        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(time);

        int mDay = alarm.get(Calendar.DAY_OF_WEEK);
        int mHour = alarm.get(Calendar.HOUR_OF_DAY);
        int mMinute = alarm.get(Calendar.MINUTE);
        //getting the alarm manager
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        mIntent = new Intent(getActivity(), AlarmReceiver.class);

        //creating a pending intent using the intent
        mPendingIntent = PendingIntent.getBroadcast(getActivity(), 5696, mIntent, 0);

        //setting the repeating alarm that will be fired every day
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, time, mPendingIntent);
        //mAlarmManager.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, mPendingIntent);
        if (mMinute < 10) {
            Toast.makeText(getActivity(), "Alarm is set for " + mDay + ", " + mHour + ":0" + mMinute, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), "Alarm is set for " + mDay + ", " + mHour + ":" + mMinute, Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAlarm() {
        //getting the alarm manager
        //mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        //mIntent = new Intent(getActivity(), AlarmReceiver.class);

        //creating a pending intent using the intent
        //mPendingIntent = PendingIntent.getBroadcast(getActivity(), 5696, mIntent, 0);

        //stopping the repeating alarm that will be fired every day
        mAlarmManager.cancel(mPendingIntent);
        AlarmReceiver.stopAlarm();
        Toast.makeText(getActivity(), "Alarm is stopped", Toast.LENGTH_SHORT).show();
    }
}
