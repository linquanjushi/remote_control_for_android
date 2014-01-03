package com.linquan.net.soket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.linquan.actions.Actions;
import com.linquan.status.Status;

import android.util.Log;
/**
 * socketk客户端
 * @author linquan
 *
 */
public class Client {
	// private String serverIp = "127.0.0.1";
	private final static String TAG = "Client";
	private static int isCollect = 0;
	int cmd = -1;
	Socket socket;
	InetSocketAddress isa;
	OutputStream os1;
	// DataOutputStream dos;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	private Client() {
	}

	private static Client instance = null;

	/**
	 * 单例实现
	 * @return
	 */
	public static Client getInstance() {
		if (instance == null) {
			instance = new Client();
		}
		return instance;
	}

	/*
	 * public Client(Socket socket) { this.socket = socket; try { os =
	 * socket.getOutputStream(); } catch (IOException e) { e.printStackTrace();
	 * } }
	 */

	// public Client(String serverIp) {
	// this.serverIp = serverIp;
	// }
/**
 * 连接服务端
 * @param serverIp 服务端IP
 * @param port 服务端端口
 * @throws UnknownHostException
 * @throws IOException
 */
	public synchronized void collectToServer(String serverIp, int port)
			throws UnknownHostException, IOException {
		Log.d("client", "try to collect to " + serverIp + " on port " + port);

		socket = new Socket();
		isa = new InetSocketAddress(serverIp, port);
		socket.connect(isa, 1000);
		// socket = new Socket(serverIp, port);
		// BufferedReader sin = new BufferedReader(
		// new InputStreamReader(System.in));
		if (socket == null || !socket.isConnected()) {
			Log.e("client", "socket is null");
			throw new IOException("socket is null");
		}
		Log.i("client", "socket is not null");
		InputStream is = socket.getInputStream();
		ois = new ObjectInputStream(is);
		Log.i("client", "wait for message from server...");
		int ok = ois.readInt();
		Log.i(TAG, "collect status:" + ok);
		if (ok == Actions.ACTION_COLLECT_OK) {
			Log.i(TAG, "collect ok return from server..");
		} else if (ok == Actions.ACTION_DISCOLLECT) {
			Log.e(TAG,
					"no right to collect to server.may be server has a controller!!");
			throw new IOException("Server had a controller!!");
		} else {
			Log.e(TAG, "unkonwn error!");
			throw new IOException("unkonwn error!!!");
		}
		os1 = socket.getOutputStream();
		// dos = new DataOutputStream(os1);
		oos = new ObjectOutputStream(os1);
		if (oos == null) {
			Log.e("client", "oos is null");
			throw new IOException("oos is null");
		}
		isCollect = 1;
		Log.i("client", "oos is not null");
		// BufferedReader is = new BufferedReader(new InputStreamReader(
		// socket.getInputStream()));

		// String readline;
		// readline = sin.readLine();
		// while (isCollect == 1) {
		int cmd = 0;
		Log.d(TAG, "say hello to server!");
		oos.writeInt(cmd);
		oos.flush();
		// os.flush();
		// System.out.println("Client cmd:" + cmd);
		// System.out.println("server:" + is.readLine());
		// readline = sin.readLine();
		// }
		// os.close();
		// is.close();
		// socket.close();
//		startScreenReceiverThread(socket);
//		oos.writeInt(0xbabe);
//		oos.flush();
	}

	/**
	 * 向服务端发送命令
	 * @param cmd
	 */
	public synchronized void sendCmd(int cmd) {
		try {
			if (isCollect == 1) {
				Log.d("Client", "sendcmd:" + cmd);
				oos.writeInt(cmd);
				oos.flush();
			} else {
				Log.e(TAG, "Not Collect to server");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向服务端发送命令
	 * @param cmd 
	 * @param param 
	 */
	public synchronized void sendCmd(int cmd, Object param) {
		try {
			if (isCollect == 1) {
				Log.d("Client", "sendcmd:" + cmd);
				oos.writeInt(cmd);
				oos.writeObject(param);
				//oos.flush();
			} else {
				Log.e(TAG, "Not Collect to server");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 断开socket连接
	 */
	public void disCollect() {
		isCollect = 0;
		try {
			if (oos != null) {
				oos.writeInt(Actions.ACTION_DISCOLLECT);
				oos.flush();
				Thread.sleep(500);
				oos.close();
			}
			if (socket != null)
				socket.close();
			Status.getInstance().sendIntentStatus("Discollected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收服务端截屏数据的线程
	 * @param socket
	 */
	private void startScreenReceiverThread(final Socket socket) {
		Log.d(TAG, "startScreenReceiverThread...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!socket.isClosed()) {
					try {
						int cmd = ois.readInt();
						Log.d(TAG, "data coming!" + cmd);
						if (cmd == 0xbabe) {
							int size = ois.readInt();
							if (size > 0) {
								byte[] buf = new byte[size];
								ois.read(buf, 0, size);
								Status.getInstance().sendIntentStatus("1", buf);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}

			}
		}).start();
	}
}
