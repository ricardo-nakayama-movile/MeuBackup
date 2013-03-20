/**
 * 
 */
package com.betatest.canalkidsbeta.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author marcosloiola
 * 
 */
public class SharedPreferencesDAO {

	public static final String PREFS_NAME = "CanalKidsBetaPrefFile";

	private static final String APP_TAG = "com.betatest.canalkidsbeta";

	public void write(Context context, String key, String value) {

		Log.i(APP_TAG, "Writing Shared Preferences");

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);

		// Commit the edits!
		editor.commit();

		Log.i(APP_TAG, "OK");

	}

	public String read(Context context, String key) {

		String value = "";

		Log.i(APP_TAG, "Looking for: " + key);

		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		value = settings.getString(key, null);

		Log.i(APP_TAG, "Found: " + value);

		return value;
	}
	
	public void delete(Context context, String key) {

		Log.i(APP_TAG, "Removing Shared Preferences");
		
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);

		// Commit the edits!
		editor.commit();
		
		Log.i(APP_TAG, "OK");

	}

}
