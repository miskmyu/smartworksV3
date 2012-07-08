package net.smartworks.model.community;

public class WorkSpace extends Community {
	
	private int spaceType;

	public int getSpaceType() {
		return spaceType;
	}
	public void setSpaceType(int spaceType) {
		this.spaceType = spaceType;
	}
	public WorkSpace(){
		super();
	}
	public WorkSpace(String id, String name){
		super(id, name);
	}
}
