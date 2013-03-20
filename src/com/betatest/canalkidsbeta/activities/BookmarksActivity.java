package com.betatest.canalkidsbeta.activities;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.VideoView;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.adapter.BookmarkAdapter;
import com.betatest.canalkidsbeta.controllers.VideoController;
import com.betatest.canalkidsbeta.dao.SharedPreferencesDAO;
import com.betatest.canalkidsbeta.fragments.NotificationBookmarkFragment;
import com.betatest.canalkidsbeta.vo.ChannelContentsResponseParcel;
import com.betatest.canalkidsbeta.vo.Movie;

public class BookmarksActivity extends FragmentActivity {

	private Context context;
	private Button backButton;
	private ListView listComplex;
	private ChannelContentsResponseParcel channelContentsResponseParcel;
	private List<Movie> movies;
	private VideoView videoView;

	private static final String TAG = BookmarksActivity.class.getSimpleName();
	
	// TODO THE SHADOW OF THE MAIN BAR IS NOT TRANSPARENT FIX IT!
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bookmarks);

		context = this;

		// create bookmarks list
		listComplex = (ListView) findViewById(R.id.list_bookmarks);

		// get movies from listActivity
		channelContentsResponseParcel = getIntent().getExtras().getParcelable(
				"movies");
		movies = Movie.getMoviesList(channelContentsResponseParcel);

		// adapter will get only the movies that are bookmarked
		BookmarkAdapter adapter = new BookmarkAdapter(movies, this);

		// if theres none bookmarked movie, the adapter will have a null movie
		// list
		if (!adapter.checkListNull()) {

			// set adapter to the list
			listComplex.setAdapter(adapter);

			// if clicked, play the movie (you can only bookmark playable
			// movies)
			listComplex.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();
					Movie movie = movies.get(arg2);

					if (sharedPreferencesDAO.read(context, "subscription") == "userNo"
							|| movie.accountType.equals("free")) {
						VideoController videoController = new VideoController();

						setContentView(R.layout.videolayout);
						videoView = (VideoView) findViewById(R.id.playvideo);
						videoController.loadVideo(context, movie.tag, movie.urlMovie, videoView);
					} else {
						// TODO CALL SUBSCRIPTION HERE
						Log.d(TAG, "No subscription");
					}
				}
			});
		} else {
			// insert fragment dynamically
			if (savedInstanceState == null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				NotificationBookmarkFragment fragment = new NotificationBookmarkFragment();
				fragmentTransaction.add(R.id.notificationBookmarkFragment,
						fragment);
				fragmentTransaction.commit();
			}
		}
		// overriding back phone's back button, it will take to last activity
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
