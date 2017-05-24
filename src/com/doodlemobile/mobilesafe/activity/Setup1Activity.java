package com.doodlemobile.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.doodlemobile.mobilesafe.R;

/**
 * 显示设置中心中导航界面1的Activity
 * 
 * @author hhw
 */
public class Setup1Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	protected void showPrePage() {
	}

	@Override
	protected void showNextPage() {
		Intent intent = new Intent(getApplicationContext(),
				Setup2Activity.class);
		startActivity(intent);

		finish();

		overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
	}

}
