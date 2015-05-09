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

public class RecentSerials {
		
	private ArrayList<TsSerialItem> seenList = new ArrayList<TsSerialItem>(); 
	private final int max_list_size = 10;
	private Context context;
	private static RecentSerials instance;
	
	private RecentSerials() {}
	
	public static RecentSerials getInstance() {
		if (instance == null) {
			instance = new RecentSerials();
		}		
		return instance; 
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public void loadFromSettings() {	
		File seenFile = new File(context.getDir("data",Context.MODE_PRIVATE),"lastSeen");
		
		try {
			ObjectInputStream seenStream = new ObjectInputStream(new FileInputStream(seenFile));
			
			ArrayList<TsSerialItem> readObject = (ArrayList<TsSerialItem>)seenStream.readObject();
			if (readObject == null) {
				seenList = new ArrayList<TsSerialItem>();
			}
			else {
				seenList = readObject;
			}
			seenStream.close();

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} 		
		catch (ClassNotFoundException e) {

		} 
		
	}
	
	public void saveToSettings() {				
		File seenFile = new File(context.getDir("data",Context.MODE_PRIVATE),"lastSeen");
		
		try {
			ObjectOutputStream seenStream = new ObjectOutputStream(new FileOutputStream(seenFile));
			seenStream.writeObject(seenList);			
			seenStream.flush();
			seenStream.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
	}
	
	public int getCount() {
		return seenList.size();		
	}
	
	public TsSerialItem get(int id) {
		if (id < 0 || id >= getCount() || getCount() == 0) {
			return new TsSerialItem();
		}
		return seenList.get(id);
	}
	
	public String getCaption(int id) {
		if (id < 0 || id >= getCount() || getCount() == 0) {
			return "";
		}
		return seenList.get(id).value;
	}
	
	public String getUrl(int id) {
		if (id < 0 || id >= getCount() || getCount() == 0) {
			return "";
		}
		return seenList.get(id).url;
	} 
	
	public void add(String capt, String url, String imgUrl) {
		TsSerialItem item = new TsSerialItem();
		item.value = capt;
		item.url = url;
		item.imgurl = imgUrl;
		
		this.add(item);
	}
	
	public void add(TsSerialItem val) {
		if (seenList.size() == 0) {
			seenList.add(val);
			return;
		}
		int len = seenList.size();
		if (len+1 > max_list_size) {
			len = max_list_size;
		}
		ArrayList<TsSerialItem> tempList = new ArrayList<TsSerialItem>();
		tempList.add(val);
		for (int i = 0; i < len; i++) {
			if (seenList.get(i).equals(val)) {
				continue;
			}
			tempList.add(seenList.get(i));
		}
		seenList = tempList;
	}
	
}
