package net.smartworks.model.work;

public class ProcessWork extends SmartWork {

	private SmartDiagram diagram;
	private String helpUrl;
	private String manualFileId;
	private String manualFileName;
	private String manualFilePath;
	private int commentCount;

	public SmartDiagram getDiagram() {
		return diagram;
	}
	public void setDiagram(SmartDiagram diagram) {
		this.diagram = diagram;
	}
	public String getHelpUrl() {
		return helpUrl;
	}
	public void setHelpUrl(String helpUrl) {
		this.helpUrl = helpUrl;
	}
	public String getManualFileId() {
		return manualFileId;
	}
	public void setManualFileId(String manualFileId) {
		this.manualFileId = manualFileId;
	}
	public String getManualFileName() {
		return manualFileName;
	}
	public void setManualFileName(String manualFileName) {
		this.manualFileName = manualFileName;
	}
	public String getManualFilePath() {
		return manualFilePath;
	}
	public void setManualFilePath(String manualFilePath) {
		this.manualFilePath = manualFilePath;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public ProcessWork() {
		super();
		super.setType(TYPE_PROCESS);
	}

	public ProcessWork(String id, String name) {
		super(id, name);
		super.setType(TYPE_PROCESS);
	}

	public ProcessWork(String id, String name, String desc, WorkCategory myCategory) {
		super(id, name, TYPE_PROCESS, desc, myCategory);
	}
}
