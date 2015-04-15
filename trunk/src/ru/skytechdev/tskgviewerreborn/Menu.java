package ru.skytechdev.tskgviewerreborn;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Menu {
	private String baseurl = "http://www.ts.kg";
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
				
		Elements menuElements = doc.select(".footer-links").select("a");
		if (menuElements.size() > 0) {
			for (int i = 0; i < menuElements.size(); i++) {
				String link = menuElements.get(i).attr("href");
				String caption = menuElements.get(i).text();
				if (link.indexOf("/show/") != -1) {
					TSMenuItem item = new TSMenuItem();
					if (link.substring(0,1).equals("/")) {
						link = baseurl + link;
					}
					
					item.url = link;
					item.value = caption;
					addItem(item);
				}
			}
			/* for kidi fix */
			TSMenuItem item = new TSMenuItem();
			item.url = "http://www.ts.kg/show/?category=3&sort=a";
			item.value = "Для детей";
			addItem(item);
			
			result = true;
		}		
		
		return result;
	}
	
}
