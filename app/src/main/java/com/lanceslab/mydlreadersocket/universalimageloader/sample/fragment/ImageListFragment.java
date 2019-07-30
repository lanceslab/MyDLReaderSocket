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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.lanceslab.mydlreadersocket.*;

//import com.lanceslab.mydlreadersocket.Client;
import com.lanceslab.mydlreadersocket.ClientLate;
import com.lanceslab.mydlreadersocket.LicenseActivity;
import com.lanceslab.mydlreadersocket.MainActivity;
import com.lanceslab.mydlreadersocket.SendPicsAsyncAtask;
import com.lanceslab.mydlreadersocket.SwipeDismissListViewTouchListener;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.RealFilePath;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.DisplayImageOptions;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.ImageLoader;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.ImageLoaderConfiguration;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.display.CircleBitmapDisplayer;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.display.FadeInBitmapDisplayer;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.listener.ImageLoadingListener;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.core.listener.SimpleImageLoadingListener;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.Constants;
import com.lanceslab.mydlreadersocket.R;
//import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


public class ImageListFragment extends AbsListViewBaseFragment {
    public static String[] imageFilesToTransfer;//
    public String[] imageFilesToFolder;
    List<String> imageFilesAddRemove;
    String[] filesToUpload;//
    public static final String CAMERA_IMAGE_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString()
                    + "/DCIM/Camera";
    public static final String CAMERA_IMAGE_BUCKET_ID =
            getBucketId(CAMERA_IMAGE_BUCKET_NAME);
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "Results Activity";
    private final int PORT = 11000;
    public static final int SHUTDOWN = 1;
    public static final int ERROR = 2;
    public static final int SENT = 3;
    public static final int SENDING = 4;
    public static final int PENDING = 7;
    public static final int CONNECTING = 5;
	public static final int INDEX = 0;
    private static int fileCount = 0;
    private static int currentFile = 0;
    private static boolean hasFilesBeenSent = false;
    String ipToConnectTo = "";
    public  boolean isAsyncTaskRunning = false;
    SendPicsAsyncAtask socketAsync;
    ImageAdapter mAdapter;
    private Handler mHandler;
    MediaPlayer mPlayer;
    Button btnSend;
    EditText etIP;
    TextView statusText, tvCount;
    String[] defaultCamImgs;// List<String> defaultCamImgs;

//==================================================  onCreateView  =====================================================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_image_list, container, false);
		listView = (ListView) rootView.findViewById(android.R.id.list);

        // =======================PERMISSION STUFF==============================
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        int permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);//REQUEST_READWRITE_STORAGE
        }
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code

            } else {
                requestPermission(); // Code for permission
            }
        }
// ==========================================PERMISSION STUFF==========================================================================


        defaultCamImgs = getLastPicsTaken(getContext());
        imageFilesToFolder = defaultCamImgs;
        defaultCamImgs = imageFilesToFolder;
        saveImageFiles();
        mAdapter = new ImageAdapter(getActivity(), defaultCamImgs);

        int poopCount = defaultCamImgs.length;//
        socketAsync = new SendPicsAsyncAtask();
        listView.setAdapter(mAdapter);
        etIP = (EditText) rootView.findViewById(R.id.etIPAddress);
        //etIP.setText("192.168.1.15");

        btnSend = (Button) rootView.findViewById(R.id.button_send);
        statusText = (TextView) rootView.findViewById(R.id.status_message);
        tvCount = (TextView) rootView.findViewById(R.id.text_count);
        fileCount = mAdapter.getCount();//
        tvCount.setText(String.valueOf(fileCount));
//====================================================================================================================================
        mHandler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case SHUTDOWN://1
                        Log.d(TAG, "In Handler's shutdown");
                        //isAsyncTaskRunning = false;
                        statusText.setText(" Done!");
                        currentFile++;

                        Log.d(TAG, "Current Number: " + currentFile + "  Count " + fileCount);
                        if(currentFile < fileCount){
                            isAsyncTaskRunning = true;
                            tvCount.setText(String.valueOf(currentFile + 1) + " of " + fileCount);
                            socketAsync = new SendPicsAsyncAtask();//
                            //socketAsync.SendPicsAsyncAtask3(mHandler,(defaultCamImgs[currentFile]), ipToConnectTo);
                            socketAsync.SendPicsAsyncAtask3(mHandler,(filesToUpload[currentFile]), ipToConnectTo);
                            socketAsync.SetTextView(statusText);
                            socketAsync.execute();
                        }else {
                            isAsyncTaskRunning = false;
                            socketAsync = null;
                            hasFilesBeenSent = true;
                            mPlayer = MediaPlayer.create(getActivity(), R.raw.jarvistherenderiscomplete2);
                            mPlayer.start();
                        }
                        break;
                    case ERROR://2
                        statusText.setText("Error");
                        isAsyncTaskRunning = false;
                        Log.d(TAG, "ERROR SENDING: " + msg.toString());
                        break;
                    case SENT://3
                        statusText.setText("SENT");

                        isAsyncTaskRunning = false;
                        Log.d(TAG, "SENT: " + msg.toString());
                        break;
                    case SENDING:// 4

                        break;
                    case CONNECTING:// 5
                        statusText.setText("CONNECTING");

                        break;
                    case PENDING:// 7:
                        statusText.setText("Sending percent: " + msg.arg1 + "%");
                        break;
                    default:
                        statusText.setText("Not in list??");
                        break;
                }
            }
        };
