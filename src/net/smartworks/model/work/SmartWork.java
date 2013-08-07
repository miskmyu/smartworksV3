package net.smartworks.model.work;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.filter.info.SearchFilterInfo;
import net.smartworks.model.report.info.ReportInfo;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.security.BuilderPolicy;
import net.smartworks.model.security.EditPolicy;
import net.smartworks.model.security.WritePolicy;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class SmartWork extends Work {

	public final static int TYPE_INFORMATION = 21;
	public final static int TYPE_PROCESS = 22;
	public final static int TYPE_SCHEDULE = 23;
	public final static int TYPE_REPORT = 24;
	
	public final static String ID_FILE_MANAGEMENT = "pkg_309666dd2bb5493c9d7e618b3a0aad96";
	public final static String ID_EVENT_MANAGEMENT = "pkg_c08a02b36192489fbc13fdb6bed6f5fc";
	public final static String ID_MEMO_MANAGEMENT = "pkg_d391d4cd01864b2cada59ab5a9b12cd5";
	public final static String ID_BOARD_MANAGEMENT = "pkg_62eeb90b11e1466b86d2d7c4dadf63ca";
	public final static String ID_USER_MANAGEMENT = "pkg_394ea78cec37434d922c73f09ab4b24e";
	public final static String ID_DEPARTMENT_MANAGEMENT = "pkg_c2156de59c14435bb551c61c1593a442";
	public final static String ID_GROUP_MANAGEMENT = "";
	public final static String ID_FORUM_MANAGEMENT = "pkg_af2c5abbdc694feab78b2706c31f3bde";
	public final static String ID_CONTACTS_MANAGEMENT = "pkg_dde3f719c50143cd821e6c46e8df8655";
	public final static String ID_MESSAGE_MANAGEMENT = "";
	public final static String ID_REPORT_MANAGEMENT = "reportManagement";
	public final static String ID_ALL_WORKS = "allSmartWorks";
	public final static String ID_SAVED_WORKS = "savedSmartWorks";

	public final static String ID_SERA_MISSION_MANAGEMENT = "pkg_dc3edb6efa47418cbd1f8fef889b4818";
	public final static String ID_SERA_MISSION_REPORT_MANAGEMENT = "pkg_8fc9ed30a64b467eb89fd35097cc6212";
	public final static String ID_SERA_NOTE_MANAGEMENT = "pkg_e4c34f837ea64b1c994d4827d8a4bb51";

	private WorkCategory myGroup = null;
	private WorkCategory myCategory = null;
	private AccessPolicy accessPolicy = new AccessPolicy();
	private WritePolicy writePolicy = new WritePolicy();
	private EditPolicy editPolicy = new EditPolicy();
	private BuilderPolicy builderPolicy = new BuilderPolicy();
	private String lastReportId;
	private ReportInfo[] reports;
	private String lastFilterId;
	private SearchFilterInfo[] searchFilters;
	private User creater;
	private LocalDate createdDate;
	private User lastModifier;
	private LocalDate lastModifiedDate;
	private boolean isRunning;
	private boolean isEditing;
	private User editingUser;
	private LocalDate editingStartDate;

	public String getFullpathName() {
		return this.myCategory.getName() + ((this.myGroup != null && this.myGroup.getId() != null) ? "▶" + this.myGroup.getName() : "") + "▶" + super.getName();
	}

	public String getPathName() {
		return this.myCategory.getName() + ((this.myGroup != null && this.myGroup.getId() != null) ? "▶" + this.myGroup.getName() : "");
	}

	public WorkCategory getMyGroup() {
		return myGroup;
	}
	public void setMyGroup(WorkCategory myGroup) {
		this.myGroup = myGroup;
	}
	public WorkCategory getMyCategory() {
		return myCategory;
	}
	public void setMyCategory(WorkCategory myCategory) {
		this.myCategory = myCategory;
	}
	public AccessPolicy getAccessPolicy() {
		return accessPolicy;
	}
	public void setAccessPolicy(AccessPolicy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}
	public WritePolicy getWritePolicy() {
		return writePolicy;
	}
	public void setWritePolicy(WritePolicy writePolicy) {
		this.writePolicy = writePolicy;
	}
	public EditPolicy getEditPolicy() {
		return editPolicy;
	}
	public void setEditPolicy(EditPolicy editPolicy) {
		this.editPolicy = editPolicy;
	}
	public BuilderPolicy getBuilderPolicy() {
		return builderPolicy;
	}
	public void setBuilderPolicy(BuilderPolicy builderPolicy) {
		this.builderPolicy = builderPolicy;
	}
	public String getLastReportId() {
		return lastReportId;
	}
	public void setLastReportId(String lastReportId) {
		this.lastReportId = lastReportId;
	}
	public String getLastFilterId() {
		return lastFilterId;
	}
	public void setLastFilterId(String lastFilterId) {
		this.lastFilterId = lastFilterId;
	}

	public SearchFilterInfo[] getSearchFilters() {
		return searchFilters;
	}
	public void setSearchFilters(SearchFilterInfo[] searchFilters) {
		this.searchFilters = searchFilters;
	}
	public ReportInfo[] getReports() {
		return reports;
	}
	public void setReports(ReportInfo[] reports) {
		this.reports = reports;
	}
	public User getCreater() {
		return creater;
	}
	public void setCreater(User creater) {
		this.creater = creater;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	public User getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}
	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public boolean isEditing() {
		return isEditing;
	}
	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}
	public User getEditingUser() {
		return editingUser;
	}
	public void setEditingUser(User editingUser) {
		this.editingUser = editingUser;
	}
	public LocalDate getEditingStartDate() {
		return editingStartDate;
	}
	public void setEditingStartDate(LocalDate editingStartDate) {
		this.editingStartDate = editingStartDate;
	}

	public SmartWork() {
		super();
	}

	public SmartWork(String id, String name) {
		super(id, name);
	}

	public SmartWork(String id, String name, int type, String desc, WorkCategory myCategory) {
		super(id, name, type, desc);
		this.myCategory = myCategory;
	}

	public boolean isEditable(){
		if(isEditing || isRunning) return false;
		return true;
	}
	
	public boolean canIStop(){
		if(SmartUtil.isBlankObject(this.editingUser)) return false;
		String currentUserId = SmartUtil.getCurrentUser().getId();
		if(isEditing && currentUserId.equals(this.editingUser.getId()))
			return true;
		return false;
	}
	
	public boolean amIBuilderUser(){
		if(SmartUtil.getCurrentUser().getUserLevel() > User.USER_LEVEL_INTERNAL_USER) return true;
		if(SmartUtil.isBlankObject(builderPolicy) || !builderPolicy.isCustomChecked() || SmartUtil.isBlankObject(builderPolicy.getCustoms())) return false;
		String currentUserId = SmartUtil.getCurrentUser().getId();
		for(UserInfo user : builderPolicy.getCustoms())
			if(user.getId().equals(currentUserId))
				return true;
		return false;
	}

	public static String getWorkTypeName(int workType){
		switch(workType){
		case SmartWork.TYPE_INFORMATION:
			return SmartMessage.getString("common.title.information_work");
		case SmartWork.TYPE_PROCESS:
			return SmartMessage.getString("common.title.process_work");
		case SmartWork.TYPE_SCHEDULE:
			return SmartMessage.getString("common.title.schedule_work");
		}
		return "";
	}
}
