package ru.skytechdev.tskgviewerreborn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SerialListActivity extends Activity implements OnItemClickListener {
	public static TSEngine tsEngine = null;	
	private ProgressDialog ProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seriallist);
		ListView serialsItems = (ListView)findViewById(R.id.listView1);
		serialsItems.setOnItemClickListener(this);
		
		String serialsElements[];
		serialsElements = new String[tsEngine.getSerialsCount()];
		for (int i = 0; i < tsEngine.getSerialsCount(); i++) {
			serialsElements[i] = tsEngine.getSerialCaption(i);
		}
		
		serialsItems.setAdapter((ListAdapter) new ArrayAdapter<String>(SerialListActivity.this,
				   android.R.layout.simple_list_item_1, serialsElements));
		
				
	}

	@Override
	protected void onStart() {
		super.onStart();			
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProgressBar = ProgressDialog.show(SerialListActivity.this, "Загрузка...",
				  "Пожалуйста ждите.... ", true, false);		
		new AsyncExecution().execute(arg2);
	}

    class AsyncExecution extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... arg0) {
			boolean result = false;
			tsEngine.selectSerial(arg0[0]);
			if (tsEngine.getSeasonCount() != 0) {
				result = true;
			}
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (!result) {
				Toast.makeText(getBaseContext(),
						"Невозможно загрузить сериал",
						Toast.LENGTH_LONG).show();
			}
			else {
				SerialActivity.tsEngine = tsEngine;
				Intent intent = new Intent(SerialListActivity.this, SerialActivity.class);
				startActivity(intent);
				
				
			}
		}
    }	

}
