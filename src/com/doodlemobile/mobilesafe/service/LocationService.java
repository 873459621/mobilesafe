package com.doodlemobile.mobilesafe.service;

import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.TextUtils;

/**
 * 提供定位服务的Service
 * 
 * @author hhw
 */
public class LocationService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String bestProvider = lm.getBestProvider(criteria, true);

		MyLocationListener listener = new MyLocationListener();
		lm.requestLocationUpdates(bestProvider, 5000, 10, listener);
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			String GPSLocation = "(" + longitude + ", " + latitude + ")";

			String contact_phone = SPUtils.getString(getApplicationContext(),
					ConstantValue.CONTACT_PHONE, null);
			if (!TextUtils.isEmpty(contact_phone)) {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(contact_phone, null, "Location: "
						+ GPSLocation, null, null);
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

	}

}
