package com.doodlemobile.mobilesafe.receiver;

import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 手机重启事件的广播接收者
 * 
 * @author hhw
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean open_security = SPUtils.getBoolean(context,
				ConstantValue.OPEN_SECURITY, false);
		if (open_security) {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String simSerialNumber = tm.getSimSerialNumber();
			String sim_number = SPUtils.getString(context,
					ConstantValue.SIM_NUMBER, null);
			if (!TextUtils.isEmpty(simSerialNumber)
					&& !simSerialNumber.equals(sim_number)) {

				String contact_phone = SPUtils.getString(context,
						ConstantValue.CONTACT_PHONE, null);
				if (!TextUtils.isEmpty(contact_phone)) {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(contact_phone, null,
							"SIM changed!", null, null);
				}

			}
		}
	}

}
