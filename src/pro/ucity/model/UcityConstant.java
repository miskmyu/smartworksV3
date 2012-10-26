/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 26.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.model;

import net.smartworks.server.engine.factory.SwManagerFactory;

public class UcityConstant {
	
	public static String getQueryByKey(String key) {
		return SwManagerFactory.getInstance().getUcityContantsManager().getQueryByKey(key);
	}
	public static String getCodeByKey(String key) {
		return SwManagerFactory.getInstance().getUcityContantsManager().getCodeByKey(key);
	}
}
