package com.linquan.status;

import com.linquan.actions.Constance;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
/**
 * 系统状态值
 * @author linquan
 *
 */
public class Status {
	private Status() {
	}

	private static Status instance = null;

	public synchronized static Status getInstance() {
		if (instance == null) {
			instance = new Status();
		}
		return instance;
	}

	/*
	 * public void setHandler(Handler handler) { private Handler handler = null;
	 * 
	 * this.handler = handler; }
	 * 
	 * public Handler getHandler() { return this.handler; }
	 */

	private Context context;

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return this.context;
	}

	private Context serverContext;

	public void setServerContext(Context context) {
		this.serverContext = context;
	}

	public Context getServerContext() {
		return this.serverContext;
	}

	public void sendIntentStatus(String status) {
		Intent intent = new Intent(Constance.INTENT_ACTION_STATUS);
		intent.putExtra("status", status);
		if (context != null) {
			context.sendBroadcast(intent);
		}
	}

	public void sendIntentStatus(String status, byte[] buf) {
		Intent intent = new Intent(Constance.INTENT_ACTION_STATUS);
		intent.putExtra("status", status);
		intent.putExtra("bufdata", buf);
		if (context != null) {
			context.sendBroadcast(intent);
		}
	}
}
