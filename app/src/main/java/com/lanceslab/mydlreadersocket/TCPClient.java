package com.lanceslab.mydlreadersocket;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.lanceslab.mydlreadersocket.universalimageloader.sample.fragment.ImageListFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by LanceTaylor on 4/4/2017.
 */

public class TCPClient {



    private static final String            TAG             = "TCPClient"     ;
    private final Handler mHandler                          ;
    private              String            ipNumber, incomingMessage, command;
    //BufferedReader in                                ;
    //PrintWriter out                               ;
    private              MessageCallback   listener        = null            ;
    private              boolean           mRun            = false           ;
    Socket socket;
    DataInputStream in;// = new DataInputStream(obj_client.getInputStream());
    DataOutputStream out;// = new DataOutputStream(obj_client.getOutputStream());

    /**
     * TCPClient class constructor, which is created in AsyncTasks after the button click.
     * @param mHandler Handler passed as an argument for updating the UI with sent messages
     * @param command  Command passed as an argument, e.g. "shutdown -r" for restarting computer
     * @param ipNumber String retrieved from IpGetter class that is looking for ip number.
     * @param listener Callback interface object
     */
    public TCPClient(Handler mHandler, String command, String ipNumber, MessageCallback listener) {
        this.listener         = listener;
        this.ipNumber         = ipNumber;
        this.command          = command ;
        this.mHandler         = mHandler;
    }

    /**
     *
     * Public method for sending the message via OutputStream object.
     * @param message Message passed as an argument and sent via OutputStream object.
     *
    **/

    /**public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
            mHandler.sendEmptyMessageDelayed(LicenseResultsActivity.SENDING, 1000);
            Log.d(TAG, "Sent Message: " + message);

        }
    }
    **/



