package com.linquan.net;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;

import android.content.SharedPreferences;
import android.util.Log;

import com.linquan.net.NetUtils;
import com.linquan.net.soket.Client;
import com.linquan.net.soket.Server;
import com.linquan.net.soket.SocketPort;
import com.linquan.status.Status;

/**
 * 远程控制，主要负责完成与服务端的连接和命令的发送
 * @author linquan
 *
 */
public class RemoteControl {

	private static String TAG = "RemoteControl";
	private Client client;
	private static RemoteControl instance = null;
	boolean isCollected = false;

	/**
	 * 实现单例
	 * @return
	 */
	public static RemoteControl getInstance() {
		if (instance == null) {
			instance = new RemoteControl();
		}
		return instance;
	}

	private RemoteControl() {
		client = Client.getInstance();
	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println("RemoteControl start...."); //
	 * test1_getLocalAreaIps(); test2_start_server(); //test2_start_client();
	 * 
	 * }
	 */

	@SuppressWarnings("unused")
	private static void test1_getLocalAreaIps() {
		try {
			Set<String> list = NetUtils.getLocalAreaIps();
			System.out.println("usable ips:" + list.size());
			Iterator<String> i = list.iterator();
			while (i.hasNext())
				System.out.println(i.next());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接服务端
	 * @param ip 尝试连接的服务器IP
	 * @return 成功则返回服务器ip
	 */
	public String collectToServer(String ip) {
		if (ip != null && !"".equals(ip)) {
			try {
				Status.getInstance().sendIntentStatus(
						"start to collect last server...");
				client.collectToServer(ip, SocketPort.PORT);
				isCollected = true;
				Status.getInstance().sendIntentStatus(
						"collected to server:" + ip);
				return ip;
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				Status.getInstance().sendIntentStatus(
						"collect last server error...");
			}
		}
		return "";
	}

	/**
	 * 连接服务器
	 * @return
	 */
	public String collectToServer() {
		Set<String> list = null;
		try {
			Status.getInstance().sendIntentStatus("start to find server...");
			list = NetUtils.getLocalAreaIps();
			Log.d(TAG, "usable ips:" + list.size());
			if (0 == list.size()) {
				Status.getInstance().sendIntentStatus("not find server !");
			}
			// Iterator i = list.iterator();
			// while (i.hasNext())
			// Log.d(TAG, i.next().toString());
		} catch (Exception e) {
			Status.getInstance().sendIntentStatus("error when find server");
			e.printStackTrace();

			return "";
		}
		if (list == null || list.size() <= 0) {
			Status.getInstance().sendIntentStatus("error not find server");
			return "";
		}
		Iterator<String> i = list.iterator();
		String ip;
		while (i.hasNext()) {
			ip = (String) (i.next());
			Log.d(TAG, "start to ip" + ip);
			try {
				Status.getInstance().sendIntentStatus(
						"start to collect server:" + ip);
				client.collectToServer(ip, SocketPort.PORT);
			} catch (Exception e) {
				// e.printStackTrace();
				Log.e(TAG, "error when collect to server:" + ip);
				continue;
			}
			Log.i(TAG, "collect ok on " + ip);
			Status.getInstance().sendIntentStatus("collected to server:" + ip);
			isCollected = true;
			return ip;
		}
		Status.getInstance().sendIntentStatus("not find usable server !");
		return "";
	}

	/**
	 * 向服务端发送命令
	 * @param cmd 要发送的键值
	 */
	public void sendMessage(int cmd) {
		if (isCollected) {
			client.sendCmd(cmd);
		} else {
			Log.e(TAG, "Not Collected to server!");
		}
	}

	/**
	 * 向服务端发送命令
	 * @param cmd 要发送的键值
	 * @param param event参数
	 */
	public void sendMessage(int cmd, Object param) {
		Log.i(TAG, "sendMessage...");
		if (isCollected) {
			client.sendCmd(cmd, param);
		} else {
			Log.e(TAG, "Not Collected to server!");
		}
	}

	/**
	 * 断开与服务端的连接
	 */
	public void discollectToServer() {
		isCollected = false;
		if (client != null) {
			client.disCollect();

		}
	}

	/**
	 * 开启服务端监听程序
	 */
	public static void start_server() {
		Log.d(TAG, "start server thread!");

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Server server = Server.getInstance();
					server.startServer(SocketPort.PORT);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

	@SuppressWarnings("unused")
	private static void test2_start_client() {
		final String serverIp = NetUtils.getLocalIP().getHostAddress();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Client client = Client.getInstance();
					client.collectToServer(serverIp, SocketPort.PORT);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

	/**
	 * 是否已连接服务端
	 * @return
	 */
	public boolean isCollected() {
		return this.isCollected;
	}

}