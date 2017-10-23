package com.hitme.omc.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProtocolUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolUtil.class);

	public static String convertBytes2HexStr(byte[] datas) {
		StringBuffer sb = new StringBuffer();
		if ((datas == null) || (datas.length == 0)) {
			return "";
		}
		for (int i = 0; i < datas.length; i++) {
			sb.append(convertByte2HexStr(datas[i])).append(" ");
		}
		return sb.toString();
	}

	public static String convertByte2HexStr(byte data) {
		String hexStr = Integer.toHexString(data & 0xFF | 0xFF00).substring(6);
		return hexStr.toUpperCase();
	}

	public static String fillString(String srcStr, int count) {
		if (StringUtils.isEmpty(srcStr)) {
			LOGGER.error("fillString srcStr is empty.");
			return "";
		}
		if (srcStr.length() >= count) {
			return srcStr.substring(0, count);
		}
		StringBuffer zeroStr = new StringBuffer();
		for (int i = 0; i < count - srcStr.length(); i++) {
			zeroStr.append('0');
		}
		return srcStr + zeroStr.toString();
	}

	public static byte[] cnvrtString2Bytes(String srcStr, int length) {
		return cnvrtString2Bytes(srcStr, "UTF-8", length);
	}

	public static byte[] cnvrtString2Bytes(String srcStr, String charset, int length) {
		try {
			byte[] bytes = srcStr.getBytes(charset);
			byte[] result = new byte[length];
			System.arraycopy(bytes, 0, result, 0, bytes.length);
			return result;
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("cnvrtString2Bytes error.");
		}
		return new byte[length];
	}

	public static byte[] cnvrtLong4Bytes(long longValue) {
		byte[] byteRst = new byte[4];
		byteRst[3] = ((byte) (int) (0xFF & longValue));
		byteRst[2] = ((byte) (int) ((0xFF00 & longValue) >> 8));
		byteRst[1] = ((byte) (int) ((0xFF0000 & longValue) >> 16));
		byteRst[0] = ((byte) (int) ((0xFF000000 & longValue) >> 24));
		return byteRst;
	}

	public static byte[] cnvrtLong2Bytes(long lVal, int len) {
		int i = len - 1;
		byte[] data = new byte[len];
		while (i >= 0) {
			data[i] = ((byte) (int) (lVal % 256L));
			i--;
			lVal >>>= 8;
		}
		return data;
	}

	public static int cnvrtByte2Int(byte[] b) {
		if (b.length < 4) {
			byte[] bTmp = b;
			b = new byte[4];
			System.arraycopy(bTmp, 0, b, 4 - bTmp.length, bTmp.length);
		}
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}

	public static byte[] cnvrtInt2Bytes(int intValue) {
		byte intbyte = cnvrtInt2Byte(intValue);
		return new byte[] { intbyte };
	}

	public static byte cnvrtInt2Byte(int intValue) {
		return (byte) (0xFF & intValue);
	}

	public static int cnvrtUnsignByte2Int(byte byteValue) {
		int intRst = byteValue & 0xFF;
		return intRst;
	}

	public static long cnvrt4Byte2Long(byte[] byteSrc) {
		long longRst = 0L;
		long longRst0 = byteSrc[0] & 0xFF;
		long longRst1 = byteSrc[1] & 0xFF;
		long longRst2 = byteSrc[2] & 0xFF;
		long longRst3 = byteSrc[3] & 0xFF;
		longRst0 <<= 24;
		longRst1 <<= 16;
		longRst2 <<= 8;
		longRst = (longRst0 | longRst1 | longRst2 | longRst3) & 0xFFFFFFFF;
		return longRst;
	}

	public static byte[] joinByteArray(byte[]... bytes) {
		byte[] byteRst = new byte[0];
		for (int i = 0; i < bytes.length; i++) {
			byteRst = ArrayUtils.addAll(byteRst, bytes[i]);
		}
		return byteRst;
	}

	public static byte[] joinByteList(List<byte[]> list) {
		byte[] byteRst = new byte[0];
		if ((list != null) && (!list.isEmpty())) {
			for (byte[] tmp : list) {
				byteRst = ArrayUtils.addAll(byteRst, tmp);
			}
		}
		return byteRst;
	}

	public static void printBytes(String flag, byte[] data) {
		if ((data == null) || (data.length == 0)) {
			LOGGER.error("data is empty");
		} else {
			LOGGER.debug("=========================" + flag + "===============================");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < data.length; i++) {
				sb.append(data[i]);
				if (i != data.length - 1) {
					sb.append(',');
				}
			}
			LOGGER.debug(sb.toString());
		}
	}
}
