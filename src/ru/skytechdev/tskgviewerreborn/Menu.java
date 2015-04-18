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
		
		doc = HttpWrapper.getHttpDoc(baseurl+"/show");
		if (doc == null) {
			return result;
		}
		
		if (getItemCount() > 0) {
			menuItems.clear();
		}
				
		Elements menuElements = doc.select("select#filter-category").select("option");
		if (menuElements.size() > 0) { 
	    		    	
			for (int i = 0; i < menuElements.size(); i++) {
				String value = menuElements.get(i).attr("value");
				if (value.equals("0")) {
					/* пропуск пункта "все категории" */
					continue;
				}
				String link = baseurl+"/show?category="+value+"&genre=0&star=0&sort=a&zero=0&year=0";
				String caption = menuElements.get(i).text();
				
					TSMenuItem item = new TSMenuItem();									
					item.url = link;
					item.value = caption;
					addItem(item);
				
			}
			
			result = true;
		}	
				
		return result;
	}
	
}
