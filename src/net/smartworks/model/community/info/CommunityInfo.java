package net.smartworks.model.community.info;

import net.smartworks.model.BaseObject;
import net.smartworks.model.community.Community;
import net.smartworks.model.community.Department;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.sera.info.MentorInfo;
import net.smartworks.model.sera.info.SeraUserInfo;
import net.smartworks.model.work.SmartWork;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.organization.model.SwoCompany;
import net.smartworks.server.engine.organization.model.SwoCompanyCond;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

public class CommunityInfo extends BaseObject {
	
	private String smallPictureName;
	private String bigPictureName;
	private boolean favorite;

	public String getSmallPictureName() {
		return smallPictureName;
	}
	public void setSmallPictureName(String smallPictureName) {
		this.smallPictureName = smallPictureName;
	}	
	public String getBigPictureName() {
		return bigPictureName;
	}
	public void setBigPictureName(String bigPictureName) {
		this.bigPictureName = bigPictureName;
	}	
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public String getOrgPicture() {
		if(CommonUtil.isEmpty(this.getBigPictureName())) {
			if(this.getClass().equals(UserInfo.class) || this.getClass().equals(SeraUserInfo.class) || this.getClass().equals(MentorInfo.class))
				return Community.NO_PICTURE_PATH + User.NO_USER_PICTURE + ".jpg";
			else if(this.getClass().equals(DepartmentInfo.class))
				return Community.NO_PICTURE_PATH + Department.DEFAULT_DEPART_PICTURE + ".gif";
			else if(this.getClass().equals(GroupInfo.class))
				return Community.NO_PICTURE_PATH + Group.DEFAULT_GROUP_PICTURE + ".gif";
		}
		return getPath() + this.getBigPictureName();
	}
	
	public String getMidPicture() {
		if(CommonUtil.isEmpty(this.getSmallPictureName())) {
			if(this.getClass().equals(UserInfo.class) || this.getClass().equals(SeraUserInfo.class) || this.getClass().equals(MentorInfo.class))
				return Community.NO_PICTURE_PATH + User.NO_USER_PICTURE + "_mid.jpg";
			else if(this.getClass().equals(DepartmentInfo.class))
				return Community.NO_PICTURE_PATH + Department.DEFAULT_DEPART_PICTURE + "_mid.gif";
			else if(this.getClass().equals(GroupInfo.class))
				return Community.NO_PICTURE_PATH + Group.DEFAULT_GROUP_PICTURE + "_mid.gif";
		}
		return getPath() + this.getSmallPictureName();
	}
	public String getMinPicture() {
		if(CommonUtil.isEmpty(this.getSmallPictureName())) {
			if(this.getClass().equals(UserInfo.class) || this.getClass().equals(SeraUserInfo.class) || this.getClass().equals(MentorInfo.class))
				return Community.NO_PICTURE_PATH + User.NO_USER_PICTURE + "_min.jpg";
			else if(this.getClass().equals(DepartmentInfo.class))
				return Community.NO_PICTURE_PATH + Department.DEFAULT_DEPART_PICTURE + "_min.gif";
			else if(this.getClass().equals(GroupInfo.class))
				return Community.NO_PICTURE_PATH + Group.DEFAULT_GROUP_PICTURE + "_min.gif";
		}
		return getPath() + this.getSmallPictureName();
	}
	public String getPath(){
		if(SmartUtil.getCurrentUser() == null || SmartUtil.getCurrentUser().isAnonymusUser()) {
			try {
				SwoCompanyCond cond = new SwoCompanyCond();
				cond.setPageNo(0);
				cond.setPageSize(1);
				SwoCompany[] company = SwManagerFactory.getInstance().getSwoManager().getCompanys("", cond, IManager.LEVEL_LITE);
				if (company == null) {
					return null;
				} else {
					return Community.PICTURE_PATH + company[0].getId() + "/" + Community.PROFILES_DIR + "/";
				}
			} catch (Exception e) {
				return null;
			}
		} else {
			return Community.PICTURE_PATH + SmartUtil.getCurrentUser().getCompanyId() + "/" + Community.PROFILES_DIR + "/";
		}
	}
	
