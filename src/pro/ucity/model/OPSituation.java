package pro.ucity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	
	public static final String SYMBOL_FOR_OP_START = "ST";

	public static final String QUERY_SELECT_FOR_START = "select * from " + System.TABLE_NAME_OPPORTAL_SITUATION + " where (" + FIELD_NAME_READ_CONFIRM + " != 'Y' or " + FIELD_NAME_READ_CONFIRM + " is null)  and " + FIELD_NAME_SITUATION_ID + " like '" + SYMBOL_FOR_OP_START + "%' and " + FIELD_NAME_STATUS + " = '" + STATUS_SITUATION_OCCURRED + "'";
	public static final String QUERY_UPDATE_FOR_READ_CONFIRM = "update " + System.TABLE_NAME_OPPORTAL_SITUATION + " set " + FIELD_NAME_READ_CONFIRM + " = 'Y' where " + FIELD_NAME_SITUATION_ID + " = ? and " + FIELD_NAME_STATUS + " = '" + STATUS_SITUATION_OCCURRED + "'";
	public static final String QUERY_SELECT_FOR_PERFORM = "select * from " + System.TABLE_NAME_OPPORTAL_SITUATION + " where " + FIELD_NAME_SITUATION_ID + " = ? and " + FIELD_NAME_STATUS + " = ?";
	
	public static final String QUERY_SELECT_EVENT_CODE = "SELECT A.SITTN_EVENT_NM FROM CMDB.TM_CM_STAT_EVENT A, USITUATION.TH_ST_SITUATION_HISTORY B, USITUATION.TM_ST_SITUATION C WHERE B.SITUATION_ID = C.SITUATION_ID AND C.CATEGORY_ID = A.CATEGORY_ID AND B.SITUATION_ID = ''";
	
	public static final KeyMap[] OPPORTAL_SITUATION_FIELDS = {
		new KeyMap("상황 아이디", "SITUATION_ID"), new KeyMap("순번", "SEQ"), new KeyMap("상태", "STATUS"),
		new KeyMap("담당자 아이디", "CHARGE_USER_ID"), new KeyMap("시작일시", "START_DATE"), new KeyMap("종료일시", "END_DATE"),
		new KeyMap("내용", "CONTENTS"), new KeyMap("담당자명", "CHARGE_USER_NAME")
	};

	private int process=-1;
	private int eventType;	
	private String serviceCode;
	private String eventCode;

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
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("SITUATION_ID"))
				dataRecord.put(keyMap.getId(), this.situationId);
			else if(keyMap.getKey().equals("SEQ"))
				dataRecord.put(keyMap.getId(), this.seq);
			else if(keyMap.getKey().equals("STATUS"))
				dataRecord.put(keyMap.getId(), this.status);
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
	
	public void startProcess() throws Exception{		
		ProcessWork processWork = (ProcessWork)SwServiceFactory.getInstance().getWorkService().getWorkById(System.getProcessId(this.process));
		if(processWork==null) return;
		
		UcityUtil.startUServiceProcess(System.getProcessId(this.process), TASK_NAME_SITUATION_OCCURRENCE,  this.getDataRecord());
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
		try {
			Class.forName(System.DATABASE_JDBC_DRIVE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				
		String opSituationSelectSql = OPSituation.QUERY_SELECT_FOR_START;
		String opSituationJoinSelectSql = OPSituation.QUERY_SELECT_EVENT_CODE;
		String opSituationUpdateSql = OPSituation.QUERY_UPDATE_FOR_READ_CONFIRM;
		try {
			
			con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
//			con.setAutoCommit(false);
			
			try{
				selectPstmt = con.prepareStatement(opSituationSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow(); 
				rs.beforeFirst();
				if (count != 0) {
					java.lang.System.out.println("############ 이벤트 발생 ################");
					java.lang.System.out.println("이벤트 발생 시간 : " + new Date());
					java.lang.System.out.println("조회 데이터 수 : " + rs.getRow());
					java.lang.System.out.println("------------ 데이터 처리 ----------------");					
					while(rs.next()) {
						try{
							String situationId = rs.getString(OPSituation.FIELD_NAME_SITUATION_ID);
							selectPstmt = con.prepareStatement(opSituationJoinSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
							ResultSet joinRs = selectPstmt.executeQuery();				
							updatePstmt = con.prepareStatement(opSituationUpdateSql);
							updatePstmt.setString(1, situationId);
							boolean result = updatePstmt.execute();
							try{
								UcityUtil.startPortalService(rs, joinRs);
//								con.commit();
							}catch (Exception se){
								se.printStackTrace();
//								con.rollback();
							}
							java.lang.System.out.println("ID : '" + situationId + "' UPDATE STATUS COMPLETE!");
						}catch (Exception we){
							we.printStackTrace();
						}
					}
					java.lang.System.out.println("############ 이벤트 처리 완료 ################");
				}
			}catch (Exception e1){
				e1.printStackTrace();
			}
		} catch (Exception e) {
			java.lang.System.out.println("UPDATE FAIL!!!!!!!!!!!!!!!!!!!!!!!!");
			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if (updatePstmt != null)
					updatePstmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				
		String opSituationSelectSql = OPSituation.QUERY_SELECT_FOR_PERFORM;
		try {
			
			con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
			
			try{
				selectPstmt = con.prepareStatement(opSituationSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				selectPstmt.setString(2, status);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if (count == 1) {
					try{
						OPSituation opSituation = new OPSituation(rs);
						if(opSituation.isValid()){
							try {
								if (selectPstmt != null)
									selectPstmt.close();
								con.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return opSituation.getDataRecord();
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
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
