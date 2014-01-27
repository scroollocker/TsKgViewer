package ru.skytechdev.tskgviewerreborn;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Menu {
	private String baseurl = "http://www.ts.kg/";
	private ArrayList<TSMenuItem> menuItems = new ArrayList<TSMenuItem>();
	
	public TSMenuItem getItemById(int id) {
		if (menuItems.size() == 0 || id < 0 || id >= menuItems.size()) {
			return new TSMenuItem();
		}
		return menuItems.get(id);		
	}
	
	public void addItem(TSMenuItem val) {
		menuItems.add(val);
	}	
	
	public int getItemCount() {
		return menuItems.size();
	}
	
	public boolean parseMenu() {
		boolean result = false;
		Document doc = null;
		
		doc = HttpWrapper.getHttpDoc(baseurl);
		if (doc == null) {
			return result;
		}
		
		if (getItemCount() > 0) {
			menuItems.clear();
		}
		
		Elements menuElements = doc.select("header").select("ul.dropdown-menu").select("a");
		if (menuElements.size() > 0) {
			for (int i = 0; i < menuElements.size(); i++) {
				String link = menuElements.get(i).attr("href");
				String caption = menuElements.get(i).text();
				if (link.indexOf("megogo") == -1) {
					TSMenuItem item = new TSMenuItem();
					if (link.indexOf("shows") != -1) {
						caption += " телепередачи";
					}
					item.url = link;
					item.value = caption;
					addItem(item);
				}
			}
			/* KIDI */
			TSMenuItem item = new TSMenuItem();
			item.url = "http://www.ts.kg/kids/";
			item.value = "Для детей";
			addItem(item);
			result = true;
		}		
		
		return result;
	}
	
}
