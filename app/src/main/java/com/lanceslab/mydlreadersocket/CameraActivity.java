package com.lanceslab.mydlreadersocket;

import android.app.Activity;
import android.os.Bundle;
/**
 * Created by LanceTaylor on 4/9/2017.
 */

public class CameraActivity  extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_basic);//FROM SAMPLES camera2basic

//        setContentView(R.layout.activity_camera_api);//activity_camera_api    texture  btn_takepicture
        //setContentView(R.layout.activity_camera);//activity_camera_api    texture  btn_takepicture
        //setContentView(R.layout.fragment_camera2_basic);
        if (null == savedInstanceState) {

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();// FROM SAMPLES camera2basic

//            getFragmentManager().beginTransaction()
//                    .replace(R.id.texture, Camera2BasicFragment.newInstance())
//                    .commit();// info

//            getFragmentManager().beginTransaction()
//                    .replace(R.id.surfaceView, Camera2BasicFragment.newInstance())
//                    .commit();



//            getFragmentManager().beginTransaction()
//                    .replace(R.id.preview, Camera2BasicFragment.newInstance())
//                    .commit();


            //DONT CRASH
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.control, Camera2BasicFragment.newInstance())
//                    .commit();

        }
    }



}
