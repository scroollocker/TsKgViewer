package ru.skytechdev.tskgviewerreborn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

	
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