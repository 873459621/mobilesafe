package com.doodlemobile.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doodlemobile.mobilesafe.R;

/**
 * 自定义组合控件，设置中心界面中的一个条目
 * 
 * @author hhw
 */
public class SettingsItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.doodlemobile.mobilesafe";

	private CheckBox cb_box;
	private TextView tv_des;
	private TextView tv_title;

	private String mDesOff;
	private String mDesOn;
	private String mDesTitle;

	public SettingsItemView(Context context) {
		this(context, null);
	}

	public SettingsItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingsItemView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View.inflate(context, R.layout.settings_item_view, this);

		initUI();
		initAttrs(attrs);

		tv_title.setText(mDesTitle);
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		cb_box = (CheckBox) findViewById(R.id.cb_box);
	}

	/**
	 * 初始化属性集合中的自定义属性
	 * 
	 * @param attrs
	 *            属性集合
	 */
	private void initAttrs(AttributeSet attrs) {
		mDesOff = attrs.getAttributeValue(NAMESPACE, "desOff");
		mDesOn = attrs.getAttributeValue(NAMESPACE, "desOn");
		mDesTitle = attrs.getAttributeValue(NAMESPACE, "desTitle");
	}

	/**
	 * 判断SettingsItemView是否被选中的方法，true为选中，false为未选中
	 * 
	 * @return SettingsItemView的选中状态
	 */
	public boolean isChecked() {
		return cb_box.isChecked();
	}

	/**
	 * 设置SettingsItemView的选中状态的方法，true为选中，false为未选中
	 * 
	 * @param checkedState
	 *            SettingsItemView的选中状态
	 */
	public void setCheckedState(boolean isChecked) {
		cb_box.setChecked(isChecked);
		if (isChecked) {
			tv_des.setText(mDesOn);
		} else {
			tv_des.setText(mDesOff);
		}
	}

}
