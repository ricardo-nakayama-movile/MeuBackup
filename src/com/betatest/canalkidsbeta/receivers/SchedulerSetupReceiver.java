package com.betatest.canalkidsbeta.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.betatest.canalkidsbeta.activities.SplashActivity;
import com.betatest.canalkidsbeta.controllers.AlarmController;

public class SchedulerSetupReceiver extends BroadcastReceiver {

	private static final String TAG = SplashActivity.class.getSimpleName();

	@Override
	public void onReceive(final Context context, final Intent intent) {

		Log.d(TAG, "Broadcast from booting received, setting alarm");

		AlarmController alarmController = new AlarmController();
		alarmController.initAlarm(context);
	}

}
