package ru.skytechdev.tskgviewerreborn;

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

public class LastSeenActivity extends Activity implements OnItemClickListener {
	private ProgressDialog ProgressBar;
	public static TSEngine tsEngine = null;
	
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
		
		ProgressBar = ProgressDialog.show(LastSeenActivity.this, "���������� �����...",
				  "��������� ������.... ", true, false);
		
		new AsyncImageLoader().execute();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProgressBar = ProgressDialog.show(LastSeenActivity.this, "���������� �����...",
				  "��������� ������.... ", true, false);		
		new AsyncExecution().execute(arg2);
	}

    class AsyncExecution extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... arg0) {
			boolean result = false;
			
			String url = tsEngine.getLastSeenUrl(arg0[0]);
			
			tsEngine.selectSerial(url);
			
			if (tsEngine.getSeasonCount() > 0) {
				result = true;
			}
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (!result) {
				Toast.makeText(getBaseContext(),
						"�� ������� ��������� ������",
						Toast.LENGTH_LONG).show();
			}
			else {
				SerialActivity.tsEngine = tsEngine;
				Intent intent = new Intent(LastSeenActivity.this, SerialActivity.class);
				startActivity(intent);								
			}
		}
    }
    
    class AsyncImageLoader extends AsyncTask<Void, Void, Void> {
    	TSItemBitmap[] items;
    	
		@Override
		protected Void doInBackground(Void... arg0) {
					
			items = new TSItemBitmap[tsEngine.getLastSeenCount()];	
			
			TSItem item;
			Bitmap btm;
			for (int i = 0; i < tsEngine.getLastSeenCount(); i++) {
				item = tsEngine.getLastSeenItem(i);
				btm = (new ImageManager(tsEngine.getBaseContext()).getBitmap(item.imgurl));
				items[i] = new TSItemBitmap(item,btm);
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
			
			TSLastSeenAdapter lSeenAdapter = new TSLastSeenAdapter(LastSeenActivity.this,items);
			
			lastSeenList.setAdapter(lSeenAdapter);
		}
    }    

}