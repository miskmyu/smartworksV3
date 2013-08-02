package net.smartworks.model.work;

public class ReportWork extends SmartWork {

	public ReportWork(){
		super();
		super.setType(SmartWork.TYPE_REPORT);
	}
	public ReportWork(String id, String name){
		super(id, name);
		super.setType(SmartWork.TYPE_REPORT);
	}
	public ReportWork(String id, String name, String desc, WorkCategory myCategory) {
		super(id, name, SmartWork.TYPE_REPORT, desc, myCategory);
	}
}
