package net.smartworks.model.calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.smartworks.model.instance.EventInstance;
import net.smartworks.util.LocalDate;

public class RepeatEvent{

	private final static String REPEAT_BY_EVERY_DAY = "everyDay";
	private final static String REPEAT_BY_EVERY_WEEK = "everyWeek";
	private final static String REPEAT_BY_BI_WEEK = "biWeek";
	private final static String REPEAT_BY_EVERY_MONTH_DATE = "everyMonthDate";
	private final static String REPEAT_BY_EVERY_MONTH_CUSTOM = "everyMonthCustom";
	private final static String REPEAT_BY_BI_MONTH_DATE = "biMonthDate";
	private final static String REPEAT_BY_BI_MONTH_CUSTOM = "biMonthCustom";
	private  final static String REPEAT_WEEK_FIRST = "firstWeek";
	private  final static String REPEAT_WEEK_SECOND = "secondWeek";
	private  final static String REPEAT_WEEK_THIRD = "thirdWeek";
	private  final static String REPEAT_WEEK_FOURTH = "fourthWeek";
	private  final static String REPEAT_WEEK_LAST = "lastWeek";
	private  final static String REPEAT_FIRST = "first";
	private  final static String REPEAT_SECOND = "second";
	private  final static String REPEAT_THIRD = "third";
	private  final static String REPEAT_FOURTH = "fourth";
	private  final static String REPEAT_LAST = "last";
	private  final static String REPEAT_DAY_MON = "mon";
	private  final static String REPEAT_DAY_TUE = "tue";
	private  final static String REPEAT_DAY_WED = "wed";
	private  final static String REPEAT_DAY_THU = "thu";
	private  final static String REPEAT_DAY_FRI = "fri";
	private  final static String REPEAT_DAY_SAT = "sat";
	private  final static String REPEAT_DAY_SUN = "sun";
	private  final static String REPEAT_END_DATE = "endDate";
	private  final static String REPEAT_END_COUNT = "repeatCount";

	public final static int	REPEAT_INTERVAL_NONE 				= -1;
	public final static int	REPEAT_INTERVAL_EVERY_DAY 			= 1;
	public final static int	REPEAT_INTERVAL_EVERY_WEEK 			= 10;
	public final static int	REPEAT_INTERVAL_BI_WEEK 			= 11;
	public final static int	REPEAT_INTERVAL_EVERY_MONTH_DATE 	= 20;
	public final static int	REPEAT_INTERVAL_EVERY_MONTH_CUSTOM 	= 21;
	public final static int	REPEAT_INTERVAL_BI_MONTH_DATE 		= 22;
	public final static int	REPEAT_INTERVAL_BI_MONTH_CUSTOM 	= 23;

	public final static int	DAY_OF_WEEK_SUNDAY		= 0;
	public final static int	DAY_OF_WEEK_MONDAY 		= 1;
	public final static int	DAY_OF_WEEK_TUESDAY 	= 2;
	public final static int	DAY_OF_WEEK_WENDSDAY 	= 3;
	public final static int	DAY_OF_WEEK_THURSDAY	= 4;
	public final static int	DAY_OF_WEEK_FRIDAY 		= 5;
	public final static int	DAY_OF_WEEK_SATERDAY 	= 6;
	
	private int 		repeatInterval; 	// 반복 기간 : 매일, 매주, 격주, 매월, 격월
	private LocalDate	repeatEndDate;		// 반복 종료일(반복종료일이나 반복횟수중 하나만 선택)
	private int			repeatCount=-1;	// 반복 횟수(반복종료일이나 반복횟수중 하나만 선택)
	private int			weekOfMonth;		// 달중에 몇번째 주 
	private int			dayOfWeek;			// 주중에 무슨 요일 
	private int			dateOfMonth;		// 월중에 무슨 일
	private Date		dateOfYear;			// 년중에 무슨일

	private LocalDate	startTime;			// 시작 시간
	private LocalDate	endTime;			// 종료 시간(선택)
	
