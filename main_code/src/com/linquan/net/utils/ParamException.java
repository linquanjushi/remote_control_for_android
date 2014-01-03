/**
 * @copyright (C) 2011 The GosTV Project
 * @filename BaseCommands.java
 * @author zt 
 * @description param exception
 * @version 1.0, 09/08/2011
 * @since 1.0
 */
package com.linquan.net.utils;

public class ParamException extends Exception {

	/**
	 * 参数异常处理
	 */
	private static final long serialVersionUID = 8154992449088821414L;

	public ParamException() {
		super();
	    }
	
	public ParamException(String message) {
		super(message);
	    }
}
