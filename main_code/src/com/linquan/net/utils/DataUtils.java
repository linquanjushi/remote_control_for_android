/**
 * @copyright (C) 2011 The GosTV Project
 * @filename BaseCommands.java
 * @author zt 
 * @description 
 * @version 1.0, 09/08/2011
 * @since 1.0
 */

package com.linquan.net.utils;

import java.io.UnsupportedEncodingException;

/**
 * 数据格式转化处理
 * 
 * @author linquan
 * 
 */
public class DataUtils {

	/**
	 * int整数转化为网络字节序(大端)的byte数组
	 */
	public static byte[] Int2HBytes(int num) {
		byte b[] = new byte[4];

		b[0] = (byte) ((num >> 24) & 0xff);
		b[1] = (byte) ((num >> 16) & 0xff);
		b[2] = (byte) ((num >> 8) & 0xff);
		b[3] = (byte) ((num >> 0) & 0xff);

		return b;
	}

	/**
	 * 网络字节序的byte数组转化为int整数
	 */
	public static int HBytes2Int(byte[] b) throws ParamException {
		if (b.length < 4) {
			throw new ParamException("HBytes2Int(byte[] ) param error!");
		}

		int value = 0;

		value += (b[0] & 0x000000FF) << 24;
		value += (b[1] & 0x000000FF) << 16;
		value += (b[2] & 0x000000FF) << 8;
		value += b[3] & 0x000000FF;

		return value;
	}

	/**
	 * 网络字节序的byte数组转化为int整数
	 */

	public static int HBytes2Int(byte[] b, int start) throws ParamException {

		if (b.length < 4 || start + 4 >= b.length) {
			throw new ParamException("HBytes2Int(byte[] , int ) param error!");
		}

		int value = 0;

		value += (b[start + 0] & 0x000000FF) << 24;
		value += (b[start + 1] & 0x000000FF) << 16;
		value += (b[start + 2] & 0x000000FF) << 8;
		value += b[start + 3] & 0x000000FF;

		return value;
	}

	/**
	 * short 转化为网络字节序的byte数组
	 */
	public static byte[] Short2HBytes(short num) {
		byte b[] = new byte[2];

		b[0] = (byte) ((num >> 8) & 0xff);
		b[1] = (byte) (num & 0xff);

		return b;
	}

	/**
	 * 网络字节序的byte数组转化为short整数
	 */
	public static short HBytes2Short(byte[] b) throws ParamException {
		if (b.length < 2) {
			throw new ParamException("HBytes2Short(byte[] ) param error!");
		}

		short value = 0;

		value += (b[0] & 0x00FF) << 8;
		value += b[1] & 0x00FF;

		return value;
	}

	/**
	 * 网络字节序的byte数组转化为short整数
	 */
	public static short HBytes2Short(byte[] b, int start) throws ParamException {
		if (b.length < 2 || start + 2 >= b.length) {
			throw new ParamException("HBytes2Short(byte[] ) param error!");
		}

		short value = 0;

		value += (b[0] & 0x00FF) << 8;
		value += b[1] & 0x00FF;

		return value;
	}

	/**
	 * 字符串转化为byte数组
	 */
	public static byte[] String2Bytes(String str) {
		return str.getBytes();
	}

	/**
	 * byte数组转化为字符串
	 */
	public static String ByteS2String(byte[] b) {
		StringBuffer result = new StringBuffer("");
		int length = b.length;
		for (int i = 0; i < length; i++) {
			result.append((char) (b[i] & 0xff));
		}
		return result.toString();
	}

	/**
	 * int转化为16进制字符串
	 * 
	 * @param i
	 * @return 十六进制字符串
	 */
	public static String IntToHexString(int i) {
		String s = Integer.toHexString(i);
		// System.out.println(s);
		return s;
	}

	/**
	 * 字节数组转化为字符串
	 * 
	 * @param start
	 *            开始的索引
	 * @param len
	 *            需要转化的字符串长度 byte数组转化为字符串
	 */
	public static String ByteS2String(byte[] b, int start, int len)
			throws ParamException {
		if (start + len >= b.length) {
			throw new ParamException(
					"ByteS2String(byte[] , int , int ) param error!");
		}

		StringBuffer result = new StringBuffer("");
		int length = len;
		for (int i = start; i < length; i++) {
			result.append((char) (b[i] & 0xff));
		}
		return result.toString();
	}

	/**
	 * 获取子數組
	 */
	public static byte[] getSubBytes(byte[] b, int start, int len) {
		if (start + len > b.length) {
			return null;
		}

		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = b[start + i];
		}
		return result;
	}

	public static String decode(byte[] src, String format)
			throws UnsupportedEncodingException {
		return new String(src, format);
	}
}
