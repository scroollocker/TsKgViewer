package ru.skytechdev.tskgviewerreborn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.skytechdev.tskgviewerreborn.R.string;
import android.content.Context;

public class Favorites {
	private ArrayList<TSItem> favList = new ArrayList<TSItem>();
	private Context context;
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public void loadFromSettings() {	
		
		File favFile = new File(context.getDir("data",Context.MODE_PRIVATE),"favorites");
		
		try {
			ObjectInputStream favStream = new ObjectInputStream(new FileInputStream(favFile));
			
			ArrayList<TSItem> readObject = (ArrayList<TSItem>)favStream.readObject();
			if (readObject == null) {
				favList = new ArrayList<TSItem>();
			}
			else {
				favList = readObject;
			}
			favStream.close();

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} 		
		catch (ClassNotFoundException e) {

		} 
	}
	
	public void saveToSettings() {	
		
		File favFile = new File(context.getDir("data",Context.MODE_PRIVATE),"favorites");
		
		try {
			ObjectOutputStream favStream = new ObjectOutputStream(new FileOutputStream(favFile));
			favStream.writeObject(favList);			
			favStream.flush();
			favStream.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
	}	
	
	public int getCount() {
		return favList.size();
	}
	
	public void clear() {
		favList.clear();
	}
	
	public boolean isExist(TSItem item) {
		boolean result = false;
		for (int i = 0; i < getCount(); i++) {			
			String url1 = item.url;			
			String url2 = getItem(i).url;
			
			url1 = parseSerialName(url1);
			url2 = parseSerialName(url2);
			
			if (!url1.isEmpty() && !url2.isEmpty()) {			
				if (url1.equals(url2)) {
					result = true;
					break;
				}
			}			
		}
		return result;
	}
	
	public void delete(TSItem item) {
		favList.remove(item);
	}
	
	public TSItem getItem(int id) {
		if (id < 0 || id >= getCount() || getCount() == 0 ) {
			return new TSItem();
		}
		return favList.get(id);
	}
	
	public void add(TSItem val) {
		if (getCount() == 0) {
			favList.add(val);
			return;
		}
		if (!favList.equals(val)) {
			favList.add(val);
		}
	}
	
	public String parseSerialName(String url) {
		Pattern serialCaptionPattern = Pattern.compile("(show/)([a-z_]*)");  
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
