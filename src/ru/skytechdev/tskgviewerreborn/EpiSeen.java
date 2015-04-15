package ru.skytechdev.tskgviewerreborn;

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

import android.content.Context;
import android.util.Log;

public class EpiSeen {
	private Context context;
	private String base_url = "http://www.ts.kg";
	private ArrayList<TSSeenEpiItem> epiList = new ArrayList<TSSeenEpiItem>();
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public void loadSerialAct(String url) {
		clear();
		
		Log.d("file_url",url);
		
		if (url.substring(0,1).equals("/")) {
			url = base_url+url;
		}
		
		String[] urlpart = url.split("/");
		if (urlpart.length == 0) {
			return;
		}	
		String serial_file;
		if (urlpart.length >= 5 ) {
			
			serial_file = urlpart[4];
			Log.d("file_url",serial_file);
		}
		else {
			return;
		}
		
		
		File favFile = new File(context.getDir("data",Context.MODE_PRIVATE),serial_file);
		
		try {
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(favFile));
			
			ArrayList<TSSeenEpiItem> readObject = (ArrayList<TSSeenEpiItem>)stream.readObject();
			if (readObject == null) {
				epiList = new ArrayList<TSSeenEpiItem>();
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
	
	public void saveSerialAct(String url) {	
		if (url.substring(0,1).equals("/")) {
			url = base_url+url;
		}
		
		String[] urlpart = url.split("/");
		if (urlpart.length == 0) {
			return;
		}	
		
		String serial_file = "";
		if (urlpart.length >= 5 ) {
			serial_file = urlpart[4];
			Log.d("file_url",serial_file);
		}
		else {
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
	
	public TSSeenEpiItem get(int index) {
		if (index < 0 || index >= getCount()) {
			return new TSSeenEpiItem();
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
			TSSeenEpiItem item;
			item = get(i);
			if (item.url.compareTo(url) == 0) {
				result = item.date;
				break;
			}
		}
		return result;
	}
	
	public void addToList(String url) {
		Log.d("seen url", "url");
		
		if (isInList(url) != "") {
			return;
		}
		
		TSSeenEpiItem item = new TSSeenEpiItem();
		item.title = "";
		item.url = url;
		
		item.date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
		
		epiList.add(item);
		saveSerialAct(item.url);
	}
}
