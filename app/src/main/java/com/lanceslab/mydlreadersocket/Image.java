package com.lanceslab.mydlreadersocket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashSet;

//import static android.app.PendingIntent.getActivity;

/**
 * Created by LanceTaylor on 4/9/2017.
 */

public class Image // implements Parcelable {
{

    public Uri uri;
    public int orientation;

    public Image(Uri mUri, int mOrientation) {
        uri = mUri;
        orientation = mOrientation;
    }

/*    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.uri, 0);
        dest.writeInt(this.orientation);
    }*/






    public void saveImage(Bitmap bitmap, String fileName) {
//        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, fileName, null);
//        Uri contentUri = Uri.parse(path);
//        final Image image = getImageFromContentUri(contentUri);

//        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri));
//
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ((TheImagePickerActivity) getActivity()).addImage(image);
//            }
//        });
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
////        super.onSaveInstanceState(outState);
//
//        ArrayList<Image> list = new ArrayList<>(arraySelectedImages);
//        outState.putParcelableArrayList("", list);
//    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        populateUi(savedInstanceState);
//    }

//    private void populateUi(Bundle savedInstanceState) {
//        ArrayList<Image> arrayImages = savedInstanceState.getParcelableArrayList("");
//
//        if (arrayImages != null) {
//            for (Image image : arrayImages) {
//                addImage(image);
//            }
//        }
//    }

    public boolean addImage(Image image) {

//        if (arraySelectedImages == null) {
//            arraySelectedImages = new HashSet<>();
//        }
//
//
//        if (arraySelectedImages.add(image)) {
//            // Do Something
//            return true;
//
//        }

        return false;
    }












}
