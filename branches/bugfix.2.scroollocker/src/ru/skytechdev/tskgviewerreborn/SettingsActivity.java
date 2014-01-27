package ru.skytechdev.tskgviewerreborn;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.app.Activity;
import android.view.Menu;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.layout.layout_settings);
	}

}
