package com.hitme.omc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hitme.omc.util.LogProxy;

@SpringBootApplication
public class OMCServerApplication {
	private static final LogProxy LOGGER = new LogProxy(OMCServerApplication.class);

	public static void main(String[] args) {
		LOGGER.info("OMC server start");
		SpringApplication.run(OMCServerApplication.class, args);
	}
}
