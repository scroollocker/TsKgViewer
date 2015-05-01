package ru.skytechdev.tskgviewerreborn.categories;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ru.skytechdev.tskgviewerreborn.utils.HttpWrapper;
import ru.skytechdev.tskgviewerreborn.utils.TsUtils;


public class Categories {
	
	private static Categories instance = null;
	private ArrayList<TsCategoryItem> menuItems = new ArrayList<TsCategoryItem>();
	
	private Categories() {}
	
	public static Categories getInstance() {
		if (instance == null) {
			instance = new Categories();			
		}
		
		return instance;
	}
	
	public TsCategoryItem getItemById(int id) {
		if (menuItems.size() == 0 || id < 0 || id >= menuItems.size()) {
			return new TsCategoryItem();
		}
		return menuItems.get(id);		
	}
	
	private void addItem(TsCategoryItem val) {
		menuItems.add(val);
	}	
	
	public int getItemCount() {
		return menuItems.size();
	}
	
	public boolean loadCategories(boolean cache) {
		boolean result = false;
		if (cache) {
			if (getItemCount() == 0) {
				result = parseMenu();				
			}
		}
		else {
			result = parseMenu();
		}		
		
		return result;
	}
	
	private boolean parseMenu() {
		boolean result = false;
		Document doc = null;
		
		doc = HttpWrapper.getHttpDoc(TsUtils.getBasePath()+"/show");
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
				
				String caption = menuElements.get(i).text();
				
				TsCategoryItem item = new TsCategoryItem();									
				item.value = value;
				item.name = caption;
				addItem(item);				
			}
			
			result = true;
		}	
				
		return result;
	}
	
}
