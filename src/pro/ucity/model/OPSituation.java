package pro.ucity.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import pro.ucity.util.UcityTest;
import pro.ucity.util.UcityUtil;
import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

public class OPSituation {

	public static final KeyMap[] OPPORTAL_SITUATION_FIELDS = {
		new KeyMap("U서비스 이벤트 아이디", "SITUATION_ID "), new KeyMap("상황 아이디", "SITUATION_ID   "), new KeyMap("순번", "SEQ   "), new KeyMap("상태", "STATUS   "),
		new KeyMap("담당자 아이디", "CHARGE_USER_ID   "), new KeyMap("담당자명", "CHARGE_USER_NAME  "), new KeyMap("시작일시", "START_DATE  "), new KeyMap("종료일시", "END_DATE  "),
		new KeyMap("접수내용", "RECEIVE_CONTENT  "), new KeyMap("처리내용", "HANDLING_CONTENT  ")
	};

	private String situationId;
	private String seq;
	private String status;
	private String chargeUserId;
	private String chargeUserName;
	private String startDate;
	private String endDate;
	private String receiveContent;
	private String handlingContent;
		
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
	public String getReceiveContent() {
		return receiveContent;
	}
	public void setReceiveContent(String receiveContent) {
		this.receiveContent = receiveContent;
	}
	public String getHandlingContent() {
		return handlingContent;
	}
	public void setHandlingContent(String handlingContent) {
		this.handlingContent = handlingContent;
	}

	public OPSituation(ResultSet resultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet)) return;
		this.setResult(resultSet);
	}
	
	public Map<String, Object> getDataRecord(){
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = OPSituation.OPPORTAL_SITUATION_FIELDS;
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("SITUATION_ID"))
				dataRecord.put(keyMap.getId(), this.situationId);
			else if(keyMap.getKey().equals("SEQ "))
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
			else if(keyMap.getKey().equals("RECEIVE_CONTENT"))
				dataRecord.put(keyMap.getId(), this.receiveContent);
			else if(keyMap.getKey().equals("HANDLING_CONTENT"))
				dataRecord.put(keyMap.getId(), this.handlingContent);
		}
//		return dataRecord;
		return UcityTest.getOPSituationDataRecord();
	}
	
	public void performTask(String processId, String taskInstId) throws Exception{
		TaskInstance taskInstance = null;
		if(SmartUtil.isBlankObject(taskInstId)) return;
		
		taskInstance = SwServiceFactory.getInstance().getInstanceService().getTaskInstanceById(processId, taskInstId);
		
		if(SmartUtil.isBlankObject(taskInstance)) return;
		
		UcityUtil.performUServiceTask(taskInstance, this.getDataRecord());		
	}
	
	public void setResult(ResultSet result){
		try{
			if(result.getRow()>0){ 
				try{
					this.situationId = result.getString("SITUATION_ID");
				}catch (Exception ex){}
				try{
					this.seq = result.getString("SEQ");
				}catch (Exception ex){}
				try{
					this.status = result.getString("STATUS ");
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
					this.receiveContent = result.getString("RECEIVE_CONTENT");
				}catch (Exception ex){}
				try{
					this.handlingContent = result.getString("HANDLING_CONTENT");
				}catch (Exception ex){}
			}
		}catch (Exception e){
			e.printStackTrace();
		}		
	}
}
