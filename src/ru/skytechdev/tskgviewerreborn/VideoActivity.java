package ru.skytechdev.tskgviewerreborn;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class VideoActivity extends Activity {
	private VideoView video;
	private ProgressDialog progress;
	private int nowPlayed;
	private EpiSeen seenEpi = new EpiSeen();
	
	private int currTryCount;
	private final int MAX_TRY_COUNT = 5;
	
	public static ArrayList<String> playlist = new ArrayList<String>();
	public static boolean grabType = true;
	
	private void showProgressBar() {
		progress = new ProgressDialog(VideoActivity.this);
		progress.setTitle("Загрузка");
		progress.setMessage("Пожалуйста ждите...");
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String prevText = "";
				if (nowPlayed < playlist.size()) {				
					String url = playlist.get(nowPlayed);
					VideoUrlGenerator urlGen = new VideoUrlGenerator();
					prevText = urlGen.makeSerialPrev(url);
				}
				return prevText;
			}
			
			@Override
			protected void onPostExecute(String result) {
				progress.setTitle("Загрузка");
				progress.setMessage(result);				
				progress.setCancelable(true);
				
			}
			
		}.execute();
		
		progress.show();
	}
	
	private void playNext() {
		nowPlayed++;
		showProgressBar();

		new AsyncExecution().execute(nowPlayed);
	}
	
	@Override 
	protected void onStart() {
		super.onStart();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	@Override 
	protected void onStop() {
		super.onStop();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		seenEpi.setContext(VideoActivity.this);
		seenEpi.loadSerialAct(playlist.get(0));
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
				Toast.makeText(VideoActivity.this, "Ошибка пробуем еще раз! Попытка# "+(currTryCount+1)+
												   " из "+MAX_TRY_COUNT, Toast.LENGTH_LONG).show();
				showProgressBar();
				new AsyncExecution().execute(nowPlayed);
				showProgressBar();
				currTryCount++;
				Log.d("Video","TRY");
			}
			else {
				Log.d("Video","TRY ERR");
				Toast.makeText(VideoActivity.this, "Ошибка! Переходим к следующему эпизоду!", Toast.LENGTH_LONG).show();
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
				
				seenEpi.addToList(playlist.get(id));
				
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
