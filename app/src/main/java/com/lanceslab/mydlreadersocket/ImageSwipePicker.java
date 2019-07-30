package com.lanceslab.mydlreadersocket;


import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LanceTaylor on 4/10/2017.
 */

public class ImageSwipePicker extends ListActivity {



    ArrayAdapter<String> mAdapter;
    List<RowItem> itemsForRows = new ArrayList<RowItem>();
    ArrayList<String> f = new ArrayList<String>();
    CustomListViewAdapter custAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.listview);
        setContentView(R.layout.image_swiper_picker);
        getSdcardImages();
        //ListView lView = (ListView)findViewById(R.id.list);

        // Set up ListView example
        String[] items = new String[20];
        for (int i = 0; i < items.length; i++) {
            items[i] = "Item " + (i + 1);
        }

//        mAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1,
//                android.R.id.text1,
//                new ArrayList<String>(Arrays.asList(items)));

//        mAdapter = new ArrayAdapter<String>(this,
//                R.layout.image_items,
//                R.id.customimageView1,
//                f);


//        mAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1,
//                android.R.id.text1,
//                f);


        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                f);


//CustomListViewAdapter(Context context, int resourceId, List<RowItem> items)


        //custAdapter = new CustomListViewAdapter(this, R.layout.image_items, itemsForRows);
        //custAdapter = new CustomListViewAdapter(this, android.R.layout.list, itemsForRows);

//        mAdapter = new ArrayAdapter<String>(this,
//                R.layout.image_items,
//                R.id.customimageView1,
//                f);


        //setListAdapter(custAdapter);
        setListAdapter(mAdapter);

        ListView listView = getListView();
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mAdapter.remove(mAdapter.getItem(position));
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling, we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
        // Set up normal ViewGroup example
        final ViewGroup dismissableContainer = (ViewGroup) findViewById(R.id.dismissable_container);
        //for (int i = 0; i < items.length; i++) {
        for (int i = 0; i < f.size() - 1; i++) {
            //final Button dismissableButton = new Button(this);
            //holder.imageView.setImageBitmap(decodeSampledBitmapFromFile(rowItem.getDesc(), 700, 350));
            final ImageView dismissableButton = new ImageView(this);
            dismissableButton.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            dismissableButton.setLayoutParams(new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //dismissableButton.setText("Button " + (i + 1));
            dismissableButton.setImageBitmap(decodeSampledBitmapFromFile(f.get(i), 600, 300));
            dismissableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(ImageSwipePicker.this,
//                            "Clicked " + ((Button) view).getText(),
//                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(ImageSwipePicker.this,
                            "Clicked " + ((ImageView) view).toString(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            // Create a generic swipe-to-dismiss touch listener.
            dismissableButton.setOnTouchListener(new SwipeDismissTouchListener(
                    dismissableButton,
                    null,
                    new SwipeDismissTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(Object token) {
                            return true;
                        }

                        @Override
                        public void onDismiss(View view, Object token) {
                            dismissableContainer.removeView(dismissableButton);
                        }
                    }));
            dismissableContainer.addView(dismissableButton);
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Toast.makeText(this,
                "Clicked " + getListAdapter().getItem(position).toString(),
                Toast.LENGTH_SHORT).show();
    }



    public void getSdcardImages() {
        String path = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload";
        File fp = new File(path);
        //File filesArray[] = fp.listFiles();
        List<File> filesList = getListFiles(fp);
        //int listLength = filesArray.length;
        int listLength = filesList.size();
        String fNames = "";
        if(listLength > 0){
            int index = 0;
            for (File imgFile: filesList) {
                //f.add(listFile[i].getAbsolutePath());
                fNames += "File name: " + imgFile.getName() + "\n";
                f.add(imgFile.getAbsolutePath());
                RowItem rItem = new RowItem(index, imgFile.getName(), imgFile.getAbsolutePath());
                // RowItem rItem = new RowItem(imgFile., imgFile.getName(), imgFile.toString());
                itemsForRows.add(rItem);
                //itemsForRows.add(index, rItem);
                //f.add(imgFile);
                Log.i("FILES:" + "---", f.toString());
                index++;
            }
        }
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



    public static Bitmap decodeSampledBitmapFromFile(String path,int reqWidth, int reqHeight) {
        // BEST QUALITY MATCH
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
           // Log.e(TAG, "Error getting bitmap", e);
            Toast.makeText(ImageSwipePicker.this,
                "Error getting bitmap \n" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        return bm;
    }





}
