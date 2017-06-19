package loan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimeZone;


/**
 * <p>
 * Title:核心计息公共工具类
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: 
 * </p>
 * Service
 * 
 * @author gaowg@jbbis.com.cn
 * @version 1.0
 * @Date 2008-1-16 下午04:40:46
 */
public class ClsPublic {

	public static TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");

	public ClsPublic() {

	}

	/**
	 * 还款计划的数据结构
	 * 
	 * @author admin
	 *
	 */
	public class RetuPlan {

		public int sTerm;
		public Date sDate;
		public double saCapiInte;
		public double saCapi;
		public double saInte;
		public double abal;

	}

	/**
	 * 计算:按揭额度,传递的参数是贷款期限tterm,月供amt,利率rate
	 * 
	 * @param tterm
	 * @param amt
	 * @param rate
	 * @return
	 */
	public static double CacuLoanAmt(int tterm, double amt, double rate) {

		double LoanAmt = 0;

		// 目前是等额本息按月还款
		LoanAmt = amt / (1 + 1 / (Math.pow((1 + rate / 1000), tterm) - 1))
				/ rate * 1000;

		return LoanAmt;
	}

	/**
	 * 计算:贷款期限,传递的参数是贷款金额abal,月供amt,利率rate
	 * 
	 * @param abal
	 * @param amt
	 * @param rate
	 * @return
	 */
	public static int CacuLoanTerm(double abal, double amt, double rate) {

		int LoanTerm = (int) (Math.log(amt / (amt - abal * rate / 1000)) / Math.log(1 + rate / 1000));

		return LoanTerm;
	}

	/**
	 * 探测 datInteEndDate 所在的月份 是否在 strSpeMonth 中间出现过
	 * 
	 * @param datInteEndDate
	 * @param strSpeMonth
	 * @return
	 */
	public static boolean IsSpeMonth(Date datInteEndDate, String strSpeMonth) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(datInteEndDate.getTime());

		String strMonth = "," + (cal.get(Calendar.MONTH) + 1) + ",";
		strSpeMonth = "," + strSpeMonth + ",";

		if (strSpeMonth.indexOf(strMonth) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据本期应还款日期判断，从分期还款额、特殊还款额1、特殊还款额2中取得本期应使用的金额
	 * 
	 * @param datInteEndDate
	 * @param dblAmt
	 * @param strSpeMonth1
	 * @param strSpeMonth2
	 * @param dblSpeAmt1
	 * @param dblSpeAmt2
	 * @return
	 */
	public static double getThisTermAmt(Date datInteEndDate, double dblAmt,
			String strSpeMonth1, String strSpeMonth2, double dblSpeAmt1, double dblSpeAmt2) {

		double temp = 0;
		// 检查当期还款日期是否在特殊还款月份内
		if (IsSpeMonth(datInteEndDate, strSpeMonth1)) {
			temp = dblSpeAmt1;
		} else if (IsSpeMonth(datInteEndDate, strSpeMonth2)) {
			temp = dblSpeAmt2;
		} else {
			temp = dblAmt;
		}

		return temp;
	}

	/**
	 * 专用于按季计息还款法，计算上次还款日期
	 * 
	 * @param ThissDate
	 * @param RetuDay
	 * @return
	 */
	public static Date getPreSeasonsDate(Date ThissDate, int RetuDay) {

		Calendar c = Calendar.getInstance();
		int DayOfThissDate = ClsPublic.getSomeDate(ThissDate, "d");
		Date PreSeasonsDate = ThissDate;
		int pdMonth = ClsPublic.getSomeDate(PreSeasonsDate, "m");

		// 如果当前应还款日是3\6\9\12月
		if (pdMonth == 3 || pdMonth == 6 || pdMonth == 9 || pdMonth == 12) {

			if (DayOfThissDate > RetuDay) { // 如果当前应还款日的“日”大于“还款日”
				c.setTime(ThissDate);
				c.set(Calendar.DAY_OF_MONTH, RetuDay);
				PreSeasonsDate = new Date(c.getTimeInMillis());
			} else {
				PreSeasonsDate = ClsPublic.DateAdd("m", -1, PreSeasonsDate, RetuDay); // 为了下面的循环，先滚一期
			}
		}

		// 如果当前应还款日不是3\6\9\12月，则向前滚到第一个3\6\9\12月
		int tMonth = ClsPublic.getSomeDate(PreSeasonsDate, "m");
		while (tMonth != 3 || tMonth != 6 || tMonth != 9 || tMonth != 12) {

			PreSeasonsDate = ClsPublic.DateAdd("m", -1, PreSeasonsDate, RetuDay);
			tMonth = ClsPublic.getSomeDate(PreSeasonsDate, "m");
		}

		return PreSeasonsDate;
	}

	/**
	 * 专用于按月计息还款法，计算上次还款日期
	 * 
	 * @param ThissDate
	 * @param RetuDay
	 * @return
	 */
	public static Date getPreMonthsDate(Date ThissDate, int RetuDay) {

		Calendar c = Calendar.getInstance();
		int DayOfThissDate = ClsPublic.getSomeDate(ThissDate, "d");
		Date PreMonthsDate = ThissDate;

		if (DayOfThissDate > RetuDay) { // 如果当前应还款日的“日”大于“还款日”
			c.setTime(ThissDate);
			c.set(Calendar.DAY_OF_MONTH, RetuDay);
			PreMonthsDate = new Date(c.getTimeInMillis());
		} else {
			PreMonthsDate = ClsPublic.DateAdd("m", -1, PreMonthsDate, RetuDay);
		}
		return PreMonthsDate;
	}

	/**
	 * 计算下次还款日期 <br>
	 * <b>由cn.com.jbbis.common.cls.ClsCompute.getNextRequital替代</b></br>
	 * 
	 * @param datReleDate
	 * @param datLastDate
	 * @param intRetuPeri
	 * @param intRetuKind
	 * @param strDueDate
	 * @param intNextSeason
	 * @param RetuDay
	 * @param boNatrual
	 * @return
	 * @deprecated
	 */
	public static String GetNextDate(Date datReleDate, Date datLastDate,
			int intRetuPeri, int intRetuKind, String strDueDate,
			int intNextSeason, int RetuDay, int boNatrual) {

		int i;
		int LAST_MON_OF_FIRST_SEASON = 3;
		Date datTemp;

		Calendar cal = Calendar.getInstance();
		cal.setTime(datLastDate);
		// 扣款日都保存在贷款分户帐中还款日RetuDay
		switch (intRetuPeri) {
		case 1: // 按月
			return ClsPublic.formatDate(DateAdd("m", 1, datLastDate, RetuDay));
		case 2: // 按季度
			if ((boNatrual == 1) || (intRetuKind == 10)) { // 使用自然季
				if (intNextSeason == 1) {

					// 增加对次季还款的支持，默认为本季还
					i = LAST_MON_OF_FIRST_SEASON;
					do {
						i = i + 3;
					} while (i - 3 >= cal.get(Calendar.MONTH) + 1);
				} else {

					// 采用当月发放当月还
					i = (int) ((cal.get(Calendar.MONTH) + 1 + 2) / 3) * 3;

					// 如果当季结息日小于上次计息日期,就将下次结息日期设为下一个结息日
					// '利用DateSerial遇到超过12的月份数时自动增长年份的方法
					if (i * 100 + RetuDay <= (cal.get(Calendar.MONTH) + 1)
							* 100 + cal.get(Calendar.DAY_OF_MONTH))
						i = i + 3;
				}
				cal.set(cal.get(Calendar.YEAR), i - 1, 1);
				datTemp = new Date(cal.getTimeInMillis());
			} else {

				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 3, 1);
				datTemp = new Date(cal.getTimeInMillis());
			}
			return GetSubsDate(datTemp, RetuDay);
		case 3:
		case 9: // 一次还清
			return strDueDate;
		}
		return null;
	}

