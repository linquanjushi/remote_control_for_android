package com.linquan.net.soket;

import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import android.util.Log;

import com.linquan.actions.Actions;
import com.linquan.event.service.ScreenShot;
import com.linquan.net.RemoteMessage;
/**
 * socket服务端
 * @author linquan
 *
 */
public class Server {

	private static int ClientCount = 0;
	private final static String TAG = "Server";
	private RemoteMessage rm = new RemoteMessage();
	private ScreenShot screenshot = null;
	// private ObjectOutputStream oos;
	// private ObjectInputStream ois;
	private boolean needshot = false;

	private static Server instance = null;

	private Server() {
	}

	/**
	 * 单例实现
	 * @return
	 */
	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	/**
	 * 开启服务端监听
	 * @param port
	 * @throws Exception
	 */
	public void startServer(int port) throws Exception {
		ClientCount = 0;
		System.out.println("start server...");
		screenshot = ScreenShot.getInstance();
		ServerSocket server = null;
		server = new ServerSocket(port);
		Socket socket = null;
		while (true) {
			socket = server.accept();
			Log.d(TAG, "someone collected to me!!!!");
			OutputStream os1 = socket.getOutputStream();
			os1.flush();
			// DataOutputStream dos = new DataOutputStream(os1);
			ObjectOutputStream oos = new ObjectOutputStream(os1);
			oos.flush();
			if (ClientCount > 0) {
				Log.w(TAG, "I have a Controller now ! NOT need this,kill it!");
				oos.writeInt(Actions.ACTION_DISCOLLECT);
				oos.flush();
				socket.close();
			} else {
				oos.writeInt(Actions.ACTION_COLLECT_OK);
				oos.flush();
				ClientCount += 1;
				Log.w(TAG, "collect OK!");
				startTalkThread(socket);
			}
		}

		// os.close();

		// server.close();
	}

	/**
	 * 与客户端通信的线程
	 * @param socket
	 * @throws Exception
	 */
	private void startTalkToclient(Socket socket) throws Exception {
		Log.d(TAG, "startTalkToclient function in");
		// String line;
		InputStream is = socket.getInputStream();
		Log.d(TAG, "startTalkToclient function in 1");
		// DataInputStream dis = new DataInputStream(is);
		ObjectInputStream ois = new ObjectInputStream(is);

		// PrintWriter os = new PrintWriter(socket.getOutputStream());
		// BufferedReader sin = new BufferedReader(
		// new InputStreamReader(System.in));
		// byte[] buf = new byte[4];
		Log.d(TAG, "start to wait first data from client");
		int data = -1;
		data = ois.readInt();
		Log.d(TAG, "first data come from client only want to say hello :"
				+ data);
		Object obj;
		// System.out.println("client:" + is.read(buf, 0, 4));
		/*
		 * try { //data = com.linquan.net.utils.DataUtils.HBytes2Int(buf); }
		 * catch (Exception e) { e.printStackTrace(); }
		 */
		// line = sin.readLine();
		while (data != 0x8fff) {
			// os.println(line);
			// os.flush();
			try {
				data = ois.readInt();
				// data = com.linquan.net.utils.DataUtils.HBytes2Int(buf);
				Log.d(TAG, "receive command:" + data);
				if (data == 0xcafe || data == 0xcaff) {
					obj = ois.readObject();
					rm.sendMessage(data, obj);
				} else if (data == 0xbabe) {
					if (!needshot) {
						needshot = true;
						startScreenShotThread(socket);
					}
				} else if (data == 0xbabf) {
					if (needshot) {
						needshot = false;
					}
				} else {
					rm.sendMessage(data);
				}
			} catch (EOFException e) {
				e.printStackTrace();
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
			// System.out.println("client:" + data);
			// line = sin.readLine();
		}

		is.close();
		socket.close();
		ClientCount -= 1;
	}

	/**
	 * 开启与客户端通信的线程
	 * @param socket
	 * @throws Exception
	 */
	private void startTalkThread(final Socket socket) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.d(TAG, "startTalkThread...");
					startTalkToclient(socket);
				} catch (Exception e) {
					ClientCount -= 1;
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 开启截屏的线程
	 * @param socket
	 */
	private void startScreenShotThread(final Socket socket) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					startScreenShot(socket);
				} catch (Exception e) {
					ClientCount -= 1;
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 截屏数据发送给客户端
	 * @param socket
	 * @throws Exception
	 */
	private void startScreenShot(Socket socket) throws Exception {
		Log.d(TAG, "startScreenShot thread..");
		ObjectOutputStream oos = new ObjectOutputStream(
				socket.getOutputStream());
		while (!socket.isClosed() && needshot) {
			Log.d(TAG, "getScreenShot...");
			try {
				ByteBuffer buf = screenshot.getScreenShot();
				oos.writeInt(0xbabe);// cmd
				oos.writeInt(buf.array().length);// size
				oos.write(buf.array(), 0, buf.array().length);// data
				oos.flush();
				Log.d(TAG, "send screen to client");
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
				needshot = false;
				break;
			}
		}

		needshot = false;
	}
}
