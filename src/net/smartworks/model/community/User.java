package net.smartworks.model.community;

import java.util.TimeZone;

import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.mail.MailAccount;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.LocaleInfo;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class User extends WorkSpace {

	public static final int USER_LEVEL_EXTERNAL_USER = 0;
	public static final int USER_LEVEL_INTERNAL_USER = 1;
	public static final int USER_LEVEL_AMINISTRATOR = 2;
	public static final int USER_LEVEL_SYSMANAGER = 3;
	public static final int USER_LEVEL_DEFAULT = USER_LEVEL_INTERNAL_USER;

	public static final int USER_ROLE_LEADER = 1;
	public static final int USER_ROLE_MEMBER = 2;
	public static final int USER_ROLE_EMAIL = 3;
	public static final String  NO_USER_PICTURE  = "no_user_picture";

	public static final String USER_ID_SYSTEM = "system@smartworks.net";
	public static final String USER_ID_ANONYMOUS = "anonymous@smartworks.net";
	public static final String USER_ID_NONE_EXISTING = "noneexisting@smartworks.net";
	public static final String USER_ID_ADMINISTRATOR = "admin@maninsoft.co.kr";
	public static final String USER_ID_PROCESS = "PROCESS";
	
	public static final String NAMING_NICKNAME_BASE = "NickNameBase";

	private String nickName;
	private int	userLevel = USER_LEVEL_DEFAULT;
	private int role = USER_ROLE_MEMBER;
	private String position;
	private String locale = LocaleInfo.LOCALE_DEFAULT;
	private String timeZone = LocalDate.TIMEZONE_SEOUL; 
	private String departmentId;
	private String department;
	private DepartmentInfo[] departments; 
	private String employeeId;
	private LocalDate hireDate;
	private String password;
	private String phoneNo;
	private String cellPhoneNo;
	private LocalDate birthday;
	private boolean lunarBirthday;
	private String homeAddress;
	private String homePhoneNo;
	private String company;
	private String companyId;
	private boolean useSignPicture;
	private String signPicture;
	private boolean online;
	private boolean useMail;
	private MailAccount[] mailAccounts;
	
	public String getNickName() {
		if(SmartUtil.isBlankObject(nickName)) return getName();
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
	public String getLongName() {
		return (SmartUtil.isBlankObject(position)) ? getNickName() : position + " " + getNickName(); 
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
	public DepartmentInfo[] getDepartments() {
		return departments;
	}
	public void setDepartments(DepartmentInfo[] departments) {
		this.departments = departments;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}	
	public boolean isUseSignPicture() {
		return useSignPicture;
	}
	public void setUseSignPicture(boolean useSignPicture) {
		this.useSignPicture = useSignPicture;
	}
	public String getSignPicture() {
		return super.getPath() + this.signPicture;
	}
	public void setSignPicture(String signPicture) {
		this.signPicture = signPicture;
	}
	public boolean isUseMail() {
		return useMail;
	}
	public void setUseMail(boolean useMail) {
		this.useMail = useMail;
	}
	public MailAccount[] getMailAccounts() {
		return mailAccounts;
	}
	public void setMailAccounts(MailAccount[] mailAccounts) {
		this.mailAccounts = mailAccounts;
	}
	public String getMailId(){
		if(!this.isUseMail() || SmartUtil.isBlankObject(this.mailAccounts)) return null;
		return this.mailAccounts[0].getEmailId();
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
	public boolean isLunarBirthday() {
		return lunarBirthday;
	}
	public void setLunarBirthday(boolean lunarBirthday) {
		this.lunarBirthday = lunarBirthday;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getHomePhoneNo() {
		return homePhoneNo;
	}
	public void setHomePhoneNo(String homePhoneNo) {
		this.homePhoneNo = homePhoneNo;
	}
	public int getSpaceType(){
		return ISmartWorks.SPACE_TYPE_USER;
	}
	
	public User(){
		super();
	}
	public User(String id, String name){
		super(id, name);
	}
	
	public String getEmailAddressShown(){
		if(this.getLongName() == null) return super.getId();
		if(SmartUtil.isEmailAddress(this.getLongName())) return this.getLongName();
		return this.getLongName() + "&lt;" + super.getId() + "&gt;";
	}
	
	public int getTimeOffsetInHour(){
		if(timeZone==null) return 0;
		return TimeZone.getTimeZone(timeZone).getRawOffset()/LocalDate.ONE_HOUR;
	}
	public UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo(getId(), getName());
		userInfo.setBigPictureName(getBigPictureName());
		userInfo.setSmallPictureName(getSmallPictureName());
		userInfo.setCellPhoneNo(getCellPhoneNo());
		userInfo.setDepartment(new DepartmentInfo(getDepartmentId(), getDepartment()));
		userInfo.setPhoneNo(getPhoneNo());
		userInfo.setPosition(getPosition());
		userInfo.setRole(getRole());
		return userInfo;
	}
	
	public boolean isAnonymusUser(){
		if(USER_ID_ANONYMOUS.equals(getId())) return true;
		return false;
	}
	
	public boolean isAdjunctUser(){
		if(SmartUtil.isBlankObject(this.departments)) return false;
		if(this.departments.length>1) return true;
		return false;
	}
	
	public String getFullDepartment(){
		if(!this.isAdjunctUser() || SmartUtil.isBlankObject(departments)) return this.department;
		String fullDepartment = "";
		for(int i=0; i<departments.length; i++){
			fullDepartment = fullDepartment + departments[i].getName() + "(" + SmartMessage.getString("organization.title.adjunct") + ")" + ((i==departments.length-1) ? "" : ", ");
		}
		return fullDepartment;
	}
	public static String getNoUserPicture(){
		return NO_PICTURE_PATH + User.NO_USER_PICTURE + ".jpg";
	}
	
	public String getEmailId(){
		if(SmartUtil.isBlankObject(mailAccounts)) return null;
		return mailAccounts[0].getEmailId();
	}
}
