package ru.skytechdev.tskgviewerreborn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MenuActivity extends Activity implements OnItemClickListener {
	public static TSEngine tsEngine = null;	
	private ProgressDialog ProgressBar;
	private int  backPressed = 0;

	@Override
	public void onBackPressed() {
		if (backPressed == 0) {
			Toast.makeText(getBaseContext(),
					"Нажмите еще раз кнопку назад для выхода",
					Toast.LENGTH_LONG).show();
			backPressed++;
		}
		else {
			finish();
			MainActivity.isExit = true;
		}
	}	
	
	@Override
	public void onStart() {
		super.onStart();
		tsEngine.clear();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		ListView MenuList = (ListView)findViewById(R.id.listView1);
		MenuList.setOnItemClickListener(this);		
		
		String menuElements[];
		menuElements = new String[tsEngine.getMenuItemCount()];
		for (int i = 0; i < tsEngine.getMenuItemCount(); i++) {
			menuElements[i] = tsEngine.getMenuItemCaption(i);
		}
		
		MenuList.setAdapter((ListAdapter) new ArrayAdapter<String>(MenuActivity.this,
				   android.R.layout.simple_list_item_1, menuElements));			
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigate, menu);
        return super.onCreateOptionsMenu(menu);
    }	
	
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.wnew : {
    			ProgressBar = ProgressDialog.show(MenuActivity.this, "Загрузка....",
    					  "Пожалуйста ждите...", true, false);
    			new AsyncExecutionMenu().execute();
    			return super.onOptionsItemSelected(item);
    		}
    		case R.id.lastsee : {
    			if (tsEngine.getLastSeenCount() == 0) {
    				Toast.makeText(getBaseContext(),
    						"Список пуст",
    						Toast.LENGTH_LONG).show();
    				return super.onOptionsItemSelected(item);
    			}
    			LastSeenActivity.tsEngine = tsEngine;
				backPressed = 0;
				Intent intent = new Intent(MenuActivity.this, LastSeenActivity.class);
				startActivity(intent);
				return super.onOptionsItemSelected(item);
    		}    
    		case R.id.favorites : {
    			if (tsEngine.getFavoritesCount() == 0) {
    				Toast.makeText(getBaseContext(),
    						"Список пуст",
    						Toast.LENGTH_LONG).show();
    				return super.onOptionsItemSelected(item);
    			}
    			FavoritesActivity.tsEngine = tsEngine;
				backPressed = 0;
				Intent intent = new Intent(MenuActivity.this, FavoritesActivity.class);
				startActivity(intent);
				return super.onOptionsItemSelected(item);
    		}      		
    		case R.id.search: {
    			SearchActivity.tsEngine = tsEngine;
				backPressed = 0;
				Intent intent = new Intent(MenuActivity.this, SearchActivity.class);
				startActivity(intent);
    			return super.onOptionsItemSelected(item);
    		}
    		case R.id.sett: {
				backPressed = 0;
				Intent intent = new Intent(tsEngine.getBaseContext(), SettingsActivity.class);
				startActivity(intent);
    			return super.onOptionsItemSelected(item);
    		}    		
    		case R.id.about: {
				backPressed = 0;
				Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
				startActivity(intent);
    			return super.onOptionsItemSelected(item);
    		}
    	}
		return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProgressBar = ProgressDialog.show(MenuActivity.this, "Загрузка...",
				  "Пожалуйста ждите...", true, false);		
		new AsyncExecution().execute(arg2);
	}

    class AsyncExecution extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... arg0) {
			boolean result = false;
			tsEngine.selectCategory(arg0[0]);
			if (tsEngine.getSerialsCount() != 0) {
				result = true;
			}
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (!result) {
				Toast.makeText(getBaseContext(),
						"Не удалось загрузить список сериалов",
						Toast.LENGTH_LONG).show();
			}
			else {
				SerialListActivity.tsEngine = tsEngine;
				backPressed = 0;
				Intent intent = new Intent(MenuActivity.this, SerialListActivity.class);
				startActivity(intent);								
			}
		}
    }	
	
    class AsyncExecutionMenu extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			boolean result = tsEngine.initNewEpi();
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (!result) {
				Toast.makeText(getBaseContext(),
						"Неудалось загрузить список новых эпизодов",
						Toast.LENGTH_LONG).show();
			}
			else {
				NewEpiActivity.tsEngine = tsEngine;
				backPressed = 0;
				Intent intent = new Intent(MenuActivity.this, NewEpiActivity.class);
				startActivity(intent);								
			}
		}
    }        
    
}
