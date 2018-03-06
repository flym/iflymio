/* Created by flym at 10/14/16 */
package io.iflym.mybatis.criteria.property;

import lombok.RequiredArgsConstructor;

/**
 * 描述一个使用sql语法描述的属性信息
 *
 * @author flym
 */
@RequiredArgsConstructor
public class SqlProperty extends AbstractAliasProperty {
    /** 相应的sql值 */
    private final String sql;

    @Override
    protected String toFrontSql() {
        return sql;
    }
}
