package com.linquan.net;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;
import java.util.TreeSet;

import com.linquan.status.Status;

import android.util.Log;
/**
 * WIFI搜寻本IP段时，需要将扫描出可用IP，用以连接。
 * @author linquan
 *
 */
class IP {
	public static Set<String> IpUseable;
	private static int rcount = 0;

	public Set<String> getUseableIPs() throws Exception {
		rcount = 0;
		PingAll();
		return IpUseable;
	}

	private static Object syncObj = new Object();

	private static int threadCount = 0;

	private final static int MAX_THREAD_COUNT = 50;
	private InetAddress localIP = null;

	public IP(InetAddress local) {
		IpUseable = new TreeSet<String>();
		localIP = local;
	}

	/**
	 * 判断ip是否在线
	 * @param ip
	 * @throws Exception
	 */
	private void Ping(String ip) throws Exception {
		while (threadCount >= MAX_THREAD_COUNT)
			Thread.sleep(50);

		synchronized (syncObj) {
			threadCount += 1;
		}
		PingIp p = new PingIp(ip);
		p.start();
	}

	/**
	 * 扫描局域网所有在线IP
	 * @throws Exception
	 */
	private void PingAll() throws Exception {
		// 首先得到本机的IP，得到网段
		String localip = localIP.getHostAddress();
		System.out.println("string localip:" + localip);
		int k = 0;
		k = localip.lastIndexOf(".");
		String ss = localip.substring(0, k + 1);
		String l = localip.substring(k + 1, localip.length());
		System.out.println("local ip last :" + l);
		int loc = Integer.valueOf(l);
		for (int i = 1; i <= 255; i++) { // 对所有局域网Ip
			if (i == loc) {
				continue;
			}
			String iip = ss + i;
			Ping(iip);
		}

		// 等着所有Ping结束
		while (threadCount > 0)
			Thread.sleep(50);
		System.out.println("ping over...");
	}

	/**
	 * 利用Java调用linux的ping命令来实现判断IP是否可用。
	 * @author linquan
	 *
	 */
	class PingIp extends Thread {
		public String ip; // IP

		public PingIp(String ip) {
			this.ip = ip;
		}

		public void run() {
			try {
				System.out.println("ping ip:" + ip + " start ...");
				// Process p = Runtime.getRuntime().exec("ping " + ip +
				// " -c 1");
				Process p = Runtime.getRuntime().exec("ping -c 1 -w 5 " + ip);
				int status = -1;
				try {
					status = p.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (status == 0) {
					IpUseable.add(ip);
					Log.d("test", " ------------------" + ip);
				}
				/*
				 * InputStreamReader ir = new
				 * InputStreamReader(p.getInputStream()); LineNumberReader input
				 * = new LineNumberReader(ir); // 读取结果行 String line = ""; while
				 * ((line = input.readLine()) != null) { Log.w("stest", line);
				 * if (line.contains("time=")) { IpUseable.add(ip); } }
				 */
				// 线程结束
				synchronized (syncObj) {
					threadCount -= 1;
					rcount++;
					if (rcount % 5 == 0) {
						Status.getInstance().sendIntentStatus(
								"find server: " + (rcount * 100 / 255) + "%");
					}
				}
				System.out.println("ping ip:" + ip + " finished !!!");
			} catch (IOException e) {
			}
		}
	}
}
