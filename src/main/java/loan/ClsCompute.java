package loan;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import loan.ClsPublic.RetuPlan;

public class ClsCompute {

	public int RetuDay = 0;

	public double BasicInteRate, FLoatFineRate, FineRate, BasicFineRate;
	public double oldInteRate;
	public double InteRate;
	public double FloatInteRate;
	public Date ReleDate, DueDate, sDate, rDate;
	public Date LastInteDate;
	public Date ThisDate;
	public String CurrSign, RateCode, FineRateType;
	public String BankId, ProdId;
	public double aBal;
	public int RetuKind, RetuPeri;
	public int tTerm, cTerm, sTerm, scTerm;
	public LinkedList<RetuPlan> list_RetuPlan = new LinkedList<RetuPlan>();

	public ClsCompute() {
	}

	/**
	 * 根据扣收金额拆解提前归还本金
	 * 
	 * @return
	 */
	public double SplitrsSubsAmt(Date inteBeginDate, Date thisDate,
			double rCapi, double OtherKillAmt, double sSubsAmt,
			String FoulMode, double BegrFoul, String AheaAmtKind,
			double FoulFloat, double InteRate, double dblTermRate,
			double IntePerc) throws Exception {

		double tempcCapi = 0;
		if ("1".equalsIgnoreCase(AheaAmtKind)) { // 确定还款本金金额
			tempcCapi = rCapi;
		} else if ("2".equalsIgnoreCase(AheaAmtKind)) { // 确定还款总金额

			double cCapi = 0;
			/**
			 * 如果开始日期或者结束日期为空，返回0
			 * 如果开始日期大于等于结束日期，返回0
			 * 扣收金额 = 提前归还本金 + 实扣利息 + 违约金 
			 *         = 提前归还本金 + ((提前归还利息 - 贴息金额) - 其他抵扣金额) + 违约金 
			 *         = 提前归还本金 + 应收利息 - 其他抵扣金额 + 违约金 
			 *         = 提前归还本金 + (提前归还本金 * 分段1日利率 * 分段1天数 + 提前归还本金 * 分段2日利率 * 分段2天数 + 
			 *                          ... + 提前归还本金 * 分段N日利率 * 分段N天数) - 其他抵扣金额 + 违约金
			 *         = 提前归还本金 * (1 + 分段1日利率 * 分段1天数 + 分段2日利率 * 分段2天数 + ... + 
			 *                          分段N日利率 * 分段N天数) - 其他抵扣金额 + 违约金
			 *                          1 + I_1 * Day_1 + I_2 * Day_2 + ... + I_N * Day_N
			 */
			// 注意：以下方法中rCapi必须传1，具体原因参见上述公式推导
			Map<String, Object> imap = new HashMap<String, Object>();

			imap = getPI_CapiFixed_Day_Ahea(inteBeginDate, thisDate, 1, InteRate, dblTermRate, IntePerc);

			// double dealInteDay = 1 + um.getDouble(ClsConst.PI_S_SUBSINTE);
			double dealInteDay = 1 + (Double) imap.get(ClsConst.PI_INTE);

			if ("0".equals(FoulMode)) { // 违约金收取方式：不收违约金
				cCapi = (sSubsAmt + OtherKillAmt) / dealInteDay;
			} else if ("1".equals(FoulMode)) { // 违约金收取方式：定比
				cCapi = (sSubsAmt + OtherKillAmt) / (dealInteDay + BegrFoul / 100 * (1 + FoulFloat * 1.0 / 100));
			} else if ("2".equals(FoulMode)) { // 违约金收取方式：定额
				cCapi = (sSubsAmt + OtherKillAmt - BegrFoul * (1 + FoulFloat * 1.0 / 100)) / dealInteDay;
			} else {
				throw new NullPointerException("NoTypeFoulMode");
			}
			tempcCapi = cCapi;
		} else {
			throw new NullPointerException("NoAheaAmtKind");
		}

		return tempcCapi;
	}

	public HashMap<String, Object> getPI_CapiFixed_Day_Ahea(Date BeginDate,
			Date EndDate, double Capi, double thisRate, double thisTermRate,
			double thisIntePerc) throws Exception {
		return getPI_CapiFixed_Ahea(BeginDate, EndDate,
				ClsConst.INTECOMPKIND_DAY, Capi, thisRate, thisTermRate,
				thisIntePerc, 0);
	}

	public HashMap<String, Object> getPI_CapiFixed_Term_Ahea(Date BeginDate,
			Date EndDate, double Capi, double thisRate, double thisTermRate,
			double thisIntePerc) throws Exception {
		return getPI_CapiFixed_Ahea(BeginDate, EndDate,
				ClsConst.INTECOMPKIND_TERM, Capi, thisRate, thisTermRate,
				thisIntePerc, 0);
	}

	public HashMap<String, Object> getPI_CapiFixed_Ahea(Date BeginDate,
			Date EndDate, String InteCompKind, double Capi, double thisRate,
			double thisTermRate, double thisIntePerc, int DaysOfTerm)
			throws Exception {
		return getPI(BeginDate, EndDate, InteCompKind, Capi, thisRate,
				thisTermRate, thisIntePerc, ClsConst.RATE_INTERATE,
				ClsConst.CAPIMODE_FIXED, DaysOfTerm);
	}

	/**
	 * <b>分段计息</b> 计算利息（使用dxLogComp.CompCapi）
	 * 
	 * @param LoanAcNo
	 *            贷款账号
	 * @param BeginDate
	 *            计息开始日期
	 * @param EndDate
	 *            计息结束日期
	 * @param InteCompKind
	 *            计息方式（按期计息、按日计息）
	 * @param Capi
	 *            计算本金
	 * @param thisRate
	 *            【【【注意】】】此处必须“月利率”（千分之，如：4.59，而非0.00459）（即：年利率 / 12 * 10）
	 * @param thisTermRate
	 *            期利率（绝对值，不带千分之，可直接用于计算）
	 * @param thisIntePerc
	 *            当前贴息比例（百分之，如：50，而非0.5）
	 * @param DaysOfTerm
	 *            一期的天数
	 * @return
	 */
	public HashMap<String, Object> getPI_CapiDBValue(Date BeginDate,
			Date EndDate, String InteCompKind, double Capi, double thisRate,
			double thisTermRate, double thisIntePerc, int DaysOfTerm)
			throws Exception {
		return getPI(BeginDate, EndDate, InteCompKind, Capi, thisRate,
				thisTermRate, thisIntePerc, ClsConst.RATE_INTERATE,
				ClsConst.CAPIMODE_DB_VALUE, DaysOfTerm);
	}

	/**
	 * <b>分段计息</b> 计算利息（按日）
	 * 
	 * @param LoanAcNo
	 *            贷款账号
	 * @param BeginDate
	 *            计息开始日期
	 * @param EndDate
	 *            计息结束日期
	 * @param Capi
	 *            计算本金
	 * @param thisRate
	 *            【【【注意】】】此处必须“月利率”（千分之，如：4.59，而非0.00459）（即：年利率 / 12 * 10）
	 * @param thisTermRate
	 *            期利率（绝对值，不带千分之，可直接用于计算）
	 * @param thisIntePerc
	 *            当前贴息比例（百分之，如：50，而非0.5）
	 * @return
	 */
	public HashMap<String, Object> getPI_CapiFixed_Day(Date BeginDate,
			Date EndDate, double Capi, double thisRate, double thisTermRate,
			double thisIntePerc) throws Exception {
		return getPI_CapiFixed(BeginDate, EndDate, ClsConst.INTECOMPKIND_DAY,
				Capi, thisRate, thisTermRate, thisIntePerc, 0);
	}

