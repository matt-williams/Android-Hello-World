package com.opentok.view;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.opentok.Publisher;

public class PublisherView extends BaseView implements Publisher.Listener {
    private static final String TAG = PublisherView.class.getCanonicalName();
    private Camera mCamera;
    private Publisher mPublisher;
    private boolean mSurfaceCreated;
    private boolean mPreviewing;

    public PublisherView(Context context) {
        super(context);
        init();
    }

    public PublisherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PublisherView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceCreated = true;
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
            mPreviewing = false;
        }
        // This usually maps to the front camera.
        mCamera = Camera.open(Camera.getNumberOfCameras() - 1);
        super.surfaceCreated(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceCreated = false;
        super.surfaceDestroyed(holder);
        if ((mPublisher == null) && (mCamera != null)) {
            mCamera.release();
            mCamera = null;
            mPreviewing = false;
        }
    }


    /**
     * Invoked when Our Publisher's rendering surface comes available.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        super.surfaceChanged(holder, format, width, height);
        if (mPreviewing) {
            mCamera.stopPreview();
        }
        try {
            mCamera.setPreviewDisplay(getHolder());
            // Note: preview will continue even if we fail to connect.
            mCamera.startPreview();
            mPreviewing = true;
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
    }

    @Override
    public void onSessionConnected() {
        super.onSessionConnected();
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                if (mCamera != null) {
                    // Session is ready to publish. Create Publisher instance from our rendering surface and camera, then connect.
                    mPublisher = mSession.createPublisher(mCamera, PublisherView.this.getHolder());
                    mPublisher.connect();
                }
            }
        });
    }

    @Override
    public void onSessionDisconnected() {
        super.onSessionDisconnected();
        android.util.Log.e(TAG, "onSessionDisconnected " + getHolder().getSurface() + " " + mCamera);
        if ((!mSurfaceCreated) && (mCamera != null)) {
            mCamera.release();
            mCamera = null;
            mPreviewing = false;
        }
    }

    @Override
    public void onPublisherStreamingStarted() {
        // Nothing to do.
    }

    @Override
    public void onPublisherDisconnected() {
        mPublisher = null;
    }

    @Override
    public void onPublisherFailed() {
        mPublisher = null;
    }

    private void init() {
        // Although this call is deprecated, Camera preview still seems to require it :-\
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // SurfaceHolders are not initially available, so we'll wait to create the publisher
        getHolder().addCallback(this);
    }
}