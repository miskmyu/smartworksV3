package net.smartworks.server.engine.common.menuitem.model;

import net.smartworks.server.engine.common.model.MisObjectCond;

public class CategoryChangeCond extends MisObjectCond{

	private String oldCategoryId;
	private String newCategoryId;
	
	public String getOldCategoryId() {
		return oldCategoryId;
	}
	public void setOldCategoryId(String oldCategoryId) {
		this.oldCategoryId = oldCategoryId;
	}
	public String getNewCategoryId() {
		return newCategoryId;
	}
	public void setNewCategoryId(String newCategoryId) {
		this.newCategoryId = newCategoryId;
	}
	


}
