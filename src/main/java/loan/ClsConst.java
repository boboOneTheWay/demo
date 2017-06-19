package loan;

/**
 * 
 * 常数定义
 * 
 */
public class ClsConst {

	/* 账务规则 */
	public final static String OLD_ACKIND = "01"; // 旧准则(适应应计非应计)
	public final static String NEW_ACKIND = "02"; // 新准则（适应正常减值）
	/** 南京银行机构代码 */
	public final static String BANK_SIGN_NJCB = "XXX";

	public final static String SUBJ_BAL_WAY_DEBIT = "0"; // 余额方向：借方
	public final static String SUBJ_BAL_WAY_CREDIT = "1"; // 余额方向：贷方

	/** 贷款应收利息 */
	public final static String SUBJ_ID_INTE_S_I = "1897";
	/** 表外应收未收利息 */
	public final static String SUBJ_ID_INTE_R_I = "7068";

	public final static String ATTR_B_NORM = "0"; // 正常
	public final static String ATTR_B_OVER_LESS_90 = "1"; // 利息违约天数小于90天/本金违约
	public final static String ATTR_B_OVER_EQUAL_90_FIRST = "2"; // 利息违约天数等于90天（等于90天，第一天数据）
	public final static String ATTR_C_ENDDATE_CURR = "3"; // 本金到期且为当期
	public final static String ATTR_C_ENDDATE_OLD = "4"; // 本金到期且为前期（前期是指扣款数据是前期产生的）
	public final static String ATTR_C_EXCEED = "5"; // 本金过期
	public final static String ATTR_B_OVER_MORE_90_OLD = "6"; // 利息违约天数大于90天（非第一天数据）
	public final static String ATTR_B_OVER_MORE_90_CURR = "7"; // 利息违约天数大于90天新产生欠息（第一天数据），特点：违约利息天数已经在90天以上，并且是新产生的利息

	public final static String OPER_ID_AUTO = "AUTO"; // 批量扣款的操作员、记账员、复核员

	/** 大本大息 */
	public final static String SUBS_RULE_MAX = "0";
	/** 小本小息 */
	public final static String SUBS_RULE_MIN = "1";
	/** 不需要按照金额类型分类 */
	public final static String AMT_SIGN_NULL = "0"; // 不需要按照金额类型分类
	/** 金额类型_本金 */
	public final static String AMT_SIGN_CAPI = "1"; // 本金
	/** 金额类型_利息 */
	public final static String AMT_SIGN_INTE = "2"; // 利息
	/** 金额类型_本金罚息 */
	public final static String AMT_SIGN_AFINE = "3"; // 本金罚息
	/** 金额类型_利息罚息 */
	public final static String AMT_SIGN_BFINE = "4"; // 利息罚息
	/** 金额类型_本金罚息复利 */
	public final static String AMT_SIGN_CFINE = "5"; // 本金罚息复利
	/** 利息罚息复利 */
	public final static String AMT_SIGN_DFINE = "6"; // 利息罚息复利
	/** 金额类型_违约金 */
	public final static String AMT_SIGN_FOUL = "7"; // 违约金
	/** 抵质押物 */
	public final static String AMT_SIGN_ASSU = "8"; // 抵质押物
	/** 担保物转抵债资产和抵债资产受理 */
	public final static String AMT_SIGN_DEBT = "9"; // 担保物转抵债资产和抵债资产受理
	/** 受托支付金额 */
	public final static String AMT_SIGN_DEPEPAY = "X"; // 受托支付金额
	/** 金融债金额 */
	public final static String AMT_SIGN_FINADEBT = "Y"; // 金融债金额
	/** 贴息金额 */
	public final static String AMT_SIGN_SALLOINTE = "Z"; // 贴息金额
	/** 计提额 */
	public final static String AMT_SIGN_RECE = "A"; // 计提金额
	/** 支付金额 */
	public final static String AMT_SIGN_PAY = "B";
	/** 交易费用 */
	public final static String AMT_SIGN_FEE = "C";
	/** 利息调整额 */
	public final static String AMT_SIGN_ADJU = "D";
	/** 实际利息收入 */
	public final static String AMT_SIGN_FACHINTE = "E";
	/** 计提本金罚息 */
	public final static String AMT_SIGN_PRORECEFINE = "K";
	/** 已入账本金罚息 */
	public final static String AMT_SIGN_ACSAFINE = "H";
	/** 未入账本金罚息 */
	public final static String AMT_SIGN_NOACSAFINE = "I";
	/** 核销本金 */
	public final static String AMT_SIGN_CANCELCAPI = "P";
	/** 核销利息 */
	public final static String AMT_SIGN_CANCELINTE = "Q";

