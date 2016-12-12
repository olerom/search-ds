package ru.mail.polis;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Nechaev Mikhail
 * Since 13/12/16.
 */
public class AVLTree<E extends Comparable<E>> implements ISortedSet<E> {

//    private Node root;
    private int size;
    private final Comparator<E> comparator;

    public AVLTree() {
        this.comparator = null;
    }

    public AVLTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public E first() {
        return null;
    }

    @Override
    public E last() {
        return null;
    }

    @Override
    public List<E> inorderTraverse() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(E el) {
        return false;
    }

    @Override
    public boolean add(E el) {
        return false;
    }

    @Override
    public boolean remove(E el) {
        return false;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }
}
