package ru.skytechdev.tskgviewerreborn.activity;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import ru.skytechdev.tskgviewerreborn.utils.SearchHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends Activity {
	private ProgressDialog ProgressBar;
	
	private SearchHelper searchHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		EditText searchView = (EditText)findViewById(R.id.editText1);
		searchView.setOnKeyListener(new View.OnKeyListener() { 
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
			    if(event.getAction() == KeyEvent.ACTION_DOWN && 
				    (keyCode == KeyEvent.KEYCODE_ENTER)) {
			    		EditText searchView = (EditText)findViewById(R.id.editText1);
			    		ProgressBar = ProgressDialog.show(SearchActivity.this, "Загрузка...",
		    					  "Пожалуйста ждите.... ", true, false);
			    		new AsyncExecutionSearch().execute(searchView.getText().toString());
						return true;
				}
				return false;
			}
		});
		Button searchBtn = (Button)findViewById(R.id.button1); 
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText searchView = (EditText)findViewById(R.id.editText1);
				String searchText = searchView.getText().toString();
				if (searchText.isEmpty()) {
					return;
				}
				ProgressBar = ProgressDialog.show(SearchActivity.this, "Загрузка...",
  					  "Пожалуйста ждите.... ", true, false);
				new AsyncExecutionSearch().execute(searchText);
			}
		});
		
		ListView searchList = (ListView) findViewById(R.id.listView1);
		searchList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ProgressBar =  ProgressDialog.show(SearchActivity.this, "Загрузка...",
	  					  "Пожалуйста ждите.... ", true, false);
				new AsyncExecutionSerial().execute(arg2);				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	class AsyncExecutionSearch extends AsyncTask<String, Void, Boolean> {
		
		public AsyncExecutionSearch() {
			searchHelper = new SearchHelper();
		}
		
		@Override
		protected Boolean doInBackground(String... arg0) {
			boolean result = false;
			int foundCount = searchHelper.search(arg0[0]);
			if (foundCount > 0) {
				result = true;
			}
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (!result) {
				Toast.makeText(getBaseContext(),
						"Ничего не найдено",
						Toast.LENGTH_LONG).show();
			}
			else {
				ListView foundList = (ListView)findViewById(R.id.listView1);	
				
				int foundCount = searchHelper.getItemsFound();
				
				String foundElements[];
				foundElements = new String[foundCount];
				for (int i = 0; i < foundCount; i++) {
					foundElements[i] = searchHelper.getSearchItem(i).value;//tsEngine.getSearchedCaption(i);
				}
				foundList.setAdapter((ListAdapter) new ArrayAdapter<String>(SearchActivity.this,
						   android.R.layout.simple_list_item_1, foundElements));								
			}
		}
		
    }    
	
	class AsyncExecutionSerial extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... arg0) {
			boolean result = false;
			int selId = arg0[0];
			String selUrl = searchHelper.getSearchItem(selId).url;

			result = TsEngine.getInstance().loadSerialInfo(selUrl);
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
				Intent intent = new Intent(SearchActivity.this, SerialActivity.class);
				startActivity(intent);									
			}
		}
		
    } 	

}
