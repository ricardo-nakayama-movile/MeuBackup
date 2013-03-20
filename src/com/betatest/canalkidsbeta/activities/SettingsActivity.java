package com.betatest.canalkidsbeta.activities;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.billing.util.IabHelper;
import com.betatest.canalkidsbeta.controllers.BookmarksController;
import com.betatest.canalkidsbeta.controllers.SubscriptionController;
import com.betatest.canalkidsbeta.dao.InternalStorageDAO;
import com.betatest.canalkidsbeta.dao.SharedPreferencesDAO;

public class SettingsActivity extends Activity {

	private static final String TAG = SettingsActivity.class.getSimpleName();

	// The object for subscription
	private IabHelper mHelper;

	private String publicKey;

	Context context;
	Activity activity;

	private Button backButton;
	private Button contactUs;
	private Button sign;
	private Button removeBookmarks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		context = this;
		activity = this;

		SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();

		// Need to verify if user is already subscribed, to allow/not allow the
		// subs
		final SubscriptionController subscriptionController = new SubscriptionController();
		publicKey = subscriptionController.getBase64EncodedPublicKey();
		mHelper = new IabHelper(context, publicKey);
		subscriptionController.initSubscription(context, activity, mHelper);

		// Back button in action bar need to do the same as phone back button
		backButton = (Button) findViewById(R.id.back_button);

		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		// Need to hide the button 'assinar' in case user already have subs
		View buttonSignFrame = findViewById(R.id.button_sign_frame);

		// TODO CREATE A CONTROLLER TO READ SHARED PREF (DAO)
		// Verify if user has subs
		if (sharedPreferencesDAO.read(context, "subscription") != "userNo") {
			// User doesn't have subs, set button 'assinar' visible
			buttonSignFrame.setVisibility(View.VISIBLE);
			sign = (Button) findViewById(R.id.button_sign);

			sign.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					subscriptionController.buySubscription(context, activity,
							v, mHelper);
				}
			});
		} else {
			// User doesn't have subs, bye button
			buttonSignFrame.setVisibility(View.GONE);
		}

		// TODO CREATE CONTROLLER FOR SENDING EMAILS
		contactUs = (Button) findViewById(R.id.button_contact_us);
		contactUs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO CHANGE TO REAL SETTINGS
				final Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "apps@movile.com" });
				emailIntent
						.putExtra(Intent.EXTRA_SUBJECT,
								"Feedback - Canal Desenho [Versão] ([Aparelho]) [Código ?]");
				emailIntent.putExtra(Intent.EXTRA_TEXT,
						"Escreva sua opinião sobre o Canal Desenho aqui: ");
				context.startActivity(Intent.createChooser(emailIntent,
						"Send mail..."));
			}
		});

		// TODO INTERNAL STORAGE DAO SHOULD BE CALLED FROM A CONTROLLER
		// internal storage will give us the movies bookmark hashmap
		InternalStorageDAO internalStorageDAO = new InternalStorageDAO();
		HashMap<String, String> bookmarkedHash = null;

		// Verify if user bookmarked any movies
		bookmarkedHash = (HashMap<String, String>) internalStorageDAO
				.readObjectFromMemory(context, "bookmarked");

		// Need to hide the button 'assinar' in case user already have subs
		View buttonRemoveBookmarkFrame = findViewById(R.id.button_remove_bookmarks_frame);

		if (!bookmarkedHash.isEmpty()) {
			buttonRemoveBookmarkFrame.setVisibility(View.VISIBLE);
			removeBookmarks = (Button) findViewById(R.id.button_remove_bookmarks);
			removeBookmarks.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					BookmarksController bookmarksController = new BookmarksController();
					bookmarksController.removeAllMoviesFromBookmarks(context);
				}
			});
		} else {
			buttonRemoveBookmarkFrame.setVisibility(View.GONE);
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
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

}
