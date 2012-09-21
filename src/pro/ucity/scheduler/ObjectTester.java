/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 20.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectTester {

	public static void main(String[] args) {
		
		Map mappingMap = new HashMap();
		mappingMap.put("화재", "table1");
		mappingMap.put("도난", "table2");
		
		String arg = "화재";
		
		String dbName = (String)mappingMap.get(arg);
		
		List<Object> list = getResult(dbName);
		
		for (int i = 0; i < list.size(); i++) {
			Object result = list.get(i);
			if (result instanceof Test) {
				System.out.println("TEST1");
				Test test = (Test)result;
				System.out.println(test.getValue());
			} else if (result instanceof Test2) {
				System.out.println("TEST2");
				Test2 test = (Test2)result;
				System.out.println(test.getValue());
			}
		}
	}
	public static List<Object> getResult(String dbName) {
		if (dbName.equalsIgnoreCase("table1")) {
			
			Test test = new Test();
			test.setValue("id");
			List<Object> resultList = new ArrayList<Object>();
			resultList.add(test);
			return resultList;
		} else if (dbName.equalsIgnoreCase("table2")) {

			Test2 test2 = new Test2();
			test2.setValue("id");
			List<Object> resultList = new ArrayList<Object>();
			resultList.add(test2);
			return resultList;
		} 
		return null;
	}
}

class Test {
	private String value = "test1Value";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
class Test2 {
	private String value = "test1Value";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
