package net.smartworks.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import net.smartworks.model.KeyMap;
import net.smartworks.model.community.User;

public class LocalDate extends Date{

	public final static int ONE_SECOND = 1000;
	public final static	int ONE_MINUTE = 60*ONE_SECOND;
	public final static int ONE_HOUR = 60*ONE_MINUTE;
	public final static long ONE_DAY = 24*ONE_HOUR;
	public final static long ONE_WEEK = 7*ONE_DAY;
	public final static long ONE_YEAR = 365*ONE_DAY;
	
	public final static String TIMEZONE_SEOUL = "Asia/Seoul";
	public final static String TIMEZONE_LOS_ANGELES = "America/Los_Angeles";

	private static final long serialVersionUID = 1L;
	private TimeZone timeZone = TimeZone.getDefault();
	private TimeZone hostTimeZone = TimeZone.getDefault();
	private Locale locale = new Locale(LocaleInfo.LOCALE_DEFAULT);
	private long localNow = System.currentTimeMillis();
	private int firstDayOfWeek = Calendar.MONDAY;
	public LocalDate(){
		super();
		super.setTime(super.getTime()-hostTimeZone.getRawOffset());
		try {
			User user = SmartUtil.getCurrentUser();
			if(user.getTimeZone()!=null && isValidTimeZone(user.getTimeZone()))
				this.timeZone = TimeZone.getTimeZone(user.getTimeZone());
			if(user.getLocale()!=null && LocaleInfo.isSupportingLocale(user.getLocale()))
				this.locale = new Locale(user.getLocale());
		} catch (Exception e) {}
	}
	public LocalDate(long GMTDate){
		super(GMTDate);
		try {
			User user = SmartUtil.getCurrentUser();
			if(user.getTimeZone()!=null && isValidTimeZone(user.getTimeZone()))
				this.timeZone = TimeZone.getTimeZone(user.getTimeZone());
			if(user.getLocale()!=null && LocaleInfo.isSupportingLocale(user.getLocale()))
				this.locale = new Locale(user.getLocale());
		} catch (Exception e) {}
	}
	
	public LocalDate(long GMTDate, String timeZone, String locale){
		super(GMTDate);
		if(isValidTimeZone(timeZone))
			this.setTimeZone(timeZone);
		if(LocaleInfo.isSupportingLocale(locale))
			this.setLocale(locale);
	}
	
	public String getLocale(){
		return locale.toString();
	}
	public void setLocale(String locale){
		if(LocaleInfo.isSupportingLocale(locale)){
			this.locale = new Locale(locale);
		}
	}
	public long getLocalDate(){
		return super.getTime() + this.timeZone.getRawOffset();
	}

	public long getGMTDate(){
		return super.getTime();
	}
	public void setGMTDate(long GMTDate){
		super.setTime(GMTDate);
	}
	public String getTimeZone(){
		if(timeZone == null)
			return null;
		return timeZone.getID();
	}
	public void setTimeZone(String timeZone){
		if(LocalDate.isValidTimeZone(timeZone)){
			this.timeZone = TimeZone.getTimeZone(timeZone);
		}
	}	
	public int getFirstDayOfWeek() {
		return firstDayOfWeek;
	}
	public void setFirstDayOfWeek(int firstDayOfWeek) {
		this.firstDayOfWeek = firstDayOfWeek;
	}
	
	public int getDateOnly(){
		Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
		cal.setTime(new Date(this.getLocalDate()));
		return cal.get(Calendar.DATE);
	}

	public int getMonth(){
		Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
		cal.setTime(new Date(this.getLocalDate()));
		return cal.get(Calendar.MONTH);
	}

	public int getYear(){
		Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
		cal.setTime(new Date(this.getLocalDate()));
		return cal.get(Calendar.YEAR);		
	}
	
