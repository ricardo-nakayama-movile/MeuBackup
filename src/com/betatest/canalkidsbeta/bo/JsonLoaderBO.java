package com.betatest.canalkidsbeta.bo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.betatest.canalkidsbeta.dao.InternalStorageDAO;
import com.betatest.canalkidsbeta.util.JSONParser;

public class JsonLoaderBO extends AsyncTask<String, Void, Void> {

	private Context context;
	private static final String TAG = JsonLoaderBO.class.getSimpleName();

	// Trick to send more parameters to an asyncTask
	public JsonLoaderBO(Context context) {
		this.context = context.getApplicationContext();
	}

	// TODO STILL GETTING FROM URL, WHAT HAPPENS IF THERE'S NO CONNECTION?
	@Override
	protected Void doInBackground(String... params) {

		Log.d(TAG, "Initializing json parser");
		String channelContentsUrl = params[0];
		String response = JSONParser.getUrlResponse(channelContentsUrl);

		// Save the json into internal storage
		InternalStorageDAO internalStorageDAO = new InternalStorageDAO();

		synchronized (this) {
			if (response != null)
				internalStorageDAO.write(context, "movieJson", response);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.d(TAG, "Json saved");
	}

}
