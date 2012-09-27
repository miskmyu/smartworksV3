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

public class ICSituation {

	public static final KeyMap[] INTCON_SITUATION_FIELDS = {
		new KeyMap("키ID", "ID "), new KeyMap("이벤트구분ID", "EVENT_ID    "), new KeyMap("X좌표", "POS_X    "), new KeyMap("Y좌표", "POS_Y    "),
		new KeyMap("Z좌표", "POS_Z    "), new KeyMap("전송시각", "SEND_TIME   "), new KeyMap("이벤트내용", "EVENT_CONTENT   "), new KeyMap("시설물구분", "SISUL_DIV   "),
		new KeyMap("시설물ID", "SISUL_ID   "), new KeyMap("기타정보", "ETC   "), new KeyMap("처리여부", "DONE_YN   "), new KeyMap("등록일시", "CREATE_DATE   "),
		new KeyMap("속성", "ATTR1   ")
	};
	
	private String id;
	private String eventId;
	private String posX;
	private String posY;
	private String posZ;
	private String sendTime;
	private String eventContent;
	private String sisulDiv;
	private String sisulId;
	private String etc;
	private String doneYn;
	private String createDate;
	private String attr1;
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getPosX() {
		return posX;
	}
	public void setPosX(String posX) {
		this.posX = posX;
	}
	public String getPosY() {
		return posY;
	}
	public void setPosY(String posY) {
		this.posY = posY;
	}
	public String getPosZ() {
		return posZ;
	}
	public void setPosZ(String posZ) {
		this.posZ = posZ;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getEventContent() {
		return eventContent;
	}
	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}
	public String getSisulDiv() {
		return sisulDiv;
	}
	public void setSisulDiv(String sisulDiv) {
		this.sisulDiv = sisulDiv;
	}
	public String getSisulId() {
		return sisulId;
	}
	public void setSisulId(String sisulId) {
		this.sisulId = sisulId;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	public String getDoneYn() {
		return doneYn;
	}
	public void setDoneYn(String doneYn) {
		this.doneYn = doneYn;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getAttr1() {
		return attr1;
	}
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	public ICSituation(ResultSet resultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet)) return;
		this.setResult(resultSet);
	}
	
	public Map<String, Object> getDataRecord(){
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = ICSituation.INTCON_SITUATION_FIELDS;
		
		if(!this.isValid()) return null;
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("ID"))
				dataRecord.put(keyMap.getId(), this.id);
			else if(keyMap.getKey().equals("EVENT_ID "))
				dataRecord.put(keyMap.getId(), this.eventId);
			else if(keyMap.getKey().equals("POS_X"))
				dataRecord.put(keyMap.getId(), this.posX);
			else if(keyMap.getKey().equals("POS_Y"))
				dataRecord.put(keyMap.getId(), this.posY);
			else if(keyMap.getKey().equals("POS_Z"))
				dataRecord.put(keyMap.getId(), this.posZ);
			else if(keyMap.getKey().equals("SEND_TIME"))
				dataRecord.put(keyMap.getId(), this.sendTime);
			else if(keyMap.getKey().equals("EVENT_CONTENT"))
				dataRecord.put(keyMap.getId(), this.eventContent);
			else if(keyMap.getKey().equals("SISUL_DIV"))
				dataRecord.put(keyMap.getId(), this.sisulDiv);
			else if(keyMap.getKey().equals("SISUL_ID"))
				dataRecord.put(keyMap.getId(), this.sisulId);
			else if(keyMap.getKey().equals("ETC"))
				dataRecord.put(keyMap.getId(), this.etc);
			else if(keyMap.getKey().equals("DONE_YN"))
				dataRecord.put(keyMap.getId(), this.doneYn);
			else if(keyMap.getKey().equals("CREATE_DATE"))
				dataRecord.put(keyMap.getId(), this.createDate);
			else if(keyMap.getKey().equals("ATTR1"))
				dataRecord.put(keyMap.getId(), this.attr1);
		}
		return dataRecord;
//		return UcityTest.getICSituationDataRecord();
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
					this.id = result.getString("ID");
				}catch (Exception ex){}
				try{
					this.eventId = result.getString("EVENT_ID");
				}catch (Exception ex){}
				try{
					this.posX = result.getString("POS_X ");
				}catch (Exception ex){}
				try{
					this.posY = result.getString("POS_Y");
				}catch (Exception ex){}
				try{
					this.posZ = result.getString("POS_Z");
				}catch (Exception ex){}
				try{
					this.sendTime = result.getString("SEND_TIME");
				}catch (Exception ex){}
				try{
					this.eventContent = result.getString("EVENT_CONTENT");
				}catch (Exception ex){}
				try{
					this.sisulDiv = result.getString("SISUL_DIV");
				}catch (Exception ex){}
				try{
					this.sisulId = result.getString("SISUL_ID");
				}catch (Exception ex){}
				try{
					this.etc = result.getString("ETC");
				}catch (Exception ex){}
				try{
					this.doneYn = result.getString("DONE_YN");
				}catch (Exception ex){}
				try{
					this.createDate = result.getString("CREATE_DATE");
				}catch (Exception ex){}
				try{
					this.attr1 = result.getString("ATTR1");
				}catch (Exception ex){}
			}
		}catch (Exception e){
			e.printStackTrace();
		}		
	}
	
	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.id) && !SmartUtil.isBlankObject(this.eventId)) return true;
		return false;
	}
}
