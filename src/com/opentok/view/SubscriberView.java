package com.opentok.view;

import android.content.Context;
import android.util.AttributeSet;

import com.opentok.Stream;
import com.opentok.Subscriber;

public class SubscriberView extends BaseView {
    private Subscriber mSubscriber;

    public SubscriberView(Context context) {
        super(context);
    }

    public SubscriberView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubscriberView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onSessionDidReceiveStream(final Stream stream) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                if (mSubscriber != null) {
                    mSubscriber.disconnect();
                }
                mSubscriber = mSession.createSubscriber(SubscriberView.this, stream);
                mSubscriber.connect();
            }
        });
    }

    @Override
    public void onSessionDidDropStream(Stream stream) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                if (mSubscriber != null) {
                    mSubscriber.disconnect();
                    mSubscriber = null;
                }
            }
        });
    }
}