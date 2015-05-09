package ru.skytechdev.tskgviewerreborn.activity;

import java.util.ArrayList;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.Serial.SerialInfo;
import ru.skytechdev.tskgviewerreborn.adapters.TsEpisodesAdapter;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import ru.skytechdev.tskgviewerreborn.structs.TsRecentItem;
import ru.skytechdev.tskgviewerreborn.utils.RecentEpisodes;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class EpisodActivity extends Activity implements OnItemClickListener {
	private ProgressDialog ProgressBar;
	private int seasonId;
	
	@Override
	protected void onStart() {
		super.onStart();
		ListView epiView = (ListView)findViewById(R.id.listView1);
						
		seasonId = getIntent().getExtras().getInt(TsEngine.TS_SEASON_EXTRA_STR);
		
		SerialInfo serialInfo = TsEngine.getInstance().getSerialInfo();
		int epiCount = serialInfo.getEpisodesCount(seasonId);		
		
		TsRecentItem[] episodes = new TsRecentItem[epiCount];
		SerialInfo serial = TsEngine.getInstance().getSerialInfo();
		String serialUrl = serial.getUrl();
		
		RecentEpisodes.getInstance().loadSerialAct(serialUrl);
		
		
		for (int i = 0; i < epiCount; i++) {
			episodes[i] = new TsRecentItem();
			episodes[i].title = serial.getSeasonCaption(seasonId) + " / " + 
					 			serial.getEpisode(seasonId, i).caption;
			episodes[i].date = RecentEpisodes.getInstance().isInList(serial.getEpisode(seasonId, i).url);
		}
		
		TsEpisodesAdapter epiAdapter = new TsEpisodesAdapter(EpisodActivity.this,episodes);
		
		epiView.setAdapter(epiAdapter);		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episod);
		ListView epiView = (ListView)findViewById(R.id.listView1);
		
		epiView.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProgressBar = ProgressDialog.show(EpisodActivity.this, "Загрузка...",
				  "Пожалуйста ждите.... ", true, false);			
		new AsyncExecution().execute(arg2);
	}
	
	class AsyncExecution extends AsyncTask<Integer, Void, Boolean> {
		private ArrayList<String> playlist = new ArrayList<String>(); 
		
		@Override
		protected Boolean doInBackground(Integer... arg0) {
			boolean result = false;	
			String url = "";
			int selectedPos = arg0[0];
			SerialInfo serial = TsEngine.getInstance().getSerialInfo();
			if (tsEngine.isDefaultPlayer()) {
				playlist.clear();
				
				int epiCount = serial.getEpisodesCount(seasonId);
				for (int i = selectedPos; i < epiCount; i++) {
					url = serial.getEpisode(seasonId, i).url;
					if (url.isEmpty()) {
						continue;
					}
					playlist.add(url);
				}
				if (!playlist.isEmpty()) {
					result = true;
				}
			}
			else {
				url = serial.getEpisode(seasonId, selectedPos).url;
				VideoUrlGenerator urlGen = new VideoUrlGenerator();
				url = urlGen.makeVideoUrl(url);				
				if (!url.isEmpty()) {
					playlist.add(url);
					result = true;
				}				
			}			
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (result == false) {
				Toast.makeText(getBaseContext(),
						"Невозможно воспроизвести эпизод",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (tsEngine.isDefaultPlayer()) {
				VideoActivity.playlist = playlist;
				Intent intent = new Intent(EpisodActivity.this, VideoActivity.class);
				startActivity(intent);
			}
			else {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(playlist.get(0)), "video/mp4");
				startActivity(intent);
			}
		}
    }	


}
