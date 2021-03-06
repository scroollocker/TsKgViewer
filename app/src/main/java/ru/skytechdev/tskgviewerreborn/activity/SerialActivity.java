package ru.skytechdev.tskgviewerreborn.activity;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import ru.skytechdev.tskgviewerreborn.serial.SerialInfo;
import ru.skytechdev.tskgviewerreborn.utils.Favorites;
import ru.skytechdev.tskgviewerreborn.utils.ImageManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SerialActivity extends Activity implements OnItemClickListener, OnClickListener {
	private boolean isInFavorites = false;
	private final String LogTag = "SerialActivity::LOG";
	
	@Override
	protected void onStart() {
		super.onStart();	
		SerialInfo serial = TsEngine.getInstance().getSerialInfo();
		Favorites favorites = Favorites.getInstance();
		
		isInFavorites = favorites.isExist(serial.getUrl());
		
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
			
		SerialInfo serial = TsEngine.getInstance().getSerialInfo();
		
		this.setTitle(serial.getCaption());
		
		discriptionView.setText(serial.getDiscription());
		
		new ImageManager().fetchImage(serial.getImg(), coverView, this.getBaseContext());
		
		int seasonCount = serial.getSeasonCount();
		
		if (seasonCount == 0) {
			Log.d(LogTag, "ERR::seasonCount == 0");
			return;
		}
		
		String seasonItem [] = new String[seasonCount];
		String epiCountItem [] = new String[seasonCount];
		int epiCount;
		
		for (int i = 0; i < seasonCount; i++) {
			seasonItem[i] = serial.getSeasonCaption(i);
			epiCount = serial.getEpisodesCount(i);
			epiCountItem[i] = "Количество эпизодов: " + String.valueOf(epiCount);
		}
		
		seasonView.setAdapter((ListAdapter) new ArrayAdapter<String>(SerialActivity.this,
				   android.R.layout.simple_list_item_1, seasonItem));

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
	public void onItemClick(AdapterView<?> notUse1, View notUse2, int itemId, long notUse3) {
		Intent intent = new Intent(SerialActivity.this, EpisodActivity.class);
		intent.putExtra(TsEngine.TS_SEASON_EXTRA_STR, itemId);
		startActivity(intent);
	}

	@Override
	public void onClick(View notUse) {
		ImageView starImg = (ImageView)findViewById(R.id.fv_img);
		TextView starText = (TextView)findViewById(R.id.fv_caption);
		
		Favorites favorites = Favorites.getInstance();
		SerialInfo serial = TsEngine.getInstance().getSerialInfo();
		
		if (isInFavorites) {
			favorites.delete(serial.getCaption(), serial.getUrl(), serial.getImg());			
			starImg.setImageResource(android.R.drawable.btn_star_big_off);
			starText.setText("Добавить в избранное");
			isInFavorites = false;
		}
		else {			
			favorites.add(serial.getCaption(), serial.getUrl(), serial.getImg());
			starImg.setImageResource(android.R.drawable.btn_star_big_on);
			starText.setText("Убрать из избранного");
			isInFavorites = true;
		}
		
		favorites.saveToSettings();
	}


}
