package net.smartworks.server.service.impl;

import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.Department;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.InstanceSpace;
import net.smartworks.model.community.OverflowGroupMemberException;
import net.smartworks.model.community.User;
import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.community.info.CommunityInfo;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.CommunityInfoList;
import net.smartworks.model.community.info.GroupMemberList;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.filter.Condition;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.FieldData;
import net.smartworks.model.instance.InformationWorkInstance;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.instance.info.IWInstanceInfo;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.mail.MailAccount;
import net.smartworks.model.notice.Notice;
import net.smartworks.model.sera.Course;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkCategoryInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.server.engine.authority.model.SwaDepartment;
import net.smartworks.server.engine.authority.model.SwaDepartmentCond;
import net.smartworks.server.engine.authority.model.SwaGroup;
import net.smartworks.server.engine.authority.model.SwaGroupCond;
import net.smartworks.server.engine.common.loginuser.manager.ILoginUserManager;
import net.smartworks.server.engine.common.loginuser.model.LoginUser;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItem;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItemList;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItemListCond;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Filters;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.SmartServerConstant;
import net.smartworks.server.engine.common.searcher.manager.ISchManager;
import net.smartworks.server.engine.common.searcher.model.SchUser;
import net.smartworks.server.engine.common.searcher.model.SchWorkspace;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.StringUtil;
import net.smartworks.server.engine.common.util.id.IDCreator;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.docfile.model.IFileModel;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainFieldConstants;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordExtend;
import net.smartworks.server.engine.infowork.form.model.SwfField;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfFormCond;
import net.smartworks.server.engine.infowork.form.model.SwfFormModel;
import net.smartworks.server.engine.mail.manager.IMailManager;
import net.smartworks.server.engine.mail.model.MailAccountCond;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoDepartment;
import net.smartworks.server.engine.organization.model.SwoDepartmentCond;
import net.smartworks.server.engine.organization.model.SwoGroup;
import net.smartworks.server.engine.organization.model.SwoGroupCond;
import net.smartworks.server.engine.organization.model.SwoGroupMember;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.organization.model.SwoUserCond;
import net.smartworks.server.engine.organization.model.SwoUserExtend;
import net.smartworks.server.engine.pkg.model.PkgPackage;
import net.smartworks.server.engine.pkg.model.PkgPackageCond;
import net.smartworks.server.engine.publishnotice.manager.IPublishNoticeManager;
import net.smartworks.server.engine.publishnotice.model.PublishNotice;
import net.smartworks.server.engine.security.model.Login;
import net.smartworks.server.engine.sera.manager.ISeraManager;
import net.smartworks.server.engine.sera.model.CourseDetail;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.server.service.util.SearchParallelProcessing;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.Semaphore;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CommunityServiceImpl implements ICommunityService {
	
	protected final Log logger = LogFactory.getLog(getClass());

	private static ISwoManager getSwoManager() {
		return SwManagerFactory.getInstance().getSwoManager();
	}
	private static IDocFileManager getDocManager() {
		return SwManagerFactory.getInstance().getDocManager();
	}
	private static ISchManager getSchManager() {
		return SwManagerFactory.getInstance().getSchManager();
	}
	private static ILoginUserManager getLoginUserManager() {
		return SwManagerFactory.getInstance().getLoginUserManager();
	}
	private static ISwdManager getSwdManager() {
		return SwManagerFactory.getInstance().getSwdManager();
	}
	private static ISeraManager getSeraManager() {
		return SwManagerFactory.getInstance().getSeraManager();
	}
	private static IPublishNoticeManager getPublishNoticeManager() {
		return SwManagerFactory.getInstance().getPublishNoticeManager();
	}
	private static IMailManager getMailManager() {
		return SwManagerFactory.getInstance().getMailManager();
	}

	@Autowired
	private SessionRegistry sessionRegistry;

	//@Autowired
	private ISeraService seraService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#getMyDepartments(java.lang.String
	 * )
	 */
	@Override
	public DepartmentInfo[] getMyDepartments() throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();

			SwoUserExtend userExtend = getSwoManager().getUserExtend(user.getId(), user.getId(), true);
			String myDeptId = userExtend.getDepartmentId();
			List<SwoDepartment> deptList = new ArrayList<SwoDepartment>();
			//겸직적용
			String adjunctDeptids = userExtend.getAdjunctDeptIds();
			if (!CommonUtil.isEmpty(adjunctDeptids)) {
				String[] ajtDeptInfo = StringUtils.tokenizeToStringArray(adjunctDeptids, ";");
				for (int i = 0; i < ajtDeptInfo.length; i++) {
					String[] ajtDeptIdInfo = StringUtils.tokenizeToStringArray(ajtDeptInfo[i], "|");
					String deptId = ajtDeptIdInfo[0];
					String position = ajtDeptIdInfo[1];
					getDeptTreeByDeptId(deptList, deptId);
				}
			}
			getDeptTreeByDeptId(deptList, myDeptId);
			DepartmentInfo[] deptInfos = new DepartmentInfo[deptList.size()];
			int index = deptList.size() - 1;

			List myFavorityCommunityList = new ArrayList();
			String companyId = user.getCompanyId();
			String userId = user.getId();
			ItmMenuItemListCond menuItemListCond = new ItmMenuItemListCond();
			menuItemListCond.setUserId(userId);
			ItmMenuItemList menuItemList = SwManagerFactory.getInstance().getItmManager().getMenuItemList(userId, menuItemListCond, IManager.LEVEL_ALL);
			if (menuItemList != null) {
				ItmMenuItem[] menuItems = menuItemList.getMenuItems();
				if (!CommonUtil.isEmpty(menuItems)) {
					for (int i = 0; i < menuItems.length; i++) {
						ItmMenuItem menuItem = menuItems[i];
						if (menuItem == null)
							continue;
						myFavorityCommunityList.add(menuItem.getPackageId());	
					}
				}
			}
			
			for (int i = 0; i < deptList.size(); i++) {
				DepartmentInfo deptInfo = new DepartmentInfo();
				SwoDepartment swDept = deptList.get(i);
				deptInfo.setId(swDept.getId());
				deptInfo.setName(swDept.getName());
				deptInfo.setDesc(swDept.getDescription());
				if (myFavorityCommunityList.contains(deptInfo.getId()))
					deptInfo.setFavorite(true);
				deptInfos[index--] = deptInfo;
			}
			return deptInfos;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}
	private void getDeptTreeByDeptId(List<SwoDepartment> deptList, String deptId) throws Exception {

		try{
			SwoDepartment dept = getSwoManager().getDepartment("", deptId, IManager.LEVEL_LITE);
			if (dept == null)
				return;
			if (!deptList.contains(dept))
				deptList.add(dept);
			if (!dept.getParentId().equalsIgnoreCase("root")) {
				getDeptTreeByDeptId(deptList, dept.getParentId());
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	@Override
	public DepartmentInfo[] getMyChildDepartments() throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			SwoUser swoUser = getSwoManager().getUser("", userId, IManager.LEVEL_LITE);
			String myDeptId = null;
			if(!CommonUtil.isEmpty(swoUser))
				myDeptId = swoUser.getDeptId();
			else return null;
			List<SwoDepartment> deptList = new ArrayList<SwoDepartment>();
			SwoDepartment swoDepartment = getSwoManager().getDepartment("", myDeptId, IManager.LEVEL_LITE);
			deptList.add(swoDepartment);
			getChildDeptTreeByDeptId(deptList, myDeptId);

			DepartmentInfo[] departmentInfos = new DepartmentInfo[deptList.size()];
			for(int i=0; i<deptList.size(); i++) {
				DepartmentInfo departmentInfo = new DepartmentInfo();
				SwoDepartment department = deptList.get(i);
				String departmentId = department.getId();
				departmentInfo = ModelConverter.getDepartmentInfoByDepartmentId(departmentId);
				departmentInfos[i] = departmentInfo;	
			}
			return departmentInfos;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	private void getChildDeptTreeByDeptId(List<SwoDepartment> deptList, String deptId) throws Exception {
		try{
			SwoDepartmentCond swoDepartmentCond = new SwoDepartmentCond();
			swoDepartmentCond.setParentId(deptId);
			SwoDepartment[] swoDepartments = getSwoManager().getDepartments("", swoDepartmentCond, IManager.LEVEL_LITE);
			if (CommonUtil.isEmpty(swoDepartments))
				return;
			for(SwoDepartment swoDepartment : swoDepartments) {
				deptList.add(swoDepartment);
				getChildDeptTreeByDeptId(deptList, swoDepartment.getId());
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#getDepartmentById(java.lang.String
	 * )
	 */
	@Override
	public Department getDepartmentById(String departmentId) throws Exception {
		try{
			if (CommonUtil.isEmpty(departmentId))
				return null;
	
			return ModelConverter.getDepartmentByDepartmentId(departmentId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#getMyGroups(java.lang.String)
	 */
	@Override
	public GroupInfo[] getMyGroups() throws Exception {
		try{
			List<GroupInfo> groupInfoList = new ArrayList<GroupInfo>();
			User user = SmartUtil.getCurrentUser();
			SwoGroupCond swoGroupCond = new SwoGroupCond();

			SwoGroupMember swoGroupMember = new SwoGroupMember();       
			swoGroupMember.setUserId(user.getId());		
			SwoGroupMember[] swoGroupMembers = new SwoGroupMember[1];
			swoGroupMembers[0] = swoGroupMember;
			swoGroupCond.setSwoGroupMembers(swoGroupMembers);
			swoGroupCond.setOrders(new Order[]{new Order("creationDate", false)});
			SwoGroup[] swoGroups = getSwoManager().getGroups(user.getId(), swoGroupCond, IManager.LEVEL_ALL);
			
			
			List myFavorityCommunityList = new ArrayList();
			String companyId = user.getCompanyId();
			String userId = user.getId();
			ItmMenuItemListCond menuItemListCond = new ItmMenuItemListCond();
			menuItemListCond.setUserId(userId);
			ItmMenuItemList menuItemList = SwManagerFactory.getInstance().getItmManager().getMenuItemList(userId, menuItemListCond, IManager.LEVEL_ALL);
			if (menuItemList != null) {
				ItmMenuItem[] menuItems = menuItemList.getMenuItems();
				if (!CommonUtil.isEmpty(menuItems)) {
					for (int i = 0; i < menuItems.length; i++) {
						ItmMenuItem menuItem = menuItems[i];
						if (menuItem == null)
							continue;
						myFavorityCommunityList.add(menuItem.getPackageId());	
					}
				}
			}
			
			if(swoGroups != null) {
				for(SwoGroup swoGroup : swoGroups) {
//					GroupInfo groupInfo = ModelConverter.getGroupInfoByGroupId(swoGroup.getId());
					GroupInfo groupInfo = ModelConverter.getGroupInfoBySwoGroup(null, swoGroup);
					if (myFavorityCommunityList.contains(groupInfo.getId()))
						groupInfo.setFavorite(true);
					groupInfoList.add(groupInfo);
				}
				GroupInfo[] groupInfos = new GroupInfo[groupInfoList.size()];
				groupInfoList.toArray(groupInfos);
				return groupInfos;
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			return null;			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#getGroupById(java.lang.String)
	 */
	@Override
	public Group getGroupById(String groupId) throws Exception {
		try{
			if (CommonUtil.isEmpty(groupId))
				return null;
			return ModelConverter.getGroupByGroupId(groupId);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private SwoUser[] getUsersByDeptId(String userId, String departmentId) throws Exception {
		
		SwoUserCond swoUserCond = new SwoUserCond();
		swoUserCond.setDeptId(departmentId);
		
		return getSwoManager().getUsers(userId, swoUserCond, IManager.LEVEL_LITE);
		
	}
	
	private SwoGroupMember[] getUsersByGroupId(String userId, String groupId) throws Exception {
		
		SwoGroup group = getSwoManager().getGroup(userId, groupId, IManager.LEVEL_ALL);
		
		if(group == null){
			return null;
		}else{
			return group.getSwoGroupMembers();
		}		
	}
		
	public String setGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			Map<String, Object> frmNewGroupProfile = (Map<String, Object>)requestBody.get("frmNewGroupProfile");
	
			Set<String> keySet = frmNewGroupProfile.keySet();
			Iterator<String> itr = keySet.iterator();
			
            //그룹 유저가 중복으로 들어가는걸 방지 하기 위한 리스트
			List groupList = new ArrayList();
			List<Map<String, String>> users = null;
			List<Map<String, String>> files = null;
			String groupId = null;
			String txtGroupName = null;
			String txtaGroupDesc = null;
			String selGroupProfileType = null;
			String txtGroupLeader = null;
			String imgGroupProfile = null;
			String groupUserId = null;
			String groupFileId = null;
			String groupFileName = null;

			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmNewGroupProfile.get(fieldId);
				if (fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					if(fieldId.equals("txtGroupMembers")) {
						users = (ArrayList<Map<String,String>>)valueMap.get("users");
					} else if(fieldId.equals("imgGroupProfile")) {
						files = (ArrayList<Map<String,String>>)valueMap.get("files");
					}
				} else if(fieldValue instanceof String) {					
					if(fieldId.equals("groupId")) {
						groupId = (String)frmNewGroupProfile.get("groupId");
					} else if(fieldId.equals("txtGroupName")) {
						txtGroupName = (String)frmNewGroupProfile.get("txtGroupName");
					} else if(fieldId.equals("txtaGroupDesc")) {
						txtaGroupDesc = (String)frmNewGroupProfile.get("txtaGroupDesc");
					} else if(fieldId.equals("selGroupProfileType")) {
						selGroupProfileType = (String)frmNewGroupProfile.get("selGroupProfileType");
						if(selGroupProfileType.equals(Group.GROUP_TYPE_OPEN))
							selGroupProfileType = SwoGroup.GROUP_TYPE_PUBLIC;
						else
							selGroupProfileType = SwoGroup.GROUP_TYPE_PRIVATE;
					} else if(fieldId.equals("txtGroupLeader")) {
						txtGroupLeader = (String)frmNewGroupProfile.get("txtGroupLeader");
					}
				}
			}

			SwoGroup swoGroup = null;

			if(groupId != null) {
				swoGroup = getSwoManager().getGroup(user.getId(), groupId, IManager.LEVEL_ALL);
			} else {
				swoGroup = new SwoGroup();
				swoGroup.setId(IDCreator.createId(SmartServerConstant.GROUP_APPR));
			}
			
			if(!CommonUtil.isEmpty(txtGroupLeader)) {
				SwoGroupMember swoGroupMember = new SwoGroupMember();
				swoGroupMember.setUserId(txtGroupLeader);
				swoGroupMember.setJoinType(SwoGroupMember.JOINTYPE_GROUPLEADER);
				swoGroupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_COMPLETE);
				swoGroupMember.setJoinDate(new LocalDate());
				swoGroup.addGroupMember(swoGroupMember);
			}

			if(!CommonUtil.isEmpty(users)) {
				for(int i=0; i < users.subList(0, users.size()).size(); i++) {
					Map<String, String> userMap = users.get(i);
					groupUserId = userMap.get("id");
					
					//그룹안에 유저를 추가할 때
					if(userMap.get("id").matches(".*@.*")){
						if(!txtGroupLeader.equals(groupUserId)) {
							SwoGroupMember swoGroupMember = new SwoGroupMember();
							swoGroupMember.setUserId(groupUserId);
							swoGroupMember.setJoinType(SwoGroupMember.JOINTYPE_INVITE);
							swoGroupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_COMPLETE);
							swoGroupMember.setJoinDate(new LocalDate());
							//중복유저추가 방지소스
							if(!groupList.contains(groupUserId)){
								swoGroup.addGroupMember(swoGroupMember);
								groupList.add(groupUserId);	
							}
						}
					}else{
						//그룹안에 부서를 추가할 때
						String departmentId = groupUserId;
						SwoUser[] deptUsers = getUsersByDeptId("", departmentId);
						if(deptUsers != null){
							int getGroupDeptIdlength = deptUsers.length;
							for (int j = 0; j < getGroupDeptIdlength; j++) {
								String deptUser = deptUsers[j].getId();
								if(!txtGroupLeader.equals(deptUser)){
									SwoGroupMember swoGroupMember = new SwoGroupMember();
									swoGroupMember.setUserId(deptUser);
									swoGroupMember.setJoinType(SwoGroupMember.JOINTYPE_INVITE);
									swoGroupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_READY);
									swoGroupMember.setJoinDate(new LocalDate());
									
									if(!groupList.contains(deptUser)){
										swoGroup.addGroupMember(swoGroupMember);
										groupList.add(deptUser);	
									}			
								}
							}
						}else{
							//그룹안에 그룹을 추가할 때
							String GroupId = groupUserId;
							SwoGroupMember[] Users = getUsersByGroupId("",GroupId);
							for (int j = 0; j < Users.length; j++) {
								String groupUser = Users[j].getUserId();
								if(!txtGroupLeader.equals(groupUser)){
									SwoGroupMember swoGroupMember = new SwoGroupMember();
									swoGroupMember.setUserId(groupUser);
									swoGroupMember.setJoinType(SwoGroupMember.JOINTYPE_INVITE);
									swoGroupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_READY);
									swoGroupMember.setJoinDate(new LocalDate());
									
									if(!groupList.contains(groupUser)){
										swoGroup.addGroupMember(swoGroupMember);
										groupList.add(groupUser);	
									}
								}
							}				
						}
					}
				}
			}

			if(!files.isEmpty()) {
				for(int i=0; i < files.subList(0, files.size()).size(); i++) {
					Map<String, String> fileMap = files.get(i);
					groupFileId = fileMap.get("fileId");
					groupFileName = fileMap.get("fileName");
					imgGroupProfile = getDocManager().insertProfilesFile(groupFileId, groupFileName, swoGroup.getId());
					swoGroup.setPicture(imgGroupProfile);
				}
			}

			swoGroup.setCompanyId(user.getCompanyId());

			SwoGroupCond swoGroupCond = new SwoGroupCond();
			swoGroupCond.setNoId(swoGroup.getId());
			swoGroupCond.setName(txtGroupName);

			long totalCount = getSwoManager().getGroupSize(user.getId(), swoGroupCond);

			if(totalCount > 0)
				throw new DuplicateKeyException("Duplicated Group Name!!");

			swoGroup.setName(txtGroupName);
			swoGroup.setDescription(txtaGroupDesc);
			swoGroup.setStatus(SwoGroup.GROUP_STATUS_OPEN);
			swoGroup.setGroupType(selGroupProfileType);
			swoGroup.setGroupLeader(txtGroupLeader);

			getSwoManager().setGroup(user.getId(), swoGroup, IManager.LEVEL_ALL);

			groupId = swoGroup.getId();

			if (CommonUtil.isEmpty(groupId))
				return null;
			return groupId;

		} catch (DuplicateKeyException dke) {
			throw new DuplicateKeyException("Duplicated Group Name!!");
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#getUserById(java.lang.String)
	 */
	@Override
	public User getUserById(String userId) throws Exception {

		try{
			if (CommonUtil.isEmpty(userId))
				return null;
	
			return ModelConverter.getUserByUserId(userId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#searchCommunityList(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public WorkSpaceInfo[] searchCommunity(String key, HttpServletRequest request) throws Exception {

		try{
			if (CommonUtil.isEmpty(key))
				return null;
	
			User cUser = SmartUtil.getCurrentUser();
	
			SchWorkspace[] workSpaceInfos = getSchManager().getSchWorkspace(cUser.getCompanyId(), cUser.getId(), key);
			
			if (CommonUtil.isEmpty(workSpaceInfos))
				return null;
			
			List<DepartmentInfo> deptList = new ArrayList<DepartmentInfo>();
			List<GroupInfo> groupList = new ArrayList<GroupInfo>();
			List<UserInfo> userList = new ArrayList<UserInfo>();

			UserInfo[] availableChatters = getAvailableChatter(request);

			for (int i=0; i < workSpaceInfos.length; i++) {
				SchWorkspace workSpaceInfo = workSpaceInfos[i];
				
				String type = workSpaceInfo.getType();

				if (type.equalsIgnoreCase("user")) {
					UserInfo userInfo = new UserInfo();
					userInfo.setOnline(false);
					String userId = workSpaceInfo.getId();
					if(!CommonUtil.isEmpty(availableChatters)) {
						for(UserInfo availableChatter : availableChatters) {
							if(userId.equals(availableChatter.getId()))
								userInfo.setOnline(true);
						}
					}
					userInfo.setId(userId);
					userInfo.setName(workSpaceInfo.getName());
					userInfo.setNickName(workSpaceInfo.getUserNickName());
					userInfo.setPosition(workSpaceInfo.getUserPosition());
					String picture = workSpaceInfo.getUserPicture();
					if(picture != null && !picture.equals("")) {
						String extension = picture.lastIndexOf(".") > 1 ? picture.substring(picture.lastIndexOf(".") + 1) : null;
						String pictureId = picture.substring(0, (picture.length() - extension.length())-1);
						userInfo.setSmallPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
					}
	
					DepartmentInfo userDeptInfo = new DepartmentInfo();
					userDeptInfo.setId(workSpaceInfo.getUserDeptId());
					userDeptInfo.setName(workSpaceInfo.getUserDeptName());
					userDeptInfo.setDesc(workSpaceInfo.getUserDeptDesc());
					
					userInfo.setDepartment(userDeptInfo);
					
					userList.add(userInfo);
				} else if (type.equalsIgnoreCase("department")) {
					DepartmentInfo deptInfo = new DepartmentInfo();
	
					deptInfo.setId(workSpaceInfo.getId());
					deptInfo.setName(workSpaceInfo.getName());
					deptInfo.setDesc(workSpaceInfo.getDescription());
					deptInfo.setFullpathName(ModelConverter.getFullpathNameByDepartmentId(workSpaceInfo.getId()));
					
					deptList.add(deptInfo);
				} else if (type.equalsIgnoreCase("group")) {
					GroupInfo groupInfo = new GroupInfo();
					
					groupInfo.setId(workSpaceInfo.getId());
					groupInfo.setName(workSpaceInfo.getName());
					groupInfo.setDesc(workSpaceInfo.getDescription());
					
					groupList.add(groupInfo);
				}
			}
			
			WorkSpaceInfo[] schWorkspaces = new WorkSpaceInfo[deptList.size() + groupList.size() + userList.size()];
			
			int j = 0;
			
			for (int i = 0; i < deptList.size(); i++) {
				DepartmentInfo dept = deptList.get(i);
				schWorkspaces[j++] = dept;
			}
			for (int i = 0; i < groupList.size(); i++) {
				GroupInfo group = groupList.get(i);
				schWorkspaces[j++] = group;
			}
			for (int i = 0; i < userList.size(); i++) {
				UserInfo user = userList.get(i);
				schWorkspaces[j++] = user;
			}
			
			return schWorkspaces;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public DepartmentInfo[] searchDepartment(String key, HttpServletRequest request) throws Exception {

		try{
			if (CommonUtil.isEmpty(key))
				return null;
	
			User cUser = SmartUtil.getCurrentUser();
	
			SchWorkspace[] workSpaceInfos = getSchManager().getSchWorkspace(cUser.getCompanyId(), cUser.getId(), key);
			
			if (CommonUtil.isEmpty(workSpaceInfos))
				return null;
			
			List<DepartmentInfo> deptList = new ArrayList<DepartmentInfo>();

			UserInfo[] availableChatters = getAvailableChatter(request);

			for (int i=0; i < workSpaceInfos.length; i++) {
				SchWorkspace workSpaceInfo = workSpaceInfos[i];
				
				String type = workSpaceInfo.getType();

				if (type.equalsIgnoreCase("department")) {
					DepartmentInfo deptInfo = new DepartmentInfo();
	
					deptInfo.setId(workSpaceInfo.getId());
					deptInfo.setName(workSpaceInfo.getName());
					deptInfo.setDesc(workSpaceInfo.getDescription());

					deptInfo.setFullpathName(ModelConverter.getFullpathNameByDepartmentId(workSpaceInfo.getId()));
					
					deptList.add(deptInfo);
				} 
			}
			
			DepartmentInfo[] schDepartments = new DepartmentInfo[deptList.size()];
			
			for (int i = 0; i < deptList.size(); i++) {
				DepartmentInfo dept = deptList.get(i);
				schDepartments[i] = dept;
			}
			
			return schDepartments;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#searchCommunityMemberList(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public UserInfo[] searchCommunityMember(String communityId, String key) throws Exception {

		try{
			if (CommonUtil.isEmpty(communityId) || CommonUtil.isEmpty(key))
				return null;
	
			User cUser = SmartUtil.getCurrentUser();
	
			SchUser[] schUsers = getSchManager().getSchCommunityMember(cUser.getCompanyId(), cUser.getId(), communityId, key);
			List<UserInfo> userList = new ArrayList<UserInfo>();
	
			if(schUsers != null) {
				for(SchUser schUser : schUsers) {
					UserInfo userInfo = new UserInfo();
					userInfo.setId(schUser.getId());
					userInfo.setName(schUser.getName());
					userInfo.setPosition(schUser.getPosition());
					userInfo.setRole(schUser.getRole());
					DepartmentInfo departmentInfo = new DepartmentInfo();
					departmentInfo.setId(schUser.getDeptId());
					departmentInfo.setName(schUser.getDeptName());
					departmentInfo.setDesc(schUser.getDeptDesc());
					userInfo.setDepartment(departmentInfo);
					userList.add(userInfo);
				}
		
				UserInfo[] userInfos = new UserInfo[userList.size()];
				userList.toArray(userInfos);
	
				return userInfos;
			}
			return null;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#getWorkSpaceById(java.lang.String
	 * )
	 */
	@Override
	public WorkSpace getWorkSpaceById(String workSpaceId) throws Exception {

		try{
			if (CommonUtil.isEmpty(workSpaceId))
				return SmartUtil.getCurrentUser();
			
			String type = getSwoManager().getTypeByWorkspaceId(workSpaceId);
	
			if(type != null) {
				if(type.equalsIgnoreCase(Community.COMMUNITY_USER)) {
					User user = this.getUserById(workSpaceId);
					return user;
				} else if(type.equalsIgnoreCase(Community.COMMUNITY_DEPARTMENT)) {
					Department department = this.getDepartmentById(workSpaceId);
					return department;
				} else if(type.equalsIgnoreCase(Community.COMMUNITY_GROUP)) {
					Group group = this.getGroupById(workSpaceId);
					return group;
				} else {
					return new InstanceSpace(workSpaceId, null);
					
				}
			}

			return SmartUtil.getCurrentUser();
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return SmartUtil.getCurrentUser();			
			// Exception Handling Required			
		}
	}

	@Override
	public WorkSpaceInfo getWorkSpaceInfoById(String workSpaceId) throws Exception {

		try{
			if (CommonUtil.isEmpty(workSpaceId))
				return null;
			
			String type = getSwoManager().getTypeByWorkspaceId(workSpaceId);
	
			if(type != null) {
				if(type.equalsIgnoreCase(Community.COMMUNITY_USER)) {
					UserInfo userInfo = ModelConverter.getUserInfoByUserId(workSpaceId);
					return userInfo;
				} else if(type.equalsIgnoreCase(Community.COMMUNITY_DEPARTMENT)) {
					DepartmentInfo departmentInfo = ModelConverter.getDepartmentInfoByDepartmentId(workSpaceId);
					return departmentInfo;
				} else if(type.equalsIgnoreCase(Community.COMMUNITY_GROUP)) {
					GroupInfo groupInfo = ModelConverter.getGroupInfoByGroupId(workSpaceId);
					return groupInfo;
				}
			}

			return null;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.smartworks.service.impl.ISmartWorks#getAvailableChatter()
	 */
	@Override
	public UserInfo[] getAvailableChatter(HttpServletRequest request) throws Exception {

		try{
		 	User user = SmartUtil.getCurrentUser();
		 	String userId = user.getId();

			UserInfo[] userInfos = null;
			List<UserInfo> userInfoList = new ArrayList<UserInfo>();

			getLoginUserManager().deleteAllLoginUser(userId);

			List<Object> allPrincipalList = sessionRegistry.getAllPrincipals();

			Map<String, LoginUser> connectionUserMap = new HashMap<String, LoginUser>();
			if(allPrincipalList.size() > 0) {
				for(Object allPrincipal : allPrincipalList) {
					String connectionUserId = ((Login)allPrincipal).getId();
					LoginUser loginUser = new LoginUser(connectionUserId, new LocalDate());
					connectionUserMap.put(connectionUserId, loginUser);
				}
			}
			if(connectionUserMap.size() > 0) {
				for(Map.Entry<String, LoginUser> entry : connectionUserMap.entrySet()) {
					String loginUserId = (String)entry.getKey();
					LoginUser loginUser = (LoginUser)entry.getValue();
					//getLoginUserManager().createLoginUser(loginUserId, loginUser);
				}
			}

			LoginUser[] loginUsers = getLoginUserManager().getLoginUsers(userId, null, IManager.LEVEL_LITE);

			if(!CommonUtil.isEmpty(loginUsers)) {
				for(LoginUser loginUser : loginUsers) {
					String loginId = loginUser.getUserId();
					UserInfo userInfo = ModelConverter.getUserInfoByUserId(loginId);
					userInfo.setOnline(true);
					userInfoList.add(userInfo);
				}
			}
			if(userInfoList.size() > 0) {
				userInfos = new UserInfo[userInfoList.size()];
				userInfoList.toArray(userInfos);
			}

			return userInfos;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#searchAvailableChatterList(java
	 * .lang.String)
	 */
	@Override
	public UserInfo[] searchAvailableChatter(String key) throws Exception {

		try{
			return SmartTest.getAvailableChatter();
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public UserInfo[] searchUser(String key) throws Exception {

		try{
			if (CommonUtil.isEmpty(key))
				return null;
	
			User cUser = SmartUtil.getCurrentUser();
	
			SchUser[] schUsers = getSchManager().getSchUser(cUser.getCompanyId(), cUser.getId(), key);
	
			if (CommonUtil.isEmpty(schUsers))
				return null;
	
			List<UserInfo> userList = new ArrayList<UserInfo>();
	
			for(SchUser schUser : schUsers) {
				UserInfo userInfo = new UserInfo();
				userInfo.setId(schUser.getId());
				userInfo.setName(schUser.getName());
				userInfo.setPosition(schUser.getPosition());
				userInfo.setRole(schUser.getRole());
				String picture = CommonUtil.toNotNull(schUser.getUserPicture());
				if(!picture.equals("")) {
					String extension = picture.lastIndexOf(".") > 1 ? picture.substring(picture.lastIndexOf(".") + 1) : null;
					String pictureId = picture.substring(0, (picture.length() - extension.length())-1);
					userInfo.setBigPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
					userInfo.setSmallPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
				} else {
					userInfo.setBigPictureName(null);
					userInfo.setSmallPictureName(null);
				}
				DepartmentInfo departmentInfo = new DepartmentInfo();
				departmentInfo.setId(schUser.getUserDeptId());
				departmentInfo.setName(schUser.getUserDeptName());
				departmentInfo.setDesc(schUser.getUserDeptDesc());
				userInfo.setDepartment(departmentInfo);
				userList.add(userInfo);
			}
	
			UserInfo[] userInfos = new UserInfo[userList.size()];
			userList.toArray(userInfos);
	
			return userInfos;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public CommunityInfo[] getMyCommunities() throws Exception {

		try{
			DepartmentInfo[] departmentInfos = getMyDepartments();
			GroupInfo[] groupInfos = getMyGroups();
			int departmentInfosLength = departmentInfos.length;
			int groupInfosLength = groupInfos == null ? 0 : groupInfos.length;
			CommunityInfo[] communityInfos = new CommunityInfo[departmentInfosLength + groupInfosLength];
			for(int i=0; i<departmentInfosLength; i++) {
				communityInfos[i] = departmentInfos[i];
			}
			for(int j=0; j<groupInfosLength; j++) {
				communityInfos[departmentInfosLength+j] = groupInfos[j];
			}
			return communityInfos;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	private DepartmentInfo[] getDepartmentInfoForUpload(String type) throws Exception {
		//내가 속한 부서 + 사용자 선택이 userId 인 부서
		List<SwoDepartment> deptList = new ArrayList<SwoDepartment>();	
		List<String> deptIdList = new ArrayList();

		User user = SmartUtil.getCurrentUser();

		SwoUserExtend userExtend = getSwoManager().getUserExtend(user.getId(), user.getId(), true);
		String myDeptId = userExtend.getDepartmentId();
		boolean isAdmin = userExtend.getAuthId().equalsIgnoreCase("ADMINISTRATOR") ? true : false;
		
		SwoDepartmentCond deptCond = new SwoDepartmentCond();
		deptCond.setId(myDeptId);
		SwoDepartment[] dept = getSwoManager().getDepartments("", deptCond, IManager.LEVEL_LITE);
		for (int i = 0; i < dept.length; i++) {
			boolean result = false;
			
			boolean isLeader = userExtend.getRoleId().equalsIgnoreCase("DEPT LEADER") ? true : false;
			
			SwaDepartmentCond myDeptAuthCond = new SwaDepartmentCond();
			myDeptAuthCond.setDeptId(dept[i].getId());
			myDeptAuthCond.setDeptAuthType(type);
			SwaDepartment myDeptAuth = SwManagerFactory.getInstance().getSwaManager().getAuthDepartment(user.getId(), myDeptAuthCond, null);
			String roleKey = myDeptAuth.getRoleKey();
			
			if (isLeader) {
				result = roleKey.indexOf(SwaDepartment.DEPT_ROLEKYE_LEADER) != -1 ? true : false;
			} else {
				result = roleKey.indexOf(SwaDepartment.DEPT_ROLEKYE_MEMBER) != -1 ? true : false;
			}
			if (isAdmin && !result) {
				result = roleKey.indexOf(SwaDepartment.DEPT_ROLEKYE_ADMIN) != -1 ? true : false;
			}
			
			if (roleKey.indexOf(SwaDepartment.DEPT_ROLEKYE_CUSTOM) != -1) {
				String customUserIds = myDeptAuth.getCustomUser();
				
				if (customUserIds.indexOf(user.getId()) != -1) {
					result = true;
				} else if (customUserIds.indexOf(user.getDepartmentId()) != -1) {
					result = true;
				} else {
					SwoGroupCond swoGroupCond = new SwoGroupCond();
					SwoGroupMember swoGroupMember = new SwoGroupMember();       
					swoGroupMember.setUserId(user.getId());		
					SwoGroupMember[] swoGroupMembers = new SwoGroupMember[1];
					swoGroupMembers[0] = swoGroupMember;
					swoGroupCond.setSwoGroupMembers(swoGroupMembers);
					swoGroupCond.setOrders(new Order[]{new Order("creationDate", false)});
					SwoGroup[] swoGroups = getSwoManager().getGroups(user.getId(), swoGroupCond, IManager.LEVEL_ALL);
					if (!CommonUtil.isEmpty(swoGroups)) {
						for (SwoGroup myGroups : swoGroups) {
							if (customUserIds.indexOf(myGroups.getId()) != -1) {
								result = true;
								break;
							}
						}
					}
				}
			}
			
			if (result && !deptIdList.contains(dept[i].getId())) {
				deptList.add(dept[i]);
				deptIdList.add(dept[i].getId());
			}
		}
		
		SwaDepartmentCond deptAuthCond = new SwaDepartmentCond();
		if (isAdmin) {
			deptAuthCond.setAdminOrCustomUserLike(user.getId());
		} else {
			deptAuthCond.setCustomUserLikek(user.getId());
		}
		deptAuthCond.setDeptAuthType(type);
		SwaDepartment[] deptAuths = SwManagerFactory.getInstance().getSwaManager().getAuthDepartments(user.getId(), deptAuthCond, null);
		if (deptAuths != null && deptAuths.length != 0) {
			String[] idIns = new String[deptAuths.length];
			for (int i = 0; i < deptAuths.length; i++) {
				idIns[i] = deptAuths[i].getDeptId();
			}
			SwoDepartmentCond deptCustomCond = new SwoDepartmentCond();
			deptCustomCond.setIdIns(idIns);
			SwoDepartment[] deptCustom = getSwoManager().getDepartments(user.getId(), deptCustomCond, IManager.LEVEL_LITE);
			for (int i = 0; i < deptCustom.length; i++) {
				if (!deptIdList.contains(deptCustom[i].getId())) {
					deptList.add(deptCustom[i]);
					deptIdList.add(deptCustom[i].getId());
				}
			}
		}
			
		DepartmentInfo[] deptInfos = new DepartmentInfo[deptList.size()];
		for (int i = 0; i < deptList.size(); i++) {
			DepartmentInfo deptInfo = new DepartmentInfo();
			SwoDepartment swDept = deptList.get(i);
			deptInfo.setId(swDept.getId());
			deptInfo.setName(swDept.getName());
			deptInfo.setDesc(swDept.getDescription());
			deptInfos[i] = deptInfo;
		}		
		
		return deptInfos;
	}
	private GroupInfo[] getGroupInfoForUpload(String type) throws Exception {
		
		User user = SmartUtil.getCurrentUser();

		SwoUserExtend userExtend = getSwoManager().getUserExtend(user.getId(), user.getId(), true);
		boolean isAdmin = userExtend.getAuthId().equalsIgnoreCase("ADMINISTRATOR") ? true : false;
		
		List<GroupInfo> resultGroupInfoList = new ArrayList<GroupInfo>();
		
		SwoGroupCond swoGroupCond = new SwoGroupCond();
		SwoGroupMember swoGroupMember = new SwoGroupMember();       
		swoGroupMember.setUserId(user.getId());		
		SwoGroupMember[] swoGroupMembers = new SwoGroupMember[1];
		swoGroupMembers[0] = swoGroupMember;
		swoGroupCond.setSwoGroupMembers(swoGroupMembers);
		swoGroupCond.setOrders(new Order[]{new Order("creationDate", false)});
		SwoGroup[] swoGroups = getSwoManager().getGroups(user.getId(), swoGroupCond, IManager.LEVEL_ALL);
		
		List groupList = new ArrayList();
		List groupIdList = new ArrayList();
		
		if(swoGroups != null) {
			for(SwoGroup swoGroup : swoGroups) {
				
				boolean result = false;
				
				String leaderId = swoGroup.getGroupLeader();
				boolean isLeader = !CommonUtil.isEmpty(leaderId) && leaderId.equalsIgnoreCase(user.getId())? true : false;
				
				SwaGroupCond myGroupAuthCond = new SwaGroupCond();
				myGroupAuthCond.setGroupId(swoGroup.getId());
				myGroupAuthCond.setGroupAuthType(type);
				SwaGroup myGroupAuth = SwManagerFactory.getInstance().getSwaManager().getAuthGroup(user.getId(), myGroupAuthCond, null);
				String roleKey = myGroupAuth.getRoleKey();
				
				if (isLeader) {
					result = roleKey.indexOf(SwaGroup.GROUP_ROLEKYE_LEADER) != -1 ? true : false;
				} else {
					result = roleKey.indexOf(SwaGroup.GROUP_ROLEKYE_MEMBER) != -1 ? true : false;
				}
				if (isAdmin && !result) {
					result = roleKey.indexOf(SwaGroup.GROUP_ROLEKYE_ADMIN) != -1 ? true : false;
				}
				
				if (roleKey.indexOf(SwaGroup.GROUP_ROLEKYE_CUSTOM) != -1) {
					String customUserIds = myGroupAuth.getCustomUser();
					
					if (customUserIds.indexOf(user.getId()) != -1) {
						result = true;
					} else if (customUserIds.indexOf(user.getDepartmentId()) != -1) {
						result = true;
					} else {
						for (SwoGroup myGroups : swoGroups) {
							if (customUserIds.indexOf(myGroups.getId()) != -1) {
								result = true;
								break;
							}
						}
					}
				}
				
				if (result && !groupIdList.contains(swoGroup.getId())) {
					groupList.add(swoGroup);
					groupIdList.add(swoGroup.getId());
				}
			}
		}
		SwaGroupCond groupAuthCond = new SwaGroupCond();
		if (isAdmin) {
			groupAuthCond.setAdminOrCustomUserLike(user.getId());
		} else {
			groupAuthCond.setCustomUserLike(user.getId());
		}
		groupAuthCond.setGroupAuthType(type);
		SwaGroup[] groupAuths = SwManagerFactory.getInstance().getSwaManager().getAuthGroups(user.getId(), groupAuthCond, null);
		if (groupAuths != null && groupAuths.length != 0) {
			String[] idIns = new String[groupAuths.length];
			for (int i = 0; i < groupAuths.length; i++) {
				idIns[i] = groupAuths[i].getGroupId();
			}
			SwoGroupCond groupCustomCond = new SwoGroupCond();
			groupCustomCond.setGroupIdIns(idIns);
			SwoGroup[] groupCustom = getSwoManager().getGroups(user.getId(), groupCustomCond, IManager.LEVEL_LITE);
			for (int i = 0; i < groupCustom.length; i++) {
				if (!groupIdList.contains(groupCustom[i].getId())) {
					groupList.add(groupCustom[i]);
					groupIdList.add(groupCustom[i].getId());
				}
			}
		}
		if (groupList.size() == 0) {
			return null;
		}
		GroupInfo[] groupInfos = new GroupInfo[groupList.size()];
		for (int i = 0; i < groupList.size(); i++) {
			GroupInfo groupInfo = new GroupInfo();
			SwoGroup swGroup = (SwoGroup)groupList.get(i);
			groupInfos[i] = ModelConverter.getGroupInfoBySwoGroup(null, swGroup);
		}		
		return groupInfos;
	}
	
	@Override
	public CommunityInfo[] getMyCommunitiesForUpload(String workId) throws Exception {

		try{
			if (workId.equalsIgnoreCase(SmartWork.ID_BOARD_MANAGEMENT)) {
				//공지사항
				//내가 등록할수 있는 부서
				DepartmentInfo[] departmentInfos =  getDepartmentInfoForUpload(SwaDepartment.DEPT_AUTHTYPE_BOARD_WRITE);
				//등록할수 있는 그룹
				
				//GroupInfo[] groupInfos = getMyGroups();
				GroupInfo[] groupInfos = getGroupInfoForUpload(SwaGroup.GROUP_AUTHTYPE_BOARD_WRITE);
				
				int departmentInfosLength = departmentInfos.length;
				int groupInfosLength = groupInfos == null ? 0 : groupInfos.length;
				CommunityInfo[] communityInfos = new CommunityInfo[departmentInfosLength + groupInfosLength];
				for(int i=0; i<departmentInfosLength; i++) {
					communityInfos[i] = departmentInfos[i];
				}
				for(int j=0; j<groupInfosLength; j++) {
					communityInfos[departmentInfosLength+j] = groupInfos[j];
				}
				return communityInfos;
				
			} else if (workId.equalsIgnoreCase(SmartWork.ID_EVENT_MANAGEMENT)) {
				//이벤트
				//내가 등록할수 있는 부서
				DepartmentInfo[] departmentInfos =  getDepartmentInfoForUpload(SwaDepartment.DEPT_AUTHTYPE_EVENT_WRITE);
				
				//등록할수 있는 그룹
				//GroupInfo[] groupInfos = getMyGroups();
				GroupInfo[] groupInfos = getGroupInfoForUpload(SwaGroup.GROUP_AUTHTYPE_EVENT_WRITE);
				
				int departmentInfosLength = departmentInfos.length;
				int groupInfosLength = groupInfos == null ? 0 : groupInfos.length;
				CommunityInfo[] communityInfos = new CommunityInfo[departmentInfosLength + groupInfosLength];
				for(int i=0; i<departmentInfosLength; i++) {
					communityInfos[i] = departmentInfos[i];
				}
				for(int j=0; j<groupInfosLength; j++) {
					communityInfos[departmentInfosLength+j] = groupInfos[j];
				}
				return communityInfos;
			} else {
				DepartmentInfo[] departmentInfos = getMyDepartments();
				GroupInfo[] groupInfos = getMyGroups();
				int departmentInfosLength = departmentInfos.length;
				int groupInfosLength = groupInfos == null ? 0 : groupInfos.length;
				CommunityInfo[] communityInfos = new CommunityInfo[departmentInfosLength + groupInfosLength];
				for(int i=0; i<departmentInfosLength; i++) {
					communityInfos[i] = departmentInfos[i];
				}
				for(int j=0; j<groupInfosLength; j++) {
					communityInfos[departmentInfosLength+j] = groupInfos[j];
				}
				return communityInfos;
			}
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public String setMyProfile(HttpServletRequest request) throws Exception {

		try{
			return null;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	/*
	 * 그룹 가입
	 * @see net.smartworks.server.service.ICommunityService#joinGroupRequest(java.util.Map, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void joinGroupRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			courseId=group_8e04540e1f8a4791bbef78eacd1acc1a, 
			userId=kj@maninsoft.co.kr
		}*/
		String courseId = (String)requestBody.get("courseId");
		String groupId = (String)requestBody.get("groupId");
		String userId = (String)requestBody.get("userId");

		if (CommonUtil.isEmpty(courseId) && !CommonUtil.isEmpty(groupId)) {
			//스마트웍스닷넷
			
			SwoGroup group = getSwoManager().getGroup(userId, groupId, IManager.LEVEL_ALL);
			
			int totalMember = group.getSwoGroupMembers() == null || group.getSwoGroupMembers().length == 0 ? 0 : group.getSwoGroupMembers().length;
			if (group.getMaxMember() != -1 && group.getMaxMember() <= totalMember) {
				throw new OverflowGroupMemberException("Overflow Group Member - Check Group MaxMember Count!");
			}
			
			if (group == null)
				return;
			if (group.isContainGroupMember(userId))
				return;
			SwoGroupMember groupMember = new SwoGroupMember();
			groupMember.setGroupId(groupId);
			groupMember.setUserId(userId);
			groupMember.setJoinType(SwoGroupMember.JOINTYPE_REQUEST);

			boolean isNoticeToGroupLeader = false;
			boolean autoApproval = group.isAutoApproval();
			
			if (autoApproval) {
				groupMember.setJoinType(SwoGroupMember.JOINTYPE_REQUEST);
				groupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_COMPLETE);
				groupMember.setJoinDate(new LocalDate());
			} else {
				groupMember.setJoinType(SwoGroupMember.JOINTYPE_REQUEST);
				groupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_READY);
				isNoticeToGroupLeader = true;
			}
			
			group.addGroupMember(groupMember);
			
			getSwoManager().setGroup(userId, group, IManager.LEVEL_ALL);
			
			if (isNoticeToGroupLeader) {
				PublishNotice pubNoticeObj = new PublishNotice(group.getGroupLeader(), PublishNotice.TYPE_NOTIFICATION, PublishNotice.REFTYPE_GROUPJOINREQUEST, group.getId());
				getPublishNoticeManager().setPublishNotice("linkadvisor", pubNoticeObj, IManager.LEVEL_ALL);
				SmartUtil.increaseNoticeCountByNoticeType(group.getGroupLeader(), Notice.TYPE_NOTIFICATION);
			}	
			
		} else {
			//세라캠퍼스
			groupId = courseId;
			
			SwoGroup group = getSwoManager().getGroup(userId, groupId, IManager.LEVEL_ALL);
			if (group == null)
				return;
			if (group.isContainGroupMember(userId))
				return;
			SwoGroupMember groupMember = new SwoGroupMember();
			groupMember.setGroupId(groupId);
			groupMember.setUserId(userId);
			groupMember.setJoinType(SwoGroupMember.JOINTYPE_REQUEST);

			CourseDetail courseDetail = getSeraManager().getCourseDetailById(groupId);
			
			boolean isNoticeToGroupLeader = false;
			if (courseDetail == null) {
				groupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_READY);
				isNoticeToGroupLeader = true;
			} else {
				boolean autoApproval = courseDetail.isAutoApproval();
				if (autoApproval) {
					groupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_COMPLETE);
					groupMember.setJoinDate(new LocalDate());
					if (seraService != null) {
						seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, 1, true);
					} else {
						logger.warn("Sera Service is Disabled. Check CommunityService Autowired!!!");
					}
				} else {
					groupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_READY);
					isNoticeToGroupLeader = true;
				}
			}
			group.addGroupMember(groupMember);
			
			getSwoManager().setGroup(userId, group, IManager.LEVEL_ALL);
			
			if (isNoticeToGroupLeader) {
				PublishNotice pubNoticeObj = new PublishNotice(group.getGroupLeader(), PublishNotice.TYPE_NOTIFICATION, PublishNotice.REFTYPE_GROUPJOINREQUEST, group.getId());
				getPublishNoticeManager().setPublishNotice("linkadvisor", pubNoticeObj, IManager.LEVEL_ALL);
				SmartUtil.increaseNoticeCountByNoticeType(group.getGroupLeader(), Notice.TYPE_NOTIFICATION);
			}	
		}
	}

	/*
	 * 그룹 맴버 초대
	 * @see net.smartworks.server.service.ICommunityService#inviteGroupMembers(java.util.Map, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void inviteGroupMembers(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			groupId=group_de3a8f11e0484cd2aa73a055338482f7, 
			users=
				[
					{
						userId=ktsoo@maninsoft.co.kr
					}
				]
		}*/
		String groupId = (String)requestBody.get("groupId");
		List<Map<String, String>> users = (ArrayList<Map<String,String>>)requestBody.get("users");
		
		if (users == null || users.size() == 0)
			return;
		
		String[] userIdArray = new String[users.size()];
		if(!CommonUtil.isEmpty(users)) {
			for(int i=0; i < users.subList(0, users.size()).size(); i++) {
				Map<String, String> userMap = users.get(i);
				userIdArray[i] = userMap.get("id");
			}
		}
		if (userIdArray == null || userIdArray.length == 0)
			return;
		
		SwoGroup group = getSwoManager().getGroup("", groupId, IManager.LEVEL_ALL);
		
		int totalMember = group.getSwoGroupMembers() == null || group.getSwoGroupMembers().length == 0 ? 0 : group.getSwoGroupMembers().length;
		if (group.getMaxMember() != -1 && group.getMaxMember() <= totalMember) {
			throw new OverflowGroupMemberException("Overflow Group Member - Check Group MaxMember Count!");
		}
		
		if (group == null)
			return;
		for (int i = 0; i < userIdArray.length; i++) {
			String userId = userIdArray[i];
			if (group.isContainGroupMember(userId))
				continue;

			SwoGroupMember groupMember = new SwoGroupMember();
			groupMember.setGroupId(groupId);
			groupMember.setUserId(userId);
			groupMember.setJoinType(SwoGroupMember.JOINTYPE_INVITE);
			groupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_COMPLETE);
			groupMember.setJoinDate(new LocalDate());

			group.addGroupMember(groupMember);
			
		}
		if (seraService != null) {
			seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, userIdArray.length, true);
		} else {
			logger.warn("Sera Service is Disabled. Check CommunityService Autowired!!!");
		}
		getSwoManager().setGroup("", group, IManager.LEVEL_ALL);
	}

	/*
	 * 그룹 가입신청 승인
	 * @see net.smartworks.server.service.ICommunityService#approvalJoinGroup(java.util.Map, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void approvalJoinGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			groupId=group_de3a8f11e0484cd2aa73a055338482f7,
			userId=ktsoo@maninsoft.co.kr, 
			approval=true
		}*/
		String groupId = (String)requestBody.get("groupId");
		String userId = (String)requestBody.get("userId");
		boolean approval = (Boolean)requestBody.get("approval");

		SwoGroup group = getSwoManager().getGroup(userId, groupId, IManager.LEVEL_ALL);
		if (group == null)
			return;
		if (!group.isContainGroupMember(userId))
			return;
		
		SwoGroupMember groupMember = group.getGroupMember(userId);
		if (approval) {
			groupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_COMPLETE);
			groupMember.setJoinDate(new LocalDate());
			if (seraService != null) {
				seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, 1, true);
			} else {
				logger.warn("Sera Service is Disabled. Check CommunityService Autowired!!!");
			}
		} else {
			group.removeGroupMember(groupMember);
		}
		getSwoManager().setGroup("", group, IManager.LEVEL_ALL);
	}

	/*
	 * 그룹 탈퇴
	 * @see net.smartworks.server.service.ICommunityService#leaveGroup(java.util.Map, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void leaveGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			groupId=group_de3a8f11e0484cd2aa73a055338482f7, 
			userId=ktsoo@maninsoft.co.kr
		}*/

		User user = SmartUtil.getCurrentUser();
		
		String groupId = (String)requestBody.get("groupId");
		String userId = user.getId();

		SwoGroup group = getSwoManager().getGroup(userId, groupId, IManager.LEVEL_ALL);
		if (group == null)
			return;
		if (!group.isContainGroupMember(userId))
			return;
		
		SwoGroupMember groupMember = group.getGroupMember(userId);
		group.removeGroupMember(groupMember);
		
		if (groupMember.getJoinType().equalsIgnoreCase(SwoGroupMember.JOINTYPE_GROUPLEADER)) {
			SwoGroupMember[] groupMembers = group.getSwoGroupMembers();
			if (groupMembers == null || groupMembers.length == 0 || (groupMembers.length == 1 && groupMembers[0].getUserId().equalsIgnoreCase(userId))) {
				//getSwoManager().removeGroup(userId, group.getId());
				//return;
				group.setGroupLeader("admin");
				if (groupMembers != null && groupMembers.length != 0) {
					groupMembers[0].setJoinType(SwoGroupMember.JOINTYPE_GROUPLEADER);
					groupMembers[0].setUserId("admin");
				}
			} else {
				for (int i = 0; i < groupMembers.length; i++) {
					SwoGroupMember groupMem = groupMembers[i];
					if (!groupMem.getUserId().equalsIgnoreCase(userId)) {
						group.setGroupLeader(groupMem.getUserId());
						groupMem.setJoinType(SwoGroupMember.JOINTYPE_GROUPLEADER);
					}	
				}
			}
		}
		
		if (seraService != null) {
			seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, 1, false);
		} else {
			logger.warn("Sera Service is Disabled. Check CommunityService Autowired!!!");
		}

		getSwoManager().setGroup("", group, IManager.LEVEL_ALL);
	}

	/*
	 * 그룹 강제탈퇴
	 * @see net.smartworks.server.service.ICommunityService#pushoutGroupMember(java.util.Map, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void pushoutGroupMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {		
		/*{
			groupId=group_de3a8f11e0484cd2aa73a055338482f7, 
			userId=ktsoo@maninsoft.co.kr
		}*/
		String groupId = (String)requestBody.get("groupId");
		String userId = (String)requestBody.get("userId");

		SwoGroup group = getSwoManager().getGroup(userId, groupId, IManager.LEVEL_ALL);
		if (group == null)
			return;
		if (!group.isContainGroupMember(userId))
			return;
		
		SwoGroupMember groupMember = group.getGroupMember(userId);
		group.removeGroupMember(groupMember);
		if (seraService != null) {
			seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, 1, false);
		} else {
			logger.warn("Sera Service is Disabled. Check CommunityService Autowired!!!");
		}

		getSwoManager().setGroup("", group, IManager.LEVEL_ALL);

	}
	@Override
	public UserInfo[] searchCompanyUser(String key) throws Exception {
		try {
			UserInfo[] userInfos = null;
			SwoUserCond swoUserCond = new SwoUserCond();
			if(CommonUtil.isEmpty(key))
				return null;

			swoUserCond.setKey(key);

			swoUserCond.setOrders(new Order[]{new Order("name", true)});

			SwoUserExtend[] swoUserExtends = getSwoManager().getUserExtends(null, swoUserCond);

			userInfos = ModelConverter.convertSwoUserExtendsToUserInfos(swoUserExtends);

			return userInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public UserInfo[] searchContact(User currentUser, String key) throws Exception {
		try {

			User user = currentUser;
			if(user == null)
				user = SmartUtil.getCurrentUser();

			String userId = user.getId();
			String companyId = user.getCompanyId();

			SwdRecordCond swdRecordCond = new SwdRecordCond();
			String domainId = "frm_contact_SYSTEM";
			swdRecordCond.setCompanyId(companyId);
			swdRecordCond.setDomainId(domainId);

			if(CommonUtil.isEmpty(key))
				return null;

			String colName = getSwdManager().getTableColName(domainId, SwdDomainFieldConstants.CONTACT_FIELDID_NAME);
			String colEmail = getSwdManager().getTableColName(domainId, SwdDomainFieldConstants.CONTACT_FIELDID_EMAIL);

			Filters fs1 = new Filters();
			fs1.addFilter(new Filter("like", colName, Filter.OPERANDTYPE_STRING, CommonUtil.toLikeString(key)));
			swdRecordCond.addFilters(fs1);
			Filters fs2 = new Filters();
			fs2.addFilter(new Filter("like", colEmail, Filter.OPERANDTYPE_STRING, CommonUtil.toLikeString(key)));
			swdRecordCond.addFilters(fs2);
			swdRecordCond.setOperator("or");

			if(!ModelConverter.isAccessibleAllInstance(SwdDomainFieldConstants.CONTACT_FORMID, userId))
				swdRecordCond.setCreationUser(userId);

			String[] workSpaceIdIns = ModelConverter.getWorkSpaceIdIns(user);
			swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);

			swdRecordCond.setOrders(new Order[]{new Order(SwdDomainFieldConstants.CONTACT_FIELDID_NAME, true)});

			SwdRecord[] swdRecords = getSwdManager().getRecords(userId, swdRecordCond, IManager.LEVEL_LITE);

			UserInfo[] userInfos = null;
			List<UserInfo> userInfoList = new ArrayList<UserInfo>();

			if(!CommonUtil.isEmpty(swdRecords)) {
				for(int i=0; i < swdRecords.length; i++) {
					SwdRecord swdRecord = swdRecords[i];
					UserInfo userInfo = new UserInfo();
					SwdDataField[] swdDataFields = swdRecord.getDataFields();
					for(SwdDataField swdDataField : swdDataFields) {
						String fieldId = swdDataField.getId();
						String value = swdDataField.getValue();
						if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_EMAIL)) {
							userInfo.setId(value);
						} else if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_NAME)) {
							userInfo.setName(value);
						} else if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_POSITION)) {
							userInfo.setPosition(value);
						} else if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_CELLPHONE)) {
							userInfo.setCellPhoneNo(value);
						} else if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_PHONE)) {
							userInfo.setPhoneNo(value);
						}
					}
					userInfo.setRole(User.USER_ROLE_EMAIL);
					if(!CommonUtil.isEmpty(userInfo.getId()))
						userInfoList.add(userInfo);
				}
			}
			if(userInfoList.size() != 0) {
				userInfos = new UserInfo[userInfoList.size()];
				userInfoList.toArray(userInfos);
			}
			return userInfos;

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public UserInfo[] searchEmailAddress(String key) throws Exception {
		try {
			User currentUser = SmartUtil.getCurrentUser();

			Semaphore semaphore = new Semaphore(2);
			Thread currentThread = Thread.currentThread();

			SearchParallelProcessing cpp = new SearchParallelProcessing(semaphore, currentThread, currentUser, 2, key);
			SearchParallelProcessing upp = new SearchParallelProcessing(semaphore, currentThread, null, 1, key);

			cpp.start();
			upp.start();

			synchronized (currentThread) {
				currentThread.wait();
			}

			UserInfo[] cUserInfos = (UserInfo[])cpp.getArrayResult();
			UserInfo[] uUserInfos = (UserInfo[])upp.getArrayResult();

			UserInfo[] finalUserInfos = null;
			List<UserInfo> finalUserInfoList = new ArrayList<UserInfo>();

			Map<String, Object> userInfoMap = new TreeMap<String, Object>();

			if(!CommonUtil.isEmpty(cUserInfos)) {
				for(UserInfo cUserInfo : cUserInfos) {
					userInfoMap.put(cUserInfo.getId(), cUserInfo);
				}
			}

			if(!CommonUtil.isEmpty(uUserInfos)) {
				for(UserInfo uUserInfo : uUserInfos) {
					userInfoMap.put(uUserInfo.getId(), uUserInfo);
				}
			}

			if(userInfoMap.size() > 0) {
				for(Map.Entry<String, Object> entry : userInfoMap.entrySet()) {
					UserInfo userInfo = (UserInfo)entry.getValue();
					finalUserInfoList.add(userInfo);
				}
			}

			if(finalUserInfoList.size() > 0) {
				finalUserInfos = new UserInfo[finalUserInfoList.size()];
				finalUserInfoList.toArray(finalUserInfos);
			}

			return finalUserInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public CommunityInfo[] getAllComsByDepartmentId(String departmentId, boolean departmentOnly) throws Exception {

		try{
			User cUser = SmartUtil.getCurrentUser();
			if(CommonUtil.isEmpty(departmentId)) {
				departmentId = cUser.getCompanyId();
			}
	
			SwoUserExtend[] swoUserExtends = getSwoManager().getAllComsByDepartmentId(departmentId, departmentOnly);
			DepartmentInfo department = ModelConverter.getDepartmentInfoByDepartmentId(departmentId);
			List<CommunityInfo> resultList = new ArrayList<CommunityInfo>();
			for(SwoUserExtend swoUserExtend : swoUserExtends) {
				String type = swoUserExtend.getType();
				if(!departmentOnly) {
					if(type.equals("u")) {
						UserInfo userInfo = new UserInfo();
						userInfo.setId(swoUserExtend.getId());
						userInfo.setName(swoUserExtend.getName());
						userInfo.setPosition(swoUserExtend.getPosition());
						userInfo.setRole(swoUserExtend.getRoleId().equals("DEPT LEADER") ? User.USER_ROLE_LEADER : User.USER_ROLE_MEMBER);
						String picture = swoUserExtend.getPictureName();
						userInfo.setPhoneNo(swoUserExtend.getPhoneNo());
						userInfo.setCellPhoneNo(swoUserExtend.getCellPhoneNo());
						
						userInfo.setDepartment(department);
						
						if(!CommonUtil.isEmpty(picture)) {
							String extension = picture.lastIndexOf(".") >= 1 ? picture.substring(picture.lastIndexOf(".") + 1) : null;
							String pictureId = picture.substring(0, (picture.length() - extension.length())-1);
							userInfo.setSmallPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
							userInfo.setBigPictureName(pictureId + Community.IMAGE_TYPE_ORIGINAL + "." + extension);
						} else {
							userInfo.setSmallPictureName(picture);
						}
						resultList.add(userInfo);
					} else {
						DepartmentInfo departmentInfo = new DepartmentInfo();
						departmentInfo.setId(swoUserExtend.getId());
						departmentInfo.setName(swoUserExtend.getName());
						departmentInfo.setDesc(swoUserExtend.getDescription());
						resultList.add(departmentInfo);
					}
				} else {
					DepartmentInfo departmentInfo = new DepartmentInfo();
					departmentInfo.setId(swoUserExtend.getId());
					departmentInfo.setName(swoUserExtend.getName());
					departmentInfo.setDesc(swoUserExtend.getDescription());
					
					departmentInfo.setFullpathName(ModelConverter.getFullpathNameByDepartmentId(swoUserExtend.getId()));
					
					resultList.add(departmentInfo);
				}
			}
			CommunityInfo[] communityInfos = new CommunityInfo[resultList.size()];
			resultList.toArray(communityInfos);
	
			return communityInfos;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public CommunityInfo[] getAllComsByGroupId(String groupId) throws Exception {

		try{
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();

			if(CommonUtil.isEmpty(groupId) || groupId.equals(cUser.getCompanyId())) {

				Map<String, Object> swoGroupMap = new HashMap<String, Object>();
				GroupInfo[] finalGroupInfos = null;
				List<GroupInfo> finalGroupInfoList = new ArrayList<GroupInfo>();

				SwoGroupCond swoGroupCond = new SwoGroupCond();

				SwoGroupMember swoGroupMember = new SwoGroupMember();       
				swoGroupMember.setUserId(userId);		
				SwoGroupMember[] swoGroupMembers = new SwoGroupMember[1];
				swoGroupMembers[0] = swoGroupMember;
				swoGroupCond.setSwoGroupMembers(swoGroupMembers);

				SwoGroup[] myGroups = getSwoManager().getGroups(userId, swoGroupCond, IManager.LEVEL_LITE);

				if(!CommonUtil.isEmpty(myGroups)) {
					for(SwoGroup myGroup : myGroups) {
						swoGroupMap.put(myGroup.getId(), myGroup);
					}
				}

				swoGroupCond = new SwoGroupCond();
				swoGroupCond.setGroupType(SwoGroup.GROUP_TYPE_PUBLIC);

				SwoGroup[] publicGroups = getSwoManager().getGroups(userId, swoGroupCond, IManager.LEVEL_LITE);

				if(!CommonUtil.isEmpty(publicGroups)) {
					for(SwoGroup publicGroup : publicGroups) {
						swoGroupMap.put(publicGroup.getId(), publicGroup);
					}
				}

				if(swoGroupMap.size() > 0) {
					for(Map.Entry<String, Object> entry : swoGroupMap.entrySet()) {
						SwoGroup swoGroup = (SwoGroup)entry.getValue();
						finalGroupInfoList.add(ModelConverter.getGroupInfoBySwoGroup(null, swoGroup));
					}
				}

				if(finalGroupInfoList.size() > 0) {
					finalGroupInfos = new GroupInfo[finalGroupInfoList.size()];
					finalGroupInfoList.toArray(finalGroupInfos);
				}

				return finalGroupInfos;
			} else {
				String[] ids = null;
				List<String> idList = new ArrayList<String>();
				SwoGroup swoGroup = getSwoManager().getGroup(cUser.getId(), groupId, IManager.LEVEL_ALL);
				if(!CommonUtil.isEmpty(swoGroup)) {
					SwoGroupMember[] swoGroupMembers = swoGroup.getSwoGroupMembers();
					if(!CommonUtil.isEmpty(swoGroupMembers)) {
						for(SwoGroupMember swoGroupMember : swoGroupMembers) {
							idList.add(swoGroupMember.getUserId());
						}
						if(idList.size() > 0) {
							ids = new String[idList.size()];
							idList.toArray(ids);
						}
					}
				}

				SwoUserExtend[] swoUserExtends = getSwoManager().getUsersExtend(cUser.getId(), ids);

				return ModelConverter.convertSwoUserExtendsToUserInfos(swoUserExtends);
			}
		} catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	public CommunityInfo[] getContactsByCategoryId(String categoryId) throws Exception {
		try {

			User user = SmartUtil.getCurrentUser();

			String userId = user.getId();
			String companyId = user.getCompanyId();

			SwdRecordCond swdRecordCond = new SwdRecordCond();
			String domainId = "frm_contact_SYSTEM";
			swdRecordCond.setCompanyId(companyId);
			swdRecordCond.setDomainId(domainId);

			String colEmail = getSwdManager().getTableColName(domainId, SwdDomainFieldConstants.CONTACT_FIELDID_EMAIL);
			Filters fs1 = new Filters();
			fs1.addFilter(new Filter("!=", colEmail, Filter.OPERANDTYPE_STRING, ""));
			swdRecordCond.addFilters(fs1);

			if(!CommonUtil.isEmpty(categoryId)) {
				String colCategory = getSwdManager().getTableColName(domainId, SwdDomainFieldConstants.CONTACT_FIELDID_CATEGORY);
				Filters fs2 = new Filters();
				//start added by jybae, 2012-8-5
				// categoryId가 uncategorized일때, null로 검색 추가
				if(categoryId.equalsIgnoreCase("uncategorized")){
					fs2.addFilter(new Filter("=", colCategory, Filter.OPERANDTYPE_STRING, null));
				//end added by jybae, 2012-8-5
				}else{
					fs2.addFilter(new Filter("=", colCategory, Filter.OPERANDTYPE_STRING, categoryId));
				}
				swdRecordCond.addFilters(fs2);
			}

			if(!ModelConverter.isAccessibleAllInstance(SwdDomainFieldConstants.CONTACT_FORMID, userId))
				swdRecordCond.setCreationUser(userId);

			String[] workSpaceIdIns = ModelConverter.getWorkSpaceIdIns(user);
			swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);

			swdRecordCond.setOrders(new Order[]{new Order(SwdDomainFieldConstants.CONTACT_FIELDID_NAME, true)});

			SwdRecord[] swdRecords = getSwdManager().getRecords(userId, swdRecordCond, IManager.LEVEL_LITE);

			if(!CommonUtil.isEmpty(categoryId)) {
				UserInfo[] userInfos = null;
				List<UserInfo> userInfoList = new ArrayList<UserInfo>();

				if(!CommonUtil.isEmpty(swdRecords)) {
					for(int i=0; i < swdRecords.length; i++) {
						SwdRecord swdRecord = swdRecords[i];
						UserInfo userInfo = new UserInfo();
						SwdDataField[] swdDataFields = swdRecord.getDataFields();
						for(SwdDataField swdDataField : swdDataFields) {
							String fieldId = swdDataField.getId();
							String value = swdDataField.getValue();
							if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_EMAIL)) {
								userInfo.setId(value);
							} else if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_NAME)) {
								userInfo.setName(value);
							} else if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_POSITION)) {
								userInfo.setPosition(value);
							} else if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_CELLPHONE)) {
								userInfo.setCellPhoneNo(value);
							} else if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_PHONE)) {
								userInfo.setPhoneNo(value);
							}
						}
						userInfo.setRole(User.USER_ROLE_EMAIL);
						userInfoList.add(userInfo);
					}
				}
				if(userInfoList.size() != 0) {
					userInfos = new UserInfo[userInfoList.size()];
					userInfoList.toArray(userInfos);
				}
				return userInfos;
			} else {
				GroupInfo[] groupInfos = null;
				List<GroupInfo> groupInfoList = new ArrayList<GroupInfo>();
				Map<String, Object> groupInfoMap = new HashMap<String, Object>();

				if(!CommonUtil.isEmpty(swdRecords)) {
					for(int i=0; i < swdRecords.length; i++) {
						SwdRecord swdRecord = swdRecords[i];
						GroupInfo groupInfo = new GroupInfo();
						SwdDataField[] swdDataFields = swdRecord.getDataFields();
						for(SwdDataField swdDataField : swdDataFields) {
							String fieldId = swdDataField.getId();
							String value = swdDataField.getValue();
							if(fieldId.equals(SwdDomainFieldConstants.CONTACT_FIELDID_CATEGORY)) {
								groupInfo = new GroupInfo(value, value);
							}
						}
						groupInfoMap.put(groupInfo.getId(), groupInfo);
					}
				}
				if(groupInfoMap.size() > 0) {
					for(Map.Entry<String, Object> entry : groupInfoMap.entrySet()) {
						GroupInfo groupInfo = (GroupInfo)entry.getValue();
						groupInfoList.add(groupInfo);
					}
				}
				if(groupInfoList.size() > 0) {
					groupInfos = new GroupInfo[groupInfoList.size()];
					groupInfoList.toArray(groupInfos);
				}
				return groupInfos;
			}

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public CommunityInfo[] getAllComsByCategoryId(String categoryId) throws Exception {
		try {
			User cUser = SmartUtil.getCurrentUser();
			if(CommonUtil.isEmpty(categoryId) || categoryId.equals(cUser.getCompanyId())) {
				return getContactsByCategoryId(null);
			} else {
				return getContactsByCategoryId(categoryId);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public MailAccount[] getMyMailAccounts() throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			MailAccountCond mailAccountCond = new MailAccountCond();
			mailAccountCond.setUserId(userId);

			net.smartworks.server.engine.mail.model.MailAccount[] sMailAccounts = getMailManager().getMailAccounts(userId, mailAccountCond, IManager.LEVEL_ALL);

			MailAccount[] vMailAccounts = null;
			List<MailAccount> vMailAccountList = new ArrayList<MailAccount>();
			if(!CommonUtil.isEmpty(sMailAccounts)) {
				for(net.smartworks.server.engine.mail.model.MailAccount sMailAccount : sMailAccounts) {
					MailAccount vMailAccount = new MailAccount();
					vMailAccount.setEmailServerId(sMailAccount.getMailServerId());
					vMailAccount.setEmailServerName(sMailAccount.getMailServerName());
					vMailAccount.setEmailId(sMailAccount.getMailId()+"@"+sMailAccount.getMailServerName());
					vMailAccount.setUserName(sMailAccount.getMailUserName());
					vMailAccount.setDeleteAfterFetched(CommonUtil.toBoolean(sMailAccount.getMailDeleteFetched()));
					vMailAccount.setPassword(sMailAccount.getMailPassword());
					vMailAccount.setSignature(sMailAccount.getMailSignature());
					vMailAccount.setUseSignature(sMailAccount.isUseMailSign());
					vMailAccount.setSenderUserTitle(sMailAccount.getSenderUserTitle());
					vMailAccountList.add(vMailAccount);
				}
			}
			if(vMailAccountList.size() > 0) {
				vMailAccounts = new MailAccount[vMailAccountList.size()];
				vMailAccountList.toArray(vMailAccounts);
			}
			return vMailAccounts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void updateGroupSetting(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			   groupId=group_3060deb892c04390a3c5f8a57402049f,
			   frmGroupSpaceSetting=   {
			      txtGroupName=광민그룹,
			      txtaGroupDesc=그룹설명입니다.,
			      rdoGroupMaxMembers=-1,(rdoGroupMaxMembers=UserDefined)
			      txtGroupMaxMembers=, (txtGroupMaxMembers=111)
			      rdoGroupAutoApproval=true, (false)
			      chkInvitableMembersLeader=on,
			      chkInvitableMembersMembers=on,
			      chkInvitableMembersCustom=on,
			      chkBoardWriteLeader=on,
			      chkBoardWriteMembers=on,
			      chkBoardWriteCustom=on,
			      chkBoardEditOwner=on,
			      chkBoardEditAdministrator=on,
			      chkBoardEditLeader=on,
			      chkBoardEditCustom=on,
			      chkEventWriteLeader=on,
			      chkEventWriteMembers=on,
			      chkEventWriteCustom=on,
			      chkEventEditOwner=on,
			      chkEventEditAdministrator=on,
			      chkEventEditLeader=on,
			      chkEventEditCustom=on,
			      txtGroupLeader=      {
			         users=         [
			            {
			               id=kmyu@maninsoft.co.kr,
			               name=선임 유광민
			            }
			         ]
			      },
			      txtInvitableMembersCustoms=      {
			         users=         [
			            {
			               id=kmyu@maninsoft.co.kr,
			               name=선임 유광민
			            }
			         ]
			      },
			      txtBoardWriteCustoms=      {
			         users=         [
			            {
			               id=kmyu@maninsoft.co.kr,
			               name=선임 유광민
			            }
			         ]
			      },
			      txtBoardEditCustoms=      {
			         users=         [
			            {
			               id=kmyu@maninsoft.co.kr,
			               name=선임 유광민
			            }
			         ]
			      },
			      txtEventWriteCustoms=      {
			         users=         [
			            {
			               id=kmyu@maninsoft.co.kr,
			               name=선임 유광민
			            }
			         ]
			      },
			      txtEventEditCustoms=      {
			         users=         [
			            {
			               id=kmyu@maninsoft.co.kr,
			               name=선임 유광민
			            }
			         ]
			      },
			      imgGroupProfile=      {
			         groupId=fg_f4854c39ec146e4939e9196e6bdc18cce48b,
			         files=         [

			         ]
			      }
			   }
			}*/
		
		String userId = SmartUtil.getCurrentUser().getId();
		String groupId = (String)requestBody.get("groupId");
		
		SwaGroupCond cond = new SwaGroupCond();
		cond.setGroupId(groupId);
		SwaGroup[] groupAuths = SwManagerFactory.getInstance().getSwaManager().getAuthGroups(userId, cond, null);
		
		SwaGroup boardWrite = null;
		SwaGroup boardEdit = null;
		SwaGroup eventWrite = null;
		SwaGroup eventEdit = null;
		SwaGroup memberInvite = null;
		
		if (groupAuths != null && groupAuths.length != 0) {
			for (int i = 0; i < groupAuths.length; i++) {
				SwaGroup deptAuth = groupAuths[i];
				String deptAuthType = deptAuth.getGroupAuthType();
				if (deptAuthType.equalsIgnoreCase(SwaGroup.GROUP_AUTHTYPE_BOARD_WRITE)) {
					boardWrite = deptAuth;
				} else if (deptAuthType.equalsIgnoreCase(SwaGroup.GROUP_AUTHTYPE_BOARD_EDIT)) {
					boardEdit = deptAuth;
				} else if (deptAuthType.equalsIgnoreCase(SwaGroup.GROUP_AUTHTYPE_EVENT_WRITE)) {
					eventWrite = deptAuth;
				} else if (deptAuthType.equalsIgnoreCase(SwaGroup.GROUP_AUTHTYPE_EVENT_EDIT)) {
					eventEdit = deptAuth;
				} else if (deptAuthType.equalsIgnoreCase(SwaGroup.GROUP_AUTHTYPE_MEMBER_INVITE)) {
					memberInvite = deptAuth;
				}
			}
		}
		
		Map<String, Object> frmGroupSpaceSetting = (Map<String, Object>)requestBody.get("frmGroupSpaceSetting");
		String txtGroupName = (String)frmGroupSpaceSetting.get("txtGroupName");
	    String txtaGroupDesc = (String)frmGroupSpaceSetting.get("txtaGroupDesc");
	    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    
		String chkBoardWriteLeader = (String)frmGroupSpaceSetting.get("chkBoardWriteLeader");
		String chkBoardWriteMembers = (String)frmGroupSpaceSetting.get("chkBoardWriteMembers");
		String chkBoardWriteCustom = (String)frmGroupSpaceSetting.get("chkBoardWriteCustom");
		
		if (boardWrite == null) { 
			boardWrite = new SwaGroup();
			boardWrite.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_BOARD_WRITE);
			boardWrite.setGroupId(groupId);
		}
		StringBuffer boardWriteRoleKeyBuff = new StringBuffer();
		boolean isBoardWriteFirst = true;
		if (chkBoardWriteLeader != null && chkBoardWriteLeader.equalsIgnoreCase("on")) {
			if (!isBoardWriteFirst)
				boardWriteRoleKeyBuff.append(";");
			boardWriteRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_LEADER);
			isBoardWriteFirst = false;
		}
		if (chkBoardWriteMembers != null && chkBoardWriteMembers.equalsIgnoreCase("on")) {
			if (!isBoardWriteFirst)
				boardWriteRoleKeyBuff.append(";");
			boardWriteRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_MEMBER);
			isBoardWriteFirst = false;
		}
		String chkBoardWriteCustomUsersStr = null;
		if (chkBoardWriteCustom != null && chkBoardWriteCustom.equalsIgnoreCase("on")) {
			if (!isBoardWriteFirst)
				boardWriteRoleKeyBuff.append(";");
			boardWriteRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_CUSTOM);
			isBoardWriteFirst = false;
			
			Map<String, Object> txtBoardWriteCustoms = (Map<String, Object>)frmGroupSpaceSetting.get("txtBoardWriteCustoms");    
			List<Map<String, String>> txtBoardWriteUsers = (ArrayList<Map<String,String>>)txtBoardWriteCustoms.get("users");
			chkBoardWriteCustomUsersStr = getUserIdsStrByList(txtBoardWriteUsers);
		}
		boardWrite.setRoleKey(boardWriteRoleKeyBuff.toString());
		boardWrite.setCustomUser(chkBoardWriteCustomUsersStr);
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String chkBoardEditOwner = (String)frmGroupSpaceSetting.get("chkBoardEditOwner");
		String chkBoardEditAdministrator = (String)frmGroupSpaceSetting.get("chkBoardEditAdministrator");
		String chkBoardEditLeader = (String)frmGroupSpaceSetting.get("chkBoardEditLeader");
		String chkBoardEditCustom = (String)frmGroupSpaceSetting.get("chkBoardEditCustom");
		
		if (boardEdit == null) { 
			boardEdit = new SwaGroup();
			boardEdit.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_BOARD_EDIT);
			boardEdit.setGroupId(groupId);
		}
		StringBuffer boardEditRoleKeyBuff = new StringBuffer();
		boolean isBoardEditFirst = true;
		
		if (chkBoardEditOwner != null && chkBoardEditOwner.equalsIgnoreCase("on")) {
			if (!isBoardEditFirst)
				boardEditRoleKeyBuff.append(";");
			boardEditRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_OWNER);
			isBoardEditFirst = false;
		}
		if (chkBoardEditAdministrator != null && chkBoardEditAdministrator.equalsIgnoreCase("on")) {
			if (!isBoardEditFirst)
				boardEditRoleKeyBuff.append(";");
			boardEditRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_ADMIN);
			isBoardEditFirst = false;
		}
		if (chkBoardEditLeader != null && chkBoardEditLeader.equalsIgnoreCase("on")) {
			if (!isBoardEditFirst)
				boardEditRoleKeyBuff.append(";");
			boardEditRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_LEADER);
			isBoardEditFirst = false;
		}
		
		String chkBoardEditCustomUsersStr = null;
		if (chkBoardEditCustom != null && chkBoardEditCustom.equalsIgnoreCase("on")) {
			if (!isBoardEditFirst)
				boardEditRoleKeyBuff.append(";");
			boardEditRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_CUSTOM);
			isBoardEditFirst = false;
			
			Map<String, Object> txtBoardEditCustoms = (Map<String, Object>)frmGroupSpaceSetting.get("txtBoardEditCustoms");    
			List<Map<String, String>> txtBoardEditUsers = (ArrayList<Map<String,String>>)txtBoardEditCustoms.get("users");
			chkBoardEditCustomUsersStr = getUserIdsStrByList(txtBoardEditUsers);
		}
		boardEdit.setRoleKey(boardEditRoleKeyBuff.toString());
		boardEdit.setCustomUser(chkBoardEditCustomUsersStr);
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String chkEventWriteLeader = (String)frmGroupSpaceSetting.get("chkEventWriteLeader");
		String chkEventWriteMembers = (String)frmGroupSpaceSetting.get("chkEventWriteMembers");
		String chkEventWriteCustom = (String)frmGroupSpaceSetting.get("chkEventWriteCustom");
		
		if (eventWrite == null) { 
			eventWrite = new SwaGroup();
			eventWrite.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_EVENT_WRITE);
			eventWrite.setGroupId(groupId);
		}
		StringBuffer eventWriteRoleKeyBuff = new StringBuffer();
		boolean isEventWriteFirst = true;
		
		if (chkEventWriteLeader != null && chkEventWriteLeader.equalsIgnoreCase("on")) {
			if (!isEventWriteFirst)
				eventWriteRoleKeyBuff.append(";");
			eventWriteRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_LEADER);
			isEventWriteFirst = false;
		}
		if (chkEventWriteMembers != null && chkEventWriteMembers.equalsIgnoreCase("on")) {
			if (!isEventWriteFirst)
				eventWriteRoleKeyBuff.append(";");
			eventWriteRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_MEMBER);
			isEventWriteFirst = false;
		}
		String chkEventWriteCustomUsersStr = null;
		if (chkEventWriteCustom != null && chkEventWriteCustom.equalsIgnoreCase("on")) {
			if (!isEventWriteFirst)
				eventWriteRoleKeyBuff.append(";");
			eventWriteRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_CUSTOM);
			isEventWriteFirst = false;
			
			Map<String, Object> txtEventWriteCustoms = (Map<String, Object>)frmGroupSpaceSetting.get("txtEventWriteCustoms");    
			List<Map<String, String>> txtEventWriteUsers = (ArrayList<Map<String,String>>)txtEventWriteCustoms.get("users");
			chkEventWriteCustomUsersStr = getUserIdsStrByList(txtEventWriteUsers);
		}
		eventWrite.setRoleKey(eventWriteRoleKeyBuff.toString());
		eventWrite.setCustomUser(chkEventWriteCustomUsersStr);
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String chkEventEditOwner = (String)frmGroupSpaceSetting.get("chkEventEditOwner");
		String chkEventEditAdministrator = (String)frmGroupSpaceSetting.get("chkEventEditAdministrator");
		String chkEventEditLeader = (String)frmGroupSpaceSetting.get("chkEventEditLeader");
		String chkEventEditCustom = (String)frmGroupSpaceSetting.get("chkEventEditCustom");
		
		if (eventEdit == null) { 
			eventEdit = new SwaGroup();
			eventEdit.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_EVENT_EDIT);
			eventEdit.setGroupId(groupId);
		}
		StringBuffer eventEditRoleKeyBuff = new StringBuffer();
		boolean isEventEditFirst = true;

		if (chkEventEditOwner != null && chkEventEditOwner.equalsIgnoreCase("on")) {
			if (!isEventEditFirst)
				eventEditRoleKeyBuff.append(";");
			eventEditRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_OWNER);
			isEventEditFirst = false;
		}
		if (chkEventEditAdministrator != null && chkEventEditAdministrator.equalsIgnoreCase("on")) {
			if (!isEventEditFirst)
				eventEditRoleKeyBuff.append(";");
			eventEditRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_ADMIN);
			isEventEditFirst = false;
		}
		if (chkEventEditLeader != null && chkEventEditLeader.equalsIgnoreCase("on")) {
			if (!isEventEditFirst)
				eventEditRoleKeyBuff.append(";");
			eventEditRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_LEADER);
			isEventEditFirst = false;
		}
		String chkEventEditCustomUsersStr = null;
		if (chkEventEditCustom != null && chkEventEditCustom.equalsIgnoreCase("on")) {
			if (!isEventEditFirst)
				eventEditRoleKeyBuff.append(";");
			eventEditRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_CUSTOM);
			isEventEditFirst = false;
			
			Map<String, Object> txtEventEditCustoms = (Map<String, Object>)frmGroupSpaceSetting.get("txtEventEditCustoms");    
			List<Map<String, String>> txtEventEditUsers = (ArrayList<Map<String,String>>)txtEventEditCustoms.get("users");
			chkEventEditCustomUsersStr = getUserIdsStrByList(txtEventEditUsers);
		}
		eventEdit.setRoleKey(eventEditRoleKeyBuff.toString());
		eventEdit.setCustomUser(chkEventEditCustomUsersStr);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		String chkInvitableMembersLeader = (String)frmGroupSpaceSetting.get("chkInvitableMembersLeader");
		String chkInvitableMembersMembers = (String)frmGroupSpaceSetting.get("chkInvitableMembersMembers");
		String chkInvitableMembersCustom = (String)frmGroupSpaceSetting.get("chkInvitableMembersCustom");
		
		if (memberInvite == null) { 
			memberInvite = new SwaGroup();
			memberInvite.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_MEMBER_INVITE);
			memberInvite.setGroupId(groupId);
		}
		StringBuffer memberInviteRoleKeyBuff = new StringBuffer();
		boolean isMemberInviteFirst = true;
		
		if (chkInvitableMembersLeader != null && chkInvitableMembersLeader.equalsIgnoreCase("on")) {
			if (!isMemberInviteFirst)
				memberInviteRoleKeyBuff.append(";");
			memberInviteRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_LEADER);
			isMemberInviteFirst = false;
		}
		if (chkInvitableMembersMembers != null && chkInvitableMembersMembers.equalsIgnoreCase("on")) {
			if (!isMemberInviteFirst)
				memberInviteRoleKeyBuff.append(";");
			memberInviteRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_MEMBER);
			isMemberInviteFirst = false;
		}
		String chkMemberInviteCustomUsersStr = null;
		if (chkInvitableMembersCustom != null && chkInvitableMembersCustom.equalsIgnoreCase("on")) {
			if (!isMemberInviteFirst)
				memberInviteRoleKeyBuff.append(";");
			memberInviteRoleKeyBuff.append(SwaGroup.GROUP_ROLEKYE_CUSTOM);
			isMemberInviteFirst = false;
			
			Map<String, Object> txtMemberInviteCustoms = (Map<String, Object>)frmGroupSpaceSetting.get("txtInvitableMembersCustoms");    
			List<Map<String, String>> txtMemberInviteUsers = (ArrayList<Map<String,String>>)txtMemberInviteCustoms.get("users");
			chkMemberInviteCustomUsersStr = getUserIdsStrByList(txtMemberInviteUsers);
		}
		memberInvite.setRoleKey(memberInviteRoleKeyBuff.toString());
		memberInvite.setCustomUser(chkMemberInviteCustomUsersStr);		
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		SwManagerFactory.getInstance().getSwaManager().setAuthGroup(userId, boardWrite, null);
		SwManagerFactory.getInstance().getSwaManager().setAuthGroup(userId, boardEdit, null);
		SwManagerFactory.getInstance().getSwaManager().setAuthGroup(userId, eventWrite, null);
		SwManagerFactory.getInstance().getSwaManager().setAuthGroup(userId, eventEdit, null);
		SwManagerFactory.getInstance().getSwaManager().setAuthGroup(userId, memberInvite, null);

		Map<String, Object> imgGroupProfile = (Map<String, Object>)frmGroupSpaceSetting.get("imgGroupProfile"); 
		String fileGroupId = (String)imgGroupProfile.get("groupId");   
		List<Map<String, String>> groupImgFiles = (ArrayList<Map<String,String>>)imgGroupProfile.get("files");
		
		SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup(userId, groupId, null);
		if (group != null) {
			group.setDescription(txtaGroupDesc);
			group.setName(txtGroupName);
			if(!groupImgFiles.isEmpty()) {
				for(int i=0; i < groupImgFiles.subList(0, groupImgFiles.size()).size(); i++) {
					Map<String, String> file = groupImgFiles.get(i);
					String profileFileId = file.get("fileId");
					String profileFileName = file.get("fileName");
					String txtGroupProfilePicture = getDocManager().insertProfilesFile(profileFileId, profileFileName, group.getId() + "_p");
					group.setPicture(txtGroupProfilePicture);
				}
			}
		}
		
		String rdoGroupMaxMembers = (String)frmGroupSpaceSetting.get("rdoGroupMaxMembers"); 
		String txtGroupMaxMembers = (String)frmGroupSpaceSetting.get("txtGroupMaxMembers"); 
		String rdoGroupAutoApproval = (String)frmGroupSpaceSetting.get("rdoGroupAutoApproval"); 
		
		if (rdoGroupMaxMembers != null && rdoGroupMaxMembers.equalsIgnoreCase("UserDefined")) {
			group.setMaxMember(CommonUtil.isEmpty(txtGroupMaxMembers) ? 0 : Integer.parseInt(txtGroupMaxMembers));
		} else {
			group.setMaxMember(-1);
		}
		if (!CommonUtil.isEmpty(rdoGroupAutoApproval) && rdoGroupAutoApproval.equalsIgnoreCase("true")) {
			group.setAutoApproval(CommonUtil.toBoolean(rdoGroupAutoApproval));
		} else {
			group.setAutoApproval(false);
		}
		
		Map<String, Object> txtGroupLeader = (Map<String, Object>)frmGroupSpaceSetting.get("txtGroupLeader");    
		List<Map<String, String>> txtGroupLeaderList = (ArrayList<Map<String,String>>)txtGroupLeader.get("users");
		chkMemberInviteCustomUsersStr = getUserIdsStrByList(txtGroupLeaderList);
		if (!CommonUtil.isEmpty(chkMemberInviteCustomUsersStr)) {
			if (!group.getGroupLeader().equalsIgnoreCase(chkMemberInviteCustomUsersStr)) {
				
				SwoGroupMember[] groupMember = group.getSwoGroupMembers();
				boolean isNewMember = true;
				for (int i = 0; i < groupMember.length; i++) {
					String groupMemberId = groupMember[i].getUserId();
					if (groupMemberId.equalsIgnoreCase(group.getGroupLeader())) {
						groupMember[i].setJoinType(SwoGroupMember.JOINTYPE_INVITE);
						groupMember[i].setJoinStatus(SwoGroupMember.JOINSTATUS_COMPLETE);
					}
					if (groupMemberId.equalsIgnoreCase(chkMemberInviteCustomUsersStr)) {
						groupMember[i].setJoinType(SwoGroupMember.JOINTYPE_GROUPLEADER);
						groupMember[i].setJoinStatus(SwoGroupMember.JOINSTATUS_COMPLETE);
						isNewMember = false;
					}
				}
				if (isNewMember) {
					SwoGroupMember newMember = new SwoGroupMember();
					newMember.setUserId(chkMemberInviteCustomUsersStr);
					newMember.setJoinType(SwoGroupMember.JOINTYPE_GROUPLEADER);
					newMember.setJoinStatus(SwoGroupMember.JOINSTATUS_COMPLETE);
					newMember.setJoinDate(new LocalDate());
					group.addGroupMember(newMember);
				}
				group.setGroupLeader(chkMemberInviteCustomUsersStr);
			}
		}
		
		SwManagerFactory.getInstance().getSwoManager().setGroup(userId, group, null);
		
	}
	@Override
	public GroupMemberList getGroupMemberInformList(String groupId) throws Exception {

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		GroupMemberList memberInformList = new GroupMemberList();
		SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup(userId, groupId, null);
		if (group == null)
			return memberInformList;
		
		List memberIdList = new ArrayList();
		List requestMemberIdList = new ArrayList();
		SwoGroupMember[] groupMembers = group.getSwoGroupMembers();
		for (int i = 0; i < groupMembers.length; i++) {
			SwoGroupMember groupMember = groupMembers[i];
			String joinType = groupMember.getJoinType();
			String joinStatus = groupMember.getJoinStatus();
			if (joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_COMPLETE)) {
				memberIdList.add(groupMember.getUserId());
			} else if (joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_READY)
						&& joinType.equalsIgnoreCase(SwoGroupMember.JOINTYPE_REQUEST)) {
				requestMemberIdList.add(groupMember.getUserId());
			}
		}
		if (memberIdList.size() != 0) {
			String[] memberIdArray = new String[memberIdList.size()];
			memberIdList.toArray(memberIdArray);
			
			SwoUserExtend[] userExtends = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(userId, memberIdArray, GroupMemberList.MAX_MEMBER_LIST, null, null, null);

			UserInfo[] userInfos = new UserInfo[userExtends.length];
				
			String groupLeader = group.getGroupLeader();
			for (int i = 0; i < userExtends.length; i++) {
				userInfos[i] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtends[i]);
				
				//그룹의 리더가 아니라면 부서의 리더들도 롤이 그룹 맴버이
				String tempMember = userInfos[i].getId();
				if (!groupLeader.equalsIgnoreCase(tempMember)) {
					userInfos[i].setRole(User.USER_ROLE_MEMBER);
				}
			}
			
			memberInformList.setMembers(userInfos);
			memberInformList.setTotalMembers(memberIdArray.length);
		} else {
			memberInformList.setTotalMembers(0);
		}
		
		if (requestMemberIdList.size() != 0) {
			String[] memberIdArray = new String[requestMemberIdList.size()];
			requestMemberIdList.toArray(memberIdArray);
			
			SwoUserExtend[] userExtends = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(userId, memberIdArray);
			UserInfo[] userInfos = new UserInfo[userExtends.length];
			for (int i = 0; i < userExtends.length; i++) {
				userInfos[i] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtends[i]);
			}
			memberInformList.setRequesters(userInfos);
			memberInformList.setTotalRequesters(userInfos.length);
		} else {
			memberInformList.setTotalRequesters(0);
		}
		return memberInformList;
	}
	@Override
	public UserInfo[] getGroupMembersById(String groupId, String lastId, int maxSize) throws Exception {

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		GroupMemberList memberInformList = new GroupMemberList();
		SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup(userId, groupId, null);
		if (group == null)
			return null;
		
		List memberIdList = new ArrayList();
		SwoGroupMember[] groupMembers = group.getSwoGroupMembers();
		for (int i = 0; i < groupMembers.length; i++) {
			SwoGroupMember groupMember = groupMembers[i];
			String joinType = groupMember.getJoinType();
			String joinStatus = groupMember.getJoinStatus();
			if (joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_COMPLETE)) {
				memberIdList.add(groupMember.getUserId());
			}
		}
		if (memberIdList.size() != 0) {
			String[] memberIdArray = new String[memberIdList.size()];
			memberIdList.toArray(memberIdArray);
			
			SwoUser lastUser = SwManagerFactory.getInstance().getSwoManager().getUser(userId, lastId, IManager.LEVEL_LITE);
			
			if (lastUser == null)
				return null;
			
			SwoUserExtend[] totaluserExtends = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(userId, memberIdArray, -1, lastUser.getName(), lastUser.getModificationDate(), null);
			SwoUserExtend[] userExtends = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(userId, memberIdArray, maxSize, lastUser.getName(), lastUser.getModificationDate(), null);

			UserInfo[] userInfos = null;
			if (totaluserExtends.length > userExtends.length) {
				userInfos = new UserInfo[userExtends.length + 1];
				userInfos[userExtends.length] = new UserInfo();
			} else {
				userInfos = new UserInfo[userExtends.length];
			}
			
			for (int i = 0; i < userExtends.length; i++) {
				userInfos[i] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtends[i]);
			}
			return userInfos;
		} else {
			return null;
		}
	}
	@Override
	public UserInfo[] searchCommunityNonMember(String communityId, String key) throws Exception {
		try{
			if (CommonUtil.isEmpty(communityId) || CommonUtil.isEmpty(key))
				return null;
	
			User cUser = SmartUtil.getCurrentUser();
	
			SchUser[] schUsers = getSchManager().getSchCommunityNonMember(cUser.getCompanyId(), cUser.getId(), communityId, key);
			List<UserInfo> userList = new ArrayList<UserInfo>();
	
			if(schUsers != null) {
				for(SchUser schUser : schUsers) {
					UserInfo userInfo = new UserInfo();
					userInfo.setId(schUser.getId());
					userInfo.setName(schUser.getName());
					userInfo.setPosition(schUser.getPosition());
					userInfo.setRole(schUser.getRole());
					DepartmentInfo departmentInfo = new DepartmentInfo();
					departmentInfo.setId(schUser.getDeptId());
					departmentInfo.setName(schUser.getDeptName());
					departmentInfo.setDesc(schUser.getDeptDesc());
					userInfo.setDepartment(departmentInfo);
					userList.add(userInfo);
				}
		
				UserInfo[] userInfos = new UserInfo[userList.size()];
				userList.toArray(userInfos);
	
				return userInfos;
			}
			return null;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	@Override
	public void updateDepartmentSetting(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			   departmentId=dept_302bcb7c7db04a7ba03a7bc43da23a07,
			   frmDepartmentSpaceSetting=   {
			      txtaDepartmentDesc=description,
			      chkBoardWriteAdministrator=on,
			      chkBoardWriteLeader=on,
			      chkBoardWriteMembers=on,
			      chkBoardWriteCustom=on,
			      chkBoardEditOwner=on,
			      chkBoardEditAdministrator=on,
			      chkBoardEditLeader=on,
			      chkBoardEditCustom=on,
			      chkEventWriteAdministrator=on,
			      chkEventWriteLeader=on,
			      chkEventWriteMembers=on,
			      chkEventWriteCustom=on,
			      chkEventEditOwner=on,
			      chkEventEditAdministrator=on,
			      chkEventEditLeader=on,
			      chkEventEditCustom=on,
			      txtBoardWriteCustoms=      {
			         users=         [
			            {
			               id=gdhong@maninsoft.co.kr,
			               name=홍길동
			            }
			         ]
			      },
			      txtBoardEditCustoms=      {
			         users=         [
			            {
			               id=gdhong@maninsoft.co.kr,
			               name=홍길동
			            }
			         ]
			      },
			      txtEventWriteCustoms=      {
			         users=         [
			            {
			               id=gdhong@maninsoft.co.kr,
			               name=홍길동
			            }
			         ]
			      },
			      txtEventEditCustoms=      {
			         users=         [
			            {
			               id=gdhong@maninsoft.co.kr,
			               name=홍길동
			            }
			         ]
			      },
			      imgDepartmentProfile=      {
			         groupId=fg_741486c3b7679b4b7abb1c3b0b12c702087b,
			         files=         [

			         ]
			      }
			   }
			}*/
		String userId = SmartUtil.getCurrentUser().getId();
		String departmentId = (String)requestBody.get("departmentId");
		
		SwaDepartmentCond cond = new SwaDepartmentCond();
		cond.setDeptId(departmentId);
		SwaDepartment[] deptAuths = SwManagerFactory.getInstance().getSwaManager().getAuthDepartments(userId, cond, null);
		
		SwaDepartment boardWrite = null;
		SwaDepartment boardEdit = null;
		SwaDepartment eventWrite = null;
		SwaDepartment eventEdit = null;
		
		if (deptAuths != null && deptAuths.length != 0) {
			for (int i = 0; i < deptAuths.length; i++) {
				SwaDepartment deptAuth = deptAuths[i];
				String deptAuthType = deptAuth.getDeptAuthType();
				if (deptAuthType.equalsIgnoreCase(SwaDepartment.DEPT_AUTHTYPE_BOARD_WRITE)) {
					boardWrite = deptAuth;
				} else if (deptAuthType.equalsIgnoreCase(SwaDepartment.DEPT_AUTHTYPE_BOARD_EDIT)) {
					boardEdit = deptAuth;
				} else if (deptAuthType.equalsIgnoreCase(SwaDepartment.DEPT_AUTHTYPE_EVENT_WRITE)) {
					eventWrite = deptAuth;
				} else if (deptAuthType.equalsIgnoreCase(SwaDepartment.DEPT_AUTHTYPE_EVENT_EDIT)) {
					eventEdit = deptAuth;
				}
			}
		}
		
		Map<String, Object> frmDepartmentSpaceSetting = (Map<String, Object>)requestBody.get("frmDepartmentSpaceSetting");
	    String txtaDepartmentDesc = (String)frmDepartmentSpaceSetting.get("txtaDepartmentDesc");
	    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    
		String chkBoardWriteAdministrator = (String)frmDepartmentSpaceSetting.get("chkBoardWriteAdministrator");
		String chkBoardWriteLeader = (String)frmDepartmentSpaceSetting.get("chkBoardWriteLeader");
		String chkBoardWriteMembers = (String)frmDepartmentSpaceSetting.get("chkBoardWriteMembers");
		String chkBoardWriteCustom = (String)frmDepartmentSpaceSetting.get("chkBoardWriteCustom");
		
		if (boardWrite == null) { 
			boardWrite = new SwaDepartment();
			boardWrite.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_BOARD_WRITE);
			boardWrite.setDeptId(departmentId);
		}
		StringBuffer boardWriteRoleKeyBuff = new StringBuffer();
		boolean isBoardWriteFirst = true;
		if (chkBoardWriteAdministrator != null && chkBoardWriteAdministrator.equalsIgnoreCase("on")) {
			if (!isBoardWriteFirst)
				boardWriteRoleKeyBuff.append(";");
			boardWriteRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_ADMIN);
			isBoardWriteFirst = false;
		}
		if (chkBoardWriteLeader != null && chkBoardWriteLeader.equalsIgnoreCase("on")) {
			if (!isBoardWriteFirst)
				boardWriteRoleKeyBuff.append(";");
			boardWriteRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_LEADER);
			isBoardWriteFirst = false;
		}
		if (chkBoardWriteMembers != null && chkBoardWriteMembers.equalsIgnoreCase("on")) {
			if (!isBoardWriteFirst)
				boardWriteRoleKeyBuff.append(";");
			boardWriteRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_MEMBER);
			isBoardWriteFirst = false;
		}
		String chkBoardWriteCustomUsersStr = null;
		if (chkBoardWriteCustom != null && chkBoardWriteCustom.equalsIgnoreCase("on")) {
			if (!isBoardWriteFirst)
				boardWriteRoleKeyBuff.append(";");
			boardWriteRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_CUSTOM);
			isBoardWriteFirst = false;
			
			Map<String, Object> txtBoardWriteCustoms = (Map<String, Object>)frmDepartmentSpaceSetting.get("txtBoardWriteCustoms");    
			List<Map<String, String>> txtBoardWriteUsers = (ArrayList<Map<String,String>>)txtBoardWriteCustoms.get("users");
			chkBoardWriteCustomUsersStr = getUserIdsStrByList(txtBoardWriteUsers);
		}
		boardWrite.setRoleKey(boardWriteRoleKeyBuff.toString());
		boardWrite.setCustomUser(chkBoardWriteCustomUsersStr);
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String chkBoardEditOwner = (String)frmDepartmentSpaceSetting.get("chkBoardEditOwner");
		String chkBoardEditAdministrator = (String)frmDepartmentSpaceSetting.get("chkBoardEditAdministrator");
		String chkBoardEditLeader = (String)frmDepartmentSpaceSetting.get("chkBoardEditLeader");
		String chkBoardEditCustom = (String)frmDepartmentSpaceSetting.get("chkBoardEditCustom");
		
		if (boardEdit == null) { 
			boardEdit = new SwaDepartment();
			boardEdit.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_BOARD_EDIT);
			boardEdit.setDeptId(departmentId);
		}
		StringBuffer boardEditRoleKeyBuff = new StringBuffer();
		boolean isBoardEditFirst = true;
		
		if (chkBoardEditOwner != null && chkBoardEditOwner.equalsIgnoreCase("on")) {
			if (!isBoardEditFirst)
				boardEditRoleKeyBuff.append(";");
			boardEditRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_OWNER);
			isBoardEditFirst = false;
		}
		if (chkBoardEditAdministrator != null && chkBoardEditAdministrator.equalsIgnoreCase("on")) {
			if (!isBoardEditFirst)
				boardEditRoleKeyBuff.append(";");
			boardEditRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_ADMIN);
			isBoardEditFirst = false;
		}
		if (chkBoardEditLeader != null && chkBoardEditLeader.equalsIgnoreCase("on")) {
			if (!isBoardEditFirst)
				boardEditRoleKeyBuff.append(";");
			boardEditRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_LEADER);
			isBoardEditFirst = false;
		}
		
		String chkBoardEditCustomUsersStr = null;
		if (chkBoardEditCustom != null && chkBoardEditCustom.equalsIgnoreCase("on")) {
			if (!isBoardEditFirst)
				boardEditRoleKeyBuff.append(";");
			boardEditRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_CUSTOM);
			isBoardEditFirst = false;
			
			Map<String, Object> txtBoardEditCustoms = (Map<String, Object>)frmDepartmentSpaceSetting.get("txtBoardEditCustoms");    
			List<Map<String, String>> txtBoardEditUsers = (ArrayList<Map<String,String>>)txtBoardEditCustoms.get("users");
			chkBoardEditCustomUsersStr = getUserIdsStrByList(txtBoardEditUsers);
		}
		boardEdit.setRoleKey(boardEditRoleKeyBuff.toString());
		boardEdit.setCustomUser(chkBoardEditCustomUsersStr);
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String chkEventWriteAdministrator = (String)frmDepartmentSpaceSetting.get("chkEventWriteAdministrator");
		String chkEventWriteLeader = (String)frmDepartmentSpaceSetting.get("chkEventWriteLeader");
		String chkEventWriteMembers = (String)frmDepartmentSpaceSetting.get("chkEventWriteMembers");
		String chkEventWriteCustom = (String)frmDepartmentSpaceSetting.get("chkEventWriteCustom");
		
		if (eventWrite == null) { 
			eventWrite = new SwaDepartment();
			eventWrite.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_EVENT_WRITE);
			eventWrite.setDeptId(departmentId);
		}
		StringBuffer eventWriteRoleKeyBuff = new StringBuffer();
		boolean isEventWriteFirst = true;
		
		if (chkEventWriteAdministrator != null && chkEventWriteAdministrator.equalsIgnoreCase("on")) {
			if (!isEventWriteFirst)
				eventWriteRoleKeyBuff.append(";");
			eventWriteRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_ADMIN);
			isEventWriteFirst = false;
		}
		if (chkEventWriteLeader != null && chkEventWriteLeader.equalsIgnoreCase("on")) {
			if (!isEventWriteFirst)
				eventWriteRoleKeyBuff.append(";");
			eventWriteRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_LEADER);
			isEventWriteFirst = false;
		}
		if (chkEventWriteMembers != null && chkEventWriteMembers.equalsIgnoreCase("on")) {
			if (!isEventWriteFirst)
				eventWriteRoleKeyBuff.append(";");
			eventWriteRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_MEMBER);
			isEventWriteFirst = false;
		}
		String chkEventWriteCustomUsersStr = null;
		if (chkEventWriteCustom != null && chkEventWriteCustom.equalsIgnoreCase("on")) {
			if (!isEventWriteFirst)
				eventWriteRoleKeyBuff.append(";");
			eventWriteRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_CUSTOM);
			isEventWriteFirst = false;
			
			Map<String, Object> txtEventWriteCustoms = (Map<String, Object>)frmDepartmentSpaceSetting.get("txtEventWriteCustoms");    
			List<Map<String, String>> txtEventWriteUsers = (ArrayList<Map<String,String>>)txtEventWriteCustoms.get("users");
			chkEventWriteCustomUsersStr = getUserIdsStrByList(txtEventWriteUsers);
		}
		eventWrite.setRoleKey(eventWriteRoleKeyBuff.toString());
		eventWrite.setCustomUser(chkEventWriteCustomUsersStr);
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String chkEventEditOwner = (String)frmDepartmentSpaceSetting.get("chkEventEditOwner");
		String chkEventEditAdministrator = (String)frmDepartmentSpaceSetting.get("chkEventEditAdministrator");
		String chkEventEditLeader = (String)frmDepartmentSpaceSetting.get("chkEventEditLeader");
		String chkEventEditCustom = (String)frmDepartmentSpaceSetting.get("chkEventEditCustom");
		
		if (eventEdit == null) { 
			eventEdit = new SwaDepartment();
			eventEdit.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_EVENT_EDIT);
			eventEdit.setDeptId(departmentId);
		}
		StringBuffer eventEditRoleKeyBuff = new StringBuffer();
		boolean isEventEditFirst = true;

		if (chkEventEditOwner != null && chkEventEditOwner.equalsIgnoreCase("on")) {
			if (!isEventEditFirst)
				eventEditRoleKeyBuff.append(";");
			eventEditRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_OWNER);
			isEventEditFirst = false;
		}
		if (chkEventEditAdministrator != null && chkEventEditAdministrator.equalsIgnoreCase("on")) {
			if (!isEventEditFirst)
				eventEditRoleKeyBuff.append(";");
			eventEditRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_ADMIN);
			isEventEditFirst = false;
		}
		if (chkEventEditLeader != null && chkEventEditLeader.equalsIgnoreCase("on")) {
			if (!isEventEditFirst)
				eventEditRoleKeyBuff.append(";");
			eventEditRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_LEADER);
			isEventEditFirst = false;
		}
		String chkEventEditCustomUsersStr = null;
		if (chkEventEditCustom != null && chkEventEditCustom.equalsIgnoreCase("on")) {
			if (!isEventEditFirst)
				eventEditRoleKeyBuff.append(";");
			eventEditRoleKeyBuff.append(SwaDepartment.DEPT_ROLEKYE_CUSTOM);
			isEventEditFirst = false;
			
			Map<String, Object> txtEventEditCustoms = (Map<String, Object>)frmDepartmentSpaceSetting.get("txtEventEditCustoms");    
			List<Map<String, String>> txtEventEditUsers = (ArrayList<Map<String,String>>)txtEventEditCustoms.get("users");
			chkEventEditCustomUsersStr = getUserIdsStrByList(txtEventEditUsers);
		}
		eventEdit.setRoleKey(eventEditRoleKeyBuff.toString());
		eventEdit.setCustomUser(chkEventEditCustomUsersStr);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(userId, boardWrite, null);
		SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(userId, boardEdit, null);
		SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(userId, eventWrite, null);
		SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(userId, eventEdit, null);

		Map<String, Object> imgDepartmentProfile = (Map<String, Object>)frmDepartmentSpaceSetting.get("imgDepartmentProfile"); 
		String fileGroupId = (String)imgDepartmentProfile.get("groupId");   
		List<Map<String, String>> deptImgFiles = (ArrayList<Map<String,String>>)imgDepartmentProfile.get("files");
		
		SwoDepartment dept = SwManagerFactory.getInstance().getSwoManager().getDepartment(userId, departmentId, null);
		if (dept != null) {
			dept.setDescription(txtaDepartmentDesc);
			if(!deptImgFiles.isEmpty()) {
				for(int i=0; i < deptImgFiles.subList(0, deptImgFiles.size()).size(); i++) {
					Map<String, String> file = deptImgFiles.get(i);
					String profileFileId = file.get("fileId");
					String profileFileName = file.get("fileName");
					String txtDepartmentProfilePicture = getDocManager().insertProfilesFile(profileFileId, profileFileName, dept.getId() + "_p");
					dept.setPicture(txtDepartmentProfilePicture);
				}
			}
		}
		SwManagerFactory.getInstance().getSwoManager().setDepartment(userId, dept, null);
		getSwoManager().getDepartmentExtend(userId, departmentId, false);
	}
	private String getUserIdsStrByList(List<Map<String, String>> users) throws Exception {
		if (users == null || users.size() == 0)
			return null;
		
		StringBuffer userIdBuff = new StringBuffer();
		boolean isFirst = true;
		if(!CommonUtil.isEmpty(users)) {
			for(int i=0; i < users.subList(0, users.size()).size(); i++) {
				Map<String, String> userMap = users.get(i);
				if (!isFirst)
					userIdBuff.append(";");
				userIdBuff.append(userMap.get("id"));
				isFirst = false;
			}
		}
		return userIdBuff.toString();
	}
	
	
	@Override
	public boolean canIUploadToWorkSpace(String workSpaceId, String workId) throws Exception {
		//workspaceId 는 부서아이디, 그룹아이디가 넘어온다

		User user = SmartUtil.getCurrentUser();
		String cUserId = user.getId();
//		if (cUserId.equalsIgnoreCase(workSpaceId))
			
			
		if (CommonUtil.isEmpty(workSpaceId)) {
			CommunityInfo[] myCommunites = getMyCommunitiesForUpload(workId);
			if (CommonUtil.isEmpty(myCommunites)) {
				return false;
			} else {
				return true;
			}
		}
		
		String type = null;
		if (workId.equalsIgnoreCase(SmartWork.ID_BOARD_MANAGEMENT)) {
			//공지사항
			type = SwaDepartment.DEPT_AUTHTYPE_BOARD_WRITE;
		} else if (workId.equalsIgnoreCase(SmartWork.ID_EVENT_MANAGEMENT)) {
			//일정
			type = SwaDepartment.DEPT_AUTHTYPE_EVENT_WRITE;
		} else {
			return false;
		}
		
		SwaDepartmentCond deptAuthCond = new SwaDepartmentCond();
		deptAuthCond.setDeptId(workSpaceId);
		deptAuthCond.setDeptAuthType(type);
		SwaDepartment deptAuth = SwManagerFactory.getInstance().getSwaManager().getAuthDepartment(user.getId(), deptAuthCond, null);
		
		if (deptAuth != null) {

			String roleKey = deptAuth.getRoleKey();
			String customUser = deptAuth.getCustomUser();
			SwoUserExtend userExtend = getSwoManager().getUserExtend(user.getId(), user.getId(), true);
			String myDepartmentId = userExtend.getDepartmentId();
			boolean isAdmin = userExtend.getAuthId().equalsIgnoreCase("ADMINISTRATOR") ? true : false;
			boolean isLeader = userExtend.getRoleId().equalsIgnoreCase("DEPT LEADER") ? true : false;
			
			if (isAdmin && roleKey.indexOf(SwaDepartment.DEPT_ROLEKYE_ADMIN) != -1)
				return true;
			if (customUser != null && customUser.indexOf(user.getId()) != -1)
				return true;
			
			if (customUser != null && customUser.indexOf(user.getDepartmentId()) != -1)
				return true;
			
			if (customUser != null) {
				GroupInfo[] myGroups = getMyGroups();
				if (!CommonUtil.isEmpty(myGroups)) {
					for (GroupInfo myGroup : myGroups) {
						if (customUser.indexOf(myGroup.getId()) != -1) 
							return true;
					}
				}
			}
			
			if (workSpaceId.equalsIgnoreCase(myDepartmentId)) {
				if (isLeader) {
					if (roleKey.indexOf(SwaDepartment.DEPT_ROLEKYE_LEADER) != -1) {
						return true;
					}
				} else {
					if (roleKey.indexOf(SwaDepartment.DEPT_ROLEKYE_MEMBER) != -1) {
						return true;
					}
				}
			}
			return false;
			
		} else {
			SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup(user.getId(), workSpaceId, null);
			if (group == null)
				return false;
			
			SwaGroupCond groupAuthCond = new SwaGroupCond();
			groupAuthCond.setGroupId(workSpaceId);
			groupAuthCond.setGroupAuthType(type);
			SwaGroup groupAuth = SwManagerFactory.getInstance().getSwaManager().getAuthGroup(user.getId(), groupAuthCond, null);
			if (groupAuth == null)
				return false;
			
			String roleKey = groupAuth.getRoleKey();
			String customUser = groupAuth.getCustomUser();
			SwoUserExtend userExtend = getSwoManager().getUserExtend(user.getId(), user.getId(), true);
			
			boolean isAdmin = userExtend.getAuthId().equalsIgnoreCase("ADMINISTRATOR") ? true : false;
			boolean isLeader = group.getGroupLeader().equalsIgnoreCase(user.getId()) ? true : false;
			
			boolean isMyGroups = false;
			
			SwoGroupMember[] groupMember = group.getSwoGroupMembers();
			if (groupMember != null && groupMember.length != 0) {
				for (int i = 0; i < groupMember.length; i++) {
					if (groupMember[i].getUserId().equalsIgnoreCase(user.getId())) {
						isMyGroups = true;
						break;
					}
				}
			}
			
			if (isAdmin && roleKey.indexOf(SwaDepartment.DEPT_ROLEKYE_ADMIN) != -1)
				return true;
			
			if (customUser != null && customUser.indexOf(user.getId()) != -1)
				return true;
			
			if (customUser != null && customUser.indexOf(user.getDepartmentId()) != -1)
				return true;
			
			if (customUser != null) {
				GroupInfo[] myGroups = getMyGroups();
				if (!CommonUtil.isEmpty(myGroups)) {
					for (GroupInfo myGroup : myGroups) {
						if (customUser.indexOf(myGroup.getId()) != -1) 
							return true;
					}
				}
			}
			
			if (isMyGroups) {
				if (isLeader) {
					if (roleKey.indexOf(SwaDepartment.DEPT_ROLEKYE_LEADER) != -1) {
						return true;
					}
				} else {
					if (roleKey.indexOf(SwaDepartment.DEPT_ROLEKYE_MEMBER) != -1) {
						return true;
					}
				}
			}
			return false;
		}
	}
	
	@Override
	public UserInfo[] getAllUsersByDepartmentId(String departmentId) throws Exception {
		
		String userId = SmartUtil.getCurrentUser().getId();
		
		List<String> relatedUserIdList = new ArrayList<String>();
		if (departmentId != null) {

			SwoUserCond userCond = new SwoUserCond();
			userCond.setDeptIdWithAdjunct(departmentId);
			
			SwoUser[] relatedUserObjs = getSwoManager().getUsers(userId, userCond, IManager.LEVEL_LITE);
			if (relatedUserObjs != null) {
				for (int i = 0; i < relatedUserObjs.length; i++) {
					SwoUser relatedUserObj = relatedUserObjs[i];
					
					relatedUserIdList.add(relatedUserObj.getId());//자기 부서원들을 array에 포함시킨다
				}
			}
			//자기 하위부서의 사람들도 포함시킨다(재귀함수를 이용)
			addSubDepartmentUsers(userId, departmentId, relatedUserIdList);//userDeptId의 자식 부서들의 사용자들을 array에 추가시킨다
		}
		
		String[] relatedUserIdArray = new String[relatedUserIdList.size()];
		relatedUserIdList.toArray(relatedUserIdArray);
		
		SwoUserExtend[] userExtends = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(userId, relatedUserIdArray);
		
		if(SmartUtil.isBlankObject(userExtends)) return null;
		
		UserInfo[] userInfos = new UserInfo[userExtends.length];
		for (int i = 0; i < userExtends.length; i++) {
			SwoUserExtend userExtend = userExtends[i];
			userInfos[i] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtend);
		}
		return userInfos;
	}
	private void addSubDepartmentUsers(String user, String parentDeptId, List<String> userList) throws Exception {

		SwoDepartmentCond deptCond = new SwoDepartmentCond();
		deptCond.setParentId(parentDeptId);
		SwoDepartment[] subDeptObjs = getSwoManager().getDepartments(user, deptCond, IManager.LEVEL_LITE);
		if (subDeptObjs == null)
			return;
		for (int i = 0; i < subDeptObjs.length; i++) {
			SwoDepartment subDeptObj = subDeptObjs[i];
			SwoUserCond userCond = new SwoUserCond();
			userCond.setDeptIdWithAdjunct(subDeptObj.getId());
			SwoUser[] teamUsers = getSwoManager().getUsers(user, userCond, IManager.LEVEL_LITE);
			if (teamUsers != null) {
				for (int j = 0; j < teamUsers.length; j++) {
					SwoUser teamUser = teamUsers[j];
					String teamUserId = teamUser.getId();
					
					if (!userList.contains(teamUserId)); {
						userList.add(teamUserId);
					}
				}
			}
			//재귀호출
			addSubDepartmentUsers(user, subDeptObj.getId(), userList);
		}	
	}
	int previousPageSize = 0;
	@Override
	public CommunityInfoList getCommunityInstanceList(int type, RequestParams params) throws Exception {
		
		CommunityInfoList communityInfoList = null;
		
		if(type == CommunityInfoList.TYPE_GROUP_INFO_LIST)
			communityInfoList = getGroupInfoList(params);
		else if(type == CommunityInfoList.TYPE_DEPARTMENT_INFO_LIST)
			communityInfoList = getDepartmentInfoList(params);
		else if(type == CommunityInfoList.TYPE_USER_INFO_LIST)
			communityInfoList = getUserInfoList(params);
		
		applyFavortyCommunityInfoToCommunityInfoList(communityInfoList);
		
		return communityInfoList;
	}
	private void applyFavortyCommunityInfoToCommunityInfoList(CommunityInfoList communityInfoList) throws Exception {
		
		if (CommonUtil.isEmpty(communityInfoList))
			return;
		
		User user = SmartUtil.getCurrentUser();
		List myFavorityCommunityList = new ArrayList();
		String companyId = user.getCompanyId();
		String userId = user.getId();
		ItmMenuItemListCond menuItemListCond = new ItmMenuItemListCond();
		menuItemListCond.setUserId(userId);
		ItmMenuItemList menuItemList = SwManagerFactory.getInstance().getItmManager().getMenuItemList(userId, menuItemListCond, IManager.LEVEL_ALL);
		if (menuItemList != null) {
			ItmMenuItem[] menuItems = menuItemList.getMenuItems();
			if (!CommonUtil.isEmpty(menuItems)) {
				for (int i = 0; i < menuItems.length; i++) {
					ItmMenuItem menuItem = menuItems[i];
					if (menuItem == null)
						continue;
					myFavorityCommunityList.add(menuItem.getPackageId());	
				}
			}
		}
		CommunityInfo[] communityInfos = communityInfoList.getCommunityDatas();
		if (CommonUtil.isEmpty(communityInfos))
			return;
		
		for (int i = 0; i < communityInfos.length; i++) {
			CommunityInfo communityInfo = communityInfos[i];
			if (myFavorityCommunityList.contains(communityInfo.getId()))
					communityInfo.setFavorite(true);
		}
	}
	
	
	private CommunityInfoList getGroupInfoList(RequestParams params) throws Exception{
		try {
			User user = SmartUtil.getCurrentUser();
			if(user == null)
				return null;
			String userId = user.getId();

			SwoGroupCond swoGroupCond = new SwoGroupCond();
			
			String filterId = params.getFilterId();
			if(filterId != null) {
				if(filterId.equals(SearchFilter.FILTER_ALL_INSTANCES)) {
				} else if(filterId.equals(SearchFilter.FILTER_MY_INSTANCES)) {
					SwoGroupMember swoGroupMember = new SwoGroupMember();       
					swoGroupMember.setUserId(user.getId());		
					SwoGroupMember[] swoGroupMembers = new SwoGroupMember[1];
					swoGroupMembers[0] = swoGroupMember;
					swoGroupCond.setSwoGroupMembers(swoGroupMembers);
				}
			}

			String searchKey = params.getSearchKey();
			if(!CommonUtil.isEmpty(searchKey))
				swoGroupCond.setNameLike(searchKey);

			String[] groupIdsByNotBelongToClosedGroup = ModelConverter.getGroupIdsByNotBelongToClosedGroup(user);
			swoGroupCond.setGroupIdNotIns(groupIdsByNotBelongToClosedGroup);

			long totalCount =  getSwoManager().getGroupSize(user.getId(), swoGroupCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > (int)totalCount)
				currentPage = 1;

			if (currentPage > 0)
				swoGroupCond.setPageNo(currentPage-1);
	
			CommunityInfoList instanceInfoList = new CommunityInfoList();
			instanceInfoList.setType(CommunityInfoList.TYPE_GROUP_INFO_LIST);
			
			SortingField sf = params.getSortingField();
			String columnName = "";
			boolean isAsc;

			if (sf != null) {
				columnName  = CommonUtil.toDefault(sf.getFieldId(), SwoGroup.A_CREATIONDATE);
				isAsc = sf.isAscending();
			} else {
				columnName = SwoGroup.A_CREATIONDATE;
				isAsc = false;
			}
			SortingField sortingField = new SortingField();
			sortingField.setFieldId(columnName);
			sortingField.setAscending(isAsc);

			if(totalCount>0){
				swoGroupCond.setPageSize(pageSize);
	
				swoGroupCond.setOrders(new Order[]{new Order(columnName, isAsc)});
	
				SwoGroup[] swoGroups = getSwoManager().getGroups(user.getId(), swoGroupCond, IManager.LEVEL_ALL);
				
				if(CommonUtil.isEmpty(swoGroups))
					return null;
		
				int recordSize = swoGroups.length;
	
				List<GroupInfo> groupInfoList = new ArrayList<GroupInfo>();
				for(int i=0; i<recordSize; i++) {
					GroupInfo groupInfo = ModelConverter.getGroupInfoBySwoGroup(null, swoGroups[i]);
					groupInfoList.add(groupInfo);
				}
				if(!CommonUtil.isEmpty(groupInfoList)) {
					GroupInfo[] groupInfos = new GroupInfo[groupInfoList.size()];
					groupInfoList.toArray(groupInfos);
					instanceInfoList.setCommunityDatas(groupInfos);
				}			
			}

			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sortingField);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);

			return instanceInfoList;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private CommunityInfoList getDepartmentInfoList(RequestParams params) throws Exception{
		try {
			User user = SmartUtil.getCurrentUser();
			if(user == null)
				return null;
			String userId = user.getId();

			SwoDepartmentCond swoDepartmentCond = new SwoDepartmentCond();
			
			swoDepartmentCond.setIdNotIns(new String[]{Department.DEPARTMENT_ID_ROOT});
			String filterId = params.getFilterId();
			if(filterId != null) {
				if(filterId.equals(SearchFilter.FILTER_ALL_INSTANCES)) {
				} else if(filterId.equals(SearchFilter.FILTER_MY_ASCEND_DEPARTMENTS)) {
					DepartmentInfo[] ascendentDepartments = getMyDepartments();
					if(!SmartUtil.isBlankObject(ascendentDepartments)){
						String[] idIns = new String[ascendentDepartments.length];
						for(int i=0; i<ascendentDepartments.length; i++){
							idIns[i] = ascendentDepartments[i].getId();
						}
						swoDepartmentCond.setIdIns(idIns);
					}
				} else if(filterId.equals(SearchFilter.FILTER_MY_DESCEND_DEPARTMENTS)) {
					DepartmentInfo[] descendentDepartments = getMyChildDepartments();
					if(!SmartUtil.isBlankObject(descendentDepartments)){
						String[] idIns = new String[descendentDepartments.length];
						for(int i=0; i<descendentDepartments.length; i++){
							idIns[i] = descendentDepartments[i].getId();
						}
						swoDepartmentCond.setIdIns(idIns);
					}
				}
			}

			String searchKey = params.getSearchKey();
			if(!CommonUtil.isEmpty(searchKey))
				swoDepartmentCond.setNameLike(searchKey);

			long totalCount =  getSwoManager().getDepartmentSize(userId, swoDepartmentCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > (int)totalCount)
				currentPage = 1;

			if (currentPage > 0)
				swoDepartmentCond.setPageNo(currentPage-1);
	
			CommunityInfoList instanceInfoList = new CommunityInfoList();
			instanceInfoList.setType(CommunityInfoList.TYPE_DEPARTMENT_INFO_LIST);
			
			SortingField sf = params.getSortingField();
			String columnName = "";
			boolean isAsc;

			if (sf != null) {
				columnName  = CommonUtil.toDefault(sf.getFieldId(), SwoDepartment.A_MODIFICATIONDATE);
				isAsc = sf.isAscending();
			} else {
				columnName = SwoDepartment.A_MODIFICATIONDATE;
				isAsc = false;
			}
			SortingField sortingField = new SortingField();
			sortingField.setFieldId(columnName);
			sortingField.setAscending(isAsc);

			if(totalCount>0){
				swoDepartmentCond.setPageSize(pageSize);
	
				swoDepartmentCond.setOrders(new Order[]{new Order(columnName, isAsc)});
	
				SwoDepartment[] swoDepartments = getSwoManager().getDepartments(user.getId(), swoDepartmentCond, IManager.LEVEL_ALL);
				
				if(CommonUtil.isEmpty(swoDepartments))
					return null;
		
				int recordSize = swoDepartments.length;
	
				List<DepartmentInfo> departmentInfoList = new ArrayList<DepartmentInfo>();
				for(int i=0; i<recordSize; i++) {
					DepartmentInfo departmentInfo = ModelConverter.getDepartmentInfoByDepartmentId(swoDepartments[i].getId());
					UserInfo[] members = getAllUsersByDepartmentId(departmentInfo.getId());
					CommunityInfo[] descendent = getAllComsByDepartmentId(departmentInfo.getId(), true);
					if(!SmartUtil.isBlankObject(members))
						departmentInfo.setNumberOfMembers(members.length);
					if(!SmartUtil.isBlankObject(descendent))
						departmentInfo.setNumberOfDescendents(descendent.length);
					departmentInfo.setLastModificationDate(new LocalDate(swoDepartments[i].getModificationDate().getTime()));
					departmentInfo.setLastModificationUser(ModelConverter.getUserInfoByUserId(swoDepartments[i].getModificationUser()));
					departmentInfoList.add(departmentInfo);
				}
				if(!CommonUtil.isEmpty(departmentInfoList)) {
					DepartmentInfo[] departmentInfos = new DepartmentInfo[departmentInfoList.size()];
					departmentInfoList.toArray(departmentInfos);
					instanceInfoList.setCommunityDatas(departmentInfos);
				}			
			}

			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sortingField);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);

			return instanceInfoList;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private CommunityInfoList getUserInfoList(RequestParams params) throws Exception{
		try {
			User user = SmartUtil.getCurrentUser();
			if(user == null)
				return null;
			String userId = user.getId();

			SwoUserCond swoUserCond = new SwoUserCond();
			
			swoUserCond.setIdNotIns(new String[]{User.USER_ID_PROCESS, User.USER_ID_ADMINISTRATOR, "admin"});
			String filterId = params.getFilterId();
			if(filterId != null) {
				if(filterId.equals(SearchFilter.FILTER_ALL_INSTANCES)) {
				} else if(filterId.equals(SearchFilter.FILTER_MY_ASCEND_DEPARTMENTS)) {
				} else if(filterId.equals(SearchFilter.FILTER_MY_DESCEND_DEPARTMENTS)) {
				}
			}

			String searchKey = params.getSearchKey();
			if(!CommonUtil.isEmpty(searchKey))
				swoUserCond.setNameLike(searchKey);

			long totalCount =  getSwoManager().getUserSize(userId, swoUserCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > (int)totalCount)
				currentPage = 1;

			if (currentPage > 0)
				swoUserCond.setPageNo(currentPage-1);
	
			CommunityInfoList instanceInfoList = new CommunityInfoList();
			instanceInfoList.setType(CommunityInfoList.TYPE_USER_INFO_LIST);
			
			SortingField sf = params.getSortingField();
			String columnName = "";
			boolean isAsc;

			if (sf != null) {
				columnName  = CommonUtil.toDefault(sf.getFieldId(), SwoUser.A_NAME);
				isAsc = sf.isAscending();
			} else {
				columnName = SwoUser.A_NAME;
				isAsc = false;
			}
			SortingField sortingField = new SortingField();
			sortingField.setFieldId(columnName);
			sortingField.setAscending(isAsc);

			if(totalCount>0){
				swoUserCond.setPageSize(pageSize);
	
				swoUserCond.setOrders(new Order[]{new Order(columnName, isAsc)});
	
				SwoUser[] swoUsers = getSwoManager().getUsers(user.getId(), swoUserCond, IManager.LEVEL_ALL);
				
				if(CommonUtil.isEmpty(swoUsers))
					return null;
		
				int recordSize = swoUsers.length;
	
				List<UserInfo> userInfoList = new ArrayList<UserInfo>();
				for(int i=0; i<recordSize; i++) {
					UserInfo userInfo = ModelConverter.getUserInfoByUserId(swoUsers[i].getId());
					DepartmentInfo departmentInfo = userInfo.getDepartment();
					if(!SmartUtil.isBlankObject(departmentInfo)){
						departmentInfo.setFullpathName(ModelConverter.getDepartmentInfoFullpathNameByDepartmentId(departmentInfo.getId()));
						userInfo.setDepartment(departmentInfo);
					}
					userInfoList.add(userInfo);
				}
				if(!CommonUtil.isEmpty(userInfoList)) {
					UserInfo[] userInfos = new UserInfo[userInfoList.size()];
					userInfoList.toArray(userInfos);
					instanceInfoList.setCommunityDatas(userInfos);
				}			
			}

			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sortingField);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);

			return instanceInfoList;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private CommunityInfo[] getCommunityInfoByIds(String[] communityIds) throws Exception {
		if (CommonUtil.isEmpty(communityIds))
			return null;
		
		List<CommunityInfo> resultList = new ArrayList<CommunityInfo>();
		for (int i = 0; i < communityIds.length; i++) {
			String communityId = communityIds[i];
			if (communityId.indexOf("@") != -1) {
				//사용자공간
				UserInfo userInfo = ModelConverter.getUserInfoByUserId(communityId);
				if (!CommonUtil.isEmpty(userInfo)) {
					resultList.add(userInfo);
				}
				
			} else if (communityId.indexOf("dept_") != -1) {
				//부서공간
				DepartmentInfo deptInfo = ModelConverter.getDepartmentInfoByDepartmentId(communityId);
				if (!CommonUtil.isEmpty(deptInfo)) {
					resultList.add(deptInfo);
				}
				
			} else if (communityId.indexOf("group_") != -1) {
				//그룹공간
				GroupInfo groupInfo = ModelConverter.getGroupInfoByGroupId(communityId);
				if (!CommonUtil.isEmpty(groupInfo)) {
					resultList.add(groupInfo);
				}
				
			} else {
				//부서공간
				DepartmentInfo deptInfo = ModelConverter.getDepartmentInfoByDepartmentId(communityId);
				if (!CommonUtil.isEmpty(deptInfo)) {
					resultList.add(deptInfo);
				}
			}
		}
		CommunityInfo[] communityInfos = new CommunityInfo[resultList.size()];
		resultList.toArray(communityInfos);
		return communityInfos;
	}
	
	
	@Override
	public CommunityInfo[] getMyFavoriteCommunities() throws Exception {		
		User cUser = SmartUtil.getCurrentUser();
		String companyId = cUser.getCompanyId();
		String userId = cUser.getId();
		
		ItmMenuItemListCond menuItemListCond = new ItmMenuItemListCond();
		menuItemListCond.setUserId(userId);

		ItmMenuItemList menuItemList = SwManagerFactory.getInstance().getItmManager().getMenuItemList(userId, menuItemListCond, IManager.LEVEL_ALL);
		if (menuItemList == null) 
			return null;
		
		ItmMenuItem[] menuItems = menuItemList.getMenuItems();
		
		if (CommonUtil.isEmpty(menuItems))
			return null;
		
		List<String> communityIdList = new ArrayList<String>();
		for (int i = 0; i < menuItems.length; i++) {
			ItmMenuItem menuItem = menuItems[i];
			if (menuItem != null && menuItem.getPackageType() != null && (menuItem.getPackageType().equalsIgnoreCase(ItmMenuItem.FAV_COMMUNITY_TYPE_USER)
					|| menuItem.getPackageType().equalsIgnoreCase(ItmMenuItem.FAV_COMMUNITY_TYPE_DEPT)
						|| menuItem.getPackageType().equalsIgnoreCase(ItmMenuItem.FAV_COMMUNITY_TYPE_GROUP))) {
				communityIdList.add(menuItem.getPackageId());
			}
		}
		String[] communityIdArray = new String[communityIdList.size()];
		communityIdList.toArray(communityIdArray);
		
		return getCommunityInfoByIds(communityIdArray);
	}
	@Override
	public void addAFavoriteCommunity(String comId) throws Exception {
		try{
			User cUser = SmartUtil.getCurrentUser();
			String companyId = cUser.getCompanyId();
			String userId = cUser.getId();
	
			ItmMenuItemListCond menuItemListCond = new ItmMenuItemListCond();
			menuItemListCond.setUserId(userId);
	
			ItmMenuItemList menuItemList = SwManagerFactory.getInstance().getItmManager().getMenuItemList(userId, menuItemListCond, IManager.LEVEL_ALL);
	
			CommunityInfo[] communityInfos = getCommunityInfoByIds(new String[]{comId});
			if (communityInfos == null)
				return;
			
			//커뮤니티아이디(사용자아이디, 부서아이디, 그룹아이디)
			if (menuItemList == null) {
				menuItemList = new ItmMenuItemList();
				menuItemList.setCompanyId(companyId);
				menuItemList.setUserId(userId);
			} else {
				ItmMenuItem[] menuItems = menuItemList.getMenuItems();
				
				if (!CommonUtil.isEmpty(menuItems)) {
					for (int i = 0; i < menuItems.length; i++) {
						ItmMenuItem menuItem = menuItems[i];
						if (menuItem.getPackageId() != null && menuItem.getPackageId().equalsIgnoreCase(comId)) {
							return;
						}
					}
				}
			}
			
			ItmMenuItem menuItem = new ItmMenuItem();
			menuItem.setCompanyId(companyId);
			menuItem.setPackageId(comId);
			menuItem.setName(communityInfos[0].getName());
			
			String communityType = null;
			
			switch (communityInfos[0].getSpaceType()) {
			case ISmartWorks.SPACE_TYPE_USER :
				communityType = ItmMenuItem.FAV_COMMUNITY_TYPE_USER;
				break;
				
			case ISmartWorks.SPACE_TYPE_DEPARTMENT:
				communityType = ItmMenuItem.FAV_COMMUNITY_TYPE_DEPT;
				break;
				
			case ISmartWorks.SPACE_TYPE_GROUP:
				communityType = ItmMenuItem.FAV_COMMUNITY_TYPE_GROUP;
				break;
			}
			
			menuItem.setPackageType(communityType);
			menuItemList.addMenuItem(menuItem);
	
			SwManagerFactory.getInstance().getItmManager().setMenuItemList(userId, menuItemList, IManager.LEVEL_ALL);

		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	@Override
	public void removeAFavoriteCommunity(String comId) throws Exception {

		User cUser = SmartUtil.getCurrentUser();
		String companyId = cUser.getCompanyId();
		String userId = cUser.getId();
		
		ItmMenuItemListCond menuItemListCond = new ItmMenuItemListCond();
		menuItemListCond.setUserId(userId);

		ItmMenuItemList menuItemList = SwManagerFactory.getInstance().getItmManager().getMenuItemList(userId, menuItemListCond, IManager.LEVEL_ALL);
		if (menuItemList == null) 
			return;
		
		ItmMenuItem[] menuItems = menuItemList.getMenuItems();
		
		if (CommonUtil.isEmpty(menuItems))
			return;
		
		for (int i = 0; i < menuItems.length; i++) {
			ItmMenuItem menuItem = menuItems[i];
			
			if (menuItem.getPackageId() != null && menuItem.getPackageId().equalsIgnoreCase(comId)) {
				menuItemList.removeMenuItem(menuItem);
				break;
			}
		}
		SwManagerFactory.getInstance().getItmManager().setMenuItemList(userId, menuItemList, IManager.LEVEL_ALL);
	}
	@Override
	public boolean isEditable_Board_EventWorkInstanceBySpacePolicy(InformationWorkInstance instance) throws Exception {
		if (CommonUtil.isEmpty(instance))
			return false;
		
		if (!instance.getWork().getId().equalsIgnoreCase(SmartWork.ID_BOARD_MANAGEMENT) && !instance.getWork().getId().equalsIgnoreCase(SmartWork.ID_EVENT_MANAGEMENT)) 
			return false;

		if (instance.getWorkSpace().getSpaceType() != ISmartWorks.SPACE_TYPE_DEPARTMENT && instance.getWorkSpace().getSpaceType() != ISmartWorks.SPACE_TYPE_GROUP) 
			return false;
		

		User cUser = SmartUtil.getCurrentUser();
		String companyId = cUser.getCompanyId();
		String userId = cUser.getId();
		String departmentId = cUser.getDepartmentId();
		
		boolean isEditableForMeBySpacePolicy = false;			
		//업무가 부서,그룹 타입이냐
			
		String workSpaceId = instance.getWorkSpace().getId();
		switch(instance.getWorkSpace().getSpaceType()){
		case ISmartWorks.SPACE_TYPE_DEPARTMENT:
			
			SwaDepartmentCond deptAuthCond = new SwaDepartmentCond();
			deptAuthCond.setDeptId(workSpaceId);
			
			if (instance.getWork().getId().equalsIgnoreCase(SmartWork.ID_BOARD_MANAGEMENT)) {
				deptAuthCond.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_BOARD_EDIT);
			} else if (instance.getWork().getId().equalsIgnoreCase(SmartWork.ID_EVENT_MANAGEMENT)) {
				deptAuthCond.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_EVENT_EDIT);
			}
			SwaDepartment[] authDepts = SwManagerFactory.getInstance().getSwaManager().getAuthDepartments("", deptAuthCond, null);
			
			if (authDepts == null)
				break;
			
			SwaDepartment editAuth = authDepts[0];
			
			String roleKeysStr = editAuth.getRoleKey();
			String[] roleKeys = StringUtils.tokenizeToStringArray(roleKeysStr, ";");
			for (int i = 0; i < roleKeys.length; i++) {
				if (roleKeys[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_CUSTOM)) {
					String usersStr = editAuth.getCustomUser();
					String[] tempUsers = StringUtils.tokenizeToStringArray(usersStr, ";");
					String[] users = ModelConverter.convertUserIdsByUserAndDeptAndGroupIdArray(tempUsers);
					if (CommonUtil.isEmpty(users))
						continue;
					for (int j = 0; j < users.length; j++) {
						String name = users[j];
						if (name.equalsIgnoreCase(userId))
							return true;
					}
					
				} else if (roleKeys[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_LEADER)) {
					int userRole = cUser.getRole();
					
					if (departmentId.equalsIgnoreCase(instance.getWorkSpace().getId()) && userRole == User.USER_ROLE_LEADER)
						return true;
					
				} else if (roleKeys[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_ADMIN)) {
					
					if (cUser.getUserLevel() == User.USER_LEVEL_AMINISTRATOR)
						return true;
				}
			}
			break;
		case ISmartWorks.SPACE_TYPE_GROUP:

			
			SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup("", workSpaceId, IManager.LEVEL_ALL);
			if (CommonUtil.isEmpty(group))
				return false;
			
			SwaGroupCond groupAuthCond = new SwaGroupCond();
			groupAuthCond.setGroupId(workSpaceId);
			if (instance.getWork().getId().equalsIgnoreCase(SmartWork.ID_BOARD_MANAGEMENT)) {
				groupAuthCond.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_BOARD_EDIT);
			} else if (instance.getWork().getId().equalsIgnoreCase(SmartWork.ID_EVENT_MANAGEMENT)) {
				groupAuthCond.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_EVENT_EDIT);
			}
			SwaGroup[] authGroups = SwManagerFactory.getInstance().getSwaManager().getAuthGroups("", groupAuthCond, null);
			
			if (authGroups == null)
				break;

			SwaGroup editGroupAuth = authGroups[0];
			
			String groupRoleKeysStr = editGroupAuth.getRoleKey();
			String[] groupRoleKeys = StringUtils.tokenizeToStringArray(groupRoleKeysStr, ";");
			for (int i = 0; i < groupRoleKeys.length; i++) {
				if (groupRoleKeys[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_CUSTOM)) {
					String usersStr = editGroupAuth.getCustomUser();
					String[] tempUsers = StringUtils.tokenizeToStringArray(usersStr, ";");
					String[] users = ModelConverter.convertUserIdsByUserAndDeptAndGroupIdArray(tempUsers);
					if (CommonUtil.isEmpty(users))
						continue;
					for (int j = 0; j < users.length; j++) {
						String name = users[j];
						if (name.equalsIgnoreCase(userId))
							return true;
					}
					
				} else if (groupRoleKeys[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_LEADER)) {
				
					String groupLeaderId = group.getGroupLeader();
					
					if (userId.equalsIgnoreCase(groupLeaderId))
						return true;
					
				} else if (groupRoleKeys[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_ADMIN)) {
					
					if (cUser.getUserLevel() == User.USER_LEVEL_AMINISTRATOR)
						return true;
				}
			}
			break;
		}	
		return false;
	}
}
