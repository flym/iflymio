package io.iflym.mybatis.criteria.property;

import io.iflym.core.util.ObjectUtils;
import lombok.Setter;
import lombok.experimental.Accessors;

import static io.iflym.mybatis.mapperx.Sqls.AS;

/**
 * 具有别名的属性映射
 * Created by flym on 6/5/2016.
 *
 * @author flym
 */
public abstract class AbstractAliasProperty extends AbstractProperty implements SingleProperty {
    /** 别名 */
    @Accessors(chain = true)
    @Setter
    protected String alias;

    /**
     * 产生前半部分sql(除alias外)
     *
     * @return 生成的前面的sql
     */
    protected abstract String toFrontSql();

    @Override
    public final String toSql() {
        String sql = toFrontSql();

        if(ObjectUtils.isEmpty(alias)) {
            return sql;
        }

        return sql + AS + alias;
    }
}
