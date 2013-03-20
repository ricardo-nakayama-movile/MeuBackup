package com.betatest.canalkidsbeta.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.betatest.canalkidsbeta.R;

public class NotificationOfflineFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		//TODO NEED TO PUT A STAR IN NOTIFICATION TEXT
		RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(
				R.layout.notification_offline_fragment, container, false);

		return relativeLayout;
	}

}
