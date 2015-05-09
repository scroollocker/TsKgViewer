package ru.skytechdev.tskgviewerreborn.activity;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.Serial.SerialsList;
import ru.skytechdev.tskgviewerreborn.categories.Categories;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import ru.skytechdev.tskgviewerreborn.structs.TsCategoryItem;
import ru.skytechdev.tskgviewerreborn.utils.Favorites;
import ru.skytechdev.tskgviewerreborn.utils.RecentSerials;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		ListView MenuList = (ListView)findViewById(R.id.listView1);
		MenuList.setOnItemClickListener(this);		
		
		Categories categories = TsEngine.getInstance().getCategories();
		
		int categoriesLen = categories.getItemCount();
		
		String menuElements[];
		menuElements = new String[categoriesLen];
		
		for (int i = 0; i < categoriesLen; i++) {
			menuElements[i] = categories.getItemById(i).name;
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
    			RecentSerials recent = RecentSerials.getInstance();    				
    			
    			if (recent.getCount() == 0) {
    				Toast.makeText(getBaseContext(),
    						"Список пуст",
    						Toast.LENGTH_LONG).show();
    				return super.onOptionsItemSelected(item);
    			}
    			
				backPressed = 0;
				
				Intent intent = new Intent(MenuActivity.this, RecentSerialsActivity.class);
				startActivity(intent);
				
				return super.onOptionsItemSelected(item);
    		}    
    		case R.id.favorites : {
    			Favorites favorites = Favorites.getInstance();
    			
    			if (favorites.getCount() == 0) {
    				Toast.makeText(getBaseContext(),
    						"Список пуст",
    						Toast.LENGTH_LONG).show();
    				return super.onOptionsItemSelected(item);
    			}
    			
				backPressed = 0;
				Intent intent = new Intent(MenuActivity.this, FavoritesActivity.class);
				startActivity(intent);
				
				return super.onOptionsItemSelected(item);
    		}      		
    		case R.id.search: {
    			/*SearchActivity.tsEngine = tsEngine;
				backPressed = 0;
				Intent intent = new Intent(MenuActivity.this, SearchActivity.class);
				startActivity(intent);
				*/
    			return super.onOptionsItemSelected(item);
    		}
    		case R.id.sett: {
				/*backPressed = 0;
				Intent intent = new Intent(tsEngine.getBaseContext(), SettingsActivity.class);
				startActivity(intent);
				*/
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
			Categories categories = TsEngine.getInstance().getCategories();
			
			TsCategoryItem category = categories.getItemById(arg0[0]);
			
			result = TsEngine.getInstance().loadSerialList(category.value);
			
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
				backPressed = 0;
				Intent intent = new Intent(MenuActivity.this, SerialListActivity.class);
				startActivity(intent);								
			}
			
		}
    }	
	
    class AsyncExecutionMenu extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			//boolean result = tsEngine.initNewEpi();
			return true;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			/*ProgressBar.dismiss();
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
			*/
		}
    }        
    
}
