package com.doodlemobile.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;
import com.doodlemobile.mobilesafe.utils.ToastUtils;

/**
 * 显示设置中心中导航界面4的Activity
 * 
 * @author hhw
 */
public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cb_box;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);

		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		cb_box = (CheckBox) findViewById(R.id.cb_box);

		boolean open_security = SPUtils.getBoolean(this,
				ConstantValue.OPEN_SECURITY, false);
		if (open_security) {
			cb_box.setText("手机防盗已开启");
			cb_box.setChecked(open_security);
		} else {
			cb_box.setText("手机防盗已关闭");
		}

		cb_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SPUtils.putBoolean(getApplicationContext(),
						ConstantValue.OPEN_SECURITY, isChecked);
				if (isChecked) {
					cb_box.setText("手机防盗已开启");
				} else {
					cb_box.setText("手机防盗已关闭");
				}
			}
		});
	}

	@Override
	protected void showPrePage() {
		Intent intent = new Intent(getApplicationContext(),
				Setup3Activity.class);
		startActivity(intent);

		finish();

		overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
	}

	@Override
	protected void showNextPage() {
		if (cb_box.isChecked()) {
			SPUtils.putBoolean(this, ConstantValue.SETUP_OVER, true);

			Intent intent = new Intent(getApplicationContext(),
					SetupOverActivity.class);
			startActivity(intent);

			finish();

			overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
		} else {
			ToastUtils.show(this, "必须开启手机防盗！");
		}
	}

}