	/**
	 * <b>分段计息</b> 计算利息（按期）
	 * 
	 * @param LoanAcNo
	 *            贷款账号
	 * @param BeginDate
	 *            计息开始日期
	 * @param EndDate
	 *            计息结束日期
	 * @param Capi
	 *            计算本金
	 * @param thisRate
	 *            【【【注意】】】此处必须“年利率”（千分之，如：4.59，而非0.00459）（即：年利率 / 12 * 10）
	 * @param thisTermRate
	 *            期利率（绝对值，不带千分之，可直接用于计算）
	 * @param thisIntePerc
	 *            当前贴息比例（百分之，如：50，而非0.5）
	 * @return
	 */
	public HashMap<String, Object> getPI_CapiFixed_Term(Date BeginDate,
			Date EndDate, double Capi, double thisRate, double thisTermRate,
			double thisIntePerc) throws Exception {
		return getPI_CapiFixed(BeginDate, EndDate, ClsConst.INTECOMPKIND_TERM,
				Capi, thisRate, thisTermRate, thisIntePerc, 0);
	}

	/**
	 * <b>分段计息</b> 计算利息（使用传入的固定计算本金）
	 * 
	 * @param LoanAcNo
	 *            贷款账号
	 * @param BeginDate
	 *            计息开始日期
	 * @param EndDate
	 *            计息结束日期
	 * @param InteCompKind
	 *            计息方式（按期计息、按日计息）
	 * @param Capi
	 *            计算本金
	 * @param thisRate
	 *            【【【注意】】】此处必须“月利率”（千分之，如：4.59，而非0.00459）（即：年利率 / 12 * 10）
	 * @param thisTermRate
	 *            期利率（绝对值，不带千分之，可直接用于计算）
	 * @param thisIntePerc
	 *            当前贴息比例（百分之，如：50，而非0.5）
	 * @param DaysOfTerm
	 *            一期的天数
	 * @return
	 */
	public HashMap<String, Object> getPI_CapiFixed(Date BeginDate,
			Date EndDate, String InteCompKind, double Capi, double thisRate,
			double thisTermRate, double thisIntePerc, int DaysOfTerm)
			throws Exception {
		// return getPI(LogCompInfo, BeginDate, EndDate, InteCompKind, Capi,
		// thisRate, thisTermRate, thisIntePerc, ClsConst.RATE_INTERATE,
		// ClsConst.CAPIMODE_FIXED, DaysOfTerm);
		return getPI(BeginDate, EndDate, InteCompKind, Capi, thisRate,
				thisTermRate, thisIntePerc, ClsConst.RATE_INTERATE,
				ClsConst.CAPIMODE_DB_VALUE, DaysOfTerm);
	}

	/**
	 * <b>分段计息</b> 计算利息（按日）（使用dxLogComp.CompCapi）
	 * 
	 * @param LoanAcNo
	 *            贷款账号
	 * @param BeginDate
	 *            计息开始日期
	 * @param EndDate
	 *            计息结束日期
	 * @param Capi
	 *            计算本金
	 * @param thisRate
	 *            【【【注意】】】此处必须“月利率”（千分之，如：4.59，而非0.00459）（即：年利率 / 12 * 10）
	 * @param thisTermRate
	 *            期利率（绝对值，不带千分之，可直接用于计算）
	 * @param thisIntePerc
	 *            当前贴息比例（百分之，如：50，而非0.5）
	 * @return
	 */
	public HashMap<String, Object> getPI_CapiDBValue_Day(Date BeginDate,
			Date EndDate, double Capi, double thisRate, double thisTermRate,
			double thisIntePerc) throws Exception {
		return getPI_CapiDBValue(BeginDate, EndDate, ClsConst.INTECOMPKIND_DAY,
				Capi, thisRate, thisTermRate, thisIntePerc, 0);
	}

	/*************************************************************************************************************************
	 * 分段计息函数
	 *************************************************************************************************************************/

	/**
	 * 分段计息核心函数
	 * 
	 * @param LoanAcNo
	 *            贷款账号
	 * @param BeginDate
	 *            计息开始日期
	 * @param EndDate
	 *            计息结束日期
	 * @param InteCompKind
	 *            计息方式（按期计息、按日计息）
	 * @param Capi
	 *            计算本金
	 * @param thisRate
	 *            【【【注意】】】此处必须“月利率”（千分之，如：4.59，而非0.00459）（即：年利率 / 12 * 10）
	 * @param thisTermRate
	 *            期利率（绝对值，不带千分之，可直接用于计算）
	 * @param thisIntePerc
	 *            当前贴息比例（百分之，如：50，而非0.5）
	 * @param FieldName
	 *            计算内容（如：利息、罚息等）
	 * @param CapiMode
	 *            本金使用模式（用于标示计算本金统一使用传入的计算本金，还是使用dxLogComp.CompCapi）
	 * @param DaysOfTerm
	 *            一期的天数
	 * @return
	 */
	private HashMap<String, Object> getPI(Date BeginDate, Date EndDate,
			String InteCompKind, double Capi, double thisRate,
			double thisTermRate, double thisIntePerc, String FieldName,
			String CapiMode, int DaysOfTerm) throws Exception {
		Map<String, Object> um = new HashMap<String, Object>();
		if (BeginDate == null || EndDate == null)
			throw new NullPointerException("DateIdNull");
		if (BeginDate.getTime() > EndDate.getTime()) {
			um.put(ClsConst.PI_INTE, 0.0);
			um.put(ClsConst.PI_S_ALLOINTE, 0.0);
			um.put(ClsConst.PI_S_SUBSINTE, 0.0);
			return (HashMap<String, Object>) um;
		}
		double I = 0, AI = 0, SI = 0;
		if (ClsConst.INTECOMPKIND_TERM.equalsIgnoreCase(InteCompKind)) {
			I = InteByTerm(Capi, thisTermRate, thisIntePerc, ClsConst.COMP_KIND_S_INTE);
			AI = InteByTerm(Capi, thisTermRate, thisIntePerc, ClsConst.COMP_KIND_S_ALLOINTE);
			SI = InteByTerm(Capi, thisTermRate, thisIntePerc, ClsConst.COMP_KIND_S_SUBSINTE);
		} else if (ClsConst.INTECOMPKIND_DAY.equalsIgnoreCase(InteCompKind)) {
			I = InteByDay2(BeginDate, EndDate, Capi, thisRate, thisIntePerc,
					FieldName, ClsConst.COMP_KIND_S_INTE);
			AI = InteByDay2(BeginDate, EndDate, Capi, thisRate, thisIntePerc,
					FieldName, ClsConst.COMP_KIND_S_ALLOINTE);
			SI = InteByDay2(BeginDate, EndDate, Capi, thisRate, thisIntePerc,
					FieldName, ClsConst.COMP_KIND_S_SUBSINTE);
		} else if (ClsConst.INTECOMPKIND_DAY_M_D.equalsIgnoreCase(InteCompKind)) {
			I = InteByDay3(BeginDate, EndDate, Capi, thisRate, thisIntePerc,
					FieldName, ClsConst.COMP_KIND_S_INTE);
			AI = InteByDay3(BeginDate, EndDate, Capi, thisRate, thisIntePerc,
					FieldName, ClsConst.COMP_KIND_S_ALLOINTE);
			SI = InteByDay3(BeginDate, EndDate, Capi, thisRate, thisIntePerc,
					FieldName, ClsConst.COMP_KIND_S_SUBSINTE);
		} else {
			throw new NullPointerException("InteCompKindNotFound");
		}
		um.put(ClsConst.PI_INTE, I);
		um.put(ClsConst.PI_S_ALLOINTE, AI);
		um.put(ClsConst.PI_S_SUBSINTE, SI);
		return (HashMap<String, Object>) um;
	}

