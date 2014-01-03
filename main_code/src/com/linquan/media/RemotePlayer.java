package com.linquan.media;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;

public class RemotePlayer {
	private MediaPlayer mp;
	private int mediaSourceId;

	public RemotePlayer() {
		mp = new MediaPlayer();
	}

	public void setMediaSourceId(int id) {
		mediaSourceId = id;
	}

	public int getMediaSourceId() {
		return mediaSourceId;
	}

	public void start() {
		mp.start();
	}

	public void pause() {
		mp.pause();
	}

	public void stop() {
		mp.stop();
	}

	public void release() {
		mp.release();
	}

	public void reset() {
		mp.reset();
	}
	
	public void setDisplay(SurfaceHolder sh){
		mp.setDisplay(sh);
	}

}