// ON CLICK HANDLER
        btnSend.setOnClickListener(new View.OnClickListener(){
                            public void onClick(View v){

            filesToUpload = mAdapter.getPicUpload(); //= getSdcardImages();

            if(hasFilesBeenSent == false) {
                ipToConnectTo = etIP.getText().toString();
                mPlayer = MediaPlayer.create(getActivity(), R.raw.jarvisestcompletationtime);
                mPlayer.start();
                System.out.println("Send Images Async Starting");
                String file_name = filesToUpload[currentFile];//filesToUpload //defaultCamImgs
                tvCount.setText(String.valueOf(currentFile + 1) + " of " + fileCount);
                socketAsync.SendPicsAsyncAtask3(mHandler, (file_name), ipToConnectTo);
                if (isAsyncTaskRunning == false) {
                    socketAsync.SetTextView(statusText);
                    socketAsync.execute();// can add params inside here
                    isAsyncTaskRunning = true;
                }
            }else if(hasFilesBeenSent == true) {
                mPlayer = MediaPlayer.create(getActivity(), R.raw.jarviswhaareyoutryingtoacheive);
                mPlayer.start();
            }
            System.out.println("SendPicsAsyncAtask2 executed");

            }
        });// END onClick HANDLER


//  ON CLICK HANDLER SwipeDismissListViewTouchListener
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        (ListView) listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks()
                        {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    fileCount--;
                                    removeItem(position);
                                    tvCount.setText(String.valueOf(fileCount));
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });



        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling, we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
                //AnimateFirstDisplayListener.displayedImages.remove(position);// Did nothing
                //mAdapter.removeAnItem(position);
                //removeItem(position);
                //onResume();
                //startImagePagerActivity(position);
			}
		});
		return rootView;
	}

