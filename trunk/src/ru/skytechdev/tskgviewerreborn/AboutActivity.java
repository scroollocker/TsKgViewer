package ru.skytechdev.tskgviewerreborn;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		TextView link = (TextView) findViewById(R.id.textView4);
		ImageView logo = (ImageView) findViewById(R.id.ls_cover);
		link.setOnClickListener(this);
		logo.setOnClickListener(this);
		TextView fb = (TextView)findViewById(R.id.textView5);
		fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String developURL = "https://www.facebook.com/SkytechDev";
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(developURL));
				startActivity(intent);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onClick(View arg0) {
		String developURL = "http://skytechdev.ru";
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(developURL));
		startActivity(intent);
	}

}
