package loan;

/**
 * <b>计算利率信息</b>
 * 
 * @author yujw@jbbis.com.cn
 *
 */
public class ClsRateDeal {

	public ClsRateDeal() throws Exception {
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

	public static double getRateOfTerm(double InteRate, String strRetuFreq, int intRetuIncr) {

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
}