	/**
	 * 计算各种利息（按期）
	 * 
	 * @param C
	 *            计算本金
	 * @param TermRate
	 *            期利率（绝对值，不带千分之，可直接用于计算）
	 * @param IntePerc
	 *            当前贴息比例（百分之，如：50，而非0.5）
	 * @param strCompKind
	 *            计算方式（如：计算应还利息、应贴利息、应扣利息）
	 * @return
	 */
	private double InteByTerm(double C, double TermRate, double IntePerc,
			String strCompKind) throws Exception {
		return C * getCalcRate(TermRate, IntePerc, strCompKind);
	}

	/**
	 * 计算各种利息（按日－实际天数）－已知计息起止日期的情况
	 * 
	 * @param BeginDate
	 *            计息开始日期
	 * @param EndDate
	 *            计息结束日期
	 * @param C
	 *            计算本金
	 * @param Rate
	 *            【【【注意】】】此处必须“月利率”（千分之，如：4.59，而非0.00459）（即：年利率 / 12 * 10）
	 * @param IntePerc
	 *            当前贴息比例（百分之，如：50，而非0.5）
	 * @param FieldName
	 *            计算内容（如：利息、罚息等）
	 * @param strCompKind
	 *            计算方式（如：计算应还利息、应贴利息、应扣利息）
	 * @return
	 */
	private double InteByDay2(Date BeginDate, Date EndDate, double C,
			double Rate, double IntePerc, String FieldName, String strCompKind)
			throws Exception {

		double dblCalcRate = getCalcRate(Rate, IntePerc, strCompKind);
		double dblCalcRateOfDay = getCalcRateOfDay(FieldName, dblCalcRate);
		return C * ClsPublic.DateDiff("d", EndDate, BeginDate)
				* dblCalcRateOfDay;

	}

	/**
	 * 计算各种利息（按日－整月加零天）－已知计息起止日期的情况
	 * 
	 * @param BeginDate
	 *            计息开始日期
	 * @param EndDate
	 *            计息结束日期
	 * @param C
	 *            计算本金
	 * @param Rate
	 *            【【【注意】】】此处必须“月利率”（千分之，如：4.59，而非0.00459）（即：年利率 / 12 * 10）
	 * @param IntePerc
	 *            当前贴息比例（百分之，如：50，而非0.5）
	 * @param FieldName
	 *            计算内容（如：利息、罚息等）
	 * @param strCompKind
	 *            计算方式（如：计算应还利息、应贴利息、应扣利息）
	 * @return
	 */
	private double InteByDay3(Date BeginDate, Date EndDate, double C,
			double Rate, double IntePerc, String FieldName, String strCompKind)
			throws Exception {

		double dblCalcRate = getCalcRate(Rate, IntePerc, strCompKind);
		double dblCalcRateOfDay = getCalcRateOfDay(FieldName, dblCalcRate);
		Map<String, Object> um = ClsPublic.DateMoDayDiff(BeginDate, EndDate);
		int intMonths = (Integer) um.get("m");
		int intDays = (Integer) um.get("d");
		return C * dblCalcRate / 1000 * intMonths + C * dblCalcRateOfDay
				* intDays;

	}

	/**
	 * 考虑“计算利息或计算罚息”等因素，得到计算利息实际所用日利率绝对值（不带带单位，如：是实际的0.00459）
	 * 
	 * @param FieldName
	 *            计算内容（如：利息、罚息等）
	 * @param dblRate
	 *            利率值
	 * @return
	 */
	private double getCalcRateOfDay(String FieldName, double dblRate)
			throws Exception {
		if (FieldName.equalsIgnoreCase(ClsConst.RATE_INTERATE)) {
			return getRateOfDay(dblRate);
		} else if (FieldName.equalsIgnoreCase(ClsConst.RATE_FINERATE)) {
			return getFineRateOfDay(dblRate);
		} else {
			throw new NullPointerException("FieldNameNotFound");
		}
	}

	/**
	 * 取得日罚息利率（绝对值，可直接用于计算）
	 * 
	 * @param FineRate
	 *            月利率（万分之）
	 * @return
	 */
	public static double getFineRateOfDay(double FineRate) {
		return FineRate / 360 / 100;
	}

	/**
	 * 取得日利率（绝对值，可直接用于计算）
	 * 
	 * @param InteRate
	 *            年利率（百分之）
	 * @return
	 */
	public static double getRateOfDay(double InteRate) {
		// NOTE：未来此方法考虑修改支持360、365、366
		// interate是年利率，目前计算日利率的规则是360天
		double dblDayRate = InteRate / 360 / 100;

		return dblDayRate;
	}

	/**
	 * 考虑贴息等因素，得到计算利息所用利率（不是绝对值，仍带单位，如：4.59，而非实际的0.00459）
	 * 
	 * @param InteRate
	 *            利率（可能是贷款利率或罚息利率）
	 * @param IntePerc
	 *            贴息比例
	 * @param strCompKind
	 *            计算方式
	 * @return
	 */
	private double getCalcRate(double InteRate, double IntePerc,
			String strCompKind) throws Exception {
		if (ClsConst.COMP_KIND_S_INTE.equalsIgnoreCase(strCompKind)) {
			return InteRate;
		} else if (ClsConst.COMP_KIND_S_SUBSINTE.equalsIgnoreCase(strCompKind)) {
			return InteRate * (1 - IntePerc / 100);
		} else if (ClsConst.COMP_KIND_S_ALLOINTE.equalsIgnoreCase(strCompKind)) {
			return InteRate * IntePerc / 100;
		} else {
			throw new NullPointerException("Not_COMP_KIND_S");
		}
	}

	/**
	 * 取得期利率（绝对值，不带千分之，可直接用于计算）
	 * 
	 * @param InteRate
	 *            年利率（百分之）
	 * @param strRetuFreq
	 *            还款频率
	 * @param intRetuIncr
	 *            还款增量
	 * @return
	 */

	public static double getRateOfTerm(double InteRate, String strRetuFreq,
			int intRetuIncr) {

		double dblTermRate = 0;

		if (strRetuFreq.equalsIgnoreCase(ClsConst.RETUFREQ_DAY)) { // 还款频率为天
			dblTermRate = getRateOfDay(InteRate) * intRetuIncr;
		} else if (strRetuFreq.equalsIgnoreCase(ClsConst.RETUFREQ_MONTH)) { // 还款频率为月
			dblTermRate = InteRate / 100 / 12 * intRetuIncr;
		} else { // 还款频率为其他，暂不考虑
			dblTermRate = 0;
		}

		return dblTermRate;
	}

