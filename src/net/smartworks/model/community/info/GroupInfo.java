package net.smartworks.model.community.info;


public class GroupInfo extends WorkSpaceInfo {

	private String desc;
	private boolean	isPublic = false;
	
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public GroupInfo(){
		super();
	}
	public GroupInfo(String id, String name){
		super(id, name);
	}

}
