package net.smartworks.model.instance;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.info.TaskInstanceInfo;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class ProcessWorkInstance extends WorkInstance {

	private int numberOfForwardHistories = 0;

	public int getNumberOfForwardHistories() {
		return numberOfForwardHistories;
	}
	public void setNumberOfForwardHistories(int numberOfForwardHistories) {
		this.numberOfForwardHistories = numberOfForwardHistories;
	}

	public ProcessWorkInstance() {
		super();
		super.setType(WorkInstance.TYPE_PROCESS);
	}

	public ProcessWorkInstance(String id, String subject, Work work, User owner, User lastModifier,
			LocalDate lastModifiedDate) {
		super(id, subject, work, owner, lastModifier, lastModifiedDate);
		super.setType(WorkInstance.TYPE_PROCESS);
	}
}
