package com.betatest.canalkidsbeta.controllers;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.betatest.canalkidsbeta.dao.InternalStorageDAO;
import com.betatest.canalkidsbeta.dao.SharedPreferencesDAO;

public class StorageController {

	private static final String TAG = StorageController.class.getSimpleName();

	
	public Void initStorages(Context context) {
		
		Log.d(TAG, "Initializing storages");
		SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();
		InternalStorageDAO internalStorageDAO = new InternalStorageDAO();
		
		
		Log.d(TAG, "Initializing bookmark list file");
		// Initializing bookmarked list file. Create it it doesn't exists
		HashMap<String, String> bookmarkedHash = (HashMap<String, String>) internalStorageDAO
				.readObjectFromMemory(context, "bookmarked");

		if (bookmarkedHash == null) {
			Log.d(TAG, "Bookmark list not present - Creating 'bookmarked'");
			bookmarkedHash = new HashMap<String, String>();
			internalStorageDAO.writeObjectToMemory(context, "bookmarked",
					bookmarkedHash);
		}

		// Initializing subscription flag. Setting as no subs.
		Log.d(TAG, "Initializing subscription");
		sharedPreferencesDAO.write(context, "subscription", "none");

		return null;

	}
}
