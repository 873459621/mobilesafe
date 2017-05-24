package com.doodlemobile.mobilesafe.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.engine.CallerLocDao;
import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;

/**
 * 提供电话归属地显示的Service
 * 
 * @author hhw
 */
public class PhoneService extends Service {

	private WindowManager mWM;
	private TelephonyManager mTM;
	private MyPhoneStateListener mListener;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

	private TextView tv_toast;
	private View v_toast;

	private int[] mDrawableIDs;
	private int mScreenHeight;
	private int mScreenWidth;
	private String mCallerLoc;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			tv_toast.setText(mCallerLoc);
		};
	};
	private InnerOutgoingCallReceiver mReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();

		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);

		mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener = new MyPhoneStateListener();
		mTM.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);

		mScreenHeight = mWM.getDefaultDisplay().getHeight();
		mScreenWidth = mWM.getDefaultDisplay().getWidth();

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		mReceiver = new InnerOutgoingCallReceiver();
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mTM != null && mListener != null) {
			mTM.listen(mListener, PhoneStateListener.LISTEN_NONE);
		}

		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

	private class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if (mWM != null && v_toast != null) {
					mWM.removeView(v_toast);
				}
				break;

			case TelephonyManager.CALL_STATE_RINGING:
				showToast(incomingNumber);
				break;
			}
		}
	}

	/**
	 * 展示电话归属地显示的Toast
	 */
	public void showToast(String incomingNumber) {
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.format = PixelFormat.TRANSLUCENT;
		mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mParams.setTitle("CallerLoc");
		mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mParams.gravity = Gravity.LEFT + Gravity.TOP;
		mParams.x = SPUtils.getInt(getApplicationContext(),
				ConstantValue.TOAST_LOCATION_X, 0);
		mParams.y = SPUtils.getInt(getApplicationContext(),
				ConstantValue.TOAST_LOCATION_Y, 0);

		v_toast = View.inflate(this, R.layout.toast_view, null);
		tv_toast = (TextView) v_toast.findViewById(R.id.tv_toast);

		mDrawableIDs = new int[] { R.drawable.toast_bg_black,
				R.drawable.toast_bg_gray, R.drawable.toast_bg_orange,
				R.drawable.toast_bg_blue, R.drawable.toast_bg_green };
		int toast_style = SPUtils.getInt(this, ConstantValue.TOAST_STYLE, 0);
		tv_toast.setBackgroundResource(mDrawableIDs[toast_style]);

		query(incomingNumber);

		v_toast.setOnTouchListener(new OnTouchListener() {

			private int startX;
			private int startY;
			private int endX;
			private int endY;

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					endX = (int) event.getRawX();
					endY = (int) event.getRawY();

					int dstX = endX - startX;
					int dstY = endY - startY;

					mParams.x += dstX;
					mParams.y += dstY;

					if (mParams.x < 0) {
						mParams.x = 0;
					}
					if (mParams.y < 0) {
						mParams.y = 0;
					}
					if (mParams.x > mScreenWidth - v_toast.getWidth()) {
						mParams.x = mScreenWidth - v_toast.getWidth();
					}
					if (mParams.y > mScreenHeight - 22 - v_toast.getHeight()) {
						mParams.y = mScreenHeight - 22 - v_toast.getHeight();
					}

					mWM.updateViewLayout(v_toast, mParams);

					startX = endX;
					startY = endY;
					break;

				case MotionEvent.ACTION_UP:
					SPUtils.putInt(getApplicationContext(),
							ConstantValue.TOAST_LOCATION_X, v_toast.getLeft());
					SPUtils.putInt(getApplicationContext(),
							ConstantValue.TOAST_LOCATION_Y, v_toast.getTop());
					break;
				}
				return true;
			}
		});

		mWM.addView(v_toast, mParams);
	}

	/**
	 * 查询电话号码归属地
	 * 
	 * @param incomingNumber
	 *            要查询的电话号码
	 */
	private void query(final String incomingNumber) {
		new Thread() {

			public void run() {
				mCallerLoc = CallerLocDao.getCallerLoc(incomingNumber);
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}

	private class InnerOutgoingCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			showToast(getResultData());
		}

	}

}
