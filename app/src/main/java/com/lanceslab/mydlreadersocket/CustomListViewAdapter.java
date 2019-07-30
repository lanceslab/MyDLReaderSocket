package com.lanceslab.mydlreadersocket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.countDown;
import static android.R.attr.data;

/**
 * Created by LanceTaylor on 4/10/2017.
 */

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    private static final String TAG = "LancesLab ViewAdapter";
    Context context;

    public CustomListViewAdapter(Context context, int resourceId, //resourceId=your layout
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        //CheckBox selsectItem;


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            //holder.selsectItem = (CheckBox) convertView.findViewById(R.id.selected_Item);
            convertView.setTag(holder);
        } else
        holder = (ViewHolder) convertView.getTag();
        //holder.selsectItem.setText(rowItem.getTitle());
        holder.txtTitle.setText(rowItem.getTitle());

//        HashMap<String, String> mapData = new HashMap<String, String>();
//        mapData = data.get(position);

        //holder.txtTitle.setText(rowItem.getTitle());
        //holder.imageView.setImageResource(rowItem.getImageId());

        //holder.imageView.setImageBitmap(decodeSampledBitmapFromFile(rowItem.getDesc(), 500, 250));
        holder.imageView.setImageBitmap(decodeSampledBitmapFromFile(rowItem.getDesc(), 700, 350));


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isChecked = ((CheckBox)arg0).isChecked();
                //final boolean isChecked = holder.selsectItem.isChecked();
                // Do something here.

            }
        });

//        holder.selsectItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                boolean isChecked = ((CheckBox)arg0).isChecked();
//                //final boolean isChecked = holder.selsectItem.isChecked();
//                // Do something here.
//
//            }
//        });

        return convertView;
    }



    private void doButtonOneClickActions(TextView txtQuantity, int rowNumber) {
//    ...
        //data.remove(rowNumber);
        notifyDataSetChanged();

        if(mOnDataChangeListener != null){
            //mOnDataChangeListener.onDataChanged(data.size());
        }
    }



    public interface OnDataChangeListener{
        public void onDataChanged(int size);
    }


    //add a setter for the listener (also in the adapter)
    OnDataChangeListener mOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }


    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) { // BEST QUALITY MATCH

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
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }




}


//
//public class CustomListViewAdapter extends ArrayAdapter<RowItem> {
//
//    Context context;
//
//    public CustomListViewAdapter(Context context, int resourceId,
//                                 List<RowItem> items) {
//        super(context, resourceId, items);
//        this.context = context;
//    }
//
//    /*private view holder class*/
//    private class ViewHolder {
//        ImageView imageView;
//        TextView txtTitle;
//        TextView txtDesc;
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        RowItem rowItem = getItem(position);
//
//        LayoutInflater mInflater = (LayoutInflater) context
//                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.list_item, null);
//            holder = new ViewHolder();
//            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
//            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
//            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
//            convertView.setTag(holder);
//        } else
//            holder = (ViewHolder) convertView.getTag();
//
//        holder.txtDesc.setText(rowItem.getDesc());
//        holder.txtTitle.setText(rowItem.getTitle());
//        holder.imageView.setImageResource(rowItem.getImageId());
//
//        return convertView;
//    }
//}
