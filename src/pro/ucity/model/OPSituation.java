package pro.ucity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tmax.tibero.jdbc.TbSQLException;

import pro.ucity.util.UcityTest;
import pro.ucity.util.UcityUtil;
import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.work.ProcessWork;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

public class OPSituation {

	public static final String STATUS_SITUATION_OCCURRED = "H01";
	public static final String STATUS_SITUATION_ACCEPTED = "H02";
	public static final String STATUS_SITUATION_PROCESSING = "H03";
	public static final String STATUS_SITUATION_RELEASE = "H04";
	public static final String STATUS_SITUATION_TRANSFER = "H05";
	public static final String STATUS_SITUATION_CANCEL = "H06";
	
	
	public static final String TASK_NAME_SITUATION_OCCURRENCE = "상황발생";
	
	public static final String FIELD_NAME_SITUATION_ID = "SITUATION_ID";
	public static final String FIELD_NAME_SEQUENCE = "SEQ";
	public static final String FIELD_NAME_STATUS = "STATUS";
	public static final String FIELD_NAME_READ_CONFIRM = "BPM_CNFM_YN";
	
	public static final String FIELD_NAME_USERVICE_CODE = "USERVICE_CD";
	public static final String FIELD_NAME_SERVICE_CODE = "UNIT_SVC_CD";
	public static final String FIELD_NAME_EVENT_CODE = "SITTN_EVENT_CD";
	public static final String FIELD_NAME_EVENT_NAME = "SITTN_EVENT_NM";
	public static final String FIELD_NAME_EVENT_DESC = "SITTN_EVENT_DESC";
	
	public static final String SYMBOL_FOR_OP_START = "ST";

	public static final String QUERY_SELECT_FOR_START = "select * from " + System.TABLE_NAME_OPPORTAL_SITUATION + " where (" + FIELD_NAME_READ_CONFIRM + " != 'Y' or " + FIELD_NAME_READ_CONFIRM + " is null)  and " + FIELD_NAME_SITUATION_ID + " like '" + SYMBOL_FOR_OP_START + "%' and " + FIELD_NAME_STATUS + " = '" + STATUS_SITUATION_ACCEPTED + "'";
	public static final String QUERY_UPDATE_FOR_READ_CONFIRM = "update " + System.TABLE_NAME_OPPORTAL_SITUATION + " set " + FIELD_NAME_READ_CONFIRM + " = 'Y' where " + FIELD_NAME_SITUATION_ID + " = ? and " + FIELD_NAME_STATUS + " =  ?";
	public static final String QUERY_SELECT_FOR_PERFORM = "select * from " + System.TABLE_NAME_OPPORTAL_SITUATION + " where " + FIELD_NAME_SITUATION_ID + " = ? and " + FIELD_NAME_STATUS + " = ? and (" + FIELD_NAME_READ_CONFIRM + " != 'Y' or " + FIELD_NAME_READ_CONFIRM + " is null)";
	public static final String QUERY_SELECT_FOR_PROCESSING_PERFORM = "select * from " + System.TABLE_NAME_OPPORTAL_SITUATION + " where " + FIELD_NAME_SITUATION_ID + " = ? and " + FIELD_NAME_STATUS + " = ?";
	public static final String QUERY_SELECT_FOR_PROCESS_PERFORM = "select * from " + System.TABLE_NAME_OPPORTAL_SITUATION + " where " + FIELD_NAME_SITUATION_ID + " = ? and (" + FIELD_NAME_STATUS + " = '" + STATUS_SITUATION_PROCESSING + "' or " + FIELD_NAME_STATUS + " = '" + STATUS_SITUATION_RELEASE + "') and (" + FIELD_NAME_READ_CONFIRM + " != 'Y' or " + FIELD_NAME_READ_CONFIRM + " is null)";
	
	public static final String QUERY_SELECT_EVENT_CODE = "SELECT A.* FROM CMDB.TM_CM_STAT_EVENT A, USITUATION.TH_ST_SITUATION_HISTORY B, USITUATION.TM_ST_SITUATION C WHERE B.SITUATION_ID = C.SITUATION_ID AND C.CATEGORY_ID = A.CATEGORY_ID AND B.SITUATION_ID = ? AND B.SEQ = '1'";
	
