package com.lanceslab.mydlreadersocket;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.lanceslab.mydlreadersocket.LicenseActivity;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
//public class MainActivity extends AppCompatActivity {
public class MainActivity extends Activity {

    // use a compound button so either checkbox or switch widgets work.
    private RadioButton radLicense, radVin;
    private TextView statusMessage;
    private TextView radioValue;
    private Button buttonScan;
    PowerConnectionReceiver powerCheck;
    MediaPlayer mPlayer;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "LancesLab Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView)findViewById(R.id.status_message);
        radioValue = (TextView)findViewById(R.id.radio_value);
        radLicense = (RadioButton)findViewById(R.id.radioLicense);
        radVin = (RadioButton)findViewById(R.id.radioVin);

        buttonScan = (Button)findViewById(R.id.btnScan);
        buttonScan.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                // Perform action on click
                if(v.getId() == R.id.btnScan){
                    if(radLicense.isChecked() && radVin.isChecked()){
                        Toast radioToast = Toast.makeText(MainActivity.this, "You cant have both Radios CHECKED", Toast.LENGTH_LONG);
                        radioToast.show();
                        return;
                    } else if(radLicense.isChecked()){
                        // Start Activity for Drivers License
                        Intent intent = new Intent(MainActivity.this, LicenseActivity.class);
                        startActivity(intent);
                    } else if(radVin.isChecked()){
//                        mPlayer = MediaPlayer.create(MainActivity.this, R.raw.ironmanunauthorized);
//                        mPlayer.start();
                        // Start Activity for VIN Numbers
                        //Intent intent = new Intent(MainActivity.this, CustomGalleryActivity.class);
                        //Intent intent = new Intent(MainActivity.this, AndroidCameraApi.class);
                        //Intent intent = new Intent(MainActivity.this, GetSavedPicsFromSdCard.class);

                        //Intent intent = new Intent(MainActivity.this, ImagePickActivity.class);
                        //Intent intent = new Intent(MainActivity.this, MakePhotoActivity.class);

                        //Camera2BasicFragment camFrag = new Camera2BasicFragment();

//                        Intent intent = new Intent(MainActivity.this, PicCaptureSendActivity.class);
//                        startActivity(intent);
//                        Toast vinToast = Toast.makeText(MainActivity.this,
//                                "Taking Pictures", Toast.LENGTH_LONG);
//                        vinToast.show();
                        Intent intent = new Intent(MainActivity.this, PicCaptureSendActivity.class);
                        startActivity(intent);
                    }

                }
            }
        });// END onClick

    }// END onCreate



}// END MainActivity
