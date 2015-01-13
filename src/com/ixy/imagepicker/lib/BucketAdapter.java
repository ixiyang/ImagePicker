package com.ixy.imagepicker.lib;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixy.imagepicker.R;


public class BucketAdapter extends BaseAdapter{

	private final List<GridItem> items;
	private final ImageLoader imageLoader;
	private final LayoutInflater mInflater;
	
	public BucketAdapter(final Context context, final List<GridItem> buckets) {
		this.items = buckets;
		this.imageLoader = new ImageLoader(context);
		this.mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.bucketitem, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setText(items.get(position).name);
		imageLoader.DisplayImage(items.get(position).path, holder.icon);
		return convertView;
	}
	private static class ViewHolder {
		ImageView icon;
		TextView text;
	}
}
