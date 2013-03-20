package com.betatest.canalkidsbeta.adapter;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.betatest.canalkidsbeta.dao.SharedPreferencesDAO;
import com.betatest.canalkidsbeta.util.ConnectionStatus;
import com.betatest.canalkidsbeta.vo.Movie;
import com.loopj.android.image.SmartImageView;

public class MovieAdapter extends BaseAdapter {

	private Context context;
	private Activity activity;

	public MovieAdapter(List<Movie> movies, Context context, Activity activity) {
		this.activity = activity;
		this.movies = movies;
		this.context = context;
	}

	// check if there's no movie in the list
	public boolean checkListNull() {
		return ((movies == null) ? true : false);
	}

	@Override
	public int getCount() {

		return movies.size();
	}

	@Override
	public Object getItem(int position) {

		return movies.get(position);
	}

	private List<Movie> movies;

	@Override
	public long getItemId(int position) {

		return movies.get(position).title.hashCode();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		// TODO INTERNAL STORAGE DAO SHOULD BE CALLED FROM A CONTROLLER
		// internal storage will give us the movies bookmark hashmap
		InternalStorageDAO internalStorageDAO = new InternalStorageDAO();
		HashMap<String, String> bookmarkedHash = null;

		// shared preferences will give us if user already subscribed
		SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();

		// TODO THIS IS JUST FOR TEST
		// sharedPreferencesDAO.write(context, "subscription", "userNo");

		final Movie movie = movies.get(position);

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

		profileImage.setImageUrl("", R.drawable.epi_place_holder);
		profileImage.setImageUrl(movies.get(position).idImage);

		// TODO INSERT THE SMARTIMAGEVIEW MODIFIED BY SILVESTRE
		// if(!profileImage.setImageUrl(movies.get(position).idImage))
		// profileImage.setImageUrl("", R.drawable.ic_launcher);

		// create bookmark button
		Button bookmarkMovie = (Button) view
				.findViewById(R.id.adapterMovieBookmark);
		ImageView playMovie = (ImageView) view
				.findViewById(R.id.adapterMoviePlay);

		// Verify if user already bought the movies, and then, set the lock or
		// not in movies
		if (sharedPreferencesDAO.read(context, "subscription") == "userNo"
				|| movie.accountType.equals("free"))
			// TODO ALLOW MOVIES HERE
			playMovie.setBackgroundResource(R.drawable.icn_play);
		else
			// TODO DO NOT ALLOW MOVIES HERE
			playMovie.setBackgroundResource(R.drawable.icn_locked);

		// Verify if user bookmarked any movies
		bookmarkedHash = (HashMap<String, String>) internalStorageDAO
				.readObjectFromMemory(context, "bookmarked");

		// there's a bookmark in internal storage, does it contains the
		// movie tag?
		if (bookmarkedHash.containsKey(movie.tag)) {
			// if yes, then bookmark is active, and the movie is in external
			// storage
			bookmarkMovie
					.setBackgroundResource(R.drawable.button_download_active);
		} else {
			// nope, item was not bookmarked
			bookmarkMovie
					.setBackgroundResource(R.drawable.button_download_inactive);
		}

		// if bookmark is clicked, bookmark video
		bookmarkMovie.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();

				if (sharedPreferencesDAO.read(context, "subscription") == "userNo"
						|| movie.accountType.equals("free")) {

					InternalStorageDAO internalStorageDAO = new InternalStorageDAO();
					HashMap<String, String> bookmarkedHash = null;

					bookmarkedHash = (HashMap<String, String>) internalStorageDAO
							.readObjectFromMemory(context, "bookmarked");

					// it's already bookmarked, so unbookmark it
					// TODO TOAST CONFIRMATION
					// "DO YOU WANT TO REMOVE BOOKMARK?"
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

						Log.i("TAG", "UNBOOKMARKED IT");

					}
					// it's not bookmarked yet, bookmark it
					else {

						ConnectionStatus connectionStatus = new ConnectionStatus();
						if (connectionStatus.checkConnectivity(context)) {

							//TODO CORRECT ANIMATION BLINK
							Button bookmarksButton = (Button) activity
									.findViewById(R.id.bookmarks_button);
							BitmapDrawable background = (BitmapDrawable) bookmarksButton
									.getBackground();
							Drawable current = background.getCurrent();
							if (current instanceof AnimationDrawable) {
								AnimationDrawable bookmarkAnimation = (AnimationDrawable) bookmarksButton
										.getBackground();
								bookmarkAnimation.setVisible(true, true);
								bookmarkAnimation.start();
							}

//							Button bookmarksButton = (Button) activity
//									.findViewById(R.id.bookmarks_button);
//							AnimationDrawable bookmarkAnimation = (AnimationDrawable) bookmarksButton
//									.getBackground();
//							bookmarkAnimation.setVisible(true, true);
//							bookmarkAnimation.start();

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
							bookmarksController.saveMovieInBookmarks(context,
									movie);

							Log.i("TAG", "BOOKMARKED IT");
						} else {
							// TODO TOAST NEED TO BE CONNECTED
							Log.i("TAG", "NOT CONNECTED TO BOOKMARK");
						}
					}

				} else {
					// TODO SHOW WARNING
					Log.i("TAG", "NO SUBS SORRY!");
				}
			}
		});

		return view;
	}
}
