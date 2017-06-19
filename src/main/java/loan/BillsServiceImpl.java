package loan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BillsServiceImpl{
	
//	@Resource
//	private TBillsMapper tBillsMapper;
	
	ClsCompute clsCompute = new ClsCompute();
	
	/**
	 * 生成账单计划
	 * 
	 * @param requestMap
	 *            :试算请求参数
	 * @throws Exception
	 */
	public List<Map<String, Object>> billplaninsert(Map<String, Object> requestMap) throws Exception {
		String repayMethod = ClsPublic.GetStrV(requestMap.get("repayMethod")); // 还款方式 98-带宽限期等额本息（先息后本），99-带宽限期等额本息（前低后高），
		// 01-按月等额本息还款法，02-按月等额本金还款法，97-带宽限期等额本息（先无后有） 90-利随本清 11-按指定周期等额本息还款法，12-按指定周期等额本金还款法
		double interestRate = ClsPublic.GetCurV(requestMap.get("interestRate")); // 贷款利率
		int cterm = ClsPublic.GetIntV(requestMap.get("periods").toString()); // 还款期次
		String endDate = ClsPublic.GetStrV(requestMap.get("endDate")); // 到期日期
		String beginDate = ClsPublic.GetStrV(requestMap.get("beginDate")); // 放款日期
		String inteBalMethod = ClsPublic.GetStrV(requestMap.get("inteBalMethod")); // 结息方式
		String intcalcMethod = ClsPublic.GetStrV(requestMap.get("intcalcMethod"));// 计息方式
		double loanAmount = ClsPublic.GetCurV(requestMap.get("loanAmount")); // 贷款金额
		int reqFreq = ClsPublic.GetIntV(requestMap.get("reqFreq").toString()); // （还本金）宽限期
		String dateInPeriod = ClsPublic.GetStrV(requestMap.get("dateInPeriod")); // 每期固定还款日
		String returnFreq = ClsPublic.GetStrV(requestMap.get("returnFreq"));
		int returnIncr = ClsPublic.GetIntV(requestMap.get("returnIncr").toString()); // 还款增量
		String endInPeriod = ClsPublic.GetStrV(requestMap.get("endInPeriod")); // 末期是否为固定还款日
		String fstRetuDateMode = ClsPublic.GetStrV(requestMap.get("fstRetuDateMode")); // 首次还款日期方式
		String firstReturnDate = ClsPublic.GetStrV(requestMap.get("firstReturnDate")); // 首次还款日

		String billKind = ""; // 账单类型

		// 计算首次还款日
		String countfirstReturnDate =  ClsCompute.getFirstRepayDate(repayMethod, inteBalMethod, beginDate, endDate, dateInPeriod,
				returnFreq, returnIncr);
//		String countfirstReturnDate = clsBillPlan.getFirstRepayDate(
//				repayMethod, inteBalMethod, beginDate, endDate, dateInPeriod,
//				returnFreq, returnIncr);
		// 统一定日，指定首次还款日期，并且首次还款日期不为空
		if ("1".equals(fstRetuDateMode) && "1".equalsIgnoreCase(inteBalMethod) && firstReturnDate != null) {
			if (firstReturnDate.compareTo(beginDate) < 0) {
				throw new Exception("首次还款日期小于放款日期!!!");
			}
		} else {
			firstReturnDate = countfirstReturnDate;
		}
		// 重新计算到期日期
		endDate = ClsCompute.getTrueEnddate(repayMethod, inteBalMethod, cterm,
				beginDate, endDate, firstReturnDate, returnFreq, returnIncr, endInPeriod, dateInPeriod);
//		endDate = clsBillPlan.getTrueEnddate(repayMethod, inteBalMethod, cterm,
//				beginDate, endDate, firstReturnDate, returnFreq, returnIncr, endInPeriod, dateInPeriod);
		// 增加针对只有1期账单的数据特殊处理，首次还款日期=到期日期
		if (cterm == 1) {
			firstReturnDate = endDate;
		}
		// 对账单类型进行处理
		billKind = ClsCompute.getBillKind(repayMethod);
		// 还款增量
		returnIncr = ClsCompute.getRepayIncrement(repayMethod, returnIncr);
		// 还款频率
		returnFreq = ClsCompute.getRepayFreq(repayMethod, returnFreq);

		Map<String, Object> upMap = new HashMap<String, Object>();
		upMap.put("returnType", repayMethod); // 还款方式
		upMap.put("firstReturnDate", firstReturnDate);// 首次还款日
		upMap.put("beginDate", beginDate);// 到期日
		upMap.put("endDate", endDate);// 到期日
		upMap.put("loanAmount", loanAmount);// 贷款金额
		upMap.put("interestType", inteBalMethod);// 结息方式
		upMap.put("lastOperId", "AUTO-ID");// 最后操作人
		upMap.put("lastOperBankId", "");// 最后机构
		upMap.put("endReturnDate", endDate); // 还款结束日期
		upMap.put("billKind", billKind); // 账单类型
		upMap.put("returnIncr", returnIncr); // 还款增量
		upMap.put("returnFreq", returnFreq); // 还款频率
		// mod by chenye 增加计息方式处理
		if ("2".equals(intcalcMethod)) {
			upMap.put("firstIntebyDay", "1"); // 首期按日计息
			upMap.put("lastIntebyDay", "1"); // 末期按日计息
		} else {
			upMap.put("firstIntebyDay", "0"); // 首期按期计息
			upMap.put("lastIntebyDay", "0"); // 末期按期计息
		}
		upMap.put("rate", interestRate);

		LinkedList<Map<String, Object>> billPlanlist = new LinkedList<Map<String, Object>>();
		if ("90".equals(repayMethod)) {
			// 还款方式是利随本清的情况
			upMap.put("installmentPayments", loanAmount);
			upMap.put("cterm", cterm); // 还款期数
			upMap.put("billNum", "1");
			upMap.put("lastTermKind", "1"); // 期末还款约定
			// 生成账单计划
			billPlanlist.add(upMap);
		} else if ("11".equals(repayMethod) || "12".equals(repayMethod)) {
			// 按指定周期等额本息还款法 add by qiao
			double Amt = 0; // 分期还款额
			double aBal = loanAmount;
			int cTerm = cterm;
			double TermRate = ClsRateDeal.getRateOfTerm(interestRate, returnFreq, returnIncr);
			Amt = clsCompute.A(billKind, aBal, cTerm, TermRate, 0, 0, 0);
			upMap.put("installmentPayments", Amt);// 分期还款额
			upMap.put("cterm", cTerm); // 还款期数
			upMap.put("billNum", "1");
			upMap.put("lastTermKind", "0"); // 期末还款约定
			// 生成账单计划
			billPlanlist.add(upMap);
		} else if ("01".equals(repayMethod) || "02".equals(repayMethod) || "50".equals(repayMethod)) {
			// 按月等额本息或按月等额本金的情况
			double Amt = 0;
			double aBal = loanAmount;
			int cTerm = cterm;
			double TermRate = ClsRateDeal.getRateOfTerm(interestRate, "M", 1);
			Amt = clsCompute.A(billKind, aBal, cTerm, TermRate, 0, 0, 0);
			upMap.put("installmentPayments", Amt);// 分期还款额
			upMap.put("cterm", cTerm); // 还款期数
			upMap.put("billNum", "1");
			// 生成账单计划
			billPlanlist.add(upMap);
		} else if ("99".equals(repayMethod) || "98".equals(repayMethod)) {
			// 带宽限期的等额本息还款法
			int reqfreq = reqFreq; // 宽限期
			String FirstRetuDate = firstReturnDate; // 开始日期
			String EndRetuDate = JBDate.getEndDate(firstReturnDate, reqfreq - 1); // 结束日期
			String RetuFreq = "M"; // 还款频率
			upMap.put("cterm", reqfreq); // 还款期数
			upMap.put("firstReturnDate", FirstRetuDate); // 首次还款日期
			upMap.put("endReturnDate", EndRetuDate); // 还款结束日期
			int intcTerm = reqfreq;
			double dblaBal = loanAmount;
			double dblTermRate = 0;
			double dblmAmt = 0;
			String reqfreqMonth = String.valueOf(reqFreq);
			upMap.put("skipmonth", reqfreqMonth); // 宽限期
			upMap.put("firstintebyday", "1"); // 首期按日计息
			upMap.put("lastintebyday", "0"); // 末期按日计息
			int intRetuIncr = 1;
			dblTermRate = ClsRateDeal.getRateOfTerm(interestRate, RetuFreq, intRetuIncr);
			dblmAmt = clsCompute.A("03", dblaBal, intcTerm, dblTermRate, 0, 0, 0); // 计算宽限期内的还款额
			upMap.put("installmentPayments", ClsPublic.formatDecimal(dblmAmt, 2)); // 分期还款额
			upMap.put("billKind", "03"); // 账单类型
			upMap.put("billNum", 1); // 账单定义编号

			// 生成账单计划
			billPlanlist.add(0, upMap);
			// 以上为宽限期内的账单计划
			Map<String, Object> upMap2 = new HashMap<String, Object>();
			upMap2.putAll(upMap);
			// 以下为宽限期后的账单计划
			String FirstRetuDate2 = JBDate.getEndDate(firstReturnDate, reqfreq); // 宽限期后的开始还款日期
			String EndRetuDate2 = endDate; // 结束日期
			intcTerm = cterm - reqfreq;
			upMap2.put("cterm", intcTerm);// 宽限期后的期数
			upMap2.put("firstReturnDate", FirstRetuDate2); // 宽限期后的首次还款日
			upMap2.put("endReturnDate", EndRetuDate2);// 到期日期
			String strRetuFreq = "M";
			intRetuIncr = 1;
			dblTermRate = ClsRateDeal.getRateOfTerm(interestRate, strRetuFreq, intRetuIncr);
			dblmAmt = clsCompute.A("01", dblaBal, intcTerm, dblTermRate, 0, 0, 0);// 计算宽限期外的分期还款额度
			upMap2.put("installmentPayments", ClsPublic.formatDecimal(dblmAmt, 2)); // 分期还款额
			upMap2.put("firstIntebyDay", "0"); // 首期按日计息
			upMap2.put("lastIntebyDay", "1"); // 末期按日计息
			upMap2.put("billKind", "01"); // 账单类型
			upMap2.put("billNum", 2); // 账单定义编号
			
			// 生成账单计划
			billPlanlist.add(1, upMap2);

		}else if("97".equals(repayMethod) ){
			int reqfreq = reqFreq;// 宽限期
			int intcTerm = reqfreq;
			double dblaBal = loanAmount;
			double dblTermRate = 0;
			double dblmAmt = 0;
			String reqfreqMonth = String.valueOf(reqFreq);
			upMap.put("skipmonth", reqfreqMonth); // 宽限期
			int intRetuIncr = 1;
			String firstRetuDate1 = JBDate.getEndDate(firstReturnDate, reqfreq); // 宽限期后的开始还款日期
			Date freqLastRetuDate = ClsPublic.getDate_Strict(JBDate.getEndDate(firstReturnDate, reqfreq-1));//最后一个宽限期内还款日期
			intcTerm = cterm - reqfreq;
			upMap.put("cterm", intcTerm); // 宽限期后的期数
			upMap.put("firstReturnDate", firstRetuDate1); // 宽限期后的首次还款日
			upMap.put("endReturnDate", endDate); // 到期日期
			String strRetuFreq = "M";
			dblTermRate = ClsRateDeal.getRateOfTerm(interestRate, strRetuFreq, intRetuIncr);
			Date beginDate1 = ClsPublic.getDate_Strict(beginDate);
            //分摊总额
			HashMap<String, Object> um = new HashMap<String,Object>();
			um = clsCompute.getPI_CapiFixed_Day(beginDate1, freqLastRetuDate, dblaBal, interestRate, dblTermRate, 0);
			double graceInteAmt = ClsPublic.getDoubleV(Double.parseDouble(um.get(ClsConst.PI_INTE).toString()), 2);
			//分摊平均额
			double aveGraceInteAmt = ClsPublic.getDoubleV(graceInteAmt/intcTerm, 2);
			upMap.put("graceInteAmt", graceInteAmt);
			upMap.put("aveGraceInteAmt", aveGraceInteAmt);
			dblmAmt = clsCompute.A("01", dblaBal, intcTerm, dblTermRate, 0, 0, 0); // 计算宽限期外的分期还款额度
			upMap.put("installmentPayments", ClsPublic.formatDecimal(dblmAmt, 2)); // 分期还款额
			upMap.put("firstIntebyDay", "0"); // 首期按期计息
			upMap.put("lastIntebyDay", "1"); // 末期按日计息
			upMap.put("billKind", "01"); // 账单类型
			upMap.put("billNum", 1); // 账单定义编号
			// 生成账单计划
			billPlanlist.add(upMap);
			}
		/*
		 * 生成静态账单 包括本息账单
		 */
		return geneStaticBills(billPlanlist);
	}
	
	/**
	 * 生成静态账单
	 */
	@SuppressWarnings("rawtypes")
	private List<Map<String, Object>> geneStaticBills(LinkedList<Map<String, Object>> dateList) throws Exception {
		ClsCompute clsCompute = new ClsCompute();
		double abal = 0;
		double IntePerc = 0;
		int spTerm = 1;
		LinkedList list = null;
		Date datInteBeginDate = null;
		Date datEndRetuDate = null;
		int intRecordOrder = 1;

		for (int i = 0; i < dateList.size(); i++) {
			datInteBeginDate = JBDate.String2Date(dateList.get(i).get("beginDate").toString(), "yyyy-MM-dd");
			if (intRecordOrder != 1) {
				datInteBeginDate = datEndRetuDate; // 除第一条以外，设置下一条账单的开始计息日期为上一条的还款结束日期
				spTerm = 1; // 除第一条意外，设置开始计算还款计划期次为：1
			} else {
				abal = Double.parseDouble(dateList.get(i).get("loanAmount").toString());
			}
			String strBillKind = dateList.get(i).get("billKind").toString();
			String strRetuFreq = dateList.get(i).get("returnFreq").toString();
			int intRetuIncr = Integer.parseInt(dateList.get(i).get("returnIncr").toString());
			Date FirstRetuDate = JBDate.String2Date(
					dateList.get(i).get("firstReturnDate").toString(), "yyyy-MM-dd");
			Date datFirstRetuDate = FirstRetuDate;
			datEndRetuDate = JBDate.String2Date(dateList.get(i).get("endReturnDate").toString(), "yyyy-MM-dd");
			Date datMaxEndRetuDate = JBDate.String2Date(dateList.get(i).get("endDate").toString(), "yyyy-MM-dd");
			String strLastTermKind = ClsPublic.GetStrV(dateList.get(i).get("lastTermKind"));
			int cterm = Integer.parseInt(dateList.get(i).get("cterm").toString());
			String strFirstInteByDay = dateList.get(i).get("firstIntebyDay").toString();
			String strLastInteByDay = dateList.get(i).get("lastIntebyDay").toString();
			double amt = Double.parseDouble(dateList.get(i).get("installmentPayments").toString());
			double rate = Double.parseDouble(dateList.get(i).get("rate").toString());
			intRecordOrder++;
			abal = clsCompute.QueryPlan(FirstRetuDate, datInteBeginDate, abal,
					rate, IntePerc, strBillKind, strRetuFreq, intRetuIncr,
					datFirstRetuDate, datEndRetuDate, strLastTermKind, cterm,
					amt, datMaxEndRetuDate, strFirstInteByDay,
					strLastInteByDay, spTerm);

		}

		list = clsCompute.list_RetuPlan;
		int count = list.size();
		List<Map<String, Object>> billsList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < count; i++) {
			ClsPublic.RetuPlan rp = (ClsPublic.RetuPlan) list.get(i);
			Map<String, Object> billMap = new HashMap<String, Object>();
			billMap.put("sCapi", ClsPublic.formatDecimal(rp.saCapi, 2));
			double sInte = 0;
			if("97".equals(dateList.get(0).get("returnType"))){
				double graceInteAmt = ClsPublic.getDoubleV(ClsPublic.GetCurV(dateList.get(0).get("graceInteAmt")), 2);
				double aveGraceInteAmt = ClsPublic.getDoubleV(ClsPublic.GetCurV(dateList.get(0).get("aveGraceInteAmt")),2);
				if(i == count-1){//宽限期内最后一期应平摊利息=总平摊利息-前几期利息之和
					aveGraceInteAmt = graceInteAmt - aveGraceInteAmt*(count-1);
				}
				sInte = rp.saInte+aveGraceInteAmt;//应还利息加分期平摊额
			}else{
				sInte = ClsPublic.getDoubleV(rp.saInte, 2);
			}
			billMap.put("sInte", sInte);
			billMap.put("sDate", ClsPublic.formatDate(rp.sDate, "yyyy-MM-dd"));
			billMap.put("sTerm", String.valueOf(i + 1));
			System.out.println("期数：" + billMap.get("sTerm") + "----应还本金：" + billMap.get("sCapi") + "----应还利息：" + billMap.get("sInte") + "----应还日期：" + billMap.get("sDate"));
			billsList.add(billMap);
		}

		return billsList;
	}

