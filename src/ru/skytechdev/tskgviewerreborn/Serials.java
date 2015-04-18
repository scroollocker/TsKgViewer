package ru.skytechdev.tskgviewerreborn;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Serials {
	private ArrayList<TSItem> serialList = new ArrayList<TSItem>();
	private String base_url = "http://www.ts.kg";
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
		
		String prevUrl = "";
		String nextUrl = "";
		
		do {
			Elements serialElements = doc.select("div.shows").select("a");
			Elements imgElements = doc.select("div.shows").select("img");
			Element next_page = doc.select("li.next").select("a").first();
			if (serialElements.size() > 0) {
				for (int j = 0; j < serialElements.size(); j++) {
					String link = serialElements.get(j).attr("href");
					String caption = serialElements.get(j).text();
					String img = imgElements.get(j).attr("src");
					TSItem item = new TSItem();
					
					if (link.substring(0,1).equals("/")) {
						link = base_url + link;
					}
					
					if (img.substring(0,1).equals("/")) {
						img = base_url + link;
					}
					
					item.imgurl = img;
					item.url = link;
					item.value = caption;
					addSerial(item);
				}
			}
						
			
			if (next_page == null) {
				break;
			}
			else {
				nextUrl = base_url+next_page.attr("href");
				if (nextUrl.equals(prevUrl)) {
					break;
				}
				else {
					prevUrl = nextUrl;
					doc = HttpWrapper.getHttpDoc(nextUrl);
				}
			}			
			
		} while(true);
		
		if (serialList.size() > 0) {
			result = true;
		}
		
		return result;
	}
}