	public final static String LIST_REM_BATCH_AC = "98"; // 批量记账
	public final static String LIST_REM_BATCH_ADDAC = "99"; // 批量补记账

	public final static String LIST_TYPE_BATCH_DATA = "00"; // 批量扣款数据
	public final static String LIST_TYPE_BATCH_SUBS = "01"; // 批量扣款
	public final static String LIST_TYPE_RETU_NORM = "02"; // 还正常贷款
	public final static String LIST_TYPE_RETU_AHEA = "03"; // 提前还款
	public final static String LIST_TYPE_RETU_AHEA_CSIGN = "05"; // 批量提前还款
	public final static String LIST_TYPE_RETU_OVER = "04"; // 还逾期贷款
	public final static String LIST_TYPE_RETU_FREE = "07"; // 特殊还款处理
	public final static String LIST_TYPE_CHG_TERM = "08"; // 期限调整
	public final static String LIST_TYPE_CALC_INTE = "10"; // 结息
	public final static String LIST_TYPE_ALLO_INTE = "11"; // 贴息
	public final static String LIST_TYPE_CHG_RATE_SINGLE = "32"; // 单户利率调整
	public final static String LIST_TYPE_CHG_RETU_KIND = "33"; // 还款方式变更
	public final static String LIST_TYPE_BAD_CANCEL = "34"; // 贷款核销
	public final static String LIST_TYPE_CHG_AC_FLAG = "35"; // 账户状态调整
	public final static String LIST_TYPE_BAD_RETU = "36"; // 贷款核销收回
	public final static String LIST_TYPE_RELE = "41"; // 贷款发放
	public final static String LIST_TYPE_TURN_OVER_BATCH = "51"; // 批量转逾期
	public final static String LIST_TYPE_TURN_OVER_SINGLE = "52"; // 手工转逾期
	public final static String LIST_TYPE_DEBT_ASSU = "61"; // 担保物转抵债资产
	public final static String LIST_TYPE_DEBT_ACCEPT = "62"; // 抵债资产受理
	public final static String LIST_TYPE_FINISH = "71"; // 业务终结

	public final static String LIST_TYPE_GEAR_DEVALUE = "AA"; // 正常转减值
	public final static String LIST_TYPE_DEVALUE_GEAR = "AB"; // 减值转正常
	public final static String LIST_TYPE_RECE = "BB"; // 减值转正常

	public final static String BUSI_TYPE_RETU_NORM = "00"; // 还正常贷款
	public final static String BUSI_TYPE_RETU_OVER = "01"; // 还逾期贷款
	public final static String BUSI_TYPE_RETU_OVER_WAIT = "02"; // 还待转逾期贷款
	public final static String BUSI_TYPE_RETU_FEES = "06"; // 费用扣收
	public final static String BUSI_TYPE_RETU_FREE = "07"; // 特殊还款处理
	public final static String BUSI_TYPE_CHG_TERM = "08"; // 期限调整
	public final static String BUSI_TYPE_RETU_AHEA_PAYOFF = "10"; // 提前结清
	public final static String BUSI_TYPE_RETU_AHEA_PAYPART = "12"; // 部分提前还款
	public final static String BUSI_TYPE_CHG_RATE = "32"; // 单户利率调整
	public final static String BUSI_TYPE_CHG_RETUKIND = "33"; // 还款方式变更
	public final static String BUSI_TYPE_BAD_CANCEL = "34"; // 贷款核销
	public final static String BUSI_TYPE_CHG_ACFLAG = "35"; // 帐户状态调整
	public final static String BUSI_TYPE_RELE = "41"; // 贷款发放
	public final static String BUSI_TYPE_TURN_OVER_BATCH = "51"; // 批量转逾期
	public final static String BUSI_TYPE_TURN_OVER_SINGLE = "52"; // 手工转逾期
	public final static String BUSI_TYPE_DEBT_FROM_ASSU = "61"; // 担保物转抵债资产
	public final static String BUSI_TYPE_DEBT_DEAL = "62"; // 抵债资产受理

