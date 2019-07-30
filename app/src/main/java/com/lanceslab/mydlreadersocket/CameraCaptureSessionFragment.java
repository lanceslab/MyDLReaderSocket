








//public class CameraCaptureSession {
//
//}
/*END CLASS*/










//    private ImageReader imageReader;
//    private Handler backgroundHandler;
//    private HandlerThread backgroundThread;
//    private String cameraId;
//    private CameraDevice cameraDevice;
//
//
//private ImageReader imageReader;
//    private Handler backgroundHandler;
//    private HandlerThread backgroundThread;
//    private String cameraId;
//    private CameraDevice cameraDevice;
//    private CameraCaptureSession cameraCaptureSession;
//    @Override
//    public void onCreate() {
//        setupCamera2();
//    }
//
//    private void setupCamera2() {
//        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//
//        try {
//
//            for (String cameraId : manager.getCameraIdList()) {
//                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
//
//                if (characteristics.get(CameraCharacteristics.LENS_FACING) != CameraCharacteristics.LENS_FACING_FRONT) {
//                    continue;
//                }
//
//                this.cameraId = cameraId;
//
//                int[] picSize = Settings.getPictureSize();
//                int picWidth = picSize[0];
//                int picHeight = picSize[1];
//
//                imageReader = ImageReader.newInstance(picWidth, picHeight, ImageFormat.JPEG, 2);
//                imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler);
//            }
//
//        } catch (CameraAccessException | NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void openCamera2() {
//        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//        try {
//
//            manager.openCamera(cameraId, cameraStateCallback, backgroundHandler);
//
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    private final CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {
//        @Override
//        public void onOpened(CameraDevice device) {
//            cameraDevice = device;
//            createCameraCaptureSession();
//        }
//
//        @Override
//        public void onDisconnected(CameraDevice cameraDevice) {}
//
//        @Override
//        public void onError(CameraDevice cameraDevice, int error) {}
//    };
//
//
//
//    private void createCaptureSession() {
//        List<Surface> outputSurfaces = new LinkedList<>();
//        outputSurfaces.add(imageReader.getSurface());
//
//        try {
//
//            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
//                @Override
//                public void onConfigured(CameraCaptureSession session) {
//                    cameraCaptureSession = session;
//                }
//
//                @Override
//                public void onConfigureFailed(CameraCaptureSession session) {}
//            }, null);
//
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private final ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
//        @Override
//        public void onImageAvailable(ImageReader reader) {
//            Image image = imageReader.acquireLatestImage();
//            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//            byte[] bytes = new byte[buffer.remaining()];
//            buffer.get(bytes);
//            try {
//                save(bytes, file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            image.close();
//            createCaptureRequest();
//        }
//        public void onConfigured(CameraCaptureSession session) {
//            cameraCaptureSession = session;
//            createCaptureRequest();
//        }
//    };
//
//
//    private void createCaptureRequest() {
//        try {
//
//            CaptureRequest.Builder requestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
//            requestBuilder.addTarget(imageReader.getSurface());
//
//            // Focus
//            requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//
//            // Orientation
//            int rotation = windowManager.getDefaultDisplay().getRotation();
//            requestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
//
//            cameraCaptureSession.capture(requestBuilder.build(), camera2Callback, null);
//
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
