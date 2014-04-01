package ru.skytechdev.tskgviewerreborn;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class SerialInfo {
	private String caption;
	private String img;
	private String url;
	private ArrayList<TSEpisodeItem> seasons = new ArrayList<TSEpisodeItem>();
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
			if (discriptionRes.attr("name").compareTo("description") == 0) {
				discription = discriptionRes.attr("content");
			}
		}
		
		img = doc.select("img.serial_cover").attr("src");		
		caption = doc.select("title").text();
		
		if (!caption.isEmpty()) {
			caption = caption.replace("TS.KG - ", "");
		}
		
		Elements serialElement = doc.select("div.episodes").select("ul");
		
		if (serialElement.size() > 0) {			
			for (int i = 0; i < serialElement.size(); i++) {
				String epiCaption;
				Elements episodes = serialElement.get(i).select("a");
				epiCaption = serialElement.get(i).select("h4").text();
				ArrayList<TSMenuItem> epiItem = new ArrayList<TSMenuItem>(); 
				for (int j = 0; j < episodes.size(); j++) {
					TSMenuItem item = new TSMenuItem();
					item.url = episodes.get(j).attr("href");
					item.value = episodes.get(j).text();
					epiItem.add(item);
				}
				addEpisodes(epiCaption, epiItem);
			}
			result = true;
			this.url = url;  
		}
				
		
		return result;
	}
	
}
