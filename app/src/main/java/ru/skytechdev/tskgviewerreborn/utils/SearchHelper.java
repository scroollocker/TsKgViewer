package ru.skytechdev.tskgviewerreborn.utils;

import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import ru.skytechdev.tskgviewerreborn.structs.TsSerialItem;

public class SearchHelper {
	private ArrayList<TsSerialItem> found = new ArrayList<TsSerialItem>();
	
	public void clear() {
		found.clear();
	}
	
	public int getItemsFound() {
		return found.size();
	}
	
	
	public TsSerialItem getSearchItem(int id) {
		if (id < 0 || id >= getItemsFound() || getItemsFound() == 0) {
			return new TsSerialItem();
		}
		return found.get(id);
	}
	
	public int search(String text) {
		if (text.isEmpty()) {
			return 0;
		}
				
		clear();
		
		text = text.trim();
		text = text.toLowerCase();

		Document doc = HttpWrapper.getHttpDoc("http://www.ts.kg/show/search/data.json");

		if (doc == null) {
			Log.e("SearchHelper::search()","document empty");
			return 0;
		}

		String jsonBody = doc.text();

		JSONParser parser = new JSONParser();

		JSONArray jsonArray = null;

		try {
			jsonArray = (JSONArray) parser.parse(jsonBody);
		} catch (ParseException e) {
			Log.e("SearchHelper::search()",e.getMessage());
			jsonArray = null;
		}

		if (jsonArray == null) {
			return 0;
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = (JSONObject)jsonArray.get(i);
			String title = (String)obj.get("title");
			if (title.toLowerCase().indexOf(text) >= 0) {
				TsSerialItem item = new TsSerialItem();
				item.imgurl = "";
				item.url = (String)obj.get("url");
				item.value = title;
				found.add(item);
			}
		}
		return getItemsFound();
	}
	
}
