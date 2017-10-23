package com.hitme.omc.handler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitme.omc.manager.ConfigManager;
import com.hitme.omc.service.AlarmService;
import com.hitme.omc.util.LogProxy;
import com.hitme.omc.util.TimeUtil;
import com.hitme.omc.util.ZipUtil;

@Component
public class AlarmSyncFileHandler {
	private static final LogProxy LOGGER = new LogProxy(AlarmSyncFileHandler.class);

	@Autowired
	private AlarmService alarmService;

	@Autowired
	private ConfigManager configManager;

	public String createAlarmFile(String startTime, String endTime, long alarmSeq, int syncSource, long currSeq) {
		LOGGER.info("begin to search alarm data to write alarm file.syncSource=" + syncSource);
		String fileName = null;
		List<String> alarmList = null;
		if (syncSource == 0) {
			alarmList = this.alarmService.searchAlarmCurr(startTime, endTime);
		} else if (syncSource == 1) {
			if (alarmSeq > 0L) {
				alarmList = this.alarmService.searchAlarmLog(alarmSeq, currSeq);
			} else {
				alarmList = this.alarmService.searchAlarmLog(startTime, endTime);
			}
		}
		if (!CollectionUtils.isEmpty(alarmList)) {
			fileName = writeAlarmFile(alarmList);
		}
		return fileName;
	}

	public String writeAlarmFile(List<String> alarmList) {
		String timeDay = TimeUtil.getCurrentStrTime("yyyyMMdd");
		String dir = this.configManager.getAlarmFileDir() + File.separator + timeDay;
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		String alarmFileName = getFileName();
		File alarmFile = new File(dir, alarmFileName);
		File fileTmp = new File(dir, alarmFileName + ".tmp");
		if (alarmFile.exists()) {
			alarmFile.delete();
		}
		try {
			RandomAccessFile raf = new RandomAccessFile(fileTmp, "rw");
			FileChannel fileChannel = raf.getChannel();
			for (String alarmJson : alarmList) {
				if (!StringUtils.isEmpty(alarmJson)) {

					alarmJson = alarmJson + "\r\n";
					byte[] data = alarmJson.getBytes();
					ByteBuffer buf = ByteBuffer.wrap(data);
					fileChannel.write(buf);
					fileChannel.force(true);
				}
			}
			fileChannel.close();
			raf.close();
			fileTmp.renameTo(alarmFile);
			String alarmFilePath = alarmFile.getAbsolutePath();
			String zipFilePath = alarmFilePath + ".zip";
			ZipUtil.zipFile(alarmFilePath, zipFilePath);
			alarmFile.delete();
			return zipFilePath;
		} catch (IOException e) {
			LOGGER.error("write alarm data file io exception", e);
		}
		return null;
	}

	public boolean checkParam(String startTime, String endTime, long alarmSeq, int syncSource) {
		if (syncSource == 0) {
			if ((StringUtils.isEmpty(startTime)) && (StringUtils.isEmpty(endTime))) {
				return false;
			}
		} else if (syncSource == 1) {
			if (alarmSeq > 0L) {
				return true;
			}
			if ((StringUtils.isEmpty(startTime)) && (StringUtils.isEmpty(endTime))) {
				return false;
			}
		}
		return true;
	}

	public String getFileName() {
		StringBuffer sb = new StringBuffer();
		sb.append("FM-").append("OMC-").append("1A-").append("V1.1.0-");
		sb.append(TimeUtil.getStrCurrentStrTime()).append("-001.txt");
		return sb.toString();
	}
}
