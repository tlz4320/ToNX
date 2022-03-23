//package cn.treeh.ToNX.Collection;
//
//import java.io.Serializable;
//import java.util.*;
//import java.util.function.BiConsumer;
//import java.util.function.BiFunction;
//import java.util.function.Function;
//
//public class RBTreeMap<K, V> extends AbstractMap<K, V>  implements Map<K, V>, Cloneable, Serializable {
//    private final Comparator<? super K> comparator;
//    private transient Entry<K, V> root;
//    private transient int size = 0;
//    private transient int modCount = 0;
//    public RBTreeMap(Comparator<? super K> comparator) {
//        this.comparator = comparator;
//    }
//    Entry<K, V> findEntry(Entry<K, V> p, K key, Comparator<? super K> cpr) {
//        if(p == null)
//            return null;
//        if(p.key.equals(key))
//            return p;
//        if (cpr != null) {
//            int cmp = cpr.compare(key, p.key);
//            if (cmp < 0) {
//                return findEntry(p.left, key, cpr);
//            } else if(cmp > 0){
//                return findEntry(p.right, key, cpr);
//            }
//            else{
//                Entry<K, V> tmp = findEntry(p.left, key, cpr);
//                return tmp == null ? findEntry(p.right, key, cpr) : tmp;
//            }
//        }
//        return null;
//    }
//    final Entry<K, V> getEntry(K k) {
//        Comparator<? super K> cpr = this.comparator;
//        return cpr == null ? null : findEntry(root, k, cpr);
//    }
//
//    public V get(Object key) {
//        Entry<K, V> p = this.getEntry((K)key);
//        return p == null ? null : p.value;
//    }
//
//    private static <K, V> boolean colorOf(Entry<K, V> p) {
//        return p == null ? true : p.color;
//    }
//
//    private static <K, V> Entry<K, V> parentOf(Entry<K, V> p) {
//        return p == null ? null : p.parent;
//    }
//
//    private static <K, V> void setColor(Entry<K, V> p, boolean c) {
//        if (p != null) {
//            p.color = c;
//        }
//
//    }
//
//    private static <K, V> Entry<K, V> leftOf(Entry<K, V> p) {
//        return p == null ? null : p.left;
//    }
//
//    private static <K, V> Entry<K, V> rightOf(Entry<K, V> p) {
//        return p == null ? null : p.right;
//    }
//    private void rotateLeft(Entry<K, V> p) {
//        if (p != null) {
//            Entry<K, V> r = p.right;
//            p.right = r.left;
//            if (r.left != null) {
//                r.left.parent = p;
//            }
//
//            r.parent = p.parent;
//            if (p.parent == null) {
//                this.root = r;
//            } else if (p.parent.left == p) {
//                p.parent.left = r;
//            } else {
//                p.parent.right = r;
//            }
//
//            r.left = p;
//            p.parent = r;
//        }
//
//    }
//
//    private void rotateRight(Entry<K, V> p) {
//        if (p != null) {
//            Entry<K, V> l = p.left;
//            p.left = l.right;
//            if (l.right != null) {
//                l.right.parent = p;
//            }
//
//            l.parent = p.parent;
//            if (p.parent == null) {
//                this.root = l;
//            } else if (p.parent.right == p) {
//                p.parent.right = l;
//            } else {
//                p.parent.left = l;
//            }
//
//            l.right = p;
//            p.parent = l;
//        }
//
//    }
//
//    private void fixAfterInsertion(Entry<K, V> x) {
//        x.color = false;
//
//        while(x != null && x != this.root && !x.parent.color) {
//            Entry<K,V> y;
//            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
//                y = rightOf(parentOf(parentOf(x)));
//                if (!colorOf(y)) {
//                    setColor(parentOf(x), true);
//                    setColor(y, true);
//                    setColor(parentOf(parentOf(x)), false);
//                    x = parentOf(parentOf(x));
//                } else {
//                    if (x == rightOf(parentOf(x))) {
//                        x = parentOf(x);
//                        this.rotateLeft(x);
//                    }
//
//                    setColor(parentOf(x), true);
//                    setColor(parentOf(parentOf(x)), false);
//                    this.rotateRight(parentOf(parentOf(x)));
//                }
//            } else {
//                y = leftOf(parentOf(parentOf(x)));
//                if (!colorOf(y)) {
//                    setColor(parentOf(x), true);
//                    setColor(y, true);
//                    setColor(parentOf(parentOf(x)), false);
//                    x = parentOf(parentOf(x));
//                } else {
//                    if (x == leftOf(parentOf(x))) {
//                        x = parentOf(x);
//                        this.rotateRight(x);
//                    }
//
//                    setColor(parentOf(x), true);
//                    setColor(parentOf(parentOf(x)), false);
//                    this.rotateLeft(parentOf(parentOf(x)));
//                }
//            }
//        }
//
//        this.root.color = true;
//    }
//    public V put(K key, V value) {
//        Entry<K, V> t = this.root;
//        if (t == null) {
//            comparator.compare(key, key);
//            this.root = new Entry(key, value, (Entry) null);
//            this.size = 1;
//            ++this.modCount;
//            return null;
//        } else {
//            Entry<K, V> entry = getEntry(key);
//            if (entry != null) {
//                V old = entry.value;
//                entry.setValue(value);
//                return old;
//            }
//            Comparator<? super K> cpr = this.comparator;
//            int cmp;
//            Entry<K, V> parent;
//            do {
//                parent = t;
//                cmp = cpr.compare(key, t.key);
//                if (cmp < 0) {
//                    t = t.left;
//                } else {
//                    t = t.right;
//                }
//            } while (t != null);
//
//            Entry<K, V> e = new Entry(key, value, parent);
//            if (cmp < 0) {
//                parent.left = e;
//            } else {
//                parent.right = e;
//            }
//
//            this.fixAfterInsertion(e);
//            ++this.size;
//            ++this.modCount;
//            return null;
//        }
//    }
//    final Entry<K, V> getFirstEntry() {
//        Entry<K, V> p = this.root;
//        if (p != null) {
//            while(p.left != null) {
//                p = p.left;
//            }
//        }
//
//        return p;
//    }
//    @Override
//    public Set<Map.Entry<K, V>> entrySet() {
//        return new EntrySet();
//    }
//
//    @Override
//    public V getOrDefault(Object key, V defaultValue) {
//        V res = get(key);
//        return res == null ? defaultValue : res;
//    }
//
//    @Override
//    public void forEach(BiConsumer<? super K, ? super V> action) {
//        Map.super.forEach(action);
//    }
//
//    @Override
//    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
//        Map.super.replaceAll(function);
//    }
//
//    @Override
//    public V putIfAbsent(K key, V value) {
//        return Map.super.putIfAbsent(key, value);
//    }
//
//    static <K, V> Entry<K, V> successor(Entry<K, V> t) {
//        if (t == null) {
//            return null;
//        } else {
//            Entry<K,V> p;
//            if (t.right != null) {
//                for(p = t.right; p.left != null; p = p.left) {
//                }
//
//                return p;
//            } else {
//                p = t.parent;
//
//                for(Entry<K,V> ch = t; p != null && ch == p.right; p = p.parent) {
//                    ch = p;
//                }
//
//                return p;
//            }
//        }
//    }
//
//    static <K, V> Entry<K, V> predecessor(Entry<K, V> t) {
//        if (t == null) {
//            return null;
//        } else {
//            Entry<K,V> p;
//            if (t.left != null) {
//                for(p = t.left; p.right != null; p = p.right) {
//                }
//
//                return p;
//            } else {
//                p = t.parent;
//
//                for(Entry<K,V> ch = t; p != null && ch == p.left; p = p.parent) {
//                    ch = p;
//                }
//
//                return p;
//            }
//        }
//    }
//    private void fixAfterDeletion(Entry<K, V> x) {
//        while(x != this.root && colorOf(x)) {
//            Entry<K,V> sib;
//            if (x == leftOf(parentOf(x))) {
//                sib = rightOf(parentOf(x));
//                if (!colorOf(sib)) {
//                    setColor(sib, true);
//                    setColor(parentOf(x), false);
//                    this.rotateLeft(parentOf(x));
//                    sib = rightOf(parentOf(x));
//                }
//
//                if (colorOf(leftOf(sib)) && colorOf(rightOf(sib))) {
//                    setColor(sib, false);
//                    x = parentOf(x);
//                } else {
//                    if (colorOf(rightOf(sib))) {
//                        setColor(leftOf(sib), true);
//                        setColor(sib, false);
//                        this.rotateRight(sib);
//                        sib = rightOf(parentOf(x));
//                    }
//
//                    setColor(sib, colorOf(parentOf(x)));
//                    setColor(parentOf(x), true);
//                    setColor(rightOf(sib), true);
//                    this.rotateLeft(parentOf(x));
//                    x = this.root;
//                }
//            } else {
//                sib = leftOf(parentOf(x));
//                if (!colorOf(sib)) {
//                    setColor(sib, true);
//                    setColor(parentOf(x), false);
//                    this.rotateRight(parentOf(x));
//                    sib = leftOf(parentOf(x));
//                }
//
//                if (colorOf(rightOf(sib)) && colorOf(leftOf(sib))) {
//                    setColor(sib, false);
//                    x = parentOf(x);
//                } else {
//                    if (colorOf(leftOf(sib))) {
//                        setColor(rightOf(sib), true);
//                        setColor(sib, false);
//                        this.rotateLeft(sib);
//                        sib = leftOf(parentOf(x));
//                    }
//
//                    setColor(sib, colorOf(parentOf(x)));
//                    setColor(parentOf(x), true);
//                    setColor(leftOf(sib), true);
//                    this.rotateRight(parentOf(x));
//                    x = this.root;
//                }
//            }
//        }
//
//        setColor(x, true);
//    }
//    private void deleteEntry(Entry<K, V> p) {
//        ++this.modCount;
//        --this.size;
//        Entry<K,V> replacement;
//        if (p.left != null && p.right != null) {
//            replacement = successor(p);
//            p.key = replacement.key;
//            p.value = replacement.value;
//            p = replacement;
//        }
//
//        replacement = p.left != null ? p.left : p.right;
//        if (replacement != null) {
//            replacement.parent = p.parent;
//            if (p.parent == null) {
//                this.root = replacement;
//            } else if (p == p.parent.left) {
//                p.parent.left = replacement;
//            } else {
//                p.parent.right = replacement;
//            }
//
//            p.left = p.right = p.parent = null;
//            if (p.color) {
//                this.fixAfterDeletion(replacement);
//            }
//        } else if (p.parent == null) {
//            this.root = null;
//        } else {
//            if (p.color) {
//                this.fixAfterDeletion(p);
//            }
//
//            if (p.parent != null) {
//                if (p == p.parent.left) {
//                    p.parent.left = null;
//                } else if (p == p.parent.right) {
//                    p.parent.right = null;
//                }
//
//                p.parent = null;
//            }
//        }
//
//    }
//    @Override
//    public boolean remove(Object key, Object value) {
//        Entry<K, V> e = getEntry((K)key);
//        if(e == null || !Objects.equals(value, e.getValue()))
//            return false;
//        deleteEntry(e);
//        return true;
//    }
//
//    @Override
//    public boolean replace(K key, V oldValue, V newValue) {
//        Entry<K, V> entry = getEntry(key);
//        if(entry == null || !Objects.equals(oldValue, entry.value))
//            return false;
//        entry.value = newValue;
//        return true;
//    }
//
//    @Override
//    public V replace(K key, V value) {
//        return put(key, value);
//    }
//
//    @Override
//    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
//        return Map.super.computeIfAbsent(key, mappingFunction);
//    }
//
//    @Override
//    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//        return Map.super.computeIfPresent(key, remappingFunction);
//    }
//
//    @Override
//    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//        return Map.super.compute(key, remappingFunction);
//    }
//
//    @Override
//    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
//        return Map.super.merge(key, value, remappingFunction);
//    }
//
//    static final class Entry<K, V> implements Map.Entry<K, V>  {
//        K key;
//        V value;
//        Entry<K, V> left;
//        Entry<K, V> right;
//        Entry<K, V> parent;
//        boolean color = true;
//
//        Entry(K key, V value, Entry<K, V> parent) {
//            this.key = key;
//            this.value = value;
//            this.parent = parent;
//        }
//
//        public K getKey() {
//            return this.key;
//        }
//
//        public V getValue() {
//            return this.value;
//        }
//
//        public V setValue(V value) {
//            V oldValue = this.value;
//            this.value = value;
//            return oldValue;
//        }
//
//        public boolean equals(Object o) {
//            if (!(o instanceof java.util.Map.Entry)) {
//                return false;
//            } else {
//                java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
//                return Objects.equals(this.key, e.getKey()) && Objects.equals(this.value, e.getValue());
//            }
//        }
//
//        public int hashCode() {
//            int keyHash = this.key == null ? 0 : this.key.hashCode();
//            int valueHash = this.value == null ? 0 : this.value.hashCode();
//            return keyHash ^ valueHash;
//        }
//
//        public String toString() {
//            return this.key + "=" + this.value;
//        }
//    }
//
//    final class EntryIterator implements Iterator<Map.Entry<K, V>> {
//        Entry<K, V> next;
//        Entry<K, V> lastReturned;
//        int expectedModCount;
//
//        EntryIterator(Entry<K, V> first) {
//            this.expectedModCount = modCount;
//            this.lastReturned = null;
//            this.next = first;
//        }
//
//        public final boolean hasNext() {
//            return this.next != null;
//        }
//
//        public Map.Entry<K, V> next() {
//            Entry<K, V> e = this.next;
//            if (e == null) {
//                throw new NoSuchElementException();
//            } else if (modCount != this.expectedModCount) {
//                throw new ConcurrentModificationException();
//            } else {
//                this.next = successor(e);
//                this.lastReturned = e;
//                return e;
//            }
//        }
//
//        final Entry<K, V> prevEntry() {
//            Entry<K, V> e = this.next;
//            if (e == null) {
//                throw new NoSuchElementException();
//            } else if (modCount != this.expectedModCount) {
//                throw new ConcurrentModificationException();
//            } else {
//                this.next = predecessor(e);
//                this.lastReturned = e;
//                return e;
//            }
//        }
//    }
//
//
//
//    class EntrySet extends AbstractSet<Map.Entry<K, V>> {
//        EntrySet() {
//        }
//
//        public Iterator<Map.Entry<K, V>> iterator() {
//            return new EntryIterator(getFirstEntry());
//        }
//
//        public boolean contains(Object o) {
//            if (!(o instanceof java.util.Map.Entry)) {
//                return false;
//            } else {
//                Map.Entry<K, V> entry = (Map.Entry)o;
//                Object value = entry.getValue();
//                Entry<K, V> p = getEntry(entry.getKey());
//                return p != null && Objects.equals(p.getValue(), value);
//            }
//        }
//
//        public boolean remove(Object o) {
//            if (!(o instanceof java.util.Map.Entry)) {
//                return false;
//            } else {
//                java.util.Map.Entry<K, V> entry = (java.util.Map.Entry)o;
//                Object value = entry.getValue();
//                Entry<K, V> p = getEntry(entry.getKey());
//                if (p != null && Objects.equals(p.getValue(), value)) {
//                    deleteEntry(p);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }
//
//        public int size() {
//            return this.size();
//        }
//
//        public void clear() {
//            this.clear();
//        }
//    }
//}