    public void sendMessage(String message){
        if (out != null /*&& !out.checkError()*/) {

            try {
                // was 124 to create file sent along file name
                out.write(CreateDataPacket("129".getBytes("UTF8"), message.getBytes("UTF8")));
                //out.write(CreateDataPacket("129".getBytes("UTF8"), "LancesLab".getBytes("UTF8")));
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //mHandler.sendEmptyMessageDelayed(ImageListFragment.SENDING, 1000);
            Log.d(TAG, "Sent Message: " + message);


        }
    }
    public void sendFinalMessage(String message){
        if (out != null) {
            try {
                out.write(CreateDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
                out.flush();
                Log.d(TAG, "Sent Final Message: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //mHandler.sendEmptyMessageDelayed(ImageListFragment.SENDING, 1000);
            Log.d(TAG, "Sent Message: " + message);


        }
    }


    public boolean isRunning(){

        if(mRun){
            return true;
        } else {
            return false;
        }

    }
    /**
     * Public method for stopping the TCPClient object ( and finalizing it after that ) from AsyncTask
     */
    public void stopClient(){
        Log.d(TAG, "Client stopped!");
        mRun = false;
    }



public void run() {

    mRun = true;

    try {
        // Creating InetAddress object from ipNumber passed via constructor from IpGetter class.
        InetAddress serverAddress = InetAddress.getByName(ipNumber);

        Log.d(TAG, "Connecting...");

        mHandler.sendEmptyMessageDelayed(ImageListFragment.CONNECTING,1000);

        Socket socket = new Socket(serverAddress, 11000);


        try {

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            Log.d(TAG, "In/Out created");
            //Sending message with command specified by AsyncTask
            //this.sendMessage(target_file.getName());
            this.sendMessage(command);

            //mHandler.sendEmptyMessageDelayed(ImageListFragment.SENDING,2000);
            //Listen for the incoming messages while mRun = true
            while (mRun) {
                if(in.read() == 2) {

                    byte[] cmd_buffer = new byte[3];
                    in.read(cmd_buffer, 0, cmd_buffer.length);
                    byte[] recv_buff = ReadStream(in);// ======gettinh stuck here================
                    switch (Integer.parseInt(new String(cmd_buffer))){
                        case 125:

                            break;
                    }
                }
                if(!isRunning()){
                    System.out.println("Stop Server informed");
                    out.write(CreateDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
                    out.flush();
                    socket.close();
                    System.out.println("Client Socket Closed");
                    break;
                }
            }

            Log.d(TAG, "Received Message: " +incomingMessage);

        }  catch (Exception e) {

            Log.d(TAG, "Error", e);
            mHandler.sendEmptyMessageDelayed(ImageListFragment.ERROR, 2000);

        } finally {

            out.flush();
            out.close();
            in.close();
            socket.close();
            mHandler.sendEmptyMessageDelayed(ImageListFragment.SENT, 3000);
            Log.d(TAG, "Socket Closed");
        }

    } catch (Exception e) {

        Log.d(TAG, "Error", e);
        mHandler.sendEmptyMessageDelayed(ImageListFragment.ERROR, 2000);

    }

    //    public void run() {
//
//        mRun = true;
//
//        try {
//            // Creating InetAddress object from ipNumber passed via constructor from IpGetter class.
//            InetAddress serverAddress = InetAddress.getByName(ipNumber);
//
//            Log.d(TAG, "Connecting...");
//
//            /**
//             * Sending empty message with static int value from MainActivity
//             * to update UI ( 'Connecting...' ).
//             *
//             * @see com.example.turnmeoff.MainActivity.CONNECTING
//             */
//            mHandler.sendEmptyMessageDelayed(LicenseResultsActivity.CONNECTING,1000);
//
//            /**
//             * Here the socket is created with hardcoded port.
//             * Also the port is given in IpGetter class.
//             *
//             * @see com.example.turnmeoff.IpGetter
//             */
//            Socket socket = new Socket(serverAddress, 11000);
//
//
//            try {
//
//                // Create PrintWriter object for sending messages to server.
//                //out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//                //Create BufferedReader object for receiving messages from server.
//                //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                out = new DataOutputStream(socket.getOutputStream());
//                in = new DataInputStream(socket.getInputStream());
//
//                Log.d(TAG, "In/Out created");
//
//                //Sending message with command specified by AsyncTask
//                this.sendMessage(command);
//
//                //
//                mHandler.sendEmptyMessageDelayed(LicenseResultsActivity.SENDING,2000);
//
//                //Listen for the incoming messages while mRun = true
//                while (mRun) {
//                    incomingMessage = in.readLine();
//                    if (incomingMessage != null && listener != null) {
//
//                        /**
//                         * Incoming message is passed to MessageCallback object.
//                         * Next it is retrieved by AsyncTask and passed to onPublishProgress method.
//                         *
//                         */
//                        listener.callbackMessageReceiver(incomingMessage);
//
//                    }
//                    incomingMessage = null;
//
//                }
//
//                Log.d(TAG, "Received Message: " +incomingMessage);
//
//            } catch (Exception e) {
//
//                Log.d(TAG, "Error", e);
//                mHandler.sendEmptyMessageDelayed(LicenseResultsActivity.ERROR, 2000);
//
//            } finally {
//
//                out.flush();
//                out.close();
//                in.close();
//                socket.close();
//                mHandler.sendEmptyMessageDelayed(LicenseResultsActivity.SENT, 3000);
//                Log.d(TAG, "Socket Closed");
//            }
//
//        } catch (Exception e) {
//
//            Log.d(TAG, "Error", e);
//            mHandler.sendEmptyMessageDelayed(LicenseResultsActivity.ERROR, 2000);
//
//        }
//
//    }

}


    private byte[] CreateDataPacket(byte[] cmd, byte[] data){
        byte[] packet =null;
        try{
            byte[] initialize = new byte[1];
            initialize[0] = 2;
            byte[] separator = new byte[1];
            separator[0] = 4;
            byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
            packet = new byte[initialize.length+cmd.length+separator.length+data_length.length+data.length];// packet size
            // copy the byte arrays into the packet array
            System.arraycopy(initialize, 0, packet, 0, initialize.length);
            System.arraycopy(cmd, 0, packet, initialize.length, cmd.length);
            System.arraycopy(data_length, 0, packet, initialize.length + cmd.length, data_length.length);
            System.arraycopy(separator, 0, packet, initialize.length + cmd.length + data_length.length, separator.length);
            System.arraycopy(data, 0, packet, initialize.length + cmd.length + data_length.length + separator.length, data.length);

        }catch (UnsupportedEncodingException ex){
            Log.d(TAG, "UnsupportedEncodingException " + ex.toString());
        }

        return packet;
    }


    private byte[] ReadStream(DataInputStream din){
        byte[] data_buff = null;

        try {
            int b = 0;
            String buff_length = "";
            while ((b = din.read()) != 4) {
                buff_length += (char) b;
            }

            int data_length = Integer.parseInt(buff_length);
            data_buff = new byte[Integer.parseInt(buff_length)];// put the info into data_buff
            int byte_read = 0;
            int byte_offset = 0;
            while (byte_offset < data_length){
                //din.read(data_buff, byte_offset, data_length - byte_offset);
                byte_read = din.read(data_buff, byte_offset, data_length - byte_offset);
                byte_offset += byte_read;
            }
        } catch (IOException e) {
            Log.d(TAG, "IOException reading DataInputStream" + e.toString());
        }
        return data_buff;
    }

    /**
     * Callback Interface for sending received messages to 'onPublishProgress' method in AsyncTask.
     *
     */
    public interface MessageCallback {
        /**
         * Method overriden in AsyncTask 'doInBackground' method while creating the TCPClient object.
         * @param message Received message from server app.
         */
        public void callbackMessageReceiver(String message);
    }


}
