package io.iflym.mybatis.criteria;

import com.google.common.collect.Lists;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 采用由map参数直接获取参数信息的方式的绑定sql实现
 *
 * @author flym
 * Created by flym on 6/12/2016.
 */
public class CriteriaBoundSql extends BoundSql {
    public static final String PARAM_VALUE_KEY_NAME = "param";

    private BoundSql sourceBoundSql;
    private List<ParameterMapping> parameterMappingList = Lists.newArrayList();

    public CriteriaBoundSql(Configuration configuration, BoundSql sourceBoundSql) {
        super(configuration, sourceBoundSql.getSql(), null, sourceBoundSql.getParameterObject());
        this.sourceBoundSql = sourceBoundSql;

        //处理参数映射及参数信息
        @SuppressWarnings("unchecked")
        List<ParamValue> paramValueList = (List) ((Map) getParameterObject()).get(PARAM_VALUE_KEY_NAME);
        for(int i = 0; i < paramValueList.size(); i++) {
            String param = "p" + String.valueOf(i);
            ParamValue<?> typeValue = paramValueList.get(i);
            ParameterMapping mapping = new ParameterMapping.Builder(configuration, param, typeValue.t2)
                    .jdbcType(typeValue.t1).build();
            parameterMappingList.add(mapping);

            setAdditionalParameter(param, typeValue.t3);
        }
    }

    @Override
    public String getSql() {
        return sourceBoundSql.getSql();
    }

    @Override
    public List<ParameterMapping> getParameterMappings() {
        return parameterMappingList;
    }
}
