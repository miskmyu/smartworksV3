/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 26.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.manager.ucityWorkList.manager.impl;

import java.util.Hashtable;
import java.util.Map;

import javax.sql.DataSource;

import net.smartworks.server.engine.common.util.CommonUtil;

import pro.ucity.manager.ucityWorkList.manager.IUcityConstantsManager;

public class UcityConstantsManagerImpl implements IUcityConstantsManager {

	DataSource dataSource = null;
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private Map<String, String> queryMap = new Hashtable<String, String>();
	private Map<String, String> codeMap = new Hashtable<String, String>();
	private Map<String, String> passUrlList = new Hashtable<String, String>();
	
	public Map<String, String> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
	}

	public Map<String, String> getCodeMap() {
		return codeMap;
	}

	public void setCodeMap(Map<String, String> codeMap) {
		this.codeMap = codeMap;
	}
	@Override
	public String getQueryByKey(String key) {
		if (CommonUtil.isEmpty(key))
			return null;
		return this.queryMap.get(key);
	}
	@Override
	public String getCodeByKey(String key) {
		if (CommonUtil.isEmpty(key))
			return null;
		return this.codeMap.get(key);
	}
	
	public Map<String, String> getPassUrlList() {
		return passUrlList;
	}
	public void setPassUrlList(Map<String, String> passUrlList) {
		this.passUrlList = passUrlList;
	}
	@Override
	public String getUrlByKey(String key) {
		if (CommonUtil.isEmpty(key))
			return null;
		return this.passUrlList.get(key);
	}

}
