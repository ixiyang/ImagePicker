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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.ixy.imagepicker.R;

public class ChooseMultiImgActivity extends FragmentActivity implements View.OnClickListener {

	private LinearLayout mLlPreview;
	private Button mBtnOK;
	private ArrayList<String> mImagePath = new ArrayList<String>();
	private ImageLoader mImageLoader;
	public static int MAXSIZE = 4;
	public static Map<String, ImageView> IMGS = new HashMap<String, ImageView>();
	public static String FM_GALLERY = "gallery";
	public static String IMG_PATHS = "img_paths";
	public static final int REQUEST_CODE_PICK_IMG = 255;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_choosemultiimg);
		
		initView();
	}

	void showBucket(final int bucketId) {
		Bundle b = new Bundle();
		b.putInt("bucket", bucketId);
		Fragment f = new ImagesFragment();
		f.setArguments(b);
		getSupportFragmentManager().beginTransaction().replace(R.id.ll_container, f, FM_GALLERY)
				.addToBackStack(null).commit();
	}

	void imageSelected(final String imgPath) {
		Intent result = new Intent();
		result.putExtra("imgPath", imgPath);
		setResult(RESULT_OK, result);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			Intent intent = new Intent();
			intent.putStringArrayListExtra(IMG_PATHS, mImagePath);
			setResult(RESULT_OK, intent);
			finish();

			break;
		default:
			break;
		}

	}

	/*
	 * protected void fillData() { for (String path :
	 * WeiboCreateActivity.sImagePaths) { addToPreview(path); }
	 * 
	 * }
	 */
	protected void initView() {
		mLlPreview = (LinearLayout) findViewById(R.id.ll_preview);
		mBtnOK = (Button) findViewById(R.id.btn_ok);
		mBtnOK.setOnClickListener(this);
		mImageLoader = new ImageLoader(this);
		setResult(RESULT_CANCELED);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			setTheme(android.R.style.Theme_Holo_NoActionBar_TranslucentDecor);
		} else {
			setTheme(android.R.style.Theme_Holo_NoActionBar);
		}

		// Create new fragment and transaction
		Fragment newFragment = new BucketsFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.ll_container, newFragment);

		// Commit the transaction
		transaction.commit();
	}

	public List<String> getImagePaths() {
		return mImagePath;
	}

	public void addToPreview(final String path) {
		SquareImageView imageView = new SquareImageView(this, null);
		LayoutParams lp = new LayoutParams(100, 100);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setPadding(20, 5, 5, 5);
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				removeFromPreview(path);
				Fragment fragment = getSupportFragmentManager().findFragmentByTag(FM_GALLERY);
				if (fragment != null) {
					ImagesFragment imagesFragment = (ImagesFragment) fragment;
					imagesFragment.getAdapter().notifyDataSetChanged();
				}
			}
		});
		mImageLoader.DisplayImage(path, imageView);
		mLlPreview.addView(imageView, lp);
		IMGS.put(path, imageView);
		mImagePath.add(path);
		mBtnOK.setText("确认\n" + mImagePath.size() + "/" + MAXSIZE);
	}

	public void removeFromPreview(String path) {
		mLlPreview.removeView(IMGS.get(path));
		mImagePath.remove(path);
		if (mImagePath.size() > 0) {
			mBtnOK.setText("确认\n" + mImagePath.size() + "/" + MAXSIZE);
		} else {
			mBtnOK.setText("确认");
		}
	}
}
