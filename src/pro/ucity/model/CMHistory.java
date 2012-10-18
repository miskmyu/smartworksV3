package pro.ucity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.tmax.tibero.jdbc.TbSQLException;

import pro.ucity.util.UcityTest;
import pro.ucity.util.UcityUtil;
import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

public class CMHistory {

	//DEV
	public static final String FIELD_NAME_COMMID_EVENT_ID = "EVET_OUTB_ID";
	public static final String FIELD_NAME_COMMID_MSG_TYPE = "MSG_KND_GUBN";

	//개발
//	public static final String FIELD_NAME_COMMID_EVENT_ID = "b.U_SVC_EVENT_ID";
//	public static final String FIELD_NAME_COMMID_OUTB_ID = "a.EVET_OUTB_ID";
//	public static final String FIELD_NAME_COMMID_EOUTB_ID = "b.EVENT_OUTB_ID";
//	public static final String FIELD_NAME_COMMID_MSG_TYPE = "SR_FLAG";
	
	//DEV
	public static final String MSG_TYPE_OCCURRENCE = "O";
	public static final String MSG_TYPE_RELEASE = "R";
	//개발
//	public static final String MSG_TYPE_OCCURRENCE = "S";
//	public static final String MSG_TYPE_RELEASE = "R";
	
	//DEV
	public static final String QUERY_SELECT_FOR_OCCURRENCE_PERFORM = "select * from " + System.TABLE_NAME_COMMID_TRACE + " where " + FIELD_NAME_COMMID_EVENT_ID + " = ? and " + FIELD_NAME_COMMID_MSG_TYPE + " = '" + MSG_TYPE_OCCURRENCE + "'";
	public static final String QUERY_SELECT_FOR_RELEASE_PERFORM = "select * from " + System.TABLE_NAME_COMMID_TRACE + " where " + FIELD_NAME_COMMID_EVENT_ID + " = ? and " + FIELD_NAME_COMMID_MSG_TYPE + " = '" + MSG_TYPE_RELEASE + "'";
	//개발
//	public static final String QUERY_SELECT_FOR_OCCURRENCE_PERFORM = "select a.* from " + System.TABLE_NAME_COMMID_TRACE + " a, " + System.TABLE_NAME_COMMID_JOIN + " b where " + FIELD_NAME_COMMID_EVENT_ID +" = ? and " + FIELD_NAME_COMMID_OUTB_ID + " = " + FIELD_NAME_COMMID_EOUTB_ID + " and " + FIELD_NAME_COMMID_MSG_TYPE + " = '" + MSG_TYPE_OCCURRENCE + "'";
//	public static final String QUERY_SELECT_FOR_RELEASE_PERFORM = "select a.* from " + System.TABLE_NAME_COMMID_TRACE + " a, " + System.TABLE_NAME_COMMID_JOIN + " b where " + FIELD_NAME_COMMID_EVENT_ID +" = ? and " + FIELD_NAME_COMMID_OUTB_ID + " = " + FIELD_NAME_COMMID_EOUTB_ID + " and " + FIELD_NAME_COMMID_MSG_TYPE + " = '" + MSG_TYPE_RELEASE + "'";
		
	  

	public static final KeyMap[] COMMID_TRACE_FIELDS = {
		new KeyMap("트랜잭션 아이디", "TRST_ID"), new KeyMap("송수신 구분", "SR_FLAG"), new KeyMap("메시지 아이디", "MSG_ID"), new KeyMap("시스템 코드", "SYS_CD"),
		new KeyMap("데이터 타입", "DATA_TYP"), new KeyMap("발생 일시", "OUTB_DTM"), new KeyMap("성공여부", "SCSS_YN"), new KeyMap("에러 코드", "ERR_CD"),
		new KeyMap("요청 응답 키", "RR_KEY"), new KeyMap("수신 프로토콜 타입", "R_PRTCL_TYP"), new KeyMap("수신 MEP 타입", "R_MEP"), new KeyMap("메시지 원본","MSG_ORIG"), new KeyMap("메시지 종류 구분", "MSG_KND_GUBN"),
		new KeyMap("송신 프로토콜 타입", "S_PRTCL_TYP"), new KeyMap("재시도 횟수", "RTR_CNT"), new KeyMap("송신 MEP 타입", "S_MEP"), new KeyMap("수신 이벤트 내용", "RCV_EVET_CNTN"),
		new KeyMap("이벤트 발생 아이디", "EVET_OUTB_ID")
	};
	
