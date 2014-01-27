package ru.skytechdev.tskgviewerreborn;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NewEpiActivity extends Activity implements OnItemClickListener {
	public static TSEngine tsEngine = null;	
	private ProgressDialog ProgressBar;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newepire);
		ListView epiList = (ListView)findViewById(R.id.listView1);
		epiList.setOnItemClickListener(this);		
		
		TSNewEpisodesItem[] items = new TSNewEpisodesItem[tsEngine.getNewEpiCount()];
		for (int i = 0; i < tsEngine.getNewEpiCount(); i++) {
			items[i] = tsEngine.getNewEpiItem(i);
		}
		
		TSNewEpiAdapter adapter = new TSNewEpiAdapter(NewEpiActivity.this,items);
		epiList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProgressBar = ProgressDialog.show(NewEpiActivity.this, "Пожалуйста ждите...",
				  "Получение данных.... ", true, false);		
		new AsyncExecution().execute(arg2);
	}

    class AsyncExecution extends AsyncTask<Integer, Void, Boolean> {
    	private int id;
    	private String newEpiUrl = "";
		@Override
		protected Boolean doInBackground(Integer... arg0) {
			boolean result = false;
			id = arg0[0];
			
			String url = tsEngine.getNewEpiUrl(id);
			
			tsEngine.selectSerial(url);
			if (tsEngine.getSeasonCount() > 0) {
				result = true;
			}
			else {
				newEpiUrl = tsEngine.getVideoUrl(tsEngine.getNewEpiUrl(id));
				Log.d("NewEpi", newEpiUrl);
				if (!tsEngine.isDefaultPlayer()) {
					VideoUrlGenerator urlGen = new VideoUrlGenerator();
					newEpiUrl = urlGen.makeVideoUrl(newEpiUrl);
				}
				
			}
			
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (!result) {				
				if (tsEngine.isDefaultPlayer()) {
					VideoActivity.playlist.clear();
					VideoActivity.playlist.add(newEpiUrl);
					Intent intent = new Intent(NewEpiActivity.this, VideoActivity.class);
					startActivity(intent);
					
				}
				else {					
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(newEpiUrl), "video/mp4");
					startActivity(intent);
				}
			}
			else {
				
				SerialActivity.tsEngine = tsEngine;
				Intent intent = new Intent(NewEpiActivity.this, SerialActivity.class);
				startActivity(intent);								
			}
		}
    }


}
