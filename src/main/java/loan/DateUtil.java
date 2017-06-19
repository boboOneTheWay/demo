package loan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * @author 
 *
 */
public class DateUtil {
	private static DateFormat df4=new SimpleDateFormat("MMdd");
	private static DateFormat df6=new SimpleDateFormat("yyyyMM");
	private static DateFormat df8=new SimpleDateFormat("yyyyMMdd");
	private static DateFormat df10=new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat df14=new SimpleDateFormat("yyyyMMddHHmmss");

    /*
     * 获得和给定日期sDate相差Days天的日期yyyy/MM/dd
     */
    public static String getRelativeDate(String date, String termUnit, int step) throws Exception {
        if (step == 0)
            return date;
        if (termUnit == null)
            throw new Exception("未传入期限单位不正确！");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        cal.setTime(formatter.parse(date));
        if (termUnit.equals(AcctConstants.TERM_UNIT_DAY)) {
            cal.add(Calendar.DATE, step);
        } else if (termUnit.equals(AcctConstants.TERM_UNIT_MONTH)) {
            cal.add(Calendar.MONTH, step);
        } else if (termUnit.equals(AcctConstants.TERM_UNIT_YEAR)) {
            cal.add(Calendar.YEAR, step);
        } else
            throw new Exception("未传入期限单位不正确！");

        return formatter.format(cal.getTime());
    }

    /*
     * 获得和给定日期sDate相差Days天的日期yyyy/MM/dd  针对平安，这是到期日的计算方法，不会大于28日
     */
    public static String getMaturityDate(String date, String termUnit, int step) throws Exception {
        String maturityDate = getRelativeDate(date, termUnit, step);
        return maturityDate;
    }

