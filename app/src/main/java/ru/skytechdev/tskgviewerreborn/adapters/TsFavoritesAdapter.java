package ru.skytechdev.tskgviewerreborn.adapters;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.structs.TsBitmapItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TsFavoritesAdapter extends ArrayAdapter<TsBitmapItem> {
	private TsBitmapItem[] items;

	private Context context;

	class ViewHolder {
		public ImageView cover;
		public TextView title;
	}
	
	public TsFavoritesAdapter(Context context, TsBitmapItem[] objects) {
		super(context, R.layout.layout_favoritesitem, objects);
		items = objects;

		this.context = context;
	}	
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		View rowView = convertView;	
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        rowView = inflater.inflate(R.layout.layout_favoritesitem, parent, false);
	        holder = new ViewHolder();
	        holder.cover = (ImageView)rowView.findViewById(R.id.fv_img);
	        holder.title  = (TextView)rowView.findViewById(R.id.fv_caption);
        
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