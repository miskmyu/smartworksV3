package net.smartworks.server.engine.common.menuitem.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.smartworks.server.engine.category.model.CtgCategory;
import net.smartworks.server.engine.category.model.CtgCategoryCond;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.menuitem.exception.ItmException;
import net.smartworks.server.engine.common.menuitem.manager.IItmManager;
import net.smartworks.server.engine.common.menuitem.model.CategoryChange;
import net.smartworks.server.engine.common.menuitem.model.FormChange;
import net.smartworks.server.engine.common.menuitem.model.FormChangeCond;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItem;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItemList;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItemListCond;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.pkg.model.PkgPackage;
import net.smartworks.server.engine.pkg.model.PkgPackageCond;
import net.smartworks.server.engine.resource.exception.SmartServerRuntimeException;
import net.smartworks.server.engine.resource.model.IPackageModel;
import net.smartworks.server.engine.resource.util.XmlUtil;

import org.hibernate.Query;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ItmManagerImpl extends AbstractManager implements IItmManager {

	public ItmMenuItemList getMenuItemList(String userId, String objId, String level) throws ItmException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				ItmMenuItemList obj = (ItmMenuItemList)this.get(ItmMenuItemList.class, objId);
				return obj;
			} else {
				ItmMenuItemListCond cond = new ItmMenuItemListCond();
				cond.setObjId(objId);
				return getMenuItemList(userId, cond, level);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}
	public ItmMenuItemList getMenuItemList(String userId, ItmMenuItemListCond cond, String level) throws ItmException {
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		ItmMenuItemList[] menuItemLists = getMenuItemLists(userId, cond, level);
		if (CommonUtil.isEmpty(menuItemLists))
			return null;
		try {
			if (menuItemLists.length != 1)
				throw new ItmException("More than 1 Object");
		} catch (ItmException e) {
			logger.error(e, e);
			throw e;
		}
		return menuItemLists[0];
	}
	public void setMenuItemList(String userId, ItmMenuItemList obj, String level) throws ItmException {
		if (level == null)
			level = LEVEL_ALL;
		try {
			if (level.equals(LEVEL_ALL)) {
				fill(userId, obj);
				set(obj);
			} else {
				StringBuffer buf = new StringBuffer();
				buf.append("update ItmMenuItemList set");
				buf.append("  companyId=:companyId, userId=:userId");
				buf.append(", creationUser=:creationUser, creationDate=:creationDate");
				buf.append(", modificationUser=:modificationUser, modificationDate=:modificationDate");
				buf.append(" where objId=:objId");
				Query query = this.getSession().createQuery(buf.toString());
				query.setString(ItmMenuItemList.A_COMPANYID, obj.getCompanyId());
				query.setString(ItmMenuItemList.A_USERID, obj.getUserId());
				query.setString(ItmMenuItemList.A_CREATIONUSER, obj.getCreationUser());
				query.setTimestamp(ItmMenuItemList.A_CREATIONDATE, obj.getCreationDate());
				query.setString(ItmMenuItemList.A_MODIFICATIONUSER, obj.getModificationUser());
				query.setTimestamp(ItmMenuItemList.A_MODIFICATIONDATE, obj.getModificationDate());
				query.setString(ItmMenuItemList.A_OBJID, obj.getObjId());
				query.executeUpdate();
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}
	public void createMenuItemList(String userId, ItmMenuItemList obj) throws ItmException {
		try {
			fill(userId, obj);
			create(obj);
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}
	public void removeMenuItemList(String userId, String objId) throws ItmException {
		try {
			remove(ItmMenuItemList.class, objId);
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}
	public void removeMenuItemList(String userId, ItmMenuItemListCond cond) throws ItmException {
		ItmMenuItemList obj = getMenuItemList(userId, cond, null);
		if (obj == null)
			return;
		removeMenuItemList(userId, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, ItmMenuItemListCond cond) throws Exception {
		String objId = null;
		String name = null;
		String companyId = null;
		String userId = null;
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;
		ItmMenuItem[] menuItems = null;
		
		if (cond != null) {
			objId = cond.getObjId();
			name = cond.getName();
			companyId = cond.getCompanyId();
			userId = cond.getUserId();
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
			menuItems = cond.getMenuItems();
		}
		buf.append(" from ItmMenuItemList obj");
		if (menuItems != null && menuItems.length != 0) {
			for (int i=0; i<menuItems.length; i++) {
				buf.append(" left join obj.menuItems as menuItem").append(i);
			}
		}
		buf.append(" where obj.id is not null");
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (companyId != null)
				buf.append(" and obj.companyId = :companyId");
			if (userId != null)
				buf.append(" and obj.userId = :userId");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null)
				buf.append(" and obj.creationDate = :creationDate");
			if (modificationUser != null)
				buf.append(" and obj.modificationUser = :modificationUser");
			if (modificationDate != null)
				buf.append(" and obj.modificationDate = :modificationDate");
			if (menuItems != null && menuItems.length != 0) {
				for (int i=0; i<menuItems.length; i++) {
					ItmMenuItem menuItem = menuItems[i];
					int menuSeqNo = menuItem.getMenuSeqNo();
					String groupId = menuItem.getGroupId();
					String imgPath = menuItem.getImgPath();
					String categoryId = menuItem.getCategoryId();
					String packageId = menuItem.getPackageId();
					String packageType = menuItem.getPackageType();
					String formId = menuItem.getFormId();
					String item_name = menuItem.getName();
					if (groupId != null)
						buf.append(" and menuItem").append(i).append(".groupId = :groupId").append(i);
					if (menuSeqNo != -1)
						buf.append(" and menuItem").append(i).append(".menuSeqNo = :menuSeqNo").append(i);
					if (imgPath != null)
						buf.append(" and menuItem").append(i).append(".imgPath = :imgPath").append(i);
					if (item_name != null)
						buf.append(" and menuItem").append(i).append(".name = :item_name").append(i);
					if (categoryId != null)
						buf.append(" and menuItem").append(i).append(".categoryId = :categoryId").append(i);
					if (packageId != null)
						buf.append(" and menuItem").append(i).append(".packageId = :packageId").append(i);
					if (packageType != null)
						buf.append(" and menuItem").append(i).append(".packageType = :packageType").append(i);
					if (formId != null)
						buf.append(" and menuItem").append(i).append(".formId = :formId").append(i);
				}
			}
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (companyId != null)
				query.setString("companyId", companyId);
			if (userId != null)
				query.setString("userId", userId);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
			if (modificationUser != null)
				query.setString("modificationUser", modificationUser);
			if (modificationDate != null)
				query.setTimestamp("modificationDate", modificationDate);
			if (menuItems != null && menuItems.length != 0) {
				for (int i=0; i<menuItems.length; i++) {
					ItmMenuItem menuItem = menuItems[i];
					int menuSeqNo = menuItem.getMenuSeqNo();
					String groupId = menuItem.getGroupId();
					String imgPath = menuItem.getImgPath();
					String categoryId = menuItem.getCategoryId();
					String packageId = menuItem.getPackageId();
					String packageType = menuItem.getPackageType();
					String formId = menuItem.getFormId();
					String item_name = menuItem.getName();
					
					if (groupId != null)
						query.setString("groupId"+i, groupId);
					if (menuSeqNo != -1)
						query.setInteger("menuSeqNo"+i, menuSeqNo);
					if (imgPath != null)
						query.setString("imgPath"+i, imgPath);
					if (categoryId != null)
						query.setString("categoryId"+i, categoryId);
					if (packageId != null)
						query.setString("packageId"+i, packageId);
					if (packageType != null)
						query.setString("packageType"+i, packageType);
					if (formId != null)
						query.setString("formId"+i, formId);
					if (item_name != null)
						query.setString("item_name"+i, item_name);
				}
			}
		}
		return query;
	}
	public long getMenuItemListSize(String userId, ItmMenuItemListCond cond) throws ItmException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf,cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}
	public ItmMenuItemList[] getMenuItemLists(String userId, ItmMenuItemListCond cond, String level) throws ItmException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId, obj.companyId, obj.userId"); 
				buf.append(", obj.creationUser, obj.creationDate");
				buf.append(", obj.modificationUser, obj.modificationDate");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					ItmMenuItemList obj = new ItmMenuItemList();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setCompanyId((String)fields[j++]);
					obj.setUserId((String)fields[j++]);
					obj.setCreationUser(((String)fields[j++]));
					obj.setCreationDate(((Date)fields[j++]));
					obj.setModificationUser(((String)fields[j++]));
					obj.setModificationDate(((Date)fields[j++]));
					objList.add(obj);
				}
				list = objList;
			}
			ItmMenuItemList[] objs = new ItmMenuItemList[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}

	@Override
	public void addMenuItem(String userId, ItmMenuItem obj) throws ItmException {
		try {
			fill(userId, obj);
			create(obj);
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}

	@Override
	public void removeMenuItem(String userId, String packageId) throws ItmException {
		if (CommonUtil.isEmpty(packageId))
			return;
		String buffer = "delete swmenuitem from swmenuitemlist where swmenuitem.objid = swmenuitemlist.objid and swmenuitemlist.userId = '" + userId + "' and swmenuitem.packageId = '" + packageId + "'";
		Query query = this.getSession().createSQLQuery(buffer.toString());
		query.executeUpdate();
	}

	public int getMaxItmSeq(String userId) throws ItmException {
		if (CommonUtil.isEmpty(userId))
			return 0;

		StringBuffer buff = new StringBuffer();
		buff.append("select max(itm.itmSeq) as maxItmSeq");
		buff.append(" from SwMenuItem itm, SwMenuItemList itemlist");
		buff.append(" where itm.objId = itemlist.objId");
		buff.append(" and itemlist.userId = :userId");

		Query query = this.getSession().createSQLQuery(buff.toString());

		query.setString("userId", userId);

		int maxItmSeq = 0;
		if(query.uniqueResult() != null)
			maxItmSeq = (Integer)query.uniqueResult();

		return maxItmSeq;

	}
	@Override
	public FormChange getFormChange(String userId, String objId, String level) throws ItmException {
		try {
			FormChange obj = (FormChange)this.get(FormChange.class, objId);
			return obj;
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}
	@Override
	public void setFormChange(String userId, FormChange obj, String level) throws ItmException {
		try {
			fill(userId, obj);
			set(obj);
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}
	@Override
	public CategoryChange getCategoryChange(String userId, String objId, String level) throws ItmException {
		try {
			CategoryChange obj = (CategoryChange)this.get(CategoryChange.class, objId);
			return obj;
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}
	@Override
	public void setCategoryChange(String userId, CategoryChange obj, String level) throws ItmException {
		try {
			fill(userId, obj);
			set(obj);
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}
	@Override
	public FormChange getFormChange(String userId, FormChangeCond cond, String level) throws ItmException {
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		FormChange[] FormChangeLists = getFormChanges(userId, cond, level);
		if (CommonUtil.isEmpty(FormChangeLists))
			return null;
		try {
			if (FormChangeLists.length != 1) {
				System.out.println("oldFormId : " + cond.getOldFormId());
				throw new ItmException("More than 1 Object");
				
			}
		} catch (ItmException e) {
			logger.error(e, e);
			throw e;
		}
		return FormChangeLists[0];
	}
	
	public FormChange[] getFormChanges(String userId, FormChangeCond cond, String level) throws ItmException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId, obj.oldFormId, obj.newFormId"); 
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					FormChange obj = new FormChange();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setOldFormId((String)fields[j++]);
					obj.setNewFormId((String)fields[j++]);
					objList.add(obj);
				}
				list = objList;
			}
			FormChange[] objs = new FormChange[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new ItmException(e);
		}
	}
	private Query appendQuery(StringBuffer buf, FormChangeCond cond) throws Exception {
		String objId = null;
		String oldFormId = null;
		String newFormId = null;
		
		FormChange[] formChange = null;
		
		if (cond != null) {
			objId = cond.getObjId();
			oldFormId = cond.getOldFormId();
			newFormId = cond.getNewFormId();
		}
		buf.append(" from FormChange obj");
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId =:objId");
			if (oldFormId != null)
				buf.append(" and obj.oldFormId =:oldFormId");
			if (newFormId != null)
				buf.append(" and obj.newFormId =:newFormId");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (oldFormId != null)
				query.setString("oldFormId", oldFormId);
			if (newFormId != null)
				query.setString("newFormId", newFormId);
		}		
		return query;
	}
	
	public void copyAllCategory(String targetCtgId, String parentCtgId) throws Exception {
		
		List packageList = new ArrayList();
		copyAllCategory(targetCtgId, parentCtgId, packageList);
		System.out.println("##################################################################################################");
		System.out.println("END OF COPY CATEGORY AND PACKAGE - total packageCount : " + packageList.size());
		System.out.println("##################################################################################################");
		for (int i = 0; i < packageList.size(); i++) {
			IPackageModel pkg = (IPackageModel)packageList.get(i);
			populateNewMappingFormIdToCopyPackage(pkg);
			System.out.println(" 0.5 초간 딜레이! ");
			Thread.sleep(500);
		}
		
	}
	
	
	public void copyAllCategory(String targetCtgId, String parentCtgId, List packageList) throws Exception {
		
		
		//카테고리를 복사를 한다
		CtgCategory ctg = SwManagerFactory.getInstance().getCtgManager().getCategory("kmyu@maninsoft.co.kr", targetCtgId, null);
		
		if (ctg == null)
			return;
		String oldCtgId = ctg.getObjId();
		
		CtgCategory newCtg = (CtgCategory)ctg.clone();
		
		String newCtgId = CommonUtil.newId();
		
		newCtg.setParentId(parentCtgId);
		newCtg.setObjId(newCtgId);
		newCtg.setName(newCtg.getName());
		
		SwManagerFactory.getInstance().getCtgManager().setCategory("kmyu@maninsoft.co.kr", newCtg, null);
		CategoryChange cc = new CategoryChange();
		cc.setOldCategoryId(oldCtgId);
		cc.setNewCategoryId(newCtgId);
		SwManagerFactory.getInstance().getItmManager().setCategoryChange("kmyu@maninsoft.co.kr", cc, null);
		System.out.println(newCtg.getName() + " 카테고리 생성 (old : " + oldCtgId + " , new : " + newCtgId + ")" );
		
		this.copyAllPackage(oldCtgId, newCtgId, packageList);
		
		CtgCategoryCond subCtgCond = new CtgCategoryCond();
		subCtgCond.setParentId(oldCtgId);
		CtgCategory[] subCtgs = SwManagerFactory.getInstance().getCtgManager().getCategorys("", subCtgCond, null);
		
		if (subCtgs == null || subCtgs.length == 0)
			return;
		for (int i = 0; i < subCtgs.length; i++) {
			CtgCategory subCtg = subCtgs[i];
			copyAllCategory(subCtg.getObjId(), newCtgId, packageList);
		}
	}
	
	
	public void copyAllPackage(String categoryId, String targetCategoryId, List packageList) throws Exception {
		
		PkgPackageCond pkgCond = new PkgPackageCond();
		pkgCond.setCategoryId(categoryId);
		PkgPackage[] pkgs = SwManagerFactory.getInstance().getPkgManager().getPackages("kmyu@maninsoft.co.kr", pkgCond, null);
		if (pkgs == null || pkgs.length == 0)
			return;
		for (int i = 0; i < pkgs.length; i++) {
			PkgPackage pkg = pkgs[i];
			IPackageModel newPkg = SwManagerFactory.getInstance().getDesigntimeManager().clonePackage("kmyu@maninsoft.co.kr", targetCategoryId, pkg.getName() , "설명", pkg.getPackageId(), 1);
			if (!packageList.contains(newPkg))
				packageList.add(newPkg);
			System.out.println(newPkg.getName() + " 패키지 생성 (old : " + pkg.getPackageId() + " , new : " + newPkg.getPackageId() + ")" );

			System.out.println(" 0.5 초간 딜레이! ");
			Thread.sleep(500);
			
		}
	}
	
	public void populateNewMappingFormIdToCopyPackage(IPackageModel pkg) throws Exception {
		
		List formList = pkg.getFormContentList();
		String formCnt = pkg.getContent();
		
		if (formList != null && !formList.isEmpty()) {
			for (Iterator<String> formItr = formList.iterator(); formItr.hasNext();) {
				String formXml = formItr.next();

				if (CommonUtil.isEmpty(formXml))
					return;
				
				Document doc = XmlUtil.parse(formXml, false, "UTF-8");
				Element root = doc.getDocumentElement();
				
				String formId = root.getAttribute("id");
				String version = root.getAttribute("version");
				String formName = root.getAttribute("name");
				String formTitle = root.getAttribute("title");
				String formSystemName = root.getAttribute("systemName");
				
				Node childrenNode = XmlUtil.getXpathNode(root, "./mappingForms");
				if (childrenNode == null)
					return;

				NodeList entityNodeList = XmlUtil.getXpathNodeList(childrenNode, "./mappingForm");
				if (CommonUtil.isEmpty(entityNodeList)) {
					System.out.println("Not Exist mappingForm Form! ");
				} else {
					for(int i = 0 ; i < entityNodeList.getLength() ; i++) {
						Element entity = (Element)entityNodeList.item(i);
						String targetFormId = entity.getAttribute("targetFormId");
						String newTargetFormId = null;

						FormChangeCond formChangeCond = new FormChangeCond();
						formChangeCond.setOldFormId(targetFormId);
						FormChange formChange = SwManagerFactory.getInstance().getItmManager().getFormChange("", formChangeCond, null);
						
						if (CommonUtil.isEmpty(formChange)) {
							FormChangeCond formChangeCond2 = new FormChangeCond();
							formChangeCond2.setNewFormId(targetFormId);
							FormChange formChange2 = SwManagerFactory.getInstance().getItmManager().getFormChange("", formChangeCond2, null);
							if (CommonUtil.isEmpty(formChange2)) {
								System.out.println("########## targetFormId : "+targetFormId+"  ############## FORM UPDATE : FORMID - " + formId + " #############################");
							} else {
								System.out.println("## already change formId : " + targetFormId);
							}
						} else {
							newTargetFormId = formChange.getNewFormId();
							entity.setAttribute("targetFormId", newTargetFormId);
						}
					}
				}
				
				//다른업무 참조
				
				Node childrenNode2 = XmlUtil.getXpathNode(root, "./children");
				NodeList entityNodeList2 = XmlUtil.getXpathNodeList(childrenNode2, "./formEntity");
				
				for(int i = 0 ; i < entityNodeList2.getLength() ; i++) {
					Element entity = (Element)entityNodeList2.item(i);
					String fieldId = entity.getAttribute("id");
					Element format = (Element)XmlUtil.getXpathNode(entity, "./format");

					if (format != null) {
						String formatType = format.getAttribute("type");
						if (formatType.equalsIgnoreCase("refFormField")) {

							Element refForm = (Element)XmlUtil.getXpathNode(format, "./refForm");
							if (refForm != null) {
								String id = refForm.getAttribute("id");
								
								String newTargetFormId = null;

								FormChangeCond formChangeCond = new FormChangeCond();
								formChangeCond.setOldFormId(id);
								FormChange formChange = SwManagerFactory.getInstance().getItmManager().getFormChange("", formChangeCond, null);
								
								if (CommonUtil.isEmpty(formChange)) {
									FormChangeCond formChangeCond2 = new FormChangeCond();
									formChangeCond2.setNewFormId(id);
									FormChange formChange2 = SwManagerFactory.getInstance().getItmManager().getFormChange("", formChangeCond2, null);
									if (CommonUtil.isEmpty(formChange2)) {
										System.out.println("######## id : "+id+"  ################ FAIL FORM UPDATE REF FORMID : FORMID - " + formId + "#############################");
									} else {
										System.out.println("## already change formId : " + id);
									}
								} else {
									newTargetFormId = formChange.getNewFormId();
									refForm.setAttribute("id", newTargetFormId);
								}
							}
						}
					}
				}

				
				//필드별 맵핑


				Node childrenNode3 = XmlUtil.getXpathNode(root, "./children");
				NodeList entityNodeList3 = XmlUtil.getXpathNodeList(childrenNode3, "./formEntity");
				
				for(int i = 0 ; i < entityNodeList3.getLength() ; i++) {
					Element entity = (Element)entityNodeList3.item(i);
					String fieldId = entity.getAttribute("id");
					Element mappings = (Element)XmlUtil.getXpathNode(entity, "./mappings");
					if (mappings == null)
						continue;
					Element pre = (Element)XmlUtil.getXpathNode(mappings, "./pre");
					NodeList preMappingList = XmlUtil.getXpathNodeList(pre, "./mapping");
					Element post = (Element)XmlUtil.getXpathNode(mappings, "./post");
					NodeList potMappingList = XmlUtil.getXpathNodeList(post, "./mapping");

					//가져오기 맵핑
					if (!CommonUtil.isEmpty(preMappingList) && preMappingList.getLength() != 0) {
						for(int j = 0 ; j < preMappingList.getLength() ; j++) {
							Element entity2 = (Element)preMappingList.item(j);
							String mappingFormId = entity2.getAttribute("mappingFormId");

							String newTargetFormId = null;

							FormChangeCond formChangeCond = new FormChangeCond();
							formChangeCond.setOldFormId(mappingFormId);
							FormChange formChange = SwManagerFactory.getInstance().getItmManager().getFormChange("", formChangeCond, null);

							if (CommonUtil.isEmpty(formChange)) {
								FormChangeCond formChangeCond2 = new FormChangeCond();
								formChangeCond2.setNewFormId(mappingFormId);
								FormChange formChange2 = SwManagerFactory.getInstance().getItmManager().getFormChange("", formChangeCond2, null);
								if (CommonUtil.isEmpty(formChange2)) {
									System.out.println("######### mappingFormId : "+mappingFormId+"  ############### FAIL FORM UPDATE : FORMID - " + formId + "#############################");
								} else {
									System.out.println("## already change formId : " + mappingFormId);
								}
							} else {
								newTargetFormId = formChange.getNewFormId();
								entity2.setAttribute("mappingFormId", newTargetFormId);
							}
						}
					}

					
					
					//내보내기 맵핑
					if (!CommonUtil.isEmpty(potMappingList) && potMappingList.getLength() !=0) {
						for(int j = 0 ; j < potMappingList.getLength() ; j++) {
							Element entity2 = (Element)potMappingList.item(j);
							String mappingFormId = entity2.getAttribute("mappingFormId");

							String newTargetFormId = null;

							FormChangeCond formChangeCond = new FormChangeCond();
							formChangeCond.setOldFormId(mappingFormId);
							FormChange formChange = SwManagerFactory.getInstance().getItmManager().getFormChange("", formChangeCond, null);

							if (CommonUtil.isEmpty(formChange)) {
								FormChangeCond formChangeCond2 = new FormChangeCond();
								formChangeCond2.setNewFormId(mappingFormId);
								FormChange formChange2 = SwManagerFactory.getInstance().getItmManager().getFormChange("", formChangeCond2, null);
								if (CommonUtil.isEmpty(formChange2)) {
									System.out.println("######### mappingFormId : "+mappingFormId+"  ############### FAIL FORM UPDATE : FORMID - " + formId + "#############################");
								} else {
									System.out.println("## already change formId : " + mappingFormId);
								}
							} else {
								newTargetFormId = formChange.getNewFormId();
								entity2.setAttribute("mappingFormId", newTargetFormId);
							}
						}
					}
				}	
				updateFormContent(formId, 1, net.smartworks.server.engine.common.util.XmlUtil.toXmlString(doc));
				System.out.println(formId + " 폼(프로세스) mapping Info 업데이트 완료!! ");
			}
		} else if (!CommonUtil.isEmpty(formCnt)) {
			
			Document doc = XmlUtil.parse(formCnt, false, "UTF-8");
			Element root = doc.getDocumentElement();
			
			String formId = root.getAttribute("id");
			String version = root.getAttribute("version");
			String formName = root.getAttribute("name");
			String formTitle = root.getAttribute("title");
			String formSystemName = root.getAttribute("systemName");
			
			Node childrenNode = XmlUtil.getXpathNode(root, "./mappingForms");
			if (childrenNode == null)
				return;

			NodeList entityNodeList = XmlUtil.getXpathNodeList(childrenNode, "./mappingForm");
			if (CommonUtil.isEmpty(entityNodeList))
				return;
			
			for(int i = 0 ; i < entityNodeList.getLength() ; i++) {
				Element entity = (Element)entityNodeList.item(i);
				String targetFormId = entity.getAttribute("targetFormId");
				String newTargetFormId = null;

				FormChangeCond formChangeCond = new FormChangeCond();
				formChangeCond.setOldFormId(targetFormId);
				FormChange formChange = SwManagerFactory.getInstance().getItmManager().getFormChange("", formChangeCond, null);
				
				if (CommonUtil.isEmpty(formChange)) {
					FormChangeCond formChangeCond2 = new FormChangeCond();
					formChangeCond2.setNewFormId(targetFormId);
					FormChange formChange2 = SwManagerFactory.getInstance().getItmManager().getFormChange("", formChangeCond2, null);
					if (CommonUtil.isEmpty(formChange2)) {
						System.out.println("########### targetFormId : "+targetFormId+"  ############# FAIL FORM UPDATE : FORMID - " + formId + "#############################");
					} else {
						System.out.println("## already change formId : " + targetFormId);
					}
				} else {
					newTargetFormId = formChange.getNewFormId();
					entity.setAttribute("targetFormId", newTargetFormId);
				}
			}
			updateFormContent(formId, 1, net.smartworks.server.engine.common.util.XmlUtil.toXmlString(doc));
			System.out.println(formId + " 폼(정보관리) mapping Info 업데이트 완료!! ");
		}
	}

	private void updateFormContent(String formId, int version, String content) throws SmartServerRuntimeException {
		String hql = "update HbFormContent set content = :content where formId = :formId and version = :version";
		Query query = this.getSession().createQuery(hql);
		query.setString("content", content);
		query.setString("formId", formId);
		query.setInteger("version", version);
		query.executeUpdate();
	}
}