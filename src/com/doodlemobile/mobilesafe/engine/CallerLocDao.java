package com.doodlemobile.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 查询电话归属地的Dao
 * 
 * @author hhw
 */
public class CallerLocDao {

	public static String path = "data/data/com.doodlemobile.mobilesafe/files/CallerLoc.db";
	private static String mLocation;

	/**
	 * 获取电话归属地
	 * 
	 * @param phoneNumber
	 *            要查询的电话号码
	 * @return 电话归属地
	 */
	public static String getCallerLoc(String phoneNumber) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor;

		String regularExpression = "^1[3-8]\\d{9}";
		if (phoneNumber.matches(regularExpression)) {

			String carrier = null;
			String prefix = phoneNumber.substring(0, 3);
			cursor = db.query("CarrierInfo", new String[] { "Carrier" },
					"prefix=?", new String[] { prefix }, null, null, null);
			if (cursor != null && cursor.moveToNext()) {
				carrier = cursor.getString(0);
			}

			String id = null;
			String number = phoneNumber.substring(0, 7);
			cursor = db.query("CallerLoc", new String[] { "location" },
					"number=?", new String[] { number }, null, null, null);
			if (cursor != null && cursor.moveToNext()) {
				id = cursor.getString(0);

				String location = null;
				cursor = db.query("LocationInfo", new String[] { "location" },
						"_id=?", new String[] { id }, null, null, null);
				if (cursor != null && cursor.moveToNext()) {
					location = cursor.getString(0);
					mLocation = location + " " + carrier;
				}
			} else {
				mLocation = "未知号码";
			}
		} else {
			String code;
			switch (phoneNumber.length()) {
			case 3:
				mLocation = "报警电话";
				break;

			case 4:
				mLocation = "模拟器";
				break;

			case 5:
				mLocation = "服务电话";
				break;

			case 7:
				mLocation = "本地电话";
				break;

			case 8:
				mLocation = "本地电话";
				break;

			case 11:
				code = phoneNumber.substring(1, 3);
				cursor = db.query("LocationInfo", new String[] { "location" },
						"code=?", new String[] { code }, null, null, null);
				if (cursor != null && cursor.moveToNext()) {
					mLocation = cursor.getString(0);
				}
				break;

			case 12:
				code = phoneNumber.substring(1, 4);
				cursor = db.query("LocationInfo", new String[] { "location" },
						"code=?", new String[] { code }, null, null, null);
				if (cursor != null && cursor.moveToNext()) {
					mLocation = cursor.getString(0);
				}
				break;

			default:
				mLocation = "未知号码";
				break;
			}
		}
		return mLocation;
	}

}
