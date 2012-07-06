package net.smartworks.model.instance;

import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.filter.info.SearchFilterInfo;

public class RunningCounts {

	private int total;
	private int assignedOnly;
	private int runningOnly;
	private SearchFilterInfo[] casterFilters;
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	public int getAssignedOnly() {
		return assignedOnly;
	}
	public void setAssignedOnly(int assignedOnly) {
		this.assignedOnly = assignedOnly;
	}
	public int getRunningOnly() {
		return runningOnly;
	}
	public void setRunningOnly(int runningOnly) {
		this.runningOnly = runningOnly;
	}
	public SearchFilterInfo[] getCasterFilters() {
		return casterFilters;
	}
	public void setCasterFilters(SearchFilterInfo[] casterFilters) {
		this.casterFilters = casterFilters;
	}

	public RunningCounts() {
		super();
	}

	public RunningCounts(int total, int assignedOnly) {
		super();
		this.total = total;
		this.assignedOnly = assignedOnly;
	}
}