	public int getRepeatInterval() {
		return repeatInterval;
	}
	public void setRepeatInterval(int repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	public LocalDate getRepeatEndDate() {
		return repeatEndDate;
	}
	public void setRepeatEndDate(LocalDate repeatEndDate) {
		this.repeatEndDate = repeatEndDate;
	}
	public int getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}
	public int getWeekOfMonth() {
		return weekOfMonth;
	}
	public void setWeekOfMonth(int weekOfMonth) {
		this.weekOfMonth = weekOfMonth;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getDateOfMonth() {
		return dateOfMonth;
	}
	public void setDateOfMonth(int dateOfMonth) {
		this.dateOfMonth = dateOfMonth;
	}
	public Date getDateOfYear() {
		return dateOfYear;
	}
	public void setDateOfYear(Date dateOfYear) {
		this.dateOfYear = dateOfYear;
	}
	public LocalDate getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDate startTime) {
		this.startTime = startTime;
	}
	public LocalDate getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDate endTime) {
		this.endTime = endTime;
	}	
	public RepeatEvent(){
		super();
	}
	
	public RepeatEvent(String repeatBy, String repeatWeek, String repeatDay, String repeatDate, String repeatEnd, String endDate, String repeatCount){
		super();
		this.repeatInterval = RepeatEvent.getRepeatInterval(repeatBy);
		this.weekOfMonth = RepeatEvent.getWeekOfMonth(repeatWeek);
		this.dayOfWeek = RepeatEvent.getDayOfWeek(repeatDay);
		this.dateOfMonth = RepeatEvent.getDateOfMonth(repeatDate);
		if(repeatEnd!=null){
			if(repeatEnd.equals(REPEAT_END_DATE)){
				try{
					this.repeatEndDate = LocalDate.convertLocalDateStringToLocalDate(endDate);
				}catch (Exception e){
				}
			}else if(repeatEnd.equals(REPEAT_END_COUNT)){
				try{
					this.repeatCount = Integer.parseInt(repeatCount);
				}catch (Exception e){
				}
				
			}
		}
	}
	
	public static int getRepeatInterval(String repeatBy){
		if(repeatBy==null) return REPEAT_INTERVAL_NONE;
		else if(repeatBy.equals(REPEAT_BY_EVERY_DAY)) return REPEAT_INTERVAL_EVERY_DAY;
		else if(repeatBy.equals(REPEAT_BY_EVERY_WEEK)) return REPEAT_INTERVAL_EVERY_WEEK;
		else if(repeatBy.equals(REPEAT_BY_BI_WEEK)) return REPEAT_INTERVAL_BI_WEEK;
		else if(repeatBy.equals(REPEAT_BY_EVERY_MONTH_DATE)) return REPEAT_INTERVAL_EVERY_MONTH_DATE;
		else if(repeatBy.equals(REPEAT_BY_EVERY_MONTH_CUSTOM)) return REPEAT_INTERVAL_EVERY_MONTH_CUSTOM;
		else if(repeatBy.equals(REPEAT_BY_BI_MONTH_DATE)) return REPEAT_INTERVAL_BI_MONTH_DATE;
		else if(repeatBy.equals(REPEAT_BY_BI_MONTH_CUSTOM)) return REPEAT_INTERVAL_BI_MONTH_CUSTOM;
		return REPEAT_INTERVAL_NONE;
	}
	
	public static int getWeekOfMonth(String repeatWeek){
		if(repeatWeek==null) return -1;
		else if(repeatWeek.equals(REPEAT_WEEK_FIRST)) return 1;
		else if(repeatWeek.equals(REPEAT_WEEK_SECOND)) return 2;
		else if(repeatWeek.equals(REPEAT_WEEK_THIRD)) return 3;
		else if(repeatWeek.equals(REPEAT_WEEK_FOURTH)) return 4;
		else if(repeatWeek.equals(REPEAT_WEEK_LAST)) return 6;
		else if(repeatWeek.equals(REPEAT_FIRST)) return 11;
		else if(repeatWeek.equals(REPEAT_SECOND)) return 12;
		else if(repeatWeek.equals(REPEAT_THIRD)) return 13;
		else if(repeatWeek.equals(REPEAT_FOURTH)) return 14;
		else if(repeatWeek.equals(REPEAT_LAST)) return 16;
		return -1;
	}

	public static int getDayOfWeek(String repeatDay){
		if(repeatDay==null) return -1;
		else if(repeatDay.equals(REPEAT_DAY_MON)) return DAY_OF_WEEK_MONDAY;
		else if(repeatDay.equals(REPEAT_DAY_TUE)) return DAY_OF_WEEK_TUESDAY;
		else if(repeatDay.equals(REPEAT_DAY_WED)) return DAY_OF_WEEK_WENDSDAY;
		else if(repeatDay.equals(REPEAT_DAY_THU)) return DAY_OF_WEEK_THURSDAY;
		else if(repeatDay.equals(REPEAT_DAY_FRI)) return DAY_OF_WEEK_FRIDAY;
		else if(repeatDay.equals(REPEAT_DAY_SAT)) return DAY_OF_WEEK_SATERDAY;
		else if(repeatDay.equals(REPEAT_DAY_SUN)) return DAY_OF_WEEK_SUNDAY;
		return -1;
	}

	public static int getDateOfMonth(String repeatDate){
		if(repeatDate==null) return -1;
		try{
			int date = Integer.parseInt(repeatDate);
			if(date<1 || date>31){
				return -1;
			}
			return date-1;
		}catch (Exception e){
			return -1;
		}
	}	
}
