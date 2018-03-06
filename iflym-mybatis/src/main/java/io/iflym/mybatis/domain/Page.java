package io.iflym.mybatis.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.RowBounds;

/**
 * 用于支持相应的分页处理
 * 主要的理念参考于spring data中的分页模型描述
 * Created by flym on 2017/8/30.
 *
 * @author flym
 * @see org.springframework.data.domain.AbstractPageRequest
 */
@Setter
@Getter
public class Page extends RowBounds {
    private static final int PAGE_INDEX_DEFAULT = 1;
    private static final int PAGE_SIZE_DEFAULT = 20;

    public static final Page NO_PAGE = new Page(0, Integer.MAX_VALUE);

    /** 当前页数 */
    private int page;
    /** 每页的数目 */
    private int size;
    /**
     * 查询的条目的总数目
     * 此值应该在相应的总数查询出来之后,再设置在相应的对象上,以提供给前端进行相应的处理
     */
    private int count;

    public Page() {
        this(PAGE_INDEX_DEFAULT, PAGE_SIZE_DEFAULT);
    }

    public Page(int page, int size) {
        super((page - 1) * size, size);

        this.page = page;
        this.size = size;
    }
}