    public static boolean monthEnd(String date) throws ParseException {
        if (date.equals(getEndDateOfMonth(date)))
            return true;
        return false;
    }
    
    
    /** 
     * @Title: isLastDayOfMonth 
     * @Description: 判断当天是否是月末
     * @param date
     * @return
     * @author l.l
     * 2016年11月28日
     */
    public static boolean isLastDayOfMonth(Date date) { 
    	
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(date); 
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1)); 
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) { 
            return true; 
        } 
        return false; 
    } 
    

    //得到月底
    public static String getEndDateOfMonth(String curDate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        cal.setTime(formatter.parse(curDate));
        int maxDays = cal.getActualMaximum(Calendar.DATE);//得到该月的最大天数
        cal.set(Calendar.DATE, maxDays);
        return formatter.format(cal.getTime());
    }
    
    
    
	/**
	 * 获取每月的最后一天的日期字符串，以“yyyy-MM-dd”字符串的格式返回。
	 * @param date 传入的当天的Date类型的参数
	 * @return
	 */
	public static String getEndDateStrOfMonth(Date date) {
		Calendar end = Calendar.getInstance();
		end.setTime(date);
		end.set(Calendar.DAY_OF_MONTH,end.getActualMaximum(Calendar.DAY_OF_MONTH));						
		return new SimpleDateFormat("yyyy-MM-dd").format(end.getTime());
	}	
    

    public static Date getDate(String curDate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        cal.setTime(formatter.parse(curDate));

        return cal.getTime();
    }
    /**
     * 
    * @Title: StringToDateParameter 
    * @Description: 指定格式将字符串转换成日期
    * @param curDate
    * @param Parameter
    * @return
    * @throws ParseException
     */
    public static Date StringToDateParameter(String curDate,String Parameter) throws ParseException{
        return new SimpleDateFormat(Parameter).parse(curDate);
    }
    /**
     * 
    * @Title: StringToDate 
    * @Description: 将字符串转成日期 格式：yyyy/MM/dd
    * @param curDate
    * @return
    * @throws ParseException
     */
    public static Date StringToDate(String curDate) throws ParseException{
        return new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
    }
    
    /**
     * 
    * @Title: dateToString 
    * @Description: 将日期转城字符串  日期格式：yyyy-MM-dd
    * @param curDate
    * @return
    * @throws ParseException
     */
    public static String dateToString(Date curDate) throws ParseException{
       return new SimpleDateFormat("yyyy-MM-dd").format(curDate);
    }
    
    /**
     * @param date  yyyy/MM/dd
     * @param type  1 返回数字  2 返回中文 3 返回英文
     * 返回星期  
     * 1 星期一 、2 星期二 、3星期三、4 星期四、5 星期五、6 星期六、7 星期日
    */
    public static String getWeekDay(String date, String type) throws ParseException {
        String[] sWeekDates = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        String[] sWeekDatesE = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
                "Saturday" };
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        cal.setTime(formatter.parse(date));
        if (type.equals("2"))
            return sWeekDates[cal.get(Calendar.DAY_OF_WEEK) - 1];
        else if (type.equals("3"))
            return sWeekDatesE[cal.get(Calendar.DAY_OF_WEEK) - 1];
        else
            return String.valueOf(cal.get(Calendar.DAY_OF_WEEK) - 1);
    }

    /**
     * @param date  yyyy/MM/dd
     * @return  yyyy/MM/dd
     * 返回某个日期对应周的所有日期
    */
    public static String[] getWeekDates(String date) throws ParseException {
        String[] sWeekDates = { "", "", "", "", "", "", "" };
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        cal.setTime(formatter.parse(date));
        //星期日
        cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 6);
        sWeekDates[0] = formatter.format(cal.getTime());
        //星期一
        cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 5);
        sWeekDates[1] = formatter.format(cal.getTime());
        //星期二
        cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 4);
        sWeekDates[2] = formatter.format(cal.getTime());
        //星期三
        cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 3);
        sWeekDates[3] = formatter.format(cal.getTime());
        //星期四
        cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 2);
        sWeekDates[4] = formatter.format(cal.getTime());
        //星期五
        cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 1);
        sWeekDates[5] = formatter.format(cal.getTime());
        //星期六
        cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));
        sWeekDates[6] = formatter.format(cal.getTime());
        return sWeekDates;
    }

    /**
     * @param beginDate
     * @param endDate
     * @return
     * @throws ParseException
     * 获取两个日期之间的天数
     */
    public static int getDays(String sBeginDate, String sEndDate) {
        Date startDate = java.sql.Date.valueOf(sBeginDate.replace('/', '-'));
        Date endDate = java.sql.Date.valueOf(sEndDate.replace('/', '-'));

        int iDays = (int) ((endDate.getTime() - startDate.getTime()) / 86400000L);
        return iDays;
    }

    /**获取两个日期之间的月数，向上取整
     * @param beginDate
     * @param endDate
     * @return
     * @throws ParseException 
     * @throws ParseException
     * 
     */
    public static int getMonths(String beginDate1, String endDate1) throws ParseException {
        Date beginDate = getDate(beginDate1);
        Date endDate = getDate(endDate1);
        Calendar former = Calendar.getInstance();
        Calendar latter = Calendar.getInstance();
        former.clear();
        latter.clear();
        boolean positive = true;
        if (beginDate.after(endDate)) {
            former.setTime(endDate);
            latter.setTime(beginDate);
            positive = false;
        } else {
            former.setTime(beginDate);
            latter.setTime(endDate);
        }

        int monthCounter = 0;
        while (former.get(Calendar.YEAR) != latter.get(Calendar.YEAR)
               || former.get(Calendar.MONTH) != latter.get(Calendar.MONTH)) {
            former.add(Calendar.MONTH, 1);
            monthCounter++;
        }

        if (former.get(Calendar.DATE) < latter.get(Calendar.DATE)) {
            monthCounter++;
        }

        if (positive)
            return monthCounter;
        else
            return -monthCounter;
    }

    /*
     * 得到当前的时间 返回值:HH:MM:SS
    */
    public static String getDateTime(Date date, String format) {
        SimpleDateFormat sdfTempDate = new SimpleDateFormat(format);
        String prev = sdfTempDate.format(date);
        return prev;
    }

    /*
     * 得到当前的日期 返回值:YYYYMMDD
    */
    public static String getStringDate(String date) throws ParseException {
        return date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
    }

    /**
     * @param time1
     * @param time2
     * @return time1在time2之前返回true,时间精确到秒
     * @throws ParseException 
     */
    public static boolean compareTime(String time1, String time2) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date former = formatter.parse(time1);
        Date after = formatter.parse(time2);
        return former.before(after);
    }

    /*
     *获取制定日期N月后日期 
    */
    public static String getAfterMonth(String sDate, int month) {
        Calendar c = Calendar.getInstance();//获得一个日历的实例   
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(sDate);//初始日期   
        } catch (Exception e) {

        }
        c.setTime(date);//设置日历时间   
        c.add(Calendar.MONTH, month);//在日历的月份上增加n个月   
        String strDate = sdf.format(c.getTime());//的到你想要得N个月后的日期   
        return strDate;

    }
    
    /**
     * 得到系统的当前日期（默认格式是YYYYMMDD）
     */
    public static String getCurrDate() {
    	Date date = new Date();
        SimpleDateFormat sdfTempDate = new SimpleDateFormat("yyyyMMdd");
        String prev = sdfTempDate.format(date);
        return prev;
    }
   /**
    * 得到系统的当前时间（默认格式是yyyy-MM-dd HH:mm:ss） 
    */
    public static String getCurrTime() {
    	Date date = new Date();
        SimpleDateFormat sdfTempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String prev = sdfTempDate.format(date);
        return prev;
    }
    
    /*
     *获取制定日期N天后日期 
    */
    public static String getAfterDays(String sDate, int day) {
        Calendar c = Calendar.getInstance();//获得一个日历的实例   
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(sDate);//初始日期   
        } catch (Exception e) {

        }
        c.setTime(date);//设置日历时间   
        c.add(Calendar.DAY_OF_MONTH, day);
        String strDate = sdf.format(c.getTime());//的到你想要得N天后的日期   
        return strDate;
    }
    
    /** 
     * @Title: getNextDays 
     * @Description: 获取制定日期N天后日期 
     * @param date
     * @return date
     * @author lin.l
     * 2016年8月17日
     */
    public static Date getNextDays(Date date,int day) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, day);  
        date = calendar.getTime();  
        return date;  
    }  
    
     
    /**
     * @Title: format8 
     * @Description: 将Date类型的日期转换成8位日期
     * @param date
     * @return
     * @author zgq
     */
	public static String format8(Date date){
		if(Optional.ofNullable(date).isPresent()){
			return df8.format(date);
		}
		return null;
	}
	
	/**
     * @Title: format4 
     * @Description: 将Date类型的日期转换成4位日期
     * @param date
     * @return
     * @author zgq
     */
	public static String format4(Date date){
		if(Optional.ofNullable(date).isPresent()){
			return df4.format(date);
		}
		return null;
	}
	/**
	 * @Title: format14 
	 * @Description: 将Date类型的日期转换成14位日期
	 * @param date
	 * @return
	 * @author zgq
	 */
	public static String format14(Date date){
		if(Optional.ofNullable(date).isPresent()){
			return df14.format(date);
		}
		return null;
	}

	/**
	 * @Title: format6
	 * @Description: 将Date类型的日期转换成6位日期
	 * @param date
	 * @return
	 */
	public static String format6(Date date){
		if(Optional.ofNullable(date).isPresent()){
			return df6.format(date);
		}
		return null;
	}
	
	/**
	 * @Title: format10
	 * @Description: 将Date类型的日期转换成10位日期 yyyy-MM-DD
	 * @param date
	 * @return
	 */
	public static String format10(Date date){
		if(Optional.ofNullable(date).isPresent()){
			return df10.format(date);
		}
		return null;
	}
	
	
	/**
	 * @Title: isDate10
	 * @Description: 判断输入的字符串日期格式是否是有效日期
	 * @param date
	 *            格式为yyyy-MM-dd的日期
	 * @return
	 * @author zgq
	 */
	public static boolean isDate10(String date) {
		boolean bRet = true;
		try {
			DateUtil.StringToDateParameter(date, "yyyy-MM-dd");
		} catch (Exception ex) {
			bRet = false;
		}
		return bRet;
	}

	/**
	 * @Title: isDayBetween1And28
	 * @Description: 判断传入的day是否在1-28之间
	 * @param day
	 * @return
	 * @author zgq
	 */
	public static boolean isDayBetween1And28(String day) {
		boolean bRet = true;
		try {
			int d = Integer.parseInt(day);
			if (d < 1 || d > 28) {
				bRet = false;
			}
		} catch (Exception ex) {
			bRet = false;
		}
		return bRet;
	}
	
	
	/**
	 * 获取日期信息
	 * 
	 * @param date
	 * @return
	 * @throws BusinessException
	 */
	public static Map<String, String> getDateInfo(Date date) {
		if (date == null) {
		}
		// 初始化日历
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// 获取日期信息
		Map<String, String> map = new HashMap<String, String>();
		map.put("day", String.valueOf(calendar.get(Calendar.DATE)));
		map.put("year", String.valueOf(calendar.get(Calendar.YEAR)));
		map.put("month", String.valueOf(calendar.get(Calendar.MONTH) + 1));
		return map;
	}
	
	/**
	 * 判断是否是年末
	 * 
	 * @param date
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isYearEnd(Date date) {
		boolean isEnd = false;
		Map<String, String> map = getDateInfo(date);
		// 判断该天是否是12月的最后一天
		if ("12".equals(map.get("month")) && isLastDayOfMonth(date)) {
			isEnd = true;
		}
		return isEnd;
	}
	
	/** 
	 * @Title: dayOfYear 
	 * @Description: 获取今天是今年的第几天
	 * @param date
	 * @return
	 * @author l.l
	 * 2016年12月28日
	 */
	public static int dayOfYear(Date date){
		Calendar ca=Calendar.getInstance();
		ca.setTime(date);
		int i=ca.get(Calendar.DAY_OF_YEAR);  
		return i;
	}
	
	/** 
	 * @Title: dayOfMonth 
	 * @Description: 获取上个月的最后一天
	 * @return
	 * @author l.l
	 * 2016年12月29日
	 */
	public static Date dayOfEndLastMonth(Date workDate){
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar cale = Calendar.getInstance(); 
		cale.setTime(workDate);
        cale.set(Calendar.DAY_OF_MONTH,0);//
        Date lastDay = cale.getTime();
		return lastDay;
	}
	
	
	
	/**
	 * 
	 * @Title: getLastYearEnd 
	 * @Description: 获取上年年底日期,yyyy-MM-dd
	 * @param workDate
	 * @return
	 * @throws BusinessException
	 * @author wfr
	 * 2017年2月10日
	 */
	public static String getLastYearEnd(Date workDate) {
		Map<String, String> mapParam = DateUtil.getDateInfo(workDate);
		String lastYear = String.valueOf(Integer.parseInt(mapParam.get("year"))-1);
		String lastYearEnd = lastYear+"-12-31";
		return lastYearEnd;
	}
	
	/**
	 * 
	 * @Title: getLastYearEnd2 
	 * @Description: 获取上年年底日期,yyyyMMdd
	 * @param workDate
	 * @return
	 * @throws BusinessException
	 * @author wfr
	 * 2017年2月14日
	 */
	public static String getLastYearEnd2(Date workDate) {
		Map<String, String> mapParam = DateUtil.getDateInfo(workDate);
		String lastYear = String.valueOf(Integer.parseInt(mapParam.get("year"))-1);
		String lastYearEnd = lastYear+"1231";
		return lastYearEnd;
	}
}
