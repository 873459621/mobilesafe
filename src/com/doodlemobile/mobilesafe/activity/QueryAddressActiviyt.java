package com.doodlemobile.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.engine.CallerLocDao;

/**
 * 电话归属地查询界面的Activity
 * 
 * @author hhw
 */
public class QueryAddressActiviyt extends Activity {

	private EditText et_phone;
	private TextView tv_result;
	private String mCcallerLoc;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			tv_result.setText(mCcallerLoc);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_address);

		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		tv_result = (TextView) findViewById(R.id.tv_result);

		et_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String phoneNumber = et_phone.getText().toString().trim();
				query(phoneNumber);
			}
		});
	}

	public void query(View v) {
		String phoneNumber = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(new long[] { 1000, 1000 }, 2);
		} else {
			query(phoneNumber);
		}
	}

	/**
	 * 查询电话号码的归属地
	 * 
	 * @param phoneNumber
	 *            要查询的电话号码
	 */
	private void query(final String phoneNumber) {
		new Thread() {

			public void run() {
				mCcallerLoc = CallerLocDao.getCallerLoc(phoneNumber);
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}

}
