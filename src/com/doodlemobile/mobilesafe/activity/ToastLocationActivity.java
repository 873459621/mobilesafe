package com.doodlemobile.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;

public class ToastLocationActivity extends Activity {

	private TextView tv_drag;
	private Button btn_top;
	private Button btn_bottom;

	private int mScreenHeight;
	private int mScreenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toast_location);

		initUI();
	}

	/**
	 * 初始化UI
	 */
	@SuppressWarnings("deprecation")
	private void initUI() {
		tv_drag = (TextView) findViewById(R.id.tv_drag);
		btn_top = (Button) findViewById(R.id.btn_top);
		btn_bottom = (Button) findViewById(R.id.btn_bottom);

		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		mScreenHeight = wm.getDefaultDisplay().getHeight();
		mScreenWidth = wm.getDefaultDisplay().getWidth();

		int toast_location_x = SPUtils.getInt(getApplicationContext(),
				ConstantValue.TOAST_LOCATION_X, 0);
		int toast_location_y = SPUtils.getInt(getApplicationContext(),
				ConstantValue.TOAST_LOCATION_Y, 0);

		if (toast_location_y > mScreenHeight / 2) {
			btn_top.setVisibility(View.VISIBLE);
			btn_bottom.setVisibility(View.INVISIBLE);
		} else {
			btn_top.setVisibility(View.INVISIBLE);
			btn_bottom.setVisibility(View.VISIBLE);
		}

		LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.leftMargin = toast_location_x;
		params.topMargin = toast_location_y;

		tv_drag.setLayoutParams(params);

		tv_drag.setOnClickListener(new OnClickListener() {

			private long mHits[] = new long[2];

			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();

				if (mHits[mHits.length - 1] - mHits[0] < 500) {
					int left = mScreenWidth / 2 - tv_drag.getWidth() / 2;
					int right = mScreenWidth / 2 + tv_drag.getWidth() / 2;
					int top = mScreenHeight / 2 - tv_drag.getHeight() / 2;
					int bottom = mScreenHeight / 2 + tv_drag.getHeight() / 2;

					tv_drag.layout(left, top, right, bottom);

					SPUtils.putInt(getApplicationContext(),
							ConstantValue.TOAST_LOCATION_X, tv_drag.getLeft());
					SPUtils.putInt(getApplicationContext(),
							ConstantValue.TOAST_LOCATION_Y, tv_drag.getTop());
				}
			}
		});

		tv_drag.setOnTouchListener(new OnTouchListener() {

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

					int left = tv_drag.getLeft() + dstX;
					int right = tv_drag.getRight() + dstX;
					int top = tv_drag.getTop() + dstY;
					int bottom = tv_drag.getBottom() + dstY;

					if (left < 0) {
						left = 0;
						right = tv_drag.getWidth();
					}
					if (top < 0) {
						top = 0;
						bottom = tv_drag.getHeight();
					}
					if (right > mScreenWidth) {
						left = mScreenWidth - tv_drag.getWidth();
						right = mScreenWidth;
					}
					if (bottom > mScreenHeight - 22) {
						top = mScreenHeight - 22 - tv_drag.getHeight();
						bottom = mScreenHeight - 22;
					}

					if (top > mScreenHeight / 2) {
						btn_top.setVisibility(View.VISIBLE);
						btn_bottom.setVisibility(View.INVISIBLE);
					} else {
						btn_top.setVisibility(View.INVISIBLE);
						btn_bottom.setVisibility(View.VISIBLE);
					}

					tv_drag.layout(left, top, right, bottom);

					startX = endX;
					startY = endY;
					break;

				case MotionEvent.ACTION_UP:
					SPUtils.putInt(getApplicationContext(),
							ConstantValue.TOAST_LOCATION_X, tv_drag.getLeft());
					SPUtils.putInt(getApplicationContext(),
							ConstantValue.TOAST_LOCATION_Y, tv_drag.getTop());
					break;
				}
				return false;
			}
		});
	}
}
