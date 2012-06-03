package net.smartworks.server.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import net.smartworks.model.notice.Notice;
import net.smartworks.model.sera.Course;
import net.smartworks.server.engine.common.loginuser.manager.ILoginUserManager;
import net.smartworks.server.engine.common.loginuser.model.LoginUser;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.SmartServerConstant;
import net.smartworks.server.engine.common.searcher.manager.ISchManager;
import net.smartworks.server.engine.common.searcher.model.SchUser;
import net.smartworks.server.engine.common.searcher.model.SchWorkspace;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.id.IDCreator;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoDepartment;
import net.smartworks.server.engine.organization.model.SwoDepartmentCond;
import net.smartworks.server.engine.organization.model.SwoGroup;
import net.smartworks.server.engine.organization.model.SwoGroupCond;
import net.smartworks.server.engine.organization.model.SwoGroupMember;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.organization.model.SwoUserCond;
import net.smartworks.server.engine.organization.model.SwoUserExtend;
import net.smartworks.server.engine.sera.manager.ISeraManager;
import net.smartworks.server.engine.sera.model.CourseDetail;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityServiceImpl implements ICommunityService {

	private ISwoManager getSwoManager() {
		return SwManagerFactory.getInstance().getSwoManager();
	}
	private IDocFileManager getDocManager() {
		return SwManagerFactory.getInstance().getDocManager();
	}
	private ISchManager getSchManager() {
		return SwManagerFactory.getInstance().getSchManager();
	}
	private ILoginUserManager getLoginUserManager() {
		return SwManagerFactory.getInstance().getLoginUserManager();
	}

	private ISeraService seraService = null;

	@Autowired
	public void setSeraService(ISeraService seraService) {
		this.seraService = seraService;
	}

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
	public String setGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			Map<String, Object> frmNewGroupProfile = (Map<String, Object>)requestBody.get("frmNewGroupProfile");
	
			Set<String> keySet = frmNewGroupProfile.keySet();
			Iterator<String> itr = keySet.iterator();
	
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
							selGroupProfileType = SwoGroup.GROUP_STATUS_OPEN;
						else
							selGroupProfileType = SwoGroup.GROUP_STATUS_CLOSED;
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
					//그룹멤버가 부서 일경우 비교
					if(userMap.get("id").matches(".*@.*")){
						if(!txtGroupLeader.equals(groupUserId)) {
							SwoGroupMember swoGroupMember = new SwoGroupMember();
							swoGroupMember.setUserId(groupUserId);
							swoGroupMember.setJoinType(SwoGroupMember.JOINTYPE_INVITE);
							swoGroupMember.setJoinStatus(SwoGroupMember.JOINSTATUS_READY);
							swoGroupMember.setJoinDate(new LocalDate());
							swoGroup.addGroupMember(swoGroupMember);
						}
					}else{
						//부서 일 경우 추가식
						String departmentId = groupUserId;
						SwoUser[] deptUsers = getUsersByDeptId("", departmentId);
						//그룹안에 그룹을 넣는 경우
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
									swoGroup.addGroupMember(swoGroupMember);
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
				if(type.equals("user")) {
					User user = this.getUserById(workSpaceId);
					return user;
				} else if(type.equals("department")) {
					Department department = this.getDepartmentById(workSpaceId);
					return department;
				} else if(type.equals("group")) {
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
				if(type.equals("user")) {
					UserInfo userInfo = ModelConverter.getUserInfoByUserId(workSpaceId);
					return userInfo;
				} else if(type.equals("department")) {
					DepartmentInfo departmentInfo = ModelConverter.getDepartmentInfoByDepartmentId(workSpaceId);
					return departmentInfo;
				} else if(type.equals("group")) {
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
			LoginUser[] loginUsers = getLoginUserManager().getLoginUsers(userId, null, IManager.LEVEL_ALL);

			UserInfo[] userInfos = null;
			List<UserInfo> userInfoList = new ArrayList<UserInfo>();

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
		
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		
		SwoGroup group = swoMgr.getGroup(userId, groupId, IManager.LEVEL_ALL);
		if (group == null)
			return;
		if (group.isContainGroupMember(userId))
			return;
		SwoGroupMember groupMember = new SwoGroupMember();
		groupMember.setGroupId(groupId);
		groupMember.setUserId(userId);
		groupMember.setJoinType(SwoGroupMember.JOINTYPE_REQUEST);

		CourseDetail courseDetail = seraMgr.getCourseDetailById(groupId);
		
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
		
		swoMgr.setGroup(userId, group, IManager.LEVEL_ALL);
		
		if (isNoticeToGroupLeader)
			SmartUtil.increaseNoticeCountByNoticeType(group.getGroupLeader(), Notice.TYPE_NOTIFICATION);
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
		
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		
		SwoGroup group = swoMgr.getGroup("", groupId, IManager.LEVEL_ALL);
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
		swoMgr.setGroup("", group, IManager.LEVEL_ALL);
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

		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		
		SwoGroup group = swoMgr.getGroup(userId, groupId, IManager.LEVEL_ALL);
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
		swoMgr.setGroup("", group, IManager.LEVEL_ALL);
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
	
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		
		SwoGroup group = swoMgr.getGroup(userId, groupId, IManager.LEVEL_ALL);
		if (group == null)
			return;
		if (!group.isContainGroupMember(userId))
			return;
		
		SwoGroupMember groupMember = group.getGroupMember(userId);
		group.removeGroupMember(groupMember);

		seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, 1, false);

		swoMgr.setGroup("", group, IManager.LEVEL_ALL);
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

		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		
		SwoGroup group = swoMgr.getGroup(userId, groupId, IManager.LEVEL_ALL);
		if (group == null)
			return;
		if (!group.isContainGroupMember(userId))
			return;
		
		SwoGroupMember groupMember = group.getGroupMember(userId);
		group.removeGroupMember(groupMember);

		seraService.scoreCoursePointByType(groupId, Course.TYPE_COURSEPOINT_MEMBER, 1, false);

		swoMgr.setGroup("", group, IManager.LEVEL_ALL);

	}

}
