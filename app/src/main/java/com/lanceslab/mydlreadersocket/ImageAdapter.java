package com.lanceslab.mydlreadersocket;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.android.gms.common.images.ImageManager;

/**
 * Created by LanceTaylor on 4/9/2017.
 */

public class ImageAdapter extends BaseAdapter {


    private LayoutInflater myInflater;
    private Bitmap[] bitmapList;

    /**
     * Maintains the state of our data
     */
//    private final ImageManager mImageManager;
//
//    private final Context mContext;
//
//    private final MyDataSetObserver mObserver;


    public ImageAdapter(Context c) {
        myInflater = LayoutInflater.from(c);
    }
//    public ImageAdapter(Context c) {
////        mImageManager = ImageManager.getInstance(c);
//        mImageManager = ImageManager.create(c);
//        mContext = c;
//        mObserver = new MyDataSetObserver();
//        //mImageManager.addObserver(mObserver);// WHY ???????
//    }

    public void setData(Bitmap[] bmImages){
        bitmapList = bmImages;

        Log.i("ContestantsPhotoAdapter", "Images passed to the adapter.");
    }



    /**
     * Used by the {@link ImageManager} to report changes in the list back to
     * this adapter.
     */
    private class MyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            notifyDataSetInvalidated();
        }
    }



    private LayoutInflater mInflater;

    public ImageAdapter() {
       // mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //return f.size();
        //return mImageManager.size();
        return bitmapList.length;
    }

    @Override
    public Object getItem(int position) {
        //return mImageManager.get(position);
        return position;
    }

    @Override
    public long getItemId(int position) {
//        final PanoramioItem panoramioItem = mImageManager.get(position);
//        return panoramioItem.getId();
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;

        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.grid_contestantsphoto, null);
            holder = new ViewHolder();
            holder.imageview = (ImageView) convertView.findViewById(R.id.imvContestantsPhoto_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageview.setImageBitmap(bitmapList[position]);

        return convertView;



//        View view;
//        if (convertView == null) {
//            // Make up a new view
//            final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
//                    Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(R.layout.image_item, null);
//        } else {
//            // Use convertView if it is available
//            view = convertView;
//        }
//        //final PanoramioItem panoramioItem = mImageManager.get(position);
//
//        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
//        imageView.setImageBitmap(panoramioItem.getBitmap());
//        return view;






//        RecyclerView.ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = mInflater.inflate(R.layout.custom_listview_items,
//                    null);
//            holder.imageview = (ImageView) convertView
//                    .findViewById(R.id.customimageView1);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (RecyclerView.ViewHolder) convertView.getTag();
//        }
//
//        Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
//        try {
//
//            holder.imageview.setImageBitmap(getResizedBitmap(myBitmap, 300, 300));
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return convertView;
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    class ViewHolder {
        ImageView imageview;
//android.support.v7.widget.RecyclerView.ViewHolder
        //imageview = (android.support.v7.widget.RecyclerView.ViewHolder)
    }
}


//}
