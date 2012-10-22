package pro.ucity.model;

public class Audit {

	public static final int MAX_AUDIT_ID				= 8;
	public static final int ID_SITUATION_OCCURRED		= 0;
	public static final int ID_COMMUNICATION_MW			= 1;
	public static final int ID_PORTAL_OCCURRED			= 2;
	public static final int ID_PORTAL_ACCEPTED			= 3;
	public static final int ID_PORTAL_PROCESSING		= 4;
	public static final int ID_PORTAL_RELEASE			= 5;
	public static final int ID_DEVICE_MW				= 6;
	public static final int ID_SITUATION_DISPLAY		= 7;

	public static final String DEFAULT_AUDIT_ID_STR		= "4";
	
	private static final String[][] TASK_NAMES_BY_AUDIT_ID = {
		new String[]{"기상특보 발생", "기상특보 종료"},
		new String[]{"통신미들웨어 발생", "통신미들웨어 종료"},
		new String[]{"상황발생"},
		new String[]{"상황등록/접수"},
		new String[]{"상황처리"},
		new String[]{"상황종료"},
		new String[]{"단말연계미들웨어"},
		new String[]{"발생 SMS발송", "미디어보드", "교통 BIT", "교통 VMS", "KIOSK", "환경 VMS", "미디어보드 중단", "교통 BIT 중단", "교통 VMS 중단", "KIOSK 중단", "환경 VMS 중단"}
	};
	
	public static String[] getTaskNamesByAuditId(int auditId){
		if(auditId < 0 || auditId >= MAX_AUDIT_ID) return null;
		return TASK_NAMES_BY_AUDIT_ID[auditId];
	};
}
