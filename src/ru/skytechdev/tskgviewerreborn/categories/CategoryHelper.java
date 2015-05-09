package ru.skytechdev.tskgviewerreborn.categories;

import ru.skytechdev.tskgviewerreborn.structs.TsSortEnum;
import ru.skytechdev.tskgviewerreborn.utils.TsUtils;

public class CategoryHelper {
	public static String getCategoryLink(String catId) {		
		return getCategoryLink(catId,TsSortEnum.SORT_A,"0");
	}
	
	public static String getCategoryLink(String catId, TsSortEnum sort) {		
		return getCategoryLink(catId,sort,"0");
	}
	
	public static String getCategoryLink(String catId,TsSortEnum sort, String genre) {
		String sortValue = "a";
		
		switch(sort) {
			case SORT_A: {
				sortValue = "a";
				break;
			}
			case SORT_Z: {
				sortValue = "z";
				break;
			}
			case SORT_NEW: {
				sortValue = "new";
				break;
			}			
		}
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(TsUtils.getBasePath())
			.append("/show?category=")
			.append(catId)
			.append("&genre=")
			.append(genre)
			.append("&star=0&sort=")
			.append(sortValue)
			.append("&zero=0&year=0");		
		
		String result = builder.toString();
		
		return result;
	}
}
