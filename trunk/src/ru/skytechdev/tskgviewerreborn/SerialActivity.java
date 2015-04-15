package ru.skytechdev.tskgviewerreborn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SerialActivity extends Activity implements OnItemClickListener, OnClickListener {

	public static TSEngine tsEngine = null;	
	private ProgressDialog ProgressBar;
	private TSItem thisSerial = new TSItem();
	private boolean isInFavorites = false;
	
	
	@Override
	protected void onStart() {
		super.onStart();	
		isInFavorites = tsEngine.isInFavorites(thisSerial.value, thisSerial.url, thisSerial.imgurl);
		ImageView starImg = (ImageView)findViewById(R.id.fv_img);
		TextView starText = (TextView)findViewById(R.id.fv_caption);
		
		if (isInFavorites) {
			starImg.setImageResource(android.R.drawable.btn_star_big_on);
			starText.setText("Убрать из избранного");
		}
		else {
			starImg.setImageResource(android.R.drawable.btn_star_big_off);
			starText.setText("Добавить в избранное");
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serial);
		ListView seasonView = (ListView)findViewById(R.id.listView1);
		seasonView.setOnItemClickListener(this);
		
		TextView discriptionView = (TextView)findViewById(R.id.ls_caption);
		ImageView coverView = (ImageView)findViewById(R.id.ls_cover);
			
		this.setTitle(tsEngine.getSerialCaption());
		
		discriptionView.setText(tsEngine.getSerialDiscription());
		
		new ImageManager().fetchImage(tsEngine.getSerialImg(), coverView, tsEngine.getBaseContext());
		
		String seasonItem [] = new String[tsEngine.getSeasonCount()];
		String epiCountItem [] = new String[tsEngine.getSeasonCount()];
		Integer epiCount;
		
		for (int i = 0; i < tsEngine.getSeasonCount(); i++) {
			seasonItem[i] = tsEngine.getSeasonCaption(i);
			epiCount = tsEngine.getEpisodesCount(i);
			epiCountItem[i] = "Количество эпизодов: " + epiCount.toString();
		}
		
		seasonView.setAdapter((ListAdapter) new ArrayAdapter<String>(SerialActivity.this,
				   android.R.layout.simple_list_item_1, seasonItem));
		
		thisSerial.imgurl = tsEngine.getSerialImg();
		thisSerial.url = tsEngine.getSerialUrl();
		thisSerial.value = tsEngine.getSerialCaption();

		ImageView starImg = (ImageView)findViewById(R.id.fv_img);
		TextView starText = (TextView)findViewById(R.id.fv_caption);
		
		starImg.setOnClickListener(this);
		starText.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProgressBar = ProgressDialog.show(SerialActivity.this, "Загрузка...",
				  "Пожалуйста ждите.... ", true, false);		
		new AsyncExecution().execute(arg2);
	}

    class AsyncExecution extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... arg0) {
			boolean result = false;
			tsEngine.selectSeason(arg0[0]);			
			if (tsEngine.getEpisodesCount(arg0[0]) != 0) {
				result = true;
			}
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (!result) {
				Toast.makeText(getBaseContext(),
						"Не удалось загрузить список эпизодов",
						Toast.LENGTH_LONG).show();
			}
			else {
				EpisodActivity.tsEngine = tsEngine;
				Intent intent = new Intent(SerialActivity.this, EpisodActivity.class);
				startActivity(intent);	
			}
		}
    }

	@Override
	public void onClick(View arg0) {
		ImageView starImg = (ImageView)findViewById(R.id.fv_img);
		TextView starText = (TextView)findViewById(R.id.fv_caption);
		if (isInFavorites) {
			tsEngine.delFromFavorites(thisSerial.value, thisSerial.url, thisSerial.imgurl);
			starImg.setImageResource(android.R.drawable.btn_star_big_off);
			starText.setText("Добавить в избранное");
			isInFavorites = false;
		}
		else {			
			tsEngine.addToFavorites(thisSerial.value, thisSerial.url, thisSerial.imgurl);
			starImg.setImageResource(android.R.drawable.btn_star_big_on);
			starText.setText("Убрать из избранного");
			isInFavorites = true;
		}
	}


}
