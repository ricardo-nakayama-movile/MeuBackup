package com.betatest.canalkidsbeta.controllers;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;

import com.betatest.canalkidsbeta.bo.BookmarksBO;
import com.betatest.canalkidsbeta.dao.ExternalStorageDAO;
import com.betatest.canalkidsbeta.dao.InternalStorageDAO;
import com.betatest.canalkidsbeta.util.ConnectionStatus;
import com.betatest.canalkidsbeta.vo.Movie;

public class BookmarksController {

	public Void saveMovieInBookmarks(Context context, Movie movie) {
		ConnectionStatus connectionStatus = new ConnectionStatus();

		// TODO TOAST NEED TO BE CONNECTED
		if (connectionStatus.checkConnectivity(context)) {
			BookmarksBO bookmarksBO = new BookmarksBO(context);
			bookmarksBO.execute(movie);
		}
		return null;
	}

	public Boolean removeMovieFromBookmarks(Context context, String movieName) {
		ExternalStorageDAO externalStorageDAO = new ExternalStorageDAO();
		externalStorageDAO.deleteMp4FromExternalStorage(context, movieName);
		return true;
	}

	public Boolean removeAllMoviesFromBookmarks(Context context) {
		ExternalStorageDAO externalStorageDAO = new ExternalStorageDAO();
		InternalStorageDAO internalStorageDAO = new InternalStorageDAO();
		HashMap<String, String> bookmarkedHash = (HashMap<String, String>) internalStorageDAO
				.readObjectFromMemory(context, "bookmarked");
		
		//TODO PROBLEM! NOT REMOVING CHECKED STAR (WHY?) IS IT REMOVING FROM INTERNAL? WHERES THE INTERNAL STORAGE?
		// avoids a ConcurrentModificationException
		Iterator<HashMap.Entry<String, String>> it = bookmarkedHash.entrySet().iterator();
		while (it.hasNext()) {
		   HashMap.Entry<String, String> entry = it.next(); 
		   if (externalStorageDAO.deleteMp4FromExternalStorage(context,
					entry.getKey())) {
			   it.remove();
		   }
		}

		internalStorageDAO.writeObjectToMemory(context, "bookmarked",
				bookmarkedHash);

		return true;
	}
}