	public static final KeyMap[] OPPORTAL_SITUATION_FIELDS = {
		new KeyMap("상황 아이디", "SITUATION_ID"), new KeyMap("순번", "SEQ"), new KeyMap("상태", "STATUS"),
		new KeyMap("담당자 아이디", "CHARGE_USER_ID"), new KeyMap("시작일시", "START_DATE"), new KeyMap("종료일시", "END_DATE"),
		new KeyMap("내용", "CONTENTS"), new KeyMap("담당자명", "CHARGE_USER_NAME")
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
	public OPSituation(ResultSet resultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet)) return;
		this.setResult(resultSet);
	}
	
	public OPSituation(ResultSet resultSet, ResultSet joinResultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet) || SmartUtil.isBlankObject(joinResultSet)) return;
		this.setResult(resultSet, joinResultSet);
	}
	
	public Map<String, Object> getDataRecord(){
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = OPSituation.OPPORTAL_SITUATION_FIELDS;
		
		if(!this.isValid()) return null;

		dataRecord.put("serviceName", this.serviceName);
		dataRecord.put("eventName", this.eventName);
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("SITUATION_ID"))
				dataRecord.put(keyMap.getId(), this.situationId);
			else if(keyMap.getKey().equals("SEQ"))
				dataRecord.put(keyMap.getId(), this.seq);
			else if(keyMap.getKey().equals("STATUS"))
				dataRecord.put(keyMap.getId(), this.getStatusName());
			else if(keyMap.getKey().equals("CHARGE_USER_ID"))
				dataRecord.put(keyMap.getId(), this.chargeUserId);
			else if(keyMap.getKey().equals("CHARGE_USER_NAME"))
				dataRecord.put(keyMap.getId(), this.chargeUserName);
			else if(keyMap.getKey().equals("START_DATE"))
				dataRecord.put(keyMap.getId(), this.startDate);
			else if(keyMap.getKey().equals("END_DATE"))
				dataRecord.put(keyMap.getId(), this.endDate);
			else if(keyMap.getKey().equals("CONTENTS"))
				dataRecord.put(keyMap.getId(), this.contents);
		}
		return dataRecord;
