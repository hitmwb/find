package com.hitme.omc.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hitme.omc.model.AccountInfo;
import com.hitme.omc.util.LogProxy;

public class AccountManager {
	private static final LogProxy LOGGER = new LogProxy(AccountManager.class);

	private static AccountManager instance;

	private Map<String, AccountInfo> accountMap = new HashMap<String, AccountInfo>();

	private AccountManager() {
		init();
	}

	public static synchronized AccountManager getInstance() {
		if (instance == null) {
			instance = new AccountManager();
		}
		return instance;
	}

	public void init() {
		this.accountMap.clear();
		try {
			InputStream cfgIn = getClass().getResourceAsStream("/account.xml");
			parseAccount(cfgIn);
			cfgIn.close();
		} catch (IOException e) {
			LOGGER.error("init error", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void parseAccount(InputStream cfgIn) {
		SAXReader saxReader = new SAXReader();
		saxReader.setValidation(false);
		try {
			Document doc = saxReader.read(cfgIn);
			Element usersNode = doc.getRootElement();
			List<Element> usersNodeList = usersNode.elements("user");
			if ((usersNodeList == null) || (usersNodeList.isEmpty())) {
				return;
			}
			for (Element userNode : usersNodeList) {
				String user = userNode.attributeValue("user");
				String key = userNode.attributeValue("key");
				String ip = userNode.attributeValue("bindIp");
				if ((user != null) && (!user.isEmpty())) {

					AccountInfo account = new AccountInfo();
					account.setUser(user);
					account.setKey(key);
					account.setBindIp(ip);
					this.accountMap.put(user, account);
				}
			}
		} catch (DocumentException e) {
			LOGGER.error("parse entity file error.", e);
		}
	}

	public Map<String, AccountInfo> getaccountMap() {
		return this.accountMap;
	}

	public AccountInfo getAccountInfo(String user) {
		return StringUtils.isEmpty(user) ? null : (AccountInfo) this.accountMap.get(user);
	}
}
