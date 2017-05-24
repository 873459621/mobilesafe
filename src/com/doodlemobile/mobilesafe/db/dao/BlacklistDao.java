package com.doodlemobile.mobilesafe.db.dao;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doodlemobile.mobilesafe.db.BlacklistOpenHelper;
import com.doodlemobile.mobilesafe.db.domain.BlacklistInfo;

/**
 * 黑名单数据库的dao，单例设计模式
 * 
 * @author hhw
 */
public class BlacklistDao {

	private static final String TABLE = "BLACKLIST";
	private static final String PHONE = "PHONE";
	private static final String MODE = "MODE";
	private static final String ID = "_ID";

	private static BlacklistDao mInstance;

	private BlacklistOpenHelper mHelper;
	private SQLiteDatabase mDB;
	private ContentValues mValues;
	private Cursor mCursor;

	private BlacklistDao(Context context) {
		mHelper = new BlacklistOpenHelper(context);
	}

	/**
	 * 获取BlacklistDao的实例
	 * 
	 * @param context
	 *            上下文
	 * @return BlacklistDao的实例
	 */
	public static BlacklistDao getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new BlacklistDao(context);
		}
		return mInstance;
	}

	/**
	 * 给黑名单添加一条记录
	 * 
	 * @param phone
	 *            电话号码
	 * @param mode
	 *            拦截模式（1：短信 2：电话 3：所有）
	 * @return 数据被插入到第几行，返回-1代表出现异常
	 */
	public long insert(String phone, int mode) {
		mDB = mHelper.getWritableDatabase();

		mValues = new ContentValues();
		mValues.put(PHONE, phone);
		mValues.put(MODE, mode);

		long insert = mDB.insert(TABLE, null, mValues);

		mDB.close();
		return insert;
	}

	/**
	 * 从黑名单删除一条记录
	 * 
	 * @param phone
	 *            电话号码
	 * @return 删除的行数
	 */
	public int delete(String phone) {
		mDB = mHelper.getWritableDatabase();

		int delete = mDB.delete(TABLE, PHONE + "=?", new String[] { phone });

		mDB.close();
		return delete;
	}

	/**
	 * 根据电话号码，去更新拦截的模式
	 * 
	 * @param phone
	 *            电话号码
	 * @param mode
	 *            拦截模式（1：短信 2：电话 3：所有）
	 * @return 更新的行数
	 */
	public int update(String phone, int mode) {
		mDB = mHelper.getWritableDatabase();

		mValues = new ContentValues();
		mValues.put(MODE, mode);
		int update = mDB.update(TABLE, mValues, PHONE + "=?",
				new String[] { phone });

		mDB.close();
		return update;
	}

	/**
	 * 查询所有黑名单中的记录
	 * 
	 * @return 黑名单bean对象的集合
	 */
	public List<BlacklistInfo> query() {
		List<BlacklistInfo> list = new LinkedList<BlacklistInfo>();
		mDB = mHelper.getWritableDatabase();

		mCursor = mDB.query(TABLE, new String[] { PHONE, MODE }, null, null,
				null, null, ID + " DESC");

		if (mCursor != null && mCursor.getCount() > 0) {
			BlacklistInfo info;
			while (mCursor.moveToNext()) {
				info = new BlacklistInfo();
				info.setPhone(mCursor.getString(0));
				info.setMode(mCursor.getInt(1));
				list.add(info);
			}
			mCursor.close();
		}

		mDB.close();
		return list;
	}

	/**
	 * 查询20条黑名单中的记录
	 * 
	 * @param index
	 *            查询的索引值
	 * @return 黑名单bean对象的集合
	 */
	public List<BlacklistInfo> query(int index) {
		List<BlacklistInfo> list = new LinkedList<BlacklistInfo>();
		mDB = mHelper.getWritableDatabase();

		mCursor = mDB.rawQuery("SELECT * FROM " + TABLE + " ORDER BY " + ID
				+ " DESC LIMIT ?, 20", new String[] { index + "" });

		if (mCursor != null && mCursor.getCount() > 0) {
			BlacklistInfo info;
			while (mCursor.moveToNext()) {
				info = new BlacklistInfo();
				info.setPhone(mCursor.getString(1));
				info.setMode(mCursor.getInt(2));
				list.add(info);
			}
			mCursor.close();
		}

		mDB.close();
		return list;
	}

	/**
	 * 获取黑名单条目总数
	 * 
	 * @return 黑名单条目总数
	 */
	public int getCount() {
		mDB = mHelper.getWritableDatabase();

		mCursor = mDB.rawQuery("SELECT COUNT(*) FROM " + TABLE, null);
		int count = 0;
		if (mCursor != null && mCursor.moveToNext()) {
			count = mCursor.getInt(0);
			mCursor.close();
		}

		mDB.close();
		return count;
	}

	/**
	 * 根据电话号码，查询拦截模式
	 * 
	 * @param phone
	 *            要查询的电话号码
	 * @return 拦截模式（1：短信 2：电话 3：所有），返回-1代表结果不存在
	 */
	public int getMode(String phone) {
		mDB = mHelper.getWritableDatabase();

		mCursor = mDB.query(TABLE, new String[] { MODE }, PHONE + "=?",
				new String[] { phone }, null, null, null);

		int mode = -1;
		if (mCursor != null && mCursor.moveToNext()) {
			mode = mCursor.getInt(0);
			mCursor.close();
		}

		mDB.close();
		return mode;
	}
	
}
