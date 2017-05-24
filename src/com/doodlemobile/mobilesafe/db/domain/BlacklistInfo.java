package com.doodlemobile.mobilesafe.db.domain;

/**
 * 黑名单数据库的bean对象
 * 
 * @author hhw
 */
public class BlacklistInfo {

	private String phone;
	private int mode;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}
