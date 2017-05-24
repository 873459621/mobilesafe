package com.doodlemobile.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * 设置中心导航界面的父类，具有手势监听的功能
 * 
 * @author hhw
 */
public abstract class BaseSetupActivity extends Activity {

	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGestureDetector = new GestureDetector(this,
				new SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						if (e1.getX() > e2.getX()) {
							showNextPage();
						} else {
							showPrePage();
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/**
	 * 显示上一个界面
	 */
	protected abstract void showPrePage();

	/**
	 * 显示下一个界面
	 */
	protected abstract void showNextPage();

	public void prePage(View v) {
		showPrePage();
	}

	public void nextPage(View v) {
		showNextPage();
	}

}
