package com.linquan.event.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 服务端程序，监听Android启动完成的广播事件，并启动服务端Socket。
 * @author linquan
 *
 */
public class BootCompleteBroadReceiver extends BroadcastReceiver {
	final static String TAG = "BootCompleteBroadReceiver";
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "BOOT_COMPLETED.....");
		if (intent.getAction().equals(ACTION)) {
			try {
				Intent newIntent = new Intent(context, SocketService.class);
				newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 注意，必须添加这个标记，否则启动会失败
				//context.startActivity(newIntent);
				context.startService(newIntent);
				// 这边可以添加开机自动启动的应用程序代码
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}

}
