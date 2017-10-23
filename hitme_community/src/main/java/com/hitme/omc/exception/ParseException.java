package com.hitme.omc.exception;

public class ParseException extends Exception {
	private static final long serialVersionUID = 1L;

	public ParseException() {
	}

	public ParseException(String errcoMsg) {
		super(errcoMsg);
	}

	public ParseException(Throwable throwable) {
		super(throwable);
	}

	public ParseException(String errorMsg, Throwable throwable) {
		super(errorMsg, throwable);
	}
}
