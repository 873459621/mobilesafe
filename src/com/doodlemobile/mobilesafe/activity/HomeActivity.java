package com.doodlemobile.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.MD5Utils;
import com.doodlemobile.mobilesafe.utils.SPUtils;
import com.doodlemobile.mobilesafe.utils.ToastUtils;

/**
 * 显示主界面的Activity
 * 
 * @author hhw
 */
public class HomeActivity extends Activity {

	private GridView gv_home;

	private String[] mTitleStrs;
	private int[] mDrawableIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initUI();
		initData();
	}

	/**
	 * 初始化九宫格的数据
	 */
	private void initData() {
		mTitleStrs = new String[] { "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计",
				"手机杀毒", "缓存清理", "高级工具", "设置中心" };
		mDrawableIds = new int[] { R.drawable.home_safe, R.drawable.home_msg,
				R.drawable.home_app, R.drawable.home_progress,
				R.drawable.home_band, R.drawable.home_virus,
				R.drawable.home_save, R.drawable.home_high,
				R.drawable.home_settings };

		gv_home.setAdapter(new MyAdapter());
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					showDialog();
					break;

				case 1:
					nextActivity(BlacklistActivity.class);
					break;

				case 2:

					break;

				case 3:

					break;

				case 4:

					break;

				case 5:

					break;

				case 6:

					break;

				case 7:
					nextActivity(AToolsActivity.class);
					break;

				case 8:
					nextActivity(SettingsActivity.class);
					break;
				}
			}
		});
	}

	/**
	 * 跳转到下一个Activity
	 * 
	 * @param cls
	 *            目标Activity的字节码文件
	 */
	protected void nextActivity(Class<?> cls) {
		Intent intent = new Intent(getApplicationContext(), cls);
		startActivity(intent);
	}

	/**
	 * 显示密码对话框
	 */
	protected void showDialog() {
		String pwd = SPUtils.getString(this, ConstantValue.MOBILE_SAVE_PWD,
				null);
		if (TextUtils.isEmpty(pwd)) {
			showSetPwdDialog();
		} else {
			showConfirmPwdDialog();
		}
	}

	/**
	 * 显示确认密码对话框
	 */
	private void showConfirmPwdDialog() {
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		final View view = View.inflate(this, R.layout.dialog_confirm_pwd, null);
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

		Button bt_confirm = (Button) view.findViewById(R.id.bt_confirm);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

		bt_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText et_confirm_pwd = (EditText) view
						.findViewById(R.id.et_confirm_pwd);

				String confirmPwd = et_confirm_pwd.getText().toString();

				if (!TextUtils.isEmpty(confirmPwd)) {
					confirmPwd = MD5Utils.encode(confirmPwd);
					String pwd = SPUtils.getString(getApplicationContext(),
							ConstantValue.MOBILE_SAVE_PWD, null);

					if (confirmPwd.equals(pwd)) {
						nextActivity(SetupOverActivity.class);
						dialog.dismiss();
					} else {
						ToastUtils.show(getApplicationContext(), "密码不一致！");
					}
				} else {
					ToastUtils.show(getApplicationContext(), "请输入密码！");
				}
			}
		});

		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 显示设置密码对话框
	 */
	private void showSetPwdDialog() {
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		final View view = View.inflate(this, R.layout.dialog_set_pwd, null);
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

		Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

		bt_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText et_set_pwd = (EditText) view
						.findViewById(R.id.et_set_pwd);
				EditText et_confirm_pwd = (EditText) view
						.findViewById(R.id.et_confirm_pwd);

				String pwd = et_set_pwd.getText().toString();
				String confirmPwd = et_confirm_pwd.getText().toString();

				if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirmPwd)) {
					if (pwd.equals(confirmPwd)) {
						nextActivity(SetupOverActivity.class);
						dialog.dismiss();

						pwd = MD5Utils.encode(pwd);
						SPUtils.putString(getApplicationContext(),
								ConstantValue.MOBILE_SAVE_PWD, pwd);
					} else {
						ToastUtils.show(getApplicationContext(), "密码不一致！");
					}
				} else {
					ToastUtils.show(getApplicationContext(), "请输入密码！");
				}
			}
		});

		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		gv_home = (GridView) findViewById(R.id.gv_home);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mTitleStrs.length;
		}

		@Override
		public Object getItem(int position) {
			return mTitleStrs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.gridview_item, null);
			}

			TextView tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			ImageView iv_icon = (ImageView) convertView
					.findViewById(R.id.iv_icon);

			tv_title.setText(mTitleStrs[position]);
			iv_icon.setBackgroundResource(mDrawableIds[position]);
			
			return convertView;
		}

	}

}
