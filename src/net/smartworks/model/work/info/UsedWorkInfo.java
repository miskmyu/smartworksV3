package net.smartworks.model.work.info;

public class UsedWorkInfo{
	
	private WorkInfo work;
	private String workFullpathName;
	private int CreatedInstanceCount;
		
	public String getWorkFullpathName() {
		return workFullpathName;
	}

	public void setWorkFullpathName(String workFullpathName) {
		this.workFullpathName = workFullpathName;
	}

	public int getCreatedInstanceCount() {
		return CreatedInstanceCount;
	}

	public void setCreatedInstanceCount(int createdInstanceCount) {
		CreatedInstanceCount = createdInstanceCount;
	}

	public WorkInfo getWork() {
		return work;
	}
	public void setWork(WorkInfo work) {
		this.work = work;
	}

	public UsedWorkInfo(){
		super();
	}

}
