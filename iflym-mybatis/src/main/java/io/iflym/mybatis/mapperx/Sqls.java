package io.iflym.mybatis.mapperx;

/**
 * 提供在相应的sql语句中使用的sql关键字信息
 * Created by flym on 2017/8/30.
 *
 * @author flym
 */
public interface Sqls {
    /** sql语句中的select,后接空格 */
    String SELECT = "select ";

    /** sql语句的insert into,后接空格 */
    String INSERT = "insert into ";

    /** update语句,后接空格 */
    String UPDATE = "update ";

    /** delete语句,后接空格 */
    String DELETE = "delete ";

    /** from语句,两边有空格 */
    String FROM = " from ";

    /** where关键字,两边有空格 */
    String WHERE = " where ";

    /** exists 关键字,两边有空格 */
    String EXISTS = " exists ";

    /** 单独的空格 */
    String S = " ";

    /** 常量 1 */
    String CONST_1 = "1";

    /** and关键字,两边有空格 */
    String AND = " and ";

    /** or关键字,两边有空格 */
    String OR = " or ";

    /** 参数?点位符 */
    String QUEST = "?";

    /** 逗号关键字 */
    String COMMA = ",";

    /** 等号关键字 */
    String EQ = "=";

    /** values关键字,两边有空格 */
    String VALUES = " values ";

    /** orderby排序,两边有空格 */
    String ORDER_BY = " order by ";

    /** limit 关键字,两边有空格 */
    String LIMIT = " limit ";

    /** 左小括号 */
    String LP = "(";

    /** 右小括号 */
    String RP = ")";

    /** 左中括号 */
    String LMB = "[";

    /** 右中括号 */
    String RMB = "]";

    /** 左大括号 */
    String LBRACE = "{";

    /** 右大括号 */
    String RBRACE = "}";

    /** mybatis中的左转义引用符号 */
    String MPLBRACE = "#{";

    /** mybatis中的无转义引用符号 */
    String M_$_LBRANCE = "${";

    /** 不相等 */
    String NEQ = " <> ";

    /** 大于 */
    String GT = " > ";

    /** 大于等于 */
    String GE = " >= ";

    /** 小于 */
    String LT = " < ";

    /** 小于等于 */
    String LE = " <= ";

    /** like 操作 */
    String LIKE = " like ";

    /** is null 操作 */
    String IS_NULL = " is null ";

    /** is not null 操作 */
    String IS_NOT_NULL = " is not null ";

    /** not 操作 */
    String NOT = " not ";

    /** between 操作 */
    String BETWEEN = " between ";

    /** in操作符 */
    String IN = " in ";

    /** 点号连接符 */
    String DOT = ".";

    /** sql的关联时的on */
    String ON = " on ";

    /** sql 查询中的别名 */
    String AS = " as ";

    /** sql中的 * 查询 */
    String STAR = "*";

    /** sql的升序 */
    String ASC = "asc";

    /** sql的降序 */
    String DESC = "desc";

    /** 函数中的count */
    String COUNT = "count";

    /** 函数中的 max */
    String MAX = "max";

    /** 函数中的 min */
    String MIN = "min";

    /** 函数中 avg */
    String AVG = "avg";

    /** 函数中的sum */
    String SUM = "sum";

    /** 聚合中的 distinct */
    String DISTINCT = " distinct ";
}
