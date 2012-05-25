package net.smartworks.util;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.MemberInformList;
import net.smartworks.model.sera.MenteeInformList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.model.sera.SeraUser;
import net.smartworks.model.sera.Team;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.MentorInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.model.sera.info.MissionReportInstanceInfo;
import net.smartworks.model.sera.info.NoteInstanceInfo;
import net.smartworks.model.sera.info.ReviewInstanceInfo;
import net.smartworks.model.sera.info.SeraUserInfo;
import net.smartworks.model.sera.info.TeamInfo;

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
		return mentor;
	}

	public static SeraUser getSeraUserById(String userId) throws Exception{
		SeraUser user = new SeraUser();
		user.setId("kmyu@maninsoft.co.kr");
		user.setName("유광민");
		user.setPosition("기술연구소장");
		user.setDepartment("기술사업팀");
		user.setLocale("ko_KR"); // ko_KR, en_US
		user.setTimeZone("SEOUL");
		user.setCompany("(주)맨인소프트");
		return user;
	}
	
	public static MissionInstanceInfo getMissionInstanceInfo1() throws Exception{
		CourseInfo course = new CourseInfo("course1", "어느젊음마법사의 코스");
		course.setLeader(SmartTest.getUserInfo2());
		course.setNumberOfGroupMember(51);
		course.setOpenDate(new LocalDate());
		course.setOwner(SmartTest.getUserInfo3());
		course.setDesc("안녕하세요 선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다.");
		return new MissionInstanceInfo("mission1", "자화상 그리기", course, SmartTest.getUserInfo1(), new LocalDate());
	}
	
	public static MissionInstanceInfo getMissionInstanceInfo2() throws Exception{
		CourseInfo course = new CourseInfo("course1", "어느젊음마법사의 코스");
		course.setLeader(SmartTest.getUserInfo2());
		course.setNumberOfGroupMember(51);
		course.setOpenDate(new LocalDate());
		course.setOwner(SmartTest.getUserInfo3());
		course.setDesc("안녕하세요 선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다.");
		return new MissionInstanceInfo("mission2", "장래희망 정리하기", course, SmartTest.getUserInfo1(), new LocalDate());
	}
	
	public static CourseInfo getCourseInfo1() throws Exception {
		CourseInfo course = new CourseInfo("course1", "어느젊음마법사의 코스");
		course.setLeader(SmartTest.getUserInfo2());
		course.setNumberOfGroupMember(51);
		course.setOpenDate(new LocalDate());
		course.setOwner(SmartTest.getUserInfo3());
		course.setDesc("안녕하세요 선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다.");
		return course;
	}

	public static CourseInfo getCourseInfo2() throws Exception {
		CourseInfo course = new CourseInfo("course2", "고등학생을위한 코스");
		course.setLeader(SmartUtil.getCurrentUser().getUserInfo());
		course.setNumberOfGroupMember(28);
		course.setOpenDate(new LocalDate());
		course.setOwner(SmartTest.getUserInfo3());
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
		course.setMissions(SeraTest.getMissionInstanceList(null, null, null));
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
		friendList.setFriends(SeraTest.getFriendRequestsForMe(null, -1));
		friendList.setTotalFriends(51);
		return friendList;		
	}

	public static MenteeInformList getCourseMenteeInformations(String courseId, int maxList) throws Exception{
		MenteeInformList menteeInformList = new MenteeInformList();
		menteeInformList.setJoinRequesters(SeraTest.getFriendRequestsForMe(null, -1));
		menteeInformList.setMentees(SeraTest.getFriendRequestsForMe(null, -1));
		menteeInformList.setNonMentees(SeraTest.getFriendRequestsForMe(null, -1));
		menteeInformList.setTotalJoinRequesters(31);
		menteeInformList.setTotalMentees(43);
		menteeInformList.setTotalNonMentees(57);
		return menteeInformList;		
	}
	
	
	public static MemberInformList getTeamMemberInformations(String teamId, int maxList) throws Exception{
		MemberInformList memberInformList = new MemberInformList();
		memberInformList.setMembers(SeraTest.getFriendRequestsForMe(null, -1));
		memberInformList.setNonMembers(SeraTest.getFriendRequestsForMe(null, -1));
		memberInformList.setTotalMembers(43);
		memberInformList.setTotalNonMembers(57);
		return memberInformList;		
	}
	
	
	public static SeraUserInfo[] getCourseMenteeInformsByType(int type, String courseId, String lastId, int maxList) throws Exception{
		return (SeraTest.getFriendRequestsForMe(null, -1));
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
	
	public static InstanceInfo[] getSeraInstances(String userId, String courseId, String missionId, LocalDate fromDate, int maxList) throws Exception{
		InstanceInfo[] boards = SmartTest.getBoardInstances();
		InstanceInfo[] events = SmartTest.getEventInstances();
		InstanceInfo message = SmartTest.getMessageInstanceInfo();
		NoteInstanceInfo seraNote1 = new NoteInstanceInfo("note1", "노트제목입니다.", SmartTest.getUserInfo1(), new LocalDate());
		seraNote1.setWorkSpace(getCourseInfo1());
		seraNote1.setContent("안녕하세요, 세라 노트 내용입니다.");
		NoteInstanceInfo seraNote2 = new NoteInstanceInfo("note1", "노트제목입니다.", SmartTest.getUserInfo1(), new LocalDate());
		seraNote2.setWorkSpace(getCourseInfo2());
		seraNote2.setContent("세라 이미지 테스트 내용입니다.");
		seraNote2.setImageSrc("http://localhost:8081/imageServer/Semiteq/Profiles/ysjung@maninsoft.co.kr_big.png");
		NoteInstanceInfo seraNote3 = new NoteInstanceInfo("note1", "노트제목입니다.", SmartTest.getUserInfo1(), new LocalDate());
		seraNote3.setWorkSpace(getCourseInfo1());
		seraNote3.setContent("세라 동영상 테스트 내용입니다.");
		seraNote3.setVideoId("u1zgFlCw8Aw");
		MissionReportInstanceInfo report1 = new MissionReportInstanceInfo("report1", "미션 수행입니다.", SmartTest.getUserInfo1(), new LocalDate());
		report1.setWorkSpace(getCourseInfo1());
		report1.setContent("미션 수행결과입니다. 좋은 평가 부탁드립니다.");
		
		InstanceInfo[] instances = new InstanceInfo[7];
		instances[0] = boards[0];
		instances[1] = events[0];
		instances[2] = message;
		instances[3] = seraNote1;
		instances[4] = seraNote2;
		instances[5] = seraNote3;
		instances[6] = report1;
		
		return instances;
	}
	public static ReviewInstanceInfo[] getReviewInstances() throws Exception{
		ReviewInstanceInfo reviewInstance1 = new ReviewInstanceInfo("review1", "조금더 보강해야 될것 같은데요????",SmartTest.getInformationWorkInfo1(), SmartTest.getUserInfo1(), new LocalDate());
		return new ReviewInstanceInfo[] {reviewInstance1};
		
	}

	public static ReviewInstanceInfo[] getReviewInstancesByCourse(String courseId, LocalDate fromDate, int maxList) throws Exception{
		ReviewInstanceInfo[] reviews = SeraTest.getReviewInstances();
		return reviews;
	}

	public static MissionInstanceInfo[] getMissionInstanceList(String courseId, LocalDate fromDate, LocalDate toDate) throws Exception{
		CourseInfo course = new CourseInfo("course1", "어느젊음마법사의 코스");
		course.setLeader(SmartTest.getUserInfo2());
		course.setNumberOfGroupMember(51);
		course.setOpenDate(new LocalDate());
		course.setOwner(SmartTest.getUserInfo3());
		course.setDesc("안녕하세요 선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다.");

		MissionInstanceInfo mission1 = new MissionInstanceInfo("mission1", "자화상 그리기 미션", course, SmartTest.getUserInfo1(), new LocalDate());
		mission1.setContent("첫번째 미션입니다. 잘 수행하시기 바랍니다.");
		mission1.setIndex(0);
		mission1.setOpenDate(new LocalDate());
		mission1.setMissionClearers(new String[]{SmartUtil.getCurrentUser().getId()});
		
		MissionInstanceInfo mission2 = new MissionInstanceInfo("mission2", "장래희망 계획 미션", course, SmartTest.getUserInfo1(), new LocalDate());
		mission2.setContent("두번째 미션입니다. 잘 수행하시기 바랍니다.");
		mission2.setIndex(1);
		mission2.setOpenDate(new LocalDate(new LocalDate().getGMTDate()-LocalDate.ONE_DAY/2));
		
		MissionInstanceInfo mission3 = new MissionInstanceInfo("mission3", "자기소개서 작성 미션", course, SmartTest.getUserInfo1(), new LocalDate());
		mission3.setContent("세번째 미션입니다. 잘 수행하시기 바랍니다.");
		mission3.setIndex(2);
		mission3.setOpenDate(new LocalDate(new LocalDate().getGMTDate()+LocalDate.ONE_DAY/2));
		MissionInstanceInfo[] missions = new MissionInstanceInfo[]{mission1, mission2, mission3};
		return missions;
	}

	public static MissionInstance getMissionById(String missionId) throws Exception{
		MissionInstance mission = new MissionInstance("mission1", "자화상 그리기 미션", SeraTest.getCourse1(), SmartTest.getUser1(), new LocalDate());
		mission.setContent("첫번째 미션입니다. 잘 수행하시기 바랍니다.");
		mission.setIndex(0);
		mission.setOpenDate(new LocalDate((new LocalDate()).getGMTDate() - LocalDate.ONE_DAY));
		mission.setCloseDate(new LocalDate((new LocalDate()).getGMTDate() + LocalDate.ONE_DAY));
		mission.setMissionClearers(new String[]{SmartUtil.getCurrentUser().getId()});
		return mission;
	}
	
	public static SeraUserInfo[] getFriendRequestsForMe(String lastId, int maxList) throws Exception{
		SeraUserInfo seraUser1 = new SeraUserInfo("ktsoo@maninsoft.co.kr", "김 태수");
		seraUser1.setGoal("개인 달성 목표입니다.");
		return new SeraUserInfo[]{seraUser1, seraUser1, seraUser1, seraUser1, seraUser1, seraUser1 };
	}

	public static Team getTeam() throws Exception{
		Team team = new Team("team1", "테스트팀입니다.");
		team.setDesc("테스트팀이니 잘 활용하시기 바랍니다.");
		team.setStart(new LocalDate());
		team.setEnd(new LocalDate());
		team.setAccessPolicy(AccessPolicy.LEVEL_PRIVATE);
		team.setMaxMembers(10);
		team.setMembers(SeraTest.getFriendRequestsForMe("", 1));
		return team;
	}
	
	public static TeamInfo[] getTeams() throws Exception{
		return new TeamInfo[] {new TeamInfo("team1", "코스팀 1"), new TeamInfo("team2", "코스팀 2"), new TeamInfo("team3", "코스팀 3"), new TeamInfo("team4", "코스팀 4")};
	}
}
