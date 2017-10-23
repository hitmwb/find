package com.hitme.omc.util;

public final class OMCConstant {
	public static final String LOGIN_USER_FLAG = "user";
	public static final String LOGIN_TYPE_FLAG = "loginType";
	public static final String FAIL_COUNT = "failCount";
	public static final String SESSION_ID = "sessionId";

	public static class ALARM_STATUS {
		public static final int CREATE = 1;
		public static final int DISCOVERY = 0;
	}

	public static class SYNC_SOURCE {
		public static final int ALARM_CURR = 0;
		public static final int ALARM_LOG = 1;
	}

	public static class THREAD_STATUS {
		public static final int STOP = -1;
		public static final int SUSPEND = 0;
		public static final int RUNNING = 1;
	}

	public static class MSG_TYPE {
		public static final int REAL_TIME_ALARM = 0;
		public static final int REQ_LOGIN_ALARM = 1;
		public static final int ACK_LOGIN_ALARM = 2;
		public static final int REQ_SYNC_ALARM_MSG = 3;
		public static final int ACK_SYNC_ALARM_MSG = 4;
		public static final int REQ_SYNC_ALARM_FILE = 5;
		public static final int ACK_SYNC_ALARM_FILE = 6;
		public static final int ACK_SYNC_ALARM_FILE_RESULT = 7;
		public static final int REQ_HEART_BEAT = 8;
		public static final int ACK_HEART_BEAT = 9;
		public static final int CLOSE_CONN_ALARM = 16;
	}

	public static class OPER_FAIL_REASON {
		public static final String NULL = "";
		public static final String PARAM_EMPTY = "Req param empty or incorrect";
		public static final String USER_NOT_EXIST = "User not exist";
		public static final String KEY_INCORRECT = "User key incorrect";
		public static final String REPEATED_LOGIN = "Repeated login";
		public static final String SYNC_ALARM_BUSY = "System busy to sync alarm";
		public static final String TCP_TYPE_ERROR = "tcp login type error";
		public static final String UNKNOW = "unknow reason";
	}

	public static class OPER_RESULT {
		public static final String SUCC = "succ";
		public static final String FAIL = "fail";
	}

	public static class LOGIN_TYPE {
		public static final String MSG = "msg";
		public static final String FTP = "ftp";
	}
}
