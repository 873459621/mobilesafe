package com.doodlemobile.mobilesafe.activity;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.engine.SmsBackup;
import com.doodlemobile.mobilesafe.engine.SmsBackup.SmsBackupProgress;

/**
 * 高级工具界面的Activity
 * 
 * @author hhw
 */
public class AToolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);

		initPhoneAddress();
		initSmsBackup();
	}

	/**
	 * 加载备份短信的功能
	 */
	private void initSmsBackup() {
		TextView tv_sms_backup = (TextView) findViewById(R.id.tv_sms_backup);
		// final ProgressBar pb_bar = (ProgressBar) findViewById(R.id.pb_bar);

		tv_sms_backup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSmsBackupDialog();
				// SmsBackup(pb_bar);
			}
		});
	}

	/**
	 * 备份短信
	 * 
	 * @param pb_bar
	 *            显示效果的进度条
	 */
	protected void SmsBackup(final ProgressBar pb_bar) {
		new Thread() {

			public void run() {
				pb_bar.setVisibility(View.VISIBLE);

				String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + File.separator + "sms.xml";
				SmsBackup.backup(getApplicationContext(), path,
						new SmsBackupProgress() {

							@Override
							public void setProgress(int value) {
								pb_bar.setProgress(value);
							}

							@Override
							public void setMax(int max) {
								pb_bar.setMax(max);
							}
						});

				pb_bar.setVisibility(View.INVISIBLE);
			};
		}.start();
	}

	/**
	 * 显示备份短信的对话框
	 */
	protected void showSmsBackupDialog() {
		final ProgressDialog dialog = new ProgressDialog(this);

		dialog.setIcon(R.drawable.ic_launcher);
		dialog.setTitle("备份短信");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		dialog.show();

		new Thread() {

			public void run() {
				String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + File.separator + "sms.xml";
				SmsBackup.backup(getApplicationContext(), path,
						new SmsBackupProgress() {

							@Override
							public void setProgress(int value) {
								dialog.setProgress(value);
							}

							@Override
							public void setMax(int max) {
								dialog.setMax(max);
							}
						});

				dialog.dismiss();
			};
		}.start();
	}

	/**
	 * 加载查询电话号码归属地的功能
	 */
	private void initPhoneAddress() {
		TextView tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);

		tv_query_phone_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						QueryAddressActiviyt.class);
				startActivity(intent);
			}
		});
	}

}
