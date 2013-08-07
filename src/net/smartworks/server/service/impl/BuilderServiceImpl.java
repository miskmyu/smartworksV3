package net.smartworks.server.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.User;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.security.EditPolicy;
import net.smartworks.model.security.WritePolicy;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.server.engine.authority.manager.ISwaManager;
import net.smartworks.server.engine.authority.model.SwaResource;
import net.smartworks.server.engine.authority.model.SwaResourceCond;
import net.smartworks.server.engine.authority.model.SwaUser;
import net.smartworks.server.engine.authority.model.SwaUserCond;
import net.smartworks.server.engine.authority.model.SwaAuthProxy;
import net.smartworks.server.engine.category.manager.ICtgManager;
import net.smartworks.server.engine.category.model.CtgCategory;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdField;
import net.smartworks.server.engine.infowork.form.manager.ISwfManager;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfFormCond;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoDepartment;
import net.smartworks.server.engine.organization.model.SwoDepartmentCond;
import net.smartworks.server.engine.pkg.manager.IPkgManager;
import net.smartworks.server.engine.pkg.model.PkgPackage;
import net.smartworks.server.engine.pkg.model.PkgPackageCond;
import net.smartworks.server.engine.process.process.manager.IPrcManager;
import net.smartworks.server.engine.process.process.model.PrcProcessInst;
import net.smartworks.server.engine.process.process.model.PrcSwProcess;
import net.smartworks.server.engine.process.process.model.PrcSwProcessCond;
import net.smartworks.server.engine.resource.manager.IResourceDesigntimeManager;
import net.smartworks.server.engine.resource.model.IPackageModel;
import net.smartworks.server.engine.resource.model.IProcessModel;
import net.smartworks.server.service.IBuilderService;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

import org.springframework.stereotype.Service;

@Service
public class BuilderServiceImpl implements IBuilderService {

	private static IPkgManager getPkgManager() {
		return SwManagerFactory.getInstance().getPkgManager();
	}
	private static ISwfManager getSwfManager() {
		return SwManagerFactory.getInstance().getSwfManager();
	}
	private static IResourceDesigntimeManager getDesigntimeManager() {
		return SwManagerFactory.getInstance().getDesigntimeManager();
	}
	private static ISwaManager getSwaManager() {
		return SwManagerFactory.getInstance().getSwaManager();
	}
	private static ICtgManager getCtgManager() {
		return SwManagerFactory.getInstance().getCtgManager();
	}
	private static IPrcManager getPrcManager() {
		return SwManagerFactory.getInstance().getPrcManager();
	}
	private static ISwoManager getSwoManager() {
		return SwManagerFactory.getInstance().getSwoManager();
	}
	private static ISwdManager getSwdManager() {
		return SwManagerFactory.getInstance().getSwdManager();
	}