//	@Override
//	public List<TBills> selectAccount(Map<String, Object> paramMap) {
//		
//		return tBillsMapper.selectAcountsDue(paramMap);
//				
//	}
//	
//	/**
//	 * 获取账单的（应还本-实还本）总和
//	 */
//	public BigDecimal getCapiSum(){
//		return tBillsMapper.getCapiSum();		
//	}
//	/**
//	 * 
//	 * @Title: getInte 
//	 * @Description: 账单的实还利息(rInte)+罚息(raFine+rbFine)
//	 * @return
//	 * @author wfr
//	 * 2016年7月26日
//	 */
//	public BigDecimal getInteSum(){
//		return tBillsMapper.getInteSum();
//	}
	
	public static void main(String[] args) throws Exception {
		
//		String repayMethod = ClsPublic.GetStrV(requestMap.get("repayMethod")); // 还款方式
		//还款方式 98-带宽限期等额本息（先息后本），99-带宽限期等额本息（前低后高），
		// 01-按月等额本息还款法，02-按月等额本金还款法，97-带宽限期等额本息（先无后有） 90-利随本清 11-按指定周期等额本息还款法，12-按指定周期等额本金还款法
//		double interestRate = ClsPublic.GetCurV(requestMap.get("interestRate")); // 贷款利率
//		int cterm = ClsPublic.GetIntV(requestMap.get("periods").toString()); // 还款期次
//		String endDate = ClsPublic.GetStrV(requestMap.get("endDate")); // 到期日期
//		String beginDate = ClsPublic.GetStrV(requestMap.get("beginDate")); // 放款日期
//		String inteBalMethod = ClsPublic.GetStrV(requestMap.get("inteBalMethod")); // 结息方式 1统一定日  2按户定日
//		String intcalcMethod = ClsPublic.GetStrV(requestMap.get("intcalcMethod"));// 计息方式  1.按期计息 2.按日计息
//		double loanAmount = ClsPublic.GetCurV(requestMap.get("loanAmount")); // 贷款金额
//		int reqFreq = ClsPublic.GetIntV(requestMap.get("reqFreq").toString()); // （还本金）宽限期
//		String dateInPeriod = ClsPublic.GetStrV(requestMap.get("dateInPeriod")); // 每期固定还款日
//		String returnFreq = ClsPublic.GetStrV(requestMap.get("returnFreq"));
//		int returnIncr = ClsPublic.GetIntV(requestMap.get("returnIncr").toString()); // 还款增量
//		String endInPeriod = ClsPublic.GetStrV(requestMap.get("endInPeriod")); // 末期是否为固定还款日
//		String fstRetuDateMode = ClsPublic.GetStrV(requestMap.get("fstRetuDateMode")); // 首次还款日期方式
//		String firstReturnDate = ClsPublic.GetStrV(requestMap.get("firstReturnDate")); // 首次还款日
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("repayMethod", "50");
		requestMap.put("interestRate", "12");
		requestMap.put("periods", "12");
		requestMap.put("endDate", "2018-06-15");
		requestMap.put("beginDate", "2017-06-15");
		requestMap.put("inteBalMethod", "1");
		requestMap.put("intcalcMethod", "1");
		requestMap.put("loanAmount", "12000");
		requestMap.put("reqFreq", "1");
		requestMap.put("dateInPeriod", "1");
		requestMap.put("returnFreq", "M");
		requestMap.put("returnIncr", "1");
		requestMap.put("endInPeriod", "2");
		requestMap.put("fstRetuDateMode", "2");
		requestMap.put("firstReturnDate", "2017-07-15");
		BillsServiceImpl bill = new BillsServiceImpl();
		bill.billplaninsert(requestMap);
		
	}
		
}
