package ru.skytechdev.tskgviewerreborn.serial;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.skytechdev.tskgviewerreborn.categories.CategoryHelper;
import ru.skytechdev.tskgviewerreborn.structs.TsSerialItem;
import ru.skytechdev.tskgviewerreborn.structs.TsSortEnum;
import ru.skytechdev.tskgviewerreborn.utils.HttpWrapper;
import ru.skytechdev.tskgviewerreborn.utils.TsUtils;


public class SerialsList {
	
	private ArrayList<TsSerialItem> serialList = new ArrayList<TsSerialItem>();
	private String lastCatId = "";
	
	public void clear() {
		serialList.clear();
	}
	
	public TsSerialItem getSerialById(int id) {
		if (id < 0 || id >= serialList.size() || serialList.size() == 0) {
			return null;
		}
		return serialList.get(id);
	}
	
	private void addSerial(TsSerialItem val) {
		serialList.add(val);
	}
	
	public int getSerialsCount() {
		return serialList.size();
	}
	
	public boolean loadSerialsList(String catId, TsSortEnum sort, String genre, boolean cache) {
		boolean result = false;
		if (cache) {
			if (!lastCatId.equals(catId) || getSerialsCount() == 0) {
				result = parseSerials(catId, sort, genre);
			}
		}
		else {
			result = parseSerials(catId, sort, genre);
		}
		
		return result;	
	}
	
	public boolean loadSerialsList(String catId, TsSortEnum sort, boolean cache) {
		return loadSerialsList(catId,sort,"0",cache);	
	}
	
	public boolean loadSerialsList(String catId, boolean cache) {
		return loadSerialsList(catId,TsSortEnum.SORT_A,"0",cache);
	}
	
	private boolean parseSerials(String catId, TsSortEnum sort, String genre) {
		boolean result = false;
		Document doc = null;
		
		String url = CategoryHelper.getCategoryLink(catId, sort, genre);
		
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
			Element next_page = doc.select("a.next-page").first();
			if (serialElements.size() > 0) {
				for (int j = 0; j < serialElements.size(); j++) {
					String link = serialElements.get(j).attr("href");
					String caption = serialElements.get(j).text();
					String img = imgElements.get(j).attr("src");
					TsSerialItem item = new TsSerialItem();
					
					if (link.substring(0,1).equals("/")) {
						link = TsUtils.getBasePath() + link;
					}
					
					if (img.substring(0,1).equals("/")) {
						img = TsUtils.getBasePath() + link;
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
				nextUrl = TsUtils.getBasePath()+next_page.attr("href");
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
			lastCatId = catId;
		}
		
		return result;
	}
}
