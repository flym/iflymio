package io.iflym.mybatis.part

import com.google.common.collect.Lists
import io.iflym.mybatis.criteria.Criteria
import io.iflym.mybatis.domain.Entity
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.ParameterMapping
import org.apache.ibatis.session.Configuration

class PartTreeBoundSql(criteria: Criteria<Entity<*>>, private val config: Configuration, parameterObject: Any)
    : BoundSql(config, null, null, parameterObject) {

    private val parameterMappingList = Lists.newArrayList<ParameterMapping>()
    private val sql = criteria.toSql()

    init {
        val paramValueList = criteria.fetchParams()
        paramValueList.forEachIndexed { i, pv ->
            val param = "p" + i
            val mapping = ParameterMapping.Builder(config, param, pv.t2).jdbcType(pv.t1).build()
            parameterMappingList.add(mapping)

            setAdditionalParameter(param, pv.t3)
        }
    }

    override fun getSql(): String = sql

    override fun getParameterMappings(): List<ParameterMapping> = parameterMappingList
}