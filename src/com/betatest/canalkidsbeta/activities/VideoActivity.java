package com.betatest.canalkidsbeta.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import com.betatest.canalkidsbeta.R;
import com.betatest.canalkidsbeta.controllers.VideoController;

public class VideoActivity extends Activity {

	private VideoView videoView;
	private Context context;
	private String movieName;
	private String movieUrl;

	private static final String TAG = VideoActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videolayout);
		context = this;

		// TODO LAYOUT SET, BUT NO CONNECTION... BLACK SCREEN OF DEATH!
		Log.d(TAG, "Loading video");
		videoView = (VideoView) findViewById(R.id.playvideo);
		VideoController videoController = new VideoController();

		movieName = getIntent().getExtras().getString("movieTag");
		movieUrl = getIntent().getExtras().getString("movieUrl");
		videoController.loadVideo(context, movieName, movieUrl, videoView);

	}
}
