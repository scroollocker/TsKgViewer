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
		
		Element dlButton = doc.select("a#dl_button").first();
		
		if (dlButton != null) {
			if (dlButton.hasAttr("href")) {
				String [] urlSplit = dlButton.attr("href").split("/");
				result = urlSplit[urlSplit.length-1];
			}
		}
		
		return result;		
	}
	
	private HashMap<String,String> getEpisodAjaxResponse(String id, String url) {
		HashMap<String,String> result = new HashMap<String,String>();
		
		if (id.isEmpty()) {
			return result;
		}
		
		HashMap<String, String> postData = new HashMap<String, String>();
		
		postData.put("action", "getEpisodeJSON");
		postData.put("episode", id);
		
		Document doc = HttpWrapper.postHttpAjax("http://www.ts.kg/ajax", postData, url);
		
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
		
		result = (HashMap<String,String>) jsObj;
		
		return result;		
	}
	
	private String getAjax(String url) {
		String result = "";
		
		String id = getEpisodeId(url);
		
		if (id.isEmpty()) {
			return result;
		}
		
		HashMap<String, String> dataResp = getEpisodAjaxResponse(id, url);
		
		result = dataResp.get("file");
		
		return result;
		
	}
	
	public String makeVideoUrl(String url) {
		if (url.isEmpty()) {
			return "";
		}
		
		String defLink = url.replace("http://www.ts.kg/" , "http://data2.ts.kg/video/") + ".mp4";
		
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
		String id = getEpiID(url);
		HashMap<String, String> dataResp = getEpisodAjaxResponse(id,url);
		
		if (dataResp == null) {
			return result;
		}
		
		result = dataResp.get("sname")+" | Сезон: "+dataResp.get("season")+" | эпизод: "+dataResp.get("alias");
		
		return result;
	}
	
	
}
