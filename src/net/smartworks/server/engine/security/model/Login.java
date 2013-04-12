/*
 * $Id: Login.java,v 1.1 2009/12/16 05:42:52 kmyu Exp $
 * created by    : CHO DAE HYON
 * creation-date : 2007. 1. 6.
 * =========================================================
 * Copyright (c) 2007 Miracom, Inc. All rights reserved.
 */
package net.smartworks.server.engine.security.model;

import java.util.Collection;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.util.LocalDate;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class Login implements UserDetails {

	private static final long serialVersionUID = 109756771178872916L;
	private String id;
	private String name;
	private String nickName;
	private String username;
	private String password;
	private int	userLevel = User.USER_LEVEL_DEFAULT;
	private int role = User.USER_ROLE_MEMBER;
	private String position;
	private String locale;
	private String timeZone;
	private String departmentId;
	private String adjunctDeptIds;
	private String department;
	private String phoneNo;
	private String cellPhoneNo;
	private String company;
	private String companyId;
	private String type;
	private String empNo;
	private String email;
	private boolean useMail;
	private String authId;
	private String bigPictureName;
	private String smallPictureName;
	private boolean useSign;
	private String signPictureName;
	private String mailUserId;
	private String mailServerId;
	private String mailServerName;
	private String mailId;
	private String mailPassword;
	private DepartmentInfo[] departments;
	private LocalDate hireDate;
	private LocalDate birthday;
	private String homePhoneNo;
	private String homeAddress;
	
	private Collection<GrantedAuthority> authorities; // 권한

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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getCellPhoneNo() {
		return cellPhoneNo;
	}
	public void setCellPhoneNo(String cellPhoneNo) {
		this.cellPhoneNo = cellPhoneNo;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isUseMail() {
		return useMail;
	}
	public void setUseMail(boolean useMail) {
		this.useMail = useMail;
	}
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
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
	public boolean isUseSign() {
		return useSign;
	}
	public void setUseSign(boolean useSign) {
		this.useSign = useSign;
	}
	public String getSignPictureName() {
		return signPictureName;
	}
	public void setSignPictureName(String signPictureName) {
		this.signPictureName = signPictureName;
	}
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	public String getMailUserId() {
		return mailUserId;
	}
	public void setMailUserId(String mailUserId) {
		this.mailUserId = mailUserId;
	}
	public String getMailServerId() {
		return mailServerId;
	}
	public void setMailServerId(String mailServerId) {
		this.mailServerId = mailServerId;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
	public String getMailServerName() {
		return mailServerName;
	}
	public void setMailServerName(String mailServerName) {
		this.mailServerName = mailServerName;
	}
	public String getAdjunctDeptIds() {
		return adjunctDeptIds;
	}
	public void setAdjunctDeptIds(String adjunctDeptIds) {
		this.adjunctDeptIds = adjunctDeptIds;
	}
	public DepartmentInfo[] getDepartments() {
		return departments;
	}
	public void setDepartments(DepartmentInfo[] departments) {
		this.departments = departments;
	}
	public LocalDate getHireDate() {
		return hireDate;
	}
	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}
	public LocalDate getBirthday() {
		return birthday;
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	public String getHomePhoneNo() {
		return homePhoneNo;
	}
	public void setHomePhoneNo(String homePhoneNo) {
		this.homePhoneNo = homePhoneNo;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
}