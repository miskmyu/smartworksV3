package net.smartworks.model.work.info;

public class SmartWorkInfo extends WorkInfo {

	private WorkCategoryInfo myGroup = null;
	private WorkCategoryInfo myCategory = null;
	private boolean isRunning;
	private boolean isEditing;
	private boolean favorite = false;
	
	public String getFullpathName(){
		return this.myCategory.getName() + ((this.myGroup != null && this.myGroup.getId() != null) ? " > " + this.myGroup.getName() : "") + " > "
				+ super.getName();		
	}
	public String getPathName(){
		return this.myCategory.getName() + ((this.myGroup != null && this.myGroup.getId() != null) ? " > " + this.myGroup.getName() : "");		
	}
	public WorkCategoryInfo getMyGroup() {
		return myGroup;
	}
	public void setMyGroup(WorkCategoryInfo myGroup) {
		this.myGroup = myGroup;
	}
	public WorkCategoryInfo getMyCategory() {
		return myCategory;
	}
	public void setMyCategory(WorkCategoryInfo myCategory) {
		this.myCategory = myCategory;
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public boolean isEditing() {
		return isEditing;
	}
	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public SmartWorkInfo(){
		super();
	}

	public SmartWorkInfo(String id, String name, int type, WorkCategoryInfo myGroup, WorkCategoryInfo myCategory){
		super(id, name, type);
		this.myGroup = myGroup;;
		this.myCategory = myCategory;
	}
	
	public boolean isEditable(){
		if(isEditing || isRunning) return false;
		return true;
	}	
}
