package net.smartworks.model.instance;

public class FieldData {

	private String fieldId;
	private String fieldType;
	private String value;
	private String groupId;
	private String[] fileNames;

	public FieldData() {
		super();
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String[] getFileNames() {
		return fileNames;
	}
	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}
	public FieldData(String fieldId, String fieldType, String value){
		super();
		this.fieldId = fieldId;
		this.fieldType = fieldType;
		this.value = value;
	}
}