	public int getSpaceType(){
		return CommunityInfo.getSpaceType(this);
	}
	
	public static int getSpaceType(CommunityInfo community){
		if(SmartUtil.isBlankObject(community)) return -1;
		if(community.getClass().equals(DepartmentInfo.class))
			return ISmartWorks.SPACE_TYPE_DEPARTMENT;
		else if(community.getClass().equals(GroupInfo.class))
			return ISmartWorks.SPACE_TYPE_GROUP;
		else if(community.getClass().equals(UserInfo.class))
			return ISmartWorks.SPACE_TYPE_USER;
		return -1;
		
	}
	
	public String getIconClass(){
		return CommunityInfo.getIconClass(CommunityInfo.getSpaceType(this));
	}
	
	public static String getIconClass(int communityType){
		if(communityType == ISmartWorks.SPACE_TYPE_DEPARTMENT)
			return Community.ICON_CLASS_DEPARTMENT;
		else if(communityType == ISmartWorks.SPACE_TYPE_GROUP)
			return Community.ICON_CLASS_GROUP;
		return "";
	}
	
	public String getListController(){
		return CommunityInfo.getListController(CommunityInfo.getSpaceType(this));
	}
	
	public static String getListController(int communityType){
		if(communityType == ISmartWorks.SPACE_TYPE_USER)
			return Community.CONTROLLER_USER_LIST;
		else if(communityType == ISmartWorks.SPACE_TYPE_DEPARTMENT)
			return Community.CONTROLLER_DEPARTMENT_LIST;
		else if(communityType == ISmartWorks.SPACE_TYPE_GROUP)
			return Community.CONTROLLER_GROUP_LIST;
		return "";
	}
	
	public String getSpaceController(){
		return CommunityInfo.getSpaceController(CommunityInfo.getSpaceType(this));
	}
	
	public static String getSpaceController(int communityType){
		if(communityType == ISmartWorks.SPACE_TYPE_USER)
			return Community.CONTROLLER_USER_SPACE;
		else if(communityType == ISmartWorks.SPACE_TYPE_DEPARTMENT)
			return Community.CONTROLLER_DEPARTMENT_SPACE;
		else if(communityType == ISmartWorks.SPACE_TYPE_GROUP)
			return Community.CONTROLLER_GROUP_SPACE;
		return "";
	}
	
	public String getListContextId(){
		return CommunityInfo.getListContextId(CommunityInfo.getSpaceType(this));
	}
	
	public static String getListContextId(int communityType){
		if(communityType == ISmartWorks.SPACE_TYPE_USER)
			return ISmartWorks.CONTEXT_PREFIX_USER_LIST + SmartWork.ID_USER_MANAGEMENT;
		else if(communityType == ISmartWorks.SPACE_TYPE_DEPARTMENT)
			return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_LIST + SmartWork.ID_DEPARTMENT_MANAGEMENT;
		else if(communityType == ISmartWorks.SPACE_TYPE_GROUP)
			return ISmartWorks.CONTEXT_PREFIX_GROUP_LIST + SmartWork.ID_GROUP_MANAGEMENT;
		return "";
	}	
	
	public String getSpaceContextId(){
		return CommunityInfo.getSpaceContextId(CommunityInfo.getSpaceType(this), getId());
	}
	
	public static String getSpaceContextId(int communityType, String communityId){
		if(communityType == ISmartWorks.SPACE_TYPE_USER)
			return ISmartWorks.CONTEXT_PREFIX_USER_SPACE + communityId;
		else if(communityType == ISmartWorks.SPACE_TYPE_DEPARTMENT)
			return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE + communityId;
		else if(communityType == ISmartWorks.SPACE_TYPE_GROUP)
			return ISmartWorks.CONTEXT_PREFIX_GROUP_SPACE + communityId;
		return "";
	}
	public CommunityInfo(){
		super();
	}
	public CommunityInfo(String id, String name){
		super(id, name);
	}

}