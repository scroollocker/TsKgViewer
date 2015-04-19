package ru.skytechdev.tskgviewerreborn;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnLongClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class NewEpiActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {
	public static TSEngine tsEngine = null;	
	private ProgressDialog ProgressBar;	
	private final int MENU_ADD_FAVORITE = 2;	
	private final int MENU_OPEN_SERIAL = 1;
	private int longPressedItem = 0;
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		longPressedItem = arg2;
		return false;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		switch(v.getId()) {
			case R.id.listView1: {				
				menu.add(0, MENU_OPEN_SERIAL, Menu.NONE, "Открыть сериал");
				menu.add(0, MENU_ADD_FAVORITE, Menu.NONE, "/\\ \\/ Избранное");
				break;
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ProgressBar = ProgressDialog.show(NewEpiActivity.this, "Загрузка...",
				  "Пожалуйста ждите.... ", true, false);
		switch(item.getItemId()) {
			case MENU_OPEN_SERIAL: {
				new SerialContextTask(MENU_OPEN_SERIAL).execute(longPressedItem);
				break;
			}
			case MENU_ADD_FAVORITE: {
				new SerialContextTask(MENU_ADD_FAVORITE).execute(longPressedItem);
				break;
			}
		}		
		
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newepire);
		ListView epiList = (ListView)findViewById(R.id.listView1);
		epiList.setOnItemClickListener(this);		
		
		epiList.setOnItemLongClickListener(this);
		registerForContextMenu(epiList);		
		
		TSNewEpisodesItem[] items = new TSNewEpisodesItem[tsEngine.getNewEpiCount()];
		for (int i = 0; i < tsEngine.getNewEpiCount(); i++) {
			items[i] = tsEngine.getNewEpiItem(i);
			if (tsEngine.isInFavorites("", items[i].link, "")) {
				items[i].isFavorite = true;
			}
			else {
				items[i].isFavorite = false;
			}
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
		ProgressBar = ProgressDialog.show(NewEpiActivity.this, "Загрузка...",
				  "Пожалуйста ждите.... ", true, false);		
		new AsyncExecution().execute(arg2);
	}
	
	class SerialContextTask extends AsyncTask<Integer, Void, Integer> {
		
		private int taskId;
		
		public SerialContextTask(int task) {			
			super();
			taskId = task;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			int itemId = params[0];
			int result = 0;
			
			Log.d("SerialContextTask::itemId", String.valueOf(itemId));
			
			String url = tsEngine.getNewEpiUrl(itemId);
			
			Log.d("SerialContextTask::url", url);			
			
			String serialName = Favorites.parseSerialName(url);
			Log.d("SerialContextTask::serialName", serialName);
			
			if (!serialName.isEmpty()) {
				String serialUrl = "http://www.ts.kg/show/"+serialName;
				tsEngine.selectSerial(serialUrl);
				if (tsEngine.getSeasonCount() > 0) {
					if (taskId == MENU_OPEN_SERIAL) {
						result = MENU_OPEN_SERIAL;
					}
					else if (taskId == MENU_ADD_FAVORITE){
						TSItem serial = new TSItem();
						
						serial.value = tsEngine.getSerialCaption();
						serial.url = tsEngine.getSerialUrl();
						serial.imgurl = tsEngine.getSerialImg();
						
						if (tsEngine.isInFavorites(serial.value,serial.url,serial.imgurl)) {
							tsEngine.delFromFavorites(serial.value,serial.url,serial.imgurl);
						}
						else {
							tsEngine.addToFavorites(serial.value,serial.url,serial.imgurl);
						}
						
						result = MENU_ADD_FAVORITE;
					}
				}
			}
			
			return result;			
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			ProgressBar.dismiss();
			if (result == 0) {
				Toast.makeText(NewEpiActivity.this, "Что то пошло не так =(", Toast.LENGTH_LONG).show();
				return;
			}
			if (result == MENU_OPEN_SERIAL) {				
				SerialActivity.tsEngine = tsEngine;
				Intent intent = new Intent(NewEpiActivity.this, SerialActivity.class);
				startActivity(intent);	
			}
			else if (result == MENU_ADD_FAVORITE){				
				Toast.makeText(NewEpiActivity.this, "Избранное обновлено", Toast.LENGTH_LONG).show();								
			}
		}
		
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
