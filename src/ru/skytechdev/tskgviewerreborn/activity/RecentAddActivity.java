package ru.skytechdev.tskgviewerreborn.activity;

import java.util.ArrayList;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.adapters.TsRecentAddAdapter;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import ru.skytechdev.tskgviewerreborn.serial.SerialInfo;
import ru.skytechdev.tskgviewerreborn.structs.TsRecentAddItem;
import ru.skytechdev.tskgviewerreborn.structs.TsSerialItem;
import ru.skytechdev.tskgviewerreborn.utils.EpisodeHelper;
import ru.skytechdev.tskgviewerreborn.utils.Favorites;
import ru.skytechdev.tskgviewerreborn.utils.RecentAddHelper;
import ru.skytechdev.tskgviewerreborn.utils.TsUtils;
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

public class RecentAddActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {
	private ProgressDialog ProgressBar;	
	private final int MENU_ADD_FAVORITE = 2;	
	private final int MENU_OPEN_SERIAL = 1;
	private int longPressedItem = 0;
	
	private ArrayList<TsRecentAddItem> recentAddItems;
	
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
		ProgressBar = ProgressDialog.show(RecentAddActivity.this, "Загрузка...",
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
		
		recentAddItems = (ArrayList<TsRecentAddItem>)getIntent().getExtras().get(TsEngine.TS_RECENTADD_EXTRA_STR);
		
		TsRecentAddItem[] items = new TsRecentAddItem[recentAddItems.size()];
		for (int i = 0; i < recentAddItems.size(); i++) {
			items[i] = recentAddItems.get(i);
			if (Favorites.getInstance().isExist(items[i].link)) {
				items[i].isFavorite = true;
			}
			else {
				items[i].isFavorite = false;
			}
		}
		
		TsRecentAddAdapter adapter = new TsRecentAddAdapter(RecentAddActivity.this,items);
		epiList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProgressBar = ProgressDialog.show(RecentAddActivity.this, "Загрузка...",
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
			
			String url = recentAddItems.get(itemId).link;
			
			Log.d("SerialContextTask::url", url);			
			
			String serialName = TsUtils.parseSerialName(url);
			Log.d("SerialContextTask::serialName", serialName);
			
			if (!serialName.isEmpty()) {
				String serialUrl = "http://www.ts.kg/show/"+serialName;
				if (TsEngine.getInstance().loadSerialInfo(serialUrl)) {
				//tsEngine.selectSerial(serialUrl);
				//if (tsEngine.getSeasonCount() > 0) {
					if (taskId == MENU_OPEN_SERIAL) {
						result = MENU_OPEN_SERIAL;
					}
					else if (taskId == MENU_ADD_FAVORITE){
						
						SerialInfo serial = TsEngine.getInstance().getSerialInfo();
						
						if (Favorites.getInstance().isExist(serial.getUrl())) {
							Favorites.getInstance().delete(serial.getCaption(), serial.getUrl(), serial.getImg());
						}
						else {
							Favorites.getInstance().add(serial.getCaption(), serial.getUrl(), serial.getImg());
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
				Toast.makeText(RecentAddActivity.this, "Что то пошло не так =(", Toast.LENGTH_LONG).show();
				return;
			}
			if (result == MENU_OPEN_SERIAL) {		
				Intent intent = new Intent(RecentAddActivity.this, SerialActivity.class);
				startActivity(intent);	
			}
			else if (result == MENU_ADD_FAVORITE){				
				Toast.makeText(RecentAddActivity.this, "Избранное обновлено", Toast.LENGTH_LONG).show();								
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
			
			String url = recentAddItems.get(id).link;
						
			if (TsEngine.getInstance().loadSerialInfo(url)) {
				result = true;
			}
			else {
				
				Log.d("NewEpi", newEpiUrl);
				if (!TsEngine.getInstance().useDefaultPlayer()) {
					newEpiUrl = EpisodeHelper.getInstance().makeVideoUrl(recentAddItems.get(id).link);//urlGen.makeVideoUrl(newEpiUrl);
				} else {
					newEpiUrl = recentAddItems.get(id).link;
				}
				
			}
			
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (!result) {				
				if (TsEngine.getInstance().useDefaultPlayer()) {
					
					ArrayList<String> playlist = new ArrayList<String>();
					playlist.add(newEpiUrl);
					
					Intent intent = new Intent(RecentAddActivity.this, VideoActivity.class);
					intent.putExtra(TsEngine.TS_PLAYLIST_EXTRA_STR, playlist);
					startActivity(intent);
					
				}
				else {					
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(newEpiUrl), "video/mp4");
					startActivity(intent);
				}
			}
			else {				
				Intent intent = new Intent(RecentAddActivity.this, SerialActivity.class);
				startActivity(intent);								
			}
		}
    }
    
}
