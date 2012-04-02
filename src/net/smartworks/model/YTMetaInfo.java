package net.smartworks.model;

public class YTMetaInfo {

	private String title;
	private String category;
	private String[] keywords;
	private boolean isPrivate;
	private String desc;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String[] getKeywords() {
		return keywords;
	}
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	public boolean isPrivate() {
		return isPrivate;
	}
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public YTMetaInfo(String title, String category, String[] keywords, String desc){
		super();
		this.title = title;
		this.category = category;
		this.keywords = keywords;
		this.desc = desc;
	}
}
