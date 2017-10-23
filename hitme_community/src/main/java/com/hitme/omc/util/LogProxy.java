package com.hitme.omc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogProxy {
	private Logger log = null;

	@SuppressWarnings("rawtypes")
	public LogProxy(Class logClass) {
		this.log = LoggerFactory.getLogger(logClass);
	}

	public void debug(Object content) {
		this.log.debug(content == null ? null : content.toString());
	}

	public void info(Object content) {
		this.log.info(content == null ? null : content.toString());
	}

	public void warn(Object content) {
		this.log.warn(content == null ? null : content.toString());
	}

	public void error(Object content) {
		this.log.error(content == null ? null : content.toString());
	}

	public void error(Object content, Throwable throwable) {
		this.log.error(content == null ? null : content.toString(), throwable);
	}
}