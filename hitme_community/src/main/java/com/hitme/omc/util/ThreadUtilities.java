package com.hitme.omc.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;

public final class ThreadUtilities {
	private static final ExecutorService cachedExecutorService = Executors
			.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("thread utilities %d").build());
	private static final ScheduledExecutorService defaultScheduledExecutorService = Executors.newScheduledThreadPool(
			Runtime.getRuntime().availableProcessors() * 2,
			new ThreadFactoryBuilder().setNameFormat("default global thread %d").build());
	private static final ScheduledExecutorService timeoutExecutorService = Executors.newScheduledThreadPool(
			Runtime.getRuntime().availableProcessors() * 2,
			new ThreadFactoryBuilder().setNameFormat("timeout global thread %d").build());
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final SimpleIoProcessorPool<NioSession> ioProcessorPool = new SimpleIoProcessorPool(
			NioProcessor.class, Runtime.getRuntime().availableProcessors() * 3);
	private static final ExecutorService defaultExecutorService = new ThreadPoolExecutor(
			Runtime.getRuntime().availableProcessors() * 10, Runtime.getRuntime().availableProcessors() * 16, 0L,
			TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>(
					Runtime.getRuntime().availableProcessors() * 16 * 20),
			new ThreadFactoryBuilder().setNameFormat("communication global thread %d").build());

	public static ExecutorService getDefaultExecutorservice() {
		return defaultExecutorService;
	}

	public static SimpleIoProcessorPool<NioSession> getIoProcessorPool() {
		return ioProcessorPool;
	}

	public static ScheduledExecutorService getDefaultScheduledExecutorService() {
		return defaultScheduledExecutorService;
	}

	public static ScheduledExecutorService getTimeoutExecutorService() {
		return timeoutExecutorService;
	}

	public static ExecutorService getCommunicationService() {
		return Executors.newCachedThreadPool(
				new ThreadFactoryBuilder().setNameFormat("communication service thread %d").build());
	}

	public static void invokeLater(Runnable doRun) {
		cachedExecutorService.execute(doRun);
	}
}
