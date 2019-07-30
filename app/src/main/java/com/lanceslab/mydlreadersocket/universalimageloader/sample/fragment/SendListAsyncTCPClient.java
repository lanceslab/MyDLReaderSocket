package com.lanceslab.mydlreadersocket.universalimageloader.sample.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;//package com.lanceslab.mydlreadersocket;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LanceTaylor on 4/4/2017.
 * 1491249714674.jpg
 */

public class SendListAsyncTCPClient {


    Socket socket;
    DataInputStream in;// = new DataInputStream(obj_client.getInputStream());
    DataOutputStream out;// = new DataOutputStream(obj_client.getOutputStream());
    private static final String          TAG = "SendListAsyncTCPClient"     ;
    private final Handler mHandler                          ;
    private              String            ipNumber, incomingMessage, command;
    //BufferedReader in                                ;
    //PrintWriter out                               ;
    private              MessageCallback   listener        = null            ;
    private              boolean           mRun            = false           ;


    /**
     * TCPClient class constructor, which is created in AsyncTasks after the button click.
     * @param mHandler Handler passed as an argument for updating the UI with sent messages
     * @param command  Command passed as an argument, e.g. "shutdown -r" for restarting computer
     * @param ipNumber String retrieved from IpGetter class that is looking for ip number.
     * @param listener Callback interface object
     */
    public SendListAsyncTCPClient(Handler mHandler, String command, String ipNumber, MessageCallback listener) {
        this.listener         = listener;
        this.ipNumber         = ipNumber;
        this.command          = command ;
        this.mHandler         = mHandler;
    }

