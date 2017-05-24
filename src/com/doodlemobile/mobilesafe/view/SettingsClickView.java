package com.doodlemobile.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;

/**
 * 自定义组合控件，设置中心界面中的一个条目
 * 
 * @author hhw
 */
public class SettingsClickView extends RelativeLayout {

	private TextView tv_des;
	private TextView tv_title;

	public SettingsClickView(Context context) {
		this(context, null);
	}

	public SettingsClickView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingsClickView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View.inflate(context, R.layout.settings_click_view, this);

		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
	}

	/**
	 * 设置标题内容
	 * 
	 * @param title
	 *            标题文本
	 */
	public void setTitle(String title) {
		tv_title.setText(title);
	}

	/**
	 * 设置描述内容
	 * 
	 * @param des
	 *            描述文本
	 */
	public void setDes(String des) {
		tv_des.setText(des);
	}

}
