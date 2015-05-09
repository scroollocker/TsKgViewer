package ru.skytechdev.tskgviewerreborn.engine;

import ru.skytechdev.tskgviewerreborn.Serial.SerialInfo;
import ru.skytechdev.tskgviewerreborn.Serial.SerialsList;
import ru.skytechdev.tskgviewerreborn.categories.Categories;
import ru.skytechdev.tskgviewerreborn.utils.Favorites;
import ru.skytechdev.tskgviewerreborn.utils.RecentEpisodes;
import ru.skytechdev.tskgviewerreborn.utils.RecentSerials;
import android.content.Context;

public class TsEngine {
	private static TsEngine instance;
	private Categories categories;
	private SerialsList serialsList;
	private SerialInfo serialInfo;
	private Context context;
	
	public static String TS_SEASON_EXTRA_STR = "season";
	
	private TsEngine() {}
	
	public void initEngine(Context context) {
		setEngineContext(context);
		
		Favorites.getInstance().setContext(context);
		Favorites.getInstance().loadFromSettings();
		
		RecentEpisodes.getInstance().setContext(context);
		
		RecentSerials.getInstance().setContext(context);
		RecentSerials.getInstance().loadFromSettings();
	}
	
	public void setEngineContext(Context context) {
		this.context = context;
	}
	
	public static TsEngine getInstance() {
		if (instance == null) {
			instance = new TsEngine();
		}
		return instance;
	}
	
	public Categories getCategories() {
		if (categories == null) {
			categories = new Categories();
		}
		return categories;
	}
	
	public SerialsList getSerialList() {
		if (serialsList == null) {
			serialsList = new SerialsList();
		}
		return serialsList;
	}
	
	public boolean loadSerialList(String catId) {
		boolean result = false;
		serialsList = new SerialsList();
		result = serialsList.loadSerialsList(catId, false);
		return result;
	}
	
	public SerialInfo getSerialInfo() {
		if (serialInfo == null) {
			serialInfo = new SerialInfo();
		}
		return serialInfo;
	}
	
	public boolean loadSerialInfo(String url) {
		boolean result = false;
		serialInfo = new SerialInfo();
		result = serialInfo.loadSerialInfo(url);
		if (result) {
			RecentSerials.getInstance().add(serialInfo.getCaption(), serialInfo.getUrl(), serialInfo.getImg());
			RecentSerials.getInstance().saveToSettings();
		}
		return result;
	}
}
