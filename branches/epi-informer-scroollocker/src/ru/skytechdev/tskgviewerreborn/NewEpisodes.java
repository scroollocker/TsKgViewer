package ru.skytechdev.tskgviewerreborn;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NewEpisodes {
	private String baseurl = "http://www.ts.kg/";
	private ArrayList<TSNewEpisodesItem> epiItems = new ArrayList<TSNewEpisodesItem>();
	
	public void clear() {
		epiItems.clear();
	}
	
	public int getEpiCount() {
		return epiItems.size();
	}
	
	public TSNewEpisodesItem getEpiById(int id) {
		if (getEpiCount() == 0 || id >= getEpiCount() || id < 0) {
			return new TSNewEpisodesItem();
		}
		
		return epiItems.get(id);
	}
	
	public boolean parseNewEpi() {
		boolean result = false;
		Document doc = null;
		
		doc = HttpWrapper.getHttpDoc(baseurl);
		if (doc == null) {
			return result;
		}
		
		if (getEpiCount() > 0) {
			epiItems.clear();
		}
		
		Elements epiElements = doc.select("tbody").select("tr");
		
		if (epiElements.size() == 0) {
			return result;
		}
		
		String currDate = "";
		
		for (int i = 0; i < epiElements.size(); i++) {
			Elements dateItem = epiElements.get(i).select("h3");
			if (dateItem.size() > 0) {
				currDate = dateItem.text();
				continue;
			}
			Elements epiItem = epiElements.get(i).select("a");
			if (epiItem.size() > 0) {
				TSNewEpisodesItem epiNode = new TSNewEpisodesItem(); 
				epiNode.caption = epiItem.text();
				epiNode.link = epiItem.attr("href");
				epiNode.date = currDate;
				Elements comItem = epiElements.get(i).select("span");
				if (comItem.size() > 0) {
					epiNode.comment = comItem.text();
				}
				else {
					epiNode.comment = "";
				}
				epiItems.add(epiNode);								
			}			
			
		}
		return  result = true;		
	}
	
	
}
