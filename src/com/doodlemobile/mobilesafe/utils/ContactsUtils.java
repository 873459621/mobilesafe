package com.doodlemobile.mobilesafe.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * 联系人工具类
 * 
 * @author hhw
 */
public class ContactsUtils {

	/**
	 * 从手机中读取联系人列表
	 * 
	 * @param context
	 *            上下文
	 * @return 联系人对象Contact的List集合
	 */
	public static List<Contact> readContacts(Context context) {
		List<Contact> contacts = new ArrayList<Contact>();

		Uri raw_contacts = Uri
				.parse("content://com.android.contacts/raw_contacts");
		Uri data = Uri.parse("content://com.android.contacts/data");

		ContentResolver resolver = context.getContentResolver();
		Cursor idCursor = resolver.query(raw_contacts,
				new String[] { "contact_id" }, null, null, null);
		if (idCursor != null && idCursor.getCount() > 0) {
			while (idCursor.moveToNext()) {

				String contact_id = idCursor.getString(0);
				if (contact_id != null) {
					Contact contact = new Contact();
					contact.setId(contact_id);

					Cursor infoCursor = resolver.query(data, new String[] {
							"data1", "mimetype" }, "raw_contact_id=?",
							new String[] { contact_id }, null);
					if (infoCursor != null && infoCursor.getCount() > 0) {
						while (infoCursor.moveToNext()) {

							String data1 = infoCursor.getString(0);
							String mimetype = infoCursor.getString(1);

							if ("vnd.android.cursor.item/name".equals(mimetype)) {
								contact.setName(data1);
							} else if ("vnd.android.cursor.item/phone_v2"
									.equals(mimetype)) {
								contact.setPhone(data1);
							} else if ("vnd.android.cursor.item/email_v2"
									.equals(mimetype)) {
								contact.setEmail(data1);
							}
						}
						infoCursor.close();
					}
					contacts.add(contact);
				}
			}
			idCursor.close();
		}
		return contacts;
	}

}
