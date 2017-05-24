package com.doodlemobile.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.doodlemobile.mobilesafe.R;

public class RocketActivity extends Activity {

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rocket);

		ImageView iv_launcher = (ImageView) findViewById(R.id.iv_launcher);
		ImageView iv_cloud = (ImageView) findViewById(R.id.iv_cloud);

		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(1000);

		iv_launcher.startAnimation(alphaAnimation);
		iv_cloud.startAnimation(alphaAnimation);

		mHandler.sendEmptyMessageDelayed(0, 1500);
	}

}
