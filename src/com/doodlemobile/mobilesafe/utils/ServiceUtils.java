package com.doodlemobile.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * Service工具类
 * 
 * @author hhw
 */
public class ServiceUtils {

	private static ActivityManager mAM;

	/**
	 * 判断服务是否正在运行
	 * 
	 * @param context
	 *            上下文
	 * @param serviceName
	 *            服务名称
	 * @return true代表服务正在运行，false代表服务已关闭
	 */
	public static boolean isRunning(Context context, String serviceName) {
		mAM = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = mAM.getRunningServices(100);
		
		for (RunningServiceInfo runningServiceInfo : services) {
			String name = runningServiceInfo.service.getClassName();
			if (name.equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

}
