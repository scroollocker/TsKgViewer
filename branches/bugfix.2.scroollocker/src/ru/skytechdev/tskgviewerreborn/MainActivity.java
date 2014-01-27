package ru.skytechdev.tskgviewerreborn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private static TSEngine tsEngine = new TSEngine();
	
	public static boolean isExit = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btnRep = (Button)findViewById(R.id.button1);        
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
			tsEngine.init(getBaseContext());			
			result = tsEngine.getIsMenuLoaded();			
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				Toast.makeText(getBaseContext(),
						"Ќе удалось загрузить список категорий",
						Toast.LENGTH_LONG).show();
				Button btnRep = (Button)findViewById(R.id.button1);
				btnRep.setVisibility(View.VISIBLE);
			}
			else {
				MenuActivity.tsEngine = tsEngine;
				Intent intent = new Intent(MainActivity.this, MenuActivity.class);
				startActivity(intent);				
			}
		}
    }

	@Override
	public void onClick(View arg0) {
		new AsyncExecution().execute();
		Button btnRep = (Button)findViewById(R.id.button1);
		btnRep.setVisibility(View.INVISIBLE);
	}
}
