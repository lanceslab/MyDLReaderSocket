package com.lanceslab.mydlreadersocket;

//
//import java.io.File;
//import java.util.ArrayList;
////import org.apache.http.client.entity.UrlEncodedFormEntity;
//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//@SuppressWarnings("unused")
//public class Client extends Activity {
//    //public class UploadActivity extends Activity {
//    private int count;
//    private Bitmap[] thumbnails;
//    private boolean[] thumbnailsselection;
//    private String[] arrPath;
//    private ImageAdapter imageAdapter;
//    private static final int PICK_FROM_CAMERA = 1;
//    ArrayList<String> IPath = new ArrayList<String>();
//    public static Uri uri;
//
//    /** Called when the activity is first created. */
//    @SuppressWarnings("deprecation")
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.activity_upload);
//        setContentView(R.layout.activity_main3);
//        final String[] columns = { MediaStore.Images.Media.DATA,
//                MediaStore.Images.Media._ID };
//        final String orderBy = MediaStore.Images.Media._ID;
//        Cursor imagecursor = managedQuery(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
//                null, orderBy);
//        int image_column_index = imagecursor
//                .getColumnIndex(MediaStore.Images.Media._ID);
//
//        this.count = imagecursor.getCount();
//        this.thumbnails = new Bitmap[this.count];
//        this.arrPath = new String[this.count];
//        this.thumbnailsselection = new boolean[this.count];
//        for (int i = 0; i < this.count; i++) {
//            imagecursor.moveToPosition(i);
//            int id = imagecursor.getInt(image_column_index);
//            int dataColumnIndex = imagecursor
//                    .getColumnIndex(MediaStore.Images.Media.DATA);
//            thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
//                    getApplicationContext().getContentResolver(), id,
//                    MediaStore.Images.Thumbnails.MICRO_KIND, null);
//            arrPath[i] = imagecursor.getString(dataColumnIndex);
//        }
//        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
//        imageAdapter = new ImageAdapter();
//        imagegrid.setAdapter(imageAdapter);
//        imagecursor.close();
//
//        // final Button cameraBtn = (Button) findViewById(R.id.cameraBtn);
//        // cameraBtn.setOnClickListener(new OnClickListener() {
//        //
//        // @Override
//        // public void onClick(View arg0) {
//        // // TODO Auto-generated method stub
//        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //
//        // uri = Uri.fromFile(new File(Environment
//        // .getExternalStorageDirectory(), "tmp_avatar_"
//        // + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//        //
//        // intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
//        // try {
//        // intent.putExtra("return-data", true);
//        // startActivityForResult(intent, PICK_FROM_CAMERA);
//        // } catch (ActivityNotFoundException e) {
//        // e.printStackTrace();
//        // }
//        // }
//        // });
//
//        final Button uploadBtn = (Button) findViewById(R.id.uploadDONE);
//        uploadBtn.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                final int len = thumbnailsselection.length;
//                int cnt = 0;
//                String selectImages = "";
//                for (int i = 0; i < len; i++) {
//                    if (thumbnailsselection[i]) {
//                        cnt++;
//                        selectImages = arrPath[i];
//                        IPath.add(selectImages);
//                    }
//                }
//
//                if (cnt == 0) {
//                    Toast.makeText(getApplicationContext(),
//                            "Please select at least one image",
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "You've selected Total " + cnt + " image(s).",
//                            Toast.LENGTH_LONG).show();
//                    Log.d("SelectedImages", selectImages);
//
//                    // Intent intentMessage = new Intent();
//                    // intentMessage.putExtra("IMAGE", IPath);
//                    // setResult(Activity.RESULT_OK, intentMessage);
//                    // finish();
//
//                    //Intent intentMessage = new Intent(UploadActivity.this, MainActivity.class);
//                    Intent intentMessage = new Intent(Client.this, MainActivity.class);
//                    intentMessage.putStringArrayListExtra("IMAGE", IPath);
//                    startActivity(intentMessage);
//                }
//            }
//        });
//    }
//
//    public class ImageAdapter extends BaseAdapter {
//        private LayoutInflater mInflater;
//
//        public ImageAdapter() {
//            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        public int getCount() {
//            return count;
//        }
//
//        public Object getItem(int position) {
//            return position;
//        }
//
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = mInflater.inflate(R.layout.galleryitem, null);
//                holder.imageview = (ImageView) convertView
//                        .findViewById(R.id.thumbImage);
//                holder.checkbox = (CheckBox) convertView
//                        .findViewById(R.id.itemCheckBox);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            holder.checkbox.setId(position);
//            holder.imageview.setId(position);
//            holder.checkbox.setOnClickListener(new OnClickListener() {
//
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    CheckBox cb = (CheckBox) v;
//                    int id = cb.getId();
//                    if (thumbnailsselection[id]) {
//                        cb.setChecked(false);
//                        thumbnailsselection[id] = false;
//                    } else {
//                        cb.setChecked(true);
//                        thumbnailsselection[id] = true;
//                    }
//                }
//            });
//
//            // holder.imageview.setOnClickListener(new OnClickListener() {
//            //
//            // public void onClick(View v) {
//            // // TODO Auto-generated method stub
//            // int id = v.getId();
//            // Intent intent = new Intent();
//            // intent.setAction(Intent.ACTION_VIEW);
//            // intent.setDataAndType(Uri.parse("file://" + arrPath[id]),
//            // "image/*");
//            // startActivity(intent);
//            // }
//            // });
//
//            holder.imageview.setImageBitmap(thumbnails[position]);
//            holder.checkbox.setChecked(thumbnailsselection[position]);
//            holder.id = position;
//            return convertView;
//        }
//    }
//
//    class ViewHolder {
//        ImageView imageview;
//        CheckBox checkbox;
//        int id;
//    }
//
//    @Override
//    public void onBackPressed() {
//        // TODO Auto-generated method stub
//        Intent i = new Intent(UploadActivity.this, MainActivity.class);
//        UploadActivity.this.finish();
//        startActivity(i);
//        super.onBackPressed();
//    }
//}
//
//












