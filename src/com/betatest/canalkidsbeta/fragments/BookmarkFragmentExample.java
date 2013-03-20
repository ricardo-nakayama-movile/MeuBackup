package com.betatest.canalkidsbeta.fragments;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.VideoView;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.activities.VideoActivity;
import com.betatest.canalkidsbeta.adapter.BookmarkAdapter;
import com.betatest.canalkidsbeta.dao.SharedPreferencesDAO;
import com.betatest.canalkidsbeta.vo.ChannelContentsResponseParcel;
import com.betatest.canalkidsbeta.vo.Movie;

public class BookmarkFragmentExample extends Fragment {

	private ListView listComplex;
	private ChannelContentsResponseParcel channelContentsResponseParcel;
	private List<Movie> movies;
	private VideoView videoView;

	private static final String TAG = BookmarkFragmentExample.class
			.getSimpleName();

	public BookmarkFragmentExample newInstance(String subs,
			ChannelContentsResponseParcel channelContentsResponse) {
		BookmarkFragmentExample f = new BookmarkFragmentExample();
		Bundle args = new Bundle();
		args.putString("subscription", subs);
		args.putParcelable("movies", channelContentsResponse);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_bookmarks, container,
				false);

		// get movies from listActivity
		channelContentsResponseParcel = getArguments().getParcelable(
				"movies");
		movies = Movie.getMoviesList(channelContentsResponseParcel);

		// adapter will get only the movies that are bookmarked
		BookmarkAdapter adapter = new BookmarkAdapter(movies, getActivity());

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

					if (sharedPreferencesDAO.read(getActivity(), "subscription") == "userNo"
							|| movie.accountType.equals("free")) {
						
						// TODO LAYOUT SET, BUT NO CONNECTION... BLACK SCREEN OF
						// DEATH!
						Intent intent = new Intent(getActivity(),
								VideoActivity.class);
						intent.putExtra("movieTag", movie.tag);
						intent.putExtra("movieUrl", movie.urlMovie);
						startActivity(intent);

					} else {
						// TODO CALL SUBSCRIPTION HERE
						Log.d(TAG, "No subscription");
					}
				}
			});
		} else {
			// insert fragment dynamically
			if (savedInstanceState == null) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				NotificationBookmarkFragment fragment = new NotificationBookmarkFragment();
				fragmentTransaction.add(R.id.notificationBookmarkFragment,
						fragment);
				fragmentTransaction.commit();
			}
		}
		
		return view;
	}
}