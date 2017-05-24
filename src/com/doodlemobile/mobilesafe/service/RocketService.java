package com.doodlemobile.mobilesafe.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.activity.RocketActivity;

public class RocketService extends Service {

	private WindowManager mWM;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

	private int mScreenHeight;
	private int mScreenWidth;
	private View v_rocket;
	private ImageView iv_rocket;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			mParams.y = (Integer) msg.obj;
			mWM.updateViewLayout(v_rocket, mParams);
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();

		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		mScreenHeight = mWM.getDefaultDisplay().getHeight();
		mScreenWidth = mWM.getDefaultDisplay().getWidth();

		showRocket();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mWM != null && v_rocket != null) {
			mWM.removeView(v_rocket);
		}
	}

	private void showRocket() {
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.format = PixelFormat.TRANSLUCENT;
		mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mParams.setTitle("Rocket");
		mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mParams.gravity = Gravity.LEFT + Gravity.TOP;

		v_rocket = View.inflate(this, R.layout.rocket_view, null);
		iv_rocket = (ImageView) v_rocket.findViewById(R.id.iv_rocket);

		AnimationDrawable background = (AnimationDrawable) iv_rocket
				.getBackground();
		background.start();

		v_rocket.setOnTouchListener(new OnTouchListener() {

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
					if (mParams.x > mScreenWidth - v_rocket.getWidth()) {
						mParams.x = mScreenWidth - v_rocket.getWidth();
					}
					if (mParams.y > mScreenHeight - 22 - v_rocket.getHeight()) {
						mParams.y = mScreenHeight - 22 - v_rocket.getHeight();
					}

					mWM.updateViewLayout(v_rocket, mParams);

					startX = endX;
					startY = endY;
					break;

				case MotionEvent.ACTION_UP:
					if (mParams.x > 100 && mParams.x < 200 && mParams.y > 350) {
						launchRocket();

						Intent intent = new Intent(getApplicationContext(),
								RocketActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					break;
				}
				return true;
			}
		});

		mWM.addView(v_rocket, mParams);
	}

	protected void launchRocket() {
		new Thread() {

			public void run() {
				for (int i = 0; i < 21; i++) {
					Message msg = Message.obtain();
					msg.obj = 350 - i * 17;
					mHandler.sendMessage(msg);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

}
