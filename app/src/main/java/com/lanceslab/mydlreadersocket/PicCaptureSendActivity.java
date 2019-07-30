package com.lanceslab.mydlreadersocket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lanceslab.mydlreadersocket.universalimageloader.sample.Constants;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.activity.HomeActivity;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.activity.SimpleImageActivity;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.ImageLoader;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.ImageLoaderConfiguration;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.fragment.ImageListFragment;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.utils.L;

/**
 * Created by LanceTaylor on 4/9/2017.
 */
public class PicCaptureSendActivity extends Activity implements View.OnClickListener {


    private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
    private static final String TAG = "Camera Capture Activity";
    private CompoundButton takePic;
    private CompoundButton choosePic;
    private TextView statusMessage;
    private TextView barcodeValue;
    String imgPath;
    ImageView img;
    boolean isKitKat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_send);


        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);
        //lanceAdd = (TextView)findViewById(R.id.lanceadd);
        takePic = (CompoundButton) findViewById(R.id.take_pic);
        choosePic = (CompoundButton) findViewById(R.id.choose_pic);

        findViewById(R.id.get_images).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        takePic.setChecked(false);
        choosePic.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.get_images) {

            if(takePic.isChecked() && choosePic.isChecked()){
                takePic.setChecked(false);
                choosePic.setChecked(false);
                Toast radioToast = Toast.makeText(PicCaptureSendActivity.this, "You cant have both Radios CHECKED", Toast.LENGTH_LONG);
                radioToast.show();
                return;
            } else if(takePic.isChecked()){
                // Start Activity for Drivers License
//                Intent intent = new Intent(PicCaptureSendActivity.this, AndroidCameraApi.class);
//                startActivity(intent);
               //Intent intent = new Intent(PicCaptureSendActivity.this, CameraActivity.class);
//                 Intent intent = new Intent(PicCaptureSendActivity.this, PhotoViewerActivity.class);
//              Intent intent = new Intent(PicCaptureSendActivity.this, FaceTrackerActivity.class);
                //startActivity(intent);
//                Intent intent = new Intent(PicCaptureSendActivity.this, LicenseActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(PicCaptureSendActivity.this, LicenseActivity.class);
                startActivity(intent);
            } else if(choosePic.isChecked()){
//                        mPlayer = MediaPlayer.create(MainActivity.this, R.raw.ironmanunauthorized);
//                        mPlayer.start();
                // Start Activity for VIN Numbers
//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    isKitKat = true;
//                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/*");
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
//                } else {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
//         //       }
                //Intent intent = new Intent(PicCaptureSendActivity.this, ImageSwipePicker.class);
                //Intent intent = new Intent(PicCaptureSendActivity.this, GetSavedPicsFromSdCard.class);
                //Intent intent = new Intent(PicCaptureSendActivity.this, ListViewWithImageAndText.class);
                //Intent intent = new Intent(PicCaptureSendActivity.this, SendPicsAsyncAtask.class);

                //Intent intent = new Intent(PicCaptureSendActivity.this, CustomGalleryActivity.class);
                //Intent intent = new Intent(PicCaptureSendActivity.this, AndroidCameraApi.class);
                //Intent intent = new Intent(PicCaptureSendActivity.this, ImagePickActivity.class);
                //Intent intent = new Intent(PicCaptureSendActivity.this, MakePhotoActivity.class);
//                startActivity(intent);
//                Toast vinToast = Toast.makeText(PicCaptureSendActivity.this,
//                        "Taking Pictures", Toast.LENGTH_LONG);
//                vinToast.show();

                Intent intent = new Intent(this, SimpleImageActivity.class);
                intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageListFragment.INDEX);
                intent.putExtra(Constants.Extra.IMAGE_POSITION, 0);
                startActivity(intent);
            }
        }

    }


    @TargetApi(19)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getData() != null && resultCode == RESULT_OK) {
            boolean isImageFromGoogleDrive = false;
            Uri uri = data.getData();

            if (isKitKat && DocumentsContract.isDocumentUri(PicCaptureSendActivity.this, uri)) {
                if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        imgPath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    else {
                        Pattern DIR_SEPORATOR = Pattern.compile("/");
                        Set<String> rv = new HashSet<>();
                        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
                        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
                        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
                        if(TextUtils.isEmpty(rawEmulatedStorageTarget))
                        {
                            if(TextUtils.isEmpty(rawExternalStorage))
                            {
                                rv.add("/storage/sdcard0");
                            }
                            else
                            {
                                rv.add(rawExternalStorage);
                            }
                        }
                        else
                        {
                            String rawUserId;
                            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
                            {
                                rawUserId = "";
                            }
                            else
                            {
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                String[] folders = DIR_SEPORATOR.split(path);
                                String lastFolder = folders[folders.length - 1];
                                boolean isDigit = false;
                                try
                                {
                                    Integer.valueOf(lastFolder);
                                    isDigit = true;
                                }
                                catch(NumberFormatException ignored)
                                {
                                }
                                rawUserId = isDigit ? lastFolder : "";
                            }
                            if(TextUtils.isEmpty(rawUserId))
                            {
                                rv.add(rawEmulatedStorageTarget);
                            }
                            else
                            {
                                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
                            }
                        }
                        if(!TextUtils.isEmpty(rawSecondaryStoragesStr))
                        {
                            String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                            Collections.addAll(rv, rawSecondaryStorages);
                        }
                        String[] temp = rv.toArray(new String[rv.size()]);
                        for (int i = 0; i < temp.length; i++)   {
                            File tempf = new File(temp[i] + "/" + split[1]);
                            if(tempf.exists()) {
                                imgPath = temp[i] + "/" + split[1];
                            }
                        }
                    }
                }
                else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    String id = DocumentsContract.getDocumentId(uri);
                    Uri contentUri = ContentUris.withAppendedId( Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = { column };
                    try {
                        cursor = PicCaptureSendActivity.this.getContentResolver().query(contentUri, projection, null, null,
                                null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(column);
                            imgPath = cursor.getString(column_index);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                }
                else if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{ split[1] };

                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = { column };

                    try {
                        cursor = PicCaptureSendActivity.this.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(column);
                            imgPath = cursor.getString(column_index);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                }
                else if("com.google.android.apps.docs.storage".equals(uri.getAuthority()))   {
                    isImageFromGoogleDrive = true;
                }
            }
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                Cursor cursor = null;
                String column = "_data";
                String[] projection = { column };

                try {
                    cursor = PicCaptureSendActivity.this.getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(column);
                        imgPath = cursor.getString(column_index);
                    }
                }
                finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imgPath = uri.getPath();
            }

            if(isImageFromGoogleDrive)  {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    img.setImageBitmap(bitmap);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else    {
                File f = new File(imgPath);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bmOptions);
                img.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void copyTestImageToSdCard(final File testImageOnSdCard) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = getAssets().open(TEST_FILE_NAME);
                    FileOutputStream fos = new FileOutputStream(testImageOnSdCard);
                    byte[] buffer = new byte[8192];
                    int read;
                    try {
                        while ((read = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                        }
                    } finally {
                        fos.flush();
                        fos.close();
                        is.close();
                    }
                } catch (IOException e) {
                    L.w("Can't copy test image onto SD card");
                }
            }
        }).start();
    }


}

