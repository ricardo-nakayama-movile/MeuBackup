package com.betatest.canalkidsbeta.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.billing.util.IabHelper;
import com.betatest.canalkidsbeta.controllers.SubscriptionController;
import com.betatest.canalkidsbeta.dao.SharedPreferencesDAO;
import com.betatest.canalkidsbeta.vo.ChannelContentsResponseParcel;

//TODO NEED TO SET TO VERTICAL AFTER VIDEO PLAYING OR BACK BUTTON IN VIDEO
//TODO NEED TO CREATE BUTTON TO CHANGE VIEWS
//TODO NEED TO REFRESH NEXT FRAGMENT

public class ViewPagerExample extends FragmentActivity {
	private MyAdapter mAdapter;
	private ViewPager mPager;

	private Context context;
	private Activity activity;

	private static final String TAG = ViewPagerExample.class.getSimpleName();

	// The object for subscription
	private IabHelper mHelper;

	private String publicKey;
	public static String subscription;

	public static ChannelContentsResponseParcel channelContentsResponseParcel;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(1);

		context = this;
		activity = this;

		SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();

		// Need to verify if user is already subscribed, to allow/not allow the
		// subs
		SubscriptionController subscriptionController = new SubscriptionController();
		publicKey = subscriptionController.getBase64EncodedPublicKey();
		mHelper = new IabHelper(context, publicKey);
		subscriptionController.initSubscription(context, activity, mHelper);

		subscription = sharedPreferencesDAO.read(context, "subscription");
		channelContentsResponseParcel = getIntent().getExtras().getParcelable(
				"movies");
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

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				// return new
				// BookmarkFragmentExample().newInstance(subscription,channelContentsResponseParcel);
				return new DetailFragmentExample();
			case 1:
				return new ListFragmentExample().newInstance(subscription,
						channelContentsResponseParcel);
			case 2:
				return new SettingsFragmentExample().newInstance(subscription);

			default:
				return null;
			}
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

}
