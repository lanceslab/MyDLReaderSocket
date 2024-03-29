/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.lanceslab.mydlreadersocket.universalimageloader.sample.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.DisplayImageOptions;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.ImageLoader;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.assist.FailReason;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.assist.ImageScaleType;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.display.FadeInBitmapDisplayer;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.listener.SimpleImageLoadingListener;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.Constants;
//import com.lanceslab.mydlreadersocket.universalimageloader.sample.R;
import com.lanceslab.mydlreadersocket.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImagePagerFragment extends BaseFragment {

	public static final int INDEX = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fr_image_pager, container, false);
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		pager.setAdapter(new ImageAdapter(getActivity()));
		pager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION, 0));
		return rootView;
	}

	private static class ImageAdapter extends PagerAdapter {

		private static final String[] IMAGE_URLS = Constants.IMAGES;

		private LayoutInflater inflater;
		private DisplayImageOptions options;

		ImageAdapter(Context context) {
			inflater = LayoutInflater.from(context);

			options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error)
					.resetViewBeforeLoading(true)
					.cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300))
					.build();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			String[] p = getSdcardImages();
			return p.length;
			//return IMAGE_URLS.length;
		}



		//public void getSdcardImages() {
		public String[] getSdcardImages() {
			String path = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload";
			String rootPath = "file:///sdcard/LancesLab/PicsToUpload/";
			File fp = new File(path);
			List<File> filesList = getListFiles(fp);
			String[] imageFiles;
			int listLength = filesList.size();
			imageFiles = new String[listLength];

			if(listLength > 0){
				int index = 0;
				for (File imgFile: filesList) {
//					listviewTitle[index] = imgFile.getAbsolutePath();
//					listviewImage[index] = index; //imgFile.getAbsoluteFile().hashCode();
//					listviewShortDescription[index] = imgFile.getName();
//					f.add(imgFile.getAbsolutePath());
//					RowItem rItem = new RowItem(index, imgFile.getName(), imgFile.getAbsolutePath());
//					itemsForRows.add(rItem);
					String fileName = imgFile.getName();
					//imageFiles[index] = imgFile.getAbsoluteFile().toString();
					imageFiles[index] = rootPath + imgFile.getName();
					index++;
				}
				return imageFiles;
			}
			return null;
		}

		//Try the following code which may help you
		List<File> getListFiles(File parentDir) {
			ArrayList<File> inFiles = new ArrayList<File>();
			File[] files = parentDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					inFiles.addAll(getListFiles(file));
				} else {
					inFiles.add(file);
				}
			}
			return inFiles;
		}



		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
			String[] imgUrls = getSdcardImages();
			//ImageLoader.getInstance().displayImage(IMAGE_URLS[position], imageView, options, new SimpleImageLoadingListener() {
			ImageLoader.getInstance().displayImage(imgUrls[position], imageView, options, new SimpleImageLoadingListener() {



				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
				}
			});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}