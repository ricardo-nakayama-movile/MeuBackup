/**
 * 
 */
package com.betatest.canalkidsbeta.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author marcosloiola
 * 
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {

	//TODO WHAT HAPPENS WHEN CONNECTIVITY CHANGES?
	private static final String CLASS_TAG = ConnectivityChangeReceiver.class
			.getSimpleName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i(CLASS_TAG, "Connectivity Change Event Received");

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo ni = cm.getActiveNetworkInfo();

		if (ni != null) {

			boolean isConnected = ni.isConnectedOrConnecting();

			Log.i(CLASS_TAG,
					"Connection: " + isConnected + " | " + ni.getTypeName());

			switch (ni.getType()) {
			case ConnectivityManager.TYPE_MOBILE:

				break;

			case ConnectivityManager.TYPE_WIFI:

				break;

			default:
				break;
			}

			if (isConnected) {

			//TODO DO SOMETHING HERE
				
			} else {

			}

		} else {

		}
	}

}
