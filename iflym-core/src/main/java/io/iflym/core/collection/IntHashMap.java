package io.iflym.core.collection;

import io.iflym.core.function.BiIntConsumer;

import java.util.function.IntFunction;

/**
 * created at 2021-03-20
 *
 * @author flym
 */
public class IntHashMap<V> {

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    static final int MAXIMUM_CAPACITY = 1 << 30;

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static class Node<V> {
        final int key;
        V value;
        Node<V> next;

        Node(int key, V value, Node<V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    transient Node<V>[] table;
    transient int size;
    int threshold;
    final float loadFactor;

    public IntHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public int size() {
        return size;
    }

    public V get(int key) {
        Node<V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }

    final Node<V> getNode(int key) {
        Node<V>[] tab;
        Node<V> first, e;
        int n;
        if((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & key]) != null) {
            if(first.key == key) {
                return first;
            }
            if((e = first.next) != null) {

                do{
                    if(e.key == key) {
                        return e;
                    }
                } while((e = e.next) != null);
            }
        }
        return null;
    }

    public boolean containsKey(int key) {
        return getNode(key) != null;
    }

    public V put(int key, V value) {
        return putVal(key, value, false, true);
    }

    public V putIfAbsent(int key, V value) {
        return putVal(key, value, true, true);
    }

    final V putVal(int key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<V>[] tab;
        Node<V> p;
        int n, i;
        if((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        if((p = tab[i = (n - 1) & key]) == null) {
            tab[i] = newNode(key, value, null);
        } else {
            Node<V> e;
            if(p.key == key) {
                e = p;
            } else {
                for(int binCount = 0; ; ++binCount) {
                    if((e = p.next) == null) {
                        p.next = newNode(key, value, null);

                        break;
                    }
                    if(e.key == key) {
                        break;
                    }
                    p = e;
                }
            }
            if(e != null) { // existing mapping for key
                V oldValue = e.value;
                if(!onlyIfAbsent || oldValue == null) {
                    e.value = value;
                }
                afterNodeAccess(e);
                return oldValue;
            }
        }
        if(++size > threshold) {
            resize();
        }
        afterNodeInsertion(evict);
        return null;
    }

    final Node<V>[] resize() {
        Node<V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if(oldCap > 0) {
            if(oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY) {
                newThr = oldThr << 1; // double threshold
            }
        } else if(oldThr > 0) // initial capacity was placed in threshold
        {
            newCap = oldThr;
        } else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if(newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes", "unchecked"})
        Node<V>[] newTab = (Node<V>[]) new Node[newCap];
        table = newTab;
        if(oldTab != null) {
            for(int j = 0; j < oldCap; ++j) {
                Node<V> e;
                if((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if(e.next == null) {
                        newTab[e.key & (newCap - 1)] = e;
                    } else { // preserve order
                        Node<V> loHead = null, loTail = null;
                        Node<V> hiHead = null, hiTail = null;
                        Node<V> next;
                        do{
                            next = e.next;
                            if((e.key & oldCap) == 0) {
                                if(loTail == null) {
                                    loHead = e;
                                } else {
                                    loTail.next = e;
                                }
                                loTail = e;
                            } else {
                                if(hiTail == null) {
                                    hiHead = e;
                                } else {
                                    hiTail.next = e;
                                }
                                hiTail = e;
                            }
                        } while((e = next) != null);
                        if(loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if(hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    public V computeIfAbsent(int key,
                             IntFunction<? extends V> mappingFunction) {
        if(mappingFunction == null) {
            throw new NullPointerException();
        }

        Node<V>[] tab;
        Node<V> first;
        int n, i;
        int binCount = 0;
        Node<V> old = null;
        if(size > threshold || (tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        if((first = tab[i = (n - 1) & key]) != null) {
            Node<V> e = first;
            do{
                if(e.key == key) {
                    old = e;
                    break;
                }
                ++binCount;
            } while((e = e.next) != null);

            V oldValue;
            if(old != null && (oldValue = old.value) != null) {
                afterNodeAccess(old);
                return oldValue;
            }
        }
        V v = mappingFunction.apply(key);
        if(v == null) {
            return null;
        } else if(old != null) {
            old.value = v;
            afterNodeAccess(old);
            return v;
        } else {
            tab[i] = newNode(key, v, first);
        }
        ++size;
        afterNodeInsertion(true);
        return v;
    }

    public V remove(int key) {
        Node<V> e;
        return (e = removeNode(key, null, false, true)) == null ?
                null : e.value;
    }

    final Node<V> removeNode(int key, Object value,
                             boolean matchValue, boolean movable) {
        Node<V>[] tab;
        Node<V> p;
        int n, index;
        if((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & key]) != null) {
            Node<V> node = null, e;
            V v;
            if(p.key == key) {
                node = p;
            } else if((e = p.next) != null) {

                do{
                    if(e.key == key) {
                        node = e;
                        break;
                    }
                    p = e;
                } while((e = e.next) != null);

            }
            if(node != null && (!matchValue || (v = node.value) == value ||
                    (value != null && value.equals(v)))) {
                if(node == p) {
                    tab[index] = node.next;
                } else {
                    p.next = node.next;
                }
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
        return null;
    }


    public void clear() {
        Node<V>[] tab;

        if((tab = table) != null && size > 0) {
            size = 0;
            for(int i = 0; i < tab.length; ++i)
                tab[i] = null;
        }
    }

    public boolean containsValue(Object value) {
        Node<V>[] tab;
        V v;
        if((tab = table) != null && size > 0) {
            for(int i = 0; i < tab.length; ++i) {
                for(Node<V> e = tab[i]; e != null; e = e.next) {
                    if((v = e.value) == value ||
                            (value != null && value.equals(v))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void forEach(BiIntConsumer<V> action) {
        Node<V>[] tab;

        if(size > 0 && (tab = table) != null) {
            for(int i = 0; i < tab.length; ++i) {
                for(Node<V> e = tab[i]; e != null; e = e.next)
                    action.accept(e.key, e.value);
            }
        }
    }

    Node<V> newNode(int key, V value, Node<V> next) {
        return new Node<>(key, value, next);
    }


    void afterNodeAccess(Node<V> p) {
    }

    void afterNodeInsertion(boolean evict) {
    }

    void afterNodeRemoval(Node<V> p) {
    }

}
