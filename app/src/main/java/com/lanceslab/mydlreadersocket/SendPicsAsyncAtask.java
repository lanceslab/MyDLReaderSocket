package com.lanceslab.mydlreadersocket;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.lanceslab.mydlreadersocket.universalimageloader.sample.fragment.ImageListFragment;
import com.lanceslab.mydlreadersocket.universalimageloader.sample.fragment.SendListAsyncTCPClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by LanceTaylor on 4/4/2017.
 * AsyncTask class which manages connection with server app and is sending shutdown command.
 */

public class SendPicsAsyncAtask extends AsyncTask<String, String, SendListAsyncTCPClient> {
    String path = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload/";
    TextView statusTextview;
    File target_file;// = new File(path);
    private  String     COMMAND     = "shutdown -s"      ;
    private              SendListAsyncTCPClient  tcpClient                        ;
    private Handler mHandler                         ;
    private static final String     TAG         = "SendLicenseAsyncTask";
    private String setIpAddress;// = "192.168.1.116";
    String imageFileString;
    /**
     * ShutdownAsyncTask constructor with handler passed as argument. The UI is updated via handler.
     * In doInBackground(...) method, the handler is passed to SendListAsyncTCPClient object.
     * @param mHandler Handler object that is retrieved from MainActivity class and passed to SendListAsyncTCPClient
     *                 class for sending messages and updating UI.
     */
    public void SendPicsAsyncAtask(Handler mHandler){
        this.mHandler = mHandler;
    }
    public void SendPicsAsyncAtask2(Handler mHandler, String command){
        this.mHandler = mHandler;
        this.COMMAND = command;
        path += COMMAND;
    }
    public void SendPicsAsyncAtask3(Handler mHandler, String command, String mIP){
        this.mHandler = mHandler;
        this.COMMAND = command;
        imageFileString = command;
        this.setIpAddress = mIP;
        path += COMMAND;
    }

    public void SetTextView(TextView _statusTextview){
        statusTextview = _statusTextview;
    }

    /**
     * Overriden method from AsyncTask class. There the TCPClient object is created.
     * @param params From MainActivity class empty string is passed.
     * @return TCPClient object for closing it in onPostExecute method.
     */
    @Override
    protected SendListAsyncTCPClient doInBackground(String... params) {
    //protected TCPClient doInBackground(String... params) {
        Log.d(TAG, "In do in background");

        try{
            // create TCPClient before running it
            tcpClient = new SendListAsyncTCPClient(mHandler,
                    COMMAND,
                    setIpAddress,
                    new SendListAsyncTCPClient.MessageCallback() {
                        @Override
                        public void callbackMessageReceiver(String message) {
                            publishProgress(message);
                        }
                    });

        }catch (NullPointerException e){
            Log.d(TAG, "Caught null pointer exception");
            e.printStackTrace();
        }
        // Run the client
        tcpClient.run();
        return null;
    }



    /**
     * Overriden method from AsyncTask class. Here we're checking if server answered properly.
     * @param values If "restart" message came, the client is stopped and computer should be restarted.
     *               Otherwise "wrong" message is sent and 'Error' message is shown in UI.
     */

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        String[] items = values[0].split(",");
        Log.d(TAG, "In progress update, values: ===================" + values[0].toString());
        switch (items[0]){
            case "PENDING":
                statusTextview.setText(items[1] + " % ");
                //mHandler.sendEmptyMessageDelayed(ImageListFragment.SENDING, 2000);
                break;
            case "SUCCESS":
                tcpClient.sendFinalMessage(COMMAND);
                tcpClient.stopClient();
                mHandler.sendEmptyMessageDelayed(ImageListFragment.SHUTDOWN, 2000);
                break;
            default:
                //tcpClient.sendMessage("wrong");
                mHandler.sendEmptyMessageDelayed(ImageListFragment.ERROR, 2000);
                //tcpClient.stopClient();
                break;
        }
//        if(values[0].equals("SUCCESS")){
//            //tcpClient.sendMessage(COMMAND);
//            tcpClient.sendFinalMessage(COMMAND);
//            tcpClient.stopClient();
//            mHandler.sendEmptyMessageDelayed(ImageListFragment.SHUTDOWN, 2000);
//
//        }/*else if (Float.parseFloat(values[0].toString()) < fSize){
//            //System.out.println("Uploaded percent: " + ((float)current_file_pointer/rw.length()) * 100 + "%");
//            float amtComplete = Float.parseFloat(values[0].toString());
//            float percAmtComplete = (fSize/amtComplete);
//            Message mess = new Message();
//            mess.what = ((int)percAmtComplete);
//            mHandler.sendMessage(mess);
//        }*/
//        else{
//            tcpClient.sendMessage("wrong");
//            mHandler.sendEmptyMessageDelayed(ImageListFragment.ERROR, 2000);
//            tcpClient.stopClient();
//        }
    }


    @Override
    protected void onPostExecute(SendListAsyncTCPClient result){
        super.onPostExecute(result);
        Log.d(TAG, "In on post execute");
        if(result != null && result.isRunning()){
            result.stopClient();
        }
        mHandler.sendEmptyMessageDelayed(ImageListFragment.SENT, 4000);

    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        Log.d(TAG, "In on Pre execute");
        //dialog = ProgressDialog.show(ImageListFragment.this, "Uploading", "Please wait...", true);
        //dialog.show();
        mHandler.sendEmptyMessageDelayed(ImageListFragment.CONNECTING,1000);
    }


}

