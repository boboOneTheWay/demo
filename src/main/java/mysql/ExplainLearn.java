package mysql;

public class ExplainLearn {

	//1.id：          标识select所属的行，如果没有子查询或者联合查询那么只会有唯一的select
	//2.select_type:  表示是简单还是复杂select，简单的是simple，意味着不包括自查询和UNION。如果有任何复杂的字部分，最外层的select标记为PRIMARY，其他部分标记有一下情况：
		//1.SUBQUERY:子查询不在from内
		//2.DERIVED: 子查询在from内，产生临时表
		//3.UNION: ...
		//3.UNION RESULT:从UNION临时表中检索
	//3.table: 使用哪个表
	//4.type：关联类型
		//1.ALL：全表扫描
		//2.index：
	
	
}