//==================================================  onCreateView  =====================================================


    public static String[] getLastPicsTaken2(Context c){
        File[] aDirArray = ContextCompat.getExternalFilesDirs(c, null);
        File path = new File(aDirArray[1], Environment.DIRECTORY_DCIM);
        String data=path.toString();
        int index=data.indexOf("Android");
        String subdata=data.substring(0,index);
        File sec=new File(subdata+"DCIM/Camera");
        File[] secs=sec.listFiles();
        String[] listFiles = new String[20];
        int listIndex = 0;
        for(int totalCountIndex = 0; totalCountIndex < 20; totalCountIndex++){
            if(! secs[totalCountIndex].getName().contains(".OutFocus")) {
                if(secs[totalCountIndex].getName().contains(".jpg")) {
                    listFiles[totalCountIndex] = secs[totalCountIndex].getAbsolutePath();
                }
            }
        }
//        for (File f: secs) {
//            listFiles[listIndex] = f.getAbsolutePath();
//            listIndex++;
//        }
        return listFiles;
    }

    public static String[] getLastPicsTaken(Context c){


        String rootPath = "file://";
        final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATA };
        final String imageOrderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC LIMIT 5";

        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeTypejpg = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg");
        String mimeTypepng = MimeTypeMap.getSingleton().getMimeTypeFromExtension("png");
        String[] selectionArgsJpg = new String[]{ mimeTypejpg };
        //String[] selectionArgsJpg = new String[]{ mimeTypejpg, mimeTypejpg };
        // Find the last picture//
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };// like image columns
        //
        final Cursor cursor = c.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , projection, selectionMimeType, selectionArgsJpg, imageOrderBy);

        //final Cursor cursor = c.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC LIMIT 10");

        String[] result = new String[cursor.getCount()];
        imageFilesToTransfer = new String[cursor.getCount()];
        int index = 0;
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            final int mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
            do {
                final String data = cursor.getString(dataColumn);
                final String mimeTyp = cursor.getString(mimeTypeColumn); //.getType()


//                if(mimeType.contains("image/png")){
//                    continue;
//                    //break;
//                }

  /* =================             create and add the Bitmap image here                 ===========================-?????????????????*/
                //result.add(data);
                result[index] = rootPath + data;// adds the fill:// in front
                imageFilesToTransfer[index] = data;
                index++;
            } while (cursor.moveToNext());
            System.out.println("Image Cursor Count: " + String.valueOf(index));
        }
        cursor.close();

        return result;
    }

    public void saveImageFiles(){
        String pathToSave = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload/";
        for (String imageFile: defaultCamImgs) {
            String imageToString = imageFile;
            Bitmap tempImage;
            FileOutputStream outputStream;
            File inFile = new File(imageFile);
            long inFileSize = inFile.length();
            String inFileName = inFile.getName();
            File outFile = new File(pathToSave, inFileName);
            try {

                try (InputStream is = new URL(imageFile /*file_url*/).openStream()) {
                    tempImage = BitmapFactory.decodeStream(is);
                }
                outputStream = new FileOutputStream(outFile);//
                Log.d(TAG, "Compressing new file at 70% quality");
                Log.d(TAG, "Size before compressing " + String.valueOf(inFileSize));


                if(inFileName.contains(".png")){
                    tempImage.compress(Bitmap.CompressFormat.PNG, 20, outputStream);
                } else {
                    tempImage.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                }
                //tempImage.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                //tempImage.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                //tempImage.compress(Bitmap.CompressFormat.PNG, 70, outputStream);// Made it much BIGGER
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "ERROR FileNotFoundException " + e.toString());
            } catch (IOException ex) {
                Log.d(TAG, "Error closing FileOutputStream " + ex.toString());
            }

            if (!outFile.exists()) {
                try {
                    outFile.createNewFile();
                    Log.d(TAG, "Creating new file " + outFile.getName());
                } catch (IOException e) {
                    Log.d(TAG, "Error creating new file.");
                }
            }
            long compressedFileSize = outFile.length();
            Log.d(TAG, "Size after compressing " + String.valueOf(compressedFileSize));
        }
        //mAdapter.notifyDataSetChanged();
    }


    public static String generatePath(Uri uri,Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if(isKitKat){
            filePath = generateFromKitkat(uri,context);
        }

        if(filePath != null){
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    /*@TargetApi(19)*/
    private static String generateFromKitkat(Uri uri,Context context){
        String filePath = null;
        if(DocumentsContract.isDocumentUri(context, uri)){
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = { MediaStore.Images.Media.DATA };
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{ id }, null);



            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    /*Base64.encodeToString*/
    private String encodeImage(String path){
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        Log.d(TAG, "Encoding Image to 64 bit string");
        return encImage;

    }

    public void  removeItem(int position){
        mAdapter.removeAnItem(position);
        //String pathToDelete = defaultCamImgs[position];
        //mAdapter.removeAnItem(pathToDelete);
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		AnimateFirstDisplayListener.displayedImages.clear();
	}


//=======================================ImageAdapter====================================================================
	private static class ImageAdapter extends BaseAdapter {
        public static Context thisContext;
		/*FROM CONSTANTS CLASS*/
		//"file:///sdcard/Universal Image Loader @#&=+-_.,!()~'%20.png", // Image from SD card with encoded symbols
		private static final String[] IMAGE_URLS = Constants.IMAGES;
		private LayoutInflater inflater;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private String[] imageFileUrls;
		private DisplayImageOptions options;

		ImageAdapter(Context context) {
			inflater = LayoutInflater.from(context);
            thisContext = context;
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_stub)
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
					.build();
		}

        ImageAdapter(Context context, String[] imgs) {
            inflater = LayoutInflater.from(context);
            thisContext = context;
            imageFileUrls = imgs;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                    .build();
        }

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

        public void  removeAnItem(String pth){

            File file = new File(pth);
            String tName = file.getName();
            if (file.isDirectory()) {
                return;
            }
            else if(file.exists())            {
                file.delete();
                file.deleteOnExit();
            }else{
                return;
            }

        }


        public void  setImageUrls(String[] iUrls){
            imageFileUrls = iUrls;
         }


		public void  removeAnItem(int position){

            String pathToDelete = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload/";
            String rootPath = "file://";
            File fileDelete = new File(imageFilesToTransfer[position]);
            String aPathpooopp = imageFilesToTransfer[position];
            String aPath = fileDelete.getAbsolutePath();
            String fileDelName = fileDelete.getName();
            fileDelete = fileDelete.getAbsoluteFile();
            if (fileDelete.exists()) {
                try {
                    fileDelete.getAbsoluteFile().delete();
                    fileDelete.delete();
                    fileDelete.deleteOnExit();
                    Log.d(TAG, "Deleting file " + fileDelName);
                } catch (Exception e) {
                    Log.d(TAG, "Error Deleting file.");
                }
            }
            File fileDelete2 = new File(pathToDelete, fileDelName);
            //File fileDelete2 = new File(rootPath + pathToDelete, fileDelName);
            fileDelete2 = fileDelete2.getAbsoluteFile();

            if (fileDelete2.exists()) {
                try {
                    fileDelete2.getAbsoluteFile().delete();
                    fileDelete2.delete();
                    fileDelete2.deleteOnExit();
                    Log.d(TAG, "Deleting file " + fileDelName);
                } catch (Exception e) {
                    Log.d(TAG, "Error Deleting file.");
                }
            }

            imageFileUrls = getPicsToUpload();
            imageFilesToTransfer = imageFileUrls;

        }

        public String[] getPicUpload() {
            String rootPath = "file://";
            String path = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload/";
            File fp = new File(path);//
            List<File> filesList = getListFiles(fp);
            String[] imageFs;
            int listLength = filesList.size();
            imageFs = new String[listLength];
            if(listLength > 0){
                int index = 0;
                for (File imgFile: filesList) {
                    if(imgFile.getName().contains(".jpg")) {
                        String fileName = imgFile.getName();
                        //imageFs[index] = imgFile.getName();
                        imageFs[index] = imgFile.getAbsolutePath();

                        index++;
                    }
                }
                return imageFs;
            }
            return null;
        }


        public String[] getPicsToUpload() {
            String rootPath = "file://";
            String path = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload/";
            File fp = new File(path);//
            List<File> filesList = getListFiles(fp);
            String[] imageFs;
            int listLength = filesList.size();
            imageFs = new String[listLength];
            if(listLength > 0){
                int index = 0;
                for (File imgFile: filesList) {
                    if(imgFile.getName().contains(".jpg")) {
                        String fileName = imgFile.getName();
                        //imageFs[index] = imgFile.getName();
                        imageFs[index] = rootPath + imgFile.getAbsolutePath();

                        index++;
                    }
                }
                return imageFs;
            }
            return null;
        }


        public String[] getSdcardImageNames() {
            String path = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload";
            String rootPath = "file:///sdcard/LancesLab/PicsToUpload/";
            File fp = new File(path);
            List<File> filesList = getListFiles(fp);
            String[] imageFs;
            int listLength = filesList.size();
            imageFs = new String[listLength];

            if(listLength > 0){
                int index = 0;
                for (File imgFile: filesList) {
                    String fileName = imgFile.getName();
                    //imageFs[index] = rootPath + imgFile.getName();
                    imageFs[index] = imgFile.getName();
                    index++;
                }
                return imageFs;
            }
            return null;
        }


        //public void getSdcardImages() {//DCIM/Camera
		public String[] getSdcardImages() {
            String path = Environment.getExternalStorageDirectory().toString()+"/DCIM";
            //String path = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload/";
            //String rootPath = "file:///sdcard/LancesLab/PicsToUpload/";
            //String rootPath = "file:///sdcard/DCIM/";
            String rootPath = "file://";
			File fp = new File(path);//
            //File fp = new File(rootPath);

            List<File> filesList = getListFiles(fp);
			String[] imageFs;
			int listLength = filesList.size();
			imageFs = new String[listLength];

			if(listLength > 0){
				int index = 0;
				for (File imgFile: filesList) {
                    if(imgFile.getName().contains(".jpg")) {
                            String fileName = imgFile.getName();
                            //imageFs[index] = rootPath + imgFile.getName();
                        imageFs[index] = rootPath + imgFile.getAbsolutePath();
                            //imageFs[index] = path + imgFile.getName();
                            //imageFs[index] = imgFile.getAbsolutePath();
                            //imageFs[index] = imgFile.getName();
                            index++;

                    }
				}
				return imageFs;
			}
			return null;
		}

		//Try the following code which may help you
		List<File> getListFiles(File parentDir) {
			ArrayList<File> inFiles = new ArrayList<File>();
			File[] files = parentDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
                    if(!file.getName().contains(".thumbnails")) {
                        inFiles.addAll(getListFiles(file));
                    }
				} else {
					inFiles.add(file);
				}
			}
			return inFiles;
		}

        @Override
        public int getCount() {
            String[] p = getPicsToUpload();
            return p.length;
        }


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

            int checkBoxCounter = 0;
            int checkBoxInitialized = 0;
            View view = convertView;
			//final ViewHolder holder;// = new ViewHolder();
             final ViewHolder holder;// = new ViewHolder();
			if (convertView == null) {
				view = inflater.inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
                //holder.text = (TextView) view.findViewById(R.id.text);
				holder.image = (ImageView) view.findViewById(R.id.image);
                holder.cbKeeper = (CheckBox) view.findViewById(R.id.keeper_to_send);

//                holder.cbKeeper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        Model element = (Model) holder.cbKeeper.getTag();
//                        element.setSelected(buttonView.isChecked());
//
////                        if(checkBoxCounter <= checkBoxInitialized){
////                            // increment counter, when we scroll the List it execute onCheckedChanged everytime so by using this stuff we can maintain the state
////                            checkBoxCounter++;
////                        }
////                        else{
////                            Model element = (Model) viewHolder.checkbox.getTag();
////                            element.setSelected(buttonView.isChecked());
////
////                            if(element.isSelected())
////                                Toast.makeText(getContext(), "You selected "+ element.getName(), Toast.LENGTH_LONG).show();
////                            else
////                                Toast.makeText(getContext(), "Not selected "+ element.getName(), Toast.LENGTH_LONG).show();
////                        }
//                    }
//                });
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

            String[] imgUrls = getPicsToUpload();
            //String[] imgUrls = getLastPicsTaken(thisContext);
            //String[] imgUrls = imageFileUrls;

            int pp = imgUrls.length;//
            File tempF = new File(imgUrls[position]);
            holder.cbKeeper.setText("Image Name " + tempF.getName());
            holder.image.setImageBitmap(decodeSampledBitmapFromFile(imgUrls[position], 300, 300));
            ImageLoader theImageLoader = ImageLoader.getInstance();
            theImageLoader.init(ImageLoaderConfiguration.createDefault(view.getContext()));
            theImageLoader.displayImage(imgUrls[position], holder.image, options, animateFirstListener);
            //theImageLoader.displayImage("", holder.image, options, animateFirstListener);
			return view;
		}

	}
