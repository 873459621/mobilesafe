package com.doodlemobile.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.doodlemobile.mobilesafe.R;
import com.doodlemobile.mobilesafe.service.LocationService;
import com.doodlemobile.mobilesafe.utils.ConstantValue;
import com.doodlemobile.mobilesafe.utils.SPUtils;

/**
 * 收到短信事件的广播接收者
 * 
 * @author hhw
 */
public class SmsReceiver extends BroadcastReceiver {

	private ComponentName mDeviceAdminSample;
	private DevicePolicyManager mDPM;

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean open_security = SPUtils.getBoolean(context,
				ConstantValue.OPEN_SECURITY, false);
		if (open_security) {
			mDeviceAdminSample = new ComponentName(context, AdminReceiver.class);
			mDPM = (DevicePolicyManager) context
					.getSystemService(Context.DEVICE_POLICY_SERVICE);

			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
				String body = sms.getMessageBody();
				
				if (body.contains("#*alarm*#")) {
					MediaPlayer player = MediaPlayer.create(context,
							R.raw.alarm);
					player.setLooping(true);
					player.start();
				} else if (body.contains("#*location*#")) {
					Intent service = new Intent(context, LocationService.class);
					context.startService(service);
				} else if (mDPM.isAdminActive(mDeviceAdminSample)) {
					if (body.contains("#*lockscreen*#")) {
						mDPM.lockNow();
						mDPM.resetPassword("123", 0);
					} else if (body.contains("#*wipedata*#")) {
						mDPM.wipeData(0);
						mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
					}
				}
			}
		}
	}

}
