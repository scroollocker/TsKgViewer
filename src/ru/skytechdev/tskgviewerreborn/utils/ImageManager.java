package ru.skytechdev.tskgviewerreborn.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.structs.TsImageItem;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageManager {	 
	
	  public Context context;	  
	  
	  public ImageManager() {}
	  
	  public ImageManager(Context context) {
		  this.context = context;
	  }
	 
	  public void fetchImage(final String iUrl, final ImageView iView, Context context) {
	    if ( iUrl == null || iView == null )
	      return;
	    
	    iView.setImageResource(R.drawable.ic_launcher);
	    TsImageItem item = new TsImageItem();
	    item.img = iView;
	    item.url = iUrl;
	    this.context = context;
	    
	    new AsyncExecution(context).execute(item);
	  }
	  
	   protected String parseFileName(String url) {
	    	String[] tile = url.split("/");
	    	return tile[tile.length-1];
	   }
	  
	  public Bitmap getBitmap(String url) {	
		    Bitmap bitmap = null;
		    HttpURLConnection conn = null;
		    BufferedInputStream buf_stream = null;

		    if (url.substring(0,1).equals("/")) {
		    	url = TsUtils.getBasePath()+url;
		    }
		    
			String fileName = parseFileName(url);

			if (Cache.isExistInCache(context, fileName)) {
				bitmap = Cache.getFileFromCache(context, fileName);
			}			    
			else {
				try {
					conn = (HttpURLConnection) new URL(url).openConnection();
					conn.setDoInput(true);
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.connect();
					buf_stream = new BufferedInputStream(conn.getInputStream(), 8192);		      

					Cache.saveImageToCache(context, buf_stream, fileName);					
					
					buf_stream.close();
					conn.disconnect();
					buf_stream = null;
					conn = null;
					
					bitmap = Cache.getFileFromCache(context,fileName);
					
				} catch (MalformedURLException ex) {
				} catch (IOException ex) {
				} catch (OutOfMemoryError e) {
					return null;
				} finally {
					if ( buf_stream != null )
						try { buf_stream.close(); } catch (IOException ex) {}
					if ( conn != null )
						conn.disconnect();
				}
				
			}
			return bitmap;
		}
	  
	  public class AsyncExecution extends AsyncTask<TsImageItem, Void, Bitmap> {
		    protected ImageView img;
		    protected Context context;
		    
		    public AsyncExecution(Context context) {
		    	this.context = context;
		    }
		    
			@Override
			protected Bitmap doInBackground(TsImageItem... arg0) {	
				TsImageItem item = arg0[0];
				img = item.img;		
			    Bitmap bitmap = null;
			    
				String fileName = parseFileName(item.url);
				if (Cache.isExistInCache(context, fileName)) {
					bitmap = Cache.getFileFromCache(context, fileName);
				}			    
				else {
					bitmap = getBitmap(item.url);
					
				}
				return bitmap;
			}
	    	
			@Override
			protected void onPostExecute(Bitmap result) {
				if (result != null) {
					img.setImageBitmap(result);
				}
			}
	    }	  
}
