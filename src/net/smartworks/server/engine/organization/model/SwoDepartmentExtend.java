/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 1. 9.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.organization.model;

import net.smartworks.model.community.Community;
import net.smartworks.server.engine.common.util.CommonUtil;

public class SwoDepartmentExtend {

	public SwoDepartmentExtend() {
		super();
	}

	public SwoDepartmentExtend(String id, String name, String description, String parentId, String picture) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.parentId = parentId;
		if(!CommonUtil.isEmpty(picture)) {
			String extension = picture.lastIndexOf(".") > 0 ? picture.substring(picture.lastIndexOf(".") + 1) : null;
			String pictureId = picture.substring(0, (picture.length() - extension.length())-1);
			this.bigPictureName = pictureId + Community.IMAGE_TYPE_ORIGINAL + "." + extension;
			this.smallPictureName = pictureId + Community.IMAGE_TYPE_THUMB + "." + extension;
		}
	}

	private String id = null;
	private String name = null;
	private String description = null;
	private String parentId = null;
	private String headId = null;
	private String bigPictureName = null;
	private String smallPictureName = null;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHeadId() {
		return headId;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
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

}