    /**
     * Public method for sending the message via OutputStream object.
     * @param message Message passed as an argument and sent via OutputStream object.
     */
    public void sendMessage(String message){
        if (out != null /*&& !out.checkError()*/) {

            try {
                out.write(CreateDataPacket("124".getBytes("UTF8"), message.getBytes("UTF8")));
                //out.write(CreateDataPacket("124".getBytes("UTF8"), "LancesLab".getBytes("UTF8")));
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
//        if(isRunning()) {
//            try {
//                socket.close();
//                Log.d(TAG, "Stopping Client");
//            } catch (IOException ex) {
//                Log.d(TAG, "Error closing socket");
//            }
//        }
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
                String path = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload/";

                path = command;
                File target_file = new File(path);//

                RandomAccessFile rw = new RandomAccessFile(target_file, "r");
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                Log.d(TAG, "In/Out created");
                //Sending message with command specified by AsyncTask
                this.sendMessage(target_file.getName());
                //this.sendMessage(command);

                //mHandler.sendEmptyMessageDelayed(ImageListFragment.SENDING,2000);
                long current_file_pointer = 0;
                //Listen for the incoming messages while mRun = true
                while (mRun) {
                    if(in.read() == 2) {

                        byte[] cmd_buffer = new byte[3];
                        in.read(cmd_buffer, 0, cmd_buffer.length);
                        byte[] recv_buff = ReadStream(in);// ======gettinh stuck here================
                        switch (Integer.parseInt(new String(cmd_buffer))){
                            case 125:
                                current_file_pointer = Long.valueOf(new String(recv_buff));
                                int buff_len = (int)(rw.length()-current_file_pointer<2000?rw.length()-current_file_pointer:2000);
                                byte[] temp_buff = new byte[buff_len];
                                if(current_file_pointer != rw.length()){
                                    rw.seek(current_file_pointer);
                                    rw.read(temp_buff, 0, temp_buff.length);
                                    out.write(CreateDataPacket("126".getBytes("UTF8"), temp_buff));
                                    out.flush();
                                    System.out.println("Uploaded percent: " + ((float)current_file_pointer/rw.length()) * 100 + "%");
                                    float p = (((float)current_file_pointer/rw.length()) * 100);
                                    String s = String.format("%.2f", p);
                                    incomingMessage = s;
                                    // ======================Report the progress=====================
                                    listener.callbackMessageReceiver("PENDING," + incomingMessage);
                                    System.out.println("==========Sent percent to Message Callback============== ");
                                }else{
                                    listener.callbackMessageReceiver("SUCCESS," + incomingMessage);
                                    stopClient();
                                }
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

//    public void Save(String directory, Bitmap bitmap)
//    {
//        var fileName = GetFileName(directory);
//        using (var os = new FileStream(fileName, FileMode.CreateNew))
//        {
//            bitmap.Compress(Bitmap.CompressFormat.Jpeg, 95, os);
//        }
//    }
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
        //public void reportProgress(int _position);

    }

}
//
//    Socket socket;// = new Socket(serverAddress, 11000);
//    //private File filePOOP;
//    BufferedInputStream bis;
//    BufferedOutputStream bos;
//    PrintWriter pw;
//    BufferedReader br;
//    ByteArrayOutputStream byteArrayOutputStream;// = new ByteArrayOutputStream();
//    OutputStream outputStream;// = socket.getOutputStream();
//    DataInputStream din;
//    DataOutputStream dout;
//    final int SERVER_PORT = 11000;
//    private static final String TAG  = "SendListAsyncTCPClient";
//    private final Handler mHandler  ;
//    private String ipNumber, incomingMessage, command;
//    BufferedReader in;
//    PrintWriter out;//WORKS!!!!!1//
//    private SendListAsyncTCPClient.MessageCallback listener = null;
//    private boolean mRun = false;
//
//
//    /**
//     * SendListAsyncTCPClient class constructor, which is created in AsyncTasks after the button click.
//     * @param mHandler Handler passed as an argument for updating the UI with sent messages
//     * @param ipNumber String retrieved from IpGetter class that is looking for ip number.
//     * @param listener Callback interface object
//     */
//    public SendListAsyncTCPClient(Handler mHandler, String command, String ipNumber, SendListAsyncTCPClient.MessageCallback listener) {
//        this.listener         = listener;
//        this.ipNumber         = ipNumber;
//        this.command          = command ;
//        this.mHandler         = mHandler;
//    }
//
//    /**
//     * Public method for sending the message via OutputStream object.
//     * @param message Message passed as an argument and sent via OutputStream object.
//     */
//    public void sendMessage(String message){
//
//        /*pppppppppppppp*/
//        if (dout != null) {
//        //if (out != null && !out.checkError()) {
//            byteArrayOutputStream = new ByteArrayOutputStream();
//
//
//
//            mHandler.sendEmptyMessageDelayed(ImageListFragment.SENDING, 1000);
//            Log.d(TAG, "Sent Message: " + message);
//            Log.d(TAG, "Incomming Message: " + incomingMessage);
//
//        }
//    }
//
//
//
//    public boolean isRunning(){
//
//        if(mRun){
//            return true;
//        } else {
//            return false;
//        }
//
//    }
//    /**
//     * Public method for stopping the TCPClient object ( and finalizing it after that ) from AsyncTask
//     */
//
//
//    public void stopClient(){
//        Log.d(TAG, "Client stopped!");
//        mRun = false;
//    }
//
//
//    public void run() {
//
//        mRun = true;
//
//        try {
//            // Creating InetAddress object from ipNumber passed via constructor from IpGetter class.
//            InetAddress serverAddress = InetAddress.getByName(ipNumber);
//
//
//            /**
//             * Sending empty message with static int value from MainActivity
//             * to update UI ( 'Connecting...' ).
//             *
//             * @see com.example.turnmeoff.MainActivity.CONNECTING
//             */
//            mHandler.sendEmptyMessageDelayed(ImageListFragment.CONNECTING,1000);
//
//            /**
//             * Here the socket is created with hardcoded port.
//             * Also the port is given in IpGetter class.
//             *
//             * @see com.example.turnmeoff.IpGetter
//             */
//            //Socket socket = new Socket(serverAddress, 11000);
//            socket = new Socket(serverAddress, 11000);
//
//            try {
//
//
//                // Create PrintWriter object for sending messages to server.
//                dout = new DataOutputStream(socket.getOutputStream());
//                //Create BufferedReader object for receiving messages from server.
//                din = new DataInputStream(socket.getInputStream());
//
//
//
//
//                Log.d(TAG, "In/Out created");
//
//                try {
//                    String tempImageName = "Test.jpg";
//                    String path = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload/";
//                    path += tempImageName;
//                    Log.d(TAG, "Connecting...");
//                    File target_file = new File(path);
//                    dout.write(CreateDataPacket("124".getBytes("UTF8"), target_file.getName().getBytes("UTF8")));
//                    //dout.write();
//                    dout.flush();
//                    //tvResults.setText("Sent file name");
//                    Log.d(TAG, "Sent file name: " + target_file.getName());
//                    RandomAccessFile rw = new RandomAccessFile(target_file, "r");
//                    boolean loop_break = false;
//                    long current_file_pointer = 0;
//                    mHandler.sendEmptyMessageDelayed(ImageListFragment.SENDING,2000);
//                    while (mRun){//true
//                        if(din.read() == 2){
//                            byte[] cmd_buff = new byte[3];
//                            din.read(cmd_buff, 0, cmd_buff.length);
//                            byte[] recv_buff = ReadStream(din);
//                            switch (Integer.parseInt(new String(recv_buff))){
//                                case 125:
//                                    current_file_pointer = Long.valueOf(new String(recv_buff));
//                                    int buff_len = (int) (rw.length() - current_file_pointer < 2000?rw.length() - current_file_pointer:2000);
//                                    byte[] temp_buff = new byte[buff_len];
//                                    if(current_file_pointer != rw.length()){
//                                        rw.seek(current_file_pointer);
//                                        rw.read(temp_buff, 0, temp_buff.length);
//                                        dout.write(CreateDataPacket("126".getBytes("UTF8"), temp_buff));
//                                        dout.flush();
//                                        String curPercent = ((float)current_file_pointer/rw.length() * 100 + "%");
//                                        System.out.println("Uploaded percent: " + ((float)current_file_pointer/rw.length()) * 100 + "%");
//                                        //listener.callbackMessageReceiver(curPercent);
//                                    }else{
//                                        loop_break = true;
//                                        mRun = false;
//                                    }
//                                    break;
//                            }
//                        }
//                        if(loop_break == true){
//                            System.out.println("Stop Server informed");
//                            dout.write(CreateDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
//                            dout.flush();
//                            System.out.println("Client Socket Closed");
//                            socket.close();
//                            break;
//                        }
//                    }
//
//                }catch (UnknownHostException ex){
//                    Log.d(TAG, "UnknownHostException: " + ex);
//                }catch (IOException ex){
//                    Log.d(TAG, "IOException: " + ex);
//                }
//                //Sending message with command specified by AsyncTask
//                //this.sendMessage(command);
//
//                //
//                //mHandler.sendEmptyMessageDelayed(ImageListFragment.SENDING,2000);
//
//                //Listen for the incoming messages while mRun = true
///*                while (mRun) {
//                    incomingMessage = in.readLine();
//                    if (incomingMessage != null && listener != null) {
//                        *//**Incoming message is passed to MessageCallback object.Next it is retrieved by AsyncTask and passed to onPublishProgress method.**//*
//                        listener.callbackMessageReceiver(incomingMessage);
//                    }
//                    incomingMessage = null;
//
//                }*/
//
//                Log.d(TAG, "Received Message: " +incomingMessage);
//
//            } catch (Exception e) {
//
//                Log.d(TAG, "Error\\n\\n\\n===============\n==============", e);
//                mHandler.sendEmptyMessageDelayed(ImageListFragment.ERROR, 2000);
//
//            } finally {
//                //outputStream.flush();
//                // outputStream.close();
//                dout.flush();
//                dout.close();
//                din.close();
////                out.flush();
////                out.close();
////                in.close();
//                socket.close();
//                mHandler.sendEmptyMessageDelayed(ImageListFragment.SENT, 3000);
//                Log.d(TAG, "Socket Closed");
//            }
//
//        } catch (Exception e) {
//
//            Log.d(TAG, "Error", e);
//            mHandler.sendEmptyMessageDelayed(ImageListFragment.ERROR, 2000);
//
//        }
//
//    }
//
//
//
//
//    /*public void getBytes(Bitmap bmp) throws IOException{*/
//    public int getBytes(Bitmap bmp) throws IOException{
//        //ImageView iv=(ImageView)findViewById(R.id.imageView1);
//        //convert the image to bitmap to be send in the intent
//        //Bitmap bmp=((BitmapDrawable)iv.getDrawable()).getBitmap();
//        int bytes = bmp.getByteCount();
//        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
//        bmp.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
//
//        byte[] array = buffer.array();
//        int start=0;
//        int len=0;
//        len=array.length;
//        if (len < 0)
//            throw new IllegalArgumentException("Negative length not allowed");
//        if (start < 0 || start >= array.length)
//            throw new IndexOutOfBoundsException("Out of bounds: " + start);
//
//        return len;
////        OutputStream out = socket.getOutputStream();
////        DataOutputStream dos = new DataOutputStream(out);
////
////        dos.writeInt(len);
////        if (len > 0) {
////            dos.write(array, start, len);
////        }
//
//    }
//
//
//    public byte[] getBytesFromBitmap(Bitmap bitmap) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
//        return stream.toByteArray();
//    }
//
//
//    //public void getSdcardImages() {
//    public String[] getSdcardImages() {
//        String path = Environment.getExternalStorageDirectory().toString()+"/LancesLab/PicsToUpload";
//        String rootPath = "file:///sdcard/LancesLab/PicsToUpload/";
//        File fp = new File(path);
//        List<File> filesList = getListFiles(fp);
//        String[] imageFiles;
//        int listLength = filesList.size();
//        imageFiles = new String[listLength];
//
//        if(listLength > 0){
//            int index = 0;
//            for (File imgFile: filesList) {
//                String fileName = imgFile.getName();
//                //imageFiles[index] = rootPath + imgFile.getName();
//                imageFiles[index] = imgFile.getName();
//                index++;
//            }
//            return imageFiles;
//        }
//        return null;
//    }
//
//
//    //Try the following code which may help you
//    List<File> getListFiles(File parentDir) {
//        ArrayList<File> inFiles = new ArrayList<File>();
//        File[] files = parentDir.listFiles();
//        for (File file : files) {
//            if (file.isDirectory()) {
//                inFiles.addAll(getListFiles(file));
//            } else {
//                inFiles.add(file);
//            }
//        }
//        return inFiles;
//    }
//
//
//    /*Base64.encodeToString*/
//    private String encodeImage(String path){
//        File imagefile = new File(path);
//        FileInputStream fis = null;
//        try{
//            fis = new FileInputStream(imagefile);
//        }catch(FileNotFoundException e){
//            e.printStackTrace();
//        }
//        Bitmap bm = BitmapFactory.decodeStream(fis);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
//        byte[] b = baos.toByteArray();
//        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
//        //Base64.de
//        Log.d(TAG, "Encoding Image to 64 bit string");
//        return encImage;
//
//    }
//
//    public Bitmap decodeFile(String filePath) {
//        // Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, o);
//        // The new size we want to scale to
//        final int REQUIRED_SIZE = 1024;
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//        int scale = 1;
//        while (true) {
//            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
//                break;
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
//        return bitmap;
//    }
//
//
//
//    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
//        // BEST QUALITY MATCH
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options);
//        // Calculate inSampleSize// Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        int inSampleSize = 1;
//        if (height > reqHeight) {
//            inSampleSize = Math.round((float)height / (float)reqHeight);
//        }
//        int expectedWidth = width / inSampleSize;
//        if (expectedWidth > reqWidth) {
//            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
//            inSampleSize = Math.round((float)width / (float)reqWidth);
//        }
//
//
//        options.inSampleSize = inSampleSize;
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//
//        return BitmapFactory.decodeFile(path, options);
//    }
//
//
//    public void imageInAndOut(String path) throws FileNotFoundException, IOException
//    {
//        try {
//            FileOutputStream fos = null;
//            File file2;
//            String MEDIA_PATH = (Environment.getExternalStorageDirectory() +
//                    "/LancesLab/PicsToUpload/" + path);
//
//            file2 = new File(MEDIA_PATH);
//
//            try {
//                if (!file2.exists()) {
//                    //file2.createNewFile();
//                    Log.d(TAG, "File not found! " + path);
//                }
//                fos = new FileOutputStream(file2);
//                Log.d(TAG, "Creating new File stream: " + path);
//            }
//            catch (Exception e1) {
//                Log.d(TAG, "Error Opening File: " + path + "\n" + e1);
//                e1.printStackTrace();
//            }
//
//            FileInputStream fis = null;
//            byte[] buffer = new byte[888192];
//
//            File fileIn;
//            fileIn = new File("/mnt/sdcard/tempPdf.pdf");
//
//            fis = new FileInputStream(fileIn);
//
//            int input = 0;
//
//            while ((input = fis.read(buffer)) != -1) { // <<-- STACK TRACE ERROR
//                fos.write(buffer, 0, input);
//                Log.i("<<<<<>>>>", "fos2 is is; " + buffer[7]);
//                try {
//                    fos.flush();
//                    fos.close();
//                    fis.close();
//                }
//                catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            //}
//            //textViewOne.setText(String.valueOf(input));
//        }
//        catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//
//    public void sendEndOfMessage(String message) {
////        if (out != null && !out.checkError()) {
////
////        }
//    }
//
//    public void initNet() throws Exception
//    {
//
////        Socket s=null;
////        try{
////            //setTitle("Startd Client");
////            System.out.println("Started Client NEW !!!!");
////            s=new Socket("localhost",SERVER_PORT);
////            bis=new BufferedInputStream(new FileInputStream(filePOOP));
////            bos=new BufferedOutputStream(s.getOutputStream());
////            pw=new PrintWriter(s.getOutputStream(),true);
////            br=new BufferedReader(new InputStreamReader(s.getInputStream()));
////
////        }catch(Exception e)
////        {
////            System.out.println("Error "+e);
////        }finally{
////
////
////        }
//    }
//
//    public void sendFile(File imgFile) throws IOException
//    {
////        pw.println(imgFile);//PrintWriter pw; LIKE out
////        //pw.println(filePOOP.getName());//PrintWriter pw;  LIKE out
////        int ch = bis.read();//BufferedInputStream
////        int counter=0;
////        pw.println(imgFile.length());//PrintWriter pw;  LIKE out
////        while(counter<imgFile.length())
////        {
////            bos.write(ch);//BufferedOutputStream
////            System.out.println(ch);
////            ch=bis.read();//BufferedInputStream
////            counter++;
////        }
////        System.out.println(ch);
////        bos.write(ch);
////        bos.flush();
////        //JOptionPane.showMessageDialog(null,br.readLine());
////        Log.d(TAG, br.readLine());// BufferedReader br;
//
//    }
//
//
//
//    //private byte[] CreateDataPacket(byte[] data){
//    private byte[] CreateDataPacket(byte[]  cmd,byte[] data){
//        byte[] packet =null;
//        try{
//            byte[] initialize = new byte[1];
//            initialize[0] = 2;
//            byte[] separator = new byte[1];
//            separator[0] = 4;
//            byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
//            packet = new byte[initialize.length+cmd.length+separator.length+data_length.length+data.length];// packet size
//            // copy the byte arrays into the packet array
//            System.arraycopy(initialize, 0, packet, 0, initialize.length);
//            System.arraycopy(cmd, 0, packet, initialize.length, cmd.length);
//            System.arraycopy(data_length, 0, packet, initialize.length + cmd.length, data_length.length);
//            System.arraycopy(separator, 0, packet, initialize.length + cmd.length + data_length.length, separator.length);
//            System.arraycopy(data, 0, packet, initialize.length + cmd.length + data_length.length + separator.length, data.length);
//
//        }catch (UnsupportedEncodingException ex){
//            Log.d(TAG, "UnsupportedEncodingException " + ex.toString());
//        }
//
//        return packet;
//    }
//
//    private byte[] ReadStream(DataInputStream din){
//        byte[] data_buff = null;
//
//        try {
//            int b = 0;
//            String buff_length = "";
//            while ((b = din.read()) != 4) {
//                buff_length += (char) b;
//            }
//
//            int data_length = Integer.parseInt(buff_length);
//            data_buff = new byte[Integer.parseInt(buff_length)];// put the info into data_buff
//            int byte_read = 0;
//            int byte_offset = 0;
//            while (byte_offset < data_length){
//                //din.read(data_buff, byte_offset, data_length - byte_offset);
//                byte_read = din.read(data_buff, byte_offset, data_length - byte_offset);
//                byte_offset += byte_read;
//            }
//        } catch (IOException e) {
//            Log.d(TAG, "IOException reading DataInputStream" + e.toString());
//        }
//        return data_buff;
//    }
//
//
//
//    /**
//     * Callback Interface for sending received messages to 'onPublishProgress' method in AsyncTask.
//     *
//     */
//    public interface MessageCallback {
//        /**
//         * Method overriden in AsyncTask 'doInBackground' method while creating the TCPClient object.
//         * @param message Received message from server app.
//         */
//        public void callbackMessageReceiver(String message);
//    }
//
//
////    private Class ClientWorker implements Runnable{
////        Socket target_socket;
////
////        public ClientWorker(Socket rev_socket){
////            target_socket = rev_socket;
////        }
////
////        @Override
////         public void run(){
////            throw new UnsupportedOperationException("Not supported yet.");
////        }
////
////    }
//
//}
