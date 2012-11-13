/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 26.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.manager.ucityWorkList.manager;

import javax.sql.DataSource;

public interface IUcityConstantsManager {
	public DataSource getDataSource();
	public String getQueryByKey(String key);
	public String getCodeByKey(String key);
	public String getUrlByKey(String key);
}
