package net.smartworks.model.work;

import net.smartworks.model.KeyMap;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class AppWork extends Work {

	public final static int TYPE_APP_WORK = 51;

	public static final KeyMap[] CATEGORIES_BY_INDUSTRY = new KeyMap[]{
		new KeyMap("CBI01", SmartMessage.getString("category.industry.1")),
		new KeyMap("CBI02", SmartMessage.getString("category.industry.2")),
		new KeyMap("CBI03", SmartMessage.getString("category.industry.3")),
		new KeyMap("CBI04", SmartMessage.getString("category.industry.4")),
		new KeyMap("CBI05", SmartMessage.getString("category.industry.5")),
		new KeyMap("CBI06", SmartMessage.getString("category.industry.6")),
		new KeyMap("CBI07", SmartMessage.getString("category.industry.7")),
		new KeyMap("CBI08", SmartMessage.getString("category.industry.8")),
		new KeyMap("CBI09", SmartMessage.getString("category.industry.9")),
		new KeyMap("CBI10", SmartMessage.getString("category.industry.10")),
		new KeyMap("CBI11", SmartMessage.getString("category.industry.11")),
		new KeyMap("CBI99", SmartMessage.getString("category.industry.99"))
	};
	public static final KeyMap[] CATEGORIES_BY_JOB = new KeyMap[]{
		new KeyMap("CBJ01", SmartMessage.getString("category.job.1")),
		new KeyMap("CBJ02", SmartMessage.getString("category.job.2")),
		new KeyMap("CBJ03", SmartMessage.getString("category.job.3")),
		new KeyMap("CBJ04", SmartMessage.getString("category.job.4")),
		new KeyMap("CBJ05", SmartMessage.getString("category.job.5")),
		new KeyMap("CBJ06", SmartMessage.getString("category.job.6")),
		new KeyMap("CBJ07", SmartMessage.getString("category.job.7")),
		new KeyMap("CBJ08", SmartMessage.getString("category.job.8")),
		new KeyMap("CBJ09", SmartMessage.getString("category.job.9")),
		new KeyMap("CBJ10", SmartMessage.getString("category.job.10")),
		new KeyMap("CBJ99", SmartMessage.getString("category.job.99"))
	};
	
	private String categoryIdByIndustry;
	private String categoryIdByJob;
	private int workType;
	private String publishedCompany;
	private LocalDate publishedDate;
	
	public String getCategoryIdByIndustry() {
		return categoryIdByIndustry;
	}

	public void setCategoryIdByIndustry(String categoryIdByIndustry) {
		this.categoryIdByIndustry = categoryIdByIndustry;
	}

	public String getCategoryIdByJob() {
		return categoryIdByJob;
	}

	public void setCategoryIdByJob(String categoryIdByJob) {
		this.categoryIdByJob = categoryIdByJob;
	}

	public int getWorkType() {
		return workType;
	}

	public void setWorkType(int workType) {
		this.workType = workType;
	}

	public String getPublishedCompany() {
		return publishedCompany;
	}

	public void setPublishedCompany(String publishedCompany) {
		this.publishedCompany = publishedCompany;
	}

	public LocalDate getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(LocalDate publishedDate) {
		this.publishedDate = publishedDate;
	}

	public boolean isNew(){
		if(SmartUtil.isBlankObject(this.publishedDate)) return false;;
		return this.publishedDate.isNew();
	}
	
	public AppWork() {
		super();
	}

	public AppWork(String id, String name) {
		super(id, name);
	}

	public AppWork(String id, String name, int type, String desc) {
		super(id, name, type, desc);
	}

	public static String getCategoryNameByIndustry(String id){
		if(SmartUtil.isBlankObject(id)) return "";
		for(int i=0; i<CATEGORIES_BY_INDUSTRY.length; i++)
			if(id.equals(CATEGORIES_BY_INDUSTRY[i].getId()))
				return CATEGORIES_BY_INDUSTRY[i].getKey();
		return "";
	}
	
	public static String getCategoryNameByJob(String id){
		if(SmartUtil.isBlankObject(id)) return "";
		for(int i=0; i<CATEGORIES_BY_JOB.length; i++)
			if(id.equals(CATEGORIES_BY_JOB[i].getId()))
				return CATEGORIES_BY_JOB[i].getKey();
		return "";
	}
	
}
