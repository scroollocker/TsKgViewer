package ru.skytechdev.tskgviewerreborn;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.util.Log;

public class SerialInfo {
	private String caption;
	private String img;
	private String url;
	private ArrayList<TSEpisodeItem> seasons = new ArrayList<TSEpisodeItem>();
	private String discription;
	private String base_url = "http://www.ts.kg";
	
	public void clear() {
		seasons.clear();
	}
	
	public String getCaption() {
		return caption;
	}
	public String getUrl() {
		return url;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
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
	
	public void addEpisodes(String cap, ArrayList<TSMenuItem> val) {
		TSEpisodeItem item = new TSEpisodeItem();
		item.caption = cap;
		item.episodes = val;
		seasons.add(item);
	}
	
	public TSEpisodeItem getSerialItem(int id) {
		if (id < 0 || id >= getSeasonCount() || getSeasonCount() == 0) {
			return new TSEpisodeItem();
		}		
		return seasons.get(id);
	}
	
	public String getSeasonCaption(int id) {
		if (id < 0 || id >= getSeasonCount() || getSeasonCount() == 0) {
			return "";
		}		
		return seasons.get(id).caption;
	}
	
	public TSMenuItem getEpisode(int seasonid, int episodeid) {
		if (seasonid < 0 || seasonid >= getSeasonCount() || getSeasonCount() == 0) {
			return new TSMenuItem();
		}		
		if (episodeid < 0 || episodeid >= getEpisodesCount(seasonid) || getEpisodesCount(seasonid) == 0) {
			return new TSMenuItem();
		}
		
		return seasons.get(seasonid).episodes.get(episodeid);
	}
	
	public boolean parseSerialInfo(String url) {
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
				String epiCaption;
				Elements episodes = serialElement.get(i).select("a");
				epiCaption = serialElement.get(i).select("h3").text();
				ArrayList<TSMenuItem> epiItem = new ArrayList<TSMenuItem>(); 
				for (int j = 0; j < episodes.size(); j++) {
					TSMenuItem item = new TSMenuItem();
					String link = episodes.get(j).attr("href");
					if (link.substring(0, 1).equals("/")) {
						link = base_url + link;
					}
					item.url = link;
					item.value = episodes.get(j).text();
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
