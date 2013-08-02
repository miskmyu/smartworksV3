package net.smartworks.server.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.community.User;
import net.smartworks.model.filter.Condition;
import net.smartworks.model.filter.ConditionOperator;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.report.ChartReport;
import net.smartworks.model.report.Data;
import net.smartworks.model.report.Report;
import net.smartworks.model.work.FileCategory;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.InformationWork;
import net.smartworks.model.work.SmartForm;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.model.work.info.FileCategoryInfo;
import net.smartworks.model.work.info.ImageCategoryInfo;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkCategoryInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.model.work.info.WorkInfoList;
import net.smartworks.server.engine.category.manager.ICtgManager;
import net.smartworks.server.engine.category.model.CtgCategory;
import net.smartworks.server.engine.category.model.CtgCategoryCond;
import net.smartworks.server.engine.common.collection.manager.IColManager;
import net.smartworks.server.engine.common.collection.model.ColList;
import net.smartworks.server.engine.common.collection.model.ColListCond;
import net.smartworks.server.engine.common.collection.model.ColObject;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.menuitem.manager.IItmManager;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItem;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItemList;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItemListCond;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.JsonUtil;
import net.smartworks.server.engine.docfile.exception.DocFileException;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.docfile.model.IFileModel;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdField;
import net.smartworks.server.engine.infowork.domain.model.SwdFieldCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.infowork.form.manager.ISwfManager;
import net.smartworks.server.engine.infowork.form.model.SwfField;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfFormCond;
import net.smartworks.server.engine.infowork.form.model.SwfFormFieldDef;
import net.smartworks.server.engine.mail.manager.IMailManager;
import net.smartworks.server.engine.mail.model.MailAccount;
import net.smartworks.server.engine.mail.model.MailAccountCond;
import net.smartworks.server.engine.mail.model.MailServer;
import net.smartworks.server.engine.opinion.manager.IOpinionManager;
import net.smartworks.server.engine.opinion.model.OpinionCond;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.pkg.manager.IPkgManager;
import net.smartworks.server.engine.pkg.model.PkgPackage;
import net.smartworks.server.engine.pkg.model.PkgPackageCond;
import net.smartworks.server.engine.process.task.manager.ITskManager;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.server.engine.process.task.model.TskTaskDef;
import net.smartworks.server.engine.process.task.model.TskTaskDefCond;
import net.smartworks.server.engine.resource.model.IFormModel;
import net.smartworks.server.engine.resource.model.IProcessModel;
import net.smartworks.server.service.ISettingsService;
import net.smartworks.server.service.IWorkService;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartConfUtil;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.claros.commons.mail.models.ConnectionProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkServiceImpl implements IWorkService {

	private static ICtgManager getCtgManager() {
		return SwManagerFactory.getInstance().getCtgManager();
	}
	private static IPkgManager getPkgManager() {
		return SwManagerFactory.getInstance().getPkgManager();
	}
	private static ISwdManager getSwdManager() {
		return SwManagerFactory.getInstance().getSwdManager();
	}
	private static ISwfManager getSwfManager() {
		return SwManagerFactory.getInstance().getSwfManager();
	}
	private static IItmManager getItmManager() {
		return SwManagerFactory.getInstance().getItmManager();
	}
	private static ISwoManager getSwoManager() {
		return SwManagerFactory.getInstance().getSwoManager();
	}
	private static IDocFileManager getDocManager() {
		return SwManagerFactory.getInstance().getDocManager();
	}
	private static ITskManager getTskManager() {
		return SwManagerFactory.getInstance().getTskManager();
	}
	private static IColManager getColManager() {
		return SwManagerFactory.getInstance().getColManager();
	}
	private static IOpinionManager getOpinionManager() {
		return SwManagerFactory.getInstance().getOpinionManager();
	}
	private static IMailManager getMailManager() {
		return SwManagerFactory.getInstance().getMailManager();
	}

	@Autowired
	private ISettingsService settingsService;
	@Autowired
	private AuthenticationManager authenticationManager;

	/*
	 * (non-Javadoc)
	 * @see net.smartworks.server.service.IWorkService#getMyFavoriteWorks(java.lang.String, java.lang.String)
	 * 사용자가 등록한 즐겨 찾기 업무를 리턴한다
	 */
	@Override
	public SmartWorkInfo[] getMyFavoriteWorks() throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			if (user == null)
				return null;
			
			ItmMenuItemListCond itemListCond = new ItmMenuItemListCond();
			itemListCond.setCompanyId(user.getCompanyId());
			itemListCond.setUserId(user.getId());
			ItmMenuItemList itmList = getItmManager().getMenuItemList(user.getId(), itemListCond, IManager.LEVEL_ALL);

			String[] packageIdArray = null;
			ItmMenuItem[] menuItems = null;
			SmartWorkInfo[] workPkgs = null;
			PkgPackage[] pkgs = null;
			if(itmList != null) {
				menuItems = itmList.getMenuItems();
				if(!CommonUtil.isEmpty(menuItems)) {
					int menuItemLength = menuItems.length;
					packageIdArray = new String[menuItemLength];
					for (int i=0; i<menuItemLength; i++) {
						ItmMenuItem item = menuItems[i];
						if(item != null) {
							String packageId = item.getPackageId();
							packageIdArray[i] = packageId;
						}
					}
				}
			}

			if(!CommonUtil.isEmpty(packageIdArray)) {
				PkgPackageCond pkgCond = new PkgPackageCond();
				pkgCond.setCompanyId(user.getCompanyId());
				pkgCond.setStatus("DEPLOYED");
				pkgCond.setPackageIdIns(packageIdArray);
				pkgs = getPkgManager().getPackages(user.getId(), pkgCond, IManager.LEVEL_LITE);
			}

			if(!CommonUtil.isEmpty(pkgs)) {
				workPkgs = ModelConverter.convertPkgPackagesToSmartWorkInfos(pkgs);
			}

			return workPkgs;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	
	
	private void getCategoryIdByCategoryId(String userId, String companyId, String categoryId, List<CtgCategory> ctgList) throws Exception {

		CtgCategoryCond ctgCond = new CtgCategoryCond();
		ctgCond.setCompanyId(companyId);
		ctgCond.setParentId(categoryId);
		ctgCond.setOrders(new Order[]{new Order(CtgCategory.A_NAME, true)});
		CtgCategory[] ctgs = getCtgManager().getCategorys(userId, ctgCond, IManager.LEVEL_LITE);
		if (ctgs == null || ctgs.length == 0)
			return;
		for (int i = 0; i < ctgs.length; i++) {
			CtgCategory ctg = ctgs[i];
			if (ctgList == null) {
				ctgList = new ArrayList<CtgCategory>();
			}
			if (!ctgList.contains(ctg)) {
				ctgList.add(ctg);
			}
			getCategoryIdByCategoryId(userId, companyId, ctg.getObjId(), ctgList);
		}
	}
	
	
	public WorkInfo[] getAllWorkCategoryByCategoryId(String categoryId) throws Exception {

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		String companyId = user.getCompanyId();
		
		List<CtgCategory> ctgList = new ArrayList();
		getCategoryIdByCategoryId(userId, companyId, categoryId, ctgList);
		
		if (ctgList.size() == 0)
			return null;
		
		CtgCategory[] ctgs = new CtgCategory[ctgList.size()];
		ctgList.toArray(ctgs);
		
		WorkInfo[] workCtgs = (WorkCategoryInfo[])ModelConverter.getWorkCategoryInfoArrayByCtgCategoryArray(ctgs);

		return workCtgs;
	}
	/*
	 * 전체 카테고리 조회 (서비스시작 상태인 업무 조회)
	 * @see net.smartworks.server.service.IWorkService#getMyAllWorksByCategoryId(java.lang.String)
	 */
	@Override
	public WorkInfo[] getMyAllWorksByCategoryId(String categoryId) throws Exception {

		try{
			//categoryId 가 null 이라면 root 카테고리 밑의 1 level 의 카테고리를 리턴한다
			//categoryId 가 넘어오면 카테고리안에 속한 2 level 카테고리(group) 와 work(package)를 리턴한다
	
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			CtgCategoryCond ctgCond = new CtgCategoryCond();
			ctgCond.setCompanyId(user.getCompanyId());
			
			if (CommonUtil.isEmpty(categoryId)) {
				//1 level category
				ctgCond.setParentId(CtgCategory.ROOTCTGID);
				String[] objIdNotIns = {"52fca4b219fef4f50119ffcd871b0000", "5e6caf381ed78430011ed887f6f200a7"};
				ctgCond.setObjIdNotIns(objIdNotIns);
				ctgCond.setOrders(new Order[]{new Order(CtgCategory.A_OBJID, "40288afb1b25f00b011b25f3c7950001"), new Order(CtgCategory.A_NAME, true)});
				CtgCategory[] ctgs = getCtgManager().getCategorys(userId, ctgCond, IManager.LEVEL_LITE);
				WorkInfo[] workCtgs = (WorkCategoryInfo[])ModelConverter.getWorkCategoryInfoArrayByCtgCategoryArray(ctgs);
				List<WorkInfo> newWorkCtgList = new ArrayList<WorkInfo>();
				WorkInfo[] newWorkCtgs = null;
				if(!CommonUtil.isEmpty(workCtgs)) {
					for(WorkInfo workCtg : workCtgs) {
						WorkCategoryInfo workCategoryInfo = (WorkCategoryInfo)workCtg;
						boolean isRunning = workCategoryInfo.isRunning();
						if(isRunning)
							newWorkCtgList.add(workCategoryInfo);
					}
					if(newWorkCtgList.size() > 0) {
						newWorkCtgs = new WorkInfo[newWorkCtgList.size()];
						newWorkCtgList.toArray(newWorkCtgs);
					}
				}
				return newWorkCtgs;
			} else {
				ctgCond.setParentId(categoryId);
				ctgCond.setOrders(new Order[]{new Order(CtgCategory.A_NAME, true)});

				PkgPackageCond pkgCond = new PkgPackageCond();
				pkgCond.setCompanyId(user.getCompanyId());
				pkgCond.setCategoryId(categoryId);
				pkgCond.setStatus("DEPLOYED");
				String[] packageIdNotIns = {"pkg_19281471d5c9404392fea653e627da9e", "pkg_24245093482e404fae15a7b48a55f854", "pkg_fbbd1761c3f144d49337dc38119caa28", "pkg_c2156de59c14435bb551c61c1593a442", "pkg_df40ac03a33c41d59586e4b201b433fd", "pkg_394ea78cec37434d922c73f09ab4b24e"};
				pkgCond.setPackageIdNotIns(packageIdNotIns);
				pkgCond.setOrders(new Order[]{new Order(PkgPackage.A_NAME, true)});

				CtgCategory[] ctgs = getCtgManager().getCategorys(userId, ctgCond, IManager.LEVEL_LITE);
				WorkInfo[] workCtgs = (WorkCategoryInfo[])ModelConverter.getWorkCategoryInfoArrayByCtgCategoryArray(ctgs);

				List<WorkInfo> newWorkCtgList = new ArrayList<WorkInfo>();
				WorkInfo[] newWorkCtgs = null;
				if(!CommonUtil.isEmpty(workCtgs)) {
					for(WorkInfo workCtg : workCtgs) {
						WorkCategoryInfo workCategoryInfo = (WorkCategoryInfo)workCtg;
						boolean isRunning = workCategoryInfo.isRunning();
						if(isRunning)
							newWorkCtgList.add(workCategoryInfo);
					}
					if(newWorkCtgList.size() > 0) {
						newWorkCtgs = new WorkInfo[newWorkCtgList.size()];
						newWorkCtgList.toArray(newWorkCtgs);
					}
				}

				PkgPackage[] pkgs = getPkgManager().getPackages(userId, pkgCond, IManager.LEVEL_LITE);

				WorkInfo[] workPkgs = (SmartWorkInfo[])ModelConverter.getSmartWorkInfoArrayByPkgPackageArray(pkgs);

				int workCtgsSize = newWorkCtgs == null? 0 : newWorkCtgs.length;
				int pkgPkgsSize = workPkgs == null? 0 : workPkgs.length;
				
				WorkInfo[] resultWork = new WorkInfo[workCtgsSize + pkgPkgsSize];
				
				//System.arraycopy(workCtgs, 0, resultWork, 0, workCtgsSize);  
				//System.arraycopy(pkgPkgs, 0, resultWork, workCtgsSize, pkgPkgsSize);
				
				List<WorkInfo> workList = new ArrayList<WorkInfo>();
				for (int i = 0; i < workCtgsSize; i++) {
					workList.add(newWorkCtgs[i]);
				}
				for (int i = 0; i < pkgPkgsSize; i++) {
					workList.add(workPkgs[i]);
				}
	
				workList.toArray(resultWork);
	
				return resultWork;
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	/*
	 * 스마트빌더 전체 카테고리 조회
	 * @see net.smartworks.server.service.IWorkService#getAllWorksByCategoryId(java.lang.String)
	 */
	@Override
	public WorkInfo[] getAllWorksByCategoryId(String categoryId) throws Exception {

		try{
			//categoryId 가 null 이라면 root 카테고리 밑의 1 level 의 카테고리를 리턴한다
			//categoryId 가 넘어오면 카테고리안에 속한 2 level 카테고리(group) 와 work(package)를 리턴한다
	
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			CtgCategoryCond ctgCond = new CtgCategoryCond();
			ctgCond.setCompanyId(user.getCompanyId());
			
			if (CommonUtil.isEmpty(categoryId)) {
				//1 level category
				ctgCond.setParentId(CtgCategory.ROOTCTGID);
//				String[] objIdNotIns = {"52fca4b219fef4f50119ffcd871b0000", "40288afb1b25f00b011b25f3c7950001"};
//				ctgCond.setObjIdNotIns(objIdNotIns);
				ctgCond.setOrders(new Order[]{new Order(CtgCategory.A_NAME, true)});
				CtgCategory[] ctgs = getCtgManager().getCategorys(userId, ctgCond, IManager.LEVEL_LITE);
				return (WorkCategoryInfo[])ModelConverter.getWorkCategoryInfoArrayByCtgCategoryArray(ctgs);
			
			} else {
				ctgCond.setParentId(categoryId);
				ctgCond.setOrders(new Order[]{new Order(CtgCategory.A_NAME, true)});
				
				PkgPackageCond pkgCond = new PkgPackageCond();
				pkgCond.setCompanyId(user.getCompanyId());
				pkgCond.setCategoryId(categoryId);
				pkgCond.setOrders(new Order[]{new Order(PkgPackage.A_NAME, true)});
				//pkgCond.setStatus("DEPLOYED");
				String[] packageIdNotIns = {"pkg_19281471d5c9404392fea653e627da9e", "pkg_24245093482e404fae15a7b48a55f854", "pkg_fbbd1761c3f144d49337dc38119caa28", "pkg_c2156de59c14435bb551c61c1593a442", "pkg_df40ac03a33c41d59586e4b201b433fd", "pkg_394ea78cec37434d922c73f09ab4b24e"};
				pkgCond.setPackageIdNotIns(packageIdNotIns);
	
				CtgCategory[] ctgs = getCtgManager().getCategorys(userId, ctgCond, IManager.LEVEL_LITE);
				WorkInfo[] workCtgs = (WorkCategoryInfo[])ModelConverter.getWorkCategoryInfoArrayByCtgCategoryArray(ctgs);
				
				PkgPackage[] pkgs = getPkgManager().getPackages(userId, pkgCond, IManager.LEVEL_LITE);
				WorkInfo[] workPkgs = (SmartWorkInfo[])ModelConverter.getSmartWorkInfoArrayByPkgPackageArray(pkgs);
	
				int workCtgsSize = workCtgs == null? 0 : workCtgs.length;
				int pkgPkgsSize = workPkgs == null? 0 : workPkgs.length;
				
				WorkInfo[] resultWork = new WorkInfo[workCtgsSize + pkgPkgsSize];
				
				//System.arraycopy(workCtgs, 0, resultWork, 0, workCtgsSize);  
				//System.arraycopy(pkgPkgs, 0, resultWork, workCtgsSize, pkgPkgsSize);
				
				List<WorkInfo> workList = new ArrayList<WorkInfo>();
				for (int i = 0; i < workCtgsSize; i++) {
					workList.add(workCtgs[i]);
				}
				for (int i = 0; i < pkgPkgsSize; i++) {
					workList.add(workPkgs[i]);
				}
	
				workList.toArray(resultWork);
	
				return resultWork;
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public ImageCategoryInfo[] getImageCategoriesByType(int displayType, String spaceId) throws Exception {

		try {
			switch (displayType) {
			case FileCategory.DISPLAY_BY_CATEGORY:
				break;
			case FileCategory.DISPLAY_BY_YEAR:
				break;
			case FileCategory.DISPLAY_BY_OWNER:
				break;
			}
			/*
			 * displayType 
			 */
			//return SmartTest.getImageCategoriesByType(displayType, spaceId);
			return ModelConverter.getImageCategoriesByType(displayType, spaceId);
		} catch(Exception e) {
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public FileCategoryInfo[] getFileCategoriesByType(int displayType, String spaceId, String parentId) throws Exception {

		try{
			//return SmartTest.getFileCategoriesByType(displayType, spaceId, parentId);
			return ModelConverter.getFileCategoriesByType(displayType, spaceId, parentId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public SmartWorkInfo[] searchWork(String key, int searchType) throws Exception {

		try{
			if (CommonUtil.isEmpty(key))
				return null;

			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			PkgPackageCond pkgCond = new PkgPackageCond();
			pkgCond.setCompanyId(user.getCompanyId());
			pkgCond.setStatus(PkgPackage.STATUS_DEPLOYED);
			pkgCond.setNameLike(key);
			PkgPackage[] pkgs = getPkgManager().getPackages(userId, pkgCond, IManager.LEVEL_ALL);
			if (pkgs == null)
				return null;
			PkgPackage[] newPkgs = null;
			if(searchType == Work.SEARCH_TYPE_START_WORK) {
				newPkgs = ModelConverter.getMyWritablePackages(pkgs);
			} else {
				newPkgs = pkgs;
			}
			SmartWorkInfo[] workPkgs = (SmartWorkInfo[])ModelConverter.getSmartWorkInfoArrayByPkgPackageArray(newPkgs);

			return workPkgs;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	public String getWorkIdByFormId(String formId) throws Exception {

		try{
			String workId = "";
			User user = SmartUtil.getCurrentUser();
	
			SwfFormCond swfCond = new SwfFormCond();
			swfCond.setCompanyId(user.getCompanyId());
			swfCond.setId(formId);
	
			SwfForm swfForm = getSwfManager().getForms(user.getId(), swfCond, IManager.LEVEL_LITE)[0];
	
			workId = swfForm.getPackageId();
	
			return workId;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	public Work getWorkById(String workId) throws Exception {

		try{
			if (CommonUtil.isEmpty(workId))
				return null;

			if(workId.equals(SmartWork.ID_REPORT_MANAGEMENT)){
				// TO DO 보고서업무 정보 가져오는 코드 구현 필요
				// 개발을 위해 임시로 스마트테스트에서 가져오게 하였음..
				// 구현하고 아래 코드는 삭제바람.
				return SmartTest.getReportWorkInformation();
			}else if(workId.equals(SmartWork.ID_ALL_WORKS)){
				// TO DO 보고서업무 정보 가져오는 코드 구현 필요
				// 개발을 위해 임시로 스마트테스트에서 가져오게 하였음..
				// 구현하고 아래 코드는 삭제바람.
				return SmartTest.getAllSmartWork();				
			}
			User user = SmartUtil.getCurrentUser();
			PkgPackageCond pkgCond = new PkgPackageCond();
			pkgCond.setCompanyId(user.getCompanyId());
			pkgCond.setPackageId(workId);
	
			PkgPackage pkg = getPkgManager().getPackage(user.getId(), pkgCond, IManager.LEVEL_LITE);

			if(pkg != null) {
				if (pkg.getType().equalsIgnoreCase("PROCESS") || pkg.getType().equalsIgnoreCase("GANTT")) {
					return getProcessWorkById(user.getCompanyId(), user.getId(), workId);
				} else {
					return getInfortmationWorkById(user.getCompanyId(), user.getId(), workId);
				}
			} else {
				return null;
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;
			// Exception Handling Required			
		}
	}

	public Work getProcessWorkById(String companyId, String userId, String workId) throws Exception {

		try{
			if (CommonUtil.isEmpty(workId))
				return null;
	
			PkgPackageCond pkgCond = new PkgPackageCond();
			pkgCond.setCompanyId(companyId);
			pkgCond.setPackageId(workId);
	
			PkgPackage pkg = getPkgManager().getPackage(userId, pkgCond, IManager.LEVEL_LITE);
			
			return ModelConverter.getProcessWorkByPkgPackage(userId, null, pkg);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	public Work getInfortmationWorkById(String companyId, String userId, String workId) throws Exception {

		try {
			InformationWork resultwork = new InformationWork();

			SwfFormCond swfCond = new SwfFormCond();
			swfCond.setPackageId(workId);
			SwfForm[] swfForms = getSwfManager().getForms(userId, swfCond, IManager.LEVEL_ALL);
			if (CommonUtil.isEmpty(swfForms))
				return null;
	
			String formId = swfForms[0].getId();
	
			SwfField[] swfFields = swfForms[0].getFields();
	
			List<FormField> resultList = new ArrayList<FormField>();
			SwdField[] swdViewFields = getSwdManager().getViewFieldList(workId, formId);

			if(swdViewFields != null) {
				for(SwdField swdViewField : swdViewFields) {
					for(SwfField swfField : swfFields) {
						String formatType = swfField.getFormat().getType();
						if(swdViewField.getDisplayOrder() > -1 && !formatType.equals("richEditor") && !formatType.equals("imageBox") && !formatType.equals("dataGrid")) {
							if(swdViewField.getFormFieldId().equals(swfField.getId())) {
								FormField formField = new FormField();
								formField.setId(swdViewField.getFormFieldId());
								formField.setName(swdViewField.getFormFieldName());
								formField.setType(formatType);
								formField.setDisplayOrder(swdViewField.getDisplayOrder());
								resultList.add(formField);
							}
						}
					}
				}
	
				FormField[] formFields = new FormField[resultList.size()];
				resultList.toArray(formFields);
	

				resultwork.setDisplayFields(formFields);
			}
	
			//권한설정
			ModelConverter.setPolicyToWork(resultwork, formId);

			PkgPackageCond pkgCond = new PkgPackageCond();
			pkgCond.setCompanyId(companyId);
			pkgCond.setPackageId(workId);

			PkgPackage pkg = getPkgManager().getPackage(userId, pkgCond, IManager.LEVEL_LITE);

			//상세필터
			resultwork.setSearchFilters(ModelConverter.getSearchFilterInfoByPkgPackage(userId, pkg));

			if(pkg != null) {
				String name = pkg.getName();
				String typeStr = pkg.getType();
				int type = typeStr.equals("PROCESS") ? SmartWork.TYPE_PROCESS : typeStr.equals("SINGLE") ? SmartWork.TYPE_INFORMATION : SmartWork.TYPE_SCHEDULE;
				String description = pkg.getDescription();

				resultwork.setId(workId);
				resultwork.setName(name);
				resultwork.setType(type);
				resultwork.setDesc(description);
				resultwork.setCreater(ModelConverter.getUserByUserId(pkg.getCreationUser()));
				resultwork.setCreatedDate(new LocalDate(pkg.getCreationDate().getTime()));
				resultwork.setLastModifier(ModelConverter.getUserByUserId(pkg.getModificationUser()));
				resultwork.setLastModifiedDate(new LocalDate(pkg.getModificationDate().getTime()));

				String packageStatus = pkg.getStatus();
				boolean isRunningPackage = false;
				boolean isEditingPackage = false;
				User editingUser = null;
				LocalDate editingStartDate = null;
				if(packageStatus.equalsIgnoreCase("DEPLOYED")) {
					isRunningPackage = true;
					isEditingPackage = false; 
				} else if(packageStatus.equalsIgnoreCase("CHECKED-OUT")) {
					isRunningPackage = false;
					isEditingPackage = true;
					editingUser = ModelConverter.getUserByUserId(pkg.getModificationUser());
					editingStartDate = new LocalDate(pkg.getModificationDate().getTime());
				} else if(packageStatus.equalsIgnoreCase("CHECKED-IN")) {
					isRunningPackage = false;
					isEditingPackage = false;
				}
				resultwork.setRunning(isRunningPackage);
				resultwork.setEditing(isEditingPackage);
				resultwork.setEditingUser(editingUser);
				resultwork.setEditingStartDate(editingStartDate);

				Map<String, WorkCategory> pkgCtgInfoMap = ModelConverter.getPkgCtgMapByPackage(pkg);

				if(pkgCtgInfoMap != null) {
					resultwork.setMyCategory(pkgCtgInfoMap.get("category"));
					resultwork.setMyGroup(pkgCtgInfoMap.get("group"));
				}
			}

			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setFormId(formId);
			SwdDomain swdDomain = getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE); 

			if(swdDomain != null) {
				resultwork.setKeyField(new FormField(swdDomain.getKeyColumn(), null, null));
				resultwork.setKeyDuplicatable(swdDomain.isKeyDuplicable());
				SwdFieldCond swdFieldCond = new SwdFieldCond();
				swdFieldCond.setDomainObjId(swdDomain.getObjId());
				Order[] order = new Order[1];
				order[0] = new Order();
				order[0].setField("displayOrder");
				order[0].setAsc(true);
				swdFieldCond.setOrders(order);
				SwdField[] swdFields = getSwdManager().getFields("", swdFieldCond, IManager.LEVEL_LITE);

				if(swdFields != null) {
					List<FormField> formFieldList = new ArrayList<FormField>();
					for(SwdField swdField : swdFields) {
						for(SwfField swfField : swfFields) {
							String formatType = swfField.getFormat().getType();
							if(swdField.getFormFieldId().equals(swfField.getId())) {
								FormField formField = new FormField();
								formField.setId(swdField.getFormFieldId());
								formField.setName(swdField.getFormFieldName());
								formField.setType(formatType);
								formField.setDisplayOrder(swdField.getDisplayOrder());
								formFieldList.add(formField);
							}
						}
					}

					FormField[] resultFormFields = new FormField[formFieldList.size()];
					formFieldList.toArray(resultFormFields);

					SmartForm smFrom = ModelConverter.getSmartFormBySwfFrom(null, swfForms[0]);
					if(smFrom != null) {
						smFrom.setFields(resultFormFields);
						resultwork.setForm(smFrom);
					}
				}
			}

			OpinionCond opinionCond = new OpinionCond();
			opinionCond.setRefId(workId);
			opinionCond.setRefType(6);
			long commentCount = getOpinionManager().getOpinionSize(userId, opinionCond);
			resultwork.setCommentCount((int)commentCount);

			resultwork.setHelpUrl(pkg.getHelpUrl());
			resultwork.setManualFileId(pkg.getManualFileName());// Manual File Group Id
			
			List<IFileModel> file = SwManagerFactory.getInstance().getDocManager().findFileGroup(pkg.getManualFileName());
			if (file != null && file.size() != 0) {
				resultwork.setManualFileName(file.get(0).getFileName());
				if (CommonUtil.isEmpty(companyId))
					companyId = SmartUtil.getCurrentUser().getCompanyId();
				resultwork.setManualFilePath(SmartConfUtil.getInstance().getImageServer() +  companyId + "/" + file.get(0).getFileName());
			}
			
			return resultwork;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}	

	@Override
	public SearchFilter getSearchFilterById(String type, String workId, String filterId) throws Exception {

		try{
			if(CommonUtil.toNotNull(filterId).equals(""))
				return null;
			User user = SmartUtil.getCurrentUser();
			if(filterId.equals(SearchFilter.FILTER_ALL_INSTANCES)) return SearchFilter.getAllInstancesFilter();
			else if(filterId.equals(SearchFilter.FILTER_MY_INSTANCES)) return SearchFilter.getMyInstancesFilter(ModelConverter.getUserByUserId(user.getId()));
			else if(filterId.equals(SearchFilter.FILTER_RECENT_INSTANCES)) return SearchFilter.getRecentInstancesFilter();
			else if(filterId.equals(SearchFilter.FILTER_MY_RECENT_INSTANCES)) return SearchFilter.getMyRecentInstancesFilter(ModelConverter.getUserByUserId(user.getId()));
			else if(filterId.equals(SearchFilter.FILTER_MY_RUNNING_INSTANCES)) return SearchFilter.getMyRunningInstancesFilter(ModelConverter.getUserByUserId(user.getId()));
			else if(filterId.equals(SearchFilter.FILTER_RUNNING_INSTANCES)) return SearchFilter.getRunningInstancesFilter();
			else return ModelConverter.getSearchFilterByFilterId(type, workId, filterId);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public List<SwfFormFieldDef> findFormFieldByForm(String formId, boolean deployedCondition) throws Exception {
		try{
			return getSwfManager().findFormFieldByForm(formId, deployedCondition);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	
	@Override
	public String getFormXml(String formId, String workId) throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(user.getCompanyId());
	
			if(!CommonUtil.isEmpty(formId))
				swfFormCond.setId(formId);
			else
				swfFormCond.setPackageId(workId);
	
			SwfForm[] swfForms = getSwfManager().getForms(user.getId(), swfFormCond, IManager.LEVEL_ALL);
			if(swfForms != null) {
				if (swfForms.length == 1) {
					return swfForms[0].getObjString();
				} else {
					//프로세스폼중에 시작 폼을 리턴한다
					Property[] extProps = new Property[] {new Property("diagramId", workId), new Property("startActivity", "true")};
					TskTaskDefCond taskCond = new TskTaskDefCond();
					taskCond.setExtendedProperties(extProps);
					TskTaskDef[] taskDefs = getTskManager().getTaskDefs(user.getId(), taskCond, IManager.LEVEL_ALL);
					if (CommonUtil.isEmpty(taskDefs))
						throw new Exception(new StringBuffer("No start activity. -> packageId :").append(workId).toString());
					TskTaskDef taskDef = taskDefs[0];
					SwfForm processStartForm = getSwfManager().getForm(user.getId(), taskDef.getForm());
					if (processStartForm != null) {
						return processStartForm.getObjString();
					} else {
						return null;
					}
				}
			} else {
				return null;
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	
	@Override
	public SmartForm getFormById(String formId, String workId) throws Exception{
		
		if (CommonUtil.isEmpty(formId))
			return null;
		
		String userId = SmartUtil.getCurrentUser().getId();
		SwfFormCond swfCond = new SwfFormCond();
		swfCond.setId(formId);
		SwfForm[] swfForms = getSwfManager().getForms(userId, swfCond, IManager.LEVEL_ALL);
		if (swfForms == null || swfForms.length == 0)
			return null;
		SmartForm smForm = ModelConverter.getSmartFormBySwfFrom(null, swfForms[0]);
		
		return smForm;
	}
	
	@Override
	public void setMyProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			Map<String, Object> frmMyProfileSetting = (Map<String, Object>)requestBody.get("frmMyProfileSetting");
	
			Set<String> keySet = frmMyProfileSetting.keySet();
			Iterator<String> itr = keySet.iterator();

			List<Map<String, String>> imgMyProfile = null;
			List<Map<String, String>> imgMySignPic = null;
			String groupId = null;
			String txtUserProfileUserId = null;
			String pwUserProfilePW = null;
			String selUserProfileLocale = null;
			String selUserProfileTimeZone = null;
			String txtUserProfileEmail = null;
			String txtUserProfilePhoneNo = null;
			String txtUserProfileCellNo = null;
			String profileFileId = null;
			String profileFileName = null;
			String txtUserProfilePicture = null;
			String txtUserProfilePosition = null;
			String txtUserProfileEmpId = null;
			boolean chkUserProfileUseEmail = false;
			boolean chkUseSignPicture = false;
			String txtUserProfileEmailId = null;
			String txtUserProfileEmailUserName = null;
			String selUserProfileEmailServerName = null;
			String chkUserProfileEmailDeleteFetched = null;
			String pwUserProfileEmailPW = null;
			String txtUserProfileSenderUserTitle = null;
			String emailSignature = null;
			String chkUserProfileEmailUseSign = null;
			String birthYear = null, birthMonth = null, birthDay = null;
			boolean lunarBirthday = false;
			String homePhoneNo = null, homeAddress = null;

			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmMyProfileSetting.get(fieldId);
				if (fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					groupId = (String)valueMap.get("groupId");
					if(!CommonUtil.isEmpty(groupId)) {
						if(fieldId.equals("imgMyProfile"))
							imgMyProfile = (ArrayList<Map<String,String>>)valueMap.get("files");
						else if(fieldId.equals("imgMySignPic"))
							imgMySignPic = (ArrayList<Map<String,String>>)valueMap.get("files");
					}
				} else if(fieldValue instanceof String) {
					String valueString = (String)fieldValue;
					if(fieldId.equals("txtUserProfileUserId"))
						txtUserProfileUserId = valueString;
					else if(fieldId.equals("pwUserProfilePW"))
						pwUserProfilePW = valueString;
						//pwUserProfilePW = DigestUtils.md5Hex(pwUserProfilePW);
					else if(fieldId.equals("selUserProfileLocale"))
						selUserProfileLocale = valueString;
					else if(fieldId.equals("selUserProfileTimeZone"))
						selUserProfileTimeZone = valueString;
					else if(fieldId.equals("txtUserProfileEmail"))
						txtUserProfileEmail = valueString;
					else if(fieldId.equals("txtUserProfilePhoneNo"))
						txtUserProfilePhoneNo = valueString;
					else if(fieldId.equals("txtUserProfileCellNo"))
						txtUserProfileCellNo = valueString;
					else if(fieldId.equals("txtUserProfilePosition"))
						txtUserProfilePosition = valueString;
					else if(fieldId.equals("txtUserProfileEmpId"))
						txtUserProfileEmpId = valueString;
					else if(fieldId.equals("chkUseSignPicture"))
						chkUseSignPicture = true;
					else if(fieldId.equals("chkUserProfileUseEmail"))
						chkUserProfileUseEmail = true;
					else if(fieldId.equals("txtUserProfileEmailId"))
						txtUserProfileEmailId = valueString;
					else if(fieldId.equals("txtUserProfileEmailUserName"))
						txtUserProfileEmailUserName = valueString;
					else if(fieldId.equals("selUserProfileEmailServerName"))
						selUserProfileEmailServerName = valueString;
					else if(fieldId.equals("chkUserProfileEmailDeleteFetched"))
						chkUserProfileEmailDeleteFetched = valueString;
					else if(fieldId.equals("pwUserProfileEmailPW"))
						pwUserProfileEmailPW = valueString;
					else if(fieldId.equals("txtUserProfileSenderUserTitle"))
						txtUserProfileSenderUserTitle = valueString;
					else if(fieldId.equals("emailSignature"))
						emailSignature = valueString;
					else if(fieldId.equals("chkUserProfileEmailUseSign"))
						chkUserProfileEmailUseSign = valueString;
					else if (fieldId.equals("txtUserBirthYear"))
						birthYear = valueString;
					else if (fieldId.equals("txtUserBirthMonth"))
						birthMonth = valueString;
					else if (fieldId.equals("txtUserBirthDay"))
						birthDay = valueString;
					else if (fieldId.equals("selUserLunarBirthday"))
						lunarBirthday = "true".equalsIgnoreCase(valueString);
					else if (fieldId.equals("txtUserHomePhoneNo"))
						homePhoneNo = valueString;
					else if (fieldId.equals("txtUserHomeAddress"))
						homeAddress = valueString;
				}
			}

			SwoUser user = getSwoManager().getUser(txtUserProfileUserId, txtUserProfileUserId, null);
	
			if(!imgMyProfile.isEmpty()) {
				for(int i=0; i < imgMyProfile.subList(0, imgMyProfile.size()).size(); i++) {
					Map<String, String> file = imgMyProfile.get(i);
					profileFileId = file.get("fileId");
					profileFileName = file.get("fileName");
					txtUserProfilePicture = getDocManager().insertProfilesFile(profileFileId, profileFileName, txtUserProfileUserId + "_p");
					user.setPicture(txtUserProfilePicture);
				}
			}
			if(!imgMySignPic.isEmpty()) {
				for(int i=0; i < imgMySignPic.subList(0, imgMySignPic.size()).size(); i++) {
					Map<String, String> file = imgMySignPic.get(i);
					profileFileId = file.get("fileId");
					profileFileName = file.get("fileName");
					txtUserProfilePicture = getDocManager().insertProfilesFile(profileFileId, profileFileName, txtUserProfileUserId + "_s");
					user.setSign(txtUserProfilePicture);
				}
			}

			//pwUserProfilePW = DigestUtils.md5Hex(pwUserProfilePW);
			user.setPassword(pwUserProfilePW);
			user.setLocale(selUserProfileLocale);
			user.setTimeZone(selUserProfileTimeZone);
			user.setEmail(txtUserProfileEmail);
			user.setUseMail(chkUserProfileUseEmail);
			user.setUseSign(chkUseSignPicture);
			user.setExtensionNo(txtUserProfilePhoneNo);
			user.setMobileNo(txtUserProfileCellNo);
			user.setPosition(txtUserProfilePosition);
			user.setEmpNo(txtUserProfileEmpId);
			user.setHomePhoneNo(homePhoneNo);
			user.setHomeAddress(homeAddress);
			if(birthYear!=null && birthYear.length()==4 && birthMonth!=null && birthDay!=null){
				if(birthMonth.length()==1) birthMonth="0"+birthMonth;
				if(birthDay.length()==1) birthDay="0"+birthDay;
				try{
					user.setBirthDay(new Date(LocalDate.convertLocalDateStringToLocalDate1(birthYear+birthMonth+birthDay).getTime()));
				}catch (Exception e){
					user.setBirthDay(null);
				}
			}else{
				user.setBirthDay(null);
			}
			user.setLunarBirthday(lunarBirthday);
			
			try {
				getSwoManager().setUser(txtUserProfileUserId, user, null);
				MailAccountCond mailAccountCond = new MailAccountCond();
				mailAccountCond.setUserId(txtUserProfileUserId);
				mailAccountCond.setMailServerId(selUserProfileEmailServerName);
				MailAccount mailAccount = getMailManager().getMailAccount(txtUserProfileUserId, mailAccountCond, IManager.LEVEL_ALL);
				if(chkUserProfileUseEmail) {
					if(SmartUtil.isBlankObject(mailAccount))
						mailAccount = new MailAccount();
					mailAccount.setUserId(txtUserProfileUserId);
					mailAccount.setMailServerId(selUserProfileEmailServerName);
					MailServer mailServer = getMailManager().getMailServer(txtUserProfileUserId, selUserProfileEmailServerName, IManager.LEVEL_ALL);
					String mailServerName = null;
					if(!SmartUtil.isBlankObject(mailServer))
						mailServerName = mailServer.getName();
					mailAccount.setMailServerName(mailServerName);
					mailAccount.setMailId(txtUserProfileEmailId);
					mailAccount.setMailUserName(txtUserProfileEmailUserName);
					mailAccount.setMailPassword(pwUserProfileEmailPW);
					String mailDeleteFetched = "false";
					if (!CommonUtil.isEmpty(chkUserProfileEmailDeleteFetched) && chkUserProfileEmailDeleteFetched.equalsIgnoreCase("on"))
						mailDeleteFetched = "true";
					mailAccount.setMailDeleteFetched(mailDeleteFetched);
					mailAccount.setMailSignature(emailSignature);
					if (chkUserProfileEmailUseSign != null && chkUserProfileEmailUseSign.equalsIgnoreCase("on")) {
						mailAccount.setUseMailSign(true);
					} else {
						mailAccount.setUseMailSign(false);
					}
					mailAccount.setSenderUserTitle(CommonUtil.toNull(txtUserProfileSenderUserTitle));
					getMailManager().setMailAccount(txtUserProfileUserId, mailAccount, IManager.LEVEL_ALL);
					ConnectionProfile profile = null;
					ConnectionProfile[] profiles = settingsService.getMailConnectionProfiles();
					if(!CommonUtil.isEmpty(profiles)) {
					    profile = profiles[0];	
					}
//					request.getSession().setAttribute("profile", profile);
//					AuthProfile auth = new AuthProfile();
//					auth.setUsername(txtUserProfileEmailId+"@"+mailServerName);
//					auth.setPassword(pwUserProfileEmailPW);
//					request.getSession().setAttribute("auth", auth);
//				    ConnectionMetaHandler handler = null;
//				    handler = MailAuth.authenticate(profile, auth, handler);
//					request.getSession().setAttribute("handler", handler);
				} else {
					if(!SmartUtil.isBlankObject(mailAccount))
						getMailManager().removeMailAccount(txtUserProfileUserId, mailAccount.getObjId());
				}
				UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());
		        Authentication authentication = authenticationManager.authenticate(authRequest);
		        SecurityContext securityContext = new SecurityContextImpl();
		        securityContext.setAuthentication(authentication);
		        SecurityContextHolder.setContext(securityContext);
		        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
		        getSwoManager().getUserExtend(txtUserProfileUserId, txtUserProfileUserId, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}
	@Override
	public SwdRecord getRecord(String workId, String recordId, String taskInstId) throws Exception{
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			SwdRecord swdRecord = null;
			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setPackageId(workId);
			SwfForm[] swfForms = null;
			String formId = null;

			if(recordId != null) {
				swfForms = getSwfManager().getForms("", swfFormCond, IManager.LEVEL_ALL);
				formId = swfForms[0].getId();
				SwdRecordCond swdRecordCond = new SwdRecordCond();
				swdRecordCond.setRecordId(recordId);
				swdRecordCond.setFormId(formId);
				swdRecord = getSwdManager().getRecord(userId, swdRecordCond, IManager.LEVEL_ALL);
				SwdDomainCond domainCond = new SwdDomainCond();
				domainCond.setFormId(formId);
				SwdDomain[] swdDomains = getSwdManager().getDomains(userId, domainCond, IManager.LEVEL_LITE);
				SwdDomain swdDomain = null;
				String tableName = null;
				if(!CommonUtil.isEmpty(swdDomains)) {
					swdDomain = swdDomains[0];
					tableName = swdDomain.getTableName();
				}
				getSwdManager().addHits(tableName, recordId);
			} else if(taskInstId != null) {
				TskTaskCond tskTaskCond = new TskTaskCond();
				tskTaskCond.setObjId(taskInstId);
				TskTask[] tskTasks = getTskManager().getTasks("", tskTaskCond, null);
				formId = tskTasks[0].getForm();
				String tskDocument = tskTasks[0].getDocument();
				swdRecord = (SwdRecord)SwdRecord.toObject(tskDocument);
				swfFormCond.setId(formId);
				if (formId.equalsIgnoreCase("SYSTEMFORM"))
					swfFormCond.setPackageId(null);
				swfForms = getSwfManager().getForms("", swfFormCond, IManager.LEVEL_ALL);
			}
			if (CommonUtil.isEmpty(swdRecord))
				return null;
			
			SwfField[] swfFields = swfForms[0].getFields();

			SwdDataField[] swdDataFields = swdRecord.getDataFields();
			for(SwdDataField swdDataField : swdDataFields) {
				for(SwfField swfField : swfFields) {
					if(swdDataField.getId().equals(swfField.getId())) {
						String formatType = swfField.getFormat().getType();
						String value = swdDataField.getValue();
						String refRecordId = swdDataField.getRefRecordId();
						List<Map<String, String>> resultUsers = null;
						List<Map<String, String>> resultDepartments = null;
						if(formatType.equals(FormField.TYPE_USER)) {
							if(value != null && refRecordId != null) {
								String[] values = value.split(";");
								String[] refRecordIds = refRecordId.split(";");
								resultUsers = new ArrayList<Map<String,String>>();
								if(values.length > 0 && refRecordIds.length > 0) {
									for(int j=0; j<values.length; j++) {
										Map<String, String> map = new LinkedHashMap<String, String>();
										map.put("userId", refRecordIds[j]);
										map.put("longName", values[j]);
										resultUsers.add(map);
									}
								} else {
									Map<String, String> map = new LinkedHashMap<String, String>();
									map.put("userId", refRecordId);
									map.put("longName", value);
									resultUsers.add(map);
								}
							}
							swdDataField.setUsers(resultUsers);
						}else if(formatType.equals(FormField.TYPE_DEPARTMENT)) {
							if(value != null && refRecordId != null) {
								String[] values = value.split(";");
								String[] refRecordIds = refRecordId.split(";");
								resultDepartments = new ArrayList<Map<String,String>>();
								if(values.length > 0 && refRecordIds.length > 0) {
									for(int j=0; j<values.length; j++) {
										Map<String, String> map = new LinkedHashMap<String, String>();
										map.put("comId", refRecordIds[j]);
										map.put("name", values[j]);
										resultDepartments.add(map);
									}
								} else {
									Map<String, String> map = new LinkedHashMap<String, String>();
									map.put("comId", refRecordId);
									map.put("name", value);
									resultDepartments.add(map);
								}
							}
							swdDataField.setDepartments(resultDepartments);
						} else if(formatType.equals(FormField.TYPE_DATE)) {
							if(value != null) {
								try {
									LocalDate localDate = LocalDate.convertGMTStringToLocalDate(value);
									if(localDate != null)
										value = localDate.toLocalDateSimpleString();
								} catch (Exception e) {
								}
							}
						} else if(formatType.equals(FormField.TYPE_TIME)) {
							if(value != null) {
								try {
									LocalDate localDate = LocalDate.convertGMTTimeStringToLocalDate(value);
									if(localDate != null)
										value = localDate.toLocalTimeShortString();
								} catch (Exception e) {
								}
							}
						} else if(formatType.equals(FormField.TYPE_DATETIME)) {
							if(value != null) {
								try {
									LocalDate localDate = LocalDate.convertGMTStringToLocalDate(value);
									if(localDate != null)
										value = localDate.toDateTimeSimpleString();
								} catch (Exception e) {
								}
							}
						} else if(formatType.equals(FormField.TYPE_FILE)) {
							List<IFileModel> iFileModelList = getDocManager().findFileGroup(value);
							if(iFileModelList.size() > 0) {
								for(int i=0; i<iFileModelList.size(); i++) {
									IFileModel fileModel = iFileModelList.get(i);
									if(fileModel.isDeleteAction()) {
										fileModel.setDeleteAction(false);
										getDocManager().updateFile(userId, fileModel);
									}
								}
							}
						}
						swdDataField.setValue(value);
					}
				}
			}
	
			return swdRecord;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	public SwdRecord getTempRecord(String taskInstId) throws Exception {
		
		//taskInstId : tempTaskId
		if (CommonUtil.isEmpty(taskInstId))
			return null;
		
		TskTask tempTask = SwManagerFactory.getInstance().getTskManager().getTask("", taskInstId, IManager.LEVEL_LITE);
		
		if (CommonUtil.isEmpty(tempTask))
			return null;
		
		String requestBodyStr = tempTask.getDocument();
		if (CommonUtil.isEmpty(requestBodyStr))
			return null;
		
		Map<String, Object> requestBody = JsonUtil.getMapByJsonString(requestBodyStr);

		String formId = (String)requestBody.get("formId");
		String refType = tempTask.getRefType();
		SwdField[] swdFields = null;
		if (refType.equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
			SwfForm form = getSwfManager().getForm("", formId);
			SwfField[] formFields = form.getFields();
			List domainFieldList = new ArrayList();
			
			//제목으로 사용할 필드 (필수>단문>첫번째)
			for (SwfField field: formFields) {
				SwdField domainField = new SwdField();
				domainField.setFormFieldId(field.getId());
				domainField.setFormFieldName(field.getName());
				domainField.setFormFieldType(field.getSystemType());
				domainField.setArray(field.isArray());
				domainField.setSystemField(field.isSystem());
				domainFieldList.add(domainField);
			}
			swdFields = new SwdField[domainFieldList.size()];
			domainFieldList.toArray(swdFields);
		} else {
			SwdDomainCond domainCond = new SwdDomainCond();
			domainCond.setFormId(formId);
			SwdDomain domain = getSwdManager().getDomain("", domainCond, null);
			swdFields = domain.getFields();
		}
		SwdRecord record = ModelConverter.getSwdRecordByRequestBody("", swdFields, requestBody, null);
		
		return record;
	}
	@Override
	public SwdRecord getRecord(HttpServletRequest request) throws Exception {

		String workId = request.getParameter("workId");
		String recordId = request.getParameter("recordId");
		String taskInstId = request.getParameter("taskInstId");
		boolean isTempSaved = CommonUtil.toBoolean(request.getParameter("isTempSaved"));
		if (isTempSaved) {
			return getTempRecord(taskInstId);
		} else {
			return getRecord(workId, recordId, taskInstId);
		}
	}

	@Override
	public String setWorkSearchFilter(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();

			int workType = (Integer)requestBody.get("workType");
			String workId = (String)requestBody.get("workId");
			String filterId = (String)requestBody.get("filterId");
			String txtNewFilterName = (String)requestBody.get("txtNewFilterName");
			List<Map<String, String>> frmSearchFilters = (ArrayList<Map<String,String>>)requestBody.get("frmSearchFilters");

			String selFilterLeftOperand = null;
			String selFilterOperator = null;
			String txtFilterStringOperand = null;
			String txtFilterDateOperand = null;
			String txtFilterDateOperandNumber = null;
			String selFilterTimeOperand = null;
			String rightOperand = null;
			String rightOperandType = null;
			String hdnFieldType = null;

			PkgPackageCond pkgPackageCond = new PkgPackageCond();
			pkgPackageCond.setPackageId(workId);
			PkgPackage pkgPackage = getPkgManager().getPackage(userId, pkgPackageCond, IManager.LEVEL_LITE);

			String resourceId = ModelConverter.getResourceIdByPkgPackage(pkgPackage);

			/*SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(companyId);
			swfFormCond.setPackageId(workId);
			String formId = getSwfManager().getForms(userId, swfFormCond, IManager.LEVEL_LITE)[0].getId();*/

			String likType = workType == SmartWork.TYPE_INFORMATION ? "record.cond." + userId : "processinst.cond." + userId;
			String lnkCorr = resourceId;

			ColList colList = null;
			ColObject[] colObjects = null;
			List<ColObject> colObjectsList = new ArrayList<ColObject>();
			ColListCond colListCond = new ColListCond();
			if(!filterId.equals("") && !txtNewFilterName.equals("")) {
				colListCond.setType(likType);
				colListCond.setCorrelation(lnkCorr);
				colList = getColManager().getList(userId, colListCond, IManager.LEVEL_ALL);
				if(colList != null) {
					colObjects = colList.getItems();
					if(!CommonUtil.isEmpty(colObjects)) {
						for(int i=0; i<colObjects.length; i++) {
							ColObject colObject = colObjects[i];
							if(CommonUtil.toNotNull(colObject.getRef()).equals(filterId)) {
								colObjectsList.add(colObject);
							}
						}
					}
					if(colObjectsList.size() > 0) {
						colObjects = new ColObject[colObjectsList.size()];
						colObjectsList.toArray(colObjects);
					}
					if (CommonUtil.isEmpty(colObjects) || colObjects.length != 1)
						return null;
				}
			} else {
				colListCond.setType(likType);
				colListCond.setCorrelation(lnkCorr);
				colList = getColManager().getList(userId, colListCond, IManager.LEVEL_ALL);
				if(colList == null) {
					colList = new ColList();
					colList.setType(likType);
					colList.setCorrelation(lnkCorr);
				}
			}

			List<Filter> filterList = new ArrayList<Filter>();
			Filter[] filters = null;
			ColObject colObject = new ColObject();
			if(frmSearchFilters != null) {
				for(int i=0; i<frmSearchFilters.size(); i++) {
					Filter filter = new Filter();
					Map<String, String> filtersMap = frmSearchFilters.get(i);
					selFilterLeftOperand = (String)filtersMap.get("selFilterLeftOperand");
					selFilterOperator = (String)filtersMap.get("selFilterOperator");
					txtFilterStringOperand = (String)filtersMap.get("txtFilterStringOperand");
					txtFilterDateOperand = (String)filtersMap.get("txtFilterDateOperand");
					txtFilterDateOperandNumber = (String)filtersMap.get("txtFilterDateOperandNumber");
					selFilterTimeOperand = (String)filtersMap.get("selFilterTimeOperand");
					hdnFieldType = (String)filtersMap.get("hdnFieldType");

					if(selFilterOperator.equals(ConditionOperator.RECENT_DAYS.getId())) {
						rightOperand = this.getRecentSomeDays(5);
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.TODAY.getId())) {
						rightOperand = this.getRecentSomeDays(1);
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.THIS_WEEK.getId())) {
						rightOperand = this.getThisWeek();
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.THIS_MONTH.getId())) {
						rightOperand = this.getRecentSomeMonths(1);
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.THIS_QUARTER.getId())) {
						rightOperand = this.getThisQuarter();
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.THIS_HALF_YEAR.getId())) {
						rightOperand = this.getThisHalfYear();
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.THIS_YEAR.getId())) {
						rightOperand = this.getThisYear();
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.RECENT_SOME_DAYS.getId())) {
						rightOperand = this.getRecentSomeDays(Integer.parseInt(txtFilterDateOperandNumber));
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.RECENT_SOME_MONTHS.getId())) {
						rightOperand = this.getRecentSomeMonths(Integer.parseInt(txtFilterDateOperandNumber));
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else {
						if(txtFilterStringOperand != null) {
							rightOperand = txtFilterStringOperand;
						} else if(txtFilterDateOperand != null){
							rightOperand = LocalDate.convertLocalDateStringToLocalDate(txtFilterDateOperand).toGMTSimpleDateString();
						} else if(selFilterTimeOperand != null){
							rightOperand = LocalDate.convertLocalTimeStringToLocalDate(selFilterTimeOperand).toGMTTimeString2();
						}
						if(hdnFieldType != null) {
							if(hdnFieldType.equals(FormField.TYPE_DATETIME) || hdnFieldType.equals(FormField.TYPE_DATE) || hdnFieldType.equals(FormField.TYPE_TIME) 
									|| hdnFieldType.equals(FormField.TYPE_USER) || hdnFieldType.equals(FormField.TYPE_OTHER_WORK)) {
								rightOperandType = hdnFieldType;
							} else {
								rightOperandType = FormField.TYPE_TEXT;
							}
						}
					}
					filter.setLeftOperandType(rightOperandType);
					filter.setLeftOperandValue(selFilterLeftOperand);
					filter.setOperator(selFilterOperator);
					filter.setRightOperandType(rightOperandType);
					filter.setRightOperandValue(rightOperand);
					filterList.add(filter);
				}
				if(filterList != null) {
					filters = new Filter[filterList.size()];
					filterList.toArray(filters);
				}
				SwdRecordCond swdRecordCond = new SwdRecordCond();
				String condId = CommonUtil.newId();
				swdRecordCond.setCondId(condId);
				swdRecordCond.setFilter(filters);
				String condName = txtNewFilterName;
				if(!CommonUtil.toNotNull(condName).equals("")) {
					swdRecordCond.setCondName(condName);
					colObject.setLabel(condName);
				}
				colObject.setRef(condId);
				colObject.setExpression(swdRecordCond.toString());
			}

			if(!CommonUtil.isEmpty(colObjects)) {
				colList.removeItem(colObjects[0]);
				colObjects[0] = colObject;
				colList.addItem(colObjects[0]);
			} else {
				colList.addItem(colObject);
			}

			getColManager().setList(userId, colList, IManager.LEVEL_ALL);

			if(!filterId.equals("")) return filterId;
			else return colObject.getRef();

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void removeWorkSearchFilter(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();

			String workId = (String)requestBody.get("workId");
			String filterId = (String)requestBody.get("filterId");
			int workType = Integer.parseInt((String)requestBody.get("workType"));

			if(CommonUtil.isEmpty(workId) || CommonUtil.isEmpty(filterId))
				return;

			PkgPackageCond pkgPackageCond = new PkgPackageCond();
			pkgPackageCond.setPackageId(workId);
			PkgPackage pkgPackage = getPkgManager().getPackage(userId, pkgPackageCond, IManager.LEVEL_LITE);

			String resourceId = ModelConverter.getResourceIdByPkgPackage(pkgPackage);

			/*SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(companyId);
			swfFormCond.setPackageId(workId);
			String formId = getSwfManager().getForms(userId, swfFormCond, IManager.LEVEL_LITE)[0].getId();*/

			String likType = workType == SmartWork.TYPE_INFORMATION ? "record.cond." + userId : "processinst.cond." + userId;
			String lnkCorr = resourceId;

			ColList colList = null;
			ColObject[] colObjects = null;
			List<ColObject> colObjectsList = new ArrayList<ColObject>();
			ColListCond colListCond = new ColListCond();

			colListCond.setType(likType);
			colListCond.setCorrelation(lnkCorr);
			colList = getColManager().getList(userId, colListCond, IManager.LEVEL_ALL);
			if(colList != null) {
				colObjects = colList.getItems();
				if(!CommonUtil.isEmpty(colObjects)) {
					for(int i=0; i<colObjects.length; i++) {
						ColObject colObject = colObjects[i];
						if(CommonUtil.toNotNull(colObject.getRef()).equals(filterId)) {
							colObjectsList.add(colObject);
						}
					}
				}
				if(colObjectsList.size() > 0) {
					colObjects = new ColObject[colObjectsList.size()];
					colObjectsList.toArray(colObjects);
				}
				if (CommonUtil.isEmpty(colObjects) || colObjects.length != 1)
					return;
			}
			colList.removeItem(colObjects[0]);
			getColManager().setList(userId, colList, IManager.LEVEL_ALL);

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public RequestParams setInstanceListParams(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
			RequestParams requestParams = new RequestParams();
	
			Map<String, Object> frmSearchInstance = (Map<String, Object>)requestBody.get("frmSearchInstance");
			if(frmSearchInstance != null) {
				String txtSearchInstance = (String)frmSearchInstance.get("txtSearchInstance");
				requestParams.setSearchKey(txtSearchInstance);
			}
	
			Map<String, Object> frmIworkFilterName = (Map<String, Object>)requestBody.get("frmIworkFilterName");
			if(frmIworkFilterName != null){
				String selFilterName = (String)frmIworkFilterName.get("selFilterName");
				requestParams.setFilterId(selFilterName);
			}

			Map<String, Object> frmPworkFilterName = (Map<String, Object>)requestBody.get("frmPworkFilterName");
			if(frmPworkFilterName != null){
				String selFilterName = (String)frmPworkFilterName.get("selFilterName");
				requestParams.setFilterId(selFilterName);
			}

			Map<String, Object> frmSortingField = (Map<String, Object>)requestBody.get("frmSortingField");
			if(frmSortingField != null){
				String hdnSortingFieldId = (String)frmSortingField.get("hdnSortingFieldId");
				String hdnSortingIsAscending = (String)frmSortingField.get("hdnSortingIsAscending");
				SortingField sortingField = new SortingField();
				sortingField.setFieldId(hdnSortingFieldId);
				sortingField.setAscending(Boolean.parseBoolean(hdnSortingIsAscending));
				requestParams.setSortingField(sortingField);
			}

			Map<String, Object> frmInstanceListPaging = (Map<String, Object>)requestBody.get("frmInstanceListPaging");
			Map<String, Object> frmWorkHourListPaging = (Map<String, Object>)requestBody.get("frmWorkHourListPaging");
			Map<String, Object> frmCompanyEventListPaging = (Map<String, Object>)requestBody.get("frmCompanyEventListPaging");
			Map<String, Object> frmApprovalLineListPaging = (Map<String, Object>)requestBody.get("frmApprovalLineListPaging");
			Map<String, Object> frmWebServiceListPaging = (Map<String, Object>)requestBody.get("frmWebServiceListPaging");
			Map<String, Object> frmExternalFormListPaging = (Map<String, Object>)requestBody.get("frmExternalFormListPaging");

			Map<String, Object> existListPaging = new LinkedHashMap<String, Object>();

			if(frmInstanceListPaging != null)
				existListPaging = frmInstanceListPaging;
			else if(frmWorkHourListPaging != null)
				existListPaging = frmWorkHourListPaging;
			else if(frmCompanyEventListPaging != null)
				existListPaging = frmCompanyEventListPaging;
			else if(frmApprovalLineListPaging != null)
				existListPaging = frmApprovalLineListPaging;
			else if(frmWebServiceListPaging != null)
				existListPaging = frmWebServiceListPaging;
			else if(frmExternalFormListPaging != null)
				existListPaging = frmExternalFormListPaging;

			String hdnCurrentPage = (String)existListPaging.get("hdnCurrentPage");
			String selPageSize = (String)existListPaging.get("selPageSize");
			boolean hdnNext10 = Boolean.parseBoolean((String)existListPaging.get("hdnNext10"));
			boolean hdnNextEnd = Boolean.parseBoolean((String)existListPaging.get("hdnNextEnd"));
			boolean hdnPrev10 = Boolean.parseBoolean((String)existListPaging.get("hdnPrev10"));
			boolean hdnPrevEnd = Boolean.parseBoolean((String)existListPaging.get("hdnPrevEnd"));
			if(hdnCurrentPage != null)
				requestParams.setCurrentPage(Integer.parseInt(hdnCurrentPage));
			if(selPageSize != null)
				requestParams.setPageSize(Integer.parseInt(selPageSize));
			if(hdnNext10)
				requestParams.setPagingAction(RequestParams.PAGING_ACTION_NEXT10);
			else if(hdnNextEnd)
				requestParams.setPagingAction(RequestParams.PAGING_ACTION_NEXTEND);
			else if(hdnPrev10)
				requestParams.setPagingAction(RequestParams.PAGING_ACTION_PREV10);
			else if(hdnPrevEnd)
				requestParams.setPagingAction(RequestParams.PAGING_ACTION_PREVEND);

			List<Map<String, Object>> frmSearchFilters = (ArrayList<Map<String, Object>>)requestBody.get("frmSearchFilters");

			if(frmSearchFilters != null)
				requestParams.setFilterId(null);

			String selFilterLeftOperand = null;
			String selFilterOperator = null;
			String txtFilterStringOperand = null;
			String txtFilterDateOperand = null;
			String txtFilterDateOperandNumber = null;
			String selFilterTimeOperand = null;
			String rightOperand = null;
			String rightOperandType = null;

			List<Condition> conditionList = new ArrayList<Condition>();

			if(frmSearchFilters != null) {
				for(int i = 0; i <  frmSearchFilters.subList(0, frmSearchFilters.size()).size(); i++) {
					Map<String, Object> filtersMap = frmSearchFilters.get(i);
					Condition condition = new Condition();
					selFilterLeftOperand = (String)filtersMap.get("selFilterLeftOperand");
					selFilterOperator = (String)filtersMap.get("selFilterOperator");
					txtFilterStringOperand = (String)filtersMap.get("txtFilterStringOperand");
					txtFilterDateOperand = (String)filtersMap.get("txtFilterDateOperand");
					txtFilterDateOperandNumber = (String)filtersMap.get("txtFilterDateOperandNumber");
					selFilterTimeOperand = (String)filtersMap.get("selFilterTimeOperand");

					if(selFilterOperator.equals(ConditionOperator.RECENT_DAYS.getId())) {
						rightOperand = this.getRecentSomeDays(5);
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.TODAY.getId())) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
						java.util.Date date = cal.getTime();
						String Today = new SimpleDateFormat("yyyyMMdd").format(date);
						try {
							cal.setTime(formatter.parse(Today));
						} catch (ParseException e) {
							throw new Exception(e);
						}
						rightOperand = new LocalDate(cal.getTime().getTime()).toGMTDateString();
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.THIS_WEEK.getId())) {
						rightOperand = this.getThisWeek();
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.THIS_MONTH.getId())) {
						//rightOperand = this.getRecentSomeMonths(1);
						rightOperand = this.getThisMonth();
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.THIS_QUARTER.getId())) {
						rightOperand = this.getThisQuarter();
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.THIS_HALF_YEAR.getId())) {
						rightOperand = this.getThisHalfYear();
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.THIS_YEAR.getId())) {
						rightOperand = this.getThisYear();
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.RECENT_SOME_DAYS.getId())) {
						rightOperand = this.getRecentSomeDays(Integer.parseInt(txtFilterDateOperandNumber));
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else if(selFilterOperator.equals(ConditionOperator.RECENT_SOME_MONTHS.getId())) {
						rightOperand = this.getRecentSomeMonths(Integer.parseInt(txtFilterDateOperandNumber));
						selFilterOperator = ConditionOperator.GREATER_EQUAL.getId();
						rightOperandType = FormField.TYPE_DATE;
					} else { 
						if(txtFilterStringOperand != null) {
							rightOperand = txtFilterStringOperand;
							String hdnFieldType = (String)filtersMap.get("hdnFieldType");
							if (!CommonUtil.isEmpty(hdnFieldType) && hdnFieldType.equalsIgnoreCase("userField")) {
								rightOperandType = FormField.TYPE_USER;
							} else {
								rightOperandType = FormField.TYPE_TEXT;
							}
							
						} else if(txtFilterDateOperand != null){
							rightOperand = LocalDate.convertLocalDateStringToLocalDate(txtFilterDateOperand).toGMTSimpleDateString();
							rightOperandType = FormField.TYPE_DATE;
						} else if(selFilterTimeOperand != null){
							rightOperand = LocalDate.convertLocalTimeStringToLocalDate(selFilterTimeOperand).toGMTTimeString2();
							rightOperandType = FormField.TYPE_TIME;
						}
					}
					condition.setLeftOperand(new FormField(selFilterLeftOperand, null, rightOperandType));
					condition.setRightOperand(rightOperand);
					condition.setOperator(selFilterOperator);
					conditionList.add(condition);
				}

				Condition[] conditions = new Condition[conditionList.size()];
				conditionList.toArray(conditions);
	
				SearchFilter searchFilter = new SearchFilter();
				searchFilter.setConditions(conditions);
				requestParams.setSearchFilter(searchFilter);
			}

			return requestParams;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	@Override
	public void addAFavoriteWork(String workId) throws Exception {

		try{
			User cUser = SmartUtil.getCurrentUser();
			String companyId = cUser.getCompanyId();
			String userId = cUser.getId();
	
			ItmMenuItemListCond menuItemListCond = new ItmMenuItemListCond();
			menuItemListCond.setUserId(userId);
	
			ItmMenuItemList menuItemList = getItmManager().getMenuItemList(userId, menuItemListCond, IManager.LEVEL_LITE);
	
			PkgPackageCond packageCond = new PkgPackageCond();
			packageCond.setPackageId(workId);
			packageCond.setCompanyId(companyId);
			PkgPackage pkg = getPkgManager().getPackage(userId, packageCond, IManager.LEVEL_LITE);
			String groupId = "";
			String categoryId = "";
			String packageType = "";
			String packageName = "";
			if(pkg != null) {
				groupId = pkg.getObjId();
				categoryId = pkg.getCategoryId();
				packageType = pkg.getType();
				packageName = pkg.getName();
			}
	
			SwfFormCond formCond = new SwfFormCond();
			formCond.setCompanyId(companyId);
			formCond.setPackageId(workId);
			SwfForm[] forms = getSwfManager().getForms(userId, formCond, IManager.LEVEL_LITE);
			String formId = "";
			if(forms != null) {
				formId = forms[0].getId();
			}
	
			ItmMenuItemList newMenuItemList = new ItmMenuItemList();
			newMenuItemList.setCompanyId(companyId);
			newMenuItemList.setUserId(userId);
	
			List<ItmMenuItem> itmMenuItemList = new ArrayList<ItmMenuItem>();
			ItmMenuItem menuItem = new ItmMenuItem();
			menuItem.setCompanyId(companyId);
			menuItem.setPackageId(workId);
			menuItem.setName(packageName);
			menuItem.setGroupId(groupId);
			menuItem.setCategoryId(categoryId);
			menuItem.setPackageType(packageType);
			menuItem.setFormId(formId);
			String objId = "";
			int itmSeq = 0;
			if(menuItemList != null) {
				objId = menuItemList.getObjId();
				itmSeq = getItmManager().getMaxItmSeq(userId) + 1;
			}
			menuItem.setObjId(objId);
	
			itmMenuItemList.add(menuItem);
	
			ItmMenuItem[] menuItems = new ItmMenuItem[itmMenuItemList.size()];
			itmMenuItemList.toArray(menuItems);
	
			newMenuItemList.setMenuItems(menuItems);
	
			if(menuItemList == null) {
				getItmManager().createMenuItemList(userId, newMenuItemList);
			} else {
				menuItem.setObjId(objId);
				menuItem.setItmSeq(itmSeq);
				getItmManager().addMenuItem(userId, menuItem);
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	@Override
	public void removeAFavoriteWork(String workId) throws Exception {

		try{
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			getItmManager().removeMenuItem(userId, workId);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public String getRecentSomeDays(int someDays) throws Exception {
		return new LocalDate(new LocalDate().getTime() - LocalDate.ONE_DAY*(someDays-1)).toGMTSimpleDateString();
	}

	public String getThisMonth() throws Exception {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		java.util.Date date = cal.getTime();
		String today = new SimpleDateFormat("yyyyMMdd").format(date);
		today = today.substring(0, 6) + "01" ;
		try {
			cal.setTime(formatter.parse(today));
		} catch (ParseException e) {
			throw new Exception(e);
		}
		return new LocalDate(cal.getTime().getTime()).toGMTDateString();
	}
	
	public String getRecentSomeMonths(int someMonths) throws Exception {
		Calendar calendar = Calendar.getInstance();
		int thisMonth = new LocalDate().getMonth();
		calendar.set(Calendar.MONTH, thisMonth - (someMonths-1));
		return new LocalDate(calendar.getTime().getTime()).toGMTSimpleDateString(); 
	}

	public String getThisWeek() throws Exception {
		Calendar calendar = Calendar.getInstance();
		int thisDayOfWeek = new LocalDate().getFirstDayOfWeek();
		calendar.set(Calendar.DAY_OF_WEEK, thisDayOfWeek-1);
		return new LocalDate(calendar.getTime().getTime()).toGMTSimpleDateString(); 	
	}

	public String getThisYear() throws Exception {
		Calendar calendar = Calendar.getInstance();
		int thisYear = new LocalDate().getYear();
		calendar.set(Calendar.YEAR, thisYear);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return new LocalDate(calendar.getTime().getTime()).toGMTSimpleDateString(); 	
	}

	public String getThisQuarter() throws Exception {
		Calendar calendar = Calendar.getInstance();
		int thisMonth = new LocalDate().getMonth();
		int thisQuarter = thisMonth / 3;
		if(thisQuarter == 0)
			thisMonth = 0;
		else if(thisQuarter == 1)
			thisMonth = 3;
		else if(thisQuarter == 2)
			thisMonth = 6;
		else if(thisQuarter == 3)
			thisMonth = 9;

		calendar.set(Calendar.MONTH, thisMonth);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		return new LocalDate(calendar.getTime().getTime()).toGMTSimpleDateString(); 
	}

	public String getThisHalfYear() throws Exception {
		Calendar calendar = Calendar.getInstance();
		int thisMonth = new LocalDate().getMonth();
		int thisHalfYear = thisMonth / 6;
		if(thisHalfYear == 0)
			thisMonth = 0;
		else if(thisHalfYear == 1)
			thisMonth = 6;

		calendar.set(Calendar.MONTH, thisMonth);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		return new LocalDate(calendar.getTime().getTime()).toGMTSimpleDateString(); 

	}
	@Override
	public void setIWorkManual(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			   workId=pkg_f6910726728d44199e253658c7937b4e,
			   frmIWorkManual=   {
			      txtaWorkDesc=업무 섦명,
			      rdoEditor=text,
			      txtaFormDesc=화면설명,
			      txtHelpUrl=http://abc.com,
			      fileManualFile=      {
			         groupId=fg_d510d6efab164a471bab33ca40fdfc540983,
			         files=         [
			            {
			               fileId=temp_5d330d5d35d643f4a2e9ecc089d93656,
			               fileName=Icon8AD2EA30.exe,
			               fileSize=6656,
			               localFilePath=D:KmWorkspaceTomcat7-imagewebappsimageServerSmartFilesManinsoftTempsemp_5d330d5d35d643f4a2e9ecc089d93656.exe
			            }
			         ]
			      }
			   }
			}*/

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		String workId = (String)requestBody.get("workId");
		Map<String, Object> frmIWorkManual = (Map<String, Object>)requestBody.get("frmIWorkManual");
		String txtaWorkDesc = (String)frmIWorkManual.get("txtaWorkDesc");
		String txtaFormDesc = (String)frmIWorkManual.get("txtaFormDesc");
		String txtHelpUrl = (String)frmIWorkManual.get("txtHelpUrl");
		
		//패키지의 설명을 저장한다
		PkgPackageCond pkgCond = new PkgPackageCond();
		pkgCond.setPackageId(workId);
		PkgPackage pkg = SwManagerFactory.getInstance().getPkgManager().getPackage(userId, pkgCond, IManager.LEVEL_ALL);
		if (pkg != null) {
			pkg.setDescription(txtaWorkDesc);
			pkg.setHelpUrl(txtHelpUrl);
			//패키지의 메뉴얼파일을 저장한다
			Map<String, Object> fileManualFile = (Map<String, Object>)frmIWorkManual.get("fileManualFile");
			
			List<Map<String, String>> fielList = (ArrayList<Map<String, String>>)fileManualFile.get("files");
			String fileGroupId = (String)fileManualFile.get("groupId");
			
//			if(!fielList.isEmpty()) {
//				for(int i=0; i < fielList.subList(0, fielList.size()).size(); i++) {
//					Map<String, String> file = fielList.get(i);
//					String fileId = file.get("fileId");
//					String fileName = file.get("fileName");
//					String manualFileName = getDocManager().insertWorkManualFile(workId, fileId, fileName);
//					pkg.setManualFileName(manualFileName);
//				}
//			}
			
			try {
				for(int i=0; i < fielList.subList(0, fielList.size()).size(); i++) {
					Map<String, String> file = fielList.get(i);
					String fileId = file.get("fileId");
					String fileName = file.get("fileName");
					String fileSize = file.get("fileSize");
					getDocManager().insertFiles("Files", workId, fileGroupId, fileId, fileName, fileSize);
					pkg.setManualFileName(fileGroupId);
				}
			} catch (Exception e) {
				throw new DocFileException("file upload fail...");
			}
			
			SwManagerFactory.getInstance().getPkgManager().setPackage(userId, pkg, IManager.LEVEL_ALL);
		}
		SwfFormCond formCond = new SwfFormCond();
		formCond.setPackageId(workId);
		SwfForm[] swForm = SwManagerFactory.getInstance().getSwfManager().getForms(userId, formCond, IManager.LEVEL_LITE);
		if (swForm != null && swForm.length != 0) {

			IFormModel form = SwManagerFactory.getInstance().getRuntimeManager().retrieveForm(userId, swForm[0].getId(), 1);
			if (form != null) {
				form.setDescription(txtaFormDesc);
				SwManagerFactory.getInstance().getDesigntimeManager().updateForm(userId, form);
			}
		}
	}
	@Override
	public void setPWorkManual(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			   workId=pkg_fe5c6d8f2c0f48eb989bc44a0d5cff4a,
			   frmPWorkManual=   {
			      txtaWorkDesc=packageDesc,
			      rdoEditor=text,
			      txtaProcessDesc=prcDesc,
			      rdoEditor0=text,
			      txtaFormDesc2=formDesc1,
			      rdoEditor1=text,
			      txtaFormDesc7=formDesc2,
			      rdoEditor2=text,
			      txtaFormDesc8=,
			      txtHelpUrl=http://webManualUrl.com,
			      fileManualFile=      {
			         groupId=fg_c35e20c6f8694f4065f865cfa1d9ffc32956,
			         files=         [
			            {
			               fileId=temp_4df9d14a86ca458c850dc3562ea14d94,
			               fileName=firefox.exe,
			               fileSize=924632,
			               localFilePath=D:KmWorkspaceTomcat7-imagewebappsimageServerSmartFilesManinsoftTempsemp_4df9d14a86ca458c850dc3562ea14d94.exe
			            }
			         ]
			      }
			   }
			}*/
		
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		String workId = (String)requestBody.get("workId");
		Map<String, Object> frmPWorkManual = (Map<String, Object>)requestBody.get("frmPWorkManual");
		String txtaWorkDesc = (String)frmPWorkManual.get("txtaWorkDesc");
		String txtaProcessDesc = (String)frmPWorkManual.get("txtaProcessDesc");
		String txtHelpUrl = (String)frmPWorkManual.get("txtHelpUrl");
		
		//패키지의 설명을 저장한다
		PkgPackageCond pkgCond = new PkgPackageCond();
		pkgCond.setPackageId(workId);
		PkgPackage pkg = SwManagerFactory.getInstance().getPkgManager().getPackage(userId, pkgCond, IManager.LEVEL_ALL);
		if (pkg != null) {
			pkg.setDescription(txtaWorkDesc);
			pkg.setHelpUrl(txtHelpUrl);
			//패키지의 메뉴얼파일을 저장한다
			Map<String, Object> fileManualFile = (Map<String, Object>)frmPWorkManual.get("fileManualFile");
			
			List<Map<String, String>> fielList = (fileManualFile==null) ? null : (ArrayList<Map<String, String>>)fileManualFile.get("files");
			String fileGroupId = (fileManualFile==null) ? null : (String)fileManualFile.get("groupId");
			
//			if(!fielList.isEmpty()) {
//				for(int i=0; i < fielList.subList(0, fielList.size()).size(); i++) {
//					Map<String, String> file = fielList.get(i);
//					String fileId = file.get("fileId");
//					String fileName = file.get("fileName");
//					String manualFileName = getDocManager().insertWorkManualFile(workId, fileId, fileName);
//					pkg.setManualFileName(manualFileName);
//				}
//			}
			try {
				if(fielList!=null){
					for(int i=0; i < fielList.subList(0, fielList.size()).size(); i++) {
						Map<String, String> file = fielList.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						String fileSize = file.get("fileSize");
						getDocManager().insertFiles("Files", workId, fileGroupId, fileId, fileName, fileSize);
						pkg.setManualFileName(fileGroupId);
					}
				}
			} catch (Exception e) {
				throw new DocFileException("file upload fail...");
			}
			
			SwManagerFactory.getInstance().getPkgManager().setPackage(userId, pkg, IManager.LEVEL_ALL);
		}
		//프로세스의 설명을 저장한다
		IProcessModel prc = SwManagerFactory.getInstance().getRuntimeManager().retrieveProcessByPackage(userId, workId, 1);
		String processId = null;
		if (prc != null) {
			processId = prc.getProcessId();
			prc.setName(pkg.getName());
			prc.setDescription(txtaProcessDesc);
			SwManagerFactory.getInstance().getDesigntimeManager().updateProcess(userId, prc);
		}
		
		TskTaskDefCond tskCond = new TskTaskDefCond();
		tskCond.setExtendedProperties(new Property[]{new Property("processId", processId)});
		TskTaskDef[] tskDefs = SwManagerFactory.getInstance().getTskManager().getTaskDefs(userId, tskCond, IManager.LEVEL_ALL);
		if (tskDefs != null && tskDefs.length != 0) {
			for (int i = 0; i < tskDefs.length; i++) {
				String actId = tskDefs[i].getExtendedPropertyValue("activityId");
				if (CommonUtil.isEmpty(actId))
					continue;

				//태스크의 데피니션의 설명을 저장한다
				String tskDesc = (String)frmPWorkManual.get("txtaFormDesc" + actId);
				if (CommonUtil.isEmpty(tskDesc))
					continue;
				
				tskDefs[i].setDescription(tskDesc);
				SwManagerFactory.getInstance().getTskManager().setTaskDef(userId, tskDefs[i], IManager.LEVEL_ALL);
				
				//태스크들의 폼의 설명을 저장한다]
				IFormModel form = SwManagerFactory.getInstance().getRuntimeManager().retrieveForm(userId, tskDefs[i].getForm(), 1);
				if (form != null) {
					form.setDescription(tskDesc);
					SwManagerFactory.getInstance().getDesigntimeManager().updateForm(userId, form);
				}
			}
		}
	}
	@Override
	public SwdRecord getRecordByKeyValue(String workId, String keyValue) throws Exception {
		//테스트 미실행
		
		
		if (CommonUtil.isEmpty(workId) || CommonUtil.isEmpty(keyValue)) 
			return null;
		
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
			
		//키컬럼을 구한다
		SwfFormCond formCond = new SwfFormCond();
		formCond.setPackageId(workId);
		SwfForm[] form = SwManagerFactory.getInstance().getSwfManager().getForms(userId, formCond, IManager.LEVEL_LITE);
		if (form == null || form.length == 0)
			return null;
		
		String formId = form[0].getId();
		SwdDomainCond domainCond = new SwdDomainCond();
		domainCond.setFormId(formId);
		SwdDomain domain = SwManagerFactory.getInstance().getSwdManager().getDomain(userId, domainCond, IManager.LEVEL_LITE);
		
		if (CommonUtil.isEmpty(domain))
			return null;
		
		String titleFieldId = domain.getTitleFieldId();
		
		String tableColName = getSwdManager().getTableColName(domain.getObjId(), titleFieldId);
		
		SwdRecordCond recordCond = new SwdRecordCond();
		recordCond.setFilter(new Filter[]{new Filter("=", tableColName, keyValue)});
		
		SwdRecord record = SwManagerFactory.getInstance().getSwdManager().getRecord(userId, recordCond, IManager.LEVEL_ALL);
		
		return record;
	}
	@Override
	public WorkInfoList getAppWorkList(RequestParams params) throws Exception {
		//return SmartTest.getAppWorkList(params);
		return null;
	}
}
