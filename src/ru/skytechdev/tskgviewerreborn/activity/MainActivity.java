package ru.skytechdev.tskgviewerreborn.activity;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.categories.Categories;
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
	
	public static boolean isExit = false;
	
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
		isExit = false;
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	if (isExit) {
    		finish();
    		return;
    	}
    	new AsyncExecution().execute();
    }
    
    
    class AsyncExecution extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			boolean result = false;
			/*tsEngine.init(getBaseContext());			
			result = tsEngine.isMenuLoaded();
			*/
			
			Categories categoryies = Categories.getInstance();
			
			result = categoryies.loadCategories(false);
			
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
				startActivity(intent);				
			}
		}
    }

	@Override
	public void onClick(View arg0) {
		new AsyncExecution().execute();
		btnRep.setVisibility(View.INVISIBLE);
		tvNotify.setVisibility(View.VISIBLE);
	}
}