	/**********************************************************************
	 * 名称：a2 功能：计算分期还款额
	 *
	 * 参数： 
	 *   strRetuKind：还款方式
	 *   strThistCapi：计算本金
	 *   intRetuTerm：计算分期还款额的贷款期限（相对于取利率的贷款期限） 
	 *   termRate：贷款利率
	 *   strFirstGainTerm：首次累进期数 
	 *   strGainTerm：累进间隔（期） 
	 *   strGainAmt：累进金额/比例
	 *
	 * 返回值： 分期还款额
	 *
	 * 说明： 0) 输入参数包括：本金，总期数，期利率(=还息间隔*正常利率)，初始期数，累进间隔期数，累进金额/比例 
	 *       1) 对于等额本息法，需填本金，总期数，期利率, 输出为分期还款额； 
	 *       2) 对于等额本金法，"按期付息，按期还本法"，"按月还本，按季还息"，需填本金，总期数， 
	 *          输出为分期还本额(不含利息)； 
	 *       3) 对于等本等息法，需填本金，总期数，期利率，输出为分期还款额(本金＋利息)；
	 *       4) 对于等额累进法，等比累进法，需填本金，总期数，期利率，初始期数，累进间隔期数，累进金额/比例, 
	 *          输出为分期还款额； 
	 *       5) 对于其它还款法，输出为0
	 **********************************************************************/
	public double A(String strRetuKind, double dblThistCapi, int intRetuTerm,
			double termRate, int intFirstGainTerm, int intGainTerm,
			double dblGainAmt) throws Exception {

		double Amt = 0;

		// NOTE：以下为作为正式程序，需要的检查（此处忽略）：
		// 1-记录日志，对传入的参数进行记录；
		// 2-检查贷款期限、利率的合法性（不能小于等于零）
		// 3-检查贷款本金的合法性（不能小于零）

		if (strRetuKind.equalsIgnoreCase(ClsConst.BILLKIND_DEBX)) { // 等额本息
			Amt = CalculateAmt_DEBX(dblThistCapi, intRetuTerm, termRate);
		} else if (strRetuKind.equalsIgnoreCase(ClsConst.BILLKIND_DEBJ)) { // 等额本金
			Amt = CalculateAmt_DEBJ(dblThistCapi, intRetuTerm, termRate);
		} else if (strRetuKind.equalsIgnoreCase(ClsConst.BILLKIND_ZHX)) { // 只还息
			Amt = 0;
		} else if (strRetuKind.equalsIgnoreCase(ClsConst.BILLKIND_DELJ)) { // 等额累进
			Amt = CalculateAmt_DELJ(dblThistCapi, intRetuTerm, termRate,
					intFirstGainTerm, intGainTerm, dblGainAmt);
		} else if (strRetuKind.equalsIgnoreCase(ClsConst.BILLKIND_DBLJ)) { // 等比累进
			Amt = CalculateAmt_DBLJ(dblThistCapi, intRetuTerm, termRate,
					intFirstGainTerm, intGainTerm, dblGainAmt);
		} else if (strRetuKind.equalsIgnoreCase(ClsConst.BILLKIND_DBDX)) { // 等本等息
			Amt = CalculateAmt_DBDX(dblThistCapi, intRetuTerm, termRate);
		} else { // 其他
			Amt = 0;
		}

		return ClsPublic.getDoubleV(Amt, 2);
	}

	/**
	 * 等额本息
	 * 
	 * @param dblThistCapi
	 * @param intRetuTerm
	 * @param termRate
	 * @return
	 * @throws SQLException
	 */
	private double CalculateAmt_DEBX(double dblThistCapi, int intRetuTerm,
			double termRate) throws SQLException {
		if (termRate == 0) { // 利率为0，则分期还款额按照等额本息处理
			return CalculateAmt_DEBJ(dblThistCapi, intRetuTerm, 0);
		}
		double Y = dblThistCapi;
		double i = termRate;
		long n = intRetuTerm;
		double a = 1 + i;
		double b = PowerCal(a, n);

		double Amt = Y * i * b / (b - 1);
		return Amt;
	}
	

	/**
	 * 等额本金
	 * 
	 * @param dblThistCapi
	 * @param intRetuTerm
	 * @param termRate
	 * @return
	 * @throws SQLException
	 */
	private double CalculateAmt_DEBJ(double dblThistCapi, int intRetuTerm,
			double termRate) throws SQLException {
		double Y = dblThistCapi;
		long n = intRetuTerm;
		// double i = termRate;

		/*
		 * 第 k 期的还款额 cal_results_p->cur_payment = Y / n + (1 - (k - (double)1) /
		 * n) Y * i;
		 */

		// 取还本额
		double Amt = Y / n;

		return Amt;
	}
	
	

	/**
	 * 等额累进
	 * 
	 * @param dblThistCapi
	 * @param intRetuTerm
	 * @param termRate
	 * @param intFirstGainTerm
	 * @param intGainTerm
	 * @param dblGainAmt
	 * @return
	 * @throws SQLException
	 */
	private double CalculateAmt_DELJ(double dblThistCapi, int intRetuTerm,
			double termRate, int intFirstGainTerm, int intGainTerm,
			double dblGainAmt) throws SQLException {

		double Y = dblThistCapi;
		double G = dblGainAmt;
		long t = intGainTerm;
		long Wb = intFirstGainTerm;

		long n = intRetuTerm;
		double i = termRate;
		long We;
		long v;// , z;
		double En, EWe, Ev1t, Et;
		double A, B, C, D;

		// Wb = n 时，公式结果与等额本息法相同。
		if ((Wb <= 0) || (Wb > n)) {
			SQLException ex = new SQLException("Amt9002");
			throw ex;
		}

		if (t <= 0) {
			SQLException ex = new SQLException("Amt9015");
			throw ex;
		}

		We = (n - Wb) % t;
		v = (n - Wb - We) / t;

		En = PowerCal(1 + i, n);
		EWe = PowerCal(1 + i, We);
		Ev1t = PowerCal(1 + i, (v + 1) * t);
		Et = PowerCal(1 + i, t);

		B = Y * i * En / (En - 1);
		C = (EWe * (Ev1t - 1) / (Et - 1) - (v + 1));

		A = B - G * C / (En - 1);

		double Amt = A;

		if (G > 0) { // 对等额递增法的限制条件
			if (A > (Y * i)) {
				// return 0;
			} else {
				SQLException ex = new SQLException("Amt9004");
				throw ex;
			}
		}

		if (G < 0) { // 对等额递减法的限制条件
			D = B / ((v + 1) - C / (En - 1));
			if (G > (0 - D)) {
				// return 0;
			} else {
				SQLException ex = new SQLException("Amt9004");
				throw ex;
			}
		}

		return Amt;

	}

	/**
	 * 等比累进
	 * 
	 * @param dblThistCapi
	 * @param intRetuTerm
	 * @param termRate
	 * @param intFirstGainTerm
	 * @param intGainTerm
	 * @param dblGainAmt
	 * @return
	 * @throws SQLException
	 */
	private double CalculateAmt_DBLJ(double dblThistCapi, int intRetuTerm,
			double termRate, int intFirstGainTerm, int intGainTerm,
			double dblGainAmt) throws SQLException {

		double Y = dblThistCapi;
		double s = dblGainAmt;
		long t = intGainTerm;
		long Wb = intFirstGainTerm;

		long n = intRetuTerm;
		double i = termRate;

		long We;
		long v;
		double En, EWb, Et, EWe, Evt;
		double Sv, Sv1;
		double A, B, C, D, F, x;

		// Wb = n 时，公式结果与等额本息法相同。
		if ((Wb <= 0) || (Wb > n)) {
			SQLException ex = new SQLException("Amt9002");
			throw ex;
		}

		if (t <= 0) {
			SQLException ex = new SQLException("Amt9015");
			throw ex;
		}
		We = (n - Wb) % t;
		v = (n - Wb - We) / t;

		if (s < 0) {
			if (s <= -1) {
				SQLException ex = new SQLException("Amt9005");
				throw ex;
			}
		}

		En = PowerCal(1 + i, n);
		EWb = PowerCal(1 + i, Wb);
		Et = PowerCal(1 + i, t);
		EWe = PowerCal(1 + i, We);
		Evt = PowerCal(1 + i, v * t);

		Sv = PowerCal(1 + s, v);
		Sv1 = PowerCal(1 + s, (v + 1));

		x = Math.abs(Et - (1 + s));
		if (x < 1e-100) { // 使用特殊计算公式
			A = Y * i * En / (Sv * ((EWb + (v + 1) * s) * EWe - (1 + s)));
		} else {
			B = Evt * EWe * (EWb - 1);
			F = (Evt - Sv) / (Et - (1 + s));
			C = (1 + s) * (Et - 1) * EWe * F;
			D = Sv1 * (EWe - 1);
			A = Y * i * En / (B + C + D);
		}
		double Amt = A;

		if (s > 0) {
			if (A > (Y * i)) {
				// return 0;
			} else {
				SQLException ex = new SQLException("Amt9005");
				throw ex;
			}
		}

		return Amt;

	}