//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketException;
//import java.util.Enumeration;
//import android.support.v7.app.ActionBarActivity;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.TextView;
//
//
//
//
//public class Client extends ActionBarActivity {
//    private TextView tvClientMsg,tvServerIP,tvServerPort;
//    public String SERVER = "192.168.1.106";
//    private final int SERVER_PORT = 11000; //Define the server port
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main3);
//
//        tvClientMsg = (TextView) findViewById(R.id.textViewClientMessage);
//        tvServerIP = (TextView) findViewById(R.id.textViewServerIP);
//        tvServerIP.setText(SERVER);
//        tvServerPort = (TextView) findViewById(R.id.textViewServerPort);
//        tvServerPort.setText(Integer.toString(SERVER_PORT));
//        //Call method
//        getDeviceIpAddress();
//        //New thread to listen to incoming connections
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    //Create a server socket object and bind it to a port
//                    ServerSocket socServer = new ServerSocket(SERVER_PORT);
//                    //Create server side client socket reference
//                    Socket socClient = null;
//                    //Infinite loop will listen for client requests to connect
//                    while (true) {
//                        //Accept the client connection and hand over communication to server side client socket
//                        socClient = socServer.accept();
//                        //For each client new instance of AsyncTask will be created
//                        ServerAsyncTask serverAsyncTask = new ServerAsyncTask();
//                        //Start the AsyncTask execution
//                        //Accepted client socket object will pass as the parameter
//                        serverAsyncTask.execute(new Socket[] {socClient});
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//    /**
//     * Get ip address of the device
//     */
//    public void getDeviceIpAddress() {
//        try {
//            //Loop through all the network interface devices
//            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
//                    .getNetworkInterfaces(); enumeration.hasMoreElements();) {
//                NetworkInterface networkInterface = enumeration.nextElement();
//                //Loop through all the ip addresses of the network interface devices
//                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface.getInetAddresses(); enumerationIpAddr.hasMoreElements();) {
//                    InetAddress inetAddress = enumerationIpAddr.nextElement();
//                    //Filter out loopback address and other irrelevant ip addresses
//                    if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
//                        //Print the device ip address in to the text view
//                        tvServerIP.setText(inetAddress.getHostAddress());
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            Log.e("ERROR:", e.toString());
//        }
//    }
//
///*    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }*/
//    /**
//     * AsyncTask which handles the commiunication with clients
//     */
//    class ServerAsyncTask extends AsyncTask<Socket, Void, String> {
//        //Background task which serve for the client
//        @Override
//        protected String doInBackground(Socket... params) {
//            String result = null;
//            //Get the accepted socket object
//            Socket mySocket = params[0];
//            try {
//                //Get the data input stream comming from the client
//                InputStream is = mySocket.getInputStream();
//                //Get the output stream to the client
//                PrintWriter out = new PrintWriter(
//                        mySocket.getOutputStream(), true);
//                //Write data to the data output stream
//                out.println("Hello from server");
//                //Buffer the data input stream
//                BufferedReader br = new BufferedReader(
//                        new InputStreamReader(is));
//                //Read the contents of the data buffer
//                result = br.readLine();
//                //Close the client connection
//                mySocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            //After finishing the execution of background task data will be write the text view
//            tvClientMsg.setText(s);
//        }
//    }
//}