	private String trstId;
	private String srFlag;
	private String msgId;
	private String sysCd;
	private String dataTyp;
	private String outbDtm;
	private String scssYn;
	private String errCd;
	private String rrKey;
	private String rPrtclTyp;
	private String rMep;
	private String msgOrig;
	private String msgKndGubn;
	private String sPrtclTyp;
	private String rtrCnt;
	private String sMep;
	private String rcvEvetCntn;
	private String evetOutbId;
	
	public String getTrstId() {
		return trstId;
	}
	public void setTrstId(String trstId) {
		this.trstId = trstId;
	}
	public String getSrFlag() {
		return srFlag;
	}
	public void setSrFlag(String srFlag) {
		this.srFlag = srFlag;
	}
	public String getSrFlagName(){
		if(SmartUtil.isBlankObject(this.srFlag)) return "";
		if(this.srFlag.equals(MSG_TYPE_OCCURRENCE)){
			return "수신";
		}else if(this.srFlag.equals(MSG_TYPE_RELEASE)){
			return "송신";
		}
		return "기타";
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getSysCd() {
		return sysCd;
	}
	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}
	public String getDataTyp() {
		return dataTyp;
	}
	public void setDataTyp(String dataTyp) {
		this.dataTyp = dataTyp;
	}
	public String getOutbDtm() {
		return outbDtm;
	}
	public void setOutbDtm(String outbDtm) {
		this.outbDtm = outbDtm;
	}
	public String getScssYn() {
		return scssYn;
	}
	public void setScssYn(String scssYn) {
		this.scssYn = scssYn;
	}
	public String getErrCd() {
		return errCd;
	}
	public void setErrCd(String errCd) {
		this.errCd = errCd;
	}
	public String getRrKey() {
		return rrKey;
	}
	public void setRrKey(String rrKey) {
		this.rrKey = rrKey;
	}
	public String getrPrtclTyp() {
		return rPrtclTyp;
	}
	public void setrPrtclTyp(String rPrtclTyp) {
		this.rPrtclTyp = rPrtclTyp;
	}
	public String getrMep() {
		return rMep;
	}
	public void setrMep(String rMep) {
		this.rMep = rMep;
	}
	public String getMsgOrig() {
		return msgOrig;
	}
	public void setMsgOrig(String msgOrig) {
		this.msgOrig = msgOrig;
	}
	public String getMsgKndGubn() {
		return msgKndGubn;
	}
	public void setMsgKndGubn(String msgKndGubn) {
		this.msgKndGubn = msgKndGubn;
	}
	public String getsPrtclTyp() {
		return sPrtclTyp;
	}
	public void setsPrtclTyp(String sPrtclTyp) {
		this.sPrtclTyp = sPrtclTyp;
	}
	public String getRtrCnt() {
		return rtrCnt;
	}
	public void setRtrCnt(String rtrCnt) {
		this.rtrCnt = rtrCnt;
	}
	public String getsMep() {
		return sMep;
	}
	public void setsMep(String sMep) {
		this.sMep = sMep;
	}
	public String getRcvEvetCntn() {
		return rcvEvetCntn;
	}
	public void setRcvEvetCntn(String rcvEvetCntn) {
		this.rcvEvetCntn = rcvEvetCntn;
	}
	public String getEvetOutbId() {
		return evetOutbId;
	}
	public void setEvetOutbId(String evetOutbId) {
		this.evetOutbId = evetOutbId;
	}
	public CMHistory(ResultSet resultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet)) return;
		this.setResult(resultSet);
	}
	
	public Map<String, Object> getDataRecord(){
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = CMHistory.COMMID_TRACE_FIELDS;
		
		if(!this.isValid()) return null;
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("TRST_ID"))
				dataRecord.put(keyMap.getId(), this.trstId);
			else if(keyMap.getKey().equals("SR_FLAG"))
				dataRecord.put(keyMap.getId(), this.getSrFlagName());
			else if(keyMap.getKey().equals("MSG_ID"))
				dataRecord.put(keyMap.getId(), this.msgId);
			else if(keyMap.getKey().equals("SYS_CD"))
				dataRecord.put(keyMap.getId(), this.sysCd);
			else if(keyMap.getKey().equals("DATA_TYP"))
				dataRecord.put(keyMap.getId(), this.dataTyp);
			else if(keyMap.getKey().equals("OUTB_DTM"))
				dataRecord.put(keyMap.getId(), this.outbDtm);
			else if(keyMap.getKey().equals("SCSS_YN"))
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.scssYn) ? "on" : "");
			else if(keyMap.getKey().equals("ERR_CD"))
				dataRecord.put(keyMap.getId(), this.errCd);
			else if(keyMap.getKey().equals("RR_KEY"))
				dataRecord.put(keyMap.getId(), this.rrKey);
			else if(keyMap.getKey().equals("R_PRTCL_TYP"))
				dataRecord.put(keyMap.getId(), this.rPrtclTyp);
			else if(keyMap.getKey().equals("R_MEP"))
				dataRecord.put(keyMap.getId(), this.rMep);
			else if(keyMap.getKey().equals("MSG_ORIG"))
				dataRecord.put(keyMap.getId(), this.msgOrig);
			else if(keyMap.getKey().equals("MSG_KND_GUBN"))
				dataRecord.put(keyMap.getId(), this.msgKndGubn);
			else if(keyMap.getKey().equals("S_PRTCL_TYP"))
				dataRecord.put(keyMap.getId(), this.sPrtclTyp);
			else if(keyMap.getKey().equals("RTR_CNT"))
				dataRecord.put(keyMap.getId(), this.rtrCnt);
			else if(keyMap.getKey().equals("S_MEP"))
				dataRecord.put(keyMap.getId(), this.sMep);
			else if(keyMap.getKey().equals("RCV_EVET_CNTN"))
				dataRecord.put(keyMap.getId(), this.rcvEvetCntn);
			else if(keyMap.getKey().equals("EVET_OUTB_ID"))
				dataRecord.put(keyMap.getId(), this.evetOutbId);
		}
		return dataRecord;