	/**
	 * 等本等息
	 * 
	 * @param dblThistCapi
	 * @param intRetuTerm
	 * @param termRate
	 * @return
	 * @throws SQLException
	 */
	private double CalculateAmt_DBDX(double dblThistCapi, int intRetuTerm,
			double termRate) {

		double Y = dblThistCapi;
		long n = intRetuTerm;
		double i = termRate;

		/*
		 * 第 k 期的还款额 cal_results_p->cur_payment = Y / n + (1 - (k - (double)1) /
		 * n) Y * i;
		 */

		// 取还本额
		// double Amt = round_amt(Y /n) + round_amt(Y * i * (n + 1) / (2 * n));
		double Amt = Y / n + Y * i * (n + 1) / (2 * n);

		return Amt;
	}

	/**
	 * 名 称：PowerCal 
	 * 功能描述：计算浮点数的整数幂 
	 * 参数说明：
	 *   base：底数 
	 *   exp：指数，只能大于等于0 
	 * 返回码：计算结果，exp小于等于0时，返回1 
	 * 备注：无
	 **/
	private double PowerCal(double base, long exp) {
		double result = 1;
		long i;

		for (i = 1; i <= exp; i++) {
			result *= base;
		}
		return result;
	}

	/**
	 * 计算还款计划
	 * 
	 * @param LoanAcNo
	 *            贷款账号
	 * @param datBeginDate
	 * @param dbltCapi
	 * @param dblInteRate
	 * @param strBillKind
	 * @param strRetuFreq
	 * @param intRetuIncr
	 * @param datFirstRetuDate
	 * @param datEndRetuDate
	 * @param strLastTermKind
	 * @param intcTerm
	 * @param dblAmt
	 * @param datMaxEndRetuDate
	 * @param strFirstInteByDay
	 * @param strLastInteByDay
	 * @param intspTerm
	 * @param strSpeMonth1
	 * @param strSpeMonth2
	 * @param dblSpeAmt1
	 * @param dblSpeAmt2
	 * @param DaysOfTerm
	 *            一期的天数
	 * @throws SQLException
	 */
	public double QueryPlan(Date FirstRetuDate, Date datBeginDate,
			double dbltCapi, double dblInteRate, double dblIntePerc,
			String strBillKind, String strRetuFreq, int intRetuIncr,
			Date datFirstRetuDate, Date datEndRetuDate, String strLastTermKind,
			int intcTerm, double dblAmt, Date datMaxEndRetuDate,
			String strFirstInteByDay, String strLastInteByDay, int intspTerm)
			throws Exception {
		// list_RetuPlan.clear();
		int i = 1;

		double sCapi = 0, sInte = 0; // curSumCapi = 0, curSumInte = 0;
		// 以下设置首期计息周期
		Date datInteBeginDate = datBeginDate;
		Date datInteEndDate = datFirstRetuDate;
		Calendar speCal = Calendar.getInstance();
		speCal.setTime(FirstRetuDate);
		int speDay = speCal.get(Calendar.DAY_OF_MONTH);
		boolean isLastMoDay = ClsPublic.isLastMoDay(speCal);// 首期是否为当月的最后一天

		double dblTermRate = ClsRateDeal.getRateOfTerm(dblInteRate,
				strRetuFreq, intRetuIncr);
		String strInteCompKind = getInteCompKind(strBillKind,
				intcTerm);
		int DaysOfTerm = getDaysOfTerm(strInteCompKind,
				strRetuFreq, intRetuIncr);
		Map<String, Object> um = new HashMap<String, Object>();
		// 计算还款计划
		for (i = intspTerm; i <= intcTerm; i++) {
			if (ClsPublic.getDoubleV(dbltCapi, 2) <= 0) {
				break;
			}
			if ((i >= intcTerm)) {
				datInteEndDate = datEndRetuDate;
			}
			if (datInteEndDate.getTime() >= datEndRetuDate.getTime()) {
				datInteEndDate = datEndRetuDate;
			}
			um = analyzeCapiInte(datInteBeginDate, datInteEndDate,
					strInteCompKind, dbltCapi, dblInteRate, dblTermRate,
					dblIntePerc, strBillKind, dblAmt, datFirstRetuDate,
					datEndRetuDate, datMaxEndRetuDate, strFirstInteByDay,
					strLastInteByDay, intspTerm, DaysOfTerm);
			sCapi = Double.parseDouble(um.get(ClsConst.PI_CAPI).toString());
			sInte = Double.parseDouble(um.get(ClsConst.PI_INTE).toString());

			// curSumCapi = curSumCapi + ClsPublic.getDoubleV(sCapi, 2);
			// curSumInte = curSumInte + ClsPublic.getDoubleV(sInte, 2);
			// list结构

			RetuPlan rp = (RetuPlan) (new ClsPublic()).new RetuPlan();
			rp.sTerm = i;
			rp.sDate = datInteEndDate;
			rp.saCapi = sCapi;
			rp.saInte = sInte;
			rp.saCapiInte = rp.saCapi + rp.saInte;
			rp.abal = dbltCapi - sCapi;
			list_RetuPlan.add(list_RetuPlan.size(), rp);

			// 本期的计息截至日期设置为下期的计息开始日期
			datInteBeginDate = datInteEndDate;
			// 计算下期计息截至日期，即：应还款日期
			/*
			 * qiao 改计算方式 // 注意以下的“(i - intspTerm + 1) + 1”，颇有玄机！！类似“cTerm -
			 * scTerm + 1”的原理 datInteEndDate =
			 * clsBillPlan.getRetuDate(datFirstRetuDate, datEndRetuDate,
			 * intRetuIncr, strLastTermKind, strRetuFreq, (i - intspTerm + 1) +
			 * 1);
			 */
			// qiao 改计算方式,只取下一个还款日
			datInteEndDate = getRetuDate2(datInteBeginDate,
					datEndRetuDate, intRetuIncr, strRetuFreq);
			if (datInteEndDate.compareTo(datEndRetuDate) < 0
					&& "M".equalsIgnoreCase(strRetuFreq)) {
				// *!*最后一期不能转 只转『还款频率』为“月”的
				if (isLastMoDay) {
					datInteEndDate = ClsPublic.getLastMoDay(datInteEndDate);
				} else {
					datInteEndDate = ClsPublic.setSpeMoDay(datInteEndDate,
							speDay);
				}
			}

			dbltCapi = dbltCapi - sCapi;
		}
		return dbltCapi;
	}