/*
=====================================
*/



//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.util.Log;
//import android.widget.Toast;
//
//public class Client {
//
//    private static final String TAG = Client.class.getSimpleName();
//
//    private Socket socket;
//    private PrintWriter out;
//    private boolean connected;
//
//    public Client()
//    {
//        socket = null;
//        out = null;
//        connected = false;
//    }
//
//
//    public void connect(Context context, String host, int port)
//    {
//        new ConnectTask(context).execute(host, String.valueOf(port));
//    }
//
//    private class ConnectTask extends AsyncTask<String, Void, Void> {
//
//        private Context context;
//
//        public ConnectTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            showToast(context, "Connecting..");
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            if (connected) {
//                showToast(context, "Connection successfull");
//            }
//            super.onPostExecute(result);
//        }
//
//        private String host;
//        private int port;
//
//        @Override
//        protected Void doInBackground(String... params) {
//            try {
//                String host = params[0];
//                int port = Integer.parseInt(params[1]);
//                socket = new Socket(host, port);
//                out = new PrintWriter(socket.getOutputStream(), true);
//            } catch (UnknownHostException e) {
//                showToast(context, "Don't know about host: " + host + ":" + port);
//                Log.e(TAG, e.getMessage());
//            } catch (IOException e) {
//                showToast(context, "Couldn't get I/O for the connection to: " + host + ":" + port);
//                Log.e(TAG, e.getMessage());
//            }
//            connected = true;
//            return null;
//        }
//
//
//    }
//
//    public void disconnect(Context context)
//    {
//        if ( connected )
//        {
//            try {
//                out.close();
//                socket.close();
//                connected = false;
//            } catch (IOException e) {
//                showToast(context, "Couldn't get I/O for the connection");
//                Log.e(TAG, e.getMessage());
//            }
//        }
//    }
//
//
//    /**
//     * Send command to a Pure Data audio engine.
//     */
//    public void send(String command)
//    {
//        if ( connected ) out.println(command +";");
//    }
//
//    private void showToast(final Context context, final String message) {
//        new Handler(context.getMainLooper()).post(new Runnable() {
//
//            @Override
//            public void run() {
//                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//}
//



//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//public class Client extends Activity {
//
//    ProgressBar loadingProgressBar;
//    ImageView[] targetImage = new ImageView[5];
//    private static final String TAG  = "SendListAsyncTCPClient";
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main3);
//
//
////        Intent thisIntent = getIntent();
////        Bundle bd = thisIntent.getExtras();
////        if(bd != null) {
////
////        }
//
////        targetImage[0] = (ImageView)findViewById(R.id.target0);
////        targetImage[1] = (ImageView)findViewById(R.id.target1);
////        targetImage[2] = (ImageView)findViewById(R.id.target2);
////        targetImage[3] = (ImageView)findViewById(R.id.target3);
////        targetImage[4] = (ImageView)findViewById(R.id.target4);
//
//        //loadingProgressBar = (ProgressBar)findViewById(R.id.loadingprogress);
//
//        //Load bitmap from internet
//        //As a example, I make all images load from the same source
//        String onLineImage0 = "1491249714674.jpg";
//        String onLineImage1 = "14912497146745.jpg";
//        String onLineImage2 = "14912497146746.jpg";
//        String onLineImage3 = "14912497146747.jpg";
//        String onLineImage4 = "1491249714674.jpg";
//        //URL onLineURL0, onLineURL1, onLineURL2, onLineURL3, onLineURL4;
//        File onLineURL0, onLineURL1, onLineURL2, onLineURL3, onLineURL4;
//        try {
////            onLineURL0 = new URL(onLineImage0);
////            onLineURL1 = new URL(onLineImage1);
////            onLineURL2 = new URL(onLineImage2);
////            onLineURL3 = new URL(onLineImage3);
////            onLineURL4 = new URL(onLineImage4);
//            onLineURL0 = new File(onLineImage0);
//            onLineURL1 = new File(onLineImage1);
//            onLineURL2 = new File(onLineImage2);
//            onLineURL3 = new File(onLineImage3);
//            onLineURL4 = new File(onLineImage4);
//            new MyNetworkTask(5, targetImage, loadingProgressBar)
//                    .execute(onLineURL0, onLineURL1, onLineURL2, onLineURL3, onLineURL4);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }/*
//        catch (MalformedURLException e) {
//            e.printStackTrace();
//        }*/
//
//
//
//
//    }
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





