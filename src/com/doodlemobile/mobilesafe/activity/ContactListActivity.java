package com.doodlemobile.mobilesafe.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.utils.Contact;
import com.doodlemobile.mobilesafe.utils.ContactsUtils;

/**
 * 显示联系人列表的Activity
 * 
 * @author hhw
 */
public class ContactListActivity extends Activity {

	private ListView lv_contact;
	private List<Contact> mContacts;

	@SuppressLint("HandlerLeak")
	private Handler mHanler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			MyAdapter adapter = new MyAdapter();
			lv_contact.setAdapter(adapter);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);

		initUI();
		initData();
	}

	/**
	 * 初始化联系人列表数据
	 */
	private void initData() {
		new Thread() {

			public void run() {
				mContacts = ContactsUtils.readContacts(getApplicationContext());
				mHanler.sendEmptyMessage(0);
			};
		}.start();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		lv_contact = (ListView) findViewById(R.id.lv_contact);

		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mContacts != null) {
					Contact contact = mContacts.get(position);
					String phone = contact.getPhone();

					Intent intent = new Intent();
					intent.putExtra("phone", phone);
					setResult(0, intent);

					finish();
				}
			}
		});
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mContacts.size();
		}

		@Override
		public Object getItem(int position) {
			return mContacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.listview_contact_item, null);

				holder = new ViewHolder();
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_mode);
				holder.tv_phone = (TextView) convertView
						.findViewById(R.id.tv_phone);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Contact contact = mContacts.get(position);
			String name = contact.getName();
			String phone = contact.getPhone();

			holder.tv_name.setText(name);
			holder.tv_phone.setText(phone);

			return convertView;
		}

	}

	private static class ViewHolder {
		public TextView tv_phone;
		public TextView tv_name;
	}

}
