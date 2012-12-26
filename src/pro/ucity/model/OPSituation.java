package pro.ucity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.tmax.tibero.jdbc.TbSQLException;

import pro.ucity.util.UcityTest;
import pro.ucity.util.UcityUtil;
import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.work.ProcessWork;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

public class OPSituation {

	private static Logger logger = Logger.getLogger(OPSituation.class);
	
	public static final String STATUS_SITUATION_OCCURRED = "H01";
	public static final String STATUS_SITUATION_ACCEPTED = "H02";
	public static final String STATUS_SITUATION_PROCESSING = "H03";
	public static final String STATUS_SITUATION_RELEASE = "H04";
	public static final String STATUS_SITUATION_TRANSFER = "H05";
	public static final String STATUS_SITUATION_CANCEL = "H06";
	
	public static final String TASK_NAME_SITUATION_OCCURRENCE = "상황등록/접수";
	
	public static final String SYMBOL_FOR_OP_START = "ST";
	
	public static final KeyMap[] OPPORTAL_SITUATION_FIELDS = {
		new KeyMap("상황 아이디", UcityConstant.getQueryByKey("OPSituation.SITUATION_ID")), new KeyMap("순번", UcityConstant.getQueryByKey("OPSituation.SEQ")), new KeyMap("상태", UcityConstant.getQueryByKey("OPSituation.STATUS")),
		new KeyMap("담당자 아이디", UcityConstant.getQueryByKey("OPSituation.CHARGE_USER_ID")), new KeyMap("시작일시", UcityConstant.getQueryByKey("OPSituation.START_DATE")), new KeyMap("종료일시", UcityConstant.getQueryByKey("OPSituation.END_DATE")),
		new KeyMap("내용", UcityConstant.getQueryByKey("OPSituation.CONTENTS")), new KeyMap("담당자명", UcityConstant.getQueryByKey("OPSituation.CHARGE_USER_NAME")), new KeyMap("발생장소명",UcityConstant.getQueryByKey("OPSituation.LC_NM"))
	};

	private int process=-1;
	private int eventType;	
	private String userviceCode;
	private String serviceCode;
	private String eventCode;
	private String serviceName;
	private String eventName;
	private String eventDesc;
	
	private String situationId;
	private String seq;
	private String status;
	private String chargeUserId;
	private String chargeUserName;
	private String startDate;
	private String endDate;
	private String contents;
	private String locationName;
	
	private String occurDate;
		
	public int getProcess() {
		return process;
	}
	public void setProcess(int process) {
		this.process = process;
	}
	public int getEventType() {
		return eventType;
	}
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	public String getUserviceCode() {
		return userviceCode;
	}
	public void setUserviceCode(String userviceCode) {
		this.userviceCode = userviceCode;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public String getSituationId() {
		return situationId;
	}
	public void setSituationId(String situationId) {
		this.situationId = situationId;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName(){
		if(SmartUtil.isBlankObject(this.status)) return "";
		if(this.status.equals(STATUS_SITUATION_OCCURRED)){
			return "발생";
		}else if(this.status.equals(STATUS_SITUATION_ACCEPTED)){
			return "접수";
		}else if(this.status.equals(STATUS_SITUATION_PROCESSING)){
			return "처리";
		}else if(this.status.equals(STATUS_SITUATION_RELEASE)){
			return "종료";
		}else if(this.status.equals(STATUS_SITUATION_CANCEL)){
			return "취소";
		}
		return "기타";
	}
	public String getChargeUserId() {
		return chargeUserId;
	}
	public void setChargeUserId(String chargeUserId) {
		this.chargeUserId = chargeUserId;
	}
	public String getChargeUserName() {
		return chargeUserName;
	}
	public void setChargeUserName(String chargeUserName) {
		this.chargeUserName = chargeUserName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getOccurDate() {
		return occurDate;
	}
	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}
//	public static int getStatusNumber(String status) {
//		if(status.equals(STATUS_SITUATION_OCCURRED)){
//			return 1;
//		}else if(status.equals(STATUS_SITUATION_ACCEPTED)){
//			return 2;
//		}else if(status.equals(STATUS_SITUATION_PROCESSING)){
//			return 3;
//		}else if(status.equals(STATUS_SITUATION_RELEASE)){
//			return 4;
//		}else if(status.equals(STATUS_SITUATION_CANCEL)){
//			return 4;
//		}
//		return 4;
//	}
	public OPSituation(ResultSet resultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet)) return;
		this.setResult(resultSet);
	}

