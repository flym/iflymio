package io.iflym.mybatis.mapperx;

import io.iflym.mybatis.criteria.Criteria;
import io.iflym.mybatis.domain.Entity;
import io.iflym.mybatis.domain.Key;
import io.iflym.mybatis.domain.Page;
import io.iflym.mybatis.domain.UniqueKey;
import io.iflym.mybatis.example.Example;
import io.iflym.mybatis.mapperx.util.MapperHelper;
import lombok.val;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.List;

/**
 * 为一个主要的数据库操作提供主要的各项crud操作
 * 本理念参考于 spring data中的 curdRepository
 *
 * @author flym
 * Created by flym on 2017/8/29.
 * @see org.springframework.data.repository.CrudRepository
 */
public interface Mapper<E extends Entity> {
    /**
     * 插入对象
     * 在添加数据时,对于自增主键的处理需要分为两种情况,如果主键信息已经由业务端提供,这种情况下应当使用业务端已经准备好的主键,而不是再由数据库
     * 自行生成;反之则需要由数据库自动生成
     * 表示在具体的sql上,即在生成的Sql中是否要存在主键列信息,如果没有主键信息,数据库即会自动生成,否则就会使用sql中设置的值
     *
     * @param e 具体要保存的实体对象
     */
    default void save(E e) {
        MapperHelper.save(this, e);
    }

    /**
     * 根据主键信息获取相应的对象
     *
     * @param key 相应的主键对象
     * @return 此对象所对应的实体, 如果没有则返回null
     */
    default E get(Key key) {
        return MapperHelper.get(this, key, null);
    }

    /**
     * 根据主键信息获取相应的对象
     *
     * @param key        相应的主键对象
     * @param resultType 指定返回结果类型
     * @return 此对象所对应的实体, 如果没有则返回null
     */
    default <T extends E> T get(Key key, Class<T> resultType) {
        return MapperHelper.get(this, key, resultType);
    }

    /**
     * 根据UK信息获取相应的对象
     *
     * @param uniqueKey 相应的UK对象
     * @return 此对象所对应的实体, 如果没有则返回null
     */
    default E get(UniqueKey uniqueKey) {
        return MapperHelper.get(this, uniqueKey, null);
    }

    /**
     * 根据惟一键信息获取相应的对象, 并返回指定类型的对象
     *
     * @param uniqueKey  相应的惟一键对象
     * @param resultType 期望的结果对象
     * @return 此对象所对应的实体, 如果没有则返回null
     */
    default <T extends E> T get(UniqueKey uniqueKey, Class<T> resultType) {
        return MapperHelper.get(this, uniqueKey, resultType);
    }

    /**
     * 根据多个主键信息获取多个对象
     *
     * @param keyList 相应的多个主键对象的集合, 不能为null
     * @return 与相同个数主键对象相对应的实体对象集合, 注: 返回的数据个数可能与参数个数不相同, 取决于具体能查找到多少个实体
     */
    default List<E> getMulti(List<Key> keyList) {
        return MapperHelper.getMulti(this, keyList);
    }

    /**
     * 根据多个主键信息获取多个对象
     *
     * @param keyList    相应的多个主键对象的集合, 不能为null
     * @param resultType 指定返回结果类型
     * @return 与相同个数主键对象相对应的实体对象集合, 注: 返回的数据个数可能与参数个数不相同, 取决于具体能查找到多少个实体
     */
    default <T extends E> List<T> getMulti(List<Key> keyList, Class<T> resultType) {
        return MapperHelper.getMulti(this, keyList, resultType);
    }

    /**
     * 根据主键信息判断是否存在相应的值对象
     *
     * @param key 相应的主键对象
     * @return 如果能找到数据, 则返回true
     */
    default boolean exists(Key key) {
        return MapperHelper.exists(this, key);
    }

    /**
     * 列出全部对象
     *
     * @return 相应此实体类型下的所有数据
     */
    default List<E> listAll() {
        return listPage(Page.NO_PAGE);
    }

    /**
     * 列出全部对象
     *
     * @param resultType 指定返回结果类型
     * @return 相应此实体类型下的所有数据
     */
    default <T extends E> List<T> listAll(Class<T> resultType) {
        return listPage(Page.NO_PAGE, resultType);
    }

    /**
     * 列出对象信息,并根据分页条件查询数据
     *
     * @param page 相应的分页参数
     * @return 在指定分页下的实体对象集合
     */
    @Sql(type = SqlCommandType.SELECT)
    default List<E> listPage(Page page) {
        return MapperHelper.listPage(this, page);
    }

    /**
     * 列出对象信息,并根据分页条件查询数据
     *
     * @param page       相应的分页参数
     * @param resultType 指定返回结果类型
     * @return 在指定分页下的实体对象集合
     */
    @Sql(type = SqlCommandType.SELECT)
    default <T extends E> List<T> listPage(Page page, Class<T> resultType) {
        return MapperHelper.listPage(this, page, resultType);
    }

    /**
     * 根据条件对象获取相应的数据,备注,此查询并不会触发相应的分页总数处理
     * 如果要获取总数,请显式地调用 {@link #countCriteria(Criteria)}进行处理,相应的参数对象不需要作任何调整
     * 之所以这样做的目的在于,避免对list查询带来不必要的复杂性,因为并不是所有的查询都可能需要总数处理
     *
     * @param criteria 构建查询体的查询对象
     * @return 满足此条件的结果集合
     */
    default List<E> listCriteria(Criteria<E> criteria) {
        return MapperHelper.listCriteria(this, criteria);
    }

    /**
     * 根据条件对象查询出结果数目
     *
     * @param criteria 构建查询体的查询对象
     * @return 此条件涉及到的结果的数目值
     */
    default int countCriteria(Criteria<E> criteria) {
        return MapperHelper.countCriteria(this, criteria);
    }

    /**
     * 更新数据
     *
     * @param e 已经修改过状态数据的实体对象
     */
    default void update(E e) {
        MapperHelper.update(this, e);
    }

    /**
     * 更新数据
     *
     * @param example 相应的映射数据
     */
    @SuppressWarnings("unchecked")
    default <X extends Entity<X>> void updateByExample(Example<X> example) {
        val selfMapper = (Mapper<X>) this;
        MapperHelper.updateByExample(selfMapper, example);
    }

    /**
     * 删除数据
     *
     * @param key 主键对象
     */
    default void delete(Key key) {
        MapperHelper.delete(this, null, key);
    }

    /**
     * 删除对象
     *
     * @param e 具体要删除的实体对象
     */
    default void delete(E e) {
        MapperHelper.delete(this, e, e.key());
    }
}
