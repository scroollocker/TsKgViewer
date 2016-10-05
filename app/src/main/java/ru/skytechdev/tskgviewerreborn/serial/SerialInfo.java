package ru.skytechdev.tskgviewerreborn.serial;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ru.skytechdev.tskgviewerreborn.structs.TsEpisodeItem;
import ru.skytechdev.tskgviewerreborn.structs.TsSeasonItem;
import ru.skytechdev.tskgviewerreborn.utils.HttpWrapper;
import ru.skytechdev.tskgviewerreborn.utils.TsUtils;
import android.util.Log;

public class SerialInfo {
		
	private String caption;
	private String img;
	private String url;
	private ArrayList<TsSeasonItem> seasons = new ArrayList<TsSeasonItem>();
	private String discription;
	
	public void clear() {
		seasons.clear();
	}
	
	public String getCaption() {
		return caption;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getImg() {
		return img;
	}
	
	public String getDiscription() {
		return discription;
	}	
	
	public int getSeasonCount() {
		return seasons.size();
	}
	
	public int getEpisodesCount(int seasonid) {		
		if (seasonid < 0 || seasonid >= getSeasonCount() || getSeasonCount() == 0) {
			return 0;
		}
		return seasons.get(seasonid).episodes.size();
	}
	
	private void addEpisodes(String cap, ArrayList<TsEpisodeItem> val) {
		TsSeasonItem item = new TsSeasonItem();
		item.caption = cap;
		item.episodes = val;
		seasons.add(item);
	}
	
	public TsSeasonItem getSerialItem(int id) {
		if (id < 0 || id >= getSeasonCount() || getSeasonCount() == 0) {
			return new TsSeasonItem();
		}		
		return seasons.get(id);
	}
	
	public String getSeasonCaption(int id) {
		if (id < 0 || id >= getSeasonCount() || getSeasonCount() == 0) {
			return "";
		}		
		return seasons.get(id).caption;
	}
	
	public TsEpisodeItem getEpisode(int seasonid, int episodeid) {
		if (seasonid < 0 || seasonid >= getSeasonCount() || getSeasonCount() == 0) {
			return new TsEpisodeItem();
		}		
		if (episodeid < 0 || episodeid >= getEpisodesCount(seasonid) || getEpisodesCount(seasonid) == 0) {
			return new TsEpisodeItem();
		}
		
		return seasons.get(seasonid).episodes.get(episodeid);
	}
	
	public boolean loadSerialInfo(String url) {
		boolean result = false;
		
		result = parseSerialInfo(url);
		
		return result;
	}
	
	private boolean parseSerialInfo(String url) {
		boolean result = false;
		Document doc = null;
		
		doc = HttpWrapper.getHttpDoc(url);
		if (doc == null) {
			return result;
		}
					
		if (getSeasonCount() > 0) {
			seasons.clear();
		}
		
		Elements discriptionRes = doc.select("meta");
		
		for (int i = 0; i < discriptionRes.size(); i++) {
			String property = discriptionRes.get(i).attr("property");
			Log.d("property", property);
			if (property.indexOf("description") != -1) {
				discription = discriptionRes.get(i).attr("content");
			}
			if (property.indexOf("image") != -1) {
				img = discriptionRes.get(i).attr("content");
			}
			if (property.indexOf("title") != -1) {
				caption = discriptionRes.get(i).attr("content");
			}
			
		}
		
		Elements serialElement = doc.select("section");
		
		if (serialElement.size() > 0) {			
			for (int i = 0; i < serialElement.size(); i++) {
				if (serialElement.get(i).hasClass("clearfix")) {
					continue;
				}
				String epiCaption;
				Elements episodes = serialElement.get(i).select("a");
				epiCaption = serialElement.get(i).select("h3").text();
				ArrayList<TsEpisodeItem> epiItem = new ArrayList<TsEpisodeItem>(); 
				for (int j = 0; j < episodes.size(); j++) {
					TsEpisodeItem item = new TsEpisodeItem();
					String link = episodes.get(j).attr("href");
					if (link.substring(0, 1).equals("/")) {
						link = TsUtils.getBasePath() + link;
					}
					item.url = link;
					item.caption = episodes.get(j).text();
					epiItem.add(item);
				}
				if (epiItem.size() > 0) {
					addEpisodes(epiCaption, epiItem);
				}
			}
			if (getSeasonCount() > 0) {
				result = true;
				this.url = url;  
			}
		}
				
		
		return result;
	}
	
}
