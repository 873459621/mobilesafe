package com.doodlemobile.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 能够获取焦点的TextView
 * 
 * @author hhw
 */
public class FocusTextView extends TextView {

	// 由系统调用（属性 + 上下文 + 样式文件）
	public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	// 由系统调用（属性 + 上下文）
	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 通过代码创建
	public FocusTextView(Context context) {
		super(context);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

}
