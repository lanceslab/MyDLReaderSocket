package com.lanceslab.mydlreadersocket;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ClientLate extends Activity {

    private Socket socket;
    private static final String TAG  = "Client Late Activity";
    private static final int SERVERPORT = 11000;
    private static final String SERVER_IP = "192.168.1.106";
    Button buttonScan;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_late);

        buttonScan = (Button)findViewById(R.id.buttonScan);
        new Thread(new ClientThread()).start();
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    ImageView imageView=(ImageView) findViewById(R.id.imageView1);//EditText et = (EditText) findViewById(R.id.EditText01);
                    Bitmap bmp=((BitmapDrawable)imageView.getDrawable()).getBitmap(); //String str = et.getText().toString();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] array = bos.toByteArray();

                    OutputStream out = socket.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(out);
                    dos.writeInt(array.length);
                    Log.d(TAG, "========================array.length; " + array.length);
                    dos.write(array, 0, array.length);
                    Log.d(TAG, "dos.write(array, 0, array.length); ");

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Sent Message: " + e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Sent Message: " + e.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Sent Message: " + e.toString());
                }
            }
        });
    }

    public void onClick(View view) {
/*        try {
            ImageView imageView=(ImageView) findViewById(R.id.imageView1);//EditText et = (EditText) findViewById(R.id.EditText01);
            Bitmap bmp=((BitmapDrawable)imageView.getDrawable()).getBitmap(); //String str = et.getText().toString();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(CompressFormat.PNG, 0 *//*ignored for PNG*//*, bos);
            byte[] array = bos.toByteArray();

            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(array.length);
            Log.d(TAG, "========================array.length; " + array.length);
            dos.write(array, 0, array.length);
            Log.d(TAG, "dos.write(array, 0, array.length); ");

        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.d(TAG, "Sent Message: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Sent Message: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Sent Message: " + e.toString());
        }*/
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }
}