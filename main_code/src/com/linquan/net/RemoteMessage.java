package com.linquan.net;

import com.linquan.net.utils.MotionEventData;

import android.app.Instrumentation;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 接收客户端命令并向系统发送
 * @author linquan
 *
 */
public class RemoteMessage {

	private Instrumentation inst;
	private final String TAG = "RemoteMessage";
	private Handler handler;

	public RemoteMessage() {
		createMessageHandleThread();
		while (handler == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		inst = new Instrumentation();
	}

	/**
	 * 向系统发送命令
	 * @param msg
	 */
	public void sendMessage(final int msg) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				sendTestKeyevnt(msg);
			}
		}).start();
	}

	/**
	 * 创建发送命令的消息循环
	 */
	private void createMessageHandleThread() {
		// need start a thread to raise looper, otherwise it will be blocked
		Thread t = new Thread() {
			public void run() {
				Log.i(TAG, "Creating handler ...");
				Looper.prepare();
				handler = new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case 0:
							MotionEvent event = (MotionEvent) msg.obj;
							inst.sendPointerSync(event);
							break;
						default:
							Log.w(TAG, "unknown message!");
						}
					}
				};
				Looper.loop();
				Log.i(TAG, "Looper thread ends");
			}
		};
		t.start();
	}

	/**
	 * 向系统发送命令
	 * @param msg 
	 * @param obj
	 */
	public void sendMessage(final int msg, final Object obj) {
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		if (msg == 0xcafe || msg == 0xcaff) {
			Log.d(TAG, "send message 1:" + msg);
			try {
				MotionEventData data = (MotionEventData) obj;
				MotionEvent event = data.getMotionEvent();
				sendTouchevnt(msg, event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// }
		// }).start();
	}

	/**
	 * 向系统发送键值命令
	 * @param action
	 */
	private void sendTestKeyevnt(final int action) {
		// handler.post(new Runnable() {
		// @Override
		// public void run() {
		try {
			Log.i(TAG, "start to send message...");
			inst.sendKeyDownUpSync(action);
			Log.i(TAG, "send message " + " finished!");
		} catch (Exception e) {
			Log.e(TAG, "error:" + e.getMessage());
			e.printStackTrace();
		}
		// }
		// });
	}

	/**
	 * 向系统发送触屏命令
	 * @param msg
	 * @param event
	 */
	public void sendTouchevnt(final int msg, final MotionEvent event) {
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// handler.post(new Runnable() {
		// public void run() {
		try {
			Log.i(TAG, "start to send message...");
			if (msg == 0xcafe) {
				Message mesg = Message.obtain(handler, 0);
				mesg.obj = event;
				mesg.sendToTarget();
				// inst.sendPointerSync(event);
			} else if (msg == 0xcaff) {
				// TODO
				// inst.sendPointerAsync(event);
			}
			Log.i(TAG, "send message  finished!");
		} catch (Exception e) {
			Log.e(TAG, "error:" + e.getMessage());
			e.printStackTrace();
		}
		// }
		// });
		// }
		// }).start();
	}
}
