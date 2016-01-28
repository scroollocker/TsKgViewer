package ru.skytechdev.tskgviewerreborn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ru.skytechdev.tskgviewerreborn.structs.TsSerialItem;
import android.content.Context;

public class Favorites {
	private ArrayList<TsSerialItem> favList = new ArrayList<TsSerialItem>();
	private Context context;
	
	private static Favorites instance = null;
	
	private Favorites() { } 
	
	public static Favorites getInstance() {
		if (instance == null) {
			instance = new Favorites();			
		}
		return instance;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public void loadFromSettings() {	
		
		File favFile = new File(context.getDir("data",Context.MODE_PRIVATE),"favorites");
		
		try {
			ObjectInputStream favStream = new ObjectInputStream(new FileInputStream(favFile));
			
			ArrayList<TsSerialItem> readObject = (ArrayList<TsSerialItem>)favStream.readObject();
			if (readObject == null) {
				favList = new ArrayList<TsSerialItem>();
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
	
	public boolean isExist(String url) {
		boolean result = false;
		for (int i = 0; i < getCount(); i++) {			
			String url1 = url;			
			String url2 = getItem(i).url;
			
			url1 = TsUtils.parseSerialName(url1);
			url2 = TsUtils.parseSerialName(url2);
			
			if (!url1.isEmpty() && !url2.isEmpty()) {			
				if (url1.equals(url2)) {
					result = true;
					break;
				}
			}			
		}
		return result;
	}
	
	public boolean isExist(TsSerialItem item) {
		boolean result = false;
		for (int i = 0; i < getCount(); i++) {			
			String url1 = item.url;			
			String url2 = getItem(i).url;
			
			url1 = TsUtils.parseSerialName(url1);
			url2 = TsUtils.parseSerialName(url2);
			
			if (!url1.isEmpty() && !url2.isEmpty()) {			
				if (url1.equals(url2)) {
					result = true;
					break;
				}
			}			
		}
		return result;
	}
	
	public void delete(String capt, String url, String imgUrl) {
		TsSerialItem item = new TsSerialItem();
		item.value = capt;
		item.url = url;
		item.imgurl = imgUrl;
		
		this.delete(item);
	}
	
	public void delete(TsSerialItem item) {
		favList.remove(item);
	}
	
	public TsSerialItem getItem(int id) {
		if (id < 0 || id >= getCount() || getCount() == 0 ) {
			return new TsSerialItem();
		}
		return favList.get(id);
	}
	
	public void add(String capt, String url, String imgUrl) {
		TsSerialItem item = new TsSerialItem();
		item.value = capt;
		item.url = url;
		item.imgurl = imgUrl;
		
		this.add(item);
	}
	
	public void add(TsSerialItem val) {
		if (getCount() == 0) {
			favList.add(val);
			return;
		}
		if (!favList.equals(val)) {
			favList.add(val);
		}
	}
	
}
