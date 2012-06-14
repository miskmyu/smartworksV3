package net.smartworks.model.work;

public class InformationWork extends SmartWork {

	private SmartForm form;
	private FormField keyField;
	private boolean keyDuplicatable=true;
	private FormField[] displayFields;
	private String helpUrl;
	private String manualFilePath;
	private String manualFileName;
	private int commentCount;
	
	public SmartForm getForm() {
		return form;
	}
	public void setForm(SmartForm form) {
		this.form = form;
	}
	public FormField getKeyField() {
		return keyField;
	}
	public void setKeyField(FormField keyField) {
		this.keyField = keyField;
	}
	public boolean isKeyDuplicatable() {
		return keyDuplicatable;
	}
	public void setKeyDuplicatable(boolean keyDuplicatable) {
		this.keyDuplicatable = keyDuplicatable;
	}
	public FormField[] getDisplayFields() {
		return displayFields;
	}
	public void setDisplayFields(FormField[] displayFields) {
		this.displayFields = displayFields;
	}
	public String getHelpUrl() {
		return helpUrl;
	}
	public void setHelpUrl(String helpUrl) {
		this.helpUrl = helpUrl;
	}
	public String getManualFilePath() {
		return manualFilePath;
	}
	public void setManualFilePath(String manualFilePath) {
		this.manualFilePath = manualFilePath;
	}
	public String getManualFileName() {
		return manualFileName;
	}
	public void setManualFileName(String manualFileName) {
		this.manualFileName = manualFileName;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	
	public InformationWork(){
		super();
		super.setType(TYPE_INFORMATION);
	}
	public InformationWork(String id, String name){
		super(id, name);
		super.setType(TYPE_INFORMATION);
	}
	public InformationWork(String id, String name, String desc, WorkCategory myCategory){
		super(id, name, TYPE_INFORMATION, desc, myCategory);
	}
}
