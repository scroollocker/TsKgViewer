package ru.skytechdev.tskgviewerreborn;

import java.util.ArrayList;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class VideoActivity extends Activity {
	private VideoView video;
	private ProgressDialog progress;
	private int nowPlayed;
	
	private int currTryCount;
	private final int MAX_TRY_COUNT = 5;
	
	public static ArrayList<String> playlist = new ArrayList<String>();
	public static boolean grabType = true;
	
	private void showProgressBar() {
		progress = ProgressDialog.show(VideoActivity.this, "", "Открытие..." ,false);
		progress.setCancelable(true);
	}
	
	private void playNext() {
		nowPlayed++;
		showProgressBar();
		new AsyncExecution().execute(nowPlayed);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		video = (VideoView)findViewById(R.id.videoView1);
		video.setMediaController(new MediaController(this));
		video.requestFocus(0);
		video.setOnPreparedListener(myOnPreparedListener);
		video.setOnCompletionListener(myOnComletionListener);
		video.setOnErrorListener(myOnErrorListener);
		nowPlayed = -1;
		playNext();	
	}

	
	MediaPlayer.OnPreparedListener myOnPreparedListener = new MediaPlayer.OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			progress.dismiss();
			currTryCount = 0;
			video.start();
		}
	};
	
	MediaPlayer.OnCompletionListener myOnComletionListener = new MediaPlayer.OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			progress.dismiss();
			video.stopPlayback();
			playNext();
		}
	};
	
	MediaPlayer.OnErrorListener myOnErrorListener = new MediaPlayer.OnErrorListener() {
		
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			progress.dismiss();
			Log.d("Video","ERRR");
			if (currTryCount < MAX_TRY_COUNT) {
				Toast.makeText(VideoActivity.this, "Ошибка! Повторная попытка "+(currTryCount+1)+
												   " из "+MAX_TRY_COUNT, Toast.LENGTH_LONG).show();
				showProgressBar();
				new AsyncExecution().execute(nowPlayed);
				showProgressBar();
				currTryCount++;
				Log.d("Video","TRY");
			}
			else {
				Log.d("Video","TRY ERR");
				Toast.makeText(VideoActivity.this, "Ошибка! Исчерпаны все попытки", Toast.LENGTH_LONG).show();
				playNext();
				currTryCount = 0;
			}
			return false;
		}
	};

    class AsyncExecution extends AsyncTask<Integer, Void, Boolean> {

    	private String url = "";
		@Override
		protected Boolean doInBackground(Integer... arg0) {
			boolean result = false;
			int id = arg0[0];
			
			int listSize = playlist.size();
			if (listSize > id) {	
				VideoUrlGenerator urlGen = new VideoUrlGenerator();
				url = urlGen.makeVideoUrl(playlist.get(id));
				result = true;
			}
			else {
				result = false;
			}
			
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			if (result == false) {
				finish();
			}
			else {
				video.setVideoURI(Uri.parse(url));
			}
		}
    }

}
