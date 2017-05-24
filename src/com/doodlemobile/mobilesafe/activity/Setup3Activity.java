package com.doodlemobile.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;
import com.doodlemobile.mobilesafe.utils.ToastUtils;

/**
 * 显示设置中心中导航界面3的Activity
 * 
 * @author hhw
 */
public class Setup3Activity extends BaseSetupActivity {

	private EditText et_phone_number;
	private Button btn_select_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);

		initUI();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			String phone = data.getStringExtra("phone");
			phone = phone.replace("-", "").replace(" ", "");

			et_phone_number.setText(phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		String phone = SPUtils.getString(this, ConstantValue.CONTACT_PHONE,
				null);

		et_phone_number = (EditText) findViewById(R.id.et_phone_number);
		btn_select_number = (Button) findViewById(R.id.btn_select_number);

		if (!TextUtils.isEmpty(phone)) {
			et_phone_number.setText(phone);
		}
		btn_select_number.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ContactListActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void showPrePage() {
		Intent intent = new Intent(getApplicationContext(),
				Setup2Activity.class);
		startActivity(intent);

		finish();

		overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
	}

	@Override
	protected void showNextPage() {
		String phone = et_phone_number.getText().toString().trim();
		if (!TextUtils.isEmpty(phone)) {
			SPUtils.putString(this, ConstantValue.CONTACT_PHONE, phone);

			Intent intent = new Intent(getApplicationContext(),
					Setup4Activity.class);
			startActivity(intent);

			finish();

			overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
		} else {
			ToastUtils.show(this, "必须设置安全号码！");
		}
	}

}
