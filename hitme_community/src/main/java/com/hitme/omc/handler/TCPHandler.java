package com.hitme.omc.handler;

public abstract interface TCPHandler<T1, T2> {
	public abstract void handle(T1 paramT1, T2 paramT2);
}
