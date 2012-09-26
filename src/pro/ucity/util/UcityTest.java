package pro.ucity.util;

import java.sql.ResultSet;

import pro.ucity.model.Adapter;
import pro.ucity.model.Event;
import pro.ucity.model.Service;
import pro.ucity.model.System;

public class UcityTest {
	// ************************** 테스트용 데이터
	// ******************************************//
	// ************************** 테스트용 데이터
	// ******************************************//

	public final static String ADAPTER_HEADER_ENV_GALE = "123456789012345678901234512345678" + Service.ID_ENVIRONMENT + Event.ID_ENV_GALE + Adapter.EVENT_TYPE_OCCURRENCE + "1212345";
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

}
