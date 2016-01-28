package ru.skytechdev.tskgviewerreborn.activity;

import java.util.ArrayList;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import ru.skytechdev.tskgviewerreborn.utils.EpisodeHelper;
import ru.skytechdev.tskgviewerreborn.utils.RecentEpisodes;
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
	private final String LogTag = "VideoActivity::LOG";
	
	private ArrayList<String> playlist = new ArrayList<String>();
	
	private void showProgressBar() {
		progress = new ProgressDialog(VideoActivity.this);
		progress.setTitle("Загрузка");
		progress.setMessage("Пожалуйста ждите...");
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... notUse) {
				String prevText = "";
				if (nowPlayed < playlist.size()) {				
					String url = playlist.get(nowPlayed);
					prevText = EpisodeHelper.getInstance().makeSerialPrev(url);
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

		new PlayerTask().execute(nowPlayed);
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
		
		playlist = (ArrayList<String>)getIntent().getExtras().get(TsEngine.TS_PLAYLIST_EXTRA_STR);
		
		if (playlist == null) {
			Log.d(LogTag, "ERR::playlist == null");
			return;
		}
		
		if (playlist.size() == 0) {
			Log.d(LogTag, "ERR::playlist.size() == 0");
			return;
		}
		
		RecentEpisodes.getInstance().loadSerialAct(playlist.get(0));
		
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
			Toast.makeText(VideoActivity.this, "Ошибка воспроизведения эпизода", Toast.LENGTH_LONG).show();
			finish();
			return false;
		}
	};

    class PlayerTask extends AsyncTask<Integer, Void, Boolean> {

    	private String url = "";
		@Override
		protected Boolean doInBackground(Integer... videoId) {
			boolean result = false;
			int id = videoId[0];
			
			int listSize = playlist.size();
			if (listSize > id) {	
				
				RecentEpisodes.getInstance().addToList(playlist.get(id));
				
				url = EpisodeHelper.getInstance().makeVideoUrl(playlist.get(id));
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
