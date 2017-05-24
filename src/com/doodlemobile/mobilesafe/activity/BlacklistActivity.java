package com.doodlemobile.mobilesafe.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.db.dao.BlacklistDao;
import com.doodlemobile.mobilesafe.db.domain.BlacklistInfo;
import com.doodlemobile.mobilesafe.utils.ToastUtils;

/**
 * 通信卫士黑名单Activity
 * 
 * @author hhw
 */
public class BlacklistActivity extends Activity {

	private BlacklistDao mDao;
	private List<BlacklistInfo> mBlacklist;
	private MyAdapter mAdapter;

	private int mMode;
	private int mCount;

	private boolean isLoading;

	private ListView lv_blacklist;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (mAdapter == null) {
				mAdapter = new MyAdapter();
				lv_blacklist.setAdapter(mAdapter);
			} else {
				mAdapter.notifyDataSetChanged();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacklist);

		initUI();
		initData();
	}

	/**
	 * 获取黑名单数据库数据
	 */
	private void initData() {
		isLoading = true;
		new Thread() {

			public void run() {
				mDao = BlacklistDao.getInstance(getApplicationContext());
				mBlacklist = mDao.query(0);
				mCount = mDao.getCount();
				mHandler.sendEmptyMessage(0);
				isLoading = false;
			};
		}.start();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		lv_blacklist = (ListView) findViewById(R.id.lv_blacklist);

		lv_blacklist.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (mCount > mBlacklist.size()
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lv_blacklist.getLastVisiblePosition() >= mBlacklist
								.size() - 1 && !isLoading) {
					isLoading = true;
					new Thread() {

						public void run() {
							mDao = BlacklistDao
									.getInstance(getApplicationContext());
							List<BlacklistInfo> list = mDao.query(mBlacklist
									.size());
							mBlacklist.addAll(list);

							mHandler.sendEmptyMessage(0);
							isLoading = false;
						};
					}.start();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	public void add(View v) {
		showDialog();
	}

	/**
	 * 显示对话框
	 */
	private void showDialog() {
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_blacklist_insert, null);

		final EditText et_add_phone = (EditText) view
				.findViewById(R.id.et_add_phone);
		RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
		Button btn_add_phone = (Button) view.findViewById(R.id.btn_add_phone);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_sms:
					mMode = 1;
					break;

				case R.id.rb_phone:
					mMode = 2;
					break;

				case R.id.rb_all:
					mMode = 3;
					break;
				}
			}
		});

		btn_add_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone = et_add_phone.getText().toString().trim();
				if (TextUtils.isEmpty(phone)) {
					ToastUtils.show(getApplicationContext(), "请输入拦截号码");
				} else {
					mDao.insert(phone, mMode);

					BlacklistInfo info = new BlacklistInfo();
					info.setPhone(phone);
					info.setMode(mMode);
					mBlacklist.add(0, info);

					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}

					dialog.dismiss();
				}
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mBlacklist.size();
		}

		@Override
		public Object getItem(int position) {
			return mBlacklist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.listview_blacklist_item, null);

				holder = new ViewHolder();
				holder.tv_phone = (TextView) convertView
						.findViewById(R.id.tv_phone);
				holder.tv_mode = (TextView) convertView
						.findViewById(R.id.tv_mode);
				holder.iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			BlacklistInfo info = mBlacklist.get(position);
			final String phone = info.getPhone();

			holder.tv_phone.setText(phone);
			switch (info.getMode()) {
			case 1:
				holder.tv_mode.setText("拦截短信");
				break;

			case 2:
				holder.tv_mode.setText("拦截电话");
				break;

			case 3:
				holder.tv_mode.setText("拦截所有");
				break;
			}

			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mDao.delete(phone);
					mBlacklist.remove(position);
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}
				}
			});

			return convertView;
		}

	}

	private static class ViewHolder {
		public TextView tv_phone;
		public TextView tv_mode;
		public ImageView iv_delete;
	}

}
