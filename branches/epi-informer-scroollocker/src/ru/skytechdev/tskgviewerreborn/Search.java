package ru.skytechdev.tskgviewerreborn;

import java.util.ArrayList;

public class Search {
	private Menu categories = null;
	private ArrayList<TSItem> found = new ArrayList<TSItem>();
	
	public void clear() {
		found.clear();
	}
	
	public int getItemsFound() {
		return found.size();
	}
		
	public void setCategories(Menu val) {
		categories = val;
	}
	
	public TSItem getSearchItem(int id) {
		if (id < 0 || id >= getItemsFound() || getItemsFound() == 0) {
			return new TSItem();
		}
		return found.get(id);
	}
	
	public void search(String text) {
		if (text.isEmpty()) {
			return;
		}
		
		if (categories == null) {
			return;
		}
		
		clear();
		
		text = text.trim();
		text = text.toLowerCase();
		
		int catCount = categories.getItemCount();
		Serials allSerials = new Serials();
		for (int i = 0; i < catCount; i++) {
			allSerials.parseSerialsByUrl(categories.getItemById(i).url);
			if (allSerials.getSerialsCount() > 0) {
				for (int j = 0; j < allSerials.getSerialsCount(); j++) {
					TSItem item = allSerials.getSerialById(j);
					if (item.value.toLowerCase().indexOf(text) != -1) {
						found.add(item);
					}
				}
			}
		}
		
	}
	
}
