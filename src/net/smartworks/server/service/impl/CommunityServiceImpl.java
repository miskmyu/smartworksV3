package net.smartworks.server.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.Department;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.community.info.CommunityInfo;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.mail.MailAccount;
import net.smartworks.model.notice.Notice;
import net.smartworks.model.sera.Course;
import net.smartworks.server.engine.common.loginuser.manager.ILoginUserManager;
import net.smartworks.server.engine.common.loginuser.model.LoginUser;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Filters;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.SmartServerConstant;
import net.smartworks.server.engine.common.searcher.manager.ISchManager;
import net.smartworks.server.engine.common.searcher.model.SchUser;
import net.smartworks.server.engine.common.searcher.model.SchWorkspace;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.id.IDCreator;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainFieldConstants;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
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
import net.smartworks.server.engine.publishnotice.manager.IPublishNoticeManager;
import net.smartworks.server.engine.publishnotice.model.PublishNotice;
import net.smartworks.server.engine.security.model.Login;
import net.smartworks.server.engine.sera.manager.ISeraManager;
import net.smartworks.server.engine.sera.model.CourseDetail;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.server.service.util.SearchParallelProcessing;
import net.smartworks.util.LocalDate;
import net.smartworks.util.Semaphore;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
public class CommunityServiceImpl implements ICommunityService {

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

	@Autowired
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
			getDeptTreeByDeptId(deptList, myDeptId);
			DepartmentInfo[] deptInfos = new DepartmentInfo[deptList.size()];
			int index = deptList.size() - 1;
			for (int i = 0; i < deptList.size(); i++) {
				DepartmentInfo deptInfo = new DepartmentInfo();
				SwoDepartment swDept = deptList.get(i);
				deptInfo.setId(swDept.getId());
				deptInfo.setName(swDept.getName());
				deptInfo.setDesc(swDept.getDescription());
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
			if(swoGroups != null) {
				for(SwoGroup swoGroup : swoGroups) {
					GroupInfo groupInfo = ModelConverter.getGroupInfoByGroupId(swoGroup.getId());
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
							swoGroupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_READY);
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
					getLoginUserManager().createLoginUser(loginUserId, loginUser);
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
		String groupId = (String)requestBody.get("courseId");
		String userId = (String)requestBody.get("userId");
		
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
				seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, 1, true);
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
				userIdArray[i] = userMap.get("userId");
			}
		}
		if (userIdArray == null || userIdArray.length == 0)
			return;
		
		SwoGroup group = getSwoManager().getGroup("", groupId, IManager.LEVEL_ALL);
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
		seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, userIdArray.length, true);
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
			seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, 1, true);
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

		seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, 1, false);

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

		seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, 1, false);

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
						if(!CommonUtil.isEmpty(picture)) {
							String extension = picture.lastIndexOf(".") > 1 ? picture.substring(picture.lastIndexOf(".") + 1) : null;
							String pictureId = picture.substring(0, (picture.length() - extension.length())-1);
							userInfo.setSmallPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
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
				fs2.addFilter(new Filter("=", colCategory, Filter.OPERANDTYPE_STRING, categoryId));
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
					vMailAccount.setUserName(sMailAccount.getMailId()+"@"+sMailAccount.getMailServerName());
					vMailAccount.setPassword(sMailAccount.getMailPassword());
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

}