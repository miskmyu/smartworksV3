package pro.ucity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.tmax.tibero.jdbc.TbSQLException;

import pro.ucity.util.UcityTest;
import pro.ucity.util.UcityUtil;
import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

public class ICSituation {

	public static final String MSG_TYPE_OCCURRENCE = "O";
	public static final String MSG_TYPE_RELEASE = "R";
	
	public static final KeyMap[] INTCON_SITUATION_FIELDS = {
		new KeyMap("키ID", UcityConstant.getQueryByKey("ICSituation.ID")), new KeyMap("이벤트구분ID", UcityConstant.getQueryByKey("ICSituation.EVENT_ID")), new KeyMap("X좌표", UcityConstant.getQueryByKey("ICSituation.POS_X")), new KeyMap("Y좌표", UcityConstant.getQueryByKey("ICSituation.POS_Y")),
		new KeyMap("Z좌표", UcityConstant.getQueryByKey("ICSituation.POS_Z")), new KeyMap("전송시각", UcityConstant.getQueryByKey("ICSituation.SEND_TIME")), new KeyMap("이벤트내용", UcityConstant.getQueryByKey("ICSituation.EVENT_CONTENT")), new KeyMap("시설물구분", UcityConstant.getQueryByKey("ICSituation.SISUL_DIV")),
		new KeyMap("시설물ID", UcityConstant.getQueryByKey("ICSituation.SISUL_ID")), new KeyMap("기타정보", UcityConstant.getQueryByKey("ICSituation.ETC")), new KeyMap("처리여부", UcityConstant.getQueryByKey("ICSituation.DONE_YN")), new KeyMap("등록일시", UcityConstant.getQueryByKey("ICSituation.CREATE_DATE")),
		new KeyMap("속성", UcityConstant.getQueryByKey("ICSituation.ATTR1"))
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
			if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.ID")))
				dataRecord.put(keyMap.getId(), this.id);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.EVENT_ID")))
				dataRecord.put(keyMap.getId(), this.eventId);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.POS_X")))
				dataRecord.put(keyMap.getId(), this.posX);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.POS_Y")))
				dataRecord.put(keyMap.getId(), this.posY);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.POS_Z")))
				dataRecord.put(keyMap.getId(), this.posZ);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.SEND_TIME")))
				dataRecord.put(keyMap.getId(), UcityUtil.getDateString(this.sendTime));
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.EVENT_CONTENT")))
				dataRecord.put(keyMap.getId(), this.eventContent);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.SISUL_DIV")))
				dataRecord.put(keyMap.getId(), this.sisulDiv);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.SISUL_ID")))
				dataRecord.put(keyMap.getId(), this.sisulId);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.ETC")))
				dataRecord.put(keyMap.getId(), this.etc);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.DONE_YN")))
				dataRecord.put(keyMap.getId(), this.doneYn);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.CREATE_DATE")))
				dataRecord.put(keyMap.getId(), this.createDate);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("ICSituation.ATTR1")))
				dataRecord.put(keyMap.getId(), this.attr1);
		}
		return dataRecord;
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
			this.id = result.getString(UcityConstant.getQueryByKey("ICSituation.ID"));
		}catch (Exception ex){}
		try{
			this.eventId = result.getString(UcityConstant.getQueryByKey("ICSituation.EVENT_ID"));
		}catch (Exception ex){}
		try{
			this.posX = result.getString(UcityConstant.getQueryByKey("ICSituation.POS_X"));
		}catch (Exception ex){}
		try{
			this.posY = result.getString(UcityConstant.getQueryByKey("ICSituation.POS_Y"));
		}catch (Exception ex){}
		try{
			this.posZ = result.getString(UcityConstant.getQueryByKey("ICSituation.POS_Z"));
		}catch (Exception ex){}
		try{
			this.sendTime = result.getString(UcityConstant.getQueryByKey("ICSituation.SEND_TIME"));
		}catch (Exception ex){}
		try{
			this.eventContent = result.getString(UcityConstant.getQueryByKey("ICSituation.EVENT_CONTENT"));
		}catch (Exception ex){}
		try{
			this.sisulDiv = result.getString(UcityConstant.getQueryByKey("ICSituation.SISUL_DIV"));
		}catch (Exception ex){}
		try{
			this.sisulId = result.getString(UcityConstant.getQueryByKey("ICSituation.SISUL_ID"));
		}catch (Exception ex){}
		try{
			this.etc = result.getString(UcityConstant.getQueryByKey("ICSituation.ETC"));
		}catch (Exception ex){}
		try{
			this.doneYn = result.getString(UcityConstant.getQueryByKey("ICSituation.DONE_YN"));
		}catch (Exception ex){}
		try{
			this.createDate = result.getString(UcityConstant.getQueryByKey("ICSituation.CREATE_DATE"));
		}catch (Exception ex){}
		try{
			this.attr1 = result.getString(UcityConstant.getQueryByKey("ICSituation.ATTR1"));
		}catch (Exception ex){}
	}
	
	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.id) && !SmartUtil.isBlankObject(this.eventId)) return true;
		return false;
	}

	public static Map<String,Object> readHistoryTable(Connection connection, String eventId, String status){
		
		if(SmartUtil.isBlankObject(connection) || SmartUtil.isBlankObject(eventId)) return null;

//		Connection con = null;
		PreparedStatement selectPstmt = null;
				
		String icSituationSelectSql = UcityConstant.getQueryByKey("ICSituation.QUERY_SELECT_FOR_RELEASE_PERFORM");
		try {
//			try{
////			    Context init = new InitialContext();
////			    Context envinit = (Context)init.lookup("java:comp/env");
////			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
////			    con = ds.getConnection();
//				con = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
//			}catch (TbSQLException te){
//				te.printStackTrace();
//				return null;
//			}
			
			try{
				selectPstmt = connection.prepareStatement(icSituationSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if (count == 1) {
					try{
						ICSituation icSituation = new ICSituation(rs);
						if(icSituation.isValid()){
							try {
								if (selectPstmt != null)
									selectPstmt.close();
//								con.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return icSituation.getDataRecord();
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
//				if(con != null)
//					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