	/** 贴息比例，贴息开始日期，贴息截止日期调整 */
	public final static String LIST_TYPE_ADJU_ALLO_INTE = "91";
	/** 其他导致利率变化的处理，如：期限调整、单户利率调整 */
	public final static String LIST_TYPE_ADJU_RATE_OTHER = "92";
	/** 批量利率调整 */
	public final static String LIST_TYPE_ADJU_RATE_BATCH = "93";
	/** 提前还款类型中增加了提前还本 */
	public final static String LIST_TYPE_AHEARETU_CAPI = "94";
	/** 固定利率 */
	public final static String INTE_ADJU_KIND_FIXED = "4";
	/** 立即调整 */
	public final static String INTE_ADJU_KIND_IMME = "0";
	/** 下年初调整 */
	public final static String INTE_ADJU_KIND_NEXT_YEAR = "2";
	/** 下月调整 */
	public final static String INTE_ADJU_KIND_NEXT_MONTH = "1";
	/** 下季调整 */
	public final static String INTE_ADJU_KIND_NEXT_SEASON = "3";
	/** 对年调整 */
	public final static String INTE_ADJU_KIND_COMP_YEAR = "5";

	/** 未处理 */
	public final static String AcStat_Init = "0";
	/** 成功 */
	public final static String AcStat_Succ = "1";
	/** 失败 */
	public final static String AcStat_Lost = "3";

	public final static String FINE_RATE_TYPE_INTE = "1"; // 基于贷款利率
	public final static String FINE_RATE_TYPE_FINE = "2"; // 基于牌告逾期利率

	public final static String RATE_INTERATE = "InteRate"; // 贷款利率
	public final static String RATE_FINERATE = "FineRate"; // 罚息利率

	public final static String ASSU_STATE_DEBT = "5"; // 待处理抵债资产

	public final static String LIST_TABNAME_RELE = "dfListRele"; // 贷款发放
	public final static String LIST_TABNAME_CHGTERM = "dfListChgTerm"; // 贷款发放
	public final static String LIST_TABNAME_RETUNORM = "dfListRetuNorm"; // 还正常贷款
	public final static String LIST_TABNAME_RETUAHEA = "dfListRetuAhea"; // 提前还款
	public final static String LIST_TABNAME_RETUOVER = "dfListRetuOver"; // 还逾期贷款
	public final static String LIST_TABNAME_RETUFREE = "dfListRetuFree"; // 特殊还款处理
	public final static String LIST_TABNAME_BADCANCEL = "dfListBadCancel"; // 核销
	public final static String LIST_TABNAME_CHGACFLAG = "dfListChgAcFlag"; // 账户状态调整
	public final static String LIST_TABNAME_CHGRATE = "dfListChgRate"; // 利率调整
	public final static String LIST_TABNAME_FINISH = "dfListFinish"; // 业务终结
	public final static String LIST_TABNAME_OTHER = "Other"; // 其他

	/** 计算应还利息 */
	public final static String COMP_KIND_S_INTE = "1";
	/** 计算应扣利息（主要用于助学贷款） */
	public final static String COMP_KIND_S_SUBSINTE = "2";
	/** 计算应贴利息（主要用于助学贷款） */
	public final static String COMP_KIND_S_ALLOINTE = "3";

	public final static String PI_INTE = "Inte"; // 利息（包括贷款利息以及罚息）
	public final static String PI_S_ALLOINTE = "sAlloInte"; // 利息（包括贷款利息以及罚息）
	public final static String PI_S_SUBSINTE = "sSubsInte"; // 利息（包括贷款利息以及罚息）
	public final static String PI_CAPI = "Capi"; // 本金

	public final static String BILLKIND_DEBX = "01"; // 等额本息
	public final static String BILLKIND_DEBJ = "02"; // 等额本金
	public final static String BILLKIND_ZHX = "03"; // 只还息
	public final static String BILLKIND_HBHX = "04"; // 按还款方式还本还息
	public final static String BILLKIND_DELJ = "23"; // 等额累进法：等额递增、等额递减
	public final static String BILLKIND_DBLJ = "24"; // 等比累进法：等比递增、等比递减
	public final static String BILLKIND_DBDX = "50"; // 等本等息

	public final static String RETUFREQ_DAY = "D"; // 天
	public final static String RETUFREQ_MONTH = "M"; // 月
	public final static String RETUFREQ_PAYOFF = "P"; // 整笔支付
	public final static String RETUFREQ_HALF_OF_MONTH = "S"; // 每半月

	public final static String Short_TermFlag = "0"; // 短期贷款
	public final static String Loang_TermFlag = "1"; // 中长期贷款

	/** 未逾期 */
	public final static String OverFlag_NO_Overdue = "0";
	/** 逾期 */
	public final static String OverFlag_Overdue = "1";
	/** 待转逾期 */
	public final static String OverFlag_Wait_Overdue = "2";

