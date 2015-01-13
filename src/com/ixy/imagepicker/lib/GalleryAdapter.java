/*
 * Copyright 2013 Thomas Hoffmann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ixy.imagepicker.lib;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ixy.imagepicker.R;

public class GalleryAdapter extends BaseAdapter {

	private final List<GridItem> items;
	private final ImageLoader imageLoader;
	private final LayoutInflater mInflater;
	private Context mContext;
	public GalleryAdapter(final Context context, final List<GridItem> buckets) {
		mContext=context;
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
		ViewHolder viewHolder;
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.multichooseimg_item, null);
			viewHolder=new ViewHolder();
			viewHolder.img=(ImageView) convertView.findViewById(R.id.grid_item_img);
			viewHolder.check_icon=(ImageView) convertView.findViewById(R.id.grid_item_img_is_checked);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		ChooseMultiImgActivity activity=(ChooseMultiImgActivity) mContext;
		GridItem item=(GridItem) getItem(position);
		if (activity.getImagePaths().contains(item.path)) {
			item.isChecked=true;
			viewHolder.check_icon.setVisibility(View.VISIBLE);
		}else {
			viewHolder.check_icon.setVisibility(View.INVISIBLE);
		}
		imageLoader.DisplayImage(items.get(position).path, viewHolder.img);
		return convertView;
		
	}

	public static class ViewHolder {
		ImageView img;
		ImageView check_icon;
	}
}
