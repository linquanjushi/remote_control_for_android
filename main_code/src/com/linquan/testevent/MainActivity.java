package com.linquan.testevent;

import com.linquan.actions.Actions;
import com.linquan.actions.Constance;
import com.linquan.media.RemotePlayerActivity;
import com.linquan.net.RemoteControl;
import com.linquan.net.RemoteMessage;
import com.linquan.status.Status;
import com.linquan.testevent.R;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 测试类，实现简单的上下左右等命令的发送
 * 
 * @author linquan
 * 
 */
public class MainActivity extends Activity {

	private EditText text;
	private Button btn_menu, btn_back, btn_up, btn_down, btn_left, btn_right,
			btn_ok, btn_collect, btn_discollect, btn_touch_mode, btn_volumn_up,
			btn_volumn_down, btn_screenshot, btn_test_media;
	private TextView tv_status;

	// private Client client;
	private RemoteControl cmdManager;
	private MyBroadcastReceiver mbr = null;
	private final static String TAG = "MainActivity";
	private String lastIp = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();

		mbr = new MyBroadcastReceiver();
		Status.getInstance().setContext(this.getApplicationContext());
		cmdManager = RemoteControl.getInstance();
		initActions();

		loadData();
	}

	private void saveData() {
		Editor sharedata = getSharedPreferences(TAG, 0).edit();
		sharedata.putString("ip", lastIp);
		sharedata.commit();
	}

	private void loadData() {
		SharedPreferences sharedata = getSharedPreferences(TAG, 0);
		lastIp = sharedata.getString("ip", "");
		Log.v("cola", "last ip=" + lastIp);
	}

	private void findViews() {
		text = (EditText) findViewById(R.id.display_editText);
		text.setText("get gevent");
		btn_up = (Button) findViewById(R.id.btn_up);
		btn_down = (Button) findViewById(R.id.btn_down);
		btn_left = (Button) findViewById(R.id.btn_left);
		btn_right = (Button) findViewById(R.id.btn_right);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_menu = (Button) findViewById(R.id.btn_menu);
		btn_back = (Button) findViewById(R.id.btn_back);

		btn_collect = (Button) findViewById(R.id.btn_collect);
		btn_discollect = (Button) findViewById(R.id.btn_discollect);
		tv_status = (TextView) findViewById(R.id.tv_status);
		btn_touch_mode = (Button) findViewById(R.id.btn_touch_mode);

		btn_volumn_up = (Button) findViewById(R.id.btn_volumn_up);
		btn_volumn_down = (Button) findViewById(R.id.btn_volumn_down);

		btn_screenshot = (Button) findViewById(R.id.btn_screenshot_test);

		btn_test_media = (Button) findViewById(R.id.btn_test_media);
	}

	private void initActions() {
		btn_menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendTestKeyevnt(Actions.ACTION_MENU);
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendTestKeyevnt(Actions.ACTION_BACK);
			}
		});
		btn_up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendTestKeyevnt(Actions.ACTION_UP);
			}
		});
		btn_down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendTestKeyevnt(Actions.ACTION_DOWN);
			}
		});
		btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendTestKeyevnt(Actions.ACTION_LEFT);
			}
		});
		btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendTestKeyevnt(Actions.ACTION_RIGHT);
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendTestKeyevnt(Actions.ACTION_ENTER);
			}
		});

		btn_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						String ip = "";
						if (!"".equals(lastIp)) {
							ip = cmdManager.collectToServer(lastIp);
						}
						if (!"".equals(ip)) {
							return;
						}
						Status.getInstance().sendIntentStatus("start...");
						ip = cmdManager.collectToServer();
						if (!"".equals(ip)) {
							lastIp = ip;
						}
					}
				}).start();
			}
		});

		btn_discollect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				cmdManager.discollectToServer();
				Log.i("test", "discollect to server!");
			}
		});

		btn_touch_mode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(MainActivity.this, TouchActivity.class);
				startActivity(i);
			}
		});

		btn_volumn_up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendTestKeyevnt(Actions.ACTION_VOLUMN_UP);
			}
		});
		btn_volumn_down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendTestKeyevnt(Actions.ACTION_VOLUMN_DOWN);
				/*
				 * MotionEvent e =
				 * MotionEvent.obtain(SystemClock.uptimeMillis(),
				 * SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
				 * v.getX(), v.getY(), 0); RemoteMessage msg = new
				 * RemoteMessage(); msg.sendTouchevnt(0xcafe, e); e =
				 * MotionEvent.obtain(SystemClock.uptimeMillis(),
				 * SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, v.getX(),
				 * v.getY(), 0); msg.sendTouchevnt(0xcafe, e);
				 */
			}
		});

		btn_screenshot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent i = new Intent();
				// i.setClass(MainActivity.this, TestScreenShotActivity.class);
				// startActivity(i);
				cmdManager.start_server();
			}
		});

		btn_test_media.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(MainActivity.this, RemotePlayerActivity.class);
				startActivity(i);

			}
		});
	}

	private void sendTestKeyevnt(final int action) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.i("test", "start to send message...");
					cmdManager.sendMessage(action);
					Log.i("test", "send message " + action + " finished!");
				} catch (Exception e) {
					Log.e("test", "error:" + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "keycode:" + keyCode);
		if ((keyCode >= Actions.ACTION_NUM_0 && keyCode <= Actions.ACTION_NUM_9)
				|| (keyCode >= Actions.ACTION_CHAR_A && keyCode <= Actions.ACTION_CHAR_Z)
				|| KeyEvent.isModifierKey(keyCode)
				|| keyCode == Actions.ACTION_BACK_DEL) {
			sendTestKeyevnt(keyCode);
			return super.onKeyDown(keyCode, event);
		}

		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_CAPS_LOCK:
			text.setText("get The Events 0");
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			Log.d("test", "receive message: left");
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			Log.d("test", "receive message: right");
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			Log.d("test", "receive message: up");
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			Log.d("test", "receive message: down");
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			Log.d("test", "receive message: center");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constance.INTENT_ACTION_STATUS)) {
				String status = intent.getStringExtra("status");
				tv_status.setText(status);
			}
		}
	}

	@Override
	public void onStart() {
		IntentFilter filter = new IntentFilter(Constance.INTENT_ACTION_STATUS);
		this.registerReceiver(mbr, filter);
		super.onStart();
	}

	@Override
	public void onStop() {
		this.unregisterReceiver(mbr);
		super.onStop();
	}

	@Override
	public void onDestroy() {
		mbr = null;
		cmdManager.discollectToServer();
		Status.getInstance().setContext(null);
		saveData();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("exit");
		TextView view = new TextView(MainActivity.this);
		view.setText("sure to exit?");
		dialog.setView(view);
		dialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MainActivity.this.finish();
					}
				});
		dialog.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.create();
		dialog.show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG, "onTouchEvent come....");
		return true;

	}

}
