package ru.skytechdev.tskgviewerreborn.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TsUtils {
	private static String basePath = "http://www.ts.kg";
	
	public static String getBasePath() {
		return basePath;	
	}
	
	public static String parseSerialName(String url) {
		Pattern serialCaptionPattern = Pattern.compile("(show/)([a-z0-9_.]*)");  
    	Matcher matcher = serialCaptionPattern.matcher(url);
    	
    	String serialName = "";
    	
    	if(matcher.find()){  
    		serialName = matcher.group(2);   
    	} 
    	
    	if (serialName == null) {
    		serialName = "";
    	}
		
    	return serialName;
	}
}
