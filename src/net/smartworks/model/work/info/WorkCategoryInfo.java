package net.smartworks.model.work.info;

import net.smartworks.model.work.WorkCategory;
import net.smartworks.util.SmartUtil;

public class WorkCategoryInfo extends WorkInfo {

	private WorkCategoryInfo parent;
	private boolean running;
	
	public WorkCategoryInfo getParent() {
		return parent;
	}
	public void setParent(WorkCategoryInfo parent) {
		this.parent = parent;
	}
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	public WorkCategoryInfo(){
		super();
		super.setType(WorkCategory.TYPE_CATEGORY);
	}
	public WorkCategoryInfo(String id, String name){
		super(id, name, WorkCategory.TYPE_CATEGORY);
	}
	
	public String getFullPathName(){
		String fullPathName = "";
		WorkCategoryInfo parent = this.parent;
		while(!SmartUtil.isBlankObject(parent)){
			fullPathName = fullPathName + parent.getName() + ">";
		}
		return fullPathName + getName();
	}
}