	/**
	 * 本息分解
	 * 
	 * @param LoanAcNo
	 *            贷款账号
	 * @param InteBeginDate
	 *            计息开始日期
	 * @param InteEndDate
	 *            计息结束日期
	 * @param InteCompKind
	 *            计息方式（按期计息、按日计息）【【【注意：该参数取值统一使用ClsCompute.getInteCompKind方法】】】
	 * @param CalcAmt
	 *            计算本金
	 * @param InteRate
	 *            【【【注意】】】此处必须“月利率”（千分之，如：4.59，而非0.00459）（即：年利率 / 12 * 10）
	 * @param TermRate
	 *            期利率（绝对值，不带千分之，可直接用于计算）
	 * @param IntePerc
	 *            当前贴息比例（百分之，如：50，而非0.5）
	 * @param BillKind
	 *            当前账单类型
	 * @param Amt
	 *            当前账单分期还款额
	 * @param FirstRetuDate
	 *            当前账单首次还款日期
	 * @param EndRetuDate
	 *            当前账单还款结束日期
	 * @param MaxEndRetuDate
	 *            所有账单的最后的还款结束日期
	 * @param FirstInteByDay
	 *            当前账单首期按日计息标志
	 * @param LastInteByDay
	 *            当前账单末期按日计息标志
	 * @param spTerm
	 *            当前账单内期次
	 * @param SpeMonth1
	 *            当前账单特殊还款月份1
	 * @param SpeMonth2
	 *            当前账单特殊还款月份2
	 * @param SpeAmt1
	 *            当前账单特殊还款金额1
	 * @param SpeAmt2
	 *            当前账单特殊还款金额2
	 * @param DaysOfTerm
	 *            一期的天数
	 */
	public Map<String, Object> analyzeCapiInte(Date InteBeginDate,
			Date InteEndDate, String InteCompKind, double CalcAmt,
			double InteRate, double TermRate, double IntePerc, String BillKind,
			double Amt, Date FirstRetuDate, Date EndRetuDate,
			Date MaxEndRetuDate, String FirstInteByDay, String LastInteByDay,
			int spTerm, int DaysOfTerm) throws Exception {

		double I = 0, AI = 0, SI = 0, C = 0;
		Map<String, Object> um = new HashMap<String, Object>();
		// 根据本期应还款日期判断，从分期还款额、特殊还款额1、特殊还款额2中取得本期应使用的金额
		// Amt = ClsPublic.getThisTermAmt(InteEndDate, Amt, 0.0,0.0, 0.0, 0.0);

		// 本息拆分分为四个步骤处理：
		// 【首先】：计算sCapi
		if (BillKind.equalsIgnoreCase(ClsConst.BILLKIND_DEBX) || BillKind.equalsIgnoreCase(ClsConst.BILLKIND_DBDX)) {
			// 等额本息时，在不考虑分段计息的情况下，先计算sInte，并通过dblAmt计算sCapi，实际目的是得到“不分段计息情况下的应还本金”
			// 此处对于sInte的四舍五入要在计算sCapi之前，因为，如果直接计算后再舍入，则舍入后的sInte＋sCapi有可能不等于舍入后的dblAmt
			I = ClsPublic.getDoubleV(CalcAmt * TermRate, 2);
			C = Amt - I;
		} else if (BillKind.equalsIgnoreCase(ClsConst.BILLKIND_DEBJ)) {
			C = Amt;
		} else if (BillKind.equalsIgnoreCase(ClsConst.BILLKIND_ZHX)) {
			C = 0;// ClsPublic.getThisTermAmt(InteEndDate, 0, SpeMonth1,
					// SpeMonth2, SpeAmt1, SpeAmt2);
		} else {
			C = 0;
		}

		// 【其次】：针对“按期计息”以及“按日计息”的账单，计算实际的sInte（有分段计息数据则分段计息，无分段计息数据则直接按期或按日计息）
		um = getPI_CapiDBValue(InteBeginDate, InteEndDate, InteCompKind,
				CalcAmt, InteRate, TermRate, IntePerc, DaysOfTerm);
		I = Double.parseDouble(um.get(ClsConst.PI_INTE).toString());
		AI = Double.parseDouble(um.get(ClsConst.PI_S_ALLOINTE).toString());
		SI = Double.parseDouble(um.get(ClsConst.PI_S_SUBSINTE).toString());

		// 【再次】：处理首末期按日计息、以及末期归还所有剩余本金等业务情况。
		// 注意：此处增加“intspTerm == 1”的条件，主要是为了处理“贷款的还款计划”时，将中间某期作为账单的首期的情况（不能按日计息）
		if (InteEndDate.compareTo(FirstRetuDate) == 0
				&& FirstInteByDay.equalsIgnoreCase("1") && spTerm == 1) { // 某账单计划的首期按日计息
			um = getPI_CapiDBValue_Day(InteBeginDate, InteEndDate, CalcAmt,
					InteRate, TermRate, IntePerc);
			I = Double.parseDouble(um.get(ClsConst.PI_INTE).toString());
			AI = Double.parseDouble(um.get(ClsConst.PI_S_ALLOINTE).toString());
			SI = Double.parseDouble(um.get(ClsConst.PI_S_SUBSINTE).toString());
		}
		if (InteEndDate.compareTo(EndRetuDate) == 0
				&& LastInteByDay.equalsIgnoreCase("1")) { // 某账单计划的末期按日计息
			um = getPI_CapiDBValue_Day(InteBeginDate, InteEndDate, CalcAmt,
					InteRate, TermRate, IntePerc);
			I = Double.parseDouble(um.get(ClsConst.PI_INTE).toString());
			AI = Double.parseDouble(um.get(ClsConst.PI_S_ALLOINTE).toString());
			SI = Double.parseDouble(um.get(ClsConst.PI_S_SUBSINTE).toString());
		}
		if (InteEndDate.compareTo(MaxEndRetuDate) == 0) { // 整个方案的末期
			if (LastInteByDay.equalsIgnoreCase("1")) { // 按日计息
				um = getPI_CapiDBValue_Day(InteBeginDate, InteEndDate, CalcAmt,
						InteRate, TermRate, IntePerc);
				I = Double.parseDouble(um.get(ClsConst.PI_INTE).toString());
				AI = Double.parseDouble(um.get(ClsConst.PI_S_ALLOINTE)
						.toString());
				SI = Double.parseDouble(um.get(ClsConst.PI_S_SUBSINTE)
						.toString());
			}
			C = CalcAmt; // 贷款的最后一期还款一次性归还所有本金
		}

		// 【最后】：考虑计算所得本息是否合法以及是否大于余额（或区间余额），然后进行相应调整
		I = ClsPublic.getDoubleV(I, 2) <= 0 ? 0 : I;
		C = ClsPublic.getDoubleV(C, 2) <= 0 ? 0 : C;
		C = ClsPublic.getDoubleV(C - CalcAmt, 2) > 0 ? CalcAmt : C; // 应还本金大于计算本金

		Map<String, Object> unikMap = new HashMap<String, Object>();
		unikMap.put(ClsConst.PI_CAPI, ClsPublic.getDoubleV(C, 2));
		unikMap.put(ClsConst.PI_INTE, ClsPublic.getDoubleV(I, 2));
		unikMap.put(ClsConst.PI_S_ALLOINTE, ClsPublic.getDoubleV(AI, 2));
		unikMap.put(ClsConst.PI_S_SUBSINTE, ClsPublic.getDoubleV(SI, 2));
		return unikMap;
	}

