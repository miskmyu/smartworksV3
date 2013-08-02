package net.smartworks.model.report;

import net.smartworks.model.work.FormField;

public class TableReport extends Report {

	public static final TableReport[] DEFAULT_TABLES_INFORMATION = new TableReport[]{};
	public static final TableReport[] DEFAULT_TABLES_PROCESS = new TableReport[]{};
	public static final TableReport[] DEFAULT_TABLES_SCHEDULE = new TableReport[]{};
	public static final TableReport[] DEFAULT_TABLES_ALL_WORKS = new TableReport[]{};
	public static final TableReport[] DEFAULT_TABLES_ALL_PROCESSES = new TableReport[]{};
	public static final TableReport[] DEFAULT_TABLES_ALL_INFORMATIONS = new TableReport[]{};
	public static final TableReport[] DEFAULT_TABLES_ALL_SCHEDULES = new TableReport[]{};

	private FormField[] displayFields;
	private FormField sortingField = FormField.FIELD_CREATED_DATE;
	private boolean sortingAscend;
	private int pageSize;
	
	public FormField[] getDisplayFields() {
		return displayFields;
	}
	public void setDisplayFields(FormField[] displayFields) {
		this.displayFields = displayFields;
	}
	public FormField getSortingField() {
		return sortingField;
	}
	public void setSortingField(FormField sortingField) {
		this.sortingField = sortingField;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public boolean isSortingAscend() {
		return sortingAscend;
	}
	public void setSortingAscend(boolean sortingAscend) {
		this.sortingAscend = sortingAscend;
	}	
	
}
