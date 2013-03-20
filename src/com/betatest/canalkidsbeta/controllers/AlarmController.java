package com.betatest.canalkidsbeta.controllers;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.betatest.canalkidsbeta.receivers.SchedulerEventReceiver;

public class AlarmController {

	private static final String TAG = AlarmController.class.getSimpleName();

	// 4 hours alarm (hardcoded to support below api 9)
	// private static final int EXEC_INTERVAL = 4 * 60 * 60 * 1000;
	private static final int EXEC_INTERVAL = 60 * 1000;

	public Void initAlarm(Context context) {
		
		Log.d(TAG, "Initializing alarm");
		// Calling alarm to get json from server every 4 hours
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intentReceiver = new Intent(context,
				SchedulerEventReceiver.class);
		PendingIntent intentExecuted = PendingIntent.getBroadcast(context, 0,
				intentReceiver, PendingIntent.FLAG_NO_CREATE);

		if (intentExecuted == null) {
			intentExecuted = PendingIntent.getBroadcast(context, 0,
					intentReceiver, PendingIntent.FLAG_CANCEL_CURRENT);
			Calendar now = Calendar.getInstance();
			now.add(Calendar.MINUTE, 15);
			// now.add(Calendar.MINUTE, 1);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					now.getTimeInMillis(), EXEC_INTERVAL, intentExecuted);
			Log.d(TAG, "Alarm set every 4 hours");
		} else {
			Log.d(TAG, "Alarm already set");
		}

		return null;
	}

}
