package ru.skytechdev.tskgviewerreborn.activity;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.adapters.TsRecentSerialsAdapter;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import ru.skytechdev.tskgviewerreborn.structs.TsBitmapItem;
import ru.skytechdev.tskgviewerreborn.structs.TsSerialItem;
import ru.skytechdev.tskgviewerreborn.utils.ImageManager;
import ru.skytechdev.tskgviewerreborn.utils.RecentSerials;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class RecentSerialsActivity extends Activity implements OnItemClickListener {
	private ProgressDialog ProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lastseennew);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		ListView lastSeenList = (ListView)findViewById(R.id.listView1);
		lastSeenList.setOnItemClickListener(this);				
		
		ProgressBar = ProgressDialog.show(RecentSerialsActivity.this, "Загрузка...",
				  "Пожалуйста ждите.... ", true, false);
		
		new ImageLoaderTask().execute();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> notUse1, View notUse2, int itemId, long notUse3) {
		ProgressBar = ProgressDialog.show(RecentSerialsActivity.this, "Загрузка...",
				  "Пожалуйста ждите.... ", true, false);		
		new SerialOpenTask().execute(itemId);
	}

    class SerialOpenTask extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... itemId) {
			boolean result = false;
			
			String url = RecentSerials.getInstance().getUrl(itemId[0]);
			
			result = TsEngine.getInstance().loadSerialInfo(url);

			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (!result) {
				Toast.makeText(getBaseContext(),
						"Новозможно загрузить сериал!",
						Toast.LENGTH_LONG).show();
			}
			else {
				Intent intent = new Intent(RecentSerialsActivity.this, SerialActivity.class);
				startActivity(intent);								
			}
		}
    }
    
    class ImageLoaderTask extends AsyncTask<Void, Void, Void> {
    	TsBitmapItem[] items;
    	
		@Override
		protected Void doInBackground(Void... notUse) {
			
			int recentCount = RecentSerials.getInstance().getCount();
					
			items = new TsBitmapItem[recentCount];	
			
			TsSerialItem item;
			Bitmap btm;
			for (int i = 0; i < recentCount; i++) {
				item = RecentSerials.getInstance().get(i);
				btm = (new ImageManager(getBaseContext()).getBitmap(item.imgurl));
				items[i] = new TsBitmapItem(item,btm);
			}
			
			return null;			
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			ProgressBar.dismiss();
			if (items.length == 0) {
				return;
			}
			ListView lastSeenList = (ListView)findViewById(R.id.listView1);
			
			TsRecentSerialsAdapter lSeenAdapter = new TsRecentSerialsAdapter(RecentSerialsActivity.this,items);
			
			lastSeenList.setAdapter(lSeenAdapter);
		}
    }    

}
