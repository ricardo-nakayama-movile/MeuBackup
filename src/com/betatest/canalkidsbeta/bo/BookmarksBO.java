package com.betatest.canalkidsbeta.bo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.betatest.canalkidsbeta.dao.ExternalStorageDAO;
import com.betatest.canalkidsbeta.interfaces.AsyncTaskInterface;
import com.betatest.canalkidsbeta.vo.Movie;

public class BookmarksBO extends
		AsyncTask<Movie, Void, Boolean> {

	private Context context;
	public AsyncTaskInterface delegate = null;

	public BookmarksBO(Context context) {
		this.context = context.getApplicationContext();
	}

	// TODO STILL GETTING FROM URL, WHAT HAPPENS IF THERE'S NO CONNECTION?
	@Override
	protected Boolean doInBackground(Movie... params) {
	
		String fileDownloadUrl = params[0].urlDownload;
		String fileName = params[0].tag;
		
		ExternalStorageDAO externalStorageDAO = new ExternalStorageDAO();
				
		if (externalStorageDAO.writeMp4ToExternalStorageFromUrl(fileDownloadUrl,
				context, fileName)) {
			Log.i("INFO", "DOWNLOAD VIDEO OK");
			return true;
		} else {
			Log.w("WARN", "DOWNLOAD VIDEO ERROR");
			return false;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
	}
}
