package ru.skytechdev.tskgviewerreborn;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
	
	private String getAjax(String url) {
		String result = "";
		
		String id = getEpisodeId(url);
		
		if (id.isEmpty()) {
			return result;
		}
		
		HashMap<String, String> postData = new HashMap<String, String>();
		
		postData.put("action", "getEpisodeJSON");
		postData.put("episode", id);
		
		Document doc = HttpWrapper.postHttpAjax("http://ts.kg/ajax", postData);
		
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
		
		result = (String) jsObj.get("file");
		
		return result;
		
	}
	
	public String makeVideoUrl(String url) {
		if (url.isEmpty()) {
			return "";
		}
		
		String link = getAjax(url);		
		
		if (link.isEmpty()) {
			link = url.replace("http://www.ts.kg/" , "http://data2.ts.kg/video/") + ".mp4";
		}
		return link;
		
	}
	
}
