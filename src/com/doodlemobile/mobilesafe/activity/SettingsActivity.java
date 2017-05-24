package com.doodlemobile.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.service.BlacklistService;
import com.doodlemobile.mobilesafe.service.PhoneService;
import com.doodlemobile.mobilesafe.service.RocketService;
import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;
import com.doodlemobile.mobilesafe.utils.ServiceUtils;
import com.doodlemobile.mobilesafe.view.SettingsClickView;
import com.doodlemobile.mobilesafe.view.SettingsItemView;

/**
 * 显示设置中心的Activity
 * 
 * @author hhw
 */
public class SettingsActivity extends Activity {

	private String[] mToastStyleDes;
	private int mToastStyle;
	private SettingsClickView scv_toast_style;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		initUpdate();
		initPhone();
		initToastStyle();
		initToastLocation();
		initBlacklist();
		initRocket();
	}

	/**
	 * 初始化小火箭条目
	 */
	private void initRocket() {
		bindService(R.id.siv_rocket, RocketService.class);
	}

	/**
	 * 初始化黑名单条目
	 */
	private void initBlacklist() {
		bindService(R.id.siv_blacklist, BlacklistService.class);
	}

	/**
	 * 初始化Toast位置条目
	 */
	private void initToastLocation() {
		SettingsClickView scv_toast_location = (SettingsClickView) findViewById(R.id.scv_toast_location);
		scv_toast_location.setTitle("电话归属地提示框位置");
		scv_toast_location.setDes("设置电话归属地提示框的位置");

		scv_toast_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ToastLocationActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化Toast样式条目
	 */
	private void initToastStyle() {
		scv_toast_style = (SettingsClickView) findViewById(R.id.scv_toast_style);

		mToastStyleDes = new String[] { "黑色", "灰色", "橙色", "蓝色", "绿色" };
		mToastStyle = SPUtils.getInt(this, ConstantValue.TOAST_STYLE, 0);
		scv_toast_style.setDes(mToastStyleDes[mToastStyle]);
		scv_toast_style.setTitle("设置电话归属地显示风格");

		scv_toast_style.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showToastStyleDialog();
			}
		});
	}

	/**
	 * 显示选择Toast样式的对话框
	 */
	protected void showToastStyleDialog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("请选择样式");

		builder.setSingleChoiceItems(mToastStyleDes, mToastStyle,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SPUtils.putInt(getApplicationContext(),
								ConstantValue.TOAST_STYLE, which);
						scv_toast_style.setDes(mToastStyleDes[which]);
						mToastStyle = which;
						dialog.dismiss();
					}
				});

		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.show();
	}

	/**
	 * 初始化电话归属地条目
	 */
	private void initPhone() {
		bindService(R.id.siv_phone, PhoneService.class);
	}

	/**
	 * 把是否开启服务与SettingsItemView是否被选中绑定起来
	 * 
	 * @param id
	 *            SettingsItemView的id
	 * @param cls
	 *            要绑定的服务的字节码文件
	 */
	private void bindService(int id, final Class<?> cls) {
		final SettingsItemView siv = (SettingsItemView) findViewById(id);

		boolean isRunning = ServiceUtils.isRunning(this, cls.getName());
		siv.setCheckedState(isRunning);

		siv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isChecked = !siv.isChecked();
				siv.setCheckedState(isChecked);

				Intent service = new Intent(getApplicationContext(), cls);
				if (isChecked) {
					startService(service);
				} else {
					stopService(service);
				}
			}
		});
	}

	/**
	 * 初始化自动更新条目
	 */
	private void initUpdate() {
		final SettingsItemView siv_update = (SettingsItemView) findViewById(R.id.siv_update);

		boolean open_update = SPUtils.getBoolean(this,
				ConstantValue.OPEN_UPDATE, false);
		siv_update.setCheckedState(open_update);

		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isChecked = !siv_update.isChecked();
				siv_update.setCheckedState(isChecked);
				SPUtils.putBoolean(getApplicationContext(),
						ConstantValue.OPEN_UPDATE, isChecked);
			}
		});
	}

}
