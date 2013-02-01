package pro.ucity.model;

import java.util.HashMap;
import java.util.Map;

public class Audit {

	public static final int ID_SITUATION_OCCURRED		= 0;
	public static final int ID_COMMUNICATION_MW			= 1;
	public static final int ID_PORTAL_OCCURRED			= 2;
	public static final int ID_PORTAL_ACCEPTED			= 3;
	public static final int ID_PORTAL_PROCESSING		= 4;
	public static final int ID_PORTAL_RELEASE			= 5;
	public static final int ID_INTEGRATED_CONTROL		= 6;
	public static final int ID_DEVICE_MW				= 7;
	public static final int ID_SITUATION_DISPLAY		= 8;

	public static final String DEFAULT_AUDIT_ID_STR		= "4";
	
	private static final String[][] TASK_NAMES_BY_AUDIT_ID = {
		new String[]{"상황발생"},
		new String[]{"통신 미들웨어(발생)", "통신 미들웨어(종료)"},
		new String[]{"기상특보 발생", "기상특보 종료", "대기특보 발생", "대기특보 종료", "수질특보 발생", "수질특보 종료", "불법주정차 발생", "불법주정차 종료", "돌발상황 발생", "돌발상황 종료", 
				"방범 CCTV 발생", "방범 CCTV 종료", "용의차량추적상황 발생", "용의차량추적상황 종료", "상수도누수 발생", "상수도누수 종료", "시설물관리 발생", "시설물관리 종료", "화재특보 발생", "화재특보 종료"},
		new String[]{"상황등록/접수"},
		new String[]{"상황처리"},
		new String[]{"상황종료"},
		new String[]{"통합관제 발생표출", "통합관제 종료표출"},
		new String[]{"표출 이력", "단말연계 미들웨어"},
		new String[]{"발생 SMS발송", "미디어보드", "교통 BIT", "교통 VMS", "KIOSK", "환경 VMS", "미디어보드 중단", "교통 BIT 중단", "교통 VMS 중단", "KIOSK 중단", "환경 VMS 중단"}
	};

	public static final int MAX_AUDIT_ID				= TASK_NAMES_BY_AUDIT_ID.length;
	
	public static String[] getTaskNamesByAuditId(int auditId){
		if(auditId < 0 || auditId >= MAX_AUDIT_ID) return null;
		return TASK_NAMES_BY_AUDIT_ID[auditId];
	};
	
	public static String getAuditNameById(int auditId){
		switch(auditId){
		case ID_SITUATION_OCCURRED:
			return "상황발생";
		case ID_COMMUNICATION_MW:
			return "통신미들웨어";
		case ID_PORTAL_OCCURRED:
			return "운영포털 - 발생";
		case ID_PORTAL_ACCEPTED:
			return "운영포털 - 접수";
		case ID_PORTAL_PROCESSING:
			return "운영포털 - 처리";
		case ID_PORTAL_RELEASE:
			return "운영포털 - 종료";
		case ID_INTEGRATED_CONTROL:
			return "통합관제";
		case ID_DEVICE_MW:
			return "단말연계 미들웨어";
		case ID_SITUATION_DISPLAY:
			return "상황표출";
		}
		return "";
	}
	
	public static Map<String, Integer> getAuditNameIdMappingMap() {
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		for (int i = 0; i < TASK_NAMES_BY_AUDIT_ID.length; i++) {
			String[] subArray = getTaskNamesByAuditId(i);
			for (int j = 0; j < subArray.length; j++) {
				String name = subArray[j];
				if (resultMap.get(name) == null) {
					resultMap.put(name, i);
				}
			}
		}
		return resultMap;
	}
	
}
