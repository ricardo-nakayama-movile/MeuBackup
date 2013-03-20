package com.betatest.canalkidsbeta.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.dao.SharedPreferencesDAO;

public class NotificationListFragment extends Fragment {

	private Button notificationListButton;
	private Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		context = container.getContext();
		
		View view = (RelativeLayout) inflater.inflate(
				R.layout.notification_list_fragment, container, false);
		
		TextView text = (TextView) view.findViewById(R.id.notificationFragmentText);
		//TODO VERIFY CONNECTION AND SET MESSAGE
		//text.setText("heythere");
		
		notificationListButton = (Button) view
				.findViewById(R.id.closeNotificationList);
		notificationListButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO AGAIN DAO IN VIEW? IS THIS A CONTROLLER OR A VIEW? CALL CONTROLLER...
				
				SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();
				sharedPreferencesDAO.write(context, "firstTimer", "no");
				
				Log.i("INFO", "Clicked in fragment");
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.notificationListFragment));
				fragmentTransaction.commit();
			}
		});

		return view;
	}

}
