package com.jrschugel.loadmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;

import com.jrschugel.loadmanager.model.Alarm;
import com.jrschugel.loadmanager.model.AlarmMsg;


/**
 * @author appsrox.com
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

//	private static final String TAG = "AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		long alarmMsgId = intent.getLongExtra(AlarmMsg.COL_ID, -1);
		long alarmId = intent.getLongExtra(AlarmMsg.COL_ALARMID, -1);

		AlarmMsg alarmMsg = new AlarmMsg(alarmMsgId);
		alarmMsg.setStatus(AlarmMsg.EXPIRED);
		alarmMsg.persist(RemindMe.db);
		
		Alarm alarm = new Alarm(alarmId);
		alarm.load(RemindMe.db);

		Intent i = new Intent(context, AlarmActive.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(i);

		/*
		Intent notificationIntent = new Intent(context, AlarmActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context,
				2024, notificationIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = context.getResources();

		SuperDialog.createDialog(1, context);
		*/
/*
        Intent Pintent = new Intent(context, AlarmActive.class);
        Pintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Pintent.putExtra("Message", alarm.getName());
        context.startActivity(Pintent);
*/
/*
		Notification.Builder builder = new Notification.Builder(context);

		builder.setContentIntent(contentIntent)
				.setSmallIcon(R.drawable.truck_icon_16)
				.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.truck_icon_16))
				.setTicker("Alarm")
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(false)
				.setContentTitle("Alarm Expired")
				.setContentText("Your alarm has expired.");
		if (RemindMe.isVibrate()) {
			long[] v = {500, 10000};
			builder.setVibrate(v);
		}
		if (alarm.getSound()) {
			builder.setSound(Uri.parse(RemindMe.getRingtone()));
//			n.defaults |= Notification.DEFAULT_SOUND;
		}
		Notification n = builder.build();

		nm.notify(2024, n);
		*/
	}


}
