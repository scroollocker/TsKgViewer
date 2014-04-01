package ru.skytechdev.tskgviewerreborn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TSEpilistAdapter extends ArrayAdapter<TSSeenEpiItem>{
	private TSSeenEpiItem[] items;
	private Context context;
	
	public TSEpilistAdapter(Context context, TSSeenEpiItem[] objects) {
		super(context, R.layout.layout_epilist, objects);
		items = objects;
		this.context = context;
	}
	
	static class ViewHolder {
		public TextView caption;
		public TextView date;
	}	
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ViewHolder holder;		
		
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        rowView = inflater.inflate(R.layout.layout_epilist, parent, false);
	        
	        holder = new ViewHolder();
	        
	        holder.caption = (TextView)rowView.findViewById(R.id.epititle);
	        holder.date = (TextView)rowView.findViewById(R.id.epidate);	        
	        
	        rowView.setTag(holder);
		}
		else {
			holder = (ViewHolder)rowView.getTag();
		}
		
		holder.caption.setText(items[position].title);
		if(items[position].date.isEmpty()) {
			holder.date.setText("");
		}
		else {
			holder.date.setText("Дата просмотра: " +items[position].date);
		}
		
        return rowView;

	}	
}