package com.linquan.actions;

import android.view.KeyEvent;

/**
 * 发送服务端的KeyEvent的keycode，可根据实际情况何须要继续添加。目前是完全匹配Android中的KeyEvent.
 * @author linquan
 *
 */
public interface Actions {
	/**
	 * 断开与服务端的连接
	 */
	public static int ACTION_DISCOLLECT = 0x8fff;
	
	/**
	 * 与服务端连接
	 */
	public static int ACTION_COLLECT_OK = 0X8FFE;
	
	/**
	 * 左键
	 */
	public static int ACTION_LEFT = KeyEvent.KEYCODE_DPAD_LEFT;
	
	/**
	 * 右键
	 */
	public static int ACTION_RIGHT = KeyEvent.KEYCODE_DPAD_RIGHT;
	
	/**
	 * 上键
	 */
	public static int ACTION_UP = KeyEvent.KEYCODE_DPAD_UP;
	
	/**
	 * 下键
	 */
	public static int ACTION_DOWN = KeyEvent.KEYCODE_DPAD_DOWN;
	
	/**
	 * 中间键 OK键
	 */
	public static int ACTION_OK = KeyEvent.KEYCODE_DPAD_CENTER;
	
	/**
	 * Enter键，OK键
	 */
	public static int ACTION_ENTER = KeyEvent.KEYCODE_ENTER;
	
	/**
	 * 菜单键
	 */
	public static int ACTION_MENU = KeyEvent.KEYCODE_MENU;
	
	/**
	 * 返回键
	 */
	public static int ACTION_BACK = KeyEvent.KEYCODE_BACK;

	/**
	 * 数字0键
	 */
	public static int ACTION_NUM_0 = KeyEvent.KEYCODE_0;
	
	/**
	 * 数字9键
	 */
	public static int ACTION_NUM_9 = KeyEvent.KEYCODE_9;
	
	/**
	 * 字母A键
	 */
	public static int ACTION_CHAR_A = KeyEvent.KEYCODE_A;
	
	/**
	 * 字母Z键
	 */
	public static int ACTION_CHAR_Z = KeyEvent.KEYCODE_Z;
	
	/**
	 * 删除键
	 */
	public static int ACTION_BACK_DEL = KeyEvent.KEYCODE_DEL;

	// public static int ACTION_CAPS_LOCK = KeyEvent.KEYCODE_CAPS_LOCK;
	
	/**
	 * 左 SHIFT 键
	 */
	public static int ACTION_SHIFT_LEFT = KeyEvent.KEYCODE_SHIFT_LEFT;
	
	/**
	 * 右 SHIFT 键
	 */
	public static int ACTION_SHIFT_RIGHT = KeyEvent.KEYCODE_SHIFT_RIGHT;

	/**
	 * 音量增加键
	 */
	public static int ACTION_VOLUMN_UP = KeyEvent.KEYCODE_VOLUME_UP;
	
	/**
	 * 音量减小键
	 */
	public static int ACTION_VOLUMN_DOWN = KeyEvent.KEYCODE_VOLUME_DOWN;

}
