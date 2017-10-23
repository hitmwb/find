package com.hitme.omc.handler;

public abstract class ReturnHandler<T> {
	public void handle(T value) {
	}

	public abstract void handle(Throwable paramThrowable);

	public void handle(Throwable ex, Object... objects) {
		handle(ex);
	}
}
