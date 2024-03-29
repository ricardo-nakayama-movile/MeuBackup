package com.betatest.canalkidsbeta.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class ExternalStorageDAO {

	private boolean mExternalStorageAvailable = false;
	private boolean mExternalStorageWriteable = false;
	private final String CLASS_TAG = ExternalStorageDAO.class.getSimpleName();
	private final String VIDEO_FORMAT_MP4 = ".mp4";

	//TODO MAKE AVAILABLE AND WRITEABLE CHECK
	/**
	 * Constructor verifies if external storage is ready to read/write
	 */
	public ExternalStorageDAO() {
		super();
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
	}

	public Boolean deleteMp4FromExternalStorage(Context context, String fileName) {
		if (mExternalStorageWriteable) {
			fileName = fileName.concat(VIDEO_FORMAT_MP4);
			File file = new File(context.getExternalFilesDir(null), fileName);
			if (file != null) {
				file.delete();
				return true;
			}
		} else {
			Log.w(CLASS_TAG, "mExternalStorageWriteable = "
					+ mExternalStorageWriteable);
		}
		return false;
	}

	/**
	 * Save a movie MP4 to external storage given an url Return true in case of
	 * success
	 * 
	 * @param downloadUrl
	 * @param context
	 * @param fileName
	 * 
	 * @return Boolean
	 */
	// TODO IT CAN BE DONE IN A CLEANER WAY
	public Boolean writeMp4ToExternalStorageFromUrl(String downloadUrl,
			Context context, String fileName) {
		try {
			Log.v("TAG", "downloading data");

			fileName = fileName.concat(VIDEO_FORMAT_MP4);
			URL url = new URL(downloadUrl);
			URLConnection connection = url.openConnection();
			connection.connect();

			int lenghtOfFile = connection.getContentLength();

			Log.v("TAG", "lenghtOfFile = " + lenghtOfFile);

			InputStream is = url.openStream();

			File fileDirectory = new File(context.getExternalFilesDir(null),
					fileName);

			FileOutputStream fos = new FileOutputStream(fileDirectory);

			byte data[] = new byte[1024];

			int count = 0;
			long total = 0;
			int progress = 0;

			while ((count = is.read(data)) != -1) {
				total += count;
				int progress_temp = (int) (total * 100) / lenghtOfFile;
				if (progress_temp % 10 == 0 && progress != progress_temp) {
					progress = progress_temp;
					Log.v("TAG", "total = " + progress);
				}
				fos.write(data, 0, count);
			}

			is.close();
			fos.close();

			Log.v("TAG", "downloading finished");
			return true;
		} catch (Exception e) {
			Log.v("TAG", "exception in downloadData");
			e.printStackTrace();
		}
		return false;
	}
}