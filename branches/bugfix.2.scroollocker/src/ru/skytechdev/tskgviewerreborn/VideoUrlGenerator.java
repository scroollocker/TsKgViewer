package ru.skytechdev.tskgviewerreborn;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class VideoUrlGenerator {
	
	private String getEpisodeId(String url) {
		Document doc = HttpWrapper.getHttpDoc(url);
		String result = "";
		if (doc == null) {
			return result;
		}
		
		Elements scripts = doc.select("script");
		
		for (int i = 0; i < scripts.size(); i++ ) {
			if (scripts.get(i).hasAttr("src")) {
				String src = scripts.get(i).attr("src");
				if (src.indexOf("episode3") != -1) {
					result = src.substring(29);
					break;
				}
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
	/*
	private void setSerialServerPrefix() {
		String epiId = "http://www.ts.kg/js/episode/1";
		
		String server = "data2";
		
		Document doc = HttpWrapper.getHttpDoc(epiId);
		if (doc != null) {
			String temp;
			String body = doc.text();
			int posS = -1;
			int posE = -1;
			posS = body.indexOf("server: \"");
			if (posS != -1) {
				posS += 9;
				posE = body.indexOf("\",", posS);
				if (posE != -1) {
					temp = body.substring(posS, posE);
					if (!temp.isEmpty()) {
						server = temp;
					}
				}
			}
		}
		//currentServer = server;		
	}
	*/
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
