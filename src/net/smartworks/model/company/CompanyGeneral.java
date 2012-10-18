package net.smartworks.model.company;

import net.smartworks.model.BaseObject;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.organization.model.SwoCompany;
import net.smartworks.util.SmartConfUtil;
import net.smartworks.util.SmartUtil;

public class CompanyGeneral extends BaseObject {

	public static final String PICTURE_PATH = SmartConfUtil.getInstance().getImageServer();
	public static final String NO_LOGO_PATH = "images/";
	public static final String DEFAULT_COMPANY_LOGO = "default_company_logo.png";
	public static final String DEFAULT_COMPANY_TITLE_LOGO = "lo_logo_f.png";
	public static final String DEFAULT_COMPANY_LOGIN_IMAGE = "login_img.gif";
	public static final String PROFILES_DIR = "Profiles";

	public static final String IMAGE_TYPE_LOGO = "_logo";
	public static final String IMAGE_TYPE_TITLELOGO = "_titlelogo";
	public static final String IMAGE_TYPE_LOGINIMAGE = "_loginimage";

	private String logoName;
	private String titleLogoName;
	private String loginImageName;
	private String sendMailHost;
	private String sendMailAccount;
	private String sendMailPassword;
	private boolean sendMailNotification;
	private boolean testAfterSaving;
	private boolean useMessagingService=true;
	
	public boolean isUseMessagingService() {
		return useMessagingService;
	}
	public void setUseMessagingService(boolean useMessagingService) {
		this.useMessagingService = useMessagingService;
	}
	public String getLogoName() {
		return logoName;
	}
	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}
	public String getTitleLogoName() {
		return titleLogoName;
	}
	public void setTitleLogoName(String titleLogoName) {
		this.titleLogoName = titleLogoName;
	}
	public String getLoginImageName() {
		return loginImageName;
	}
	public void setLoginImageName(String loginImageName) {
		this.loginImageName = loginImageName;
	}
	public String getSendMailHost() {
		return sendMailHost;
	}
	public void setSendMailHost(String sendMailHost) {
		this.sendMailHost = sendMailHost;
	}
	public String getSendMailAccount() {
		return sendMailAccount;
	}
	public void setSendMailAccount(String sendMailAccount) {
		this.sendMailAccount = sendMailAccount;
	}
	public String getSendMailPassword() {
		return sendMailPassword;
	}
	public void setSendMailPassword(String sendMailPassword) {
		this.sendMailPassword = sendMailPassword;
	}
	public boolean isSendMailNotification() {
		return sendMailNotification;
	}
	public void setSendMailNotification(boolean sendMailNotification) {
		this.sendMailNotification = sendMailNotification;
	}
	public boolean isTestAfterSaving() {
		return testAfterSaving;
	}
	public void setTestAfterSaving(boolean testAfterSaving) {
		this.testAfterSaving = testAfterSaving;
	}

	public String getCompanyLogo() {
		if(!CommonUtil.isExistImage(getPath() + this.getLogoName())) {
			return NO_LOGO_PATH + DEFAULT_COMPANY_LOGO;
		}
		return getPath() + this.getLogoName();
	}

	public String getCompanyTitleLogo() {
		if(!CommonUtil.isExistImage(getPath() + this.getTitleLogoName())) {
			return NO_LOGO_PATH + DEFAULT_COMPANY_TITLE_LOGO;
		}
		return getPath() + this.getTitleLogoName();
	}

	public String getCompanyLoginImage() {
		if(!CommonUtil.isExistImage(getPath() + this.getLoginImageName())) {
			return NO_LOGO_PATH + DEFAULT_COMPANY_LOGIN_IMAGE;
		}
		return getPath() + this.getLoginImageName();
	}

	public String getCompanyId() {
		try {
			String companyId = SmartUtil.getCurrentUser().getCompanyId();
			if(CommonUtil.isEmpty(companyId)) {
				SwoCompany[] swoCompanies = SwManagerFactory.getInstance().getSwoManager().getCompanys(null, null, null);
				if(!CommonUtil.isEmpty(swoCompanies))
					companyId = swoCompanies[0].getId();
			}
			return companyId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String getPath(){
		return PICTURE_PATH + getCompanyId() + "/" + PROFILES_DIR + "/";
	}

	public CompanyGeneral(){
		super();
	}

	public CompanyGeneral(String id, String name){
		super(id, name);
	}
}