//    //private class MyNetworkTask extends AsyncTask<URL, Integer, Void>{
//    private class MyNetworkTask extends AsyncTask<File, Integer, Void>{
//        ImageView[] tIV;
//        Bitmap[] tBM;
//        ProgressBar tProgressBar;
//
//        public MyNetworkTask(int numberOfImage, ImageView[] iv, ProgressBar pb){
//
//            tBM = new Bitmap[numberOfImage];
//
//            tIV = new ImageView[numberOfImage];
//            for(int i = 0; i < numberOfImage; i++){
//                tIV[i] = iv[i];
//            }
//
//            tProgressBar = pb;
//        }
//
//        @Override
//        protected Void doInBackground(File... urls) {
//            //protected Void doInBackground(URL... urls) {
//
//            if (urls.length > 0){
//                for(int i = 0; i < urls.length; i++){
//                    //URL networkUrl = urls[i];
//                    File imgFile = urls[i];
//                    //try {
//                        //tBM[i] = BitmapFactory.decodeStream(networkUrl.openConnection().getInputStream());
//                        Bitmap bitmap2 = decodeSampledBitmapFromFile(imgFile.getAbsolutePath(),500,500);
//                        tBM[i] = bitmap2;
//                    /*} catch (IOException e) {
//                        e.printStackTrace();
//                    }*/
//
//                    publishProgress(i);
//
//                    //insert dummy delay to simulate lone time operation
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        Log.d(TAG, "Caught InterruptedException exception");
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//            return null;
//        }
//
//
//
//
//        @Override
//        protected void onPostExecute(Void result) {
//            Toast.makeText(getBaseContext(), "Finished", Toast.LENGTH_LONG).show();
//            Log.d(TAG, "Inside onPostExecute Method: ");
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//
//            if(values.length > 0){
//                for(int i = 0; i < values.length; i++){
//                    tIV[values[i]].setImageBitmap(tBM[values[i]]);
//                    tProgressBar.setProgress(values[i]+1);
//                    Log.d(TAG, "================================ Progress: " + (values[i]+1));
//                }
//            }
//
//        }
//
//    }
//
//}
//
//
//






