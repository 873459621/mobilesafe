package com.doodlemobile.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Stream工具类
 * 
 * @author hhw
 */
public class StreamUtils {

	/**
	 * 将流转换成字符串
	 * 
	 * @param in
	 *            流对象
	 * @return 流转换成的字符串，返回null代表异常
	 */
	public static String streamToString(InputStream in) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			copy(in, baos);
			return baos.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(in, baos);
		}
		return null;
	}

	/**
	 * 把输入流中的数据拷贝到输出流
	 * 
	 * @param in
	 *            输入流
	 * @param out
	 *            输出流
	 * @throws IOException
	 *             拷贝过程中出现的异常
	 */
	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
	}

	/**
	 * 关闭输入流和输出流
	 * 
	 * @param in
	 *            输入流
	 * @param out
	 *            输出流
	 */
	public static void close(InputStream in, OutputStream out) {
		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