//		return UcityTest.getOPSituationDataRecord();
	}

	private void setJoinResult(ResultSet joinResult){
		if(SmartUtil.isBlankObject(joinResult)) return;
		
		try{
			this.userviceCode = joinResult.getString("USERVICE_CD");
		}catch (Exception e){}
		try{
			this.serviceCode = joinResult.getString(FIELD_NAME_SERVICE_CODE);
		}catch (Exception e){}
		try{
			this.eventCode = joinResult.getString(FIELD_NAME_EVENT_CODE);
		}catch (Exception e){}
		try{
			this.eventName = joinResult.getString(FIELD_NAME_EVENT_NAME);
		}catch (Exception e){}
		try{
			this.eventDesc = joinResult.getString(FIELD_NAME_EVENT_DESC);
		}catch (Exception e){}
		
		this.process = Event.getProcessByEventId(Event.getEventIdByCode(this.userviceCode, this.serviceCode, this.eventCode));
		this.serviceName = Service.getServiceNameByCode(Service.getServiceCodeByUCode(this.userviceCode));
	}
	
	public void startProcess() throws Exception{		
		ProcessWork processWork = (ProcessWork)SwServiceFactory.getInstance().getWorkService().getWorkById(System.getProcessId(this.process));
		if(processWork==null || SmartUtil.isBlankObject(this.serviceName) || SmartUtil.isBlankObject(this.eventName)) return;
		
		UcityUtil.startPortalProcess(System.getProcessId(this.process), this.situationId, this.startDate, this.getDataRecord());
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
			this.situationId = result.getString("SITUATION_ID");
		}catch (Exception ex){}
		try{
			this.seq = result.getString("SEQ");
		}catch (Exception ex){}
		try{
			this.status = result.getString("STATUS");
		}catch (Exception ex){}
		try{
			this.chargeUserId = result.getString("CHARGE_USER_ID");
		}catch (Exception ex){}
		try{
			this.chargeUserName = result.getString("CHARGE_USER_NAME");
		}catch (Exception ex){}
		try{
			this.startDate = result.getString("START_DATE");
		}catch (Exception ex){}
		try{
			this.endDate = result.getString("END_DATE");
		}catch (Exception ex){}
		try{
			this.contents = result.getString("CONTENTS");
		}catch (Exception ex){}
	}
	
	public void setResult(ResultSet result, ResultSet joinResult){
		if(SmartUtil.isBlankObject(result)) return;

		try{
			this.situationId = result.getString("SITUATION_ID");
		}catch (Exception ex){}
		try{
			this.seq = result.getString("SEQ");
		}catch (Exception ex){}
		try{
			this.status = result.getString("STATUS");
		}catch (Exception ex){}
		try{
			this.chargeUserId = result.getString("CHARGE_USER_ID");
		}catch (Exception ex){}
		try{
			this.chargeUserName = result.getString("CHARGE_USER_NAME");
		}catch (Exception ex){}
		try{
			this.startDate = result.getString("START_DATE");
		}catch (Exception ex){}
		try{
			this.endDate = result.getString("END_DATE");
		}catch (Exception ex){}
		try{
			this.contents = result.getString("CONTENTS");
		}catch (Exception ex){}
		
		setJoinResult(joinResult);
	}
	
	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.situationId) && !SmartUtil.isBlankObject(this.seq) && !SmartUtil.isBlankObject(this.status))
			return true;
		return false;
	}

	public static boolean isDisplayableStatus(String status){
		if(SmartUtil.isBlankObject(status)) return false;
		if(status.equals(STATUS_SITUATION_ACCEPTED) || status.equals(STATUS_SITUATION_PROCESSING)) return true;
		return false;
	}
	
	public static void readHistoryTableToStart(){
		java.lang.System.out.println("############ START checking PORTAL History To Start  ################");
		try {
			Class.forName(System.DATABASE_JDBC_DRIVE);
		} catch (ClassNotFoundException e) {
			java.lang.System.out.println("[ERROR] PORTAL 이벤트 데이터베이스 오류 종료");
			e.printStackTrace();
			return;
		}

		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				
		String opSituationSelectSql = OPSituation.QUERY_SELECT_FOR_START;
		String opSituationJoinSelectSql = OPSituation.QUERY_SELECT_EVENT_CODE;
		String opSituationUpdateSql = OPSituation.QUERY_UPDATE_FOR_READ_CONFIRM;
		try {
			
			try{
				con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
			}catch (TbSQLException te){
				java.lang.System.out.println("[ERROR] PORTAL 이벤트 데이터베이스 오류 종료");
				te.printStackTrace();
				java.lang.System.out.println("############ END checking PORTAL History To Start  ################");
				return;
			}
//			con.setAutoCommit(false);
			try{
				selectPstmt = con.prepareStatement(opSituationSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow(); 
				rs.beforeFirst();
				if (count != 0) {
					java.lang.System.out.println("============== PORTAL 이벤트 발생 ===============");
					java.lang.System.out.println("이벤트 발생 시간 : " + new Date());
					java.lang.System.out.println("이벤트 발생 갯수 : " + count);
					while(rs.next()) {
						try{
							String situationId = rs.getString(OPSituation.FIELD_NAME_SITUATION_ID);
							String status = rs.getString(OPSituation.FIELD_NAME_STATUS);
							
							selectPstmt = con.prepareStatement(opSituationJoinSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
							selectPstmt.setString(1, situationId);
							ResultSet joinRs = selectPstmt.executeQuery();
							joinRs.last();
							if(joinRs.getRow()==1){
								joinRs.first();
								updatePstmt = con.prepareStatement(opSituationUpdateSql);
								updatePstmt.setString(1, situationId);
								updatePstmt.setString(2, status);
								boolean result = updatePstmt.execute();
								try{
									OPSituation opSituation = new OPSituation(rs, joinRs);
									opSituation.startProcess();
//									con.commit();
									java.lang.System.out.println("[SUCCESS] 새로운 PORTAL 발생 이벤트(아이디 : '" + situationId + ")가 정상적으로 시작되었습니다!");
								}catch (Exception se){
									java.lang.System.out.println("[ERROR] 새로운 PORTAL 발생 이벤트를 시작하는데 오류가 발생하였습니다!");
									se.printStackTrace();
//									if(con != null)
//										con.rollback();
								}
							}else{
								java.lang.System.out.println("[JOIN ERROR] 새로운 PORTAL 발생 이벤트를 시작하는데 오류가 발생하였습니다!");								
							}
						}catch (Exception we){
							java.lang.System.out.println("[ERROR] PORTAL 이벤트 데이터베이스 오류 종료");
							we.printStackTrace();
							java.lang.System.out.println("############ END checking PORTAL History To Start  ################");
							return;
						}
					}
				}
			}catch (Exception e1){
				java.lang.System.out.println("[ERROR] PORTAL 이벤트 데이터베이스 오류 종료");
				e1.printStackTrace();
			}
		} catch (Exception e) {
			java.lang.System.out.println("[ERROR] PORTAL 이벤트 데이터베이스 오류 종료");
			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if (updatePstmt != null)
					updatePstmt.close();
				if(con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			java.lang.System.out.println("############ END checking PORTAL History To Start  ################");
		}

	}

	public static Map<String,Object> readHistoryTable(String eventId, String status){
		
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(status)) return null;
		try {
			Class.forName(System.DATABASE_JDBC_DRIVE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				
		String opSituationSelectSql = (status.equals(STATUS_SITUATION_PROCESSING)) ?  OPSituation.QUERY_SELECT_FOR_PROCESS_PERFORM : OPSituation.QUERY_SELECT_FOR_PERFORM;
		String opSituationUpdateSql = OPSituation.QUERY_UPDATE_FOR_READ_CONFIRM;
		try {
			try{
				con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
			}catch (TbSQLException te){
				te.printStackTrace();
				return null;
			}
			
			try{
				selectPstmt = con.prepareStatement(opSituationSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				if(!status.equals(STATUS_SITUATION_PROCESSING))
					selectPstmt.setString(2, status);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if (count >= 1) {
					try{
						OPSituation opSituation = new OPSituation(rs);
						if(opSituation.isValid()){
							updatePstmt = con.prepareStatement(opSituationUpdateSql);
							updatePstmt.setString(1, opSituation.getSituationId());
							updatePstmt.setString(2, status);
							boolean result = updatePstmt.execute();
							try {
								if (selectPstmt != null)
									selectPstmt.close();
								con.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return opSituation.getDataRecord();
						}else{
							
						}
					}catch (Exception we){
						we.printStackTrace();
					}
				}
			}catch (Exception e1){
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if(con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