	/**
	 * 计算罚息 复利
	 * 
	 * @param LoanAcNo
	 *            贷款账号
	 * @param datFineBeginDate
	 * @param datCurrDate
	 * @param curCapi
	 * @param curInte
	 * @param curcFineRate
	 * @param curRaLastFine
	 * @param curRbLastFine
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getFine(Date datFineBeginDate, Date datCurrDate,
			double curCapi, double curInte, double curcFineRate,
			double curRaLastFine, double curRbLastFine) throws Exception {

		double raFine = 0, rbFine = 0;
		raFine = this.getPF(datFineBeginDate, datCurrDate, curCapi,
				curcFineRate);
		raFine = raFine <= 0 ? 0 : raFine;

		rbFine = this.getPF(datFineBeginDate, datCurrDate, curInte,
				curcFineRate);
		rbFine = rbFine <= 0 ? 0 : rbFine;

		Map<String, Object> um = new HashMap<String, Object>();
		um.put(ClsConst.AMT_SIGN_AFINE,
				ClsPublic.getDoubleV(raFine + curRaLastFine, 2));
		um.put(ClsConst.AMT_SIGN_BFINE,
				ClsPublic.getDoubleV(rbFine + curRbLastFine, 2));
		return um;
	}

	/**
	 * <b>分段计息</b> 计算罚息
	 * 
	 * @param LoanAcNo
	 *            贷款账号
	 * @param BeginDate
	 *            计息开始日期
	 * @param EndDate
	 *            计息结束日期
	 * @param Capi
	 *            计算本金
	 * @param thisRate
	 *            当前罚息利率
	 */
	public double getPF(Date BeginDate, Date EndDate, double Capi,
			double thisRate) throws Exception {

		Map<String, Object> um = getPI(BeginDate, EndDate,
				ClsConst.INTECOMPKIND_DAY, Capi, thisRate, 0, 0,
				ClsConst.RATE_FINERATE, ClsConst.CAPIMODE_FIXED, 0);
		return Double.parseDouble(um.get(ClsConst.PI_INTE).toString());

	}
	
	public static String getFirstRepayDate(String repayMethod, String interestType,
			String beginDate, String endDate, String dateInPeriod,
			String returnFreq, int returnIncr) {
		String firstReturnDate = "";
		/*
		 * monthEndType 按户定日，月末还款日期约定 1-按实际日期执行 2-按约定日期执行
		 */
		String monthEndType = "2";
		if (("01".equals(repayMethod)) && ("2".equals(interestType))) {// 01-按月等额本息还款法--按户定日
			if ("2".equals(monthEndType)) {
				if ("29".equals(beginDate.substring(8, 10)) || "30".equals(beginDate.substring(8, 10))
						|| "31".equals(beginDate.substring(8, 10))) {
					firstReturnDate = DateUtil.getAfterMonth(beginDate.substring(0, 8) + "01", 2); // 日期格式YYYY-MM-DD
				} else {
					firstReturnDate = DateUtil.getAfterMonth(beginDate, 1); // 日期格式YYYY-MM-DD
				}
			} else {
				firstReturnDate = DateUtil.getAfterMonth(beginDate, 1);
			}
		} else if ("01".equals(repayMethod) && ("1".equals(interestType))) { // 01-按月等额本息还款法--统一定日
			firstReturnDate = DateUtil.getAfterMonth(beginDate.substring(0, 8)
					+ dateInPeriod, 1); // 日期格式 YYYY-MM-DD
		}
		
		
		if (("50".equals(repayMethod)) && ("2".equals(interestType))) {// 01-按月等本等息还款法--按户定日
			if ("2".equals(monthEndType)) {
				if ("29".equals(beginDate.substring(8, 10)) || "30".equals(beginDate.substring(8, 10))
						|| "31".equals(beginDate.substring(8, 10))) {
					firstReturnDate = DateUtil.getAfterMonth(beginDate.substring(0, 8) + "01", 2); // 日期格式YYYY-MM-DD
				} else {
					firstReturnDate = DateUtil.getAfterMonth(beginDate, 1); // 日期格式YYYY-MM-DD
				}
			} else {
				firstReturnDate = DateUtil.getAfterMonth(beginDate, 1);
			}
		} else if ("50".equals(repayMethod) && ("1".equals(interestType))) { // 01-按月等等本等息还款法--统一定日
			firstReturnDate = DateUtil.getAfterMonth(beginDate.substring(0, 8)
					+ dateInPeriod, 1); // 日期格式 YYYY-MM-DD
		}
		
		
		else if (("02".equals(repayMethod)) && ("2".equals(interestType))) { // 01-按月等额本金还款法--按户定日
			if ("2".equals(monthEndType)) {
				if ("29".equals(beginDate.substring(8, 10)) || "30".equals(beginDate.substring(8, 10))
						|| "31".equals(beginDate.substring(8, 10))) {
					firstReturnDate = DateUtil.getAfterMonth(
							beginDate.substring(0, 8) + "01", 2); // 日期格式YYYY-MM-DD
				} else {
					firstReturnDate = DateUtil.getAfterMonth(beginDate, 1); // 日期格式YYYY-MM-DD
				}
			} else {
				firstReturnDate = DateUtil.getAfterMonth(beginDate, 1);
			}
		} else if ("02".equals(repayMethod) && ("1".equals(interestType))) {// 01-按月等额本金还款法--统一定日
			firstReturnDate = DateUtil.getAfterMonth(beginDate.substring(0, 8)
					+ dateInPeriod, 1); // 日期格式 YYYY-MM-DD
		} else if (("11".equals(repayMethod) || "12".equals(repayMethod))
				&& ("2".equals(interestType))) { // 11-按指定周期等额本息还款法
												 // 12-按指定周期等额本金还款法--按户定日 add qiao
			if ("D".equals(returnFreq)) {
				firstReturnDate = DateUtil.getAfterDays(beginDate, returnIncr); // 日期格式YYYY-MM-DD
			} else if ("M".equals(returnFreq)) {
				firstReturnDate = DateUtil.getAfterMonth(beginDate, returnIncr); // 日期格式 YYYY-MM-DD
			}
		} else if ("99".equals(repayMethod)||"98".equals(repayMethod)||"97".equals(repayMethod)) { // 先低后高||先息后本||先无后有
			if( "1".equals(interestType)){ // 统一定日
				firstReturnDate = DateUtil.getAfterMonth(beginDate.substring(0, 8)
						+ dateInPeriod, 1); // 日期格式 YYYY-MM-DD
			}else if("2".equals(interestType)){// 按户定日
				if ("2".equals(monthEndType)) {
					if ("29".equals(beginDate.substring(8, 10)) || "30".equals(beginDate.substring(8, 10))
							|| "31".equals(beginDate.substring(8, 10))) {
						firstReturnDate = DateUtil.getAfterMonth(
								beginDate.substring(0, 8) + "28", 1); // 日期格式YYYY-MM-DD
					} else {
						firstReturnDate = DateUtil.getAfterMonth(beginDate, 1); // 日期格式YYYY-MM-DD
					}
				} else {
					firstReturnDate = DateUtil.getAfterMonth(beginDate, 1);
				}
			}
		} else if ("90".equals(repayMethod)) { // 利随本清
			firstReturnDate = endDate;
		}
		return firstReturnDate;
	}
	
