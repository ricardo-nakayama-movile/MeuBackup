package com.betatest.canalkidsbeta.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.controllers.BookmarksController;
import com.betatest.canalkidsbeta.dao.InternalStorageDAO;
import com.betatest.canalkidsbeta.util.ConnectionStatus;
import com.betatest.canalkidsbeta.vo.Movie;
import com.loopj.android.image.SmartImageView;

public class BookmarkAdapter extends BaseAdapter {

	private Context context;
	private List<Movie> bookmarkMovies;

	private static final String TAG = BookmarkAdapter.class.getSimpleName();
	
	public BookmarkAdapter(List<Movie> bookmarkMovies, Context context) {

		// internal storage will give us the movies bookmark hashmap
		InternalStorageDAO internalStorageDAO = new InternalStorageDAO();
		HashMap<String, String> bookmarkedHash = null;

		this.bookmarkMovies = new ArrayList<Movie>();

		// Verify if user bookmarked any movies
		bookmarkedHash = (HashMap<String, String>) internalStorageDAO
				.readObjectFromMemory(context, "bookmarked");

		// we need to check if the movie is in bookmarks
		Iterator<Movie> iterator = bookmarkMovies.iterator();
		while (iterator.hasNext()) {
			Movie movie = iterator.next();

			// if it is, we will add it to the bookmark movie list
			if (bookmarkedHash.containsKey(movie.tag))
				this.bookmarkMovies.add(movie);
		}

		// there's no bookmark, list will be empty
		if (this.bookmarkMovies.isEmpty()) {
			this.bookmarkMovies = null;
		}

		this.context = context;
	}

	// check if there's no movie in the list
	public boolean checkListNull() {
		return ((bookmarkMovies == null) ? true : false);
	}

	@Override
	public int getCount() {
		return bookmarkMovies.size();
	}

	@Override
	public Object getItem(int position) {

		return bookmarkMovies.get(position);
	}

	@Override
	public long getItemId(int position) {

		return bookmarkMovies.get(position).title.hashCode();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final Movie movie = bookmarkMovies.get(position);

		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.adapter_main_item_channel, null);
		}

		// set title in movie box
		TextView textViewTitle = (TextView) view
				.findViewById(R.id.adapterMovieTitle);
		textViewTitle.setText(movie.title);

		// set image in movie box
		SmartImageView profileImage = (SmartImageView) view
				.findViewById(R.id.adapterMoviePicture);
		profileImage.setImageUrl(bookmarkMovies.get(position).idImage);

		// create bookmark button
		Button bookmarkMovie = (Button) view
				.findViewById(R.id.adapterMovieBookmark);
		ImageView playMovie = (ImageView) view
				.findViewById(R.id.adapterMoviePlay);

		//TODO THIS IS NOT CORRECT, WHEN SUBSCRIPTION IS OVER, ALL VIDEOS BECOME LOCKED AGAIN
		// User already bought all so it will all be play icon
		playMovie.setBackgroundResource(R.drawable.icn_play);

		// bookmarks are active, after all, we are in a bookmarked page
		bookmarkMovie.setBackgroundResource(R.drawable.button_download_active);

		bookmarkMovie.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InternalStorageDAO internalStorageDAO = new InternalStorageDAO();
				HashMap<String, String> bookmarkedHash = null;

				bookmarkedHash = (HashMap<String, String>) internalStorageDAO
						.readObjectFromMemory(context, "bookmarked");

				// it's already bookmarked, so unbookmark it
				// TODO TOAST CONFIRMATION "DO YOU WANT TO REMOVE BOOKMARK?"
				if (bookmarkedHash.containsKey(movie.tag)) {

					// remove from bookmark hashmap/internal storage
					bookmarkedHash.remove(movie.tag);
					internalStorageDAO.writeObjectToMemory(context,
							"bookmarked", bookmarkedHash);

					// set image to inactive
					v.findViewById(R.id.adapterMovieBookmark)
							.setBackgroundResource(
									R.drawable.button_download_inactive);

					// remove from external storage
					BookmarksController bookmarksController = new BookmarksController();
					bookmarksController.removeMovieFromBookmarks(context,
							movie.tag);

					Log.d(TAG, "UNBOOKMARKED IT");

				}
				// it's not bookmarked yet, bookmark it
				else {

					ConnectionStatus connectionStatus = new ConnectionStatus();
					if (connectionStatus.checkConnectivity(context)) {

						// put bookmark in hashmap/internal storage
						bookmarkedHash.put(movie.tag, movie.title);
						internalStorageDAO.writeObjectToMemory(context,
								"bookmarked", bookmarkedHash);

						// set image to active
						v.findViewById(R.id.adapterMovieBookmark)
								.setBackgroundResource(
										R.drawable.button_download_active);

						// download movie to external storage
						BookmarksController bookmarksController = new BookmarksController();
						bookmarksController
								.saveMovieInBookmarks(context, movie);

						Log.d(TAG, "BOOKMARKED IT");
					} else {
						// TODO TOAST NEED TO BE CONNECTED
						Log.d(TAG, "NOT CONNECTED TO BOOKMARK");
					}
				}

			}
		});

		return view;
	}
}
