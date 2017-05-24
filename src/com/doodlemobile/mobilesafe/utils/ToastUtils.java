package com.doodlemobile.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * 
 * @author hhw
 */
public class ToastUtils {

	/**
	 * 显示一个短时Toast
	 * 
	 * @param context
	 *            上下文
	 * @param text
	 *            显示的文本
	 */
	public static void show(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

}
