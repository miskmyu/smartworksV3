package net.smartworks.util;

import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.MentorInfo;
import net.smartworks.model.sera.info.MissionInfo;

public class SeraTest {

	public static Mentor getMentor1() throws Exception{
		Mentor mentor = new Mentor();
		mentor.setId("kmyu@maninsoft.co.kr");
		mentor.setName("유광민");
		mentor.setPosition("기술연구소장");
		mentor.setDepartment("기술사업팀");
		mentor.setLocale("ko_KR"); // ko_KR, en_US
		mentor.setTimeZone("SEOUL");
		mentor.setCompany("(주)맨인소프트");
		return mentor;
	}
	
	public static MentorInfo getMentorInfo1() throws Exception{
		MentorInfo mentor = new MentorInfo();
		mentor.setId("kmyu@maninsoft.co.kr");
		mentor.setName("유광민");
		mentor.setPosition("기술연구소장");
		mentor.setDepartment("기술사업팀");
		mentor.setLocale("ko_KR"); // ko_KR, en_US
		mentor.setTimeZone("SEOUL");
		mentor.setCompany("(주)맨인소프트");
		return mentor;
	}
	
	public static Mentor getMentor2() throws Exception{
		Mentor mentor = new Mentor();
		mentor.setId("ysjung@maninsoft.co.kr");
		mentor.setName("정윤식");
		mentor.setPosition("기술연구소장");
		mentor.setDepartment("기술사업팀");
		mentor.setLocale("ko_KR"); // ko_KR, en_US
		mentor.setTimeZone("SEOUL");
		mentor.setCompany("(주)맨인소프트");
		return mentor;
	}
	
	public static MentorInfo getMentorInfo2() throws Exception{
		MentorInfo mentor = new MentorInfo();
		mentor.setId("ysjung@maninsoft.co.kr");
		mentor.setName("정윤식");
		mentor.setPosition("기술연구소장");
		mentor.setDepartment("기술사업팀");
		mentor.setLocale("ko_KR"); // ko_KR, en_US
		mentor.setTimeZone("SEOUL");
		mentor.setCompany("(주)맨인소프트");
		return mentor;
	}
	
	public static MissionInfo getMissionInfo1() throws Exception{
		return new MissionInfo("mission1", "자화상 그리기");
	}
	
	public static MissionInfo getMissionInfo2() throws Exception{
		return new MissionInfo("mission2", "장래희망 정리하기");
	}
	
	public static CourseInfo getCourseInfo1() throws Exception {
		CourseInfo course = new CourseInfo("course1", "어느젊음마법사의 코스");
		course.setLastMission(getMissionInfo1());
		course.setLeader(SmartTest.getUserInfo2());
		course.setNumberOfGroupMember(51);
		course.setOpenDate(new LocalDate());
		course.setOwner(SmartTest.getUserInfo3());
		course.setTargetPoint(18);
		course.setAchievedPoint(5);
		course.setDesc("안녕하세요 선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다.");
		return course;
	}

	public static CourseInfo getCourseInfo2() throws Exception {
		CourseInfo course = new CourseInfo("course2", "고등학생을위한 코스");
		course.setLastMission(getMissionInfo2());
		course.setLeader(SmartUtil.getCurrentUser().getUserInfo());
		course.setNumberOfGroupMember(28);
		course.setOpenDate(new LocalDate());
		course.setOwner(SmartTest.getUserInfo3());
		course.setTargetPoint(9);
		course.setAchievedPoint(8);
		course.setDesc("안녕하세요 선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다.");
		return course;
	}

	public static Course getCourse1() throws Exception {
		Course course = new Course("course1", "어느젊음마법사의 코스", new UserInfo[] { SmartTest.getUserInfo1(), SmartTest.getUserInfo2() }, getMentor1());
		course.setAutoApproval(true);
		User user1 = SmartTest.getUser1();
		user1.setId("ysjung@maninsoft.co.kr");
		course.setLeader(user1);
//		course.setLeader(smartTest.getUser1());
		course.setNumberOfGroupMember(51);
		course.setOpenDate(new LocalDate());
		course.setOwner(SmartTest.getUser3());
		course.setTargetPoint(18);
		course.setAchievedPoint(5);
		course.setDesc("안녕하세요 선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다.");
		return course;
	}

	public static CourseList getCoursesById(String userId, int maxList) throws Exception{
		CourseList courseList = new CourseList();
		courseList.setRunnings(21);
		courseList.setAttendings(8);
		courseList.setRunningCourses(new CourseInfo[]{getCourseInfo2(), getCourseInfo2(), getCourseInfo2(), getCourseInfo2(), getCourseInfo2()});
		courseList.setAttendingCourses(new CourseInfo[]{getCourseInfo1(), getCourseInfo1(), getCourseInfo1(), getCourseInfo1(), getCourseInfo1()});
		return courseList;
	}
	
	public static CourseInfo[] getCoursesById(String userId, int courseType, LocalDate fromDate, int maxList) throws Exception{
		return new CourseInfo[]{getCourseInfo1(), getCourseInfo2(), getCourseInfo2(), getCourseInfo2(), getCourseInfo2(), getCourseInfo1(), new CourseInfo()};
	}
	
	public static Course getCourseById(String courseId) throws Exception{
		return getCourse1();
	}
	
	public static Mentor getMentorById(String mentorId) throws Exception{
		return getMentor1();
	}
	
	public static FriendList getFriendsById(String userId, int maxList) throws Exception{
		FriendList friendList = new FriendList();
		friendList.setFriends(SmartTest.getAvailableChatter());
		friendList.setTotalFriends(51);
		return friendList;		
	}
	
	public static UserInfo[] getFriendsById(String userId, String lastId, int maxList) throws Exception{
		return SmartTest.getAvailableChatter();
	}
	
	public static InstanceInfo[] getCourseNotices(String courseId, LocalDate fromDate, int maxList) throws Exception{
		InstanceInfo[] boards = SmartTest.getBoardInstances();
		InstanceInfo[] events = SmartTest.getEventInstances();
		InstanceInfo[] instances = new InstanceInfo[5];
		for(int i=0; i<3; i++)
			instances[i] = boards[i];
		for(int i=0; i<2; i++)
			instances[3+i] = events[i];
		
		return instances;
	}
}
