package com.betatest.canalkidsbeta.activities;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.adapter.MovieAdapter;
import com.betatest.canalkidsbeta.billing.util.IabHelper;
import com.betatest.canalkidsbeta.controllers.SubscriptionController;
import com.betatest.canalkidsbeta.dao.SharedPreferencesDAO;
import com.betatest.canalkidsbeta.fragments.NotificationListFragment;
import com.betatest.canalkidsbeta.fragments.NotificationOfflineFragment;
import com.betatest.canalkidsbeta.util.ConnectionStatus;
import com.betatest.canalkidsbeta.vo.ChannelContentsResponseParcel;
import com.betatest.canalkidsbeta.vo.Movie;

public class ListActivity extends FragmentActivity {

	private ListView listComplex;
	private ChannelContentsResponseParcel channelContentsResponseParcel;
	private List<Movie> movies;
	private Button subscriptionButton;

	private Button settingsButton;
	private Button bookmarksButton;
	private MovieAdapter adapter;

	// The object for subscription
	private IabHelper mHelper;

	private String publicKey;

	private Context context;
	private Activity activity;

	private static final String TAG = ListActivity.class.getSimpleName();

	// TODO THE SHADOW OF THE MAIN BAR IS NOT TRANSPARENT FIX IT!
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_list);

		context = this;
		activity = this;

		final SubscriptionController subscriptionController = new SubscriptionController();
		publicKey = subscriptionController.getBase64EncodedPublicKey();
		mHelper = new IabHelper(context, publicKey);

		// verify and consume in-apps
		subscriptionController.initSubscription(context, activity, mHelper);

		SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();

		// insert fragment dynamically

		ConnectionStatus connectionStatus = new ConnectionStatus();

		if (connectionStatus.checkConnectivity(context)) {
			if (savedInstanceState == null
					&& sharedPreferencesDAO.read(context, "firstTimer") == null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				NotificationListFragment fragment = new NotificationListFragment();
				fragmentTransaction
						.add(R.id.notificationListFragment, fragment);
				fragmentTransaction.commit();
			}
		}else{
			if (savedInstanceState == null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				NotificationOfflineFragment fragment = new NotificationOfflineFragment();
				fragmentTransaction
						.add(R.id.notificationOfflineFragment, fragment);
				fragmentTransaction.commit();
			}
		}

		// go to settings activity
		settingsButton = (Button) findViewById(R.id.settings_button);
		settingsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, SettingsActivity.class);
				startActivity(intent);
			}
		});

		// go to bookmarks activity, sending the movies list
		bookmarksButton = (Button) findViewById(R.id.bookmarks_button);
		bookmarksButton
				.setBackgroundResource(R.drawable.bookmark_button_animation);
		bookmarksButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, BookmarksActivity.class);
				intent.putExtra("movies", channelContentsResponseParcel);
				startActivity(intent);
			}
		});

		listComplex = (ListView) findViewById(R.id.list_main);

		// get movies list loaded in splash screen
		channelContentsResponseParcel = getIntent().getExtras().getParcelable(
				"movies");

		movies = Movie.getMoviesList(channelContentsResponseParcel);

		// create adapter to populate the list
		adapter = new MovieAdapter(movies, context, activity);

		if (!adapter.checkListNull()) {

			// create button at the end of the list
			View footerView = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.list_footer, null, false);
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

					if (sharedPreferencesDAO.read(context, "subscription") == "userNo"
							|| movie.accountType.equals("free")) {

						// TODO LAYOUT SET, BUT NO CONNECTION... BLACK SCREEN OF
						// DEATH!
						Intent intent = new Intent(context, VideoActivity.class);
						intent.putExtra("movieTag", movie.tag);
						intent.putExtra("movieUrl", movie.urlMovie);
						startActivity(intent);

					} else {
						Log.d(TAG, "No subscription, calling play store");
						subscriptionController.buySubscription(context,
								activity, arg1, mHelper);
					}

				}
			});

			// TODO BUTTON SHOULD ONLY BE VISIBLE WHEN NOT SUBSCRIBED OR WORKING
			// ONLINE
			// if clicked in the button at the end of the list, call
			// subscription
			subscriptionButton = (Button) footerView
					.findViewById(R.id.button_list_end);
			subscriptionButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					subscriptionController.buySubscription(context, activity,
							v, mHelper);
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	public void onDestroy() {
		super.onDestroy();

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

}