	@Override
	public void startWorkService(String workId) throws Exception {

		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String compId = null;
			if (cuser != null) {
				userId = cuser.getId();
				compId = cuser.getCompanyId();
			}	
			String packageId = workId;
			
			PkgPackageCond pkgCond = new PkgPackageCond();
			pkgCond.setPackageId(packageId);
			PkgPackage pkg = getPkgManager().getPackage(userId, pkgCond, IManager.LEVEL_LITE);
			if (pkg == null)
				throw new Exception("Not Exist Package(Work) - workId : " + workId);
			
			String type = pkg.getType();
			
			//default 권한
			String receId = null;
			LocalDate date = new LocalDate();
			if (packageId != null) {
				if (type.equals("SINGLE")){
					SwfFormCond swfCond = new SwfFormCond();
					swfCond.setPackageId(packageId);
					SwfForm[] swfObj = getSwfManager().getForms(userId, swfCond, "all");
					if (swfObj != null) {
					receId = swfObj[0].getId();
					}
				} else {
					IProcessModel prcModel = getDesigntimeManager().retrieveProcessByPackage(userId, packageId, 1);
					if (prcModel != null) {
					receId = prcModel.getProcessId();
					}
				}
				if (receId != null) {
					SwaResourceCond swaCond = new SwaResourceCond();
					swaCond.setResourceId(receId);
					SwaResource[] swaObj = getSwaManager().getResources(userId, swaCond, "all");
					if (swaObj == null){
						SwaResource swaobjs = new SwaResource();
						//R
						swaobjs.setResourceId(receId);
						swaobjs.setType(0);
						swaobjs.setMode("R");
						swaobjs.setPermission("PUB_ALL");
						swaobjs.setCreationUser(userId);
						swaobjs.setModificationUser(userId);
						swaobjs.setCompanyId(compId);
						swaobjs.setCreationDate(date);
						swaobjs.setModificationDate(date);
						getSwaManager().setResource(userId, swaobjs, null);
						//W
						SwaResource swaobjs1 = new SwaResource();
						swaobjs1.setResourceId(receId);
						swaobjs1.setType(0);
						swaobjs1.setMode("W");
						swaobjs1.setPermission("PUB_ALL");
						swaobjs1.setCreationUser(userId);
						swaobjs1.setModificationUser(userId);
						swaobjs1.setCompanyId(compId);
						swaobjs1.setCreationDate(date);
						swaobjs1.setModificationDate(date);
						getSwaManager().setResource(userId, swaobjs1, null);
						//M
						SwaResource swaobjs2 = new SwaResource();
						swaobjs2.setResourceId(receId);
						swaobjs2.setType(0);
						swaobjs2.setMode("M");
						swaobjs2.setPermission("PUB_NO");
						swaobjs2.setCreationUser(userId);
						swaobjs2.setModificationUser(userId);
						swaobjs2.setCompanyId(compId);
						swaobjs2.setCreationDate(date);
						swaobjs2.setModificationDate(date);
						getSwaManager().setResource(userId, swaobjs2, null);
						//D
						SwaResource swaobjs3 = new SwaResource();
						swaobjs3.setResourceId(receId);
						swaobjs3.setType(0);
						swaobjs3.setMode("D");
						swaobjs3.setPermission("PUB_NO");
						swaobjs3.setCreationUser(userId);
						swaobjs3.setModificationUser(userId);
						swaobjs3.setCompanyId(compId);
						swaobjs3.setCreationDate(date);
						swaobjs3.setModificationDate(date);
						getSwaManager().setResource(userId, swaobjs3, null);
					}
				}
			}
			getDesigntimeManager().deployPackage(userId, compId, packageId, 1);

		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}		
	}

	@Override
	public void stopWorkService(String workId) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			getDesigntimeManager().undeployPackage(userId, workId, 1);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}		
	}

	@Override
	public void startWorkEditing(String workId) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			getDesigntimeManager().checkOutPackage(userId, workId, 1);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}		
	}

	@Override
	public void stopWorkEditing(String workId) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			getDesigntimeManager().checkInPackage(userId, workId, 1);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}		
	}

	public void setSwaUsers(List<Map<String, String>> maps, String resourceId, String mode) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String companyId = user.getCompanyId();

			for(int i=0; i<maps.size(); i++) {
				Map<String, String> userMap = maps.get(i);
				String id = userMap.get("id");
				SwaUser swaUser = new SwaUser();
				swaUser.setResourceId(resourceId);
				swaUser.setMode(mode);
				swaUser.setUserId(id);
				String type = getSwoManager().getTypeByWorkspaceId(id);

				if(type == null)
					throw new ArrayIndexOutOfBoundsException("non-existent user or department!!");

				type = type.equalsIgnoreCase(Community.COMMUNITY_USER) ? SwaUser.TYPE_USER : type.equalsIgnoreCase(Community.COMMUNITY_DEPARTMENT) ? SwaUser.TYPE_DEPT : SwaUser.TYPE_GROUP;
				swaUser.setType(type);
				swaUser.setCompanyId(companyId);
				getSwaManager().setUser(userId, swaUser, null);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("non-existent user or department!!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	@Override
	public void setWorkSettings(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			String workId = (String)requestBody.get("workId");
			Map<String, Object> frmWorkSettings = (Map<String, Object>)requestBody.get("frmWorkSettings");

			Set<String> keySet = frmWorkSettings.keySet();
			Iterator<String> itr = keySet.iterator();

			String rdoKeyField = null;
			String hdnDisplayField = null;
			List<String> hdnDisplayFields = null;
			String firstField = null;
			String radKeyDuplicable = null;
			String rdoAccessLevel = null;
			String rdoWriteLevel = null;
			String rdoEditLevel = null;

			List<Map<String, String>> txtAccessableUsers = null;
			List<Map<String, String>> txtWritableUsers = null;
			List<Map<String, String>> txtEditableUsers = null;

			boolean isFirst = true;
			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmWorkSettings.get(fieldId);
				if(fieldValue instanceof String) {
					String stringValue = (String)fieldValue;
					if(isFirst){
						firstField = stringValue;
						isFirst = false;
					}
					if(fieldId.equals("rdoKeyField"))
						rdoKeyField = stringValue;
					else if(fieldId.equals("hdnDisplayFields"))
						hdnDisplayField = stringValue;
					else if(fieldId.equals("rdoAccessLevel"))
						rdoAccessLevel = stringValue;
					else if(fieldId.equals("rdoWriteLevel"))
						rdoWriteLevel = stringValue;
					else if(fieldId.equals("rdoEditLevel"))
						rdoEditLevel = stringValue;
					else if(fieldId.equals("radKeyDuplicable"))
						radKeyDuplicable = stringValue;
				} else if(fieldValue instanceof ArrayList) {
					List<String> valueList = (ArrayList<String>)fieldValue;
					if(fieldId.equals("hdnDisplayFields"))
						hdnDisplayFields = valueList;
				} else if(fieldValue instanceof LinkedHashMap) {
					List<Map<String, String>> users = (ArrayList<Map<String,String>>)((LinkedHashMap)fieldValue).get("users");
					if(fieldId.equals("txtAccessableUsers"))
						txtAccessableUsers = users;
					else if(fieldId.equals("txtWritableUsers"))
						txtWritableUsers = users;
					else if(fieldId.equals("txtEditableUsers"))
						txtEditableUsers = users;
				}
			}

			if(rdoKeyField == null && (hdnDisplayField != null || hdnDisplayFields != null)){
				rdoKeyField = firstField;
			}
			String resourceId = null;
			if(rdoKeyField != null) { //정보관리업무
				SwfFormCond swfFormCond = new SwfFormCond();
				swfFormCond.setPackageId(workId);
				SwfForm[] swfForms = getSwfManager().getForms(userId, swfFormCond, IManager.LEVEL_LITE);
				if(!CommonUtil.isEmpty(swfForms))
					resourceId = swfForms[0].getId();

				SwdDomainCond swdDomainCond = new SwdDomainCond();
				swdDomainCond.setFormId(resourceId);
				swdDomainCond.setCompanyId(companyId);
				SwdDomain swdDomain = getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_ALL);
				SwdField[] swdFields = null;
				if(swdDomain != null) {
					swdDomain.setKeyColumn(rdoKeyField);
					swdDomain.setTitleFieldId(rdoKeyField);
					swdFields = swdDomain.getFields();
				}

				if(!CommonUtil.isEmpty(swdFields)) {
					for(SwdField swdField : swdFields) {
						swdField.setDisplayOrder(-1);
					}
					int displayOrder = 0;
					if(!CommonUtil.isEmpty(hdnDisplayField)) {
						for(SwdField swdField : swdFields) {
							if(hdnDisplayField.equals(swdField.getFormFieldId())) {
								swdField.setDisplayOrder(displayOrder);
							}
						}
					} else if(!CommonUtil.isEmpty(hdnDisplayFields.size())) {
						for(String displayField : hdnDisplayFields) {
							for(SwdField swdField : swdFields) {
								if(displayField.equals(swdField.getFormFieldId())) {
									swdField.setDisplayOrder(displayOrder);
									displayOrder++;
									break;
								}
							}
						}
					}
				}
				swdDomain.setKeyDuplicable(Boolean.parseBoolean(radKeyDuplicable));
				swdDomain.setFields(swdFields);
				getSwdManager().setDomain(userId, swdDomain, IManager.LEVEL_ALL);
			} else { //프로세스업무, 일정계획업무
				PrcSwProcessCond prcSwProcessCond = new PrcSwProcessCond();
				prcSwProcessCond.setPackageId(workId);
				PrcSwProcess[] prcSwProcesses = getPrcManager().getSwProcesses(userId, prcSwProcessCond);
				if(!CommonUtil.isEmpty(prcSwProcesses))
					resourceId = prcSwProcesses[0].getProcessId();
			}

			SwaResource[] swaResources = null;
			if(resourceId != null) {
				SwaResourceCond swaResourceCond = new SwaResourceCond();
				swaResourceCond.setResourceId(resourceId);
				swaResources = getSwaManager().getResources(userId, swaResourceCond, IManager.LEVEL_LITE);
			}
			if(!CommonUtil.isEmpty(swaResources)) {
				for(SwaResource swaResource : swaResources) {
					String objId = swaResource.getObjId();
					String mode = swaResource.getMode();
					if(mode.equalsIgnoreCase(SwaResource.MODE_DELETE)) {
						getSwaManager().removeResource(userId, objId);
					} else {
						if(mode.equalsIgnoreCase(SwaResource.MODE_READ)) {
							swaResource.setPermission(rdoAccessLevel.equals(Integer.toString(AccessPolicy.LEVEL_PUBLIC)) ? SwaResource.PERMISSION_ALL : rdoAccessLevel.equals(Integer.toString(AccessPolicy.LEVEL_CUSTOM)) ? SwaResource.PERMISSION_SELECT : SwaResource.PERMISSION_NO);
						} else if(mode.equalsIgnoreCase(SwaResource.MODE_WRITE)) {
							swaResource.setPermission(rdoWriteLevel.equals(Integer.toString(WritePolicy.LEVEL_PUBLIC)) ? SwaResource.PERMISSION_ALL : SwaResource.PERMISSION_SELECT);
						} else if(mode.equalsIgnoreCase(SwaResource.MODE_MODIFY)) {
							//start 2012.08.22 프로세스 권한 설정일 경우,수정권한 안하게 설정
							if(rdoKeyField != null){
							swaResource.setPermission(rdoEditLevel.equals(Integer.toString(EditPolicy.LEVEL_PUBLIC)) ? SwaResource.PERMISSION_ALL : rdoEditLevel.equals(Integer.toString(EditPolicy.LEVEL_CUSTOM)) ? SwaResource.PERMISSION_SELECT : SwaResource.PERMISSION_NO);
							}
							//end jybae 
						}
						getSwaManager().setResource(userId, swaResource, null);
					}
				}
			} else {
				String[] modes = new String[]{SwaResource.MODE_READ, SwaResource.MODE_WRITE, SwaResource.MODE_MODIFY};
				for(int i=0; i<modes.length; i++) {
					SwaResource swaResource = new SwaResource();
					swaResource.setCompanyId(companyId);
					swaResource.setResourceId(resourceId);
					swaResource.setType(0);
					String mode = modes[i];
					swaResource.setMode(mode);
					if(mode.equalsIgnoreCase(SwaResource.MODE_READ)) {
						swaResource.setPermission(rdoAccessLevel.equals(Integer.toString(AccessPolicy.LEVEL_PUBLIC)) ? SwaResource.PERMISSION_ALL : rdoAccessLevel.equals(Integer.toString(AccessPolicy.LEVEL_CUSTOM)) ? SwaResource.PERMISSION_SELECT : SwaResource.PERMISSION_NO);
					} else if(mode.equalsIgnoreCase(SwaResource.MODE_WRITE)) {
						swaResource.setPermission(rdoWriteLevel.equals(Integer.toString(WritePolicy.LEVEL_PUBLIC)) ? SwaResource.PERMISSION_ALL : SwaResource.PERMISSION_SELECT);
					} else if(mode.equalsIgnoreCase(SwaResource.MODE_MODIFY)) {
						swaResource.setPermission(rdoEditLevel.equals(Integer.toString(EditPolicy.LEVEL_PUBLIC)) ? SwaResource.PERMISSION_ALL : rdoEditLevel.equals(Integer.toString(EditPolicy.LEVEL_CUSTOM)) ? SwaResource.PERMISSION_SELECT : SwaResource.PERMISSION_NO);
					}
					getSwaManager().setResource(userId, swaResource, null);
				}
			}

			SwaUser[] swaUsers = null;
			if(resourceId != null) {
				SwaUserCond swaUserCond = new SwaUserCond();
				swaUserCond.setResourceId(resourceId);
				swaUsers = getSwaManager().getUsers(userId, swaUserCond, IManager.LEVEL_LITE);
			}
			if(!CommonUtil.isEmpty(swaUsers)) {
				for(SwaUser swaUser : swaUsers) {
					String objId = swaUser.getObjId();
					getSwaManager().removeUser(userId, objId);
				}
			}
			if(!CommonUtil.isEmpty(txtAccessableUsers)) {
				if(rdoAccessLevel.equals(Integer.toString(AccessPolicy.LEVEL_CUSTOM)))
					setSwaUsers(txtAccessableUsers, resourceId, SwaUser.MODE_READ);
			}
			if(!CommonUtil.isEmpty(txtWritableUsers)) {
				if(rdoWriteLevel.equals(Integer.toString(WritePolicy.LEVEL_CUSTOM)))
					setSwaUsers(txtWritableUsers, resourceId, SwaUser.MODE_WRITE);
			}
			if(!CommonUtil.isEmpty(txtEditableUsers)) {
				if(rdoEditLevel.equals(Integer.toString(EditPolicy.LEVEL_CUSTOM)))
					setSwaUsers(txtEditableUsers, resourceId, SwaUser.MODE_MODIFY);
			}
			
			//userProxy
			getSwaManager().removeAllAuthProxyByResourceId(userId, resourceId);
			//resourceId, rdoAcceessLevel, txtAccessableUsers
			SwaAuthProxy authProxy = new SwaAuthProxy();
			authProxy.setAccessLevel(rdoAccessLevel);
			authProxy.setResourceId(resourceId);
			if (rdoAccessLevel.equals(Integer.toString(AccessPolicy.LEVEL_CUSTOM))) {
				StringBuffer accessValueBuff = new StringBuffer();
				for(int i=0; i< txtAccessableUsers.size(); i++) {
					Map<String, String> userMap = txtAccessableUsers.get(i);
					String id = userMap.get("id");
					accessValueBuff.append(id).append(";");
				}
				authProxy.setAccessValue(accessValueBuff.toString());
			}
			getSwaManager().setAuthProxy(userId, authProxy, IManager.LEVEL_ALL);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public void publishWorkToStore(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	@Override
	public void createNewCategory(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		//{frmNewWorkCategroy={txtCategoryName=test, txtCategoryDesc=}}
		try{
			Map<String, Object> frmNewWorkCategory = (Map<String, Object>)requestBody.get("frmNewWorkCategroy");
			
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String compId = null;
			if (cuser != null) {
				userId = cuser.getId();
				compId = cuser.getCompanyId();
			}

			//String parentCategoryId = request.getParameter("parentCategoryId");
			String parentCategoryId = "_PKG_ROOT_";
			String name = (String)frmNewWorkCategory.get("txtCategoryName");
			String desc = (String)frmNewWorkCategory.get("txtCategoryDesc");
		
			CtgCategory ctg = new CtgCategory();
			ctg.setCompanyId(compId);
			ctg.setName(name);
			ctg.setDescription(desc);
			ctg.setParentId(parentCategoryId);
			ctg.setDisplayOrder(1);
			//ICategoryModel category = catMgr.createCategory(userId, parentCategoryId, name, desc);
			CtgCategory category = getCtgManager().createCategory(userId, ctg);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	@Override
	public void setCategory(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		//{categoryId=402880eb359835ed013598367d080001, frmNewWorkCategroy={txtCategoryName=test a, txtCategoryDesc=a}}
		try{
			Map<String, Object> frmNewWorkCategory = (Map<String, Object>)requestBody.get("frmNewWorkCategroy");
			
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String compId = null;
			if (cuser != null) {
				userId = cuser.getId();
				compId = cuser.getCompanyId();
			}

			String categoryId = (String)requestBody.get("categoryId");
			String name = (String)frmNewWorkCategory.get("txtCategoryName");
			String desc = (String)frmNewWorkCategory.get("txtCategoryDesc");

			CtgCategory category = getCtgManager().getCategory(userId, categoryId, IManager.LEVEL_ALL);
			if (category == null)
				return;
			category.setName(name);
			category.setDescription(desc);
			//ICategoryModel category = catMgr.createCategory(userId, parentCategoryId, name, desc);
			getCtgManager().setCategory(userId, category, IManager.LEVEL_ALL);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	@Override
	public void removeCategory(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		//{categoryId=402880eb359835ed013598367d080001}
		try{
			String categoryId = (String)requestBody.get("categoryId");
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String compId = null;
			if (cuser != null) {
				userId = cuser.getId();
				compId = cuser.getCompanyId();
			}
			
			//삭제하기 전에 실행되고 있는 패키지가 속한 카테고리는 삭제 할수 없다
			PkgPackageCond cond = new PkgPackageCond();
			cond.setCompanyId(compId);
			cond.setCategoryId(categoryId);
			long pkgCount = getPkgManager().getPackageSize(userId, cond);
			
			if (pkgCount > 0)
				throw new Exception("Delete Category(" +categoryId+ ") failed - Exist Sub Packages!" );

			getCtgManager().removeCategory(userId, categoryId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	@Override
	public void createNewWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		//{parentId=402880eb3598dd16013598dfb1d40001, frmNewWorkDefinition={txtWorkName=test info, chkWorkType=21, txtaWorkDesc=test info ...}}
		try{
			System.out.println(requestBody);
			
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String compId = null;
			if (cuser != null) {
				userId = cuser.getId();
				compId = cuser.getCompanyId();
			}
			String categoryId = (String)requestBody.get("parentId");
			Map<String, Object> frmNewWorkDefinition = (Map<String, Object>)requestBody.get("frmNewWorkDefinition");
			String name = (String)frmNewWorkDefinition.get("txtWorkName");
			String type = (String)frmNewWorkDefinition.get("chkWorkType");
			String desc = (String)frmNewWorkDefinition.get("txtaWorkDesc");
//			TYPE_CATEGORY = 11;				
//			TYPE_INFORMATION = 21;
//			TYPE_PROCESS = 22;
//			TYPE_SCHEDULE = 23;

			if(Integer.parseInt(type) == WorkCategory.TYPE_CATEGORY) {
				CtgCategory ctg = new CtgCategory();
				ctg.setCompanyId(compId);
				ctg.setName(name);
				ctg.setParentId(categoryId);
				ctg.setDescription(desc);
				ctg.setDisplayOrder(2);
				getCtgManager().createCategory(userId, ctg);
			} else {
				switch (Integer.parseInt(type)) {
	
				case SmartWork.TYPE_INFORMATION :
					type = PrcProcessInst.PROCESSINSTTYPE_INFORMATION;
					break;
				case SmartWork.TYPE_PROCESS :
					type = PrcProcessInst.PROCESSINSTTYPE_PROCESS;
					break;
				case SmartWork.TYPE_SCHEDULE :
					type = PrcProcessInst.PROCESSINSTTYPE_GANTT;
					break;
				}

				if (CommonUtil.isEmpty(type))
					getDesigntimeManager().createPackage(userId, categoryId, name, desc);
				else
					getDesigntimeManager().createPackage(userId, categoryId, type, name, desc);
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}
	private void updateFormName(String packageId, String newName) throws Exception {
		
		SwfFormCond formCond = new SwfFormCond();
		formCond.setPackageId(packageId);
		SwfForm[] forms = getSwfManager().getForms("", formCond, IManager.LEVEL_ALL);
		if (forms == null || forms.length > 1)
			throw new Exception("More Then 1 Forms : packageId = " + packageId);
		SwfForm form = forms[0];
		
		SwManagerFactory.getInstance().getDesigntimeManager().updateFormName("", form.getId(), 1, newName);
		
	}
	
	private void updateProcessName(String packageId, String newName) throws Exception{ 
		
		PrcSwProcessCond prcSwProcessCond = new PrcSwProcessCond();
		prcSwProcessCond.setPackageId(packageId);
		PrcSwProcess[] prcSwProcesses = getPrcManager().getSwProcesses("", prcSwProcessCond);
	
		PrcSwProcess process = prcSwProcesses[0];
		
		SwManagerFactory.getInstance().getDesigntimeManager().updateProcessName("", process.getProcessId(), 1, newName);
		
	}
	@Override
	public void setWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try{
			/*{
				workId=pkg_949b8ab3061b4f5289d9683fd0efeb9c, 
				frmNewWorkDefinition={
											txtWorkName=변경할 정보관리업무123132, 
											selWorkCategoryId=4028802e3812eb6e013812f3ec7f0001, 
											selWorkGroupId=없음, 
											txtaWorkDesc=
									}
			}*/

			String workId = (String)requestBody.get("workId");
			Map<String, String> workDefinitionMap = (Map<String, String>)requestBody.get("frmNewWorkDefinition");
			
			if (CommonUtil.isEmpty(workId))
				return;
			
			if (workDefinitionMap == null)
				return;
			
			String workName = workDefinitionMap.get("txtWorkName");
			
			String userId = SmartUtil.getCurrentUser().getId();
			
			PkgPackageCond pkgCond = new PkgPackageCond();
			pkgCond.setPackageId(workId);
			
			PkgPackage pkg = SwManagerFactory.getInstance().getPkgManager().getPackage(userId, pkgCond, IManager.LEVEL_ALL);
			
			//업무 이름이 변경이 된다면 swform 의 formContents xml 안에 값도 변경을 해줘야한다
			//폼을 배치할때 xml 안의 이름을 사용하기 때문에
			if(pkg.getType().equals(PkgPackage.TYPE_SINGLE)){ 
				updateFormName(workId, workName);
			}else{
				updateProcessName(workId,workName);
			}
			
			
			String workCategoryId = workDefinitionMap.get("selWorkCategoryId");
			String workGroupId = workDefinitionMap.get("selWorkGroupId");
			String workDesc = workDefinitionMap.get("txtaWorkDesc");
			
			if (pkg == null)
				return;
		
			pkg.setCategoryId(workCategoryId);
			//업무 그룹아이디가 넘어 온다면 카테고리 아이디가 의미가 없다 업무그룹아이디로 지정을 해놓으면 그 업무그룹이 상위
			//카테고리아이디를 가지고 있기 때문
			//현재 workGroupId 가 "없음" 이런식으로 넘어온다..아직 구현이 안되어 있는듯....
			if (!CommonUtil.isEmpty(workGroupId) && !workGroupId.equalsIgnoreCase("없음"))
				pkg.setCategoryId(workGroupId);
			pkg.setDescription(workDesc);
			pkg.setName(workName);
	
			SwManagerFactory.getInstance().getPkgManager().setPackage(userId, pkg, IManager.LEVEL_ALL);
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	public void removeWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		//pkg_bcec7491878f42f1a235fd2392134029
		String workId = (String)requestBody.get("workId");
		String userId = SmartUtil.getCurrentUser().getId();
		
		SwManagerFactory.getInstance().getDesigntimeManager().deletePackage(userId, workId, 1);
		
	}
	public String copyWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		
		/*{
			workId=pkg_61fbd7b05967490d97695ee0595cb7d6,
			frmMoveWorkDefinition={
						selWorkCategoryId=40288023382b729501382b890d530003, 
						selWorkGroupId=없음, 
						txtToWorkName=asd_복사본, 
						txtaToWorkDesc=
						}
		}*/

		String userId = SmartUtil.getCurrentUser().getId();
		String workId = (String)requestBody.get("workId");
		Map<String, Object> targetDefinition = (Map<String, Object>)requestBody.get("frmMoveWorkDefinition");
		String targetCategoryId = (String)targetDefinition.get("selWorkCategoryId");
		String targetWorkGroupId = (String)targetDefinition.get("selWorkGroupId");
		String targetWorkName = CommonUtil.toNull((String)targetDefinition.get("txtToWorkName"));
		String targetWorkDesc = CommonUtil.toNull((String)targetDefinition.get("txtaToWorkDesc"));

		//업무 그룹아이디가 넘어 온다면 카테고리 아이디가 의미가 없다 업무그룹아이디로 지정을 해놓으면 그 업무그룹이 상위
		//카테고리아이디를 가지고 있기 때문
		//현재 workGroupId 가 "없음" 이런식으로 넘어온다..아직 구현이 안되어 있는듯....
		if (!CommonUtil.isEmpty(targetWorkGroupId) && !targetWorkGroupId.equalsIgnoreCase("없음"))
			targetCategoryId = targetWorkGroupId;

		IPackageModel pkg = SwManagerFactory.getInstance().getDesigntimeManager().clonePackage(userId, targetCategoryId, targetWorkName, targetWorkDesc, workId, 1);
		
		String newWorkId = pkg.getPackageId();
		return newWorkId;
	}
	@Override
	public String moveWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		
		/*{
			workId=pkg_61fbd7b05967490d97695ee0595cb7d6, 
			frmMoveWorkDefinition={
						selWorkCategoryId=52fca4b219fef4f50119ffcd871b0000, 
						selWorkGroupId=없음
						}
		}*/
		
		String userId = SmartUtil.getCurrentUser().getId();
		String workId = (String)requestBody.get("workId");
		Map<String, Object> targetDefinition = (Map<String, Object>)requestBody.get("frmMoveWorkDefinition");
		String targetCategoryId = (String)targetDefinition.get("selWorkCategoryId");
		String targetWorkGroupId = (String)targetDefinition.get("selWorkGroupId");

		//업무 그룹아이디가 넘어 온다면 카테고리 아이디가 의미가 없다 업무그룹아이디로 지정을 해놓으면 그 업무그룹이 상위
		//카테고리아이디를 가지고 있기 때문
		//현재 workGroupId 가 "없음" 이런식으로 넘어온다..아직 구현이 안되어 있는듯....
		if (!CommonUtil.isEmpty(targetWorkGroupId) && !targetWorkGroupId.equalsIgnoreCase("없음"))
			targetCategoryId = targetWorkGroupId;

		SwManagerFactory.getInstance().getRuntimeManager().doMovePackageCategory(userId, workId, targetCategoryId);
		
		return workId;
		
	}
	@Override
	public void downloadAppWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
