package cn.treeh.ToNX.Collection;


import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OrderList<E>{
    private int size = 0;

    public E get(int i) {
        if(i > size - 1)
            return null;
        Node p = root;
        while(--i > 0){
            p = p.next;
        }
        return p.value;
    }

    public void forEach(Consumer<? super E> action) {
        Node p = root;
        while(p != null){
            action.accept(p.value);
            p = p.next;
        }
    }


    public int size() {
        return size;
    }

    public E pop(){
        if(root == null)
            return null;
        Node p = root;
        root = root.next;
        size--;
        return p.value;
    }
    public void remove(E e){
        if(size == 0)
            return;
        if(Objects.equals(e, root.value)){
            size--;
            root = root.next;
        }
        else if(size == 1){
            return;
        }
        Node p = root;
        while(p.next != null){
            if(Objects.equals(p.next.value, e)){
                p.next = p.next.next;
                size--;
                return;
            }
            p = p.next;
        }
    }
    public void removeIf(Predicate<? super E> filter) {
        if(size == 0)
            return;
        while(filter.test(root.value)){
            size--;
            root = root.next;
        }
        Node p = root;
        while (p.next != null) {
            if (filter.test(p.next.value)) {
                p.next = p.next.next;
                size--;
            } else {
                p = p.next;
            }
        }
    }
    public void add(E v){
        Node nn = new Node();
        nn.value = v;
        size++;
        if(root == null){
            root = nn;
            return;
        }
        Node p = root;
        if(cpr.compare(v, p.value) < 0){
            nn.next = root;
            root = nn;
            return;
        }
        if(root.next == null){
            root.next = nn;
            return;
        }
        while(p.next != null && cpr.compare(p.next.value, v) <= 0){
            p = p.next;
        }
        nn.next = p.next;
        p.next = nn;
    }
    class Node{
        E value;
        Node next;
    }
    private Comparator<E> cpr;
    Node root;
    public OrderList(Comparator<E> c){
        cpr = c;
    }
}
