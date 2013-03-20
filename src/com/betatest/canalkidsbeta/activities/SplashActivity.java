package com.betatest.canalkidsbeta.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.bo.JsonLoaderBO;
import com.betatest.canalkidsbeta.bo.PojoLoaderBO;
import com.betatest.canalkidsbeta.controllers.AlarmController;
import com.betatest.canalkidsbeta.controllers.StorageController;
import com.betatest.canalkidsbeta.fragments.ViewPagerExample;
import com.betatest.canalkidsbeta.interfaces.AsyncTaskInterface;
import com.betatest.canalkidsbeta.util.ConnectionStatus;
import com.betatest.canalkidsbeta.vo.ChannelContentsResponseParcel;

public class SplashActivity extends Activity implements AsyncTaskInterface {

	private Context context;
	private String channelContentsUrl = "https://s3.amazonaws.com/nativeapps/channel_kids/videos/channelkids_ios.json";

	private static final String TAG = SplashActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		setContentView(R.layout.activity_splash);

		// Set alarm to load json from server
		Log.d(TAG, "Setting alarm");
		AlarmController alarmController = new AlarmController();
		alarmController.initAlarm(context);

		// Initializing storages
		StorageController storageController = new StorageController();
		storageController.initStorages(context);

		// Check connection status
		ConnectionStatus connectionStatus = new ConnectionStatus();

		if (connectionStatus.checkConnectivity(context)) {
			Log.d(TAG, "Connected to wifi/mobile");

			// Load json from server and save it in the internal storage
			Log.d(TAG, "Loading Json from server");
			JsonLoaderBO jsonLoaderTask = new JsonLoaderBO(context);
			jsonLoaderTask.execute(channelContentsUrl);
		}

		// Read from internal storage and create pojo with the information
		Log.d(TAG, "Creating Pojo");
		PojoLoaderBO pojoLoaderBO = new PojoLoaderBO(context);
		pojoLoaderBO.delegate = this;
		pojoLoaderBO.execute();
	}

	@Override
	public void processFinish(final ChannelContentsResponseParcel output) {

		// Intent intent = new Intent(context, ListActivity.class);
		// intent.putExtra("movies", output);
		// startActivity(intent);
		// finish();

		Intent intent = new Intent(context, ViewPagerExample.class);
		intent.putExtra("movies", output);
		startActivity(intent);
		finish();
	}

}
