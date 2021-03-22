package io.iflym.core.collection;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Map;

/**
 * 备注: 此数据结构不支持 /a/** /b 的这种路径，针对这种路径，请使用spring PathPattern 或 正则匹配表达式
 * created at 2021-03-20
 *
 * @author flym
 */
@Slf4j
public class LevelTree<V> {
    private static final int STAR1_HASH = '*';
    private static final int STAR2_HASH = '*' * 31 + '*';

    private static final char SLASH = '/';

    /** 当前节点 hash 值 */
    private int hash;
    /** 当前节点 字符串 值 */
    private String str;
    /** 存储的实际数据 */
    private V value;

    /** 当前节点是否有叶子节点值,即存放最终数据 */
    private boolean hasValue;

    /** 当前节点的子节点是否有 * 节点 */
    private boolean star1Leaf = false;
    /** 当前节点的子节点中对 * 节点的引用 */
    private LevelTree<V> star1LeafTree;

    /** 当前节点的子节点是否有 ** 节点 */
    private boolean star2Leaf = false;
    /** 当前节点的子节点中对 ** 节点的引用 */
    private LevelTree<V> star2LeafTree;

    /** 当前子节点存储是否使用普通的intHashTree, 不处理hash冲突的问题 */
    private boolean fastPath = true;
    /** 无冲突时的 子节点树 */
    private IntHashMap<LevelTree<V>> leafTree;
    /** 存在冲突时的字符串子节点树 */
    private Map<String, LevelTree<V>> slowLeafTree;

    public LevelTree() {
    }

    private LevelTree(int hash, String str, V v) {
        resetValue(hash, str, v);
    }

    /** 添加数据 */
    public void put(String s, V v) {
        int sidx = firstNonSlash(s);

        doPut(s, sidx, s.length(), v);
    }

    public V get(String s) {
        int sidx = firstNonSlash(s);

        return doGetValue(s, sidx, s.length());
    }

    public boolean containsKey(String s) {
        int sidx = firstNonSlash(s);

        return doGetValue(s, sidx, s.length()) != null;
    }

    private V doGetValue(String s, int sidx, int eidx) {
        int k = 0;
        LevelTree<V> st = this;
        int preIdx = sidx;

        for(int i = sidx; i < eidx; i++) {
            char c = s.charAt(i);

            if(c == SLASH) {
                val tmpParent = st;

                st = tmpParent.getLeaf(k, s, preIdx, i);
                //未找到时，支持匹配 a/*/b 中的 *
                if(st == null) {
                    st = tmpParent.star1LeafTree;
                    V v;
                    if(st != null && (v = st.doGetValue(s, i + 1, eidx)) != null) {
                        return v;
                    }

                    return null;
                }

                if(st.star2Leaf) {
                    return st.star2LeafTree.value;
                }

                //继续往下，要求m 非空
                if(st.leafIsNull()) {
                    return null;
                }

                k = 0;
                preIdx = i + 1;
            } else {
                k = k * 31 + c;
            }
        }

        val parent = st;

        //支持 /a/* /a/** 匹配 /a/b 的情况
        if(parent.star1Leaf) {
            return parent.star1LeafTree.value;
        }
        if(parent.star2Leaf) {
            return parent.star2LeafTree.value;
        }

        //支持 /a/** 匹配 /a 的情况
        st = parent.getLeaf(k, str, preIdx, eidx);

        if(st == null) {
            return null;
        }

        if(st.star2Leaf) {
            return st.value;
        }

        //最后的全量匹配 要求当前节点必须单独有值
        return st.hasValue ? st.value : null;
    }

    private void doPut(String s, int sidx, int eidx, V v) {
        int preIdx = sidx;

        int k = 0;
        LevelTree<V> st;
        LevelTree<V> parent = this;

        for(int i = sidx; i < eidx; i++) {
            char c = s.charAt(i);
            if(c == SLASH) {
                st = parent.addLeaf(k, s.substring(preIdx, i), v);

                if(isStar1Value(st.hash, st.str)) {
                    parent.star1Leaf = true;
                    parent.star1LeafTree = st;
                }

                parent = st;

                k = 0;
                preIdx = i + 1;
            } else {
                k = k * 31 + c;
            }
        }

        boolean stIsRoot = false;
        val leafValue = s.substring(preIdx, eidx);

        if(parent == this) {
            st = parent;
            stIsRoot = true;

            st.resetValue(hash, str, v);
        } else {
            st = parent.addLeaf(k, leafValue, v);
        }

        st.hasValue = true;

        if(isStar2Value(st.hash, st.str)) {
            if(!stIsRoot) {
                parent.star2Leaf = true;
                parent.star2LeafTree = st;
            }
        } else if(isStar1Value(st.hash, st.str)) {
            if(!stIsRoot) {
                parent.star1Leaf = true;
                parent.star1LeafTree = st;
            }
        }
    }

    private void resetValue(int hash, String str, V v) {
        this.hash = hash;
        this.str = str;
        this.value = v;
    }

    private void initLeafTree() {
        if(leafTree == null) {
            leafTree = new IntHashMap<>();
        }
    }

    private void initLeafStringTree() {
        if(slowLeafTree == null) {
            slowLeafTree = Maps.newHashMap();
        }
    }

    private boolean leafIsNull() {
        if(fastPath) {
            return leafTree == null;
        }
        return slowLeafTree == null;
    }

    private LevelTree<V> addLeaf(int hash, String str, V v) {
        if(fastPath) {
            initLeafTree();
            LevelTree<V> leaf = leafTree.get(hash);
            if(leaf != null) {
                if(!leaf.str.equals(str)) {
                    log.warn("出现Hash冲突，准备退化为慢速匹配. {}:{}", leaf.str, str);
                    reStringTreeify();
                }

                return leaf;
            }

            leaf = new LevelTree<>(hash, str, v);
            leafTree.put(hash, leaf);

            return leaf;
        }

        return slowLeafTree.computeIfAbsent(str, noused -> new LevelTree<>(hash, str, v));
    }

    private LevelTree<V> getLeaf(int hash, String str, int sidx, int eidx) {
        if(fastPath) {
            return leafTree.get(hash);
        }

        return slowLeafTree.get(str.substring(sidx, eidx));
    }

    private void reStringTreeify() {
        initLeafStringTree();

        leafTree.forEach((i, v) -> slowLeafTree.put(v.str, v));

        this.leafTree = null;
        this.fastPath = false;
    }

    private static boolean isStar1Value(int hash, String str) {
        return hash == STAR1_HASH && str.equals("*");
    }

    private static boolean isStar2Value(int hash, String str) {
        return hash == STAR2_HASH && str.equals("**");
    }

    private static int firstNonSlash(String s) {
        int i = 0;
        while(s.charAt(i) == '/')
            i++;

        return i;
    }
}