	public int getWeekOfMonth(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(this.getLocalDate()));
		cal.setFirstDayOfWeek(this.firstDayOfWeek);
		cal.setMinimalDaysInFirstWeek(7);
		return cal.get(Calendar.WEEK_OF_MONTH);		
	}
	public int getWeekOfMonth(int minimalDaysInFirstWeek){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(this.getLocalDate()));
		cal.setFirstDayOfWeek(this.firstDayOfWeek);
		cal.setMinimalDaysInFirstWeek(minimalDaysInFirstWeek);
		return cal.get(Calendar.WEEK_OF_MONTH);		
	}
	public int getWeekOfYear(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(this.getLocalDate()));
		cal.setFirstDayOfWeek(this.firstDayOfWeek);
		cal.setMinimalDaysInFirstWeek(7);
		return cal.get(Calendar.WEEK_OF_YEAR);		
	}
	
	public int getDayOfWeek(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(this.getLocalDate()));
		cal.setFirstDayOfWeek(this.firstDayOfWeek);
		cal.setMinimalDaysInFirstWeek(7);
		return cal.get(Calendar.DAY_OF_WEEK);		
	}
	
	public int getWeeksOfMonth(int minimalDaysInFirstWeek){
	    Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(this.getLocalDate()));
		cal.setFirstDayOfWeek(this.firstDayOfWeek);
		cal.setMinimalDaysInFirstWeek(minimalDaysInFirstWeek);

	    int ndays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    int weeks[] = new int[ndays];
	    for (int i = 0; i < ndays; i++)
	    {
	        weeks[i] = cal.get(Calendar.WEEK_OF_MONTH);
	        cal.add(Calendar.DATE, 1);
	    }
	    return weeks[ndays-1];
	}
	
	public int getWeeksOfMonth(){
	    Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(this.getLocalDate()));
		cal.setFirstDayOfWeek(this.firstDayOfWeek);
		cal.setMinimalDaysInFirstWeek(7);

	    int ndays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    int weeks[] = new int[ndays];
	    for (int i = 0; i < ndays; i++)
	    {
	        weeks[i] = cal.get(Calendar.WEEK_OF_MONTH);
	        cal.add(Calendar.DATE, 1);
	    }
	    return weeks[ndays-1];
	}
	
	public int getDaysOfMonth(int minimalDaysInFirstWeek){
	    Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(this.getLocalDate()));
		cal.setFirstDayOfWeek(this.firstDayOfWeek);
		cal.setMinimalDaysInFirstWeek(minimalDaysInFirstWeek);

	    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public int getDaysOfMonth(){
	    Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(this.getLocalDate()));
		cal.setFirstDayOfWeek(this.firstDayOfWeek);
		cal.setMinimalDaysInFirstWeek(7);

	    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public String toLocalString(){
		if(isToday()){
			return (new SimpleDateFormat("HH:mm")).format(getLocalTime());
		}else if(isThisYear()){
			return (new SimpleDateFormat("MM.dd HH:mm")).format(getLocalTime());
		}
		return (new SimpleDateFormat("yyyy.MM.dd HH:mm")).format(getLocalTime());			
	}

	public String toString(){
		if(isToday()){
			return (new SimpleDateFormat("HH:mm")).format(getTime());
		}else if(isThisYear()){
			return (new SimpleDateFormat("MM.dd HH:mm")).format(getTime());
		}
		return (new SimpleDateFormat("yyyy.MM.dd HH:mm")).format(getTime());			
	}

	public String toLocalDateString(){
		return DateFormat.getDateInstance(DateFormat.FULL, this.locale).format(getLocalTime());
	}
	
	public String toLocalMonthFullString(){
		return (new SimpleDateFormat("yyyy MMMM", this.locale)).format(getLocalTime());		
	}
	
	public String toLocalYearString(){
		return (new SimpleDateFormat("yyyy", this.locale)).format(getLocalTime());		
	}
	
	public String toLocalMonthOnlyString(){
		return (new SimpleDateFormat("MM", this.locale)).format(getLocalTime());		
	}
	
	public String toLocalDateOnlyString(){
		return (new SimpleDateFormat("dd", this.locale)).format(getLocalTime());		
	}
	
	public String toLocalMonthShortString(){
		return (new SimpleDateFormat("MMM", this.locale)).format(getLocalTime());		
	}
	
	public String toLocalDateShortString(){
		return (new SimpleDateFormat("MM.dd E", this.locale)).format(getLocalTime());
	}

	public String toLocalDateSimpleString(){
		return (new SimpleDateFormat("yyyy.MM.dd", this.locale)).format(getLocalTime());
	}

	public String toDateSimpleString(){
		return (new SimpleDateFormat("yyyy.MM.dd", this.locale)).format(getTime());
	}

	public String toLocalDateSimple2String(){
		return (new SimpleDateFormat("yyyy-MM-dd", this.locale)).format(getLocalTime());
	}

	public String toLocalDateLongString(){
		return (new SimpleDateFormat("yyyy.MM.dd E", this.locale)).format(getLocalTime());
	}

	public String toLocalMonthString(){
		return (new SimpleDateFormat("yyyy.MM", this.locale)).format(getLocalTime());
	}

	public String toLocalDateTimeSimpleString(){
		return (new SimpleDateFormat("yyyy.MM.dd HH:mm", this.locale)).format(getLocalTime());
	}

	public String toDateTimeSimpleString(){
		return (new SimpleDateFormat("yyyy.MM.dd HH:mm", this.locale)).format(getTime());
	}

	public String toLocalDateValue(){
		return (new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", this.locale)).format(getLocalTime());
	}

	public String toDateValue(){
		return (new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", this.locale)).format(getTime());
	}

	public String toLocalDateString2(){
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", this.locale)).format(getLocalTime());
	}

	public String toLocalTimeString2(){
		return (new SimpleDateFormat("HH:mm", this.locale)).format(getLocalTime());
	}

	public String toLocalDayString(){
		return (new SimpleDateFormat("E", this.locale)).format(getLocalTime());
	}

	public String toGMTDateString(){
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(getGMTDate());
	}
	public String toGMTDateString2(){
		return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")).format(getGMTDate());
	}
	public String toGMTSimpleDateString(){
		return (new SimpleDateFormat("yyyy-MM-dd")).format(getGMTDate());
	}

	public String toGMTSimpleDateString2(){
		return (new SimpleDateFormat("yyyy.MM.dd")).format(getGMTDate());
	}

	public String toGMTTimeString(){
		return (new SimpleDateFormat("HH:mm:ss")).format(getGMTDate());
	}

	public String toGMTTimeString2(){
		return (new SimpleDateFormat("HH:mm")).format(getGMTDate());
	}

	public String toLocalTimeString(){
		return DateFormat.getTimeInstance(DateFormat.MEDIUM, this.locale).format(getLocalTime());
	}

	public String toLocalTimeSimpleString(){
		return (new SimpleDateFormat("HH:mm:ss")).format(getLocalTime());
	}

	public String toLocalTimeShortString(){
		return (new SimpleDateFormat("HH:mm")).format(getLocalTime());
	}

	public String toTimeShortString(){
		return (new SimpleDateFormat("HH:mm")).format(getTime());
	}

	public void plusToGMTTime(long timeValue){
		this.setTime(this.getTime() + timeValue);
	}

	public boolean isSameDate(LocalDate when){
		if((this).toLocalDateSimple2String().equals(when.toLocalDateSimple2String())) return true;
//		if( getLocalDateOnly(this).getTime() == getLocalDateOnly(when).getTime()) return true;
		return false;
	}

	public boolean isBeforeDate(LocalDate when){
		if( getLocalDateOnly(this).getTime() > getLocalDateOnly(when).getTime()) return true;
		
		return false;
	}

	public boolean isAfterDate(LocalDate when){
		if(getLocalDateOnly(this).getTime() < getLocalDateOnly(when).getTime()) return true;
		return false;
	}
	
	public static long getDiffDate(Date fromDate, Date toDate){
		if(fromDate==null || toDate==null) return 0;
		return (toDate.getTime() - fromDate.getTime())/LocalDate.ONE_DAY;		
	}
	
	public long getLocalTime(){
		if(this.timeZone == null){
			return super.getTime() + TimeZone.getDefault().getRawOffset();
		}else{
			return super.getTime() + this.timeZone.getRawOffset();
		}
	}
	
	public boolean isNew(){
		if(this.getTime() >= ((new LocalDate()).getTime() - 2*LocalDate.ONE_DAY))
			return true;
		return false;		
	}
	
	public static boolean isValidTimeZone(String timeZone){
		String[] zoneIds = TimeZone.getAvailableIDs();
		for(String str : zoneIds)
			if(str.equals(timeZone)) return true;
		return false;
		
	}	
	
	public static String convertTimeToString(long time){
		return (new SimpleDateFormat("HH:mm")).format(time - TimeZone.getDefault().getRawOffset());
	}
	public static Date convertLocalToGMT(long localDate, String timeZone){
		if(isValidTimeZone(timeZone))
			return new Date(localDate - TimeZone.getTimeZone(timeZone).getRawOffset());
		return new Date(localDate);
	}
	public static Date convertGMTToLocal(long GMTDate, String timeZone){
		if(isValidTimeZone(timeZone))
			return new Date(GMTDate + TimeZone.getTimeZone(timeZone).getRawOffset());
		return new Date(GMTDate);
	}

	public static Date convertStringToDate(String yyyyMMddHHmm) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMddHHmm) || yyyyMMddHHmm.length()!=12) return null;
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		return df.parse(yyyyMMddHHmm);					
	}

	public static Date convertStringToDateTime(String yyyyMMddHHmm) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMddHHmm) || yyyyMMddHHmm.length()!=14) return null;
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.parse(yyyyMMddHHmm);					
	}

	public static Date convertTimeStringToDate(String HHmm) throws Exception{
		if(SmartUtil.isBlankObject(HHmm)) return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.parse("1900-01-01 " + HHmm + ":00");
	}

	public static LocalDate convertLocalStringToLocalDate(String yyyyMMddHHmmssSSS) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMddHHmmssSSS) || yyyyMMddHHmmssSSS.length() < 21) return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return new LocalDate((df.parse(yyyyMMddHHmmssSSS)).getTime() - TimeZone.getTimeZone(SmartUtil.getCurrentUser().getTimeZone()).getRawOffset());					
	}

	public static LocalDate convertLocalDateTimeStringToLocalDate(String yyyyMMddHHmm) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMddHHmm) || yyyyMMddHHmm.length()!=16) return null;
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		return new LocalDate((df.parse(yyyyMMddHHmm)).getTime() - TimeZone.getTimeZone(SmartUtil.getCurrentUser().getTimeZone()).getRawOffset());					
	}

	public static LocalDate convertLocalDateStringToLocalDate2(String yyyyMMdd) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMdd) || yyyyMMdd.length()!=10) return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return new LocalDate((df.parse(yyyyMMdd)).getTime() - TimeZone.getTimeZone(SmartUtil.getCurrentUser().getTimeZone()).getRawOffset());					
	}

	public static LocalDate convertLocalDateStringToLocalDate(String yyyyMMdd) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMdd) || yyyyMMdd.length()!=10) return null;
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		return new LocalDate((df.parse(yyyyMMdd)).getTime() - TimeZone.getTimeZone(SmartUtil.getCurrentUser().getTimeZone()).getRawOffset());					
	}

	public static LocalDate convertLocalYearStringToLocalDate(String yyyy) throws Exception{
		if(SmartUtil.isBlankObject(yyyy) || yyyy.length()!=4) return null;
		DateFormat df = new SimpleDateFormat("yyyy");
		return new LocalDate((df.parse(yyyy)).getTime() - TimeZone.getTimeZone(SmartUtil.getCurrentUser().getTimeZone()).getRawOffset());					
	}

	public static LocalDate convertLocalTimeStringToLocalDate(String HHmm) throws Exception{
		if(SmartUtil.isBlankObject(HHmm) || HHmm.length()!=5) return null;
		DateFormat df = new SimpleDateFormat("HH:mm");
		return new LocalDate((df.parse(HHmm)).getTime() - TimeZone.getTimeZone(SmartUtil.getCurrentUser().getTimeZone()).getRawOffset());					
	}

	public static LocalDate convertGMTStringToLocalDate(String yyyyMMddHHmmssSSS) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMddHHmmssSSS) || yyyyMMddHHmmssSSS.length() < 21) return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return new LocalDate((df.parse(yyyyMMddHHmmssSSS)).getTime() + TimeZone.getTimeZone(SmartUtil.getCurrentUser().getTimeZone()).getRawOffset());
	}

	public static LocalDate convertGMTStringToLocalDate2(String yyyyMMddHHmmssSSS) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMddHHmmssSSS) || yyyyMMddHHmmssSSS.length() < 21) return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return new LocalDate((df.parse(yyyyMMddHHmmssSSS)).getTime());
	}

	public static Date convertGMTStringToDate(String yyyyMMddHHmmssSSS) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMddHHmmssSSS) || yyyyMMddHHmmssSSS.length() < 21) return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return new Date((df.parse(yyyyMMddHHmmssSSS)).getTime());
	}

	public static LocalDate convertGMTSimpleStringToLocalDate(String yyyyMMdd) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMdd) || yyyyMMdd.length()!=10) return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return new LocalDate((df.parse(yyyyMMdd)).getTime());					
	}
	public static LocalDate convertGMTSimple2StringToLocalDate(String yyyyMMdd) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMdd) || yyyyMMdd.length()!=10) return null;
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		return new LocalDate((df.parse(yyyyMMdd)).getTime());					
	}
	public static LocalDate convertGMTTimeStringToLocalDate(String HHmmss) throws Exception{
		if(SmartUtil.isBlankObject(HHmmss) || HHmmss.length()!=8) return null;
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		return new LocalDate((df.parse(HHmmss)).getTime());
	}

	public static LocalDate convertGMTTimeStringToLocalDate2(String HHmm) throws Exception{
		if(SmartUtil.isBlankObject(HHmm) || HHmm.length()!=5) return null;
		DateFormat df = new SimpleDateFormat("HH:mm");
		return new LocalDate((df.parse(HHmm)).getTime());
	}

	public static LocalDate convertLocalString2ToLocalDate(String yyyyMMddHHmmssSS) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMddHHmmssSS) || yyyyMMddHHmmssSS.length()!=19) return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return new LocalDate((df.parse(yyyyMMddHHmmssSS)).getTime() - TimeZone.getTimeZone(SmartUtil.getCurrentUser().getTimeZone()).getRawOffset());					
	}

	public static LocalDate convertStringToLocalDate(String yyyyMMddHHmm) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMMddHHmm) || yyyyMMddHHmm.length()!=16) return null;
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		return new LocalDate((df.parse(yyyyMMddHHmm)).getTime() - TimeZone.getTimeZone(SmartUtil.getCurrentUser().getTimeZone()).getRawOffset());
	}

	public static LocalDate convertLocalMonthStringToLocalDate(String yyyyMM) throws Exception{
		if(SmartUtil.isBlankObject(yyyyMM) || (yyyyMM.length()!=7 && yyyyMM.length()!=10)) return null;
		DateFormat df = new SimpleDateFormat("yyyy.MM");
		if(yyyyMM.length() == 10) df = new SimpleDateFormat("yyyy.MM.dd"); 
		return new LocalDate((df.parse(yyyyMM)).getTime() - TimeZone.getTimeZone(SmartUtil.getCurrentUser().getTimeZone()).getRawOffset());
	}

	public static LocalDate convertLocalMonthWithDiffMonth(LocalDate localDate, int diffMonth) throws Exception{
		int toMonth = localDate.getMonth() + diffMonth;
		int toYear = localDate.getYear();
		while(toMonth < 0 || toMonth > 11){
			if(toMonth < 0){
				toYear--;
				toMonth = toMonth+12;
			}else{
				toYear++;
				toMonth = toMonth-12;
			}
		}
		return convertLocalMonthStringToLocalDate(String.format("%04d", toYear) + "." + String.format("%02d", toMonth+1));
	}
	
	public static LocalDate convertLocalDateWithDiffMonth(LocalDate localDate, int diffMonth) throws Exception{
		if(localDate==null) return null;
		String ddhhmm = localDate.toLocalDateTimeSimpleString().substring(8, 16);
		int toMonth = localDate.getMonth() + diffMonth;
		int toYear = localDate.getYear();
		while(toMonth < 0 || toMonth > 11){
			if(toMonth < 0){
				toYear--;
				toMonth = toMonth+12;
			}else{
				toYear++;
				toMonth = toMonth-12;
			}
		}
		try{
			LocalDate toLocalDate = convertLocalDateTimeStringToLocalDate(String.format("%04d", toYear) + "." + String.format("%02d", toMonth+1) + "." + ddhhmm);
			return toLocalDate;
		}catch (Exception e){
		}
		return null;
	}

	public static LocalDate convertLocalDateWithDiffMonth(LocalDate localDate, int diffMonth, int weekOfMonth, int dayOfWeek) throws Exception{
		if(localDate==null) return null;
		String hhmm = localDate.toLocalDateTimeSimpleString().substring(11, 16);
		int toMonth = localDate.getMonth() + diffMonth;
		int toYear = localDate.getYear();
		while(toMonth < 0 || toMonth > 11){
			if(toMonth < 0){
				toYear--;
				toMonth = toMonth+12;
			}else{
				toYear++;
				toMonth = toMonth-12;
			}
		}
		
		LocalDate toLocalMonth = convertLocalMonthStringToLocalDate(String.format("%04d", toYear) + "." + String.format("%02d", toMonth+1));
		
		int toDate = 0;
		boolean isLastWeek = false;
		int dayDiff =  (dayOfWeek+1) - toLocalMonth.getDayOfWeek();
		if(weekOfMonth<=10){
			if(weekOfMonth==6){
				weekOfMonth = toLocalMonth.getWeeksOfMonth(1);
				isLastWeek = true;
			}
			if(dayOfWeek+1<toLocalMonth.firstDayOfWeek && toLocalMonth.firstDayOfWeek <= toLocalMonth.getDayOfWeek()){
				dayDiff = dayDiff+7;
			}
			toDate = (weekOfMonth - toLocalMonth.getWeekOfMonth(1))*7 + dayDiff;
		}else{
			weekOfMonth = weekOfMonth-10;
			if(weekOfMonth==6){
				weekOfMonth = toLocalMonth.getWeeksOfMonth(1);
				isLastWeek = true;
			}
			toDate = (weekOfMonth - toLocalMonth.getWeekOfMonth(1))*7 + (dayDiff<0 ? 7+dayDiff : dayDiff);
			if(isLastWeek && toDate+1>toLocalMonth.getDaysOfMonth()) toDate = toDate-7;
		}
		try{
			LocalDate toLocalDate = convertLocalDateTimeStringToLocalDate(String.format("%04d", toYear) + "." + String.format("%02d", toMonth+1) + "." +  String.format("%02d", toDate+1) + " " + hhmm);
			return toLocalDate;
		}catch (Exception e){
		}
		return null;
	}

	public static long convertStringToTime(String yyyyMMddHHmm) throws Exception{
		return convertStringToDate(yyyyMMddHHmm).getTime();					
	}

	public static long convertTimeStringToTime(String HHmm) throws Exception{
		if(SmartUtil.isBlankObject(HHmm) || HHmm.length()!=5) return 0;
		DateFormat df = new SimpleDateFormat("HH:mm");
		return df.parse(HHmm).getTime();					
	}

	public static KeyMap[] getAvailableTimeZoneNames(String locale) throws Exception{
		String[] timeZoneIds = TimeZone.getAvailableIDs();
		KeyMap[] timeZoneNames = new KeyMap[timeZoneIds.length];
		for(int i=0; i<timeZoneIds.length; i++){
			timeZoneNames[i] = new KeyMap(timeZoneIds[i], TimeZone.getTimeZone(timeZoneIds[i]).getDisplayName(new Locale(locale)));
		}
		return timeZoneNames;
	}

	public static String getDayLocalString(int day){
		if(day< Calendar.SUNDAY || day > Calendar.SATURDAY) return "";
		return SmartMessage.getString("calendar.title.day." + day);
	}

	private boolean isToday(){
		if((new LocalDate()).toLocalDateSimple2String().equals(this.toLocalDateSimple2String()))
//		if(getLocalDateOnly(this).getTime() == getLocalDateOnly(new LocalDate()).getTime())
			return true;
		return false;
	}

	private boolean isThisYear(){
		if(getLocalYearOnly(this).getTime() == getLocalYearOnly(new LocalDate()).getTime())
			return true;
		return false;
	}

	private LocalDate getLocalDateOnly(LocalDate localDate){
		LocalDate lDate = null;
		try{
			lDate =  LocalDate.convertLocalDateStringToLocalDate(localDate.toLocalDateSimpleString());
		}catch (Exception e){
		}
		return lDate;
	}

	private LocalDate getLocalYearOnly(LocalDate localDate){
		LocalDate lDate = null;
		try{
			lDate =  LocalDate.convertLocalYearStringToLocalDate(localDate.toLocalYearString());
		}catch (Exception e){
		}
		return lDate;

	}
}
