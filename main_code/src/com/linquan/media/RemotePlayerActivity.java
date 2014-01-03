package com.linquan.media;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.linquan.testevent.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class RemotePlayerActivity extends Activity implements
		SurfaceHolder.Callback, MediaPlayer.OnPreparedListener,
		MediaPlayer.OnCompletionListener {

	private MediaPlayer mPlayer;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private String path1 = "http://192.168.1.102:8083";
	private String path = "/mnt/sdcard/1080p.mp4";
	private static final String TAG = "RemotePlayerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_remote_player);
		mPlayer = new MediaPlayer();
		mPlayer.setOnPreparedListener(this);
		mPlayer.setOnCompletionListener(this);
		mSurfaceView = (SurfaceView) findViewById(R.id.surface);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG,"surfaceChanged, width:"+width+", height:"+height);

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mPlayer.setDisplay(holder);
		FileDescriptor fd = new FileDescriptor();
		try {
			Method method = fd.getClass().getMethod("setInt$", int.class);
			try {
				method.invoke(fd, 33);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.d(TAG,"fd:"+fd.toString());
		
		try {
			mPlayer.setDataSource(path);
			mPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mPlayer.release();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mPlayer.start();
	}
	
	public void onDestroy(){
		super.onDestroy();
		mPlayer.release();
	}

}
