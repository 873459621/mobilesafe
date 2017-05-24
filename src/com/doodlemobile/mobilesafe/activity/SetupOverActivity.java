package com.doodlemobile.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;

/**
 * 显示设置中心中界面的Activity
 * 
 * @author hhw
 */
public class SetupOverActivity extends Activity {

	private TextView tv_phone;
	private TextView tv_reset;
	private ImageView iv_lock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean setup_over = SPUtils.getBoolean(this, ConstantValue.SETUP_OVER,
				false);
		if (setup_over) {
			setContentView(R.layout.activity_setup_over);
			initUI();
		} else {
			Intent intent = new Intent(getApplicationContext(),
					Setup1Activity.class);
			startActivity(intent);

			finish();
		}
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_reset = (TextView) findViewById(R.id.tv_reset);
		iv_lock = (ImageView) findViewById(R.id.iv_lock);

		String phone = SPUtils.getString(this, ConstantValue.CONTACT_PHONE,
				null);
		tv_phone.setText(phone);

		boolean open_security = SPUtils.getBoolean(this,
				ConstantValue.OPEN_SECURITY, false);
		if (open_security) {
			iv_lock.setBackgroundResource(R.drawable.open);
		} else {
			iv_lock.setBackgroundResource(R.drawable.close);
		}
		iv_lock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean open_security = !SPUtils.getBoolean(
						getApplicationContext(), ConstantValue.OPEN_SECURITY,
						false);
				if (open_security) {
					iv_lock.setBackgroundResource(R.drawable.open);
				} else {
					iv_lock.setBackgroundResource(R.drawable.close);
				}
				SPUtils.putBoolean(getApplicationContext(),
						ConstantValue.OPEN_SECURITY, open_security);
			}
		});

		tv_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						Setup1Activity.class);
				startActivity(intent);

				finish();
			}
		});
	}

}
