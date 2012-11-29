package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkCategoryInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class InstanceInfo implements Comparable<InstanceInfo> {

	private String id;
	private String subject;
	private int type=-1;
//	private WorkInfo work;
	private String workId;
	private String workName;
	private int workType;
	private String workFullPathName;
	private boolean isWorkRunning;
//	private WorkSpaceInfo workSpace;
	private String workSpaceId;
	private String workSpaceName;
	private int workSpaceType;
	private String workSpaceMinPicture;
	private int status=-1;
	private int numberOfAssociatedWorks=0;
	private UserInfo owner;
	private LocalDate createdDate;
	private UserInfo lastModifier;
	private LocalDate lastModifiedDate;
	
	private Property[] extentedProperty;
	
	public boolean isNew() {
		if(SmartUtil.isBlankObject(lastModifiedDate)) return false;
		return lastModifiedDate.isNew();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
//	public WorkInfo getWork() {
//		return work;
//	}
//	public void setWork(WorkInfo work) {
//		this.work = work;
//	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getNumberOfAssociatedWorks() {
		return numberOfAssociatedWorks;
	}
	public void setNumberOfAssociatedWorks(int numberOfAssociatedWorks) {
		this.numberOfAssociatedWorks = numberOfAssociatedWorks;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
//	public WorkSpaceInfo getWorkSpace() {
//		if (workSpace == null && owner != null)
//			return owner;
//		return workSpace;
//	}
//	public void setWorkSpace(WorkSpaceInfo workSpace) {
//		this.workSpace = workSpace;
//	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	public UserInfo getOwner() {
		return owner;
	}
	public void setOwner(UserInfo owner) {
		this.owner = owner;
	}
	public UserInfo getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(UserInfo lastModifier) {
		this.lastModifier = lastModifier;
	}
	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public int getWorkType() {
		return workType;
	}
	public void setWorkType(int workType) {
		this.workType = workType;
	}
	public String getWorkFullPathName() {
		return workFullPathName;
	}
	public void setWorkFullPathName(String workFullPathName) {
		this.workFullPathName = workFullPathName;
	}
	public boolean isWorkRunning() {
		return isWorkRunning;
	}
	public void setWorkRunning(boolean isWorkRunning) {
		this.isWorkRunning = isWorkRunning;
	}
	public String getWorkSpaceId() {
		return workSpaceId;
	}
	public void setWorkSpaceId(String workSpaceId) {
		this.workSpaceId = workSpaceId;
	}
	public String getWorkSpaceName() {
		return workSpaceName;
	}
	public void setWorkSpaceName(String workSpaceName) {
		this.workSpaceName = workSpaceName;
	}
	public int getWorkSpaceType() {
		return workSpaceType;
	}
	public void setWorkSpaceType(int workSpaceType) {
		this.workSpaceType = workSpaceType;
	}
	public String getWorkSpaceMinPicture() {
		return workSpaceMinPicture;
	}
	public void setWorkSpaceMinPicture(String workSpaceMinPicture) {
		this.workSpaceMinPicture = workSpaceMinPicture;
	}
	public InstanceInfo(){
		super();
	}	
	public InstanceInfo(String id, String subject, int type, UserInfo owner, UserInfo lastModifier,
			LocalDate lastModifiedDate) {
		super();
		this.id = id;
		this.subject = subject;
		this.type = type;
		this.owner = owner;
		this.lastModifier = lastModifier;
		this.lastModifiedDate = lastModifiedDate;
	}
	@Override
	public int compareTo(InstanceInfo o) {
		return this.getLastModifiedDate().compareTo(((InstanceInfo)o).getLastModifiedDate());
	}
	public Property[] getExtentedProperty() {
		return extentedProperty;
	}
	public void setExtentedProperty(Property[] extentedProperty) {
		this.extentedProperty = extentedProperty;
	}
	
	public void setWorkInfo(WorkInfo work){
		if(SmartUtil.isBlankObject(work)){
			this.setWorkInfo(null, null, -1, false, null);			
			return;
		}
		boolean isWorkRunning=false;
		String workFullPathName = "";
		if(work.getClass().equals(SmartWorkInfo.class)){
			workFullPathName = ((SmartWorkInfo)work).getFullpathName();
			isWorkRunning = ((SmartWorkInfo)work).isRunning();
		}else if(work.getClass().equals(WorkCategoryInfo.class))
			isWorkRunning = ((WorkCategoryInfo)work).isRunning();
		
		this.setWorkInfo(work.getId(), work.getName(), work.getType(), isWorkRunning, workFullPathName);
	}
	public void setWorkInfo(String workId, String workName, int workType, boolean isWorkRunning, String workFullPathName){
		this.setWorkId(workId);
		this.setWorkName(workName);
		this.setWorkType(workType);
		this.setWorkRunning(isWorkRunning);
		this.setWorkFullPathName(workFullPathName);
		
	}
	
	public void setWorkSpaceInfo(WorkSpaceInfo workSpace){
		if(SmartUtil.isBlankObject(workSpace))
			this.setWorkSpaceInfo(null, null, -1, null);
		else 
			this.setWorkSpaceInfo(workSpace.getId(), workSpace.getName(), workSpace.getSpaceType(), workSpace.getMinPicture());
	}
	public void setWorkSpaceInfo(String workSpaceId, String workSpaceName, int workSpaceType, String workSpaceMinPicture){
		this.setWorkSpaceId(workSpaceId);						
		this.setWorkSpaceName(workSpaceName);						
		this.setWorkSpaceType(workSpaceType);						
		this.setWorkSpaceMinPicture(workSpaceMinPicture);		
	}
}
