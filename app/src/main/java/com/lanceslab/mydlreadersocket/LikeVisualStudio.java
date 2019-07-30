package com.lanceslab.mydlreadersocket;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by LanceTaylor on 4/16/2017.
 */

public class LikeVisualStudio {

    private static final String TAG  = "Like Visual Studio";

    public  static void clientMain(String[] args){


        try{
            ServerSocket server_socket = new ServerSocket(11000);
            while (true){
                server_socket.accept();
            }
        }catch (IOException ex){
            Log.d(TAG, "Sent Message: " + ex.toString());
        }

    }


//    Class ClientWorker implements Runnable{
//        Socket target_socket;
//
//        public ClientWorker(Socket rev_socket){
//            target_socket = rev_socket;
//        }
//
//        @Override
//         public void run(){
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//    }

}


