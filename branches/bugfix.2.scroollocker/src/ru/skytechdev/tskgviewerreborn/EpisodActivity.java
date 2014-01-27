package ru.skytechdev.tskgviewerreborn;

import java.io.File;
import java.util.ArrayList;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EpisodActivity extends Activity implements OnItemClickListener {
	public static TSEngine tsEngine = null;	
	private ProgressDialog ProgressBar;
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episod);
		ListView epiView = (ListView)findViewById(R.id.listView1);

		int seasonId = tsEngine.getSelectedSeasonId();
		
		String epiItem [] = new String[tsEngine.getEpisodesCount(seasonId)];
		
		for (int i = 0; i < tsEngine.getEpisodesCount(seasonId); i++) {
			epiItem[i] = tsEngine.getSeasonCaption(seasonId) + " / " + 
						 tsEngine.getEpisodeCaption(i);
		}
		
		epiView.setAdapter((ListAdapter) new ArrayAdapter<String>(EpisodActivity.this,
				   android.R.layout.simple_list_item_1, epiItem));	
		
		epiView.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProgressBar = ProgressDialog.show(EpisodActivity.this, "Пожалуйста ждите...",
				  "Получение данных.... ", true, false);			
		new AsyncExecution().execute(arg2);
	}
	
	class AsyncExecution extends AsyncTask<Integer, Void, Boolean> {
		private ArrayList<String> playlist = new ArrayList<String>(); 
		
		@Override
		protected Boolean doInBackground(Integer... arg0) {
			boolean result = false;	
			String url = "";
			int selectedPos = arg0[0];
			if (tsEngine.isDefaultPlayer()) {
				playlist.clear();
				int seasonId = tsEngine.getSelectedSeasonId();
				int epiCount = tsEngine.getEpisodesCount(seasonId);
				for (int i = selectedPos; i < epiCount; i++) {
					url = tsEngine.getVideoUrl(i);
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
				url = tsEngine.getVideoUrl(selectedPos);
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
						"Не удалось загрузить выбранную серию",
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
