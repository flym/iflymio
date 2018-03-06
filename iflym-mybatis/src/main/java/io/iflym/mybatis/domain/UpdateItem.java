package io.iflym.mybatis.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;

/**
 * 用于描述一个实体对象单个属性的更新记录
 * Created by flym on 2017/9/1.
 *
 * @author flym
 */
@RequiredArgsConstructor
@Getter
public class UpdateItem<T, E extends Updatable> {
    /** 相应的实体对象 */
    private final E entity;
    /** 哪个属性 */
    private final Field property;
    /** 旧值 */
    private final T oldValue;
    /** 新值 */
    private final T newValue;
}
