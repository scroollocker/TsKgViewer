package ru.skytechdev.tskgviewerreborn.activity;

import java.util.ArrayList;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.categories.Categories;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import ru.skytechdev.tskgviewerreborn.structs.TsCategoryItem;
import ru.skytechdev.tskgviewerreborn.structs.TsRecentAddItem;
import ru.skytechdev.tskgviewerreborn.utils.Favorites;
import ru.skytechdev.tskgviewerreborn.utils.RecentAddHelper;
import ru.skytechdev.tskgviewerreborn.utils.RecentSerials;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
	private int  backPressCount = 0;
	private final String LogTag = "MenuActivity::LOG";
	
	@Override
	public void onBackPressed() {
		if (backPressCount == 0) {
			Toast.makeText(getBaseContext(),
					"Нажмите еще раз кнопку назад для выхода",
					Toast.LENGTH_LONG).show();
			backPressCount++;
		}
		else {
			setResult(RESULT_OK);
			finish();
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
		
		if (categoriesLen == 0) {
			Log.d(LogTag, "ERR::categoriesLen == 0");
			return;
		}
		
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
    			new RecentAddLoaderTask().execute();
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
    			
				backPressCount = 0;				
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
    			
    			backPressCount = 0;
				Intent intent = new Intent(MenuActivity.this, FavoritesActivity.class);
				startActivity(intent);
				
				return super.onOptionsItemSelected(item);
    		}      		
    		case R.id.search: {
				backPressCount = 0;
				Intent intent = new Intent(MenuActivity.this, SearchActivity.class);
				startActivity(intent);
				
    			return super.onOptionsItemSelected(item);
    		}
    		case R.id.sett: {
				backPressCount = 0;
				Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
				startActivity(intent);
				
    			return super.onOptionsItemSelected(item);
    		}    		
    		case R.id.about: {
				backPressCount = 0;
				Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
				startActivity(intent);
				
    			return super.onOptionsItemSelected(item);
    		}
    	}
		return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> notUse1, View notUse2, int itemId, long notUse3) {
		ProgressBar = ProgressDialog.show(MenuActivity.this, "Загрузка...",
				  "Пожалуйста ждите...", true, false);		
		new SerialListLoaderTask().execute(itemId);
	}

    class SerialListLoaderTask extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... itemId) {
			boolean result = false;
			Categories categories = TsEngine.getInstance().getCategories();
			
			TsCategoryItem category = categories.getItemById(itemId[0]);
			
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
				backPressCount = 0;
				Intent intent = new Intent(MenuActivity.this, SerialListActivity.class);
				startActivity(intent);								
			}
			
		}
    }	
	
    class RecentAddLoaderTask extends AsyncTask<Void, Void, Boolean> {

    	private RecentAddHelper recentAdd;
    	
    	public RecentAddLoaderTask() {
    		recentAdd = new RecentAddHelper();
    	}
    	
		@Override
		protected Boolean doInBackground(Void... notUse) {
			boolean result = recentAdd.parseNewEpi();
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
				backPressCount = 0;
				Intent intent = new Intent(MenuActivity.this, RecentAddActivity.class);
				
				ArrayList<TsRecentAddItem> recentAddItems = recentAdd.getAllItems();
				
				intent.putExtra(TsEngine.TS_RECENTADD_EXTRA_STR, recentAddItems);
				
				startActivity(intent);								
			}
			
		}
    }        
    
}
