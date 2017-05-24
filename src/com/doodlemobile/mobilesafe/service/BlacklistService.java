package com.doodlemobile.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.doodlemobile.mobilesafe.db.dao.BlacklistDao;

/**
 * 提供黑名单拦截的Service
 * 
 * @author hhw
 */
public class BlacklistService extends Service {

	private InnerSmsReceiver mReceiver;
	private BlacklistDao mDao;
	private Uri mUri;
	private ContentResolver mResolver;
	private MyObserver mObserver;
	private TelephonyManager mTM;
	private MyPhoneStateListener mListener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mDao = BlacklistDao.getInstance(this);

		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(1000);
		mReceiver = new InnerSmsReceiver();
		registerReceiver(mReceiver, filter);

		mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener = new MyPhoneStateListener();
		mTM.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}

		if (mObserver != null) {
			mResolver.unregisterContentObserver(mObserver);
		}

		if (mTM != null && mListener != null) {
			mTM.listen(mListener, PhoneStateListener.LISTEN_NONE);
		}
	}

	private class InnerSmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);

				String address = sms.getOriginatingAddress();
				int mode = mDao.getMode(address);
				if (mode == 1 || mode == 3) {
					abortBroadcast();
				}
			}
		}

	}

	private class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				int mode = mDao.getMode(incomingNumber);
				if (mode == 2 || mode == 3) {
					endCall();
					deleteCalls(incomingNumber);
				}
				break;
			}
		}

	}

	/**
	 * 通过反射来调用被Android系统隐藏的endCall方法
	 */
	private void endCall() {
		try {
			Class<?> clazz = Class.forName("android.os.ServiceManager");
			Method getService = clazz.getMethod("getService", String.class);
			IBinder iBinder = (IBinder) getService.invoke(null,
					Context.TELEPHONY_SERVICE);
			ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);

			iTelephony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除来电的通话记录
	 * 
	 * @param phone
	 *            来电号码
	 */
	public void deleteCalls(String phone) {
		mUri = Uri.parse("content://call_log/calls");
		mResolver = getContentResolver();
		mObserver = new MyObserver(new Handler(), phone);
		mResolver.registerContentObserver(mUri, true, mObserver);
	}

	private class MyObserver extends ContentObserver {

		private String phone;

		public MyObserver(Handler handler, String phone) {
			super(handler);

			this.phone = phone;
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);

			mResolver.delete(mUri, "number=?", new String[] { phone });
		}

	}

}
