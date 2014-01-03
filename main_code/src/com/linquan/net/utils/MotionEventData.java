package com.linquan.net.utils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
/**
 * 触屏事件数据封装及序列号操作
 * @author linquan
 *
 */
public class MotionEventData implements Externalizable {

	long downTime, eventTime;
	int action, pointerCount, metaState, deviceId, edgeFlags, source;
	float x, y, pressure, size, xPrecision, yPrecision;
	private static String TAG = "MotionEventData";

	public MotionEvent getMotionEvent() {
		MotionEvent e = MotionEvent.obtain(downTime, eventTime, action, x, y,
				pressure, size, metaState, xPrecision, yPrecision, deviceId,
				edgeFlags);
		e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), action, x, y, metaState);
		// e.setSource(source);

		Log.d(TAG, "downTime:" + downTime + " eventTime:" + eventTime
				+ " action:" + action + " x:" + x + " y:" + y + " pressure:"
				+ pressure + " size:" + size + " metaState:" + metaState
				+ " xPrecision:" + xPrecision + " yPrecision:" + yPrecision
				+ " deviceId:" + deviceId + " edgeFlags:" + edgeFlags);
		// e = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState);
		return e;
	}

	public void getMotionEventData(MotionEvent e) {
		downTime = e.getDownTime();
		eventTime = e.getEventTime();
		action = e.getAction();
		pointerCount = e.getPointerCount();
		metaState = e.getMetaState();
		deviceId = e.getDeviceId();
		edgeFlags = e.getEdgeFlags();
		x = e.getX();
		y = e.getY();
		pressure = e.getPressure();
		size = e.getSize();
		Log.d(TAG, "count::" + e.getPointerCount());
		xPrecision = e.getXPrecision();
		yPrecision = e.getYPrecision();
		source = e.getSource();
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException,
			ClassNotFoundException {
		downTime = input.readLong();
		eventTime = input.readLong();

		action = input.readInt();
		pointerCount = input.readInt();
		metaState = input.readInt();
		deviceId = input.readInt();
		edgeFlags = input.readInt();
		source = input.readInt();

		x = input.readFloat();
		y = input.readFloat();
		pressure = input.readFloat();
		size = input.readFloat();
		xPrecision = input.readFloat();
		yPrecision = input.readFloat();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeLong(downTime);
		output.writeLong(eventTime);

		output.writeInt(action);
		output.writeInt(pointerCount);
		output.writeInt(metaState);
		output.writeInt(deviceId);
		output.writeInt(edgeFlags);
		output.writeInt(source);

		output.writeFloat(x);
		output.writeFloat(y);
		output.writeFloat(pressure);
		output.writeFloat(size);
		output.writeFloat(xPrecision);
		output.writeFloat(yPrecision);
	}

}
