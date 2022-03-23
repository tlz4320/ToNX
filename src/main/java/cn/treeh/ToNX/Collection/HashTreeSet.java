package cn.treeh.ToNX.Collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class HashTreeSet<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, Serializable {
    private transient NavigableMap<E, Object> m;
    private static final Object PRESENT = new Object();
    private static final long serialVersionUID = -2479143000061671589L;

    HashTreeSet(NavigableMap<E, Object> m) {
        this.m = m;
    }

    public HashTreeSet() {
        this((NavigableMap)(new HashTreeMap()));
    }

    public HashTreeSet(Comparator<? super E> comparator) {
        this((NavigableMap)(new HashTreeMap(comparator)));
    }

    public HashTreeSet(Collection<? extends E> c) {
        this();
        this.addAll(c);
    }

    public HashTreeSet(SortedSet<E> s) {
        this(s.comparator());
        this.addAll(s);
    }

    public Iterator<E> iterator() {
        return this.m.navigableKeySet().iterator();
    }

    public Iterator<E> descendingIterator() {
        return this.m.descendingKeySet().iterator();
    }

    public NavigableSet<E> descendingSet() {
        return new HashTreeSet(this.m.descendingMap());
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

    public boolean add(E e) {
        return this.m.put(e, PRESENT) == null;
    }

    public boolean remove(Object o) {
        return this.m.remove(o) == PRESENT;
    }

    public void clear() {
        this.m.clear();
    }

    public boolean addAll(Collection<? extends E> c) {
        return super.addAll(c);
    }

    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return new HashTreeSet(this.m.subMap(fromElement, fromInclusive, toElement, toInclusive));
    }

    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return new HashTreeSet(this.m.headMap(toElement, inclusive));
    }

    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return new HashTreeSet(this.m.tailMap(fromElement, inclusive));
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

    public Comparator<? super E> comparator() {
        return this.m.comparator();
    }

    public E first() {
        return this.m.firstKey();
    }

    public E last() {
        return this.m.lastKey();
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

    public E pollFirst() {
        Map.Entry<E, ?> e = this.m.pollFirstEntry();
        return e == null ? null : e.getKey();
    }

    public E pollLast() {
        Map.Entry<E, ?> e = this.m.pollLastEntry();
        return e == null ? null : e.getKey();
    }

    public Object clone() {
        HashTreeSet clone;
        try {
            clone = (HashTreeSet)super.clone();
        } catch (CloneNotSupportedException var3) {
            throw new InternalError(var3);
        }

        clone.m = new HashTreeMap(this.m);
        return clone;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(this.m.comparator());
        s.writeInt(this.m.size());
        Iterator var2 = this.m.keySet().iterator();

        while(var2.hasNext()) {
            E e = (E)var2.next();
            s.writeObject(e);
        }

    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        Comparator<? super E> c = (Comparator)s.readObject();
        HashTreeMap<E, Object> tm = new HashTreeMap(c);
        this.m = tm;
        int size = s.readInt();
        tm.readTreeSet(size, s, PRESENT);
    }

    public Spliterator<E> spliterator() {
        return HashTreeMap.keySpliteratorFor(this.m);
    }
}

