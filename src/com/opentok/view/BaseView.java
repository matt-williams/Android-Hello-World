package com.opentok.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.opentok.Session;
import com.opentok.Stream;

public class BaseView extends SurfaceView implements SurfaceHolder.Callback, Session.Listener {
    private static final String TAG = BaseView.class.getCanonicalName();
    ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private String mSessionId;
    private String mTokenKey;
    private String mApiKey;
    Session mSession;
    boolean mConnected;

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void connect(String sessionId, String tokenKey, String apiKey) {
        disconnectSession();
        mSessionId = sessionId;
        mTokenKey = tokenKey;
        mApiKey = apiKey;
        connectSession();
    }

    public void disconnect() {
        mSessionId = null;
        mTokenKey = null;
        mApiKey = null;
        disconnectSession();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        connectSession();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Nothing to do.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        disconnectSession();
    }

    @Override
    public void onSessionConnected() {
        // Default implementation - do nothing.
    }

    @Override
    public void onSessionDidReceiveStream(Stream stream) {
        // Default implementation - do nothing.
    }

    @Override
    public void onSessionDidDropStream(Stream stream) {
        // Default implementation - do nothing.
    }

    @Override
    public void onSessionDisconnected() {
        // Default implementation - do nothing.
    }

    @Override
    public void onSessionError(Exception e) {
        Log.e(TAG, "Session.Listener.onSessionError", e);
    }

    private void connectSession() {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                if ((mSession == null) &&
                    (mSessionId != null) && (mTokenKey != null) && (mApiKey != null) &&
                    (getHolder().getSurface() != null)) {
                    // Go ahead and prepare session instance and connect.
                    mSession = Session.newInstance(getContext().getApplicationContext(),
                            mSessionId,
                            mTokenKey,
                            mApiKey,
                            BaseView.this);
                    mSession.connect();
                }
            }
        });
    }

    private void disconnectSession() {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                if (mSession != null) {
                    mSession.disconnect();
                    mSession = null;
                }
            }
        });
    }
}