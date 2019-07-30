package com.lanceslab.mydlreadersocket;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getDataDirectory;

/**
 * Created by LanceTaylor on 4/9/2017.
 */

public class GetSavedPicsFromSdCard extends Activity {

    public static Cursor cursor;
    private int columnIndex;
    private File file;
    private String SD_CARD_ROOT;
    ArrayList<String> f = new ArrayList<String>();
    List<RowItem> itemsForRows = new ArrayList<RowItem>();
    File[] listFile;
    //ImageAdapter adapter;
    CustomListViewAdapter listAdapter;
    Button btProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.sdcard_layout);
          setContentView(R.layout.listview);

        getSdcardImages();

        btProcess = (Button)findViewById(R.id.btnProcess);
        ListView lv1 = (ListView) findViewById(R.id.list_view);
        //adapter = new ImageAdapter(GetSavedPicsFromSdCard.this);//listAdapter
        //CustomListViewAdapter(Context context, int resourceId, //resourceId=your layout List<RowItem> items)
        //RowItem(int imageId, String title, String desc)
        listAdapter = new CustomListViewAdapter(GetSavedPicsFromSdCard.this, lv1.getId(), itemsForRows);
        lv1.setAdapter(listAdapter);
        //lv1.setAdapter(adapter);
        Toast fileToast = Toast.makeText(GetSavedPicsFromSdCard.this,
                ("Working: !!!!!!!!!!!!!!!!"), Toast.LENGTH_LONG);
        fileToast.show();

        btProcess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //listAdapter.

            }
        });

        listAdapter.setOnDataChangeListener(new CustomListViewAdapter.OnDataChangeListener(){
            public void onDataChanged(int size){
                //do whatever here




            }
        });

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



}