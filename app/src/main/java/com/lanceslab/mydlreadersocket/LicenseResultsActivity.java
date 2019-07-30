package com.lanceslab.mydlreadersocket;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.MainThread;//
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.content.ContentValues.TAG;
import static android.net.NetworkInfo.State.CONNECTING;
import static android.webkit.ConsoleMessage.MessageLevel.ERROR;


/**
 * Created by LanceTaylor on 4/3/2017.
 */

public class LicenseResultsActivity extends Activity implements View.OnClickListener {

    private final String IP = "192.168.1.106";
    private static final String TAG = "Results Activity";
    private final int PORT = 11000;
    public static final int SHUTDOWN = 1;
    public static final int ERROR = 2;
    public static final int SENT = 3;
    public static final int SENDING = 4;
    public static final int CONNECTING = 5;
    public  String tempIp;
    ClientSocketAsyncYask socketAsync;
    Handler mHandler;
    //Client myClient;
    Socket socket;
    DriversLicense licenseInfo;
    Button btnPassObject;
    MediaPlayer mPlayer;
//    TextView lNameTV, mNameTV, fNameTV, bDateTV, ageTV, lNumTV, addStreetTV,
//    addCityTV, addStateTV, addZipTV, genderTV, issDateTV, expDateTV, countyTV;
    TextView sendResults;

    EditText lNameET, mNameET, fNameET, bDateET, ageET, lNumET, addStreetET, addCityET, addStateET,
            addZipET, genderET, issDateET, expDateET, countyET, ipAddressET;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_results);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // FOR SOCKET SENDING
        socketAsync = new ClientSocketAsyncYask();


        btnPassObject = (Button)findViewById(R.id.btnPassObject);
//        lNameTV = (TextView)findViewById(R.id.tvLName);
//        mNameTV = (TextView)findViewById(R.id.tvMName);
//        fNameTV = (TextView)findViewById(R.id.tvFName);
//        bDateTV = (TextView)findViewById(R.id.tvDob);
//        //ageTV = (TextView)findViewById(R.id.age);
//        lNumTV = (TextView)findViewById(R.id.tvLicNum);
//        addStreetTV = (TextView)findViewById(R.id.tvStreet);
//        addCityTV = (TextView)findViewById(R.id.tvCity);
//        addStateTV = (TextView)findViewById(R.id.tvState);
//        addZipTV = (TextView)findViewById(R.id.tvZip);
//        genderTV = (TextView)findViewById(R.id.tvGender);
//        issDateTV = (TextView)findViewById(R.id.);
//        expDateTV = (TextView)findViewById(R.id.);
//        countyTV = (TextView)findViewById(R.id.);
        sendResults = (TextView)findViewById(R.id.sendResults);
        ipAddressET = (EditText)findViewById(R.id.etIPAddress);
        ipAddressET.setText(IP);
        lNameET = (EditText)findViewById(R.id.etLName);
        mNameET = (EditText)findViewById(R.id.etMName);
        fNameET = (EditText)findViewById(R.id.etFName);
        bDateET = (EditText)findViewById(R.id.etDob);
        //ageET = (EditText)findViewById(R.id.);
        lNumET = (EditText)findViewById(R.id.etLicNum);
        addStreetET = (EditText)findViewById(R.id.etStreet);
        addCityET = (EditText)findViewById(R.id.etCity);
        addStateET = (EditText)findViewById(R.id.etState);
        addZipET = (EditText)findViewById(R.id.etZip);
        genderET = (EditText)findViewById(R.id.etGender);
