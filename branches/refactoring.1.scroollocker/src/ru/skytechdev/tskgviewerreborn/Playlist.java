package ru.skytechdev.tskgviewerreborn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class Playlist {
	private ArrayList<String> item = null;
	private Context context;
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public void setEpisodeItem(ArrayList<String> item) {
		this.item = item;
	}
	
	public File makePlaylist() {
		if (item == null) {			
			return null;
		}
		File tempPls = null;
		try {
			tempPls = File.createTempFile("playlist", ".m3u",context.getCacheDir());
			tempPls.setReadable(true, false);
			Log.d("FilePath", tempPls.getPath());
		} catch(IOException e) {
			Log.d("Exception", "Can't create file");
			return null;
		}
		
		if (tempPls.canWrite()) {
			FileWriter filewriter = null;
			try {
				filewriter = new FileWriter(tempPls);

				BufferedWriter out = new BufferedWriter(filewriter);

				out.write("#EXTM3U\n");
				int epiCount = item.size();
				for (int i = 0; i < epiCount; i++) {
					String url = item.get(i);
					out.write(url+"\n");
				}

				out.close();
			} catch (IOException e) {
				Log.d("Err", "exception!!");
				return null;
			}            
		}
		return tempPls;
		
	}
}
