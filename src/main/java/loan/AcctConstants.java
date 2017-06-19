package loan;

public class AcctConstants {
    
    public static final String REPAY_AMOUNT_TYPE_CORPUS="0";//还款金额为本金
    public static final String REPAY_AMOUNT_TYPE_CORPUS_INTEREST="1";//还款金额为本金加利息
    
    public static final boolean CORP_ADJUST_TYPE = true;  //true-本金=期供-整段计息   false-本金=期供-分段计息
    
    public static final String TERM_UNIT_DAY    = "D";//期限天
    public static final String TERM_UNIT_MONTH  = "M";//期限月
    public static final String TERM_UNIT_YEAR   = "Y";//期限年
    
    public static final String RateCode_YEAR    = "01";//年利率
    public static final String RateCode_MONTH   = "02";//月利率
    public static final String RateCode_DAY     = "03";//日利率
    
    public static final String INTE_BEARING_TYPE_ActualDays = "0";//按实际天数计息
    public static final String INTE_BEARING_TYPE_InNameDays = "1";//按名义天数计息，月30天
    
    public static final int LOANTURN_DAYS = 90; //默认的应计转非应计天数
    
    public static final int MONEY_PRECISION=2;//金额精度
    public static final int RATE_PRECISION=7;//金额精度
    
    public static final String OPERATION_BATCH = "B";   //批量操作
    public static final String OPERATION_SINGLE = "S";  //单笔操作（柜面操作）
    
    //贷款状态
    public static final String LOAN_STATUS_TYPE_InnerLoan = "0";        //应计贷款
    public static final String LOAN_STATUS_TYPE_OutLoan = "1";          //非应计贷款
    public static final String LOAN_STATUS_TYPE_NORMALPAYOFF = "10";    //正常还清
    public static final String LOAN_STATUS_TYPE_OVERDUEPAYOFF = "30";   //逾期还清
    public static final String LOAN_STATUS_TYPE_AHEADPAYOFF = "20";     //提前还清
    public static final String LOAN_STATUS_TYPE_CANCALPAYOFF = "60";    //贷款核销
    public static final String LOAN_STATUS_TYPE_RUSHPAYOFF = "80";      //冲撤还清
    public static final String LOAN_STATUS_TYPE_GUARANTYPAYOFF = "90";  //担保代偿结清
    

    public static final String YES = "1";//是
    public static final String NO = "0";//否
    
    //还款周期定义
    public static final String RETURNPERIOD_M1 = "M1";//按月
    public static final String RETURNPERIOD_M3 = "M3";//按季
    public static final String RETURNPERIOD_M0 = "M0";//一次
    public static final String RETURNPERIOD_M6 = "M6";//按半年
    public static final String RETURNPERIOD_M12 = "M12";//按年
    public static final String RETURNPERIOD_2M = "2M";//按双周
    public static final String RETURNPERIOD_M2 = "M2";//按双月
    public static final String RETURNPERIOD_MX = "MX";//自定义

    //基准利率调整周期
    public static final String RATEADJUCT_CHANGE_NEXTYEARFIRST = "2"; //次年初
    public static final String RATEADJUCT_CHANGE_NEXTYEARTODATE = "3";//次年对月对日
    public static final String RATEADJUCT_CHANGE_MONTH = "4";//按月
    public static final String RATEADJUCT_CHANGE_SEASON = "5";//按季
    public static final String RATEADJUCT_CHANGE_NEXTPAYDATE = "6";//下次还款日（双周供用）
    public static final String RATEADJUCT_CHANGE_NOCHANGE = "7";//不调整
    
}