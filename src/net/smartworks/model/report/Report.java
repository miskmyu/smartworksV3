package net.smartworks.model.report;

import net.smartworks.model.BaseObject;
import net.smartworks.model.KeyMap;
import net.smartworks.model.community.User;
import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class Report extends BaseObject {

	public final static int TYPE_CHART = 1;
	public final static int TYPE_MATRIX = 2;
	public final static int TYPE_TABLE = 3;

	public static final KeyMap VALUE_TYPE_COUNT = new KeyMap("count", "report.value.type.count");
	public static final KeyMap VALUE_TYPE_SUM = new KeyMap("sum", "report.value.type.sum");
	public static final	KeyMap VALUE_TYPE_MEAN = new KeyMap("mean", "report.value.type.mean");
	public static final KeyMap VALUE_TYPE_MIN = new KeyMap("min", "report.value.type.min");
	public static final KeyMap VALUE_TYPE_MAX = new KeyMap("max", "report.value.type.max");

	public static final KeyMap AXIS_SELECTOR_BY_HOUR = new KeyMap("byHour", "report.axis.date.by_hour");
	public static final KeyMap AXIS_SELECTOR_BY_DAY = new KeyMap("byDay", "report.axis.date.by_day");
	public static final KeyMap AXIS_SELECTOR_BY_WEEK = new KeyMap("byWeek", "report.axis.date.by_week");
	public static final KeyMap AXIS_SELECTOR_BY_MONTH = new KeyMap("byMonth", "report.axis.date.by_month");
	public static final KeyMap AXIS_SELECTOR_BY_QUARTER = new KeyMap("byQuarter", "report.axis.date.by_quarter");
	public static final KeyMap AXIS_SELECTOR_BY_HALF_YEAR = new KeyMap("byHalfYear", "report.axis.date.by_half_year");
	public static final KeyMap AXIS_SELECTOR_BY_YEAR = new KeyMap("byYear", "report.axis.date.by_year");

	public static final KeyMap AXIS_SELECTOR_USER_NAME = new KeyMap("userName", "report.axis.user.name");
	public static final KeyMap AXIS_SELECTOR_USER_DEPARTMENT = new KeyMap("userDepartment", "report.axis.user.department");
	public static final KeyMap AXIS_SELECTOR_USER_POSITION = new KeyMap("userPosition", "report.axis.user.position");
	public static final KeyMap AXIS_SELECTOR__LEVEL = new KeyMap("userLevel", "report.axis.user.level");
	public static final KeyMap AXIS_SELECTOR_USER_LOCALE = new KeyMap("userLocale", "report.axis.user.locale");

	public static final KeyMap AXIS_SORT_ASCEND = new KeyMap("ascend", "report.axis.sort.ascend");
	public static final KeyMap AXIS_SORT_DESCEND = new KeyMap("descend", "report.axis.sort.descend");

	private int type=-1;
	private SearchFilter searchFilter;
	private Work work;
	private User owner;
	private LocalDate createdDate;
	private User lastModifier;
	private LocalDate lastModifiedDate;

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public SearchFilter getSearchFilter() {
		return searchFilter;
	}
	public void setSearchFilter(SearchFilter searchFilter) {
		this.searchFilter = searchFilter;
	}
	public Work getWork() {
		return work;
	}
	public void setWork(Work work) {
		this.work = work;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public User getLastModifier() {
		return lastModifier;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}
	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public Report() {
		super();
	}
	
	public Report(String id, String name){
		super(id, name);
	}

	public Report(String id, String name, int type, User owner, User lastModifier,
			LocalDate lastModifiedDate) {
		super(id, name);
		this.type = type;
		this.owner = owner;
		this.lastModifier = lastModifier;
		this.lastModifiedDate = lastModifiedDate;
	}
}