	/**
	 * 计算到期日
	 * 
	 * @throws Exception
	 */
	public static String getTrueEnddate(String repayMethod, String inteBalMethod,
			int loanTerm, String begindate, String endDate,
			String firstReturnDate, String returnFreq, int returnIncr,
			String endInPeriod, String dateInPeriod) throws Exception {
		/*
		 * monthEndType 按户定日，月末还款日期约定 1-按实际日期执行 2-按约定日期执行
		 */
		String monthEndType = "2";
		Date beginDate = ClsPublic.getDate_Strict(begindate);

		if ("90".equals(repayMethod)) {// 利随本清业务不动到期日期
			if ("D".equals(returnFreq)) {
				endDate = JBDate.getEndDate2(begindate, returnIncr);
			} else if ("M".equals(returnFreq)) {
				endDate = DateUtil.dateToString(JBDate.getEndDate2(beginDate, loanTerm * returnIncr));
			}
		} else if ("11".equals(repayMethod) || "12".equals(repayMethod)) { // 11-按指定周期等额本息还款法
																		   // 12-按指定周期等额本息还款法
			if ("D".equals(returnFreq)) {
				endDate = JBDate.getEndDate2(begindate, loanTerm * returnIncr);
			} else if ("M".equals(returnFreq)) {
				endDate = DateUtil.dateToString(JBDate.getEndDate2(beginDate, loanTerm * returnIncr));
			}
		} else {
			if ("1".equals(inteBalMethod)) { // 统一定日，到期日前推一天
				if ("1".equals(endInPeriod)) { // 末期是否为固定还款日
					endDate = JBDate.getEndDate(firstReturnDate, loanTerm - 1);
				} else {
					endDate = DateUtil.dateToString(JBDate.getEndDate(beginDate, loanTerm));
				}
			} else if ("2".equals(inteBalMethod)) {// 按户定日，到期日不前推
				if ("1".equals(monthEndType)) {
					endDate = DateUtil.dateToString(JBDate.getEndDate2(beginDate, loanTerm));
				} else if ("2".equals(monthEndType)) {
					endDate = JBDate.getEndDate(firstReturnDate, loanTerm - 1);
				}
			}
		}
		return endDate;
	}
	
	/**
	 * 根据还款方式获取对账单类型
	 */
	public static String getBillKind(String repayMethod) throws Exception {
		String billKind = "";
		if ("01".equals(repayMethod) || "11".equals(repayMethod)
				|| "99".equals(repayMethod) || "98".equals(repayMethod)||"97".equals(repayMethod)) { // 等额本息
			billKind = "01";
		} else if ("02".equals(repayMethod) || "12".equals(repayMethod)) { // 等额本金
			billKind = "02";
		} else if ("90".equals(repayMethod)) { // 利随本清
			billKind = "04";
		}
		else if ("50".equals(repayMethod)) { // 等本等息
			billKind = "50";
		}
		return billKind;
	}
	
	/**
	 * 根据还款方式获取还款增量
	 */
	public static int getRepayIncrement(String repayMethod, int returnIncr)
			throws Exception {
		int returnIncrEnd = 0;
		if ("01".equals(repayMethod) || "02".equals(repayMethod) || "50".equals(repayMethod)
				|| "99".equals(repayMethod) || "98".equals(repayMethod)||"97".equals(repayMethod)) {
			returnIncrEnd = 1;
		} else if ("11".equals(repayMethod) || "12".equals(repayMethod)) {
			returnIncrEnd = returnIncr;
		} else if ("90".equals(repayMethod)) {
			returnIncrEnd = 0;
		}
		return returnIncrEnd;
	}

	/**
	 * 根据还款方式获取还款频率
	 */
	public static String getRepayFreq(String repayMethod, String returnFreq)
			throws Exception {
		String returnFreqEnd = "";
		if ("01".equals(repayMethod) || "02".equals(repayMethod)|| "50".equals(repayMethod)
				|| "99".equals(repayMethod) || "98".equals(repayMethod)||"97".equals(repayMethod)) {
			returnFreqEnd = "M";
		} else if ("11".equals(repayMethod) || "12".equals(repayMethod)) {
			returnFreqEnd = returnFreq;
		} else if ("90".equals(repayMethod)) {
			returnFreqEnd = "M";
		}
		return returnFreqEnd;
	}
	
	/**
	 * 得到计息方式
	 * 
	 * @param BillKind
	 *            当前账单类型
	 * @return
	 */
	public String getInteCompKind(String strBillKind, int intcTerm)
			throws Exception {
		if (ClsConst.BILLKIND_DEBX.equalsIgnoreCase(strBillKind)
				|| ClsConst.BILLKIND_DEBJ.equalsIgnoreCase(strBillKind)
				|| ClsConst.BILLKIND_DELJ.equalsIgnoreCase(strBillKind)
				|| ClsConst.BILLKIND_DBLJ.equalsIgnoreCase(strBillKind)
				|| ClsConst.BILLKIND_DBDX.equalsIgnoreCase(strBillKind)) {
			return ClsConst.INTECOMPKIND_TERM;
		} else if (ClsConst.BILLKIND_ZHX.equalsIgnoreCase(strBillKind)
				|| ClsConst.BILLKIND_HBHX.equalsIgnoreCase(strBillKind)) {
			// return 1 == intcTerm ? ClsConst.INTECOMPKIND_DAY :
			// ClsConst.INTECOMPKIND_TERM;
			return ClsConst.INTECOMPKIND_DAY;
		} else {
			throw new NullPointerException("BillKindNotFound");
		}
	}
	
	/**
	 * 得到一期的天数
	 * 
	 * @param InteCompKind
	 *            计息方式（按期计息、按日计息）
	 * @param RetuFreq
	 *            当前还款频率
	 * @param RetuIncr
	 *            当前还款增量
	 * @return
	 */
	public int getDaysOfTerm(String InteCompKind, String RetuFreq, int RetuIncr)
			throws Exception {
		if (ClsConst.INTECOMPKIND_DAY.equalsIgnoreCase(InteCompKind)) {
			return 0;
		} else if (ClsConst.INTECOMPKIND_DAY_M_D.equalsIgnoreCase(InteCompKind)) {
			return 0;
		} else if (ClsConst.INTECOMPKIND_TERM.equalsIgnoreCase(InteCompKind)) {
			if (ClsConst.RETUFREQ_DAY.equalsIgnoreCase(RetuFreq)) {
				return RetuIncr;
			} else if (ClsConst.RETUFREQ_MONTH.equalsIgnoreCase(RetuFreq)) {
				return RetuIncr * 30;
			} else if (ClsConst.RETUFREQ_HALF_OF_MONTH.equalsIgnoreCase(RetuFreq)) {
				throw new NullPointerException("RetuFreqNotFound");
			} else if (ClsConst.RETUFREQ_PAYOFF.equalsIgnoreCase(RetuFreq)) {
				throw new NullPointerException("RetuFreqNotFound");
			} else {
				throw new NullPointerException("RetuFreqNotFound");
			}
		} else {
			throw new NullPointerException("InteCompKindNotFound");
		}
	}
	
	/**
	 * <b>账单计划 add by qiao</b> 取得还款日期 新增无需判断 期末约定 只取下一个还款日
	 * 
	 * @param BegDate
	 *            当期开始日期
	 * @param EndDate
	 *            整笔贷款结束日期
	 * @param RetuIncr
	 *            还款增量
	 * @param RetuFreq
	 *            还款频率 D:按日 M:按月
	 * @return util.Date 下一期的还款日期
	 * @throws Exception
	 */
	public java.util.Date getRetuDate2(java.util.Date BegDate,
			java.util.Date EndDate, int RetuIncr, String RetuFreq)
			throws Exception {

		RetuFreq = ClsPublic.GetStrV(RetuFreq);
		Calendar BgDate = Calendar.getInstance();
		BgDate.setTime(BegDate);
		Calendar EdDate = Calendar.getInstance();
		EdDate.setTime(EndDate);
		Calendar countDate = Calendar.getInstance();
		countDate.setTime(BegDate);

		if (RetuFreq.equalsIgnoreCase("D")) {
			countDate.add(Calendar.DAY_OF_MONTH, RetuIncr);
		} else if (RetuFreq.equalsIgnoreCase("M")) {
			countDate.add(Calendar.MONTH, RetuIncr);
		}
		java.util.Date RetuDate = new java.util.Date(
				countDate.getTimeInMillis());
		if (RetuDate.getTime() >= EndDate.getTime()) {
			RetuDate = EndDate;
		}
		return RetuDate;
	}



}
