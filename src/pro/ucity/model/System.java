package pro.ucity.model;

import net.smartworks.model.KeyMap;

public class System {

	public static final int MAX_PROCESS						= 10;
	public static final int PROCESS_ENV_WEAHTER				= 0;
	public static final int PROCESS_ENV_ATMOSPHERE			= 1;
	public static final int PROCESS_ENV_WATER				= 2;
	public static final int PROCESS_TRAFFIC_ILLEGAL_PARKING	= 3;
	public static final int PROCESS_TRAFFIC_INCIDENT		= 4;
	public static final int PROCESS_DISASTER_FIRE			= 5;
	public static final int PROCESS_CRIME_CCTV				= 6;
	public static final int PROCESS_CRIME_VEHICLES			= 7;
	public static final int PROCESS_WATERWORKS_LEAKS		= 8;
	public static final int PROCESS_FACILITY_MANAGEMENT		= 9;
	
	
	public static final int ID_OPERATION_PORTAL 		= 1;
	public static final int ID_INTEGRATED_CONTROL 		= 2;
	public static final int ID_COMMUNICATION_MIDDLEWARE = 3;
	public static final int ID_DEVICE_MIDDLEWARE 		= 4;
	public static final int ID_ADAPTER 					= 5;
	
	public static final int TABLE_ID_OPPORTAL_SITUATION 	= 11;
	public static final int TABLE_ID_OPPORTAL_DISPLAY 		= 12;
	public static final int TABLE_ID_INTCON_SITUATION 		= 21;
	public static final int TABLE_ID_COMMID_TRACE 			= 31;
	public static final int TABLE_ID_DEVMID_SEND_STATUS 	= 41;
	public static final int TABLE_ID_DEVMID_DEVICE_STATUS 	= 42;
	public static final int TABLE_ID_ADAPTER_HISTORY		= 51;	
	
	public static final String TABLE_NAME_OPPORTAL_SITUATION 	= "TH_ST_SITUATION_HISTORY ";
	public static final String TABLE_NAME_OPPORTAL_DISPLAY 		= "TN_ST_DISPLAY ";
	public static final String TABLE_NAME_INTCON_SITUATION 		= "US1.TN_CTL_EVENT_INFO";
	public static final String TABLE_NAME_COMMID_TRACE 			= "TB_COM_INTG_LOG";
	public static final String TABLE_NAME_DEVMID_SEND_STATUS 	= "TN_IDM_SEND_STATUS";
	public static final String TABLE_NAME_DEVMID_DEVICE_STATUS 	= "TN_IDM_DEVICE_STATUS ";
	public static final String TABLE_NAME_ADAPTER_HISTORY		= "TB_UAST_CMNC_HS";
	
	public static final KeyMap[][] ADAPTER_HISTORY_FIELDS = {
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")}		
	};
	
	public static final KeyMap[] COMMID_TRACE_FIELDS = {
		new KeyMap("트랜잭션 아이디", "TRST_ID"), new KeyMap("송수신 구분", "SR_FLAG "), new KeyMap("메시지 아이디", "MSG_ID "), new KeyMap("시스템 코드", "SYS_CD "),
		new KeyMap("데이터 타입", "DATA_TYP "), new KeyMap("발생 일시", "OUTB_DTM "), new KeyMap("성공여부", "SCSS_YN "), new KeyMap("에러 코드", "ERR_CD "),
		new KeyMap("요청 응답 키", "RR_KEY "), new KeyMap("수신 프로토콜 타입", "R_PRTCL_TYP "), new KeyMap("수신 MEP 타입", "R_MEP "), new KeyMap("메시지 종류 구분", "MSG_KND_GUBN "),
		new KeyMap("송신 프로토콜 타입", "S_PRTCL_TYP "), new KeyMap("재시도 횟수", "RTR_CNT "), new KeyMap("송신 MEP 타입", "S_MEP "), new KeyMap("수신 이벤트 내용", "RCV_EVET_CNTN "),
		new KeyMap("이벤트 발생 아이디", "EVET_OUTB_ID ")
	};
	
	public static final KeyMap[] OPPORTAL_SITUATION_FIELDS = {
		new KeyMap("U서비스 이벤트 아이디", "SITUATION_ID "), new KeyMap("상황 아이디", "SITUATION_ID   "), new KeyMap("순번", "SEQ   "), new KeyMap("상태", "STATUS   "),
		new KeyMap("담당자 아이디", "CHARGE_USER_ID   "), new KeyMap("담당자명", "CHARGE_USER_NAME  "), new KeyMap("시작일시", "START_DATE  "), new KeyMap("종료일시", "END_DATE  "),
		new KeyMap("접수내용", "RECEIVE_CONTENT  "), new KeyMap("처리내용", "HANDLING_CONTENT  ")
	};
	
	public static final KeyMap[] OPPORTAL_DISPLAY_FIELDS = {
		new KeyMap("U서비스 이벤트 아이디", "SITUATION_ID "), new KeyMap("상황표출 아이디", "DISPLAY_ID    "), new KeyMap("상황 아이디", "SITUATION_ID    "), new KeyMap("상황표출 내용", "DISPLAY_CONTENT    "),
		new KeyMap("상황표출 이미지경로", "DISPLAY_IMAGE_PATH    "), new KeyMap("상황표출 동영상경로", "DISPLAY_MOVIE_PATH   "), new KeyMap("상황표출시간(초)", "DISPLAY_SECOND   "), new KeyMap("미디어보드표출여부", "MB_YN   "),
		new KeyMap("VMS표출여부", "VMS_YN   "), new KeyMap("BIT표출여부", "BIT_YN   "), new KeyMap("홈세대기표출여부", "WP_YN   "), new KeyMap("휴대폰표출여부", "MP_YN   "),
		new KeyMap("표츌요청일시", "REQUEST_DATE   ")
	};
	
	public static final KeyMap[] INTCON_SITUATION_FIELDS = {
		new KeyMap("키ID", "ID "), new KeyMap("이벤트구분ID", "EVENT_ID    "), new KeyMap("X좌표", "POS_X    "), new KeyMap("Y좌표", "POS_Y    "),
		new KeyMap("Z좌표", "POS_Z    "), new KeyMap("전송시각", "SEND_TIME   "), new KeyMap("이벤트내용", "EVENT_CONTENT   "), new KeyMap("시설물구분", "SISUL_DIV   "),
		new KeyMap("시설물ID", "SISUL_ID   "), new KeyMap("기타정보", "ETC   "), new KeyMap("처리여부", "DONE_YN   "), new KeyMap("등록일시", "CREATE_DATE   "),
		new KeyMap("속성", "ATTR1   ")
	};
	
	
}
