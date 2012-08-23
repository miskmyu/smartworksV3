package net.smartworks.server.engine.common.menuitem.model;

import net.smartworks.server.engine.common.model.MisObject;

public class CategoryChange extends MisObject{

	public static final String A_OLDCATEGORYID = "oldCategoryId";
	public static final String A_NEWCATEGORYID = "newCategoryId";
	
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