//import android.util.Log;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.nio.ByteBuffer;
//import java.util.Arrays;
//
//public class Client {
//
//    private static final String TAG  = "Client Class";
//    public static final int BUFFER_SIZE = 500102;
//    public void send(String file_name){
//        try {
//            Socket socket = new Socket("192.168.1.116", 11000);
//            File file = new File(file_name);
//            System.out.println(file_name);
//            ObjectInputStream ois = new ObjectInputStream(
//                    socket.getInputStream());
//            ObjectOutputStream oos = new ObjectOutputStream(
//                    socket.getOutputStream());
//            oos.writeObject(file.getName());
//            FileInputStream fis = new FileInputStream(file);
//            //byte[] buffer = new byte[Server.BUFFER_SIZE];
//            byte[] buffer = new byte[BUFFER_SIZE];
//            Integer bytesRead = 0;
//            while ((bytesRead = fis.read(buffer)) > 0) {
//                oos.writeObject(bytesRead);
//                oos.writeObject(Arrays.copyOf(buffer, buffer.length));
//            }
//            ois = null;
//            oos = null;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    public void sendFile(File file_name){
//        try {
//            Socket socket = new Socket("192.168.1.116", 11000);
//            File file =file_name;// new File(file_name);
//            System.out.println(file_name);
//            ObjectInputStream ois = new ObjectInputStream(
//                    socket.getInputStream());
//            ObjectOutputStream oos = new ObjectOutputStream(
//                    socket.getOutputStream());
//            oos.writeObject(file.getName());
//            FileInputStream fis = new FileInputStream(file);
//            //byte[] buffer = new byte[Server.BUFFER_SIZE];
//            byte[] buffer = new byte[BUFFER_SIZE];
//            Integer bytesRead = 0;
//            while ((bytesRead = fis.read(buffer)) > 0) {
//                oos.writeObject(bytesRead);
//                oos.writeObject(Arrays.copyOf(buffer, buffer.length));
//            }
//            ois = null;
//            oos = null;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//
//    Socket connection;
//    ObjectInputStream input;
//    ByteBuffer byteBuffer;
//    byte[] buffer;
//    public void poop(){
//
//
//
//        try {
//            connection = new Socket("192.168.1.116", 8080);
//            input = new ObjectInputStream(connection.getInputStream());
//            int i = 0;
//            while (i < 5) {
//                i++;
//                //ByteBuffer buffer = ByteBuffer.allocate(baseSixtyFourSize); //Create a new buffer
//                buffer = (byte[]) input.readObject();
//                //Toast.makeText(getApplicationContext(), "buffer length = " + buffer.length, Toast.LENGTH_LONG).show();
//                //screenCapture.setImageBitmap(BitmapFactory.decodeByteArray(buffer, 0, buffer.length));
//                Log.d(TAG, "================================== buffer length =  " + buffer.length);
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            Log.d(TAG, "\n\n==================================   " + e.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d(TAG, "\n\n==================================   " + e.toString());
//        }finally {
//            try {
//                connection.close();
//            } catch (IOException e) {
//               //Toast.makeText(getApplicationContext(), "Error with closing connection", Toast.LENGTH_LONG).show();
//                Log.d(TAG, "\n\n==================================   " + e.toString());
//            }
//        }
//
//    }
//
//
//    public static void main(String[] args) throws Exception {
//        Client c = new Client();
//        //c.send("D://1.jpg");         // first image path
//        //c.send("D://0.jpg");        //  second image path
//    }
//}


/*PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP*/




//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.nio.charset.StandardCharsets;
//
///**
// * Created by LanceTaylor on 4/4/2017.
// */
//
//public class Client extends AsyncTask<Void, Void, Void> {
//
//    String dstAddress;
//    int dstPort;
//    String response = "";
//    TextView textResponse;
//    String licenseInfo = "";
//    JSONObject licenseInfoJson;
//
//    //    Client(String addr, int port, TextView textResponse) {
//    Client(String addr, int port, TextView textResponse, String liInfo) {
//        dstAddress = addr;
//        dstPort = port;
//        licenseInfo = liInfo;
//        this.textResponse = textResponse;
//    }
//    Client(String addr, int port, TextView textResponse, JSONObject liInfo) {
//        dstAddress = addr;
//        dstPort = port;
//        licenseInfoJson = liInfo;
//        this.textResponse = textResponse;
//    }
//
//
//
//    @Override
//    //protected String doInBackground(Void... arg0) {
//    protected Void doInBackground(Void... arg0) {
//        Socket socket = null;
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
//        try {
//            socket = new Socket(dstAddress, dstPort);
////            PrintWriter out;
////            BufferedReader in;
//            //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
//            //byte[] buffer = new byte[1024];
//            //int bytesRead;
//            System.out.println("Client about to Send.");
//            licenseInfo = licenseInfo + "<EOF>";
//            System.out.println("This is what the info sent looks like.\n " + licenseInfo);
//            byteArrayOutputStream.write(licenseInfo.getBytes());
//            byteArrayOutputStream.flush();
//            byteArrayOutputStream.close();
//            System.out.println("Client sent.");
//
////            OutputStreamWriter out = new OutputStreamWriter(
////                    socket.getOutputStream(), StandardCharsets.UTF_8)) {
////                out.write(licenseInfoJson.toString());
////            }
//            //out = new PrintWriter(licenseInfo);
//            //out.println(licenseInfo);
////            out = new PrintWriter(licenseInfo);
////            out.println(licenseInfo);
////            out.append("<EOF>");
//            //out.flush();
//            //boolean errState = out.checkError();
//            //out.close();
//
//
////            if(errState == true){
////                response = "Error connecting\n :( :(";
////            }else{
////                response = "Success connecting\n :) :)";
////            }
//
//
//
//
//
//            //InputStream inputStream = socket.getInputStream();
//
//
////            while ((bytesRead = inputStream.read(buffer)) != -1) {
////                byteArrayOutputStream.write(buffer, 0, bytesRead);
////                response += byteArrayOutputStream.toString("UTF-8");
////            }
//
//            //if (out != null && !out.checkError()) {
////            if (byteArrayOutputStream != null) {
////                byteArrayOutputStream..println(message);
////                out.flush();
////                mHandler.sendEmptyMessageDelayed(LicenseResultsActivity.SENDING, 1000);
////                Log.d(TAG, "Sent Message: " + message);
////
////            }
//
//
//
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            response = "UnknownHostException: " + e.toString();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            response = "IOException: " + e.toString();
//        } catch (Exception e) {
//            response = "Exception Not Known: " + e.toString();
//        } finally {
//            if (socket != null) {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        //socket.close();
//        //return response;
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//        textResponse.setText(response);
//        super.onPostExecute(result);
//    }
//
//
//}
//






//    @Override
//    //protected String doInBackground(Void... arg0) {
//    protected Void doInBackground(Void... arg0) {
//        Socket socket = null;
//
//        try {
//            socket = new Socket(dstAddress, dstPort);
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
//            byte[] buffer = new byte[1024];
//
//            int bytesRead;
//            InputStream inputStream = socket.getInputStream();
//
//			/*
//             * notice: inputStream.read() will block if no data return
//			 */
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//                response += byteArrayOutputStream.toString("UTF-8");
//            }
//
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            response = "UnknownHostException: " + e.toString();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            response = "IOException: " + e.toString();
//        } finally {
//            if (socket != null) {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        //return response;
//        return null;
//    }