//        issDateET = (EditText)findViewById(R.id);
//        expDateET = (EditText)findViewById(R.id.);
//        countyET = (EditText)findViewById(R.id.);


        Intent thisIntent = getIntent();
        Bundle bd = thisIntent.getExtras();
        if(bd != null)
        {
            licenseInfo = (DriversLicense) bd.get("license");
            ipAddressET.setText(IP);
            fNameET.setText(licenseInfo.getFName());
            mNameET.setText(licenseInfo.getMName());
            lNameET.setText(licenseInfo.getLName());
            bDateET.setText(licenseInfo.getDOB());
            lNumET.setText(licenseInfo.getLicNum());
            addStreetET.setText(licenseInfo.getStreet());
            addCityET.setText(licenseInfo.getCity());
            addStateET.setText(licenseInfo.getState());
            addZipET.setText(licenseInfo.getZip());
            genderET.setText(licenseInfo.getGender());
            mPlayer = MediaPlayer.create(LicenseResultsActivity.this, R.raw.jarvistherenderiscomplete2);
            mPlayer.start();
//            lNameTV.setText(licenseInfo.getLName());
//            mNameTV.setText(licenseInfo.getMName());
//            fNameTV.setText(licenseInfo.getFName());
//            bDateTV.setText(licenseInfo.getDOB());
//            //ageTV.setText(licenseInfo.getDOB());
//            lNumTV.setText(licenseInfo.getLicNum());
//            addStreetTV.setText(licenseInfo.getStreet());
//            addCityTV.setText(licenseInfo.getCity());
//            addStateTV.setText(licenseInfo.getState());
//            addZipTV.setText(licenseInfo.getZip());
//            genderTV.setText(licenseInfo.getGender());

        }



        btnPassObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set up a socket in android the socket must be created in a thread.
                if (view.getId() == R.id.btnPassObject) {
//                Toast socketErrorToast = Toast.makeText(LicenseResultsActivity.this,
//                        ("   Connecting After you clicked the Button  "), Toast.LENGTH_LONG);
//                socketErrorToast.show();
                mHandler = new Handler()
                {
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case SHUTDOWN:
                                Log.d(TAG, "In Handler's shutdown");

                                sendResults.setText("SHUTDOWN");
                                break;
                            case ERROR:
                                sendResults.setText("Error");

                                break;
                            case SENT:
                                sendResults.setText("SENT");
                                break;
                            case SENDING:
                                sendResults.setText("SENDING");
                                break;
                            case CONNECTING:
                                sendResults.setText("CONNECTING");
                                break;
                            default:
                                sendResults.setText("Not in list??");
                                break;

                        }
                    }
                };
                String deviceName = Settings.Secure.getString(getContentResolver(), "bluetooth_name");
                System.out.println("LicenseResultsActivity Starting");
                    if(addStreetET.getText().toString() != licenseInfo.getStreet()){
                        licenseInfo.setStreet(addStreetET.getText().toString());
                    }
                    if(addCityET.getText().toString() != licenseInfo.getCity()){
                        licenseInfo.setCity(addCityET.getText().toString());
                    }
                    if(addStateET.getText().toString() != licenseInfo.getState()){
                        licenseInfo.setState(addStateET.getText().toString());
                    }
                    if(addZipET.getText().toString() != licenseInfo.getZip()){
                        licenseInfo.setZip(addZipET.getText().toString());
                    }

                    String tempIp = ipAddressET.getText().toString();
                    if(tempIp != IP){
                        socketAsync.ClientSocketAsyncYask3(mHandler,
                                (licenseInfo.toString() + deviceName + "<EOF>"), tempIp);
                        socketAsync.execute();
                    }else{
                        socketAsync.ClientSocketAsyncYask2(mHandler, (licenseInfo.toString() + deviceName + "<EOF>"));
                        socketAsync.execute();
                    }
                mPlayer = MediaPlayer.create(LicenseResultsActivity.this, R.raw.jarvisaccessingoracle);
                mPlayer.start();
                Toast socketSuccessToast = Toast.makeText(LicenseResultsActivity.this,
                        ("   Success Sending Data!    "), Toast.LENGTH_LONG);
                socketSuccessToast.show();
                System.out.println("LicenseResultsActivity executed");
                finish();


                }// END IF btnPassObject
                else if (view.getId() == R.id.etIPAddress) {
                    String temp = ipAddressET.getText().toString();
                    if(temp != IP){
                        tempIp = temp;
                    }
                }
            }
        });// END ONCLICK HANDLER


    }



    @Override
    public void onClick(View view) {
/*// Set up a socket in android the socket must be created in a thread.
        if (view.getId() == R.id.btnPassObject) {

            //new Thread(new MainThread()).start();
            String response;
            try{
                Socket socket = new Socket(IP, PORT);

                genderTV.setText("Sending data to PC");

                OutputStream out = socket.getOutputStream();
                PrintWriter output = new PrintWriter(out);
                output.println(licenseInfo);
                out.flush();
                out.close();
                Toast socketSuccessToast = Toast.makeText(LicenseResultsActivity.this,
                        ("Success Sending Data! \n " + licenseInfo.getLicNum()), Toast.LENGTH_LONG);
                socketSuccessToast.show();
            }catch (IOException e){
                Toast socketErrorToast = Toast.makeText(LicenseResultsActivity.this,
                        ("Error \n " + e.toString()), Toast.LENGTH_LONG);
                socketErrorToast.show();
            } *//*catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //
                Toast socketErrorToast = Toast.makeText(LicenseResultsActivity.this,
                        ("Error \n " + e.toString()), Toast.LENGTH_LONG);
                socketErrorToast.show();
                response = "UnknownHostException: " + e.toString();
            } *//*finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }



        }*/
    }







    class MainThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress address = InetAddress.getByName(IP);
                socket = new Socket(address,PORT);
                new Thread(new GetThread()).start();
            } catch (UnknownHostException e1){
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }// END MainThread


    class GetThread implements Runnable {

        @Override
        public void run() {

            try {
                InputStreamReader isR = new InputStreamReader(socket.getInputStream());
                OutputStream out = socket.getOutputStream();
                BufferedReader bfr = new BufferedReader(isR);
                while(true) {
                    String textMessage = bfr.readLine();
                    // TODO: Insert logic which use the recived message (textMessage)

                    PrintWriter output = new PrintWriter(out);
                    output.println(licenseInfo + "<EOF>");
                    out.flush();
                    out.close();
                    Toast socketSuccessToast = Toast.makeText(LicenseResultsActivity.this,
                            ("Success Sending Data! \n " + licenseInfo.getLicNum()), Toast.LENGTH_LONG);
                    socketSuccessToast.show();
                }
            } catch (UnknownHostException e1){
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }// END GetThread




}
