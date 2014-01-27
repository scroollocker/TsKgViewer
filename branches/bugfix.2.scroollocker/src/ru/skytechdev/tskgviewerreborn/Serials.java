package ru.skytechdev.tskgviewerreborn;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Serials {
	private ArrayList<TSItem> serialList = new ArrayList<TSItem>();
	
	public void clear() {
		serialList.clear();
	}
	
	public TSItem getSerialById(int id) {
		if (id < 0 || id >= serialList.size() || serialList.size() == 0) {
			return null;
		}
		return serialList.get(id);
	}
	
	public void addSerial(TSItem val) {
		serialList.add(val);
	}
	
	public int getSerialsCount() {
		return serialList.size();
	}
	
	public boolean parseSerialsByUrl(String url) {
		boolean result = false;
		Document doc = null;
		
		doc = HttpWrapper.getHttpDoc(url);
		if (doc == null) {
			return result;
		}
		
		if (getSerialsCount() > 0) {
			serialList.clear();
		}
		
		Elements serialElements = doc.select("div.categoryblocks").select("a");
		Elements imgElements = doc.select("div.categoryblocks").select("img");
		if (serialElements.size() > 0) {
			for (int i = 0; i < serialElements.size(); i++) {
				String link = serialElements.get(i).attr("href");
				String caption = serialElements.get(i).text();
				String img = imgElements.get(i).attr("src");
				TSItem item = new TSItem();
				item.imgurl = img;
				item.url = link;
				item.value = caption;
				addSerial(item);
			}
			result = true;
		}			
		
		return result;
	}
}
