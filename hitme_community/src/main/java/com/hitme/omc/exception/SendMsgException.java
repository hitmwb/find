package com.hitme.omc.exception;

public class SendMsgException extends Exception {
	private static final long serialVersionUID = 1L;

	public SendMsgException(String message) {
		super(message);
	}

	public SendMsgException(String message, Throwable cause) {
		super(message, cause);
	}
}
