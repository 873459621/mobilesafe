package com.doodlemobile.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;
import com.doodlemobile.mobilesafe.utils.ToastUtils;
import com.doodlemobile.mobilesafe.view.SettingsItemView;

/**
 * 显示设置中心中导航界面2的Activity
 * 
 * @author hhw
 */
public class Setup2Activity extends BaseSetupActivity {

	private SettingsItemView siv_sim_bound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);

		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		siv_sim_bound = (SettingsItemView) findViewById(R.id.siv_sim_bound);

		String sim_number = SPUtils.getString(this, ConstantValue.SIM_NUMBER,
				null);
		if (TextUtils.isEmpty(sim_number)) {
			siv_sim_bound.setCheckedState(false);
		} else {
			siv_sim_bound.setCheckedState(true);
		}

		siv_sim_bound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean checked = !siv_sim_bound.isChecked();
				if (checked) {
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();
					SPUtils.putString(getApplicationContext(),
							ConstantValue.SIM_NUMBER, simSerialNumber);
				} else {
					SPUtils.remove(getApplicationContext(),
							ConstantValue.SIM_NUMBER);
				}
				siv_sim_bound.setCheckedState(checked);
			}
		});
	}

	@Override
	protected void showPrePage() {
		Intent intent = new Intent(getApplicationContext(),
				Setup1Activity.class);
		startActivity(intent);

		finish();

		overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
	}

	@Override
	protected void showNextPage() {
		String simSerialNumber = SPUtils.getString(this,
				ConstantValue.SIM_NUMBER, null);
		if (TextUtils.isEmpty(simSerialNumber)) {
			ToastUtils.show(this, "必须绑定SIM卡！");
		} else {
			Intent intent = new Intent(getApplicationContext(),
					Setup3Activity.class);
			startActivity(intent);

			finish();

			overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
		}
	}

}
