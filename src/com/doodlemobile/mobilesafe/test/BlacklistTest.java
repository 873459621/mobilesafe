package com.doodlemobile.mobilesafe.test;

import com.doodlemobile.mobilesafe.db.dao.BlacklistDao;

import android.test.AndroidTestCase;

public class BlacklistTest extends AndroidTestCase {

	private BlacklistDao mDao = BlacklistDao.getInstance(getContext());

	public void insert() {
		mDao.insert("110", 1);
	}

}
