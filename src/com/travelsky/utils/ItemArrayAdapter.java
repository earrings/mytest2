package com.travelsky.utils;

import java.util.List;

import com.travelsky.movie.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ItemArrayAdapter extends ArrayAdapter<ListItem> {	
	private int mResource;
	private LayoutInflater mInflater;
	private Context context;
	
	public ItemArrayAdapter(Context context, int resource,List<ListItem> objects) {
		
		
		super(context, resource, objects);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResource = resource;
		this.context = context;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		if(null == convertView){
			convertView = mInflater.inflate(R.layout.sd_list, null);
		}
		
		ImageView image = (ImageView)convertView.findViewById(R.id.publicity);
		TextView textView = (TextView)convertView.findViewById(R.id.mp4describe);
		
		ListItem item = getItem(position);
		textView.setText(item.getTitle());
		
		
		return convertView;
	}
}
