package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.FieldData;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.util.LocalDate;

public class IWInstanceInfo extends WorkInstanceInfo {

	private int views;
	private FieldData[] displayDatas;
	private boolean isApproval=false;

	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public FieldData[] getDisplayDatas() {
		return displayDatas;
	}
	public void setDisplayDatas(FieldData[] displayDatas) {
		this.displayDatas = displayDatas;
	}

	public boolean isApproval() {
		return isApproval;
	}
	public void setApproval(boolean isApproval) {
		this.isApproval = isApproval;
	}
	public IWInstanceInfo(){
		super();
		super.setType(WorkInstance.TYPE_INFORMATION);
	}	
//	public IWInstanceInfo(String id, String name, WorkInfo work, UserInfo owner, UserInfo lastModifier, LocalDate lastModifiedDate){
//		super(id, name, owner, lastModifier, lastModifiedDate);
//		super.setWork(work);
//		super.setType(WorkInstance.TYPE_INFORMATION);
//	}
	public IWInstanceInfo(String id, String name, String workId, String workName, int workType, UserInfo owner, UserInfo lastModifier, LocalDate lastModifiedDate){
		super(id, name, owner, lastModifier, lastModifiedDate);
		super.setWorkId(workId);
		super.setWorkName(workName);
		super.setWorkType(workType);
		super.setType(WorkInstance.TYPE_INFORMATION);
	}
	public IWInstanceInfo(String id, String name, WorkInfo work, UserInfo owner, UserInfo lastModifier, LocalDate lastModifiedDate){
		super(id, name, owner, lastModifier, lastModifiedDate);
		super.setWorkInfo(work);
		super.setType(WorkInstance.TYPE_INFORMATION);
	}
	
}
