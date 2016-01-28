package ru.skytechdev.tskgviewerreborn.activity;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.categories.Categories;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			finish();
		}
		
	}
	
	private Button btnRep;
	private TextView tvNotify;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnRep = (Button)findViewById(R.id.button1);  
        tvNotify = (TextView)findViewById(R.id.ls_caption);
		btnRep.setVisibility(View.INVISIBLE);
		btnRep.setOnClickListener(this);
		
		TsEngine.getInstance().initEngine(MainActivity.this);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	new LoadCategoriesTask().execute();
    }
    
    
    class LoadCategoriesTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... notUsed) {
			boolean result = false;
						
			Categories categoryies = TsEngine.getInstance().getCategories();
			
			if (categoryies.getItemCount() > 0) {
				result = categoryies.loadCategories(true);
			}
			else {
				result = categoryies.loadCategories(false);
			}
			
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				Toast.makeText(getBaseContext(),
						"Неудалось загрузить список категорий",
						Toast.LENGTH_LONG).show();
				tvNotify.setVisibility(View.INVISIBLE);
				btnRep.setVisibility(View.VISIBLE);
			}
			else {
				Intent intent = new Intent(MainActivity.this, MenuActivity.class);
				startActivityForResult(intent,0);			
			}
		}
    }

	@Override
	public void onClick(View notUsed) {
		new LoadCategoriesTask().execute();
		btnRep.setVisibility(View.INVISIBLE);
		tvNotify.setVisibility(View.VISIBLE);
	}
}
