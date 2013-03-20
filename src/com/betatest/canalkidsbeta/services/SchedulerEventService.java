package com.betatest.canalkidsbeta.services;

import java.util.Date;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.betatest.canalkidsbeta.bo.JsonLoaderBO;
import com.betatest.canalkidsbeta.interfaces.AsyncTaskInterface;
import com.betatest.canalkidsbeta.vo.ChannelContentsResponseParcel;

public class SchedulerEventService extends IntentService implements AsyncTaskInterface{

	private static final String APP_TAG = "com.betatest.canalkidsbeta";
	private String channelContentsUrl = "https://s3.amazonaws.com/nativeapps/channel_kids/videos/channelkids_ios.json";
	
	/**
	 * A constructor is required, and must call the super IntentService(String)
	 * constructor with a name for the worker thread.
	 */
	public SchedulerEventService() {
		super("CanalKidsBetaVideoListUpdate");
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns,
	 * IntentService stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(APP_TAG, "SERVICE CALLED AT: " + new Date().toString());

		JsonLoaderBO jsonLoader = new JsonLoaderBO(this);
		jsonLoader.execute(channelContentsUrl);
	}

	@Override
	public void processFinish(ChannelContentsResponseParcel output) {
		// TODO Auto-generated method stub
		
	}

}