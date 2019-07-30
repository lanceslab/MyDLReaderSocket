package com.lanceslab.mydlreadersocket;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by LanceTaylor on 4/5/2017.
 */

public class SendfileActivity extends Activity {


    DriversLicense licenseInfo = null;
    JSONObject jasonLicenseObject;// = licenseInfo.toJSONObject();
//    String lName, mName, fName, bDate, age, lNum, addStreet, addCity, addState,
//            addZip, gender, issDate, expDate, county, sendResults;
    Button send;// = (Button) findViewById(R.id.bSend);
    /** Called when the activity is first created. */
    TextView lNameTV, mNameTV, fNameTV, bDateTV, ageTV, lNumTV, addStreetTV, addCityTV, addStateTV,
            addZipTV, genderTV, issDateTV, expDateTV, countyTV, status;
    //final TextView status = (TextView) findViewById(R.id.tvStatus);

    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendfile);
        System.out.println("45");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        img = (ImageView) findViewById(R.id.ivPic);
        status = (TextView) findViewById(R.id.tvStatus);
        lNameTV = (TextView)findViewById(R.id.tvLName);
        mNameTV = (TextView)findViewById(R.id.tvMName);
        fNameTV = (TextView)findViewById(R.id.tvFName);
        bDateTV = (TextView)findViewById(R.id.tvDob);
        lNumTV = (TextView)findViewById(R.id.tvLicNum);
        addStreetTV = (TextView)findViewById(R.id.tvStreet);
        addCityTV = (TextView)findViewById(R.id.tvCity);
        addStateTV = (TextView)findViewById(R.id.tvState);
        addZipTV = (TextView)findViewById(R.id.tvZip);
        genderTV = (TextView)findViewById(R.id.tvGender);
        System.out.println("47");
        send = (Button) findViewById(R.id.bSend);
        // Get Data from Extras sent from previous Activity
        Intent thisIntent = getIntent();
        Bundle bd = thisIntent.getExtras();
        /*      Textvews Set Text statements*/
        if(bd != null) {
           licenseInfo = (DriversLicense) bd.get("license");
           lNameTV.setText(licenseInfo.getLName());
            mNameTV.setText(licenseInfo.getMName());
            fNameTV.setText(licenseInfo.getFName());
            bDateTV.setText(licenseInfo.getDOB());
            lNumTV.setText(licenseInfo.getLicNum());
            addStreetTV.setText(licenseInfo.getStreet());
            addCityTV.setText(licenseInfo.getCity());
            addStateTV.setText(licenseInfo.getState());
            addZipTV.setText(licenseInfo.getZip());
            genderTV.setText(licenseInfo.getGender());
            jasonLicenseObject = licenseInfo.toJSONObject();
        }

        // Button Send
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String licenseInfoString;
                if(licenseInfo != null){
                    //licenseInfoString = licenseInfo.toString();
                    licenseInfoString = licenseInfo.toJSON();
                }else{
                    licenseInfoString = "";
                }
                Socket sock;
                try {
                    sock = new Socket("192.168.1.106", 11000);
                    System.out.println("Connecting...");

                    // sendfile
                    Toast trySendToast = Toast.makeText(SendfileActivity.this,
                            "Connecting...", Toast.LENGTH_LONG);
                    trySendToast.show();


                    //File myFile = new File (selectedImagePath);
                    //byte [] mybytearray  = new byte [(int)myFile.length()];
                    //FileInputStream fis = new FileInputStream(myFile);
                    //BufferedInputStream bis = new BufferedInputStream(fis);
                    //bis.read(mybytearray,0,mybytearray.length);


                    byte [] mybytearray  = new byte [(int)licenseInfoString.length()];
                    FileInputStream fis = new FileInputStream(licenseInfoString);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);

                    OutputStream os = sock.getOutputStream();
                    os.write(mybytearray,0,mybytearray.length);
                    //os.write(mybytearray,0,mybytearray.length);
                    os.flush();
                    os.close();

//                    ObjectOutputStream objOS = new ObjectOutputStream(sock.getOutputStream());
//                    objOS.writeObject(jasonLicenseObject);
//                    objOS.flush();
//                    objOS.close();

                    System.out.println("Sending...");
                    sock.close();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



            }
        });

        // Button Path
/*        ((Button) findViewById(R.id.bBrowse))
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        System.out.println("40");
                        Intent intent = new Intent();
                        intent.setType("image*//*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(
                                Intent.createChooser(intent, "Select Picture"),
                                SELECT_PICTURE);
                        System.out.println("47");
                    }
                });
        ;*/


        // Button Path
        ((Button) findViewById(R.id.bBrowse))
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        System.out.println("40");
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
//                        Intent intent2 = new Intent();
//                        intent2.setType("image/*");
//                        intent2.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(
//                                Intent.createChooser(intent2, "Select Picture"),
//                                SELECT_PICTURE);
//                        System.out.println("47");
                    }
                });
        ;
        System.out.println("51");





    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                TextView path = (TextView) findViewById(R.id.tvPath);
                path.setText("Image Path : " + selectedImagePath);
                img.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



}
