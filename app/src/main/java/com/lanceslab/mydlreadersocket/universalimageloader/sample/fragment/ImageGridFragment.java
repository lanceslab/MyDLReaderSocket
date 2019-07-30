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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.DisplayImageOptions;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.ImageLoader;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.assist.FailReason;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.listener.ImageLoadingProgressListener;
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
public class ImageGridFragment extends AbsListViewBaseFragment {

	public static final int INDEX = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fr_image_grid, container, false);
		listView = (GridView) rootView.findViewById(R.id.grid);
		((GridView) listView).setAdapter(new ImageAdapter(getActivity()));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startImagePagerActivity(position);
			}
		});
		return rootView;
	}

	private static class ImageAdapter extends BaseAdapter {

		private static final String[] IMAGE_URLS = Constants.IMAGES;

		private LayoutInflater inflater;

		private DisplayImageOptions options;

		ImageAdapter(Context context) {
			inflater = LayoutInflater.from(context);

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_stub)
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
		}

		@Override
		public int getCount() {
			String[] p = getSdcardImages();
			return p.length;
			//return IMAGE_URLS.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}


			String[] imgUrls = getSdcardImages();
			ImageLoader.getInstance()
					.displayImage(imgUrls[position], holder.imageView, options, new SimpleImageLoadingListener() {
//					.displayImage(IMAGE_URLS[position], holder.imageView, options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri, View view, int current, int total) {
							holder.progressBar.setProgress(Math.round(100.0f * current / total));
						}
					});

			return view;
		}
	}

	static class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}
}