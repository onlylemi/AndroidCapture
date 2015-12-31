package com.onlylemi.android.capture;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;


public class PreviewSurface extends CameraSurface implements
        Camera.PreviewCallback {

    private static final String TAG = "PreviewSurface:";

    private String ipname = "";
    private ColorListener listener;

    public PreviewSurface(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public void onPreviewFrame(byte[] paramArrayOfByte, Camera paramCamera) {
        Size size = paramCamera.getParameters().getPreviewSize();
        // use "image.compressToJpeg()" to change image data format from "YUV" to "jpg"
        YuvImage image = new YuvImage(paramArrayOfByte, ImageFormat.NV21,
                size.width, size.height, null);
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();

        try {
            if (image != null) {
                image.compressToJpeg(new Rect(0, 0, size.width, size.height),
                        80, outstream);
                outstream.flush();
                //start thread to send image data
                Thread th = new SendDataThread(outstream, ipname, 6000);
                th.start();
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error:" + ex.getMessage());
        }

        if (listener != null) {
            int color = BitmapFactory.decodeStream(new ByteArrayInputStream(outstream.toByteArray
                    ())).getPixel(size.width / 2, size.height / 2);
            listener.onColor(color);
        }

    }

    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        super.surfaceCreated(paramSurfaceHolder);
        this.camera.setPreviewCallback(this);
    }

    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
        this.camera.setPreviewCallback(null);
        super.surfaceDestroyed(paramSurfaceHolder);
    }

    public void setIP(String ipname) {
        this.ipname = ipname;
    }


    public void setColorListener(ColorListener listener) {
        this.listener = listener;
    }

    public interface ColorListener {
        void onColor(int color);
    }

}
