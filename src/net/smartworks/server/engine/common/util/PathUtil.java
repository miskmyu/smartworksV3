package net.smartworks.server.engine.common.util;

import org.springframework.util.StringUtils;

public class PathUtil {
	public static String toPath(String path) {
		if (path == null)
			return null;
		StringBuffer buf = new StringBuffer();
		String[] paths = StringUtils.tokenizeToStringArray(path, "/");
		int count = 0;
		for (int i=0; i<paths.length; i++) {
			if (paths[i].length() == 0)
				continue;
			if (count++ != 0)
				buf.append("/");
			buf.append(paths[i]);
		}
		path = buf.toString();
		if (path.length() == 0)
			path = "/";
		return path;
	}
	
	public static String toPath(String parent, String name) {
		if (name == null)
			return null;
		if (parent == null)
			parent = "";
		return new StringBuffer().append(PathUtil.toPath(parent)).append("/").append(toName(name)).toString();
	}
	
	public static String toParentPath(String path) {
		if (path == null)
			return null;
		if (path.indexOf("\\") != -1)
			path = StringUtils.replace(path, "\\", "/");
		StringBuffer buf = new StringBuffer();
		String[] paths = StringUtils.tokenizeToStringArray(path, "/");
//		int count = 0;
		for (int i=0; i<paths.length-1; i++) {
			if (paths[i].length() == 0)
				continue;
//			if (count++ != 0)
//				buf.append("/");
			buf.append("/");
			buf.append(paths[i]);
		}
		String parentPath = buf.toString();
		return parentPath;
	}
	
	public static String toName(String path) {
		if (path == null)
			return null;
		if (path.indexOf("\\") != -1)
			path = StringUtils.replace(path, "\\", "/");
		String name = null;
		String[] paths = StringUtils.tokenizeToStringArray(path, "/");
		if (paths == null || paths.length == 0)
			name = path;
		else {
			name = paths[paths.length-1];
		}
		return name;
	}
	public static String toScopeName(String path) {
		if (path == null)
			return null;
		String name = null;
		String[] paths = StringUtils.tokenizeToStringArray(path, "/");
		if (paths == null || paths.length == 0)
			name = path;
		else {
			name = paths[0];
		}
		return name;
	}		
	
}
