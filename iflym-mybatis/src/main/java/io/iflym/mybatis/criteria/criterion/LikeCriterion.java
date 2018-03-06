package io.iflym.mybatis.criteria.criterion;

import io.iflym.mybatis.criteria.LikeTypeValue;

import static io.iflym.mybatis.mapperx.Sqls.LIKE;

/**
 * 描述like的比较器
 * Created by flym on 6/3/2016.
 *
 * @author flym
 */
public class LikeCriterion extends SimpleExpression<String> {
    public LikeCriterion(String property, String value, LikeTypeValue likeType) {
        super(property, likeType.wrap(value), LIKE);
    }
}
