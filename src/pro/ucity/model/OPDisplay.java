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

public class OPDisplay {

	public static final KeyMap[] OPPORTAL_DISPLAY_FIELDS = {
		new KeyMap("U서비스 이벤트 아이디", "SITUATION_ID "), new KeyMap("상황표출 아이디", "DISPLAY_ID    "), new KeyMap("상황 아이디", "SITUATION_ID    "), new KeyMap("상황표출 내용", "DISPLAY_CONTENT    "),
		new KeyMap("상황표출 이미지경로", "DISPLAY_IMAGE_PATH    "), new KeyMap("상황표출 동영상경로", "DISPLAY_MOVIE_PATH   "), new KeyMap("상황표출시간(초)", "DISPLAY_SECOND   "), new KeyMap("미디어보드표출여부", "MB_YN   "),
		new KeyMap("VMS표출여부", "VMS_YN   "), new KeyMap("BIT표출여부", "BIT_YN   "), new KeyMap("홈세대기표출여부", "WP_YN   "), new KeyMap("휴대폰표출여부", "MP_YN   "),
		new KeyMap("표츌요청일시", "REQUEST_DATE   ")
	};
	
	private String situationId;
	private String displayId;
	private String displayContent;
	private String displayImagePath;
	private String displayMoviePath;
	private String displaySecond;
	private String mbYn;
	private String vmsYn;
	private String bitYn;
	private String wpYn;
	private String mpYn;
	private String requestDate;
		
	public String getSituationId() {
		return situationId;
	}
	public void setSituationId(String situationId) {
		this.situationId = situationId;
	}
	public String getDisplayId() {
		return displayId;
	}
	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}
	public String getDisplayContent() {
		return displayContent;
	}
	public void setDisplayContent(String displayContent) {
		this.displayContent = displayContent;
	}
	public String getDisplayImagePath() {
		return displayImagePath;
	}
	public void setDisplayImagePath(String displayImagePath) {
		this.displayImagePath = displayImagePath;
	}
	public String getDisplayMoviePath() {
		return displayMoviePath;
	}
	public void setDisplayMoviePath(String displayMoviePath) {
		this.displayMoviePath = displayMoviePath;
	}
	public String getDisplaySecond() {
		return displaySecond;
	}
	public void setDisplaySecond(String displaySecond) {
		this.displaySecond = displaySecond;
	}
	public String getMbYn() {
		return mbYn;
	}
	public void setMbYn(String mbYn) {
		this.mbYn = mbYn;
	}
	public String getVmsYn() {
		return vmsYn;
	}
	public void setVmsYn(String vmsYn) {
		this.vmsYn = vmsYn;
	}
	public String getBitYn() {
		return bitYn;
	}
	public void setBitYn(String bitYn) {
		this.bitYn = bitYn;
	}
	public String getWpYn() {
		return wpYn;
	}
	public void setWpYn(String wpYn) {
		this.wpYn = wpYn;
	}
	public String getMpYn() {
		return mpYn;
	}
	public void setMpYn(String mpYn) {
		this.mpYn = mpYn;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public OPDisplay(ResultSet resultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet)) return;
		this.setResult(resultSet);
	}
	
	public Map<String, Object> getDataRecord(){
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = OPDisplay.OPPORTAL_DISPLAY_FIELDS;

		if(!this.isValid()) return null;
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("SITUATION_ID"))
				dataRecord.put(keyMap.getId(), this.situationId);
			else if(keyMap.getKey().equals("DISPLAY_ID "))
				dataRecord.put(keyMap.getId(), this.displayId);
			else if(keyMap.getKey().equals("DISPLAY_CONTENT"))
				dataRecord.put(keyMap.getId(), this.displayContent);
			else if(keyMap.getKey().equals("DISPLAY_IMAGE_PATH"))
				dataRecord.put(keyMap.getId(), this.displayImagePath);
			else if(keyMap.getKey().equals("DISPLAY_MOVIE_PATH"))
				dataRecord.put(keyMap.getId(), this.displayMoviePath);
			else if(keyMap.getKey().equals("DISPLAY_SECOND"))
				dataRecord.put(keyMap.getId(), this.displaySecond);
			else if(keyMap.getKey().equals("MB_YN"))
				dataRecord.put(keyMap.getId(), this.mbYn);
			else if(keyMap.getKey().equals("VMS_YN"))
				dataRecord.put(keyMap.getId(), this.vmsYn);
			else if(keyMap.getKey().equals("BIT_YN"))
				dataRecord.put(keyMap.getId(), this.bitYn);
			else if(keyMap.getKey().equals("WP_YN"))
				dataRecord.put(keyMap.getId(), this.wpYn);
			else if(keyMap.getKey().equals("MP_YN"))
				dataRecord.put(keyMap.getId(), this.mpYn);
			else if(keyMap.getKey().equals("REQUEST_DATE"))
				dataRecord.put(keyMap.getId(), this.requestDate);
		}
		return dataRecord;
//		return UcityTest.getOPDisplayDataRecord();
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
			this.displayId = result.getString("DISPLAY_ID");
		}catch (Exception ex){}
		try{
			this.displayContent = result.getString("DISPLAY_CONTENT ");
		}catch (Exception ex){}
		try{
			this.displayImagePath = result.getString("DISPLAY_IMAGE_PATH");
		}catch (Exception ex){}
		try{
			this.displayMoviePath = result.getString("DISPLAY_MOVIE_PATH");
		}catch (Exception ex){}
		try{
			this.displaySecond = result.getString("DISPLAY_SECOND");
		}catch (Exception ex){}
		try{
			this.mbYn = result.getString("MB_YN");
		}catch (Exception ex){}
		try{
			this.vmsYn = result.getString("VMS_YN");
		}catch (Exception ex){}
		try{
			this.bitYn = result.getString("BIT_YN");
		}catch (Exception ex){}
		try{
			this.wpYn = result.getString("WP_YN");
		}catch (Exception ex){}
		try{
			this.mpYn = result.getString("MP_YN");
		}catch (Exception ex){}
		try{
			this.requestDate = result.getString("REQUEST_DATE");
		}catch (Exception ex){}
	}
	
	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.situationId) && !SmartUtil.isBlankObject(this.displayId)) return true;
		return false;
	}
}
