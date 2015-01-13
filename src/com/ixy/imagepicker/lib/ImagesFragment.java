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

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.ixy.imagepicker.R;
import com.ixy.imagepicker.lib.GalleryAdapter.ViewHolder;

public class ImagesFragment extends Fragment {

	private GalleryAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.gallery, null);
		Cursor cur = getActivity().getContentResolver()
				.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Images.Media.DATA,
								MediaStore.Images.Media.DISPLAY_NAME },
						MediaStore.Images.Media.BUCKET_ID + " = ?",
						new String[] { String.valueOf(getArguments().getInt("bucket")) }, null);

		final List<GridItem> images = new ArrayList<GridItem>(cur.getCount());

		if (cur != null) {
			if (cur.moveToFirst()) {
				while (!cur.isAfterLast()) {
					images.add(new GridItem(cur.getString(1), cur.getString(0)));
					cur.moveToNext();
				}
			}
			cur.close();
		}

		GridView grid = (GridView) v.findViewById(R.id.grid);
		grid.setNumColumns(4);
		mAdapter = new GalleryAdapter(getActivity(), images);
		grid.setAdapter(mAdapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ChooseMultiImgActivity activity = (ChooseMultiImgActivity) getActivity();
				GridItem item = (GridItem) parent.getAdapter().getItem(position);

				if (item.isChecked) {
					item.isChecked = false;
					((ViewHolder) view.getTag()).check_icon.setVisibility(View.INVISIBLE);
					activity.removeFromPreview(item.path);
				} else {

					if (activity.getImagePaths().size() >= ChooseMultiImgActivity.MAXSIZE) {
						Toast.makeText(activity, "最多选择" + ChooseMultiImgActivity.MAXSIZE + "张图片",
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						item.isChecked = true;
						((ViewHolder) view.getTag()).check_icon.setVisibility(View.VISIBLE);
						activity.addToPreview(item.path);
					}
				}
			}
		});
		return v;
	}

	public GalleryAdapter getAdapter() {
		return mAdapter;
	}
}
