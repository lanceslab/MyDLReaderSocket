package com.lanceslab.mydlreadersocket;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import static com.lanceslab.mydlreadersocket.R.id.cam;
//import de.vogella.cameara.api.R;

/**
 * Created by LanceTaylor on 4/9/2017.
 */

public class MakePhotoActivity extends Activity {

    List<Camera.Size> mSupportedPreviewSizes;
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    public final static String DEBUG_TAG = "MakePhotoActivity";
    private Camera camera;
    private int cameraId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cam_main);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        //surfaceView = (SurfaceView) findViewById(R.id.preview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceCallback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // do we have a camera?
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                camera = Camera.open(cameraId);
            }
        }
    }

    public void onClick(View view) {
        camera.startPreview();
        camera.takePicture(null, null,
                new PhotoHandler(getApplicationContext()));
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(DEBUG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }


    SurfaceHolder.Callback surfaceCallback=new SurfaceHolder.Callback(){

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (Throwable t) {
                Toast vinToast = Toast.makeText(MakePhotoActivity.this,
                        "ERROR: surface created: \n" + t.toString(), Toast.LENGTH_LONG);
                vinToast.show();
            }
        }

        public void surfaceChanged(SurfaceHolder holder,int format, int width,int height) {

//            if(camera != null) {
//                Camera.Parameters parameters = camera.getParameters();
//                parameters.setPreviewSize(width, height);
//                requestLayout();
//
//                camera.setParameters(parameters);
//                camera.startPreview();
//            }

            Camera.Parameters parameters = camera.getParameters();
            //parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            parameters.setPreviewSize(width, height);
            camera.setParameters(parameters);
            camera.startPreview();

//            Camera.Parameters params = camera.getParameters();
//            mSupportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
//            params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);

            //Camera.Size size = getBestPreviewSize(width, height, params);
//            Camera.Size size = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
//            Camera.Size pictureSize = getSmallestPictureSize(params);
//            if (size != null && pictureSize != null) {
//                params.setPreviewSize(size.width, size.height);
//                params.setPictureSize(pictureSize.width, pictureSize.height);
//                camera.setParameters(params);
//                camera.startPreview();
//
//            }
//            Camera.Size size = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
//            if (size != null) {
//                params.setPreviewSize(size.width, size.height);
//                camera.setParameters(params);
//                camera.startPreview();
//
//            }
        }



        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
        }
    };



    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }



}
