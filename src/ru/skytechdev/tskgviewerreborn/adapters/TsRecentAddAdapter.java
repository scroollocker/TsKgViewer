package ru.skytechdev.tskgviewerreborn.adapters;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.structs.TsRecentAddItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TsRecentAddAdapter extends ArrayAdapter<TsRecentAddItem> {
	private TsRecentAddItem[] items;
	private Context context;
	
	public TsRecentAddAdapter(Context context, TsRecentAddItem[] objects) {
		super(context, R.layout.layout_newepiitem, objects);
		items = objects;
		this.context = context;
	}
	
	static class ViewHolder {
		public TextView caption;
		public TextView date;
		public TextView comment;
		public ImageView favorite;
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ViewHolder holder;		
		
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        rowView = inflater.inflate(R.layout.layout_newepiitem, parent, false);
	        
	        holder = new ViewHolder();
	        
	        holder.caption = (TextView)rowView.findViewById(R.id.ne_caption);
	        holder.date = (TextView)rowView.findViewById(R.id.ne_date);
	        holder.comment = (TextView)rowView.findViewById(R.id.ne_comment);
	        holder.favorite = (ImageView)rowView.findViewById(R.id.ne_isfavorite);
	        
	        rowView.setTag(holder);
		}
		else {
			holder = (ViewHolder)rowView.getTag();
		}
		
		holder.caption.setText(items[position].caption);
        holder.date.setText(items[position].date);
        holder.comment.setText(items[position].comment);
        if (items[position].isFavorite) {
        	holder.favorite.setVisibility(View.VISIBLE);
        }
        else {
        	holder.favorite.setVisibility(View.GONE);
        }
		
        return rowView;

	}	
}