	/** 借（付）方蓝字 */
	public final static String Debit_Blue = "1";

	/** 借（付）方红字 */
	public final static String Debit_Red = "2";

	/** 贷（收）方蓝字 */
	public final static String Loan_Blue = "3";

	/** 贷（收）方红字 */
	public final static String Loan_Red = "4";

	/** 是否成功标记_是 */
	public final static String SuccFlag_Yes = "1";

	/** 是否成功标记_否 */
	public final static String SuccFlag_No = "0";

	/** 借贷标记<b>借方</b> */
	public final static String AccKeep_Debit = "0";

	/** 借贷标记<b>贷方</b> */
	public final static String AccKeep_Credit = "1";

	/** 红蓝字<b>红字</b> */
	public final static String AccColor_Red = "01";

	/** 红蓝字<b>蓝字</b> */
	public final static String AccColor_Blue = "00";

	/** <b>业务状态</b> 申请 */
	public final static String OperationState_DBAPP = "10";
	/** <b>业务状态</b> 分户账 */
	public final static String OPerationState_LOANBAL = "20";
	/** <b>业务状态</b> 发放 */
	public final static String OPerationState_LISTRELE = "30";
	/** <b>业务状态</b> 正常回收 */
	public final static String OPerationState_LISTRETUNORM = "40";

	public final static int INTEBASE_30_DAYS_PER_MONTH = 360;
	public final static int INTEBASE_COMM_DAYS = 365;
	public final static int INTEBASE_FACT_DAYS = 366;

	/** 按期计息 */
	public final static String INTECOMPKIND_TERM = "1"; // 按期计息
	/** 按日计息（实际天数） */
	public final static String INTECOMPKIND_DAY = "2"; // 按日计息（实际天数）
	/** 按日计息（整月加零天） */
	public final static String INTECOMPKIND_DAY_M_D = "3"; // 按日计息（整月加零天）

	/** 分段计息时使用固定的“计算本金”（统一使用传入的值） */
	public final static String CAPIMODE_FIXED = "CapiFixed"; // 分段计息时使用固定的“计算本金”（统一使用传入的值）
	/** 分段计息时使用固定的“计算本金”（统一使用传入的值） */
	public final static String CAPIMODE_DB_VALUE = "CapiDBValue"; // 分段计息时，使用dxLogComp.CompCapi

	public final static String RISK_FLAG_NORM_1 = "11"; // 正常一档
	public final static String RISK_FLAG_NORM_2 = "12"; // 正常二档
	public final static String RISK_FLAG_ATTE_1 = "21"; // 关注一档
	public final static String RISK_FLAG_ATTE_2 = "22"; // 关注二档
	public final static String RISK_FLAG_BAD_1 = "31"; // 次级一档
	public final static String RISK_FLAG_BAD_2 = "32"; // 次级二档
	public final static String RISK_FLAG_DOUBT_1 = "41"; // 可疑一档
	public final static String RISK_FLAG_DOUBT_2 = "42"; // 可疑二档
	public final static String RISK_FLAG_LOSS_1 = "51"; // 损失

	public final static String AC_FLAG2_ACCRUAL = "0"; // 应计
	public final static String AC_FLAG2_NON_ACCRUAL = "1"; // 非应计

	public final static String AC_FLAG_ACCRUAL = "a"; // 应计
	public final static String AC_FLAG_NON_ACCRUAL = "p"; // 非应计

	/** 按日计提 */
	public static final String InteReceType_DAY = "1";
	/** 按月计提 */
	public static final String InteReceType_MONTH = "2";
	/** 按期计提 */
	public static final String InteReceType_TERM = "3";
	/** 实际利率计算步长 */
	public static final double InteRece_FloatRate = 0.01;

	/** 是 */
	public final static String YES = "1";
	/** 否 */
	public final static String NO = "0";

	/**
	 * 担保类型: REGIKIND_TYPE_PLEDGE 1-抵押
	 */
	public static final String REGIKIND_TYPE_PLEDGE = "1"; // 抵押

	/**
	 * 担保类型: REGIKIND_TYPE_IMPAWN 2-质押
	 */
	public static final String REGIKIND_TYPE_IMPAWN = "2"; // 质押

	/**
	 * 担保类型: REGIKIND_TYPE_ARTIFICIAL 4-法人保证
	 */
	public static final String REGIKIND_TYPE_ARTIFICIAL = "4"; // 法人保证

