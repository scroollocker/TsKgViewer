package ru.skytechdev.tskgviewerreborn.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Cache {
	
	public static boolean isExistInCache(Context context, String file) {
		Log.d("Cache", context.getCacheDir()+"/"+file);
		File coverImg = new File(context.getCacheDir()+"/"+file);
		boolean result = false;
		if (coverImg.exists()) {
			Log.d("Cache", file+" found in cache");
			result = true;
		}
		return result;
	}
	
	public static void saveImageToCache(Context context, BufferedInputStream stream, String file) {
		
		File cacheFile = new File(context.getCacheDir(),file);
		Log.d("Cache", "Start save....");		
		try {
			FileOutputStream fileStream = new FileOutputStream(cacheFile);
			Log.d("Cache", "Outputstream started....");
			byte[] buffer = new byte[8192];
			int byteRead = 0;
			while((byteRead = stream.read(buffer)) != -1) {				
				fileStream.write(buffer, 0, byteRead);
			}
			fileStream.flush();
			fileStream.close();
			
			Log.d("Cache", file+" writed to cache");
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			return;
		}
	}
	
	public static Bitmap getFileFromCache(Context context, String file) {
		File cacheFile = new File(context.getCacheDir(),file);
		Bitmap bitmap = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
		return bitmap;
	}
}
