package net.smartworks.model.community.info;

import net.smartworks.model.instance.SortingField;
public class CommunityInfoList {
	
	public static final int TYPE_GROUP_INFO_LIST = 1; 
	public static final int TYPE_DEPARTMENT_INFO_LIST = 2; 
	public static final int TYPE_USER_INFO_LIST = 3; 
	
	private int type;
	private CommunityInfo[]	communityDatas;
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
	public CommunityInfo[] getCommunityDatas() {
		return communityDatas;
	}
	public void setCommunityDatas(CommunityInfo[] communityDatas) {
		this.communityDatas = communityDatas;
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
	public CommunityInfoList(){
		super();
	}
}
