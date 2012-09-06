/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 1. 16.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.common.util;

public class StringUtil {

	public StringUtil() {
		super();
	} 

	/**
	 * 문자 자르기, 생략 문자는 tag로 표기
	 * @param str
	 * @param startPoint
	 * @param endPoint
	 * @param tag
	 * @return
	 */
	public static String subString(String str, int startPoint, int endPoint, String tag) {

		str = CommonUtil.toNotNull(str);
		int engCount = 0;
		if(!str.equals("")) {
			for(int i=0; i<str.length(); i++) {
			    if(Character.getType(str.charAt(i)) == 2)
			    	engCount++;
			}
			if(engCount > 0) {
				engCount = engCount / 2;
			}
			endPoint = (endPoint + engCount) < str.length() ? (endPoint + engCount) : endPoint;
			if(str.length() > endPoint)
				str = str.substring(startPoint, endPoint) + tag;
		}

		return str;
	}
    /**
	 * String UnEscape 처리
	 * 
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src
							.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
	
	/**
	 * String Escape 처리
	 * @param src
	 * @return
	 */
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}
}