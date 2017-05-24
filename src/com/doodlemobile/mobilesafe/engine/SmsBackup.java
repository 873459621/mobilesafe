package com.doodlemobile.mobilesafe.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

/**
 * 备份短信的引擎类
 * 
 * @author hhw
 */
public class SmsBackup {

	/**
	 * 备份短信
	 * 
	 * @param context
	 *            上下文
	 * @param path
	 *            备份的路径
	 * @param dialog
	 *            要更新的对话框的对象
	 */
	public static void backup(Context context, String path,
			SmsBackupProgress sbp) {
		FileOutputStream fos = null;
		Cursor cursor = null;
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);

			ContentResolver resolver = context.getContentResolver();
			Uri uri = Uri.parse("content://sms/");
			cursor = resolver.query(uri, new String[] { "address", "date",
					"type", "body" }, null, null, null);
			int count = cursor.getCount();

			if (cursor != null && count > 0) {
				if (sbp != null) {
					sbp.setMax(count);
				}

				XmlSerializer serializer = Xml.newSerializer();
				serializer.setOutput(fos, "utf-8");

				serializer.startDocument("utf-8", true);
				serializer.startTag(null, "smss");

				int value = 0;
				while (cursor.moveToNext()) {
					serializer.startTag(null, "sms");

					serializer.startTag(null, "address");
					serializer.text(cursor.getString(0));
					serializer.endTag(null, "address");

					serializer.startTag(null, "date");
					serializer.text(cursor.getString(1));
					serializer.endTag(null, "date");

					serializer.startTag(null, "type");
					serializer.text(cursor.getString(2));
					serializer.endTag(null, "type");

					serializer.startTag(null, "body");
					serializer.text(cursor.getString(3));
					serializer.endTag(null, "body");

					serializer.endTag(null, "sms");

					if (sbp != null) {
						sbp.setProgress(++value);
					}

					Thread.sleep(500);
				}

				serializer.endTag(null, "smss");
				serializer.endDocument();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (cursor != null) {
					cursor.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public interface SmsBackupProgress {

		public void setMax(int max);

		public void setProgress(int value);

	}

}
