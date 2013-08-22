package net.smartworks.server.engine.common.util;

import java.util.Properties;

public class ConfigPropertiesUtil {

	public static String getProperties(String key) throws Exception {
		
		Properties prop = new Properties();
		prop.load(ConfigPropertiesUtil.class.getResourceAsStream("config.properties"));
		
		String propStr = prop.getProperty(key);
		return propStr;
	}
}
