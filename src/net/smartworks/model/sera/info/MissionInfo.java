package net.smartworks.model.sera.info;

import net.smartworks.model.BaseObject;
import net.smartworks.model.community.Community;
import net.smartworks.model.community.Department;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.sera.Course;
import net.smartworks.server.engine.security.model.Login;
import net.smartworks.util.LocalDate;


public class MissionInfo extends BaseObject{

	public MissionInfo(){
		super();
	}
	public MissionInfo(String id, String name){
		super(id, name);
	}
}
