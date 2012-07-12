package net.smartworks.model.community.info;

public class DepartmentInfo extends WorkSpaceInfo {

	private String desc;
	private String fullpathName = "";
		
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getFullpathName() {
		return fullpathName;
	}
	public void setFullpathName(String fullpathName) {
		this.fullpathName = fullpathName;
	}
	public DepartmentInfo(){
		super();
	}
	public DepartmentInfo(String id, String name){
		super(id, name);
	}
	public DepartmentInfo(String id, String name, String desc){
		super(id, name);
		this.desc = desc;
	}

}