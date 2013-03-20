package com.betatest.canalkidsbeta.fragments;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.activities.VideoActivity;
import com.betatest.canalkidsbeta.adapter.MovieAdapter;
import com.betatest.canalkidsbeta.dao.SharedPreferencesDAO;
import com.betatest.canalkidsbeta.vo.ChannelContentsResponseParcel;
import com.betatest.canalkidsbeta.vo.Movie;

public class ListFragmentExample extends Fragment {

	private ListView listComplex;
	private ChannelContentsResponseParcel channelContentsResponseParcel;
	private List<Movie> movies;
	private Button subscriptionButton;

	private MovieAdapter adapter;

	private static final String TAG = ListFragmentExample.class.getSimpleName();

	public ListFragmentExample newInstance(String subs,
			ChannelContentsResponseParcel channelContentsResponse) {
		ListFragmentExample f = new ListFragmentExample();
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

		View view = inflater.inflate(R.layout.activity_main_list, container,
				false);

		listComplex = (ListView) view.findViewById(R.id.list_main);

		// get movies list loaded in splash screen
		channelContentsResponseParcel = getArguments().getParcelable("movies");

		movies = Movie.getMoviesList(channelContentsResponseParcel);

		// create adapter to populate the list
		adapter = new MovieAdapter(movies, getActivity(), getActivity());

		if (!adapter.checkListNull()) {

			// create button at the end of the list
			View footerView = ((LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE)).inflate(
					R.layout.list_footer, null, false);
			listComplex.addFooterView(footerView);

			// set adapter to the list
			listComplex.setAdapter(adapter);

			// if clicked in a movie and the movie is playable, play it
			listComplex.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					// TODO DAO IN VIEW??? CHANGE IT TO CALL A CONTROLLER
					SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();
					Movie movie = movies.get(arg2);

					if (sharedPreferencesDAO
							.read(getActivity(), "subscription") == "userNo"
							|| movie.accountType.equals("free")) {

						// TODO LAYOUT SET, BUT NO CONNECTION... BLACK SCREEN OF
						// DEATH!
						Intent intent = new Intent(getActivity(),
								VideoActivity.class);
						intent.putExtra("movieTag", movie.tag);
						intent.putExtra("movieUrl", movie.urlMovie);
						startActivity(intent);

					} else {
						Log.d(TAG, "No subscription, calling play store");
						//TODO CALL SUBSCRIPTION
						// subscriptionController.buySubscription(context,
						// activity, arg1, mHelper);
					}

				}
			});
			
			subscriptionButton = (Button) footerView
					.findViewById(R.id.button_list_end);
			subscriptionButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// subscriptionController.buySubscription(context, activity,
					// v, mHelper);
				}
			});
		}

		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}
}