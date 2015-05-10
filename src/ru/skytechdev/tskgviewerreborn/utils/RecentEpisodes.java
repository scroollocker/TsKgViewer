package ru.skytechdev.tskgviewerreborn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.skytechdev.tskgviewerreborn.structs.TsRecentItem;
import android.content.Context;
import android.util.Log;

public class RecentEpisodes {
	private Context context;
	private ArrayList<TsRecentItem> epiList = new ArrayList<TsRecentItem>();
	private static RecentEpisodes instance;
	
	private RecentEpisodes() {}
	
	public static RecentEpisodes getInstance() {
		if (instance == null) {
			instance = new RecentEpisodes();
		}
		return instance;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public void loadSerialAct(String url) {
		clear();
		
		String serial_file = TsUtils.parseSerialName(url);
		
		Log.d("LOAD: seen serial file name", serial_file);
		
		if (serial_file.isEmpty()) {
			return;
		}
				
		File favFile = new File(context.getDir("data",Context.MODE_PRIVATE),serial_file);
		
		try {
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(favFile));
			
			ArrayList<TsRecentItem> readObject = (ArrayList<TsRecentItem>)stream.readObject();
			if (readObject == null) {
				epiList = new ArrayList<TsRecentItem>();
			}
			else {
				epiList = readObject;
			}
			stream.close();

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} 		
		catch (ClassNotFoundException e) {

		} 
	}
	
	private void saveSerialAct(String url) {
		String serial_file = TsUtils.parseSerialName(url);
		
		Log.d("SAVE: seen serial file name", serial_file);
		
		if (serial_file.isEmpty()) {
			return;
		}
				
		File favFile = new File(context.getDir("data",Context.MODE_PRIVATE),serial_file);
		
		try {
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(favFile));
			stream.writeObject(epiList);			
			stream.flush();
			stream.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
	}		
	
	public int getCount() {
		return epiList.size();
	}
	
	public TsRecentItem get(int index) {
		if (index < 0 || index >= getCount()) {
			return new TsRecentItem();
		}
		else {
			return epiList.get(index);
		}
	}
	
	public void clear() {
		epiList.clear();
	}
	
	public String isInList(String url) {
				
		String result = "";
		for (int i = 0; i < getCount(); i++) {
			TsRecentItem item;
			item = get(i);
			if (item.url.compareTo(url) == 0) {
				result = item.date;
				break;
			}
		}
		return result;
	}
	
	public void addToList(String url) {
		Log.d("seen url", url);		
		
		if (isInList(url) != "") {
			return;
		}
		
		TsRecentItem item = new TsRecentItem();
		item.title = "";
		item.url = url;
			
		item.date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
			
		epiList.add(item);
		saveSerialAct(item.url);		
	}
	
}
