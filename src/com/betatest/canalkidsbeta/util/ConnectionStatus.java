package com.betatest.canalkidsbeta.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionStatus {

	private static final String TAG = ConnectionStatus.class.getSimpleName();

	private ConnectivityManager connectivityManager;
	private NetworkInfo wifiInfo, mobileInfo;

	//TODO RE-CHECK THIS JAVADOC
	/**
	 * Check for <code>TYPE_WIFI</code> and <code>TYPE_MOBILE</code> connection
	 * using <code>isConnected()</code> Checks for generic Exceptions and writes
	 * them to logcat as <code>CheckConnectivity Exception</code>. Make sure
	 * AndroidManifest.xml has appropriate permissions.
	 * 
	 * @param con
	 *            Application context
	 * @return Boolean
	 */
	public Boolean checkConnectivity(Context context) {
		
		Log.d(TAG, "Checking connectivity");
		try {
			connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			wifiInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			mobileInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
				Log.d(TAG, "Wifi/mobile Connection found");
				return true;
			}
		} catch (Exception e) {
			//TODO CHANGE THIS LOG
			Log.w(TAG, "PROBLEM " + e);
		}
		Log.d(TAG, "No connection found - working offline");
		return false;
	}
}
