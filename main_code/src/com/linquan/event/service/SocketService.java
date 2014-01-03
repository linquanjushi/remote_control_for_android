package com.linquan.event.service;

import com.linquan.net.RemoteControl;
import com.linquan.status.Status;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 服务端Socket，用于接收客户端发来的消息
 * @author linquan
 *
 */
public class SocketService extends Service {

	private static final String TAG = "SocketService";
	private IBinder binder = new SocketService.LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}


	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
		Status.getInstance().setServerContext(this.getApplicationContext());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		RemoteControl.start_server();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	public class LocalBinder extends Binder {
		// 返回本地服务
		SocketService getService() {
			return SocketService.this;
		}
	}

}
