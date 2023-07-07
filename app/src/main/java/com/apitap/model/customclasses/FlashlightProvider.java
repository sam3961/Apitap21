package com.apitap.model.customclasses;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;

public class FlashlightProvider {
    private static final String TAG = FlashlightProvider.class.getSimpleName();
    private Camera mCamera;
    private Camera.Parameters parameters;
    private CameraManager camManager;
    private Context context;
    public static final String CAMERA_FRONT = "1";
    public static final String CAMERA_BACK = "0";

    private String cameraId = CAMERA_BACK;
    private boolean isFlashSupported;
    private boolean isTorchOn;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mPreviewSession;

    public FlashlightProvider(Context context) {
        this.context = context;
    }

    public void turnFlashlightOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

                String cameraId = null; // Usually front camera is at 0 position
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0];
                    camManager.setTorchMode(cameraId, true);
                }
            } catch (CameraAccessException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            mCamera = Camera.open();
            parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
    }

    public void turnFlashlightOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                String cameraId;
                camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
                    camManager.setTorchMode(cameraId, false);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            mCamera = Camera.open();
            parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            mCamera.stopPreview();
        }
    }

}