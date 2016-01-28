package ru.skytechdev.tskgviewerreborn.utils;

import java.util.ArrayList;

import ru.skytechdev.tskgviewerreborn.categories.Categories;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import ru.skytechdev.tskgviewerreborn.serial.SerialsList;
import ru.skytechdev.tskgviewerreborn.structs.TsSerialItem;

public class SearchHelper {
	private ArrayList<TsSerialItem> found = new ArrayList<TsSerialItem>();
	
	public void clear() {
		found.clear();
	}
	
	public int getItemsFound() {
		return found.size();
	}
	
	
	public TsSerialItem getSearchItem(int id) {
		if (id < 0 || id >= getItemsFound() || getItemsFound() == 0) {
			return new TsSerialItem();
		}
		return found.get(id);
	}
	
	public int search(String text) {
		if (text.isEmpty()) {
			return 0;
		}
				
		clear();
		
		text = text.trim();
		text = text.toLowerCase();
		Categories categories = TsEngine.getInstance().getCategories();
		int catCount = categories.getItemCount();
		
		for (int i = 0; i < catCount; i++) {
			if (!TsEngine.getInstance().loadSerialList(categories.getItemById(i).value)) {
				break;
			}
			
			SerialsList serialList = TsEngine.getInstance().getSerialList();
			
			if (serialList.getSerialsCount() > 0) {
				for (int j = 0; j < serialList.getSerialsCount(); j++) {
					TsSerialItem item = serialList.getSerialById(j);
					if (item.value.toLowerCase().indexOf(text) != -1) {
						found.add(item);
					}
				}
			}
		}
		
		return getItemsFound();
	}
	
}
