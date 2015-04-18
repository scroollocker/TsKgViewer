package ru.skytechdev.tskgviewerreborn;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

public class VideoUrlGenerator {
	
	private String getEpisodeId(String url) {
		Document doc = HttpWrapper.getHttpDoc(url);
		String result = "";
		if (doc == null) {
			return result;
		}
		
		Element episodeItem = doc.select("input#episode_id_input").first();
		
		if (episodeItem != null) {
			result = episodeItem.attr("value");			
		}
		
		return result;		
	}
	
	private Object getEpisodAjaxResponse(String id, String url) {
		Object result = null;
		
		if (id.isEmpty()) {
			return result;
		}
		
		HashMap<String, String> postData = new HashMap<String, String>();
		
		postData.put("episode", id);
		
		Document doc = HttpWrapper.postHttpAjax("http://www.ts.kg/show/episode/episode.json", postData, url);
		
		if (doc == null) {
			return result;
		}
		
		String respBody = doc.text();
		
		JSONParser parser = new JSONParser();
		
		JSONObject jsObj = null;
		
		
		try {
			jsObj = (JSONObject) parser.parse(respBody);
		} catch (ParseException e) {
			jsObj = null;
		}
		
		if (jsObj == null) {
			return result;
		}		
		
		result = jsObj;
		
		return result;		
	}
	
	private String getAjax(String url) {
		String result = "";
		
		String id = getEpisodeId(url);
		
		if (id.isEmpty()) {
			return result;
		}
		
		JSONObject dataResp = (JSONObject)getEpisodAjaxResponse(id, url);
		
		if (dataResp == null) {
			return result;
		}
		
		JSONObject file = (JSONObject)dataResp.get("file");
		
		if (file == null) {
			return result;
		}
		
		HashMap<String,String> fileMap = (HashMap<String,String>)file;
		
		result = fileMap.get("mp4");
		
		return result;
		
	}
	
	public String makeVideoUrl(String url) {
		if (url.isEmpty()) {
			return "";
		}
		
		String defLink = url.replace("http://www.ts.kg/" , "http://data1.ts.kg/video/") + ".mp4";
		
		String link = getAjax(url);		
		
		if (link == null) {
			link = defLink;
		}
		else if (link.isEmpty()) {
			link = defLink;
		}
		
		Log.d("finalyUrl", link);
		
		return link;
		
	}
	
	public String getEpiID(String url) {
		return getEpisodeId(url);
	}
	
	public String makeSerialPrev(String url) {
		
		String result = "";
		result = "Загрузка сериала...";
		
		return result;
	}
	
	
}
