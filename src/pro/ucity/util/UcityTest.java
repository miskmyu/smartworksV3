package pro.ucity.util;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import pro.ucity.model.Adapter;
import pro.ucity.model.Event;
import pro.ucity.model.Service;
import pro.ucity.model.System;

public class UcityTest {
	// ************************** 테스트용 데이터
	// ******************************************//
	// ************************** 테스트용 데이터
	// ******************************************//

	public final static String ADAPTER_HEADER_ENV_GALE = "123456789012345678901234512345678" + Service.ID_ENVIRONMENT + Event.ID_ENV_GALE + Event.TYPE_OCCURRENCE + "1212345";
	public final static String ADAPTER_BODY_ENV_GALE = "eventId00001||201209250921||시설물001||중앙초등학교앞";
	
	public static void startProcess(int process){
		Adapter adapter = null;
		switch(process){
		case System.PROCESS_ENV_WEAHTER:
			adapter = new Adapter(ADAPTER_HEADER_ENV_GALE, ADAPTER_BODY_ENV_GALE);
			break;
		case System.PROCESS_ENV_ATMOSPHERE:
			break;
		case System.PROCESS_ENV_WATER:
			break;
		case System.PROCESS_TRAFFIC_ILLEGAL_PARKING:
			break;
		case System.PROCESS_TRAFFIC_INCIDENT:
			break;
		case System.PROCESS_DISASTER_FIRE:
			break;
		case System.PROCESS_CRIME_CCTV:
			break;
		case System.PROCESS_CRIME_VEHICLES:
			break;
		case System.PROCESS_WATERWORKS_LEAKS:
			break;
		case System.PROCESS_FACILITY_MANAGEMENT:
			break;
		}
		try{
			adapter.startProcess();
		}catch (Exception e){
		}
	}
	
	public static ResultSet readTable(String tableName, String eventId) throws Exception{
		return null;
	}

	public static Map<String, Object> getCMHistoryDataRecord(){
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		dataRecord.put("트랜잭션 아이디", "transid001");
		dataRecord.put("송수신 구분", "송신");
		dataRecord.put("메시지 아이디", "message001");
		dataRecord.put("시스템 코드", "systemcode001");
		dataRecord.put("데이터 타입", "datatype01");
		dataRecord.put("발생 일시", "201209260910");
		dataRecord.put("성공여부", true);
		dataRecord.put("에러코드", "err001");
		dataRecord.put("요청 응답 키", "key001");
		dataRecord.put("수신 프로토콜 타입", "protocoltype001");
		dataRecord.put("수신 MEP 타입", "rmeptype01");
		dataRecord.put("메시지 종류 구분", "messageTypeGubun");
		dataRecord.put("송신 프로토콜 타입", "sprotocoltype001");
		dataRecord.put("재시도 횟수", "2");
		dataRecord.put("송신 MEP 타입", "smepType");
		dataRecord.put("수신 이벤트 내용", "sdfdfdsfdsfwekrjhwker32048sjdfsf");
		dataRecord.put("이벤트 발생 아이디", "eventOccurreceId");
		return dataRecord;
	}

	public static Map<String, Object> getOPDisplayDataRecord(){
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		dataRecord.put("U서비스 이벤트 아이디", "event0001");
		dataRecord.put("상황표출 아이디", "display001");
		dataRecord.put("상황 아이디", "situation001");
		dataRecord.put("상황표출 내용", "상황표출내용입니다.ㄴㅇㄹㅇ");
		dataRecord.put("상황표출 이미지경로", "이미지경로");
		dataRecord.put("상황표출 동영상경로", "동영상경로");
		dataRecord.put("상황표출시간(초)", "500");
		dataRecord.put("미디어보드표출여부", "Y");
		dataRecord.put("VMS표출여부", "N");
		dataRecord.put("BIT표출여부", "Y");
		dataRecord.put("홈세대기표출여부", "N");
		dataRecord.put("휴대폰표출여부", "Y");
		dataRecord.put("표츌요청일시", "201209272250");
		return dataRecord;
	}

	public static Map<String, Object> getOPSituationDataRecord(){
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		dataRecord.put("U서비스 이벤트 아이디", "situationevent001");
		dataRecord.put("상황 아이디", "event001");
		dataRecord.put("순번", "001");
		dataRecord.put("상태", "발생");
		dataRecord.put("담당자 아이디", "user001");
		dataRecord.put("담당자명", "정윤식");
		dataRecord.put("시작일시", "201209262110");
		dataRecord.put("종료일시", "201209282150");
		dataRecord.put("접수내용", "접수내용입니다.  ㄴㅇ라ㅓㄴ이렁");
		dataRecord.put("처리내용", "처리내용입니다. ㄴ아ㅓㄹㄴ아ㅣ러대쟈겨닝ㄹ");
		return dataRecord;
	}

	public static Map<String, Object> getICSituationDataRecord(){
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		dataRecord.put("키ID", "key001");
		dataRecord.put("이벤트구분ID", "event001");
		dataRecord.put("X좌표", "xlocation");
		dataRecord.put("Y좌표", "ylocation");
		dataRecord.put("Z좌표", "zlocation");
		dataRecord.put("전송시각", "201209280900");
		dataRecord.put("이벤트내용", "이벤트내용입니다");
		dataRecord.put("시설물구분", "시설물구분냉용");
		dataRecord.put("시설물ID", "시설물아이디낸용");
		dataRecord.put("기타정보", "기타정보내용입니다. ㄴㅇㄹㅇ");
		dataRecord.put("처리여부", "Y");
		dataRecord.put("등록일시", "201209302321");
		dataRecord.put("속성", "속성입니다....");
		return dataRecord;
	}
}
