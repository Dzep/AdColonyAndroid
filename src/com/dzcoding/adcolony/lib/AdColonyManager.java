package com.dzcoding.adcolony.lib;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyV4VCListener;
import com.jirbo.adcolony.AdColonyVideoAd;
import com.jirbo.adcolony.AdColonyVideoListener;

/**
 * An AdColony wrapper for Android.
 * @version 1.0.0
 * @author dzep
 */
public class AdColonyManager implements AdColonyVideoListener, AdColonyV4VCListener {

	private String mAppId = "";
	private String mZoneId = "";
	private Activity mActivity = null;
	private AdColonyVideoAd mVideoAd = null;	
	private AdColonyManagerListener mListener = null;
	
	/**
	 * Creates an AdColonyManager.
	 * @param activity The activity where to attach the AdColony activities to.
	 * @param appId The application ID.
	 * @param zoneId The application zone ID.
	 */
	public AdColonyManager(Activity activity, String appId, String zoneId) {
		mActivity = activity;
		mAppId = appId;
		mZoneId = zoneId;
		
		configure();
	}
	
	/**
	 * Sets the AdColonyManagerListener.
	 * @param listener The object which implements the AdColonyManagerListener methods.
	 */
	public void setListener (AdColonyManagerListener listener) {
		mListener = listener;
	}
	
	protected void configure() {
		// version - arbitrary application version
		// store - google or amazon
		String appInfo = String.format("version:%s,store:google", getAppVersion(mActivity));
		AdColony.configure(mActivity, appInfo, mAppId, mZoneId);
		AdColony.addV4VCListener(this);

		// create a video ad
		createNewVideoAd();
	}
	
	private void createNewVideoAd() {
		mVideoAd = new AdColonyVideoAd();
	}
	
	/**
	 * Checks if the video ad is ready to be played.
	 * @return TRUE if the video is ready and available.
	 */
	public boolean isVideoAdReady() {
		boolean isConfigured = (mVideoAd != null);
		boolean isReady = false;
		boolean isAvailable = false;
		if (isConfigured) {
			isReady = mVideoAd.isReady();
			isAvailable = mVideoAd.getV4VCAvailable();
		}
		return (isReady && isAvailable);
	}
	
	/**
	 * Plays the video ad.
	 */
	public void play() {
		// check if the video ad is ready.
		if (isVideoAdReady()) {
			mVideoAd.showV4VC(this, false);
		}
	}
	
	/**
	 * Call this onPause method of the activity.
	 */
	public void pause() {
		AdColony.pause();
	}
	
	/**
	 * Call this onResume method of the activity.
	 */
	public void resume() {
		AdColony.resume(mActivity);
	}
	
	/// AdColony video listener
	@Override
	public void onAdColonyVideoFinished() {
		if (mListener != null && mListener instanceof AdColonyManagerListener) {
			mListener.onAdColonyManagerVideoFinished();
		}
	}

	@Override
	public void onAdColonyVideoStarted() {
		if (mListener != null && mListener instanceof AdColonyManagerListener) {
			mListener.onAdColonyManagerVideoStarted();
		}
	}
	
	/// AdColony v4vc listener
	@Override
	public void onV4VCResult(boolean success, String currencyName, int currencyAmount) {
		if (mListener != null && mListener instanceof AdColonyManagerListener) {
			mListener.onAdColonyManagerVideoResult(success, currencyName, currencyAmount);
		}
	}
	
	/// Helpers
	private static String getAppVersion(Context context) {
		String appVersion = "";
		try {
			appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			appVersion = "1.0";
		}
		return appVersion;
	}

	/// AdColonyManager listener
	public interface AdColonyManagerListener {
		public void onAdColonyManagerVideoResult(boolean success, String currencyName, int currencyAmount);
		public void onAdColonyManagerVideoStarted();
		public void onAdColonyManagerVideoFinished();
	}
	
}
