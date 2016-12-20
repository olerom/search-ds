package ru.mail.polis;

import java.util.*;

import static ru.mail.polis.RedBlackTree.Color.BLACK;
import static ru.mail.polis.RedBlackTree.Color.RED;

//TODO: write code here
public class RedBlackTree<E extends Comparable<E>> implements ISortedSet<E> {

    enum Color {BLACK, RED}

    class RBNode {
        Color color;
        E key;
        RBNode right;
        RBNode left;
        RBNode parent;

        RBNode(E k) {
            this();
            key = k;
        }

        RBNode() {
            color = BLACK;
            left = null;
            right = null;
            parent = null;
        }
    }

    private int size;
    private final Comparator<E> comparator;
    private final RBNode nil = new RBNode();
    private RBNode root = nil;

    public RedBlackTree() {
        root.left = nil;
        root.right = nil;
        root.parent = nil;
        this.comparator = null;
    }

    public RedBlackTree(Comparator<E> comparator) {
        root.left = nil;
        root.right = nil;
        root.parent = nil;
        this.comparator = comparator;
    }

    @Override
    public E first() {
        if (root == nil) {
            throw new NoSuchElementException("RedBlackTree is empty, no first element");
        } else {
            RBNode min = root;
            while (min.left != nil) {
                min = min.left;
            }
            return min.key;
        }
    }

    @Override
    public E last() {
        if (root == nil) {
            throw new NoSuchElementException("RedBlackTree is empty, no last element");
        } else {
            RBNode max = root;
            while (max.right != nil) {
                max = max.right;
            }
            return max.key;
        }
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> list = new ArrayList<>();
        list = getList(root, list);
        return list;
    }

    private List<E> getList(RBNode curr, List<E> list) {
        if (curr == nil) return list;
        getList(curr.left, list);
        list.add(curr.key);
        getList(curr.right, list);
        return list;
    }

    @Override
    public int size() {
        if (size < 0) {
            return Integer.MAX_VALUE;
        } else {
            return size;
        }
    }

    @Override
    public boolean isEmpty() {
        return root == nil;
    }

    @Override
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException("Input value is null");
        } else {
            return checkContains(root, value);
        }
    }

    private boolean checkContains(RBNode node, E value) {
        if (node == null || node.key == null) {
            return false;
        } else if (compare(node.key, value) == 0) {
            return true;
        } else if (compare(node.key, value) == 1) {
            return checkContains(node.left, value);
        } else if (compare(node.key, value) == -1) {
            return checkContains(node.right, value);
        }
        return false;
    }

    @Override
    public boolean add(E value) {
        if (value == null){
            throw new NullPointerException("Input value is null");
        }
        if (contains(value)) {
            return false;
        }
        RBNode a = new RBNode(value);
        rbInsert(a);
        size++;
        return true;
    }

    @Override
    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException("Input value is null");
        }
        if (!contains(value)) {
            return false;
        }

        removeRB(root, value);

        size--;
        return true;
    }

    private void removeRB(RBNode p, E q) {
        if (compare(p.key, q) == 1) {
            // Ищем у левого ребенка.
            removeRB(p.left, q);
        } else if (compare(p.key, q) == -1) {
            // Ну и у правого.
            removeRB(p.right, q);
        } else if (compare(p.key, q) == 0) {
            // Нашли ноду, удаляем.
            rbDelete(p);
        }
    }

    private void rbDelete(RBNode z) {
        RBNode y;
        if (z.left == nil || z.right == nil) {
            y = z;
        } else {
//            y = treeSuccessor(z);
            // В Кормене successor
            y = treePreDeccessor(z);
        }
        RBNode x;
        if (y.left != nil) {
            x = y.left;
        } else {
            x = y.right;
        }

        x.parent = y.parent;

        if (y.parent == nil) {
            root = x;
        } else {
            if (y == y.parent.left) {
                y.parent.left = x;
            } else {
                y.parent.right = x;
            }
        }

        if (y != z) {
            z.key = y.key;
            z.color = y.color;
        }

        if (y.color == BLACK) {
            rbDeleteFixup(x);
        }
    }

    private void rbDeleteFixup(RBNode x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                RBNode w = x.parent.right;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        w.left.color = BLACK;
                        w.color = RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                RBNode w = x.parent.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }

        }
        x.color = BLACK;
    }


    private RBNode treeSuccessor(RBNode x) {
        if (x.right != nil) {
            RBNode q = x.right;
            while (q.left != nil) {
                q = q.left;
            }
            return q;
        }
        RBNode y = x.parent;
        while (y != nil && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    private RBNode treePreDeccessor(RBNode x) {
        if (x.left != nil) {
            RBNode q = x.left;
            while (q.right != nil) {
                q = q.right;
            }
            return q;
        }
        RBNode y = x.parent;
        while (y != nil && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    public void rbInsert(RBNode node) {
        RBNode y = nil;
        RBNode x = root;

        while (x != nil) {
            y = x;
            if (compare(node.key, x.key) == -1) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;

        if (y == nil) {
            root = node;
        } else {
            if (compare(node.key, y.key) == -1) {
                y.left = node;
            } else {
                y.right = node;
            }
        }

        node.left = nil;
        node.right = nil;
        node.color = RED;

        rbInsertFixup(node);
    }

    private void rbInsertFixup(RBNode z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                RBNode y = z.parent.parent.right;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;

                    rightRotate(z.parent.parent);
                }

            } else {
                RBNode y = z.parent.parent.left;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;

                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void leftRotate(RBNode a) {
        // Просто делаем малый левый поворот как на вики итмо.
        RBNode b = a.right;
        b.parent = a.parent;

        a.right = b.left;

        if (a.right != nil) {
            a.right.parent = a;
        }

        b.left = a;
        a.parent = b;

        // Если есть узел выше, надо чтобы он указывал на поменянную ноду.
        if (b.parent != nil) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else if (b.parent.left == a) {
                b.parent.left = b;
            }
        }

        // Отличие от Кормена. Лучше спросить Михаила, если не забуду
        if (b.parent == nil) {
            root = b;
        }

    }

    private void rightRotate(RBNode b) {
        // Тоже просто делаем малый правый поворот как на вики итмо.
        RBNode a = b.left;
        a.parent = b.parent;

        b.left = a.right;

        if (b.left != nil) {
            b.left.parent = b;
        }

        a.right = b;
        b.parent = a;

        // Если есть узел выше, надо чтобы он указывал на поменянную ноду.
        if (a.parent != nil) {
            if (a.parent.right == b) {
                a.parent.right = a;
            } else if (a.parent.left == b) {
                a.parent.left = a;
            }
        }

        // Отличие от Кормена. Лучше спросить Михаила, если не забуду
        if (b.parent == nil) {
            root = b;
        }

    }

    //**********
    // Уже не симметр, но пох - для дебага сойдет
    public void simmetr() {
        printLined();
    }

    private void printLined() {
        if (root == nil) return;
        Queue<RBNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            RBNode curr = queue.poll();
            if (curr.left != nil) {
                queue.add(curr.left);
            }
            if (curr.right != nil) {
                queue.add(curr.right);
            }
            System.out.printf(curr.key + " ");
        }
        System.out.println();
    }
}
