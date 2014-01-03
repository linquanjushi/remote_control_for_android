package com.linquan.event.service;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.linquan.status.Status;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

/**
 * 实现截屏的程序，在服务端运行，用于向客户端发送服务端当前界面。
 * 次程序用到系统权限，需要在Android源码中编译。
 * @author linquan
 *
 */
public class ScreenShot {
	private WindowManager mWindowManager;
	// private Matrix mDisplayMatrix;
	private Display mDisplay;
	private DisplayMetrics mDisplayMetrics;
	private Bitmap mScreenBitmap;
	private int width;
	private int height;

	private static ScreenShot instance = null;

	public static ScreenShot getInstance() {
		if (instance == null) {
			instance = new ScreenShot();
		}
		return instance;
	}

	private ScreenShot() {
		Context context = Status.getInstance().getServerContext();
		if (context == null) {
			context = Status.getInstance().getContext();
		}
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		// mDisplayMatrix = new Matrix();
		mDisplay = mWindowManager.getDefaultDisplay();
		mDisplayMetrics = new DisplayMetrics();

		// TODO
		// mDisplay.getRealMetrics(mDisplayMetrics);
		width = mDisplayMetrics.widthPixels;
		height = mDisplayMetrics.heightPixels;

	}

	public ByteBuffer getScreenShot() {
		Log.d("screenshot", "getScreenShot");
		// TODO
		// mScreenBitmap = Surface.screenshot(width, height);
		int size = mScreenBitmap.getByteCount();
		Log.d("screenshot", "bitmap size:" + size);
		ByteBuffer buf = ByteBuffer.allocateDirect(size);
		mScreenBitmap.copyPixelsToBuffer(buf);
		return buf;
	}

	public Bitmap getBitmap(byte[] buf) {
		Bitmap ss = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		ByteBuffer buf1 = ByteBuffer.wrap(buf);
		ss.copyPixelsFromBuffer(buf1);

		ss.setHasAlpha(false);
		ss.prepareToDraw();
		return ss;
	}
}
