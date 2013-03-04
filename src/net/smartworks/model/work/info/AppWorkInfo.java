package net.smartworks.model.work.info;

import net.smartworks.model.work.AppWork;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class AppWorkInfo extends WorkInfo {

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
	
	public AppWorkInfo(){
		super();
	}

	public AppWorkInfo(String id, String name, int type){
		super(id, name, type);
	}
}
