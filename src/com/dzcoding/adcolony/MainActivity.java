package com.dzcoding.adcolony;

import com.dzcoding.adcolony.lib.AdColonyManager;
import com.dzcoding.adcolony.lib.AdColonyManager.AdColonyManagerListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements AdColonyManagerListener {

	private AdColonyManager mAdColonyManager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// create AdColonyManager and set its listener.
		mAdColonyManager = new AdColonyManager(this, "app6c3bcf6ab9ec4ad8a0ca48", "vzcb39a9c8ec8c42b584ae27");
		mAdColonyManager.setListener(this);
		
		// set the buttons listener.
		final Button checkVideoAvailabilityButton = (Button) findViewById(R.id.check_video_ad_availability_button);
		checkVideoAvailabilityButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mAdColonyManager != null) {
					boolean isReady = mAdColonyManager.isVideoAdReady();
					Button playVideoButton = (Button) findViewById(R.id.play_video_button);
					playVideoButton.setEnabled(isReady);
					
					TextView availabilityText = (TextView) findViewById(R.id.availability_text);
					availabilityText.setText(isReady ? R.string.available : R.string.not_available);
				}
			}
		});
		
		
		final Button playVideoButton = (Button) findViewById(R.id.play_video_button);
		playVideoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mAdColonyManager != null) {
					boolean isReady = mAdColonyManager.isVideoAdReady();
					if (isReady) {
						mAdColonyManager.play();
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mAdColonyManager != null) {
			mAdColonyManager.pause();
		}
	} 
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mAdColonyManager != null) {
			mAdColonyManager.resume();
		}
	}

	/// AdColonyManager listener
	@Override
	public void onAdColonyManagerVideoResult(boolean success, String currencyName, int currencyAmount) {
		if (success) {
			Toast.makeText(this, "You earned " + currencyAmount + " " + currencyName, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Failed to earn " + currencyAmount + " " + currencyName + " from the video ad.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onAdColonyManagerVideoStarted() {
		Toast.makeText(this, "The video has started.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAdColonyManagerVideoFinished() {
		Toast.makeText(this, "Video playback finished.", Toast.LENGTH_SHORT).show();
	}
}
