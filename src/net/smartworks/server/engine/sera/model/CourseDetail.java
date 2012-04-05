/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 4. 4.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.model;

import net.smartworks.model.sera.Team;
import net.smartworks.model.sera.info.MissionInfo;
import net.smartworks.util.LocalDate;

public class CourseDetail {
	private String object;
	private String[] categories;
	private String[] keywords;
	private int duration;
	private LocalDate start;
	private LocalDate end;
	private int maxMentees;
	private boolean autoApproval;
	private boolean payable;
	private int fee;
	private Team team;
	private int targetPoint;
	private int achievedPoint;
}
