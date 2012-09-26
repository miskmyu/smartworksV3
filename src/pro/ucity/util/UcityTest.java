package pro.ucity.util;

import pro.ucity.model.Adapter;
import pro.ucity.model.Event;
import pro.ucity.model.System;

public class UcityTest {
	// ************************** 테스트용 데이터
	// ******************************************//
	// ************************** 테스트용 데이터
	// ******************************************//

	private final static String ADAPTER_DATA_ENV_GALE = "eventId00001||201209250921||시설물001||중앙초등학교앞";
	
	public static void startProcess(int process){
		Adapter adapter = null;
		switch(process){
		case System.PROCESS_ENV_WEAHTER:
			adapter = new Adapter(Event.ID_ENV_GALE, Event.COMM_TYPE_OCCURRENCE, ADAPTER_DATA_ENV_GALE);
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

}
