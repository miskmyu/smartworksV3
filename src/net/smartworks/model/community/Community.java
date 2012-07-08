package net.smartworks.model.community;

import net.smartworks.model.BaseObject;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.SeraUser;
import net.smartworks.model.work.SmartWork;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.organization.model.SwoCompany;
import net.smartworks.server.engine.organization.model.SwoCompanyCond;
import net.smartworks.server.engine.security.model.Login;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartConfUtil;
import net.smartworks.util.SmartUtil;

public class Community extends BaseObject {

	public static final String PICTURE_PATH = SmartConfUtil.getInstance().getImageServer();
	public static final String NO_PICTURE_PATH = "images/";
	public static final String PROFILES_DIR = "Profiles";
	public static final String COMMUNITY_USER = "User";
	public static final String COMMUNITY_DEPARTMENT = "Department";
	public static final String COMMUNITY_GROUP = "Group";
	
	public static final String CONTROLLER_USER_LIST = "user_list.sw";
	public static final String CONTROLLER_DEPARTMENT_LIST = "department_list.sw";
	public static final String CONTROLLER_GROUP_LIST = "group_list.sw";

	public static final String ICON_CLASS_DEPARTMENT = "icon_division_s";
	public static final String ICON_CLASS_GROUP = "icon_group_s";

	public static final String CONTROLLER_USER_SPACE = "user_space.sw";
	public static final String CONTROLLER_DEPARTMENT_SPACE = "department_space.sw";
	public static final String CONTROLLER_GROUP_SPACE = "group_space.sw";

	public static final String IMAGE_TYPE_ORIGINAL = "_origin";
	public static final String IMAGE_TYPE_THUMB = "_thumb";

	private String bigPictureName;
	private String smallPictureName;

	public String getBigPictureName() {
		return bigPictureName;
	}
	public void setBigPictureName(String bigPictureName) {
		this.bigPictureName = bigPictureName;
	}
	public String getSmallPictureName() {
		return smallPictureName;
	}
	public void setSmallPictureName(String smallPictureName) {
		this.smallPictureName = smallPictureName;
	}

	public String getOrgPicture() {
		if(!CommonUtil.isExistImage(getPath() + this.getBigPictureName())) {
			if(this.getClass().equals(Login.class) || this.getClass().equals(User.class) || this.getClass().equals(SeraUser.class) || this.getClass().equals(Mentor.class))
				return NO_PICTURE_PATH + User.NO_USER_PICTURE + ".jpg";
			else if(this.getClass().equals(Department.class))
				return NO_PICTURE_PATH + Department.DEFAULT_DEPART_PICTURE + ".gif";
			else if(this.getClass().equals(Group.class))
				return NO_PICTURE_PATH + Group.DEFAULT_GROUP_PICTURE + ".gif";
		}
		return getPath() + this.getBigPictureName();
	}

	public String getMidPicture() {
		if(!CommonUtil.isExistImage(getPath() + this.getSmallPictureName())) {
			if(this.getClass().equals(Login.class) || this.getClass().equals(User.class) || this.getClass().equals(SeraUser.class) || this.getClass().equals(Mentor.class))
				return NO_PICTURE_PATH + User.NO_USER_PICTURE + "_mid.jpg";
			else if(this.getClass().equals(Department.class))
				return NO_PICTURE_PATH + Department.DEFAULT_DEPART_PICTURE + "_mid.gif";
			else if(this.getClass().equals(Group.class))
				return NO_PICTURE_PATH + Group.DEFAULT_GROUP_PICTURE + "_mid.gif";
		}
		return getPath() + this.getSmallPictureName();
	}

	public String getMinPicture() {
		if(!CommonUtil.isExistImage(getPath() + this.getSmallPictureName())) {
			if(this.getClass().equals(Login.class) || this.getClass().equals(User.class) || this.getClass().equals(SeraUser.class) || this.getClass().equals(Mentor.class))
				return NO_PICTURE_PATH + User.NO_USER_PICTURE + "_min.jpg";
			else if(this.getClass().equals(Department.class))
				return NO_PICTURE_PATH + Department.DEFAULT_DEPART_PICTURE + "_min.gif";
			else if(this.getClass().equals(Group.class))
				return NO_PICTURE_PATH + Group.DEFAULT_GROUP_PICTURE + "_min.gif";
		}
		return getPath() + this.getSmallPictureName();
	}

	public String getPath() {
		if(SmartUtil.getCurrentUser() == null || SmartUtil.getCurrentUser().isAnonymusUser()) {
			try {
				SwoCompanyCond cond = new SwoCompanyCond();
				cond.setPageNo(0);
				cond.setPageSize(1);
				SwoCompany[] company = SwManagerFactory.getInstance().getSwoManager().getCompanys("", cond, IManager.LEVEL_LITE);
				if (company == null) {
					return null;
				} else {
					return PICTURE_PATH + company[0].getId() + "/" + PROFILES_DIR + "/";
				}
			} catch (Exception e) {
				return null;
			}
		} else {
			return PICTURE_PATH + SmartUtil.getCurrentUser().getCompanyId() + "/" + PROFILES_DIR + "/";	
		}
	}
	public String getIconClass(){
		if(this.getClass().equals(Department.class))
			return Community.ICON_CLASS_DEPARTMENT;
		else if(this.getClass().equals(Group.class))
			return Community.ICON_CLASS_GROUP;
		return "";
	}
	public String getListController(){
		if(this.getClass().equals(User.class))
			return Community.CONTROLLER_USER_LIST;
		else if(this.getClass().equals(Department.class))
			return Community.CONTROLLER_DEPARTMENT_LIST;
		else if(this.getClass().equals(Group.class))
			return Community.CONTROLLER_GROUP_LIST;
		return "";
	}
	public String getSpaceController(){
		if(this.getClass().equals(User.class))
			return Community.CONTROLLER_USER_SPACE;
		else if(this.getClass().equals(Department.class))
			return Community.CONTROLLER_DEPARTMENT_SPACE;
		else if(this.getClass().equals(Group.class))
			return Community.CONTROLLER_GROUP_SPACE;
		return "";
	}
	public String getListContextId(){
		if(this.getClass().equals(User.class))
			return ISmartWorks.CONTEXT_PREFIX_USER_LIST + SmartWork.ID_USER_MANAGEMENT;
		else if(this.getClass().equals(Department.class))
			return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_LIST + SmartWork.ID_DEPARTMENT_MANAGEMENT;
		else if(this.getClass().equals(Group.class))
			return ISmartWorks.CONTEXT_PREFIX_GROUP_LIST + SmartWork.ID_GROUP_MANAGEMENT;
		return "";
	}
	public String getSpaceContextId(){
		if(this.getClass().equals(User.class))
			return ISmartWorks.CONTEXT_PREFIX_USER_SPACE + getId();
		else if(this.getClass().equals(Department.class))
			return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE + getId();
		else if(this.getClass().equals(Group.class))
			return ISmartWorks.CONTEXT_PREFIX_GROUP_SPACE + getId();
		return "";
	}
	public Community(){
		super();
	}
	public Community(String id, String name){
		super(id, name);
	}

}