	public OPSituation(ResultSet resultSet, ResultSet joinResultSet, ResultSet joinFacilitySet){
		super();
		if(SmartUtil.isBlankObject(resultSet) || SmartUtil.isBlankObject(joinResultSet)) return;
		this.setResult(resultSet, joinResultSet, joinFacilitySet);
	}
	
	public Map<String, Object> getDataRecord(){
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = OPSituation.OPPORTAL_SITUATION_FIELDS;
		
		if(!this.isValid()) return null;

		dataRecord.put("serviceName", this.serviceName);
		dataRecord.put("eventName", this.eventName);
		if(!SmartUtil.isBlankObject(this.locationName)){
			dataRecord.put("eventPlace", this.locationName);
		}
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSituation.SITUATION_ID")))
				dataRecord.put(keyMap.getId(), this.situationId);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSituation.SEQ")))
				dataRecord.put(keyMap.getId(), this.seq);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSituation.STATUS")))
				dataRecord.put(keyMap.getId(), this.getStatusName());
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSituation.CHARGE_USER_ID")))
				dataRecord.put(keyMap.getId(), this.chargeUserId);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSituation.CHARGE_USER_NAME")))
				dataRecord.put(keyMap.getId(), this.chargeUserName);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSituation.START_DATE")))
				dataRecord.put(keyMap.getId(), UcityUtil.getDateString(this.startDate));
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSituation.END_DATE")))
				dataRecord.put(keyMap.getId(), UcityUtil.getDateString(this.endDate));
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSituation.CONTENTS")))
				dataRecord.put(keyMap.getId(), this.contents);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSituation.LC_NM")))
				dataRecord.put(keyMap.getId(), this.locationName);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSituation.OCCUR_DATE")))
				dataRecord.put(keyMap.getId(), this.occurDate);
		}
		return dataRecord;
	}

	private void setJoinFacility(ResultSet joinFacility){
		if(SmartUtil.isBlankObject(joinFacility)) return;
		
		try{
			this.locationName = joinFacility.getString("LC_NM");
		}catch (Exception e){}
	}
	
	private void setJoinResult(ResultSet joinResult){
		if(SmartUtil.isBlankObject(joinResult)) return;
		
		try{
			this.userviceCode = joinResult.getString(UcityConstant.getQueryByKey("OPSituation.USERVICE_CD"));
		}catch (Exception e){}
		try{
			this.serviceCode = joinResult.getString(UcityConstant.getQueryByKey("OPSituation.FIELD_NAME_SERVICE_CODE"));
		}catch (Exception e){}
		try{
			this.eventCode = joinResult.getString(UcityConstant.getQueryByKey("OPSituation.FIELD_NAME_EVENT_CODE"));
		}catch (Exception e){}
		try{
			this.eventName = joinResult.getString(UcityConstant.getQueryByKey("OPSituation.FIELD_NAME_EVENT_NAME"));
		}catch (Exception e){}
		try{
			this.eventDesc = joinResult.getString(UcityConstant.getQueryByKey("OPSituation.FIELD_NAME_EVENT_DESC"));
		}catch (Exception e){}
		try{
			this.occurDate = joinResult.getString(UcityConstant.getQueryByKey("OPSituation.OCCUR_DATE"));
			if(!SmartUtil.isBlankObject(this.occurDate)){
				this.occurDate = this.occurDate.replaceAll("-", "");
				this.occurDate = this.occurDate.replaceAll(" ", "");
				this.occurDate = this.occurDate.replaceAll(":", "");
				this.occurDate = this.occurDate.replace(".0", "");
			}
		}catch (Exception ex){}
		
		this.process = Event.getProcessByEventId(Event.getEventIdByCode(this.userviceCode, this.serviceCode, this.eventCode));
		this.serviceName = Service.getServiceNameByCode(Service.getServiceCodeByUCode(this.userviceCode));
	}
	
	public void startProcess() throws Exception{		
		ProcessWork processWork = (ProcessWork)SwServiceFactory.getInstance().getWorkService().getWorkById(System.getProcessId(this.process));
		if(processWork==null || SmartUtil.isBlankObject(this.serviceName) || SmartUtil.isBlankObject(this.eventName)) return;
		if(UcityUtil.ucityWorklistSearch(System.getProcessId(this.process),this.situationId) == true ){
		UcityUtil.startPortalProcess(System.getProcessId(this.process), this.situationId, this.occurDate, this.getDataRecord());
		}else{
			logger.info("[PASS] PORTAL 발생 이벤트(아이디 : '" + situationId + ")가 중복실행으로 인해, 중지되었습니다.");
//			java.lang.System.out.println("[PASS] PORTAL 발생 이벤트(아이디 : '" + situationId + ")가 중복실행으로 인해, 중지되었습니다.");
			return;
		}
	}
	
	public void performTask(String processId, String taskInstId) throws Exception{
		TaskInstance taskInstance = null;
		if(SmartUtil.isBlankObject(taskInstId)) return;
		
		taskInstance = SwServiceFactory.getInstance().getInstanceService().getTaskInstanceById(processId, taskInstId);
		
		if(SmartUtil.isBlankObject(taskInstance)) return;
		
		UcityUtil.performUServiceTask(taskInstance, this.getDataRecord());		
	}
	
	public void setResult(ResultSet result){
		if(SmartUtil.isBlankObject(result)) return; 
		try{
			this.situationId = result.getString(UcityConstant.getQueryByKey("OPSituation.SITUATION_ID"));
		}catch (Exception ex){}
		try{
			this.seq = result.getString(UcityConstant.getQueryByKey("OPSituation.SEQ"));
		}catch (Exception ex){}
		try{
			this.status = result.getString(UcityConstant.getQueryByKey("OPSituation.STATUS"));
		}catch (Exception ex){}
		try{
			this.chargeUserId = result.getString(UcityConstant.getQueryByKey("OPSituation.CHARGE_USER_ID"));
		}catch (Exception ex){}
		try{
			this.chargeUserName = result.getString(UcityConstant.getQueryByKey("OPSituation.CHARGE_USER_NAME"));
		}catch (Exception ex){}
		try{
			this.startDate = result.getString(UcityConstant.getQueryByKey("OPSituation.START_DATE"));
			if(!SmartUtil.isBlankObject(this.startDate)){
				this.startDate = this.startDate.replaceAll("-", "");
				this.startDate = this.startDate.replaceAll(" ", "");
				this.startDate = this.startDate.replaceAll(":", "");
				this.startDate = this.startDate.replace(".0", "");
			}
		}catch (Exception ex){}
		try{
			this.endDate = result.getString(UcityConstant.getQueryByKey("OPSituation.END_DATE"));
			if(!SmartUtil.isBlankObject(this.endDate)){
				this.endDate = this.endDate.replaceAll("-", "");
				this.endDate = this.endDate.replaceAll(" ", "");
				this.endDate = this.endDate.replaceAll(":", "");
				this.endDate = this.endDate.replace(".0", "");
			}
		}catch (Exception ex){}
		try{
			this.contents = result.getString(UcityConstant.getQueryByKey("OPSituation.CONTENTS"));
		}catch (Exception ex){}
		try{
			this.locationName = result.getString(UcityConstant.getQueryByKey("OPSituation.LC_NM"));
		}catch (Exception ex){}
	}
	
	public void setResult(ResultSet result, ResultSet joinResult, ResultSet joinFacilitySet){
		if(SmartUtil.isBlankObject(result)) return;

		try{
			this.situationId = result.getString(UcityConstant.getQueryByKey("OPSituation.SITUATION_ID"));
		}catch (Exception ex){}
		try{
			this.seq = result.getString(UcityConstant.getQueryByKey("OPSituation.SEQ"));
		}catch (Exception ex){}
		try{
			this.status = result.getString(UcityConstant.getQueryByKey("OPSituation.STATUS"));
		}catch (Exception ex){}
		try{
			this.chargeUserId = result.getString(UcityConstant.getQueryByKey("OPSituation.CHARGE_USER_ID"));
		}catch (Exception ex){}
		try{
			this.chargeUserName = result.getString(UcityConstant.getQueryByKey("OPSituation.CHARGE_USER_NAME"));
		}catch (Exception ex){}
		try{
			this.startDate = result.getString(UcityConstant.getQueryByKey("OPSituation.START_DATE"));
			if(!SmartUtil.isBlankObject(this.startDate)){
				this.startDate = this.startDate.replaceAll("-", "");
				this.startDate = this.startDate.replaceAll(" ", "");
				this.startDate = this.startDate.replaceAll(":", "");
				this.startDate = this.startDate.replace(".0", "");
			}
		}catch (Exception ex){}
		try{
			this.endDate = result.getString(UcityConstant.getQueryByKey("OPSituation.END_DATE"));
			if(!SmartUtil.isBlankObject(this.endDate)){
				this.endDate = this.endDate.replaceAll("-", "");
				this.endDate = this.endDate.replaceAll(" ", "");
				this.endDate = this.endDate.replaceAll(":", "");
				this.endDate = this.endDate.replace(".0", "");
			}
		}catch (Exception ex){}
		try{
			this.contents = result.getString(UcityConstant.getQueryByKey("OPSituation.CONTENTS"));
		}catch (Exception ex){}
		try{
			this.locationName = result.getString(UcityConstant.getQueryByKey("OPSituation.LC_NM"));
		}catch (Exception ex){}

		
		setJoinResult(joinResult);
		setJoinFacility(joinFacilitySet);
	}
	
	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.situationId) && !SmartUtil.isBlankObject(this.seq) && !SmartUtil.isBlankObject(this.status))
			return true;
		return false;
	}

	public static boolean isDisplayableStatus(String status){
		if(SmartUtil.isBlankObject(status)) return false;
		if(status.equals(STATUS_SITUATION_PROCESSING)) return true;
		return false;
	}
	
	public static void readHistoryTableToStart(){
//		if(SmartUtil.isBlankObject(connection)) return;
		
		logger.info("############ START checking PORTAL History To Start  ################");

		Connection connection = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
		int number = 1;
				
		String opSituationSelectSql = UcityConstant.getQueryByKey("OPSituation.QUERY_SELECT_FOR_START");
		String opSituationJoinSelectSql = UcityConstant.getQueryByKey("OPSituation.QUERY_SELECT_EVENT_CODE");
		String opSituationJoinFacilitySql = UcityConstant.getQueryByKey("OPSituation.QUERY_SELECT_FOR_FACILITY");
		String opSituationUpdateSql = UcityConstant.getQueryByKey("OPSituation.QUERY_UPDATE_FOR_READ_CONFIRM");
		String opSituationRollbackSql = UcityConstant.getQueryByKey("OPSituation.QUERY_UPDATE_FOR_READ_ROLLBACK");
		try {
			
			try{
			    Context init = new InitialContext();
			    Context envinit = (Context)init.lookup("java:comp/env");
			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
			    connection = ds.getConnection();
//				connection = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
			}catch (TbSQLException te){
				logger.error("DB Connection error : OPSituation.readHistoryTableToStart.449");
//				java.lang.System.out.println("[ERROR] PORTAL 이벤트 데이터베이스 오류 종료");
//				te.printStackTrace();
//				java.lang.System.out.println("############ END checking PORTAL History To Start  ################");
				return;
			}
//			connection.setAutoCommit(false);			
			try{
				selectPstmt = connection.prepareStatement(opSituationSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow(); 
				rs.beforeFirst();
				if (count != 0) {
					int processedCount = 0;
					logger.info("============== PORTAL 이벤트 발생 ===============");
					logger.info("이벤트 발생 시간 : " + new Date());
					logger.info("이벤트 발생 갯수 : " + count);
					while(rs.next() && number == 1 && !Thread.currentThread().isInterrupted()) {
						try{
							String situationId = rs.getString(UcityConstant.getQueryByKey("OPSituation.FIELD_NAME_SITUATION_ID"));
							String status = rs.getString(UcityConstant.getQueryByKey("OPSituation.FIELD_NAME_STATUS"));

							selectPstmt = connection.prepareStatement(opSituationJoinFacilitySql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
							selectPstmt.setString(1, situationId);
							ResultSet joinFacilityRs = selectPstmt.executeQuery();
							joinFacilityRs.first();
							
							selectPstmt = connection.prepareStatement(opSituationJoinSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
							selectPstmt.setString(1, situationId);
							ResultSet joinRs = selectPstmt.executeQuery();
							joinRs.last();
							if(joinRs.getRow()==1){
								joinRs.first();
								updatePstmt = connection.prepareStatement(opSituationUpdateSql);
								updatePstmt.setString(1, situationId);
								updatePstmt.setString(2, status);
								boolean result = updatePstmt.execute();
								try{
									
									OPSituation opSituation = new OPSituation(rs, joinRs, joinFacilityRs);
									opSituation.startProcess();
//									connection.commit();
									logger.info("[SUCCESS] 새로운 PORTAL 발생 이벤트(아이디 : '" + situationId + ")가 정상적으로 시작되었습니다!");
								}catch (Exception se){
//									java.lang.System.out.println("[ERROR] 새로운 PORTAL 발생 이벤트를 시작하는데 오류가 발생하였습니다!");
									logger.error("startProcess error : opSituation.startProcess.495");
//									if(connection != null)
//										connection.rollback();
//									updatePstmt = connection.prepareStatement(opSituationRollbackSql);
//									updatePstmt.setString(1, situationId);
//									updatePstmt.setString(2, status);
//									result = updatePstmt.execute();
								}
								selectPstmt = connection.prepareStatement(opSituationJoinFacilitySql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
								selectPstmt.setString(1, situationId);
								joinFacilityRs = selectPstmt.executeQuery();
								joinFacilityRs.first();
								
								selectPstmt = connection.prepareStatement(opSituationJoinSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
								selectPstmt.setString(1, situationId);
								joinRs = selectPstmt.executeQuery();
								joinRs.last();
								if(joinRs.getRow()!=1){
									number = 0;
								}else{
									logger.info("============== PORTAL 이벤트 발생(while) ===============");
									logger.info("이벤트 발생 시간 : " + new Date());
									logger.info("이벤트 발생 갯수 : " + count);
								}
							}else{
								logger.info("[JOIN ERROR] 새로운 PORTAL 발생 이벤트를 시작하는데 오류가 발생하였습니다!");								
//								updatePstmt = connection.prepareStatement(opSituationRollbackSql);
//								updatePstmt.setString(1, situationId);
//								updatePstmt.setString(2, status);
//								boolean result = updatePstmt.execute();
							}
						}catch (Exception we){
//							java.lang.System.out.println("[ERROR] PORTAL 이벤트 데이터베이스 오류 종료");
							logger.error("startProcess error : opSituation.startProcess.528");
//							java.lang.System.out.println("############ END checking PORTAL History To Start  ################");
							return;
						}
					}
				}
			}catch (Exception e1){
//				java.lang.System.out.println("[ERROR] PORTAL 이벤트 데이터베이스 오류 종료");
//				e1.printStackTrace();
				logger.error("readHistoryTableToStart error : OPSituation.537");
			}
		} catch (Exception e) {
//			java.lang.System.out.println("[ERROR] PORTAL 이벤트 데이터베이스 오류 종료");
//			e.printStackTrace();
			logger.error("readHistoryTableToStart error : opSituation.startProcess.542");
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if (updatePstmt != null)
					updatePstmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				logger.error("connection close error : opSituation.readHistoryTableToStart.554");
			}
			logger.info("############ END checking PORTAL History To Start  ################");
		}

	}

	public static Map<String,Object> readHistoryTable(String eventId, String status){
		
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(status)) return null;

		Connection connection = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				

//		String opSituationSelectSql = (status.equals(STATUS_SITUATION_PROCESSING)) ?  OPSituation.QUERY_SELECT_FOR_PROCESS_PERFORM : OPSituation.QUERY_SELECT_FOR_PERFORM;
//		String opSituationUpdateSql = OPSituation.QUERY_UPDATE_FOR_READ_CONFIRM;
		
//		String opSituationSelectSql = (status.equals(STATUS_SITUATION_PROCESSING)) ? UcityConstant.getQueryByKey("OPSituation.QUERY_SELECT_FOR_PROCESS_PERFORM") : ((status.equals(STATUS_SITUATION_OCCURRED)) ? UcityConstant.getQueryByKey("OPSituation.QUERY_SELECT_FOR_OCCURRED") : UcityConstant.getQueryByKey("OPSituation.QUERY_SELECT_FOR_PERFORM"));
//		status.equals(STATUS_SITUATION_PROCESSING) ? UcityConstant.getQueryByKey("OPSituation.QUERY_SELECT_FOR_PROCESS_PERFORM") :
		String opSituationSelectSql = UcityConstant.getQueryByKey("OPSituation.QUERY_SELECT_PERFORM");
		String opSituationUpdateSql = UcityConstant.getQueryByKey("OPSituation.QUERY_UPDATE_FOR_READ_CONFIRM");
		
		try {
			try{
			    Context init = new InitialContext();
			    Context envinit = (Context)init.lookup("java:comp/env");
			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
			    connection = ds.getConnection();
//				connection = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
			}catch (TbSQLException te){
//				te.printStackTrace()
//				return null;
				logger.error("DB Connection error : OPSituation.readHistoryTable.588");
			}
			connection.setAutoCommit(false);
			try{
				selectPstmt = connection.prepareStatement(opSituationSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
//				if(!status.equals(STATUS_SITUATION_PROCESSING) && !status.equals(STATUS_SITUATION_OCCURRED))
//					selectPstmt.setString(2, status);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if (count >= 1) {
					try{
						String statusId = rs.getString(UcityConstant.getQueryByKey("OPSituation.FIELD_NAME_STATUS"));
						OPSituation opSituation = new OPSituation(rs);
						if(opSituation.isValid() && status.equals(statusId) || (status.equals(STATUS_SITUATION_RELEASE) && statusId.equals(STATUS_SITUATION_CANCEL))){
							updatePstmt = connection.prepareStatement(opSituationUpdateSql);
							updatePstmt.setString(1, opSituation.getSituationId());
							updatePstmt.setString(2, status);
							boolean result = updatePstmt.execute();
							try {
								if (selectPstmt != null)
									selectPstmt.close();
								    connection.commit();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								logger.error("select error : OPSituation.readHistoryTable.616");
								if(connection != null)
									connection.rollback();								
							}
							return opSituation.getDataRecord();
						}else{
							return opSituation.getDataRecord();
						}
					}catch (Exception we){
						logger.error("select error : OPSituation.readHistoryTable.625");
					}
				}
			}catch (Exception e1){
				logger.error("select error : OPSituation.readHistoryTable.629");
			}
		} catch (Exception e) {
			logger.error("select error : OPSituation.readHistoryTable.632");
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if (updatePstmt != null)
					updatePstmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				logger.error("Connection Close error : OPSituation.readHistoryTable.644");
			}
		}

		Map<String, Object> dataRecord = new HashMap<String, Object>();
		if(OPDisplay.checkIfDisplay(eventId, false) || OPDisplay.checkIfDisplay(eventId, true) || OPSms.checkIfDisplay(eventId)){
			return dataRecord;
		}

		return null;
	}
}
