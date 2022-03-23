package cn.treeh.ToNX.Collection;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class HashTreeMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Cloneable, Serializable {
    private final Comparator<? super K> comparator;
    private transient HashTreeMap.Entry<K, V> root;
    private transient int size = 0;
    private transient int modCount = 0;
    private transient HashTreeMap<K, V>.EntrySet entrySet;
    private transient HashTreeMap.KeySet<K> navigableKeySet;
    private transient NavigableMap<K, V> descendingMap;
    private static final Object UNBOUNDED = new Object();
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private static final long serialVersionUID = 919286545866124006L;
    private Collection<V> values;

    public HashTreeMap() {
        this.comparator = null;
    }

    public HashTreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    public HashTreeMap(Map<? extends K, ? extends V> m) {
        this.comparator = null;
        this.putAll(m);
    }

    public HashTreeMap(SortedMap<K, ? extends V> m) {
        this.comparator = m.comparator();

        try {
            this.buildFromSorted(m.size(), m.entrySet().iterator(), (ObjectInputStream)null, null);
        } catch (ClassNotFoundException | IOException var3) {
        }

    }

    public int size() {
        return this.size;
    }

    public boolean containsKey(Object key) {
        return this.getEntry((K) key) != null;
    }

    public boolean containsValue(Object value) {
        for(HashTreeMap.Entry<K,V> e = this.getFirstEntry(); e != null; e = successor(e)) {
            if (valEquals(value, e.value)) {
                return true;
            }
        }

        return false;
    }

    public V get(Object key) {
        HashTreeMap.Entry<K, V> p = this.getEntry((K)key);
        return p == null ? null : p.value;
    }

    public Comparator<? super K> comparator() {
        return this.comparator;
    }

    public K firstKey() {
        return key(this.getFirstEntry());
    }

    public K lastKey() {
        return key(this.getLastEntry());
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        super.putAll(map);
    }
    HashTreeMap.Entry<K, V> findEntry(HashTreeMap.Entry<K, V> p, K key, Comparator<? super K> cpr) {
        if(p == null)
            return null;
        if(p.key.equals(key))
            return p;
        if (cpr != null) {
            int cmp = cpr.compare(key, p.key);
            if (cmp < 0) {
                return findEntry(p.left, key, cpr);
            } else if(cmp > 0){
                return findEntry(p.right, key, cpr);
            }
            else{
                HashTreeMap.Entry<K, V> tmp = findEntry(p.left, key, cpr);
                return tmp == null ? findEntry(p.right, key, cpr) : tmp;
            }
        }
        return null;
    }
    final HashTreeMap.Entry<K, V> getEntry(K k) {
        Comparator<? super K> cpr = this.comparator;
        return findEntry(root, k, cpr);
    }

    final HashTreeMap.Entry<K, V> getCeilingEntry(K key) {
        HashTreeMap.Entry<K,V> p = this.root;

        while(p != null) {
            int cmp = this.compare(key, p.key);
            if (cmp < 0) {
                if (p.left == null) {
                    return p;
                }

                p = p.left;
            } else {
                if (cmp <= 0) {
                    return p;
                }

                if (p.right == null) {
                    HashTreeMap.Entry<K, V> parent = p.parent;

                    for(HashTreeMap.Entry<K,V> ch = p; parent != null && ch == parent.right; parent = parent.parent) {
                        ch = parent;
                    }

                    return parent;
                }

                p = p.right;
            }
        }

        return null;
    }

    final HashTreeMap.Entry<K, V> getFloorEntry(K key) {
        HashTreeMap.Entry<K,V> p = this.root;

        while(p != null) {
            int cmp = this.compare(key, p.key);
            if (cmp > 0) {
                if (p.right == null) {
                    return p;
                }

                p = p.right;
            } else {
                if (cmp >= 0) {
                    return p;
                }

                if (p.left == null) {
                    HashTreeMap.Entry<K, V> parent = p.parent;

                    for(HashTreeMap.Entry<K,V> ch = p; parent != null && ch == parent.left; parent = parent.parent) {
                        ch = parent;
                    }

                    return parent;
                }

                p = p.left;
            }
        }

        return null;
    }

    final HashTreeMap.Entry<K, V> getHigherEntry(K key) {
        HashTreeMap.Entry<K,V> p = this.root;

        while(p != null) {
            int cmp = this.compare(key, p.key);
            if (cmp < 0) {
                if (p.left == null) {
                    return p;
                }

                p = p.left;
            } else {
                if (p.right == null) {
                    HashTreeMap.Entry<K, V> parent = p.parent;

                    for(HashTreeMap.Entry<K,V> ch = p; parent != null && ch == parent.right; parent = parent.parent) {
                        ch = parent;
                    }

                    return parent;
                }

                p = p.right;
            }
        }

        return null;
    }

    final HashTreeMap.Entry<K, V> getLowerEntry(K key) {
        HashTreeMap.Entry<K,V> p = this.root;

        while(p != null) {
            int cmp = this.compare(key, p.key);
            if (cmp > 0) {
                if (p.right == null) {
                    return p;
                }

                p = p.right;
            } else {
                if (p.left == null) {
                    HashTreeMap.Entry<K, V> parent = p.parent;

                    for(HashTreeMap.Entry<K,V> ch = p; parent != null && ch == parent.left; parent = parent.parent) {
                        ch = parent;
                    }

                    return parent;
                }

                p = p.left;
            }
        }

        return null;
    }

    public V put(K key, V value) {
        HashTreeMap.Entry<K, V> t = this.root;
        if (t == null) {
            this.compare(key, key);
            this.root = new HashTreeMap.Entry(key, value, (HashTreeMap.Entry)null);
            this.size = 1;
            ++this.modCount;
            return null;
        } else {
            HashTreeMap.Entry<K, V> entry = getEntry(key);
            if(entry != null) {
                entry.setValue(value);
                return value;
            }
            Comparator<? super K> cpr = this.comparator;
            int cmp;
            HashTreeMap.Entry<K,V> parent;
            if (cpr != null) {
                do {
                    parent = t;
                    cmp = cpr.compare(key, t.key);
                    if (cmp < 0) {
                        t = t.left;
                    } else {
                        t = t.right;
                    }
                } while(t != null);
            } else {
                if (key == null) {
                    throw new NullPointerException();
                }

                Comparable k = (Comparable)key;

                do {
                    parent = t;
                    cmp = k.compareTo(t.key);
                    if (cmp < 0) {
                        t = t.left;
                    } else {
                        t = t.right;
                    }
                } while(t != null);
            }

            HashTreeMap.Entry<K, V> e = new HashTreeMap.Entry(key, value, parent);
            if (cmp < 0) {
                parent.left = e;
            } else {
                parent.right = e;
            }

            this.fixAfterInsertion(e);
            ++this.size;
            ++this.modCount;
            return null;
        }
    }

    public V remove(Object key) {
        HashTreeMap.Entry<K, V> p = this.getEntry((K)key);
        if (p == null) {
            return null;
        } else {
            V oldValue = p.value;
            this.deleteEntry(p);
            return oldValue;
        }
    }

    public void clear() {
        ++this.modCount;
        this.size = 0;
        this.root = null;
    }

    public Object clone() {
        HashTreeMap clone;
        try {
            clone = (HashTreeMap)super.clone();
        } catch (CloneNotSupportedException var4) {
            throw new InternalError(var4);
        }

        clone.root = null;
        clone.size = 0;
        clone.modCount = 0;
        clone.entrySet = null;
        clone.navigableKeySet = null;
        clone.descendingMap = null;

        try {
            clone.buildFromSorted(this.size, this.entrySet().iterator(), (ObjectInputStream)null, (Object)null);
        } catch (ClassNotFoundException | IOException var3) {
        }

        return clone;
    }

    public java.util.Map.Entry<K, V> firstEntry() {
        return exportEntry(this.getFirstEntry());
    }

    public java.util.Map.Entry<K, V> lastEntry() {
        return exportEntry(this.getLastEntry());
    }

    public java.util.Map.Entry<K, V> pollFirstEntry() {
        HashTreeMap.Entry<K, V> p = this.getFirstEntry();
        java.util.Map.Entry<K, V> result = exportEntry(p);
        if (p != null) {
            this.deleteEntry(p);
        }

        return result;
    }

    public java.util.Map.Entry<K, V> pollLastEntry() {
        HashTreeMap.Entry<K, V> p = this.getLastEntry();
        java.util.Map.Entry<K, V> result = exportEntry(p);
        if (p != null) {
            this.deleteEntry(p);
        }

        return result;
    }

    public java.util.Map.Entry<K, V> lowerEntry(K key) {
        return exportEntry(this.getLowerEntry(key));
    }

    public K lowerKey(K key) {
        return keyOrNull(this.getLowerEntry(key));
    }

    public java.util.Map.Entry<K, V> floorEntry(K key) {
        return exportEntry(this.getFloorEntry(key));
    }

    public K floorKey(K key) {
        return keyOrNull(this.getFloorEntry(key));
    }

    public java.util.Map.Entry<K, V> ceilingEntry(K key) {
        return exportEntry(this.getCeilingEntry(key));
    }

    public K ceilingKey(K key) {
        return keyOrNull(this.getCeilingEntry(key));
    }

    public java.util.Map.Entry<K, V> higherEntry(K key) {
        return exportEntry(this.getHigherEntry(key));
    }

    public K higherKey(K key) {
        return keyOrNull(this.getHigherEntry(key));
    }

    public Set<K> keySet() {
        return this.navigableKeySet();
    }

    public NavigableSet<K> navigableKeySet() {
        HashTreeMap.KeySet<K> nks = this.navigableKeySet;
        return nks != null ? nks : (this.navigableKeySet = new HashTreeMap.KeySet(this));
    }

    public NavigableSet<K> descendingKeySet() {
        return this.descendingMap().navigableKeySet();
    }

    public Collection<V> values() {
        Collection<V> vs = this.values;
        if (vs == null) {
            vs = new HashTreeMap.Values();
            this.values = (Collection)vs;
        }

        return (Collection)vs;
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        HashTreeMap<K, V>.EntrySet es = this.entrySet;
        return es != null ? es : (this.entrySet = new HashTreeMap.EntrySet());
    }

    public NavigableMap<K, V> descendingMap() {
        NavigableMap<K, V> km = this.descendingMap;
        return km != null ? km : (this.descendingMap = new HashTreeMap.DescendingSubMap(this, true, (Object)null, true, true, (Object)null, true));
    }

    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return new HashTreeMap.AscendingSubMap(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
    }

    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return new HashTreeMap.AscendingSubMap(this, true, (Object)null, true, false, toKey, inclusive);
    }

    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return new HashTreeMap.AscendingSubMap(this, false, fromKey, inclusive, true, (Object)null, true);
    }

    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return this.subMap(fromKey, true, toKey, false);
    }

    public SortedMap<K, V> headMap(K toKey) {
        return this.headMap(toKey, false);
    }

    public SortedMap<K, V> tailMap(K fromKey) {
        return this.tailMap(fromKey, true);
    }

    public boolean replace(K key, V oldValue, V newValue) {
        HashTreeMap.Entry<K, V> p = this.getEntry(key);
        if (p != null && Objects.equals(oldValue, p.value)) {
            p.value = newValue;
            return true;
        } else {
            return false;
        }
    }

    public V replace(K key, V value) {
        HashTreeMap.Entry<K, V> p = this.getEntry(key);
        if (p != null) {
            V oldValue = p.value;
            p.value = value;
            return oldValue;
        } else {
            return null;
        }
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        int expectedModCount = this.modCount;

        for(HashTreeMap.Entry<K,V> e = this.getFirstEntry(); e != null; e = successor(e)) {
            action.accept(e.key, e.value);
            if (expectedModCount != this.modCount) {
                throw new ConcurrentModificationException();
            }
        }

    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        int expectedModCount = this.modCount;

        for(HashTreeMap.Entry<K,V> e = this.getFirstEntry(); e != null; e = successor(e)) {
            e.value = function.apply(e.key, e.value);
            if (expectedModCount != this.modCount) {
                throw new ConcurrentModificationException();
            }
        }

    }

    Iterator<K> keyIterator() {
        return new HashTreeMap.KeyIterator(this.getFirstEntry());
    }

    Iterator<K> descendingKeyIterator() {
        return new HashTreeMap.DescendingKeyIterator(this.getLastEntry());
    }

    final int compare(Object k1, Object k2) {
        return this.comparator == null ? ((Comparable)k1).compareTo(k2) :
                this.comparator.compare((K)k1, (K)k2);
    }

    static final boolean valEquals(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    static <K, V> java.util.Map.Entry<K, V> exportEntry(HashTreeMap.Entry<K, V> e) {
        return e == null ? null : new SimpleImmutableEntry(e);
    }

    static <K, V> K keyOrNull(HashTreeMap.Entry<K, V> e) {
        return e == null ? null : e.key;
    }

    static <K> K key(HashTreeMap.Entry<K, ?> e) {
        if (e == null) {
            throw new NoSuchElementException();
        } else {
            return e.key;
        }
    }

    final HashTreeMap.Entry<K, V> getFirstEntry() {
        HashTreeMap.Entry<K, V> p = this.root;
        if (p != null) {
            while(p.left != null) {
                p = p.left;
            }
        }

        return p;
    }

    final HashTreeMap.Entry<K, V> getLastEntry() {
        HashTreeMap.Entry<K, V> p = this.root;
        if (p != null) {
            while(p.right != null) {
                p = p.right;
            }
        }

        return p;
    }

    static <K, V> HashTreeMap.Entry<K, V> successor(HashTreeMap.Entry<K, V> t) {
        if (t == null) {
            return null;
        } else {
            HashTreeMap.Entry<K,V> p;
            if (t.right != null) {
                for(p = t.right; p.left != null; p = p.left) {
                }

                return p;
            } else {
                p = t.parent;

                for(HashTreeMap.Entry<K,V> ch = t; p != null && ch == p.right; p = p.parent) {
                    ch = p;
                }

                return p;
            }
        }
    }

    static <K, V> HashTreeMap.Entry<K, V> predecessor(HashTreeMap.Entry<K, V> t) {
        if (t == null) {
            return null;
        } else {
            HashTreeMap.Entry<K,V> p;
            if (t.left != null) {
                for(p = t.left; p.right != null; p = p.right) {
                }

                return p;
            } else {
                p = t.parent;

                for(HashTreeMap.Entry<K,V> ch = t; p != null && ch == p.left; p = p.parent) {
                    ch = p;
                }

                return p;
            }
        }
    }

    private static <K, V> boolean colorOf(HashTreeMap.Entry<K, V> p) {
        return p == null ? true : p.color;
    }

    private static <K, V> HashTreeMap.Entry<K, V> parentOf(HashTreeMap.Entry<K, V> p) {
        return p == null ? null : p.parent;
    }

    private static <K, V> void setColor(HashTreeMap.Entry<K, V> p, boolean c) {
        if (p != null) {
            p.color = c;
        }

    }

    private static <K, V> HashTreeMap.Entry<K, V> leftOf(HashTreeMap.Entry<K, V> p) {
        return p == null ? null : p.left;
    }

    private static <K, V> HashTreeMap.Entry<K, V> rightOf(HashTreeMap.Entry<K, V> p) {
        return p == null ? null : p.right;
    }

    private void rotateLeft(HashTreeMap.Entry<K, V> p) {
        if (p != null) {
            HashTreeMap.Entry<K, V> r = p.right;
            p.right = r.left;
            if (r.left != null) {
                r.left.parent = p;
            }

            r.parent = p.parent;
            if (p.parent == null) {
                this.root = r;
            } else if (p.parent.left == p) {
                p.parent.left = r;
            } else {
                p.parent.right = r;
            }

            r.left = p;
            p.parent = r;
        }

    }

    private void rotateRight(HashTreeMap.Entry<K, V> p) {
        if (p != null) {
            HashTreeMap.Entry<K, V> l = p.left;
            p.left = l.right;
            if (l.right != null) {
                l.right.parent = p;
            }

            l.parent = p.parent;
            if (p.parent == null) {
                this.root = l;
            } else if (p.parent.right == p) {
                p.parent.right = l;
            } else {
                p.parent.left = l;
            }

            l.right = p;
            p.parent = l;
        }

    }

    private void fixAfterInsertion(HashTreeMap.Entry<K, V> x) {
        x.color = false;

        while(x != null && x != this.root && !x.parent.color) {
            HashTreeMap.Entry<K,V> y;
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                y = rightOf(parentOf(parentOf(x)));
                if (!colorOf(y)) {
                    setColor(parentOf(x), true);
                    setColor(y, true);
                    setColor(parentOf(parentOf(x)), false);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        this.rotateLeft(x);
                    }

                    setColor(parentOf(x), true);
                    setColor(parentOf(parentOf(x)), false);
                    this.rotateRight(parentOf(parentOf(x)));
                }
            } else {
                y = leftOf(parentOf(parentOf(x)));
                if (!colorOf(y)) {
                    setColor(parentOf(x), true);
                    setColor(y, true);
                    setColor(parentOf(parentOf(x)), false);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        this.rotateRight(x);
                    }

                    setColor(parentOf(x), true);
                    setColor(parentOf(parentOf(x)), false);
                    this.rotateLeft(parentOf(parentOf(x)));
                }
            }
        }

        this.root.color = true;
    }

    private void deleteEntry(HashTreeMap.Entry<K, V> p) {
        ++this.modCount;
        --this.size;
        HashTreeMap.Entry<K,V> replacement;
        if (p.left != null && p.right != null) {
            replacement = successor(p);
            p.key = replacement.key;
            p.value = replacement.value;
            p = replacement;
        }

        replacement = p.left != null ? p.left : p.right;
        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) {
                this.root = replacement;
            } else if (p == p.parent.left) {
                p.parent.left = replacement;
            } else {
                p.parent.right = replacement;
            }

            p.left = p.right = p.parent = null;
            if (p.color) {
                this.fixAfterDeletion(replacement);
            }
        } else if (p.parent == null) {
            this.root = null;
        } else {
            if (p.color) {
                this.fixAfterDeletion(p);
            }

            if (p.parent != null) {
                if (p == p.parent.left) {
                    p.parent.left = null;
                } else if (p == p.parent.right) {
                    p.parent.right = null;
                }

                p.parent = null;
            }
        }

    }

    private void fixAfterDeletion(HashTreeMap.Entry<K, V> x) {
        while(x != this.root && colorOf(x)) {
            HashTreeMap.Entry<K,V> sib;
            if (x == leftOf(parentOf(x))) {
                sib = rightOf(parentOf(x));
                if (!colorOf(sib)) {
                    setColor(sib, true);
                    setColor(parentOf(x), false);
                    this.rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib)) && colorOf(rightOf(sib))) {
                    setColor(sib, false);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib))) {
                        setColor(leftOf(sib), true);
                        setColor(sib, false);
                        this.rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }

                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), true);
                    setColor(rightOf(sib), true);
                    this.rotateLeft(parentOf(x));
                    x = this.root;
                }
            } else {
                sib = leftOf(parentOf(x));
                if (!colorOf(sib)) {
                    setColor(sib, true);
                    setColor(parentOf(x), false);
                    this.rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) && colorOf(leftOf(sib))) {
                    setColor(sib, false);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib))) {
                        setColor(rightOf(sib), true);
                        setColor(sib, false);
                        this.rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }

                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), true);
                    setColor(leftOf(sib), true);
                    this.rotateRight(parentOf(x));
                    x = this.root;
                }
            }
        }

        setColor(x, true);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.size);
        Iterator var2 = this.entrySet().iterator();

        while(var2.hasNext()) {
            java.util.Map.Entry<K, V> e = (java.util.Map.Entry)var2.next();
            s.writeObject(e.getKey());
            s.writeObject(e.getValue());
        }

    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int size = s.readInt();
        this.buildFromSorted(size, (Iterator)null, s, null);
    }

    void readTreeSet(int size, ObjectInputStream s, V defaultVal) throws IOException, ClassNotFoundException {
        this.buildFromSorted(size, (Iterator)null, s, defaultVal);
    }

    void addAllForTreeSet(SortedSet<? extends K> set, V defaultVal) {
        try {
            this.buildFromSorted(set.size(), set.iterator(), (ObjectInputStream)null, defaultVal);
        } catch (ClassNotFoundException | IOException var4) {
        }

    }

    private void buildFromSorted(int size, Iterator<?> it, ObjectInputStream str, V defaultVal) throws IOException, ClassNotFoundException {
        this.size = size;
        this.root = this.buildFromSorted(0, 0, size - 1, computeRedLevel(size), it, str, defaultVal);
    }

    private final HashTreeMap.Entry<K, V> buildFromSorted(int level, int lo, int hi, int redLevel, Iterator<?> it, ObjectInputStream str, V defaultVal) throws IOException, ClassNotFoundException {
        if (hi < lo) {
            return null;
        } else {
            int mid = lo + hi >>> 1;
            HashTreeMap.Entry<K, V> left = null;
            if (lo < mid) {
                left = this.buildFromSorted(level + 1, lo, mid - 1, redLevel, it, str, defaultVal);
            }

            Object key;
            Object value;
            if (it != null) {
                if (defaultVal == null) {
                    java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry)it.next();
                    key = entry.getKey();
                    value = entry.getValue();
                } else {
                    key = it.next();
                    value = defaultVal;
                }
            } else {
                key = str.readObject();
                value = defaultVal != null ? defaultVal : str.readObject();
            }

            HashTreeMap.Entry<K, V> middle = new HashTreeMap.Entry(key, value, (HashTreeMap.Entry)null);
            if (level == redLevel) {
                middle.color = false;
            }

            if (left != null) {
                middle.left = left;
                left.parent = middle;
            }

            if (mid < hi) {
                HashTreeMap.Entry<K, V> right = this.buildFromSorted(level + 1, mid + 1, hi, redLevel, it, str, defaultVal);
                middle.right = right;
                right.parent = middle;
            }

            return middle;
        }
    }

    private static int computeRedLevel(int size) {
        return 31 - Integer.numberOfLeadingZeros(size + 1);
    }

    static <K> Spliterator<K> keySpliteratorFor(NavigableMap<K, ?> m) {
        if (m instanceof HashTreeMap) {
            HashTreeMap<K, Object> t = (HashTreeMap)m;
            return t.keySpliterator();
        } else {
            if (m instanceof HashTreeMap.DescendingSubMap) {
                HashTreeMap.DescendingSubMap<K, ?> dm = (HashTreeMap.DescendingSubMap)m;
                HashTreeMap<K, ?> tm = dm.m;
                if (dm == tm.descendingMap) {
                    return tm.descendingKeySpliterator();
                }
            }

            HashTreeMap.NavigableSubMap<K, ?> sm = (HashTreeMap.NavigableSubMap)m;
            return sm.keySpliterator();
        }
    }

    final Spliterator<K> keySpliterator() {
        return new HashTreeMap.KeySpliterator(this, (HashTreeMap.Entry)null, (HashTreeMap.Entry)null, 0, -1, 0);
    }

    final Spliterator<K> descendingKeySpliterator() {
        return new HashTreeMap.DescendingKeySpliterator(this, (HashTreeMap.Entry)null, (HashTreeMap.Entry)null, 0, -2, 0);
    }

    static final class EntrySpliterator<K, V> extends HashTreeMap.HashTreeMapSpliterator<K, V> implements Spliterator<java.util.Map.Entry<K, V>> {
        EntrySpliterator(HashTreeMap<K, V> tree, HashTreeMap.Entry<K, V> origin, HashTreeMap.Entry<K, V> fence, int side, int est, int expectedModCount) {
            super(tree, origin, fence, side, est, expectedModCount);
        }

        public HashTreeMap.EntrySpliterator<K, V> trySplit() {
            if (this.est < 0) {
                this.getEstimate();
            }

            int d = this.side;
            HashTreeMap.Entry<K, V> e = this.current;
            HashTreeMap.Entry<K, V> f = this.fence;
            HashTreeMap.Entry<K, V> s = e != null && e != f ? (d == 0 ? this.tree.root : (d > 0 ? e.right : (d < 0 && f != null ? f.left : null))) : null;
            if (s != null && s != e && s != f && this.tree.compare(e.key, s.key) < 0) {
                this.side = 1;
                return new HashTreeMap.EntrySpliterator(this.tree, e, this.current = s, -1, this.est >>>= 1, this.expectedModCount);
            } else {
                return null;
            }
        }

        public void forEachRemaining(Consumer<? super java.util.Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            } else {
                if (this.est < 0) {
                    this.getEstimate();
                }

                HashTreeMap.Entry<K, V> f = this.fence;
                HashTreeMap.Entry<K,V> e;
                if ((e = this.current) != null && e != f) {
                    this.current = f;

                    HashTreeMap.Entry<K,V> p;
                    do {
                        action.accept(e);
                        HashTreeMap.Entry<K,V> pl;
                        if ((p = e.right) != null) {
                            while((pl = p.left) != null) {
                                p = pl;
                            }
                        } else {
                            while((p = e.parent) != null && e == p.right) {
                                e = p;
                            }
                        }

                        e = p;
                    } while(p != null && p != f);

                    if (this.tree.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }

            }
        }

        public boolean tryAdvance(Consumer<? super java.util.Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            } else {
                if (this.est < 0) {
                    this.getEstimate();
                }

                HashTreeMap.Entry<K,V> e;
                if ((e = this.current) != null && e != this.fence) {
                    this.current = HashTreeMap.successor(e);
                    action.accept(e);
                    if (this.tree.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 1 | 4 | 16;
        }

        public Comparator<Map.Entry<K, V>> getComparator() {
            // Adapt or create a key-based comparator
            if (tree.comparator != null) {
                return Map.Entry.comparingByKey(tree.comparator);
            }
            else {
                return (Comparator<Map.Entry<K, V>> & Serializable) (e1, e2) -> {
                    @SuppressWarnings("unchecked")
                    Comparable<? super K> k1 = (Comparable<? super K>) e1.getKey();
                    return k1.compareTo(e2.getKey());
                };
            }
        }
    }

    static final class ValueSpliterator<K, V> extends HashTreeMap.HashTreeMapSpliterator<K, V> implements Spliterator<V> {
        ValueSpliterator(HashTreeMap<K, V> tree, HashTreeMap.Entry<K, V> origin, HashTreeMap.Entry<K, V> fence, int side, int est, int expectedModCount) {
            super(tree, origin, fence, side, est, expectedModCount);
        }

        public HashTreeMap.ValueSpliterator<K, V> trySplit() {
            if (this.est < 0) {
                this.getEstimate();
            }

            int d = this.side;
            HashTreeMap.Entry<K, V> e = this.current;
            HashTreeMap.Entry<K, V> f = this.fence;
            HashTreeMap.Entry<K, V> s = e != null && e != f ? (d == 0 ? this.tree.root : (d > 0 ? e.right : (d < 0 && f != null ? f.left : null))) : null;
            if (s != null && s != e && s != f && this.tree.compare(e.key, s.key) < 0) {
                this.side = 1;
                return new HashTreeMap.ValueSpliterator(this.tree, e, this.current = s, -1, this.est >>>= 1, this.expectedModCount);
            } else {
                return null;
            }
        }

        public void forEachRemaining(Consumer<? super V> action) {
            if (action == null) {
                throw new NullPointerException();
            } else {
                if (this.est < 0) {
                    this.getEstimate();
                }

                HashTreeMap.Entry<K, V> f = this.fence;
                HashTreeMap.Entry<K,V> e;
                if ((e = this.current) != null && e != f) {
                    this.current = f;

                    HashTreeMap.Entry<K,V> p;
                    do {
                        action.accept(e.value);
                        HashTreeMap.Entry<K,V> pl;
                        if ((p = e.right) != null) {
                            while((pl = p.left) != null) {
                                p = pl;
                            }
                        } else {
                            while((p = e.parent) != null && e == p.right) {
                                e = p;
                            }
                        }

                        e = p;
                    } while(p != null && p != f);

                    if (this.tree.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }

            }
        }

        public boolean tryAdvance(Consumer<? super V> action) {
            if (action == null) {
                throw new NullPointerException();
            } else {
                if (this.est < 0) {
                    this.getEstimate();
                }

                HashTreeMap.Entry<K,V> e;
                if ((e = this.current) != null && e != this.fence) {
                    this.current = HashTreeMap.successor(e);
                    action.accept(e.value);
                    if (this.tree.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 16;
        }
    }

    static final class DescendingKeySpliterator<K, V> extends HashTreeMap.HashTreeMapSpliterator<K, V> implements Spliterator<K> {
        DescendingKeySpliterator(HashTreeMap<K, V> tree, HashTreeMap.Entry<K, V> origin, HashTreeMap.Entry<K, V> fence, int side, int est, int expectedModCount) {
            super(tree, origin, fence, side, est, expectedModCount);
        }

        public HashTreeMap.DescendingKeySpliterator<K, V> trySplit() {
            if (this.est < 0) {
                this.getEstimate();
            }

            int d = this.side;
            HashTreeMap.Entry<K, V> e = this.current;
            HashTreeMap.Entry<K, V> f = this.fence;
            HashTreeMap.Entry<K, V> s = e != null && e != f ? (d == 0 ? this.tree.root : (d < 0 ? e.left : (d > 0 && f != null ? f.right : null))) : null;
            if (s != null && s != e && s != f && this.tree.compare(e.key, s.key) > 0) {
                this.side = 1;
                return new HashTreeMap.DescendingKeySpliterator(this.tree, e, this.current = s, -1, this.est >>>= 1, this.expectedModCount);
            } else {
                return null;
            }
        }

        public void forEachRemaining(Consumer<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            } else {
                if (this.est < 0) {
                    this.getEstimate();
                }

                HashTreeMap.Entry<K, V> f = this.fence;
                HashTreeMap.Entry<K,V> e;
                if ((e = this.current) != null && e != f) {
                    this.current = f;

                    HashTreeMap.Entry<K,V> p;
                    do {
                        action.accept(e.key);
                        HashTreeMap.Entry<K,V> pr;
                        if ((p = e.left) != null) {
                            while((pr = p.right) != null) {
                                p = pr;
                            }
                        } else {
                            while((p = e.parent) != null && e == p.left) {
                                e = p;
                            }
                        }

                        e = p;
                    } while(p != null && p != f);

                    if (this.tree.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }

            }
        }

        public boolean tryAdvance(Consumer<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            } else {
                if (this.est < 0) {
                    this.getEstimate();
                }

                HashTreeMap.Entry<K,V> e;
                if ((e = this.current) != null && e != this.fence) {
                    this.current = HashTreeMap.predecessor(e);
                    action.accept(e.key);
                    if (this.tree.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 1 | 16;
        }
    }

    static final class KeySpliterator<K, V> extends HashTreeMap.HashTreeMapSpliterator<K, V> implements Spliterator<K> {
        KeySpliterator(HashTreeMap<K, V> tree, HashTreeMap.Entry<K, V> origin, HashTreeMap.Entry<K, V> fence, int side, int est, int expectedModCount) {
            super(tree, origin, fence, side, est, expectedModCount);
        }

        public HashTreeMap.KeySpliterator<K, V> trySplit() {
            if (this.est < 0) {
                this.getEstimate();
            }

            int d = this.side;
            HashTreeMap.Entry<K, V> e = this.current;
            HashTreeMap.Entry<K, V> f = this.fence;
            HashTreeMap.Entry<K, V> s = e != null && e != f ? (d == 0 ? this.tree.root : (d > 0 ? e.right : (d < 0 && f != null ? f.left : null))) : null;
            if (s != null && s != e && s != f && this.tree.compare(e.key, s.key) < 0) {
                this.side = 1;
                return new HashTreeMap.KeySpliterator(this.tree, e, this.current = s, -1, this.est >>>= 1, this.expectedModCount);
            } else {
                return null;
            }
        }

        public void forEachRemaining(Consumer<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            } else {
                if (this.est < 0) {
                    this.getEstimate();
                }

                HashTreeMap.Entry<K, V> f = this.fence;
                HashTreeMap.Entry<K,V> e;
                if ((e = this.current) != null && e != f) {
                    this.current = f;

                    HashTreeMap.Entry<K,V> p;
                    do {
                        action.accept(e.key);
                        HashTreeMap.Entry<K,V> pl;
                        if ((p = e.right) != null) {
                            while((pl = p.left) != null) {
                                p = pl;
                            }
                        } else {
                            while((p = e.parent) != null && e == p.right) {
                                e = p;
                            }
                        }

                        e = p;
                    } while(p != null && p != f);

                    if (this.tree.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }

            }
        }

        public boolean tryAdvance(Consumer<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            } else {
                if (this.est < 0) {
                    this.getEstimate();
                }

                HashTreeMap.Entry<K,V> e;
                if ((e = this.current) != null && e != this.fence) {
                    this.current = HashTreeMap.successor(e);
                    action.accept(e.key);
                    if (this.tree.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 1 | 4 | 16;
        }

        public final Comparator<? super K> getComparator() {
            return this.tree.comparator;
        }
    }

    static class HashTreeMapSpliterator<K, V> {
        final HashTreeMap<K, V> tree;
        HashTreeMap.Entry<K, V> current;
        HashTreeMap.Entry<K, V> fence;
        int side;
        int est;
        int expectedModCount;

        HashTreeMapSpliterator(HashTreeMap<K, V> tree, HashTreeMap.Entry<K, V> origin, HashTreeMap.Entry<K, V> fence, int side, int est, int expectedModCount) {
            this.tree = tree;
            this.current = origin;
            this.fence = fence;
            this.side = side;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        final int getEstimate() {
            int s;
            if ((s = this.est) < 0) {
                HashTreeMap t;
                if ((t = this.tree) != null) {
                    this.current = s == -1 ? t.getFirstEntry() : t.getLastEntry();
                    s = this.est = t.size;
                    this.expectedModCount = t.modCount;
                } else {
                    s = this.est = 0;
                }
            }

            return s;
        }

        public final long estimateSize() {
            return (long)this.getEstimate();
        }
    }

    static final class Entry<K, V> implements java.util.Map.Entry<K, V> {
        K key;
        V value;
        HashTreeMap.Entry<K, V> left;
        HashTreeMap.Entry<K, V> right;
        HashTreeMap.Entry<K, V> parent;
        boolean color = true;

        Entry(K key, V value, HashTreeMap.Entry<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof java.util.Map.Entry)) {
                return false;
            } else {
                java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
                return HashTreeMap.valEquals(this.key, e.getKey()) && HashTreeMap.valEquals(this.value, e.getValue());
            }
        }

        public int hashCode() {
            int keyHash = this.key == null ? 0 : this.key.hashCode();
            int valueHash = this.value == null ? 0 : this.value.hashCode();
            return keyHash ^ valueHash;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }


    static final class DescendingSubMap<K, V> extends HashTreeMap.NavigableSubMap<K, V> {
        private static final long serialVersionUID = 912986545866120460L;
        private final Comparator<? super K> reverseComparator;

        DescendingSubMap(HashTreeMap<K, V> m, boolean fromStart, K lo, boolean loInclusive, boolean toEnd, K hi, boolean hiInclusive) {
            super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
            this.reverseComparator = Collections.reverseOrder(this.m.comparator);
        }

        public Comparator<? super K> comparator() {
            return this.reverseComparator;
        }

        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            if (!this.inRange(fromKey, fromInclusive)) {
                throw new IllegalArgumentException("fromKey out of range");
            } else if (!this.inRange(toKey, toInclusive)) {
                throw new IllegalArgumentException("toKey out of range");
            } else {
                return new HashTreeMap.DescendingSubMap(this.m, false, toKey, toInclusive, false, fromKey, fromInclusive);
            }
        }

        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            if (!this.inRange(toKey, inclusive)) {
                throw new IllegalArgumentException("toKey out of range");
            } else {
                return new HashTreeMap.DescendingSubMap(this.m, false, toKey, inclusive, this.toEnd, this.hi, this.hiInclusive);
            }
        }

        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            if (!this.inRange(fromKey, inclusive)) {
                throw new IllegalArgumentException("fromKey out of range");
            } else {
                return new HashTreeMap.DescendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, false, fromKey, inclusive);
            }
        }

        public NavigableMap<K, V> descendingMap() {
            NavigableMap<K, V> mv = this.descendingMapView;
            return mv != null ? mv : (this.descendingMapView = new HashTreeMap.AscendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, this.toEnd, this.hi, this.hiInclusive));
        }

        Iterator<K> keyIterator() {
            return new HashTreeMap.NavigableSubMap.DescendingSubMapKeyIterator(this.absHighest(), this.absLowFence());
        }

        Spliterator<K> keySpliterator() {
            return new HashTreeMap.NavigableSubMap.DescendingSubMapKeyIterator(this.absHighest(), this.absLowFence());
        }

        Iterator<K> descendingKeyIterator() {
            return new HashTreeMap.NavigableSubMap.SubMapKeyIterator(this.absLowest(), this.absHighFence());
        }

        public Set<java.util.Map.Entry<K, V>> entrySet() {
            HashTreeMap.NavigableSubMap<K, V>.EntrySetView es = this.entrySetView;
            return es != null ? es : (this.entrySetView = new HashTreeMap.DescendingSubMap.DescendingEntrySetView());
        }

        HashTreeMap.Entry<K, V> subLowest() {
            return this.absHighest();
        }

        HashTreeMap.Entry<K, V> subHighest() {
            return this.absLowest();
        }

        HashTreeMap.Entry<K, V> subCeiling(K key) {
            return this.absFloor(key);
        }

        HashTreeMap.Entry<K, V> subHigher(K key) {
            return this.absLower(key);
        }

        HashTreeMap.Entry<K, V> subFloor(K key) {
            return this.absCeiling(key);
        }

        HashTreeMap.Entry<K, V> subLower(K key) {
            return this.absHigher(key);
        }

        final class DescendingEntrySetView extends HashTreeMap.NavigableSubMap<K, V>.EntrySetView {
            DescendingEntrySetView() {
                super();
            }

            public Iterator<java.util.Map.Entry<K, V>> iterator() {
                return DescendingSubMap.this.new DescendingSubMapEntryIterator(DescendingSubMap.this.absHighest(), DescendingSubMap.this.absLowFence());
            }
        }
    }

    static final class AscendingSubMap<K, V> extends HashTreeMap.NavigableSubMap<K, V> {
        private static final long serialVersionUID = 912986545866124060L;

        AscendingSubMap(HashTreeMap<K, V> m, boolean fromStart, K lo, boolean loInclusive, boolean toEnd, K hi, boolean hiInclusive) {
            super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
        }

        public Comparator<? super K> comparator() {
            return this.m.comparator();
        }

        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            if (!this.inRange(fromKey, fromInclusive)) {
                throw new IllegalArgumentException("fromKey out of range");
            } else if (!this.inRange(toKey, toInclusive)) {
                throw new IllegalArgumentException("toKey out of range");
            } else {
                return new HashTreeMap.AscendingSubMap(this.m, false, fromKey, fromInclusive, false, toKey, toInclusive);
            }
        }

        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            if (!this.inRange(toKey, inclusive)) {
                throw new IllegalArgumentException("toKey out of range");
            } else {
                return new HashTreeMap.AscendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, false, toKey, inclusive);
            }
        }

        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            if (!this.inRange(fromKey, inclusive)) {
                throw new IllegalArgumentException("fromKey out of range");
            } else {
                return new HashTreeMap.AscendingSubMap(this.m, false, fromKey, inclusive, this.toEnd, this.hi, this.hiInclusive);
            }
        }

        public NavigableMap<K, V> descendingMap() {
            NavigableMap<K, V> mv = this.descendingMapView;
            return mv != null ? mv : (this.descendingMapView = new HashTreeMap.DescendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, this.toEnd, this.hi, this.hiInclusive));
        }

        Iterator<K> keyIterator() {
            return new HashTreeMap.NavigableSubMap.SubMapKeyIterator(this.absLowest(), this.absHighFence());
        }

        Spliterator<K> keySpliterator() {
            return new HashTreeMap.NavigableSubMap.SubMapKeyIterator(this.absLowest(), this.absHighFence());
        }

        Iterator<K> descendingKeyIterator() {
            return new HashTreeMap.NavigableSubMap.DescendingSubMapKeyIterator(this.absHighest(), this.absLowFence());
        }

        public Set<java.util.Map.Entry<K, V>> entrySet() {
            HashTreeMap.NavigableSubMap<K, V>.EntrySetView es = this.entrySetView;
            return es != null ? es : (this.entrySetView = new HashTreeMap.AscendingSubMap.AscendingEntrySetView());
        }

        HashTreeMap.Entry<K, V> subLowest() {
            return this.absLowest();
        }

        HashTreeMap.Entry<K, V> subHighest() {
            return this.absHighest();
        }

        HashTreeMap.Entry<K, V> subCeiling(K key) {
            return this.absCeiling(key);
        }

        HashTreeMap.Entry<K, V> subHigher(K key) {
            return this.absHigher(key);
        }

        HashTreeMap.Entry<K, V> subFloor(K key) {
            return this.absFloor(key);
        }

        HashTreeMap.Entry<K, V> subLower(K key) {
            return this.absLower(key);
        }

        final class AscendingEntrySetView extends HashTreeMap.NavigableSubMap<K, V>.EntrySetView {
            AscendingEntrySetView() {
                super();
            }

            public Iterator<java.util.Map.Entry<K, V>> iterator() {
                return AscendingSubMap.this.new SubMapEntryIterator(AscendingSubMap.this.absLowest(), AscendingSubMap.this.absHighFence());
            }
        }
    }

    abstract static class NavigableSubMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Serializable {
        private static final long serialVersionUID = -2102997345730753016L;
        final HashTreeMap<K, V> m;
        final K lo;
        final K hi;
        final boolean fromStart;
        final boolean toEnd;
        final boolean loInclusive;
        final boolean hiInclusive;
        transient NavigableMap<K, V> descendingMapView;
        transient HashTreeMap.NavigableSubMap<K, V>.EntrySetView entrySetView;
        transient HashTreeMap.KeySet<K> navigableKeySetView;

        NavigableSubMap(HashTreeMap<K, V> m, boolean fromStart, K lo, boolean loInclusive, boolean toEnd, K hi, boolean hiInclusive) {
            if (!fromStart && !toEnd) {
                if (m.compare(lo, hi) > 0) {
                    throw new IllegalArgumentException("fromKey > toKey");
                }
            } else {
                if (!fromStart) {
                    m.compare(lo, lo);
                }

                if (!toEnd) {
                    m.compare(hi, hi);
                }
            }

            this.m = m;
            this.fromStart = fromStart;
            this.lo = lo;
            this.loInclusive = loInclusive;
            this.toEnd = toEnd;
            this.hi = hi;
            this.hiInclusive = hiInclusive;
        }

        final boolean tooLow(Object key) {
            if (!this.fromStart) {
                int c = this.m.compare(key, this.lo);
                if (c < 0 || c == 0 && !this.loInclusive) {
                    return true;
                }
            }

            return false;
        }

        final boolean tooHigh(Object key) {
            if (!this.toEnd) {
                int c = this.m.compare(key, this.hi);
                if (c > 0 || c == 0 && !this.hiInclusive) {
                    return true;
                }
            }

            return false;
        }

        final boolean inRange(Object key) {
            return !this.tooLow(key) && !this.tooHigh(key);
        }

        final boolean inClosedRange(Object key) {
            return (this.fromStart || this.m.compare(key, this.lo) >= 0) && (this.toEnd || this.m.compare(this.hi, key) >= 0);
        }

        final boolean inRange(Object key, boolean inclusive) {
            return inclusive ? this.inRange(key) : this.inClosedRange(key);
        }

        final HashTreeMap.Entry<K, V> absLowest() {
            HashTreeMap.Entry<K, V> e = this.fromStart ? this.m.getFirstEntry() : (this.loInclusive ? this.m.getCeilingEntry(this.lo) : this.m.getHigherEntry(this.lo));
            return e != null && !this.tooHigh(e.key) ? e : null;
        }

        final HashTreeMap.Entry<K, V> absHighest() {
            HashTreeMap.Entry<K, V> e = this.toEnd ? this.m.getLastEntry() : (this.hiInclusive ? this.m.getFloorEntry(this.hi) : this.m.getLowerEntry(this.hi));
            return e != null && !this.tooLow(e.key) ? e : null;
        }

        final HashTreeMap.Entry<K, V> absCeiling(K key) {
            if (this.tooLow(key)) {
                return this.absLowest();
            } else {
                HashTreeMap.Entry<K, V> e = this.m.getCeilingEntry(key);
                return e != null && !this.tooHigh(e.key) ? e : null;
            }
        }

        final HashTreeMap.Entry<K, V> absHigher(K key) {
            if (this.tooLow(key)) {
                return this.absLowest();
            } else {
                HashTreeMap.Entry<K, V> e = this.m.getHigherEntry(key);
                return e != null && !this.tooHigh(e.key) ? e : null;
            }
        }

        final HashTreeMap.Entry<K, V> absFloor(K key) {
            if (this.tooHigh(key)) {
                return this.absHighest();
            } else {
                HashTreeMap.Entry<K, V> e = this.m.getFloorEntry(key);
                return e != null && !this.tooLow(e.key) ? e : null;
            }
        }

        final HashTreeMap.Entry<K, V> absLower(K key) {
            if (this.tooHigh(key)) {
                return this.absHighest();
            } else {
                HashTreeMap.Entry<K, V> e = this.m.getLowerEntry(key);
                return e != null && !this.tooLow(e.key) ? e : null;
            }
        }

        final HashTreeMap.Entry<K, V> absHighFence() {
            return this.toEnd ? null : (this.hiInclusive ? this.m.getHigherEntry(this.hi) : this.m.getCeilingEntry(this.hi));
        }

        final HashTreeMap.Entry<K, V> absLowFence() {
            return this.fromStart ? null : (this.loInclusive ? this.m.getLowerEntry(this.lo) : this.m.getFloorEntry(this.lo));
        }

        abstract HashTreeMap.Entry<K, V> subLowest();

        abstract HashTreeMap.Entry<K, V> subHighest();

        abstract HashTreeMap.Entry<K, V> subCeiling(K var1);

        abstract HashTreeMap.Entry<K, V> subHigher(K var1);

        abstract HashTreeMap.Entry<K, V> subFloor(K var1);

        abstract HashTreeMap.Entry<K, V> subLower(K var1);

        abstract Iterator<K> keyIterator();

        abstract Spliterator<K> keySpliterator();

        abstract Iterator<K> descendingKeyIterator();

        public boolean isEmpty() {
            return this.fromStart && this.toEnd ? this.m.isEmpty() : this.entrySet().isEmpty();
        }

        public int size() {
            return this.fromStart && this.toEnd ? this.m.size() : this.entrySet().size();
        }

        public final boolean containsKey(Object key) {
            return this.inRange(key) && this.m.containsKey(key);
        }

        public final V put(K key, V value) {
            if (!this.inRange(key)) {
                throw new IllegalArgumentException("key out of range");
            } else {
                return this.m.put(key, value);
            }
        }

        public final V get(Object key) {
            return !this.inRange(key) ? null : this.m.get(key);
        }

        public final V remove(Object key) {
            return !this.inRange(key) ? null : this.m.remove(key);
        }

        public final java.util.Map.Entry<K, V> ceilingEntry(K key) {
            return HashTreeMap.exportEntry(this.subCeiling(key));
        }

        public final K ceilingKey(K key) {
            return HashTreeMap.keyOrNull(this.subCeiling(key));
        }

        public final java.util.Map.Entry<K, V> higherEntry(K key) {
            return HashTreeMap.exportEntry(this.subHigher(key));
        }

        public final K higherKey(K key) {
            return HashTreeMap.keyOrNull(this.subHigher(key));
        }

        public final java.util.Map.Entry<K, V> floorEntry(K key) {
            return HashTreeMap.exportEntry(this.subFloor(key));
        }

        public final K floorKey(K key) {
            return HashTreeMap.keyOrNull(this.subFloor(key));
        }

        public final java.util.Map.Entry<K, V> lowerEntry(K key) {
            return HashTreeMap.exportEntry(this.subLower(key));
        }

        public final K lowerKey(K key) {
            return HashTreeMap.keyOrNull(this.subLower(key));
        }

        public final K firstKey() {
            return HashTreeMap.key(this.subLowest());
        }

        public final K lastKey() {
            return HashTreeMap.key(this.subHighest());
        }

        public final java.util.Map.Entry<K, V> firstEntry() {
            return HashTreeMap.exportEntry(this.subLowest());
        }

        public final java.util.Map.Entry<K, V> lastEntry() {
            return HashTreeMap.exportEntry(this.subHighest());
        }

        public final java.util.Map.Entry<K, V> pollFirstEntry() {
            HashTreeMap.Entry<K, V> e = this.subLowest();
            java.util.Map.Entry<K, V> result = HashTreeMap.exportEntry(e);
            if (e != null) {
                this.m.deleteEntry(e);
            }

            return result;
        }

        public final java.util.Map.Entry<K, V> pollLastEntry() {
            HashTreeMap.Entry<K, V> e = this.subHighest();
            java.util.Map.Entry<K, V> result = HashTreeMap.exportEntry(e);
            if (e != null) {
                this.m.deleteEntry(e);
            }

            return result;
        }

        public final NavigableSet<K> navigableKeySet() {
            HashTreeMap.KeySet<K> nksv = this.navigableKeySetView;
            return nksv != null ? nksv : (this.navigableKeySetView = new HashTreeMap.KeySet(this));
        }

        public final Set<K> keySet() {
            return this.navigableKeySet();
        }

        public NavigableSet<K> descendingKeySet() {
            return this.descendingMap().navigableKeySet();
        }

        public final SortedMap<K, V> subMap(K fromKey, K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }

        public final SortedMap<K, V> headMap(K toKey) {
            return this.headMap(toKey, false);
        }

        public final SortedMap<K, V> tailMap(K fromKey) {
            return this.tailMap(fromKey, true);
        }

        final class DescendingSubMapKeyIterator extends HashTreeMap.NavigableSubMap<K, V>.SubMapIterator<K> implements Spliterator<K> {
            DescendingSubMapKeyIterator(HashTreeMap.Entry<K, V> last, HashTreeMap.Entry<K, V> fence) {
                super(last, fence);
            }

            public K next() {
                return this.prevEntry().key;
            }

            public void remove() {
                this.removeDescending();
            }

            public Spliterator<K> trySplit() {
                return null;
            }

            public void forEachRemaining(Consumer<? super K> action) {
                while(this.hasNext()) {
                    action.accept(this.next());
                }

            }

            public boolean tryAdvance(Consumer<? super K> action) {
                if (this.hasNext()) {
                    action.accept(this.next());
                    return true;
                } else {
                    return false;
                }
            }

            public long estimateSize() {
                return 9223372036854775807L;
            }

            public int characteristics() {
                return 17;
            }
        }

        final class SubMapKeyIterator extends HashTreeMap.NavigableSubMap<K, V>.SubMapIterator<K> implements Spliterator<K> {
            SubMapKeyIterator(HashTreeMap.Entry<K, V> first, HashTreeMap.Entry<K, V> fence) {
                super(first, fence);
            }

            public K next() {
                return this.nextEntry().key;
            }

            public void remove() {
                this.removeAscending();
            }

            public Spliterator<K> trySplit() {
                return null;
            }

            public void forEachRemaining(Consumer<? super K> action) {
                while(this.hasNext()) {
                    action.accept(this.next());
                }

            }

            public boolean tryAdvance(Consumer<? super K> action) {
                if (this.hasNext()) {
                    action.accept(this.next());
                    return true;
                } else {
                    return false;
                }
            }

            public long estimateSize() {
                return 9223372036854775807L;
            }

            public int characteristics() {
                return 21;
            }

            public final Comparator<? super K> getComparator() {
                return NavigableSubMap.this.comparator();
            }
        }

        final class DescendingSubMapEntryIterator extends HashTreeMap.NavigableSubMap<K, V>.SubMapIterator<java.util.Map.Entry<K, V>> {
            DescendingSubMapEntryIterator(HashTreeMap.Entry<K, V> last, HashTreeMap.Entry<K, V> fence) {
                super(last, fence);
            }

            public java.util.Map.Entry<K, V> next() {
                return this.prevEntry();
            }

            public void remove() {
                this.removeDescending();
            }
        }

        final class SubMapEntryIterator extends HashTreeMap.NavigableSubMap<K, V>.SubMapIterator<java.util.Map.Entry<K, V>> {
            SubMapEntryIterator(HashTreeMap.Entry<K, V> first, HashTreeMap.Entry<K, V> fence) {
                super(first, fence);
            }

            public java.util.Map.Entry<K, V> next() {
                return this.nextEntry();
            }

            public void remove() {
                this.removeAscending();
            }
        }

        abstract class SubMapIterator<T> implements Iterator<T> {
            HashTreeMap.Entry<K, V> lastReturned;
            HashTreeMap.Entry<K, V> next;
            final Object fenceKey;
            int expectedModCount;

            SubMapIterator(HashTreeMap.Entry<K, V> first, HashTreeMap.Entry<K, V> fence) {
                this.expectedModCount = NavigableSubMap.this.m.modCount;
                this.lastReturned = null;
                this.next = first;
                this.fenceKey = fence == null ? HashTreeMap.UNBOUNDED : fence.key;
            }

            public final boolean hasNext() {
                return this.next != null && this.next.key != this.fenceKey;
            }

            final HashTreeMap.Entry<K, V> nextEntry() {
                HashTreeMap.Entry<K, V> e = this.next;
                if (e != null && e.key != this.fenceKey) {
                    if (NavigableSubMap.this.m.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    } else {
                        this.next = HashTreeMap.successor(e);
                        this.lastReturned = e;
                        return e;
                    }
                } else {
                    throw new NoSuchElementException();
                }
            }

            final HashTreeMap.Entry<K, V> prevEntry() {
                HashTreeMap.Entry<K, V> e = this.next;
                if (e != null && e.key != this.fenceKey) {
                    if (NavigableSubMap.this.m.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    } else {
                        this.next = HashTreeMap.predecessor(e);
                        this.lastReturned = e;
                        return e;
                    }
                } else {
                    throw new NoSuchElementException();
                }
            }

            final void removeAscending() {
                if (this.lastReturned == null) {
                    throw new IllegalStateException();
                } else if (NavigableSubMap.this.m.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                } else {
                    if (this.lastReturned.left != null && this.lastReturned.right != null) {
                        this.next = this.lastReturned;
                    }

                    NavigableSubMap.this.m.deleteEntry(this.lastReturned);
                    this.lastReturned = null;
                    this.expectedModCount = NavigableSubMap.this.m.modCount;
                }
            }

            final void removeDescending() {
                if (this.lastReturned == null) {
                    throw new IllegalStateException();
                } else if (NavigableSubMap.this.m.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                } else {
                    NavigableSubMap.this.m.deleteEntry(this.lastReturned);
                    this.lastReturned = null;
                    this.expectedModCount = NavigableSubMap.this.m.modCount;
                }
            }
        }

        abstract class EntrySetView extends AbstractSet<java.util.Map.Entry<K, V>> {
            private transient int size = -1;
            private transient int sizeModCount;

            EntrySetView() {
            }

            public int size() {
                if (NavigableSubMap.this.fromStart && NavigableSubMap.this.toEnd) {
                    return NavigableSubMap.this.m.size();
                } else {
                    if (this.size == -1 || this.sizeModCount != NavigableSubMap.this.m.modCount) {
                        this.sizeModCount = NavigableSubMap.this.m.modCount;
                        this.size = 0;
                        Iterator i = this.iterator();

                        while(i.hasNext()) {
                            ++this.size;
                            i.next();
                        }
                    }

                    return this.size;
                }
            }

            public boolean isEmpty() {
                HashTreeMap.Entry<K, V> n = NavigableSubMap.this.absLowest();
                return n == null || NavigableSubMap.this.tooHigh(n.key);
            }

            public boolean contains(Object o) {
                if (!(o instanceof java.util.Map.Entry)) {
                    return false;
                } else {
                    java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry)o;
                    Object key = entry.getKey();
                    if (!NavigableSubMap.this.inRange(key)) {
                        return false;
                    } else {
                        HashTreeMap.Entry<?, ?> node = NavigableSubMap.this.m.getEntry((K)key);
                        return node != null && HashTreeMap.valEquals(node.getValue(), entry.getValue());
                    }
                }
            }

            public boolean remove(Object o) {
                if (!(o instanceof java.util.Map.Entry)) {
                    return false;
                } else {
                    java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry)o;
                    Object key = entry.getKey();
                    if (!NavigableSubMap.this.inRange(key)) {
                        return false;
                    } else {
                        HashTreeMap.Entry<K, V> node = NavigableSubMap.this.m.getEntry((K)key);
                        if (node != null && HashTreeMap.valEquals(node.getValue(), entry.getValue())) {
                            NavigableSubMap.this.m.deleteEntry(node);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
    }

    final class DescendingKeyIterator extends HashTreeMap<K, V>.PrivateEntryIterator<K> {
        DescendingKeyIterator(HashTreeMap.Entry<K, V> first) {
            super(first);
        }

        public K next() {
            return this.prevEntry().key;
        }

        public void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            } else if (HashTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else {
                HashTreeMap.this.deleteEntry(this.lastReturned);
                this.lastReturned = null;
                this.expectedModCount = HashTreeMap.this.modCount;
            }
        }
    }

    final class KeyIterator extends HashTreeMap<K, V>.PrivateEntryIterator<K> {
        KeyIterator(HashTreeMap.Entry<K, V> first) {
            super(first);
        }

        public K next() {
            return this.nextEntry().key;
        }
    }

    final class ValueIterator extends HashTreeMap<K, V>.PrivateEntryIterator<V> {
        ValueIterator(HashTreeMap.Entry<K, V> first) {
            super(first);
        }

        public V next() {
            return this.nextEntry().value;
        }
    }

    final class EntryIterator extends HashTreeMap<K, V>.PrivateEntryIterator<java.util.Map.Entry<K, V>> {
        EntryIterator(HashTreeMap.Entry<K, V> first) {
            super(first);
        }

        public java.util.Map.Entry<K, V> next() {
            return this.nextEntry();
        }
    }

    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        HashTreeMap.Entry<K, V> next;
        HashTreeMap.Entry<K, V> lastReturned;
        int expectedModCount;

        PrivateEntryIterator(HashTreeMap.Entry<K, V> first) {
            this.expectedModCount = HashTreeMap.this.modCount;
            this.lastReturned = null;
            this.next = first;
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        final HashTreeMap.Entry<K, V> nextEntry() {
            HashTreeMap.Entry<K, V> e = this.next;
            if (e == null) {
                throw new NoSuchElementException();
            } else if (HashTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else {
                this.next = HashTreeMap.successor(e);
                this.lastReturned = e;
                return e;
            }
        }

        final HashTreeMap.Entry<K, V> prevEntry() {
            HashTreeMap.Entry<K, V> e = this.next;
            if (e == null) {
                throw new NoSuchElementException();
            } else if (HashTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else {
                this.next = HashTreeMap.predecessor(e);
                this.lastReturned = e;
                return e;
            }
        }

        public void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            } else if (HashTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else {
                if (this.lastReturned.left != null && this.lastReturned.right != null) {
                    this.next = this.lastReturned;
                }

                HashTreeMap.this.deleteEntry(this.lastReturned);
                this.expectedModCount = HashTreeMap.this.modCount;
                this.lastReturned = null;
            }
        }
    }

    static final class KeySet<E> extends AbstractSet<E> implements NavigableSet<E> {
        private final NavigableMap<E, ?> m;

        KeySet(NavigableMap<E, ?> map) {
            this.m = map;
        }

        public Iterator<E> iterator() {
            return this.m instanceof HashTreeMap ? ((HashTreeMap)this.m).keyIterator() : ((HashTreeMap.NavigableSubMap)this.m).keyIterator();
        }

        public Iterator<E> descendingIterator() {
            return this.m instanceof HashTreeMap ? ((HashTreeMap)this.m).descendingKeyIterator() : ((HashTreeMap.NavigableSubMap)this.m).descendingKeyIterator();
        }

        public int size() {
            return this.m.size();
        }

        public boolean isEmpty() {
            return this.m.isEmpty();
        }

        public boolean contains(Object o) {
            return this.m.containsKey(o);
        }

        public void clear() {
            this.m.clear();
        }

        public E lower(E e) {
            return this.m.lowerKey(e);
        }

        public E floor(E e) {
            return this.m.floorKey(e);
        }

        public E ceiling(E e) {
            return this.m.ceilingKey(e);
        }

        public E higher(E e) {
            return this.m.higherKey(e);
        }

        public E first() {
            return this.m.firstKey();
        }

        public E last() {
            return this.m.lastKey();
        }

        public Comparator<? super E> comparator() {
            return this.m.comparator();
        }

        public E pollFirst() {
            java.util.Map.Entry<E, ?> e = this.m.pollFirstEntry();
            return e == null ? null : e.getKey();
        }

        public E pollLast() {
            java.util.Map.Entry<E, ?> e = this.m.pollLastEntry();
            return e == null ? null : e.getKey();
        }

        public boolean remove(Object o) {
            int oldSize = this.size();
            this.m.remove(o);
            return this.size() != oldSize;
        }

        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
            return new HashTreeMap.KeySet(this.m.subMap(fromElement, fromInclusive, toElement, toInclusive));
        }

        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
            return new HashTreeMap.KeySet(this.m.headMap(toElement, inclusive));
        }

        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
            return new HashTreeMap.KeySet(this.m.tailMap(fromElement, inclusive));
        }

        public SortedSet<E> subSet(E fromElement, E toElement) {
            return this.subSet(fromElement, true, toElement, false);
        }

        public SortedSet<E> headSet(E toElement) {
            return this.headSet(toElement, false);
        }

        public SortedSet<E> tailSet(E fromElement) {
            return this.tailSet(fromElement, true);
        }

        public NavigableSet<E> descendingSet() {
            return new HashTreeMap.KeySet(this.m.descendingMap());
        }

        public Spliterator<E> spliterator() {
            return HashTreeMap.keySpliteratorFor(this.m);
        }
    }

    class EntrySet extends AbstractSet<java.util.Map.Entry<K, V>> {
        EntrySet() {
        }

        public Iterator<java.util.Map.Entry<K, V>> iterator() {
            return HashTreeMap.this.new EntryIterator(HashTreeMap.this.getFirstEntry());
        }

        public boolean contains(Object o) {
            if (!(o instanceof java.util.Map.Entry)) {
                return false;
            } else {
                java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry)o;
                Object value = entry.getValue();
                HashTreeMap.Entry<K, V> p = HashTreeMap.this.getEntry((K) entry.getKey());
                return p != null && HashTreeMap.valEquals(p.getValue(), value);
            }
        }

        public boolean remove(Object o) {
            if (!(o instanceof java.util.Map.Entry)) {
                return false;
            } else {
                java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry)o;
                Object value = entry.getValue();
                HashTreeMap.Entry<K, V> p = HashTreeMap.this.getEntry((K) entry.getKey());
                if (p != null && HashTreeMap.valEquals(p.getValue(), value)) {
                    HashTreeMap.this.deleteEntry(p);
                    return true;
                } else {
                    return false;
                }
            }
        }

        public int size() {
            return HashTreeMap.this.size();
        }

        public void clear() {
            HashTreeMap.this.clear();
        }
    }

    class Values extends AbstractCollection<V> {
        Values() {
        }

        public Iterator<V> iterator() {
            return HashTreeMap.this.new ValueIterator(HashTreeMap.this.getFirstEntry());
        }

        public int size() {
            return HashTreeMap.this.size();
        }

        public boolean contains(Object o) {
            return HashTreeMap.this.containsValue(o);
        }

        public boolean remove(Object o) {
            for(HashTreeMap.Entry<K,V> e = HashTreeMap.this.getFirstEntry(); e != null; e = HashTreeMap.successor(e)) {
                if (HashTreeMap.valEquals(e.getValue(), o)) {
                    HashTreeMap.this.deleteEntry(e);
                    return true;
                }
            }

            return false;
        }

        public void clear() {
            HashTreeMap.this.clear();
        }

    }
}