//		return UcityTest.getCMHistoryDataRecord();		
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
			this.trstId = result.getString("TRST_ID");
		}catch (Exception ex){}
		try{
			this.srFlag = result.getString("SR_FLAG");
		}catch (Exception ex){}
		try{
			this.msgId = result.getString("MSG_ID");
		}catch (Exception ex){}
		try{
			this.sysCd = result.getString("SYS_CD");
		}catch (Exception ex){}
		try{
			this.dataTyp = result.getString("DATA_TYP");
		}catch (Exception ex){}
		try{
			this.outbDtm = result.getString("OUTB_DTM");
		}catch (Exception ex){}
		try{
			this.scssYn = result.getString("SCSS_YN");
		}catch (Exception ex){}
		try{
			this.errCd = result.getString("ERR_CD");
		}catch (Exception ex){}
		try{
			this.rrKey = result.getString("RR_KEY");
		}catch (Exception ex){}
		try{
			this.rPrtclTyp = result.getString("R_PRTCL_TYP");
		}catch (Exception ex){}
		try{
			this.rMep = result.getString("R_MEP");
		}catch (Exception ex){}
		try{
			this.rMep = result.getString("MSG_ORIG");
		}catch (Exception ex){}
		try{
			this.msgKndGubn = result.getString("MSG_KND_GUBN");
		}catch (Exception ex){}
		try{
			this.sPrtclTyp = result.getString("S_PRTCL_TYP");
		}catch (Exception ex){}
		try{
			this.rtrCnt = result.getString("RTR_CNT");
		}catch (Exception ex){}
		try{
			this.sMep = result.getString("S_MEP");
		}catch (Exception ex){}
		try{
			this.rcvEvetCntn = result.getString("RCV_EVET_CNTN");
		}catch (Exception ex){}
		try{
			this.evetOutbId = result.getString("EVET_OUTB_ID");
		}catch (Exception ex){}
	}

	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.trstId) && !SmartUtil.isBlankObject(this.evetOutbId))
			return true;
		return false;
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
				
		String cmHistorySelectSql = (status.equals(MSG_TYPE_OCCURRENCE)) ? CMHistory.QUERY_SELECT_FOR_OCCURRENCE_PERFORM : CMHistory.QUERY_SELECT_FOR_RELEASE_PERFORM;
		try {
			try{
				con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
			}catch (TbSQLException te){
				te.printStackTrace();
				return null;
			}
			
			try{
				selectPstmt = con.prepareStatement(cmHistorySelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if (count == 1) {
					try{
						CMHistory cmHistory = new CMHistory(rs);
						if(cmHistory.isValid()){
							try {
								if (selectPstmt != null)
									selectPstmt.close();
								con.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return cmHistory.getDataRecord();
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
