package com.lanceslab.mydlreadersocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

/**
 * Created by LanceTaylor on 4/7/2017.
 */

public class ShutdownReceiver extends BroadcastReceiver {





    MediaPlayer mPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
//<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
        //IntentFilter iFilterBoot = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);

        //Intent bootStatus = context.registerReceiver(null, iFilterBoot);
        String action = intent.getAction();
         if(action.equals(Intent.ACTION_SHUTDOWN)){
            mPlayer = playSound(R.raw.jarvispoweringdownfordiagnostics);
            mPlayer.start();
        }
        //                ACTION_REBOOT                 ACTION_SHUTDOWN



    }




    private  MediaPlayer  playSound(int soundFile){

        MediaPlayer mPlayer = MediaPlayer.create(null, soundFile);
        //mPlayer.start();
        return mPlayer;

    }











}
