package com.opentok.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;

import com.opentok.view.PublisherView;
import com.opentok.view.SubscriberView;

/**
 * This application demonstrates the basic workflow for getting started with the OpenTok Android SDK.
 * Currently the user is expected to provide rendering surfaces for the SDK, so we'll create
 * SurfaceHolder instances for each component.
 *
 */
public class MainActivity extends Activity {
    private static final String SESSION_ID = "2_MX4xMzExMjU3MX43Mi41LjE2Ny4xNTh-VGh1IE9jdCAxOCAxNToxMzoyOCBQRFQgMjAxMn4wLjMzMjY4NDF-";
    private static final String TOKEN_KEY = "T1==cGFydG5lcl9pZD0xMzExMjU3MSZzaWc9ZmFjMmIwZGI1ZjQ4NzdjY2VlOTEzYzkzN2UxNDQ2MTAwODY3Mjk0Njpyb2xlPW1vZGVyYXRvciZzZXNzaW9uX2lkPTJfTVg0eE16RXhNalUzTVg0M01pNDFMakUyTnk0eE5UaC1WR2gxSUU5amRDQXhPQ0F4TlRveE16b3lPQ0JRUkZRZ01qQXhNbjR3TGpNek1qWTROREYtJmNyZWF0ZV90aW1lPTEzNTc3MjIwNTEmbm9uY2U9MC40MzYxNzgxODY5MjMwNTI1NQ==";
    private static final String API_KEY = "13112571";
	private PublisherView publisherView;
	private SubscriberView subscriberView;
	private WakeLock wakeLock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		publisherView = (PublisherView)findViewById(R.id.publisherview);
		publisherView.connect(SESSION_ID, TOKEN_KEY, API_KEY);
		subscriberView = (SubscriberView)findViewById(R.id.subscriberview);
        subscriberView.connect(SESSION_ID, TOKEN_KEY, API_KEY);

		// Disable screen dimming
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
						"Full Wake Lock");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onStop() {
		super.onStop();

		if (wakeLock.isHeld()) {
			wakeLock.release();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!wakeLock.isHeld()) {
			wakeLock.acquire();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (wakeLock.isHeld()) {
			wakeLock.release();
		}
	}
}
