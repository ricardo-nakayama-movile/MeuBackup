package com.betatest.canalkidsbeta.controllers;

import java.io.File;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.betatest.canalkidsbeta.activities.VideoActivity;
import com.betatest.canalkidsbeta.util.ConnectionStatus;

public class VideoController {

	private final String VIDEO_FORMAT_MP4 = ".mp4";
	private static final String TAG = VideoController.class.getSimpleName();

	/**
	 * Verify if video was already downloaded Parameter movieName must not have
	 * format appended
	 * 
	 * @param context
	 * @param movieName
	 * 
	 * @return Boolean
	 */
	public Boolean verifyExistsVideoMp4(Context context, String movieName) {

		Log.d(TAG, "Verifying if video is in storage");

		movieName = movieName.concat(VIDEO_FORMAT_MP4);
		File file = new File(context.getExternalFilesDir(null), movieName);

		if (file.exists()) {
			Log.d(TAG, "Video found in storage");
			return true;
		}

		Log.d(TAG, "Video not found in storage");
		return false;
	}

	/**
	 * Choose whether a video should be played from stream or offline
	 * 
	 * @param context
	 * @param movie
	 * @param videoView
	 * 
	 * @return Void
	 */
	public Void loadVideo(Context context, String movieTag, String movieUrl,
			VideoView videoView) {
		String filePath = context.getExternalFilesDir(null).toString();
		String fileName = movieTag.concat(VIDEO_FORMAT_MP4);
		filePath = filePath.concat("/" + fileName);

		Log.d(TAG, "Checking if we should play video offline or streaming");

		if (verifyExistsVideoMp4(context, movieTag)) {
			playVideoOffline(context, filePath, videoView);
		} else {
			ConnectionStatus connectionStatus = new ConnectionStatus();

			// TODO TOAST NEED TO BE CONNECTED
			if (connectionStatus.checkConnectivity(context)) {
				playVideoStream(context, movieUrl, videoView);
			}
		}

		return null;
	}

	/**
	 * Play an already downloaded video
	 * 
	 * @param context
	 * @param filePath
	 * @param videoView
	 * 
	 * @return Void
	 */
	public Void playVideoOffline(Context context, String filePath,
			VideoView videoView) {

		Log.d(TAG, "Playing video offline");

		videoView.setVideoPath(filePath);

		MediaController mediaController = new MediaController(context);
		mediaController.setMediaPlayer(videoView);

		videoView.setMediaController(mediaController);
		videoView.requestFocus();
		videoView.start();

		videoView.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer arg0) {
				
				arg0.release();
				// TODO RETURN TO LAST SCREEN
			}
		});

		return null;
	}

	/**
	 * Play a video streaming
	 * 
	 * @param context
	 * @param url
	 * @param videoView
	 * 
	 * @return Void
	 */
	public Void playVideoStream(Context context, String url, VideoView videoView) {

		Log.d(TAG, "Playing video stream");

		MediaController mediaController = new MediaController(context);
		mediaController.setAnchorView(videoView);

		Uri video = Uri.parse(url);
		videoView.setMediaController(mediaController);
		videoView.setVideoURI(video);
		videoView.start();

		videoView.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer arg0) {
				// TODO RETURN TO LAST SCREEN

			}
		});

		return null;
	}
}
