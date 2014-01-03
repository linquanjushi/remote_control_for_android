package com.linquan.testevent;

import java.util.Timer;
import java.util.TimerTask;

import com.linquan.actions.Actions;
import com.linquan.actions.Constance;
import com.linquan.event.service.ScreenShot;
import com.linquan.net.RemoteControl;
import com.linquan.net.RemoteMessage;
import com.linquan.net.utils.MotionEventData;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
/**
 * 测试 向服务端发送触屏事件的例子
 * @author linquan
 *
 */
public class TouchActivity extends Activity {

	private String TAG = "TouchActivity";
	private static Handler handler = null;
	private RemoteControl comManager = null;
	private int backCount = 0;
	boolean waiting = false;
	private ImageView imv;

	private RemoteMessage rm;
	private boolean debug = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置为横屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_touch);
		imv = (ImageView) findViewById(R.id.ts_imv);
		initEventLoop();
		comManager = RemoteControl.getInstance();
		backCount = 0;
		if (debug)
			rm = new RemoteMessage();

	}

	private void initEventLoop() {
		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() {
		 */
		/*
		 * Looper looper = Looper.myLooper(); if (looper == null) { looper =
		 * Looper.getMainLooper(); } if (looper == null) {
		 * android.util.Log.e(TAG,
		 * "ERROR! when create comhandler the loop is null !"); }
		 */
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.d(TAG, "handler messsage:" + msg.what);
				switch (msg.what) {
				case 0:
					sendTouchEvent(msg.obj);
					break;
				case 1:
					sendKeyEvent(msg.arg1);
					break;
				}
			}
		};
		// }
		// }).start();
	}

	/*
	 * private class ComHandler extends Handler {
	 * 
	 * public ComHandler(Looper looper) { super(looper);
	 * 
	 * }
	 * 
	 * public ComHandler() {
	 * 
	 * }
	 * 
	 * @Override public void handleMessage(Message msg) { Log.d(TAG,
	 * "handler messsage:" + msg.what); switch (msg.what) { case 0:
	 * sendTouchEvent(msg.obj); break; case 1: sendKeyEvent(msg.arg1); break; }
	 * } }
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			Log.i(TAG, "onTouchEvent...");
			MotionEventData data = new MotionEventData();
			data.getMotionEventData(event);
			// event.recycle();
			Message msg = handler.obtainMessage(0);
			// Log.i(TAG, "onTouchEvent4...");
			msg.obj = data;
			// handler.sendMessage(msg);

			sendTouchEvent(data);

			// Log.i(TAG, "onTouchEvent5...");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	private void sendTouchEvent(final Object event) {
		try {
			Log.i(TAG, "sendTouchEvent...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (debug) {
						rm.sendMessage(0xcafe, event);
					} else
						comManager.sendMessage(0xcafe, event);
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void sendKeyEvent(int cmd) {
		comManager.sendMessage(cmd);
	}

	Timer timer = new Timer();

	TimerTask task = new MyTimerTask();

	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			backCount = 0;
			Log.d(TAG, "task over");
		}
	};

	@Override
	public void onBackPressed() {

		try {
			timer.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		timer = new Timer();
		backCount++;
		Log.d(TAG, "back count:" + backCount);
		try {
			task = new MyTimerTask();
			timer.schedule(task, 350);
		} catch (Exception e) {
			e.printStackTrace();
		}

		sleep(300);
		if (backCount >= 3) {
			backCount = 0;
			this.finish();
		} else {
			Message msg = new Message();
			msg.what = 1;
			msg.arg1 = Actions.ACTION_BACK;
			handler.sendMessage(msg);
		}
	}

	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "menu clicked...");
		Message msg = new Message();
		msg.what = 1;
		msg.arg1 = Actions.ACTION_MENU;
		handler.sendMessage(msg);
		return true;
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * Log.d(TAG, "keycode:" + keyCode); switch (keyCode) { case
	 * Actions.ACTION_MENU: Log.d(TAG, "menu clicked..."); Message msg = new
	 * Message(); msg.what = 1; msg.arg1 = Actions.ACTION_MENU;
	 * handler.sendMessage(msg); return true; default: return
	 * super.onKeyDown(keyCode, event); }
	 * 
	 * }
	 */

	class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constance.INTENT_ACTION_STATUS)) {
				Log.d(TAG, "BroadcastReceiver...");
				String status = intent.getStringExtra("status");
				byte[] buf = intent.getByteArrayExtra("bufdata");
				Bitmap b = ScreenShot.getInstance().getBitmap(buf);
				imv.setImageBitmap(b);
			}
		}
	}

}
