package com.betatest.canalkidsbeta.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.util.Log;

public class InternalStorageDAO {
	private static final String TAG = InternalStorageDAO.class.getSimpleName();

	public void write(Context context, String fileName, String value) {

		try {

			Log.d(TAG, "Writing Internal Storage");

			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(value.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "Error: " + e);
		} catch (IOException e) {
			Log.d(TAG, "Error: " + e);
		}

		Log.d(TAG, "Write internal storage done");

	}

	public String read(Context context, String fileName) {
		StringBuilder sb = new StringBuilder();

		try {

			Log.d(TAG, "Reading internal storage");

			FileInputStream in = context.openFileInput(fileName);
			InputStreamReader inputStreamReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}

		} catch (FileNotFoundException e) {
			Log.d(TAG, "Error: " + e);

		} catch (IOException e) {
			Log.d(TAG, "Error: " + e);

		}

		Log.d(TAG, "Read internal storage done");

		return sb.toString();

	}

	/**
	 * Write an object to internal memory
	 * 
	 * @param context
	 * @param filename
	 * 
	 * @param object
	 */

	public void writeObjectToMemory(Context context, String filename,
			Object object) {
		FileOutputStream fos;
		try {
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(object);
			os.close();
			Log.d(TAG, "Object successfully written: " + filename);
		} catch (FileNotFoundException e) {
			Log.d(TAG, "Object couldn't be opened: " + filename);
			Log.d(TAG, "Error: " + e);
		} catch (IOException e) {
			Log.d(TAG, "Object couldn't be opened: " + filename);
			Log.d(TAG, "Error: " + e);
		}

	}

	/**
	 * Read object from internal memory
	 * 
	 * @param context
	 * @param filename
	 * 
	 * @return
	 */

	public Object readObjectFromMemory(Context context, String filename) {
		Object defautObject = null;
		FileInputStream fis;
		try {
			fis = context.openFileInput(filename);
			ObjectInputStream is = new ObjectInputStream(fis);
			defautObject = is.readObject();
			is.close();
			Log.d(TAG, "Object successfully read: " + filename);
		} catch (FileNotFoundException e) {
			Log.d(TAG, "Object couldn't be opened: " + filename);
			Log.d(TAG, "Error: " + e);

		} catch (StreamCorruptedException e) {
			Log.d(TAG, "Object couldn't be opened: " + filename);
			Log.d(TAG, "Error: " + e);

		} catch (IOException e) {
			Log.d(TAG, "Object couldn't be opened: " + filename);
			Log.d(TAG, "Error: " + e);

		} catch (ClassNotFoundException e) {
			Log.d(TAG, "Object couldn't be opened: " + filename);
			Log.d(TAG, "Error: " + e);

		}

		return defautObject;

	}
}
