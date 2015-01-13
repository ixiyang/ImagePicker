package com.ixy.imagepicker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ixy.imagepicker.R;
import com.ixy.imagepicker.lib.ChooseMultiImgActivity;
import com.ixy.imagepicker.lib.GridItem;
import com.ixy.imagepicker.lib.ImageLoader;

public class MainActivity extends Activity {

	private GridView gv_gallery;
	GridAdapter mPreviewAdapter;
	private ArrayList<String> sImagePaths;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gv_gallery = (GridView) findViewById(R.id.grid);
		gv_gallery.setNumColumns(4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:
			pickImage();
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void pickImage() {
		Intent intent = new Intent(this, ChooseMultiImgActivity.class);
		startActivityForResult(intent, ChooseMultiImgActivity.REQUEST_CODE_PICK_IMG);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			if (requestCode == ChooseMultiImgActivity.REQUEST_CODE_PICK_IMG) {
				onImagePicked(data);
			}
		}
	}

	private void onImagePicked(Intent data) {
		sImagePaths = data.getStringArrayListExtra(ChooseMultiImgActivity.IMG_PATHS);
		if (sImagePaths.size() < 0) {
			gv_gallery.setVisibility(View.INVISIBLE);
			return;
		}
		List<GridItem> items = new ArrayList<GridItem>();
		for (String path : sImagePaths) {
			GridItem item = new GridItem("", path);
			items.add(item);
		}
		// 添加extra item
		// GridItem item = new GridItem("", "");
		// items.add(item);
		mPreviewAdapter = new GridAdapter(this, items);
		gv_gallery.setAdapter(mPreviewAdapter);
		gv_gallery.setVisibility(View.VISIBLE);
		gv_gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

			}
		});
	}

	private class GridAdapter extends BaseAdapter {

		List<GridItem> mItems = new ArrayList<GridItem>();
		ImageLoader mImageLoader;

		public GridAdapter(Activity activity) {
			mImageLoader = new ImageLoader(activity);
		}

		public GridAdapter(Activity activity, List<GridItem> items) {
			mItems = items;
			mImageLoader = new ImageLoader(activity);
		}

		public List<GridItem> getData() {
			return mItems;
		}

		public void add(GridItem item) {
			mItems.add(item);
			notifyDataSetChanged();
		}

		public void addAll(List<GridItem> items) {
			mItems.addAll(items);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = (ImageView) LayoutInflater.from(MainActivity.this).inflate(
						R.layout.imageitem, null);
			} else {
				imageView = (ImageView) convertView;
			}
			GridItem item = (GridItem) mItems.get(position);
			if (TextUtils.isEmpty(item.getPath())) {
				// default Image

			} else {
				mImageLoader.DisplayImage(item.getPath(), imageView);
			}
			return imageView;
		}

	}
}
