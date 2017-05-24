package com.doodlemobile.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 黑名单的数据库
 * 
 * @author hhw
 */
public class BlacklistOpenHelper extends SQLiteOpenHelper {

	public BlacklistOpenHelper(Context context) {
		super(context, "blacklist.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE BLACKLIST (_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "PHONE VARCHAR(20), MODE INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
