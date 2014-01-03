package com.linquan.net;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;

import android.util.Log;
/**
 * 网络工具，主要用来实现获取本机IP，和本网段所有在线IP
 * @author linquan
 *
 */
public class NetUtils {

	/**
	 * 获取本机物理地址
	 * @return
	 */
	public static String getMacAddressFromEtcFile() {
		try {
			Locale locale = Locale.getDefault();
			return loadFileAsString("/sys/class/net/eth0/address")
					.toUpperCase(locale) .substring(0, 17) ;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从文件中读取内容
	 * @param filePath 文件地址
	 * @return 文件内容
	 * @throws java.io.IOException
	 */
	public static String loadFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}

	/**
	 * 获取本机IP
	 * @return
	 */
	public static InetAddress getLocalIP() {
		Enumeration<NetworkInterface> allNetInterfaces = null;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}
		InetAddress ip = null;
		InetAddress result = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
					.nextElement();
			System.out.println(netInterface.getName());
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					System.out.println("本机的IP = " + ip.getHostAddress());
					if (ip.isLoopbackAddress()) {
						System.out.println("loop back addresss...");
						result = ip;
					} else {
						System.out.println("wlan address");
						result = ip;
					}
				}
			}
		}
		return result;
	}

	public static Set<String> getLocalAreaIps() throws Exception {
		InetAddress local = getLocalIP();
		if (local == null) {
			Log.e("NetUtils", "get local ip error !");
			throw new Exception("get local ip error!");
		}
		String localip = local.getHostAddress();
		int k = 0;
		k = localip.lastIndexOf(".");
		String ss = localip.substring(0, k + 1);
		if ("127.0.0.".equals(ss)) {
			throw new Exception("Pls Open WiFi!");
		}
		IP ip = new IP(local);
		return ip.getUseableIPs();
	}

}