	/**
	 * 担保类型: REGIKIND_TYPE_PERSON 5-自然人保证
	 */
	public static final String REGIKIND_TYPE_PERSON = "5"; // 自然人保证

	/**
	 * 科目代码类型: SUBJIDKIND_01 01-固定完整科目代码
	 */
	public static final String SUBJIDKIND_01 = "01";
	/**
	 * 科目代码类型: SUBJIDKIND_02 02-固定前缀＋科目代码
	 */
	public static final String SUBJIDKIND_02 = "02";
	/**
	 * 科目代码类型: SUBJIDKIND_03 03-科目代码＋固定后缀
	 */
	public static final String SUBJIDKIND_03 = "03";
	/**
	 * 科目代码类型: SUBJIDKIND_04 04-固定前缀＋科目代码＋固定后缀
	 */
	public static final String SUBJIDKIND_04 = "04";
	/**
	 * 科目代码类型: SUBJIDKIND_05 05-账务机构号码＋科目代码
	 */
	public static final String SUBJIDKIND_05 = "05";
	/**
	 * 科目代码类型: SUBJIDKIND_06 06-科目代码＋账务机构代码
	 */
	public static final String SUBJIDKIND_06 = "06";
	/**
	 * 科目代码类型: SUBJIDKIND_07 07-账务机构号码＋科目代码+固定后缀
	 */
	public static final String SUBJIDKIND_07 = "07";
	/**
	 * 科目代码类型: SUBJIDKIND_08 08-固定前缀+科目代码＋账务机构代码
	 */
	public static final String SUBJIDKIND_08 = "08";
	/**
	 * 科目代码类型: SUBJIDKIND_91 91-放款专户账号
	 */
	public static final String SUBJIDKIND_91 = "91";
	/**
	 * 科目代码类型: SUBJIDKIND_92 92-还款专户账号
	 */
	public static final String SUBJIDKIND_92 = "92";
	/**
	 * 科目代码类型: SUBJIDKIND_93 93-费用发生账号
	 */
	public static final String SUBJIDKIND_93 = "93";
	/**
	 * 科目代码类型: SUBJIDKIND_94 94-系统内部过渡户
	 */
	public static final String SUBJIDKIND_94 = "94";

	/**
	 * 科目代码类型: SUBJIDKIND_95 银承汇票签发时，需要得到结算账号
	 */
	public static final String SUBJIDKIND_95 = "95";
	/**
	 * 科目代码类型: SUBJIDKIND_60 60-放款结算户账号
	 */
	public static final String SUBJIDKIND_60 = "60";
	/**
	 * 科目代码类型: SUBJIDKIND_70 70-优分期放款中间户
	 */
	public static final String SUBJIDKIND_70 = "70";
	/**
	 * 系统内置用户 批量提前还款
	 */
	public static final String SYSTEM_FINAL_USER_CSIGN = "CSIGN";
	public static final String SYSTEM_FINAL_USER_AFSUBS = "AFSUBS";
	public static final String SYSTEM_FINAL_USER_FUND_PAYOFF = "FUNDPAYOFF";
	/** 减值标志 正常 */
	public static final String ImpaiFlag_NO = "0";
	/** 减值标志 已减值 */
	public static final String ImpaiFlag_YES = "A";

	public static final String CHARGETYPE_CUST_AC = "00";
	public static final String CHARGETYPE_AFSUBS = "01";
	public static final String CHARGETYPE_AFSUBS_AHEA = "05";
	public static final String CHARGETYPE_ASSU_SUBS = "02";
	public static final String CHARGETYPE_ASSU_ADD = "03";
	public static final String CHARGETYPE_FUND_PAYOFF = "04";
	public static final String CHARGETYPE_AFSUBS_BY_MONTH_ADD = "21";
	public static final String CHARGETYPE_AFSUBS_ONCE_ADD = "22";
	public static final String CHARGETYPE_FUND_PAYOFF_ADD = "23";
	public static final String CHARGETYPE_OTHER = "99";

	/** 还款调整分期还款额 */
	public static final String RETUTYPE_ADJUAMT_YES = "1";
	/** 还款 不调整分期还款额度 */
	public static final String RETUTYPE_ADJUAMT_NO = "2";

	public static final String STOP_SUBS_FLAG_NO = "0";
	public static final String STOP_SUBS_FLAG_AF_N_MONTHS = "1";

	/** 记账 */
	public static final String AcKeep_Deal = "0";
	/** 冲账 */
	public static final String AcKeep_ReDeal = "1";

	public ClsConst() {
	}

}