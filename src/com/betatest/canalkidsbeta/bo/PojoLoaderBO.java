package com.betatest.canalkidsbeta.bo;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.betatest.canalkidsbeta.dao.InternalStorageDAO;
import com.betatest.canalkidsbeta.interfaces.AsyncTaskInterface;
import com.betatest.canalkidsbeta.util.JSONParser;
import com.betatest.canalkidsbeta.vo.ChannelContentsResponseParcel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class PojoLoaderBO extends
		AsyncTask<Void, Void, ChannelContentsResponseParcel> {

	private Context context;
	private ChannelContentsResponseParcel channelContentsResponseParcel;
	public AsyncTaskInterface delegate = null;
	private static final String TAG = PojoLoaderBO.class.getSimpleName();

	// Trick to send more parameters to an asyncTask
	public PojoLoaderBO(Context context) {
		this.context = context.getApplicationContext();
	}

	// TODO STILL GETTING FROM URL, WHAT HAPPENS IF THERE'S NO CONNECTION?
	@Override
	protected ChannelContentsResponseParcel doInBackground(Void... params) {

		String json;
		
		InternalStorageDAO internalStorageDAO = new InternalStorageDAO();
		json = internalStorageDAO.read(context, "movieJson");

		try {
			channelContentsResponseParcel = JSONParser
					.getChannelContentsPojoFromJson(json);
			Log.d(TAG, "Got contents from json and loaded pojo");
		} catch (JsonParseException e) {
			Log.d(TAG,"Error: " + e);
			return null;

		} catch (JsonMappingException e) {
			Log.d(TAG,"Error: " + e);
			return null;

		} catch (IOException e) {
			Log.d(TAG,"Error: " + e);
			return null;
		}

		return channelContentsResponseParcel;
	}

	@Override
	protected void onPostExecute(ChannelContentsResponseParcel result) {
		delegate.processFinish(result);
	}
}