	/**
	 * 计算下次还款日期 ，按月计算
	 * 
	 * @param datsDate
	 * @param RetuDay
	 * @return
	 */
	public static String GetSubsDate(Date datsDate, int RetuDay) {

		Date datTmp;
		String strdatTmp, strdatsDate;

		Calendar cal = Calendar.getInstance();
		cal.setTime(datsDate);
		cal.set(Calendar.DAY_OF_MONTH, RetuDay);
		datTmp = new Date(cal.getTimeInMillis());
		strdatTmp = ClsPublic.formatDate(datTmp, "yyyyMM");
		strdatsDate = ClsPublic.formatDate(datsDate, "yyyyMM");

		if (RetuDay > 28) {

			if (!strdatTmp.equals(strdatsDate)) {

				cal.setTime(datsDate);
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1);
				datTmp = new Date(cal.getTimeInMillis());
				datTmp = ClsPublic.DateAdd("d", -1, datTmp, RetuDay);
			}
		}
		return ClsPublic.formatDate(datTmp, "yyyyMMdd");
	}

	/**
	 * 根据期限及还款方式、周期计算期数
	 * 
	 * @param intTerm
	 * @param intRetuKind
	 * @param intRetuPeri
	 * @param datReleDate
	 * @param DatDueDate
	 * @param intNextSeason
	 * @param RetuDay
	 * @return
	 */
	public static int GetPeriCount(int intTerm, int intRetuKind,
			int intRetuPeri, Date datReleDate, Date datDueDate,
			int intNextSeason, int RetuDay) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(datDueDate);

		int PeriCount;
		if ((intRetuKind == 3) || (intRetuKind == 9)) {
			PeriCount = 1;
		} else {

			if (intRetuPeri == 1) {

				if (intRetuKind == 11) { // 按月结息，到期一次性还款
					PeriCount = GetMonthCounts(datReleDate, datDueDate, RetuDay);
				} else {
					PeriCount = GetMonthCounts(datReleDate, datDueDate, 0);
				}
			} else if (intRetuPeri == 2) {
				if (intRetuKind == 10) { // 按季结息，到期一次性还款
					PeriCount = GetSeasonCounts(datReleDate, datDueDate,
							intNextSeason, RetuDay) + 1;
				} else {
					PeriCount = (int) (intTerm + 2) / 3;
				}
			} else if (intRetuPeri == 9) {
				PeriCount = 1;
			} else {
				PeriCount = intTerm;
			}
		}
		return PeriCount;
	}

	/**
	 * 得到发放日期到到期日期间贷款应还款的期数
	 * 
	 * @param datReleDate
	 * @param DatDueDate
	 * @param RetuDay
	 * @return
	 */
	public static int GetMonthCounts(Date datReleDate, Date DatDueDate,
			int RetuDay) {

		int i = 0;
		Date datRetuDate = datReleDate;

		while (datRetuDate.compareTo(DatDueDate) < 0) { // 为了同一处理贷款期限为整月和不为整月的情况，此处为“<”，而不能为“<=”
			i = i + 1;
			datRetuDate = ClsPublic.DateAdd("m", 1, datRetuDate, RetuDay);
		}
		return i;
	}

	/**
	 * 得到发放日期到到期日期间贷款应还款跨越自然季的期数
	 * 
	 * @param datReleDate
	 * @param DatDueDate
	 * @param intNextSeason
	 * @param RetuDay
	 * @return
	 */
	public static int GetSeasonCounts(Date datReleDate, Date DatDueDate,
			int intNextSeason, int RetuDay) {

		int i = 0;
		Date datRetuDate;
		Calendar cal = Calendar.getInstance();
		cal.setTime(datReleDate);

		// 以下算法需要注意
		// 由于cal.set(year, month, day)中的month是从0到11的，而非1到12，因此以下算法中get后要加1，set前要减1
		cal.set(cal.get(Calendar.YEAR),
				((int) (((cal.get(Calendar.MONTH) + 1) + 2) / 3) + intNextSeason) * 3 - 1, RetuDay);
		datRetuDate = new Date(cal.getTimeInMillis());
		if (datRetuDate.compareTo(datReleDate) <= 0) {
			datRetuDate = ClsPublic.DateAdd("q", 1, datRetuDate, RetuDay);
		}

		while (datRetuDate.compareTo(DatDueDate) < 0) {
			i = i + 1;
			datRetuDate = ClsPublic.DateAdd("q", 1, datRetuDate, RetuDay);
		}
		return i;
	}

	/**
	 * 写入数据:字符型
	 * 
	 * @param va
	 * @return
	 */
	public static String SetStrV(String va) {

		String RetStr = "";
		RetStr = "'" + va + "'";
		return RetStr;
	}

	/**
	 * 读取数据:过滤空值 - 对字符型
	 * 
	 * @param va
	 * @return
	 */
	public static String GetStrV(Object va) {

		String RetStr = "";
		if (va == null) {
			return RetStr;
		} else {
			return va.toString().trim();
		}
	}

	/**
	 * 写入数据:数值型
	 * 
	 * @param va
	 * @return
	 */
	public static String SetCurV(String va) {

		return va == null ? "0" : va.trim();

	}

	/**
	 * 读取数据:过滤空值 - 对数值型
	 * 
	 * @param va
	 * @return
	 */
	public static double GetCurV(Object va) {
		if (va == null) {
			return 0;
		}
		if (va.toString().trim().length() == 0) {
			return 0;
		}
		return new Double(va.toString()).doubleValue();
	}

	/**
	 * 读取数据:过滤空值 - 对数值型
	 * 
	 * @param va
	 * @return
	 */
	public static int GetIntV(String va) {

		return (va == null || va.trim().length() == 0) ? 0 : new Integer(va).intValue();
	}

	/**
	 * 写入数据:日期型
	 * 
	 * @param va
	 * @return
	 */
	public static String setdatv(String va) {

		String ChgStr = "'" + va + "'";
		return ChgStr;
	}

	/**
	 * 读取数据:过滤空值 - 对日期型 主要用于拼接SQL语句时方便处理null值
	 * 
	 * @param s
	 * @return
	 */
	public static String getDatVForSQL(String s) {
		if (s == null || s.trim().equalsIgnoreCase("")) {
			return "null";
		} else {
			return "'" + s.trim() + "'";
		}
	}

	/**
	 * 将如果字符串长度小于2在前面补"0",补足两位
	 * 
	 * @param va
	 * @return
	 */
	public static String formatstr(String va) {

		if (va != null) {
			va = va.trim();
			switch (va.length()) {
			case 0:
				return "00" + va;
			case 1:
				return "0" + va;
			default:
				return va;
			}
		} else {
			return va;
		}
	}

	/*************************************************************************************************************************
	 * Decimal
	 *************************************************************************************************************************/
	/**
	 * 格式化double数据类型
	 * 
	 * @param value
	 * @param digit
	 * @return String
	 */
	public static String formatDecimal(double value, int digit) {

		DecimalFormat f = new DecimalFormat("0." + repeat("0", digit));
		String sValue = f.format(value + 0.000000001);
		return sValue;
	}

	/**
	 * 格式化double数据类型
	 * 
	 * @param dblValue
	 * @param intDigit
	 * @return Double
	 */
	public static double getDoubleV(double dblValue, int intDigit) {

		return Double.parseDouble(formatDecimal(dblValue, intDigit));
	}

	/**
	 * double数据类型 相加
	 * 
	 * @param dblValue
	 * @param intDigit
	 * @return Double
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * * 两个Double数相减 *
	 * 
	 * @param v1
	 *            *
	 * @param v2
	 *            *
	 * @return Double
	 */
	public static Double sub(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return new Double(b1.subtract(b2).doubleValue());
	}

	/**
	 * 格式化double数据类型 补位
	 * 
	 * @param value
	 * @param count
	 * @return
	 */
	public static String repeat(String value, int count) {
		if (count < 0) {
			return "";
		}
		StringBuffer buf = new StringBuffer(count * value.length());
		for (int i = 0; i < count; i++) {
			buf.append(value);
		}
		return buf.toString();
	}

	public static int getDays(String begin, String end) throws ParseException {

		if (begin == null || begin.equalsIgnoreCase("") || end == null
				|| end.equalsIgnoreCase("")) {
			return 0;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setTimeZone(timeZone);
		BigDecimal bdDeno = new BigDecimal(String.valueOf(1000 * 24 * 3600));
		BigDecimal bdDiff = new BigDecimal(String.valueOf(0));
		BigDecimal bdDays = new BigDecimal(String.valueOf(0));

		bdDiff = new BigDecimal(String.valueOf(format.parse(end).getTime()
				- format.parse(begin).getTime()));
		bdDays = bdDiff.divide(bdDeno, 2, BigDecimal.ROUND_HALF_UP);

		return bdDays.intValue();
	}

	/*************************************************************************************************************************
	 * Date
	 *************************************************************************************************************************/

	/**
	 * 将字符串转换成日期对象
	 * 
	 * @param d
	 * @return
	 */
	public static java.util.Date getDate(String d) {

		try {
			if (d != null && !d.trim().equalsIgnoreCase("")) {
				String t = "yyyy-MM-dd";
				/*
				 * if(d.trim().length() == 14){ String s = d.substring(8, 14);
				 * if(s.equals("000000")){ d = d.substring(0, 8) +
				 * ClsPublic.getOSTime("kkmmss"); } t = "yyyyMMddkkmmss"; }
				 */
				if (d.trim().length() == 14
						&& d.substring(8, 14).equals("000000")) {
					d = d.substring(0, 8);
				}

				SimpleDateFormat sdf = new SimpleDateFormat(t);
				sdf.setTimeZone(timeZone);
				java.util.Date d1 = sdf.parse(d);

				return d1;
			} else {
				throw new NullPointerException("ParameterIsNull");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字符串转换成日期对象（严格控制必须是日期值）
	 * 
	 * @param d
	 * @return
	 */
	public static Date getDate_Strict(String d) throws ParseException {

		if (d != null && !d.trim().equalsIgnoreCase("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(timeZone);
			java.util.Date d1 = sdf.parse(d);
			return new Date(d1.getTime());
		} else {
			return null;
		}
	}

	/**
	 * 日期操作方法 返回两个日期的间隔，如果interval="d" 返回天数，如果interval="m" 返回月数，否则返回0 <br>
	 * <b>按照算头不算尾的原则计算</b></br>
	 * 
	 * @param interval
	 * @param EndDate
	 * @param BeginDate
	 * @return
	 */
	public static int DateDiff(String interval, Date EndDate, Date BeginDate) {

		int i = 0;
		Calendar cEnd = Calendar.getInstance();
		Calendar cBegin = Calendar.getInstance();
		cEnd.setTime(EndDate);
		cBegin.setTime(BeginDate);

		cEnd.set(Calendar.SECOND, 0);
		cEnd.set(Calendar.MINUTE, 0);
		cEnd.set(Calendar.HOUR_OF_DAY, 0);
		cBegin.set(Calendar.SECOND, 0);
		cBegin.set(Calendar.MINUTE, 0);
		cBegin.set(Calendar.HOUR_OF_DAY, 0);

		if ("d".equals(interval)) {

			long intev = cEnd.getTimeInMillis() - cBegin.getTimeInMillis();
			double dd = (intev / (1000.0D * 60.0D * 60.0D * 24.0D));
			i = (int) dd;
			if ((dd - (double) i) > 0.1D) {
				i += 1;
			}
		} else if ("m".equals(interval)) {

			i = (cEnd.get(Calendar.YEAR) - cBegin.get(Calendar.YEAR)) * 12
					+ (cEnd.get(Calendar.MONTH) - cBegin.get(Calendar.MONTH));

			Date datDateTemp = DateAdd("m", i, BeginDate, 0);
			if (datDateTemp.before(EndDate)) {
				i = i + 1;
			}
		} else if ("y".equals(interval)) {

			return cEnd.get(Calendar.YEAR) - cBegin.get(Calendar.YEAR);
		} else {

			i = 0;
		}

		return i;
	}

	/**
	 * 将日期增加给定的间隔 intTermOver
	 * 
	 * @param interval
	 *            增加类型 年 月 日 季 y m d q
	 * @param intTermOver
	 *            -4 减少个单位 ＋4 增加4个单位
	 * @param date
	 * @return
	 */
	public static Date DateAdd(String interval, int intTermOver,
			java.util.Date date, int RetuDay) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if ("d".equals(interval)) {
			c.add(Calendar.DAY_OF_YEAR, intTermOver);
		} else if ("m".equals(interval)) {
			c.add(Calendar.MONTH, intTermOver);
		} else if ("y".equals(interval)) {
			c.add(Calendar.YEAR, intTermOver);
		} else if ("q".equals(interval)) {
			c.add(Calendar.MONTH, intTermOver * 3);
		}

		if (RetuDay == 0) {// retuday等于0表示纯粹的日期加减
			return new Date(c.getTimeInMillis());
		}

		// 下面的处理是为了生成sdate
		if (RetuDay > 28) {
			int m = c.get(Calendar.MONTH) + 1;
			c.set(Calendar.DAY_OF_MONTH, RetuDay);

			int m1 = c.get(Calendar.MONTH) + 1;
			while (m1 != m) {
				c.add(Calendar.DAY_OF_MONTH, -1);
				m1 = c.get(Calendar.MONTH) + 1;
			}
		} else {

			c.set(Calendar.DAY_OF_MONTH, RetuDay);
		}

		return new Date(c.getTimeInMillis());
	}

	/**
	 * 将日期格式化成字符串 结果为 yyyyMMdd
	 * 
	 * @param date
	 * @return yyyyMMdd
	 */
	public static String formatDate(Date date) {

		return formatDate(date, "yyyyMMdd");

	}

	/**
	 * 将日期格式化成字符串
	 * 
	 * @param date
	 * @param format
	 *            可以为：yyyyMMdd，yyyy-MM-dd kk:mm:ss
	 * @return
	 */
	public static String formatDate(Date date, String format) {

		if (date == null || format == null) {
			throw new NullPointerException("DateDoNull");
		}

		SimpleDateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(timeZone);
		String temp = new String(df.format(date));

		if (temp == null || temp.trim().length() == 0) {
			throw new NullPointerException("formatWorkDateErr");
		}

		return temp;
	}

	/**
	 * 取得util.Date的年y 月m 日d 返回 -1 为错误
	 * 
	 * @return
	 */
	public static int getSomeDate(java.util.Date date, String type) {

		if (date == null || type == null) {
			return -1;
		}
		if (type.length() <= 0) {
			return -1;
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(date.getTime());

		int num = 0;
		type = new String(type.trim().toLowerCase());

		if (type.equals("y")) {
			num = cal.get(Calendar.YEAR);
		} else if (type.equals("m")) {
			num = cal.get(Calendar.MONTH) + 1;
		} else if (type.equals("d")) {
			num = cal.get(Calendar.DATE);
		} else {
			num = -1;
		}

		return num;
	}

	/**
	 * util.Date型日期转化sql.Date(年月日)型日期
	 * 
	 * @Param: p_utilDate util.Date型日期
	 * @Return: java.sql.Date sql.Date型日期
	 * @Author:
	 * @Date: 2006-10-31
	 */
	public static java.util.Date toUtilDateFromSqlDate(java.sql.Date p_utilDate) {

		java.util.Date returnDate = null;
		if (p_utilDate != null) {
			returnDate = new java.util.Date(p_utilDate.getTime());
		}
		return returnDate;
	}

	/**
	 * 取得当前操作系统的工作时间 kkmmss
	 * 
	 * @param fmt
	 * @return
	 */
	public static String getOSTime(String fmt) {

		return formatDate(new Date(), fmt);
	}

	/*取工作日加系统时间，可以调用getworktime
	 * public static Date getDateProOSTime(String date) throws ParseException {
		return getDateProOSTime(getDate(date));
	}

	
	 * 指定日期加当前系统时间
	 
	public static Date getDateProOSTime(Date date) throws ParseException {

		String sDate = ClsPublic.formatDate(date, "yyyyMMdd");
		String sTime = ClsPublic.formatDate(new Date(), "kkmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
		sdf.setTimeZone(timeZone);
		return sdf.parse(sDate.concat(sTime));
	}*/

	/**
	 * 根据字节截取字符
	 * 
	 * @param str
	 *            源
	 * @param num
	 *            字节数
	 * @return
	 */
	public static String interceptByte(String str, int num) {

		if ("".equals(str) || null == str) {
			return str;
		}
		str = new String(str);
		int re = 0; // 初始化字节数
		for (int i = 0; i < str.length(); i++) {
			re += String.valueOf(str.charAt(i)).getBytes().length; // 计字节数
			if (re > num) {
				return str.substring(0, i);
			}
		}
		return str;
	}

	/**
	 * 取得指定日期的时间 (工作日期 + OS时间) yyyyMMddkkmmss
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Date getWorkTime(Date WorkDate) throws ParseException {

		return ClsPublic.getWorkTime(WorkDate, "yyyyMMddkkmmss");

	}

	/**
	 * 取得指定日期的时间 (工作日期 + OS时间) yyyyMMddkkmmss
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Date getWorkTime(Date WorkDate, String simple)
			throws ParseException {

		String da = ClsPublic.formatDate(WorkDate);
		String te = ClsPublic.getOSTime("kkmmss");

		SimpleDateFormat sdf = new SimpleDateFormat(simple);
		sdf.setTimeZone(timeZone);
		Date date = sdf.parse(da + te);

		return date;
	}

	/**
	 * 计算两个日期之间日期间隔的整月差，整日差
	 * 
	 * @param BeginDate
	 *            开始日期
	 * @param EndDate
	 *            结束日期
	 * @return {m:整月, d:整日}
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> DateMoDayDiff(Date BeginDate, Date EndDate) {

		Map<String, Object> info = DateMoDayDiffToInfo(BeginDate, EndDate);
		int way = (Integer) info.get("way");
		Map<String, Object> mMap = (Map<String, Object>) info.get("m");
		Map<String, Object> dMap = (Map<String, Object>) info.get("d");

		Map<String, Object> un = new HashMap<String, Object>();
		un.put("m", String.valueOf(mMap.size() * way));
		un.put("d", String.valueOf(dMap.size() * way));
		return un;
	}

	/**
	 * 计算两个日期之间日期间隔的整月差，整日差
	 * 
	 * @param BeginDate
	 *            开始日期
	 * @param EndDate
	 *            结束日期
	 * @return {m:整月, d:整日}
	 */
	public static Map<String, Object> DateMoDayDiffToInfo(Date BeginDate, Date EndDate) {

		Date bCal = null, eCal = null;

		int way = 0, t = BeginDate.compareTo(EndDate);
		if (t > 0) {
			way = -1;
			bCal = new Date(EndDate.getTime());
			eCal = new Date(BeginDate.getTime());
		} else if (t < 0) {
			way = 1;
			bCal = new Date(BeginDate.getTime());
			eCal = new Date(EndDate.getTime());
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("m", String.valueOf(0));
			map.put("d", String.valueOf(0));
			return map;
		}

		Map<String, Object> mMap = new HashMap<String, Object>();
		Map<String, Object> dMap = new HashMap<String, Object>();

		Date bDate = new Date(bCal.getTime());
		Date eDate = new Date(eCal.getTime());
		// 请保留注释部分
		// System.out.println(formatDate(bDate));
		// System.out.println(formatDate(eDate));
		// System.out.println("======================================");
		int bDay = getSomeDate(bDate, "d");
		int m = 0, d = 0;
		for (m = 1; true; m++) {
			Date tDate = DateAdd("m", m, bDate, 0);
			// System.out.println(formatDate(tDate) + "   " + m);
			int tNum1 = eDate.compareTo(tDate);
			if (0 > tNum1) {
				tDate = DateAdd("m", --m, bDate, 0);
				// System.out.println(formatDate(tDate) + "   " + m);
				Date tDate2 = new Date(tDate.getTime());
				for (d = 1; 0 < eDate.compareTo(tDate); d++) {
					tDate = DateAdd("d", d, tDate2, 0);
					dMap.put(String.valueOf(dMap.size()), tDate);
					// System.out.println(formatDate(tDate) + "   " + m);
					int tDay = getSomeDate(tDate, "d");
					if (bDay == tDay) {
						m++;
						mMap.put(String.valueOf(mMap.size()), tDate2);

						d = 0;
						dMap.clear();

						tDate2 = new Date(tDate.getTime());
					}
				}
				d--;
				dMap.remove(String.valueOf(dMap.size()));
				break;
			} else if (0 == tNum1) {
				d = 0;
				break;
			} else {
				mMap.put(String.valueOf(mMap.size()), tDate);
				continue;
			}
		}

		Map<String, Object> un = new HashMap<String, Object>();
		un.put("m", mMap);
		un.put("d", dMap);
		un.put("way", way);
		return un;
	}

	/**
	 * 是否为当月的最后一天
	 * 
	 * @param agl
	 * @return
	 */
	public static boolean isLastMoDay(Calendar agl) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(agl.getTimeInMillis());
		int oldMo = cal.get(Calendar.MONTH);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		int newMo = cal.get(Calendar.MONTH);
		return oldMo != newMo;
	}

	/**
	 * 取得当月的最后一天
	 * 
	 * @param obj
	 *            java.util.Date java.util.Calendar
	 * @return
	 */
	public static Date getLastMoDay(Object obj) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(ClsPublic.timeZone);
		if (obj instanceof java.util.Date) {
			cal.setTime(((java.util.Date) obj));
		} else if (obj instanceof java.util.Calendar) {
			cal.setTimeInMillis(((java.util.Calendar) obj).getTimeInMillis());
		} else {
			throw new NullPointerException();
		}

		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 设置date为当月的指定日day，若day大于date当月的最大天数则设置为date当月的最后一天
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date setSpeMoDay(Date date, int day) {
		Calendar tCal = Calendar.getInstance();
		tCal.setTimeZone(timeZone);
		tCal.setTime(date);
		Date LastDate = getLastMoDay(date);

		Calendar lCal = Calendar.getInstance();
		lCal.setTimeZone(timeZone);
		lCal.setTime(LastDate);
		int LastDay = lCal.get(Calendar.DAY_OF_MONTH);

		tCal.set(Calendar.DAY_OF_MONTH, day <= LastDay ? day : LastDay);
		return new Date(tCal.getTimeInMillis());
	}

	/**
	 * 取得两个日期中的小的日志，如果日期中有空值，则返回
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Date getMinDate(Date date1, Date date2) {

		if (date1 == null) {
			return date2;
		} else if (date2 == null) {
			return date1;
		} else {
			return date1.compareTo(date2) >= 0 ? date2 : date1;
		}
	}

	/**
	 * 拷贝文件
	 * 
	 * @param FouFilePath
	 *            源文件
	 * @param TeeFilePath
	 *            目标目录
	 */
	public void CopyFile(String FouFilePath, String TeeFilePath) throws IOException {

		File fouFile = new File(FouFilePath);
		if (!fouFile.exists()) {
			return;
		}

		File teeFile = new File(TeeFilePath);
		if (teeFile.exists()) {

			if (fouFile.isFile() && teeFile.isFile()) {
				teeFile.delete();
			} else if (fouFile.isDirectory() && teeFile.isDirectory()) {
			} else {
				// throw new
				// IOException("FileExist:".concat(teeFile.getAbsolutePath()));
			}
		} else {
			teeFile.mkdirs();
		}

		if (fouFile.isDirectory()) {
			File t = new File(TeeFilePath, fouFile.getName());
			t.mkdirs();
			File[] tFile = fouFile.listFiles();
			for (int i = 0; i < tFile.length; i++) {
				CopyFile(tFile[i].getAbsolutePath(), t.getAbsolutePath());
			}
		} else {
			FileOutputStream fos = new FileOutputStream(new File(teeFile, fouFile.getName()));
			FileInputStream fis = new FileInputStream(FouFilePath);
			byte[] buf = new byte[100 * 1024];
			int bytesRead;
			while ((bytesRead = fis.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
				fos.flush();
			}
			fos.close();
			fis.close();
		}
	}

	/**
	 * 删除文件、文件夹
	 * 
	 * @param path
	 *            文件路径
	 */
	public void DelFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] fList = file.listFiles();
				for (int i = 0; fList != null && i < fList.length; i++) {
					DelFile(fList[i].getAbsolutePath());
				}
				file.delete();
			} else {
				file.delete();
			}
		}
	}

	/**
	 * 取得定字节长的字符
	 * 
	 * @param str
	 * @param num
	 * @return
	 */
	public static String getSubStr(String s, int m) {

		if (s == null || s.length() <= m) {
			return s;
		}
		int i;
		for (i = 0; i < m; i++) {
			if (s.charAt(i) > 0xff) {
				m--;
			}
		}
		if (m > s.length()) {
			m = s.length();
		}
		return s.substring(0, m) + " ";
	}

	public static boolean in(String str, String[] org) {
		if (str == null || org == null) {
			return false;
		} else {
			for (int i = 0; i < org.length; i++) {
				if (str.equalsIgnoreCase(org[i])) {
					return true;
				}
			}
			return false;
		}
	}

	public static boolean in(int i, int[] list) {

		if (list == null) {
			return false;
		} else {
			for (int j = 0; j < list.length; j++) {
				if (i == list[j]) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 判断输入的工作日期是否符合当前的时间标准
	 * 
	 * @param WorkDate
	 *            当前的系统日期
	 * @param Freq
	 *            时间间隔 YY QQ MM
	 * @param MonIncr
	 *            月增量，相对频率的起始月而言
	 * @param day
	 *            约定的日期,具体的赋值为:first(月初),end(月末),具体的约定日(1,3,4,5) 如果为多天时，用逗号分隔
	 * @return
	 */
	public static boolean isRun(Date WorkDate, String Freq, String MOnIncr, String day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(WorkDate);
		Freq = Freq.toUpperCase();
		Calendar comp = Calendar.getInstance();
		comp.set(Calendar.MONTH, 0);
		comp.set(Calendar.DATE, 1);
		int month;
		int monthIncr;
		if ("end".equalsIgnoreCase(day)) {
			if (!endOfMon(WorkDate)) {
				return false;
			}
		} else if ("first".equalsIgnoreCase(day)) {
			if (1 != cal.get(Calendar.DATE)) {
				return false;
			}
		} else {
			int date = Integer.parseInt(day);
			if (cal.get(Calendar.DATE) != date) {
				return false;
			}
		}
		if (in(Freq.toUpperCase(), "YY,QQ")) {
			if (null == MOnIncr) {
				monthIncr = 0;
			} else {
				monthIncr = Integer.parseInt(MOnIncr);
			}
			month = cal.get(Calendar.MONTH) - monthIncr;
			if ("YY".equalsIgnoreCase(Freq)) {
				month = month % 12;
			} else {
				month = month % 3;
			}
			if (month != 0) {
				return false;
			}
		}

		return true;

	}

	/**
	 * 判断
	 * 
	 * @param sourceStr
	 *            要查找的字符串
	 * @param InStr
	 *            查找的标准
	 * @return
	 */

	public static boolean in(String sourceStr, String InStr) {
		if (InStr.contains(sourceStr)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param sourceStr
	 * @param InStr
	 * @return
	 */
	public static boolean not_in(String sourceStr, String InStr) {
		if (!InStr.contains(sourceStr)) {
			return true;
		}
		return false;
	}

	/**
	 * 当前日期所在月的天数
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static int daysOfMon(Date date) {
		if (date == null) {
			return -1;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(cal.DATE, 1);
			cal.roll(cal.DATE, -1);
			return cal.get(cal.DATE);
		}
	}

	/**
	 * 当前日期所在年的天数
	 * 
	 * @param date
	 * @return
	 */
	public static int daysOfYear(Date date) {
		if (date == null) {
			return -1;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, 0);
			cal.setTime(date);
			cal.add(Calendar.YEAR, 1);
			Date nextDate = cal.getTime();
			return getDays(date, nextDate);
		}
	}

	/**
	 * 计算两个日期之间间隔的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDays(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return 0;
		} else {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			long intev = cal2.getTimeInMillis() - cal1.getTimeInMillis();
			int iValue = (int) (intev / (1000 * 60 * 60 * 24));
			return iValue;
		}
	}

	/**
	 * 取得两个日期中较小的一个 （不为空的）
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回日期小的，如果为空则返回其中非空的。
	 */
	public static Date min(Date date1, Date date2) {

		if (date1 == null) {
			return date2;
		} else if (date2 == null) {
			return date1;
		} else {
			return date1.compareTo(date2) >= 0 ? date2 : date1;
		}
	}

	/**
	 * 取得两个日期中较大的一个 （不为空的）
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回日期大的，如果为空则返回其中非空的。
	 */
	public static Date max(Date date1, Date date2) {

		if (date1 == null) {
			return date2;
		} else if (date2 == null) {
			return date1;
		} else {
			return date1.compareTo(date2) >= 0 ? date1 : date2;
		}
	}

	/**
	 * 日期操作方法 返回两个日期的间隔 <br>
	 * <b>计算两个日期之间的差额，返回小数的形式</b></br>
	 * 
	 * @param interval
	 *            支持参数"Y y M m D d"
	 * @param maxDate
	 * @param minDate
	 * @return 两个日期之间按间隔定义的时间间隔，如果两个日期中有为空的话，则返回0
	 * @throws Exception
	 */
	public static double between(String interval, Date minDate, Date maxDate)
			throws Exception {
		if (minDate == null || maxDate == null) {
			return 0.0;
		} else {
			double iValue = 0.0d;
			Calendar date1 = Calendar.getInstance();
			Calendar date2 = Calendar.getInstance();
			date1.setTime(minDate);
			date2.setTime(maxDate);
			if ("d".equalsIgnoreCase(interval)) {
				iValue = getDays(minDate, maxDate);
			} else if ("m".equalsIgnoreCase(interval)) {
				double dayValue = date2.get(Calendar.DATE) - date1.get(Calendar.DATE);
				iValue = (date2.get(Calendar.YEAR) - date1.get(Calendar.YEAR)) * 12
						+ (date2.get(Calendar.MONTH) - date1.get(Calendar.MONTH)) + dayValue / daysOfMon(minDate);
			} else if ("y".equalsIgnoreCase(interval)) {
				iValue = (date2.get(Calendar.YEAR) - date1.get(Calendar.YEAR))
						+ (double) (date2.get(Calendar.MONTH) - date1.get(Calendar.MONTH)) / 12
						+ (double) (date2.get(Calendar.DATE) - date1.get(Calendar.DATE)) / daysOfYear(minDate);
			} else {
				throw new Exception("invalid parameter");
			}
			return iValue;
		}
	}

	/**
	 * 日期操作方法 返回两个日期的间隔 <br>
	 * <b>计算两个日期之间的差额，返回小数的形式</b></br>
	 * 
	 * @param interval
	 *            支持参数"Y y M m D d"
	 * @param maxDate
	 * @param minDate
	 * @return 两个日期之间按间隔定义的时间间隔，如果两个日期中有为空的话，则返回0
	 * @throws Exception
	 */
	public static int compact_between(String interval, Date minDate,
			Date maxDate) throws Exception {
		if (minDate == null || maxDate == null) {
			return 0;
		}
		if (minDate.compareTo(maxDate) == 0) {
			return 0;
		}

		Calendar max = Calendar.getInstance();
		max.setTime(maxDate);
		Calendar min = Calendar.getInstance();
		min.setTime(minDate);
		if ("y".equalsIgnoreCase(interval)) {
			return (int) between("y", minDate, maxDate);
		} else if ("m".equalsIgnoreCase(interval)) {
			max.set(Calendar.YEAR, min.get(Calendar.YEAR));
			return (int) between("m", min.getTime(), max.getTime());
		} else if ("d".equalsIgnoreCase(interval)) {
			max.set(Calendar.YEAR, min.get(Calendar.YEAR));
			max.set(Calendar.MONTH, min.get(Calendar.MONTH));
			return (int) between("d", min.getTime(), max.getTime());
		} else {
			throw new Exception("invalid parameter");
		}
	}

	/**
	 * 日期操作方法 返回两个日期的间隔 <br>
	 * <b>计算两个日期之间的差额，按取整形式返回</b></br>
	 * 
	 * @param interval
	 *            支持参数"Y y M m D d"
	 * @param maxDate
	 * @param minDate
	 * @return 两个日期之间按间隔定义的时间间隔(取整)，如果两个日期中有为空的话，则返回0
	 */
	public static int floor(String interval, Date minDate, Date maxDate) {
		try {
			return (int) between(interval, minDate, maxDate);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 日期操作方法 返回两个日期的间隔 <br>
	 * <b>计算两个日期之间的差额，按取整形式返回</b></br>
	 * 
	 * @param interval
	 *            支持参数"Y y M m D d"
	 * @param maxDate
	 * @param minDate
	 * @return 两个日期之间按间隔定义的时间间隔(向上取整)，如果两个日期中有为空的话，则返回0
	 */
	public static int ceil(String interval, Date minDate, Date maxDate) {
		try {
			return (int) Math.ceil(between(interval, minDate, maxDate));
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 是否为当月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean endOfMon(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar anotherCal = Calendar.getInstance();
		anotherCal.setTime(date);
		anotherCal.add(Calendar.DATE, 1);
		return cal.get(Calendar.MONTH) == anotherCal.get(Calendar.MONTH) ? false : true;
	}

	/**
	 * 是否为当年的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean endOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return endOfMon(date) && (cal.get(Calendar.MONTH) == 11) ? true : false;
	}

	/**
	 * 是否为当季的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean endOfSeason(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return endOfMon(date) && ((cal.get(Calendar.MONTH) + 1) % 3 == 0) ? true : false;
	}

	/**
	 * 返回date的对年对月对日下前一年的日子，主要是考虑闰年的情况
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashSet getCompactfloorYearMonthDay(Date date) {
		HashSet set = new HashSet<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar comp = Calendar.getInstance();
		comp.setTime(date);

		cal.add(Calendar.MONTH, -12);
		set.add(cal.getTime());
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 12);
		// Date curryearmonday = cal.getTime();
		if (cal.compareTo(comp) == 0) {
			set.add(cal.getTime());
		}
		return set;
	}

	/**
	 * 判断该天是否是二月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean IsLeapDay(Date date) {
		boolean flag = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.MONTH) == 1 && endOfMon(date)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断输入的日期是否为每月的每旬。
	 * 
	 * @param sysdate
	 * @return
	 */
	public static boolean isMonthSection(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DATE) == 10 || cal.get(Calendar.DATE) == 20) {
			return true;
		} else if (ClsPublic.endOfMon(date)) {
			return true;
		} else {
			return false;
		}
	}

}
