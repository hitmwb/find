package com.hitme.omc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	private static final LogProxy LOGGER = new LogProxy(ZipUtil.class);

	public static void zipFile(String filepath, String zippath) {
		InputStream input = null;
		ZipOutputStream zipOut = null;
		try {
			File file = new File(filepath);
			File zipFile = new File(zippath);
			input = new FileInputStream(file);
			zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			zipOut.putNextEntry(new ZipEntry(file.getName()));
			int temp = 0;
			while ((temp = input.read()) != -1) {
				zipOut.write(temp);
			}
			return;
		} catch (IOException e) {
			LOGGER.error("zip file error.", e);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				LOGGER.error("close input error.", e);
			}
			try {
				zipOut.close();
			} catch (IOException e) {
				LOGGER.error("close zipout error.", e);
			}
		}
	}
}
