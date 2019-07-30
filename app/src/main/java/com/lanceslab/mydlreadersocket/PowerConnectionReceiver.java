package com.lanceslab.mydlreadersocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.BatteryManager;

/**
 * Created by LanceTaylor on 4/7/2017.
 */

public class PowerConnectionReceiver extends BroadcastReceiver {

    private int someLevel = 0;
    MediaPlayer mPlayer;
    Context thisContext;

    @Override
    public void onReceive(Context context, Intent intent) {
//                <action android:name="android.intent.action.ACTION_POWER_CONNECTED"/>
//                <action android:name="android.intent.action.ACTION_POWER_DISCONNECTED"/>
        thisContext = context;
        String action = intent.getAction();
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        mPlayer = playSound(R.raw.jarvisimportingpref);
//        mPlayer.start();
        Intent batteryStatus = context.registerReceiver(null, iFilter);
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        // Is it Charging
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        // Is it pluged in
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        // USB
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        // AC
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;



        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        someLevel = level;
        // Battery Percent
        float batteryPct = level / (float)scale;
        // PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP
        if(action.equals(Intent.ACTION_POWER_CONNECTED)){
            mPlayer = playSound(R.raw.jarvisitischarging);
            mPlayer.start();
            if(isCharging) {
//                mPlayer = playSound(R.raw.jarvisitischarging);
//                mPlayer.start();
                if (batteryPct > 24.00) {
                    mPlayer = playSound(R.raw.jarvis24percent);
                    mPlayer.start();
                } else if (batteryPct < 24.00 && batteryPct >= 14.00) {
                    mPlayer = playSound(R.raw.jarvispowerdownyo14percent);
                    mPlayer.start();
                }
                if (usbCharge) {
                    mPlayer = playSound(R.raw.jarvispowersourcequestionable);
                    mPlayer.start();

                } else if (acCharge) {
                    mPlayer = playSound(R.raw.jarvisitischarging);
                    mPlayer.start();
                    mPlayer = playSound(R.raw.jarvispoww400percent);
                    mPlayer.start();
                }
            }else {
//                mPlayer = playSound(R.raw.jarvispoww400percent);
//                mPlayer.start();
            }
        }else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                    //  2, 5, 7, 14, 24
                    if(batteryPct <= 100.00 && batteryPct > 24.00){
                        mPlayer = playSound(R.raw.jarvisdeviceselfsustaining);
                        mPlayer.start();
                    }else if (batteryPct == 24.00){
                        mPlayer = playSound(R.raw.jarvis24percent);
                        mPlayer.start();
                    }else if (batteryPct < 24.00 && batteryPct >= 14.00){
                        mPlayer = playSound(R.raw.jarvispowerdownyo14percent);
                        mPlayer.start();
                    }else if (batteryPct < 14.00 && batteryPct >= 7.00){
                        mPlayer = playSound(R.raw.jarvis7percent);
                        mPlayer.start();
                    }else if (batteryPct < 7.00 && batteryPct >= 5.00){
                        mPlayer = playSound(R.raw.jarvisbattery5percent);
                        mPlayer.start();
                    }else if (batteryPct < 5.00 && batteryPct >= 2.00){
                        mPlayer = playSound(R.raw.jarvis2percent);
                        mPlayer.start();
                    }else {
                        mPlayer = playSound(R.raw.ironmanpower);
                        mPlayer.start();
                    }
        }
//PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP

//        if(iFilter.actionsIterator() != null)
//        {
//            while (iFilter.actionsIterator().hasNext()) {
//                if(iFilter.actionsIterator().toString() == Intent.ACTION_POWER_CONNECTED){
//
//
//                    if(isCharging) {
//                        mPlayer = playSound(R.raw.jarvisitischarging);
//                        mPlayer.start();
//                        if (batteryPct > 24.00) {
//                            mPlayer = playSound(R.raw.jarvis24percent);
//                            mPlayer.start();
//                        } else if (batteryPct < 24.00 && batteryPct >= 14.00) {
//                            mPlayer = playSound(R.raw.jarvispowerdownyo14percent);
//                            mPlayer.start();
//                        }
//                        if (usbCharge) {
//                            mPlayer = playSound(R.raw.jarvispowersourcequestionable);
//                            mPlayer.start();
//
//                        } else if (acCharge) {
//                            mPlayer = playSound(R.raw.jarvisitischarging);
//                            mPlayer.start();
//                        }
//                    }else {
//                        mPlayer = playSound(R.raw.jarvispoww400percent);
//                        mPlayer.start();
//                    }
//
//                }else if(iFilter.actionsIterator().toString() == Intent.ACTION_POWER_DISCONNECTED){
//                    //mPlayer = playSound(R.raw.jarvisItIsCharging);
//                    //  2, 5, 7, 14, 24
//                    if(batteryPct <= 100.00 && batteryPct > 24.00){
//                        mPlayer = playSound(R.raw.jarvisdeviceselfsustaining);
//                        mPlayer.start();
//                    }else if (batteryPct == 24.00){
//                        mPlayer = playSound(R.raw.jarvis24percent);
//                        mPlayer.start();
//                    }else if (batteryPct < 24.00 && batteryPct >= 14.00){
//                        mPlayer = playSound(R.raw.jarvispowerdownyo14percent);
//                        mPlayer.start();
//                    }else if (batteryPct < 14.00 && batteryPct >= 7.00){
//                        mPlayer = playSound(R.raw.jarvis7percent);
//                        mPlayer.start();
//                    }else if (batteryPct < 7.00 && batteryPct >= 5.00){
//                        mPlayer = playSound(R.raw.jarvisbattery5percent);
//                        mPlayer.start();
//                    }else if (batteryPct < 5.00 && batteryPct >= 2.00){
//                        mPlayer = playSound(R.raw.jarvis2percent);
//                        mPlayer.start();
//                    }else {
//                        mPlayer = playSound(R.raw.ironmanpower);
//                        mPlayer.start();
//                    }
//
//
//                }
//            }
//        }



    }


    public int getLevel(){
        return someLevel;
    }


    private  MediaPlayer  playSound(int soundFile){

        MediaPlayer mPlayer = MediaPlayer.create(thisContext, soundFile);
       // mPlayer.start();
        return mPlayer;

    }



}
