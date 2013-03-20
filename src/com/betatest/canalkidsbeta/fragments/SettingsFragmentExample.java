package com.betatest.canalkidsbeta.fragments;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.controllers.BookmarksController;
import com.betatest.canalkidsbeta.dao.InternalStorageDAO;

public class SettingsFragmentExample extends Fragment {

	private Button backButton;
	private Button contactUs;
	private Button sign;
	private Button removeBookmarks;

	private static final String TAG = SettingsFragmentExample.class.getSimpleName();
	
	public SettingsFragmentExample newInstance(String subs) {
		SettingsFragmentExample f = new SettingsFragmentExample();
		Bundle args = new Bundle();
		args.putString("subscription", subs);
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

		View view = inflater.inflate(R.layout.activity_settings, container,
				false);

		// Need to hide the button 'assinar' in case user already have subs
		View buttonSignFrame = view.findViewById(R.id.button_sign_frame);

		// TODO CREATE A CONTROLLER TO READ SHARED PREF (DAO)
		// Verify if user has subs
		if (getArguments().getString("subscription") != "userNo") {
			// User doesn't have subs, set button 'assinar' visible
			buttonSignFrame.setVisibility(View.VISIBLE);
			sign = (Button) view.findViewById(R.id.button_sign);

			sign.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// subscriptionController.buySubscription(context, activity,
					// v, mHelper);
				}
			});
		} else {
			// User doesn't have subs, bye button
			buttonSignFrame.setVisibility(View.GONE);
		}

		// TODO CREATE CONTROLLER FOR SENDING EMAILS
		contactUs = (Button) view.findViewById(R.id.button_contact_us);
		contactUs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO CHANGE TO REAL SETTINGS
				final Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "apps@movile.com" });
				emailIntent
						.putExtra(Intent.EXTRA_SUBJECT,
								"Feedback - Canal Desenho [Versão] ([Aparelho]) [Código ?]");
				emailIntent.putExtra(Intent.EXTRA_TEXT,
						"Escreva sua opinião sobre o Canal Desenho aqui: ");
				getActivity().startActivity(Intent.createChooser(emailIntent,
						"Send mail..."));
			}
		});
		
		// TODO INTERNAL STORAGE DAO SHOULD BE CALLED FROM A CONTROLLER
		// internal storage will give us the movies bookmark hashmap
		InternalStorageDAO internalStorageDAO = new InternalStorageDAO();
		HashMap<String, String> bookmarkedHash = null;

		// Verify if user bookmarked any movies
		bookmarkedHash = (HashMap<String, String>) internalStorageDAO
				.readObjectFromMemory(getActivity(), "bookmarked");

		// Need to hide the button 'assinar' in case user already have subs
		View buttonRemoveBookmarkFrame = view.findViewById(R.id.button_remove_bookmarks_frame);

		if (!bookmarkedHash.isEmpty()) {
			buttonRemoveBookmarkFrame.setVisibility(View.VISIBLE);
			removeBookmarks = (Button) view.findViewById(R.id.button_remove_bookmarks);
			removeBookmarks.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					BookmarksController bookmarksController = new BookmarksController();
					bookmarksController.removeAllMoviesFromBookmarks(getActivity());
				}
			});
		} else {
			buttonRemoveBookmarkFrame.setVisibility(View.GONE);
		}
		
		return view;
	}
}