package com.betatest.canalkidsbeta.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.betatest.canalkidsbeta.R;

public class ImageFragmentExample extends Fragment {

	public static ImageFragmentExample newInstance(int imageResourceId) {
		ImageFragmentExample myFragment = new ImageFragmentExample();
		Bundle args = new Bundle();
		args.putInt("imageResourceId", imageResourceId);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("Test", "hello");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.details_b, container, false);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		imageView.setImageResource(getArguments().getInt("imageResourceId", 0));
		return view;
	}
}