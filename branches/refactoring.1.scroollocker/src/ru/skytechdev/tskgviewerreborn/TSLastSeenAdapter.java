package ru.skytechdev.tskgviewerreborn;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
public class TSLastSeenAdapter extends ArrayAdapter<TSItem> {
	private TSItem[] items;
	private ImageView[] covers;
	private Context context;
	//boolean firstRun;
	
	public TSLastSeenAdapter(Context context, TSItem[] objects) {
		super(context, R.layout.layout_lastseenitem, objects);
		items = objects;
		covers = new ImageView[items.length];
		//for (int i = 0; i < items.length; i++) {
		//	covers[i] = new ImageView(context);
		//	new ImageManager().fetchImage(items[i].imgurl, covers[i], context);
		//}
		this.context = context;
		//firstRun = true;
	}	
	
	class ViewHolder {
		public ImageView cover;
		public TextView title;
		public int position;
	}
	
	
	protected Bitmap downloadImage(String url) {				
	    Bitmap bitmap = null;
	    HttpURLConnection conn = null;
	    BufferedInputStream buf_stream = null;
		//String fileName = parseFileName(item.url);
		//if (Cache.isExistInCache(context, fileName)) {
		//	bitmap = Cache.getFileFromCache(context, fileName);
		//}			    
		//else {
			try {
				conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setDoInput(true);
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.connect();
				buf_stream = new BufferedInputStream(conn.getInputStream(), 8192);
	      
				//BufferedInputStream temp = new BufferedInputStream(buf_stream);
				
				bitmap = BitmapFactory.decodeStream(buf_stream);
				//Cache.saveImageToCache(context, temp, fileName);
				
				buf_stream.close();
				conn.disconnect();
				buf_stream = null;
	      
				conn = null;
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
			
		//}
		return bitmap;
	}	
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		//View rowView = convertView;		
		
		final int pos = position;
		
		/*if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        rowView = inflater.inflate(R.layout.layout_lastseenitem, parent, false);
	        holder = new ViewHolder();
	        holder.cover = (ImageView)rowView.findViewById(R.id.ls_cover);
	        holder.title  = (TextView)rowView.findViewById(R.id.ls_caption);
	        holder.position = position;
	        
	        /*if (firstRun) {
	        	for (int i = 0; i < items.length; i++) {
	        		covers[i] = new ImageView(rowView.getContext());
	        		new ImageManager().fetchImage(items[i].imgurl, covers[i], context);
	        	}
	       //}
	        if (covers[i] == null) {
	        	
	        }
	        
	        
	        /*if (covers[position] == null) {
				new ImageManager().fetchImage(items[position].imgurl, holder.cover, context);
		        covers[position] = holder.cover;
			}
	        
	        rowView.setTag(holder);	        
		}
		else {
			holder = (ViewHolder)rowView.getTag();			
		}
		   
		
	    //ImageView cover = (ImageView)rowView.findViewById(R.id.ls_cover);
	    //TextView title  = (TextView)rowView.findViewById(R.id.ls_caption);
	    //title.setText(items[position].value);
        //new ImageManager().fetchImage(items[position].imgurl, cover, context);
	    
	    
        //holder.title.setText(items[position].value);
        
        //holder.cover = covers[position];
        
        //new ImageManager().fetchImage(items[position].imgurl, holder.cover, context);
		
		//holder.cover = covers[position];
		//holder.cover.setImageDrawable(covers[position].getDrawable());
		
		//new ImageManager().fetchImage(items[position].imgurl, holder.cover, context);
		
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_lastseenitem, parent, false);
        ImageView cover = (ImageView)rowView.findViewById(R.id.ls_cover);
	    TextView title  = (TextView)rowView.findViewById(R.id.ls_caption);
	   
        //if (covers[position] == null) {
        	//new ImageManager().fetchImage(items[position].imgurl, cover, context);
        	//covers[position] = cover;        	
        //}
        //else {
        	//cover = covers[position];
        //}
        
	    //cover.setImageBitmap(downloadImage(items[position].imgurl));
	    
        title.setText(items[position].value);
		//View rowView = LayoutInflater.from(context).inflate(R.layout.layout_lastseenitem, null);
		
	    //ImageView cover = (ImageView)rowView.findViewById(R.id.ls_cover);
	    //TextView title  = (TextView)rowView.findViewById(R.id.ls_caption);
	   
	    //title.setText(items[position].value);
        //new ImageManager().fetchImage(items[position].imgurl, cover, context);
	    
		return rowView;
	}
	
}
*/
	
class ViewHolder {
	public ImageView cover;
	public TextView title;
}

public class TSLastSeenAdapter extends ArrayAdapter<TSItemBitmap> {
	private TSItemBitmap[] items;

	private Context context;

	
	public TSLastSeenAdapter(Context context, TSItemBitmap[] objects) {
		super(context, R.layout.layout_lastseenitem, objects);
		items = objects;

		this.context = context;
	}	
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		View rowView = convertView;	
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        rowView = inflater.inflate(R.layout.layout_lastseenitem, parent, false);
	        holder = new ViewHolder();
	        holder.cover = (ImageView)rowView.findViewById(R.id.ls_cover);
	        holder.title  = (TextView)rowView.findViewById(R.id.ls_caption);
        
	        rowView.setTag(holder);	        
		}
		else {
			holder = (ViewHolder)rowView.getTag();			
		}

		holder.cover.setImageBitmap(items[position].bitmap);
		holder.title.setText(items[position].item.value);
	    
		return rowView;
	}
	
}