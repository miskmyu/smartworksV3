package net.smartworks.model.work.info;

import net.smartworks.model.instance.SortingField;
public class WorkInfoList {
	
	public static final int TYPE_APP_WORK_LIST = 1;
	
	private int type;
	private WorkInfo[]	workDatas;
	private SortingField sortedField;
	private int pageSize;
	private int	totalPages;
	private int currentPage;
	private int totalSize;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public WorkInfo[] getWorkDatas() {
		return workDatas;
	}
	public void setWorkDatas(WorkInfo[] workDatas) {
		this.workDatas = workDatas;
	}
	public SortingField getSortedField() {
		return sortedField;
	}
	public void setSortedField(SortingField sortedField) {
		this.sortedField = sortedField;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	public WorkInfoList(){
		super();
	}
}
