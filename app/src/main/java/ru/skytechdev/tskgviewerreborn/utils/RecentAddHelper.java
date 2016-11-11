package ru.skytechdev.tskgviewerreborn.utils;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ru.skytechdev.tskgviewerreborn.structs.TsRecentAddItem;
import ru.skytechdev.tskgviewerreborn.utils.TsUtils;

public class RecentAddHelper {
	
	private ArrayList<TsRecentAddItem> epiItems = new ArrayList<TsRecentAddItem>();
	
	public void clear() {
		epiItems.clear();
	}
	
	public int getEpiCount() {
		return epiItems.size();
	}
	
	public TsRecentAddItem getEpiById(int id) {
		if (getEpiCount() == 0 || id >= getEpiCount() || id < 0) {
			return new TsRecentAddItem();
		}
		
		return epiItems.get(id);
	}
	
	public ArrayList<TsRecentAddItem> getAllItems() {
		return epiItems;
	}
	
	public boolean parseNewEpi() {
		boolean result = false;
		Document doc = null;
		
		doc = HttpWrapper.getHttpDoc(TsUtils.getBasePath());
		if (doc == null) {
			return result;
		}
		
		if (getEpiCount() > 0) {
			epiItems.clear();
		}
		
		Elements epiElements = doc.select(".col-xs-4").select(".news");
		
		if (epiElements.size() == 0) {
			return result;
		}
		
		String currDate = "";
		
		for (int i = 0; i < epiElements.size(); i++) {
			/*
			Elements dateItem = epiElements.get(i).select("h3");
			if (dateItem.size() > 0) {
				currDate = dateItem.text();
				continue;
			}
			*/
			Elements epiItem = epiElements.get(i).select("a");
			Elements comItem = epiElements.get(i).select("span.label");
			Elements epiDetail = epiElements.get(i).select("small");
			if (epiItem.size() > 0) {
				TsRecentAddItem epiNode = new TsRecentAddItem();
				String link = epiItem.attr("href");
				if (link.substring(0, 1).equals("/")) {
					link = TsUtils.getBasePath()+link;
				}
				
				epiNode.caption = epiItem.text();
				if (epiDetail.size() > 0) {
					epiNode.caption += " ("+epiDetail.text()+")";
				}
				epiNode.link = link;
				if (!epiNode.link.matches(".*ts.kg.*")) {
					continue;
				}
				epiNode.date = currDate;
				
				if (comItem.size() > 0) {
					epiNode.comment = comItem.text();
					if (epiNode.comment.equals("Новости")) {
						continue;
					}										
				}
				else {
					epiNode.comment = "";
				}
				epiItems.add(epiNode);								
			}			
			
		}
		return true;
	}
	
	
}
