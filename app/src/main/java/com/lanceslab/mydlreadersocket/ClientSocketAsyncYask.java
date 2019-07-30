package com.lanceslab.mydlreadersocket;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by LanceTaylor on 4/4/2017.
 * AsyncTask class which manages connection with server app and is sending shutdown command.
 */

public class ClientSocketAsyncYask extends AsyncTask<String, String, TCPClient> {

    private  String     COMMAND     = "shutdown -s"      ;
    private              TCPClient  tcpClient                        ;
    private Handler mHandler                         ;
    private static final String     TAG         = "SendLicenseAsyncTask";
    private String setIpAddress = "192.168.1.106";
    /**
     * ShutdownAsyncTask constructor with handler passed as argument. The UI is updated via handler.
     * In doInBackground(...) method, the handler is passed to TCPClient object.
     * @param mHandler Handler object that is retrieved from MainActivity class and passed to TCPClient
     *                 class for sending messages and updating UI.
     */
    public void ClientSocketAsyncYask(Handler mHandler){
        this.mHandler = mHandler;
    }
    public void ClientSocketAsyncYask2(Handler mHandler, String command){
        this.mHandler = mHandler;
        this.COMMAND = command;
    }
    public void ClientSocketAsyncYask3(Handler mHandler, String command, String mIP){
        this.mHandler = mHandler;
        this.COMMAND = command;
        this.setIpAddress = mIP;
    }

    /**
     * Overriden method from AsyncTask class. There the TCPClient object is created.
     * @param params From MainActivity class empty string is passed.
     * @return TCPClient object for closing it in onPostExecute method.
     */
    @Override
    protected TCPClient doInBackground(String... params) {
        Log.d(TAG, "In do in background");

        try{
            // create TCPClient before running it
            tcpClient = new TCPClient(mHandler,
                    COMMAND,
                    setIpAddress,
                    new TCPClient.MessageCallback() {
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
        Log.d(TAG, "In progress update, values: " + values.toString());
        if(values[0].equals("SUCCESS")){
            tcpClient.sendMessage(COMMAND);
            tcpClient.stopClient();
            mHandler.sendEmptyMessageDelayed(LicenseResultsActivity.SHUTDOWN, 2000);

        }else{
            tcpClient.sendMessage("wrong");
            mHandler.sendEmptyMessageDelayed(LicenseResultsActivity.ERROR, 2000);
            tcpClient.stopClient();
        }
    }


    @Override
    protected void onPostExecute(TCPClient result){
        super.onPostExecute(result);
        Log.d(TAG, "In on post execute");
        if(result != null && result.isRunning()){
            result.stopClient();
        }
        mHandler.sendEmptyMessageDelayed(LicenseResultsActivity.SENT, 4000);

    }







/**
             * Sending empty message with static int value from MainActivity
             * to update UI ( 'Connecting...' ).
             *
             * @see com.example.turnmeoff.MainActivity.CONNECTING
             *//*

            mHandler.sendEmptyMessageDelayed(MainActivity.CONNECTING,1000);

            */
/**
             * Here the socket is created with hardcoded port.
             * Also the port is given in IpGetter class.
             *
             * @see com.example.turnmeoff.IpGetter
             *//*

            Socket socket = new Socket(serverAddress, 4444);


            try {

                // Create PrintWriter object for sending messages to server.
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //Create BufferedReader object for receiving messages from server.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Log.d(TAG, "In/Out created");

                //Sending message with command specified by AsyncTask
                this.sendMessage(command);

                //
                mHandler.sendEmptyMessageDelayed(MainActivity.SENDING,2000);

                //Listen for the incoming messages while mRun = true
                while (mRun) {
                    incomingMessage = in.readLine();
                    if (incomingMessage != null && listener != null) {

                        */
/**
                         * Incoming message is passed to MessageCallback object.
                         * Next it is retrieved by AsyncTask and passed to onPublishProgress method.
                         *
                         *//*

                        listener.callbackMessageReceiver(incomingMessage);

                    }
                    incomingMessage = null;

                }

                Log.d(TAG, "Received Message: " +incomingMessage);

            } catch (Exception e) {

                Log.d(TAG, "Error", e);
                mHandler.sendEmptyMessageDelayed(MainActivity.ERROR, 2000);

            } finally {

                out.flush();
                out.close();
                in.close();
                socket.close();
                mHandler.sendEmptyMessageDelayed(MainActivity.SENT, 3000);
                Log.d(TAG, "Socket Closed");
            }

        } catch (Exception e) {

            Log.d(TAG, "Error", e);
            mHandler.sendEmptyMessageDelayed(MainActivity.ERROR, 2000);

        }

    }
*/







}




//}