//=======================================ImageAdapter====================================================================

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        // BEST QUALITY MATCH
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize// Raw height and width of image
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

    public static void saveImageFile(String path, Context c){
        String pathToSave = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload/";

        String imageToString = path;
        Bitmap tempImage;
        FileOutputStream outputStream;
        File inFile = new File(path);
        long inFileSize = inFile.length();
        String inFileName = inFile.getName();
        File outFile = new File(pathToSave, inFileName);
        try {

            try ( InputStream is = new URL( path /*file_url*/ ).openStream() ) {
                tempImage = BitmapFactory.decodeStream( is );
            }
            outputStream = new FileOutputStream(outFile);//
            Log.d(TAG, "Compressing new file at 70% quality");
            Log.d(TAG, "Size before compressing " + String.valueOf(inFileSize));
            tempImage.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
            //tempImage.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
            //tempImage.compress(Bitmap.CompressFormat.PNG, 70, outputStream);// Made it much BIGGER
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "ERROR FileNotFoundException " + e.toString());
        } catch (IOException ex) {
            Log.d(TAG, "Error closing FileOutputStream " + ex.toString());
        }

        if (!outFile.exists()) {
            try {
                outFile.createNewFile();
                Log.d(TAG, "Creating new file " + outFile.getName());
            } catch (IOException e) {
                Log.d(TAG, "Error creating new file.");
            }
        }
        long compressedFileSize = outFile.length();
        Log.d(TAG, "Size after compressing " + String.valueOf(compressedFileSize));
    }


    static class ViewHolder {
        //TextView text;
		ImageView image;
        CheckBox cbKeeper;
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

    //String MEDIA_PATH = android.os.Environment.getExternalStorageDirectory()
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


    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    //public static List<String> getCameraImages(Context context) {// RETURNED ZERO COUNT
    public static String[] getCameraImages(Context context) {
        final String[] projection = { MediaStore.Images.Media.DATA };
        final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        final String[] selectionArgs = { CAMERA_IMAGE_BUCKET_ID };
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        //ArrayList<String> result = new ArrayList<String>(cursor.getCount());
        String[] result = new String[cursor.getCount()];
        int index = 0;
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                final String data = cursor.getString(dataColumn);
                //result.add(data);
                result[index] = data;
                index++;
            } while (cursor.moveToNext());
            System.out.println("Image Cursor Count: " + String.valueOf(index));
        }
        cursor.close();
        return result;
    }





}



